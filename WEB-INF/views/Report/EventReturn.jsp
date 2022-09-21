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
	// set datepicker
	$("#StartDay").datepicker();
	$("#EndDay").datepicker();
	$("#StartDay2").datepicker();
	$("#EndDay2").datepicker();
});
function allCheck(){
	if($("#ALLCHK").is(":checked")){
        $(".ECHECK").prop("checked", true);
    }
    else{
        $(".ECHECK").prop("checked", false);
    }
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
	var form = document.statusForm;
	form.submit();
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
	$("#ENAME").val(DOCNO);
	$('#dim-layer2').fadeOut();
}
function search()
{
	var form = document.statusForm;
	form.submit();
}
function ERETURN(ETYPE){
	if($('input:checkbox[name=ECHECK]:checked').length == 0)
		return;
	$('#ETYPE').val(ETYPE);
	var $el = $("#layer_CMNT");
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
    	var CMNT = $('#CMNT').val();
    	var ETYPE = $('#ETYPE').val();
    	var MSG = '';
    	if(ETYPE == '3')
    		MSG = '분실처리 하시겠습니까?';
   		else
    	   	MSG = '분실취소 하시겠습니까?';
    	var confirm_Save = confirm(MSG);
    	if ( confirm_Save == true ) {
        	var form = document.statusForm;
        	form.action = "EventReturn_Update.do";
        	form.submit();
        }
        return false;
    });
    $el.find("#BTN_POP_CLOSE").click(function(){    	
        isDim ? $('#dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="statusForm" method="post" action="EventReturn_Search.do">
		<input type="hidden" name="menuGroup" value="menu_stats" id="menuGroup">
		<input type="hidden" name="PAGE" value="1" id="PAGE">
		<input type="hidden" name="ETYPE" value="" id="ETYPE">
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

			<div class="subtitle" style="text-align: right;">관리 > 비품 설치 및 회수 현황</div>
			<h2>비품 설치 및 회수 현황</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 700px;">
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
						<td style="text-align: center;">입고일자</td>
						<td>
							<input type="text" name="StartDay" id="StartDay" value="${StartDay}"  style="width:40%;" readonly="readonly">
							&nbsp;~&nbsp;
							<input type="text" name="EndDay" id="EndDay" value="${EndDay}"  style="width:40%;" readonly="readonly">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">행사정보</td>
						<td>
							<input type="text" name="ENAME" id="ENAME" value="${ENAME}"  style="width:50%;">
							<a href="javascript:pop_EVENT();" class="search grey button" >행사정보 조회</a>	
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">구 분</td>
						<td>
							<input type="radio" name="BISSTS" value=""  <c:if test="${empty BISSTS}">checked="checked"</c:if>> 전체
							<input type="radio" name="BISSTS" value="1" <c:if test="${BISSTS == '1'}">checked="checked"</c:if>> 설치중
							<input type="radio" name="BISSTS" value="2" <c:if test="${BISSTS == '2'}">checked="checked"</c:if>> 회수
							<input type="radio" name="BISSTS" value="5" <c:if test="${BISSTS == '5'}">checked="checked"</c:if>> 미회수
							<input type="radio" name="BISSTS" value="3" <c:if test="${BISSTS == '3'}">checked="checked"</c:if>> 분실
							<input type="radio" name="BISSTS" value="4" <c:if test="${BISSTS == '4'}">checked="checked"</c:if>> 분실취소
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
				<a href="javascript:ERETURN('3')" class="search grey button">분실</a>
				<a href="javascript:ERETURN('4')" class="search grey button">분실취소</a>
			</div>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 5%;"><input type="checkbox" name="ALLCHK" id="ALLCHK" style="width:90%;" onClick="javascript:allCheck();"/></th>
							<th style="width: 7%;">행사번호</th>
							<th style="width: 7%;">본 부</th>
							<th style="width: 6%">센 터</th>
							<th style="width: 7%">상품코드</th>
							<th style="width: 7%">대분류</th>
							<th style="width: 7%">중분류</th>
							<th style="width: 7%">소분류</th>
							<th style="width: 5%">수량</th>
							<th style="width: 7%">설치일</th>
							<th style="width: 6%">설치자</th>
							<th style="width: 7%">회수일</th>
							<th style="width: 7%">회수자</th>
							<th style="width: 7%">상 태</th>
							<th style="width: 8%">비고</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${EeventReturnSize > 0 }">
					<c:forEach var="EeventReturnList" items="${EeventReturnList}" varStatus="st">
						<tr style="height: 40px;">
							<td><input type="checkbox" name="ECHECK" class="ECHECK" value="${EeventReturnList.DEM_DOCNO}^${EeventReturnList.DEM_CENTER}^${EeventReturnList.DEM_MODEL}" style="width:90%;"/></td>
							<td>${EeventReturnList.DEM_DOCNO}</td>
							<td>${EeventReturnList.MCI_BonbuNAME}</td>
							<td>${EeventReturnList.MCI_CenterName}</td>	 		 	
							<td>${EeventReturnList.DEM_MODEL}</td>	 
							<td>${EeventReturnList.MMC_LGROUPNAME}</td>	 
							<td>${EeventReturnList.MMC_MGROUP}</td>	 
							<td>${EeventReturnList.MMC_SGROUP}</td>	 
							<td>${EeventReturnList.DEM_QTY}</td>	 
							<td>${EeventReturnList.DEM_DATE}</td>	 
							<td>${EeventReturnList.DEM_USERNAME}</td>
							<td>
							<c:choose>
							<c:when test="${EeventReturnList.DEM_STS == 1 or EeventReturnList.DEM_STS == 3 or EeventReturnList.DEM_STS == 5}">
								${EeventReturnList.DEM_RETEMPDATE}(예정)
							</c:when>
							<c:otherwise>
								${EeventReturnList.DEM_REDATE}
							</c:otherwise>
							</c:choose>
							</td>	
							<td>${EeventReturnList.DEM_REUESRNAME}</td>	
							<td>
								<c:choose>
								<c:when test="${EeventReturnList.DEM_STS == '1'}">
									설치중
								</c:when>
								<c:when test="${EeventReturnList.DEM_STS == '2'}">
									회수
								</c:when>
								<c:when test="${EeventReturnList.DEM_STS == '3'}">
									<span style="color:red">분실</span> 
								</c:when>
								<c:when test="${EeventReturnList.DEM_STS == '4'}">
									회수<span style="color:red">(분실취소)</span> 
								</c:when>
								<c:otherwise>
									<span style="color:red">미회수</span> 
								</c:otherwise>
								</c:choose>
							</td>
							<td>${EeventReturnList.DEM_CMNT}</td>		 	 
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
				<div id="layer_CMNT" class="pop-layer" style="padding: 20px 25px;width: 300px;height: 100px;">
        			<div class="scroll_table" id="pop_table" style="height: 70%;overflow:auto;">
						<input type="text" name="CMNT" id="CMNT" style="width:95%;">
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CHECK" class="search grey button">확인</a>
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종료</a>
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