<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.DecimalFormat"%>
<link rel="stylesheet" id="slider-cycle-css" href="css/slider-cycle.css" type="text/css" media="all" />
<script src="js/menu.js" language="javascript"></script>
<script src="js/common.js" language="javascript"></script>
<div id="header" class="group">
	<!-- START LOGO -->
	<div id="logo" class="group">
		<a href="main.do" title="A+라이프 재고관리 시스템"> 
			<img src="images/logo.png" alt="A+라이프 재고관리 시스템" />
		</a>
	</div>
	<!-- END LOGO -->

	<!-- START NAV -->
	<div id="nav" class="group">
		<ul id="menu" class="level-1 white">
			<li class="home" id="menu_home">
				<a href="main.do" id="menu_home_title">메인</a>
			</li>
			<li class="config" id="menu_base">
				<a href="#" id="menu_base_title">기준정보관리</a>
				<ul class="sub-menu">
					<c:if test="${level == '001'}">
					<li><a href="ComCode.do">공통코드관리</a></li>
					</c:if>
					<c:if test="${level == '001' or level == '002'}">
					<li><a href="User_list.do">사용자 관리</a></li>
					</c:if>
					<c:if test="${level == '001' or level == '002'}">
					<li><a href="ModelList.do">상품코드 관리</a></li>
					</c:if>
					<c:if test="${level == '001'}">
					<li><a href="Centerlist.do">본부 관리</a></li>
					</c:if>
					<li><a href="NOTICE_LIST.do">공지사항 관리</a></li>
				</ul>
			</li>
			<li class="condition" id="menu_Inout">
				<a href="#" id="menu_Inout_title">입출고</a>
				<ul class="sub-menu">
					<c:if test="${level == '001' or  level == '002'}"><li><a href="LogcInList.do">물류센터 입고</a></li></c:if>
					<c:if test="${level == '001' or  level == '002'}"><li><a href="BonbuInList.do">본부 입고</a></li></c:if>
					<li><a href="OrderList.do">본부 발주</a></li>
					<li><a href="BonbuMove_List.do">본부별 이동</a></li>
					<li><a href="Cfm_List.do">본부 입고 승인</a></li>
					<c:if test="${level == '001'}">
					<li><a href="OtherList.do">기타 출고</a></li>
					</c:if>
				</ul>
			</li>
			<li class="statics" id="menu_stats">
				<a href="#" id="menu_stats_title">관리</a>
				<ul class="sub-menu">
					<li><a href="InOutReport.do">입출고 현황</a></li>
					<li><a href="JegoReport.do">현재고 현황</a></li>
					<li><a href="InOutListReport.do">입출고 내역 조회</a></li>
					<li><a href="BonbuStatusReport.do">본부 현황</a></li>
					<li><a href="EventReturn.do">비품 설치 및 회수 현황</a></li>
					<li><a href="ModelBepum.do">비품 현황</a></li>
					<c:if test="${level == '001' or  level == '002'}"><li><a href="JegoMagamReport.do">재고 마감 현황</a></li></c:if>
				</ul>
			</li>
			<c:if test="${level != '005'}">
			<li class="condition" id="menu_customer">
				<a href="#" id="menu_customer_title">서식자료</a>
				<ul class="sub-menu">
					<c:if test="${level != '005'}"><li><a href="Dealformat_List.do">거래내역서 출력</a></li></c:if>
					<c:if test="${level != '005'}"><li><a href="Jegoformat_List.do">재고명세서 출력</a></li></c:if>
				</ul>
			</li>
			</c:if>
		</ul>
	</div>
	<!-- END NAV -->
</div>

<script type="text/javascript">
var dropdown=new TINY.dropdown.init("dropdown", {id:'menu', active:'menuhover'});
</script>
  