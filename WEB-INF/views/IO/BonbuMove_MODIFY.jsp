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
	minDate: new Date('${MoveInfo.DBO_DATE}'),
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
	if ($('#DBM_DATE').length) {
		$('#DBM_DATE').datepicker();
	}
});
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function IsJego(){
	var MODEL = $("#MODEL").val();
	var LOGC = $("#LOGC").val();
	var BONBU = $("#BONBU option:selected").val();
	if(BONBU.length == 0){
		alert("출고 본부를 지정해주세요."); 
		return;
	}
	var CENTER = $("#CENTER option:selected").val();
	if(CENTER.length == 0){
		alert("출고 센터를 지정해주세요."); 
		return;
	}
	var DBM_DATE = $("#DBM_DATE").val();
	if(DBM_DATE.length == 0){
		alert("출고일을 지정해주세요."); 
		return;
	}
	var DBM_QTY = $("#DBM_QTY").val();
	if(DBM_QTY.length == 0){
		alert("출고 수량을 지정해주세요."); 
		return;
	}
	if(DBM_QTY == '0'){
		alert("출고 수량을 지정해주세요."); 
		return;
	}
	if(MODEL.length > 0 && LOGC.length > 0){
		$.ajax({
			type:"GET",
			url:"Ajax_IsJego.do",
			data: "MODEL="+MODEL+"&LOGC="+LOGC+"&BONBU="+BONBU+"&CENTER="+CENTER+"&NOWDATE="+DBM_DATE+"&DBM_QTY="+DBM_QTY,
			success:function(data){
				if(data == '-1'){
					document.getElementById('MSG').innerText = "출고 본부 정보를 가져오지 못했습니다.";
					alert("출고 본부 정보를 가져오지 못했습니다."); 
					return;
				}
				else if(data == '-2'){
					document.getElementById('MSG').innerText = "출고수량이 잘못되었습니다.";
					alert("출고수량이 잘못되었습니다."); 
					return;
				}
				else if(data == '-3'){
					document.getElementById('MSG').innerText = "재고를 확인하지 못했습니다. 다시 시도해주세요.";
					alert("재고를 확인하지 못했습니다. 다시 시도해주세요."); 
					return;
				}
				else{
					document.getElementById('MSG').innerText = data;
					alert(data); 
					return;
				}
			}, error: function(xmlHttpObj){
				alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
			}
		});	
	}
}
function save()
{
	var LEVEL = document.getElementById("LEVEL").value;
	if(LEVEL == '003' || LEVEL == '004'){
		if(CFM == 'N'){
			document.getElementById('MSG').innerText = "담당자 확인이 되지 않았습니다.";
			alert("담당자 확인이 되지 않았습니다."); 
			return;
		}
		var form = document.moveForm;
		form.submit();
		return;
	}
	else{
		var BONBU = $("#BONBU option:selected").val().length;
		if(BONBU == 0){
			document.getElementById('MSG').innerText = "출고 본부를 지정해주세요.";
			alert("출고 본부를 지정해주세요"); 
			return;
		}
		
		var CENTER = $("#CENTER option:selected").val().length;
		if(CENTER == 0){
			document.getElementById('MSG').innerText = "출고 센터를 지정해주세요.";
			alert("출고 센터를 지정해주세요"); 
			return;
		}
		
		var DBM_DATE = $('#DBM_DATE').val();
		if(DBM_DATE.length == 0){
			document.getElementById('MSG').innerText = "출고일을 입력해주세요.";
			alert("출고일을 입력해주세요."); 
			return;
		}

		var OUTJEGO = $('#OUTJEGO').val();
		if(OUTJEGO.length == 0){
			document.getElementById('MSG').innerText = "출고 재고를 가져오지 못했습니다.\n출고 본부와 출고 센터를 지정해주세요.";
			alert("출고 재고를 가져오지 못했습니다. 출고 본부와 출고 센터를 지정해주세요."); 
			return;
		}
		if(OUTJEGO == '0'){
			document.getElementById('MSG').innerText = "출고 본부에 현재고가 없습니다.";
			alert("출고 본부에 현재고가 없습니다."); 
			return;
		}
		var DBM_QTY = $('#DBM_QTY').val();
		if(DBM_QTY.length == 0){
			document.getElementById('MSG').innerText = "출고 수량을 입력해주세요.";
			alert("출고 수량을 입력해주세요."); 
			return;
		}
		else{
			var OUTQTY = Number(DBM_QTY);
			var JEGO = Number(OUTJEGO);
			if(OUTQTY > JEGO){
				document.getElementById('MSG').innerText = "출고 수량은 현 재고보다 작아야합니다.";
				alert("출고 수량은 현 재고보다 작아야합니다."); 
				return;
			}
		}
		var form = document.moveForm;
		form.submit();	
	}
}
function CFMSAVE()
{	
	var LEVEL = document.getElementById("LEVEL").value;
	var CFM = document.getElementById("CFM").value;
	if((LEVEL == '003' || LEVEL == '004') && CFM == 'N'){
		document.getElementById('MSG').innerText = "담당자 확인이 되지 않았습니다.";
		alert("담당자 확인이 되지 않았습니다."); 
		return;
	}
	
	var BONBU = $("#BONBU option:selected").val().length;
	if(BONBU == 0){
		document.getElementById('MSG').innerText = "출고 본부를 지정해주세요.";
		alert("출고 본부를 지정해주세요"); 
		return;
	}
	
	var CENTER = $("#CENTER option:selected").val().length;
	if(CENTER == 0){
		document.getElementById('MSG').innerText = "출고 센터를 지정해주세요.";
		alert("출고 센터를 지정해주세요"); 
		return;
	}
	
	var DBM_DATE = $('#DBM_DATE').val();
	if(DBM_DATE.length == 0){
		document.getElementById('MSG').innerText = "출고일을 입력해주세요.";
		alert("출고일을 입력해주세요."); 
		return;
	}

	var OUTJEGO = $('#OUTJEGO').val();
	if(OUTJEGO.length == 0){
		document.getElementById('MSG').innerText = "출고 재고를 가져오지 못했습니다.\n출고 본부와 출고 센터를 지정해주세요.";
		alert("출고 재고를 가져오지 못했습니다. 출고 본부와 출고 센터를 지정해주세요."); 
		return;
	}
	if(OUTJEGO == '0'){
		document.getElementById('MSG').innerText = "출고 본부에 현재고가 없습니다.";
		alert("출고 본부에 현재고가 없습니다."); 
		return;
	}
	var DBM_QTY = $('#DBM_QTY').val();
	if(DBM_QTY.length == 0){
		document.getElementById('MSG').innerText = "출고 수량을 입력해주세요.";
		alert("출고 수량을 입력해주세요."); 
		return;
	}
	else{
		var OUTQTY = Number(DBM_QTY);
		var JEGO = Number(OUTJEGO);
		if(OUTQTY > JEGO){
			document.getElementById('MSG').innerText = "출고 수량은 현 재고보다 작아야합니다.";
			alert("출고 수량은 현 재고보다 작아야합니다."); 
			return;
		}
	}
	
	var form = document.moveForm;
	form.action = "BonbuMove_CFMSAVE.do";
	form.submit();
}
function cancel() {
	var form = document.moveForm;
	form.action = "BonbuMove_List.do";
	form.submit();
}
function getCENTER(obj){
	$("#OUTJEGO").val('0');
	var LOGC = document.getElementById("LOGC").value;
	$.ajax({
		type:"GET",
		url:"Ajax_InoutCenter.do",
		data: "LOGC="+LOGC+"&BONBU="+obj.value,
		success:function(html){
			$("#td_C").empty();
			$("#td_C").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getJEGO(){
	$("#OUTJEGO").val('0');
	$("#td_JEGO").text('0');
	var MODEL = $("#MODEL").val();
	var LOGC = $("#LOGC").val();
	var BONBU = $("#BONBU option:selected").val();
	var CENTER = $("#CENTER option:selected").val();
	if(MODEL.length > 0 && LOGC.length > 0 && BONBU.length > 0 && CENTER.length > 0 ){
		$.ajax({
			type:"GET",
			url:"Ajax_GETJEGO.do",
			data: "MODEL="+MODEL+"&LOGC="+LOGC+"&BONBU="+BONBU+"&CENTER="+CENTER,
			success:function(data){
				if(data == '-1'){
					document.getElementById('MSG').innerText = DBM_DATE + "기준 재고를 가져오지 못했습니다.";
					alert(DBM_DATE + "기준 재고를 가져오지 못했습니다."); 
					return;
				}
				$("#OUTJEGO").val(data);
				$("#td_JEGO").text(data);
			}, error: function(xmlHttpObj){
				alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
			}
		});	
	}
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="moveForm" method="post" action="BonbuMove_MODIFY_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type='hidden' name='DOCNO'  id='DOCNO' value='${MoveInfo.DBO_DOCNO}' >
		<input type='hidden' name='LOGC'  id='LOGC' value='${MoveInfo.INLOGC}' >
		<input type='hidden' name='CFM'  id='CFM' value='${MoveInfo.DBM_CFM}' >
		<input type='hidden' name='LEVEL'  id='LEVEL' value='${level}' >
		<input type='hidden' name='INJEGO'  id='INJEGO' value='${MoveInfo.DMJ_JEGO}'>
		<input type='hidden' name='MODEL'  id='MODEL' value='${MoveInfo.DBO_MODEL}' >
		<input type='hidden' name='OUTJEGO'  id='OUTJEGO' value='${MoveInfo.OUTJEGO}'>
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부별 이동 > 본부별 이동 수정</div>
			<h2>본부별 이동 수정</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">접수번호</td>
							<td style="width: 70%;text-align: left;">
								${MoveInfo.DBO_DOCNO}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								${MoveInfo.INLOGCNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주본부</td>
							<td style="width: 70%;text-align: left;">
								${MoveInfo.INBONBUNAME}&nbsp;${MoveInfo.INCENTERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주자</td>
							<td style="width: 70%;text-align: left;">
								${MoveInfo.MUT_UserName}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">발주일</td>
							<td style="width: 70%;text-align: left;">
								${MoveInfo.DBO_DATE}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">비 고</td>
							<td style="width: 70%;text-align: left;">
								${MoveInfo.DBO_CMNT}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
				
			</div>
			<div class="scroll_table" id="Table_MLIST">
				<table>
					<thead>
						<tr>
							<th style="width: 20%;">상품코드</th>
							<th style="width: 20%;">대분류</th>
							<th style="width: 20%;">중분류</th>
							<th style="width: 20%">소분류</th>
							<th style="width: 20%">발주요청수량</th>
						</tr>
					</thead>
					<tbody  id='mList'>
						<tr style="height: 40px;">
							<td style="padding: 2px;">${MoveInfo.DBO_MODEL}</td>
							<td style="padding: 2px;">${MoveInfo.MMC_LGROUPNAME}</td>
							<td style="padding: 2px;">${MoveInfo.MMC_MGROUP}</td>
							<td style="padding: 2px;">${MoveInfo.MMC_SGROUP}</td> 	
							<td style="padding: 2px;">${MoveInfo.DBO_QTY}</td>		 	
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 15%;">출고본부</td>
							<td style="width: 35%;text-align: left;">
								<c:choose>
								<c:when test="${(level == '001' || level == '002') and MoveInfo.DBM_Sts == 'N' and MoveInfo.DBO_BisSts == 'N'}">
								<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
								<option value="" selected="selected">선택</option>
								<c:if test="${not empty BONBULIST}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
									<option value="${BONBULIST.MCI_Bonbu}" <c:if test="${MoveInfo.OUTBONBU == BONBULIST.MCI_Bonbu}">selected="selected"</c:if>>${BONBULIST.MCI_BonbuNAME}</option>
								</c:forEach>
								</c:if>
								</select>
								</c:when>
								<c:otherwise>
								${MoveInfo.OUTBONBUNAME}
								</c:otherwise>
								</c:choose>
							</td>
							<td style="width: 15%;">입고본부</td>
							<td style="width: 35%;text-align: left;">
								${MoveInfo.INBONBUNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 15%;">센 터</td>
							<td style="width: 35%;text-align: left;"  id = 'td_C' >
								<c:choose>
								<c:when test="${(level == '001' || level == '002') and MoveInfo.DBM_Sts == 'N' and MoveInfo.DBO_BisSts == 'N'}">
								<select style="width: 90%;" name="CENTER" id = "CENTER" onchange="getJEGO()">
									<option value="">선택</option>
									<c:if test="${not empty CENTERLIST}">
									<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
									<option value="${CENTERLIST.MCI_Center}" <c:if test="${MoveInfo.OUTCENTER == CENTERLIST.MCI_Center}">selected="selected"</c:if>>${CENTERLIST.MCI_CenterName}</option>
									</c:forEach>
									</c:if>
								</select>
								</c:when>
								<c:otherwise>
								${MoveInfo.OUTCENTERNAME}
								</c:otherwise>
								</c:choose>
							</td>
							<td style="width: 15%;">센 터</td>
							<td style="width: 35%;text-align: left;">
								${MoveInfo.INCENTERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 15%;">현 재고</td>
							<td style="width: 35%;text-align: left;"  id='td_JEGO' >
								${MoveInfo.OUTJEGO}
							</td>
							<td style="width: 15%;">현 재고</td>
							<td style="width: 35%;text-align: left;">
								${MoveInfo.DMJ_JEGO}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">출고일</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${(level == '001' || level == '002') and MoveInfo.DBM_Sts == 'N' and MoveInfo.DBO_BisSts == 'N'}">
								<input type="text" name="DBM_DATE" id="DBM_DATE" value="${MoveInfo.DBM_DATE}" style="width:80%;" readonly="readonly">
								</c:when>
								<c:otherwise>
								${MoveInfo.DBM_DATE}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">출고수량</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${(level == '001' || level == '002') and MoveInfo.DBM_Sts == 'N' and MoveInfo.DBO_BisSts == 'N'}">
								<input type="number" name="DBM_QTY" id="DBM_QTY" value="${MoveInfo.DBM_QTY}" style="width:80%;"  onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="0">
								</c:when>
								<c:otherwise>
								${MoveInfo.DBM_QTY}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">이동방법</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${MoveInfo.DBM_Sts == 'N' and MoveInfo.DBO_BisSts == 'N' }">
								<input type="text" name="DBM_HOWMOVE" id="DBM_HOWMOVE" value="${MoveInfo.DBM_HOWMOVE}" style="width:80%;"/>
								</c:when>
								<c:otherwise>
								${MoveInfo.DBM_HOWMOVE}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">비 고</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${MoveInfo.DBM_Sts == 'N' and MoveInfo.DBO_BisSts == 'N' }">
								<input type="text" name="DBM_CMNT" id="DBM_CMNT" value="${MoveInfo.DBM_CMNT}" style="width:80%;"/>
								</c:when>
								<c:otherwise>
								${MoveInfo.DBM_CMNT}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:if test="${MoveInfo.DBM_CFM == 'N' and (level == '001' or level == '002')}">
				<a href="javascript:CFMSAVE();" class="search grey button">담당자 확인</a>
				</c:if>
				<c:if test="${MOVECFM == 'Y'}">
				<a href="javascript:save();" class="search grey button">이동등록</a>
				</c:if>
				<c:if test="${(level == '001' or level == '002') and MoveInfo.DBO_BisSts == 'N' and MoveInfo.DBM_Sts == 'N'}">
				<a href="javascript:IsJego();" class="search grey button">출하재고확인</a>
				</c:if>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>