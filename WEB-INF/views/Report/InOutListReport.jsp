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
$(document).ready(function(){
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
	$("#StartDay").datepicker();
	$("#EndDay").datepicker();
	$("#StartDay2").datepicker();
	$("#EndDay2").datepicker();
});
function search()
{
	var form = document.inOutListForm;
	form.action = "InOutListReport_Search.do";
	form.submit();
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
function fn_paging(PAGE)
{
	document.getElementById("PAGE").value = PAGE;
	var form = document.inOutListForm;
	form.action = "InOutListReport_Search.do";
	form.submit();
}
function getMLIST(obj){
	var POP_LOGC = '';
	var TagName = $('#POP_LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		POP_LOGC = $("#POP_LOGC option:selected").val();
	}
	else{
		POP_LOGC = $("#POP_LOGC").val();
	}
	console.log("TagName = " + TagName );
	console.log("POP_LOGC = " + POP_LOGC );
	$("#SCODEList").empty();
	var option = "<option value=''>선택</option>";
    $('#SCODEList').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_MLIST.do",
		data: "LOGC="+POP_LOGC+"&LCODE="+obj.value,
		success:function(html){
			$("#td_M").empty();
			$("#td_M").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});
}
function getSLIST(obj){
	var POP_LOGC = '';
	var TagName = $('#POP_LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		POP_LOGC = $("#POP_LOGC option:selected").val();
	}
	else{
		POP_LOGC = $("#POP_LOGC").val();
	}
	var LCODE = document.getElementById("LCODEList").value;
	$.ajax({
		type:"GET",
		url:"Ajax_SLIST.do",
		data: "LOGC="+POP_LOGC+"&LCODE="+LCODE+"&MCODE="+obj.value,
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
	var POP_LOGC = '';
	var TagName = $('#POP_LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		POP_LOGC = $("#POP_LOGC option:selected").val();
	}
	else{
		POP_LOGC = $("#POP_LOGC").val();
	}
	var POP_MNAME = $("#POP_MNAME").val();
	var MCODENAME = $("#MCODENAME").val();
	var SCODENAME = $("#SCODENAME").val();
	$.ajax({
		type:"POST",
		url:"Ajax_Order_search.do",
		data: "LCODEList="+LCODEList+"&MCODEList="+MCODEList+"&SCODEList="+SCODEList+"&LOGC="+POP_LOGC+"&MNAME="+POP_MNAME+"&MCODENAME="+MCODENAME+"&SCODENAME="+SCODENAME+"&PAGE=" + PAGE,
		success:function(html){
			$("#pop_table").empty();
			$("#pop_table").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function pop_Model(){
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
}
function AddModel(MCODE, LGROUP, MGROUP, SGROUP){
	$("#MNAME").val(MCODE);
	$('#dim-layer').fadeOut();
}
function pop_EVENT(){
	var $el = $("#layer_Event");
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

    $el.find("#BTN_POP_CLOSE2").click(function(){    	
        isDim ? $('#dim-layer2').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });
}
function Ajax_Event_search(PAGE){
	var BONBU = '';
	if ($('#POP_BONBU').length) {
		var index = $("#POP_BONBU option").index($("#POP_BONBU option:selected"));
		if(index > 0)
			BONBU = $("#POP_BONBU option:selected").text();
	}
	var CENTER = '';
	if ($('#POP_CENTER').length) {
		var index = $("#POP_CENTER option").index($("#POP_CENTER option:selected"));
		if(index > 0)
			CENTER = $("#POP_CENTER option:selected").text();
	}
	var EUSERNAME = '';
	if ($('#EUSERNAME').length) {
		EUSERNAME = $("#EUSERNAME").val();
	}
	var EUSERID = '';
	if ($('#EUSERID').length) {
		EUSERID = $("#EUSERID").val();
	}
	var StartDay = $("#StartDay2").val();
	var EndDay = $("#EndDay2").val();
	var GNAME = $("#GNAME").val();
	var JNAME = $("#JNAME").val();
	var ESTATE = $("#ESTATE option:selected").val();
	var GTYPE = $("#GTYPE option:selected").val();
	$.ajax({
		type:"POST",
		url:"Ajax_Event_search.do",
		data: "StartDay="+StartDay+"&EndDay="+EndDay+"&BONBU="+BONBU+"&CENTER="+CENTER+"&EUSERID="+EUSERID+"&EUSERNAME="+EUSERNAME+"&GNAME="+GNAME+"&JNAME="+JNAME+"&ESTATE="+ESTATE+"&GTYPE="+GTYPE+"&PAGE=" + PAGE,
		success:function(html){
			$("#pop_table2").empty();
			$("#pop_table2").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function POP_getBONBU(obj){
	$("#POP_CENTER").empty();
	var option = "<option value=''>선택</option>";
    $('#POP_CENTER').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_POP_BONBU.do",
		data: "LOGC="+obj.value,
		success:function(html){
			$("#td_POP_B").empty();
			$("#td_POP_B").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function POP_getCENTER(obj){
	var POP_LOGC = document.getElementById("POP_LOGC2").value;
	$.ajax({
		type:"GET",
		url:"Ajax_POP_CENTER.do",
		data: "LOGC="+POP_LOGC+"&BONBU="+obj.value,
		success:function(html){
			$("#td_POP_C").empty();
			$("#td_POP_C").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function AddEvent(DOCNO){
	$("#EVENT").val(DOCNO);
	$('#dim-layer2').fadeOut();
}
function downexcel(){
	var form = document.inOutListForm;
	form.action = "InOutListReport_Excel.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="inOutListForm" method="post" action="InOutListReport_Search.do">
		<input type="hidden" name="menuGroup" value="menu_stats" id="menuGroup">
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

			<div class="subtitle" style="text-align: right;">관리 > 입출고 내역 조회</div>
			<h2>입출고 내역 조회</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 50%;">
					<c:choose>
					<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
					<tr>
						<td style="text-align: center;">물류센터</td>
						<td>
							<select style="width: 90%;" name="LOGC" id="LOGC"  onchange="getBONBU(this)">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
								<option value="${LOGCLIST.MCI_LOGC}" <c:if test="${LOGC == LOGCLIST.MCI_LOGC}">selected="selected"</c:if>>${LOGCLIST.MCI_LOGCNAME}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">본 부</td>
						<td id = 'td_B'>
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
						<td style="text-align: center;">센 터</td>
						<td id = 'td_C'>
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
					<c:when test="${UserInfo.MUT_POSITION == '003'}">
					<tr>
						<td style="text-align: center;">물류센터</td>
						<td>
							${UserInfo.MCI_LOGCNAME}
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">본 부</td>
						<td>
							${UserInfo.MCI_BonbuNAME}
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">센 터</td>
						<td id = 'td_C'>
							<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
								<option value="${CENTERLIST.MCI_Center}" <c:if test="${CENTER == CENTERLIST.MCI_Center}">selected="selected" </c:if>>${CENTERLIST.MCI_CenterName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					</c:when>
					<c:otherwise>
					<tr>
						<td style="text-align: center;">본부</td>
						<td>
							${UserInfo.MCI_BonbuNAME}&nbsp;${UserInfo.MCI_CenterName}
						</td>
					</tr>
					</c:otherwise>
					</c:choose>
					<tr>
						<td style="text-align: center;">구 분</td>
						<td>
							<input type="radio" name = "IOTYPE" value = "0" <c:if test="${IOTYPE == 0 or empty IOTYPE}">checked="checked"</c:if>> 전체
							<input type="radio" name = "IOTYPE" value = "1" <c:if test="${IOTYPE == 1}">checked="checked"</c:if>> 입 고
							<input type="radio" name = "IOTYPE" value = "2" <c:if test="${IOTYPE == 2}">checked="checked"</c:if>> 출 고
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">일 자</td>
						<td>
							<input type="text" name="StartDay" id="StartDay" value="${StartDay}"  style="width:40%;" readonly="readonly">
							&nbsp;~&nbsp;
							<input type="text" name="EndDay" id="EndDay" value="${EndDay}"  style="width:40%;" readonly="readonly">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">행사번호</td>
						<td>
							<input type="text" name="EVENT" id="EVENT" value="${EVENT}"  style="width:50%;">
							<a href="javascript:pop_EVENT();" class="search grey button" >행사번호</a>	
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
						<td colspan="2" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<div style="text-align:right;">
				<a href="javascript:downexcel()" class="search grey button">엑셀 다운로드</a>
			</div>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 8%;">입출고 구분</th>
							<th style="width: 8%">구 분</th>
							<th style="width: 9%">접수번호</th>
							<th style="width: 9%">날짜</th>
							<th style="width: 9%">물류센터</th>
							<th style="width: 8%">본 부</th>
							<th style="width: 9%">센 터</th>
							<th style="width: 9%">상품코드</th>
							<th style="width: 7%">대분류</th>
							<th style="width: 9%">중분류</th>
							<th style="width: 9%">소분류</th>
							<th style="width: 6%">수 량</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${INOUTLISTSize > 0 }">
					<c:forEach var="INOUTLIST" items="${INOUTLIST}" varStatus="st">
						<tr style="height: 40px;">
							<td>
							<c:if test="${INOUTLIST.IOTYPE == '1'}">
								입고
							</c:if>
							<c:if test="${INOUTLIST.IOTYPE == '2'}">
								출고
							</c:if>
							</td>
							<td>${INOUTLIST.RESNNAME}</td>
							<td>${INOUTLIST.DOCNO}</td> 	
							<td>${INOUTLIST.IODATE}</td>
							<td>${INOUTLIST.LOGCNAME}</td>
							<td>${INOUTLIST.BONBUNAME}</td>
							<td>${INOUTLIST.CENTERNAME}</td>
							<td>${INOUTLIST.MODEL}</td>
							<td>${INOUTLIST.MMC_LGROUPNAME}</td> 	 
							<td>${INOUTLIST.MMC_MGROUP}</td> 	 	 
							<td>${INOUTLIST.MMC_SGROUP}</td>
							<td>${INOUTLIST.QTY}</td>	 		 	
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
				<div id="layer_model" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<tr>
								<td style="text-align: center;">물류센터</td>
								<td colspan="2">
									<c:choose>
									<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
									<select style="width: 90%;" name="POP_LOGC" id="POP_LOGC" >
										<option value="">선택</option>
										<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
										</c:forEach>
									</select>
									</c:when>
									<c:otherwise>
										${UserInfo.MCI_LOGCNAME}
										<input type="hidden" name="POP_LOGC" value="${UserInfo.MCI_LOGC}" id="POP_LOGC">
									</c:otherwise>
									</c:choose>
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
					<div id="pop_table"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
						
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종 료</a>
					</div>
    			</div>
			</div>
			<div id="dim-layer2" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_Event" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;overflow:auto;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<c:choose>
							<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
							<tr>
								<td style="width: 30%;text-align: center;">물류센터</td>
								<td style="width: 70%;text-align: left;">
									<select style="width: 90%;" name="POP_LOGC2" id="POP_LOGC2"  onchange="POP_getBONBU(this)">
									<option value="">선택</option>
									<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:forEach>
									</select>
								</td>
							</tr>
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">본 부</td>
								<td style="width: 70%;text-align: left;" id = 'td_POP_B'>
									<select style="width: 90%;" name="POP_BONBU" id = "POP_BONBU" onchange="POP_getCENTER(this)">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">센 터</td>
								<td style="width: 70%;text-align: left;"  id = 'td_POP_C'>
									<select style="width: 90%;" name="POP_CENTER" id = "POP_CENTER">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							</c:when>
							<c:when test="${UserInfo.MUT_POSITION == '003'}">
							<tr>
								<td style="width: 30%;text-align: center;">물류센터</td>
								<td style="width: 70%;text-align: left;">
									${UserInfo.MCI_LOGCNAME}
								</td>
							</tr>
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">본 부</td>
								<td style="width: 70%;text-align: left;">
									${UserInfo.MCI_BonbuNAME}
								</td>
							</tr>
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">센 터</td>
								<td style="width: 70%;text-align: left;">
									<select style="width: 90%;" name="POP_CENTER" id = "POP_CENTER">
										<option value="">선택</option>
										<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
										<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							</c:when>
							<c:otherwise>
							<tr>
								<td style="width: 30%;text-align: center;">본 부</td>
								<td style="width: 70%;text-align: left;">
									${UserInfo.MCI_BonbuNAME}&nbsp;${UserInfo.MCI_CenterName}
								</td>
							</tr>
							</c:otherwise>
							</c:choose>
							<tr>
								<td style="width: 30%;text-align: center;">의전팀장</td>
								<td style="width: 70%;text-align: left;">
									<c:if test="${UserInfo.MUT_POSITION != '005'}">
									<input type="text" name="EUSERNAME" id="EUSERNAME" value="" style="width:90%;">
									</c:if>
									<c:if test="${UserInfo.MUT_POSITION == '005'}">
									${UserInfo.MUT_USERNAME}
									<input type="hidden" name="EUSERID" id="EUSERID" value="${UserInfo.MUT_USERID}">
									</c:if>
								</td>
							</tr>
							<tr>
								<td style="width: 30%;text-align: center;">일 자</td>
								<td style="width: 70%;text-align: left;">
									<input type="text" name="StartDay2" id="StartDay2" value="${StartDay2}"  style="width:40%;" readonly="readonly">
									&nbsp;~&nbsp;
									<input type="text" name="EndDay2" id="EndDay2" value=""  style="width:40%;" readonly="readonly">
								</td>
							</tr>
							<tr>
								<td style="width: 30%;text-align: center;">단체명</td>
								<td style="width: 70%;text-align: left;">
									<input type="text" name="GNAME" id="GNAME" value=""  style="width:90%;">
								</td>
							</tr>
							<tr>
								<td style="width: 30%;text-align: center;">장례식장</td>
								<td style="width: 70%;text-align: left;">
									<input type="text" name="JNAME" id="JNAME" value=""  style="width:90%;">
								</td>
							</tr>
							<tr>
								<td style="width: 30%;text-align: center;">행사상태</td>
								<td style="width: 70%;text-align: left;">
									<select style="width: 90%;" name="ESTATE" id="ESTATE" >
										<option value="">선택</option>
										<option value="접수">접수</option>
										<option value="접수취소">접수취소</option>
										<option value="취소">취소</option>
										<option value="예약">예약</option>
										<option value="진행">진행</option>
										<option value="완료">완료</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width: 30%;text-align: center;">일반단체구분</td>
								<td style="width: 70%;text-align: left;">
									<select style="width: 90%;" name="GTYPE" id="GTYPE">
									<option value="">선택</option>
									<c:forEach var="GTYPELIST" items="${GTYPELIST}" varStatus="st">
										<option value="${GTYPELIST.GTYPE}">${GTYPELIST.GTYPE}</option>
									</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: center;">
									<a href="javascript:Ajax_Event_search(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table2"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
						
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE2" class="search grey button">종 료</a>
					</div>
    			</div>
			</div>
		</div>
	</form:form>
</body>
</html>