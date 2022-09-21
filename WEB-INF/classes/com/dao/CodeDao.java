package com.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vo.CodeVo;

@Repository("codeDao")
public class CodeDao{
	@Resource(name="sqlSession")
	private SqlSessionTemplate session;
	
	public List<CodeVo> getLcodeInfo(){
		return session.selectList("getLcodeInfo");
	}
	
	public List<CodeVo> getMCodeList(String LCODE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		return session.selectList("getMCodeList", map);
	}
	
	public List<CodeVo> getSCodeList(String LCODE, String MCODE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		return session.selectList("getSCodeList", map);
	}
	
	public CodeVo getLcodeInfo_Detail(String LCODE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		return session.selectOne("getLcodeInfo_Detail", map);
	}
	
	public CodeVo getMcodeInfo_Detail(String LCODE, String MCODE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		return session.selectOne("getMcodeInfo_Detail", map);
	}
	
	public CodeVo getScodeInfo_Detail(String LCODE, String MCODE, String SCODE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		map.put("SCODE", SCODE);
		return session.selectOne("getScodeInfo_Detail", map);
	}
	
	public List<CodeVo> getLcodeDetail(String LCODE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		return session.selectList("getLcodeDetail", map);
	}
	
	public List<CodeVo> getMcodeDetail(String LCODE, String MCODE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		return session.selectList("getMcodeDetail", map);
	}
	
	public List<CodeVo> getLNAMESearch(String LNAME){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LNAME", LNAME);
		return session.selectList("getLNAMESearch", map);
	}
	
	public List<CodeVo> getMNAMESearch(String MNAME){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MNAME", MNAME);
		return session.selectList("getMNAMESearch", map);
	}
	
	public List<CodeVo> getSNAMESearch(String SNAME){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("SNAME", SNAME);
		return session.selectList("getSNAMESearch", map);
	}
	public int insertCODE(CodeVo vo) throws Exception{
		return session.insert("insertCODE", vo);
	}
	public int updateLCODE(CodeVo vo) throws Exception{
		return session.update("updateLCODE", vo);
	}
	public int updateMCODE(CodeVo vo) throws Exception{
		return session.update("updateMCODE", vo);
	}
	public int updateSCODE(CodeVo vo) throws Exception{
		return session.update("updateSCODE", vo);
	}
	public int deleteCODE(String LCODE) throws Exception{
		return session.update("deleteCODE", LCODE);
	}
	public int deleteMCODE(String LCODE, String MCODE) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		return session.update("deleteMCODE", map);
	}
	public int deleteSCODE(String LCODE, String MCODE, String SCODE) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		map.put("SCODE", SCODE);
		return session.update("deleteSCODE", map);
	}
	public List<LinkedHashMap<String, Object>> getAndroidSCodeList(String LCODE, String MCODE) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		return session.selectList("getAndroidSCodeList", map);
	}
}
