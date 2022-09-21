<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<select style="width: 90%;" name="CENTER" id = "CENTER" onchange="getJEGO()">
	<option value="">선택</option>
	<c:if test="${not empty CENTERLIST}">
	<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
	<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
	</c:forEach>
	</c:if>
</select>