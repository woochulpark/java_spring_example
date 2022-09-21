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
	$("#EndDay").datepicker();
});
var PMSG = '${msg}';
if(PMSG.length > 0){
	alert(PMSG);
}
function save()
{
	if(document.getElementById('TYPE').value == "I"){
		var KINDS_radio = document.getElementsByName('KINDS');
		var sel_KINDS = null;
		for(var i=0;i<KINDS_radio.length;i++){
			if(KINDS_radio[i].checked == true){ 
				sel_KINDS = KINDS_radio[i].value;
			}
		}
		if(sel_KINDS == null){
			document.getElementById('MSG').innerText = "구분을 선택하세요.";
    	    alert("구분을 선택하세요."); 
			return false;
		}
	
		var LOGC_x = document.getElementById("LOGC").selectedIndex;
		var LOGC_y = document.getElementById("LOGC").options;
		var LOGC_idx = LOGC_y[LOGC_x].index;
		if(LOGC_idx == 0)
		{
			document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
			alert("물류센터를 선택해주세요."); 
			document.getElementById('LOGC').focus();
			return;
		}
	
		var LCODE_x = document.getElementById("LCODE").selectedIndex;
		var LCODE_y = document.getElementById("LCODE").options;
		var LCODE_idx = LCODE_y[LCODE_x].index;
		if(LCODE_idx == 0)
		{
			document.getElementById('MSG').innerText = "대분류를 선택해주세요.";
			alert("대분류를 선택해주세요."); 
			document.getElementById('LCODE').focus();
			return;
		}
	}
	
	var form = document.modelForm;
	form.submit();
	
}
function Mdelete() {
	var del_chk = confirm('상품코드를 삭제하시겠습니까?');
	if(del_chk==true){
		var form = document.modelForm;
		form.action = "MODEL_Add_Del.do";
		form.submit();
	}
}
function cancel() {
	var form = document.modelForm;
	if(document.getElementById('TYPE').value == "I"){
		form.action = "ModelList.do";
	}
	else{
		form.action = "MODEL_DETAIL.do";
	}
	form.submit();
}
function getLLIST(){
	var option = "<option value=''>선택</option>";
	$("#LCODEList option:eq(0)").prop("selected", true);
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
    $("#MCODEList").empty();
    $('#MCODEList').append(option);
}
function getMLIST(obj){
	var option = "<option value=''>선택</option>";
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
	var LOGC = $("#pop_LOGC option:selected").val();
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
	var LOGC = $("#pop_LOGC option:selected").val();
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
function Ajax_ModelList_search(PAGE){
	var LCODEList = $("#LCODEList option:selected").val();
	var MCODEList = $("#MCODEList option:selected").val();
	var SCODEList = $("#SCODEList option:selected").val();
	var LOGC = $("#pop_LOGC option:selected").val();
	var MNAME = $("#MNAME").val();
	var MCODENAME = $("#MCODENAME").val();
	var SCODENAME = $("#SCODENAME").val();
	$.ajax({
		type:"GET",
		url:"Ajax_ModelList_search.do",
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

function MODEL_VAL(KIND, LOGC, LGROUP, MGROUP, SGROUP, UINT, ERPMODEL, STARTDAY, ENDDAY, BISSTS, CMNT){
	$("input:radio[name=KINDS][value=" + KIND + "]").prop('checked', true);
	$("#LOGC").val(LOGC).attr("selected", "selected");
	$("#LCODE").val(LGROUP).attr("selected", "selected");
	$("#MGROUP").val(MGROUP);
	$("#SGROUP").val(SGROUP);
	$("#UNIT").val(UINT);
	$("#ERPMODEL").val(ERPMODEL);
	$("#StartDay").val(STARTDAY);
	$("#EndDay").val(ENDDAY);
	$("input:radio[name=BizSts][value=" + BISSTS + "]").prop('checked', true);
	$("#CMNT").val(CMNT);
	$('#dim-layer').fadeOut();
}

function barcode(){
	var MCODE = document.getElementById("MCODE").value;
	$.ajax({
		type:"POST",
		url:"Ajax_ViewImage.do",
		data: "MCODE="+MCODE,
		success:function(data){
			if(data == '-1'){
				document.getElementById('MSG').innerText = "상품코드 정보를 불러오지 못했습니다.";
				alert("상품코드 정보를 불러오지 못했습니다.");
			}
			else if(data == '-2' || data == '-3' ){
				document.getElementById('MSG').innerText = "라벨 이미지를 가져오지 못했습니다.";
				alert("라벨 이미지를 가져오지 못했습니다.");
			}
			else{
				var imgtag = "<img id='preview' src='display.do?PATH=LABEL&FILE=" + MCODE + ".jpg&ORGFILE=" + MCODE + ".jpg' style='width: 100%; height:auto' >";
				$("#div_img").empty();
				$("#div_img").append(imgtag);
				$("#BTN_DOWN").attr("href", "download.do?sysFile=" + MCODE + ".jpg&orgFile=" + MCODE + ".jpg&PATH=LABEL")
			}
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	
	var $el = $("#layer_Img");
    var isDim = $el.prev().hasClass('dimBg');//dimmed 레이어를 감지하기 위한 boolean 변수

    isDim ? $('#Bar_layer').fadeIn() : $el.fadeIn();

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

    $el.find("#BTN_CLOSE").click(function(){
        isDim ? $('#Bar_layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });

    $('.layer .dimBg').click(function(){
        $('#Bar_layer').fadeOut();
        return false;
    });
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="modelForm" method="post" action="MODEL_Add_Save.do" >
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="TYPE" value="${TYPE}" id="TYPE">
		<input type="hidden" name="MCODE" value="${MCODE}" id="MCODE">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 상품코드 관리 > ${PAGE}</div>
			<h2>${PAGE}</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">구 분</td>
							<td style="width: 70%;text-align: left;">
							<c:if test="${TYPE == 'M'}">
								 ${ModelInfo.MMC_KINDSNAME}
							</c:if>
							<c:if test="${TYPE == 'I'}">
							<c:forEach var="MKINDLIST" items="${MKINDLIST}" varStatus="st">
								<input type="radio" name = "KINDS" value = "${MKINDLIST.MCC_S_CODE}" <c:if test="${st.index == 0}">checked="checked"</c:if>> ${MKINDLIST.MCC_S_NAME}
							</c:forEach>
							<a href="javascript:pop_Model();" class="search grey button" >코드조회</a>	
							</c:if>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								<c:if test="${TYPE == 'I'}">
								<select style="width: 90%;" name="LOGC" id="LOGC">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
								</c:forEach>
								</select>
								</c:if>
								<c:if test="${TYPE == 'M'}">
									${ModelInfo.MMC_LOGCNAME}
								</c:if>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">대분류</td>
							<td style="width: 70%;text-align: left;">
								<c:if test="${TYPE == 'I'}">
								<select style="width: 90%;" name="LCODE" id="LCODE">
								<option value="">선택</option>
								<c:forEach var="LCODELIST" items="${LCODELIST}" varStatus="st">
									<option value="${LCODELIST.MCC_S_CODE}">${LCODELIST.MCC_S_NAME}</option>
								</c:forEach>
								</select>
								</c:if>
								<c:if test="${TYPE == 'M'}">
									${ModelInfo.MMC_LGROUPNAME}
								</c:if>
							</td>
						</tr>
						<c:if test="${TYPE == 'M'}">
						<tr style="height: 40px">
							<td style="width: 30%;">상품코드</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_CODE}
							</td>
						</tr>
						</c:if>
						<tr style="height: 40px">
							<td style="width: 30%;">중분류</td>
							<td style="width: 70%;text-align: left;">
								<input type="text" name="MGROUP" id="MGROUP" value="${ModelInfo.MMC_MGROUP}"  style="width:90%;"/>
							</td>
						</tr>

						<tr style="height: 40px">
							<td style="width: 30%;">소분류</td>
							<td style="width: 70%;text-align: left;">
								<input type="text" name="SGROUP" id="SGROUP" value="${ModelInfo.MMC_SGROUP}"  style="width:90%;"/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">단 위</td>
							<td style="width: 70%;text-align: left;">
								<input type="text" name="UNIT" id="UNIT" value="${ModelInfo.MMC_UNIT}"  style="width:90%;"/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">ERP 상품코드</td>
							<td style="width: 70%;text-align: left;">
								<input type="text" name="ERPMODEL" id="ERPMODEL" value="${ModelInfo.MMC_ERPMODEL}"  style="width:90%;"/>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">시작일</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="StartDay" id="StartDay" value="${ModelInfo.MMC_StartDay}"  style="width:90%;" readonly="readonly"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">종료일</td>
							<td style="width: 70%;text-align: left;"><input type="text" name="EndDay" id="EndDay" value="${ModelInfo.MMC_EndDay}"  style="width:90%;" readonly="readonly"></td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">운영여부</td>
							<td style="width: 70%;text-align: left;">
							<input type="radio" name = "BizSts" value = "Y" <c:if test="${ModelInfo.MMC_BizSts == 'Y' or empty ModelInfo.MMC_BizSts}">checked="checked"</c:if>> 운영
							<input type="radio" name = "BizSts" value = "N" <c:if test="${ModelInfo.MMC_BizSts == 'N'}">checked="checked"</c:if>> 종료
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">비 고</td>
							<td style="width: 70%;text-align: left;">
								<input type="text" name="CMNT" id="CMNT" value="${ModelInfo.MMC_CMNT}"  style="width:90%;"/>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
						
			<div style="text-align: center;">
				<c:if test="${TYPE == 'M'}">
					<a href="javascript:Mdelete();" class="search grey button">삭제</a>
				</c:if>
				<a href="javascript:save();" class="search grey button">저장</a>
				<c:if test="${TYPE == 'M'}">
					<a href="javascript:barcode();" class="search grey button">바코드 출력</a>
				</c:if>
				<a href="javascript:cancel();" class="search grey button">돌아가기</a>
			</div>
			<div style="text-align: center;">
				<p class="center" id='MSG' style="color: red;font-weight: bold;font-size: 20px;">${msg}</p>
			</div>
			<div class="scroll_table">
				변경이력
				<br/>
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 30%;">일시</th>
							<th style="width: 30%;">변경자</th>
							<th style="width: 40%;">변경내용</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${ModelIlyeogSize > 0 }">
					<c:forEach var="ModelIlyeog" items="${ModelIlyeog}" varStatus="st">
						<tr>
							<td>${ModelIlyeog.MML_DATE}</td>
							<td>${ModelIlyeog.MML_USERNAME}</td>
							<td>${ModelIlyeog.MML_CMNT}</td> 	 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<div id="dim-layer" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_model" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="searchfield">
						<table style="width: 90%;">
							<tr>
								<td style="text-align: center;">물류센터</td>
								<td colspan="2">
									<select style="width: 90%;" name="pop_LOGC" id="pop_LOGC" onchange="getLLIST()">
									<option value="">선택</option>
									<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<option value="${LOGCLIST.MCI_LOGC}">${LOGCLIST.MCI_LOGCNAME}</option>
									</c:forEach>
									</select>
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
								<td style="text-align: center;width: 30%">중분류</td>
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
									<a href="javascript:Ajax_ModelList_search(1);" class="search grey button">검색</a>
								</td>
							</tr>
						</table>
					</div>
					<div id="pop_table"  style="height: 320px;overflow:auto;margin-bottom: 10px;">
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종료</a>
					</div>
    			</div>
			</div>
			<div id="Bar_layer" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_Img" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 650px;">
        			<div id="div_img">
        				
        			</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_DOWN" class="search grey button">다운로드</a>
						<a href="#" id = "BTN_CLOSE" class="search grey button">종료</a>
					</div>
    			</div>
			</div>
		</div>
	</form:form>
</body>
</html>