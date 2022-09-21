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
	// set datepicker
	$("#StartDay").datepicker();
	//$("#EndDay").datepicker();
	
	$("input[name='BizSts']").change(function(){
		if($("input[name='BizSts']:checked").val() == "N"){
			var Now = new Date(),
			   StrNow = String(Now),
			   nowYear = String(Now.getFullYear()),
			   nowMon = String(Now.getMonth()+1),
			   nowDay = String(Now.getDate());
			if(nowMon.length == 1) {
				nowMon = "0"+nowMon
			}
			var NowToday = nowYear+"-"+nowMon+"-"+nowDay;
			$("#EndDay").val(NowToday);
		}
		else{
			$("#EndDay").val('');
		}
	});
});
function NowDate(){
	
}
function save()
{	

	var check = true;
	var ORDER_LEN = document.getElementById('ORDER').value.length;
	if(ORDER_LEN > 3)
	{
		document.getElementById('MSG').innerText = "정렬 순서는 3자리 까지 숫자만 입력가능 합니다.";
		document.getElementById('ORDER').focus();
		check = false;
	}
	else if(ORDER_LEN == 0)
	{
		document.getElementById('MSG').innerText = "정렬 순서를 입력해주세요.";
		document.getElementById('ORDER').focus();
		check = false;	
	}
	
	if(document.getElementById('TYPE').value == "I" && (document.getElementById('CENTER').value == "" || document.getElementById('CENTER').value == null)){
		document.getElementById('MSG').innerText = "센터 번호를 입력해주세요.";
		document.getElementById('CENTER').focus();
		check = false;
	}
	if(document.getElementById('CNAME').value == "" || document.getElementById('CNAME').value == null){
		document.getElementById('MSG').innerText = "센터명을 입력해주세요.";
		document.getElementById('CNAME').focus();
		check = false;
	}
	if(check){
		var form = document.centerForm;
		form.submit();
	}
	
}
function Center_delete() {
	var del_chk = confirm('센터정보를 삭제하면 시스템 운영에 문제가 생길 수 있습니다. 삭제하시겠습니까?');
	if(del_chk==true){
		var form = document.centerForm;
		form.action = "CENTER_Add_Del.do";
		form.submit();
	}
}
function cancel() {
	var form = document.centerForm;
	form.action = "CenterList_BONBU.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="centerForm" modelAttribute="centerForm" method="post" action="CENTER_Add_Save.do">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="TYPE" value="${TYPE}" id="TYPE">
		<input type="hidden" name="LOGC" value="${LOGC}" id="LOGC">
		<input type="hidden" name="LNAME" value="${LNAME}" id="LNAME">
		<input type="hidden" name="BONBU" value="${BONBU}" id="BONBU">
		<input type="hidden" name="BNAME" value="${BNAME}" id="BNAME">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 본부관리 > ${PAGE}</div>
			<h2>${PAGE}</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터 번호</td>
							<td style="width: 70%;text-align: left;">
								${LOGC}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터명</td>
							<td style="width: 70%;text-align: left;">
								${LNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">본부 번호</td>
							<td style="width: 70%;text-align: left;">
								${BONBU}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">본부 명칭</td>
							<td style="width: 70%;text-align: left;">
								${BNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">센터 번호</td>
							<td style="width: 70%;text-align: left;">
								<c:if test="${TYPE == 'M'}">${CENTER}<input type="hidden" name="CENTER" value="${CENTER}" id="CENTER"></c:if>
								<c:if test="${TYPE == 'I'}"><input type="text" name="CENTER" id="CENTER" value="${CENTER}"  style="width:90%;"/></c:if>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">센터 명칭</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="CNAME" id="CNAME" value="${CNAME}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">사업자 번호</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="BRN" id="BRN" value="${BRN}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">상 호</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="CoName" id="CoName" value="${CoName}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">대표자</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="CEO" id="CEO" value="${CEO}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">주 소</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="Addr" id="Addr" value="${Addr}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">업 태</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="CoType" id="CoType" value="${CoType}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">종 목</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="Jongmog" id="Jongmog" value="${Jongmog}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">담당자</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="PIC" id="PIC" value="${PIC}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">담당 연락처</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="PICHP" id="PICHP" value="${PICHP}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">시작일</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="StartDay" id="StartDay" value="${StartDay}"  style="width:90%;" readonly="readonly"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">종료일</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="EndDay" id="EndDay" value="${EndDay}"  style="width:90%;" readonly="readonly"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">운영여부</td>
							<td style="width: 70%;text-align: left;">
							<input type="radio" name = "BizSts" value = "Y" <c:if test="${BizSts == 'Y' or empty BizSts}">checked="checked"</c:if>> 운영
							<input type="radio" name = "BizSts" value = "N" <c:if test="${BizSts == 'N'}">checked="checked"</c:if>> 종료
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">정렬순서</td>
							<td style="width: 70%;text-align: left;"><input type='number' min='0' max='999' step='1' name="ORDER" id="ORDER" value="${ORDER}"  style="width:90%;" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">비 고</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="REMARK" id="REMARK" value="${REMARK}"  style="width:90%;"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:if test="${TYPE == 'M'}">
					<a href="javascript:Center_delete();" class="search grey button">삭제</a>
				</c:if>
				<a href="javascript:save();" class="search grey button">저장</a>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>