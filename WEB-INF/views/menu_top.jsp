<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	DecimalFormat df = new DecimalFormat("00");
	Calendar cal = Calendar.getInstance();
	int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
	String[] week = {"일", "월", "화", "수", "목", "금", "토"};
	int hour = cal.get(Calendar.HOUR_OF_DAY);
	int min = cal.get(Calendar.MINUTE);
	String ampm = "";
	if(hour > 12){
		hour -= 12;
		ampm = "오후";
	}else{
		ampm = "오전";
	}
	String Today = Integer.toString(cal.get(Calendar.YEAR)) + "년 " + df.format(cal.get(Calendar.MONTH) + 1) + "월 " + df.format(cal.get(Calendar.DATE)) + "일 (" + week[day_of_week-1] + ") "
			+ampm+" "+ hour +"시 " + min + "분";
%>
                <div id="topbar">
                    <div class="inner">
					<ul class="topbar_title">
						 <li class="title"></li></ul>
                        <ul class="topbar_links">
                            <li><span id = 'Today'><%=Today%></span>&nbsp;&nbsp;|</li>
							<li><img src="images/user_icon.png" style="vertical-align: middle;"><a href="User_Modify.do?USERID=${Userinfo.MUT_USERID}">${Userinfo.MUT_USERNAME} 님</a> 환영합니다 <a href="logout.do" class="xsmall grey button" style="valign: middle;">로그아웃</a>  
                            </li>
                        </ul>
                        <div class="clear"></div>
                    </div>
                    <!-- end.inner -->
                </div>
  