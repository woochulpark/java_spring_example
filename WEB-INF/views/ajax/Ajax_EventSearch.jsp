<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- START TABLE -->
<div class="scroll_table">
	<table id='nTbl'>
		<thead>
			<tr>
				<th style="width: 10%;">본부</th>
				<th style="width: 10%;">센터</th>
				<th style="width: 10%;">행사번호</th>
				<th style="width: 10%">의전팀장</th>
				<th style="width: 10%">행사시작일시</th>
				<th style="width: 10%">단체명</th>
				<th style="width: 10%">장례식장</th>
				<th style="width: 10%">행사상태</th>
				<th style="width: 10%">일반단체구분</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${EVENTLISTSize > 0 }">
			<c:forEach var="EVENTLIST" items="${EVENTLIST}" varStatus="st">
			<tr style="height: 40px;" onclick="javascript:AddEvent('${EVENTLIST.DOCNO}')">
				<td>${EVENTLIST.BONBUNAME}</td>
				<td>${EVENTLIST.CENTERNAME}</td>
				<td>${EVENTLIST.DOCNO}</td> 	
				<td>${EVENTLIST.EUSERNAME}</td>
				<td>${EVENTLIST.EVENTDATE}</td> 	 
				<td>${EVENTLIST.GROUPNAME}</td> 	 	 
				<td>${EVENTLIST.JNAME}</td> 	
				<td>${EVENTLIST.ESTATE}</td> 	
				<td>${EVENTLIST.GTYPE}</td> 	 		 	
			</tr>
			</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>
<div style="text-align: center;">
<c:if test="${pagination.curRange ne 1 }">
<a href="#" onClick="Ajax_Event_search(1)">[처음]</a> 
</c:if>
<c:if test="${pagination.curPage ne 1}">
<a href="#" onClick="Ajax_Event_search('${pagination.prevPage }')">[이전]</a> 
</c:if>
<c:forEach var="pageNum" begin="${pagination.startPage }" end="${pagination.endPage }">
<c:choose>
<c:when test="${pageNum eq  pagination.curPage}">
<span style="font-weight: bold;"><a href="#" onClick="Ajax_Event_search('${pageNum }')">${pageNum }</a></span> 
</c:when>
<c:otherwise>
<a href="#" onClick="Ajax_Event_search('${pageNum }')">${pageNum }</a> 
</c:otherwise>
</c:choose>
</c:forEach>
<c:if test="${pagination.curPage ne pagination.pageCnt && pagination.pageCnt > 0}">
<a href="#" onClick="Ajax_Event_search('${pagination.nextPage }')">[다음]</a> 
</c:if>
<c:if test="${pagination.curRange ne pagination.rangeCnt && pagination.rangeCnt > 0}">
<a href="#" onClick="Ajax_Event_search('${pagination.pageCnt }')">[끝]</a> 
</c:if>
</div>