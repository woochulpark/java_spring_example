<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
<script language="javascript">
$(document).ready(function(){
	Comm_LmergeRows(nTbl,0);
	Comm_MmergeRows(nTbl);
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
});
function Comm_LmergeRows(oTbl, iCell) {  
	var argu  = mergeRows.arguments; 
	var cnt  = 1;                                        // rowspan 값 
	var oRow;                                            // 현재 Row Object 
	var oCell;                                            // 현재 Cell Object 
	var iRow;                                            // 이전에 일치했던 Row Index 
	var vPre;                                            // 이전에 일치했던 값 
	var vCur;                                            // 현재 값 
	var bChk  = false;                                    // 처음 일치인지 여부 
	
	if (vCur == '' || vCur == null){
		try { 
		 	for (var i=0; i<oTbl.rows.length; i++) {              // Row Index만큼 Loop 
		   		for (var j=0; j<oTbl.rows[i].cells.length; j++) { // Cell Index 만큼 Loop 
		    		if (iCell == -1 || iCell == j) {                              // 비교할 Cell Index와 현재 Cell Index가 동일하면,, 
		    			vCur = oTbl.rows[i].cells[j].innerHTML; 
		     
		     			if (vPre == vCur && vCur != '' && vCur != null) {                          // 이전값과 현재값이 동일하면,, 
		      				if (bChk == false) {                        // 처음 일치시에만 적용 
		       					iRow = i-1; 
		       					bChk = true; 
		      				} // end of if 
		
		      				cnt++; 
		      				oTbl.rows[iRow].cells[j+1].rowSpan = cnt; 
		      				oTbl.rows[i].deleteCell(j+1); 
		      				oTbl.rows[iRow].cells[j].rowSpan = cnt; 
		      				oTbl.rows[i].deleteCell(j);
		     			} else {                                      // 이전값과 현재값이 다르면,, 
		      				cnt = 1; 
		      				vPre = vCur; 
		      				bChk = false; 
		     			} // end of if 
		
		     			break; 
		    		} // end of if 
		   		} // end of for 
		  	} // end of for 
		} catch (e) { 
		 	alert(e.description); 
		} // end of try{} catch{} 
	}

} // end of function 

