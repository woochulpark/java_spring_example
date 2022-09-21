package com.service;

import java.security.MessageDigest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.UserDao;
import com.vo.AndroidVo;
import com.vo.CenterVo;
import com.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public String getUserID(HttpServletRequest request){
		String user_id = request.getSession().getAttribute("user_id")==null?"":request.getSession().getAttribute("user_id").toString();
		return user_id;
	}
	
	public UserVo getUserInfo(String USERID){
		try {
			return userDao.getUserInfo(USERID);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public List<UserVo> getUserList(String LOGC, String BONBU, String CENTER, String UNAME, int PAGE, int PSIZE){
		return userDao.getUserList(LOGC, BONBU, CENTER, UNAME, PAGE, PSIZE);
	}
	
	public List<UserVo> getPOPUserList(String LOGC, String BONBU, String CENTER, String UNAME, int PAGE, int PSIZE){
		return userDao.getPOPUserList(LOGC, BONBU, CENTER, UNAME, PAGE, PSIZE);
	}
	
	public boolean login_process(String id, String pw, Map<String, Object> map){
		try {
			UserVo user = userDao.getUserInfo(id);
			String sha256Pass = encrypt(pw);
			if(user==null){
				map.put("result", "false");
				map.put("msg", "id 또는 패스워드가 일치하지 않습니다.");
			}else{
				if(sha256Pass.equals(user.getMUT_USERPWD())){
					if(user.getMUT_BIZSTS().equals("N"))
					{
						map.put("result", "false");
						map.put("msg", "퇴직한 사용자 입니다.");
					}
					else {
						map.put("result", "pass");
						map.put("msg", "");
					}
				}else{
					map.put("result", "false");
					map.put("msg", "id 또는 패스워드가 일치하지 않습니다.");
				}
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	public AndroidVo android_login(String id, String pw){
		AndroidVo vo = new AndroidVo();
		if(id.length() == 0) {
			vo.returnCode = "1004";
			vo.returnMsg = "ID를 받아오지 못했습니다.";
			return vo;
		}
		if(pw.length() == 0) {
			vo.returnCode = "1004";
			vo.returnMsg = "패스워드를 받아오지 못했습니다.";
			return vo;
		}
		try {
			UserVo user = userDao.getUserInfo(id);
			if(user==null){
				vo.returnCode = "1007";
				vo.returnMsg = "id 또는 패스워드가 일치하지 않습니다.";
			}else{
				if(pw.equals(user.getMUT_USERPWD())){
					if(user.getMUT_BIZSTS().equals("N")) {
						vo.returnCode = "1007";
						vo.returnMsg = "퇴직한 사용자 입니다.";
					}
					else {
						vo.returnCode = "0000";
						vo.field1 = user.getMUT_USERID();
						vo.field2 = user.getMUT_USERNAME();
						vo.field3 = user.getMUT_CENTER();
						vo.field4 = user.getMUT_POSITION();
					}
				}else{
					vo.returnCode = "1007";
					vo.returnMsg = "id 또는 패스워드가 일치하지 않습니다.";
				}
			}
		}
		catch (Exception e) {
			vo.returnCode = "1005";
			vo.returnMsg = "DB에러!! DB연결상태를 확인해주세요.";
		}
		return vo;
	}
	public String encrypt(String planText) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(planText.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public boolean session_check(HttpServletRequest request){
		String session_id = (String) request.getSession().getAttribute("user_id");

		if (session_id == null || session_id == "") {
			return false;
		}
		return true;
	}
	
	public int getIDCHECK(String USERID){
		try {
			return userDao.getIDCHECK(USERID);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int insertUser(UserVo vo){
		try {
			return userDao.insertUser(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int updateUser(UserVo vo){
		try {
			return userDao.updateUser(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteUser(String USERID){
		try {
			return userDao.deleteUser(USERID);
		}
		catch (Exception e) {
			return -1;
		}
	}
}
