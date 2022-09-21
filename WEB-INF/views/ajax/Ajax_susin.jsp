<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<thead>
						<tr>
							<th style="width: 5%;"><input type="checkbox" id="pop_all" name="pop_all" value="" onClick="javascript:allCheck();"/></th>
							<th style="width: 25%;">본 부</th>
							<th style="width: 25%">센 터</th>
							<th style="width: 20%">직 급</th>
							<th style="width: 25%">이 름</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${UserListSize > 0 }">
					<c:forEach var="UserList" items="${UserList}" varStatus="st">
						<tr style="height: 40px;" onclick="javascript:User_Add('${UserList.MUT_USERID}')">
							<td><input type="checkbox" name="SUSIN_CHK" class="SUSIN_CHK" value="${UserList.MUT_USERID}"  style="width:90%;"/></td>
							<td>${UserList.MCI_BonbuNAME}</td>
							<td>${UserList.MCI_CenterName}</td> 	
							<td>${UserList.POSITIONNAME}</td>
							<td>${UserList.MUT_USERNAME}</td>	  	 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
            <c:if test="${pagination.curRange ne 1 }">
            <a href="#" onClick="pop_search(1)">[처음]</a> 
            </c:if>
            <c:if test="${pagination.curPage ne 1}">
            <a href="#" onClick="pop_search('${pagination.prevPage }')">[이전]</a> 
            </c:if>
            <c:forEach var="pageNum" begin="${pagination.startPage }" end="${pagination.endPage }">
            <c:choose>
            <c:when test="${pageNum eq  pagination.curPage}">
            <span style="font-weight: bold;"><a href="#" onClick="pop_search('${pageNum }')">${pageNum }</a></span> 
            </c:when>
            <c:otherwise>
            <a href="#" onClick="pop_search('${pageNum }')">${pageNum }</a> 
            </c:otherwise>
            </c:choose>
            </c:forEach>
            <c:if test="${pagination.curPage ne pagination.pageCnt && pagination.pageCnt > 0}">
            <a href="#" onClick="pop_search('${pagination.nextPage }')">[다음]</a> 
            </c:if>
            <c:if test="${pagination.curRange ne pagination.rangeCnt && pagination.rangeCnt > 0}">
            <a href="#" onClick="pop_search('${pagination.pageCnt }')">[끝]</a> 
            </c:if>
            </div>