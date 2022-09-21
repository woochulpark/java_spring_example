package com.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vo.CenterVo;

@Repository("centerDao")
public class CenterDao {
	
	@Resource(name="sqlSession")
	private SqlSessionTemplate session;
	
	public List<CenterVo> getLOGCInfo(){
		return session.selectList("getLOGCInfo");
	}
	
	public List<CenterVo> getBONBUList(String LOGC){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		return session.selectList("getBONBUList", map);
	}
	
	public List<CenterVo> getCENTERList(String LOGC, String BONBU){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		return session.selectList("getCENTERList", map);
	}
	
	public CenterVo getLOGCInfo_Detail(String LOGC){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		return session.selectOne("getLOGCInfo_Detail", map);
	}
	
	public CenterVo getBONBUInfo_Detail(String LOGC, String BONBU){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		return session.selectOne("getBONBUInfo_Detail", map);
	}
	
	public CenterVo getCENTERInfo_Detail(String LOGC, String BONBU, String CENTER){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		return session.selectOne("getCENTERInfo_Detail", map);
	}
	
	public List<CenterVo> getLOGCDetail(String LOGC){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		return session.selectList("getLOGCDetail", map);
	}
	
	public List<CenterVo> getBONBUDetail(String LOGC, String BONBU){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		return session.selectList("getBONBUDetail", map);
	}
	
	public List<CenterVo> getLOGCNAMESearch(String LNAME){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LNAME", LNAME);
		return session.selectList("getLOGCNAMESearch", map);
	}
	
	public List<CenterVo> getBNAMESearch(String BNAME){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("BNAME", BNAME);
		return session.selectList("getBNAMESearch", map);
	}
	
	public List<CenterVo> getCNAMESearch(String CNAME){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CNAME", CNAME);
		return session.selectList("getCNAMESearch", map);
	}
	public int insertCenter(CenterVo vo) throws Exception{
		return session.insert("insertCenter", vo);
	}
	public int updateLOGC(CenterVo vo) throws Exception{
		return session.update("updateLOGC", vo);
	}
	public int updateLOGCNM(CenterVo vo) throws Exception{
		return session.update("updateLOGCNM", vo);
	}
	public int updateBONBU(CenterVo vo) throws Exception{
		return session.update("updateBONBU", vo);
	}
	public int updateCENTER(CenterVo vo) throws Exception{
		return session.update("updateCENTER", vo);
	}
	public int deleteLOGC(String LOGC) throws Exception{
		return session.update("deleteLOGC", LOGC);
	}
	public int deleteBONBU(String LOGC, String BONBU) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		return session.update("deleteBONBU", map);
	}
	public int deleteCENTER(String LOGC, String BONBU, String CENTER) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		return session.update("deleteCENTER", map);
	}
	public List<CenterVo> getAllList(Map<String, Object> map){
		return session.selectList("getAllList", map);
	}
	public List<LinkedHashMap<String, Object>> getAndroidLOGCInfo() throws Exception{
		return session.selectList("getAndroidLOGCInfo");
	}
	public List<LinkedHashMap<String, Object>> getAndroidBONBUList(String LOGC) throws Exception{
		return session.selectList("getAndroidBONBUList", LOGC);
	}
	public List<LinkedHashMap<String, Object>> getAndroidCENTERList(String LOGC, String BONBU) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		return session.selectList("getAndroidCENTERList", map);
	}
	public int getCenterJego(String CENTER){
		return session.selectOne("getCenterJego", CENTER);
	}
	public int getCenterUser(String CENTER){
		return session.selectOne("getCenterUser", CENTER);
	}
}
