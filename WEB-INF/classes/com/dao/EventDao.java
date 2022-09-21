package com.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vo.InOutListVo;

@Repository("eventDAO")
public class EventDao {
	@Resource(name="sqlSessionErp")
	private SqlSessionTemplate session;
	
	public List<InOutListVo> getEventInfo(Map<String, Object> map){
		return session.selectList("getEventInfo", map);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidEventInfo(Map<String, Object> map) throws Exception{
		return session.selectList("getAndroidEventInfo", map);
	}
	
	public List<InOutListVo> getGTYPELIST(){
		return session.selectList("getGTYPELIST");
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidGTYPELIST() throws Exception{
		return session.selectList("getAndroidGTYPELIST");
	}
	
	public LinkedHashMap<String, Object> getAndroidEventDetail(String DOCNO) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		return session.selectOne("getAndroidEventDetail", map);
	}
}
