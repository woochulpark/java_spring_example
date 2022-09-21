<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
$.datepicker.regional['ko']= {
	prevText:'이전달',
	nextText:'다음달',
	currentText:'오늘',
	yearRange: "1900:2100",
	monthNames:['년 1월','년 2월','년 3월','년 4월','년 5월','년 6월','년 7월','년 8월','년 9월','년 10월','년 11월','년 12월'],
	monthNamesShort:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	dayNames:['일','월','화','수','목','금','토'],
	dayNamesShort:['일','월','화','수','목','금','토'],
	dayNamesMin:['일','월','화','수','목','금','토'],
	weekHeader:'Wk',
	dateFormat:'yy-mm-dd',
	firstDay:0,
	isRTL:false,
	showMonthAfterYear:true,
	changeMonth: true,
	changeYear: true,
	yearSuffix:'',
	closeText:'취소',
	showButtonPanel: true,
	onClose: function () {
		if ($(window.event.srcElement).hasClass('ui-datepicker-close')) {
			$(this).val('');
		}
	}
};

$.datepicker.setDefaults($.datepicker.regional['ko']); 

//jquery ready
$(function() {
	$("#JEJO").datepicker();
	$("#INDATE").datepicker();
});
function TotaolPrice(){
	var price = $("#INPRICE").val().replace(/,/g,'');
	var cnt = $("#INCNT").val();
	var td = $("#td_total");
	if(price.length > 0 && cnt.length > 0){
		td.text(numberWithCommas(price * cnt));
		console.log("price = " + price + ", cnt = " + cnt);
	}
}
function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
function Modify() {
	var JEJO = document.getElementById("JEJO").value;
	if(JEJO.length == 0){
		alert("제조일자를 입력해주세요."); 
		return;
	}
	var INDATE = document.getElementById("INDATE").value;
	if(INDATE.length == 0){
		alert("입고일자를 입력해주세요."); 
		return;
	}
	var INPRICE = document.getElementById("INPRICE").value;
	if(INPRICE.length == 0){
		document.getElementById("INPRICE").value = '0';
	}
	var INCNT = document.getElementById("INCNT").value;
	if(INCNT.length == 0 || INCNT == '0'){
		alert("입고수량를 입력해주세요."); 
		return;
	}
	document.getElementById("INPRICE").value = INPRICE.replace(/,/g,'');
	var form = document.LogcInForm;
	form.submit();
}
function SetComma(str) { 
	str=str.replace(/,/g,''); 
	console.log(str)
	var retValue = ""; 
	if(isNaN(str)==false)
	{ 
		retValue = numberWithCommas(str);
	}
	return retValue; 
}
function cancel() {
	var form = document.LogcInForm;
	form.action = "LogcIn_DETAIL.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="LogcInForm" method="post" action="LogcIn_Modify_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="DOCNO" value="${LogcInInfo.DIL_DOCNO}" id="DOCNO">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 물류센터 입고 > 물류센터 입고 수정</div>
			<h2>물류센터 입고 수정</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MCI_LOGCNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">접수번호</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">상품코드</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.DIL_MODEL}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">대분류</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MMC_LGROUPNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">중분류</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MMC_MGROUP}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">소분류</td>
							<td style="width: 70%;text-align: left;">
								${LogcInInfo.MMC_SGROUP}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">제조일자</td>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='JEJO' id='JEJO' style='width:90%;' value="${LogcInInfo.DIL_JEJO}" readonly='readonly' />
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">입고일자</td>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='INDATE' id='INDATE' style='width:90%;' value="${LogcInInfo.DIL_INDate}" readonly='readonly' />
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">입고단가</td>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='INPRICE' id='INPRICE' value="<fmt:formatNumber value="${LogcInInfo.DIL_INPRICE}" pattern="#,###" />" style='width:90%;' onkeyUp="this.value=SetComma(this.value)" onfocus="this.value=SetComma(this.value)" onchange="TotaolPrice()" min='0'/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">입고수량</td>
							<td style="width: 70%;text-align: left;">
								<input type='number' name='INCNT' id='INCNT' value="${LogcInInfo.DIL_QTY}" style='width:90%;' onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" onchange="TotaolPrice()" min='0'/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">총금액</td>
							<td style="width: 70%;text-align: left;" id="td_total">
								<fmt:formatNumber value="${LogcInInfo.DIL_INPRICE * LogcInInfo.DIL_QTY}" pattern="#,###" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:if test="${LogcInInfo.DIL_OUTSts == 'N'}">
				<a href="javascript:Modify();" class="search grey button">수정</a>
				</c:if>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
		</div>
	</form:form>
</body>
</html>