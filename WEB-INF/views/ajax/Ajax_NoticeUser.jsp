<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
				<table>
					<thead>
						<tr>
							<th style="width: 20%;">본 부</th>
							<th style="width: 20%">센 터</th>
							<th style="width: 20%">직 급</th>
							<th style="width: 20%">이 름</th>
							<th style="width: 20%">확인일자</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${UserListSize > 0 }">
					<c:forEach var="UserList" items="${UserList}" varStatus="st">
						<tr style="height: 40px;">
							<td>${UserList.MCI_BonbuNAME}</td>
							<td>${UserList.MCI_CenterName}</td> 	
							<td>${UserList.POSITIONNAME}</td>
							<td>${UserList.MUT_UserName}</td>
							<td>${UserList.MNU_DATE}</td>	  	 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>