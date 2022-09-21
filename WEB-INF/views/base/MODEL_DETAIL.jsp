<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
function save()
{
	var form = document.modelForm;
	form.submit();
}

function cancel() {
	var form = document.modelForm;
	form.action = "ModelList.do";
	form.submit();
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
	<form:form name="modelForm" method="post" action="MODEL_Add.do" >
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="TYPE" value="M" id="TYPE">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 상품코드 관리 > 상세화면</div>
			<h2>상세화면</h2>

			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<tbody>
						<tr style="height: 40px">
							<td style="width: 30%;">구 분</td>
							<td style="width: 70%;text-align: left;">
							${ModelInfo.MMC_KINDSNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">물류센터</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_LOGCNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">대분류</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_LGROUPNAME}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">상품코드</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_CODE}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">중분류</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_MGROUP}
							</td>
						</tr>

						<tr style="height: 40px">
							<td style="width: 30%;">소분류</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_SGROUP}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">단 위</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_UNIT}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">ERP 상품코드</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_ERPMODEL}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">시작일</td>
							<td style="width: 70%;text-align: left;">
							${ModelInfo.MMC_StartDay}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">종료일</td>
							<td style="width: 70%;text-align: left;">
							${ModelInfo.MMC_EndDay}
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">운영여부</td>
							<td style="width: 70%;text-align: left;">
							<c:if test="${ModelInfo.MMC_BizSts == 'Y'}">운영</c:if>
							<c:if test="${ModelInfo.MMC_BizSts == 'N'}">종료</c:if>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="width: 30%;">비 고</td>
							<td style="width: 70%;text-align: left;">
								${ModelInfo.MMC_CMNT}
							</td>
						</tr>
					</tbody>
				</table>
			</div>
						
			<div style="text-align: center;">
				<c:if test="${level == '001'}">
				<a href="javascript:save();" class="search grey button">수정</a>
				</c:if>
				<a href="javascript:barcode();" class="search grey button">바코드 출력</a>
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