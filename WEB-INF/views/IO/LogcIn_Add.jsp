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
var MCODELIST = new Array();
var POPLOGC = '';
var POPLOGCNAME = '';
$.datepicker.regional['ko']= {
	prevText:'이전달',
	nextText:'다음달',
	currentText:'오늘',
	yearRange: "1900:2100",
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
	$("input[name=JEJO]").datepicker();
	$("input[name=INDATE]").datepicker();
});
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function save()
{
	if(MCODELIST.length == 0)
	{
		document.getElementById('MSG').innerText = "상품코드를 추가해주세요.";
		alert("상품코드를 추가해주세요."); 
		return;
	}
	
	var saveChk = true;
    $("input[name=JEJO]").each(function(idx){   
    	var JEJODATE = $(this).val();
    	console.log('JEJODATE = ' + JEJODATE);
    	if(JEJODATE.length == 0)
    	{
    		saveChk = false;
    		return false;
    	}
    });
    if(!saveChk){
    	document.getElementById('MSG').innerText = "제조일자를 모두 입력해주세요.";
		alert("제조일자를 모두 입력해주세요."); 
		return;
    }
    $("input[name=INDATE]").each(function(idx){   
    	var INDATE = $(this).val();
    	console.log('INDATE = ' + INDATE);
    	if(INDATE.length == 0)
    	{
    		saveChk = false;
    		return false;
    	}
    });
    if(!saveChk){
    	document.getElementById('MSG').innerText = "입고일자를 모두 입력해주세요.";
		alert("입고일자를 모두 입력해주세요."); 
		return;
    }
    $("input[name=INPRICE]").each(function(idx){   
    	var INPRICE = $(this).val();
    	if(INPRICE.length == 0)
    	{
    		$(this).val('0');
    	}
    });
    if(!saveChk){
    	document.getElementById('MSG').innerText = "입고단가를 모두 입력해주세요.";
		alert("입고단가를 모두 입력해주세요."); 
		return;
    }
    $("input[name=INCNT]").each(function(idx){   
    	var INCNT = $(this).val();
    	console.log('INCNT = ' + INCNT);
    	if(INCNT.length == 0 || INCNT == 0)
    	{
    		saveChk = false;
    		return false;
    	}
    });
    if(!saveChk){
    	document.getElementById('MSG').innerText = "입고수량을 모두 입력해주세요.";
		alert("입고수량을 모두 입력해주세요."); 
		return;
    }
    
    $("input[name=INPRICE]").each(function(idx){   
    	var INPRICE = $(this).val();
    	$(this).val(INPRICE.replace(/,/g,''));
    });
	var form = document.LogcInForm;
	form.submit();
}
function Mdelete() {
	var del_chk = confirm('상품코드를 삭제하시겠습니까?');
	if(del_chk==true){
    	var checkbox = $("input[name=MODEL_DEL]:checked");
    	for(var i=checkbox.length-1; i>-1; i--){
    	    var tr = checkbox.parent().parent().eq(i);  // checkbox 태그의 두 단계 상위 태그가 tr이기 때문에
    	    var td = tr.children();  // td 태그는 tr 태그의 하위에 있으므로
    	    var MCODE = td.eq(1).text();
    	    console.log('MCODE = ' + MCODE);
    	    MCODELIST.splice($.inArray(MCODE, MCODELIST),1);
    	    tr.remove();
    	}
	}
}
function cancel() {
	var form = document.LogcInForm;
	form.action = "LogcInList.do";
	form.submit();
}
function getMLIST(obj){
	$("#SCODEList").empty();
	var option = "<option value=''>선택</option>";
    $('#SCODEList').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_MLIST.do",
		data: "LOGC="+POPLOGC+"&LCODE="+obj.value,
		success:function(html){
			$("#td_M").empty();
			$("#td_M").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});
}
function getSLIST(obj){
	var LCODE = document.getElementById("LCODEList").value;
	$.ajax({
		type:"GET",
		url:"Ajax_SLIST.do",
		data: "LOGC="+POPLOGC+"&LCODE="+LCODE+"&MCODE="+obj.value,
		success:function(html){
			$("#td_S").empty();
			$("#td_S").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function Ajax_LogcIn_search(PAGE){
	var LCODEList = $("#LCODEList option:selected").val();
	var MCODEList = $("#MCODEList option:selected").val();
	var SCODEList = $("#SCODEList option:selected").val();
	var MNAME = $("#MNAME").val();
	var MCODENAME = $("#MCODENAME").val();
	var SCODENAME = $("#SCODENAME").val();
	$.ajax({
		type:"GET",
		url:"Ajax_LogcIn_search.do",
		data: "LCODEList="+LCODEList+"&MCODEList="+MCODEList+"&SCODEList="+SCODEList+"&LOGC="+POPLOGC+"&MNAME="+MNAME+"&MCODENAME="+MCODENAME+"&SCODENAME="+SCODENAME+"&PAGE=" + PAGE,
		success:function(html){
			$("#pop_table").empty();
			$("#pop_table").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function pop_Model(){
	var LOGC = $("#LOGC option:selected").val();
	var LOGCTEXT = $("#LOGC option:selected").text();
	if(LOGC.length == 0){
		document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
		alert("물류센터를 선택해주세요."); 
		return;
	}
	POPLOGC = LOGC;
	POPLOGCNAME = LOGCTEXT;
	$("#id_popLogc").text(POPLOGCNAME);
	
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

    $el.find("#BTN_POP_CHECK").click(function(){
    	var checkbox = $("input[name=pop_Add]:checked");
    	checkbox.each(function (i) {
    	    var tr = checkbox.parent().parent().eq(i);  // checkbox 태그의 두 단계 상위 태그가 tr이기 때문에
    	    var td = tr.children();  // td 태그는 tr 태그의 하위에 있으므로
    	 
    	    var MCODE = td.eq(2).text();
    	    var LGROUP = td.eq(3).text();
    	    var MGROUP = td.eq(4).text();
    	    var SGROUP = td.eq(5).text();
    	    
    	    if($.inArray(MCODE, MCODELIST) != -1){
    	    	return true
    	    }
    	    var NOWDATE = $("#NOWDATE").val();
    	    var html = "";
    	    html += "<tr style='height: 40px;' id='tr_" + MCODE + "'>";
    	    html += "<td><input type='checkbox' name='MODEL_DEL' class='MODEL_DEL' style='width:90%;'/><input type='hidden' name='MCODE' value='" + MCODE + "' ></td>";
    	    html += "<td>" + MCODE + "</td>";
    	    html += "<td>" + LGROUP + "</td>";
    	    html += "<td>" + MGROUP + "</td>";
    	    html += "<td>" + SGROUP + "</td>";
    	    html += "<td><input type='text' name='JEJO' style='width:90%;' value='" + NOWDATE + "' readonly='readonly' /></td>";
    	    html += "<td><input type='text' name='INDATE' style='width:90%;' value='" + NOWDATE + "' readonly='readonly' /></td>";
    	    html += "<td><input type='text' name='INPRICE' style='width:90%;' onkeyUp=\"this.value=SetComma(this.value)\" onfocus=\"this.value=SetComma(this.value)\" onchange=\"TotaolPrice('tr_" + MCODE + "')\" min='0'/></td>";
    	    html += "<td><input type='number' name='INCNT' style='width:90%;' onKeyup=\"this.value=this.value.replace(/[^0-9]/g,'');\" onchange=\"TotaolPrice('tr_" + MCODE + "')\" min='0'/></td>";
    	    html += "<td></td>";
    	    html += "</tr>";
    	    $('#mList').append(html);
    	    MCODELIST.push(MCODE);
    	    $(document).find("input[name=JEJO]").datepicker();     
    	    $(document).find("input[name=INDATE]").datepicker();   
    	});
        isDim ? $('#dim-layer').fadeOut() : $el.fadeOut();
        return false;
    });
    
    $el.find("#BTN_POP_CLOSE").click(function(){    	
        isDim ? $('#dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });

    $('.layer .dimBg').click(function(){
        $('#dim-layer').fadeOut();
        return false;
    });
}
function delPop(){
	if($("#pop_Add_ALL").is(":checked")){
		$("input[name=pop_Add]").prop("checked", true);
    }
    else{
    	$("input[name=pop_Add]").prop("checked", false);
    }
}
function delallCheck(){
	if($("#DEL_ALL").is(":checked")){
		$("input[name=MODEL_DEL]").prop("checked", true);
    }
    else{
    	$("input[name=MODEL_DEL]").prop("checked", false);
    }
}
function TotaolPrice(id){
	var tr = $("#" + id); //tr값
	var price = tr.find('input[name=INPRICE]').val().replace(/,/g,''); 
	var cnt = tr.find('input[name=INCNT]').val();
	var td = tr.children();  // td 태그는 tr 태그의 하위에 있으므로
	if(price.length > 0 && cnt.length > 0){
		td.eq(9).text(numberWithCommas(price * cnt));
	}
	
}
function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
function ModelCheck(){
	POPLOGC = '';
	POPLOGCNAME = '';
	$("#id_popLogc").text('');
	$("#pop_table").empty();
	
	$("#LCODEList option:eq(0)").prop("selected", true);
	var option = "<option value=''>선택</option>";
	$("#MCODEList").empty();
    $('#MCODEList').append(option);
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
    $("#MNAME").val('');
    
	var len = $('#Table_MLIST tbody tr').length;
	if(len > 0){
		$("#Table_MLIST").find('tbody').empty();
		MCODELIST.length = 0;
	}
}
function SetComma(str) { 
	str=str.replace(/,/g,''); 
	console.log(str)
	var retValue = ""; 
	if(isNaN(str)==false)
	{ 
		retValue = numberWithCommas(str);
	}
	return retValue; 
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="LogcInForm" method="post" action="LogcIn_Add_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="NOWDATE" value="${NOWDATE}" id="NOWDATE">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 물류센터 입고 > 물류센터 입고 등록</div>
			<h2>물류센터 입고 등록</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
							<select style="width: 90%;" name="LOGC" id = "LOGC" onchange='ModelCheck()'>
								<option value="">선택</option>
								<c:if test="${not empty LOGCLIST}">
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<option value="${LOGCLIST.MCI_LOGC}" <c:if test="${LOGCLIST.MCI_LOGC == LOGC}">selected="selected"</c:if>>${LOGCLIST.MCI_LOGCNAME}</option>
								</c:forEach>
								</c:if>
							</select>
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
							<th style="width: 5%;"><input type="checkbox" name="DEL_ALL" id="DEL_ALL" style="width:90%;" onClick="javascript:delallCheck();"/></th>
							<th style="width: 10%;">상품코드</th>
							<th style="width: 10%;">대분류</th>
							<th style="width: 10%">중분류</th>
							<th style="width: 10%">소분류</th>
							<th style="width: 10%">제조일자</th>
							<th style="width: 10%">입고일자</th>
							<th style="width: 10%">입고단가</th>
							<th style="width: 10%">입고수량</th>
							<th style="width: 10%">총금액</th>
						</tr>
					</thead>
					<tbody  id='mList'>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<a href="javascript:Mdelete();" class="search grey button">삭제</a>
				<a href="javascript:save();" class="search grey button">입고</a>
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
									<a href="javascript:Ajax_LogcIn_search(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CHECK" class="search grey button">확 인</a>
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종 료</a>
					</div>
    			</div>
			</div>
		</div>
	</form:form>
</body>
</html>