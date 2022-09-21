function getCookie(name){
	var nameOfCookie = name + "=";
	var x=0;
	
	while(x<= document.cookie.length){
		var y = (x+nameOfCookie.length);
		if(document.cookie.substring(x,y) == nameOfCookie){
			if((endOfCookie = document.cookie.indexOf(";", y)) == -1){
				endOfCookie = document.cookie.length;
			}
			return unescape(document.cookie.substring(y,endOfCookie));
		}
		x=document.cookie.indexOf("",x) +1;
		if(x==0) break;
	}
	return "";
}

function checkAccMenu(id){
	var value = "ID=" + id;
	
	if(id != 'ADMIN'){
		$.ajax({
			type: 'POST',
			url: './checkAccMenu.do',
			dataType: 'JSON',
			data: value,
			success: function(data){
				if(data.LIST.length == 0){
					alert('등록된 사용 가능한 메뉴가 없습니다 !');
					window.location = "index.do";
					return;
				}
			}
		});
	}
}

function mergeRows() {  
	var argu  = mergeRows.arguments; 
	var cnt  = 1;                                        // rowspan 값 
	var oTbl  = argu[0];     //테이블 객체      // 비교할 Table Object, default=첫번째 테이블 
	var oRow;                                            // 현재 Row Object 
	var oCell;                                            // 현재 Cell Object 
	var iRow;                                            // 이전에 일치했던 Row Index 
	var iCell = (argu[1] == null) ? 0 : argu[1];            // 비교할 Cell Index, default=0 
	var vPre;                                            // 이전에 일치했던 값 
	var vCur;                                            // 현재 값 
	var bChk  = false;                                    // 처음 일치인지 여부 
	
	if (vCur == '' || vCur == null){
		try { 
		 	for (var i=0; i<oTbl.rows.length; i++) {              // Row Index만큼 Loop 
		   		for (var j=0; j<oTbl.rows[i].cells.length; j++) { // Cell Index 만큼 Loop 
		    		if (iCell == -1 || iCell == j) {                              // 비교할 Cell Index와 현재 Cell Index가 동일하면,, 
		    			vCur = oTbl.rows[i].cells[j].innerHTML; 
		     
	//	     			if (vPre == vCur) {                          // 이전값과 현재값이 동일하면,, 
		     			if (vPre == vCur && vCur != '' && vCur != null) {                          // 이전값과 현재값이 동일하면,, 
		      				if (bChk == false) {                        // 처음 일치시에만 적용 
		       					iRow = i-1; 
		       					bChk = true; 
		      				} // end of if 
		
		      				cnt++; 
		      				oTbl.rows[iRow].cells[j].rowSpan = cnt; 
		      				oTbl.rows[i].deleteCell(j); 
		     			} else {                                      // 이전값과 현재값이 다르면,, 
		      				cnt = 1; 
		      				vPre = vCur; 
		      				bChk = false; 
		     			} // end of if 
		
		     			break; 
		    		} // end of if 
		   		} // end of for 
		  	} // end of for 
		} catch (e) { 
		} // end of try{} catch{} 
	}
} // end of function 

$(document).ready(function(){
	if(document.getElementById('menuGroup').value == "menu_home"){
		document.getElementById('menu_home').className = "homeselected";
		menu_home_title.style.color = "#ce282f";
	}else{
		document.getElementById('menu_home').className = "home";
	}
	if(document.getElementById('menuGroup').value == "menu_base"){
		document.getElementById('menu_base').className = "configselected";
		menu_base_title.style.color = "#ce282f";
	}else{
		document.getElementById('menu_base').className = "config";
	}
	if(document.getElementById('menuGroup').value == "menu_Inout"){
		document.getElementById('menu_Inout').className = "conditionselected";
		menu_Inout_title.style.color = "#ce282f";
	}else{
		document.getElementById('menu_Inout').className = "condition";
	}
	if(document.getElementById('menuGroup').value == "menu_stats"){
		document.getElementById('menu_stats').className = "staticsselected";
		menu_stats_title.style.color = "#ce282f";
	}else{
		document.getElementById('menu_stats').className = "statics";
	}
	if(document.getElementById('menuGroup').value == "menu_customer"){
		document.getElementById('menu_customer').className = "conditionselected";
		menu_customer_title.style.color = "#ce282f";
	}else{
		document.getElementById('menu_customer').className = "condition";
	}
})

