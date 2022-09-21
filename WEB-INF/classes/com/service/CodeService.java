package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.CodeDao;
import com.vo.CodeVo;
import com.vo.UserVo;

@Service
public class CodeService {
	@Autowired
	private CodeDao codeDao;
	
	public List<CodeVo> getLcodeInfo(){
		return codeDao.getLcodeInfo();
	}
	
	public List<CodeVo> getMCodeList(String LCODE){
		return codeDao.getMCodeList(LCODE);
	}
	
	public List<CodeVo> getSCodeList(String LCODE, String MCODE){
		return codeDao.getSCodeList(LCODE, MCODE);
	}
	
	public CodeVo getLcodeInfo_Detail(String LCODE){
		return codeDao.getLcodeInfo_Detail(LCODE);
	}
	
	public CodeVo getMcodeInfo_Detail(String LCODE, String MCODE){
		return codeDao.getMcodeInfo_Detail(LCODE, MCODE);
	}
	
	public CodeVo getScodeInfo_Detail(String LCODE, String MCODE, String SCODE){
		return codeDao.getScodeInfo_Detail(LCODE, MCODE, SCODE);
	}
	
	public List<CodeVo> getLcodeDetail(String LCODE){
		List<CodeVo> LcodeList = codeDao.getLcodeDetail(LCODE);
		HashMap<String, Integer> LhashMap = new HashMap<String, Integer>();
		for (CodeVo codeVo : LcodeList) {
			String TEMP_LCODE = codeVo.getMCC_L_CODE();
			if(LhashMap.containsKey(TEMP_LCODE)) {
				LhashMap.put(TEMP_LCODE, LhashMap.get(TEMP_LCODE) + 1);
			}
			else {
				LhashMap.put(TEMP_LCODE, 1);
			}
		}
		List<CodeVo> returnList = new ArrayList<CodeVo>();
		for (CodeVo codeVo : LcodeList) {
			String TEMP_LCODE = codeVo.getMCC_L_CODE();
			String TEMP_MCODE = codeVo.getMCC_M_CODE();
			if(LhashMap.get(TEMP_LCODE) > 1 && !TEMP_MCODE.equals(" ")) {
				returnList.add(codeVo);
			}
			else if(LhashMap.get(TEMP_LCODE) == 1) {
				returnList.add(codeVo);
			}
		}
		return returnList;
	}
	
	public List<CodeVo> getMcodeDetail(String LCODE, String MCODE){
		List<CodeVo> McodeList = codeDao.getMcodeDetail(LCODE, MCODE);
		HashMap<String, Integer> MhashMap = new HashMap<String, Integer>();
		for (CodeVo codeVo : McodeList) {
			String TEMP_LCODE = codeVo.getMCC_L_CODE();
			String TEMP_MCODE = codeVo.getMCC_M_CODE();
			if(TEMP_LCODE.equals(LCODE) && TEMP_MCODE.equals(MCODE)) {
				if(MhashMap.containsKey(TEMP_MCODE)) {
					MhashMap.put(TEMP_MCODE, MhashMap.get(TEMP_MCODE) + 1);
				}
				else {
					MhashMap.put(TEMP_MCODE, 1);
				}
			}
		}
		List<CodeVo> returnList = new ArrayList<CodeVo>();
		for (CodeVo codeVo : McodeList) {
			String TEMP_LCODE = codeVo.getMCC_L_CODE();
			String TEMP_MCODE = codeVo.getMCC_M_CODE();
			String TEMP_SCODE = codeVo.getMCC_S_CODE();
			if(TEMP_LCODE.equals(LCODE)) {
				if(TEMP_MCODE.equals(MCODE)) {
					if(MhashMap.get(TEMP_MCODE) > 1 && !TEMP_SCODE.equals(" ")) {
						returnList.add(codeVo);
					}
					else if(MhashMap.get(TEMP_MCODE) == 1) {
						returnList.add(codeVo);
					}
				}
				else if(!TEMP_MCODE.equals(" ")){
					returnList.add(codeVo);
				}
			}
			else {
				returnList.add(codeVo);
			}
		}
		return returnList;
	}
	
	public List<CodeVo> getLNAMESearch(String LNAME){
		return codeDao.getLNAMESearch(LNAME);
	}
	
	public List<CodeVo> getMNAMESearch(String MNAME){
		return codeDao.getMNAMESearch(MNAME);
	}
	
	public List<CodeVo> getSNAMESearch(String SNAME){
		return codeDao.getSNAMESearch(SNAME);
	}
	public int insertCODE(CodeVo vo){
		try {
			return codeDao.insertCODE(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int updateLCODE(CodeVo vo){
		try {
			return codeDao.updateLCODE(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int updateMCODE(CodeVo vo){
		try {
			return codeDao.updateMCODE(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int updateSCODE(CodeVo vo){
		try {
			return codeDao.updateSCODE(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int deleteCODE(String LCODE){
		try {
			return codeDao.deleteCODE(LCODE);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int deleteMCODE(String LCODE, String MCODE){
		try {
			return codeDao.deleteMCODE(LCODE, MCODE);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int deleteSCODE(String LCODE, String MCODE, String SCODE){
		try {
			return codeDao.deleteSCODE(LCODE, MCODE, SCODE);
		}
		catch (Exception e) {
			return -1;
		}
	}
	public List<LinkedHashMap<String, Object>> getAndroidSCodeList(String LCODE, String MCODE)  throws Exception{
		return codeDao.getAndroidSCodeList(LCODE, MCODE);
	}
}
