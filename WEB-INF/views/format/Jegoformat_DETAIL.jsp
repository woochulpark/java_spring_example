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
$(document).ready(function(){
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
});
function cancel() {
	var form = document.formatForm;
	form.action = "Jegoformat_List.do";
	form.submit();
}
function JegoCfm(STS)
{
	document.getElementById("STS").value = STS;
	var form = document.formatForm;
	form.action = "JegoCfm.do";
	form.submit();
}
function JegoPrint(){
	var DOCNO = document.getElementById("DOCNO").value;
	$.ajax({
		type:"GET",
		url:"Ajax_JegoExcel.do",
		data: "DOCNO=" + DOCNO,
		success:function(data){
			if(data == '1'){
				location.href = 'download.do?sysFile=' + DOCNO + '.xlsx&orgFile=재고명세서_' + DOCNO + '.xlsx&PATH=EXCEL';
			}
			else if(data == '-1'){
				alert('발급정보를 가져오지 못했습니다.');
			}
			else if(data == '-2'){
				alert('출력 권한이 없습니다.');
			}
			else if(data == '-3'){
				alert('재고현황을 가져오지 못했습니다.');
			}
			else if(data == '-4'){
				alert('알수없는 에러! 다시 시도해주세요.');
			}
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="formatForm" method="post" action="Jegoformat_List_Search.do">
		<input type="hidden" name="menuGroup" value="menu_customer" id="menuGroup">
		<input type="hidden" name="DOCNO" id="DOCNO" value="${JealFormatInfo.DDJ_DOCNO}">
		<input type="hidden" name="PAGE" id="PAGE" value="1" >
		<input type="hidden" name="STS" id="STS">
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

			<div class="subtitle" style="text-align: right;">서식자료 > 재고명세서 상세</div>
			<h2>재고명세서 상세</h2>
			
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 30%;">내역서 번호</th>
							<td style="width: 70%;text-align: left;" colspan="3">
								${JealFormatInfo.DDJ_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">발급일자</th>
							<td style="width: 70%;text-align: left;" colspan="3">
								${JealFormatInfo.DDJ_DATE}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">작성자</th>
							<td style="width: 70%;text-align: left;" colspan="3">
								${JealFormatInfo.DDJ_USER}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">조회기간</th>
							<td style="width: 70%;text-align: left;">
								${JealFormatInfo.DDJ_INFODATE}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 25%;">본 부</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${JealFormatInfo.CENTERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">등록번호</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${JealFormatInfo.BRN}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">상 호</th>
							<td style="width: 25%;text-align: left;">
								${JealFormatInfo.CONAME}
							</td>
							
							<th style="width: 25%;">성 명</th>
							<td style="width: 25%;text-align: left;">
								${JealFormatInfo.CEO}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">주 소</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${JealFormatInfo.ADDR}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">업 태</th>
							<td style="width: 25%;text-align: left;">
								${JealFormatInfo.COTYPE}
							</td>
							<th style="width: 25%;">종 목</th>
							<td style="width: 25%;text-align: left;">
								${JealFormatInfo.JM}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="scroll_table">
				<table id="nTbl">
					<thead>
						<tr>
							<th style="width: 33%;">승인여부</th>
							<th style="width: 33%;">승인자</th>
							<th style="width: 34%;">승인일자</th>
						</tr>
					</thead>
					<tbody  id='mList'>
						<tr style="height: 40px;">
							<c:choose>
								<c:when test="${JealFormatInfo.DDJ_STS == '0'}">
								<td>미승인</td>
								<td>-</td>
								<td>-</td> 	
								</c:when>
								<c:when test="${JealFormatInfo.DDJ_STS == '1'}">
								<td>승인</td>
								<td>${JealFormatInfo.STSUSERNM}</td>
								<td>${JealFormatInfo.DDJ_STSDATE}</td> 	
								</c:when>
								<c:otherwise>
								<td>반려</td>
								<td>${JealFormatInfo.STSUSERNM}</td>
								<td>${JealFormatInfo.DDJ_STSDATE}</td> 	
								</c:otherwise>
							</c:choose>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 30%;">전달 및 특이사항</th>
							<td style="width: 70%;text-align: left;" colspan="3">
								<textarea class="dealaddr" name="DDJ_CMNT">${JealFormatInfo.DDJ_CMNT}</textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div style="text-align: center;">
				<c:if test="${JealFormatInfo.DDJ_STS == '0' and (UserInfo.MUT_POSITION == '001' or ((UserInfo.MUT_POSITION == '003' or UserInfo.MUT_POSITION == '004') and JealFormatInfo.DDJ_CENTER == UserInfo.MUT_CENTER))}">
				<a href="javascript:JegoCfm('1');" class="search grey button">승 인</a>
				<a href="javascript:JegoCfm('2');" class="search grey button">반 려</a>
				</c:if>
				<a href="javascript:JegoPrint();" class="search grey button">출 력</a>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
		</div>
	</form:form>
</body>
</html>