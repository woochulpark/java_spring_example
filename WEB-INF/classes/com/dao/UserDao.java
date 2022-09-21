package com.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vo.UserVo;

@Repository("userDAO")
public class UserDao {
	
	@Resource(name="sqlSession")
	private SqlSessionTemplate session;
	
	public UserVo getUserInfo(String USERID) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("USERID", USERID);
		return session.selectOne("getUserInfo", map);
	}
	
	public List<UserVo> getDealUserInfo(String CENTER){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CENTER", CENTER);
		return session.selectList("getDealUserInfo", map);
	}
	
	public List<UserVo> getUserList(String LOGC, String BONBU, String CENTER, String UNAME, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("UNAME", UNAME);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return session.selectList("getUserList", map);
	}
	
	public List<UserVo> getPOPUserList(String LOGC, String BONBU, String CENTER, String UNAME, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("UNAME", UNAME);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return session.selectList("getPOPUserList", map);
	}
	
	public int getIDCHECK(String USERID) throws Exception{
		return session.selectOne("getIDCHECK", USERID);
	}
	
	public int insertUser(UserVo vo) throws Exception{
		return session.insert("insertUser", vo);
	}
	
	public int updateUser(UserVo vo) throws Exception{
		return session.update("updateUser", vo);
	}
	
	public int deleteUser(String USERID) throws Exception{
		return session.delete("deleteUser", USERID);
	}
}
