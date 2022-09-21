<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- START TABLE -->
<div class="scroll_table">
	<table id='nTbl'>
		<thead>
			<tr>
				<th style="width: 9%;">접수번호</th>
				<th style="width: 9%;">상품코드</th>
				<th style="width: 10%;">대분류</th>
				<th style="width: 10%">중분류</th>
				<th style="width: 10%">소분류</th>
				<th style="width: 9%">제조일자</th>
				<th style="width: 9%">입고일자</th>
				<th style="width: 9%">입고단가</th>
				<th style="width: 8%">입고수량</th>
				<th style="width: 9%">총금액</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${LogcInListSize > 0 }">
		<c:forEach var="LogcInList" items="${LogcInList}" varStatus="st">
			<tr style="height: 40px;" onclick="javascript:AddLogcIn('${LogcInList.DIL_DOCNO}', '${LogcInList.DIL_MODEL}', '${LogcInList.MMC_LGROUPNAME}', '${LogcInList.MMC_MGROUP}', '${LogcInList.MMC_SGROUP}', '${LogcInList.DIL_JEJO}', '${LogcInList.DIL_INDate}', '<fmt:formatNumber value="${LogcInList.DIL_INPRICE}" pattern="#,###" />', '${LogcInList.DIL_QTY}', '<fmt:formatNumber value="${LogcInList.INTOTAL}" pattern="#,###" />')">
				<td>${LogcInList.DIL_DOCNO}</td>
				<td>${LogcInList.DIL_MODEL}</td>
				<td>${LogcInList.MMC_LGROUPNAME}</td>
				<td>${LogcInList.MMC_MGROUP}</td> 	
				<td>${LogcInList.MMC_SGROUP}</td>
				<td>${LogcInList.DIL_JEJO}</td> 	 
				<td>${LogcInList.DIL_INDate}</td> 	 	 
				<td><fmt:formatNumber value="${LogcInList.DIL_INPRICE}" pattern="#,###" /></td> 	
				<td>${LogcInList.DIL_QTY}</td>
				<td><fmt:formatNumber value="${LogcInList.INTOTAL}" pattern="#,###" /></td>	  	 		 	
			</tr>
		</c:forEach>
		</c:if>
		</tbody>
	</table>
</div>
<div style="text-align: center;">
<c:if test="${pagination.curRange ne 1 }">
<a href="#" onClick="Ajax_BonbuIn_search(1)">[처음]</a> 
</c:if>
<c:if test="${pagination.curPage ne 1}">
<a href="#" onClick="Ajax_BonbuIn_search('${pagination.prevPage }')">[이전]</a> 
</c:if>
<c:forEach var="pageNum" begin="${pagination.startPage }" end="${pagination.endPage }">
<c:choose>
<c:when test="${pageNum eq  pagination.curPage}">
<span style="font-weight: bold;"><a href="#" onClick="Ajax_BonbuIn_search('${pageNum }')">${pageNum }</a></span> 
</c:when>
<c:otherwise>
<a href="#" onClick="Ajax_BonbuIn_search('${pageNum }')">${pageNum }</a> 
</c:otherwise>
</c:choose>
</c:forEach>
<c:if test="${pagination.curPage ne pagination.pageCnt && pagination.pageCnt > 0}">
<a href="#" onClick="Ajax_BonbuIn_search('${pagination.nextPage }')">[다음]</a> 
</c:if>
<c:if test="${pagination.curRange ne pagination.rangeCnt && pagination.rangeCnt > 0}">
<a href="#" onClick="Ajax_BonbuIn_search('${pagination.pageCnt }')">[끝]</a> 
</c:if>
</div>