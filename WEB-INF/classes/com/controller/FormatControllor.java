package com.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dao.FormatDao;
import com.service.CenterService;
import com.service.CodeService;
import com.service.CommonService;
import com.service.FileService;
import com.service.FormatService;
import com.service.InOutService;
import com.service.UserService;
import com.vo.CenterVo;
import com.vo.CodeVo;
import com.vo.DealFormatVo;
import com.vo.DealModelVo;
import com.vo.JegoFormatVo;
import com.vo.ModelVo;
import com.vo.MoveVo;
import com.vo.OtherVo;
import com.vo.UserVo;

@Controller
public class FormatControllor {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CenterService centerService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private FormatService formatService;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "Dealformat_List")
	public ModelAndView Dealformat_List(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		
		if(UserInfo.getMUT_POSITION().equals("005"))
		{
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = "";
		String CENTER = "";
		String CTYPE = "";
		if(UserInfo.getMUT_POSITION().equals("004")) {
			CTYPE = "0";
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		else if(UserInfo.getMUT_POSITION().equals("003")) {
			CTYPE = "0";
			BONBU = UserInfo.getMCI_Bonbu();
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST", CENTERLIST);
			mav.addObject("CENTER", UserInfo.getMCI_Center());
		}
		else {
			CTYPE = "";
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST", BONBULIST);
			
			List<CenterVo> INBONBULIST = centerService.getBONBUList(LOGC);
			mav.addObject("INBONBULIST", INBONBULIST);
		}
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		List<DealFormatVo> DealFormatList = formatService.getDealFormatList(StartDay, EndDay, "", "", "", LOGC, BONBU, CENTER, "", "", CTYPE, 1, 10);
		int listCnt = DealFormatList.size() == 0 ? 0 : DealFormatList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("format/Dealformat_List");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("DealFormatList", DealFormatList);
		mav.addObject("DealFormatListSize", DealFormatList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", 1);
		mav.addObject("CTYPE", CTYPE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("msg", msg);
		mav.addObject("LOGC", LOGC);
		mav.addObject("INLOGC", LOGC);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		return mav;
	}
	
	@RequestMapping(value = "Dealformat_List_Search")
	public ModelAndView Dealformat_List_Search(HttpServletRequest request,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "INLOGC", defaultValue = "") String INLOGC,
			@RequestParam(value = "INBONBU", defaultValue = "") String INBONBU,
			@RequestParam(value = "INCENTER", defaultValue = "") String INCENTER,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "CTYPE", defaultValue = "") String CTYPE,
			@RequestParam(value = "INSTSTYPE", defaultValue = "") String INSTSTYPE,
			@RequestParam(value = "OUTSTSTYPE", defaultValue = "") String OUTSTSTYPE,
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
		
		if(UserInfo.getMUT_POSITION().equals("005"))
		{
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		if(UserInfo.getMUT_POSITION().equals("004")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
			if(CTYPE.equals("2")) {
				INLOGC = "";
				INBONBU = "";
				INCENTER = "";
			}
			else if(CTYPE.equals("1")) {
				INLOGC = LOGC;
				INBONBU = BONBU;
				INCENTER = CENTER;
				LOGC = "";
				BONBU = "";
				CENTER = "";
			}
		}
		else if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST", CENTERLIST);
			if(CTYPE.equals("2")) {
				INLOGC = "";
				INBONBU = "";
				INCENTER = "";
			}
			else if(CTYPE.equals("1")) {
				INLOGC = LOGC;
				INBONBU = BONBU;
				INCENTER = CENTER;
				LOGC = "";
				BONBU = "";
				CENTER = "";
			}
		}
		else {
			CTYPE = "";
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST", BONBULIST);
			mav.addObject("CENTERLIST", CENTERLIST);
			
			List<CenterVo> INBONBULIST = centerService.getBONBUList(INLOGC);
			List<CenterVo> INCENTERLIST = centerService.getCENTERList(INLOGC, INBONBU);
			mav.addObject("INBONBULIST", INBONBULIST);
			mav.addObject("INCENTERLIST", INCENTERLIST);
		}
		
		List<DealFormatVo> DealFormatList = formatService.getDealFormatList(StartDay, EndDay, INLOGC, INBONBU, INCENTER, LOGC, BONBU, CENTER, INSTSTYPE, OUTSTSTYPE, CTYPE, PAGE, 10);
		int listCnt = DealFormatList.size() == 0 ? 0 : DealFormatList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("format/Dealformat_List");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("DealFormatList", DealFormatList);
		mav.addObject("DealFormatListSize", DealFormatList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("INLOGC", INLOGC);
		mav.addObject("INBONBU", INBONBU);
		mav.addObject("INCENTER", INCENTER);
		mav.addObject("CTYPE", CTYPE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("INSTSTYPE", INSTSTYPE);
		mav.addObject("OUTSTSTYPE", OUTSTSTYPE);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_POPBONBU")
	public ModelAndView Ajax_MOVEBONBU(
			@RequestParam(value = "TYPE") String TYPE,
			@RequestParam(value = "LOGC") String LOGC,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		mav.setViewName("ajax/Ajax_POPBONBU");
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("type",TYPE);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_POPCENTER")
	public ModelAndView Ajax_MOVECENTER(
			@RequestParam(value = "TYPE") String TYPE,
			@RequestParam(value = "LOGC") String LOGC,
			@RequestParam(value = "BONBU") String BONBU,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		mav.setViewName("ajax/Ajax_POPCENTER");
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("type",TYPE);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_DealSearch")
	public ModelAndView Ajax_Search(HttpServletRequest request, 
			@RequestParam(value = "POPCTYPE", defaultValue = "Y") String POPCTYPE,
			@RequestParam(value = "POPStartDay", defaultValue = "") String POPStartDay,
			@RequestParam(value = "POPEndDay", defaultValue = "") String POPEndDay,
			@RequestParam(value = "POPLOGC", defaultValue = "") String POPLOGC,
			@RequestParam(value = "POPBONBU", defaultValue = "") String POPBONBU,
			@RequestParam(value = "POPCENTER", defaultValue = "") String POPCENTER,
			@RequestParam(value = "POPINLOGC", defaultValue = "") String POPINLOGC,
			@RequestParam(value = "POPINBONBU", defaultValue = "") String POPINBONBU,
			@RequestParam(value = "POPINCENTER", defaultValue = "") String POPINCENTER,
			@RequestParam(value = "POP_MNAME", defaultValue = "") String POP_MNAME,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE,
			@RequestParam(value = "PTYPE", defaultValue = "1") int PTYPE,
			@RequestParam(value = "PSIZE", defaultValue = "10") int PSIZE
			) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("ajax/Ajax_DealSearch");
		
		if(userService.getUserID(request).equals("")){
			mav.addObject("DealInoutListSize", 0);
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.addObject("DealInoutListSize", 0);
			return mav;
		}
		
		if(UserInfo.getMUT_POSITION().equals("005"))
		{
			mav.addObject("DealInoutListSize", 0);
			return mav;
		}
		
		if(UserInfo.getMUT_POSITION().equals("004")) {
			if(POPCTYPE.equals("Y")) {
				POPLOGC = UserInfo.getMCI_LOGC();
				POPBONBU = UserInfo.getMCI_Bonbu();
				POPCENTER = UserInfo.getMCI_Center();
			}
			else {
				POPINLOGC = UserInfo.getMCI_LOGC();
				POPINBONBU = UserInfo.getMCI_Bonbu();
				POPINCENTER = UserInfo.getMCI_Center();
			}
		}
		else if(UserInfo.getMUT_POSITION().equals("003")) {
			if(POPCTYPE.equals("Y")) {
				POPLOGC = UserInfo.getMCI_LOGC();
				POPBONBU = UserInfo.getMCI_Bonbu();
			}
			else {
				POPINLOGC = UserInfo.getMCI_LOGC();
				POPINBONBU = UserInfo.getMCI_Bonbu();
			}
		}
		
		List<DealFormatVo> DealInoutList = formatService.getDealInoutInfo(POPStartDay, POPEndDay, POPINLOGC, POPINBONBU, POPINCENTER, POPLOGC, POPBONBU, POPCENTER, POP_MNAME, PTYPE, PAGE, PSIZE);
		int listCnt = DealInoutList.size() == 0 ? 0 : DealInoutList.get(0).getTotalCnt();
		CommonService common  = new CommonService();
		common.SetPaging(listCnt, PAGE, PSIZE);
		
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("DealInoutList", DealInoutList);
		mav.addObject("DealInoutListSize", DealInoutList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("PTYPE", PTYPE);
		return mav;
	}
	
	@RequestMapping(value = "Dealformat_Add_Save")
	public ModelAndView Dealformat_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "Deal_CHK", defaultValue = "") String DOCNO,
			@RequestParam(value = "POPStartDay", defaultValue = "") String POPStartDay,
			@RequestParam(value = "POPEndDay", defaultValue = "") String POPEndDay) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/Dealformat_List.do");
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(POPStartDay.length() == 0)
		{
			mav.addObject("msg","출고일자를 가져오지 못했습니다.");
			return mav;
		}
		
		if(POPEndDay.length() == 0)
		{
			mav.addObject("msg","출고일자를 가져오지 못했습니다.");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		int result = formatService.insertFormat(DOCNO, userService.getUserID(request), POPStartDay, POPEndDay);
		if(result == -1) {
			mav.addObject("msg","접수번호가 잘못되었습니다.");
		}
		else if(result == -2) {
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
		}
		else if(result == -3) {
			mav.addObject("msg","발급 목록을 불러오지 못했습니다.");
		}
		else if(result == -4) {
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
		}
		else {
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
			Calendar nowDate = Calendar.getInstance();
			mav.setViewName("redirect:/Dealformat_List_Search.do");
			mav.addObject("STSTYPE","0");
			mav.addObject("StartDay",format1.format(nowDate.getTime()));
			if(UserInfo.getMUT_POSITION().equals("001") || UserInfo.getMUT_POSITION().equals("002"))
				mav.addObject("CTYPE","");
			else
				mav.addObject("CTYPE","0");
		}
		return mav;
	}
	
	@RequestMapping(value = "Dealformat_DETAIL")
	public ModelAndView Dealformat_DETAIL(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/Dealformat_List.do");
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		DealFormatVo vo = formatService.getDealFormatInfo(DOCNO);
		if(vo == null)
		{
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(UserInfo.getMUT_POSITION().equals("005") ||
				( 
					( UserInfo.getMUT_POSITION().equals("003") || UserInfo.getMUT_POSITION().equals("004") ) 
					&& !vo.getDDT_INCENTER().equals(UserInfo.getMUT_CENTER()) && !vo.getDDT_OUTCENTER().equals(UserInfo.getMUT_CENTER())
				)
		)
		{
			mav.addObject("msg","권한이 없습니다!");
			return mav;
		}
		
		mav.setViewName("format/Dealformat_DETAIL");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("DealFormatInfo", vo);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("msg", msg);
		return mav;
	}
	
	@RequestMapping(value = "/DealCfm")
	public ModelAndView DealCfm(
			@RequestParam(value = "DOCNO") String DOCNO,
			@RequestParam(value = "STSTYPE") String STSTYPE,
			@RequestParam(value = "STS") String STS,
			@RequestParam(value = "DDT_CMNT") String CMNT,
			HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/Dealformat_DETAIL.do");
		mav.addObject("DOCNO",DOCNO);
		
		String INSTS = "";
		String OUTSTS = "";
		if(STSTYPE.equals("IN")) {
			INSTS = STS;
		}
		else if(STSTYPE.equals("OUT")) {
			OUTSTS = STS;
		}
		else {
			mav.addObject("msg","승인 또는 반려 값을 가져오지 못했습니다.");
			return mav;
		}
		
		if(STS.length() == 0) {
			mav.addObject("msg","승인 또는 반려 값을 가져오지 못했습니다.");
			return mav;
		}
		
		int result = formatService.updateDocTRANCfm(DOCNO, INSTS, OUTSTS, CMNT, userService.getUserID(request));
		if(result == -1)
		{
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
		}
		return mav;
	}
	
	@RequestMapping(value="/Ajax_DealExcel", produces="application/text; charset=utf8")	
	public @ResponseBody String Ajax_DealExcel(
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			HttpServletRequest request) {
		
		if(fileService.Isfile(DOCNO + ".xlsx", "EXCEL", request))
			return "1";
		else{
			DealFormatVo vo = formatService.getDealFormatInfo(DOCNO);
			if(vo == null)
			{
				return "-1";
			}
			if(!vo.getDDT_OUTSTS().equals("1") || !vo.getDDT_INSTS().equals("1")) {
				return "-2";
			}
			return formatService.createExcel(vo, request);
		}
	}
	
	@RequestMapping(value = "Jegoformat_Add_Save")
	public ModelAndView Jegoformat_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "DDJ_CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "JEGOCHK", defaultValue = "") String JEGOCHK,
			@RequestParam(value = "YEAR", defaultValue = "0") int YEAR,
			@RequestParam(value = "MONTH", defaultValue = "0") int MONTH) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/BonbuStatusReport.do");
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		if(CENTER.length() == 0)
		{
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(JEGOCHK.length() == 0)
		{
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(YEAR == 0)
		{
			mav.addObject("msg","연도를 가져오지 못했습니다.");
			return mav;
		}
		if(YEAR < 1753 || YEAR > 9999) {
			mav.addObject("msg","올바른 연도를 입력해주세요.");
			return mav;
		}
		
		if(MONTH == 0)
		{
			mav.addObject("msg","날짜를 가져오지 못했습니다.");
			return mav;
		}
		if(MONTH < 1 || MONTH > 12) {
			mav.addObject("msg","올바른 날짜를 선택해주세요.");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		int result = formatService.insertJegoFormat(CENTER, JEGOCHK, userService.getUserID(request), YEAR, MONTH);
		if(result == -1) {
			mav.addObject("msg","상품코드 정보가 잘못되었습니다.");
		}
		else if(result == -2) {
			mav.addObject("msg","상품코드가 잘못되었습니다.");
		}
		else if(result == -3) {
			mav.addObject("msg","현재고를 가져오지 못했습니다.");
		}
		else if(result == -4) {
			mav.addObject("msg","상품코드 정보가 없습니다. 다시 시도해주세요.");
		}
		else if(result == -5) {
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
		}
		else {
			mav.addObject("msg","발급완료!");
		}
		return mav;
	}
	
	@RequestMapping(value = "Jegoformat_List")
	public ModelAndView Jegoformat_List(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		
		if(UserInfo.getMUT_POSITION().equals("005"))
		{
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		if(UserInfo.getMUT_POSITION().equals("003")) {
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		else if(UserInfo.getMUT_POSITION().equals("001") || UserInfo.getMUT_POSITION().equals("002")) {
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("BONBULIST", BONBULIST);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		List<JegoFormatVo> JegoFormatList = formatService.getJegoFormatList(StartDay, EndDay, LOGC, BONBU, CENTER, "", 1, 10);
		int listCnt = JegoFormatList.size() == 0 ? 0 : JegoFormatList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		mav.setViewName("format/Jegoformat_List");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("JegoFormatList", JegoFormatList);
		mav.addObject("JegoFormatListSize", JegoFormatList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", 1);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("msg", msg);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		return mav;
	}
	
	@RequestMapping(value = "Jegoformat_List_Search")
	public ModelAndView Jegoformat_List_Search(HttpServletRequest request,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "INSTSTYPE", defaultValue = "") String STSTYPE,
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
		
		if(UserInfo.getMUT_POSITION().equals("005"))
		{
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		if(UserInfo.getMUT_POSITION().equals("004")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();

		}
		else if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		else {
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("BONBULIST", BONBULIST);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		
		List<JegoFormatVo> JegoFormatList = formatService.getJegoFormatList(StartDay, EndDay, LOGC, BONBU, CENTER, STSTYPE, PAGE, 10);
		int listCnt = JegoFormatList.size() == 0 ? 0 : JegoFormatList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("format/Jegoformat_List");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("JegoFormatList", JegoFormatList);
		mav.addObject("JegoFormatListSize", JegoFormatList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("STSTYPE", STSTYPE);
		return mav;
	}
	
	@RequestMapping(value = "Jegoformat_DETAIL")
	public ModelAndView Jegoformat_DETAIL(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/Jegoformat_List.do");
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		JegoFormatVo vo = formatService.getJegoFormatInfo(DOCNO);
		if(vo == null)
		{
			mav.addObject("msg","발급정보를 가져오지 못했습니다.");
			return mav;
		}
		
		mav.setViewName("format/Jegoformat_DETAIL");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("JealFormatInfo", vo);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("msg", msg);
		return mav;
	}
	
	@RequestMapping(value = "/JegoCfm")
	public ModelAndView JegoCfm(
			@RequestParam(value = "DOCNO") String DOCNO,
			@RequestParam(value = "STS") String STS,
			@RequestParam(value = "DDJ_CMNT") String CMNT,
			HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/Jegoformat_DETAIL.do");
		mav.addObject("DOCNO",DOCNO);
		
		if(STS.length() == 0) {
			mav.addObject("msg","승인 또는 반려 값을 가져오지 못했습니다.");
			return mav;
		}
		
		int result = formatService.updateJegoDocTRANCfm(DOCNO, STS, CMNT, userService.getUserID(request));
		if(result == -1)
		{
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
		}
		return mav;
	}
	
	@RequestMapping(value="/Ajax_JegoExcel", produces="application/text; charset=utf8")	
	public @ResponseBody String Ajax_JegoExcel(
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			HttpServletRequest request) {
		
		if(fileService.Isfile(DOCNO + ".xlsx", "EXCEL", request))
			return "1";
		else{
			JegoFormatVo vo = formatService.getJegoFormatInfo(DOCNO);
			if(vo == null)
			{
				return "-1";
			}
			return formatService.createJegoExcel(vo, request);
		}
	}
}
