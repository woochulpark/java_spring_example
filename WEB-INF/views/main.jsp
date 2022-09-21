<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
<script language="javascript">
var PMSG = '${uploadPath}';
if(PMSG.length > 0){
	alert(PMSG);
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
		}, error: function(xmlHttpObj){
			alert(xmlHttpObj.responseText+'오류가 발생하였습니다.');
		}
	});	

    $el.find("#BTN_POP_CLOSE").click(function(){
        isDim ? $('#dim-layer').fadeOut() : $el.fadeOut(); // 닫기 버튼을 클릭하면 레이어가 닫힌다.
        return false;
    });

    $('.layer .dimBg').click(function(){
        $('#dim-layer').fadeOut();
        return false;
    });
}


function pop_Draggable(id){
	$('#'+id).draggable();
}

function pop_close(type,dimid,docno)
{
	$('#'+dimid).fadeOut();
	//오늘하루보지않기 버튼을 누른 경우
    if(type == 1){
    	//'testCookie' 이름의 쿠키가 있는지 체크한다.
        if($.cookie('popcheck') == undefined){
            //쿠키가 없는 경우 testCookie 쿠키를 추가
            $.cookie('popcheck', docno, { expires: 1, path: '/' });
            /**
                설명 :
                임의로 testCookie라는 이름에 Y라는 값을 넣어주었고,
                expires값으로 1을 주어 1일 후 쿠키가 삭제되도록 하였다.
                path값을 '/'로 주면 해당사이트 모든페이지에서 유효한 쿠키를 생성한다.
                특정페이지에서만 작동하려면 페이지 경로를 작성하면 된다.
            **/
        }
        else{
        	 $.cookie('popcheck', $.cookie('popcheck') + "," + docno, { expires: 1, path: '/' });
        }
    }
}
</script>
</head>
<body class="home image-sphere-style responsive">
	<form:form name="noticeForm" method="post" action="main.do">
		<input type="hidden" name="menuGroup" value="menu_home" id="menuGroup">
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

			<div class="subtitle" style="text-align: right;">메인화면</div>
			<h2>공지사항</h2>

			<div class="scroll_table" style="min-height: 300px;">
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
						<tr style='height: 40px;<c:if test="${NoticeList.INFOCHK == 'N' }">font-weight: bold;</c:if>'>
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
            <div id="dim-layer" class="dim-layer">
				<div class="dimBg"></div>
				<div id="layer_USERCHK" class="pop-layer" style="padding: 20px 25px;width: 800px;height: 600px;">
        			<div class="scroll_table" id="pop_table" style="height: 550px;overflow: auto;margin-bottom: 10px;">
						
					</div>
					<div style="text-align: center;">
						<a href="#" id = "BTN_POP_CLOSE" class="search grey button">종료</a>
					</div>
    			</div>
			</div>
			<c:if test="${PopupListSize > 0 }">
			<c:forEach var="PopupList" items="${PopupList}" varStatus="st">
			<c:if test="${empty cookie.popcheck.value or fn:indexOf(cookie.popcheck.value,PopupList.POPID) == -1}">
			<div id="dim-layer${st.index}" class="popWindow" style="top: ${100 + (st.index * 50)}px;left: ${100 + (st.index * 50)}px" onclick="pop_Draggable('dim-layer${st.index}')">
				<div style="text-align: center;background-color: #B2CCFF;margin-bottom: 10px;">
					${PopupList.MNI_TITLE}
				</div>
				<div style="width: 95%;margin: auto;height: 300px;">
					<textarea readonly="readonly" style="width: 100%;height: 100%;">${PopupList.MNI_Cmnt}</textarea>
				</div>
				<div style="text-align: center;margin-top: 20px;">
					<a href="#" onClick="pop_close('1','dim-layer${st.index}','${PopupList.POPID}')" class="search grey button">하루 보이지 않기</a>
					<a href="#" onClick="pop_close('0','dim-layer${st.index}','${PopupList.POPID}')" class="search grey button">종료</a>
				</div>
			</div>
			</c:if>
			</c:forEach>
			</c:if>
		</div>
	</form:form>
</body>
</html>