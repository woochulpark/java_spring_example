<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${BonbuInListSize > 0 }">
<table>
	<thead>
		<tr>
			<th style="width: 15%;">접수번호</th>
			<th style="width: 15%;">출고일자</th>
			<th style="width: 15%;">본 부</th>
			<th style="width: 15%;">센 터</th>
			<th style="width: 10%">입고수량</th>
			<th style="width: 15%">입고일자</th>
			<th style="width: 15%">물류상태</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="BonbuInList" items="${BonbuInList}" varStatus="st">
		<tr style="height: 40px;">
			<td>${BonbuInList.DBI_DOCNO}</td>
			<td>${BonbuInList.DIL_OUTDATE}</td>
			<td>${BonbuInList.MCI_BonbuNAME}</td>
			<td>${BonbuInList.MCI_CenterName}</td> 	
			<td>${BonbuInList.DIL_QTY}</td>	
			<td>
			<c:if test="${BonbuInList.DBI_BisSts == 'Y'}">
				${BonbuInList.DBI_CFMDATE}
			</c:if>
			<c:if test="${BonbuInList.DBI_BisSts == 'N'}">
				&nbsp;-&nbsp;
			</c:if>
			</td>
			<td>
			<c:if test="${BonbuInList.DBI_BisSts == 'Y'}">
				입고완료
			</c:if>
			<c:if test="${BonbuInList.DBI_BisSts == 'N'}">
				미입고
			</c:if>
			</td> 	  	 
		</tr>
		</c:forEach>
	</tbody>
</table>
</c:if>