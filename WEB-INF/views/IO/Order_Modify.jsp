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
$.datepicker.regional['ko']= {
	prevText:'이전달',
	nextText:'다음달',
	currentText:'오늘',
	yearRange: "2020:2100",
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
	$("#ORDERDATE").datepicker();
});
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function save()
{	
	var ORDERDATE = $("#ORDERDATE").val();
	if(ORDERDATE.length == 0){
		document.getElementById('MSG').innerText = "발주일을 입력해주세요.";
		alert("발주일을 입력해주세요."); 
		return;
	}
	
	var OrderQty = $("#OrderQty").val();
	if(OrderQty.length == 0 || OrderQty == '0'){
		document.getElementById('MSG').innerText = "발주요청수량을 입력해주세요.";
		alert("발주요청수량을 입력해주세요."); 
		return;
	}
	
	var form = document.orderForm;
	form.submit();
}
function cancel() {
	var form = document.orderForm;
	form.action = "Order_DETAIL.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="orderForm" method="post" action="Order_Modify_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="DOCNO" value="${OrderInfo.DBO_DOCNO}" id="DOCNO">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부발주 > 본부발주 수정</div>
			<h2>본부발주 수정</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">접수번호</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.DBO_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주본부</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.MCI_BonbuNAME}&nbsp;${OrderInfo.MCI_CenterName}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주자</td>
							<td style="width: 70%;text-align: left;">
								${OrderInfo.DBO_USERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주일</td>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='ORDERDATE' id='ORDERDATE' value='${OrderInfo.DBO_DATE}' style='width:90%;' readonly='readonly' />
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">비 고</td>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='CMNT' id='CMNT' value='${OrderInfo.DBO_CMNT}' style='width:90%;' />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" id="Table_MLIST">
				<table>
					<thead>
						<tr>
							<th style="width: 20%;">상품코드</th>
							<th style="width: 20%;">대분류</th>
							<th style="width: 20%">중분류</th>
							<th style="width: 20%">소분류</th>
							<th style="width: 20%">발주요청수량</th>
						</tr>
					</thead>
					<tbody  id='mList'>
						<tr style="height: 40px;">
							<td>${OrderInfo.DBO_MODEL}</td>
							<td>${OrderInfo.MMC_LGROUPNAME}</td>
							<td>${OrderInfo.MMC_MGROUP}</td>
							<td>${OrderInfo.MMC_SGROUP}</td> 	
							<td><input type='number' name='OrderQty' id='OrderQty' value='${OrderInfo.DBO_QTY}' style='width:90%;' onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min='0'/></td>	 	 	
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<a href="javascript:save();" class="search grey button">수 정</a>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>