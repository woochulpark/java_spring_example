<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${type == 'B'}">
	<select style="width: 30%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
		<option value="">선택</option>
		<c:if test="${not empty BONBULIST}">
		<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
		<option value="${BONBULIST.MCI_Bonbu}">${BONBULIST.MCI_BonbuNAME}</option>
		</c:forEach>
		</c:if>
	</select>
	<select style="width: 30%;" name="CENTER" id = "CENTER">
		<option value="">선택</option>
	</select>
</c:if>
<c:if test="${type == 'C'}">
	<select style="width: 30%;" name="CENTER" id = "CENTER">
		<option value="">선택</option>
		<c:if test="${not empty CENTERLIST}">
		<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
		<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
		</c:forEach>
		</c:if>
	</select>
</c:if>
