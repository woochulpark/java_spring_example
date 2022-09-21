<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
$(document).ready(function(){
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
});
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
	// set datepicker
	$("#StartDay").datepicker();
	$("#EndDay").datepicker();
});
function search()
{
	var form = document.LogcInForm;
	form.submit();
}
function LogcIn_Add()
{
	var form = document.LogcInForm;
	form.action = "LogcIn_Add.do";
	form.submit();
}
function LogcIn_DETAIL(DOCNO)
{
	document.getElementById("DOCNO").value = DOCNO;
	var form = document.LogcInForm;
	form.action = "LogcIn_DETAIL.do";
	form.submit();
}
function fn_paging(PAGE)
{
	document.getElementById("PAGE").value = PAGE;
	var form = document.LogcInForm;
	form.submit();
}
function BONBU_DETAIL(DOCNO){
	var $el = $("#layer_BONBUCHK");
    var isDim = $el.prev().hasClass('dimBg');
    $("#pop_table").empty();
	$.ajax({
		type:"POST",
		url:"Ajax_BonbuCheck.do",
		data: "DOCNO="+DOCNO,
		success:function(html){
			$("#pop_table").append(html);
			
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
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	

    $el.find("#BTN_POP_CLOSE").click(function(){
        isDim ? $('#dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });
}

function getMLIST(obj){
	var LOGC = $("#POPLOGC option:selected").val();
	if(LOGC.length == 0){
		alert("물류센터를 선택해주세요."); 
		return;
	}
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
	var LOGC = $("#POPLOGC option:selected").val();
	if(LOGC.length == 0){
		alert("물류센터를 선택해주세요."); 
		return;
	}
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
	var LOGC = $("#POPLOGC option:selected").val();
	if(LOGC.length == 0){
		alert("물류센터를 선택해주세요."); 
		return;
	}
	var MNAME = $("#POP_MNAME").val();
	var MCODENAME = $("#MCODENAME").val();
	var SCODENAME = $("#SCODENAME").val();
	$.ajax({
		type:"POST",
		url:"Ajax_Order_search.do",
		data: "LCODEList="+LCODEList+"&MCODEList="+MCODEList+"&SCODEList="+SCODEList+"&LOGC="+LOGC+"&MNAME="+MNAME+"&MCODENAME="+MCODENAME+"&SCODENAME="+SCODENAME+"&PAGE=" + PAGE,
		success:function(html){
			$("#pop_table2").empty();
			$("#pop_table2").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function pop_Model(){
	var LOGC = $("#POPLOGC option:selected").val();
	var LOGCTEXT = $("#POPLOGC option:selected").text();
	
	var $el = $("#layer_model");
    var isDim = $el.prev().hasClass('dimBg');//dimmed 레이어를 감지하기 위한 boolean 변수

    isDim ? $('#dim-layer2').fadeIn() : $el.fadeIn();

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
        isDim ? $('#dim-layer2').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });
}
function AddModel(MCODE, LGROUP, MGROUP, SGROUP){
	$("#MNAME").val(MCODE);
    $('#dim-layer2').fadeOut();
}
function getPOPChange(){
	$("#LCODEList option:eq(0)").prop("selected", true);
	$("#MCODEList").empty();
	var option = "<option value=''>선택</option>";
    $('#MCODEList').append(option);
	$("#SCODEList").empty();
	var option = "<option value=''>선택</option>";
    $('#SCODEList').append(option);
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="LogcInForm" method="post" action="LogcInList_search.do">
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="DOCNO" value="" id="DOCNO">
		<input type="hidden" name="PAGE" value="1" id="PAGE">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 물류센터 입고</div>
			<h2>물류센터 입고</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 700px;">
					<tr>
						<td style="text-align: center;">물류센터</td>
						<td>
							<select style="width: 90%;" name="LOGC" id = "LOGC">
								<option value="">선택</option>
								<c:if test="${not empty LOGCLIST}">
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<c:if test="${LOGC == LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}" selected="selected">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
									<c:if test="${LOGC != LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">입고일자</td>
						<td>
							<input type="text" name="StartDay" id="StartDay" value="${StartDay}"  style="width:40%;" readonly="readonly">
							&nbsp;~&nbsp;
							<input type="text" name="EndDay" id="EndDay" value="${EndDay}"  style="width:40%;" readonly="readonly">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">상품코드</td>
						<td>
							<input type="text" name="MNAME" id="MNAME" value="${MNAME}"  style="width:50%;">
							<a href="javascript:pop_Model();" class="search grey button" >코드조회</a>	
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">물류상태</td>
						<td>
							<input type="radio" name="BISSTS" value=""  <c:if test="${empty BISSTS}">checked="checked"</c:if>> 전체
							<input type="radio" name="BISSTS" value="0" <c:if test="${BISSTS == '0'}">checked="checked"</c:if>> 입고
							<input type="radio" name="BISSTS" value="1" <c:if test="${BISSTS == '1'}">checked="checked"</c:if>> 출고중
							<input type="radio" name="BISSTS" value="2" <c:if test="${BISSTS == '2'}">checked="checked"</c:if>> 출고완료
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<c:if test="${UserInfo.MUT_POSITION == '001'}">
			<div style="text-align:right;">
				<a href="javascript:LogcIn_Add()" class="search grey button">물류센터 입고</a>
			</div>
			</c:if>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 9%;">접수번호</th>
							<th style="width: 9%;">상품코드</th>
							<th style="width: 10%;">대분류</th>
							<th style="width: 10%">중분류</th>
							<th style="width: 10%">소분류</th>
							<th style="width: 9%">제조일자</th>
							<th style="width: 9%">입고일자</th>
							<th style="width: 9%">입고단가</th>
							<th style="width: 8%">입고수량</th>
							<th style="width: 9%">총금액</th>
							<th style="width: 8%">물류상태</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${LogcInListSize > 0 }">
					<c:forEach var="LogcInList" items="${LogcInList}" varStatus="st">
						<tr style="height: 40px;">
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.DIL_DOCNO}</td>
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.DIL_MODEL}</td>
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.MMC_LGROUPNAME}</td>
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.MMC_MGROUP}</td> 	
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.MMC_SGROUP}</td>
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.DIL_JEJO}</td> 	 
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.DIL_INDate}</td> 	 	 
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')"><fmt:formatNumber value="${LogcInList.DIL_INPRICE}" pattern="#,###" /></td> 	
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')">${LogcInList.DIL_QTY}</td>
							<td onclick="javascript:LogcIn_DETAIL('${LogcInList.DIL_DOCNO}')"><fmt:formatNumber value="${LogcInList.INTOTAL}" pattern="#,###" /></td>
							<td <c:if test="${LogcInList.bizSTS == '1' or LogcInList.bizSTS == '2'}">onclick="javascript:BONBU_DETAIL('${LogcInList.DIL_DOCNO}')"</c:if>>
								<c:if test="${LogcInList.bizSTS == '0'}">
									입고
								</c:if>
								<c:if test="${LogcInList.bizSTS == '1'}">
									출고중
								</c:if>
								<c:if test="${LogcInList.bizSTS == '2'}">
									출고완료
								</c:if>
							</td> 	  	 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
            <c:if test="${pagination.curRange ne 1 }">
            <a href="#" onClick="fn_paging(1)">[처음]</a> 
            </c:if>
            <c:if test="${pagination.curPage ne 1}">
            <a href="#" onClick="fn_paging('${pagination.prevPage }')">[이전]</a> 
            </c:if>
            <c:forEach var="pageNum" begin="${pagination.startPage }" end="${pagination.endPage }">
            <c:choose>
            <c:when test="${pageNum eq  pagination.curPage}">
            <span style="font-weight: bold;"><a href="#" onClick="fn_paging('${pageNum }')">${pageNum }</a></span> 
            </c:when>
            <c:otherwise>
            <a href="#" onClick="fn_paging('${pageNum }')">${pageNum }</a> 
            </c:otherwise>
            </c:choose>
            </c:forEach>
            <c:if test="${pagination.curPage ne pagination.pageCnt && pagination.pageCnt > 0}">
            <a href="#" onClick="fn_paging('${pagination.nextPage }')">[다음]</a> 
            </c:if>
            <c:if test="${pagination.curRange ne pagination.rangeCnt && pagination.rangeCnt > 0}">
            <a href="#" onClick="fn_paging('${pagination.pageCnt }')">[끝]</a> 
            </c:if>
            </div>
            <div id="dim-layer" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_BONBUCHK" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 600px;">
        			<div class="scroll_table" id="pop_table" style="height: 95%;overflow:auto;">
						
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종료</a>
					</div>
    			</div>
			</div>
			<div id="dim-layer2" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_model" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<tr>
								<td style="text-align: center;">물류센터</td>
								<td colspan="2">
									<select style="width: 90%;" name="POPLOGC" id="POPLOGC"  onchange="getPOPChange()">
										<option value="" selected="selected" >선택</option>
										<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
										</c:forEach>
									</select>
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
									<input type="text" name="POP_MNAME" id="POP_MNAME" value=""  style="width:90%;">
								</td>
							</tr>
							<tr>
								<td colspan="3" style="text-align: center;">
									<a href="javascript:Ajax_Order_search(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table2"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
						
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