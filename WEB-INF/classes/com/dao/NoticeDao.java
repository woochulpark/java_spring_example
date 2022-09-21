package com.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vo.NoticeVo;
import com.vo.UserVo;

@Repository("noticeDao")
public class NoticeDao {
	
	@Resource(name="sqlSession")
	private SqlSessionTemplate sqlSessionTemplate;
	
	public NoticeVo getNoticeInfo(String DOCNO) {
		return sqlSessionTemplate.selectOne("getNoticeInfo", DOCNO);
	}
	
	public NoticeVo getLastNoticeDocNo() {
		return sqlSessionTemplate.selectOne("getLastNoticeDocNo");
	}
	
	public List<NoticeVo> getNoticeList(String TITLE, int PAGE, int PSIZE, String USERID){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("TITLE", TITLE);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		map.put("USERID", USERID);
		return sqlSessionTemplate.selectList("getNoticeList", map);
	}
	public List<NoticeVo> getNoticeMainList(int PAGE, int PSIZE, String USERID){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		String PTYPE = "Y";
		if(PSIZE == 0)
			PTYPE = "N";
		map.put("PTYPE", PTYPE);
		map.put("USERID", USERID);
		return sqlSessionTemplate.selectList("getNoticeMainList", map);
	}
	public NoticeVo getAndroidNoticeInfo(String DOCNO) throws Exception{
		return sqlSessionTemplate.selectOne("getAndroidNoticeInfo", DOCNO);
	}
	public List<LinkedHashMap<String, Object>> getAndroidFileList(String DOCNO) throws Exception{
		return sqlSessionTemplate.selectList("getAndroidFileList", DOCNO);
	}
	public List<LinkedHashMap<String, Object>> getAndroidNoticeMainList(String USERID) throws Exception{
		return sqlSessionTemplate.selectList("getAndroidNoticeMainList", USERID);
	}
	public List<NoticeVo> getNoticeMainPopup(String USERID){
		return sqlSessionTemplate.selectList("getNoticeMainPopup", USERID);
	}
	public List<UserVo> getSusinList(String DOCNO){
		return sqlSessionTemplate.selectList("getSusinList", DOCNO);
	}
	public List<NoticeVo> getNoticeFileList(String DOCNO) throws Exception{
		return sqlSessionTemplate.selectList("getNoticeFileList", DOCNO);
	}
	
	public NoticeVo getNoticeFileInfo(String DOCNO,String SEQ) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("SEQ", SEQ);
		return sqlSessionTemplate.selectOne("getNoticeFileInfo", map);
	}
	
	public List<NoticeVo> getNoticeUser(String DOCNO){
		return sqlSessionTemplate.selectList("getNoticeUser", DOCNO);
	}
	
	public int deleteNoticeFile(String DOCNO,String SEQ) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("SEQ", SEQ);
		return sqlSessionTemplate.delete("deleteNoticeFile", map);
	}
	
	public int insertSusinList(List<NoticeVo> list) {
		return sqlSessionTemplate.insert("insertSusinList", list);
	}
	
	public int insertNotice(NoticeVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertNotice", vo);
	}
	
	public int insertNoticeFile(NoticeVo vo) throws Exception{
		return sqlSessionTemplate.insert("insertNoticeFile", vo);
	}
	
	public int updateNotice(NoticeVo vo) throws Exception{
		return sqlSessionTemplate.update("updateNotice", vo);
	}
	
	public int updateNoticeUser(String DOCNO,String USERID) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("USERID", USERID);
		return sqlSessionTemplate.update("updateNoticeUser", map);
	}
	
	public int deleteSusinList(String DOCNO) {
		return sqlSessionTemplate.delete("deleteSusinList", DOCNO);
	}
	
	public int deleteNoticeUser(String DOCNO) {
		return sqlSessionTemplate.delete("deleteNoticeUser", DOCNO);
	}
	
	public int deleteNotice(String DOCNO) {
		return sqlSessionTemplate.delete("deleteNotice", DOCNO);
	}
}
