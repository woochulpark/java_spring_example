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
function save()
{
	var form = document.noticeForm;
	form.submit();
}
function cancel() {
	var form = document.noticeForm;
	form.action = "NOTICE_LIST.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="noticeForm" id="noticeForm" method="post" action="Notice_Add.do" enctype="multipart/form-data">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="DOCNO" value="${DOCNO}" id="DOCNO">
		<input type="hidden" name="TYPE" value="M" id="TYPE">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 공지사항 > ${PAGE}</div>
			<h2>${PAGE}</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">제 목</td>
							<td style="width: 70%;text-align: left;">
								${NoticeInfo.MNI_TITLE}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">작성자</td>
							<td style="width: 70%;text-align: left;">
								${UNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">팝업공지여부</td>
							<td style="width: 70%;text-align: left;">
								<c:if test="${NoticeInfo.MNI_POPUP == 'Y'}">공지</c:if> 
								<c:if test="${NoticeInfo.MNI_POPUP == 'N' or empty NoticeInfo.MNI_POPUP}">공지안함</c:if> 
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">게시판 상단공지</td>
							<td style="width: 70%;text-align: left;">
								<c:if test="${NoticeInfo.MNI_TopNotice == '0'}">등록</c:if> 
								<c:if test="${NoticeInfo.MNI_TopNotice == '1' or empty NoticeInfo.MNI_TopNotice}">등록안함</c:if> 
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">지정여부</td>
							<td style="width: 70%;text-align: left;" onchange="setReceiveType()">
								<c:if test="${NoticeInfo.MNI_ReceiveType == '0'}">전체</c:if>
								<c:if test="${NoticeInfo.MNI_ReceiveType == '1'}">본부</c:if>
								<c:if test="${NoticeInfo.MNI_ReceiveType == '2'}">지정</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- START TABLE -->
			<div class="scroll_table" id="DIV_CENTER" <c:if test="${NoticeInfo.MNI_ReceiveType != '1'}"> style="display:none"</c:if>>
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								${NoticeInfo.MCI_LOGCNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">본 부</td>
							<td style="width: 70%;text-align: left;" id = 'td_B'>
								${NoticeInfo.MCI_BonbuNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">센 터</td>
							<td style="width: 70%;text-align: left;"  id = 'td_C'>
								${NoticeInfo.MCI_CenterName}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" id="DIV_SUSIN" style="<c:if test="${NoticeInfo.MNI_ReceiveType != '2'}">display:none;</c:if>margin:5px;height: 250px;overflow: auto;background-color: #fff;">
				<table id='susin_table'>
					<thead>
						<tr>
							<th style="width: 25%;">본부</th>
							<th style="width: 25%;">센터</th>
							<th style="width: 25%">직급</th>
							<th style="width: 25%">이름</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${SusinListSize > 0 }">
					<c:forEach var="SusinList" items="${SusinList}" varStatus="st">
						<tr style="height: 40px;">
							<td>${SusinList.MCI_BonbuNAME}</td>
							<td>${SusinList.MCI_CenterName}</td> 	
							<td>${SusinList.POSITIONNAME}</td>
							<td>${SusinList.MUT_USERNAME}</td> 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" style="text-align: center;">
				<textarea class="addr" name="CMNT" readonly="readonly">${NoticeInfo.MNI_Cmnt}</textarea>
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
				<c:if test="${userInfo.MUT_POSITION == '001' or userInfo.MUT_POSITION == '002'}">
				<a href="javascript:save();" class="search grey button">수정</a>
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