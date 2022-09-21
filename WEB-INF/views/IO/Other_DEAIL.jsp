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
function cancel() {
	var form = document.otherForm;
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="otherForm" id="otherForm" method="post" action="OtherList.do">
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="DOCNO" value="${OtherInfo.DOO_DOCNO}" id="DOCNO">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 기타출고 > 기타출고 상세</div>
			<h2>기타출고 상세</h2>

			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								${OtherInfo.MCI_LOGCNAME}
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
						</tr>
					</thead>
					<tbody  id='mList'>
						<tr style="height: 40px;">
							<td>${OtherInfo.DOO_MODEL}</td>
							<td>${OtherInfo.MMC_LGROUPNAME}</td>
							<td>${OtherInfo.MMC_MGROUP}</td>
							<td>${OtherInfo.MMC_SGROUP}</td>	 	
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 30%;">출고본부</th>
							<td style="width: 70%;text-align: left;" id='td_B'>
								${OtherInfo.MCI_BonbuNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">센 터</th>
							<td style="width: 70%;text-align: left;" id='td_C'>
								${OtherInfo.MCI_CenterName}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">현 재고</th>
							<td style="width: 70%;text-align: left;" id="td_jego">
								${OtherInfo.DMJ_JEGO}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">접수번호</th>
							<td style="width: 70%;text-align: left;" id="td_jego">
								${OtherInfo.DOO_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">출고일</th>
							<td style="width: 70%;text-align: left;">
								${OtherInfo.DOO_DATE}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">출고수량</th>
							<td style="width: 70%;text-align: left;">
								${OtherInfo.DOO_QTY}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">출고사유</th>
							<td style="width: 70%;text-align: left;">
								${OtherInfo.DOO_OUTCMNT}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">비 고</th>
							<td style="width: 70%;text-align: left;">
								${OtherInfo.DOO_CMNT}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" style="text-align: center;">
				<table id="file_table">
					<thead>
						<tr style="height: 40px">
							<th style="width: 70%;">파일명</th>
							<th style="width: 30%;">용량</th>
						</tr>
					</thead>
					<tbody id="fileTableTbody">
						<c:if test="${not empty FileList}">
						<c:forEach var="FileList" items="${FileList}" varStatus="st">
						<tr style="height: 40px;">
							<td><a href="download.do?sysFile=${FileList.MDF_FILE}&orgFile=${FileList.MDF_FILE_Org}">${FileList.MDF_FILE_Org}</a></td>
							<td>${FileList.MDF_FILE_SIZE} MB</td> 	
						</tr>
						</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>