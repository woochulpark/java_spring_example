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
$(document).ready(function(){
	if($("#nTbl").length)
		mergeRows(nTbl,0); 
	if($("#nTbl2").length)
		mergeRows(nTbl2,0); 
	var PMSG = '${msg}';
	if(PMSG.length > 0){
		alert(PMSG);
	}
});
function getMLIST(obj){
	var LOGC = '';
	var TagName = $('#LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		LOGC = $("#LOGC option:selected").val();
	}
	else{
		LOGC = $("#LOGC").val();
	}
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
	var LOGC = '';
	var TagName = $('#LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		LOGC = $("#LOGC option:selected").val();
	}
	else{
		LOGC = $("#LOGC").val();
	}
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
	var LOGC = '';
	var TagName = $('#LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		LOGC = $("#LOGC option:selected").val();
	}
	else{
		LOGC = $("#LOGC").val();
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
	var LOGC = '';
	var LOGCTEXT = '';
	var TagName = $('#LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		LOGC = $("#LOGC option:selected").val();
		LOGCTEXT = $("#LOGC option:selected").text();
		if(LOGC.length == 0){
			document.getElementById('MSG').innerText = "물류센터를 선택해주세요.";
			alert("물류센터를 선택해주세요."); 
			return;
		}
	}
	else{
		LOGC = $("#LOGC").val();
		LOGCTEXT = $("#LOGCTEXT").val();
	}
	$("#id_popLogc").text(LOGCTEXT);
	
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
}
function ModelCheck(){
	$("#id_popLogc").text('');
	$("#pop_table").empty();
	
	$("#LCODEList option:eq(0)").prop("selected", true);
	var option = "<option value=''>선택</option>";
	$("#MCODEList").empty();
    $('#MCODEList').append(option);
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
    $("#MNAME").val('');
    
	var len = $('#Table_MLIST tbody tr').length;
	if(len > 0){
		$("#Table_MLIST").find('tbody').empty();
	}
}
function search()
{
	var MCODE = $('#MCODE').val();
	var len = $('#Table_MLIST tbody tr').length;
	if(len == 0 || MCODE.length == 0){
		document.getElementById('MSG').innerText = "상품코드를 조회하여 추가해주세요.";
		alert("상품코드를 조회하여 추가해주세요."); 
		return;
	}
	
	var YEAR = $('#YEAR').val();
	if(YEAR.length == 0){
		document.getElementById('MSG').innerText = "연도를 입력해주세요.";
		alert("연도를 입력해주세요."); 
		return;
	}
	var I_YEAR = Number(YEAR);
	if(I_YEAR < 1753 || I_YEAR > 9999){
		document.getElementById('MSG').innerText = "올바른 연도를 입력해주세요.";
		alert("올바른 연도를 입력해주세요."); 
		return;
	}
	
	var form = document.InOutReportForm;
	form.submit();
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="InOutReportForm" method="post" action="InOutReport_Search.do" >
		<input type="hidden" name="menuGroup" value="menu_stats" id="menuGroup">
		<input type="hidden" name="MCODE" value="${MODELINFO.MMC_CODE}" id="MCODE">
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

			<div class="subtitle" style="text-align: right;">관리 > 본부 입출고 현황</div>
			<h2>본부 입출고 현황</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table>
					<tbody>
						<c:choose>
						<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
							<select style="width: 90%;" name="LOGC" id = "LOGC" onchange='ModelCheck()'>
								<option value="">선택</option>
								<c:if test="${not empty LOGCLIST}">
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
									<option value="${LOGCLIST.MCI_LOGC}" <c:if test="${LOGC == LOGCLIST.MCI_LOGC}">selected="selected"</c:if>>
									${LOGCLIST.MCI_LOGCNAME}
									</option>
								</c:forEach>
								</c:if>
							</select>
							</td>
						</tr>
						</c:when>
						<c:otherwise>
						<tr>
							<td>물류센터</td>
							<td>
								${UserInfo.MCI_LOGCNAME}
								<input type="hidden" name="LOGC" value="${UserInfo.MCI_LOGC}" id="LOGC">
								<input type="hidden" name="LOGCTEXT" value="${UserInfo.MCI_LOGCNAME}" id="LOGCTEXT">
							</td>
						</tr>
						</c:otherwise>
						</c:choose>
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
							<th style="width: 25%;">상품코드</th>
							<th style="width: 25%;">대분류</th>
							<th style="width: 25%">중분류</th>
							<th style="width: 25%">소분류</th>
						</tr>
					</thead>
					<tbody  id='mList'>
						<c:if test="${not empty MODELINFO}">
						<tr style='height: 40px;'>
    						<td>${MODELINFO.MMC_CODE}</td>
    						<td>${MODELINFO.MMC_LGROUPNAME}</td>
    						<td>${MODELINFO.MMC_MGROUP}</td>
    						<td>${MODELINFO.MMC_SGROUP}</td>
    					</tr>
    					</c:if>
					</tbody>
				</table>
			</div>
			<div class="scroll_table">
				<table>
					<tbody>
						<tr style="height: 40px">
							<th style="width: 30%;">연도</th>
							<th style="width: 30%;">구분1</th>
							<th style="width: 30%;">구분2</th>
						</tr>
						<tr>
							<td><input type="number" name="YEAR" id="YEAR" value="${YEAR}" style="width:80%;"  onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="1990" max="2100"></td>
							<td style="text-align: left;">
								<select style="width: 90%;" name="INTYPE" id = "INTYPE">
									<option value="0" <c:if test="${INTYPE == '0'}">selected="selected"</c:if>>전체</option>
									<option value="1" <c:if test="${INTYPE == '1'}">selected="selected"</c:if>>입고</option>
									<option value="2" <c:if test="${INTYPE == '2'}">selected="selected"</c:if>>출고</option>
								</select>
							</td>
							<td style="text-align: left;">
								<select style="width: 90%;" name="CTYPE" id = "CTYPE">
									<option value="0" <c:if test="${CTYPE == '0'}">selected="selected"</c:if>>본부별</option>
									<option value="1" <c:if test="${CTYPE == '1'}">selected="selected"</c:if>>센터별</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
				<a href="javascript:search();" class="search grey button">검 색</a>
			</div>
			<div style="width: 1300px;overflow:auto;">
				<c:if test="${not empty INLIST}">
				<table id="nTbl" style="table-layout:fixed;width: 1920px;" class="ReportTable">
					<thead>
						<tr style="height: 40px">
							<th style="width: 80px;" rowspan="2">본부</th>
							<c:if test="${CTYPE == '1'}">
							<th style="width: 80px;" rowspan="2">센터</th>
							</c:if>
							<th style="width: 80px;" rowspan="2">이월재고</th>
							<th style="width: 120px;" colspan="3">입고 계</th>
							<th style="width: 120px;" colspan="3">1월</th>
							<th style="width: 120px;" colspan="3">2월</th>
							<th style="width: 120px;" colspan="3">3월</th>
							<th style="width: 120px;" colspan="3">4월</th>
							<th style="width: 120px;" colspan="3">5월</th>
							<th style="width: 120px;" colspan="3">6월</th>
							<th style="width: 120px;" colspan="3">7월</th>
							<th style="width: 120px;" colspan="3">8월</th>
							<th style="width: 120px;" colspan="3">9월</th>
							<th style="width: 120px;" colspan="3">10월</th>
							<th style="width: 120px;" colspan="3">11월</th>
							<th style="width: 120px;" colspan="3">12월</th>
						</tr>
						<tr style="height: 40px">
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">물류</th>
							<th style="width: 40px;">본부</th>
						</tr>
					</thead>
					<tbody>
						<c:set var="jegototal" value="0" />
						<c:set var="subtotal" value="0" />
						<c:set var="sublogc" value="0" />
						<c:set var="subbonbu" value="0" />
						<c:set var="m1total" value="0" />
						<c:set var="m1logc" value="0" />
						<c:set var="m1bonbu" value="0" />
						<c:set var="m2total" value="0" />
						<c:set var="m2logc" value="0" />
						<c:set var="m2bonbu" value="0" />
						<c:set var="m3total" value="0" />
						<c:set var="m3logc" value="0" />
						<c:set var="m3bonbu" value="0" />
						<c:set var="m4total" value="0" />
						<c:set var="m4logc" value="0" />
						<c:set var="m4bonbu" value="0" />
						<c:set var="m5total" value="0" />
						<c:set var="m5logc" value="0" />
						<c:set var="m5bonbu" value="0" />
						<c:set var="m6total" value="0" />
						<c:set var="m6logc" value="0" />
						<c:set var="m6bonbu" value="0" />
						<c:set var="m7total" value="0" />
						<c:set var="m7logc" value="0" />
						<c:set var="m7bonbu" value="0" />
						<c:set var="m8total" value="0" />
						<c:set var="m8logc" value="0" />
						<c:set var="m8bonbu" value="0" />
						<c:set var="m9total" value="0" />
						<c:set var="m9logc" value="0" />
						<c:set var="m9bonbu" value="0" />
						<c:set var="m10total" value="0" />
						<c:set var="m10logc" value="0" />
						<c:set var="m10bonbu" value="0" />
						<c:set var="m11total" value="0" />
						<c:set var="m11logc" value="0" />
						<c:set var="m11bonbu" value="0" />
						<c:set var="m12total" value="0" />
						<c:set var="m12logc" value="0" />
						<c:set var="m12bonbu" value="0" />
						<c:forEach var="INLIST" items="${INLIST}" varStatus="st">
						<c:if test="${INLIST.SUMTYPE == 1}">
						<c:set var="jegototal" value="${jegototal + INLIST.JEGO}" />
						<c:set var="subtotal" value="${subtotal + INLIST.INSUBTOTAL}" />
						<c:set var="sublogc" value="${sublogc + INLIST.INSUBLOGCTOTAL}" />
						<c:set var="subbonbu" value="${subbonbu + INLIST.INSUBBONBUTOTAL}" />
						<c:set var="m1total" value="${m1total + INLIST.INM1TOTAL}" />
						<c:set var="m1logc" value="${m1logc + INLIST.INM1LOGC}" />
						<c:set var="m1bonbu" value="${m1bonbu + INLIST.INM1BONBU}" />
						<c:set var="m2total" value="${m2total + INLIST.INM2TOTAL}" />
						<c:set var="m2logc" value="${m2logc + INLIST.INM2LOGC}" />
						<c:set var="m2bonbu" value="${m2bonbu + INLIST.INM2BONBU}" />
						<c:set var="m3total" value="${m3total + INLIST.INM3TOTAL}" />
						<c:set var="m3logc" value="${m3logc + INLIST.INM3LOGC}" />
						<c:set var="m3bonbu" value="${m3bonbu + INLIST.INM3BONBU}" />
						<c:set var="m4total" value="${m4total + INLIST.INM4TOTAL}" />
						<c:set var="m4logc" value="${m4logc + INLIST.INM4LOGC}" />
						<c:set var="m4bonbu" value="${m4bonbu + INLIST.INM4BONBU}" />
						<c:set var="m5total" value="${m5total + INLIST.INM5TOTAL}" />
						<c:set var="m5logc" value="${m5logc + INLIST.INM5LOGC}" />
						<c:set var="m5bonbu" value="${m5bonbu + INLIST.INM5BONBU}" />
						<c:set var="m6total" value="${m6total + INLIST.INM6TOTAL}" />
						<c:set var="m6logc" value="${m6logc + INLIST.INM6LOGC}" />
						<c:set var="m6bonbu" value="${m6bonbu + INLIST.INM6BONBU}" />
						<c:set var="m7total" value="${m7total + INLIST.INM7TOTAL}" />
						<c:set var="m7logc" value="${m7logc + INLIST.INM7LOGC}" />
						<c:set var="m7bonbu" value="${m7bonbu + INLIST.INM7BONBU}" />
						<c:set var="m8total" value="${m8total + INLIST.INM8TOTAL}" />
						<c:set var="m8logc" value="${m8logc + INLIST.INM8LOGC}" />
						<c:set var="m8bonbu" value="${m8bonbu + INLIST.INM8BONBU}" />
						<c:set var="m9total" value="${m9total + INLIST.INM9TOTAL}" />
						<c:set var="m9logc" value="${m9logc + INLIST.INM9LOGC}" />
						<c:set var="m9bonbu" value="${m9bonbu + INLIST.INM9BONBU}" />
						<c:set var="m10total" value="${m10total + INLIST.INM10TOTAL}" />
						<c:set var="m10logc" value="${m10logc + INLIST.INM10LOGC}" />
						<c:set var="m10bonbu" value="${m10bonbu + INLIST.INM10BONBU}" />
						<c:set var="m11total" value="${m11total + INLIST.INM11TOTAL}" />
						<c:set var="m11logc" value="${m11logc + INLIST.INM11LOGC}" />
						<c:set var="m11bonbu" value="${m11bonbu + INLIST.INM11BONBU}" />
						<c:set var="m12total" value="${m12total + INLIST.INM12TOTAL}" />
						<c:set var="m12logc" value="${m12logc + INLIST.INM12LOGC}" />
						<c:set var="m12bonbu" value="${m12bonbu + INLIST.INM12BONBU}" />
						</c:if>
						<c:if test="${CTYPE == 0 and INLIST.SUMTYPE == 1 or CTYPE == 1}">
						<tr>
							<td>${INLIST.BONBUNAME}</td>
							<c:if test="${CTYPE == '1'}">
							<td>${INLIST.CENTERNAME}</td>
							</c:if>
							<td>${INLIST.JEGO}</td>
							<td>${INLIST.INSUBTOTAL}</td>
							<td>${INLIST.INSUBLOGCTOTAL}</td>
							<td>${INLIST.INSUBBONBUTOTAL}</td>
							<td>${INLIST.INM1TOTAL}</td>
							<td>${INLIST.INM1LOGC}</td>
							<td>${INLIST.INM1BONBU}</td>
							<td>${INLIST.INM2TOTAL}</td>
							<td>${INLIST.INM2LOGC}</td>
							<td>${INLIST.INM2BONBU}</td>
							<td>${INLIST.INM3TOTAL}</td>
							<td>${INLIST.INM3LOGC}</td>
							<td>${INLIST.INM3BONBU}</td>
							<td>${INLIST.INM4TOTAL}</td>
							<td>${INLIST.INM4LOGC}</td>
							<td>${INLIST.INM4BONBU}</td>
							<td>${INLIST.INM5TOTAL}</td>
							<td>${INLIST.INM5LOGC}</td>
							<td>${INLIST.INM5BONBU}</td>
							<td>${INLIST.INM6TOTAL}</td>
							<td>${INLIST.INM6LOGC}</td>
							<td>${INLIST.INM6BONBU}</td>
							<td>${INLIST.INM7TOTAL}</td>
							<td>${INLIST.INM7LOGC}</td>
							<td>${INLIST.INM7BONBU}</td>
							<td>${INLIST.INM8TOTAL}</td>
							<td>${INLIST.INM8LOGC}</td>
							<td>${INLIST.INM8BONBU}</td>
							<td>${INLIST.INM9TOTAL}</td>
							<td>${INLIST.INM9LOGC}</td>
							<td>${INLIST.INM9BONBU}</td>
							<td>${INLIST.INM10TOTAL}</td>
							<td>${INLIST.INM10LOGC}</td>
							<td>${INLIST.INM10BONBU}</td>
							<td>${INLIST.INM11TOTAL}</td>
							<td>${INLIST.INM11LOGC}</td>
							<td>${INLIST.INM11BONBU}</td>
							<td>${INLIST.INM12TOTAL}</td>
							<td>${INLIST.INM12LOGC}</td>
							<td>${INLIST.INM12BONBU}</td>
						</tr>
						</c:if>
						</c:forEach>
						<tr>
							<c:if test="${CTYPE == '0'}">
							<td>계</td>
							</c:if>
							<c:if test="${CTYPE == '1'}">
							<td colspan="2">계</td>
							</c:if>
							<td>${jegototal}</td>
							<td>${subtotal}</td>
							<td>${sublogc}</td>
							<td>${subbonbu}</td>
							<td>${m1total}</td>
							<td>${m1logc}</td>
							<td>${m1bonbu}</td>
							<td>${m2total}</td>
							<td>${m2logc}</td>
							<td>${m2bonbu}</td>
							<td>${m3total}</td>
							<td>${m3logc}</td>
							<td>${m3bonbu}</td>
							<td>${m4total}</td>
							<td>${m4logc}</td>
							<td>${m4bonbu}</td>
							<td>${m5total}</td>
							<td>${m5logc}</td>
							<td>${m5bonbu}</td>
							<td>${m6total}</td>
							<td>${m6logc}</td>
							<td>${m6bonbu}</td>
							<td>${m7total}</td>
							<td>${m7logc}</td>
							<td>${m7bonbu}</td>
							<td>${m8total}</td>
							<td>${m8logc}</td>
							<td>${m8bonbu}</td>
							<td>${m9total}</td>
							<td>${m9logc}</td>
							<td>${m9logc}</td>
							<td>${m10total}</td>
							<td>${m10logc}</td>
							<td>${m10bonbu}</td>
							<td>${m11total}</td>
							<td>${m11logc}</td>
							<td>${m11bonbu}</td>
							<td>${m12total}</td>
							<td>${m12logc}</td>
							<td>${m12bonbu}</td>
						</tr>
					</tbody>
				</table>
				</c:if>
				<c:if test="${not empty OUTLIST}">
				<table id="nTbl2" style="table-layout:fixed;width: 2080px;" class="ReportTable">
					<thead>
						<tr style="height: 40px">
							<th style="width: 80px;" rowspan="2">본부</th>
							<c:if test="${CTYPE == '1'}">
							<th style="width: 80px;" rowspan="2">센터</th>
							</c:if>
							<th style="width: 80px;" rowspan="2">이월재고</th>
							<th style="width: 160px;" colspan="4">출고 계</th>
							<th style="width: 160px;" colspan="4">1월</th>
							<th style="width: 160px;" colspan="4">2월</th>
							<th style="width: 160px;" colspan="4">3월</th>
							<th style="width: 160px;" colspan="4">4월</th>
							<th style="width: 160px;" colspan="4">5월</th>
							<th style="width: 160px;" colspan="4">6월</th>
							<th style="width: 160px;" colspan="4">7월</th>
							<th style="width: 160px;" colspan="4">8월</th>
							<th style="width: 160px;" colspan="4">9월</th>
							<th style="width: 160px;" colspan="4">10월</th>
							<th style="width: 160px;" colspan="4">11월</th>
							<th style="width: 160px;" colspan="4">12월</th>
						</tr>
						<tr style="height: 40px">
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
							<th style="width: 40px;">계</th>
							<th style="width: 40px;">행사</th>
							<th style="width: 40px;">기타</th>
							<th style="width: 40px;">본부</th>
						</tr>
					</thead>
					<tbody>
						<c:set var="jegototal" value="0" />
						<c:set var="subtotal" value="0" />
						<c:set var="subevent" value="0" />
						<c:set var="subother" value="0" />
						<c:set var="subbonbu" value="0" />
						<c:set var="m1total" value="0" />
						<c:set var="m1event" value="0" />
						<c:set var="m1other" value="0" />
						<c:set var="m1bonbu" value="0" />
						<c:set var="m2total" value="0" />
						<c:set var="m2event" value="0" />
						<c:set var="m2other" value="0" />
						<c:set var="m2bonbu" value="0" />
						<c:set var="m3total" value="0" />
						<c:set var="m3event" value="0" />
						<c:set var="m3other" value="0" />
						<c:set var="m3bonbu" value="0" />
						<c:set var="m4total" value="0" />
						<c:set var="m4event" value="0" />
						<c:set var="m4other" value="0" />
						<c:set var="m4bonbu" value="0" />
						<c:set var="m5total" value="0" />
						<c:set var="m5event" value="0" />
						<c:set var="m5other" value="0" />
						<c:set var="m5bonbu" value="0" />
						<c:set var="m6total" value="0" />
						<c:set var="m6event" value="0" />
						<c:set var="m6other" value="0" />
						<c:set var="m6bonbu" value="0" />
						<c:set var="m7total" value="0" />
						<c:set var="m7event" value="0" />
						<c:set var="m7other" value="0" />
						<c:set var="m7bonbu" value="0" />
						<c:set var="m8total" value="0" />
						<c:set var="m8event" value="0" />
						<c:set var="m8other" value="0" />
						<c:set var="m8bonbu" value="0" />
						<c:set var="m9total" value="0" />
						<c:set var="m9event" value="0" />
						<c:set var="m9other" value="0" />
						<c:set var="m9bonbu" value="0" />
						<c:set var="m10total" value="0" />
						<c:set var="m10event" value="0" />
						<c:set var="m10other" value="0" />
						<c:set var="m10bonbu" value="0" />
						<c:set var="m11total" value="0" />
						<c:set var="m11event" value="0" />
						<c:set var="m11other" value="0" />
						<c:set var="m11bonbu" value="0" />
						<c:set var="m12total" value="0" />
						<c:set var="m12event" value="0" />
						<c:set var="m12other" value="0" />
						<c:set var="m12bonbu" value="0" />
						<c:forEach var="OUTLIST" items="${OUTLIST}" varStatus="st">
						<c:if test="${OUTLIST.SUMTYPE == 1}">
						<c:set var="jegototal" value="${jegototal + OUTLIST.JEGO}" />
						<c:set var="subtotal" value="${subtotal + OUTLIST.OUTSUBTOTAL}" />
						<c:set var="subevent" value="${subevent + OUTLIST.OUTSUBEVENTTOTAL}" />
						<c:set var="subother" value="${subother + OUTLIST.OUTSUBOTHERTOTAL}" />
						<c:set var="subbonbu" value="${subbonbu + OUTLIST.OUTSUBBONBUTOTAL}" />
						<c:set var="m1total" value="${m1total + OUTLIST.OUTM1TOTAL}" />
						<c:set var="m1event" value="${m1event + OUTLIST.OUTM1EVENT}" />
						<c:set var="m1other" value="${m1other + OUTLIST.OUTM1OTHER}" />
						<c:set var="m1bonbu" value="${m1bonbu + OUTLIST.OUTM1BONBU}" />
						<c:set var="m2total" value="${m2total + OUTLIST.OUTM2TOTAL}" />
						<c:set var="m2event" value="${m2event + OUTLIST.OUTM2EVENT}" />
						<c:set var="m2other" value="${m2other + OUTLIST.OUTM2OTHER}" />
						<c:set var="m2bonbu" value="${m2bonbu + OUTLIST.OUTM2BONBU}" />
						<c:set var="m3total" value="${m3total + OUTLIST.OUTM3TOTAL}" />
						<c:set var="m3event" value="${m3event + OUTLIST.OUTM3EVENT}" />
						<c:set var="m3other" value="${m3other + OUTLIST.OUTM3OTHER}" />
						<c:set var="m3bonbu" value="${m3bonbu + OUTLIST.OUTM3BONBU}" />
						<c:set var="m4total" value="${m4total + OUTLIST.OUTM4TOTAL}" />
						<c:set var="m4event" value="${m4event + OUTLIST.OUTM4EVENT}" />
						<c:set var="m4other" value="${m4other + OUTLIST.OUTM4OTHER}" />
						<c:set var="m4bonbu" value="${m4bonbu + OUTLIST.OUTM4BONBU}" />
						<c:set var="m5total" value="${m5total + OUTLIST.OUTM5TOTAL}" />
						<c:set var="m5event" value="${m5event + OUTLIST.OUTM5EVENT}" />
						<c:set var="m5other" value="${m5other + OUTLIST.OUTM5OTHER}" />
						<c:set var="m5bonbu" value="${m5bonbu + OUTLIST.OUTM5BONBU}" />
						<c:set var="m6total" value="${m6total + OUTLIST.OUTM6TOTAL}" />
						<c:set var="m6event" value="${m6event + OUTLIST.OUTM6EVENT}" />
						<c:set var="m6other" value="${m6other + OUTLIST.OUTM6OTHER}" />
						<c:set var="m6bonbu" value="${m6bonbu + OUTLIST.OUTM6BONBU}" />
						<c:set var="m7total" value="${m7total + OUTLIST.OUTM7TOTAL}" />
						<c:set var="m7event" value="${m7event + OUTLIST.OUTM7EVENT}" />
						<c:set var="m7other" value="${m7other + OUTLIST.OUTM7OTHER}" />
						<c:set var="m7bonbu" value="${m7bonbu + OUTLIST.OUTM7BONBU}" />
						<c:set var="m8total" value="${m8total + OUTLIST.OUTM8TOTAL}" />
						<c:set var="m8event" value="${m8event + OUTLIST.OUTM8EVENT}" />
						<c:set var="m8other" value="${m8other + OUTLIST.OUTM8OTHER}" />
						<c:set var="m8bonbu" value="${m8bonbu + OUTLIST.OUTM8BONBU}" />
						<c:set var="m9total" value="${m9total + OUTLIST.OUTM9TOTAL}" />
						<c:set var="m9event" value="${m9event + OUTLIST.OUTM9EVENT}" />
						<c:set var="m9other" value="${m9other + OUTLIST.OUTM9OTHER}" />
						<c:set var="m9bonbu" value="${m9bonbu + OUTLIST.OUTM9BONBU}" />
						<c:set var="m10total" value="${m10total + OUTLIST.OUTM10TOTAL}" />
						<c:set var="m10event" value="${m10event + OUTLIST.OUTM10EVENT}" />
						<c:set var="m10other" value="${m10other + OUTLIST.OUTM10OTHER}" />
						<c:set var="m10bonbu" value="${m10bonbu + OUTLIST.OUTM10BONBU}" />
						<c:set var="m11total" value="${m11total + OUTLIST.OUTM11TOTAL}" />
						<c:set var="m11event" value="${m11event + OUTLIST.OUTM11EVENT}" />
						<c:set var="m11other" value="${m11other + OUTLIST.OUTM11OTHER}" />
						<c:set var="m11bonbu" value="${m11bonbu + OUTLIST.OUTM11BONBU}" />
						<c:set var="m12total" value="${m12total + OUTLIST.OUTM12TOTAL}" />
						<c:set var="m12event" value="${m12event + OUTLIST.OUTM12EVENT}" />
						<c:set var="m12other" value="${m12other + OUTLIST.OUTM12OTHER}" />
						<c:set var="m12bonbu" value="${m12bonbu + OUTLIST.OUTM12BONBU}" />
						</c:if>
						<c:if test="${CTYPE == 0 and OUTLIST.SUMTYPE == 1 or CTYPE == 1}">
						<tr>
							<td>${OUTLIST.BONBUNAME}</td>
							<c:if test="${CTYPE == '1'}">
							<td>${OUTLIST.CENTERNAME}</td>
							</c:if>
							<td>${OUTLIST.JEGO}</td>
							<td>${OUTLIST.OUTSUBTOTAL}</td>
							<td>${OUTLIST.OUTSUBEVENTTOTAL}</td>
							<td>${OUTLIST.OUTSUBOTHERTOTAL}</td>
							<td>${OUTLIST.OUTSUBBONBUTOTAL}</td>
							<td>${OUTLIST.OUTM1TOTAL}</td>
							<td>${OUTLIST.OUTM1EVENT}</td>
							<td>${OUTLIST.OUTM1OTHER}</td>
							<td>${OUTLIST.OUTM1BONBU}</td>
							<td>${OUTLIST.OUTM2TOTAL}</td>
							<td>${OUTLIST.OUTM2EVENT}</td>
							<td>${OUTLIST.OUTM2OTHER}</td>
							<td>${OUTLIST.OUTM2BONBU}</td>
							<td>${OUTLIST.OUTM3TOTAL}</td>
							<td>${OUTLIST.OUTM3EVENT}</td>
							<td>${OUTLIST.OUTM3OTHER}</td>
							<td>${OUTLIST.OUTM3BONBU}</td>
							<td>${OUTLIST.OUTM4TOTAL}</td>
							<td>${OUTLIST.OUTM4EVENT}</td>
							<td>${OUTLIST.OUTM4OTHER}</td>
							<td>${OUTLIST.OUTM4BONBU}</td>
							<td>${OUTLIST.OUTM5TOTAL}</td>
							<td>${OUTLIST.OUTM5EVENT}</td>
							<td>${OUTLIST.OUTM5OTHER}</td>
							<td>${OUTLIST.OUTM5BONBU}</td>
							<td>${OUTLIST.OUTM6TOTAL}</td>
							<td>${OUTLIST.OUTM6EVENT}</td>
							<td>${OUTLIST.OUTM6OTHER}</td>
							<td>${OUTLIST.OUTM6BONBU}</td>
							<td>${OUTLIST.OUTM7TOTAL}</td>
							<td>${OUTLIST.OUTM7EVENT}</td>
							<td>${OUTLIST.OUTM7OTHER}</td>
							<td>${OUTLIST.OUTM7BONBU}</td>
							<td>${OUTLIST.OUTM8TOTAL}</td>
							<td>${OUTLIST.OUTM8EVENT}</td>
							<td>${OUTLIST.OUTM8OTHER}</td>
							<td>${OUTLIST.OUTM8BONBU}</td>
							<td>${OUTLIST.OUTM9TOTAL}</td>
							<td>${OUTLIST.OUTM9EVENT}</td>
							<td>${OUTLIST.OUTM9OTHER}</td>
							<td>${OUTLIST.OUTM9BONBU}</td>
							<td>${OUTLIST.OUTM10TOTAL}</td>
							<td>${OUTLIST.OUTM10EVENT}</td>
							<td>${OUTLIST.OUTM10OTHER}</td>
							<td>${OUTLIST.OUTM10BONBU}</td>
							<td>${OUTLIST.OUTM11TOTAL}</td>
							<td>${OUTLIST.OUTM11EVENT}</td>
							<td>${OUTLIST.OUTM11OTHER}</td>
							<td>${OUTLIST.OUTM11BONBU}</td>
							<td>${OUTLIST.OUTM12TOTAL}</td>
							<td>${OUTLIST.OUTM12EVENT}</td>
							<td>${OUTLIST.OUTM12OTHER}</td>
							<td>${OUTLIST.OUTM12BONBU}</td>
						</tr>
						</c:if>
						</c:forEach>
						<tr>
							<c:if test="${CTYPE == '0'}">
							<td>계</td>
							</c:if>
							<c:if test="${CTYPE == '1'}">
							<td colspan="2">계</td>
							</c:if>
							<td>${jegototal}</td>
							<td>${subtotal}</td>
							<td>${subevent}</td>
							<td>${subother}</td>
							<td>${subbonbu}</td>
							<td>${m1total}</td>
							<td>${m1event}</td>
							<td>${m1other}</td>
							<td>${m1bonbu}</td>
							<td>${m2total}</td>
							<td>${m2event}</td>
							<td>${m2other}</td>
							<td>${m2bonbu}</td>
							<td>${m3total}</td>
							<td>${m3event}</td>
							<td>${m3other}</td>
							<td>${m3bonbu}</td>
							<td>${m4total}</td>
							<td>${m4event}</td>
							<td>${m4other}</td>
							<td>${m4bonbu}</td>
							<td>${m5total}</td>
							<td>${m5event}</td>
							<td>${m5other}</td>
							<td>${m5bonbu}</td>
							<td>${m6total}</td>
							<td>${m6event}</td>
							<td>${m6other}</td>
							<td>${m6bonbu}</td>
							<td>${m7total}</td>
							<td>${m7event}</td>
							<td>${m7other}</td>
							<td>${m7bonbu}</td>
							<td>${m8total}</td>
							<td>${m8event}</td>
							<td>${m8other}</td>
							<td>${m8bonbu}</td>
							<td>${m9total}</td>
							<td>${m9event}</td>
							<td>${m9other}</td>
							<td>${m9bonbu}</td>
							<td>${m10total}</td>
							<td>${m10event}</td>
							<td>${m10other}</td>
							<td>${m10bonbu}</td>
							<td>${m11total}</td>
							<td>${m11event}</td>
							<td>${m11other}</td>
							<td>${m11bonbu}</td>
							<td>${m12total}</td>
							<td>${m12event}</td>
							<td>${m12other}</td>
							<td>${m12bonbu}</td>
						</tr>
					</tbody>
				</table>
				</c:if>
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