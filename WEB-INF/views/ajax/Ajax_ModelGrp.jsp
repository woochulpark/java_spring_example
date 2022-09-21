<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${type == 'M'}">
	<select style="width: 90%;" name="MCODEList" id = "MCODEList" onchange="getSLIST(this)">
		<option value="" selected="selected">선택</option>
		<c:if test="${not empty MCODELIST}">
		<c:forEach var="MCODELIST" items="${MCODELIST}" varStatus="st">
		<option value="${MCODELIST.MMC_MGROUP}">${MCODELIST.MMC_MGROUP}</option>
		</c:forEach>
		</c:if>
	</select>
</c:if>
<c:if test="${type == 'S'}">
	<select style="width: 90%;" name="SCODEList" id = "SCODEList">
		<option value="" selected="selected">선택</option>
		<c:if test="${not empty SCODELIST}">
		<c:forEach var="SCODELIST" items="${SCODELIST}" varStatus="st">
		<option value="${SCODELIST.MMC_SGROUP}">${SCODELIST.MMC_SGROUP}</option>
		</c:forEach>
		</c:if>
	</select>
</c:if>
