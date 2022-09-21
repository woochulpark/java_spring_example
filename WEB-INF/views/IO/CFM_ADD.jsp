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
	minDate: new Date('${CFMINFO.OUTDATE}'),
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
	$('#DIC_DATE').datepicker();
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
		var form = $('#cfmForm');
		var html = "<input type='file' name='Addfile' id='" + id + "' onchange='fileCheck(this.id)' style='background: #EAEAEA;display: none;'/>";
		form.append(html);
		fileKey++;
	}
	$('#'+ id).click();
}
function fileCheck(id) {
	console.log("fileCheck ID = " + id);
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
function CFM_FILE_DEL(DOCNO, SEQ, id){
	console.log("DEL FILE ID = '" + id + "' ")
	$.ajax({
		type:"POST",
		url:"Ajax_FILE_DEL.do",
		data: "DOCNO="+DOCNO+"&SEQ="+SEQ,
		success:function(data){
			if(data == null || data == '' || data == '-1'){
				document.getElementById('MSG').innerText = "파일삭제 실패! 다시 시도해주세요.";
				alert("파일삭제 실패! 다시 시도해주세요.");
			}
			else{
				$('#'+ id).closest("tr").remove(); 
			}
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function save()
{
	var OUTDATE = $("#OUTDATE").val();
	if(OUTDATE.length == 0){
		document.getElementById('MSG').innerText = "물류 출고일을 가져오지 못했습니다.";
		alert("물류 출고일을 가져오지 못했습니다."); 
		return;
	}
	
	if($("#DIC_DATE").length){
		var DIC_DATE = $("#DIC_DATE").val();
		if(DIC_DATE.length == 0){
			document.getElementById('MSG').innerText = "입고일을 가져오지 못했습니다.";
			alert("입고일을 가져오지 못했습니다."); 
			return;
		}
	
		var date_OUT = new Date(OUTDATE);
		var date_IN = new Date(DIC_DATE);
		if(date_OUT > date_IN){
			document.getElementById('MSG').innerText = "출고일 이후 날짜를 입력해야 합니다.";
			alert("출고일 이후 날짜를 입력해야 합니다."); 
			return;
		}
	}
	var form = document.cfmForm;
	form.submit();
}
function cancel() {
	var form = document.cfmForm;
	form.action = "Cfm_List_Search.do";
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="cfmForm" id="cfmForm" method="post" action="CFM_ADD_Save.do" enctype="multipart/form-data">
		<input type="hidden" name="menuGroup" value="menu_Inout" id="menuGroup">
		<input type="hidden" name="DOCNO" value="${CFMINFO.DOCNO}" id="DOCNO">
		<input type="hidden" name="INTYPE" value="${INTYPE}" id="INTYPE">
		<input type="hidden" name="CFMsts" value="${CFMINFO.CFMsts}" id="CFMsts">
		<input type="hidden" name="PAGE" value="${PAGE}">
		<input type="hidden" name="LOGC" value="${LOGC}">
		<input type="hidden" name="BONBU" value="${BONBU}">
		<input type="hidden" name="CENTER" value="${CENTER}">
		<input type="hidden" name="INSTS" value="${INSTS}">
		<input type="hidden" name="CFMSTS" value="${CFMSTS}">
		<input type="hidden" name="StartDay" value="${StartDay}">
		<input type="hidden" name="EndDay" value="${EndDay}">
		<input type="hidden" name="MNAME" value="${MNAME}">
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

			<div class="subtitle" style="text-align: right;">입출고 관리 > 본부 입고 승인 > 입고 승인</div>
			<h2>입고 승인</h2>
			
			<div class="scroll_table" id="Table_MLIST">
				<table>
					<thead>
						<tr>
							<th style="width: 25%;">상품코드</th>
							<th style="width: 25%;">대분류</th>
							<th style="width: 25%">중분류</th>
							<th style="width: 25%">소분류</th>
						</tr>
					</thead>
					<tbody>
						<tr style="height: 40px;">
							<td>${CFMINFO.MODEL}</td>
							<td>${CFMINFO.MMC_LGROUPNAME}</td>
							<td>${CFMINFO.MMC_MGROUP}</td>
							<td>${CFMINFO.MMC_SGROUP}</td>	 	
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 25%;">접수번호</th>
							<td style="text-align: center;" colspan="3">
								${CFMINFO.DOCNO}
							</td>
						</tr>
						<c:choose>
						<c:when test="${INTYPE == '1'}">
						<tr style="height: 40px">
							<th style="width: 25%;">발주 본부</th>
							<td style="width: 25%;text-align: center;">
								${CFMINFO.INBONBU_NAME}&nbsp;${CFMINFO.INCENTER_NAME}
							</td>
							<th style="width: 25%;text-align: center;">
								발주자
							</th>
							<td style="width: 25%;text-align: center;">
								${CFMINFO.DBO_USERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">발주일</th>
							<td style="width: 25%;text-align: center;">
								${CFMINFO.INDATE}
							</td>
							<th style="width: 25%;text-align: center;">
								발주요청수량
							</th>
							<td style="width: 25%;text-align: center;">
								${CFMINFO.INQTY}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">비 고</th>
							<td style="text-align: center;" colspan="3">
								${CFMINFO.DBO_CMNT}
							</td>
						</tr>
						</c:when>
						<c:otherwise>
						<tr style="height: 40px">
							<th style="width: 25%;">입고본부</th>
							<td style="text-align: center;" colspan="3">
								${CFMINFO.INBONBU_NAME}&nbsp;${CFMINFO.INCENTER_NAME}
							</td>
						</tr>
						</c:otherwise>
						</c:choose>
						<tr style="height: 40px">
							<th style="width: 25%;">출고본부</th>
							<td style="text-align: center;" colspan="3">
								${CFMINFO.OUT_BONBU}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">출고일</th>
							<td style="width: 25%;text-align: center;">
								${CFMINFO.OUTDATE}
								<input type="hidden" name="OUTDATE" value="${CFMINFO.OUTDATE}" id="OUTDATE">
							</td>
							<th style="width: 25%;text-align: center;">
								출고수량
							</th>
							<td style="width: 25%;text-align: center;">
								${CFMINFO.OUTQTY}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">입고일</th>
							<td style="width: 25%;text-align: center;">
								<c:choose>
								<c:when test="${CFMINFO.CFMsts == 'N'}">
								<input type='text' name=DIC_DATE id='DIC_DATE' value="${CFMINFO.OUTDATE}" style='width:90%;' readonly='readonly' />
								</c:when>
								<c:otherwise>
								${CFMINFO.DIC_DATE}
								</c:otherwise>
								</c:choose>
							</td>
							<th style="width: 25%;text-align: center;">
								입고 승인자
							</th>
							<td style="width: 25%;text-align: center;">
								${CFMINFO.DIC_USERNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<th style="width: 25%;">특이사항</th>
							<td style="text-align: center;" colspan="3">
								<input type="text" name="DIC_CMNT" id="DIC_CMNT" value="${CFMINFO.DIC_CMNT}"  style="width:90%;">
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
						<c:if test="${not empty FileList}">
						<c:forEach var="FileList" items="${FileList}" varStatus="st">
						<tr style="height: 40px;">
							<td>
								<a href="javascript:CFM_FILE_DEL('${FileList.MDF_DOCNO}','${FileList.MDF_SEQ}', 'btn_delfile${FileList.MDF_SEQ}');" id='btn_delfile${FileList.MDF_SEQ}' class="search grey button">삭 제</a>
							</td>
							<td><a href="download.do?sysFile=${FileList.MDF_FILE}&orgFile=${FileList.MDF_FILE_Org}">${FileList.MDF_FILE_Org}</a></td>
							<td>${FileList.MDF_FILE_SIZE} MB</td> 	
						</tr>
						</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<c:choose>
				<c:when test="${CFMINFO.CFMsts == 'N'}">
				<a href="javascript:save();" class="search grey button">입고하기</a>
				</c:when>
				<c:otherwise>
				<a href="javascript:save();" class="search grey button">저 장</a>
				</c:otherwise>
				</c:choose>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>