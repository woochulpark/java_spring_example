<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${type == 'OUT'}">
	<select style="width: 90%;" name="POPBONBU" id = "POPBONBU" onchange="getPOPCENTER(this, 'OUT')">
		<option value="" selected="selected">선택</option>
		<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
		<option value="${BONBULIST.MCI_Bonbu}" >${BONBULIST.MCI_BonbuNAME}</option>
		</c:forEach>
	</select>
</c:if>
<c:if test="${type == 'IN'}">
	<select style="width: 90%;" name="POPINBONBU" id = "POPINBONBU" onchange="getPOPCENTER(this, 'IN')">
		<option value="" selected="selected">선택</option>
		<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
		<option value="${BONBULIST.MCI_Bonbu}" >${BONBULIST.MCI_BonbuNAME}</option>
		</c:forEach>
	</select>
</c:if>
