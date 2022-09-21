<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" type="text/css" media="all" href="css/style.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	getLogin();
	var PMSG = '${msg}';
	if(PMSG.length > 0){
		alert(PMSG);
	}
});

function login(){
	var form = document.form;
	
	//쿠기 저장 부분
	if(form.saveid.checked==true){
 		setCookie("user_id",document.getElementById("id").value,9999) //쿠키이름을 저장
	}else{ //아이디 저장을 체크하지 않았을때
 		setCookie("user_id",document.getElementById("id").value,0) //날짜를 0으로 저장하여 쿠키 삭제
	}

	if(form.savepwd.checked==true){
 		setCookie("password",document.getElementById("passwd").value,9999) //쿠키이름을 저장
	}else{ //아이디 저장을 체크하지 않았을때
 		setCookie("password",document.getElementById("passwd").value,0) //날짜를 0으로 저장하여 쿠키 삭제
	}
    form.submit();
}

// 쿠키에 아디이 저장
function setCookie(name, value, expiredays){
	var todayDate = new Date();
	todayDate.setDate(todayDate.getDate() + expiredays);
	document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";";
}

 //쿠키 불러오는 함수
function getCookie(Name){ 
	 var search =Name + "=";
	if(document.cookie.length > 0){
 		offset = document.cookie.indexOf(search);

 		if (offset != -1){
			offset += search.length;
			end = document.cookie.length;
			
			if(end == -1)
				end =document.cookie.length;
			return unescape(document.cookie.substring(offset,end));
		}
	}
}

function getLogin(){
	var form = document.form;
	
	document.getElementById('id').focus();
	
	if (getCookie("user_id")){ 
		var id_value = getCookie("user_id").split(";")
		document.getElementById("id").value=id_value[0]; 
		form.saveid.checked=true;  
	} 
	if (getCookie("password")){ 
		var pwd_value = getCookie("password").split(";")
		document.getElementById("passwd").value=pwd_value[0]; 
		form.savepwd.checked=true;  
	} 
	/*
	if (getCookie("autologin")){ 
		var auto_value = getCookie("autologin").split(";");
		form.autologin.checked=true;  
	} 
	
	if (form.autologin.checked == true){
		login();
	}
	*/
}

function checkIt() {
	var form = document.form;
	var id = document.getElementById('id').value;
	var passwd = document.getElementById('passwd').value;
	
	if( id==null || id=="" || id=="ID를 입력하세요") {
		alert('아이디를 입력해주세요!');
		document.getElementById('id').focus();
		return;
	}
	if( passwd==null || passwd=="" || passwd=="비밀번호를 입력하세요" ) {
		alert('비밀번호를 입력해 주세요!');
		passwd = document.getElementById('passwd').focus();
		return;
	}
	login();
}
</script>
<head>
<meta charset="UTF-8">
<title>A+라이프 재고관리 시스템</title>
</head>
<body class="home image-sphere-style responsive" style="background-image:url('images/backImage.jpg'); background-repeat: round;">
	<div style="width: 100%;height: 100%;vertical-align:middle">
		<div id="login">
			<form action="login.do" method="post" name="form">
				<div>
					<input tabindex="1" name="id" type="text" value='' id="id" onkeyup="if((arguments[0]||event).keyCode == 13) javascript:checkIt()" placeholder="사번"/>
				</div>
				<div>
					<input tabindex="2" name="password" type="password" value='' id="passwd" onkeyup="if((arguments[1]||event).keyCode == 13) javascript:checkIt()" placeholder="패스워드" />
				</div>
				<div style = "padding:5px 0 0px;margin-bottom:0;width:100%;color:#545252;font-size: 1em;">
					<input type="checkbox" name="saveid" value="" style = "border: 0px;padding: 0px;">아이디저장
					<input type="checkbox" name="savepwd" value="" style = "border: 0px;padding: 0px;">비밀번호저장
				</div>
				<div style = "margin-top: 40px;padding:5px 0 0px;margin-bottom:0;margin-right:2%;width:100%">
					<a href="javascript:checkIt();" class="login grey button">로그인</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>