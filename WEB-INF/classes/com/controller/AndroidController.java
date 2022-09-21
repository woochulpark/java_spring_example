package com.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.service.CenterService;
import com.service.CodeService;
import com.service.CommonService;
import com.service.FileService;
import com.service.InOutService;
import com.service.ModelService;
import com.service.NoticeService;
import com.service.UserService;
import com.vo.AndroidVo;
import com.vo.CenterVo;
import com.vo.CfmVo;
import com.vo.CodeVo;
import com.vo.EventVo;
import com.vo.InOutListVo;
import com.vo.ModelVo;
import com.vo.MoveVo;
import com.vo.NoticeVo;
import com.vo.UserVo;

@Controller
public class AndroidController {
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CenterService centerService;
	
	@Autowired
	private InOutService inOutService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	private ModelService modelService;
	
	@RequestMapping(value = "HTTP_LOGIN", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String  HTTP_LOGIN(HttpServletRequest request,
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "password", defaultValue = "")  String pw) {
		AndroidVo vo  = userService.android_login(id, pw);
		Gson gson = new Gson();
		String json = gson.toJson(vo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_MAIN", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String  HTTP_MAIN(HttpServletRequest request,
			@RequestParam(value = "id", defaultValue = "") String id) {
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "ID를 받아오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		try {
			List<LinkedHashMap<String, Object>> NoticeList = noticeService.getAndroidNoticeMainList(id);
			if(NoticeList == null) {
				androidVo.returnCode = "1006";
				androidVo.returnMsg = "DB에러!! 다시 시도해주세요.";
				androidVo.TABSIZE = 0;
			}
			else {
				androidVo.returnCode = "0000";
				androidVo.TABSIZE = NoticeList.size();
				androidVo.IT_TAB1 = NoticeList;
			}
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
			androidVo.TABSIZE = 0;
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_MAIN_DETAIL", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_MAIN_DETAIL(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0 || DOCNO.length() == 0 ) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "공지사항 상세정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		try {
			NoticeVo NoticeInfo = noticeService.getAndroidNoticeInfo(DOCNO);
			if(NoticeInfo != null) {
				noticeService.updateNoticeUser(DOCNO, id);
				List<LinkedHashMap<String, Object>> FileList = noticeService.getAndroidFileList(DOCNO);
				if(FileList != null) {
					androidVo.returnCode = "0000";
					androidVo.returnMsg = "";
					androidVo.field1 = NoticeInfo.getMNI_DOCNO();
					androidVo.field2 = NoticeInfo.getMNI_TITLE();
					androidVo.field3 = NoticeInfo.getMNI_Cmnt();
					androidVo.IT_TAB1 = FileList;
					androidVo.TABSIZE = FileList.size();
				}
				else {
					androidVo.returnCode = "1006";
					androidVo.returnMsg = "첨부파일 목록을 가져오지 못했습니다.";
				}
			}
			else {
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "공지사항 상세정보를 가져오지 못했습니다.";
			}
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_CFM", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_CFM(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id
			) {

		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			String LOGC = "";
			String BONBU = "";
			String CENTER = "";
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				CENTER = UserInfo.getMCI_Center();
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = UserInfo.getMCI_Center();
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = UserInfo.getMCI_CenterName();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				List<LinkedHashMap<String, Object>> CENTERLIST = centerService.getAndroidCENTERList(LOGC, BONBU);
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = "";
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = "";
				androidVo.TABSIZE = CENTERLIST.size();
				androidVo.IT_TAB1 = CENTERLIST;
			}
			else {
				List<LinkedHashMap<String, Object>> LOGCLIST = centerService.getAndroidLOGCInfo();
				androidVo.TABSIZE = LOGCLIST.size();
				androidVo.IT_TAB1 = LOGCLIST;
			}

			List<LinkedHashMap<String, Object>> CFMLIST = inOutService.getAndroidInCFMList("", "", LOGC, BONBU, CENTER, "");
			androidVo.TABSIZE2 = CFMLIST.size();
			androidVo.IT_TAB2 = CFMLIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_CFM_SEARCH", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_CFM_SEARCH(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "CFMSTS", defaultValue = "") String CFMSTS
			) {

		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				CENTER = UserInfo.getMCI_Center();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
			}

			List<LinkedHashMap<String, Object>> CFMLIST = inOutService.getAndroidInCFMList(StartDay, EndDay, LOGC, BONBU, CENTER, CFMSTS);
			androidVo.TABSIZE = CFMLIST.size();
			androidVo.IT_TAB1 = CFMLIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_CFM_ADD", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_CFM_ADD(HttpServletRequest request,
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "INTYPE", defaultValue = "") String INTYPE,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}

		if(DOCNO.length() == 0 || INTYPE.length() == 0 ) {
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "본부입고승인 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			CfmVo CFMINFO = null;
			if(INTYPE.equals("0")) {
				CFMINFO = inOutService.getInCFMLogcInfo(DOCNO);
				if(CFMINFO == null) {
					androidVo.returnCode = "1006";
					androidVo.returnMsg = "입고승인정보를 가져오지 못했습니다.";
					Gson gson = new Gson();
					String json = gson.toJson(androidVo);
					return json;
				}
				androidVo.field1 = INTYPE;
				androidVo.field2 = CFMINFO.getDOCNO();
				androidVo.field3 = CFMINFO.getOUT_BONBU();
				androidVo.field4 = CFMINFO.getINBONBU_NAME();
				androidVo.field5 = CFMINFO.getINCENTER_NAME();
				androidVo.field6 = CFMINFO.getMODEL();
				androidVo.field7 = CFMINFO.getMMC_LGROUPNAME();
				androidVo.field8 = CFMINFO.getMMC_MGROUP();
				androidVo.field9 = CFMINFO.getMMC_SGROUP();
				androidVo.field10 = CFMINFO.getOUTDATE();
				androidVo.field11 = String.valueOf(CFMINFO.getOUTQTY());
				androidVo.field12 = CFMINFO.getDIC_DATE();
				androidVo.field13 = CFMINFO.getDIC_USERNAME();
				androidVo.field14 = CFMINFO.getDIC_CMNT();
				androidVo.field15 = CFMINFO.getCFMsts();
			}
			else {
				CFMINFO = inOutService.getInCFMBonbuInfo(DOCNO);
				if(CFMINFO == null) {
					androidVo.returnCode = "1006";
					androidVo.returnMsg = "입고승인정보를 가져오지 못했습니다.";
					Gson gson = new Gson();
					String json = gson.toJson(androidVo);
					return json;
				}
				androidVo.field1 = INTYPE;
				androidVo.field2 = CFMINFO.getDOCNO();
				androidVo.field3 = CFMINFO.getOUT_BONBU();
				androidVo.field4 = CFMINFO.getINBONBU_NAME();
				androidVo.field5 = CFMINFO.getINCENTER_NAME();
				androidVo.field6 = CFMINFO.getMODEL();
				androidVo.field7 = CFMINFO.getMMC_LGROUPNAME();
				androidVo.field8 = CFMINFO.getMMC_MGROUP();
				androidVo.field9 = CFMINFO.getMMC_SGROUP();
				androidVo.field10 = CFMINFO.getINDATE();
				androidVo.field11 = String.valueOf(CFMINFO.getINQTY());
				androidVo.field12 = CFMINFO.getDBO_CMNT();
				androidVo.field13 = CFMINFO.getOUTDATE();
				androidVo.field14 = String.valueOf(CFMINFO.getOUTQTY());
				androidVo.field15 = CFMINFO.getDBO_USERNAME();
				androidVo.field16 = CFMINFO.getDIC_DATE();
				androidVo.field17 = CFMINFO.getDIC_USERNAME();
				androidVo.field18 = CFMINFO.getDIC_CMNT();
				androidVo.field19 = CFMINFO.getCFMsts();
			}

			List<LinkedHashMap<String, Object>> FileList = noticeService.getAndroidFileList(DOCNO);

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
			if(FileList != null) {
				androidVo.IT_TAB1 = FileList;
				androidVo.TABSIZE = FileList.size();
			}
			else {
				androidVo.TABSIZE = 0;
			}
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_BONBU", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_BONBU(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBUTYPE", defaultValue = "0") String BONBUTYPE
			) {

		AndroidVo androidVo = new AndroidVo();
		if(LOGC.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "물류센터 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		try {
			List<LinkedHashMap<String, Object>> BONBULIST = centerService.getAndroidBONBUList(LOGC);
			androidVo.TABSIZE = BONBULIST.size();
			androidVo.IT_TAB1 = BONBULIST;
			androidVo.field1 = BONBUTYPE;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_CENTER", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_CENTER(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "BONBUTYPE", defaultValue = "0") String BONBUTYPE
			) {

		AndroidVo androidVo = new AndroidVo();
		if(LOGC.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "물류센터 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		if(BONBU.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "본부 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			List<LinkedHashMap<String, Object>> CENTERLIST = centerService.getAndroidCENTERList(LOGC, BONBU);
			androidVo.TABSIZE = CENTERLIST.size();
			androidVo.IT_TAB1 = CENTERLIST;
			androidVo.field1 = BONBUTYPE;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_FILE_DEL", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public @ResponseBody String HTTP_FILE_DEL(
			HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "SEQ", defaultValue = "") String SEQ
			) {

		AndroidVo androidVo = new AndroidVo();
		if(DOCNO.length() == 0 || SEQ.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "파일 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		int I_SEQ = 0;
		try {
			if(SEQ.indexOf(".") == -1)
				I_SEQ = Integer.parseInt(SEQ);
			else {
				double temp = Double.parseDouble(SEQ);
				I_SEQ = (int)temp;
			}
		}
		catch (Exception e) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "파일 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			NoticeVo fileinfo = noticeService.getNoticeFileInfo(DOCNO, String.valueOf(I_SEQ));
			if(fileinfo == null) {
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "파일 정보를 가져오지 못했습니다.";
			}
			else {
				int result = fileService.fileDelete(fileinfo.getMDF_FILE(), "", request);
				if(result == -1)
				{
					androidVo.returnCode = "2108";
					androidVo.returnMsg = "파일삭제실패! 다시 시도해주세요.";
				}
				else {
					result = noticeService.deleteNoticeFile(DOCNO, String.valueOf(I_SEQ));
					if(result == -1)
					{
						androidVo.returnCode = "2108";
						androidVo.returnMsg = "파일삭제실패! 다시 시도해주세요.";
					}
					else {
						List<LinkedHashMap<String, Object>> FileList = noticeService.getAndroidFileList(DOCNO);
						if(FileList != null) {
							androidVo.IT_TAB1 = FileList;
							androidVo.TABSIZE = FileList.size();
						}
						androidVo.returnCode = "0000";
						androidVo.returnMsg = "";
					}
				}
			}
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_FILE_Snd", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public @ResponseBody String HTTP_FILE_Snd(
			HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "INTYPE", defaultValue = "") String INTYPE,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "DIC_DATE", defaultValue = "") String DIC_DATE,
			@RequestParam(value = "DIC_CMNT", defaultValue = "") String DIC_CMNT,
			@RequestParam(value = "CFMsts", defaultValue = "") String CFMsts,
			@RequestParam(value="file", required = false) List<MultipartFile> fileList
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(DOCNO.length() == 0 || id.length() == 0 ) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "입고승인 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		if(!CFMsts.equals("N") && !CFMsts.equals("Y"))
		{
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "입고승인 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		if(!INTYPE.equals("0") && !INTYPE.equals("1"))
		{
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "입고승인 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		int result = inOutService.Android_updateCfm(CFMsts, DOCNO, INTYPE, id, DIC_DATE, DIC_CMNT, "BonbuCfm_PASS", fileList, request);
		if(result == -1) {
			androidVo.returnCode = "1008";
			androidVo.returnMsg = "저장실패! 다시 시도해주세요.";
		}
		else if(result == -2) {
			androidVo.returnCode = "2107";
			androidVo.returnMsg = "일부 파일 저장에 실패하였습니다.";
		}
		else {
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_EVENTOUT_LIST", method = RequestMethod.POST, produces="application/text; charset=utf8")
	public @ResponseBody String HTTP_EVENTOUT_LIST(
			HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		try {
			String LOGC = "";
			String BONBU = "";
			String CENTER = "";
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				CENTER = UserInfo.getMCI_Center();
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = UserInfo.getMCI_Center();
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = UserInfo.getMCI_CenterName();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				List<LinkedHashMap<String, Object>> CENTERLIST = centerService.getAndroidCENTERList(LOGC, BONBU);
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = "";
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = "";
				androidVo.TABSIZE = CENTERLIST.size();
				androidVo.IT_TAB1 = CENTERLIST;
			}
			else {
				List<LinkedHashMap<String, Object>> LOGCLIST = centerService.getAndroidLOGCInfo();
				androidVo.TABSIZE = LOGCLIST.size();
				androidVo.IT_TAB1 = LOGCLIST;
			}

			List<LinkedHashMap<String, Object>> CFMLIST = inOutService.getAndroidEventList("", "", LOGC, BONBU, CENTER, "");
			androidVo.TABSIZE2 = CFMLIST.size();
			androidVo.IT_TAB2 = CFMLIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_EVENTOUT_SEARCH", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENTOUT_SEARCH(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "EVENTNO", defaultValue = "") String EVENTNO
			) {

		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				CENTER = UserInfo.getMCI_Center();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
			}
			List<LinkedHashMap<String, Object>> CFMLIST = inOutService.getAndroidEventList(StartDay, EndDay, LOGC, BONBU, CENTER, EVENTNO);
			androidVo.TABSIZE = CFMLIST.size();
			androidVo.IT_TAB1 = CFMLIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_EVENT_LIST", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENT_LIST(
			HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id) {
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		try {
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = UserInfo.getMCI_Center();
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = UserInfo.getMCI_CenterName();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				String LOGC = UserInfo.getMCI_LOGC();
				String BONBU = UserInfo.getMCI_Bonbu();
				List<LinkedHashMap<String, Object>> CENTERLIST = centerService.getAndroidCENTERList(LOGC, BONBU);
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = "";
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = "";
				androidVo.TABSIZE = CENTERLIST.size();
				androidVo.IT_TAB1 = CENTERLIST;
			}
			else {
				List<LinkedHashMap<String, Object>> LOGCLIST = centerService.getAndroidLOGCInfo();
				androidVo.TABSIZE = LOGCLIST.size();
				androidVo.IT_TAB1 = LOGCLIST;
			}
			List<LinkedHashMap<String, Object>> GTYPELIST = inOutService.getAndroidGTYPELIST();
			androidVo.TABSIZE2 = GTYPELIST.size();
			androidVo.IT_TAB2 = GTYPELIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_EVENT_SEARCH", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENT_SEARCH(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id,
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
			@RequestParam(value = "EVENTTYPE", defaultValue = "0") String EVENTTYPE
			) {

		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("003")) {
				BONBU = UserInfo.getMCI_BonbuNAME();
			}
			else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				BONBU = UserInfo.getMCI_BonbuNAME();
				CENTER = UserInfo.getMCI_CenterName();
			}
			
			StartDay = StartDay.replaceAll("-", "");
			EndDay = EndDay.replaceAll("-", "");
			
			List<LinkedHashMap<String, Object>> EVENTLIST = inOutService.getAndroidEventInfo(StartDay, EndDay, BONBU, CENTER, EUSERID, EUSERNAME, GNAME, JNAME, ESTATE, GTYPE);
			androidVo.TABSIZE = EVENTLIST.size();
			androidVo.IT_TAB1 = EVENTLIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_BONBU_LIST", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_BONBU_LIST(
			HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id) {
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		try {
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = UserInfo.getMCI_Center();
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = UserInfo.getMCI_CenterName();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				String LOGC = UserInfo.getMCI_LOGC();
				String BONBU = UserInfo.getMCI_Bonbu();
				List<LinkedHashMap<String, Object>> CENTERLIST = centerService.getAndroidCENTERList(LOGC, BONBU);
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = "";
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = "";
				androidVo.TABSIZE = CENTERLIST.size();
				androidVo.IT_TAB1 = CENTERLIST;
			}
			else {
				List<LinkedHashMap<String, Object>> LOGCLIST = centerService.getAndroidLOGCInfo();
				androidVo.TABSIZE = LOGCLIST.size();
				androidVo.IT_TAB1 = LOGCLIST;
			}

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_LGROUP_LIST", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_LGROUP_LIST(HttpServletRequest request) {
		
		AndroidVo androidVo = new AndroidVo();
		try {
			List<LinkedHashMap<String, Object>> LOGCLIST = centerService.getAndroidLOGCInfo();
			androidVo.TABSIZE = LOGCLIST.size();
			androidVo.IT_TAB1 = LOGCLIST;
			List<LinkedHashMap<String, Object>> LCODELIST = codeService.getAndroidSCodeList("200","201");
			androidVo.TABSIZE2 = LCODELIST.size();
			androidVo.IT_TAB2 = LCODELIST;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_MGROUP_LIST", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_MGROUP_LIST(HttpServletRequest request,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE) {
		
		AndroidVo androidVo = new AndroidVo();
		try {
			List<LinkedHashMap<String, Object>> MCODELIST = modelService.MgroupAndroidList(LOGC, LCODE);
			androidVo.TABSIZE = MCODELIST.size();
			androidVo.IT_TAB1 = MCODELIST;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_SGROUP_LIST", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_SGROUP_LIST(HttpServletRequest request,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE){
		
		AndroidVo androidVo = new AndroidVo();
		try {
			List<LinkedHashMap<String, Object>> SCODELIST = modelService.SgroupAndroidList(LOGC, LCODE, MCODE);
			androidVo.TABSIZE = SCODELIST.size();
			androidVo.IT_TAB1 = SCODELIST;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_MODEL_LIST", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_MODEL_LIST(HttpServletRequest request, 
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "LCODE", defaultValue = "") String LCODE,
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "SCODE", defaultValue = "") String SCODE,
			@RequestParam(value = "MNAME", defaultValue = "") String MNAME
			) {
		
		AndroidVo androidVo = new AndroidVo();
		try {
			List<LinkedHashMap<String, Object>> ModelList = modelService.getAndroidModelList(LOGC, LCODE, MCODE, SCODE, MNAME);
			androidVo.TABSIZE = ModelList.size();
			androidVo.IT_TAB1 = ModelList;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_EVENT_MODEL", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENT_MODEL(HttpServletRequest request, 
			@RequestParam(value = "MCODE", defaultValue = "") String MCODE,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER
			) {
		
		AndroidVo androidVo = new AndroidVo();
		try {
			LinkedHashMap<String, Object> vo = inOutService.getAndroidJegoModel(MCODE, LOGC+BONBU+CENTER);
			if(vo == null) {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "선택한 본부에 재고가 없습니다.";
			}
			else {
				String JEGO = "0";
				JEGO = vo.get("OUTJEGO") != null ? vo.get("OUTJEGO").toString() : "0";
				androidVo.field1 = JEGO;
				androidVo.field2 = vo.get("DMJ_MODEL").toString();
				androidVo.field3 = vo.get("MMC_LGROUPNAME").toString();
				androidVo.field4 = vo.get("MMC_MGROUP").toString();
				androidVo.field5 = vo.get("MMC_SGROUP").toString();
				androidVo.field6 = vo.get("MMC_KINDS").toString();
				androidVo.returnCode = "0000";
				androidVo.returnMsg = "";
			}
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value = "HTTP_EVENT_SAVE", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENT_SAVE(HttpServletRequest request, 
			@RequestParam(value = "MDATA", defaultValue = "") String MDATA,
			@RequestParam(value = "ID", defaultValue = "") String ID
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(MDATA.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "행사출고 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		if(ID.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(ID);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			Gson gson = new Gson();
			AndroidVo vo = gson.fromJson(MDATA, AndroidVo.class);
			if(vo == null)
			{
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "행사출고 정보를 가져오지 못했습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			if(vo.field1.length() != 13)
			{
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "행사번호가 잘못되었습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			LinkedHashMap<String, Object> EventInfo = inOutService.getAndroidEventDetail(vo.field1);
			if(EventInfo == null)
			{
				androidVo.returnCode = "1006";
				androidVo.returnMsg = "행사정보를 가져오지 못했습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			String ESTATE = EventInfo.get("ESTATE").toString();
			if(!ESTATE.equals("접수") && !ESTATE.equals("진행")) {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "행사상태가 접수 혹은 진행중인 행사정보만 출하할 수 있습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			if(vo.field2.length() == 0)
			{
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "출고본부를 가져오지 못했습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			inOutService.insertEVENT(androidVo, vo, ID, "Event_Add");
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_EVENT_UPDATE", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENT_UPDATE(HttpServletRequest request, 
			@RequestParam(value = "MDATA", defaultValue = "") String MDATA,
			@RequestParam(value = "ID", defaultValue = "") String ID
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(MDATA.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "행사출고 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		if(ID.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(ID);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			Gson gson = new Gson();
			AndroidVo vo = gson.fromJson(MDATA, AndroidVo.class);
			if(vo == null)
			{
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "행사출고 정보를 가져오지 못했습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			if(vo.field1.length() != 13)
			{
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "행사번호가 잘못되었습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			LinkedHashMap<String, Object> EventInfo = inOutService.getAndroidEventDetail(vo.field1);
			if(EventInfo == null)
			{
				androidVo.returnCode = "1006";
				androidVo.returnMsg = "행사정보를 가져오지 못했습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			String ESTATE = EventInfo.get("ESTATE").toString();
			if(ESTATE.equals("완료")) {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "완료된 행사정보는 수정할 수 없습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			inOutService.updateEVENT(androidVo, vo, ID, "Event_Update");
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_EVENT_DELETE", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENT_DELETE(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "ID", defaultValue = "") String ID
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(DOCNO.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "행사출고 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		if(ID.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(ID);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			Gson gson = new Gson();
			if(DOCNO.length() != 13)
			{
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "행사번호가 잘못되었습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			LinkedHashMap<String, Object> EventInfo = inOutService.getAndroidEventDetail(DOCNO);
			if(EventInfo == null)
			{
				androidVo.returnCode = "1006";
				androidVo.returnMsg = "행사정보를 가져오지 못했습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			String ESTATE = EventInfo.get("ESTATE").toString();
			if(ESTATE.equals("완료")) {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "완료된 행사정보는 삭제할 수 없습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			inOutService.deleteEVENT(androidVo, DOCNO);
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_EVENTMODEL_DELETE", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENTMODEL_DELETE(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "MODEL", defaultValue = "") String MODEL,
			@RequestParam(value = "ID", defaultValue = "") String ID
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(DOCNO.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "행사출고 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		if(ID.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(ID);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			Gson gson = new Gson();
			if(DOCNO.length() != 13)
			{
				androidVo.returnCode = "1004";
				androidVo.returnMsg = "행사번호가 잘못되었습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			LinkedHashMap<String, Object> EventInfo = inOutService.getAndroidEventDetail(DOCNO);
			if(EventInfo == null)
			{
				androidVo.returnCode = "1006";
				androidVo.returnMsg = "행사정보를 가져오지 못했습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			String ESTATE = EventInfo.get("ESTATE").toString();
			if(ESTATE.equals("완료")) {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "완료된 행사정보는 삭제할 수 없습니다.";
				String json = gson.toJson(androidVo);
				return json;
			}
			int result = inOutService.deleteEVENTMODEL(androidVo, DOCNO, CENTER, MODEL);
			if(result == 1) {
				androidVo.field1 = CENTER;
				androidVo.field2 = MODEL;
			}
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_EVENT_DETAIL", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_EVENT_DETAIL(HttpServletRequest request, 
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "id", defaultValue = "") String id
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			LinkedHashMap<String, Object> vo = inOutService.getAndroidEvent(DOCNO);
			LinkedHashMap<String, Object> Evo = inOutService.getAndroidEventDetail(DOCNO);
			List<LinkedHashMap<String, Object>> MLIST = inOutService.getAndroidEventModel(DOCNO);
			if(vo == null || Evo == null || MLIST == null) {
				androidVo.returnCode = "1006";
				androidVo.returnMsg = "행사출고 정보를 가져오지 못했습니다.";
				Gson gson = new Gson();
				String json = gson.toJson(androidVo);
				return json;
			}
			String LEVEL = UserInfo.getMUT_POSITION();
			String LOGC = vo.get("MCI_LOGC").toString();
			String BONBU = vo.get("MCI_Bonbu").toString();
			String CENTER = vo.get("MCI_Center").toString();
			String ULOGC = UserInfo.getMCI_LOGC();
			String UBONBU = UserInfo.getMCI_Bonbu();
			String UCENTER = UserInfo.getMCI_Center();
			if(LEVEL.equals("004") || LEVEL.equals("005")) {
				if(!LOGC.equals(ULOGC) || !BONBU.equals(UBONBU) || !CENTER.equals(UCENTER)) {
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = "권한이 없습니다.";
					Gson gson = new Gson();
					String json = gson.toJson(androidVo);
					return json;
				}
			}
			else if(LEVEL.equals("003")) {
				if(!LOGC.equals(ULOGC) || !BONBU.equals(UBONBU)) {
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = "권한이 없습니다.";
					Gson gson = new Gson();
					String json = gson.toJson(androidVo);
					return json;
				}
			}
			
			androidVo.field1 = vo.get("DEO_DOCNO").toString();
			androidVo.field2 = vo.get("DEO_CENTER").toString();
			androidVo.field3 = vo.get("MCI_LOGC").toString();
			androidVo.field4 = vo.get("MCI_LOGCNAME").toString();
			androidVo.field5 = vo.get("MCI_Bonbu").toString();
			androidVo.field6 = vo.get("MCI_BonbuNAME").toString();
			androidVo.field7 = vo.get("MCI_Center").toString();
			androidVo.field8 = vo.get("MCI_CenterName").toString();
			androidVo.field9 = vo.get("DEO_USER").toString();
			androidVo.field10 = vo.get("MUT_UserName").toString();
			androidVo.field11 = vo.get("DEO_DATE").toString();
			androidVo.field12 = vo.get("DEO_CMNT").toString();
			androidVo.field13 = Evo.get("BONBUNAME").toString();
			androidVo.field14 = Evo.get("CENTERNAME").toString();
			androidVo.field15 = Evo.get("EUSERNAME").toString();
			androidVo.field16 = Evo.get("GROUPNAME").toString();
			androidVo.field17 = Evo.get("JNAME").toString();
			androidVo.field18 = Evo.get("ESTATE").toString();
			androidVo.field19 = Evo.get("GTYPE").toString();
			androidVo.field20 = "";
			androidVo.field21 = "";
			androidVo.field22 = LEVEL;
			androidVo.TABSIZE = MLIST.size();
			androidVo.IT_TAB1 = MLIST;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_RETURN", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_RETURN(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id
			) {

		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			String LOGC = "";
			String BONBU = "";
			String CENTER = "";
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				CENTER = UserInfo.getMCI_Center();
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = UserInfo.getMCI_Center();
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = UserInfo.getMCI_CenterName();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				List<LinkedHashMap<String, Object>> CENTERLIST = centerService.getAndroidCENTERList(LOGC, BONBU);
				androidVo.field2 = UserInfo.getMCI_LOGC();
				androidVo.field3 = UserInfo.getMCI_Bonbu();
				androidVo.field4 = "";
				androidVo.field5 = UserInfo.getMCI_LOGCNAME();
				androidVo.field6 = UserInfo.getMCI_BonbuNAME();
				androidVo.field7 = "";
				androidVo.TABSIZE = CENTERLIST.size();
				androidVo.IT_TAB1 = CENTERLIST;
			}
			else {
				androidVo.field2 = "";
				androidVo.field3 = "";
				androidVo.field4 = "";
				List<LinkedHashMap<String, Object>> LOGCLIST = centerService.getAndroidLOGCInfo();
				androidVo.TABSIZE = LOGCLIST.size();
				androidVo.IT_TAB1 = LOGCLIST;
			}
			
			

			List<LinkedHashMap<String, Object>> RetuenLIST = inOutService.getAndroidModelRetuen("", "", LOGC, BONBU, CENTER, "", "1");
			androidVo.TABSIZE2 = RetuenLIST.size();
			androidVo.IT_TAB2 = RetuenLIST;
			
			List<LinkedHashMap<String, Object>> BISLIST = codeService.getAndroidSCodeList("100","200");
			androidVo.TABSIZE3 = BISLIST.size();
			androidVo.IT_TAB3 = BISLIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_RETURN_SEARCH", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_RETURN_SEARCH(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "StartDay", defaultValue = "") String StartDay,
			@RequestParam(value = "EndDay", defaultValue = "") String EndDay,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "ENAME", defaultValue = "") String ENAME,
			@RequestParam(value = "BISSTS", defaultValue = "") String BISSTS
			) {

		if(BISSTS.equals("0"))
			BISSTS = "";
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		try {
			androidVo.field1 = UserInfo.getMUT_POSITION();
			if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				CENTER = UserInfo.getMCI_Center();
			}
			else if(UserInfo.getMUT_POSITION().equals("003")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
			}
			
			androidVo.field2 = LOGC;
			androidVo.field3 = BONBU;
			androidVo.field4 = CENTER;

			List<LinkedHashMap<String, Object>> RetuenLIST = inOutService.getAndroidModelRetuen(StartDay, EndDay, LOGC, BONBU, CENTER, ENAME, BISSTS);
			androidVo.TABSIZE = RetuenLIST.size();
			androidVo.IT_TAB1 = RetuenLIST;

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	
	@RequestMapping(value = "HTTP_RETURN_UPDATE", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String EventReturn_Update(HttpServletRequest request, 
			@RequestParam(value = "id", defaultValue = "") String id,
			@RequestParam(value = "LOGC", defaultValue = "") String LOGC,
			@RequestParam(value = "BONBU", defaultValue = "") String BONBU,
			@RequestParam(value = "CENTER", defaultValue = "") String CENTER,
			@RequestParam(value = "CCODE", defaultValue = "") String CCODE,
			@RequestParam(value = "DOCNO", defaultValue = "") String DOCNO,
			@RequestParam(value = "MODEL", defaultValue = "") String MODEL,
			@RequestParam(value = "BISSTS", defaultValue = "") String BISSTS,
			@RequestParam(value = "POS", defaultValue = "1") String POS
			) {
		
		AndroidVo androidVo = new AndroidVo();
		if(id.length() == 0) {
			androidVo.returnCode = "1004";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		UserVo UserInfo = userService.getUserInfo(id);
		if(UserInfo == null)
		{
			androidVo.returnCode = "1006";
			androidVo.returnMsg = "사용자 정보를 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		String msg = inOutService.updateAndroidReturn(DOCNO, CCODE, MODEL, BISSTS, id);
		if(msg.length() == 0) {
			if(UserInfo.getMUT_POSITION().equals("003")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
			}
			else if(UserInfo.getMUT_POSITION().equals("004") || UserInfo.getMUT_POSITION().equals("005")) {
				LOGC = UserInfo.getMCI_LOGC();
				BONBU = UserInfo.getMCI_Bonbu();
				CENTER = UserInfo.getMCI_Center();
			}

			List<LinkedHashMap<String, Object>> RetuenLIST;
			try {
				RetuenLIST = inOutService.getAndroidModelRetuen("", "", LOGC, BONBU, CENTER, "", "");
				androidVo.TABSIZE = RetuenLIST.size();
				androidVo.IT_TAB1 = RetuenLIST;
				
				androidVo.field1 = UserInfo.getMUT_POSITION();
				androidVo.field2 = LOGC;
				androidVo.field3 = BONBU;
				androidVo.field4 = CENTER;
				androidVo.field5 = DOCNO;
				androidVo.field6 = CCODE;
				androidVo.field7 = MODEL;
				androidVo.returnCode = "0000";
				androidVo.returnMsg = "";
			} catch (Exception e) {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "에러발생! 다시 시도해주세요.";
			}
		}
		else {
			androidVo.returnCode = "1008";
			androidVo.returnMsg = msg;
		}
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	
	@RequestMapping(value = "HTTP_VERSION", method = RequestMethod.POST, produces="application/text; charset=utf8")	
	public @ResponseBody String HTTP_VERSION(HttpServletRequest request) {
		
		AndroidVo androidVo = new AndroidVo();
		String PATH = fileService.filePath(request, "APK");
		if(PATH.length() == 0) {
			androidVo.returnCode = "2109";
			androidVo.returnMsg = "업데이트 파일목록을 가져오지 못했습니다.";
			Gson gson = new Gson();
			String json = gson.toJson(androidVo);
			return json;
		}
		
		String VERSION = "001.000.000.000";
		String VFILE = "";
		try {
			File UpdateList = new File(PATH);
			if(UpdateList.exists()) {
				File filterResult[] = UpdateList.listFiles();
				if(filterResult.length > 0) {
					Arrays.sort(filterResult);
					for (int i = (filterResult.length - 1); i >= 0; i--)
					{
						File file = filterResult[i];
						String FNAME = file.getName();
						if (file.isFile() && FNAME.length() >= 20 && FNAME.indexOf("_") >= 0) {
							VERSION = FNAME.substring(FNAME.indexOf("_") + 1, FNAME.lastIndexOf("."));
							VFILE = FNAME;
							break;
						}
					}
				}
			}
			androidVo.field1 = VERSION;
			androidVo.field2 = VFILE;
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "버전을 가져오지 못했습니다.";
		}
		Gson gson = new Gson();
		String json = gson.toJson(androidVo);
		return json;
	}
	@RequestMapping(value="/HTTP_UPDATE")	
	public ModelAndView HTTP_UPDATE(
			@RequestParam(value = "sysFile", defaultValue = "") String sysFile,
			HttpServletRequest request) {
		
		String fullPath = fileService.filePath(request, "APK") + "/" + sysFile;
		File file = new File(fullPath);

		return new ModelAndView("DownloadView", "downloadFile", file);
	}
}
