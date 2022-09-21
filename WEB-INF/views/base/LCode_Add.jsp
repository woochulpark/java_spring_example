<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
<script language="javascript">
function save()
{	
	var check = true;
	if(document.getElementById('TYPE').value == "I" && (document.getElementById('LCODE').value == "" || document.getElementById('LCODE').value == null)){
		document.getElementById('MSG').innerText = "대분류 번호를 입력해주세요.";
		document.getElementById('LCODE').focus();
		check = false;
	}
	if(document.getElementById('LNAME').value == "" || document.getElementById('LNAME').value == null){
		document.getElementById('MSG').innerText = "대분류명을 입력해주세요.";
		document.getElementById('LNAME').focus();
		check = false;
	}
	if(check){
		var form = document.codeForm;
		form.submit();
	}
	
}
function Code_delete() {
	var del_chk = confirm('공통코드를 삭제하면 시스템 운영에 문제가 생길 수 있습니다. 삭제하시겠습니까?');
	if(del_chk==true){
		var form = document.codeForm;
		form.action = "LCode_Add_Del.do";
		form.submit();
	}
}
function cancel() {
	var form = document.codeForm;
	form.action = "ComCode.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="codeForm" modelAttribute="codeForm" method="post" action="LCode_Add_Save.do">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="TYPE" value="${TYPE}" id="TYPE">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 공통 코드 관리 > ${PAGE}</div>
			<h2>${PAGE}</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">대분류 번호</td>
							<td style="width: 70%;text-align: left;">
								<c:if test="${TYPE == 'M'}">${LCODE}<input type="hidden" name="LCODE" value="${LCODE}" id="LCODE"></c:if>
								<c:if test="${TYPE == 'I'}"><input type="text" name="LCODE" id="LCODE" value="${LCODE}"  style="width:90%;"/></c:if>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">대분류 명칭</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="LNAME" id="LNAME" value="${LNAME}"  style="width:90%;"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:if test="${TYPE == 'M'}">
					<a href="javascript:Code_delete();" class="search grey button">삭제</a>
				</c:if>
				<a href="javascript:save();" class="search grey button">저장</a>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>