package com.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dao.NoticeDao;
import com.vo.BonbuVo;
import com.vo.LogcInVo;
import com.vo.NoticeVo;
import com.vo.UserVo;

@Service
public class NoticeService {
	
	@Autowired
	NoticeDao noticeDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	ApplicationContext applicationContext;
	
	public NoticeVo getNoticeInfo(String DOCNO){
		return noticeDao.getNoticeInfo(DOCNO);
	}
	
	public List<NoticeVo> getNoticeList(String TITLE, int PAGE, int PSIZE, String USERID){
		return noticeDao.getNoticeList(TITLE, PAGE, PSIZE, USERID);
	}
	
	public List<NoticeVo> getNoticeMainList(int PAGE, int PSIZE, String USERID){
		return noticeDao.getNoticeMainList(PAGE, PSIZE, USERID);
	}
	
	public NoticeVo getAndroidNoticeInfo(String DOCNO) throws Exception{
		return noticeDao.getAndroidNoticeInfo(DOCNO);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidFileList(String DOCNO) throws Exception{
		return noticeDao.getAndroidFileList(DOCNO);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidNoticeMainList(String USERID) throws Exception{
		return noticeDao.getAndroidNoticeMainList(USERID);
	}
	
	public List<NoticeVo> getNoticeMainPopup(String USERID){
		return noticeDao.getNoticeMainPopup(USERID);
	}
	
	public List<UserVo> getSusinList(String DOCNO){
		return noticeDao.getSusinList(DOCNO);
	}
	
	public List<NoticeVo> getNoticeFileList(String DOCNO){
		try {
			return noticeDao.getNoticeFileList(DOCNO);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public NoticeVo getNoticeFileInfo(String DOCNO, String SEQ) throws Exception{
		return noticeDao.getNoticeFileInfo(DOCNO, SEQ);
	}
	
	public List<NoticeVo> getNoticeUser(String DOCNO){
		return noticeDao.getNoticeUser(DOCNO);
	}
	
	public int insertSusinList(List<NoticeVo> list){
		try {
			return noticeDao.insertSusinList(list);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int insertNotice(NoticeVo vo){
		try {
			NoticeVo SeqVo = noticeDao.getLastNoticeDocNo();
			int SEQ = SeqVo.getMNI_SEQ() + 1;
			String NOWDATE = SeqVo.getMNI_DATE();
			String BASE = NOWDATE + "00";
			String DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");
			System.out.println("MODEL SEQ = " + SEQ + ", BASE = " + BASE + " DOCNO  = " + DOCNO);
			vo.setMNI_DOCNO(DOCNO);
			vo.setMNI_DATE(NOWDATE);
			vo.setMNI_SEQ(SEQ);
			return noticeDao.insertNotice(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int insertNoticeFile(NoticeVo vo, MultipartFile file, HttpServletRequest request){
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			noticeDao.insertNoticeFile(vo);
			String NOTICE_FILE = vo.getMDF_DOCNO() + vo.getMDF_SEQ() + vo.getMDF_FILE_Org();
			int fileresult = fileService.fileUpload(file, "", "", NOTICE_FILE, request);
			if(fileresult == -1)
			{
				throw new Exception();
			}
			else if(fileresult == -2)
			{
				throw new Exception();
			}
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -1;
		}
	}
	
	public int updateNotice(NoticeVo vo){
		try {
			return noticeDao.updateNotice(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int updateNoticeUser(String DOCNO, String USERID){
		try {
			return noticeDao.updateNoticeUser(DOCNO, USERID);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteNoticeFile(String DOCNO, String SEQ){
		try {
			return noticeDao.deleteNoticeFile(DOCNO, SEQ);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteSusinList(String DOCNO){
		try {
			return noticeDao.deleteSusinList(DOCNO);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteNoticeUser(String DOCNO){
		try {
			return noticeDao.deleteNoticeUser(DOCNO);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteNotice(String DOCNO){
		try {
			return noticeDao.deleteNotice(DOCNO);
		}
		catch (Exception e) {
			return -1;
		}
	}
}
