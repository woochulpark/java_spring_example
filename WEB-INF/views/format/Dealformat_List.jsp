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
$(document).ready(function(){
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
	$("#StartDay").datepicker();
	$("#EndDay").datepicker();
	$("#POPStartDay").datepicker();
	$("#POPEndDay").datepicker();
});
function search()
{
	var form = document.formatForm;
	form.submit();
}
function getBONBU(obj, TYPE){
	var center = "";
	var td = "";
	if(TYPE == "OUT"){
		center = "CENTER";
		td = "td_B";
	}
	else{ 
		center = "INCENTER";
		td = "td_INB";
	}
	$("#" + center).empty();
	var option = "<option value=''>선택</option>";
    $("#" + center).append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_MOVEBONBU.do",
		data: "TYPE=" + TYPE + "&LOGC="+obj.value,
		success:function(html){
			$("#" + td).empty();
			$("#" + td).append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getCENTER(obj, TYPE){
	var td = "";
	var id = "";
	if(TYPE == "OUT"){
		td = "td_C";
		id = "LOGC";
	}
	else{ 
		td = "td_INC";
		id = "INLOGC";
	}
	var LOGC = document.getElementById(id).value;
	$.ajax({
		type:"GET",
		url:"Ajax_MOVECENTER.do",
		data: "TYPE=" + TYPE + "&LOGC="+LOGC+"&BONBU="+obj.value,
		success:function(html){
			$("#" + td).empty();
			$("#" + td).append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getPOPBONBU(obj, TYPE){
	var center = "";
	var td = "";
	if(TYPE == "OUT"){
		center = "POPCENTER";
		td = "pop_OutTd_B";
	}
	else{ 
		center = "POPINCENTER";
		td = "pop_InTd_B";
	}
	$("#" + center).empty();
	var option = "<option value=''>선택</option>";
    $("#" + center).append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_POPBONBU.do",
		data: "TYPE=" + TYPE + "&LOGC="+obj.value,
		success:function(html){
			$("#" + td).empty();
			$("#" + td).append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getPOPCENTER(obj, TYPE){
	var td = "";
	var id = "";
	if(TYPE == "OUT"){
		td = "pop_OutTd_C";
		id = "POPLOGC";
	}
	else{ 
		td = "pop_InTd_C";
		id = "POPINLOGC";
	}
	var LOGC = document.getElementById(id).value;
	$.ajax({
		type:"GET",
		url:"Ajax_POPCENTER.do",
		data: "TYPE=" + TYPE + "&LOGC="+LOGC+"&BONBU="+obj.value,
		success:function(html){
			$("#" + td).empty();
			$("#" + td).append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function PTYPE_OnOff()
{
	var PTYPEValue = $('input:radio[name="PTYPE"]:checked').val();
	if(PTYPEValue == '0')
		$("#PSIZE").attr("readonly",true); 
	else
		$("#PSIZE").removeAttr("readonly");
}
function fn_paging(PAGE)
{
	document.getElementById("PAGE").value = PAGE;
	var form = document.formatForm;
	form.submit();
}
function Ajax_DealSearch(PAGE){
	var POPStartDay = $("#POPStartDay").val();
	var POPEndDay = $("#POPEndDay").val();
	if(POPStartDay.length == 0){
		alert("출고일자를 선택해주세요.\n출고일자는 거래내역서에 거래내역기간이 됩니다.");
		return;
	}
	if(POPEndDay.length == 0){
		alert("출고일자를 선택해주세요.\n출고일자는 거래내역서에 거래내역기간이 됩니다.");
		return;
	}
	var OUTLOGC = '';
	var OUTBONBU = '';
	var OUTCENTER = '';
	var INLOGC = '';
	var INBONBU = '';
	var INCENTER = '';
	if ($('#POPLOGC').length)
		OUTLOGC = $("#POPLOGC option:selected").val();
	if ($('#POPBONBU').length)
		OUTBONBU = $("#POPBONBU option:selected").val();
	if ($('#POPCENTER').length)
		OUTCENTER = $("#POPCENTER option:selected").val();
	if ($('#POPINLOGC').length)
		INLOGC = $("#POPINLOGC option:selected").val();
	if ($('#POPINBONBU').length)
		INBONBU = $("#POPINBONBU option:selected").val();
	if ($('#POPINCENTER').length)
		INCENTER = $("#POPINCENTER option:selected").val();
	var POPCTYPE = '';
	if ($("input[name='POPCTYPE']").length){
		POPCTYPE = $('input:radio[name="POPCTYPE"]:checked').val();
		if(POPCTYPE == 'Y'){
			INLOGC = OUTLOGC;
			INBONBU = OUTBONBU;
			INCENTER = OUTCENTER;
			OUTLOGC = '';
			OUTBONBU = '';
			OUTCENTER = '';
			if($('#POPINCENTER').length){
				OUTCENTER = $("#POPINCENTER option:selected").val();
			}
		}
	}
	var POP_MNAME = $("#POP_MNAME").val();
	var PTYPE = '1';
	if ($("input[name='PTYPE']").length)
		PTYPE = $('input:radio[name="PTYPE"]:checked').val();
	var PSIZE = $("#PSIZE").val();
	var SendData =  "POPCTYPE="+POPCTYPE+"&POPStartDay="+POPStartDay+"&POPEndDay="+POPEndDay+
					"&POPINLOGC="+INLOGC+"&POPINBONBU="+INBONBU+"&POPINCENTER="+INCENTER+
					"&POPLOGC="+OUTLOGC+"&POPBONBU="+OUTBONBU+"&POPCENTER="+OUTCENTER+
					"&POP_MNAME="+POP_MNAME+"&PTYPE="+PTYPE+"&PSIZE="+PSIZE+
					"&PAGE=" + PAGE;
	console.log("SendData = " + SendData);
	$.ajax({
		type:"POST",
		url:"Ajax_DealSearch.do",
		data: SendData,
		success:function(html){
			$("#pop_table").empty();
			$("#pop_table").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getMLIST(obj){
	var POP_LOGC = '';
	var TagName = $('#MODEL_LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		POP_LOGC = $("#MODEL_LOGC option:selected").val();
	}
	else{
		POP_LOGC = $("#MODEL_LOGC").val();
	}
	$("#SCODEList").empty();
	var option = "<option value=''>선택</option>";
    $('#SCODEList').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_MLIST.do",
		data: "LOGC="+POP_LOGC+"&LCODE="+obj.value,
		success:function(html){
			$("#td_M").empty();
			$("#td_M").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});
}
function getSLIST(obj){
	var POP_LOGC = '';
	var TagName = $('#MODEL_LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		POP_LOGC = $("#MODEL_LOGC option:selected").val();
	}
	else{
		POP_LOGC = $("#MODEL_LOGC").val();
	}
	var LCODE = document.getElementById("LCODEList").value;
	$.ajax({
		type:"GET",
		url:"Ajax_SLIST.do",
		data: "LOGC="+POP_LOGC+"&LCODE="+LCODE+"&MCODE="+obj.value,
		success:function(html){
			$("#td_S").empty();
			$("#td_S").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function Ajax_Order_search(PAGE){
	var LCODEList = $("#LCODEList option:selected").val();
	var MCODEList = $("#MCODEList option:selected").val();
	var SCODEList = $("#SCODEList option:selected").val();
	var POP_LOGC = '';
	var TagName = $('#MODEL_LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		POP_LOGC = $("#MODEL_LOGC option:selected").val();
	}
	else{
		POP_LOGC = $("#MODEL_LOGC").val();
	}
	var POP_MNAME = $("#MODEL_MNAME").val();
	var MCODENAME = $("#MCODENAME").val();
	var SCODENAME = $("#SCODENAME").val();
	$.ajax({
		type:"POST",
		url:"Ajax_Order_search.do",
		data: "LCODEList="+LCODEList+"&MCODEList="+MCODEList+"&SCODEList="+SCODEList+"&LOGC="+POP_LOGC+"&MNAME="+POP_MNAME+"&MCODENAME="+MCODENAME+"&SCODENAME="+SCODENAME+"&PAGE=" + PAGE,
		success:function(html){
			$("#pop_table2").empty();
			$("#pop_table2").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function pop_Model(){
	var $el = $("#layer_model");
    var isDim = $el.prev().hasClass('dimBg');//dimmed 레이어를 감지하기 위한 boolean 변수

    isDim ? $('#dim-layer2').fadeIn() : $el.fadeIn();

    var $elWidth = ~~($el.outerWidth()),
        $elHeight = ~~($el.outerHeight()),
        docWidth = $(document).width(),
        docHeight = $(document).height();

    // 화면의 중앙에 레이어를 띄운다.
    if ($elHeight < docHeight || $elWidth < docWidth) {
        $el.css({
            marginTop: -$elHeight /2,
            marginLeft: -$elWidth/2
        })
    } else {
        $el.css({top: 0, left: 0});
    }

    $el.find("#BTN_POP_CLOSE").click(function(){    	
        isDim ? $('#dim-layer2').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });
}
function AddModel(MCODE, LGROUP, MGROUP, SGROUP){
	$("#POP_MNAME").val(MCODE);
	$('#dim-layer2').fadeOut();
}
function DOC_Add(){
	var $el = $("#layer_format");
    var isDim = $el.prev().hasClass('dimBg');//dimmed 레이어를 감지하기 위한 boolean 변수

    isDim ? $('#dim-layer').fadeIn() : $el.fadeIn();

    var $elWidth = ~~($el.outerWidth()),
        $elHeight = ~~($el.outerHeight()),
        docWidth = $(document).width(),
        docHeight = $(document).height();

    // 화면의 중앙에 레이어를 띄운다.
    if ($elHeight < docHeight || $elWidth < docWidth) {
        $el.css({
            marginTop: -$elHeight /2,
            marginLeft: -$elWidth/2
        })
    } else {
        $el.css({top: 0, left: 0});
    }
    $el.find("#BTN_POP_CLOSE").click(function(){    	
        isDim ? $('#dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });
}
function DocSave(){
	if($('input:checkbox[name=Deal_CHK]:checked').length > 0){
		var confirm_Save = confirm("거래내역서를 발급하시겠습니까?");
    	if ( confirm_Save == true ) {
    		var form = document.formatForm;
    		form.action = "Dealformat_Add_Save.do";
    		form.submit();
    	}
	}
	else{
		$('#dim-layer').fadeOut();
	}
}
function allCheck(){
	if($("#pop_all").is(":checked")){
        $(".Deal_CHK").prop("checked", true);
    }
    else{
        $(".Deal_CHK").prop("checked", false);
    }
}
function CTYPE_OnOff()
{
	var CTYPEValue = $('input:radio[name="POPCTYPE"]:checked').val();
	if(CTYPEValue == 'Y'){
		$("#pop_Bnm1").text("입고본부");
		$("#pop_Bnm2").text("출고본부");
	}
	else{
		$("#pop_Bnm1").text("출고본부");
		$("#pop_Bnm2").text("입고본부");
	}
}
function DealDetail(DOCNO)
{
	document.getElementById("DOCNO").value = DOCNO;
	var form = document.formatForm;
	form.action = "Dealformat_DETAIL.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="formatForm" method="post" action="Dealformat_List_Search.do">
		<input type="hidden" name="menuGroup" value="menu_customer" id="menuGroup">
		<input type="hidden" name="DOCNO" value="" id="DOCNO">
		<input type="hidden" name="PAGE" value="1" id="PAGE">
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

			<div class="subtitle" style="text-align: right;">서식자료 > 거래내역서</div>
			<h2>거래내역서</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 80%;">
					<tr>
						<td style="text-align: center;width: 25%;">일 자</td>
						<td colspan="3" style="width: 75%;">
							<input type="text" name="StartDay" id="StartDay" value="${StartDay}"  style="width:40%;" readonly="readonly">
							&nbsp;~&nbsp;
							<input type="text" name="EndDay" id="EndDay" value="${EndDay}"  style="width:40%;" readonly="readonly">
						</td>
					</tr>
					<c:choose>
					<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
					<tr>
						<td style="width: 25%;text-align: center;">출고본부</td>
						<td style="width: 25%">
							<select style="width: 90%;" name="LOGC" id="LOGC"  onchange="getBONBU(this, 'OUT')">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<c:if test="${LOGC == LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}" selected="selected">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
									<c:if test="${LOGC != LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
						<td id = 'td_B' style="width: 25%">
							<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this, 'OUT')">
								<option value="">선택</option>
								<c:if test="${not empty BONBULIST}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
									<c:if test="${BONBU == BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}" selected="selected">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
									<c:if test="${BONBU != BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td id = 'td_C' style="width: 25%">
							<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:if test="${not empty CENTERLIST}">
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
									<c:if test="${CENTER == CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}" selected="selected">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
									<c:if test="${CENTER != CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					<tr>
						<td style="width: 25%;text-align: center;">입고본부</td>
						<td style="width: 25%">
							<select style="width: 90%;" name="INLOGC" id="INLOGC"  onchange="getBONBU(this, 'IN')">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<c:if test="${INLOGC == LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}" selected="selected">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
									<c:if test="${INLOGC != LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
						<td id = 'td_INB' style="width: 25%">
							<select style="width: 90%;" name="INBONBU" id = "INBONBU" onchange="getCENTER(this, 'IN')">
								<option value="">선택</option>
								<c:if test="${not empty INBONBULIST}">
								<c:forEach var="BONBULIST" items="${INBONBULIST}" varStatus="st">
									<c:if test="${INBONBU == BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}" selected="selected">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
									<c:if test="${INBONBU != BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td id = 'td_INC' style="width: 25%">
							<select style="width: 90%;" name="INCENTER" id = "INCENTER">
								<option value="">선택</option>
								<c:if test="${not empty INCENTERLIST}">
								<c:forEach var="CENTERLIST" items="${INCENTERLIST}" varStatus="st">
									<c:if test="${INCENTER == CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}" selected="selected">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
									<c:if test="${INCENTER != CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					</c:when>
					<c:when test="${UserInfo.MUT_POSITION == '003'}">
					<tr>
						<td style="text-align: center;">구 분</td>
						<td colspan="3">
							<input type="radio" name = "CTYPE" value = "0" <c:if test="${empty CTYPE or CTYPE == '0'}">checked="checked"</c:if>> 전체
							<input type="radio" name = "CTYPE" value = "1" <c:if test="${CTYPE == '1'}">checked="checked"</c:if>> 입고
							<input type="radio" name = "CTYPE" value = "2" <c:if test="${CTYPE == '2'}">checked="checked"</c:if>> 출고
						</td>
					</tr>
					<tr>
						<td style="width: 25%;text-align: center;">본 부</td>
						<td colspan="3">
							${UserInfo.MCI_LOGCNAME}&nbsp;${UserInfo.MCI_BonbuNAME}&nbsp;
							<select style="width: 50%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
								<option value="${CENTERLIST.MCI_Center}" <c:if test="${CTYPE == '0' and CENTER == CENTERLIST.MCI_Center or CTYPE == '1' and INCENTER == CENTERLIST.MCI_Center or CTYPE == '2' and CENTER == CENTERLIST.MCI_Center }">selected="selected"</c:if>>${CENTERLIST.MCI_CenterName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					</c:when>
					<c:otherwise>
					<tr>
						<td style="text-align: center;">구 분</td>
						<td colspan="3">
							<input type="radio" name = "CTYPE" value = "0" <c:if test="${empty CTYPE or CTYPE == '0'}">checked="checked"</c:if>> 전체
							<input type="radio" name = "CTYPE" value = "1" <c:if test="${CTYPE == '1'}">checked="checked"</c:if>> 입고
							<input type="radio" name = "CTYPE" value = "2" <c:if test="${CTYPE == '2'}">checked="checked"</c:if>> 출고
						</td>
					</tr>
					<tr>
						<td style="width: 25%;text-align: center;">본 부</td>
						<td colspan="3">
							${UserInfo.MCI_LOGCNAME}&nbsp;${UserInfo.MCI_BonbuNAME}&nbsp;${UserInfo.MCI_CenterName}
						</td>
					</tr>
					</c:otherwise>
					</c:choose>
					<tr>
						<td style="text-align: center;width: 25%;">입고 승인여부</td>
						<td colspan="3" style="width: 75%;">
							<input type="radio" name = "INSTSTYPE" value = "" <c:if test="${empty INSTSTYPE or INSTSTYPE == ''}">checked="checked"</c:if>> 전체
							<input type="radio" name = "INSTSTYPE" value = "0" <c:if test="${INSTSTYPE == '0'}">checked="checked"</c:if>> 미승인
							<input type="radio" name = "INSTSTYPE" value = "1" <c:if test="${INSTSTYPE == '1'}">checked="checked"</c:if>> 승인
							<input type="radio" name = "INSTSTYPE" value = "2" <c:if test="${INSTSTYPE == '2'}">checked="checked"</c:if>> 반려
						</td>
					</tr>
					<tr>
						<td style="text-align: center;width: 25%;">출고 승인여부</td>
						<td colspan="3" style="width: 75%;">
							<input type="radio" name = "OUTSTSTYPE" value = "" <c:if test="${empty OUTSTSTYPE or OUTSTSTYPE == ''}">checked="checked"</c:if>> 전체
							<input type="radio" name = "OUTSTSTYPE" value = "0" <c:if test="${OUTSTSTYPE == '0'}">checked="checked"</c:if>> 미승인
							<input type="radio" name = "OUTSTSTYPE" value = "1" <c:if test="${OUTSTSTYPE == '1'}">checked="checked"</c:if>> 승인
							<input type="radio" name = "OUTSTSTYPE" value = "2" <c:if test="${OUTSTSTYPE == '2'}">checked="checked"</c:if>> 반려
						</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<c:if test="${level != '005'}">
			<div style="text-align:right;">
				<a href="javascript:DOC_Add()" class="search grey button">발급하기</a>
			</div>
			</c:if>
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 10%;">발급일자</th>
							<th style="width: 10%;">내역서 번호</th>
							<th style="width: 20%;">조회기간</th>
							<th style="width: 15%;">입고본부</th>
							<th style="width: 15%;">출고본부</th>
							<th style="width: 10%;">입고확인</th>
							<th style="width: 10%;">출고확인</th>
							<th style="width: 10%">특이사항</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${DealFormatListSize > 0 }">
					<c:forEach var="DealFormatList" items="${DealFormatList}" varStatus="st">
						<tr style="height: 40px;" onclick="javascript:DealDetail('${DealFormatList.DDT_DOCNO}')">
							<td style="padding: 2px;">${DealFormatList.DDT_DATE}</td>
							<td style="padding: 2px;">${DealFormatList.DDT_DOCNO}</td>
							<td style="padding: 2px;">${DealFormatList.DDT_STARTDAY}&nbsp;~&nbsp;${DealFormatList.DDT_ENDDAY}</td>
							<td style="padding: 2px;">${DealFormatList.INCENTER}</td> 	
							<td style="padding: 2px;">${DealFormatList.OUTCENTER}</td>
							<td style="padding: 2px;">
								<c:choose>
									<c:when test="${DealFormatList.DDT_INSTS == '0'}">
									미승인
									</c:when>
									<c:when test="${DealFormatList.DDT_INSTS == '1'}">
									승인
									</c:when>
									<c:otherwise>
									반려
									</c:otherwise>
								</c:choose>
							</td> 	 
							<td style="padding: 2px;">
								<c:choose>
									<c:when test="${DealFormatList.DDT_OUTSTS == '0'}">
									미승인
									</c:when>
									<c:when test="${DealFormatList.DDT_OUTSTS == '1'}">
									승인
									</c:when>
									<c:otherwise>
									반려
									</c:otherwise>
								</c:choose>
							</td>
							<td style="padding: 2px;">${DealFormatList.DDT_CMNT}</td>
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
            <c:if test="${pagination.curRange ne 1 }">
            <a href="#" onClick="fn_paging(1)">[처음]</a> 
            </c:if>
            <c:if test="${pagination.curPage ne 1}">
            <a href="#" onClick="fn_paging('${pagination.prevPage }')">[이전]</a> 
            </c:if>
            <c:forEach var="pageNum" begin="${pagination.startPage }" end="${pagination.endPage }">
            <c:choose>
            <c:when test="${pageNum eq  pagination.curPage}">
            <span style="font-weight: bold;"><a href="#" onClick="fn_paging('${pageNum }')">${pageNum }</a></span> 
            </c:when>
            <c:otherwise>
            <a href="#" onClick="fn_paging('${pageNum }')">${pageNum }</a> 
            </c:otherwise>
            </c:choose>
            </c:forEach>
            <c:if test="${pagination.curPage ne pagination.pageCnt && pagination.pageCnt > 0}">
            <a href="#" onClick="fn_paging('${pagination.nextPage }')">[다음]</a> 
            </c:if>
            <c:if test="${pagination.curRange ne pagination.rangeCnt && pagination.rangeCnt > 0}">
            <a href="#" onClick="fn_paging('${pagination.pageCnt }')">[끝]</a> 
            </c:if>
            </div>
            <div id="dim-layer" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_format" class="pop-layer" style="padding: 20px 25px;width: 1000px;height: 750px;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<tr>
								<td style="text-align: center;width: 25%;">출고일자</td>
								<td colspan="3" style="width: 75%;">
									<input type="text" name="POPStartDay" id="POPStartDay" value=""  style="width:40%;" readonly="readonly">
									&nbsp;~&nbsp;
									<input type="text" name="POPEndDay" id="POPEndDay" value=""  style="width:40%;" readonly="readonly">
								</td>
							</tr>
							<c:choose>
							<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
							<tr>
								<td style="width: 25%;text-align: center;">출고본부</td>
								<td style="width: 25%">
									<select style="width: 90%;" name="POPLOGC" id="POPLOGC"  onchange="getPOPBONBU(this, 'OUT')">
										<option value="" selected="selected">선택</option>
										<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
										</c:forEach>
									</select>
								</td>
								<td id = 'pop_OutTd_B' style="width: 25%">
									<select style="width: 90%;" name="POPBONBU" id = "POPBONBU" onchange="getPOPCENTER(this, 'OUT')">
										<option value="">선택</option>
									</select>
								</td>
								<td id = 'pop_OutTd_C' style="width: 25%">
									<select style="width: 90%;" name="POPCENTER" id = "POPCENTER">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width: 25%;text-align: center;">입고본부</td>
								<td style="width: 25%">
									<select style="width: 90%;" name="POPINLOGC" id="POPINLOGC"  onchange="getPOPBONBU(this, 'IN')">
										<option value="" selected="selected">선택</option>
										<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
										</c:forEach>
									</select>
								</td>
								<td id = 'pop_InTd_B' style="width: 25%">
									<select style="width: 90%;" name="POPINBONBU" id = "POPINBONBU" onchange="getPOPCENTER(this, 'IN')">
										<option value="">선택</option>
									</select>
								</td>
								<td id = 'pop_InTd_C' style="width: 25%">
									<select style="width: 90%;" name="POPINCENTER" id = "POPINCENTER">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							</c:when>
							<c:when test="${UserInfo.MUT_POSITION == '003'}">
							<tr>
								<td style="text-align: center;">구 분</td>
								<td colspan="3">
									<input type="radio" name = "POPCTYPE" value = "Y" checked="checked" onclick="CTYPE_OnOff();"> 입고
									<input type="radio" name = "POPCTYPE" value = "N" onclick="CTYPE_OnOff();"> 출고
								</td>
							</tr>
							<tr>
								<td style="width: 25%;text-align: center;" id="pop_Bnm1">입고본부</td>
								<td style="width: 25%">
									<select style="width: 90%;" name="POPLOGC" id="POPLOGC"  onchange="getPOPBONBU(this, 'OUT')">
										<option value="" selected="selected">선택</option>
										<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
										</c:forEach>
									</select>
								</td>
								<td id = 'pop_OutTd_B' style="width: 25%">
									<select style="width: 90%;" name="POPBONBU" id = "POPBONBU" onchange="getPOPCENTER(this, 'OUT')">
										<option value="">선택</option>
									</select>
								</td>
								<td id = 'pop_OutTd_C' style="width: 25%">
									<select style="width: 90%;" name="POPCENTER" id = "POPCENTER">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width: 25%;text-align: center;" id="pop_Bnm2">출고본부</td>
								<td colspan="3">
									${UserInfo.MCI_LOGCNAME}&nbsp;${UserInfo.MCI_BonbuNAME}&nbsp;
									<select style="width: 50%;" name="POPINCENTER" id = "POPINCENTER">
										<option value="">선택</option>
										<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
										<option value="${CENTERLIST.MCI_Center}" <c:if test="${CENTER == CENTERLIST.MCI_Center}">selected="selected" </c:if>>${CENTERLIST.MCI_CenterName}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							</c:when>
							<c:otherwise>
							<tr>
								<td style="text-align: center;">구 분</td>
								<td colspan="3">
									<input type="radio" name = "POPCTYPE" value = "Y" checked="checked" onclick="CTYPE_OnOff();"> 입고
									<input type="radio" name = "POPCTYPE" value = "N" onclick="CTYPE_OnOff();"> 출고
								</td>
							</tr>
							<tr>
								<td style="width: 25%;text-align: center;" id="pop_Bnm1">입고본부</td>
								<td style="width: 25%">
									<select style="width: 90%;" name="POPLOGC" id="POPLOGC"  onchange="getPOPBONBU(this, 'OUT')">
										<option value="" selected="selected">선택</option>
										<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
										</c:forEach>
									</select>
								</td>
								<td id = 'pop_OutTd_B' style="width: 25%">
									<select style="width: 90%;" name="POPBONBU" id = "POPBONBU" onchange="getPOPCENTER(this, 'OUT')">
										<option value="">선택</option>
									</select>
								</td>
								<td id = 'pop_OutTd_C' style="width: 25%">
									<select style="width: 90%;" name="POPCENTER" id = "POPCENTER">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width: 25%;text-align: center;" id="pop_Bnm2">출고본부</td>
								<td colspan="3">
									${UserInfo.MCI_LOGCNAME}&nbsp;${UserInfo.MCI_BonbuNAME}&nbsp;${UserInfo.MCI_CenterName}
								</td>
							</tr>
							</c:otherwise>
							</c:choose>
							<tr>
								<td style="text-align: center;">상품코드명</td>
								<td colspan="3">
									<input type="text" name="POP_MNAME" id="POP_MNAME" value=""  style="width:50%;">
									<a href="javascript:pop_Model();" class="search grey button" >코드조회</a>	
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">화면표시개수</td>
								<td colspan="3">
									<input type="radio" name="PTYPE" value = "0" onclick="PTYPE_OnOff();"/>모두표시
									<input type="radio" name="PTYPE" value = "1" checked="checked" onclick="PTYPE_OnOff();" />입력
									<input type="number" name="PSIZE" id="PSIZE" value="10" style="width:45%;" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="1" max="100">
								</td>
							</tr>
							<tr>
								<td colspan="4" style="text-align: center;"  colspan="4">
									<a href="javascript:Ajax_DealSearch(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table"  style="height: 390px;overflow:auto;margin-bottom: 10px;">
						
					</div>
					<div style="text-align: center;">
						<a href="javascript:DocSave();" class="search grey button">발 급</a>
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종 료</a>
					</div>
    			</div>
			</div>
			<div id="dim-layer2" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_model" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<tr>
								<td style="text-align: center;">물류센터</td>
								<td colspan="2">
									<c:choose>
									<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
									<select style="width: 90%;" name="MODEL_LOGC" id="MODEL_LOGC" >
										<option value="">선택</option>
										<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
										</c:forEach>
									</select>
									</c:when>
									<c:otherwise>
										${UserInfo.MCI_LOGCNAME}
										<input type="hidden" name="MODEL_LOGC" value="${UserInfo.MCI_LOGC}" id="MODEL_LOGC">
									</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">대분류</td>
								<td colspan="2">
									<select style="width: 90%;" name="LCODEList" id="LCODEList"  onchange="getMLIST(this)">
										<option value="">선택</option>
										<c:forEach var="LCODELIST" items="${LCODELIST}" varStatus="st">
											<option value="${LCODELIST.MCC_S_CODE}">${LCODELIST.MCC_S_NAME}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="text-align: center;width: 30%;">중분류</td>
								<td id = 'td_M' style="width: 35%;">
									<select style="width: 90%;" name="MCODEList" id = "MCODEList" onchange="getSLIST(this)">
										<option value="">선택</option>
									</select>
								</td>
								<td style="width: 35%;">
									<input type="text" name="MCODENAME" id="MCODENAME" style="width:90%;">
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">소분류</td>
								<td id = 'td_S'>
									<select style="width: 90%;" name="SCODEList" id = "SCODEList">
										<option value="">선택</option>
									</select>
								</td>
								<td>
									<input type="text" name="SCODENAME" id="SCODENAME" style="width:90%;">
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">상품코드명</td>
								<td colspan="2">
									<input type="text" name="MODEL_MNAME" id="MODEL_MNAME" value=""  style="width:90%;">
								</td>
							</tr>
							<tr>
								<td colspan="3" style="text-align: center;">
									<a href="javascript:Ajax_Order_search(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table2"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
						
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종 료</a>
					</div>
    			</div>
			</div>
		</div>
	</form:form>
</body>
</html>