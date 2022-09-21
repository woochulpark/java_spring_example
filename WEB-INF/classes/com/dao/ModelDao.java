package com.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vo.ModelVo;

@Repository("modelDao")
public class ModelDao {
	
	@Resource(name="sqlSession")
	private SqlSessionTemplate sqlSessionTemplate;
	
	public ModelVo getModelInfo(String MCODE) {
		return sqlSessionTemplate.selectOne("getModelInfo", MCODE);
	}
	
	public int getLastModelDocNo(String LOGC, String LGROUP, String KINDS) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MMC_LOGC", LOGC);
		map.put("MMC_LGROUP", LGROUP);
		map.put("MMC_KINDS", KINDS);
		return sqlSessionTemplate.selectOne("getLastModelDocNo", map);
	}
	
	public List<ModelVo> getModelList(String LOGC, String LCODE, String MCODE, String SCODE, String MNAME, String MCODENAME, String SCODENAME, String BISSTS, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		map.put("SCODE", SCODE);
		map.put("MNAME", MNAME);
		map.put("MCODENAME", MCODENAME);
		map.put("SCODENAME", SCODENAME);
		map.put("BISSTS", BISSTS);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return sqlSessionTemplate.selectList("getModelList", map);
	}
	
	public List<ModelVo> ModelIlyeog(String MCODE){
		return sqlSessionTemplate.selectList("ModelIlyeog", MCODE);
	}
	
	public List<ModelVo> MgroupList(String LOGC, String LCODE) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("LCODE", LCODE);
		return sqlSessionTemplate.selectList("MgroupList", map);
	}
	
	public List<ModelVo> SgroupList(String LOGC, String LCODE, String MCODE) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		return sqlSessionTemplate.selectList("SgroupList", map);
	}
	
	public int insertModel(ModelVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertModel", vo);
	}
	
	public int insertModelIlyeog(ModelVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertModelIlyeog", vo);
	}
	
	public int updateModel(ModelVo vo) throws Exception{
		return sqlSessionTemplate.update("updateModel", vo);
	}
	
	public int deleteModel(String MCODE) throws Exception{
		return sqlSessionTemplate.delete("deleteModel", MCODE);
	}
	
	public int deleteModelIlyeog(String MCODE) throws Exception{
		return sqlSessionTemplate.delete("deleteModelIlyeog", MCODE);
	}
	
	public List<LinkedHashMap<String, Object>> MgroupAndroidList(String LOGC, String LCODE)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("LCODE", LCODE);
		return sqlSessionTemplate.selectList("MgroupAndroidList", map);
	}
	
	public List<LinkedHashMap<String, Object>> SgroupAndroidList(String LOGC, String LCODE, String MCODE)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		return sqlSessionTemplate.selectList("SgroupAndroidList", map);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidModelList(String LOGC, String LCODE, String MCODE, String SCODE, String MNAME) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		map.put("SCODE", SCODE);
		map.put("MNAME", MNAME);
		return sqlSessionTemplate.selectList("getAndroidModelList", map);
	}
}
