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

$(function() {
	mergeRows(Table_Bonbu,0); // 위 행과 비교하여 같다면 첫번째 컬럼 머지
	if($("#SUS_CNT").val() == '0')
		$('#OUTDATE').datepicker();
});
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function save()
{	
	var DOCNO = $('#DOCNO').val();
	var len = $('#Table_MLIST tbody tr').length;
	if(len == 0 || DOCNO.length == 0){
		document.getElementById('MSG').innerText = "물류입고 정보를 가져오지 못했습니다.";
		alert("물류입고 정보를 가져오지 못했습니다."); 
		return;
	}

	var len = $('#Table_Bonbu tbody tr').length;
	if(len == 0){
		document.getElementById('MSG').innerText = "본부 내역이 없습니다.";
		alert("본부 내역이 없습니다."); 
		return;
	}
	
	
	var SUS_CNT = $("#SUS_CNT").val();
	if(SUS_CNT == '0'){
		var OUTDATE = $("#OUTDATE").val();
		if(OUTDATE.length == 0){
			document.getElementById('MSG').innerText = "물류 출고일을 입력해주세요.";
			alert("물류 출고일을 입력해주세요."); 
			return;
		}
		
		var INDATE = $("#INDATE").val();
		if(INDATE.length == 0){
			document.getElementById('MSG').innerText = "물류 입고일을 가져오지 못했습니다.";
			alert("물류 입고일을 가져오지 못했습니다."); 
			return;
		}
		
		var date_OUT = new Date(OUTDATE);
		var date_IN = new Date(INDATE);
		if(date_OUT < date_IN){
			document.getElementById('MSG').innerText = "입고일 이후 날짜를 입력해야 합니다.";
			alert("입고일 이후 날짜를 입력해야 합니다."); 
			return;
		}
	}
	
	var OutTotal = 0;
    $("input[name=OUTCNT]").each(function(idx){   
    	var OUTCNT = $(this).val();
    	console.log('OUTCNT = ' + OUTCNT);
    	if(OUTCNT.length == 0 || OUTCNT == '0')
    	{
    		$(this).val('0');
    	}
    	else{
    		OutTotal += Number($(this).val()); 
    	}
    });

    var INQTY = document.getElementById("INQTY").value;
    if(INQTY.length == 0 || INQTY == '0')
	{
    	document.getElementById('MSG').innerText = "출고 예상량을 가져오지 못했습니다.";
		alert("출고 예상량을 가져오지 못했습니다."); 
		return;
	}
    if(Number(INQTY) != OutTotal ){
    	document.getElementById('MSG').innerText = "출고수량의 합이 입고수량과 맞지않습니다.";
		alert("출고수량의 합이 입고수량과 맞지않습니다."); 
		return;
    }
    if(OutTotal == 0){
    	document.getElementById('MSG').innerText = "출고수량을 입력해주세요.";
		alert("출고수량을 입력해주세요."); 
		return;
    }
    
	var form = document.BonbuInForm;
	form.submit();
}
function cancel() {
	var form = document.BonbuInForm;
	form.action = "BonbuIn_DETAIL.do";
	form.submit();
}
function Ajax_JegoUpdate(){
	var DOCNO = $('#DOCNO').val();
	var len = $('#Table_MLIST tbody tr').length;
	if(len == 0 || DOCNO == null || DOCNO.length == 0){
		document.getElementById('MSG').innerText = "물류입고를 내역을 조회하여 추가해주세요.";
		alert("물류입고 내역을 조회하여 추가해주세요."); 
		return;
	}

	var len = $('#Table_Bonbu tbody tr').length;
	if(len == 0){
		document.getElementById('MSG').innerText = "본부 내역이 없습니다.";
		alert("본부 내역이 없습니다."); 
		return;
	}
	
	//적정재고
	var CENTERLIST = [];
    var JEGOLIST = [];
	var JEGOTAG = $("input[name=JEGO]");
	JEGOTAG.each(function(idx){   
    	var tr = JEGOTAG.parent().parent().eq(idx);  // checkbox 태그의 두 단계 상위 태그가 tr이기 때문에
 	    var td = tr.children();  // td 태그는 tr 태그의 하위에 있으므로
 	    var CENTER = td.eq(0).find("input[name=CENTER]").val();
 	    var JEGOVAL = $(this).val();
 	   	if(JEGOVAL.length == 0 || JEGOVAL == '0')
   		{
   			$(this).val('0');
   		}
 	   JEGOLIST.push(JEGOVAL);
 	   CENTERLIST.push(CENTER);
 	   console.log("JEGO = " + JEGOVAL + ", CENTER = " + CENTER);
    });
    
    var objParams = {
    		"TYPE"      : "M", 
            "DOCNO"      : DOCNO, 
            "CENTER" : CENTERLIST,   
            "JEGO" : JEGOLIST     
        };
	
	$.ajax({
		type:"POST",
		url:"Ajax_JegoUpdate.do",
		data: objParams,
		success:function(html){
			$("#DIV_BLIST").empty();
			$("#DIV_BLIST").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});
}
function TotaolPrice(){
	var OutTotal = 0;
    $("input[name=OUTCNT]").each(function(idx){   
    	var OUTCNT = $(this).val();
    	if(OUTCNT.length > 0)
    	{
    		OutTotal += Number($(this).val()); 
    	}
    });
    $("#td_Outcnt").text(OutTotal);
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="BonbuInForm" method="post" action="BonbuIn_Modify_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type='hidden' name='INQTY' id='INQTY' value='${LogcInInfo.DIL_QTY}' >
		<input type='hidden' name='DOCNO'  id='DOCNO' value='${LogcInInfo.DIL_DOCNO}' >
		<input type='hidden' name='SUS_CNT'  id='SUS_CNT' value='${LogcInInfo.SUS_CNT}' >
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부입고 > 본부입고 수정</div>
			<h2>본부입고 수정</h2>

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
					</tbody>
				</table>
			</div>
			<div class="scroll_table" id="Table_MLIST">
				<table id="nTbl">
					<thead>
						<tr>
							<th style="width: 10%;">물류 접수번호</th>
							<th style="width: 10%;">상품코드</th>
							<th style="width: 10%;">대분류</th>
							<th style="width: 10%">중분류</th>
							<th style="width: 10%">소분류</th>
							<th style="width: 10%">제조일자</th>
							<th style="width: 10%">입고일자</th>
							<th style="width: 10%">입고단가</th>
							<th style="width: 10%">입고수량</th>
							<th style="width: 10%">총금액</th>
						</tr>
					</thead>
					<tbody  id='mList'>
						<tr style="height: 40px;">
							<td>${LogcInInfo.DIL_DOCNO}</td>
							<td>${LogcInInfo.DIL_MODEL}</td>
							<td>${LogcInInfo.MMC_LGROUPNAME}</td>
							<td>${LogcInInfo.MMC_MGROUP}</td> 	
							<td>${LogcInInfo.MMC_SGROUP}</td>
							<td>${LogcInInfo.DIL_JEJO}</td> 	
							<td>${LogcInInfo.DIL_INDate}<input type='hidden' name='INDATE' id='INDATE' value='${LogcInInfo.DIL_INDate}' ></td> 
							<td>${LogcInInfo.DIL_INPRICE}</td> 
							<td>${LogcInInfo.DIL_QTY}</td> 
							<td>${LogcInInfo.INTOTAL}</td>  	 	
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">출고 예상량</td>
							<td style="width: 70%;text-align: left;" id="td_Outcnt">${LogcInInfo.OUTTOTAL}</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류 출고일</td>
							<td style="width: 70%;text-align: left;">
								<c:if test="${LogcInInfo.SUS_CNT == '0'}">
								<input type='text' name='OUTDATE' id='OUTDATE' value='${LogcInInfo.DIL_OUTDATE}' style='width:90%;' readonly='readonly' />
								</c:if>
								<c:if test="${LogcInInfo.SUS_CNT != '0'}">
								${LogcInInfo.DIL_OUTDATE}
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" id="DIV_BLIST">
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
			</div>
			<div style="text-align: center;">
				<a href="javascript:Ajax_JegoUpdate();" class="search grey button">적정재고반영</a>
				<a href="javascript:save();" class="search grey button">물류출고</a>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>