<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- START TABLE -->
<div class="scroll_table">
	<table id='nTbl'>
		<thead>
			<tr>
				<th style="width: 5%;"><input type="checkbox" id="pop_all" name="pop_all" value="" onClick="javascript:allCheck();"/></th>
				<th style="width: 10%;">접수번호</th>
				<th style="width: 10%;">입고일자</th>
				<th style="width: 12%;">입고본부</th>
				<th style="width: 10%;">출고일자</th>
				<th style="width: 12%">출고본부</th>
				<th style="width: 10%">대분류</th>
				<th style="width: 10%">중분류</th>
				<th style="width: 10%">소분류</th>
				<th style="width: 11%">상품코드</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${DealInoutListSize > 0 }">
			<c:forEach var="DealInoutList" items="${DealInoutList}" varStatus="st">
			<tr style="height: 40px;">
				<td><input type="checkbox" name="Deal_CHK" class="Deal_CHK" value="${DealInoutList.DOCNO}"  style="width:90%;"/></td>
				<td>${DealInoutList.DOCNO}</td>
				<td>${DealInoutList.INDATE}</td>
				<td>${DealInoutList.INCENTER}</td> 	
				<td>${DealInoutList.OUTDATE}</td>
				<td>${DealInoutList.OUTCENTER}</td> 	 	 
				<td>${DealInoutList.MMC_LGROUPNAME}</td> 	
				<td>${DealInoutList.MMC_MGROUP}</td>
				<td>${DealInoutList.MMC_SGROUP}</td> 
				<td>${DealInoutList.MODEL}</td>	  	 		 	
			</tr>
			</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>
<c:if test="${PTYPE == 1 }">
<div style="text-align: center;">
<c:if test="${pagination.curRange ne 1 }">
<a href="#" onClick="Ajax_DealSearch(1)">[처음]</a> 
</c:if>
<c:if test="${pagination.curPage ne 1}">
<a href="#" onClick="Ajax_DealSearch('${pagination.prevPage }')">[이전]</a> 
</c:if>
<c:forEach var="pageNum" begin="${pagination.startPage }" end="${pagination.endPage }">
<c:choose>
<c:when test="${pageNum eq  pagination.curPage}">
<span style="font-weight: bold;"><a href="#" onClick="Ajax_DealSearch('${pageNum }')">${pageNum }</a></span> 
</c:when>
<c:otherwise>
<a href="#" onClick="Ajax_DealSearch('${pageNum }')">${pageNum }</a> 
</c:otherwise>
</c:choose>
</c:forEach>
<c:if test="${pagination.curPage ne pagination.pageCnt && pagination.pageCnt > 0}">
<a href="#" onClick="Ajax_DealSearch('${pagination.nextPage }')">[다음]</a> 
</c:if>
<c:if test="${pagination.curRange ne pagination.rangeCnt && pagination.rangeCnt > 0}">
<a href="#" onClick="Ajax_DealSearch('${pagination.pageCnt }')">[끝]</a> 
</c:if>
</div>
</c:if>