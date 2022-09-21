package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.CenterDao;
import com.vo.CenterVo;

@Service
public class CenterService {
	@Autowired
	private CenterDao centerDao;
	
	public List<CenterVo> getLOGCInfo(){
		return centerDao.getLOGCInfo();
	}
	
	public List<CenterVo> getBONBUList(String LOGC){
		return centerDao.getBONBUList(LOGC);
	}
	
	public List<CenterVo> getCENTERList(String LOGC, String BONBU){
		return centerDao.getCENTERList(LOGC, BONBU);
	}
	
	public CenterVo getLOGCInfo_Detail(String LOGC){
		return centerDao.getLOGCInfo_Detail(LOGC);
	}
	
	public CenterVo getBONBUInfo_Detail(String LOGC, String BONBU){
		return centerDao.getBONBUInfo_Detail(LOGC, BONBU);
	}
	
	public CenterVo getCENTERInfo_Detail(String LOGC, String BONBU, String CENTER){
		return centerDao.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
	}
	
	public List<CenterVo> getLOGCDetail(String LOGC){
		List<CenterVo> LOGCList = centerDao.getLOGCDetail(LOGC);
		HashMap<String, Integer> LhashMap = new HashMap<String, Integer>();
		for (CenterVo CenterVo : LOGCList) {
			String TEMP_LOGC = CenterVo.getMCI_LOGC();
			if(LhashMap.containsKey(TEMP_LOGC)) {
				LhashMap.put(TEMP_LOGC, LhashMap.get(TEMP_LOGC) + 1);
			}
			else {
				LhashMap.put(TEMP_LOGC, 1);
			}
		}
		List<CenterVo> returnList = new ArrayList<CenterVo>();
		for (CenterVo CenterVo : LOGCList) {
			String TEMP_LOGC = CenterVo.getMCI_LOGC();
			String TEMP_BONBU = CenterVo.getMCI_Bonbu();
			if(LhashMap.get(TEMP_LOGC) > 1 && !TEMP_BONBU.equals(" ")) {
				returnList.add(CenterVo);
			}
			else if(LhashMap.get(TEMP_LOGC) == 1) {
				returnList.add(CenterVo);
			}
		}
		return returnList;
	}
	
	public List<CenterVo> getBONBUDetail(String LOGC, String BONBU){
		List<CenterVo> BONBUList = centerDao.getBONBUDetail(LOGC, BONBU);
		HashMap<String, Integer> MhashMap = new HashMap<String, Integer>();
		for (CenterVo CenterVo : BONBUList) {
			String TEMP_LOGC = CenterVo.getMCI_LOGC();
			String TEMP_BONBU = CenterVo.getMCI_Bonbu();
			if(TEMP_LOGC.equals(LOGC) && TEMP_BONBU.equals(BONBU)) {
				if(MhashMap.containsKey(TEMP_BONBU)) {
					MhashMap.put(TEMP_BONBU, MhashMap.get(TEMP_BONBU) + 1);
				}
				else {
					MhashMap.put(TEMP_BONBU, 1);
				}
			}
		}
		List<CenterVo> returnList = new ArrayList<CenterVo>();
		for (CenterVo CenterVo : BONBUList) {
			String TEMP_LOGC = CenterVo.getMCI_LOGC();
			String TEMP_BONBU = CenterVo.getMCI_Bonbu();
			String TEMP_CENTER = CenterVo.getMCI_Center();
			if(TEMP_LOGC.equals(LOGC)) {
				if(TEMP_BONBU.equals(BONBU)) {
					if(MhashMap.get(TEMP_BONBU) > 1 && !TEMP_CENTER.equals(" ")) {
						returnList.add(CenterVo);
					}
					else if(MhashMap.get(TEMP_BONBU) == 1) {
						returnList.add(CenterVo);
					}
				}
				else if(!TEMP_BONBU.equals(" ")){
					returnList.add(CenterVo);
				}
			}
			else {
				returnList.add(CenterVo);
			}
		}
		return returnList;
	}
	
	public List<CenterVo> getLOGCNAMESearch(String LNAME){
		return centerDao.getLOGCNAMESearch(LNAME);
	}
	
	public List<CenterVo> getBNAMESearch(String MNAME){
		return centerDao.getBNAMESearch(MNAME);
	}
	
	public List<CenterVo> getCNAMESearch(String SNAME){
		return centerDao.getCNAMESearch(SNAME);
	}
	public int insertCenter(CenterVo vo){
		try {
			return centerDao.insertCenter(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int updateLOGC(CenterVo vo){
		try {
			return centerDao.updateLOGC(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int updateLOGCNM(CenterVo vo){
		try {
			return centerDao.updateLOGCNM(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int updateBONBU(CenterVo vo){
		try {
			return centerDao.updateBONBU(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int updateCENTER(CenterVo vo){
		try {
			return centerDao.updateCENTER(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int deleteLOGC(String LOGC){
		try {
			return centerDao.deleteLOGC(LOGC);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int deleteBONBU(String LOGC, String BONBU){
		try {
			return centerDao.deleteBONBU(LOGC, BONBU);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int deleteCENTER(String LOGC, String BONBU, String CENTER){
		try {
			return centerDao.deleteCENTER(LOGC, BONBU, CENTER);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidLOGCInfo() throws Exception{
		return centerDao.getAndroidLOGCInfo();
	}

	public List<LinkedHashMap<String, Object>> getAndroidBONBUList(String LOGC) throws Exception{
		return centerDao.getAndroidBONBUList(LOGC);
	}

	public List<LinkedHashMap<String, Object>> getAndroidCENTERList(String LOGC, String BONBU) throws Exception{
		return centerDao.getAndroidCENTERList(LOGC, BONBU);
	}
}
