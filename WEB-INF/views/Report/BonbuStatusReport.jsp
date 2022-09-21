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
	var PMSG = '${msg}';
	if(PMSG.length > 0){
		alert(PMSG);
	}
});
function search()
{
	var form = document.statusForm;
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
function fn_paging(PAGE)
{
	document.getElementById("PAGE").value = PAGE;
	var form = document.statusForm;
	form.submit();
}
function PTYPE_OnOff()
{
	var PTYPEValue = $('input:radio[name="PTYPE"]:checked').val();
	if(PTYPEValue == '0')
		$("#PSIZE").attr("readonly",true); 
	else
		$("#PSIZE").removeAttr("readonly");
}
function allCheck(){
	if($("#pop_all").is(":checked")){
        $(".JEGOCHK").prop("checked", true);
    }
    else{
        $(".JEGOCHK").prop("checked", false);
    }
}
function DOC_Add(CENTER){
	if($('input:checkbox[name=JEGOCHK]:checked').length > 0){
		var confirm_Save = confirm("재고내역서를 발급하시겠습니까?");
    	if ( confirm_Save == true ) {
    		document.getElementById("DDJ_CENTER").value = CENTER;
    		var form = document.statusForm;
    		form.action = "Jegoformat_Add_Save.do";
    		form.submit();
    	}
	}
}
function getMLIST(obj){
	var option = "<option value=''>선택</option>";
	$("#SCODEList").empty();
    $('#SCODEList').append(option);
	var LOGC = '';
    var TagName = $('#LOGC').prop('tagName');
	if(TagName == 'SELECT'){
		LOGC = $("#LOGC option:selected").val();
	}
	else{
		LOGC = $("#LOGC").val();
	}
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
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="statusForm" method="post" action="BonbuStatusReport_Search.do">
		<input type="hidden" name="menuGroup" value="menu_stats" id="menuGroup">
		<input type="hidden" name="PAGE" value="1" id="PAGE">
		<input type="hidden" id="DDJ_CENTER" name="DDJ_CENTER" value="">
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

			<div class="subtitle" style="text-align: right;">관리 > 본부 현황</div>
			<h2>본부 현황</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 70%;">
					<c:choose>
					<c:when test="${UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '002'}">
					<tr>
						<td style="text-align: center;width: 20%;">본 부</td>
						<td style="width: 26%;">
							<select style="width: 90%;" name="LOGC" id="LOGC"  onchange="getBONBU(this)">
								<option value="">선택</option>
								<c:forEach var="LOGCLIST" items="${LOGCLIST}" varStatus="st">
								<option value="${LOGCLIST.MCI_LOGC}" <c:if test="${LOGC == LOGCLIST.MCI_LOGC}">selected="selected"</c:if>>${LOGCLIST.MCI_LOGCNAME}</option>
								</c:forEach>
							</select>
						</td>
						<td id = 'td_B' style="width: 27%;">
							<select style="width: 90%;" name="BONBU" id = "BONBU" onchange="getCENTER(this)">
								<option value="">선택</option>
								<c:if test="${not empty BONBU and BONBU != ''}">
								<c:forEach var="BONBULIST" items="${BONBULIST}" varStatus="st">
								<option value="${BONBULIST.MCI_Bonbu}" <c:if test="${BONBU == BONBULIST.MCI_Bonbu}">selected="selected" </c:if>>${BONBULIST.MCI_BonbuNAME}</option>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td id = 'td_C' style="width: 27%;">
							<select style="width: 90%;" name="CENTER" id = "CENTER">
								<option value="">선택</option>
								<c:if test="${not empty CENTER and CENTER != ''}">
								<c:forEach var="CENTERLIST" items="${CENTERLIST}" varStatus="st">
								<option value="${CENTERLIST.MCI_Center}" <c:if test="${CENTER == CENTERLIST.MCI_Center}">selected="selected" </c:if>>${CENTERLIST.MCI_CenterName}</option>
								</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					</c:when>
					<c:when test="${UserInfo.MUT_POSITION == '003'}">
					<tr>
						<td style="text-align: center;">본 부</td>
						<td colspan="3">
							<input type="hidden" id="LOGC" name="LOGC" value="${LOGC}" >
							${UserInfo.MCI_LOGCNAME}&nbsp;${UserInfo.MCI_BonbuNAME}
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">센 터</td>
						<td id = 'td_C' colspan="3">
							<select style="width: 90%;" name="CENTER" id = "CENTER">
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
						<td style="text-align: center;">본부</td>
						<td colspan="3">
							<input type="hidden" id="LOGC" name="LOGC" value="${LOGC}" >
							${UserInfo.MCI_BonbuNAME}&nbsp;${UserInfo.MCI_CenterName}
						</td>
					</tr>
					</c:otherwise>
					</c:choose>
					<tr>
						<td style="text-align: center;">일 자</td>
						<td colspan="3">
							<input type="number" name="YEAR" id="YEAR" value="${YEAR}" style="width:45%;"  onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="2020" max="2100">
							<select style="width: 45%;" name="MONTH" id = "MONTH">
							<c:forEach var="i" begin="1" end="12">
    						<option value="${i}" <c:if test="${MONTH == i}">selected="selected" </c:if>>${i}월</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">대분류</td>
						<td colspan="3">
							<select style="width: 90%;" name="LCODEList" id="LCODEList" onchange="getMLIST(this)">
								<option value="">선택</option>
								<c:forEach var="LCODELIST" items="${LCODELIST}" varStatus="st">
									<c:if test="${LCODE == LCODELIST.MCC_S_CODE}">
										<option value="${LCODELIST.MCC_S_CODE}" selected="selected">${LCODELIST.MCC_S_NAME}</option>
									</c:if>
									<c:if test="${LCODE != LCODELIST.MCC_S_CODE}">
										<option value="${LCODELIST.MCC_S_CODE}">${LCODELIST.MCC_S_NAME}</option>
									</c:if>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">중분류</td>
						<td id = 'td_M'>
							<select style="width: 90%;" name="MCODEList" id = "MCODEList" onchange="getSLIST(this)">
								<option value="">선택</option>
								<c:if test="${not empty MCODELIST}">
								<c:forEach var="MCODELIST" items="${MCODELIST}" varStatus="st">
									<c:if test="${MCODE == MCODELIST.MMC_MGROUP}">
										<option value="${MCODELIST.MMC_MGROUP}" selected="selected">${MCODELIST.MMC_MGROUP}</option>
									</c:if>
									<c:if test="${MCODE != MCODELIST.MMC_MGROUP}">
										<option value="${MCODELIST.MMC_MGROUP}">${MCODELIST.MMC_MGROUP}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td colspan="2">
							<input type="text" name="MCODENAME" id="MCODENAME" value="${MCODENAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">소분류</td>
						<td id = 'td_S'>
							<select style="width: 90%;" name="SCODEList" id = "SCODEList">
								<option value="">선택</option>
								<c:if test="${not empty SCODELIST}">
								<c:forEach var="SCODELIST" items="${SCODELIST}" varStatus="st">
									<c:if test="${SCODE == SCODELIST.MMC_SGROUP}">
										<option value="${SCODELIST.MMC_SGROUP}" selected="selected">${SCODELIST.MMC_SGROUP}</option>
									</c:if>
									<c:if test="${SCODE != SCODELIST.MMC_SGROUP}">
										<option value="${SCODELIST.MMC_SGROUP}">${SCODELIST.MMC_SGROUP}</option>
									</c:if>
								</c:forEach>
								</c:if>
							</select>
						</td>
						<td colspan="2">
							<input type="text" name="SCODENAME" id="SCODENAME" value="${SCODENAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">상품코드명</td>
						<td colspan="3">
							<input type="text" name="MNAME" id="MNAME" value="${MNAME}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td style="text-align: center;">화면표시개수</td>
						<td colspan="3">
							<input type="radio" name = "PTYPE" value = "0" <c:if test="${PTYPE == 0}">checked="checked"</c:if> onclick="PTYPE_OnOff();"/>모두표시
							<input type="radio" name = "PTYPE" value = "1" <c:if test="${empty PTYPE or PTYPE == 1}">checked="checked"</c:if> onclick="PTYPE_OnOff();" />입력
							<input type="number" name="PSIZE" id="PSIZE" value="${PSIZE}" style="width:45%;" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');" min="1" max="100">
						</td>
					</tr>
					<tr>
						<td colspan="4" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<c:if test="${not empty CenterInfo and BonbuStatusListSize > 0 and (UserInfo.MUT_POSITION == '001' or UserInfo.MUT_POSITION == '003' or UserInfo.MUT_POSITION == '004')}">
			<div style="text-align:right;">
				<a href="javascript:DOC_Add('${CenterInfo.MCI_CODE}')" class="search grey button">발급하기</a>
			</div>
			</c:if>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 4%;"><input type="checkbox" id="pop_all" name="pop_all" value="" onClick="javascript:allCheck();"/></th>
							<th style="width: 7%;">물류센터</th>
							<th style="width: 8%">본 부</th>
							<th style="width: 9%">센 터</th>
							<th style="width: 9%">상품코드</th>
							<th style="width: 9%">대분류</th>
							<th style="width: 8%">중분류</th>
							<th style="width: 9%">소분류</th>
							<th style="width: 8%">전월 마감재고</th>
							<th style="width: 7%">입 고</th>
							<th style="width: 7%">출 고</th>
							<th style="width: 7%">현 재고</th>
							<th style="width: 7%">적정재고</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${BonbuStatusListSize > 0 }">
					<c:forEach var="BonbuStatusList" items="${BonbuStatusList}" varStatus="st">
						<tr style="height: 40px;">
							<td>
								<input type="checkbox" name="JEGOCHK" class="JEGOCHK" value="${BonbuStatusList.MMC_CODE}^${BonbuStatusList.NOWJEGO}"  style="width:90%;"/>
							</td>
							<td>${CenterInfo.MCI_LOGCNAME}</td>
							<td>${CenterInfo.MCI_BonbuNAME}</td>
							<td>${CenterInfo.MCI_CenterName}</td> 	
							<td>${BonbuStatusList.MMC_CODE}</td>
							<td>${BonbuStatusList.MMC_LGROUPNAME}</td>
							<td>${BonbuStatusList.MMC_MGROUP}</td>
							<td>${BonbuStatusList.MMC_SGROUP}</td>
							<td>${BonbuStatusList.PJEGO}</td>
							<td>${BonbuStatusList.INJEGO}</td> 	 
							<td>${BonbuStatusList.OUTJEGO}</td> 	 	 
							<td>${BonbuStatusList.NOWJEGO}</td>
							<td>${BonbuStatusList.MMJ_JEGO}</td>	 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<c:if test="${PTYPE == 1 }">
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
            </c:if>
		</div>
	</form:form>
</body>
</html>