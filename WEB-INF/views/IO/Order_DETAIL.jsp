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
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function OrderModify()
{
	var form = document.orderForm;
	form.submit();
}
function OrderDelete()
{
	var form = document.orderForm;
	form.action = "Order_Delete.do";
	form.submit();
}
function cancel() {
	var form = document.orderForm;
	form.action = "OrderList.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="orderForm" method="post" action="Order_Modify.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="DOCNO" value="${OrderInfo.DBO_DOCNO}" id="DOCNO">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부발주 > 본부발주 상세</div>
			<h2>본부발주 상세</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">접수번호</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.DBO_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주본부</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.MCI_BonbuNAME}&nbsp;${OrderInfo.MCI_CenterName}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주자</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.DBO_USERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주일</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.DBO_DATE}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">비 고</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.DBO_CMNT}
							</td>
						</tr>
					</tbody>
				</table>
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
						<tr style="height: 40px;">
							<td>${OrderInfo.DBO_MODEL}</td>
							<td>${OrderInfo.MMC_LGROUPNAME}</td>
							<td>${OrderInfo.MMC_MGROUP}</td>
							<td>${OrderInfo.MMC_SGROUP}</td> 	
							<td>${OrderInfo.DBO_QTY}</td>	 	 	
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:if test="${((level == '001' or level == '002') and OrderInfo.DBO_BisSts == 'N' and OrderInfo.DBM_Sts == 'N')
					or ((level == '003' or level == '004') and OrderInfo.DBO_BisSts == 'N' and OrderInfo.DBM_Sts == 'N' and OrderInfo.DBM_CFM == 'N')}">
				<a href="javascript:OrderModify();" class="search grey button">수 정</a>
				<a href="javascript:OrderDelete();" class="search grey button">발주취소</a>
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