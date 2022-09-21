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
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
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
	$("#EndDay").datepicker();
});
function fileCheck(obj) {
    pathpoint = obj.value.lastIndexOf('.');
    filepoint = obj.value.substring(pathpoint+1,obj.length);
    filetype = filepoint.toLowerCase();
    var filePath = obj.value;
	//전체경로를 \ 나눔.
	var filePathSplit = filePath.split('\\'); 
	//전체경로를 \로 나눈 길이.
	var filePathLength = filePathSplit.length;
	//마지막 경로를 .으로 나눔.
	var fileNameSplit = filePathSplit[filePathLength-1].split('.');
	//파일명 : .으로 나눈 앞부분
	var fileName = fileNameSplit[0];
	//파일 확장자 : .으로 나눈 뒷부분
	var fileExt = fileNameSplit[1];
	//파일 크기
	var fileSize = obj.files[0].size;
	var maxSize = 10*1024*1024;
	if(fileSize > maxSize) {
		document.getElementById("MSG").innerText = '10메가바이트 이상 업로드할 수 없습니다.';
    	var newitem = "<input name='file' type='file' class='file' id='imgInp' onchange='fileCheck(this)'/>";
    	$("#div_file").empty();
		$("#div_file").append(newitem);
        return false;
	}
	
    if(filetype=='jpg' || filetype=='gif' || filetype=='png' || filetype=='jpeg' || filetype=='bmp') {
    	document.getElementById('file_path').value = fileNameSplit;
    } else {
    	document.getElementById("MSG").innerText = '이미지 파일만 선택할 수 있습니다.';
    	var newitem = "<input name='file' type='file' class='file' id='imgInp' onchange='fileCheck(this)'/>";
    	$("#div_file").empty();
		$("#div_file").append(newitem);
        return false;
    }
}
function save()
{
	if(document.getElementById("LOGC") != null){
		var LOGC_x = document.getElementById("LOGC").selectedIndex;
		var LOGC_y = document.getElementById("LOGC").options;
		var LOGC_idx = LOGC_y[LOGC_x].index;
		if(LOGC_idx == 0)
		{
			document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
			document.getElementById('LOGC').focus();
			return;
		}
	}
	if(document.getElementById("BONBU") != null){
		var BONBU_x = document.getElementById("BONBU").selectedIndex;
		var BONBU_y = document.getElementById("BONBU").options;
		var BONBU_idx = BONBU_y[BONBU_x].index;
		if(BONBU_idx == 0)
		{
			document.getElementById('MSG').innerText = "본부를 선택해주세요.";
			document.getElementById('BONBU').focus();
			return;
		}
	}
	if(document.getElementById("CENTER") != null){
		var CENTER_x = document.getElementById("CENTER").selectedIndex;
		var CENTER_y = document.getElementById("CENTER").options;
		var CENTER_idx = CENTER_y[CENTER_x].index;
		if(CENTER_idx == 0)
		{
			document.getElementById('MSG').innerText = "센터를 선택해주세요.";
			document.getElementById('CENTER').focus();
			return;
		}
	}
	
	if(document.getElementById("POSITION") != null){
		var POSITION_x = document.getElementById("POSITION").selectedIndex;
		var POSITION_y = document.getElementById("POSITION").options;
		var POSITION_idx = POSITION_y[POSITION_x].index;
		if(POSITION_idx == 0)
		{
			document.getElementById('MSG').innerText = "직급을 선택해주세요.";
			document.getElementById('POSITION').focus();
			return;
		}
	}
	
	if(document.getElementById('PASS').value != document.getElementById('PASSCHK').value){
		document.getElementById('MSG').innerText = "패스워드가 일치하지 않습니다.";
		document.getElementById('PASS').focus();
		return;
	}
	
	var form = document.userForm;
	form.submit();
	
}
function User_delete() {
	var del_chk = confirm('사용자를 삭제하시겠습니까?');
	if(del_chk==true){
		var form = document.userForm;
		form.action = "User_Add_Del.do";
		form.submit();
	}
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
function IDCHECK(){
	var USERID = document.getElementById("USERID").value;
	$.ajax({
		type:"POST",
		url:"Ajax_IDCHECK.do",
		data: "USERID="+USERID,
		success:function(data){
			if(data == null || data == '' || data == '-1'){
				document.getElementById('MSG').innerText = "DB에러! 중복체크 실패";
			}
			else if(data != '0'){
				document.getElementById('MSG').innerText = "중복된 ID입니다.";
			}
			else{
				document.getElementById('MSG').innerText = "사용할 수 있는 ID입니다.";
			}
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="userForm" method="post" action="User_Modify_Save.do" enctype="multipart/form-data">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="USERID" value="${UserInfo.MUT_USERID}" id="USERID">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 사용자 관리 > 사용자 수정</div>
			<h2>사용자 수정</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">사번</td>
							<td style="width: 70%;text-align: left;">
								${UserInfo.MUT_USERID}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">이름</td>
							<td style="width: 70%;text-align: left;">
								<input type="text" name="UNAME" id="UNAME" value="${UserInfo.MUT_USERNAME}"  style="width:90%;"/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">패스워드</td>
							<td style="width: 70%;text-align: left;">
								<input type="password" name="PASS" id="PASS" value=""  style="width:40%;"/>
								확인
								<input type="password" name="PASSCHK" id="PASSCHK" value=""  style="width:40%;"/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${level == '001' or  level == '002'}">
								<select style="width: 90%;" name="LOGC" id="LOGC"  onchange="getBONBU(this)">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<c:if test="${UserInfo.MCI_LOGC == LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}" selected="selected">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
									<c:if test="${UserInfo.MCI_LOGC != LOGCLIST.MCI_LOGC}">
										<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:if>
								</c:forEach>
								</select>
								</c:when>
								<c:otherwise>
									${UserInfo.MCI_LOGCNAME}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">본 부</td>
							<td style="width: 70%;text-align: left;" id = 'td_B'>
								<c:choose>
								<c:when test="${level == '001' or  level == '002'}">
								<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
								<option value="">선택</option>
								<c:if test="${not empty BONBULIST}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
									<c:if test="${UserInfo.MCI_Bonbu == BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}" selected="selected">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
									<c:if test="${UserInfo.MCI_Bonbu != BONBULIST.MCI_Bonbu}">
										<option value="${BONBULIST.MCI_Bonbu}">${BONBULIST.MCI_BonbuNAME}</option>
									</c:if>
								</c:forEach>
								</c:if>
								</select>
								</c:when>
								<c:otherwise>
									${UserInfo.MCI_BonbuNAME}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">센 터</td>
							<td style="width: 70%;text-align: left;"  id = 'td_C'>
								<c:choose>
								<c:when test="${level == '001' or  level == '002'}">
								<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:if test="${not empty CENTERLIST}">
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
									<c:if test="${UserInfo.MCI_Center == CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}" selected="selected">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
									<c:if test="${UserInfo.MCI_Center != CENTERLIST.MCI_Center}">
										<option value="${CENTERLIST.MCI_Center}">${CENTERLIST.MCI_CenterName}</option>
									</c:if>
								</c:forEach>
								</c:if>
								</select>
								</c:when>
								<c:otherwise>
									${UserInfo.MCI_CenterName}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">전화번호</td>
							<td style="width: 70%;text-align: left;"><input type='text' name="HP" id="HP" value="${UserInfo.MUT_HP_TEL}"  style="width:90%;"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">직 급</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${level == '001' or  level == '002'}">
								<select style="width: 90%;" name="POSITION" id="POSITION">
								<option value="">선택</option>
								<c:forEach var="POSITION_LIST" items="${POSITION_LIST}" varStatus="st">
									<c:if test="${UserInfo.MUT_POSITION == POSITION_LIST.MCC_S_CODE}">
										<option value="${POSITION_LIST.MCC_S_CODE}" selected="selected">${POSITION_LIST.MCC_S_NAME}</option>
									</c:if>
									<c:if test="${UserInfo.MUT_POSITION != POSITION_LIST.MCC_S_CODE}">
										<option value="${POSITION_LIST.MCC_S_CODE}">${POSITION_LIST.MCC_S_NAME}</option>
									</c:if>
								</c:forEach>
								</select>
								</c:when>
								<c:otherwise>
									${UserInfo.POSITIONNAME}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">시작일</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${level == '001' or  level == '002'}">
								<input type="text" name="StartDay" id="StartDay" value="${UserInfo.MUT_STARTDAY}"  style="width:90%;" readonly="readonly">
								</c:when>
								<c:otherwise>
									${UserInfo.MUT_STARTDAY}
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">종료일</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${level == '001' or  level == '002'}">
								<input type="text" name="EndDay" id="EndDay" value="${UserInfo.MUT_ENDDAY}"  style="width:90%;" readonly="readonly">
								</c:when>
								<c:otherwise>
									${UserInfo.MUT_ENDDAY}
								</c:otherwise>
								</c:choose>	
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">근무구분</td>
							<td style="width: 70%;text-align: left;">
								<c:choose>
								<c:when test="${level == '001' or  level == '002'}">
								<input type="radio" name = "BizSts" value = "Y" <c:if test="${UserInfo.MUT_BIZSTS == 'Y' or empty UserInfo.MUT_BIZSTS}">checked="checked"</c:if>> 근무중
								<input type="radio" name = "BizSts" value = "N" <c:if test="${UserInfo.MUT_BIZSTS == 'N'}">checked="checked"</c:if>> 퇴직
								</c:when>
								<c:otherwise>
									<c:if test="${UserInfo.MUT_BIZSTS == 'Y'}">근무중</c:if>
									<c:if test="${UserInfo.MUT_BIZSTS == 'N'}">퇴직</c:if>
								</c:otherwise>
								</c:choose>	
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">Sign</td>
							<td style="width: 70%;text-align: left;">
								<input type="hidden" name="SIGN_ORG_FILE" value="${UserInfo.MUT_SIGN_ORG}"/>
								<input type="hidden" name="SIGN_FILE" value="${UserInfo.MUT_SIGN}"/>
								<input id="file_path" name="file_path" type="text" style="width:300px; height:22px;" value="${UserInfo.MUT_SIGN_ORG}" readonly="readonly">
								<div style="float: left;" id="div_file">
									<input name="file" type="file" class="file" id="imgInp" onchange="fileCheck(this)"/>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<c:if test="${UserInfo.MUT_SIGN != null && UserInfo.MUT_SIGN != ''}">
			<div style="width: 30%;height: auto;overflow: hidden;">
				<div style="width: 200px; height:auto; margin:1em;">
					<img id="preview" src="display.do?PATH=SIGN&FILE=${UserInfo.MUT_SIGN}&ORGFILE=${UserInfo.MUT_SIGN_ORG}" style="width: 100%; height:auto" onerror="this.src='images/img_blank.jpg'" />
				</div>
				<div id="map-canvas" style="width: 300px; height: 200px;margin:1em;"></div>
			</div>
			</c:if>
			<div style="text-align: center;">
				<c:if test="${level == '001' or  level == '002'}">
				<a href="javascript:User_delete();" class="search grey button">삭제</a>
				</c:if>
				<a href="javascript:save();" class="search grey button">저장</a>
				<a href="javascript:history.go(-1);" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
		</div>
	</form:form>
</body>
</html>