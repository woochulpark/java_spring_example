package com.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dao.CenterDao;
import com.google.zxing.WriterException;
import com.service.CenterService;
import com.service.CodeService;
import com.service.CommonService;
import com.service.FileService;
import com.service.InOutService;
import com.service.ModelService;
import com.service.NoticeService;
import com.service.UserService;
import com.vo.*;

@Controller
public class BaseController {
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CenterService centerService;
	
	@Autowired
	private CenterDao centerDao;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private ModelService modelService;
	
	@RequestMapping(value = "ComCode")
	public ModelAndView ComCode(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CodeVo> CodeList = codeService.getLcodeInfo();
		mav.setViewName("base/Code_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CodeList", CodeList);
		mav.addObject("CodeListSize", CodeList.size());
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "codelist_search")
	public ModelAndView codelist_search(HttpServletRequest request,
			@RequestParam(value = "CodeKind", defaultValue = "") String CodeKind,
			@RequestParam(value = "CNAME", defaultValue = "") String CNAME
	) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		mav.setViewName("base/Code_list");
		if(CodeKind.equals("L")) {
			List<CodeVo> CodeList = codeService.getLNAMESearch(CNAME);
			mav.addObject("user_id", userService.getUserID(request));
			mav.addObject("CodeList", CodeList);
			mav.addObject("CodeListSize", CodeList.size());
		}
		else if(CodeKind.equals("M")) {
			List<CodeVo> CodeList = codeService.getMNAMESearch(CNAME);
			mav.addObject("user_id", userService.getUserID(request));
			mav.addObject("CodeList", CodeList);
			mav.addObject("CodeListSize", CodeList.size());
		}
		else {
			List<CodeVo> CodeList = codeService.getSNAMESearch(CNAME);
			mav.addObject("user_id", userService.getUserID(request));
			mav.addObject("CodeList", CodeList);
			mav.addObject("CodeListSize", CodeList.size());
		}
		mav.addObject("CodeKind", CodeKind);
		mav.addObject("CNAME", CNAME);
		return mav;
	}
	
	@RequestMapping(value = "codelist_Lcode")
	public ModelAndView codelist_Lcode(HttpServletRequest request, @RequestParam(value = "LCODE", defaultValue = "") String LCODE) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CodeVo> CodeList = codeService.getLcodeDetail(LCODE);
		mav.setViewName("base/Code_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CodeList", CodeList);
		mav.addObject("CodeListSize", CodeList.size());
		mav.addObject("LCODE", LCODE);
		mav.addObject("MCODE", "");
		return mav;
	}
	@RequestMapping(value = "codelist_Mcode")
	public ModelAndView codelist_Mcode(HttpServletRequest request, 
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CodeVo> CodeList = codeService.getMcodeDetail(LCODE, MCODE);
		mav.setViewName("base/Code_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CodeList", CodeList);
		mav.addObject("CodeListSize", CodeList.size());
		mav.addObject("LCODE", LCODE);
		mav.addObject("MCODE", MCODE);
		return mav;
	}
	@RequestMapping(value = "LCode_Add")
	public ModelAndView LCode_Add(HttpServletRequest request, 
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		mav.setViewName("base/LCode_Add");
		String TYPE = "";
		
		if(LCODE.length() == 0)
		{
			mav.addObject("PAGE", "대분류 추가");
			TYPE = "I";
		}
		else {
			mav.addObject("PAGE", "대분류 수정");
			TYPE = "M";
			CodeVo CodeInfo = codeService.getLcodeInfo_Detail(LCODE);
			if(CodeInfo == null)
			{
				mav.setViewName("redirect:/ComCode.do");
				mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
				return mav;
			}
			mav.addObject("LCODE", CodeInfo.getMCC_L_CODE());
			mav.addObject("LNAME", CodeInfo.getMCC_L_NAME());
		}
		mav.addObject("TYPE", TYPE);
		mav.addObject("msg", msg);
		mav.addObject("user_id", userService.getUserID(request));
		return mav;
	}
	
	@RequestMapping(value = "LCode_Add_Save")
	public ModelAndView LCode_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "LNAME", defaultValue = "") String LNAME) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}

		CodeVo vo = new CodeVo();
		vo.setMCC_L_CODE(LCODE);
		vo.setMCC_M_CODE(" ");
		vo.setMCC_S_CODE(" ");
		vo.setMCC_L_NAME(LNAME);
		vo.setMCC_M_NAME("");
		vo.setMCC_S_NAME("");
		vo.setMCC_SORT_ORDER("0");
		vo.setMCC_CODE_REMARK("");
		vo.setCMN_MAK_PROG("LCode_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("LCode_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(TYPE.equals("I")){
			if(codeService.insertCODE(vo) <= 0) {
				mav.setViewName("redirect:/LCode_Add.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("msg","저장실패! 대분류 코드가 중복되지 않았는 지 확인해주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/ComCode.do");
				return mav;
			}
		}
		else if(TYPE.equals("M")){
			if(codeService.updateLCODE(vo) <= 0) {
				mav.setViewName("redirect:/LCode_Add.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/ComCode.do");
				return mav;
			}
		}
		else {
			mav.setViewName("redirect:/ComCode.do");
			mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
			return mav;
		}
	}
	
	@RequestMapping(value = "LCode_Add_Del")
	public ModelAndView LCode_Add_Save(HttpServletRequest request,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(codeService.deleteCODE(LCODE) <= 0) {
			mav.setViewName("redirect:/LCode_Add.do");
			mav.addObject("LCODE", LCODE);
			return mav;
		}
		else {
			mav.setViewName("redirect:/ComCode.do");
			return mav;
		}
	}
	
	@RequestMapping(value = "MCode_Add")
	public ModelAndView MCode_Add(HttpServletRequest request, 
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		CodeVo LCodeInfo = codeService.getLcodeInfo_Detail(LCODE);
		if(LCodeInfo == null)
		{
			mav.setViewName("redirect:/ComCode.do");
			mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
			return mav;
		}
		mav.addObject("LCODE", LCodeInfo.getMCC_L_CODE());
		mav.addObject("LNAME", LCodeInfo.getMCC_L_NAME());
		
		mav.setViewName("base/MCode_Add");
		String TYPE = "";
		if(MCODE.length() == 0)
		{
			mav.addObject("PAGE", "중분류 추가");
			TYPE = "I";
		}
		else {
			mav.addObject("PAGE", "중분류 수정");
			TYPE = "M";
			CodeVo MCodeInfo = codeService.getMcodeInfo_Detail(LCODE, MCODE);
			if(MCodeInfo == null)
			{
				mav.setViewName("redirect:/ComCode.do");
				mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
				return mav;
			}
			mav.addObject("MCODE", MCodeInfo.getMCC_M_CODE());
			mav.addObject("MNAME", MCodeInfo.getMCC_M_NAME());
		}
		mav.addObject("TYPE", TYPE);
		mav.addObject("msg", msg);
		mav.addObject("user_id", userService.getUserID(request));
		return mav;
	}
	
	@RequestMapping(value = "MCode_Add_Save")
	public ModelAndView MCode_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "LNAME", defaultValue = "") String LNAME,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}

		CodeVo vo = new CodeVo();
		vo.setMCC_L_CODE(LCODE);
		vo.setMCC_M_CODE(MCODE);
		vo.setMCC_S_CODE(" ");
		vo.setMCC_L_NAME(LNAME);
		vo.setMCC_M_NAME(MNAME);
		vo.setMCC_S_NAME("");
		vo.setMCC_SORT_ORDER("0");
		vo.setMCC_CODE_REMARK("");
		vo.setCMN_MAK_PROG("MCode_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("MCode_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(TYPE.equals("I")){
			if(codeService.insertCODE(vo) <= 0) {
				mav.setViewName("redirect:/MCode_Add.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("MCODE",MCODE);
				mav.addObject("msg","저장실패! 중분류 코드가 중복되지 않았는 지 확인해주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/codelist_Lcode.do");
				mav.addObject("LCODE",LCODE);
				return mav;
			}
		}
		else if(TYPE.equals("M")){
			if(codeService.updateMCODE(vo) <= 0) {
				mav.setViewName("redirect:/MCode_Add.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("MCODE",MCODE);
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/codelist_Lcode.do");
				mav.addObject("LCODE",LCODE);
				return mav;
			}
		}
		else {
			mav.setViewName("redirect:/ComCode.do");
			mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
			return mav;
		}
	}
	
	@RequestMapping(value = "MCode_Add_Del")
	public ModelAndView MCode_Add_Del(HttpServletRequest request,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(codeService.deleteMCODE(LCODE, MCODE) <= 0) {
			mav.setViewName("redirect:/MCode_Add.do");
			mav.addObject("LCODE", LCODE);
			mav.addObject("MCODE", MCODE);
			return mav;
		}
		else {
			mav.setViewName("redirect:/ComCode.do");
			return mav;
		}
	}
	
	@RequestMapping(value = "SCode_Add")
	public ModelAndView SCode_Add(HttpServletRequest request, 
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "SCODE", defaultValue = "") String SCODE,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		CodeVo MCodeInfo = codeService.getMcodeInfo_Detail(LCODE, MCODE);
		if(MCodeInfo == null)
		{
			mav.setViewName("redirect:/ComCode.do");
			mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
			return mav;
		}
		mav.addObject("LCODE", MCodeInfo.getMCC_L_CODE());
		mav.addObject("LNAME", MCodeInfo.getMCC_L_NAME());
		mav.addObject("MCODE", MCodeInfo.getMCC_M_CODE());
		mav.addObject("MNAME", MCodeInfo.getMCC_M_NAME());
		
		mav.setViewName("base/SCode_Add");
		
		String TYPE = "";
		if(SCODE.length() == 0)
		{
			mav.addObject("PAGE", "소분류 추가");
			TYPE = "I";
		}
		else {
			mav.addObject("PAGE", "소분류 수정");
			TYPE = "M";
			CodeVo SCodeInfo = codeService.getScodeInfo_Detail(LCODE, MCODE, SCODE);
			if(SCodeInfo == null)
			{
				mav.setViewName("redirect:/ComCode.do");
				mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
				return mav;
			}
			mav.addObject("SCODE", SCodeInfo.getMCC_S_CODE());
			mav.addObject("SNAME", SCodeInfo.getMCC_S_NAME());
			mav.addObject("ORDER", SCodeInfo.getMCC_SORT_ORDER());
			mav.addObject("REMARK", SCodeInfo.getMCC_CODE_REMARK());
		}
		mav.addObject("TYPE", TYPE);
		mav.addObject("msg", msg);
		mav.addObject("user_id", userService.getUserID(request));
		return mav;
	}
	
	@RequestMapping(value = "SCode_Add_Save")
	public ModelAndView SCode_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "LNAME", defaultValue = "") String LNAME,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "SCODE", defaultValue = "") String SCODE,
			@RequestParam(value = "SNAME", defaultValue = "") String SNAME,
			@RequestParam(value = "ORDER", defaultValue = "") String ORDER,
			@RequestParam(value = "REMARK", defaultValue = "") String REMARK) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}

		CodeVo vo = new CodeVo();
		vo.setMCC_L_CODE(LCODE);
		vo.setMCC_M_CODE(MCODE);
		vo.setMCC_S_CODE(SCODE);
		vo.setMCC_L_NAME(LNAME);
		vo.setMCC_M_NAME(MNAME);
		vo.setMCC_S_NAME(SNAME);
		vo.setMCC_SORT_ORDER(ORDER);
		vo.setMCC_CODE_REMARK(REMARK);
		vo.setCMN_MAK_PROG("SCode_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("SCode_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(TYPE.equals("I")){
			if(codeService.insertCODE(vo) <= 0) {
				mav.setViewName("redirect:/SCode_Add.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("MCODE",MCODE);
				mav.addObject("SCODE",SCODE);
				mav.addObject("msg","저장실패! 소분류 코드가 중복되지 않았는 지 확인해주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/codelist_Mcode.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("MCODE",MCODE);
				return mav;
			}
		}
		else if(TYPE.equals("M")){
			if(codeService.updateSCODE(vo) <= 0) {
				mav.setViewName("redirect:/SCode_Add.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("MCODE",MCODE);
				mav.addObject("SCODE",SCODE);
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/codelist_Mcode.do");
				mav.addObject("LCODE",LCODE);
				mav.addObject("MCODE",MCODE);
				return mav;
			}
		}
		else {
			mav.setViewName("redirect:/ComCode.do");
			mav.addObject("msg","공통코드 정보를 가져오지 못했습니다.");
			return mav;
		}
	}
	
	@RequestMapping(value = "SCode_Add_Del")
	public ModelAndView SCode_Add_Del(HttpServletRequest request,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "SCODE", defaultValue = "") String SCODE) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(codeService.deleteSCODE(LCODE, MCODE, SCODE) <= 0) {
			mav.setViewName("redirect:/SCode_Add.do");
			mav.addObject("LCODE", LCODE);
			mav.addObject("MCODE", MCODE);
			mav.addObject("SCODE", SCODE);
			return mav;
		}
		else {
			mav.setViewName("redirect:/ComCode.do");
			return mav;
		}
	}
	
	@RequestMapping(value = "Centerlist")
	public ModelAndView Centerlist(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> CenterList = centerService.getLOGCInfo();
		mav.setViewName("base/Center_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CenterList", CenterList);
		mav.addObject("CenterListSize", CenterList.size());
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "CenterList_search")
	public ModelAndView centerlist_search(HttpServletRequest request,
			@RequestParam(value = "CenterKind", defaultValue = "") String CenterKind,
			@RequestParam(value = "CNAME", defaultValue = "") String CNAME
	) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		mav.setViewName("base/Center_list");
		if(CenterKind.equals("L")) {
			List<CenterVo> CenterList = centerService.getLOGCNAMESearch(CNAME);
			mav.addObject("user_id", userService.getUserID(request));
			mav.addObject("CenterList", CenterList);
			mav.addObject("CenterListSize", CenterList.size());
		}
		else if(CenterKind.equals("M")) {
			List<CenterVo> CenterList = centerService.getBNAMESearch(CNAME);
			mav.addObject("user_id", userService.getUserID(request));
			mav.addObject("CenterList", CenterList);
			mav.addObject("CenterListSize", CenterList.size());
		}
		else {
			List<CenterVo> CenterList = centerService.getCNAMESearch(CNAME);
			mav.addObject("user_id", userService.getUserID(request));
			mav.addObject("CenterList", CenterList);
			mav.addObject("CenterListSize", CenterList.size());
		}
		mav.addObject("CenterKind", CenterKind);
		mav.addObject("CNAME", CNAME);
		return mav;
	}
	
	@RequestMapping(value = "CenterList_LOGC")
	public ModelAndView CenterList_LOGC(HttpServletRequest request, @RequestParam(value = "LOGC", defaultValue = "") String LOGC) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> CenterList = centerService.getLOGCDetail(LOGC);
		mav.setViewName("base/Center_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CenterList", CenterList);
		mav.addObject("CenterListSize", CenterList.size());
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", "");
		return mav;
	}
	@RequestMapping(value = "CenterList_BONBU")
	public ModelAndView CenterList_BONBU(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> CenterList = centerService.getBONBUDetail(LOGC, BONBU);
		mav.setViewName("base/Center_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CenterList", CenterList);
		mav.addObject("CenterListSize", CenterList.size());
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		return mav;
	}
	@RequestMapping(value = "LOGC_Add")
	public ModelAndView LOGC_Add(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		mav.setViewName("base/LOGC_Add");
		String TYPE = "";
		if(LOGC.length() == 0)
		{
			mav.addObject("PAGE", "물류센터 추가");
			TYPE = "I";
		}
		else {
			mav.addObject("PAGE", "물류센터 수정");
			TYPE = "M";
			CenterVo CenterInfo = centerService.getLOGCInfo_Detail(LOGC);
			if(CenterInfo == null)
			{
				mav.setViewName("redirect:/Centerlist.do");
				mav.addObject("msg","물류센터 정보를 가져오지 못했습니다.");
				return mav;
			}
			mav.addObject("LOGC", CenterInfo.getMCI_LOGC());
			mav.addObject("LNAME", CenterInfo.getMCI_LOGCNAME());
			mav.addObject("BRN", CenterInfo.getMCI_BRN());
			mav.addObject("CoName", CenterInfo.getMCI_CoName());
			mav.addObject("CEO", CenterInfo.getMCI_CEO());
			mav.addObject("Addr", CenterInfo.getMCI_Addr());
			mav.addObject("CoType", CenterInfo.getMCI_CoType());
			mav.addObject("Jongmog", CenterInfo.getMCI_Jongmog());
			mav.addObject("PIC", CenterInfo.getMCI_PIC());
			mav.addObject("PICHP", CenterInfo.getMCI_PICHP());
		}
		mav.addObject("TYPE", TYPE);
		mav.addObject("msg", msg);
		mav.addObject("user_id", userService.getUserID(request));
		return mav;
	}
	
	@RequestMapping(value = "LOGC_Add_Save")
	public ModelAndView LOGC_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "LNAME", defaultValue = "") String LNAME,
			@RequestParam(value = "BRN", defaultValue = "") String BRN,
			@RequestParam(value = "CoName", defaultValue = "") String CoName,
			@RequestParam(value = "CEO", defaultValue = "") String CEO,
			@RequestParam(value = "Addr", defaultValue = "") String Addr,
			@RequestParam(value = "CoType", defaultValue = "") String CoType,
			@RequestParam(value = "Jongmog", defaultValue = "") String Jongmog,
			@RequestParam(value = "PIC", defaultValue = "") String PIC,
			@RequestParam(value = "PICHP", defaultValue = "") String PICHP
			) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		CenterVo vo = new CenterVo();
		vo.setMCI_LOGC(LOGC);
		vo.setMCI_Bonbu(" ");
		vo.setMCI_Center(" ");
		vo.setMCI_LOGCNAME(LNAME);
		vo.setMCI_BonbuNAME("");
		vo.setMCI_CenterName("");
		vo.setMCI_CODE("");
		vo.setMCI_BRN(BRN);
		vo.setMCI_CoName(CoName);
		vo.setMCI_CEO(CEO);
		vo.setMCI_Addr(Addr);
		vo.setMCI_CoType(CoType);
		vo.setMCI_Jongmog(Jongmog);
		vo.setMCI_PIC(PIC);
		vo.setMCI_PICHP(PICHP);
		vo.setMCI_StartDay(null);
		vo.setMCI_EndDay(null);
		vo.setMCI_BizSts("Y");
		vo.setMCI_CMNT("");
		vo.setMCI_SORT_ORDER("0");
		vo.setCMN_MAK_PROG("LOGC_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("LOGC_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(TYPE.equals("I")){
			if(centerService.insertCenter(vo) <= 0) {
				mav.setViewName("redirect:/LOGC_Add.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("msg","저장실패! 물류센터 코드가 중복되지 않았는 지 확인해주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/Centerlist.do");
				return mav;
			}
		}
		else if(TYPE.equals("M")){
			int result = centerService.updateLOGC(vo);
			int result2 = centerService.updateLOGCNM(vo);
			if(result <= 0 || result2 <= 0) {
				mav.setViewName("redirect:/LOGC_Add.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/Centerlist.do");
				return mav;
			}
		}
		else {
			mav.setViewName("redirect:/Centerlist.do");
			mav.addObject("msg","본부 정보를 가져오지 못했습니다.");
			return mav;
		}
	}
	
	@RequestMapping(value = "LOGC_Add_Del")
	public ModelAndView LOGC_Add_Del(HttpServletRequest request,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(centerService.deleteLOGC(LOGC) <= 0) {
			mav.setViewName("redirect:/LCode_Add.do");
			mav.addObject("LOGC", LOGC);
			return mav;
		}
		else {
			mav.setViewName("redirect:/Centerlist.do");
			return mav;
		}
	}
	
	@RequestMapping(value = "BONBU_Add")
	public ModelAndView BONBU_Add(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		CenterVo LOGCInfo = centerService.getLOGCInfo_Detail(LOGC);
		if(LOGCInfo == null)
		{
			mav.setViewName("redirect:/Centerlist.do");
			mav.addObject("msg","물류센터 정보를 가져오지 못했습니다.");
			return mav;
		}
		mav.addObject("LOGC", LOGCInfo.getMCI_LOGC());
		mav.addObject("LNAME", LOGCInfo.getMCI_LOGCNAME());
		
		mav.setViewName("base/BONBU_Add");
		String TYPE = "";
		if(BONBU.length() == 0)
		{
			mav.addObject("PAGE", "본부 추가");
			TYPE = "I";
		}
		else {
			mav.addObject("PAGE", "본부 수정");
			TYPE = "M";
			CenterVo BONBUInfo = centerService.getBONBUInfo_Detail(LOGC, BONBU);
			if(BONBUInfo == null)
			{
				mav.setViewName("redirect:/Centerlist.do");
				mav.addObject("msg","본부 정보를 가져오지 못했습니다.");
				return mav;
			}
			mav.addObject("BONBU", BONBUInfo.getMCI_Bonbu());
			mav.addObject("BNAME", BONBUInfo.getMCI_BonbuNAME());
		}
		mav.addObject("TYPE", TYPE);
		mav.addObject("msg", msg);
		mav.addObject("user_id", userService.getUserID(request));
		return mav;
	}
	
	@RequestMapping(value = "BONBU_Add_Save")
	public ModelAndView BONBU_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "LNAME", defaultValue = "") String LNAME,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "BNAME", defaultValue = "") String BNAME) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}

		CenterVo vo = new CenterVo();
		vo.setMCI_LOGC(LOGC);
		vo.setMCI_Bonbu(BONBU);
		vo.setMCI_Center(" ");
		vo.setMCI_LOGCNAME(LNAME);
		vo.setMCI_BonbuNAME(BNAME);
		vo.setMCI_CenterName("");
		vo.setMCI_CODE("");
		vo.setMCI_BRN("");
		vo.setMCI_CoName("");
		vo.setMCI_CEO("");
		vo.setMCI_Addr("");
		vo.setMCI_CoType("");
		vo.setMCI_Jongmog("");
		vo.setMCI_PIC("");
		vo.setMCI_PICHP("");
		vo.setMCI_StartDay(null);
		vo.setMCI_EndDay(null);
		vo.setMCI_BizSts("Y");
		vo.setMCI_CMNT("");
		vo.setMCI_SORT_ORDER("0");
		vo.setCMN_MAK_PROG("BONBU_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("BONBU_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(TYPE.equals("I")){
			if(centerService.insertCenter(vo) <= 0) {
				mav.setViewName("redirect:/BONBU_Add.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("BONBU",BONBU);
				mav.addObject("msg","저장실패! 본부 코드가 중복되지 않았는 지 확인해주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/CenterList_LOGC.do");
				mav.addObject("LOGC",LOGC);
				return mav;
			}
		}
		else if(TYPE.equals("M")){
			if(centerService.updateBONBU(vo) <= 0) {
				mav.setViewName("redirect:/BONBU_Add.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("BONBU",BONBU);
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/CenterList_LOGC.do");
				mav.addObject("LOGC",LOGC);
				return mav;
			}
		}
		else {
			mav.setViewName("redirect:/Centerlist.do");
			mav.addObject("msg","본부 정보를 가져오지 못했습니다.");
			return mav;
		}
	}
	
	@RequestMapping(value = "BONBU_Add_Del")
	public ModelAndView BONBU_Add_Del(HttpServletRequest request,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}

		if(centerService.deleteBONBU(LOGC, BONBU) <= 0) {
			mav.setViewName("redirect:/BONBU_Add.do");
			mav.addObject("LOGC", LOGC);
			mav.addObject("BONBU", BONBU);
			return mav;
		}
		else {
			mav.setViewName("redirect:/Centerlist.do");
			return mav;
		}
	}
	
	@RequestMapping(value = "CENTER_Add")
	public ModelAndView CENTER_Add(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		CenterVo BONBUInfo = centerService.getBONBUInfo_Detail(LOGC, BONBU);
		if(BONBUInfo == null)
		{
			mav.setViewName("redirect:/Centerlist.do");
			mav.addObject("msg","본부 정보를 가져오지 못했습니다.");
			return mav;
		}
		mav.addObject("LOGC", BONBUInfo.getMCI_LOGC());
		mav.addObject("LNAME", BONBUInfo.getMCI_LOGCNAME());
		mav.addObject("BONBU", BONBUInfo.getMCI_Bonbu());
		mav.addObject("BNAME", BONBUInfo.getMCI_BonbuNAME());
		
		mav.setViewName("base/CENTER_Add");
		
		String TYPE = "";
		if(CENTER.length() == 0)
		{
			mav.addObject("PAGE", "센터 추가");
			TYPE = "I";
		}
		else {
			mav.addObject("PAGE", "센터 수정");
			TYPE = "M";
			CenterVo CENTERInfo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
			if(CENTERInfo == null)
			{
				mav.setViewName("redirect:/Centerlist.do");
				mav.addObject("msg","센터 정보를 가져오지 못했습니다.");
				return mav;
			}
			mav.addObject("CENTER", CENTERInfo.getMCI_Center());
			mav.addObject("CNAME", CENTERInfo.getMCI_CenterName());
			mav.addObject("BRN", CENTERInfo.getMCI_BRN());
			mav.addObject("CoName", CENTERInfo.getMCI_CoName());
			mav.addObject("CEO", CENTERInfo.getMCI_CEO());
			mav.addObject("Addr", CENTERInfo.getMCI_Addr());
			mav.addObject("CoType", CENTERInfo.getMCI_CoType());
			mav.addObject("Jongmog", CENTERInfo.getMCI_Jongmog());
			mav.addObject("PIC", CENTERInfo.getMCI_PIC());
			mav.addObject("PICHP", CENTERInfo.getMCI_PICHP());
			mav.addObject("StartDay", CENTERInfo.getMCI_StartDay());
			mav.addObject("EndDay", CENTERInfo.getMCI_EndDay());
			mav.addObject("BizSts", CENTERInfo.getMCI_BizSts());
			mav.addObject("ORDER", CENTERInfo.getMCI_SORT_ORDER());
			mav.addObject("REMARK", CENTERInfo.getMCI_CMNT());
		}
		mav.addObject("TYPE", TYPE);
		mav.addObject("msg", msg);
		mav.addObject("user_id", userService.getUserID(request));
		return mav;
	}
	
	@RequestMapping(value = "CENTER_Add_Save")
	public ModelAndView CENTER_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "LNAME", defaultValue = "") String LNAME,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "BNAME", defaultValue = "") String BNAME,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "CNAME", defaultValue = "") String CNAME,
			@RequestParam(value = "BRN", defaultValue = "") String BRN,
			@RequestParam(value = "CoName", defaultValue = "") String CoName,
			@RequestParam(value = "CEO", defaultValue = "") String CEO,
			@RequestParam(value = "Addr", defaultValue = "") String Addr,
			@RequestParam(value = "CoType", defaultValue = "") String CoType,
			@RequestParam(value = "Jongmog", defaultValue = "") String Jongmog,
			@RequestParam(value = "PIC", defaultValue = "") String PIC,
			@RequestParam(value = "PICHP", defaultValue = "") String PICHP,
			@RequestParam(value = "StartDay", defaultValue = "1900-01-01") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "2100-12-31") String EndDay,
			@RequestParam(value = "BizSts", defaultValue = "Y") String BizSts,
			@RequestParam(value = "ORDER", defaultValue = "") String ORDER,
			@RequestParam(value = "REMARK", defaultValue = "") String REMARK) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}

		CenterVo vo = new CenterVo();
		vo.setMCI_LOGC(LOGC);
		vo.setMCI_Bonbu(BONBU);
		vo.setMCI_Center(CENTER);
		vo.setMCI_LOGCNAME(LNAME);
		vo.setMCI_BonbuNAME(BNAME);
		vo.setMCI_CenterName(CNAME);
		vo.setMCI_CODE(LOGC + BONBU + CENTER);
		vo.setMCI_BRN(BRN);
		vo.setMCI_CoName(CoName);
		vo.setMCI_CEO(CEO);
		vo.setMCI_Addr(Addr);
		vo.setMCI_CoType(CoType);
		vo.setMCI_Jongmog(Jongmog);
		vo.setMCI_PIC(PIC);
		vo.setMCI_PICHP(PICHP);
		vo.setMCI_StartDay(StartDay);
		vo.setMCI_EndDay(EndDay);
		vo.setMCI_BizSts(BizSts);
		vo.setMCI_CMNT(REMARK);
		vo.setMCI_SORT_ORDER(ORDER);
		vo.setCMN_MAK_PROG("CENTER_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("CENTER_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(TYPE.equals("I")){
			if(centerService.insertCenter(vo) <= 0) {
				mav.setViewName("redirect:/CENTER_Add.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("BONBU",BONBU);
				mav.addObject("CENTER",CENTER);
				mav.addObject("msg","저장실패! 센터 코드가 중복되지 않았는 지 확인해주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/CenterList_BONBU.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("BONBU",BONBU);
				return mav;
			}
		}
		else if(TYPE.equals("M")){
			if(BizSts.equals("N"))
			{
				if(centerDao.getCenterUser(LOGC + BONBU + CENTER) > 0 || centerDao.getCenterJego(LOGC + BONBU + CENTER) > 0){
					mav.setViewName("redirect:/CENTER_Add.do");
					mav.addObject("LOGC",LOGC);
					mav.addObject("BONBU",BONBU);
					mav.addObject("CENTER",CENTER);
					mav.addObject("msg","운영 종료 불가! 센터에 재고가 아직 남아있거나 소속 되어있는 사용자가 있습니다.");
					return mav;
				}
			}
			if(centerService.updateCENTER(vo) <= 0) {
				mav.setViewName("redirect:/CENTER_Add.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("BONBU",BONBU);
				mav.addObject("CENTER",CENTER);
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
				return mav;
			}
			else {
				mav.setViewName("redirect:/CenterList_BONBU.do");
				mav.addObject("LOGC",LOGC);
				mav.addObject("BONBU",BONBU);
				return mav;
			}
		}
		else {
			mav.setViewName("redirect:/Centerlist.do");
			mav.addObject("msg","본부 정보를 가져오지 못했습니다.");
			return mav;
		}
	}
	
	@RequestMapping(value = "CENTER_Add_Del")
	public ModelAndView CENTER_Add_Del(HttpServletRequest request,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		if(centerService.deleteCENTER(LOGC, BONBU, CENTER) <= 0) {
			mav.setViewName("redirect:/CENTER_Add.do");
			mav.addObject("LOGC", LOGC);
			mav.addObject("BONBU", BONBU);
			mav.addObject("CENTER", CENTER);
			return mav;
		}
		else {
			mav.setViewName("redirect:/Centerlist.do");
			return mav;
		}
	}
	
	@RequestMapping(value = "/Ajax_BONBU.do")
	public ModelAndView Ajax_BONBU(@RequestParam(value = "LOGC") String LOGC,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		mav.setViewName("ajax/Ajax_view");
		mav.addObject("BONBULIST",BONBULIST);
		
		mav.addObject("type","B");
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_CENTER.do")
	public ModelAndView Ajax_CENTER(@RequestParam(value = "LOGC") String LOGC,
									@RequestParam(value = "BONBU") String BONBU,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		mav.setViewName("ajax/Ajax_view");
		mav.addObject("CENTERLIST",CENTERLIST);
		
		mav.addObject("type","C");
		return mav;
	}
	
	@RequestMapping(value = "User_list")
	public ModelAndView User_list(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001") && !UserInfo.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		List<UserVo> UserList = userService.getUserList("", "", "", "", 1, 10);
		int listCnt = UserList.size() == 0 ? 0 : UserList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, 1);
		
		mav.setViewName("base/User_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("UserList", UserList);
		mav.addObject("UserListSize", UserList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "Userlist_search")
	public ModelAndView Userlist_search(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "UNAME", defaultValue = "") String UNAME,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!UserInfo.getMUT_POSITION().equals("001") && !UserInfo.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		List<UserVo> UserList = userService.getUserList(LOGC, BONBU, CENTER, UNAME, PAGE, 10);
		int listCnt = UserList.size() == 0 ? 0 : UserList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("base/User_list");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("UserList", UserList);
		mav.addObject("UserListSize", UserList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("UNAME", UNAME);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		return mav;
	}
	
	@RequestMapping(value = "User_Add")
	public ModelAndView User_Add(HttpServletRequest request, 
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo LogUser = userService.getUserInfo(userService.getUserID(request));
		if(LogUser == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!LogUser.getMUT_POSITION().equals("001") && !LogUser.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CodeVo> POSITION_LIST = codeService.getSCodeList("100", "POZSN");
		
		mav.setViewName("base/User_Add");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("POSITION_LIST", POSITION_LIST);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "User_Add_Save")
	public ModelAndView User_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "USERID", defaultValue = "") String USERID,
			@RequestParam(value = "UNAME", defaultValue = "") String UNAME,
			@RequestParam(value = "PASS", defaultValue = "") String PASS,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "POSITION", defaultValue = "") String POSITION,
			@RequestParam(value = "HP", defaultValue = "") String HP,
			@RequestParam(value = "StartDay", defaultValue = "1900-01-01") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "2100-12-31") String EndDay,
			@RequestParam(value = "BizSts", defaultValue = "Y") String BizSts,
			@RequestParam(value = "SIGN_ORG_FILE", defaultValue = "") String SIGN_ORG_FILE,
			@RequestParam(value = "SIGN_FILE", defaultValue = "") String SIGN_FILE,
			@RequestParam(value = "file") MultipartFile file) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo LogUser = userService.getUserInfo(userService.getUserID(request));
		if(LogUser == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!LogUser.getMUT_POSITION().equals("001") && !LogUser.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}

		if(USERID.length() == 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","아이디를 입력해주세요.");
			return mav;
		}
		if(UNAME.length() == 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","이름을 입력해주세요.");
			return mav;
		}
		if(PASS.length() == 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","패스워드를 입력해주세요.");
			return mav;
		}
		if(LOGC.length() == 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","물류센터를 선택해주세요.");
			return mav;
		}
		if(BONBU.length() == 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","본부를 선택해주세요.");
			return mav;
		}
		if(CENTER.length() == 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","센터를 선택해주세요.");
			return mav;
		}
		if(POSITION.length() == 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","직급을 선택해주세요.");
			return mav;
		}

		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo == null)
		{
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","본부정보를 가져오지 못했습니다.");
			return mav;
		}
		if(centerVo.getMCI_CODE() == null || centerVo.getMCI_CODE().length() == 0) 
		{
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","본부정보를 가져오지 못했습니다.");
			return mav;
		}

		UserVo vo = new UserVo();
		vo.setMUT_USERID(USERID);
		vo.setMUT_USERNAME(UNAME);
		String password = PASS.length() > 0 ? userService.encrypt(PASS) : "";
		vo.setMUT_USERPWD(password);
		vo.setMUT_CENTER(centerVo.getMCI_CODE());
		vo.setMUT_POSITION(POSITION);
		vo.setMUT_HP_TEL(HP);
		vo.setMUT_STARTDAY(StartDay);
		vo.setMUT_ENDDAY(EndDay);
		vo.setMUT_BIZSTS(BizSts);
		String orgFile = "";
		String sysFile = "";
		if (file.getOriginalFilename() == null || file.getOriginalFilename().equals("")){
			vo.setMUT_SIGN(SIGN_FILE);
			vo.setMUT_SIGN_ORG(SIGN_ORG_FILE);
		}else{
			orgFile = file.getOriginalFilename();
			sysFile = USERID + "_" + orgFile;
			vo.setMUT_SIGN_ORG(orgFile);
			vo.setMUT_SIGN(sysFile);
		}
		vo.setCMN_MAK_PROG("User_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("User_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(userService.insertUser(vo) <= 0) {
			mav.setViewName("redirect:/User_Add.do");
			mav.addObject("msg","저장실패! 사번이 중복되지 않았는 지 확인해주세요.");
		}
		else {
			if(!orgFile.equals("")) {
				int result = fileService.fileUpload(file, SIGN_FILE, "SIGN", sysFile, request);
				if(result == -1)
				{
					mav.setViewName("redirect:/User_Add.do");
					mav.addObject("msg","파일 업로드 실패! 다시 시도해주세요.");
					return mav;
				}
				else if(result == -2)
				{
					mav.setViewName("redirect:/User_Add.do");
					mav.addObject("msg","10메가바이트 이상 업로드 할 수 없습니다.");
					return mav;
				}
			}
			mav.setViewName("redirect:/User_list.do");
			mav.addObject("USERID",USERID);
		}
		return mav;
	}
	
	@RequestMapping(value = "User_Modify")
	public ModelAndView User_Modify(HttpServletRequest request, 
			@RequestParam(value = "USERID", defaultValue = "") String USERID,
			@RequestParam(value = "msg", defaultValue = "") String msg) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo LogUser = userService.getUserInfo(userService.getUserID(request));
		if(LogUser == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!LogUser.getMUT_POSITION().equals("001") && !LogUser.getMUT_POSITION().equals("002") && !LogUser.getMUT_USERID().equals(USERID)) {
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(USERID);
		if(UserInfo == null)
		{
			mav.setViewName("redirect:/User_list.do");
			mav.addObject("msg","사용자 정보를 가져오지 못했습니다.");
			return mav;
		}
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(UserInfo.getMCI_LOGC());
		List<CenterVo> CENTERLIST = centerService.getCENTERList(UserInfo.getMCI_LOGC(), UserInfo.getMCI_Bonbu());
		List<CodeVo> POSITION_LIST = codeService.getSCodeList("100", "POZSN");

		mav.setViewName("base/User_Modify");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("POSITION_LIST", POSITION_LIST);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("level", LogUser.getMUT_POSITION());
		mav.addObject("msg",msg);
		return mav;
	}

	@RequestMapping(value = "User_Modify_Save")
	public ModelAndView User_Modify_Save(HttpServletRequest request,
			@RequestParam(value = "USERID", defaultValue = "") String USERID,
			@RequestParam(value = "UNAME", defaultValue = "") String UNAME,
			@RequestParam(value = "PASS", defaultValue = "") String PASS,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "POSITION", defaultValue = "") String POSITION,
			@RequestParam(value = "HP", defaultValue = "") String HP,
			@RequestParam(value = "StartDay", defaultValue = "1900-01-01") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "2100-12-31") String EndDay,
			@RequestParam(value = "BizSts", defaultValue = "Y") String BizSts,
			@RequestParam(value = "SIGN_ORG_FILE", defaultValue = "") String SIGN_ORG_FILE,
			@RequestParam(value = "SIGN_FILE", defaultValue = "") String SIGN_FILE,
			@RequestParam(value = "file", defaultValue = "") MultipartFile file) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo LogUser = userService.getUserInfo(userService.getUserID(request));
		if(LogUser == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!LogUser.getMUT_POSITION().equals("001") && !LogUser.getMUT_POSITION().equals("002") && !LogUser.getMUT_USERID().equals(USERID)) {
			mav.setViewName("index");
			return mav;
		}

		if(USERID.length() == 0) {
			if(LogUser.getMUT_POSITION().equals("001") || LogUser.getMUT_POSITION().equals("002"))
				mav.setViewName("redirect:/User_list.do");
			else
				mav.setViewName("redirect:/main.do");
			mav.addObject("msg","아이디를 가져오지 못했습니다.");
			return mav;
		}

		UserVo vo = new UserVo();
		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo != null && centerVo.getMCI_CODE() != null && centerVo.getMCI_CODE().length() > 0)
		{
			vo.setMUT_CENTER(centerVo.getMCI_CODE());
		}

		vo.setMUT_USERID(USERID);
		vo.setMUT_USERNAME(UNAME);
		String password = PASS.length() > 0 ? userService.encrypt(PASS) : "";
		vo.setMUT_USERPWD(password);
		vo.setMUT_POSITION(POSITION);
		vo.setMUT_HP_TEL(HP);
		if(LogUser.getMUT_POSITION().equals("001") || LogUser.getMUT_POSITION().equals("002")) {
			vo.setMUT_STARTDAY(StartDay);
			vo.setMUT_ENDDAY(EndDay);
			vo.setMUT_BIZSTS(BizSts);
		}
		String orgFile = "";
		String sysFile = "";
		if (file.getOriginalFilename() != null && file.getOriginalFilename().length() > 0){
			orgFile = file.getOriginalFilename();
			sysFile = USERID + "_" + orgFile;
			vo.setMUT_SIGN_ORG(orgFile);
			vo.setMUT_SIGN(sysFile);
		}
		vo.setCMN_MAK_PROG("User_Modify");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("User_Modify");
		vo.setCMN_UPD_ID(userService.getUserID(request));

		if(userService.updateUser(vo) <= 0) {
			if(LogUser.getMUT_POSITION().equals("001") || LogUser.getMUT_POSITION().equals("002")) {
				mav.setViewName("redirect:/User_Modify.do");
				mav.addObject("USERID",USERID);
			}
			else
				mav.setViewName("redirect:/main.do");
			mav.addObject("msg","저장실패! 다시 시도해 주세요.");
		}
		else {
			if(!orgFile.equals("")) {
				int result = fileService.fileUpload(file, SIGN_FILE, "SIGN", sysFile, request);
				if(result == -1)
				{
					if(LogUser.getMUT_POSITION().equals("001") || LogUser.getMUT_POSITION().equals("002")) {
						mav.setViewName("redirect:/User_Modify.do");
						mav.addObject("USERID",USERID);
					}
					else
						mav.setViewName("redirect:/main.do");
					mav.addObject("msg","파일 업로드 실패! 다시 시도해주세요.");
					return mav;
				}
				else if(result == -2)
				{
					if(LogUser.getMUT_POSITION().equals("001") || LogUser.getMUT_POSITION().equals("002")) {
						mav.setViewName("redirect:/User_Modify.do");
						mav.addObject("USERID",USERID);
					}
					else
						mav.setViewName("redirect:/main.do");
					mav.addObject("msg","10메가바이트 이상 업로드 할 수 없습니다.");
					return mav;
				}
			}
			if(LogUser.getMUT_POSITION().equals("001") || LogUser.getMUT_POSITION().equals("002"))
				mav.setViewName("redirect:/User_list.do");
			else
				mav.setViewName("redirect:/main.do");
		}
		return mav;
	}

	@RequestMapping(value = "User_Add_Del")
	public ModelAndView User_Add_Del(HttpServletRequest request,
			@RequestParam(value = "USERID", defaultValue = "") String USERID,
			@RequestParam(value = "SIGN_FILE", defaultValue = "") String SIGN_FILE) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}

		UserVo LogUser = userService.getUserInfo(userService.getUserID(request));
		if(LogUser == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!LogUser.getMUT_POSITION().equals("001") && !LogUser.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(USERID.length() == 0) {
			mav.setViewName("redirect:/User_list.do");
			mav.addObject("msg","삭제실패! 사용자 정보를 가져올 수 없습니다.");
			return mav;
		}
		if(userService.deleteUser(USERID) <= 0) {
			mav.setViewName("redirect:/User_Modify.do");
			mav.addObject("USERID", USERID);
			mav.addObject("msg","삭제실패! 다시 시도해 주세요.");
			return mav;
		}
		else {
			fileService.fileDelete(SIGN_FILE, "SIGN", request);
			mav.setViewName("redirect:/User_list.do");
			return mav;
		}
	}
	
	@RequestMapping("/Ajax_IDCHECK")
	public @ResponseBody String Ajax_IDCHECK(
			@RequestParam(value = "USERID", defaultValue = "")	String USERID) {

		int IDCHECK = userService.getIDCHECK(USERID);
	    return String.valueOf(IDCHECK);
	}
	
	@RequestMapping(value = "NOTICE_LIST")
	public ModelAndView NOTICE_LIST(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		List<NoticeVo> NoticeList = noticeService.getNoticeList("", 1, 10, userService.getUserID(request));
		int listCnt = NoticeList.size() == 0 ? 0 : NoticeList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, 1);
		
		mav.setViewName("base/NOTICE_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("NoticeList", NoticeList);
		mav.addObject("NoticeListSize", NoticeList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("level", UserInfo.getMUT_POSITION());
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "Notice_search")
	public ModelAndView Notice_search(HttpServletRequest request, 
			@RequestParam(value = "TITLE", defaultValue = "") String TITLE,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		List<NoticeVo> NoticeList = noticeService.getNoticeList(TITLE, PAGE, 10, userService.getUserID(request));
		int listCnt = NoticeList.size() == 0 ? 0 : NoticeList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("base/NOTICE_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("NoticeList", NoticeList);
		mav.addObject("NoticeListSize", NoticeList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("level", UserInfo.getMUT_POSITION());
		mav.addObject("TITLE", TITLE);
		return mav;
	}
	
	@RequestMapping(value = "Notice_Add")
	public ModelAndView Notice_Add(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "msg", defaultValue = "") String msg,
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		UserVo userInfo = userService.getUserInfo(userService.getUserID(request));
		if(userInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		String PAGE = "";
		NoticeVo NoticeInfo = null;
		if(DOCNO.length() > 0)
			NoticeInfo = noticeService.getNoticeInfo(DOCNO);
		if(NoticeInfo == null) {
			if(!userInfo.getMUT_POSITION().equals("001") && !userInfo.getMUT_POSITION().equals("002")) {
				mav.setViewName("index");
				return mav;
			}
			TYPE = "I";
			PAGE = "공지사항 추가";
			mav.setViewName("base/NOTICE_Add");
			mav.addObject("UNAME", userInfo.getMUT_USERNAME());
		}
		else {
			noticeService.updateNoticeUser(DOCNO, userService.getUserID(request));
			mav.addObject("NoticeInfo", NoticeInfo);
			if(TYPE.equals("M")) {
				if(!userInfo.getMUT_POSITION().equals("001") && !userInfo.getMUT_POSITION().equals("002")) {
					mav.setViewName("index");
					return mav;
				}
				mav.setViewName("base/NOTICE_Add");
				TYPE = "M";
				PAGE = "공지사항 수정";
			}
			else {
				mav.setViewName("base/NOTICE_DETAIL");
				PAGE = "공지사항 상세";
			}
			List<UserVo> SusinList = noticeService.getSusinList(DOCNO);
			mav.addObject("SusinList",SusinList);
			mav.addObject("SusinListSize",SusinList.size());
			List<NoticeVo> FileList = noticeService.getNoticeFileList(DOCNO);
			if(FileList != null) {
				mav.addObject("FileList",FileList);
			}
			else {
				msg += msg.length() > 0 ? " , 첨부파일 목록을 가져오지 못했습니다." : ""; 
			}
			if(NoticeInfo.getMNI_CTYPE().equals("1")) {
				List<CenterVo> BONBULIST = centerService.getBONBUList(NoticeInfo.getMCI_LOGC());
				mav.addObject("BONBULIST", BONBULIST);
			}
			else if(NoticeInfo.getMNI_CTYPE().equals("2")) {
				List<CenterVo> BONBULIST = centerService.getBONBUList(NoticeInfo.getMCI_LOGC());
				mav.addObject("BONBULIST", BONBULIST);
				List<CenterVo> CENTERLIST = centerService.getCENTERList(NoticeInfo.getMCI_LOGC(), NoticeInfo.getMCI_Bonbu());
				mav.addObject("CENTERLIST", CENTERLIST);
			}
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		
		
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("TYPE", TYPE);
		mav.addObject("PAGE", PAGE);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("DOCNO", DOCNO);
		mav.addObject("msg",msg);
		mav.addObject("userInfo",userInfo);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_POP_BONBU.do")
	public ModelAndView Ajax_POP_BONBU(@RequestParam(value = "LOGC") String LOGC,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		mav.setViewName("ajax/Ajax_pop_view");
		mav.addObject("BONBULIST",BONBULIST);
		
		mav.addObject("type","B");
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_POP_CENTER.do")
	public ModelAndView Ajax_POP_CENTER(@RequestParam(value = "LOGC") String LOGC,
									@RequestParam(value = "BONBU") String BONBU,
									HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		mav.setViewName("ajax/Ajax_pop_view");
		mav.addObject("CENTERLIST",CENTERLIST);
		
		mav.addObject("type","C");
		return mav;
	}
	
	@RequestMapping(value = "Ajax_POP_User")
	public ModelAndView Ajax_POP_User(HttpServletRequest request, 
			@RequestParam(value = "POP_LOGC", defaultValue = "") String POP_LOGC,
			@RequestParam(value = "POP_BONBU", defaultValue = "") String POP_BONBU,
			@RequestParam(value = "POP_CENTER", defaultValue = "") String POP_CENTER,
			@RequestParam(value = "POP_UNAME", defaultValue = "") String POP_UNAME,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		List<UserVo> UserList = userService.getPOPUserList(POP_LOGC, POP_BONBU, POP_CENTER, POP_UNAME, PAGE, 10);
		int listCnt = UserList.size() == 0 ? 0 : UserList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("ajax/Ajax_susin");
		mav.addObject("UserList", UserList);
		mav.addObject("UserListSize", UserList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		return mav;
	}
	
	@RequestMapping(value = "Notice_Add_Save")
	public ModelAndView Notice_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "TITLE", defaultValue = "") String TITLE,
			@RequestParam(value = "POPUP", defaultValue = "") String POPUP,
			@RequestParam(value = "TOPNOTICE", defaultValue = "") String TOPNOTICE,
			@RequestParam(value = "RECEIVETYPE", defaultValue = "") String RECEIVETYPE,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "SUSINID", defaultValue = "") String SUSINID,
			@RequestParam(value="SUSINID", required = false) List<String> SUSINlist,
			@RequestParam(value = "CMNT", defaultValue = "") String CMNT,
			@RequestParam(value="Addfile", required = false) List<MultipartFile> fileList,
			@RequestParam(value = "TEMPSTS", defaultValue = "") String TEMPSTS
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		UserVo userInfo = userService.getUserInfo(userService.getUserID(request));
		if(userInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		if(!userInfo.getMUT_POSITION().equals("001") && !userInfo.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		NoticeVo vo = new NoticeVo();
		vo.setMNI_TITLE(TITLE);
		vo.setMNI_UserID(userService.getUserID(request));
		vo.setMNI_POPUP(POPUP);
		vo.setMNI_TopNotice(TOPNOTICE);
		vo.setMNI_ReceiveType(RECEIVETYPE);
		if(RECEIVETYPE.equals("1")) {
			if(LOGC.length() > 0 && BONBU.length() == 0 && CENTER.length() == 0)
				vo.setMNI_CTYPE("0");
			else if(LOGC.length() > 0 && BONBU.length() > 0 && CENTER.length() == 0)
				vo.setMNI_CTYPE("1");
			else if(LOGC.length() > 0 && BONBU.length() > 0 && CENTER.length() > 0)
				vo.setMNI_CTYPE("2");
			else {
				mav.setViewName("redirect:/Notice_Add.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","본부를 지정해주세요!");
				return mav;
			}
			vo.setMCI_LOGC(LOGC);
			vo.setMCI_Bonbu(BONBU);
			vo.setMCI_Center(CENTER);
		}
		else {
			vo.setMNI_CTYPE("");
			vo.setMCI_LOGC("");
			vo.setMCI_Bonbu("");
			vo.setMCI_Center("");
		}
		vo.setMNI_TempSts(TEMPSTS);
		vo.setMNI_Cmnt(CMNT);
		vo.setCMN_MAK_PROG("Notice_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("Notice_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		int result = 0;
		NoticeVo NoticeInfo = null;
		if(DOCNO.length() > 0)
			NoticeInfo = noticeService.getNoticeInfo(DOCNO);
		if(NoticeInfo == null) {
			result = noticeService.insertNotice(vo);
			DOCNO = vo.getMNI_DOCNO();
		}
		else {
			vo.setMNI_DOCNO(DOCNO);
			result = noticeService.updateNotice(vo);
		}
		
		if(result >= 0) {
			boolean filechk = true;
			for (MultipartFile file : fileList) {
				String orgFile = "";
				if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")){
					orgFile = file.getOriginalFilename();
					NoticeVo fileVo = new NoticeVo();
					fileVo.setMDF_DOCNO(DOCNO);
					fileVo.setMDF_FILE_Org(orgFile);
					fileVo.setMDF_FILE_SIZE(String.format("%.2f", ((double)file.getSize() / 1024 / 1024)));
					int fileresult = noticeService.insertNoticeFile(fileVo, file, request);
					if(fileresult < 0) {
						/*
						System.out.println("vo.getMDF_SEQ() = " + fileVo.getMDF_SEQ());
						String SIGN_FILE = DOCNO + fileVo.getMDF_SEQ() + orgFile;
						fileresult = fileService.fileUpload(file, "", "", SIGN_FILE, request);
						if(fileresult == -1)
						{
							mav.setViewName("redirect:/Notice_Add.do");
							mav.addObject("DOCNO",DOCNO);
							mav.addObject("msg","파일 업로드 실패! 다시 시도해주세요.");
							return mav;
						}
						else if(fileresult == -2)
						{
							mav.setViewName("redirect:/Notice_Add.do");
							mav.addObject("DOCNO",DOCNO);
							mav.addObject("msg","10메가바이트 이상 업로드 할 수 없습니다.");
							return mav;
						}*/
						filechk = false;
					}
				}
			}
			
			int susinresult = noticeService.deleteSusinList(DOCNO);
			if(susinresult >= 0) {
				if(RECEIVETYPE.equals("2") && SUSINlist != null && SUSINlist.size() > 0) {
					//중복제거
					ArrayList<String> SUSINlist_TEMP = new ArrayList<>();
					for(String susindata : SUSINlist){
					    if(!SUSINlist_TEMP.contains(susindata))
					    	SUSINlist_TEMP.add(susindata);
					}
					List<NoticeVo> susinList = new ArrayList<NoticeVo>();
					for (String SUSINid : SUSINlist) {
						NoticeVo vo2 = new NoticeVo();
						vo2.setMNS_DOCNO(DOCNO);
						vo2.setMNS_UserID(SUSINid);
						susinList.add(vo2);
					}
					susinresult = noticeService.insertSusinList(susinList);
					if(susinresult < 0) {
						mav.setViewName("redirect:/Notice_Add.do");
						mav.addObject("DOCNO",DOCNO);
						mav.addObject("msg","수신자등록에 실패하였습니다.");
						return mav;
					}
				}
			}
			else {
				mav.setViewName("redirect:/Notice_Add.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","수신자 업데이트에 실패하였습니다.");
				return mav;
			}
			
			mav.setViewName("redirect:/Notice_Add.do");
			mav.addObject("DOCNO",DOCNO);
			if(!filechk)
				mav.addObject("msg","일부파일 업로드에 실패하였습니다.");
			return mav;
		}
		else {
			mav.setViewName("redirect:/Notice_Add.do");
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
			return mav;
		}
	}
	
	@RequestMapping(value = "Notice_Add_Del")
	public ModelAndView Notice_Add_Del(HttpServletRequest request,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		UserVo userInfo = userService.getUserInfo(userService.getUserID(request));
		if(userInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}

		if(!userInfo.getMUT_POSITION().equals("001") && !userInfo.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}

		if(DOCNO.length() == 0) {
			mav.setViewName("redirect:/NOTICE_LIST.do");
			mav.addObject("msg","문서번호를 가져오지 못했습니다.");
			return mav;
		}

		NoticeVo NoticeInfo = noticeService.getNoticeInfo(DOCNO);
		if(NoticeInfo == null) {
			mav.setViewName("redirect:/NOTICE_LIST.do");
			mav.addObject("msg","공지사항 정보를 가져오지 못했습니다.");
			return mav;
		}


		List<NoticeVo> FileList = noticeService.getNoticeFileList(DOCNO);
		if(FileList != null) {
			for (NoticeVo noticeVo : FileList) {
				int result = fileService.fileDelete(noticeVo.getMDF_FILE(), "", request);
				if(result == -1) {
					mav.setViewName("redirect:/Notice_Add.do");
					mav.addObject("DOCNO",DOCNO);
					mav.addObject("msg","첨부파일 삭제실패!");
					return mav;
				}
				else {
					result = noticeService.deleteNoticeFile(noticeVo.getMDF_DOCNO(), noticeVo.getMDF_SEQ());
					if(result == -1) {
						mav.setViewName("redirect:/Notice_Add.do");
						mav.addObject("DOCNO",DOCNO);
						mav.addObject("msg","첨부파일 삭제실패!");
						return mav;
					}
				}
			}
			int susinResult = noticeService.deleteSusinList(DOCNO);
			if(susinResult == -1) {
				mav.setViewName("redirect:/Notice_Add.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","수신정보 삭제실패!");
				return mav;
			}

			int UserResult = noticeService.deleteNoticeUser(DOCNO);
			if(UserResult == -1) {
				mav.setViewName("redirect:/Notice_Add.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","조회 정보 삭제실패!");
				return mav;
			}

			int noticeResult = noticeService.deleteNotice(DOCNO);
			if(noticeResult == -1) {
				mav.setViewName("redirect:/Notice_Add.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","공지사항 삭제실패!");
				return mav;
			}

			mav.setViewName("redirect:/NOTICE_LIST.do");
			return mav;
		}
		else {
			mav.setViewName("redirect:/Notice_Add.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","첨부파일 목록을 가져오지 못했습니다.");
			return mav;
		}
	}
	
	@RequestMapping("/Ajax_FILE_DEL")
	public @ResponseBody String Ajax_FILE_DEL(
			HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "SEQ", defaultValue = "") String SEQ
			) {

		try {
			NoticeVo fileinfo = noticeService.getNoticeFileInfo(DOCNO, SEQ);
			if(fileinfo == null) {
				return "-1";
			}
			else {
				int result = fileService.fileDelete(fileinfo.getMDF_FILE(), "", request);
				if(result == -1)
					return "-1";

				result = noticeService.deleteNoticeFile(DOCNO, SEQ);
				if(result == -1)
					return "-1";
			}
		}
		catch (Exception e) {
			return "-1";
		}
	    return "1";
	}
	
	@RequestMapping(value = "Ajax_CheckUser")
	public ModelAndView Ajax_CheckUser(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		List<NoticeVo> UserList = noticeService.getNoticeUser(DOCNO);
		
		mav.setViewName("ajax/Ajax_NoticeUser");
		mav.addObject("UserList", UserList);
		mav.addObject("UserListSize", UserList.size());
		return mav;
	}
	
	@RequestMapping(value = "ModelList")
	public ModelAndView ModelList(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		if(!UserInfo.getMUT_POSITION().equals("001") && !UserInfo.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		List<ModelVo> ModelList = modelService.getModelList("", "", "", "", "", "", "", "", 1, 10);
		int listCnt = ModelList.size() == 0 ? 0 : ModelList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, 1);
		
		mav.setViewName("base/MODEL_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("ModelList", ModelList);
		mav.addObject("ModelListSize", ModelList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("msg",msg);
		mav.addObject("level", UserInfo.getMUT_POSITION());
		return mav;
	}
	
	@RequestMapping(value = "ModelList_search")
	public ModelAndView ModelList_search(HttpServletRequest request, 
			@RequestParam(value = "LCODEList", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODEList", defaultValue = "") String MCODE,
			@RequestParam(value = "SCODEList", defaultValue = "") String SCODE,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "MCODENAME", defaultValue = "") String MCODENAME,
			@RequestParam(value = "SCODENAME", defaultValue = "") String SCODENAME,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		if(!UserInfo.getMUT_POSITION().equals("001") && !UserInfo.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		List<ModelVo> MCODELIST = modelService.MgroupList(LOGC, LCODE);
		List<ModelVo> SCODELIST = modelService.SgroupList(LOGC, LCODE, MCODE);
		List<ModelVo> ModelList = modelService.getModelList(LOGC, LCODE, MCODE, SCODE, MNAME, MCODENAME, SCODENAME, "", PAGE, 10);
		int listCnt = ModelList.size() == 0 ? 0 : ModelList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("base/MODEL_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("ModelList", ModelList);
		mav.addObject("ModelListSize", ModelList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("MCODELIST", MCODELIST);
		mav.addObject("SCODELIST", SCODELIST);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("LCODE", LCODE);
		mav.addObject("MCODE", MCODE);
		mav.addObject("SCODE", SCODE);
		mav.addObject("LOGC", LOGC);
		mav.addObject("MNAME", MNAME);
		mav.addObject("MCODENAME", MCODENAME);
		mav.addObject("SCODENAME", SCODENAME);
		mav.addObject("level", UserInfo.getMUT_POSITION());
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_MLIST.do")
	public ModelAndView Ajax_MLIST(
			@RequestParam(value = "LOGC") String LOGC,
			@RequestParam(value = "LCODE") String LCODE,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<ModelVo> MCODELIST = modelService.MgroupList(LOGC, LCODE);
		mav.setViewName("ajax/Ajax_ModelGrp");
		mav.addObject("MCODELIST",MCODELIST);
		mav.addObject("type","M");
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_SLIST.do")
	public ModelAndView Ajax_SLIST(
			@RequestParam(value = "LOGC") String LOGC,
			@RequestParam(value = "LCODE") String LCODE,
			@RequestParam(value = "MCODE") String MCODE,
									HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<ModelVo> SCODELIST = modelService.SgroupList(LOGC, LCODE, MCODE);
		mav.setViewName("ajax/Ajax_ModelGrp");
		mav.addObject("SCODELIST",SCODELIST);
		mav.addObject("type","S");
		return mav;
	}
	
	@RequestMapping(value = "MODEL_DETAIL")
	public ModelAndView MODEL_DETAIL(HttpServletRequest request, 
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "msg", defaultValue = "") String msg) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		if(!UserInfo.getMUT_POSITION().equals("001") && !UserInfo.getMUT_POSITION().equals("002")) {
			mav.setViewName("index");
			return mav;
		}
		
		ModelVo ModelInfo = modelService.getModelInfo(MCODE);
		List<ModelVo> ModelIlyeog = modelService.ModelIlyeog(MCODE);
		if(ModelInfo == null)
		{
			mav.setViewName("redirect:/ModelList.do");
			mav.addObject("msg","상품코드를 가져오지 못했습니다.");
			return mav;
		}
		mav.addObject("ModelInfo", ModelInfo);
		mav.addObject("ModelIlyeog", ModelIlyeog);
		mav.addObject("ModelIlyeogSize", ModelIlyeog.size());

		mav.setViewName("base/MODEL_DETAIL");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("MCODE", MCODE);
		mav.addObject("level", UserInfo.getMUT_POSITION());
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "MODEL_Add")
	public ModelAndView MODEL_Add(HttpServletRequest request, 
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CodeVo> MKINDLIST = codeService.getSCodeList("200","200");
		
		String TYPE = "";
		if(MCODE.length() == 0)
		{
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("LCODELIST",  LCODELIST);
			mav.addObject("PAGE", "상품코드 추가");
			TYPE = "I";
		}
		else {
			ModelVo ModelInfo = modelService.getModelInfo(MCODE);
			List<ModelVo> ModelIlyeog = modelService.ModelIlyeog(MCODE);
			if(ModelInfo == null)
			{
				mav.setViewName("redirect:/ModelList.do");
				mav.addObject("msg","상품코드를 가져오지 못했습니다.");
				return mav;
			}
			mav.addObject("ModelInfo", ModelInfo);
			mav.addObject("ModelIlyeog", ModelIlyeog);
			mav.addObject("ModelIlyeogSize", ModelIlyeog.size());
			mav.addObject("PAGE", "사용자 수정");
			TYPE = "M";
		}
		mav.setViewName("base/MODEL_Add");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("MKINDLIST", MKINDLIST);
		mav.addObject("TYPE", TYPE);
		mav.addObject("MCODE", MCODE);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "MODEL_Add_Save")
	public ModelAndView MODEL_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "KINDS", defaultValue = "") String KINDS,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MGROUP", defaultValue = "") String MGROUP,
			@RequestParam(value = "SGROUP", defaultValue = "") String SGROUP,
			@RequestParam(value = "UNIT", defaultValue = "") String UNIT,
			@RequestParam(value = "StartDay", defaultValue = "1900-01-01") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "2100-12-31") String EndDay,
			@RequestParam(value = "BizSts", defaultValue = "Y") String BizSts,
			@RequestParam(value = "CMNT", defaultValue = "") String CMNT,
			@RequestParam(value = "ERPMODEL", defaultValue = "") String ERPMODEL) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}

		if(TYPE.equals("I")) {
			if(KINDS.length() == 0) {
				mav.setViewName("redirect:/MODEL_Add.do");
				mav.addObject("msg","구분을 선택하세요.");
				return mav;
			}
			if(LOGC.length() == 0) {
				mav.setViewName("redirect:/MODEL_Add.do");
				mav.addObject("msg","물류센터를 선택하세요.");
				return mav;
			}
			if(LCODE.length() == 0) {
				mav.setViewName("redirect:/MODEL_Add.do");
				mav.addObject("msg","대분류를 선택하세요.");
				return mav;
			}
		}	

		ModelVo vo = new ModelVo();
		vo.setMMC_CODE(MCODE);
		vo.setMMC_LOGC(LOGC);
		vo.setMMC_LGROUP(LCODE);
		vo.setMMC_MGROUP(MGROUP);
		vo.setMMC_SGROUP(SGROUP);
		vo.setMMC_KINDS(KINDS);
		vo.setMMC_UNIT(UNIT);
		vo.setMMC_StartDay(StartDay);
		vo.setMMC_EndDay(EndDay);
		vo.setMMC_BizSts(BizSts);
		vo.setMMC_CMNT(CMNT);
		vo.setMMC_ERPMODEL(ERPMODEL);
		vo.setCMN_MAK_PROG("Model_Add");
		vo.setCMN_MAK_ID(userService.getUserID(request));
		vo.setCMN_UPD_PROG("Model_Add");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		if(TYPE.equals("I")){
			if(modelService.insertModel(vo) <= 0) {
				mav.setViewName("redirect:/MODEL_Add.do");
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
			}
			else {
				mav.setViewName("redirect:/MODEL_DETAIL.do");
				mav.addObject("MCODE",vo.getMMC_CODE());
			}
		}
		else if(TYPE.equals("M")){
			if(modelService.updateModel(vo) <= 0) {
				mav.setViewName("redirect:/MODEL_Add.do");
				mav.addObject("MCODE",MCODE);
				mav.addObject("msg","저장실패! 다시 시도해 주세요.");
			}
			else {
				mav.setViewName("redirect:/MODEL_DETAIL.do");
				mav.addObject("MCODE",MCODE);
			}
		}
		else {
			mav.setViewName("redirect:/ModelList.do");
			mav.addObject("msg","상품코드 정보를 가져오지 못했습니다.");
		}
		return mav;
	}
	
	@RequestMapping(value = "MODEL_Add_Del")
	public ModelAndView MODEL_Add_Del(HttpServletRequest request,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE) {

		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		if(MCODE.length() == 0) {
			mav.setViewName("redirect:/ModelList.do");
			mav.addObject("msg","삭제실패! 상품코드 정보를 가져올 수 없습니다.");
			return mav;
		}
		if(modelService.deleteModel(MCODE) <= 0) {
			mav.setViewName("redirect:/MODEL_Add.do");
			mav.addObject("MCODE", MCODE);
			mav.addObject("msg","삭제실패! 다시 시도해 주세요.");
			return mav;
		}
		else {
			mav.setViewName("redirect:/ModelList.do");
			return mav;
		}
	}
	@RequestMapping(value = "Ajax_ModelList_search")
	public ModelAndView Ajax_ModelList_search(HttpServletRequest request, 
			@RequestParam(value = "LCODEList", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODEList", defaultValue = "") String MCODE,
			@RequestParam(value = "SCODEList", defaultValue = "") String SCODE,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "MCODENAME", defaultValue = "") String MCODENAME,
			@RequestParam(value = "SCODENAME", defaultValue = "") String SCODENAME,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE
			) {
		
		ModelAndView mav = new ModelAndView();
		
		List<ModelVo> ModelList = modelService.getModelList(LOGC, LCODE, MCODE, SCODE, MNAME, MCODENAME, SCODENAME, "", PAGE, 10);
		int listCnt = ModelList.size() == 0 ? 0 : ModelList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("ajax/Ajax_ModelSearch");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("ModelList", ModelList);
		mav.addObject("ModelListSize", ModelList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		return mav;
	}
	@RequestMapping("/Ajax_ViewImage")
	public @ResponseBody String Ajax_ViewImage(HttpServletRequest request, 
			@RequestParam(value = "MCODE", defaultValue = "")	String MCODE) {
		
		ModelVo ModelInfo = modelService.getModelInfo(MCODE);
		if(ModelInfo == null)
			return String.valueOf("-1");
		else {
			if(fileService.fileDelete(MCODE, "LABEL", request) < 0)
				return String.valueOf("-2");
			try {
				modelService.WriteQrCode(ModelInfo.getMMC_CODE(), ModelInfo.getMMC_LGROUPNAME(), ModelInfo.getMMC_MGROUP(), ModelInfo.getMMC_SGROUP(), 200, 200, request);
				if(!fileService.Isfile(MCODE + ".jpg", "LABEL", request))
					throw new Exception();
				return String.valueOf("1");
			} catch (Exception e) {
				return String.valueOf("-3");
			} 
		}
	}
}
