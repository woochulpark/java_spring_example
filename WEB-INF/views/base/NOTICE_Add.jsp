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
var ArraySUSIN = new Array();
//jquery ready
$(function() {
	SETPAGE();
});
function SETPAGE(){
	var TYPE = document.getElementById("TYPE").value;
	var DOCNO = document.getElementById("DOCNO").value;
	if(TYPE == 'M' && DOCNO.length > 0){
		setReceiveType();
	}
}
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
// 등록할 전체 파일 사이즈
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
		var form = $('#noticeForm');
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
function save(TEMPSTS)
{
	if(totalFileSize > maxUploadSize){
		document.getElementById('MSG').innerText = "100MB이상 업로드할 수 없습니다.";
		alert("100MB이상 업로드할 수 없습니다.");
		return;
	}
	var REC_x = document.getElementById("RECEIVETYPE").selectedIndex;
	var REC_y = document.getElementById("RECEIVETYPE").options;
	var REC_idx = REC_y[REC_x].index;
	if(REC_idx == 1)
	{
		var LOGC_x = document.getElementById("LOGC").selectedIndex;
		var LOGC_y = document.getElementById("LOGC").options;
		var LOGC_idx = LOGC_y[LOGC_x].index;
		if(LOGC_idx == 0)
		{
			document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
			document.getElementById('LOGC').focus();
			alert("물류센터를 선택해주세요.");
			return;
		}
	}
	else if(REC_idx == 2){
		var len = $('#susin_table tbody tr').length;
		if(len == 0){
			document.getElementById('MSG').innerText = "수신자를 추가해주세요.";
			alert("수신자를 추가해주세요.");
			return;
		}
	}
	if(document.getElementById('TITLE').value == "" || document.getElementById('TITLE').value == null){
		document.getElementById('MSG').innerText = "제목을 입력해주세요.";
		document.getElementById('TITLE').focus();
		alert("제목을 입력해주세요.");
		return;
	}
	document.getElementById('TEMPSTS').value = TEMPSTS;
	
	var form = document.noticeForm;
	form.submit();

}
function Notice_delete() {
	var del_chk = confirm('공지사항을 삭제하시겠습니까?');
	if(del_chk==true){
		var form = document.noticeForm;
		form.action = "Notice_Add_Del.do";
		form.submit();
	}
}
function cancel() {
	var form = document.noticeForm;
	form.action = "NOTICE_LIST.do";
	form.submit();
}
function getBONBU(obj){
	$("#CENTER").empty();
	var option = "<option value=''>선택</option>";
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
function getCENTER(obj){
	var LOGC = document.getElementById("LOGC").value;
	$.ajax({
		type:"GET",
		url:"Ajax_CENTER.do",
		data: "LOGC="+LOGC+"&BONBU="+obj.value,
		success:function(html){
			$("#td_C").empty();
			$("#td_C").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function POP_getBONBU(obj){
	$("#POP_CENTER").empty();
	var option = "<option value=''>선택</option>";
    $('#POP_CENTER').append(option);
	$.ajax({
		type:"GET",
		url:"Ajax_POP_BONBU.do",
		data: "LOGC="+obj.value,
		success:function(html){
			$("#td_POP_B").empty();
			$("#td_POP_B").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function POP_getCENTER(obj){
	var POP_LOGC = document.getElementById("POP_LOGC").value;
	$.ajax({
		type:"GET",
		url:"Ajax_POP_CENTER.do",
		data: "LOGC="+POP_LOGC+"&BONBU="+obj.value,
		success:function(html){
			$("#td_POP_C").empty();
			$("#td_POP_C").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function setReceiveType(){
	var RECEIVETYPE = document.getElementById("RECEIVETYPE").value;
	if(RECEIVETYPE != '2'){
		var len = $('#susin_table tbody tr').length;
		if(len > 0){
			var susin_chk = confirm('수신자를 모두 삭제하고 지정여부를 변경하시겠습니까?');
			if(susin_chk==true){
				$("#susin_table").find('tbody').empty();
			}
			else{
				document.getElementById("RECEIVETYPE")[2].selected = true;
			}
		}
	}
	if(RECEIVETYPE == '0'){
		document.getElementById("DIV_CENTER").style.display = 'none';
		document.getElementById("DIV_SUSIN").style.display = 'none';
	}
	else if(RECEIVETYPE == '1'){
		document.getElementById("DIV_CENTER").style.display = 'block';
		document.getElementById("DIV_SUSIN").style.display = 'none';
	}
	else if(RECEIVETYPE == '2'){
		document.getElementById("DIV_CENTER").style.display = 'none';
		document.getElementById("DIV_SUSIN").style.display = 'block';
	}
}

function pop_search(PAGE){
	var POP_LOGC = document.getElementById("POP_LOGC").value;
	var POP_BONBU = document.getElementById("POP_BONBU").value;
	var POP_CENTER = document.getElementById("POP_CENTER").value;
	var POP_UNAME = document.getElementById("POP_UNAME").value;
	$.ajax({
		type:"POST",
		url:"Ajax_POP_User.do",
		data: "POP_LOGC="+POP_LOGC+"&POP_BONBU="+POP_BONBU+"&POP_CENTER="+POP_CENTER+"&POP_UNAME="+POP_UNAME+"&PAGE="+PAGE,
		success:function(html){
			$("#pop_table").empty();
			$("#pop_table").append(html);
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
function SUSIN_ADD(){
	var $el = $("#layer_SUSIN");
    var isDim = $el.prev().hasClass('dimBg');//dimmed 레이어를 감지하기 위한 boolean 변수

    isDim ? $('.dim-layer').fadeIn() : $el.fadeIn();

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
    
    $el.find("#BTN_POP_SAVE").click(function(){
    	var checkbox = $("input[name=SUSIN_CHK]:checked");
    	checkbox.each(function (i) {
    	    var tr = checkbox.parent().parent().eq(i);  // checkbox 태그의 두 단계 상위 태그가 tr이기 때문에
    	    var td = tr.children();  // td 태그는 tr 태그의 하위에 있으므로
    	 
    	    var USERID = $(this).val();
    	    var BONBU = td.eq(1).text();
    	    var CENTER = td.eq(2).text();
    	    var POSI = td.eq(3).text();
    	    var NAME = td.eq(4).text();
    	    
    	    if($.inArray(USERID, ArraySUSIN) != -1){
    	    	return true
    	    }
    	    
    	    var html = "";
    	    html += "<tr style='height: 40px;'>";
    	    html += "<td><input type='checkbox' name='SUSIN_DEL' class='SUSIN_DEL' style='width:90%;'/><input type='hidden' name='SUSINID' value='" + USERID + "' >";
    	    html += "<td>" + BONBU + "</td>";
    	    html += "<td>" + CENTER + "</td>";
    	    html += "<td>" + POSI + "</td>";
    	    html += "<td>" + NAME + "</td>";
    	    html += "</tr>";
    	    ArraySUSIN.push(USERID);
    	    $('#susin_table > tbody:last').append(html);
    	});
        isDim ? $('.dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });

    $el.find("#BTN_POP_CLOSE").click(function(){
        isDim ? $('.dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });

    $('.layer .dimBg').click(function(){
        $('.dim-layer').fadeOut();
        return false;
    });
}
function SUSIN_DELETE(){
	var SUSIN_DEL = $("[name=SUSIN_DEL]:checked");
	for(var i=SUSIN_DEL.length-1; i>-1; i--){
	    var tr = SUSIN_DEL.parent().parent().eq(i);  // checkbox 태그의 두 단계 상위 태그가 tr이기 때문에
	    var td = tr.children();  // td 태그는 tr 태그의 하위에 있으므로
	    var USERID = td.eq(0).find('input[type="hidden"]').val();
	    console.log('USERID = ' + USERID);
	    ArraySUSIN.splice($.inArray(USERID, ArraySUSIN),1);
	    tr.remove();
	}
}
function allCheck(){
	if($("#pop_all").is(":checked")){
        $(".SUSIN_CHK").prop("checked", true);
    }
    else{
        $(".SUSIN_CHK").prop("checked", false);
    }
}
function delallCheck(){
	if($("#DEL_ALL").is(":checked")){
        $(".SUSIN_DEL").prop("checked", true);
    }
    else{
        $(".SUSIN_DEL").prop("checked", false);
    }
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
function NOTICE_FILE_DEL(DOCNO, SEQ, id){
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
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="noticeForm" id="noticeForm" method="post" action="Notice_Add_Save.do" enctype="multipart/form-data">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="TYPE" value="${TYPE}" id="TYPE">
		<input type="hidden" name="DOCNO" value="${DOCNO}" id="DOCNO">
		<input type="hidden" name="TEMPSTS" value="" id="TEMPSTS">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 공지사항 > ${PAGE}</div>
			<h2>${PAGE}</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">제 목</td>
							<td style="width: 70%;text-align: left;">
								<input type="text" name="TITLE" id="TITLE" value="${NoticeInfo.MNI_TITLE}"  style="width:90%;"/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">작성자</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
            						<c:when test="${TYPE == 'M'}">
            							${NoticeInfo.MUT_UserName}
            						</c:when>
            						<c:otherwise>
            							${UNAME}
            						</c:otherwise>
            					</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">팝업공지여부</td>
							<td style="width: 70%;text-align: left;">
								<input type="radio" name = "POPUP" value = "Y" <c:if test="${NoticeInfo.MNI_POPUP == 'Y'}">checked="checked"</c:if>> 공지
								<input type="radio" name = "POPUP" value = "N" <c:if test="${NoticeInfo.MNI_POPUP == 'N' or empty NoticeInfo.MNI_POPUP}">checked="checked"</c:if>> 공지안함
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">게시판 상단공지</td>
							<td style="width: 70%;text-align: left;">
								<input type="radio" name = "TOPNOTICE" value = "0" <c:if test="${NoticeInfo.MNI_TopNotice == '0'}">checked="checked"</c:if>> 등록
								<input type="radio" name = "TOPNOTICE" value = "1" <c:if test="${NoticeInfo.MNI_TopNotice == '1' or empty NoticeInfo.MNI_TopNotice}">checked="checked"</c:if>> 등록안함
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">지정여부</td>
							<td style="width: 70%;text-align: left;" onchange="setReceiveType()">
								<select style="width: 90%;" name="RECEIVETYPE" id = "RECEIVETYPE">
									<option value="0" <c:if test="${NoticeInfo.MNI_ReceiveType == '0'}">selected="selected"</c:if>>전체</option>
									<option value="1" <c:if test="${NoticeInfo.MNI_ReceiveType == '1'}">selected="selected"</c:if>>본부</option>
									<option value="2" <c:if test="${NoticeInfo.MNI_ReceiveType == '2'}">selected="selected"</c:if>>지정</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- START TABLE -->
			<div class="scroll_table" id="DIV_CENTER" style="display:none">
				<table>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								<select style="width: 90%;" name="LOGC" id="LOGC"  onchange="getBONBU(this)">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<c:if test="${NoticeInfo.MCI_LOGC == LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}" selected="selected">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
									<c:if test="${NoticeInfo.MCI_LOGC != LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
								</c:forEach>
								</select>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">본 부</td>
							<td style="width: 70%;text-align: left;" id = 'td_B'>
								<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
								<option value="">선택</option>
								<c:if test="${not empty BONBULIST}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
									<c:if test="${NoticeInfo.MCI_Bonbu == BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}" selected="selected">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
									<c:if test="${NoticeInfo.MCI_Bonbu != BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
								</c:forEach>
								</c:if>
								</select>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">센 터</td>
							<td style="width: 70%;text-align: left;"  id = 'td_C'>
								<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:if test="${not empty CENTERLIST}">
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
									<c:if test="${NoticeInfo.MCI_Center == CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}" selected="selected">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
									<c:if test="${NoticeInfo.MCI_Center != CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
								</c:forEach>
								</c:if>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" id="DIV_SUSIN" style="margin:5px;display:none;height: 250px;overflow: auto;background-color: #fff;">
				<div>
					<a href="javascript:SUSIN_ADD();" class="search grey button">추 가</a>
					<a href="javascript:SUSIN_DELETE();" class="search grey button">삭 제</a>
				</div>
				<table id='susin_table'>
					<thead>
						<tr>
							<th style="width: 5%;"><input type="checkbox" name="DEL_ALL" id="DEL_ALL" style="width:90%;" onClick="javascript:delallCheck();"/></th>
							<th style="width: 25%;">본부</th>
							<th style="width: 25%;">센터</th>
							<th style="width: 20%">직급</th>
							<th style="width: 25%">이름</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${SusinListSize > 0 }">
					<c:forEach var="SusinList" items="${SusinList}" varStatus="st">
						<tr style="height: 40px;">
							<td>
								<input type="checkbox" name="SUSIN_DEL" class="SUSIN_DEL" style="width:90%;"/>
								<input type="hidden" name="SUSINID" value="${SusinList.MUT_USERID}" >
							</td>
							<td>${SusinList.MCI_BonbuNAME}</td>
							<td>${SusinList.MCI_CenterName}</td> 	
							<td>${SusinList.POSITIONNAME}</td>
							<td>${SusinList.MUT_USERNAME}</td> 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<div class="scroll_table" style="text-align: center;">
				<textarea class="addr" name="CMNT">${NoticeInfo.MNI_Cmnt}</textarea>
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
								<a href="javascript:NOTICE_FILE_DEL('${FileList.MDF_DOCNO}','${FileList.MDF_SEQ}', 'btn_delfile${FileList.MDF_SEQ}');" id='btn_delfile${FileList.MDF_SEQ}' class="search grey button">삭 제</a>
							</td>
							<td><a href="download.do?sysFile=${FileList.MDF_FILE}&orgFile=${FileList.MDF_FILE_Org}">${FileList.MDF_FILE_Org}</a></td>
							<td>${FileList.MDF_FILE_SIZE} MB</td> 	
						</tr>
						</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
			<div class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_SUSIN" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="searchfield">
						<table style="width: 80%;">
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">물류센터</td>
								<td style="width: 70%;text-align: left;">
									<select style="width: 90%;" name="POP_LOGC" id="POP_LOGC"  onchange="POP_getBONBU(this)">
									<option value="">선택</option>
									<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:forEach>
									</select>
								</td>
							</tr>
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">본 부</td>
								<td style="width: 70%;text-align: left;" id = 'td_POP_B'>
									<select style="width: 90%;" name="POP_BONBU" id = "POP_BONBU" onchange="POP_getCENTER(this)">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">센 터</td>
								<td style="width: 70%;text-align: left;"  id = 'td_POP_C'>
									<select style="width: 90%;" name="POP_CENTER" id = "POP_CENTER">
										<option value="">선택</option>
									</select>
								</td>
							</tr>
							<tr style="height: 40px">
								<td style="width: 30%;text-align: center;">이 름</td>
								<td style="width: 70%;text-align: left;">
									<input type="text" name="POP_UNAME" id="POP_UNAME" value=""  style="width:90%;"/>
								</td>
							</tr>
							<tr>
								<td colspan="2"  style="text-align: center;">
									<a href="javascript:pop_search('1');" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table">
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_SAVE" class="search grey button">확인</a>
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종료</a>
					</div>
    			</div>
			</div>

			<div style="text-align: center;">
				<c:if test="${userInfo.MUT_POSITION == '001' or userInfo.MUT_POSITION == '002'}">
				<c:if test="${TYPE == 'M'}">
					<a href="javascript:Notice_delete();" class="search grey button">삭제</a>
				</c:if>
				<a href="javascript:save('Y');" class="search grey button">저장</a>
				<c:if test="${(TYPE == 'M' && NoticeInfo.MNI_TempSts == 'N') || TYPE == 'I'}">
					<a href="javascript:save('N');" class="search grey button">임시저장</a>
				</c:if>
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