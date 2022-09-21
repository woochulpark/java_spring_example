<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script src="js/jquery-ui.js" language="javascript"></script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
<script language="javascript">
$.datepicker.regional['ko']= {	 
	prevText:'이전달',
	nextText:'다음달',
	currentText:'오늘',
	yearRange: "2020:2100",
	monthNames:['년 1월','년 2월','년 3월','년 4월','년 5월','년 6월','년 7월','년 8월','년 9월','년 10월','년 11월','년 12월'],
	monthNamesShort:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	dayNames:['일','월','화','수','목','금','토'],
	dayNamesShort:['일','월','화','수','목','금','토'],
	dayNamesMin:['일','월','화','수','목','금','토'],
	weekHeader:'Wk',
	dateFormat:'yy-mm-dd',
	firstDay:0,
	isRTL:false,
	showMonthAfterYear:true,
	changeMonth: true,
	changeYear: true,
	yearSuffix:'',
	closeText:'취소',
	showButtonPanel: true,
	onClose: function () {
		if ($(window.event.srcElement).hasClass('ui-datepicker-close')) {
			$(this).val('');
		}
	}
};

$.datepicker.setDefaults($.datepicker.regional['ko']); 

//jquery ready
$(function() {
	$("#ORDERDATE").datepicker();
});
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function save()
{
	var MCODE = $('#MCODE').val();
	var len = $('#Table_MLIST tbody tr').length;
	if(len == 0 || MCODE.length == 0){
		document.getElementById('MSG').innerText = "상품코드를 조회하여 추가해주세요.";
		alert("상품코드를 조회하여 추가해주세요."); 
		return;
	}
	
	var ORDERDATE = $("#ORDERDATE").val();
	if(ORDERDATE.length == 0){
		document.getElementById('MSG').innerText = "발주일을 입력해주세요.";
		alert("발주일을 입력해주세요."); 
		return;
	}
	
	var OrderQty = $("#OrderQty").val();
	if(OrderQty.length == 0 || OrderQty == '0'){
		document.getElementById('MSG').innerText = "발주요청수량을 입력해주세요.";
		alert("발주요청수량을 입력해주세요."); 
		return;
	}
	
	var form = document.orderForm;
	form.submit();
}
function cancel() {
	var form = document.orderForm;
	form.action = "OrderList.do";
	form.submit();
}
function getMLIST(obj){
	var LOGC = $("#ULOGC").val();
	$("#SCODEList").empty();
	var option = "<option value=''>선택</option>";
    $('#SCODEList').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_MLIST.do",
		data: "LOGC="+LOGC+"&LCODE="+obj.value,
		success:function(html){
			$("#td_M").empty();
			$("#td_M").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getSLIST(obj){
	var LOGC = $("#ULOGC").val();
	var LCODE = document.getElementById("LCODEList").value;
	$.ajax({
		type:"GET",
		url:"Ajax_SLIST.do",
		data: "LOGC="+LOGC+"&LCODE="+LCODE+"&MCODE="+obj.value,
		success:function(html){
			$("#td_S").empty();
			$("#td_S").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function Ajax_Order_search(PAGE){
	var LCODEList = $("#LCODEList option:selected").val();
	var MCODEList = $("#MCODEList option:selected").val();
	var SCODEList = $("#SCODEList option:selected").val();
	var LOGC = $("#ULOGC").val();
	var MNAME = $("#MNAME").val();
	var MCODENAME = $("#MCODENAME").val();
	var SCODENAME = $("#SCODENAME").val();
	$.ajax({
		type:"POST",
		url:"Ajax_Order_search.do",
		data: "LCODEList="+LCODEList+"&MCODEList="+MCODEList+"&SCODEList="+SCODEList+"&LOGC="+LOGC+"&MNAME="+MNAME+"&MCODENAME="+MCODENAME+"&SCODENAME="+SCODENAME+"&PAGE=" + PAGE,
		success:function(html){
			$("#pop_table").empty();
			$("#pop_table").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function pop_Model(){
	var LOGC = $("#ULOGC").val();
	
	var $el = $("#layer_model");
    var isDim = $el.prev().hasClass('dimBg');//dimmed 레이어를 감지하기 위한 boolean 변수

    isDim ? $('#dim-layer').fadeIn() : $el.fadeIn();

    var $elWidth = ~~($el.outerWidth()),
        $elHeight = ~~($el.outerHeight()),
        docWidth = $(document).width(),
        docHeight = $(document).height();

    // 화면의 중앙에 레이어를 띄운다.
    if ($elHeight < docHeight || $elWidth < docWidth) {
        $el.css({
            marginTop: -$elHeight /2,
            marginLeft: -$elWidth/2
        })
    } else {
        $el.css({top: 0, left: 0});
    }
    
    $el.find("#BTN_POP_CLOSE").click(function(){    	
        isDim ? $('#dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });

    $('.layer .dimBg').click(function(){
        $('#dim-layer').fadeOut();
        return false;
    });
}
function AddModel(MCODE, LGROUP, MGROUP, SGROUP){
	$('#mList').empty();
	
	$("#MCODE").val(MCODE);
	
	var html = "";
    html += "<tr style='height: 40px;'>";
    html += "<td>" + MCODE + "</td>";
    html += "<td>" + LGROUP + "</td>";
    html += "<td>" + MGROUP + "</td>";
    html += "<td>" + SGROUP + "</td>";
    html += "<td><input type='number' name='OrderQty' id='OrderQty' style='width:90%;' onKeyup=\"this.value=this.value.replace(/[^0-9]/g,'');\" min='0'/></td>";
    html += "</tr>";
    $('#mList').append(html);
    $('#dim-layer').fadeOut();
}
function getBONBU(obj){
	$("#CENTER").empty();
	var option = "<option value=''>선택</option>";
    $('#CENTER').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_BONBU.do",
		data: "LOGC="+obj.value,
		success:function(html){
			$("#td_B").empty();
			$("#td_B").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getCENTER(obj){
	var LOGC = document.getElementById("LOGC").value;
	$.ajax({
		type:"GET",
		url:"Ajax_CENTER.do",
		data: "LOGC="+LOGC+"&BONBU="+obj.value,
		success:function(html){
			$("#td_C").empty();
			$("#td_C").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="orderForm" method="post" action="Order_Add_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="ULOGC" value="${UserInfo.MCI_LOGC}" id="ULOGC">
		<input type="hidden" name="UESRID" value="${UserInfo.MUT_USERID}" id="UESRID">
		<input type="hidden" name="MCODE" value="" id="MCODE">
		<!-- START TOPBAR -->
		<!-- menu start -->
		<jsp:include page="/menu_top.do">
			<jsp:param name="user_id" value="${user_id}" />
		</jsp:include>
		<!-- menu end -->
		<!-- END TOPBAR -->

		<!-- START WRAPPER -->
		<div class="wrapper group">
			<!-- START HEADER -->
			<!-- menu start -->
			<jsp:include page="/menu_list.do">
				<jsp:param name="user_id" value="${user_id}" />
			</jsp:include>
			<!-- menu end -->
			<!-- END HEADER -->

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부발주 > 본부발주 등록</div>
			<h2>본부발주 등록</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<c:choose>
						<c:when test="${UserInfo.MUT_POSITION == '001'}">
						<tr>
							<th style="text-align: center;width: 20%;">물류센터</th>
							<td style="width: 26%;text-align: left;">
								<select style="width: 90%;" name="LOGC" id="LOGC"  onchange="getBONBU(this)">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
								<option value="${LOGCLIST.MCI_LOGC}" <c:if test="${LOGC == LOGCLIST.MCI_LOGC}">selected="selected"</c:if>>${LOGCLIST.MCI_LOGCNAME}</option>
								</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<th style="text-align: center;width: 20%;">본 부</th>
							<td id = 'td_B' style="width: 27%;text-align: left;">
								<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
								<option value="">선택</option>
								<c:if test="${not empty BONBU and BONBU != ''}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
								<option value="${BONBULIST.MCI_Bonbu}" <c:if test="${BONBU == BONBULIST.MCI_Bonbu}">selected="selected" </c:if>>${BONBULIST.MCI_BonbuNAME}</option>
								</c:forEach>
								</c:if>
								</select>
							</td>
						</tr>
						<tr>
							<th style="text-align: center;width: 20%;">센 터</th>
							<td id = 'td_C' style="width: 27%;text-align: left;">
								<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:if test="${not empty CENTER and CENTER != ''}">
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
								<option value="${CENTERLIST.MCI_Center}" <c:if test="${CENTER == CENTERLIST.MCI_Center}">selected="selected" </c:if>>${CENTERLIST.MCI_CenterName}</option>
								</c:forEach>
								</c:if>
								</select>
							</td>
						</tr>
						</c:when>
						<c:otherwise>
						<tr style="height: 40px">
							<th style="width: 30%;">발주본부</th>
							<td style="width: 70%;text-align: left;">
								${UserInfo.MCI_BonbuNAME}&nbsp;${UserInfo.MCI_CenterName}
							</td>
						</tr>
						</c:otherwise>
						</c:choose>
						<tr style="height: 40px">
							<th style="width: 30%;">발주자</th>
							<td style="width: 70%;text-align: left;">
								${UserInfo.MUT_USERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">발주일</th>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='ORDERDATE' id='ORDERDATE' style='width:90%;' readonly='readonly' />
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">비 고</th>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='CMNT' id='CMNT' style='width:90%;' />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
				<a href="javascript:pop_Model();" class="search grey button" >코드조회</a>	
			</div>
			<div class="scroll_table" id="Table_MLIST">
				<table>
					<thead>
						<tr>
							<th style="width: 20%;">상품코드</th>
							<th style="width: 20%;">대분류</th>
							<th style="width: 20%">중분류</th>
							<th style="width: 20%">소분류</th>
							<th style="width: 20%">발주요청수량</th>
						</tr>
					</thead>
					<tbody  id='mList'>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<a href="javascript:save();" class="search grey button">발주하기</a>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
			<div id="dim-layer" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_model" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<tr>
								<td style="text-align: center;">물류센터</td>
								<td id="id_popLogc" colspan="2">
									${UserInfo.MCI_LOGCNAME}
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">대분류</td>
								<td colspan="2">
									<select style="width: 90%;" name="LCODEList" id="LCODEList"  onchange="getMLIST(this)">
										<option value="">선택</option>
										<c:forEach var="LCODELIST" items="${LCODELIST}" varStatus="st">
											<option value="${LCODELIST.MCC_S_CODE}">${LCODELIST.MCC_S_NAME}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="text-align: center;width: 30%;">중분류</td>
								<td id = 'td_M' style="width: 35%;">
									<select style="width: 90%;" name="MCODEList" id = "MCODEList" onchange="getSLIST(this)">
										<option value="">선택</option>
									</select>
								</td>
								<td style="width: 35%;">
									<input type="text" name="MCODENAME" id="MCODENAME" style="width:90%;">
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">소분류</td>
								<td id = 'td_S'>
									<select style="width: 90%;" name="SCODEList" id = "SCODEList">
										<option value="">선택</option>
									</select>
								</td>
								<td>
									<input type="text" name="SCODENAME" id="SCODENAME" style="width:90%;">
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">상품코드명</td>
								<td colspan="2">
									<input type="text" name="MNAME" id="MNAME" value=""  style="width:90%;">
								</td>
							</tr>
							<tr>
								<td colspan="3" style="text-align: center;">
									<a href="javascript:Ajax_Order_search(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종 료</a>
					</div>
    			</div>
			</div>
		</div>
	</form:form>
</body>
</html>