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
});
function search()
{
	var form = document.formatForm;
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
	var form = document.formatForm;
	form.submit();
}
function JegoDetail(DOCNO)
{
	document.getElementById("DOCNO").value = DOCNO;
	var form = document.formatForm;
	form.action = "Jegoformat_DETAIL.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="formatForm" method="post" action="Jegoformat_List_Search.do">
		<input type="hidden" name="menuGroup" value="menu_customer" id="menuGroup">
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

			<div class="subtitle" style="text-align: right;">서식자료 > 재고명세서</div>
			<h2>재고명세서</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 80%;">
					<tr>
						<td style="text-align: center;width: 25%;">일 자</td>
						<td colspan="3" style="width: 75%;">
							<input type="text" name="StartDay" id="StartDay" value="${StartDay}"  style="width:40%;" readonly="readonly">
							&nbsp;~&nbsp;
							<input type="text" name="EndDay" id="EndDay" value="${EndDay}"  style="width:40%;" readonly="readonly">
						</td>
					</tr>
					<c:choose>
					<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
					<tr>
						<td style="width: 25%;text-align: center;">본 부</td>
						<td style="width: 25%">
							<select style="width: 90%;" name="LOGC" id="LOGC"  onchange="getBONBU(this)">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<c:if test="${LOGC == LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}" selected="selected">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
									<c:if test="${LOGC != LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
						<td  id = 'td_B' style="width: 25%">
							<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
								<option value="">선택</option>
								<c:if test="${not empty BONBULIST}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
									<c:if test="${BONBU == BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}" selected="selected">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
									<c:if test="${BONBU != BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td id = 'td_C' style="width: 25%">
							<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:if test="${not empty CENTERLIST}">
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
									<c:if test="${CENTER == CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}" selected="selected">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
									<c:if test="${CENTER != CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					</c:when>
					<c:when test="${UserInfo.MUT_POSITION == '003'}">
					<tr>
						<td style="width: 25%;text-align: center;">본 부</td>
						<td colspan="3">
							${UserInfo.MCI_LOGCNAME}&nbsp;${UserInfo.MCI_BonbuNAME}&nbsp;
							<select style="width: 50%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
								<option value="${CENTERLIST.MCI_Center}" <c:if test="${CENTER == CENTERLIST.MCI_Center}">selected="selected"</c:if>>${CENTERLIST.MCI_CenterName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					</c:when>
					<c:otherwise>
					<tr>
						<td style="width: 25%;text-align: center;">본 부</td>
						<td colspan="3">
							${UserInfo.MCI_LOGCNAME}&nbsp;${UserInfo.MCI_BonbuNAME}&nbsp;${UserInfo.MCI_CenterName}
						</td>
					</tr>
					</c:otherwise>
					</c:choose>
					<tr>
						<td style="text-align: center;width: 25%;">승인여부</td>
						<td colspan="3" style="width: 75%;">
							<input type="radio" name = "STSTYPE" value = "" <c:if test="${empty STSTYPE or STSTYPE == ''}">checked="checked"</c:if>> 전체
							<input type="radio" name = "STSTYPE" value = "0" <c:if test="${STSTYPE == '0'}">checked="checked"</c:if>> 미승인
							<input type="radio" name = "STSTYPE" value = "1" <c:if test="${STSTYPE == '1'}">checked="checked"</c:if>> 승인
							<input type="radio" name = "STSTYPE" value = "2" <c:if test="${STSTYPE == '2'}">checked="checked"</c:if>> 반려
						</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 16%;">발급일자</th>
							<th style="width: 16%;">내역서 번호</th>
							<th style="width: 16%;">조회기간</th>
							<th style="width: 16%;">발행본부</th>
							<th style="width: 16%;">확 인</th>
							<th style="width: 20%">특이사항</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${JegoFormatListSize > 0 }">
					<c:forEach var="JegoFormatList" items="${JegoFormatList}" varStatus="st">
						<tr style="height: 40px;"  onclick="javascript:JegoDetail('${JegoFormatList.DDJ_DOCNO}')">
							<td style="padding: 2px;">${JegoFormatList.DDJ_DATE}</td>
							<td style="padding: 2px;">${JegoFormatList.DDJ_DOCNO}</td>
							<td style="padding: 2px;">${JegoFormatList.DDJ_INFODATE}</td>
							<td style="padding: 2px;">${JegoFormatList.JEGOCENTER}</td>
							<td style="padding: 2px;">
								<c:choose>
									<c:when test="${JegoFormatList.DDJ_STS == '0'}">
									미승인
									</c:when>
									<c:when test="${JegoFormatList.DDJ_STS == '1'}">
									승인
									</c:when>
									<c:otherwise>
									반려
									</c:otherwise>
								</c:choose>
							</td>
							<td style="padding: 2px;">${JegoFormatList.DDJ_CMNT}</td>
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
		</div>
	</form:form>
</body>
</html>