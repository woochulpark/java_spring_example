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
function Modify() {
	var form = document.LogcInForm;
	form.submit();
}
function Delete() {
	var Del_Chk = confirm('입고정보를 삭제하시겠습니까?');
	if(Del_Chk==true){
		var form = document.LogcInForm;
		form.action = "LogcIn_Delete.do";
		form.submit();
	}
}
function cancel() {
	var form = document.LogcInForm;
	form.action = "LogcInList.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="LogcInForm" method="post" action="LogcIn_Modify.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="DOCNO" value="${LogcInInfo.DIL_DOCNO}" id="DOCNO">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 물류센터 입고 > 물류센터 입고 상세</div>
			<h2>물류센터 입고 상세</h2>

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
						<tr style="height: 40px">
							<td style="width: 30%;">접수번호</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">상품코드</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_MODEL}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">대분류</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MMC_LGROUPNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">중분류</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MMC_MGROUP}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">소분류</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MMC_SGROUP}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">제조일자</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_JEJO}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">입고일자</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_INDate}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">입고단가</td>
							<td style="width: 70%;text-align: left;">
								<fmt:formatNumber value="${LogcInInfo.DIL_INPRICE}" pattern="#,###" />
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">입고수량</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_QTY}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">총금액</td>
							<td style="width: 70%;text-align: left;">
								<fmt:formatNumber value="${LogcInInfo.DIL_INPRICE * LogcInInfo.DIL_QTY}" pattern="#,###" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:if test="${LogcInInfo.DIL_OUTSts == 'N' && level == '001'}">
				<a href="javascript:Modify();" class="search grey button">수정</a>
				<a href="javascript:Delete();" class="search grey button">삭제</a>
				</c:if>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
		</div>
	</form:form>
</body>
</html>