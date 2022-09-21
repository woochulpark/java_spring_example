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
	var PMSG = '${msg}';
	if(PMSG.length > 0){
		alert(PMSG);
	}
});
function search()
{
	var form = document.statusForm;
	form.action = "JegoMagamReport_Search.do";
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
function downexcel(){
	var form = document.statusForm;
	form.action = "JegoMagamReport_Excel.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="statusForm" method="post" action="JegoMagamReport_Search.do">
		<input type="hidden" name="menuGroup" value="menu_stats" id="menuGroup">
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

			<div class="subtitle" style="text-align: right;">관리 > 재고 마감 현황</div>
			<h2>재고 마감 현황</h2>

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
						<td style="text-align: center;">일 자</td>
						<td>
							<input type="number" name="YEAR" id="YEAR" value="${YEAR}" style="width:45%;"  onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="2020" max="2100">
							<select style="width: 45%;" name="MONTH" id = "MONTH">
							<c:forEach var="i" begin="1" end="12">
    						<option value="${i}" <c:if test="${MONTH == i}">selected="selected" </c:if>>${i}월</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">대분류</td>
						<td>
							<select style="width: 90%;" name="LGROUP" id="LGROUP">
								<option value="">선택</option>
								<c:forEach var="LCODELIST" items="${LCODELIST}" varStatus="st">
									<option value="${LCODELIST.MCC_S_CODE}" <c:if test="${LGROUP == LCODELIST.MCC_S_CODE}">selected="selected" </c:if>>${LCODELIST.MCC_S_NAME}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">구 분</td>
						<td>
							<select style="width: 90%;" name="KINDS" id="KINDS">
								<option value="">선택</option>
								<c:forEach var="KINDSLIST" items="${KINDSLIST}" varStatus="st">
									<c:if test="${KINDSLIST.MCC_S_CODE != 3}">
									<option value="${KINDSLIST.MCC_S_CODE}" <c:if test="${KINDS == KINDSLIST.MCC_S_CODE}">selected="selected" </c:if>>${KINDSLIST.MCC_S_NAME}</option>
									</c:if>
								</c:forEach>
							</select>
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
			<c:if test="${not empty JEGO_LIST and JEGO_LISTSIZE > 0}">
			<div style="width: 1300px;overflow:auto;">
				<table id="nTbl" style="table-layout:fixed;width: 1700px;" class="ReportTable">
					<thead>
						<tr style="height: 40px">
							<th style="width: 6%;" rowspan="2">본부</th>
							<th style="width: 6%;" rowspan="2">센터</th>
							<th style="width: 7%;" rowspan="2">상품코드</th>
							<th style="width: 7%;" rowspan="2">대분류</th>
							<th style="width: 11%;" rowspan="2">중분류</th>
							<th style="width: 7%;" rowspan="2">소분류</th>
							<th style="width: 8%;" colspan="2">전월재고</th>
							<th style="width: 8%;" colspan="2">물류센터 입고</th>
							<th style="width: 8%;" colspan="2">본부별 이동(입고)</th>
							<th style="width: 8%;" colspan="2">본부별 이동(출고)</th>
							<th style="width: 8%;" colspan="2">기타출고</th>
							<th style="width: 8%;" colspan="2">당월 행사 출고</th>
							<th style="width: 8%;" colspan="2">당월 말일 재고</th>
						</tr>
						<tr style="height: 40px">
							<th style="width: 3%;">수량</th>
							<th style="width: 5%;">금액</th>
							<th style="width: 3%;">수량</th>
							<th style="width: 5%;">금액</th>
							<th style="width: 3%;">수량</th>
							<th style="width: 5%;">금액</th>
							<th style="width: 3%;">수량</th>
							<th style="width: 5%;">금액</th>
							<th style="width: 3%;">수량</th>
							<th style="width: 5%;">금액</th>
							<th style="width: 3%;">수량</th>
							<th style="width: 5%;">금액</th>
							<th style="width: 3%;">수량</th>
							<th style="width: 5%;">금액</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="JEGO_LIST" items="${JEGO_LIST}" varStatus="st">
						<c:choose>
						<c:when test="${JEGO_LIST.sumtype == 1}">
						<tr style="background: #F2FFED">
							<td>${JEGO_LIST.MCI_BonbuNAME}</td>
							<td>${JEGO_LIST.MCI_CenterName}</td>
							<td></td>
							<td>${JEGO_LIST.MMC_LGROUPNAME}</td>
							<td colspan="2">소계</td>
							<td>${JEGO_LIST.pqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.pprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.logcqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.logcprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.moveinqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.moveinprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.moveoutqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.moveoutprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.otherqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.otherprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.eventqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.eventprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.nowqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.nowprice}" pattern="#,###" /></td>
						</tr>
						</c:when>
						<c:when test="${JEGO_LIST.sumtype == 2}">
						<tr style="background: #FFFFE4">
							<td>${JEGO_LIST.MCI_BonbuNAME}</td>
							<td>${JEGO_LIST.MCI_CenterName}</td>
							<td colspan="4">합계</td>
							<td>${JEGO_LIST.pqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.pprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.logcqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.logcprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.moveinqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.moveinprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.moveoutqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.moveoutprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.otherqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.otherprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.eventqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.eventprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.nowqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.nowprice}" pattern="#,###" /></td>
						</tr>
						</c:when>
						<c:otherwise>
						<tr>
							<td>${JEGO_LIST.MCI_BonbuNAME}</td>
							<td>${JEGO_LIST.MCI_CenterName}</td>
							<td>${JEGO_LIST.MMC_CODE}</td>
							<td>${JEGO_LIST.MMC_LGROUPNAME}</td>
							<td>${JEGO_LIST.MMC_MGROUP}</td>
							<td>${JEGO_LIST.MMC_SGROUP}</td>
							<td>${JEGO_LIST.pqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.pprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.logcqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.logcprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.moveinqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.moveinprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.moveoutqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.moveoutprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.otherqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.otherprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.eventqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.eventprice}" pattern="#,###" /></td>
							<td>${JEGO_LIST.nowqty}</td>
							<td><fmt:formatNumber value="${JEGO_LIST.nowprice}" pattern="#,###" /></td>
						</tr>
						</c:otherwise>
						</c:choose>
						</c:forEach>
					</tbody>
				</table>
			</div>
			</c:if>
		</div>
	</form:form>
</body>
</html>