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
	var form = document.modelForm;
	form.submit();
}
function MODEL_Add()
{
	var form = document.modelForm;
	form.action = "MODEL_Add.do";
	form.submit();
}
function MODEL_DETAIL(MCODE){
	document.getElementById("MCODE").value = MCODE;
	var form = document.modelForm;
	form.action = "MODEL_DETAIL.do";
	form.submit();
}
function getLLIST(){
	var option = "<option value=''>선택</option>";
	$("#LCODEList option:eq(0)").prop("selected", true);
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
    $("#MCODEList").empty();
    $('#MCODEList').append(option);
}
function getMLIST(obj){
	var option = "<option value=''>선택</option>";
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
	var LOGC = $("#LOGC option:selected").val();
	$.ajax({
		type:"GET",
		url:"Ajax_MLIST.do",
		data: "LOGC="+LOGC+"&LCODE="+obj.value,
		success:function(html){
			$("#td_M").empty();
			$("#td_M").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getSLIST(obj){
	var LOGC = $("#LOGC option:selected").val();
	var LCODE = document.getElementById("LCODEList").value;
	$.ajax({
		type:"GET",
		url:"Ajax_SLIST.do",
		data: "LOGC="+LOGC+"&LCODE="+LCODE+"&MCODE="+obj.value,
		success:function(html){
			$("#td_S").empty();
			$("#td_S").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function fn_paging(PAGE)
{
	document.getElementById("PAGE").value = PAGE;
	var form = document.modelForm;
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="modelForm" method="post" action="ModelList_search.do">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="MCODE" value="" id="MCODE">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 상품코드관리</div>
			<h2>상품코드</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 700px;">
					<tr>
						<td style="text-align: center;">물류센터</td>
						<td colspan="2">
							<select style="width: 90%;" name="LOGC" id = "LOGC" onchange="getLLIST()">
								<option value="">선택</option>
								<c:if test="${not empty LOGCLIST}">
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<c:if test="${LOGC == LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}" selected="selected">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
									<c:if test="${LOGC != LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">대분류</td>
						<td colspan="2">
							<select style="width: 90%;" name="LCODEList" id="LCODEList" onchange="getMLIST(this)">
								<option value="">선택</option>
								<c:forEach var="LCODELIST" items="${LCODELIST}" varStatus="st">
									<c:if test="${LCODE == LCODELIST.MCC_S_CODE}">
										<option value="${LCODELIST.MCC_S_CODE}" selected="selected">${LCODELIST.MCC_S_NAME}</option>
									</c:if>
									<c:if test="${LCODE != LCODELIST.MCC_S_CODE}">
										<option value="${LCODELIST.MCC_S_CODE}">${LCODELIST.MCC_S_NAME}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="width: 30%;text-align: center;">중분류</td>
						<td style="width: 35%" id = 'td_M'>
							<select style="width: 90%;" name="MCODEList" id = "MCODEList" onchange="getSLIST(this)">
								<option value="">선택</option>
								<c:if test="${not empty MCODELIST}">
								<c:forEach var="MCODELIST" items="${MCODELIST}" varStatus="st">
									<c:if test="${MCODE == MCODELIST.MMC_MGROUP}">
										<option value="${MCODELIST.MMC_MGROUP}" selected="selected">${MCODELIST.MMC_MGROUP}</option>
									</c:if>
									<c:if test="${MCODE != MCODELIST.MMC_MGROUP}">
										<option value="${MCODELIST.MMC_MGROUP}">${MCODELIST.MMC_MGROUP}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td style="width: 35%;">
							<input type="text" name="MCODENAME" id="MCODENAME" value="${MCODENAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">소분류</td>
						<td id = 'td_S'>
							<select style="width: 90%;" name="SCODEList" id = "SCODEList">
								<option value="">선택</option>
								<c:if test="${not empty SCODELIST}">
								<c:forEach var="SCODELIST" items="${SCODELIST}" varStatus="st">
									<c:if test="${SCODE == SCODELIST.MMC_SGROUP}">
										<option value="${SCODELIST.MMC_SGROUP}" selected="selected">${SCODELIST.MMC_SGROUP}</option>
									</c:if>
									<c:if test="${SCODE != SCODELIST.MMC_SGROUP}">
										<option value="${SCODELIST.MMC_SGROUP}">${SCODELIST.MMC_SGROUP}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td>
							<input type="text" name="SCODENAME" id="SCODENAME" value="${SCODENAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">상품코드명</td>
						<td colspan="2">
							<input type="text" name="MNAME" id="MNAME" value="${MNAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td colspan="3" style="text-align: center;" >
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<c:if test="${level == '001'}">
			<div style="text-align:right;">
				<a href="javascript:MODEL_Add()" class="search grey button">상품코드 추가</a>
			</div>
			</c:if>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 10%;">물류센터</th>
							<th style="width: 10%;">상품코드</th>
							<th style="width: 10%;">대분류</th>
							<th style="width: 13%">중분류</th>
							<th style="width: 13%">소분류</th>
							<th style="width: 10%">ERP코드</th>
							<th style="width: 12%">시작일</th>
							<th style="width: 12%">종료일</th>
							<th style="width: 10%">운영여부</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${ModelListSize > 0 }">
					<c:forEach var="ModelList" items="${ModelList}" varStatus="st">
						<tr style="height: 40px;" <c:if test="${level == '001' or  level == '002'}">onclick="javascript:MODEL_DETAIL('${ModelList.MMC_CODE}')"</c:if>>
							<td>${ModelList.MMC_LOGCNAME}</td>
							<td>${ModelList.MMC_CODE}</td>
							<td>${ModelList.MMC_LGROUPNAME}</td> 	
							<td>${ModelList.MMC_MGROUP}</td>
							<td>${ModelList.MMC_SGROUP}</td> 	 
							<td>${ModelList.MMC_ERPMODEL}</td> 	 	 
							<td>${ModelList.MMC_StartDay}</td> 	
							<td>${ModelList.MMC_EndDay}</td>
							<td>
								<c:if test="${ModelList.MMC_BizSts == 'Y'}">
									운영
								</c:if>
								<c:if test="${ModelList.MMC_BizSts == 'N'}">
									종료
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