<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script src="js/jquery-ui.js" language="javascript"></script>
<script src="js/common.js" language="javascript"></script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
<script language="javascript">
$(document).ready(function(){
	mergeRows(nTbl,0); // 위 행과 비교하여 같다면 첫번째 컬럼 머지
	var PMSG = '${msg}';
	if(PMSG.length > 0){
		alert(PMSG);
	}
});
function BonbuIn_Modify()
{
	var form = document.BonbuInForm;
	form.submit();
}
function BonbuIn_Delete()
{
	var form = document.BonbuInForm;
	form.action = "BonbuIn_Delete.do";
	form.submit();
}
function cancel() {
	var form = document.BonbuInForm;
	form.action = "BonbuInList.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="BonbuInForm" method="post" action="BonbuIn_Modify.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type='hidden' name='DOCNO'  id='DOCNO' value='${LogcInInfo.DIL_DOCNO}' >
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부입고 > 본부입고 상세</div>
			<h2>본부입고 상세</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MCI_LOGCNAME}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" id="Table_MLIST">
				<table>
					<thead>
						<tr>
							<th style="width: 10%;">물류 접수번호</th>
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
						<tr style="height: 40px;">
							<td>${LogcInInfo.DIL_DOCNO}</td>
							<td>${LogcInInfo.DIL_MODEL}</td>
							<td>${LogcInInfo.MMC_LGROUPNAME}</td>
							<td>${LogcInInfo.MMC_MGROUP}</td> 	
							<td>${LogcInInfo.MMC_SGROUP}</td>
							<td>${LogcInInfo.DIL_JEJO}</td> 	
							<td>${LogcInInfo.DIL_INDate}</td> 
							<td>${LogcInInfo.DIL_INPRICE}</td> 
							<td>${LogcInInfo.DIL_QTY}</td> 
							<td>${LogcInInfo.INTOTAL}</td>  	 	
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">출고수량</td>
							<td style="width: 70%;text-align: left;" id="td_Outcnt">
								${LogcInInfo.OUTTOTAL}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류 출고일</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_OUTDATE}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" >
				<table id="nTbl">
					<thead>
						<tr>
							<th style="width: 15%;">본 부</th>
							<th style="width: 15%;">센 터</th>
							<th style="width: 20%">접수번호</th>
							<th style="width: 20%">출고수량</th>
							<th style="width: 15%">현 재고</th>
							<th style="width: 15%">적정 재고</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="BonbuInInfo" items="${BonbuInInfo}" varStatus="st">
						<tr style="height: 40px;">
							<td>${BonbuInInfo.MCI_BonbuNAME}</td>
							<td>${BonbuInInfo.MCI_CenterName}</td>
							<td>${BonbuInInfo.DBI_DOCNO}</td>
							<td>${BonbuInInfo.DBI_QTY}</td> 	
							<td>${BonbuInInfo.DMJ_JEGO}</td>
							<td>${BonbuInInfo.MMJ_JEGO}</td> 	 	 	
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:if test="${level == '001'}">
				<a href="javascript:BonbuIn_Modify();" class="search grey button">수정</a>
				<c:if test="${LogcInInfo.SUS_CNT == '0' }">
				<a href="javascript:BonbuIn_Delete();" class="search grey button">삭제</a>
				</c:if>
				</c:if>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>