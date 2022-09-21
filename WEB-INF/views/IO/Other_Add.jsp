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
	$('#OUTDATE').datepicker();
});
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}

//등록할 전체 파일 사이즈
var totalFileSize = 0;
//파일명 리스트
var fileNameList = new Array();
// 등록 가능한 파일 사이즈 MB
var uploadSize = 10;
// 등록 가능한 총 파일 사이즈 MB
var maxUploadSize = 100;
var fileKey = 1;
function fileClick(){
	var len = $("input[name=Addfile]").length;
	var check = true;
	var id = '';
	if(len > 0){
		var Addfile = $("input[name=Addfile]");
		Addfile.each(function (i) {
			var fileid = $(this).attr('id');
			var len = $(this).val().length;
			if(len == 0)
			{
				id = fileid;
				check = false;
				return false;
			}
		});
	}
	if(check){
		id = 'Addfile' + fileKey;
		var form = $('#otherForm');
		var html = "<input type='file' name='Addfile' id='" + id + "' onchange='fileCheck(this.id)' style='background: #EAEAEA;display: none;'/>";
		form.append(html);
		fileKey++;
	}
	$('#'+ id).click();
}
function fileCheck(id) {
	var obj = document.getElementById(id);
	var files = obj.files;
    for (var i = 0; i < files.length; i++) {
    	var fileName = files[i].name;
    	var fileNameArr = fileName.split("\.");
    	var ext = fileNameArr[fileNameArr.length - 1].toLowerCase();
    	var fileSize = files[i].size / 1024 / 1024;
    	if($.inArray(ext, ['exe', 'bat', 'sh', 'java', 'jsp', 'html', 'js', 'css', 'xml']) >= 0){
            // 확장자 체크
            alert("등록 불가 확장자가 있습니다.\n'exe', 'bat', 'sh', 'java', 'jsp', 'html', 'js', 'css', 'xml'");
            $('#'+ id).remove(); 
            break;
        }else if(fileSize > uploadSize){
            // 파일 사이즈 체크
            alert("용량 초과\n업로드 가능 용량 : " + uploadSize + " MB");
            $('#'+ id).remove(); 
            break;
        }else{
            // 전체 파일 사이즈
            totalFileSize += fileSize;
            fileNameList.push(fileName);
            // 업로드 파일 목록 생성
            addFileList(id, fileName, fileSize);
        }
    }
}
//업로드 파일 목록 생성
function addFileList(id, fileName, fileSize){
    var html = "";
    html += "<tr>";
    html += "    <td><input type='checkbox' name='FILE_DEL' class='FILE_DEL' style='width:90%;' value='" + id + "'/></td>";
    html += "    <td>" + fileName + "</td>";
    html += "    <td>" + fileSize.toFixed(2) + " MB</td>";
    html += "</tr>"

    $('#fileTableTbody').append(html);
}
function fileAllCheck(){
	if($("#file_ALLCHK").is(":checked")){
        $("input[name=FILE_DEL]").prop("checked", true);
    }
    else{
    	$("input[name=FILE_DEL]").prop("checked", false);
    }
}
function FILE_DELETE(){
	for(var i=$("[name=FILE_DEL]:checked").length-1; i>-1; i--){ 
		var id = $("[name=FILE_DEL]:checked").eq(i).val();
		$('#'+ id).remove(); 
		$("[name=FILE_DEL]:checked").eq(i).closest("tr").remove(); 
	}﻿ 
}

