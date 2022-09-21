<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${BonbuInInfoSize > 0 }">
<c:if test="${TYPE == 'I' }">
<table id="Table_Bonbu">
	<thead>
		<tr>
			<th style="width: 15%;">본 부</th>
			<th style="width: 15%;">센 터</th>
			<th style="width: 20%">출고수량</th>
			<th style="width: 15%">현 재고</th>
			<th style="width: 15%">적정 재고</th>
			<th style="width: 20%">적정 재고 설정</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="BonbuInInfo" items="${BonbuInInfo}" varStatus="st">
		<tr style="height: 40px;">
			<td>${BonbuInInfo.MCI_BonbuNAME}<input type='hidden' name='CENTER' value='${BonbuInInfo.MCI_CODE}' ></td>
			<td>${BonbuInInfo.MCI_CenterName}</td>
			<td><input type="number" name='OUTCNT' value='0' style='width:90%;' onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="0" onchange="TotaolPrice()"/></td> 	
			<td>${BonbuInInfo.DMJ_JEGO}</td>
			<td>${BonbuInInfo.MMJ_JEGO}</td> 	 
			<td><input type='number' name='JEGO' value='${BonbuInInfo.MMJ_JEGO}' style='width:90%;' onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="0"/></td> 		 	
		</tr>
	</c:forEach>
	</tbody>
</table>
</c:if>
<c:if test="${TYPE == 'M' }">
<table id="Table_Bonbu">
	<thead>
		<tr>
			<th style="width: 10%;">본 부</th>
			<th style="width: 10%;">센 터</th>
			<th style="width: 15%">접수번호</th>
			<th style="width: 15%">출고수량</th>
			<th style="width: 15%">현 재고</th>
			<th style="width: 15%">적정 재고</th>
			<th style="width: 15%">적정 재고 설정</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="BonbuInInfo" items="${BonbuInInfo}" varStatus="st">
		<tr style="height: 40px;">
			<td>${BonbuInInfo.MCI_BonbuNAME}<input type='hidden' name='CENTER' value='${BonbuInInfo.MCI_CODE}' ></td>
			<td>${BonbuInInfo.MCI_CenterName}</td>
			<td>${BonbuInInfo.DBI_DOCNO}</td>
			<td>
			<c:if test="${BonbuInInfo.DBI_BisSts == 'N' or empty BonbuInInfo.DBI_BisSts}">
				<input type="number" name='OUTCNT' value='${BonbuInInfo.DBI_QTY}' style='width:90%;' onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="0" onchange="TotaolPrice()"/>
			</c:if>
			<c:if test="${BonbuInInfo.DBI_BisSts == 'Y'}">
				${BonbuInInfo.DBI_QTY}
				<input type='hidden' name='OUTCNT' value='${BonbuInInfo.DBI_QTY}' >
			</c:if>
			</td> 	
			<td>${BonbuInInfo.DMJ_JEGO}</td>
			<td>${BonbuInInfo.MMJ_JEGO}</td>
			<td><input type='number' name='JEGO' value='${BonbuInInfo.MMJ_JEGO}' style='width:90%;' onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="0"/></td> 		 		 	
		</tr>
		</c:forEach>
	</tbody>
</table>
</c:if>
</c:if>