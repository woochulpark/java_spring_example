package com.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.beust.jcommander.internal.Console;
import com.service.CenterService;
import com.service.CodeService;
import com.service.CommonService;
import com.service.FileService;
import com.service.InOutService;
import com.service.ModelService;
import com.service.NoticeService;
import com.service.UserService;
import com.vo.BonbuStatusVo;
import com.vo.BonbuVo;
import com.vo.CenterVo;
import com.vo.CfmVo;
import com.vo.CodeVo;
import com.vo.EventVo;
import com.vo.InOutListVo;
import com.vo.InoutReportVo;
import com.vo.JegoMagamReportVo;
import com.vo.JegoVo;
import com.vo.LogcInVo;
import com.vo.ModelBepumVo;
import com.vo.ModelVo;
import com.vo.MoveVo;
import com.vo.NoticeVo;
import com.vo.OrderVo;
import com.vo.OtherVo;
import com.vo.UserVo;

@Controller
public class InOutController {
	@Autowired
	private InOutService inOutService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CenterService centerService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "LogcInList")
	public ModelAndView LogcInList(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		String LOGC = UserInfo.getMCI_LOGC();
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<LogcInVo> LogcInList = inOutService.getLogcInList(StartDay, EndDay, "", "", "", 1, 10);
		int listCnt = LogcInList.size() == 0 ? 0 : LogcInList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/LOGC_IN_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("LogcInList", LogcInList);
		mav.addObject("LogcInListSize", LogcInList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LOGC", LOGC);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("msg",msg);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		return mav;
	}
	
	@RequestMapping(value = "LogcInList_search")
	public ModelAndView LogcInList_search(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "BISSTS", defaultValue = "") String BISSTS,
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
		List<LogcInVo> LogcInList = inOutService.getLogcInList(StartDay, EndDay, LOGC, MNAME, BISSTS, PAGE, 10);
		int listCnt = LogcInList.size() == 0 ? 0 : LogcInList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/LOGC_IN_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("BISSTS", BISSTS);
		mav.addObject("LogcInList", LogcInList);
		mav.addObject("LogcInListSize", LogcInList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("MNAME", MNAME);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "LogcIn_Add")
	public ModelAndView LogcIn_Add(HttpServletRequest request, 
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
		String LOGC = UserInfo.getMCI_LOGC();
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Date time = new Date();
		String NOWDATE = format1.format(time);
		mav.setViewName("IO/LogcIn_Add");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("NOWDATE", NOWDATE);
		mav.addObject("LOGC", LOGC);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "Ajax_LogcIn_search")
	public ModelAndView Ajax_LogcIn_search(HttpServletRequest request, 
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
		
		List<ModelVo> ModelList = modelService.getModelList(LOGC, LCODE, MCODE, SCODE, MNAME, MCODENAME, SCODENAME, "Y", PAGE, 10);
		int listCnt = ModelList.size() == 0 ? 0 : ModelList.get(0).getTotalCnt();
		CommonService common  = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("ajax/Ajax_LogcInSearch");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("ModelList", ModelList);
		mav.addObject("ModelListSize", ModelList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		return mav;
	}
	
	@RequestMapping(value = "LogcIn_DETAIL")
	public ModelAndView LogcIn_DETAIL(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		mav.setViewName("IO/LogcIn_DETAIL");
		mav.addObject("LogcInInfo", LogcInInfo);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("level", UserInfo.getMUT_POSITION());
		return mav;
	}
	
	@RequestMapping(value = "LogcIn_Add_Save")
	public ModelAndView LogcIn_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "JEJO", defaultValue = "") String JEJO,
			@RequestParam(value = "INDATE", defaultValue = "") String INDATE,
			@RequestParam(value = "INPRICE", defaultValue = "0") String INPRICE,
			@RequestParam(value = "INCNT", defaultValue = "0") String INCNT) {

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
		
		
		String[] MCODELIST = MCODE.split(",");
		String[] JEJOLIST = JEJO.split(",");
		String[] INDATELIST = INDATE.split(",");
		String[] INPRICELIST = INPRICE.split(",");
		String[] INCNTLIST = INCNT.split(",");
		if(MCODELIST.length != JEJOLIST.length ||
				JEJOLIST.length != INDATELIST.length ||
				INDATELIST.length != INPRICELIST.length ||
				INPRICELIST.length != INCNTLIST.length
				)
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}

		int InsertCnt = 0;
		for(int i = 0; i < MCODELIST.length; ++i) {
			String TEMPMCODE = MCODELIST[i];
			String TEMPJEJO = JEJOLIST[i];
			String TEMPINDATE = INDATELIST[i];
			int TEMPINPRICE = 0;
			int TEMPINCNT = 0;
			try {
				TEMPINPRICE = Integer.parseInt(INPRICELIST[i]);
				TEMPINCNT = Integer.parseInt(INCNTLIST[i]);
			}
			catch (Exception e) {
				continue;
			}
			if(TEMPMCODE.length() == 0)
			{
				continue;
			}
			if(TEMPJEJO.length() == 0)
			{
				continue;
			}
			if(TEMPINDATE.length() == 0)
			{
				continue;
			}
			if(TEMPINCNT == 0)
			{
				continue;
			}
			LogcInVo vo = new LogcInVo();
			vo.setDIL_LOGC(LOGC);
			vo.setDIL_MODEL(TEMPMCODE);
			vo.setDIL_JEJO(TEMPJEJO);
			vo.setDIL_INDate(TEMPINDATE);
			vo.setDIL_INPRICE(TEMPINPRICE);
			vo.setDIL_QTY(TEMPINCNT);
			vo.setDIL_OUTSts("N");
			vo.setCMN_MAK_PROG("LogcIn_Add");
			vo.setCMN_MAK_ID(userService.getUserID(request));
			vo.setCMN_UPD_PROG("LogcIn_Add");
			vo.setCMN_UPD_ID(userService.getUserID(request));
			if(inOutService.insertLogcIn(vo) <= 0) {
				continue;
			}
			else {
				InsertCnt++;
			}
		}
		if(InsertCnt == MCODELIST.length) {
			mav.setViewName("redirect:/LogcInList.do");
		}
		else if(InsertCnt == 0) {
			mav.setViewName("redirect:/LogcIn_Add.do");
			mav.addObject("msg","저장실패! 다시 시도해 주세요.");
		}
		else {
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","일부 상품코드 입고실패! 다시 시도해 주세요.");
		}
		return mav;
	}
	
	@RequestMapping(value = "LogcIn_Modify")
	public ModelAndView LogcIn_Modify(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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

		if(!UserInfo.getMUT_POSITION().equals("001") ) {
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(LogcInInfo.getDIL_OUTSts().equals("Y"))
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","이미 출고된 입고정보입니다.");
			return mav;
		}
		
		mav.setViewName("IO/LogcIn_Modify");
		mav.addObject("DOCNO", DOCNO);
		mav.addObject("LogcInInfo", LogcInInfo);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "LogcIn_Modify_Save")
	public ModelAndView LogcIn_Modify_Save(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "JEJO", defaultValue = "") String JEJO,
			@RequestParam(value = "INDATE", defaultValue = "") String INDATE,
			@RequestParam(value = "INPRICE", defaultValue = "0") String INPRICE,
			@RequestParam(value = "INCNT", defaultValue = "0") String INCNT,
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
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(JEJO.length() == 0) {
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","제조일자를 입력해주세요.");
			return mav;
		}
		
		if(INDATE.length() == 0) {
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","입고일자를 입력해주세요.");
			return mav;
		}
		
		int i_PRICE = 0;
		int i_INCNT = 0;
		try {
			i_PRICE = Integer.parseInt(INPRICE);
			i_INCNT = Integer.parseInt(INCNT);
		}
		catch (Exception e) {
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","입고단가 또는 입고수량이 잘못되었습니다.");
			return mav;
		}
		
		if(i_INCNT == 0) {
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","입고수량을 입력해주세요.");
			return mav;
		}
		
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(LogcInInfo.getDIL_OUTSts().equals("Y"))
		{
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","이미 출고된 입고정보입니다.");
			return mav;
		}
		
		LogcInVo vo = new LogcInVo();
		vo.setDIL_DOCNO(DOCNO);
		vo.setDIL_INDate(INDATE);
		vo.setDIL_JEJO(JEJO);
		vo.setDIL_INPRICE(i_PRICE);
		vo.setDIL_QTY(i_INCNT);
		vo.setCMN_UPD_PROG("LogcIn_Modify");
		vo.setCMN_UPD_ID(userService.getUserID(request));
		mav.setViewName("redirect:/LogcIn_DETAIL.do");
		mav.addObject("DOCNO",DOCNO);
		if(inOutService.updateLogcIn(vo) <= 0) {
			mav.addObject("msg","수정실패! 다시 시도해주세요.");
		}
		return mav;
	}
	
	@RequestMapping(value = "LogcIn_Delete")
	public ModelAndView v(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO) {
		
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

		if(!UserInfo.getMUT_POSITION().equals("001") ) {
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/LogcInList.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","입고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(LogcInInfo.getDIL_OUTSts().equals("Y"))
		{
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("msg","이미 출고된 입고정보입니다.");
			return mav;
		}
		
		if(inOutService.deleteLogcIn(DOCNO) <= 0) {
			mav.setViewName("redirect:/LogcIn_DETAIL.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","삭제실패! 다시 시도해주세요.");
		}
		else {
			mav.setViewName("redirect:/LogcInList.do");
			return mav;
		}

		return mav;
	}
	
	@RequestMapping(value = "BonbuInList")
	public ModelAndView BonbuInList(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		CommonService common = new CommonService();
		String InStartDay = common.StartDay();
		String InEndDay = common.EndDay();
		String OutStartDay = common.StartDay();
		String OutEndDay = common.EndDay();

		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<LogcInVo> BonbuInList = inOutService.getBonbuInList(InStartDay, InEndDay, OutStartDay, OutEndDay, LOGC, "", 1, 10);
		int listCnt = BonbuInList.size() == 0 ? 0 : BonbuInList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/Bonbu_IN_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("BonbuInList", BonbuInList);
		mav.addObject("BonbuInListSize", BonbuInList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LOGC", LOGC);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("InStartDay", InStartDay);
		mav.addObject("InEndDay", InEndDay);
		mav.addObject("OutStartDay", OutStartDay);
		mav.addObject("OutEndDay", OutEndDay);
		return mav;
	}
	
	@RequestMapping(value = "BonbuInList_search")
	public ModelAndView BonbuInList_search(HttpServletRequest request, 
			@RequestParam(value = "InStartDay", defaultValue = "") String InStartDay,
			@RequestParam(value = "InEndDay", defaultValue = "") String InEndDay,
			@RequestParam(value = "OutStartDay", defaultValue = "") String OutStartDay,
			@RequestParam(value = "OutEndDay", defaultValue = "") String OutEndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
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
		
		if(UserInfo.getMUT_POSITION().equals("003") || UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<LogcInVo> BonbuInList = inOutService.getBonbuInList(InStartDay, InEndDay, OutStartDay, OutEndDay, LOGC, MNAME, PAGE, 10);
		int listCnt = BonbuInList.size() == 0 ? 0 : BonbuInList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/Bonbu_IN_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("BonbuInList", BonbuInList);
		mav.addObject("BonbuInListSize", BonbuInList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("InStartDay", InStartDay);
		mav.addObject("InEndDay", InEndDay);
		mav.addObject("OutStartDay", OutStartDay);
		mav.addObject("OutEndDay", OutEndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("MNAME", MNAME);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "Ajax_BonbuIn_search")
	public ModelAndView Ajax_BonbuIn_search(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE
			) {
		
		ModelAndView mav = new ModelAndView();
		
		List<LogcInVo> LogcInList = inOutService.getLogcInList(StartDay, EndDay, LOGC, MNAME, "0", PAGE, 10);
		int listCnt = LogcInList.size() == 0 ? 0 : LogcInList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("ajax/Ajax_BonbuInSearch");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("LogcInList", LogcInList);
		mav.addObject("LogcInListSize", LogcInList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("MNAME", MNAME);
		return mav;
	}
	
	@RequestMapping(value = "BonbuIn_Add")
	public ModelAndView BonbuIn_Add(HttpServletRequest request,
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
		String LOGC = UserInfo.getMCI_LOGC();
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
		Date time = new Date();
		String NOWDATE = format1.format(time);
		mav.setViewName("IO/Bonbu_Add");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("NOWDATE",NOWDATE);
		mav.addObject("LOGC", LOGC);
		return mav;
	}
	
	@RequestMapping(value = "Ajax_BonbuList")
	public ModelAndView Ajax_BonbuList(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO
			) {
		
		ModelAndView mav = new ModelAndView();
		if(DOCNO.length() == 0)
		{
			mav.setViewName("ajax/BonbuList");
			mav.addObject("BonbuInInfo", null);
			mav.addObject("BonbuInInfoSize", 0);
			return mav;
		}
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("ajax/BonbuList");
			mav.addObject("BonbuInInfo", null);
			mav.addObject("BonbuInInfoSize", 0);
			return mav;
		}
		
		List<BonbuVo> BonbuInInfo = inOutService.getBonbuInInfo(LogcInInfo.getDIL_LOGC(), LogcInInfo.getDIL_DOCNO(), LogcInInfo.getDIL_MODEL());
		
		mav.setViewName("ajax/Ajax_BonbuList");
		mav.addObject("BonbuInInfo", BonbuInInfo);
		mav.addObject("BonbuInInfoSize", BonbuInInfo.size());
		mav.addObject("TYPE", "I");
		return mav;
	}
	
	@RequestMapping(value = "Ajax_JegoUpdate")
	public ModelAndView Ajax_JegoUpdate(HttpServletRequest request, 
			@RequestParam(value = "TYPE", defaultValue = "") String TYPE,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value="CENTER[]") List<String> CENTER, 
			@RequestParam(value="JEGO[]") List<String> JEGO
			) {
		
		ModelAndView mav = new ModelAndView();
		
		boolean JegoChk = true;
		if(DOCNO.length() == 0)
		{
			JegoChk = false;
		}
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			JegoChk = false;
		}
		
		if(CENTER.size() == JEGO.size() && JegoChk) {
			for(int i = 0; i < CENTER.size(); ++i) {
				String TCENTER = CENTER.get(i);
				String TJEGO = JEGO.get(i);
				if(TCENTER.length() == 0 || TJEGO.length() == 0)
					continue;
				int i_Jego = 0;
				try {
					i_Jego = Integer.parseInt(TJEGO);
				}
				catch (Exception e) {
					continue;
				}
				inOutService.updateModelJego(LogcInInfo.getDIL_MODEL(), TCENTER, i_Jego, "BonbuIn_Add", userService.getUserID(request));
			}
		}
		
		
		List<BonbuVo> BonbuInInfo = inOutService.getBonbuInInfo(LogcInInfo.getDIL_LOGC(), LogcInInfo.getDIL_DOCNO(), LogcInInfo.getDIL_MODEL());
		
		mav.setViewName("ajax/Ajax_BonbuList");
		mav.addObject("BonbuInInfo", BonbuInInfo);
		mav.addObject("BonbuInInfoSize", BonbuInInfo.size());
		mav.addObject("TYPE", TYPE);
		return mav;
	}
	
	@RequestMapping(value = "BonbuIn_Add_Save")
	public ModelAndView BonbuIn_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "OUTDATE", defaultValue = "") String OUTDATE,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "OUTCNT", defaultValue = "") String OUTCNT) {

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

		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		if(OUTDATE.length() == 0)
		{
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","출고일자를 가져오지 못했습니다.");
			return mav;
		}
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			Date D_OUTDATE = transFormat.parse(OUTDATE);
			Date INDATE = transFormat.parse(LogcInInfo.getDIL_INDate());
			int compare = INDATE.compareTo(D_OUTDATE);
		    if ( compare > 0 )
		    {
		    	mav.setViewName("redirect:/BonbuIn_Add.do");
				mav.addObject("msg","출고일이 입고일보다 작습니다.");
				return mav;
		    }
		}
		catch (Exception e) {
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","출고일자를 가져오지 못했습니다.");
			return mav;
		}
		String[] CENTERLIST = CENTER.split(",");
		String[] OUTCNTLIST = OUTCNT.split(",");
		if(CENTERLIST.length != OUTCNTLIST.length)
		{
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","본부 출고정보를 가져오지 못했습니다.");
			return mav;
		}

		int totalCnt = 0;
		for(int i = 0; i < OUTCNTLIST.length; ++i) {
			String TOUTCNT = OUTCNTLIST[i];
			if(TOUTCNT.length() > 0 && !TOUTCNT.equals("0")) {
				int i_OUTCNT = 0;
				try {
					i_OUTCNT = Integer.parseInt(TOUTCNT);
					totalCnt += i_OUTCNT;
				}
				catch (Exception e) {
					mav.setViewName("redirect:/BonbuIn_Add.do");
					mav.addObject("msg","출고수량이 잘못되었습니다.");
					return mav;
				}
			}
		}

		if(totalCnt != LogcInInfo.getDIL_QTY()) {
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","출고수량의 합이 입고수량과 맞지않습니다.");
			return mav;
		}

		if(totalCnt == 0) {
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","출고수량을 입력해주세요.");
			return mav;
		}
		int result = inOutService.insertBonbuIn(LogcInInfo.getDIL_DOCNO(), LogcInInfo.getDIL_MODEL(), OUTDATE, CENTERLIST, OUTCNTLIST, userService.getUserID(request));
		if(result > 0) {
			mav.setViewName("redirect:/BonbuIn_DETAIL.do");
			mav.addObject("DOCNO",DOCNO);
		}
		else {
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","저장실패! 다시 시도해 주세요.");
		}
		return mav;
	}
	
	@RequestMapping(value = "BonbuIn_DETAIL")
	public ModelAndView BonbuIn_DETAIL(HttpServletRequest request,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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
	
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		List<BonbuVo> BonbuInInfo = inOutService.getBonbuInInfo(LogcInInfo.getDIL_LOGC(), LogcInInfo.getDIL_DOCNO(), LogcInInfo.getDIL_MODEL());
		
		mav.setViewName("IO/Bonbu_DETAIL");
		mav.addObject("LogcInInfo", LogcInInfo);
		mav.addObject("BonbuInInfo", BonbuInInfo);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("level", UserInfo.getMUT_POSITION());
		return mav;
	}
	
	@RequestMapping(value = "BonbuIn_Modify")
	public ModelAndView BonbuIn_Modify(HttpServletRequest request,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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
	
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		List<BonbuVo> BonbuInInfo = inOutService.getBonbuInInfo(LogcInInfo.getDIL_LOGC(), LogcInInfo.getDIL_DOCNO(), LogcInInfo.getDIL_MODEL());
		
		mav.setViewName("IO/Bonbu_Modify");
		mav.addObject("LogcInInfo", LogcInInfo);
		mav.addObject("BonbuInInfo", BonbuInInfo);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "BonbuIn_Modify_Save")
	public ModelAndView BonbuIn_Modify_Save(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "OUTDATE", defaultValue = "") String OUTDATE,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "OUTCNT", defaultValue = "") String OUTCNT) {

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

		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		if(LogcInInfo.getSUS_CNT().equals("0"))
		{
			if(OUTDATE.length() == 0) {
				mav.setViewName("redirect:/BonbuIn_Modify.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","출고일자를 가져오지 못했습니다.");
				return mav;
			}

			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			try 
			{
				Date D_OUTDATE = transFormat.parse(OUTDATE);
				Date INDATE = transFormat.parse(LogcInInfo.getDIL_INDate());
				int compare = INDATE.compareTo(D_OUTDATE);
			    if ( compare > 0 )
			    {
			    	mav.setViewName("redirect:/BonbuIn_Modify.do");
					mav.addObject("DOCNO",DOCNO);
					mav.addObject("msg","출고일이 입고일보다 작습니다.");
					return mav;
			    }
			}
			catch (Exception e) {
				mav.setViewName("redirect:/BonbuIn_Modify.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","출고일자를 가져오지 못했습니다.");
				return mav;
			}
		}
		String[] CENTERLIST = CENTER.split(",");
		String[] OUTCNTLIST = OUTCNT.split(",");
		if(CENTERLIST.length != OUTCNTLIST.length)
		{
			mav.setViewName("redirect:/BonbuIn_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","본부 출고정보를 가져오지 못했습니다.");
			return mav;
		}

		int totalCnt = 0;
		for(int i = 0; i < OUTCNTLIST.length; ++i) {
			String TOUTCNT = OUTCNTLIST[i];
			int i_OUTCNT = 0;
			try {
				i_OUTCNT = Integer.parseInt(TOUTCNT);
				totalCnt += i_OUTCNT;
			}
			catch (Exception e) {
				mav.setViewName("redirect:/BonbuIn_Modify.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","출고수량이 잘못되었습니다.");
				return mav;
			}
		}

		if(totalCnt != LogcInInfo.getDIL_QTY()) {
			mav.setViewName("redirect:/BonbuIn_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고수량의 합이 입고수량과 맞지않습니다.");
			return mav;
		}

		if(totalCnt == 0) {
			mav.setViewName("redirect:/BonbuIn_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고수량을 입력해주세요.");
			return mav;
		}

		int result = inOutService.updateBonbuIn(DOCNO, CENTERLIST, OUTCNTLIST, userService.getUserID(request));
		if(result > 0) {
			mav.setViewName("redirect:/BonbuIn_DETAIL.do");
			mav.addObject("DOCNO",DOCNO);
			return mav;
		}
		else {
			mav.setViewName("redirect:/BonbuIn_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","수정실패! 다시 시도해주세요.");
			return mav;
		}
	}
	@RequestMapping(value = "BonbuIn_Delete")
	public ModelAndView BonbuIn_Delete(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO) {

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

		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}
		LogcInVo LogcInInfo = inOutService.getLogcInInfo(DOCNO);
		if(LogcInInfo == null)
		{
			mav.setViewName("redirect:/BonbuIn_Add.do");
			mav.addObject("msg","물류입고 정보를 가져오지 못했습니다.");
			return mav;
		}

		int result = inOutService.deleteBonbuIn(DOCNO, userService.getUserID(request));
		if(result > 0) {
			mav.setViewName("redirect:/BonbuInList.do");
			mav.addObject("DOCNO",DOCNO);
			return mav;
		}
		else {
			mav.setViewName("redirect:/BonbuIn_DETAIL.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","삭제실패! 다시 시도해주세요.");
			return mav;
		}
	}
	
	@RequestMapping(value = "OrderList")
	public ModelAndView OrderList(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		if(UserInfo.getMUT_POSITION().equals("003")) {
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		else if(UserInfo.getMUT_POSITION().equals("001") || UserInfo.getMUT_POSITION().equals("002")) {
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST", BONBULIST);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		List<OrderVo> OrderList = inOutService.getOrderList(StartDay, EndDay, LOGC, BONBU, CENTER, "", 1, 10);
		int listCnt = OrderList.size() == 0 ? 0 : OrderList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/ORDER_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("OrderList", OrderList);
		mav.addObject("OrderListSize", OrderList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		return mav;
	}
	
	@RequestMapping(value = "OrderList_search")
	public ModelAndView OrderList_search(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
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
		
		if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
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
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST", BONBULIST);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		List<OrderVo> OrderList = inOutService.getOrderList(StartDay, EndDay, LOGC, BONBU, CENTER, MNAME, PAGE, 10);
		int listCnt = OrderList.size() == 0 ? 0 : OrderList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/ORDER_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("OrderList", OrderList);
		mav.addObject("OrderListSize", OrderList.size());
		mav.addObject("pagination", common);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("MNAME", MNAME);
		mav.addObject("pageNum", PAGE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "Order_Add")
	public ModelAndView Order_Add(HttpServletRequest request, 
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
		
		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		if(UserInfo.getMUT_POSITION().equals("001")) {
			String LOGC = UserInfo.getMCI_LOGC();
			String BONBU = UserInfo.getMCI_Bonbu();
			String CENTER = UserInfo.getMCI_Center();
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST",BONBULIST);
			mav.addObject("CENTERLIST",CENTERLIST);
			mav.addObject("LOGC",LOGC);
			mav.addObject("BONBU",BONBU);
			mav.addObject("CENTER",CENTER);
		}
		
		mav.setViewName("IO/Order_Add");
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "Ajax_Order_search")
	public ModelAndView Ajax_Order_search(HttpServletRequest request, 
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
		
		List<ModelVo> ModelList = modelService.getModelList(LOGC, LCODE, MCODE, SCODE, MNAME, MCODENAME, SCODENAME, "Y", PAGE, 10);
		int listCnt = ModelList.size() == 0 ? 0 : ModelList.get(0).getTotalCnt();
		CommonService common  = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("ajax/Ajax_OrderSearch");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("ModelList", ModelList);
		mav.addObject("ModelListSize", ModelList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		return mav;
	}
	
	@RequestMapping(value = "Order_Add_Save")
	public ModelAndView Order_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "UESRID", defaultValue = "") String UESRID,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "ORDERDATE", defaultValue = "") String ORDERDATE,
			@RequestParam(value = "OrderQty", defaultValue = "0") String OrderQty,
			@RequestParam(value = "CMNT", defaultValue = "") String CMNT,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		UserVo UserInfo = userService.getUserInfo(UESRID);
		if(UserInfo == null) {
			mav.setViewName("redirect:/Order_Add.do");
			mav.addObject("msg","발주자 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(UESRID.length() == 0)
		{
			mav.setViewName("redirect:/Order_Add.do");
			mav.addObject("msg","발주자 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(MCODE.length() == 0)
		{
			mav.setViewName("redirect:/Order_Add.do");
			mav.addObject("msg","상품정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(ORDERDATE.length() == 0)
		{
			mav.setViewName("redirect:/Order_Add.do");
			mav.addObject("msg","발주일을 입력해주세요.");
			return mav;
		}
		
		if(OrderQty.length() == 0 || OrderQty.equals("0"))
		{
			mav.setViewName("redirect:/Order_Add.do");
			mav.addObject("msg","발주요청수량을 입력해주세요.");
			return mav;
		}
		
		int i_OrderQty = 0;
		try {
			i_OrderQty = Integer.parseInt(OrderQty);
		}
		catch (Exception e) {
			mav.setViewName("redirect:/Order_Add.do");
			mav.addObject("msg","발주요청수량이 잘못되었습니다.");
			return mav;
		}
		
		OrderVo orderVo = new OrderVo();
		String DBO_USER = UserInfo.getMUT_USERID();
		String DBO_CENTER = "";
		if(UserInfo.getMUT_POSITION().equals("001")) {
			if(LOGC.length() == 0 || BONBU.length() == 0 || CENTER.length() == 0) {
				mav.setViewName("redirect:/Order_Add.do");
				mav.addObject("msg","발주본부를 선택해주세요.");
				return mav;
			}
			CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
			if(centerVo == null) {
				mav.setViewName("redirect:/Order_Add.do");
				mav.addObject("msg","발주본부를 가져오지 못했습니다.");
				return mav;
			}
			DBO_CENTER = centerVo.getMCI_CODE();
		}
		else {
			DBO_CENTER = UserInfo.getMUT_CENTER();
		}
		
		int result = inOutService.insertOrder(orderVo, DBO_CENTER, DBO_USER, ORDERDATE, CMNT, MCODE, i_OrderQty, userService.getUserID(request));
		if(result < 0) {
			mav.setViewName("redirect:/Order_Add.do");
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
			return mav;
		}
		else {
			mav.setViewName("redirect:/Order_DETAIL.do");
			mav.addObject("DOCNO",orderVo.getDBO_DOCNO());
			return mav;
		}
	}
	@RequestMapping(value = "Order_DETAIL")
	public ModelAndView Order_DETAIL(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		OrderVo OrderInfo = inOutService.getOrderInfo(DOCNO);
		if(OrderInfo == null)
		{
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		mav.setViewName("IO/Order_DETAIL");
		mav.addObject("OrderInfo", OrderInfo);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("level", UserInfo.getMUT_POSITION());
		return mav;
	}
	
	@RequestMapping(value = "Order_Modify")
	public ModelAndView Order_Modify(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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

		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		OrderVo OrderInfo = inOutService.getOrderInfo(DOCNO);
		if(OrderInfo == null) {
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		mav.setViewName("IO/Order_Modify");
		mav.addObject("OrderInfo", OrderInfo);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "Order_Modify_Save")
	public ModelAndView Order_Modify_Save(HttpServletRequest request,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "ORDERDATE", defaultValue = "") String ORDERDATE,
			@RequestParam(value = "OrderQty", defaultValue = "0") String OrderQty,
			@RequestParam(value = "CMNT", defaultValue = "") String CMNT) {
		
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

		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		OrderVo OrderInfo = inOutService.getOrderInfo(DOCNO);
		if(OrderInfo == null) {
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(ORDERDATE.length() == 0)
		{
			mav.setViewName("redirect:/Order_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","발주일을 입력해주세요.");
			return mav;
		}
		
		if(OrderQty.length() == 0 || OrderQty.equals("0"))
		{
			mav.setViewName("redirect:/Order_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","발주요청수량을 입력해주세요.");
			return mav;
		}
		
		int i_OrderQty = 0;
		try {
			i_OrderQty = Integer.parseInt(OrderQty);
		}
		catch (Exception e) {
			mav.setViewName("redirect:/Order_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","발주요청수량이 잘못되었습니다.");
			return mav;
		}
		

		int result = inOutService.updateOrder(UserInfo.getMUT_POSITION(), DOCNO, ORDERDATE, i_OrderQty, CMNT, "Order_Modify", userService.getUserID(request));
		if(result <= 0) {
			mav.setViewName("redirect:/Order_Modify.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
			return mav;
		}
		else {
			mav.setViewName("redirect:/Order_DETAIL.do");
			mav.addObject("DOCNO",DOCNO);
			return mav;
		}
	}
	
	@RequestMapping(value = "Order_Delete")
	public ModelAndView Order_Delete(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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

		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("index");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		OrderVo OrderInfo = inOutService.getOrderInfo(DOCNO);
		if(OrderInfo == null) {
			mav.setViewName("redirect:/OrderList.do");
			mav.addObject("msg","발주정보를 가져오지 못했습니다.");
			return mav;
		}
		
		int result = inOutService.deleteOrder(UserInfo.getMUT_POSITION(), DOCNO);
		if(result <= 0) {
			mav.setViewName("redirect:/Order_DETAIL.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","삭제실패! 다시 시도해주세요.");
			return mav;
		}
		else {
			mav.setViewName("redirect:/OrderList.do");
			return mav;
		}
	}
	
	@RequestMapping(value = "Ajax_BonbuCheck")
	public ModelAndView Ajax_BonbuCheck(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO
			) {
		
		ModelAndView mav = new ModelAndView();
		
		if(DOCNO.length() > 0) {
			List<BonbuVo> BonbuInList = inOutService.getBonbuInCheck(DOCNO);
			mav.addObject("BonbuInList", BonbuInList);
			mav.addObject("BonbuInListSize", BonbuInList.size());
		}
		else {
			mav.addObject("BonbuInListSize", 0);
		}
		mav.setViewName("ajax/Ajax_BonbuCheck");
		return mav;
	}
	
	@RequestMapping(value = "BonbuMove_List")
	public ModelAndView BonbuMove_List(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		else if(UserInfo.getMUT_POSITION().equals("001") || UserInfo.getMUT_POSITION().equals("002")) {
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			mav.addObject("LOGCLIST", LOGCLIST);
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST", BONBULIST);
			
			List<CenterVo> INBONBULIST = centerService.getBONBUList(LOGC);
			mav.addObject("INBONBULIST", INBONBULIST);
			BONBU = "";
			CENTER = "";
		}
		CommonService common = new CommonService();
		String OutStartDay = common.StartDay();
		String OutEndDay = common.EndDay();
		String InStartDay = common.StartDay();
		String InEndDay = common.EndDay();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		List<MoveVo> MoveList = inOutService.getBonbuMOVEList(UserInfo, "", LOGC, BONBU, CENTER, "", "", "", OutStartDay, OutEndDay, InStartDay, InEndDay, "", 1, 10);
		int listCnt = MoveList.size() == 0 ? 0 : MoveList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		mav.setViewName("IO/BonbuMove_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("MoveList", MoveList);
		mav.addObject("MoveListSize", MoveList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("msg",msg);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("INLOGC", LOGC);
		mav.addObject("INBONBU", BONBU);
		mav.addObject("INCENTER", CENTER);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("OutStartDay", OutStartDay);
		mav.addObject("OutEndDay", OutEndDay);
		mav.addObject("InStartDay", InStartDay);
		mav.addObject("InEndDay", InEndDay);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_MOVEBONBU")
	public ModelAndView Ajax_MOVEBONBU(
			@RequestParam(value = "TYPE") String TYPE,
			@RequestParam(value = "LOGC") String LOGC,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		mav.setViewName("ajax/Ajax_MOVEBONBU");
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("type",TYPE);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_MOVECENTER")
	public ModelAndView Ajax_MOVECENTER(
			@RequestParam(value = "TYPE") String TYPE,
			@RequestParam(value = "LOGC") String LOGC,
			@RequestParam(value = "BONBU") String BONBU,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		mav.setViewName("ajax/Ajax_MOVECENTER");
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("type",TYPE);
		return mav;
	}
	
	@RequestMapping(value = "BonbuMove_List_Search")
	public ModelAndView BonbuMove_List_Search(HttpServletRequest request,
			@RequestParam(value = "OutStartDay", defaultValue = "") String OutStartDay,
			@RequestParam(value = "OutEndDay", defaultValue = "") String OutEndDay,
			@RequestParam(value = "InStartDay", defaultValue = "") String InStartDay,
			@RequestParam(value = "InEndDay", defaultValue = "") String InEndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "INLOGC", defaultValue = "") String INLOGC,
			@RequestParam(value = "INBONBU", defaultValue = "") String INBONBU,
			@RequestParam(value = "INCENTER", defaultValue = "") String INCENTER,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "CTYPE", defaultValue = "") String CTYPE,
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
		
		if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
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
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
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
		
		List<MoveVo> MoveList = inOutService.getBonbuMOVEList(UserInfo, CTYPE, LOGC, BONBU, CENTER, INLOGC, INBONBU, INCENTER, OutStartDay, OutEndDay, InStartDay, InEndDay, MNAME, PAGE, 10);
		int listCnt = MoveList.size() == 0 ? 0 : MoveList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/BonbuMove_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("MoveList", MoveList);
		mav.addObject("MoveListSize", MoveList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("OutStartDay", OutStartDay);
		mav.addObject("OutEndDay", OutEndDay);
		mav.addObject("InStartDay", InStartDay);
		mav.addObject("InEndDay", InEndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("INLOGC", INLOGC);
		mav.addObject("INBONBU", INBONBU);
		mav.addObject("INCENTER", INCENTER);
		mav.addObject("CTYPE", CTYPE);
		mav.addObject("MNAME", MNAME);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "BonbuMove_MODIFY")
	public ModelAndView BonbuMove_MODIFY(HttpServletRequest request,
			@RequestParam(value = "msg", defaultValue = "") String msg,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO) {
		
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
		
		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("redirect:/BonbuMove_List.do");
			mav.addObject("msg","권한이 없습니다.");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_List.do");
			mav.addObject("msg","본부별이동 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		MoveVo MoveInfo = inOutService.getBonbuMOVEInfo(DOCNO);
		if(MoveInfo == null)
		{
			mav.setViewName("redirect:/BonbuMove_List.do");
			mav.addObject("msg","본부별이동 정보를 가져오지 못했습니다.");
			return mav;
		}
		MoveVo JEGO = inOutService.getJegoModel(MoveInfo.getDBO_MODEL(), MoveInfo.getDBM_CENTER(), MoveInfo.getDBM_DATE());
		MoveInfo.setOUTJEGO(JEGO == null ? 0 : JEGO.getOUTJEGO());
		
		List<CenterVo> BONBULIST = centerService.getBONBUList(MoveInfo.getINLOGC());
		if(MoveInfo.getOUTCENTER() != null && MoveInfo.getOUTCENTER().length() > 0) {
			List<CenterVo> CENTERLIST = centerService.getCENTERList(MoveInfo.getINLOGC(), MoveInfo.getOUTBONBU());
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		String MOVECFM = "N";
		if(MoveInfo.getDBO_BisSts().equals("N") && MoveInfo.getDBM_Sts().equals("N")) {
			if(UserInfo.getMUT_POSITION().equals("001") || UserInfo.getMUT_POSITION().equals("002")) {
				MOVECFM = "Y";
			}
			else if(UserInfo.getMUT_POSITION().equals("003")){
				if(MoveInfo.getDBM_CFM().equals("Y") 
						&& MoveInfo.getOUTLOGC().equals(UserInfo.getMCI_LOGC())
						&& MoveInfo.getOUTBONBU().equals(UserInfo.getMCI_Bonbu())) {
					MOVECFM = "Y";
				}
			}
			else {
				if(MoveInfo.getDBM_CFM().equals("Y") 
						&& MoveInfo.getOUTLOGC().equals(UserInfo.getMCI_LOGC())
						&& MoveInfo.getOUTBONBU().equals(UserInfo.getMCI_Bonbu())
						&& MoveInfo.getOUTCENTER().equals(UserInfo.getMCI_Center())) {
					MOVECFM = "Y";
				}
			}
		}
		
		
		mav.setViewName("IO/BonbuMove_MODIFY");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("level", UserInfo.getMUT_POSITION());
		mav.addObject("BONBULIST", BONBULIST);
		mav.addObject("MoveInfo", MoveInfo);
		mav.addObject("MOVECFM", MOVECFM);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_InoutCenter")
	public ModelAndView Ajax_CENTER(@RequestParam(value = "LOGC") String LOGC,
									@RequestParam(value = "BONBU") String BONBU,
								  HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		mav.setViewName("ajax/Ajax_CENTER");
		mav.addObject("CENTERLIST",CENTERLIST);
		return mav;
	}
	
	@RequestMapping(value = "/Ajax_GETJEGO")
	public @ResponseBody String Ajax_GETJEGO(
									@RequestParam(value = "MODEL") String MODEL,
									@RequestParam(value = "LOGC") String LOGC,
									@RequestParam(value = "BONBU") String BONBU,
									@RequestParam(value = "CENTER") String CENTER,
									@RequestParam(value = "NOWDATE", defaultValue = "") String NOWDATE,
								  HttpServletRequest request){
		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo == null)
			return "-1";
		
		MoveVo MoveInfo = inOutService.getJegoModel(MODEL, centerVo.getMCI_CODE(), NOWDATE);
		if(MoveInfo == null)
			return "0";
		return String.valueOf(MoveInfo.getOUTJEGO());
	}
	
	@RequestMapping(value = "BonbuMove_CFMSAVE")
	public ModelAndView BonbuMove_CFMSAVE(HttpServletRequest request,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "DBM_DATE", defaultValue = "") String DBM_DATE,
			@RequestParam(value = "DBM_QTY", defaultValue = "") String DBM_QTY,
			@RequestParam(value = "DBM_HOWMOVE", defaultValue = "") String DBM_HOWMOVE,
			@RequestParam(value = "DBM_CMNT", defaultValue = "") String DBM_CMNT) {
		
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
		
		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("redirect:/BonbuMove_List.do");
			mav.addObject("msg","권한이 없습니다.");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_List.do");
			mav.addObject("msg","본부별이동 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		MoveVo MoveInfo = inOutService.getBonbuMOVEInfo(DOCNO);
		if(MoveInfo == null)
		{
			mav.setViewName("redirect:/BonbuMove_List.do");
			mav.addObject("msg","본부별이동 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(BONBU.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고정보를 가져오지 못했습니다.");
			return mav;
		}
		if(CENTER.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고정보를 가져오지 못했습니다.");
			return mav;
		}
		if(DBM_DATE.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고일를 가져오지 못했습니다.");
			return mav;
		}
		
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			Date OUTDATE = transFormat.parse(DBM_DATE);
			Date INDATE = transFormat.parse(MoveInfo.getDBO_DATE());
			int compare = INDATE.compareTo(OUTDATE);
		    if ( compare > 0 )
		    {
		    	mav.setViewName("redirect:/BonbuMove_MODIFY.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","출고일이 발주일보다 작습니다.");
				return mav;
		    }
		}
		catch (Exception e) {
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","발주일 또는 출고일이 잘못되었습니다.");
			return mav;
		}
		
		int i_DBM_QTY = 0; 
		try {
			i_DBM_QTY = Integer.parseInt(DBM_QTY);
		}
		catch (Exception e) {
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고수량이 잘못되었습니다.");
			return mav;
		}
		
		if(i_DBM_QTY <= 0) {
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고수량을 지정해주세요.");
			return mav;
		}
		
		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo == null)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고 본부를 가져오지 못했습니다.");
			return mav;
		}
		if(centerVo.getMCI_CODE().equals(MoveInfo.getDBO_CENTER())) {
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고 본부와 입고 본부가 같습니다.");
			return mav;
		}
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("NOWJEGO", 0);
		map.put("OUTJEGO", 0);
		map.put("OUTQTY", 0);
		int result = inOutService.getJegoChk(MoveInfo.getDBO_MODEL(), centerVo.getMCI_CODE(), DBM_DATE, i_DBM_QTY, map);
		mav.setViewName("redirect:/BonbuMove_MODIFY.do");
		mav.addObject("DOCNO",DOCNO);
		if(result == -1) {
			mav.addObject("msg","적정재고 확인에 실패하였습니다.");
			return mav;
		}
		else if(result == 0) {
			mav.addObject("msg","출고 수량을 확인해주세요. 현 재고=" + map.get("NOWJEGO") + " 출고일 기준 재고=" + map.get("OUTJEGO")  + " 출고수량=" + i_DBM_QTY);
			return mav;
		}
		
		result = inOutService.updateMoveCFM(DOCNO, centerVo.getMCI_CODE(), DBM_DATE, DBM_QTY, DBM_HOWMOVE, DBM_CMNT, "BonbuMove_CFM", userService.getUserID(request));
		mav.setViewName("redirect:/BonbuMove_MODIFY.do");
		mav.addObject("DOCNO",DOCNO);
		if(result <= 0) {
			mav.addObject("msg","업데이트 실패! 다시 시도해주세요.");
		}
		return mav;
	}
	
	@RequestMapping(value = "BonbuMove_MODIFY_Save")
	public ModelAndView BonbuMove_MODIFY_Save(HttpServletRequest request,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "DBM_DATE", defaultValue = "") String DBM_DATE,
			@RequestParam(value = "DBM_QTY", defaultValue = "") String DBM_QTY,
			@RequestParam(value = "DBM_HOWMOVE", defaultValue = "") String DBM_HOWMOVE,
			@RequestParam(value = "DBM_CMNT", defaultValue = "") String DBM_CMNT) {
		
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
		
		if(UserInfo.getMUT_POSITION().equals("005")) {
			mav.setViewName("redirect:/BonbuMove_List.do");
			mav.addObject("msg","권한이 없습니다.");
			return mav;
		}
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("msg","본부별이동 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		MoveVo MoveInfo = inOutService.getBonbuMOVEInfo(DOCNO);
		if(MoveInfo == null)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("msg","본부별이동 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		int i_DBM_QTY = 0;
		if(UserInfo.getMUT_POSITION().equals("003") || UserInfo.getMUT_POSITION().equals("004")) {
			if(MoveInfo.getDBM_CFM().equals("N")) {
				mav.setViewName("redirect:/BonbuMove_MODIFY.do");
				mav.addObject("msg","담당자 확인이 되지 않았습니다.");
				return mav;
			}
			LOGC = MoveInfo.getINLOGC();
			BONBU = MoveInfo.getOUTBONBU();
			CENTER = MoveInfo.getOUTCENTER();
			DBM_DATE = MoveInfo.getDBM_DATE();
			i_DBM_QTY = MoveInfo.getDBM_QTY();
		}
		else {
			try {
				i_DBM_QTY = Integer.parseInt(DBM_QTY);
			}
			catch (Exception e) {
				mav.setViewName("redirect:/BonbuMove_MODIFY.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","출고수량을 가져오지 못했습니다.");
				return mav;
			}
		}
		
		if(LOGC.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(BONBU.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고정보를 가져오지 못했습니다.");
			return mav;
		}

		if(CENTER.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(DBM_DATE.length() == 0)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고일를 가져오지 못했습니다.");
			return mav;
		}
		
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			Date OUTDATE = transFormat.parse(DBM_DATE);
			Date INDATE = transFormat.parse(MoveInfo.getDBO_DATE());
			int compare = INDATE.compareTo(OUTDATE);
		    if ( compare > 0 )
		    {
		    	mav.setViewName("redirect:/BonbuMove_MODIFY.do");
				mav.addObject("DOCNO",DOCNO);
				mav.addObject("msg","출고일이 발주일보다 작습니다.");
				return mav;
		    }
		}
		catch (Exception e) {
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","발주일 또는 출고일이 잘못되었습니다.");
			return mav;
		}
		
		if(i_DBM_QTY <= 0) {
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고수량을 지정해주세요.");
			return mav;
		}
		
		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo == null)
		{
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고 본부를 가져오지 못했습니다.");
			return mav;
		}
		
		if(centerVo.getMCI_CODE().equals(MoveInfo.getDBO_CENTER())) {
			mav.setViewName("redirect:/BonbuMove_MODIFY.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("msg","출고 본부와 입고 본부가 같습니다.");
			return mav;
		}
		
		int result = inOutService.updateMove(DOCNO, userService.getUserID(request), DBM_HOWMOVE, DBM_CMNT, "BonbuMove_Save", MoveInfo.getDBO_MODEL(), DBM_DATE, i_DBM_QTY, centerVo.getMCI_CODE(), MoveInfo.getDBM_CFM());
		mav.setViewName("redirect:/BonbuMove_MODIFY.do");
		mav.addObject("DOCNO",DOCNO);
		if(result <= 0) {
			mav.addObject("msg","업데이트 실패! 재고를 다시 확인해주세요.");
		}
		return mav;
	}
	@RequestMapping(value="Ajax_IsJego", produces="application/text; charset=utf8")
	public @ResponseBody String Ajax_IsJego(
			@RequestParam(value = "MODEL") String MODEL,
			@RequestParam(value = "LOGC") String LOGC,
			@RequestParam(value = "BONBU") String BONBU,
			@RequestParam(value = "CENTER") String CENTER,
			@RequestParam(value = "NOWDATE") String NOWDATE,
			@RequestParam(value = "DBM_QTY") String DBM_QTY,
		  HttpServletRequest request) {
		
		if(LOGC.length() == 0)
		{
			return "-1";
		}
		
		if(BONBU.length() == 0)
		{
			return "-1";
		}

		if(CENTER.length() == 0)
		{
			return "-1";
		}
		
		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo == null)
		{
			return "-1";
		}
		
		int i_DBM_QTY = 0;
		try {
			i_DBM_QTY = Integer.parseInt(DBM_QTY);
		}
		catch (Exception e) {
			return "-2";
		}
		if(i_DBM_QTY <= 0) {
			return "-2";
		}
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("NOWJEGO", 0);
		map.put("OUTJEGO", 0);
		map.put("OUTQTY", 0);
		int result = inOutService.getJegoChk(MODEL, centerVo.getMCI_CODE(), NOWDATE, i_DBM_QTY, map);
		if(result == -1) {
			return "-3";
		}
		String msg = "현 재고=" + map.get("NOWJEGO") + " 출고일 기준 재고=" + map.get("OUTJEGO")  + " 출고수량=" + i_DBM_QTY;
		return msg;
	}
	
	@RequestMapping(value = "Cfm_List")
	public ModelAndView Cfm_List(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		else if(UserInfo.getMUT_POSITION().equals("001") || UserInfo.getMUT_POSITION().equals("002")) {
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST", BONBULIST);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		List<CfmVo> CFMLIST = inOutService.getInCFMList(StartDay, EndDay, LOGC, BONBU, CENTER, "", "", "", 1, 10);
		int listCnt = CFMLIST.size() == 0 ? 0 : CFMLIST.get(0).getTotalCnt();
		
		common.SetPaging(listCnt, 1);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		mav.setViewName("IO/CFM_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CFMLIST", CFMLIST);
		mav.addObject("CFMLISTSize", CFMLIST.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("msg",msg);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		return mav;
	}
	
	@RequestMapping(value = "Cfm_List_Search")
	public ModelAndView Cfm_List_Search(HttpServletRequest request,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "INSTS", defaultValue = "") String INSTS,
			@RequestParam(value = "CFMSTS", defaultValue = "") String CFMSTS,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
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
		
		if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
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
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST", BONBULIST);
			mav.addObject("CENTERLIST", CENTERLIST);
		}
		
		List<CfmVo> CFMLIST = inOutService.getInCFMList(StartDay, EndDay, LOGC, BONBU, CENTER, MNAME, INSTS, CFMSTS, PAGE, 10);
		int listCnt = CFMLIST.size() == 0 ? 0 : CFMLIST.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/CFM_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CFMLIST", CFMLIST);
		mav.addObject("CFMLISTSize", CFMLIST.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("INSTS", INSTS);
		mav.addObject("CFMSTS", CFMSTS);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("MNAME", MNAME);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "CFM_ADD")
	public ModelAndView CFM_ADD(HttpServletRequest request,
			@RequestParam(value = "INTYPE", defaultValue = "") String INTYPE,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "msg", defaultValue = "") String msg,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "INSTS", defaultValue = "") String INSTS,
			@RequestParam(value = "CFMSTS", defaultValue = "") String CFMSTS,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
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
		
		CfmVo CFMINFO = null;
		try {
			if(INTYPE.equals("0"))
				CFMINFO = inOutService.getInCFMLogcInfo(DOCNO);
			else
				CFMINFO = inOutService.getInCFMBonbuInfo(DOCNO);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		if(CFMINFO == null) {
			mav.setViewName("redirect:/Cfm_List.do");
			mav.addObject("msg","입고승인정보를 가져오지 못했습니다.");
			mav.addObject("PAGE", PAGE);
			mav.addObject("LOGC", LOGC);
			mav.addObject("BONBU", BONBU);
			mav.addObject("CENTER", CENTER);
			mav.addObject("INSTS", INSTS);
			mav.addObject("CFMSTS", CFMSTS);
			mav.addObject("StartDay", StartDay);
			mav.addObject("EndDay", EndDay);
			mav.addObject("MNAME", MNAME);
			return mav;
		}
		
		List<NoticeVo> FileList = noticeService.getNoticeFileList(DOCNO);
		if(FileList != null) {
			mav.addObject("FileList",FileList);
		}
		else {
			msg += msg.length() > 0 ? " , 첨부파일 목록을 가져오지 못했습니다." : ""; 
		}
		
		mav.setViewName("IO/CFM_ADD");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("CFMINFO", CFMINFO);
		mav.addObject("INTYPE", INTYPE);
		mav.addObject("FileList",FileList);
		mav.addObject("msg",msg);
		mav.addObject("PAGE", PAGE);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("INSTS", INSTS);
		mav.addObject("CFMSTS", CFMSTS);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("MNAME", MNAME);
		return mav;
	}
	
	@RequestMapping(value = "CFM_ADD_Save")
	public ModelAndView CFM_ADD_Save(HttpServletRequest request,
			@RequestParam(value = "INTYPE", defaultValue = "") String INTYPE,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "DIC_DATE", defaultValue = "") String DIC_DATE,
			@RequestParam(value = "DIC_CMNT", defaultValue = "") String DIC_CMNT,
			@RequestParam(value = "CFMsts", defaultValue = "") String CFMsts,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "INSTS", defaultValue = "") String INSTS,
			@RequestParam(value = "CFMSTS", defaultValue = "") String CFMSTS,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE,
			@RequestParam(value="Addfile", required = false) List<MultipartFile> fileList
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
		
		if(!CFMsts.equals("N") && !CFMsts.equals("Y"))
		{
			mav.setViewName("redirect:/CFM_ADD.do");
			mav.addObject("DOCNO",DOCNO);
			mav.addObject("INTYPE",INTYPE);
			mav.addObject("msg","입고승인 정보를 가져오지 못했습니다.");
			mav.addObject("PAGE", PAGE);
			mav.addObject("LOGC", LOGC);
			mav.addObject("BONBU", BONBU);
			mav.addObject("CENTER", CENTER);
			mav.addObject("INSTS", INSTS);
			mav.addObject("CFMSTS", CFMSTS);
			mav.addObject("StartDay", StartDay);
			mav.addObject("EndDay", EndDay);
			mav.addObject("MNAME", MNAME);
			return mav;
		}
		
		int result = inOutService.updateCfm(CFMsts, DOCNO, INTYPE, userService.getUserID(request), DIC_DATE, DIC_CMNT, "BonbuCfm_PASS", fileList, request);
		mav.setViewName("redirect:/CFM_ADD.do");
		mav.addObject("DOCNO",DOCNO);
		mav.addObject("INTYPE",INTYPE);
		if(result == -1) {
			mav.addObject("msg","저장실패! 다시 시도해주세요.");
		}
		else if(result == -2) {
			mav.addObject("msg","일부 파일 저장에 실패하였습니다.");
		}
		mav.addObject("PAGE", PAGE);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("INSTS", INSTS);
		mav.addObject("CFMSTS", CFMSTS);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("MNAME", MNAME);
		return mav;
	}
	
	@RequestMapping(value = "OtherList")
	public ModelAndView OtherList(HttpServletRequest request, @RequestParam(value = "msg", defaultValue = "") String msg) {
		
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
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		List<OtherVo> OtherList = inOutService.getOtherList(StartDay, EndDay, "", "", "", "", 1, 10);
		int listCnt = OtherList.size() == 0 ? 0 : OtherList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/OTHER_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("OtherList", OtherList);
		mav.addObject("OtherListSize", OtherList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", "1");
		mav.addObject("msg",msg);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		return mav;
	}
	
	@RequestMapping(value = "OtherList_search")
	public ModelAndView OtherList_search(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
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
		
		if(!UserInfo.getMUT_POSITION().equals("001")) {
			mav.setViewName("index");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		List<OtherVo> OtherList = inOutService.getOtherList(StartDay, EndDay, LOGC, BONBU, CENTER, MNAME, PAGE, 10);
		int listCnt = OtherList.size() == 0 ? 0 : OtherList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("IO/OTHER_LIST");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("OtherList", OtherList);
		mav.addObject("OtherListSize", OtherList.size());
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("pagination", common);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("MNAME", MNAME);
		mav.addObject("pageNum", PAGE);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "Other_Add")
	public ModelAndView Other_Add(HttpServletRequest request, 
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
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		
		mav.setViewName("IO/Other_Add");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST", BONBULIST);
		mav.addObject("CENTERLIST", CENTERLIST);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "Other_Add_Save")
	public ModelAndView Other_Add_Save(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "OUTDATE", defaultValue = "") String OUTDATE,
			@RequestParam(value = "QTY", defaultValue = "0") String QTY,
			@RequestParam(value = "OUTCMNT", defaultValue = "") String OUTCMNT,
			@RequestParam(value = "CMNT", defaultValue = "") String CMNT,
			@RequestParam(value="Addfile", required = false) List<MultipartFile> fileList,
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
		
		if(LOGC.length() == 0 || BONBU.length() == 0 || CENTER.length() == 0)
		{
			mav.setViewName("redirect:/Other_Add.do");
			mav.addObject("msg","출고본부 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo == null)
		{
			mav.setViewName("redirect:/Other_Add.do");
			mav.addObject("msg","출고본부 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(MCODE.length() == 0)
		{
			mav.setViewName("redirect:/Other_Add.do");
			mav.addObject("msg","상품코드 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		if(OUTDATE.length() == 0)
		{
			mav.setViewName("redirect:/Other_Add.do");
			mav.addObject("msg","출고일을 가져오지 못했습니다.");
			return mav;
		}
		
		if(QTY.length() == 0 || QTY.equals("0"))
		{
			mav.setViewName("redirect:/Other_Add.do");
			mav.addObject("msg","출고수량을 가져오지 못했습니다.");
			return mav;
		}
		int i_QTY = 0;
		try {
			i_QTY = Integer.parseInt(QTY);
		}
		catch (Exception e) {
			mav.setViewName("redirect:/Other_Add.do");
			mav.addObject("msg","출고수량이 잘못되었습니다.");
			return mav;
		}
		if(i_QTY == 0)
		{
			mav.setViewName("redirect:/Other_Add.do");
			mav.addObject("msg","출고수량을 가져오지 못했습니다.");
			return mav;
		}
		
		OtherVo vo = new OtherVo();
		int result = inOutService.insertOther(vo, centerVo.getMCI_CODE(), MCODE, OUTDATE, i_QTY, OUTCMNT, CMNT, userService.getUserID(request), "Other_Add", fileList, request);
		
		if(result <= 0) {
			mav.setViewName("redirect:/Other_Add.do");
			if(result == -2)
				mav.addObject("msg","첨부파일 업로드 실패!");
			else
				mav.addObject("msg","저장실패! 다시 시도해주세요.");
		}
		else {
			mav.setViewName("redirect:/Other_DEAIL.do");
			mav.addObject("DOCNO",vo.getDOO_DOCNO());
		}
		return mav;
	}
	
	@RequestMapping(value = "Other_DEAIL")
	public ModelAndView Other_Add(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
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
		
		if(DOCNO.length() == 0)
		{
			mav.setViewName("redirect:/OtherList.do");
			mav.addObject("msg","기타출고 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		OtherVo OtherInfo = inOutService.getOtherInfo(DOCNO);
		if(OtherInfo == null)
		{
			mav.setViewName("redirect:/OtherList.do");
			mav.addObject("msg","기타출고 정보를 가져오지 못했습니다.");
			return mav;
		}
		
		List<NoticeVo> FileList = noticeService.getNoticeFileList(DOCNO);
		if(FileList != null) {
			mav.addObject("FileList",FileList);
		}
		else {
			msg += msg.length() > 0 ? " , 첨부파일 목록을 가져오지 못했습니다." : ""; 
		}
		
		mav.setViewName("IO/Other_DEAIL");
		mav.addObject("OtherInfo", OtherInfo);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("FileList",FileList);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "InOutReport")
	public ModelAndView InOutReport(HttpServletRequest request, 
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
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		String LOGC = UserInfo.getMCI_LOGC();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		mav.setViewName("Report/InOutReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("YEAR", year);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("LOGC",LOGC);
		mav.addObject("UserInfo", UserInfo);
		return mav;
	}
	
	@RequestMapping(value = "InOutReport_Search")
	public ModelAndView InOutReport_Search(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "YEAR", defaultValue = "0") int YEAR,
			@RequestParam(value = "INTYPE", defaultValue = "0") int INTYPE,
			@RequestParam(value = "CTYPE", defaultValue = "1") int CTYPE
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
		
		String BONBU = "";
		String CENTER = "";
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		else {
			if(LOGC.length() == 0)
			{
				mav.setViewName("redirect:/InOutReport.do");
				mav.addObject("msg","물류센터를 선택해주세요.");
				return mav;
			}
		}
		
		if(MCODE.length() == 0)
		{
			mav.setViewName("redirect:/InOutReport.do");
			mav.addObject("msg","상품코드를 추가해주세요.");
			return mav;
		}
		
		if(YEAR == 0)
		{
			mav.setViewName("redirect:/InOutReport.do");
			mav.addObject("msg","연도를 입력해주세요.");
			return mav;
		}
		if(YEAR < 1753 || YEAR > 9999) {
			mav.setViewName("redirect:/InOutReport.do");
			mav.addObject("msg","올바른 연도를 입력해주세요.");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		ModelVo MODELINFO = modelService.getModelInfo(MCODE);
		int result = 0;
		if(INTYPE == 0 || INTYPE == 1) {
			List<InoutReportVo> INLIST = new ArrayList<InoutReportVo>();
			result = inOutService.getInReport(LOGC, BONBU, CENTER, UserInfo.getMUT_POSITION(), INLIST, MCODE, YEAR);
			mav.addObject("INLIST",INLIST);
			
			if(result <= 0) {
				mav.setViewName("redirect:/InOutReport.do");
				mav.addObject("msg","목록을 가져오지 못했습니다.");
				return mav;
			}
		}
		if(INTYPE == 0 || INTYPE == 2) {
			List<InoutReportVo> OUTLIST = new ArrayList<InoutReportVo>();
			result = inOutService.getOutReport(LOGC, BONBU, CENTER, UserInfo.getMUT_POSITION(), OUTLIST, MCODE, YEAR);
			mav.addObject("OUTLIST",OUTLIST);
			
			if(result <= 0) {
				mav.setViewName("redirect:/InOutReport.do");
				mav.addObject("msg","목록을 가져오지 못했습니다.");
				return mav;
			}
		}
		
		mav.setViewName("Report/InOutReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("MODELINFO",MODELINFO);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("YEAR",YEAR);
		mav.addObject("INTYPE",INTYPE);
		mav.addObject("CTYPE",CTYPE);
		mav.addObject("LOGC",LOGC);
		mav.addObject("UserInfo", UserInfo);
		return mav;
	}
	
	@RequestMapping(value = "JegoReport")
	public ModelAndView JegoReport(HttpServletRequest request, 
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
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		String LOGC = UserInfo.getMCI_LOGC();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String NowDay = sdf.format(cal.getTime());
		mav.setViewName("Report/JegoReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("NowDay", NowDay);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LOGC",LOGC);
		return mav;
	}
	
	@RequestMapping(value = "JegoReport_Search")
	public ModelAndView JegoReport_Search(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "NowDay", defaultValue = "0") String NowDay,
			@RequestParam(value = "CTYPE", defaultValue = "1") int CTYPE
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
		
		String BONBU = "";
		String CENTER = "";
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		else {
			if(LOGC.length() == 0)
			{
				mav.setViewName("redirect:/JegoReport.do");
				mav.addObject("msg","물류센터를 선택해주세요.");
				return mav;
			}
		}
		
		if(MCODE.length() == 0)
		{
			mav.setViewName("redirect:/JegoReport.do");
			mav.addObject("msg","상품코드를 추가해주세요.");
			return mav;
		}
		String pDay = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			String TempDay = cal.get(Calendar.YEAR) + "-12-31";
			Date D_TempDay = sdf.parse(TempDay);
			Calendar tempc = Calendar.getInstance();
			tempc.setTime(D_TempDay);
			tempc.add(Calendar.YEAR, -1);
			pDay = sdf.format(tempc.getTime());
			mav.addObject("pDay",pDay);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String StartDay = "";
		String EndDay = "";
		int MONTH = 0;
		try 
		{
			if(NowDay.length() == 0)
			{
				NowDay = sdf.format(cal.getTime());
			}
			int YEAR = cal.get(Calendar.YEAR);
			StartDay = YEAR + "-01-01";
			
			Date D_NowDay = sdf.parse(NowDay);
			cal.setTime(D_NowDay);
	        int LastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), LastDay); //월은 -1해줘야 해당월로 인식
	        EndDay = sdf.format(cal.getTime());
	        MONTH = cal.get(Calendar.MONTH) + 1;
		}
		catch (Exception e) {
			mav.setViewName("redirect:/JegoReport.do");
			mav.addObject("msg","일자를 가져오지 못했습니다.");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		ModelVo MODELINFO = modelService.getModelInfo(MCODE);
		int result = 0;
		List<JegoVo> JEGO_LIST = new ArrayList<JegoVo>();
		result = inOutService.getNowJegoReport(pDay, StartDay, EndDay, MONTH, LOGC, BONBU, CENTER, JEGO_LIST, MCODE, NowDay, UserInfo.getMUT_POSITION());
		mav.addObject("JEGO_LIST",JEGO_LIST);
		if(result <= 0) {
			mav.setViewName("redirect:/JegoReport.do");
			mav.addObject("msg","목록을 가져오지 못했습니다.");
			return mav;
		}
		mav.setViewName("Report/JegoReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("MODELINFO",MODELINFO);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("NowDay",NowDay);
		mav.addObject("CTYPE",CTYPE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LOGC",LOGC);
		mav.addObject("MONTH", MONTH);
		return mav;
	}
	
	
	@RequestMapping(value = "InOutListReport")
	public ModelAndView InOutListReport(HttpServletRequest request, 
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		List<InOutListVo> GTYPELIST = inOutService.getGTYPELIST();
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		List<InOutListVo> INOUTLIST = inOutService.getInOutListReport(StartDay, EndDay, LOGC, BONBU, CENTER, "", 0, "", 1, 10);
		int listCnt = INOUTLIST.size() == 0 ? 0 : INOUTLIST.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		mav.addObject("StartDay2",sdf.format(cal.getTime()));
		
		mav.setViewName("Report/InOutListReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("INOUTLIST", INOUTLIST);
		mav.addObject("INOUTLISTSize", INOUTLIST.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", 1);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("GTYPELIST",GTYPELIST);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		return mav;
	}
	
	@RequestMapping(value = "InOutListReport_Search")
	public ModelAndView InOutListReport_Search(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "IOTYPE", defaultValue = "0") int IOTYPE,
			@RequestParam(value = "EVENT", defaultValue = "") String EVENT,
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
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		List<InOutListVo> INOUTLIST = inOutService.getInOutListReport(StartDay, EndDay, LOGC, BONBU, CENTER, MNAME, IOTYPE, EVENT, PAGE, 10);
		int listCnt = INOUTLIST.size() == 0 ? 0 : INOUTLIST.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		mav.addObject("StartDay2",sdf.format(cal.getTime()));
		
		List<InOutListVo> GTYPELIST = inOutService.getGTYPELIST();
		mav.setViewName("Report/InOutListReport");
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("StartDay",StartDay);
		mav.addObject("EndDay",EndDay);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("MNAME",MNAME);
		mav.addObject("IOTYPE",IOTYPE);
		mav.addObject("EVENT",EVENT);
		mav.addObject("INOUTLIST", INOUTLIST);
		mav.addObject("INOUTLISTSize", INOUTLIST.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("GTYPELIST", GTYPELIST);
		return mav;
	}
	
	@RequestMapping(value = "Ajax_Event_search")
	public ModelAndView Ajax_Event_search(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "EUSERID", defaultValue = "") String EUSERID,
			@RequestParam(value = "EUSERNAME", defaultValue = "") String EUSERNAME,
			@RequestParam(value = "GNAME", defaultValue = "") String GNAME,
			@RequestParam(value = "JNAME", defaultValue = "") String JNAME,
			@RequestParam(value = "ESTATE", defaultValue = "") String ESTATE,
			@RequestParam(value = "GTYPE", defaultValue = "") String GTYPE,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE
			) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("ajax/Ajax_EventSearch");
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.addObject("EVENTLISTSize", 0);
			return mav;
		}
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			BONBU = UserInfo.getMCI_BonbuNAME();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			BONBU = UserInfo.getMCI_BonbuNAME();
			CENTER = UserInfo.getMCI_CenterName();
		}
		
		StartDay = StartDay.replaceAll("-", "");
		EndDay = EndDay.replaceAll("-", "");
		
		List<InOutListVo> EVENTLIST = inOutService.getEventInfo(StartDay, EndDay, BONBU, CENTER, EUSERID, EUSERNAME, GNAME, JNAME, ESTATE, GTYPE, PAGE, 10);
		int listCnt = EVENTLIST.size() == 0 ? 0 : EVENTLIST.get(0).getTotalCnt();
		CommonService common  = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.addObject("EVENTLIST", EVENTLIST);
		mav.addObject("EVENTLISTSize", EVENTLIST.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		return mav;
	}
	
	@RequestMapping(value = "BonbuStatusReport")
	public ModelAndView BonbuStatusReport(HttpServletRequest request, 
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
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		
		int listCnt = 0;
		CommonService common = new CommonService();
		common.SetPaging(listCnt, 1);
		
		Calendar cal = Calendar.getInstance();
		try {
			mav.addObject("YEAR",cal.get(Calendar.YEAR));
			mav.addObject("MONTH",cal.get(Calendar.MONTH) + 1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("Report/BonbuStatusReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("BonbuStatusListSize", 0);
		mav.addObject("pagination", common);
		mav.addObject("pageNum", 1);
		mav.addObject("PSIZE",10);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "BonbuStatusReport_Search")
	public ModelAndView BonbuStatusReport_Search(HttpServletRequest request, 
			@RequestParam(value = "YEAR", defaultValue = "0") int YEAR,
			@RequestParam(value = "MONTH", defaultValue = "0") int MONTH,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "LCODEList", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODEList", defaultValue = "") String MCODE,
			@RequestParam(value = "SCODEList", defaultValue = "") String SCODE,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "MCODENAME", defaultValue = "") String MCODENAME,
			@RequestParam(value = "SCODENAME", defaultValue = "") String SCODENAME,
			@RequestParam(value = "PTYPE", defaultValue = "1") int PTYPE,
			@RequestParam(value = "PSIZE", defaultValue = "10") int PSIZE,
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
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			mav.addObject("CENTERLIST",CENTERLIST);
			
			if(CENTER.length() == 0)
			{
				mav.setViewName("redirect:/BonbuStatusReport.do");
				mav.addObject("msg","센터를 선택해주세요.");
				return mav;
			}
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		else {
			List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
			List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
			List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
			if(LOGC.length() == 0)
			{
				mav.setViewName("redirect:/BonbuStatusReport.do");
				mav.addObject("msg","물류센터를 선택해주세요.");
				return mav;
			}
			if(BONBU.length() == 0)
			{
				mav.setViewName("redirect:/BonbuStatusReport.do");
				mav.addObject("msg","본부를 선택해주세요.");
				return mav;
			}
			if(CENTER.length() == 0)
			{
				mav.setViewName("redirect:/BonbuStatusReport.do");
				mav.addObject("msg","센터를 선택해주세요.");
				return mav;
			}
			mav.addObject("LOGCLIST", LOGCLIST);
			mav.addObject("BONBULIST",BONBULIST);
			mav.addObject("CENTERLIST",CENTERLIST);
		}
		
		if(YEAR == 0)
		{
			mav.setViewName("redirect:/BonbuStatusReport.do");
			mav.addObject("msg","연도를 입력해주세요.");
			return mav;
		}
		if(YEAR < 1753 || YEAR > 9999) {
			mav.setViewName("redirect:/BonbuStatusReport.do");
			mav.addObject("msg","올바른 연도를 입력해주세요.");
			return mav;
		}
		
		if(MONTH == 0)
		{
			mav.setViewName("redirect:/BonbuStatusReport.do");
			mav.addObject("msg","올바른 날짜를 입력해주세요.");
			return mav;
		}
		if(MONTH < 1 || MONTH > 12) {
			mav.setViewName("redirect:/BonbuStatusReport.do");
			mav.addObject("msg","올바른 날짜를 입력해주세요.");
			return mav;
		}
		CenterVo centerVo = centerService.getCENTERInfo_Detail(LOGC, BONBU, CENTER);
		if(centerVo == null)
		{
			mav.setViewName("redirect:/BonbuStatusReport.do");
			mav.addObject("msg","본부정보를 가져오지 못했습니다.");
			return mav;
		}
		List<BonbuStatusVo> BonbuStatusList = inOutService.getBonbuStatusReport(YEAR, MONTH, LOGC, centerVo.getMCI_CODE(), LCODE, MCODE, SCODE, MNAME, MCODENAME, SCODENAME, PTYPE, PAGE, PSIZE);
		if(PTYPE == 1) {
			int listCnt = BonbuStatusList.size() == 0 ? 0 : BonbuStatusList.get(0).getTotalCnt();
			CommonService common = new CommonService();
			common.SetPaging(listCnt, PAGE, PSIZE);
			mav.addObject("pagination", common);
			mav.addObject("pageNum", PAGE);
		}
		else {
			mav.addObject("pageNum", 1);
		}
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		List<ModelVo> MCODELIST = modelService.MgroupList(LOGC, LCODE);
		List<ModelVo> SCODELIST = modelService.SgroupList(LOGC, LCODE, MCODE);
		
		mav.setViewName("Report/BonbuStatusReport");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("YEAR",YEAR);
		mav.addObject("MONTH",MONTH);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("PTYPE",PTYPE);
		mav.addObject("PSIZE",PSIZE);
		mav.addObject("CenterInfo",centerVo);
		mav.addObject("BonbuStatusList", BonbuStatusList);
		mav.addObject("BonbuStatusListSize", BonbuStatusList.size());
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("MCODELIST", MCODELIST);
		mav.addObject("SCODELIST", SCODELIST);
		mav.addObject("LCODE", LCODE);
		mav.addObject("MCODE", MCODE);
		mav.addObject("SCODE", SCODE);
		mav.addObject("MNAME", MNAME);
		mav.addObject("MCODENAME", MCODENAME);
		mav.addObject("SCODENAME", SCODENAME);
		return mav;
	}
	
	@RequestMapping(value = "EventReturn")
	public ModelAndView EventReturn(HttpServletRequest request, 
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		CommonService common = new CommonService();
		String StartDay = common.StartDay();
		String EndDay = common.EndDay();
		List<EventVo> EeventReturnList = null;
		try {
			EeventReturnList = inOutService.getEventReturn(StartDay, EndDay, LOGC, BONBU, CENTER, "", "", 1, 10);
		} catch (Exception e) {
			msg = "정보를 가져오지 못했습니다. 다시 시도해주세요.";
		}
		int listCnt = EeventReturnList == null || EeventReturnList.size() == 0 ? 0 : EeventReturnList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		
		List<InOutListVo> GTYPELIST = inOutService.getGTYPELIST();
		
		mav.setViewName("Report/EventReturn");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("EeventReturnList", EeventReturnList);
		mav.addObject("EeventReturnSize", EeventReturnList == null || EeventReturnList.size() == 0 ? 0 : EeventReturnList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", 1);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("StartDay", StartDay);
		mav.addObject("EndDay", EndDay);
		mav.addObject("GTYPELIST",GTYPELIST);
		return mav;
	}
	
	@RequestMapping(value = "EventReturn_Search")
	public ModelAndView EventReturn_Search(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "ENAME", defaultValue = "") String ENAME,
			@RequestParam(value = "BISSTS", defaultValue = "") String BISSTS,
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
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		CommonService common = new CommonService();
		List<EventVo> EeventReturnList = null;
		String msg = "";
		try {
			EeventReturnList = inOutService.getEventReturn(StartDay, EndDay, LOGC, BONBU, CENTER, ENAME, BISSTS, PAGE, 10);
		} catch (Exception e) {
			msg = "정보를 가져오지 못했습니다. 다시 시도해주세요.";
		}
		int listCnt = EeventReturnList == null || EeventReturnList.size() == 0 ? 0 : EeventReturnList.get(0).getTotalCnt();
		common.SetPaging(listCnt, PAGE);
		
		List<InOutListVo> GTYPELIST = inOutService.getGTYPELIST();
		
		mav.setViewName("Report/EventReturn");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("StartDay",StartDay);
		mav.addObject("EndDay",EndDay);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("ENAME",ENAME);
		mav.addObject("BISSTS",BISSTS);
		mav.addObject("EeventReturnList", EeventReturnList);
		mav.addObject("EeventReturnSize", EeventReturnList == null || EeventReturnList.size() == 0 ? 0 : EeventReturnList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("GTYPELIST", GTYPELIST);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "EventReturn_Update")
	public ModelAndView EventReturn_Update(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "ENAME", defaultValue = "") String ENAME,
			@RequestParam(value = "BISSTS", defaultValue = "") String BISSTS,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE,
			@RequestParam(value = "ETYPE", defaultValue = "1") String ETYPE,
			@RequestParam(value = "CMNT", defaultValue = "") String CMNT,
			@RequestParam(value = "ECHECK", defaultValue = "1") String ECHECK
			) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		String msg = inOutService.updateEventReturn(ECHECK, CMNT, ETYPE, userService.getUserID(request));
		
		UserVo UserInfo = userService.getUserInfo(userService.getUserID(request));
		if(UserInfo == null)
		{
			mav.setViewName("index");
			return mav;
		}
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		CommonService common = new CommonService();
		List<EventVo> EeventReturnList = null;
		try {
			EeventReturnList = inOutService.getEventReturn(StartDay, EndDay, LOGC, BONBU, CENTER, ENAME, BISSTS, PAGE, 10);
		} catch (Exception e) {
			mav.setViewName("redirect:/EventReturn.do");
			mav.addObject("msg","정보를 가져오지 못했습니다. 다시 시도해주세요.");
			return mav;
		}
		int listCnt = EeventReturnList == null || EeventReturnList.size() == 0 ? 0 : EeventReturnList.get(0).getTotalCnt();
		common.SetPaging(listCnt, PAGE);
		
		List<InOutListVo> GTYPELIST = inOutService.getGTYPELIST();
		
		mav.setViewName("Report/EventReturn");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("StartDay",StartDay);
		mav.addObject("EndDay",EndDay);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("ENAME",ENAME);
		mav.addObject("BISSTS",BISSTS);
		mav.addObject("EeventReturnList", EeventReturnList);
		mav.addObject("EeventReturnSize", EeventReturnList == null || EeventReturnList.size() == 0 ? 0 : EeventReturnList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("GTYPELIST", GTYPELIST);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "ModelBepum")
	public ModelAndView ModelBepum(HttpServletRequest request, 
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		CommonService common = new CommonService();
		List<ModelBepumVo> BepumList = null;
		try {
			BepumList = inOutService.getModelBepum(LOGC, BONBU, CENTER, "", "", 1, 10);
		} catch (Exception e) {
			msg = "정보를 가져오지 못했습니다. 다시 시도해주세요.";
		}
		int listCnt = BepumList == null || BepumList.size() == 0 ? 0 : BepumList.get(0).getTotalCnt();
		common.SetPaging(listCnt, 1);
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("Report/ModelBepum");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("BepumList", BepumList);
		mav.addObject("BepumListSize", BepumList == null || BepumList.size() == 0 ? 0 : BepumList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", 1);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		return mav;
	}
	
	@RequestMapping(value = "ModelBepum_Search")
	public ModelAndView ModelBepum_Search(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "BISSTS", defaultValue = "") String BISSTS,
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
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		CommonService common = new CommonService();
		List<ModelBepumVo> BepumList = null;
		String msg = "";
		try {
			BepumList = inOutService.getModelBepum(LOGC, BONBU, CENTER, MNAME, BISSTS, PAGE, 10);
		} catch (Exception e) {
			msg = "정보를 가져오지 못했습니다. 다시 시도해주세요.";
		}
		int listCnt = BepumList == null || BepumList.size() == 0 ? 0 : BepumList.get(0).getTotalCnt();
		common.SetPaging(listCnt, PAGE);
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		
		mav.setViewName("Report/ModelBepum");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("MNAME",MNAME);
		mav.addObject("BISSTS",BISSTS);
		mav.addObject("BepumList", BepumList);
		mav.addObject("BepumListSize", BepumList == null || BepumList.size() == 0 ? 0 : BepumList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "JegoMagamReport")
	public ModelAndView JegoMagamReport(HttpServletRequest request, 
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
		
		String LOGC = UserInfo.getMCI_LOGC();
		String BONBU = UserInfo.getMCI_Bonbu();
		String CENTER = UserInfo.getMCI_Center();
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		List<CodeVo> KINDSLIST = codeService.getSCodeList("200","200");
		
		Calendar cal = Calendar.getInstance();
		try {
			mav.addObject("YEAR",cal.get(Calendar.YEAR));
			mav.addObject("MONTH",cal.get(Calendar.MONTH) + 1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		mav.setViewName("Report/JegoMagamReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("KINDSLIST", KINDSLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("JEGO_LISTSIZE", 0);
		return mav;
	}
	
	@RequestMapping(value = "JegoMagamReport_Search")
	public ModelAndView JegoMagamReport_Search(HttpServletRequest request, 
			@RequestParam(value = "YEAR", defaultValue = "0") int YEAR,
			@RequestParam(value = "MONTH", defaultValue = "0") int MONTH,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "LGROUP", defaultValue = "") String LGROUP,
			@RequestParam(value = "KINDS", defaultValue = "") String KINDS
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
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		
		if(YEAR == 0)
		{
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","연도를 입력해주세요.");
			return mav;
		}
		if(YEAR < 1753 || YEAR > 9999) {
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","올바른 연도를 입력해주세요.");
			return mav;
		}
		
		if(MONTH == 0)
		{
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","올바른 날짜를 입력해주세요.");
			return mav;
		}
		if(MONTH < 1 || MONTH > 12) {
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","올바른 날짜를 입력해주세요.");
			return mav;
		}
		
		List<CenterVo> LOGCLIST = centerService.getLOGCInfo();
		List<CenterVo> BONBULIST = centerService.getBONBUList(LOGC);
		List<CenterVo> CENTERLIST = centerService.getCENTERList(LOGC, BONBU);
		
		List<JegoMagamReportVo> JEGO_LIST = null;
		String msg = "";
		try {
			JEGO_LIST = inOutService.getJegoMagamReport(YEAR, MONTH, LOGC, BONBU, CENTER, LGROUP, KINDS);
		} catch (Exception e) {
			msg = "정보를 가져오지 못했습니다. 다시 시도해주세요.";
		}
		
		List<CodeVo> LCODELIST = codeService.getSCodeList("200","201");
		List<CodeVo> KINDSLIST = codeService.getSCodeList("200","200");
		
		mav.setViewName("Report/JegoMagamReport");
		mav.addObject("LOGCLIST", LOGCLIST);
		mav.addObject("BONBULIST",BONBULIST);
		mav.addObject("CENTERLIST",CENTERLIST);
		mav.addObject("LOGC",LOGC);
		mav.addObject("BONBU",BONBU);
		mav.addObject("CENTER",CENTER);
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("msg",msg);
		mav.addObject("UserInfo", UserInfo);
		mav.addObject("KINDSLIST", KINDSLIST);
		mav.addObject("LCODELIST", LCODELIST);
		mav.addObject("YEAR", YEAR);
		mav.addObject("MONTH", MONTH);
		mav.addObject("LOGC", LOGC);
		mav.addObject("BONBU", BONBU);
		mav.addObject("CENTER", CENTER);
		mav.addObject("LGROUP", LGROUP);
		mav.addObject("KINDS", KINDS);
		mav.addObject("JEGO_LIST", JEGO_LIST);
		mav.addObject("JEGO_LISTSIZE", JEGO_LIST == null ? 0 : JEGO_LIST.size());
		return mav;
	}
	@RequestMapping(value = "JegoMagamReport_Excel")
	public ModelAndView JegoMagamReport_Excel(HttpServletRequest request, 
			@RequestParam(value = "YEAR", defaultValue = "0") int YEAR,
			@RequestParam(value = "MONTH", defaultValue = "0") int MONTH,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "LGROUP", defaultValue = "") String LGROUP,
			@RequestParam(value = "KINDS", defaultValue = "") String KINDS
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
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		
		if(YEAR == 0)
		{
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","연도를 입력해주세요.");
			return mav;
		}
		if(YEAR < 1753 || YEAR > 9999) {
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","올바른 연도를 입력해주세요.");
			return mav;
		}
		
		if(MONTH == 0)
		{
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","올바른 날짜를 입력해주세요.");
			return mav;
		}
		if(MONTH < 1 || MONTH > 12) {
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","올바른 날짜를 입력해주세요.");
			return mav;
		}
		
		List<JegoMagamReportVo> JEGO_LIST = null;
		String msg = "";
		try {
			JEGO_LIST = inOutService.getJegoMagamReport(YEAR, MONTH, LOGC, BONBU, CENTER, LGROUP, KINDS);
		} catch (Exception e) {
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","정보를 가져오지 못했습니다.");
			return mav;
		}
		UUID uuid = UUID.randomUUID();
		String savedName = uuid.toString()+"_JegoMagam.xlsx";
		int result = inOutService.createJegoMagamExcel(JEGO_LIST, request, savedName);
		if(result == -1) {
			mav.setViewName("redirect:/JegoMagamReport.do");
			mav.addObject("msg","엑셀파일 생성실패! 다시 시도해주세요.");
			return mav;
		}
		
		String fullPath = fileService.filePath(request, "TEMP") + "/" + savedName;
		File file = new File(fullPath);

		mav.setViewName("DownloadExcel");
		mav.addObject("downloadFile",file);
		mav.addObject("OrgName","재고 마감 현황.xlsx");
		return mav;
	}
	
	@RequestMapping(value = "InOutListReport_Excel")
	public ModelAndView InOutListReport_Excel(HttpServletRequest request, 
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME,
			@RequestParam(value = "IOTYPE", defaultValue = "0") int IOTYPE,
			@RequestParam(value = "EVENT", defaultValue = "") String EVENT
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
		
		if(UserInfo.getMUT_POSITION().equals("003")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
		}
		else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
			LOGC = UserInfo.getMCI_LOGC();
			BONBU = UserInfo.getMCI_Bonbu();
			CENTER = UserInfo.getMCI_Center();
		}
		
		List<InOutListVo> INOUTLIST = inOutService.getInOutListReport_Excel(StartDay, EndDay, LOGC, BONBU, CENTER, MNAME, IOTYPE, EVENT);
		
		UUID uuid = UUID.randomUUID();
		String savedName = uuid.toString()+"_InOutReport.xlsx";
		int result = inOutService.createInOutReportExcel(INOUTLIST, request, savedName);
		if(result == -1) {
			mav.setViewName("redirect:/InOutListReport.do");
			mav.addObject("msg","엑셀파일 생성실패! 다시 시도해주세요.");
			return mav;
		}
		
		String fullPath = fileService.filePath(request, "TEMP") + "/" + savedName;
		File file = new File(fullPath);

		mav.setViewName("DownloadExcel");
		mav.addObject("downloadFile",file);
		mav.addObject("OrgName","입출고 내역 조회.xlsx");
		return mav;
	}
}