function save()
{
	var LOGC = $("#LOGC option:selected").val();
	if(LOGC.length == 0){
		document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
		alert("물류센터를 선택해주세요."); 
		return;
	}
	var BONBU = $("#BONBU option:selected").val();
	if(BONBU.length == 0){
		document.getElementById('MSG').innerText = "본부를 선택해주세요.";
		alert("본부를 선택해주세요."); 
		return;
	}
	var CENTER = $("#CENTER option:selected").val();
	if(CENTER.length == 0){
		document.getElementById('MSG').innerText = "센터를 선택해주세요.";
		alert("센터를 선택해주세요."); 
		return;
	}
	
	var MCODE = $("#MCODE").val();
	var len = $('#Table_MLIST tbody tr').length;
	if(len == 0 || MCODE.length == 0){
		document.getElementById('MSG').innerText = "상품코드를 추가해주세요.";
		alert("상품코드를 추가해주세요."); 
		return;
	}
	
	var OUTDATE = $("#OUTDATE").val();
	if(OUTDATE.length == 0){
		document.getElementById('MSG').innerText = "출고일을 선택해주세요.";
		alert("출고일을 선택해주세요."); 
		return;
	}
	
	var OUTJEGO = $('#OUTJEGO').val();
	if(OUTJEGO.length == 0){
		document.getElementById('MSG').innerText = "현 재고를 가져오지 못했습니다.\n출고 본부와 출고 센터를 지정해주세요.";
		alert("현 재고를 가져오지 못했습니다. 출고 본부와 출고 센터를 지정해주세요."); 
		return;
	}
	if(OUTJEGO == '0'){
		document.getElementById('MSG').innerText = "출고 본부에 현 재고가 없습니다.";
		alert("출고 본부에 현 재고가 없습니다."); 
		return;
	}
	var QTY = $('#QTY').val();
	if(QTY.length == 0){
		document.getElementById('MSG').innerText = "출고 수량을 입력해주세요.";
		alert("출고 수량을 입력해주세요."); 
		return;
	}
	else{
		var OUTQTY = Number(QTY);
		var JEGO = Number(OUTJEGO);
		if(OUTQTY > JEGO){
			document.getElementById('MSG').innerText = "출고 수량은 현 재고보다 작아야합니다.";
			alert("출고 수량은 현 재고보다 작아야합니다."); 
			return;
		}
	}
	
	var form = document.otherForm;
	form.submit();
}
function cancel() {
	var form = document.otherForm;
	form.action = "OtherList.do";
	form.submit();
}
function getMLIST(obj){
	var LOGC = $("#LOGC option:selected").val();
	$("#SCODEList").empty();
	var option = "<option value=''>선택</option>";
    $('#SCODEList').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_MLIST.do",
		data: "LOGC="+LOGC+"&LCODE="+obj.value,
		success:function(html){
			$("#td_M").empty();
			$("#td_M").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getSLIST(obj){
	var LOGC = $("#LOGC option:selected").val();
	var LCODE = document.getElementById("LCODEList").value;
	$.ajax({
		type:"GET",
		url:"Ajax_SLIST.do",
		data: "LOGC="+LOGC+"&LCODE="+LCODE+"&MCODE="+obj.value,
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
	var LOGC = $("#LOGC option:selected").val();
	if(LOGC.length == 0){
		return;
	}
	var MNAME = $("#MNAME").val();
	var MCODENAME = $("#MCODENAME").val();
	var SCODENAME = $("#SCODENAME").val();
	$.ajax({
		type:"POST",
		url:"Ajax_Order_search.do",
		data: "LCODEList="+LCODEList+"&MCODEList="+MCODEList+"&SCODEList="+SCODEList+"&LOGC="+LOGC+"&MNAME="+MNAME+"&MCODENAME="+MCODENAME+"&SCODENAME="+SCODENAME+"&PAGE=" + PAGE,
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
	var LOGCNAME = $("#LOGC option:selected").text();
	if(LOGC.length == 0){
		document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
		alert("물류센터를 선택해주세요."); 
		return;
	}
	$("#id_popLogc").text(LOGCNAME);
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
function AddModel(MCODE, LGROUP, MGROUP, SGROUP){
	$('#mList').empty();
	$("#MCODE").val(MCODE);
	var html = "";
    html += "<tr style='height: 40px;'>";
    html += "<td>" + MCODE + "</td>";
    html += "<td>" + LGROUP + "</td>";
    html += "<td>" + MGROUP + "</td>";
    html += "<td>" + SGROUP + "</td>";
    html += "</tr>";
    $('#mList').append(html);
    $('#dim-layer').fadeOut();
    getJEGO();
}
function getCENTER(obj){
	var LOGC = $("#LOGC option:selected").val();
	$("#OUTJEGO").val('0');
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
	$("#td_jego").text('0');
	var MODEL = $("#MCODE").val();
	if(MODEL.length == 0){
		return;
	}
	var LOGC = $("#LOGC option:selected").val();
	if(LOGC.length == 0){
		return;
	}
	var BONBU = $("#BONBU option:selected").val();
	if(BONBU.length == 0){
		return;
	}
	var CENTER = $("#CENTER option:selected").val();
	if(CENTER.length == 0){
		return;
	}
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
			$("#td_jego").text(data);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function getBONBU(obj){
	var LOGC = $("#LOGC option:selected").val();
	var LOGCNAME = $("#LOGC option:selected").text();
	var option = "<option value=''>선택</option>";
	$("#id_popLogc").text('');
	$("#td_jego").text('');
	$("#OUTJEGO").val('0');
	$("#pop_table").empty();
	$("#LCODEList option:eq(0)").prop("selected", true);
	$("#MCODEList").empty();
    $('#MCODEList').append(option);
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
    $("#MNAME").val('');
    
	var len = $('#Table_MLIST tbody tr').length;
	if(len > 0){
		$("#Table_MLIST").find('tbody').empty();
		$("#MCODE").val("");
	}
	$("#CENTER").empty();
    $('#CENTER').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_BONBU.do",
		data: "LOGC="+obj.value,
		success:function(html){
			$("#td_B").empty();
			$("#td_B").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function IsJego(){
	var MCODE = $("#MCODE").val();
	var len = $('#Table_MLIST tbody tr').length;
	if(len == 0 || MCODE.length == 0){
		document.getElementById('MSG').innerText = "상품코드를 추가해주세요.";
		alert("상품코드를 추가해주세요."); 
		return;
	}
	var LOGC = $("#LOGC option:selected").val();
	if(LOGC.length == 0){
		alert("물류센터를 지정해주세요."); 
		return;
	}
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
	var OUTDATE = $("#OUTDATE").val();
	if(OUTDATE.length == 0){
		alert("출고일을 지정해주세요."); 
		return;
	}
	var QTY = $("#QTY").val();
	if(QTY.length == 0){
		alert("출고 수량을 지정해주세요."); 
		return;
	}
	if(QTY == '0'){
		alert("출고 수량을 지정해주세요."); 
		return;
	}
	$.ajax({
		type:"GET",
		url:"Ajax_IsJego.do",
		data: "MODEL="+MCODE+"&LOGC="+LOGC+"&BONBU="+BONBU+"&CENTER="+CENTER+"&NOWDATE="+OUTDATE+"&DBM_QTY="+QTY,
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
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="otherForm" id="otherForm" method="post" action="Other_Add_Save.do" enctype="multipart/form-data">
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="MCODE" value="" id="MCODE">
		<input type='hidden' name='OUTJEGO'  id='OUTJEGO' value='0'>
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 기타출고 > 기타출고 등록</div>
			<h2>기타출고 등록</h2>

			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
							<select style="width: 90%;" name="LOGC" id = "LOGC" onchange="getBONBU(this)">
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
				<a href="javascript:pop_Model();" class="search grey button" >코드조회</a>	
			</div>
			<div class="scroll_table" id="Table_MLIST">
				<table>
					<thead>
						<tr>
							<th style="width: 20%;">상품코드</th>
							<th style="width: 20%;">대분류</th>
							<th style="width: 20%">중분류</th>
							<th style="width: 20%">소분류</th>
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
							<th style="width: 30%;">출고본부</th>
							<td style="width: 70%;text-align: left;" id='td_B'>
								<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
								<option value="">선택</option>
								<c:if test="${not empty BONBULIST}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
									<option value="${BONBULIST.MCI_Bonbu}" <c:if test="${BONBU == BONBULIST.MCI_Bonbu}">selected="selected" </c:if>>${BONBULIST.MCI_BonbuNAME}</option>
								</c:forEach>
								</c:if>
								</select>
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">센 터</th>
							<td style="width: 70%;text-align: left;" id='td_C'>
								<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:if test="${not empty CENTERLIST}">
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
								<option value="${CENTERLIST.MCI_Center}" <c:if test="${CENTER == CENTERLIST.MCI_Center}">selected="selected" </c:if>>${CENTERLIST.MCI_CenterName}</option>
								</c:forEach>
								</c:if>
								</select>
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">현 재고</th>
							<td style="width: 70%;text-align: left;" id="td_jego">
								
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">출고일</th>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='OUTDATE' id='OUTDATE' style='width:80%;' readonly="readonly"/>
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">출고수량</th>
							<td style="width: 70%;text-align: left;">
								<input type="number" name="QTY" id="QTY" style="width:80%;"  onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="0">
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">출고사유</th>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='OUTCMNT' id='OUTCMNT' style='width:90%;' />
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 30%;">비 고</th>
							<td style="width: 70%;text-align: left;">
								<input type='text' name='CMNT' id='CMNT' style='width:90%;' />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" style="text-align: center;">
				<div style="text-align: left;" id="div_file">
					<a href="javascript:fileClick();" class="search grey button">파일첨부</a>
					<a href="javascript:FILE_DELETE();" class="search grey button">삭 제</a>
				</div>
				<table id="file_table">
					<thead>
						<tr style="height: 40px">
							<th style="width: 15%;"><input type="checkbox" name="file_ALLCHK" id="file_ALLCHK" style="width:90%;" onClick="javascript:fileAllCheck();"/></th>
							<th style="width: 70%;">파일명</th>
							<th style="width: 15%;">용량</th>
						</tr>
					</thead>
					<tbody id="fileTableTbody">
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<a href="javascript:save();" class="search grey button">출 고</a>
				<a href="javascript:IsJego();" class="search grey button">출하재고확인</a>
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
								<td id="id_popLogc" colspan="2">
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
									<input type="text" name="MNAME" id="MNAME" value=""  style="width:90%;">
								</td>
							</tr>
							<tr>
								<td colspan="3" style="text-align: center;">
									<a href="javascript:Ajax_Order_search(1);" class="search grey button">검색</a>
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