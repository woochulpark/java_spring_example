<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
<script language="javascript">
$(document).ready(function(){
	var msg = "${msg}";
	if(msg != null && msg != ""){
		alert(msg);
	}
});
function search()
{
	var form = document.noticeForm;
	form.submit();
}
function Notice_Add(DOCNO)
{
	document.getElementById("DOCNO").value = DOCNO;
	var form = document.noticeForm;
	form.action = "Notice_Add.do";
	form.submit();
}
function fn_paging(PAGE)
{
	document.getElementById("PAGE").value = PAGE;
	var form = document.noticeForm;
	form.submit();
}
function USER_CHECK(DOCNO){
	var $el = $("#layer_USERCHK");
    var isDim = $el.prev().hasClass('dimBg');//dimmed 레이어를 감지하기 위한 boolean 변수
    $("#pop_table").empty();
	$.ajax({
		type:"POST",
		url:"Ajax_CheckUser.do",
		data: "DOCNO="+DOCNO,
		success:function(html){
			$("#pop_table").append(html);
			
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
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
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
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="noticeForm" method="post" action="Notice_search.do">
		<input type="hidden" name="menuGroup" value="menu_base" id="menuGroup">
		<input type="hidden" name="DOCNO" value="" id="DOCNO">
		<input type="hidden" name="PAGE" value="1" id="PAGE">
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

			<div class="subtitle" style="text-align: right;">기준 정보 관리 > 공지사항</div>
			<h2>공지사항</h2>

			<!-- SEARCHFILED FORM -->
			<div id="searchfield">
				<table style="width: 50%;">
					<tr>
						<td style="text-align: center;">제 목</td>
						<td>
							<input type="text" name="TITLE" id="TITLE" value="${TITLE}"  style="width:90%;">
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">
							<a href="javascript:search();" class="search grey button">검색</a>
						</td>
					</tr>
				</table>
			</div>
			<c:if test="${level == '001' or  level == '002'}">
			<!-- START TABLE -->
			<div style="text-align:right;">
				<a href="javascript:Notice_Add('')" class="search grey button">신 규</a>
			</div>
			</c:if>
			<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 10%;">번 호</th>
							<th style="width: 10%;">공지범위</th>
							<th style="width: 15%">공지일</th>
							<th style="width: 45%">제 목</th>
							<th style="width: 10%">작성자</th>
							<th style="width: 10%">공지확인</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${NoticeListSize > 0 }">
					<c:forEach var="NoticeList" items="${NoticeList}" varStatus="st">
						<tr style='height: 40px;<c:if test="${NoticeList.MNI_TempSts == 'N' }">background-color: #FFEAEA;</c:if><c:if test="${NoticeList.INFOCHK == 'N' }">font-weight: bold;</c:if>'>
							<td onclick="javascript:Notice_Add('${NoticeList.MNI_DOCNO}')">${NoticeList.rownum}</td>
							<td onclick="javascript:Notice_Add('${NoticeList.MNI_DOCNO}')">
								<c:choose>
            						<c:when test="${NoticeList.MNI_ReceiveType == '0'}">
            							전체
            						</c:when>
            						<c:when test="${NoticeList.MNI_ReceiveType == '1'}">
            							본부
            						</c:when>
            						<c:otherwise>
            							지정
            						</c:otherwise>
            					</c:choose>
							</td>
							<td onclick="javascript:Notice_Add('${NoticeList.MNI_DOCNO}')">${NoticeList.MNI_DATE}</td> 	
							<td onclick="javascript:Notice_Add('${NoticeList.MNI_DOCNO}')">${NoticeList.MNI_TITLE}</td>
							<td onclick="javascript:Notice_Add('${NoticeList.MNI_DOCNO}')">${NoticeList.MUT_UserName}</td> 	 
							<td onClick="USER_CHECK('${NoticeList.MNI_DOCNO}')">${NoticeList.USERCNT}</td>  	 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
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
            <div class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_USERCHK" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 600px;">
        			<div class="scroll_table" id="pop_table" style="height: 95%;overflow:auto;">
						
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종료</a>
					</div>
    			</div>
			</div>
		</div>
	</form:form>
</body>
</html>