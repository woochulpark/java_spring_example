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
$(document).ready(function(){
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
});
function search()
{
	var form = document.userForm;
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
function User_Add()
{
	var form = document.userForm;
	form.action = "User_Add.do";
	form.submit();
}
function User_Modify(USERID)
{
	document.getElementById("USERID").value = USERID;
	var form = document.userForm;
	form.action = "User_Modify.do";
	form.submit();
}
function fn_paging(PAGE)
{
	document.getElementById("PAGE").value = PAGE;
	var form = document.userForm;
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="userForm" modelAttribute="userForm" method="post" action="Userlist_search.do">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="USERID" value="" id="USERID">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 사용자 관리</div>
			<h2>사용자 관리</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 50%;">
					<tr>
						<td style="text-align: center;">물류센터</td>
						<td>
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
					</tr>
					<tr>
						<td style="text-align: center;">본 부</td>
						<td id = 'td_B'>
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
					</tr>
					<tr>
						<td style="text-align: center;">센 터</td>
						<td id = 'td_C'>
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
					<tr>
						<td style="text-align: center;">이 름</td>
						<td>
							<input type="text" name="UNAME" id="UNAME" value="${UNAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<!-- START TABLE -->
			<div style="text-align:right;">
				<a href="javascript:User_Add()" class="search grey button">신 규</a>
			</div>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 10%;">물류센터</th>
							<th style="width: 10%;">본 부</th>
							<th style="width: 10%">센 터</th>
							<th style="width: 10%">직 급</th>
							<th style="width: 20%">이 름</th>
							<th style="width: 15%">시작일</th>
							<th style="width: 15%">종료일</th>
							<th style="width: 10%">근로상태</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${UserListSize > 0 }">
					<c:forEach var="UserList" items="${UserList}" varStatus="st">
						<tr style="height: 40px;" onclick="javascript:User_Modify('${UserList.MUT_USERID}')">
							<td>${UserList.MCI_LOGCNAME}</td>
							<td>${UserList.MCI_BonbuNAME}</td>
							<td>${UserList.MCI_CenterName}</td> 	
							<td>${UserList.POSITIONNAME}</td>
							<td>${UserList.MUT_USERNAME}</td> 	 
							<td>${UserList.MUT_STARTDAY}</td> 	 	 
							<td>${UserList.MUT_ENDDAY}</td> 	
							<td>
								<c:if test="${UserList.MUT_BIZSTS == 'Y'}">
									근무중
								</c:if>
								<c:if test="${UserList.MUT_BIZSTS == 'N'}">
									퇴직
								</c:if>
							</td> 	  	 		 	
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