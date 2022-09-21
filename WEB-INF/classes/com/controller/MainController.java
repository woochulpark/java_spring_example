package com.controller;

import org.springframework.http.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.service.CommonService;
import com.service.FileService;
import com.service.NoticeService;
import com.service.UserService;
import com.vo.NoticeVo;
import com.vo.UserVo;

@Controller
public class MainController {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "index")
	public ModelAndView index(HttpServletRequest request,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			@RequestParam("id") String id, @RequestParam("password") String pw) {
		Map<String, Object> login_result = new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView();
		boolean logChk = userService.login_process(id, pw, login_result);
		if(!logChk) {
			mav.setViewName("redirect:/index.do");
			mav.addObject("msg","DB에러! DB 연결상태를 확인해주세요.");
			return mav;
		}
		String result = (String) login_result.get("result");
		if (result.equals("pass")) {
			mav.setViewName("redirect:/main.do");
			request.getSession().setAttribute("user_id", id.toUpperCase());
		}
		else {
			mav.setViewName("redirect:/index.do");
			mav.addObject("msg", login_result.get("msg"));
		}
		return mav;
	}
	
	@RequestMapping(value = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		request.getSession().invalidate();
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping(value = "main")
	public ModelAndView main(HttpServletRequest request,
			@RequestParam(value = "PAGE", defaultValue = "1") int PAGE,
			@RequestParam(value = "msg", defaultValue = "") String msg) {
		
		ModelAndView mav = new ModelAndView();
		if(userService.getUserID(request).equals("")){
			mav.setViewName("index");
			return mav;
		}
		
		List<NoticeVo> NoticeList = noticeService.getNoticeMainList(PAGE, 10, userService.getUserID(request));
		List<NoticeVo> PopupList = noticeService.getNoticeMainPopup(userService.getUserID(request));
		int listCnt = NoticeList.size() == 0 ? 0 : NoticeList.get(0).getTotalCnt();
		CommonService common = new CommonService();
		common.SetPaging(listCnt, PAGE);
		
		mav.setViewName("main");
		mav.addObject("user_id", userService.getUserID(request));
		mav.addObject("NoticeList", NoticeList);
		mav.addObject("NoticeListSize", NoticeList.size());
		mav.addObject("PopupList", PopupList);
		mav.addObject("PopupListSize", PopupList.size());
		mav.addObject("pagination", common);
		mav.addObject("pageNum", PAGE);
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping("/menu_top.do")
	public ModelAndView menu_top(HttpServletRequest request,
			@RequestParam("user_id") String user_id) {

		UserVo Userinfo = userService.getUserInfo(user_id);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("menu_top");
		mav.addObject("Userinfo", Userinfo);
		return mav;
	}

	@RequestMapping("/menu_list.do")
	public ModelAndView menu_list(HttpServletRequest request,
			@RequestParam("user_id") String user_id) {

		ModelAndView mav = new ModelAndView();
		String level = "";
		String id = userService.getUserID(request);
		if(id.length() > 0) {
			UserVo userVo = userService.getUserInfo(id);
			if(userVo != null)
				level = userVo.getMUT_POSITION();
		}
		mav.setViewName("menu_list");
		mav.addObject("user_id", id);
		mav.addObject("level", level);
		return mav;
	}
	
	@RequestMapping(value="/download")	
	public ModelAndView download(
			@RequestParam(value = "sysFile", defaultValue = "") String sysFile,
			@RequestParam(value = "orgFile", defaultValue = "") String orgFile,
			@RequestParam(value = "PATH", defaultValue = "") String PATH,
			HttpServletRequest request) {
		
		String fullPath = fileService.filePath(request, PATH) + "/" + sysFile;
		File file = new File(fullPath);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("DownloadView");
		mav.addObject("downloadFile", file);
		mav.addObject("orgFile", orgFile);
		return mav;
	}
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ResponseEntity<byte[]> displayFile(HttpServletRequest request, @RequestParam("PATH") String PATH, @RequestParam("ORGFILE") String ORGFILE, @RequestParam("FILE") String FILE)throws Exception{
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		String uploadPath = fileService.filePath(request, PATH);
		//logger.info("FILE NAME : " + fileName);
		try {
			String formatName = FILE.substring(FILE.lastIndexOf(".")+1);
			MediaType mType = fileService.getType(formatName);
			HttpHeaders headers = new HttpHeaders();
			in = new FileInputStream(uploadPath+"/"+FILE);
			
			//step: change HttpHeader ContentType
			if(mType != null) {
				//image file(show image)
				headers.setContentType(mType);
			}else {
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; filename=\"" + new String(ORGFILE.getBytes("UTF-8"), "ISO-8859-1")+"\""); 
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);

		}catch(Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}finally {
			in.close();
		}
		return entity;
	}
}
