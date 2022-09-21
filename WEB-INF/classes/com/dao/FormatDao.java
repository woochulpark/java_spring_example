package com.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vo.DealFormatVo;
import com.vo.DealModelVo;
import com.vo.JegoFormatVo;
import com.vo.JegoModelVo;
import com.vo.LogcInVo;

@Repository("formatDao")
public class FormatDao {
	@Resource(name="sqlSession")
	private SqlSessionTemplate sqlSessionTemplate;
	
	public DealFormatVo getDealFormatInfo(String DOCNO) {
		return sqlSessionTemplate.selectOne("getDealFormatInfo", DOCNO);
	}
	
	public List<DealFormatVo> getDealFormatList(Map<String, Object> map) {
		return sqlSessionTemplate.selectList("getDealFormatList", map);
	}
	
	public List<DealFormatVo> getDealInoutInfo(Map<String, Object> map) {
		return sqlSessionTemplate.selectList("getDealInoutInfo", map);
	}
	
	public List<DealModelVo> getFormatModelList(String DOCNO) {
		return sqlSessionTemplate.selectList("getFormatModelList", DOCNO);
	}
	
	public DealFormatVo getFormatLogcInfo(String DOCNO) {
		return sqlSessionTemplate.selectOne("getFormatLogcInfo", DOCNO);
	}
	
	public DealFormatVo getFormatBonbuInfo(String DOCNO) {
		return sqlSessionTemplate.selectOne("getFormatBonbuInfo", DOCNO);
	}
	public int insertDocTRAN(DealFormatVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertDocTRAN", vo);
	}
	public DealFormatVo getLastFormatDocNo() {
		return sqlSessionTemplate.selectOne("getLastFormatDocNo");
	}
	public int insertDocTRANModel(DealModelVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertDocTRANModel", vo);
	}
	public int updateDocTRANCfm(DealFormatVo vo) throws Exception{
		return sqlSessionTemplate.update("updateDocTRANCfm", vo);
	}
	public List<JegoFormatVo> getJegoFormatList(Map<String, Object> map) {
		return sqlSessionTemplate.selectList("getJegoFormatList", map);
	}
	public JegoFormatVo getLastJegoFormatDocNo() {
		return sqlSessionTemplate.selectOne("getLastJegoFormatDocNo");
	}
	public int insertJegoDocTRAN(JegoFormatVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertJegoDocTRAN", vo);
	}
	public int insertJegoDocTRANModel(JegoModelVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertJegoDocTRANModel", vo);
	}
	public JegoFormatVo getJegoFormatInfo(String DOCNO) {
		return sqlSessionTemplate.selectOne("getJegoFormatInfo", DOCNO);
	}
	public int updateJegoDocTRANCfm(JegoFormatVo vo) throws Exception{
		return sqlSessionTemplate.update("updateJegoDocTRANCfm", vo);
	}
	public List<JegoModelVo> getJegoFormatModelList(String DOCNO) {
		return sqlSessionTemplate.selectList("getJegoFormatModelList", DOCNO);
	}
}