function Comm_MmergeRows(oTbl) {
	var argu  = mergeRows.arguments; 
	var cnt  = 1;                                        // rowspan 값 
	var oRow;                                            // 현재 Row Object 
	var oCell;                                            // 현재 Cell Object 
	var iRow;                                            // 이전에 일치했던 Row Index 
	var iCell;
	var vPre;                                            // 이전에 일치했던 값 
	var vCur;                                            // 현재 값 
	var bChk  = false;                                    // 처음 일치인지 여부
	if (vCur == '' || vCur == null){
		try { 
		 	for (var i=1; i<oTbl.rows.length; i++) {              // Row Index만큼 Loop 
		 		console.log("CELL LEN " + oTbl.rows[i].cells.length);
		 		if(oTbl.rows[i].cells.length == 6)
		 			iCell = 2;
		 		else
		 			iCell = 0;
		   		for (var j=0; j<oTbl.rows[i].cells.length; j++) { // Cell Index 만큼 Loop 
		    		if (iCell == -1 || iCell == j) {                              // 비교할 Cell Index와 현재 Cell Index가 동일하면,, 
		    			vCur = oTbl.rows[i].cells[j].innerHTML; 
		    			console.log("ROW = " + i + ", CELL = " + j + ", vCur = " + vCur);
		     			if (vPre == vCur && vCur != '' && vCur != null) {                          // 이전값과 현재값이 동일하면,, 
		      				if (bChk == false) {                        // 처음 일치시에만 적용 
		       					iRow = i-1; 
		       					bChk = true; 
		      				} // end of if 
		      				cnt++; 
		      				if(oTbl.rows[iRow].cells.length == 6){
		      					oTbl.rows[iRow].cells[2+1].rowSpan = cnt; 
		      					oTbl.rows[iRow].cells[2].rowSpan = cnt; 
		      				}
		      				else{
		      					oTbl.rows[iRow].cells[j+1].rowSpan = cnt; 
		      					oTbl.rows[iRow].cells[j].rowSpan = cnt; 
		      				}
		      				if(oTbl.rows[i].cells.length == 6){
		      					oTbl.rows[i].deleteCell(2+1); 
		      					oTbl.rows[i].deleteCell(2); 
		      				}
		      				else{
		      					oTbl.rows[i].deleteCell(0+1); 
		      					oTbl.rows[i].deleteCell(0)
		      				}
		      				console.log("vPre = " + vPre + ", vCur = " + vCur);
		     			} else {                                      // 이전값과 현재값이 다르면,, 
		      				cnt = 1; 
		      				vPre = vCur; 
		      				console.log("vPre = " + vPre);
		      				bChk = false; 
		     			} // end of if 
		     			break; 
		    		} // end of if 
		   		} // end of for 
		  	} // end of for 
		} catch (e) { 
		 	alert(e.description); 
		} // end of try{} catch{} 
	}
} // end of function 
function search()
{
	var form = document.codeForm;
	form.action = "codelist_search.do";
	form.submit();
}
function Lcode(L_CODE)
{
	document.getElementById("LCODE").value = L_CODE;
	document.getElementById("MCODE").value = "";
	document.getElementById("SCODE").value = "";
	var form = document.codeForm;
	form.action = "codelist_Lcode.do";
	form.submit();
}
function Mcode(L_CODE, M_CODE)
{
	document.getElementById("LCODE").value = L_CODE;
	document.getElementById("MCODE").value = M_CODE;
	document.getElementById("SCODE").value = "";
	var form = document.codeForm;
	form.action = "codelist_Mcode.do";
	form.submit();
}
function Lcode_Add(L_CODE)
{
	document.getElementById("LCODE").value = L_CODE;
	document.getElementById("MCODE").value = "";
	document.getElementById("SCODE").value = "";
	var form = document.codeForm;
	form.action = "LCode_Add.do";
	form.submit();
}
function Mcode_Add(L_CODE, M_CODE)
{
	document.getElementById("LCODE").value = L_CODE;
	document.getElementById("MCODE").value = M_CODE;
	document.getElementById("SCODE").value = "";
	var form = document.codeForm;
	form.action = "MCode_Add.do";
	form.submit();
}
function Scode_Add(L_CODE, M_CODE, S_CODE)
{
	document.getElementById("LCODE").value = L_CODE;
	document.getElementById("MCODE").value = M_CODE;
	document.getElementById("SCODE").value = S_CODE;
	var form = document.codeForm;
	form.action = "SCode_Add.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="codeForm" modelAttribute="codeForm" method="post" action="codelist_search.do">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="LCODE" value="" id="LCODE">
		<input type="hidden" name="MCODE" value="" id="MCODE">
		<input type="hidden" name="SCODE" value="" id="SCODE">
		<!-- START TOPBAR -->
		<!-- menu start -->
		<jsp:include page="/menu_top.do">
			<jsp:param name="user_id" value="${user_id}" />
		</jsp:include>
		<!-- menu end -->
		<!-- END TOPBAR -->

		<!-- START WRAPPER -->
		<div class="wrapper group">
			<!-- START HEADER -->
			<!-- menu start -->
			<jsp:include page="/menu_list.do">
				<jsp:param name="user_id" value="${user_id}" />
			</jsp:include>
			<!-- menu end -->
			<!-- END HEADER -->

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 공통 코드 관리</div>
			<h2>공통 코드 관리</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 50%;">
					<tr>
						<td style="text-align: center;">구 분</td>
						<td>
							<select style="width: 90%;" name="CodeKind">
								<option value="L" <c:if test="${empty CodeKind or CodeKind == 'L'}">selected="selected"</c:if>>&nbsp;대분류&nbsp;</option>
								<option value="M" <c:if test="${CodeKind == 'M'}">selected="selected"</c:if>>&nbsp;중분류&nbsp;</option>
								<option value="S" <c:if test="${CodeKind == 'S'}">selected="selected"</c:if>>&nbsp;소분류&nbsp;</option>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">코드명</td>
						<td>
							<input type="text" name="CNAME" id="CNAME" value="${CNAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
						<!-- START TABLE -->
			<div style="text-align:right;">
				<a href="javascript:Lcode_Add('')" class="search grey button">대분류추가</a>
			</div>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 20%;">대분류</th>
							<th style="width: 10%;">확 인</th>
							<th style="width: 20%">중분류</th>
							<th style="width: 10%">확 인</th>
							<th style="width: 20%">소분류</th>
							<th style="width: 25%">비 고</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${CodeListSize > 0 }">
					<c:forEach var="codeList" items="${CodeList}" varStatus="st">
						<tr style="height: 40px;">
							<c:if test="${LCODE == codeList.MCC_L_CODE}">
							<td style="background-color: #CEFBC9"><a href="javascript:Lcode_Add('${codeList.MCC_L_CODE}');">&nbsp;${codeList.MCC_L_CODE}-${codeList.MCC_L_NAME}</a></td>
							<td style="background-color: #CEFBC9"><a href="javascript:Mcode_Add('${codeList.MCC_L_CODE}','');" class="search grey button" style="padding: 10px;margin: 5px;background-color: #0054FF">하위추가</a></td>
							</c:if>
							<c:if test="${LCODE != codeList.MCC_L_CODE}">
							<td><a href="javascript:Lcode_Add('${codeList.MCC_L_CODE}');">&nbsp;${codeList.MCC_L_CODE}-${codeList.MCC_L_NAME}</a></td>
							<td><a href="javascript:Lcode('${codeList.MCC_L_CODE}');" class="search grey button" style="padding: 10px;margin: 5px">하위확인</a></td>
							</c:if>
							<c:if test="${not empty MCODE and MCODE == codeList.MCC_M_CODE}">
							<td style="background-color: #CEFBC9"><c:if test="${not empty codeList.MCC_M_CODE and codeList.MCC_M_CODE != ' '}"><a href="javascript:Mcode_Add('${codeList.MCC_L_CODE}','${codeList.MCC_M_CODE}');">&nbsp;${codeList.MCC_M_CODE}-${codeList.MCC_M_NAME}</a></c:if></td>
							<td style="background-color: #CEFBC9"><c:if test="${not empty codeList.MCC_M_CODE and codeList.MCC_M_CODE != ' '}"><a href="javascript:Scode_Add('${codeList.MCC_L_CODE}','${codeList.MCC_M_CODE}','');" class="search grey button" style="padding: 10px;margin: 5px;background-color: #0054FF">하위추가</a></c:if></td>
							</c:if>
							<c:if test="${empty MCODE or MCODE != codeList.MCC_M_CODE}">
							<td><c:if test="${not empty codeList.MCC_M_CODE and codeList.MCC_M_CODE != ' '}"><a href="javascript:Mcode_Add('${codeList.MCC_L_CODE}','${codeList.MCC_M_CODE}');">&nbsp;${codeList.MCC_M_CODE}-${codeList.MCC_M_NAME}</a></c:if></td>
							<td><c:if test="${not empty codeList.MCC_M_CODE and codeList.MCC_M_CODE != ' '}"><a href="javascript:Mcode('${codeList.MCC_L_CODE}','${codeList.MCC_M_CODE}');" class="search grey button" style="padding: 10px;margin: 5px;">하위확인</a></c:if></td>
							</c:if>
							<td><c:if test="${not empty codeList.MCC_S_CODE and codeList.MCC_S_CODE != ' '}"><a href="javascript:Scode_Add('${codeList.MCC_L_CODE}','${codeList.MCC_M_CODE}','${codeList.MCC_S_CODE}');">&nbsp;${codeList.MCC_S_CODE}-${codeList.MCC_S_NAME}</a></c:if></td>
							<td><c:if test="${not empty codeList.MCC_S_CODE and codeList.MCC_S_CODE != ' '}"><a href="javascript:Scode_Add('${codeList.MCC_L_CODE}','${codeList.MCC_M_CODE}','${codeList.MCC_S_CODE}');">${codeList.MCC_CODE_REMARK}</a></c:if></td>
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>
</html>