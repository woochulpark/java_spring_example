<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${type == 'OUT'}">
	<select style="width: 90%;" name="POPCENTER" id = "POPCENTER">
		<option value="">선택</option>
		<c:if test="${not empty CENTERLIST}">
		<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
		<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
		</c:forEach>
		</c:if>
	</select>
</c:if>
<c:if test="${type == 'IN'}">
	<select style="width: 90%;" name="POPINCENTER" id = "POPINCENTER">
		<option value="">선택</option>
		<c:if test="${not empty CENTERLIST}">
		<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
		<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
		</c:forEach>
		</c:if>
	</select>
</c:if>
