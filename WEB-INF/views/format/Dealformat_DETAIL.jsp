<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script src="js/jquery-ui.js" language="javascript"></script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
<script language="javascript">
$(document).ready(function(){
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
});
function cancel() {
	var form = document.formatForm;
	form.action = "Dealformat_List.do";
	form.submit();
}
function DealCfm(STSTYPE, STS)
{
	document.getElementById("STSTYPE").value = STSTYPE;
	document.getElementById("STS").value = STS;
	var form = document.formatForm;
	form.action = "DealCfm.do";
	form.submit();
}
function DealPrint(){
	var DOCNO = document.getElementById("DOCNO").value;
	$.ajax({
		type:"GET",
		url:"Ajax_DealExcel.do",
		data: "DOCNO=" + DOCNO,
		success:function(data){
			if(data == '1'){
				location.href = 'download.do?sysFile=' + DOCNO + '.xlsx&orgFile=거래내역서_' + DOCNO + '.xlsx&PATH=EXCEL';
			}
			else if(data == '-1'){
				alert('발급정보를 가져오지 못했습니다.');
			}
			else if(data == '-2'){
				alert('출력 권한이 없습니다.');
			}
			else if(data == '-3'){
				alert('거래내역을 가져오지 못했습니다.');
			}
			else if(data == '-4'){
				alert('알수없는 에러! 다시 시도해주세요.');
			}
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="formatForm" method="post" action="Dealformat_List_Search.do">
		<input type="hidden" name="menuGroup" value="menu_customer" id="menuGroup">
		<input type="hidden" name="DOCNO" id="DOCNO" value="${DealFormatInfo.DDT_DOCNO}">
		<input type="hidden" name="PAGE" id="PAGE" value="1" >
		<input type="hidden" name="STS" id="STS">
		<input type="hidden" name="STSTYPE" id="STSTYPE">
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

			<div class="subtitle" style="text-align: right;">서식자료 > 거래내역서 상세</div>
			<h2>거래내역서 상세</h2>
			
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 30%;">내역서 번호</th>
							<td style="width: 70%;text-align: left;" colspan="3">
								${DealFormatInfo.DDT_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">발급일자</th>
							<td style="width: 70%;text-align: left;" colspan="3">
								${DealFormatInfo.DDT_DATE}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">조회기간</th>
							<td style="width: 70%;text-align: left;">
								${DealFormatInfo.DDT_STARTDAY}&nbsp;~&nbsp;${DealFormatInfo.DDT_ENDDAY}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 25%;">출고본부</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${DealFormatInfo.OUTCENTERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">등록번호</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${DealFormatInfo.OUTBRN}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">상 호</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.OUTCONAME}
							</td>
							
							<th style="width: 25%;">성 명</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.OUTCEO}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">주 소</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${DealFormatInfo.OUTADDR}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">업 태</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.OUTCOTYPE}
							</td>
							<th style="width: 25%;">종 목</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.OUTJM}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 25%;">입고본부</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${DealFormatInfo.INCENTERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">등록번호</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${DealFormatInfo.INBRN}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">상 호</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.INCONAME}
							</td>
							<th style="width: 25%;">성 명</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.INCEO}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">주 소</th>
							<td style="width: 75%;text-align: left;" colspan="3">
								${DealFormatInfo.INADDR}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">업 태</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.INCOTYPE}
							</td>
							<th style="width: 25%;">종 목</th>
							<td style="width: 25%;text-align: left;">
								${DealFormatInfo.INJM}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="scroll_table">
				<table id="nTbl">
					<thead>
						<tr>
							<th style="width: 16%;">입고 승인여부</th>
							<th style="width: 17%;">입고 승인자</th>
							<th style="width: 17%;">입고 승인일자</th>
							<th style="width: 16%;">출고 승인여부</th>
							<th style="width: 17%;">출고 승인자</th>
							<th style="width: 17%;">출고 승인일자</th>
						</tr>
					</thead>
					<tbody  id='mList'>
						<tr style="height: 40px;">
							<c:choose>
								<c:when test="${DealFormatInfo.DDT_INSTS == '0'}">
								<td>미승인</td>
								<td>-</td>
								<td>-</td> 	
								</c:when>
								<c:when test="${DealFormatInfo.DDT_INSTS == '1'}">
								<td>승인</td>
								<td>${DealFormatInfo.INSTSUSERNM}</td>
								<td>${DealFormatInfo.DDT_INSTSDATE}</td> 	
								</c:when>
								<c:otherwise>
								<td>반려</td>
								<td>${DealFormatInfo.INSTSUSERNM}</td>
								<td>${DealFormatInfo.DDT_INSTSDATE}</td> 	
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${DealFormatInfo.DDT_OUTSTS == '0'}">
								<td>미승인</td>
								<td>-</td>
								<td>-</td> 	
								</c:when>
								<c:when test="${DealFormatInfo.DDT_OUTSTS == '1'}">
								<td>승인</td>
								<td>${DealFormatInfo.OUTSTSUSERNM}</td>
								<td>${DealFormatInfo.DDT_OUTSTSDATE}</td> 	
								</c:when>
								<c:otherwise>
								<td>반려</td>
								<td>${DealFormatInfo.OUTSTSUSERNM}</td>
								<td>${DealFormatInfo.DDT_OUTSTSDATE}</td> 	
								</c:otherwise>
							</c:choose>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 30%;">전달 및 특이사항</th>
							<td style="width: 70%;text-align: left;" colspan="3">
								<textarea class="dealaddr" name="DDT_CMNT">${DealFormatInfo.DDT_CMNT}</textarea>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div style="text-align: center;">
				<c:if test="${DealFormatInfo.DDT_INSTS == '0' and DealFormatInfo.DDT_OUTSTS != '2' and (UserInfo.MUT_POSITION == '001' or ((UserInfo.MUT_POSITION == '003' or UserInfo.MUT_POSITION == '004') and DealFormatInfo.DDT_INCENTER == UserInfo.MUT_CENTER))}">
				<a href="javascript:DealCfm('IN','1');" class="search grey button">입고본부 승인</a>
				<a href="javascript:DealCfm('IN','2');" class="search grey button">입고본부 반려</a>
				</c:if>
				<c:if test="${DealFormatInfo.DDT_OUTSTS == '0' and DealFormatInfo.DDT_INSTS != '2' and (UserInfo.MUT_POSITION == '001' or ((UserInfo.MUT_POSITION == '003' or UserInfo.MUT_POSITION == '004') and DealFormatInfo.DDT_OUTCENTER == UserInfo.MUT_CENTER))}">
				<a href="javascript:DealCfm('OUT','1');" class="search grey button">출고본부 승인</a>
				<a href="javascript:DealCfm('OUT','2');" class="search grey button">출고본부 반려</a>
				</c:if>
				<c:if test="${DealFormatInfo.DDT_OUTSTS == '1' and DealFormatInfo.DDT_INSTS == '1' and (UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002' or ((UserInfo.MUT_POSITION == '003' or UserInfo.MUT_POSITION == '004') and (DealFormatInfo.DDT_OUTCENTER == UserInfo.MUT_CENTER or DealFormatInfo.DDT_INCENTER == UserInfo.MUT_CENTER)))}">
				<a href="javascript:DealPrint();" class="search grey button">출 력</a>
				</c:if>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
		</div>
	</form:form>
</body>
</html>