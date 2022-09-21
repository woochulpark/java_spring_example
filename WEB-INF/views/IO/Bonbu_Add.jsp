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
	$("#popStartDay").datepicker();
	$("#popEndDay").datepicker();
	$('#OUTDATE').datepicker();
});
var POPLOGC = '';
var POPLOGCNAME = '';
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function save()
{	
	var DOCNO = $('#DOCNO').val();
	var len = $('#Table_MLIST tbody tr').length;
	if(len == 0 || DOCNO.length == 0){
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
function Clear(){
	$("#Table_MLIST").find('tbody').empty();
	$("#DIV_BLIST").empty();
	$("#DOCNO").val('');
	$("#INQTY").val('');
	$("#td_Outcnt").text('0');
	$("#OUTDATE").val('');
	POPLOGC = '';
	POPLOGCNAME = '';
	$("#id_popLogc").text('');
	$("#pop_table").empty();
	
	
    $("#MNAME").val('');
    $("input[name=OUTCNT]").each(function(idx){   
    	$(this).val('0');
    });
    $("input[name=JEGO]").each(function(idx){   
    	$(this).val('0');
    });
}
function ClearData() {
	var del_chk = confirm('초기화 하시겠습니까?');
	if(del_chk==true){
		Clear();
	}
}
function cancel() {
	var form = document.BonbuInForm;
	form.action = "BonbuInList.do";
	form.submit();
}
function Ajax_BonbuIn_search(PAGE){
	var popStartDay = $("#popStartDay").val();
	var popEndDay = $("#popEndDay").val();
	var MNAME = $("#MNAME").val();
	$.ajax({
		type:"GET",
		url:"Ajax_BonbuIn_search.do",
		data: "StartDay="+popStartDay+"&EndDay="+popEndDay+"&LOGC="+POPLOGC+"&MNAME="+MNAME+"&PAGE=" + PAGE,
		success:function(html){
			$("#pop_table").empty();
			$("#pop_table").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function pop_Model(){
	var LOGC = $("#LOGC option:selected").val();
	var LOGCTEXT = $("#LOGC option:selected").text();
	if(LOGC.length == 0){
		document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
		alert("물류센터를 선택해주세요."); 
		return;
	}
	POPLOGC = LOGC;
	POPLOGCNAME = LOGCTEXT;
	$("#id_popLogc").text(POPLOGCNAME);
	
	var $el = $("#layer_model");
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

    $('.layer .dimBg').click(function(){
        $('#dim-layer').fadeOut();
        return false;
    });
}
function AddLogcIn(DOCNO, MODEL, LGROUP, MGROUP, SGROUP, JEJODATE, INDATE, INPRICE, INCNT, TOTAL){
	var len = $('#Table_MLIST tbody tr').length;
	if(len > 0){
		return;
	}
	$.ajax({
		type:"POST",
		url:"Ajax_BonbuList.do",
		data: "DOCNO="+DOCNO,
		success:function(html){
			$("#DIV_BLIST").empty();
			$("#DIV_BLIST").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
			return;
		}
	});	
	
	$("#DOCNO").val(DOCNO);
	$("#INQTY").val(INCNT);
	
	var html = "";
    html += "<tr style='height: 40px;'>";
    html += "<td>" + DOCNO + "</td>";
    html += "<td>" + MODEL + "</td>";
    html += "<td>" + LGROUP + "</td>";
    html += "<td>" + MGROUP + "</td>";
    html += "<td>" + SGROUP + "</td>";
    html += "<td>" + JEJODATE + "</td>";
    html += "<td><input type='hidden' name='INDATE' id='INDATE' value='"+ INDATE +"' >" + INDATE + "</td>";
    html += "<td>" + INPRICE + "</td>";
    html += "<td>" + INCNT + "</td>";
    html += "<td>" + TOTAL + "</td>";
    html += "</tr>";
    $('#mList').append(html);
    $('#dim-layer').fadeOut();
}
function ModelCheck(){
	var len = $('#Table_MLIST tbody tr').length;
	if(len > 0){
		Clear();
	}
}
function Ajax_JegoUpdate(){
	
	var LOGC = $("#LOGC option:selected").val();
	var LOGCTEXT = $("#LOGC option:selected").text();
	if(LOGC.length == 0){
		document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
		alert("물류센터를 선택해주세요."); 
		return;
	}
	
	var DOCNO = $('#DOCNO').val();
	console.log("DOCNO = " + DOCNO);
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
    		"TYPE"      : "I", 
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
	<form:form name="BonbuInForm" method="post" action="BonbuIn_Add_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type='hidden' name='INQTY' id='INQTY' value='' >
		<input type='hidden' name='DOCNO'  id='DOCNO' value='' >
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부입고 > 본부입고 등록</div>
			<h2>본부입고 등록</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
							<select style="width: 90%;" name="LOGC" id = "LOGC" onchange='ModelCheck()'>
								<option value="">선택</option>
								<c:if test="${not empty LOGCLIST}">
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<option value="${LOGCLIST.MCI_LOGC}" <c:if test="${LOGCLIST.MCI_LOGC == LOGC}">selected="selected"</c:if>>${LOGCLIST.MCI_LOGCNAME}</option>
								</c:forEach>
								</c:if>
							</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
				<a href="javascript:pop_Model();" class="search grey button" >물류입고 조회</a>	
			</div>
			<div class="scroll_table" id="Table_MLIST">
				<table>
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
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">출고 예상량</td>
							<td style="width: 70%;text-align: left;" id="td_Outcnt">0</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류 출고일</td>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='OUTDATE' id='OUTDATE' style='width:90%;' readonly='readonly' value='${NOWDATE}'/>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" id="DIV_BLIST">
				
			</div>
			<div style="text-align: center;">
				<a href="javascript:ClearData();" class="search grey button">초기화</a>
				<a href="javascript:Ajax_JegoUpdate();" class="search grey button">적정재고반영</a>
				<a href="javascript:save();" class="search grey button">물류출고</a>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
			<div id="dim-layer" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_model" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<tr>
								<td style="text-align: center;">물류센터</td>
								<td id="id_popLogc">
								</td>
							</tr>
							<tr>
								<td>입고일자</td>
								<td>
									<input type="text" name="popStartDay" id="popStartDay" value="${StartDay}" style="width:40%;" readonly="readonly">
									&nbsp;~&nbsp;
									<input type="text" name="popEndDay" id="popEndDay" value="${EndDay}" style="width:40%;" readonly="readonly">
								</td>
							</tr>
							<tr>
								<td style="text-align: center;">상품코드</td>
								<td>
									<input type="text" name="MNAME" id="MNAME" value=""  style="width:90%;">
								</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align: center;">
									<a href="javascript:Ajax_BonbuIn_search(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
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