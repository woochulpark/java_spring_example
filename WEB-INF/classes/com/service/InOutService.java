package com.service;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.jdbc.Null;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.internal.Console;
import com.dao.CenterDao;
import com.dao.EventDao;
import com.dao.InOutDao;
import com.dao.ModelDao;
import com.vo.AndroidVo;
import com.vo.BonbuStatusVo;
import com.vo.BonbuVo;
import com.vo.CenterVo;
import com.vo.CfmVo;
import com.vo.EventModelVo;
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
import com.vo.PriceInVo;
import com.vo.PriceOutVo;
import com.vo.UserVo;

@Service
public class InOutService {
	@Autowired
	private ModelDao modelDao;
	
	@Autowired
	private InOutDao inOutDao;
	
	@Autowired
	private CenterDao centerDao;
	
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private FileService fileService;
	
	public LogcInVo getLogcInInfo(String DOCNO) {
		return inOutDao.getLogcInInfo(DOCNO);
	}
	
	public List<LogcInVo> getLogcInList(String StartDay, String EndDay, String LOGC, String MNAME, String BISSTS, int PAGE, int PSIZE){
		return inOutDao.getLogcInList(StartDay, EndDay, LOGC, MNAME, BISSTS, PAGE, PSIZE);
	}
	
	public int insertLogcIn(LogcInVo vo){
		try {
			LogcInVo SeqVo = inOutDao.getLastLogcInDocNo();
			int SEQ = SeqVo.getDIL_SEQ() + 1;
			String NOWDATE = SeqVo.getCMN_MAK_DATE();
			String BASE = NOWDATE + "01";
			String DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");
			vo.setDIL_DOCNO(DOCNO);
			vo.setDIL_SEQ(SEQ);
			int result = inOutDao.insertLogcIn(vo);
			return result;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int updateLogcIn(LogcInVo vo){
		try {
			int result = inOutDao.updateLogcIn(vo);
			return result;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteLogcIn(String DOCNO){
		try {
			int result = inOutDao.deleteLogcIn(DOCNO);
			return result;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public List<LogcInVo> getBonbuInList(String InStartDay, String InEndDay, String OutStartDay, String OutEndDay, String LOGC, String MNAME, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("InStartDay", InStartDay);
		map.put("InEndDay", InEndDay);
		map.put("OutStartDay", OutStartDay);
		map.put("OutEndDay", OutEndDay);
		map.put("MNAME", MNAME);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return inOutDao.getBonbuInList(map);
	}
	
	public List<BonbuVo> getBonbuInInfo(String LOGC, String DOCNO, String MODEL){
		return inOutDao.getBonbuInInfo(LOGC, DOCNO, MODEL);
	}
	
	public List<BonbuVo> getBonbuInCheck(String DOCNO){
		return inOutDao.getBonbuInCheck(DOCNO);
	}
	
	public int updateModelJego(String MODEL, String CENTER, int JEGO, String PROG, String USERID){
		try {
			int result = inOutDao.updateModelJego(MODEL, CENTER, JEGO, PROG, USERID);
			return result;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int insertBonbuIn(String TOPDOCNO, String MODEL, String OutDATE, String[] CENTERLIST, String[] OUTCNTLIST, String USERID){
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			for(int i = 0; i < CENTERLIST.length; ++i) {
				String TCENTER = CENTERLIST[i];
				String TOUTCNT = OUTCNTLIST[i];
				if(TCENTER.length() == 0)
				{
					throw new Exception();
				}
				if(TOUTCNT.length() == 0 || TOUTCNT.equals("0"))
				{
					continue;
				}
				int i_OUTCNT = 0;
				try {
					i_OUTCNT = Integer.parseInt(TOUTCNT);
				}
				catch (Exception e) {
					throw new Exception();
				}
				BonbuVo vo = new BonbuVo();
				vo.setDBI_TOPDOCNO(TOPDOCNO);
				vo.setDBI_CENTER(TCENTER);
				vo.setDBI_QTY(i_OUTCNT);
				vo.setDBI_BisSts("N");
				vo.setCMN_MAK_PROG("BonbuIn_Add");
				vo.setCMN_MAK_ID(USERID);
				vo.setCMN_UPD_PROG("BonbuIn_Add");
				vo.setCMN_UPD_ID(USERID);
				BonbuVo SeqVo = inOutDao.getLastBonbuInDocNo();
				int SEQ = SeqVo.getDBI_SEQ() + 1;
				String NOWDATE = SeqVo.getCMN_MAK_DATE();
				String BASE = NOWDATE + "02";
				String DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");
				vo.setDBI_DOCNO(DOCNO);
				vo.setDBI_SEQ(SEQ);
				if(inOutDao.insertBonbuIn(vo, MODEL, OutDATE) <= 0)
					throw new Exception();
			}
			if(inOutDao.updateOutLogcIn(TOPDOCNO,"Y",OutDATE,"BonbuIn_Add",USERID) <= 0)
				throw new Exception();
			LogcInVo LogcInInfo = getLogcInInfo(TOPDOCNO);
			if(LogcInInfo == null)
			{
				throw new Exception();
			}
			if(!LogcInInfo.getOUTTOTAL().equals(String.valueOf(LogcInInfo.getDIL_QTY())))
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
	public int updateBonbuIn(String TOPDOCNO, String[] CENTERLIST, String[] OUTCNTLIST, String USERID){
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		
		try {
			transaction.start();
			for(int i = 0; i < CENTERLIST.length; ++i) {
				String TCENTER = CENTERLIST[i];
				String TOUTCNT = OUTCNTLIST[i];
				if(TCENTER.length() == 0)
				{
					throw new Exception();
				}
				int i_OUTCNT = Integer.parseInt(TOUTCNT);
				BonbuVo SeqVo = inOutDao.getLastBonbuInDocNo();
				int SEQ = SeqVo.getDBI_SEQ() + 1;
				String NOWDATE = SeqVo.getCMN_MAK_DATE();
				String BASE = NOWDATE + "02";
				String DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");
				int result = inOutDao.updateBonbuIn(TOPDOCNO, TCENTER, i_OUTCNT, "N", DOCNO, SEQ, "BonbuIn_Modify", USERID);
				if(result < 0)
					throw new Exception();
			}
			LogcInVo LogcInInfo = getLogcInInfo(TOPDOCNO);
			if(LogcInInfo == null)
			{
				throw new Exception();
			}
			if(!LogcInInfo.getOUTTOTAL().equals(String.valueOf(LogcInInfo.getDIL_QTY())))
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
	
	public int deleteBonbuIn(String DOCNO, String USERID) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			int BonbuCount = inOutDao.getBonbuInCount(DOCNO);
			int result = inOutDao.deleteBonbuIn(DOCNO);
			if(BonbuCount != result)
				throw new Exception();
			else {
				if(inOutDao.updateOutLogcIn(DOCNO,"N",null,"BonbuIn_Delte",USERID) <= 0)
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
	
	public OrderVo getOrderInfo(String DOCNO) {
		return inOutDao.getOrderInfo(DOCNO);
	}
	
	public List<OrderVo> getOrderList(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String MNAME, int PAGE, int PSIZE){
		return inOutDao.getOrderList(StartDay, EndDay, LOGC, BONBU, CENTER, MNAME, PAGE, PSIZE);
	}
	
	public int insertOrder(OrderVo orderVo, String center, String USER, String ORDERDATE, String CMNT, String MODEL, int QTY, String USERID){
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			OrderVo SeqVo = inOutDao.getLastOrderDocNo();
			int SEQ = SeqVo.getDBO_SEQ() + 1;
			String NOWDATE = SeqVo.getCMN_MAK_DATE();
			String BASE = NOWDATE + "03";
			String DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");
			orderVo.setDBO_DOCNO(DOCNO);
			orderVo.setDBO_SEQ(SEQ);
			orderVo.setDBO_CENTER(center);
			orderVo.setDBO_USER(USER);
			orderVo.setDBO_DATE(ORDERDATE);
			orderVo.setDBO_QTY(QTY);
			orderVo.setDBO_BisSts("N");
			orderVo.setDBO_MODEL(MODEL);
			orderVo.setDBO_CMNT(CMNT);
			orderVo.setCMN_MAK_PROG("Order_Add");
			orderVo.setCMN_MAK_ID(USERID);
			orderVo.setCMN_UPD_PROG("Order_Add");
			orderVo.setCMN_UPD_ID(USERID);
			if(inOutDao.insertOrder(orderVo)<= 0)
				throw new Exception();
			if(inOutDao.insertMove(orderVo)<= 0)
				throw new Exception();
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -1;
		}
	}
	
	public int updateOrder(String LEVEL, String DOCNO, String ORDERDATE, int QTY, String CMNT, String PROG, String USERID) {
		try {
			OrderVo orderVo = new OrderVo();
			orderVo.setDBO_DOCNO(DOCNO);
			orderVo.setDBO_DATE(ORDERDATE);
			orderVo.setDBO_QTY(QTY);
			orderVo.setDBO_CMNT(CMNT);
			orderVo.setCMN_UPD_PROG(PROG);
			orderVo.setCMN_UPD_ID(USERID);
			orderVo.setLEVEL(LEVEL);
			int result = inOutDao.updateOrder(orderVo);
			return result;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteOrder(String LEVEL, String DOCNO) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("DOCNO", DOCNO);
			map.put("LEVEL", LEVEL);
			if(inOutDao.deleteOrder(map) <= 0)
				throw new Exception();
			if(inOutDao.deleteMOVE(map) <= 0)
				throw new Exception();
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -1;
		}
	}
	
	public List<MoveVo> getBonbuMOVEList(UserVo UserInfo, String CTYPE, String LOGC, String BONBU, String CENTER, String INLOGC, String INBONBU, String INCENTER, String OutStartDay, String OutEndDay, String InStartDay, String InEndDay, String MNAME, int PAGE, int PSIZE){
		if(!UserInfo.getMUT_POSITION().equals("001") && !UserInfo.getMUT_POSITION().equals("002")) {
			if(CTYPE.equals("N") ) {
				INLOGC = "";
				INBONBU = "";
				INCENTER = "";
			}
			else {
				INLOGC = LOGC;
				INBONBU = BONBU;
				INCENTER = CENTER;
				LOGC = "";
				BONBU = "";
				CENTER = "";
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("INLOGC", INLOGC);
		map.put("INBONBU", INBONBU);
		map.put("INCENTER", INCENTER);
		map.put("OutStartDay", OutStartDay);
		map.put("OutEndDay", OutEndDay);
		map.put("InStartDay", InStartDay);
		map.put("InEndDay", InEndDay);
		map.put("MNAME", MNAME);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		map.put("CTYPE", CTYPE);
		if(!UserInfo.getMUT_POSITION().equals("001") && !UserInfo.getMUT_POSITION().equals("002"))
			return inOutDao.getBonbuMOVEList2(map);
		else
			return inOutDao.getBonbuMOVEList(map);
	}
	
	public MoveVo getBonbuMOVEInfo(String DOCNO) {
		return inOutDao.getBonbuMOVEInfo(DOCNO);
	}
	
	public MoveVo getJegoModel(String MODEL, String CENTER, String NOWDATE) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MODEL", MODEL);
		map.put("CENTER", CENTER);
		map.put("NOWDATE", NOWDATE);
		return inOutDao.getJegoModel(map);
	}
	
	public int updateMoveCFM(String DOCNO, String CENTER, String OUTDATE, String OUTQTY, String HOWMOVE, String CMNT, String PROG, String USERID) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("CENTER", CENTER);
		map.put("OUTDATE", OUTDATE);
		map.put("OUTQTY", OUTQTY);
		map.put("HOWMOVE", HOWMOVE);
		map.put("CMNT", CMNT);
		map.put("PROG", PROG);
		map.put("USERID", USERID);
		try {
			return inOutDao.updateMoveCFM(map);
		} catch (Exception e) {
			return -1;
		}
	}
	
	public int getJegoChk(String MODEL, String CENTER, String NOWDATE, int QTY, Map<String, Integer> map){
		MoveVo moveVo = getJegoModel(MODEL, CENTER, NOWDATE);
		if(moveVo == null) {
			map = null;
			return -1;
		}
		int NOWJEGO = moveVo.getDMJ_JEGO();
		int OUTJEGO = moveVo.getOUTJEGO();
		map.put("NOWJEGO", NOWJEGO);
		map.put("OUTJEGO", OUTJEGO);
		map.put("OUTQTY", QTY);
		if(OUTJEGO < QTY) {
			return 0;
		}
		else {
			if(NOWJEGO < QTY) {
				return 0;
			}
			else { 
				return 1;
			}
		}
	}
	public int getJegoChk(String MODEL, String CENTER, String NOWDATE, int QTY) throws Exception{
		MoveVo moveVo = getJegoModel(MODEL, CENTER, NOWDATE);
		if(moveVo == null) {
			return -1;
		}
		int NOWJEGO = moveVo.getDMJ_JEGO();
		int OUTJEGO = moveVo.getOUTJEGO();
		if(OUTJEGO < QTY) {
			return 0;
		}
		else {
			if(NOWJEGO < QTY) {
				return 0;
			}
			else { 
				return 1;
			}
		}
	}
	public int updateMove(String DOCNO, String USERID, String HOWMOVE, String CMNT, String PROG, String MODEL, String DBMDATE, int QTY, String CENTER, String CFM) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			MoveVo vo = new MoveVo();
			vo.setDBM_DOCNO(DOCNO);
			vo.setDBM_CENTER(CENTER);
			vo.setDBM_DATE(DBMDATE);
			vo.setDBM_QTY(QTY);
			vo.setDBM_UESR(USERID);
			vo.setDBM_HOWMOVE(HOWMOVE);
			vo.setDBM_CMNT(CMNT);
			vo.setDBM_CFM(CFM);
			vo.setCMN_UPD_PROG(PROG);
			vo.setCMN_UPD_ID(USERID);
			//MoveVo JegoVo = getJegoModel(MODEL, CENTER, DBMDATE);
			int result = getJegoChk(MODEL, CENTER, DBMDATE, QTY);
			if(result != 1)
				throw new Exception();
			result = inOutDao.updateMove(vo);
			if(result <= 0)
				throw new Exception();
			result = inOutDao.insertInOut(DOCNO, MODEL, "2", DBMDATE, QTY, "2", CENTER, PROG, USERID);
			if(result <= 0)
				throw new Exception();
			result = inOutDao.updateChulhaJego(MODEL,CENTER, QTY, DBMDATE);
			if(result <= 0)
				throw new Exception();
			result = insertPriceOut(DOCNO, CENTER, MODEL, QTY, USERID, PROG);
			if(result <= 0)
				throw new Exception();
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -1;
		}
	}
	
	public List<CfmVo> getInCFMList(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String MNAME, String INSTS, String CFMSTS, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("MNAME", MNAME);
		map.put("INSTS", INSTS);
		map.put("CFMSTS", CFMSTS);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return inOutDao.getInCFMList(map);
	}
	
	public CfmVo getInCFMLogcInfo(String DOCNO) throws Exception{
		return inOutDao.getInCFMLogcInfo(DOCNO);
	}
	
	public CfmVo getInCFMBonbuInfo(String DOCNO) throws Exception{
		return inOutDao.getInCFMBonbuInfo(DOCNO);
	}
	
	public int insertPriceBonbuIn(String DOCNO, String PROG, String USERID) throws Exception{
		PriceInVo vo = new PriceInVo();
		vo.setDPI_DOCNO(DOCNO);
		vo.setCMN_MAK_PROG(PROG);
		vo.setCMN_MAK_ID(USERID);
		vo.setCMN_UPD_PROG(PROG);
		vo.setCMN_UPD_ID(USERID);
		return inOutDao.insertPriceBonbuIn(vo);
	}
	
	public int insertPriceMoveIn(String DOCNO, String PROG, String USERID) throws Exception{
		PriceOutVo vo = new PriceOutVo();
		vo.setDPO_DOCNO(DOCNO);
		vo.setCMN_MAK_PROG(PROG);
		vo.setCMN_MAK_ID(USERID);
		vo.setCMN_UPD_PROG(PROG);
		vo.setCMN_UPD_ID(USERID);
		return inOutDao.insertPriceMoveIn(vo);
	}
	
	public int updateCfm(String CFMsts, String DOCNO, String INTYPE, String USERID, String DIC_DATE, String CMNT, String PROG, List<MultipartFile> fileList, HttpServletRequest request) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			CfmVo vo = new CfmVo();
			vo.setDIC_DOCNO(DOCNO);
			vo.setDIC_USER(USERID);
			if(INTYPE.equals("0"))
				vo.setDIC_RESN("1");
			else
				vo.setDIC_RESN("2");
			vo.setDIC_DATE(DIC_DATE);
			vo.setDIC_CMNT(CMNT);
			vo.setCMN_MAK_PROG(PROG);
			vo.setCMN_MAK_ID(USERID);
			vo.setCMN_UPD_PROG(PROG);
			vo.setCMN_UPD_ID(USERID);
			if(CFMsts.equals("N")) {
				int result = inOutDao.insertCfm(vo);
				if(result <= 0)
					throw new Exception();

				if(INTYPE.equals("0")) {
					result = inOutDao.insertCfmLogcInOut(DOCNO, USERID);
					if(result <= 0)
						throw new Exception();

					result = inOutDao.updateInBonbuCfm(DOCNO);
					if(result <= 0)
						throw new Exception();
					
					result = inOutDao.updateInLogcJego(DOCNO);
					if(result <= 0)
						throw new Exception();

					result = insertPriceBonbuIn(DOCNO, PROG, USERID);
					if(result <= 0)
						throw new Exception();
				}
				else
				{
					result = inOutDao.insertCfmBonbuInOut(DOCNO, USERID);
					if(result <= 0)
						throw new Exception();

					result = inOutDao.updateOrderCfm(DOCNO);
					if(result <= 0)
						throw new Exception();

					result = inOutDao.updateInBonbuJego(DOCNO);
					if(result <= 0)
						throw new Exception();
					
					result = insertPriceMoveIn(DOCNO, PROG, USERID);
					if(result <= 0)
						throw new Exception();
				}
			}
			else {
				int result = inOutDao.updateCfm(vo);
				if(result <= 0)
					throw new Exception();
			}
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
						filechk = false;
					}
				}
			}
			if(!filechk)
				return -2;
			
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -1;
		}
	}
	
	public List<OtherVo> getOtherList(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String MNAME, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("MNAME", MNAME);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return inOutDao.getOtherList(map);
	}
	
	public int insertOther(OtherVo vo, String CENTER, String MODEL, String OUTDATE, int QTY, String OUTCMNT, String CMNT, String USERID, String PROG, List<MultipartFile> fileList, HttpServletRequest request) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			OtherVo SeqVo = inOutDao.getLastOtherDocNo();
			int SEQ = SeqVo.getDOO_SEQ() + 1;
			String NOWDATE = SeqVo.getCMN_MAK_DATE();
			String BASE = NOWDATE + "04";
			String DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");
			vo.setDOO_DOCNO(DOCNO);
			vo.setDOO_SEQ(SEQ);
			vo.setDOO_MODEL(MODEL);
			vo.setDOO_CENTER(CENTER);
			vo.setDOO_DATE(OUTDATE);
			vo.setDOO_QTY(QTY);
			vo.setDOO_OUTCMNT(OUTCMNT);
			vo.setDOO_CMNT(CMNT);
			vo.setCMN_MAK_PROG(PROG);
			vo.setCMN_MAK_ID(USERID);
			vo.setCMN_UPD_PROG(PROG);
			vo.setCMN_UPD_ID(USERID);
			int result = getJegoChk(MODEL, CENTER, OUTDATE, QTY);
			if(result != 1)
				throw new Exception();
			result = inOutDao.insertOther(vo);
			if(result <= 0)
				throw new Exception();
			result = inOutDao.insertInOut(DOCNO, MODEL, "2", OUTDATE, QTY, "3", CENTER, PROG, USERID);
			if(result <= 0)
				throw new Exception();
			result = inOutDao.updateChulhaJego(MODEL, CENTER, QTY, OUTDATE);
			if(result <= 0)
				throw new Exception();
			result = insertPriceOut(DOCNO, CENTER, MODEL, QTY, USERID, PROG);
			if(result <= 0)
				throw new Exception();
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
						filechk = false;
					}
				}
			}
			if(!filechk)
				return -2;
			
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -1;
		}
	}
	
	public OtherVo getOtherInfo(String DOCNO){
		return inOutDao.getOtherInfo(DOCNO);
	}
	
	public int getInReport(String LOGC, String BONBU, String CENTER, String LEVEL, List<InoutReportVo> IN_LIST, String MODEL, int YEAR){
		try {
			String StartDay = YEAR + "-01-01";
			String EndDay = YEAR + "-12-31";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("INOUT_TYPE", "1");
			map.put("StartDay", StartDay);
			map.put("EndDay", EndDay);
			map.put("MODEL", MODEL);
			map.put("LOGC", LOGC);
			map.put("BONBU", BONBU);
			map.put("CENTER", CENTER);
			map.put("LEVEL", LEVEL);
			List<InoutReportVo> JEGOList = inOutDao.getJegoModelReport(map);
			List<InoutReportVo> InOutList = inOutDao.getInoutReportCenter(map);
			List<CenterVo> CenterList = centerDao.getAllList(map);
			if(CenterList == null || CenterList.size() == 0) {
				return 0;
			}
			InoutReportVo TotalData = null;
			int BonbuCnt = 0;
			for(int i = 0; i < CenterList.size(); ++i) {
				CenterVo centerVo = CenterList.get(i);
				InoutReportVo ReData = new InoutReportVo();
				ReData.setMCI_CODE(centerVo.getMCI_CODE());
				ReData.setLOGC(centerVo.getMCI_LOGC());
				ReData.setBONBU(centerVo.getMCI_Bonbu());
				ReData.setCENTER(centerVo.getMCI_Center());
				ReData.setLOGCNAME(centerVo.getMCI_LOGCNAME());
				ReData.setBONBUNAME(centerVo.getMCI_BonbuNAME());
				ReData.setCENTERNAME(centerVo.getMCI_CenterName());
				int JEGO = 0;
				if(JEGOList != null && JEGOList.size() > 0) {
					for(InoutReportVo jegoVo : JEGOList) {
						if(jegoVo.getDMJ_CENTER().equals(centerVo.getMCI_CODE())) {
							JEGO = jegoVo.getJEGO();
						}
					}
				}
				ReData.setJEGO(JEGO);
				if(InOutList != null && InOutList.size() > 0) {
					for(InoutReportVo inoutVo : InOutList) {
						if(inoutVo.getDIB_CENTER().equals(centerVo.getMCI_CODE())) {
							ReData.setDIB_IOTYPE("1");
							switch (inoutVo.getMONTH()) {
							case 1:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM1LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM1BONBU(inoutVo.getCNT());
								}
								break;
							case 2:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM2LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM2BONBU(inoutVo.getCNT());
								}
								break;
							case 3:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM3LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM3BONBU(inoutVo.getCNT());
								}
								break;
							case 4:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM4LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM4BONBU(inoutVo.getCNT());
								}
								break;
							case 5:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM5LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM5BONBU(inoutVo.getCNT());
								}
								break;
							case 6:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM6LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM6BONBU(inoutVo.getCNT());
								}
								break;
							case 7:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM7LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM7BONBU(inoutVo.getCNT());
								}
								break;
							case 8:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM8LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM8BONBU(inoutVo.getCNT());
								}
								break;
							case 9:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM9LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM9BONBU(inoutVo.getCNT());
								}
								break;
							case 10:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM10LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM10BONBU(inoutVo.getCNT());
								}
								break;
							case 11:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM11LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM11BONBU(inoutVo.getCNT());
								}
								break;
							case 12:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setINM12LOGC(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setINM12BONBU(inoutVo.getCNT());
								}
								break;
							default:
								break;
							}
						}
					}
				}
				ReData.setINM1TOTAL(ReData.getINM1LOGC() + ReData.getINM1BONBU());
				ReData.setINM2TOTAL(ReData.getINM2LOGC() + ReData.getINM2BONBU());
				ReData.setINM3TOTAL(ReData.getINM3LOGC() + ReData.getINM3BONBU());
				ReData.setINM4TOTAL(ReData.getINM4LOGC() + ReData.getINM4BONBU());
				ReData.setINM5TOTAL(ReData.getINM5LOGC() + ReData.getINM5BONBU());
				ReData.setINM6TOTAL(ReData.getINM6LOGC() + ReData.getINM6BONBU());
				ReData.setINM7TOTAL(ReData.getINM7LOGC() + ReData.getINM7BONBU());
				ReData.setINM8TOTAL(ReData.getINM8LOGC() + ReData.getINM8BONBU());
				ReData.setINM9TOTAL(ReData.getINM9LOGC() + ReData.getINM9BONBU());
				ReData.setINM10TOTAL(ReData.getINM10LOGC() + ReData.getINM10BONBU());
				ReData.setINM11TOTAL(ReData.getINM11LOGC() + ReData.getINM11BONBU());
				ReData.setINM12TOTAL(ReData.getINM12LOGC() + ReData.getINM12BONBU());
				ReData.setINSUBLOGCTOTAL(
						ReData.getINM1LOGC() + 
						ReData.getINM2LOGC() + 
						ReData.getINM3LOGC() + 
						ReData.getINM4LOGC() + 
						ReData.getINM5LOGC() + 
						ReData.getINM6LOGC() + 
						ReData.getINM7LOGC() + 
						ReData.getINM8LOGC() + 
						ReData.getINM9LOGC() + 
						ReData.getINM10LOGC() + 
						ReData.getINM11LOGC() + 
						ReData.getINM12LOGC()
						);
				ReData.setINSUBBONBUTOTAL(
						ReData.getINM1BONBU() + 
						ReData.getINM2BONBU() + 
						ReData.getINM3BONBU() + 
						ReData.getINM4BONBU() + 
						ReData.getINM5BONBU() + 
						ReData.getINM6BONBU() + 
						ReData.getINM7BONBU() + 
						ReData.getINM8BONBU() + 
						ReData.getINM9BONBU() + 
						ReData.getINM10BONBU() + 
						ReData.getINM11BONBU() + 
						ReData.getINM12BONBU()
						);
				ReData.setINSUBTOTAL(ReData.getINSUBLOGCTOTAL() + ReData.getINSUBBONBUTOTAL());
				
				int ReType = 1;
				if(TotalData == null) {
					TotalData = (InoutReportVo)ReData.clone();
					BonbuCnt = 0;
					ReType = 1;
				}
				else {
					if(TotalData.getLOGC().equals(ReData.getLOGC()) && TotalData.getBONBU().equals(ReData.getBONBU())) {
						TotalData.setJEGO(TotalData.getJEGO() + ReData.getJEGO());
						TotalData.setINSUBTOTAL(TotalData.getINSUBTOTAL() + ReData.getINSUBTOTAL());
						TotalData.setINSUBLOGCTOTAL(TotalData.getINSUBLOGCTOTAL() + ReData.getINSUBLOGCTOTAL());
						TotalData.setINSUBBONBUTOTAL(TotalData.getINSUBBONBUTOTAL() + ReData.getINSUBBONBUTOTAL());
						TotalData.setINM1TOTAL(TotalData.getINM1TOTAL() + ReData.getINM1TOTAL());
						TotalData.setINM1LOGC(TotalData.getINM1LOGC() + ReData.getINM1LOGC());
						TotalData.setINM1BONBU(TotalData.getINM1BONBU() + ReData.getINM1BONBU());
						TotalData.setINM2TOTAL(TotalData.getINM2TOTAL() + ReData.getINM2TOTAL());
						TotalData.setINM2LOGC(TotalData.getINM2LOGC() + ReData.getINM2LOGC());
						TotalData.setINM2BONBU(TotalData.getINM2BONBU() + ReData.getINM2BONBU());
						TotalData.setINM3TOTAL(TotalData.getINM3TOTAL() + ReData.getINM3TOTAL());
						TotalData.setINM3LOGC(TotalData.getINM3LOGC() + ReData.getINM3LOGC());
						TotalData.setINM3BONBU(TotalData.getINM3BONBU() + ReData.getINM3BONBU());
						TotalData.setINM4TOTAL(TotalData.getINM4TOTAL() + ReData.getINM4TOTAL());
						TotalData.setINM4LOGC(TotalData.getINM4LOGC() + ReData.getINM4LOGC());
						TotalData.setINM4BONBU(TotalData.getINM4BONBU() + ReData.getINM4BONBU());
						TotalData.setINM5TOTAL(TotalData.getINM5TOTAL() + ReData.getINM5TOTAL());
						TotalData.setINM5LOGC(TotalData.getINM5LOGC() + ReData.getINM5LOGC());
						TotalData.setINM5BONBU(TotalData.getINM5BONBU() + ReData.getINM5BONBU());
						TotalData.setINM6TOTAL(TotalData.getINM6TOTAL() + ReData.getINM6TOTAL());
						TotalData.setINM6LOGC(TotalData.getINM6LOGC() + ReData.getINM6LOGC());
						TotalData.setINM6BONBU(TotalData.getINM6BONBU() + ReData.getINM6BONBU());
						TotalData.setINM7TOTAL(TotalData.getINM7TOTAL() + ReData.getINM7TOTAL());
						TotalData.setINM7LOGC(TotalData.getINM7LOGC() + ReData.getINM7LOGC());
						TotalData.setINM7BONBU(TotalData.getINM7BONBU() + ReData.getINM7BONBU());
						TotalData.setINM8TOTAL(TotalData.getINM8TOTAL() + ReData.getINM8TOTAL());
						TotalData.setINM8LOGC(TotalData.getINM8LOGC() + ReData.getINM8LOGC());
						TotalData.setINM8BONBU(TotalData.getINM8BONBU() + ReData.getINM8BONBU());
						TotalData.setINM9TOTAL(TotalData.getINM9TOTAL() + ReData.getINM9TOTAL());
						TotalData.setINM9LOGC(TotalData.getINM9LOGC() + ReData.getINM9LOGC());
						TotalData.setINM9BONBU(TotalData.getINM9BONBU() + ReData.getINM9BONBU());
						TotalData.setINM10TOTAL(TotalData.getINM10TOTAL() + ReData.getINM10TOTAL());
						TotalData.setINM10LOGC(TotalData.getINM10LOGC() + ReData.getINM10LOGC());
						TotalData.setINM10BONBU(TotalData.getINM10BONBU() + ReData.getINM10BONBU());
						TotalData.setINM11TOTAL(TotalData.getINM11TOTAL() + ReData.getINM11TOTAL());
						TotalData.setINM11LOGC(TotalData.getINM11LOGC() + ReData.getINM11LOGC());
						TotalData.setINM11BONBU(TotalData.getINM11BONBU() + ReData.getINM11BONBU());
						TotalData.setINM12TOTAL(TotalData.getINM12TOTAL() + ReData.getINM12TOTAL());
						TotalData.setINM12LOGC(TotalData.getINM12LOGC() + ReData.getINM12LOGC());
						TotalData.setINM12BONBU(TotalData.getINM12BONBU() + ReData.getINM12BONBU());
						BonbuCnt++;
						ReType = 0;
						IN_LIST.get(IN_LIST.size()-1).setSUMTYPE(0);
					}
					else {
						if(BonbuCnt > 0) {
							TotalData.setCENTER("");
							TotalData.setCENTERNAME("소계");
							TotalData.setSUMTYPE(1);
							IN_LIST.add(TotalData);
						}
						TotalData = (InoutReportVo)ReData.clone();
						BonbuCnt = 0;
						ReType = 1;
					}
				}
				ReData.setSUMTYPE(ReType);
				IN_LIST.add(ReData);
				if((CenterList.size() - 1) == i && ReType == 0 && BonbuCnt > 0) {
					TotalData.setCENTER("");
					TotalData.setCENTERNAME("소계");
					TotalData.setSUMTYPE(1);
					IN_LIST.add(TotalData);
				}
			}
			return IN_LIST.size();
		}
		catch (Exception e) {
			return -1;
		}
	}
	public int getOutReport(String LOGC, String BONBU, String CENTER, String LEVEL, List<InoutReportVo> OUT_LIST, String MODEL, int YEAR){
		try {
			String StartDay = YEAR + "-01-01";
			String EndDay = YEAR + "-12-31";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("INOUT_TYPE", "2");
			map.put("StartDay", StartDay);
			map.put("EndDay", EndDay);
			map.put("MODEL", MODEL);
			map.put("LOGC", LOGC);
			map.put("BONBU", BONBU);
			map.put("CENTER", CENTER);
			map.put("LEVEL", LEVEL);
			List<InoutReportVo> JEGOList = inOutDao.getJegoModelReport(map);
			List<InoutReportVo> InOutList = inOutDao.getInoutReportCenter(map);
			List<CenterVo> CenterList = centerDao.getAllList(map);
			if(CenterList == null || CenterList.size() == 0) {
				return 0;
			}
			InoutReportVo TotalData = null;
			int BonbuCnt = 0;
			for(int i = 0; i < CenterList.size(); ++i) {
				CenterVo centerVo = CenterList.get(i);
				InoutReportVo ReData = new InoutReportVo();
				ReData.setMCI_CODE(centerVo.getMCI_CODE());
				ReData.setLOGC(centerVo.getMCI_LOGC());
				ReData.setBONBU(centerVo.getMCI_Bonbu());
				ReData.setCENTER(centerVo.getMCI_Center());
				ReData.setLOGCNAME(centerVo.getMCI_LOGCNAME());
				ReData.setBONBUNAME(centerVo.getMCI_BonbuNAME());
				ReData.setCENTERNAME(centerVo.getMCI_CenterName());
				int JEGO = 0;
				if(JEGOList != null && JEGOList.size() > 0) {
					for(InoutReportVo jegoVo : JEGOList) {
						if(jegoVo.getDMJ_CENTER().equals(centerVo.getMCI_CODE())) {
							JEGO = jegoVo.getJEGO();
						}
					}
				}
				ReData.setJEGO(JEGO);
				if(InOutList != null && InOutList.size() > 0) {
					for(InoutReportVo inoutVo : InOutList) {
						if(inoutVo.getDIB_CENTER().equals(centerVo.getMCI_CODE())) {
							ReData.setDIB_IOTYPE("2");
							switch (inoutVo.getMONTH()) {
							case 1:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM1EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM1BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM1OTHER(inoutVo.getCNT());
								}
								break;
							case 2:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM2EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM2BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM2OTHER(inoutVo.getCNT());
								}
								break;
							case 3:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM3EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM3BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM3OTHER(inoutVo.getCNT());
								}
								break;
							case 4:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM4EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM4BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM4OTHER(inoutVo.getCNT());
								}
								break;
							case 5:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM5EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM5BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM5OTHER(inoutVo.getCNT());
								}
								break;
							case 6:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM6EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM6BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM6OTHER(inoutVo.getCNT());
								}
								break;
							case 7:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM7EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM7BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM7OTHER(inoutVo.getCNT());
								}
								break;
							case 8:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM8EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM8BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM8OTHER(inoutVo.getCNT());
								}
								break;
							case 9:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM9EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM9BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM9OTHER(inoutVo.getCNT());
								}
								break;
							case 10:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM10EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM10BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM10OTHER(inoutVo.getCNT());
								}
								break;
							case 11:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM11EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM11BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM11OTHER(inoutVo.getCNT());
								}
								break;
							case 12:
								if(inoutVo.getDIB_RESN().equals("1"))
								{
									ReData.setOUTM12EVENT(inoutVo.getCNT());
								}
								else if(inoutVo.getDIB_RESN().equals("2")) 
								{
									ReData.setOUTM12BONBU(inoutVo.getCNT());
								}
								else {
									ReData.setOUTM12OTHER(inoutVo.getCNT());
								}
								break;
							default:
								break;
							}
						}
					}
				}
				ReData.setOUTM1TOTAL(ReData.getOUTM1EVENT() + ReData.getOUTM1BONBU() + ReData.getOUTM1OTHER());
				ReData.setOUTM2TOTAL(ReData.getOUTM2EVENT() + ReData.getOUTM2BONBU() + ReData.getOUTM2OTHER());
				ReData.setOUTM3TOTAL(ReData.getOUTM3EVENT() + ReData.getOUTM3BONBU() + ReData.getOUTM3OTHER());
				ReData.setOUTM4TOTAL(ReData.getOUTM4EVENT() + ReData.getOUTM4BONBU() + ReData.getOUTM4OTHER());
				ReData.setOUTM5TOTAL(ReData.getOUTM5EVENT() + ReData.getOUTM5BONBU() + ReData.getOUTM5OTHER());
				ReData.setOUTM6TOTAL(ReData.getOUTM6EVENT() + ReData.getOUTM6BONBU() + ReData.getOUTM6OTHER());
				ReData.setOUTM7TOTAL(ReData.getOUTM7EVENT() + ReData.getOUTM7BONBU() + ReData.getOUTM7OTHER());
				ReData.setOUTM8TOTAL(ReData.getOUTM8EVENT() + ReData.getOUTM8BONBU() + ReData.getOUTM8OTHER());
				ReData.setOUTM9TOTAL(ReData.getOUTM9EVENT() + ReData.getOUTM9BONBU() + ReData.getOUTM9OTHER());
				ReData.setOUTM10TOTAL(ReData.getOUTM10EVENT() + ReData.getOUTM10BONBU() + ReData.getOUTM10OTHER());
				ReData.setOUTM11TOTAL(ReData.getOUTM11EVENT() + ReData.getOUTM11BONBU() + ReData.getOUTM11OTHER());
				ReData.setOUTM12TOTAL(ReData.getOUTM12EVENT() + ReData.getOUTM12BONBU() + ReData.getOUTM12OTHER());
				ReData.setOUTSUBEVENTTOTAL(
						ReData.getOUTM1EVENT() + 
						ReData.getOUTM2EVENT() + 
						ReData.getOUTM3EVENT() + 
						ReData.getOUTM4EVENT() + 
						ReData.getOUTM5EVENT() + 
						ReData.getOUTM6EVENT() + 
						ReData.getOUTM7EVENT() + 
						ReData.getOUTM8EVENT() + 
						ReData.getOUTM9EVENT() + 
						ReData.getOUTM10EVENT() + 
						ReData.getOUTM11EVENT() + 
						ReData.getOUTM12EVENT()
						);
				ReData.setOUTSUBBONBUTOTAL(
						ReData.getOUTM1BONBU() + 
						ReData.getOUTM2BONBU() + 
						ReData.getOUTM3BONBU() + 
						ReData.getOUTM4BONBU() + 
						ReData.getOUTM5BONBU() + 
						ReData.getOUTM6BONBU() + 
						ReData.getOUTM7BONBU() + 
						ReData.getOUTM8BONBU() + 
						ReData.getOUTM9BONBU() + 
						ReData.getOUTM10BONBU() + 
						ReData.getOUTM11BONBU() + 
						ReData.getOUTM12BONBU()
						);
				ReData.setOUTSUBOTHERTOTAL(
						ReData.getOUTM1OTHER() + 
						ReData.getOUTM2OTHER() + 
						ReData.getOUTM3OTHER() + 
						ReData.getOUTM4OTHER() + 
						ReData.getOUTM5OTHER() + 
						ReData.getOUTM6OTHER() + 
						ReData.getOUTM7OTHER() + 
						ReData.getOUTM8OTHER() + 
						ReData.getOUTM9OTHER() + 
						ReData.getOUTM10OTHER() + 
						ReData.getOUTM11OTHER() + 
						ReData.getOUTM12OTHER()
						);
				ReData.setOUTSUBTOTAL(ReData.getOUTSUBEVENTTOTAL() + ReData.getOUTSUBBONBUTOTAL() + ReData.getOUTSUBOTHERTOTAL());
				int ReType = 1;
				if(TotalData == null) {
					TotalData = (InoutReportVo)ReData.clone();
					BonbuCnt = 0;
					ReType = 1;
				}
				else {
					if(TotalData.getLOGC().equals(ReData.getLOGC()) && TotalData.getBONBU().equals(ReData.getBONBU())) {
						TotalData.setJEGO(TotalData.getJEGO() + ReData.getJEGO());
						TotalData.setOUTSUBTOTAL(TotalData.getOUTSUBTOTAL() + ReData.getOUTSUBTOTAL());
						TotalData.setOUTSUBEVENTTOTAL(TotalData.getOUTSUBEVENTTOTAL() + ReData.getOUTSUBEVENTTOTAL());
						TotalData.setOUTSUBOTHERTOTAL(TotalData.getOUTSUBOTHERTOTAL() + ReData.getOUTSUBOTHERTOTAL());
						TotalData.setOUTSUBBONBUTOTAL(TotalData.getOUTSUBBONBUTOTAL() + ReData.getOUTSUBBONBUTOTAL());
						TotalData.setOUTM1TOTAL(TotalData.getOUTM1TOTAL() + ReData.getOUTM1TOTAL());
						TotalData.setOUTM1EVENT(TotalData.getOUTM1EVENT() + ReData.getOUTM1EVENT());
						TotalData.setOUTM1OTHER(TotalData.getOUTM1OTHER() + ReData.getOUTM1OTHER());
						TotalData.setOUTM1BONBU(TotalData.getOUTM1BONBU() + ReData.getOUTM1BONBU());
						TotalData.setOUTM2TOTAL(TotalData.getOUTM2TOTAL() + ReData.getOUTM2TOTAL());
						TotalData.setOUTM2EVENT(TotalData.getOUTM2EVENT() + ReData.getOUTM2EVENT());
						TotalData.setOUTM2OTHER(TotalData.getOUTM2OTHER() + ReData.getOUTM2OTHER());
						TotalData.setOUTM2BONBU(TotalData.getOUTM2BONBU() + ReData.getOUTM2BONBU());
						TotalData.setOUTM3TOTAL(TotalData.getOUTM3TOTAL() + ReData.getOUTM3TOTAL());
						TotalData.setOUTM3EVENT(TotalData.getOUTM3EVENT() + ReData.getOUTM3EVENT());
						TotalData.setOUTM3OTHER(TotalData.getOUTM3OTHER() + ReData.getOUTM3OTHER());
						TotalData.setOUTM3BONBU(TotalData.getOUTM3BONBU() + ReData.getOUTM3BONBU());
						TotalData.setOUTM4TOTAL(TotalData.getOUTM4TOTAL() + ReData.getOUTM4TOTAL());
						TotalData.setOUTM4EVENT(TotalData.getOUTM4EVENT() + ReData.getOUTM4EVENT());
						TotalData.setOUTM4OTHER(TotalData.getOUTM4OTHER() + ReData.getOUTM4OTHER());
						TotalData.setOUTM4BONBU(TotalData.getOUTM4BONBU() + ReData.getOUTM4BONBU());
						TotalData.setOUTM5TOTAL(TotalData.getOUTM5TOTAL() + ReData.getOUTM5TOTAL());
						TotalData.setOUTM5EVENT(TotalData.getOUTM5EVENT() + ReData.getOUTM5EVENT());
						TotalData.setOUTM5OTHER(TotalData.getOUTM5OTHER() + ReData.getOUTM5OTHER());
						TotalData.setOUTM5BONBU(TotalData.getOUTM5BONBU() + ReData.getOUTM5BONBU());
						TotalData.setOUTM6TOTAL(TotalData.getOUTM6TOTAL() + ReData.getOUTM6TOTAL());
						TotalData.setOUTM6EVENT(TotalData.getOUTM6EVENT() + ReData.getOUTM6EVENT());
						TotalData.setOUTM6OTHER(TotalData.getOUTM6OTHER() + ReData.getOUTM6OTHER());
						TotalData.setOUTM6BONBU(TotalData.getOUTM6BONBU() + ReData.getOUTM6BONBU());
						TotalData.setOUTM7TOTAL(TotalData.getOUTM7TOTAL() + ReData.getOUTM7TOTAL());
						TotalData.setOUTM7EVENT(TotalData.getOUTM7EVENT() + ReData.getOUTM7EVENT());
						TotalData.setOUTM7OTHER(TotalData.getOUTM7OTHER() + ReData.getOUTM7OTHER());
						TotalData.setOUTM7BONBU(TotalData.getOUTM7BONBU() + ReData.getOUTM7BONBU());
						TotalData.setOUTM8TOTAL(TotalData.getOUTM8TOTAL() + ReData.getOUTM8TOTAL());
						TotalData.setOUTM8EVENT(TotalData.getOUTM8EVENT() + ReData.getOUTM8EVENT());
						TotalData.setOUTM8OTHER(TotalData.getOUTM8OTHER() + ReData.getOUTM8OTHER());
						TotalData.setOUTM8BONBU(TotalData.getOUTM8BONBU() + ReData.getOUTM8BONBU());
						TotalData.setOUTM9TOTAL(TotalData.getOUTM9TOTAL() + ReData.getOUTM9TOTAL());
						TotalData.setOUTM9EVENT(TotalData.getOUTM9EVENT() + ReData.getOUTM9EVENT());
						TotalData.setOUTM9OTHER(TotalData.getOUTM9OTHER() + ReData.getOUTM9OTHER());
						TotalData.setOUTM9BONBU(TotalData.getOUTM9BONBU() + ReData.getOUTM9BONBU());
						TotalData.setOUTM10TOTAL(TotalData.getOUTM10TOTAL() + ReData.getOUTM10TOTAL());
						TotalData.setOUTM10EVENT(TotalData.getOUTM10EVENT() + ReData.getOUTM10EVENT());
						TotalData.setOUTM10OTHER(TotalData.getOUTM10OTHER() + ReData.getOUTM10OTHER());
						TotalData.setOUTM10BONBU(TotalData.getOUTM10BONBU() + ReData.getOUTM10BONBU());
						TotalData.setOUTM11TOTAL(TotalData.getOUTM11TOTAL() + ReData.getOUTM11TOTAL());
						TotalData.setOUTM11EVENT(TotalData.getOUTM11EVENT() + ReData.getOUTM11EVENT());
						TotalData.setOUTM11OTHER(TotalData.getOUTM11OTHER() + ReData.getOUTM11OTHER());
						TotalData.setOUTM11BONBU(TotalData.getOUTM11BONBU() + ReData.getOUTM11BONBU());
						TotalData.setOUTM12TOTAL(TotalData.getOUTM12TOTAL() + ReData.getOUTM12TOTAL());
						TotalData.setOUTM12EVENT(TotalData.getOUTM12EVENT() + ReData.getOUTM12EVENT());
						TotalData.setOUTM12OTHER(TotalData.getOUTM12OTHER() + ReData.getOUTM12OTHER());
						TotalData.setOUTM12BONBU(TotalData.getOUTM12BONBU() + ReData.getOUTM12BONBU());
						BonbuCnt++;
						ReType = 0;
						OUT_LIST.get(OUT_LIST.size()-1).setSUMTYPE(0);
					}
					else {
						if(BonbuCnt > 0) {
							TotalData.setCENTER("");
							TotalData.setCENTERNAME("소계");
							TotalData.setSUMTYPE(1);
							OUT_LIST.add(TotalData);
						}
						TotalData = (InoutReportVo)ReData.clone();
						BonbuCnt = 0;
						ReType = 1;
					}
				}
				ReData.setSUMTYPE(ReType);
				OUT_LIST.add(ReData);
				if((CenterList.size() - 1) == i && ReType == 0 && BonbuCnt > 0) {
					TotalData.setCENTER("");
					TotalData.setCENTERNAME("소계");
					TotalData.setSUMTYPE(1);
					OUT_LIST.add(TotalData);
				}
			}
			return OUT_LIST.size();
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int getNowJegoReport(String pDay, String StartDay, String EndDay, int MONTH, String LOGC, String BONBU, String CENTER, List<JegoVo> JEGO_LIST, String MODEL, String NowDay, String LEVEL){
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("StartDay", StartDay);
			map.put("EndDay", EndDay);
			map.put("MODEL", MODEL);
			map.put("LOGC", LOGC);
			map.put("BONBU", BONBU);
			map.put("CENTER", CENTER);
			map.put("NowDay", NowDay);
			map.put("LEVEL", LEVEL);
			map.put("MONTH", MONTH);
			map.put("pDay", pDay);
			List<JegoVo> TEMP_JEGOLIST = inOutDao.getNowJegoReport(map);
			if(TEMP_JEGOLIST == null || TEMP_JEGOLIST.size() == 0) {
				return 0;
			}

			JegoVo TotalData = null;
			int BonbuCnt = 0;
			for (int i = 0; i < TEMP_JEGOLIST.size(); ++i) {
				JegoVo jegoVo = TEMP_JEGOLIST.get(i);
				int ReType = 1;
				if(TotalData == null) {
					TotalData = (JegoVo)jegoVo.clone();
					BonbuCnt = 0;
					ReType = 1;
				}
				else {
					if(TotalData.getLOGC().equals(jegoVo.getLOGC()) && TotalData.getBONBU().equals(jegoVo.getBONBU())) {
						TotalData.setPJEGO(TotalData.getPJEGO() + jegoVo.getPJEGO());
						TotalData.setNOWJEGO(TotalData.getNOWJEGO() + jegoVo.getNOWJEGO());
						TotalData.setMMJ_JEGO(TotalData.getMMJ_JEGO() + jegoVo.getMMJ_JEGO());
						TotalData.setM1JEGO(TotalData.getM1JEGO() + jegoVo.getM1JEGO());
						if(MONTH >= 2)
							TotalData.setM2JEGO(TotalData.getM2JEGO() + jegoVo.getM2JEGO());
						if(MONTH >= 3)
							TotalData.setM3JEGO(TotalData.getM3JEGO() + jegoVo.getM3JEGO());
						if(MONTH >= 4)
							TotalData.setM4JEGO(TotalData.getM4JEGO() + jegoVo.getM4JEGO());
						if(MONTH >= 5)
							TotalData.setM5JEGO(TotalData.getM5JEGO() + jegoVo.getM5JEGO());
						if(MONTH >= 6)
							TotalData.setM6JEGO(TotalData.getM6JEGO() + jegoVo.getM6JEGO());
						if(MONTH >= 7)
							TotalData.setM7JEGO(TotalData.getM7JEGO() + jegoVo.getM7JEGO());
						if(MONTH >= 8)
							TotalData.setM8JEGO(TotalData.getM8JEGO() + jegoVo.getM8JEGO());
						if(MONTH >= 9)
							TotalData.setM9JEGO(TotalData.getM9JEGO() + jegoVo.getM9JEGO());
						if(MONTH >= 10)
							TotalData.setM10JEGO(TotalData.getM10JEGO() + jegoVo.getM10JEGO());
						if(MONTH >= 11)
							TotalData.setM11JEGO(TotalData.getM11JEGO() + jegoVo.getM11JEGO());
						if(MONTH >= 12)
							TotalData.setM12JEGO(TotalData.getM12JEGO() + jegoVo.getM12JEGO());
						BonbuCnt++;
						ReType = 0;
						JEGO_LIST.get(JEGO_LIST.size()-1).setSUMTYPE(0);
					}
					else {
						if(BonbuCnt > 0) {
							TotalData.setCENTER("");
							TotalData.setCENTERNAME("소계");
							TotalData.setSUMTYPE(1);
							JEGO_LIST.add(TotalData);
						}
						TotalData = (JegoVo)jegoVo.clone();
						BonbuCnt = 0;
						ReType = 1;
					}
				}
				jegoVo.setSUMTYPE(ReType);
				JEGO_LIST.add(jegoVo);
				if((TEMP_JEGOLIST.size() - 1) == i && ReType == 0 && BonbuCnt > 0) {
					TotalData.setCENTER("");
					TotalData.setCENTERNAME("소계");
					TotalData.setSUMTYPE(1);
					JEGO_LIST.add(TotalData);
				}
			}
			JegoVo totalVo = new JegoVo();
			totalVo.setSUMTYPE(2);
			for (JegoVo vo : JEGO_LIST) {
				if(vo.getSUMTYPE() == 1) {
					totalVo.setPJEGO(totalVo.getPJEGO() + vo.getPJEGO());
					totalVo.setNOWJEGO(totalVo.getNOWJEGO() + vo.getNOWJEGO());
					totalVo.setMMJ_JEGO(totalVo.getMMJ_JEGO() + vo.getMMJ_JEGO());
					totalVo.setM1JEGO(totalVo.getM1JEGO() + vo.getM1JEGO());
					if(MONTH >= 2)
						totalVo.setM2JEGO(totalVo.getM2JEGO() + vo.getM2JEGO());
					if(MONTH >= 3)
						totalVo.setM3JEGO(totalVo.getM3JEGO() + vo.getM3JEGO());
					if(MONTH >= 4)
						totalVo.setM4JEGO(totalVo.getM4JEGO() + vo.getM4JEGO());
					if(MONTH >= 5)
						totalVo.setM5JEGO(totalVo.getM5JEGO() + vo.getM5JEGO());
					if(MONTH >= 6)
						totalVo.setM6JEGO(totalVo.getM6JEGO() + vo.getM6JEGO());
					if(MONTH >= 7)
						totalVo.setM7JEGO(totalVo.getM7JEGO() + vo.getM7JEGO());
					if(MONTH >= 8)
						totalVo.setM8JEGO(totalVo.getM8JEGO() + vo.getM8JEGO());
					if(MONTH >= 9)
						totalVo.setM9JEGO(totalVo.getM9JEGO() + vo.getM9JEGO());
					if(MONTH >= 10)
						totalVo.setM10JEGO(totalVo.getM10JEGO() + vo.getM10JEGO());
					if(MONTH >= 11)
						totalVo.setM11JEGO(totalVo.getM11JEGO() + vo.getM11JEGO());
					if(MONTH >= 12)
						totalVo.setM12JEGO(totalVo.getM12JEGO() + vo.getM12JEGO());
				}
			}
			JEGO_LIST.add(totalVo);
			return JEGO_LIST.size();
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public List<InOutListVo> getInOutListReport(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String MNAME, int IOTYPE, String EVENT, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("MNAME", MNAME);
		map.put("IOTYPE", IOTYPE);
		map.put("EVENT", EVENT);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return inOutDao.getInOutListReport(map);
	}
	
	public List<InOutListVo> getInOutListReport_Excel(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String MNAME, int IOTYPE, String EVENT){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("MNAME", MNAME);
		map.put("IOTYPE", IOTYPE);
		map.put("EVENT", EVENT);
		return inOutDao.getInOutListReport_Excel(map);
	}
	
	public List<InOutListVo> getEventInfo(String StartDay, String EndDay, String BONBU, String CENTER, String EUSERID, String EUSERNAME, String GNAME, String JNAME, String ESTATE, String GTYPE, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("EUSERID", EUSERID);
		map.put("EUSERNAME", EUSERNAME);
		map.put("GNAME", GNAME);
		map.put("JNAME", JNAME);
		map.put("ESTATE", ESTATE);
		map.put("GTYPE", GTYPE);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return eventDao.getEventInfo(map);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidEventInfo(String StartDay, String EndDay, String BONBU, String CENTER, String EUSERID, String EUSERNAME, String GNAME, String JNAME, String ESTATE, String GTYPE)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("EUSERID", EUSERID);
		map.put("EUSERNAME", EUSERNAME);
		map.put("GNAME", GNAME);
		map.put("JNAME", JNAME);
		map.put("ESTATE", ESTATE);
		map.put("GTYPE", GTYPE);
		return eventDao.getAndroidEventInfo(map);
	}
	
	public List<InOutListVo> getGTYPELIST(){
		return eventDao.getGTYPELIST();
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidGTYPELIST() throws Exception{
		return eventDao.getAndroidGTYPELIST();
	}
	
	public List<BonbuStatusVo> getBonbuStatusReport(int YEAR, int MONTH, String LOGC, String CENTER, String LCODE, String MCODE, String SCODE, String MNAME, String MCODENAME, String SCODENAME, int PTYPE, int PAGE, int PSIZE){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(YEAR, MONTH-1, 1); //월은 -1해줘야 해당월로 인식
        int LastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String StartDay = dateFormat.format(cal.getTime());
        cal.set(YEAR, MONTH-1, LastDay); //월은 -1해줘야 해당월로 인식
        String EndDay = dateFormat.format(cal.getTime());
        cal.add(Calendar.MONTH, -1);
        LastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), LastDay);
        String PDATE = dateFormat.format(cal.getTime());
        
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("CENTER", CENTER);
		map.put("PTYPE", PTYPE);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		map.put("PDATE", PDATE);
		map.put("LCODE", LCODE);
		map.put("MCODE", MCODE);
		map.put("SCODE", SCODE);
		map.put("MNAME", MNAME);
		map.put("MCODENAME", MCODENAME);
		map.put("SCODENAME", SCODENAME);
		return inOutDao.getBonbuStatusReport(map);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidInCFMList(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String CFMSTS) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("CFMSTS", CFMSTS);
		return inOutDao.getAndroidInCFMList(map);
	}
	
	public int Android_updateCfm(String CFMsts, String DOCNO, String INTYPE, String USERID, String DIC_DATE, String CMNT, String PROG, List<MultipartFile> fileList, HttpServletRequest request) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			CfmVo vo = new CfmVo();
			vo.setDIC_DOCNO(DOCNO);
			vo.setDIC_USER(USERID);
			if(INTYPE.equals("0"))
				vo.setDIC_RESN("1");
			else
				vo.setDIC_RESN("2");
			vo.setDIC_DATE(DIC_DATE);
			vo.setDIC_CMNT(CMNT);
			vo.setCMN_MAK_PROG(PROG);
			vo.setCMN_MAK_ID(USERID);
			vo.setCMN_UPD_PROG(PROG);
			vo.setCMN_UPD_ID(USERID);
			if(CFMsts.equals("N")) {
				int result = inOutDao.insertCfm(vo);
				if(result <= 0)
					throw new Exception();

				if(INTYPE.equals("0")) {
					result = inOutDao.insertCfmLogcInOut(DOCNO, USERID);
					if(result <= 0)
						throw new Exception();

					result = inOutDao.updateInBonbuCfm(DOCNO);
					if(result <= 0)
						throw new Exception();

					result = inOutDao.updateInLogcJego(DOCNO);
					if(result <= 0)
						throw new Exception();
					
					result = insertPriceBonbuIn(DOCNO, PROG, USERID);
					if(result <= 0)
						throw new Exception();
				}
				else
				{
					result = inOutDao.insertCfmBonbuInOut(DOCNO, USERID);
					if(result <= 0)
						throw new Exception();

					result = inOutDao.updateOrderCfm(DOCNO);
					if(result <= 0)
						throw new Exception();

					result = inOutDao.updateInBonbuJego(DOCNO);
					if(result <= 0)
						throw new Exception();
					
					result = insertPriceMoveIn(DOCNO, PROG, USERID);
					if(result <= 0)
						throw new Exception();
				}
			}
			else {
				int result = inOutDao.updateCfm(vo);
				if(result <= 0)
					throw new Exception();
			}
			boolean filechk = true;
			for (MultipartFile file : fileList) {
				String orgFile = "";
				if (file.getOriginalFilename() != null && !file.getOriginalFilename().equals("")){
					orgFile = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
					NoticeVo fileVo = new NoticeVo();
					fileVo.setMDF_DOCNO(DOCNO);
					fileVo.setMDF_FILE_Org(orgFile);
					fileVo.setMDF_FILE_SIZE(String.format("%.2f", ((double)file.getSize() / 1024 / 1024)));
					int fileresult = noticeService.insertNoticeFile(fileVo, file, request);
					if(fileresult < 0) {
						filechk = false;
					}
				}
			}
			if(!filechk)
				return -2;
			
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -1;
		}
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidEventList(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String EVENTNO) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("EVENTNO", EVENTNO);
		return inOutDao.getAndroidEventList(map);
	}
	
	public LinkedHashMap<String, Object> getAndroidJegoModel(String MODEL, String CENTER) throws Exception{
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Date time = new Date();
		String NOWDATE = format1.format(time);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MODEL", MODEL);
		map.put("CENTER", CENTER);
		map.put("NOWDATE", NOWDATE);
		return inOutDao.getAndroidJegoModel(map);
	}
	
	public LinkedHashMap<String, Object> getAndroidEvent(String DOCNO) throws Exception{
		return inOutDao.getAndroidEvent(DOCNO);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidEventModel(String DOCNO) throws Exception{
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Date time = new Date();
		String NOWDATE = format1.format(time);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);	
		map.put("NOWDATE", NOWDATE);
		return inOutDao.getAndroidEventModel(map);
	}
	
	public int insertEVENT(AndroidVo androidVo, AndroidVo vo, String USERID, String PROG) {
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Date time = new Date();
		String NOWDATE = format1.format(time);
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			EventVo vo1 = new EventVo();
			vo1.setDEO_DOCNO(vo.field1);
			vo1.setDEO_CENTER(vo.field2);
			vo1.setDEO_DATE(NOWDATE);
			vo1.setDEO_USER(USERID);
			vo1.setDEO_CMNT(vo.field3);
			vo1.setCMN_MAK_PROG(PROG);
			vo1.setCMN_MAK_ID(USERID);
			vo1.setCMN_UPD_PROG(PROG);
			vo1.setCMN_UPD_ID(USERID);
			int result = inOutDao.insertEVENT(vo1);
			if(result <= 0)
			{
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "행사출고 저장실패! 행사번호가 중복되었는 지 확인해주세요.";
				transaction.end();
				return -1;
			}
			if(vo.TABSIZE > 0 && vo.IT_TAB1 != null && vo.IT_TAB1.size() > 0) {
				for (int i = 0; i < vo.TABSIZE; i++) {
					EventModelVo mVo = new EventModelVo();
					LinkedHashMap<String, Object> dr = vo.IT_TAB1.get(i);
					String MODEL = dr.get("MODEL").toString();
					if(MODEL.length() != 11)
					{
						androidVo.returnCode = "XXXX";
						androidVo.returnMsg = "출고 상품코드가 잘못되었습니다.";
						transaction.end();
						return -2;
					}
					String CENTER = dr.get("CENTER").toString();
					if(CENTER.length() == 0)
					{
						androidVo.returnCode = "XXXX";
						androidVo.returnMsg = "출고 본부를 가져오지 못했습니다.";
						transaction.end();
						return -2;
					}
					int TMP_QTY = 0;
                    try {
                    	TMP_QTY = Integer.parseInt(dr.get("QTY").toString());
                    }
                    catch (Exception ex){
                    	androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 출고 수량이 잘못되었습니다.";
                    	transaction.end();
        				return -3;
                    }
                    if(TMP_QTY < 1)
                    {
                    	androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 출고 수량을 입력해주세요.";
        				transaction.end();
        				return -4;
        			}
                    String REDATE = dr.get("KINDS").toString().equals("3") ? dr.get("REDATE").toString() : "";
                    if(REDATE.length() > 0) {
                    	SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                    	try 
                    	{
                    		Date D_REDATE = transFormat.parse(REDATE);
                    		mVo.setDEM_RETEMPDATE(REDATE);
                    	}
                    	catch (Exception e) {
                    		androidVo.returnCode = "XXXX";
                    		androidVo.returnMsg = MODEL + " 회수예정일이 잘못되었습니다.";
                    		transaction.end();
                    		return -5;
                    	}
                    }
                    else {
                    	mVo.setDEM_RETEMPDATE(null);
                    }
        			MoveVo JegoVo = getJegoModel(MODEL, CENTER, NOWDATE);
        			if(JegoVo == null)
        			{
        				androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 재고가 없습니다.";
        				transaction.end();
        				return -6;
        			}
        			int NOWJEGO = JegoVo.getDMJ_JEGO();
        			int OUTJEGO = JegoVo.getOUTJEGO();
        			if(OUTJEGO < TMP_QTY)
        			{
        				androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = "상품코드 : " + MODEL + ", " + NOWDATE + "일 기준 재고보다 출고수량이 많습니다(현재고:" + OUTJEGO + ")";
        				transaction.end();
        				return -7;
        			}
        			if(NOWJEGO < TMP_QTY)
        			{
        				androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = "상품코드 : " + MODEL + " 현재고보다 출고수량이 많습니다(현재고:" + NOWJEGO + ")";
        				transaction.end();
        				return -7;
        			}
        			
					
					mVo.setDEM_DOCNO(vo.field1);
					mVo.setDEM_CENTER(CENTER);
					mVo.setDEM_MODEL(MODEL);
                    mVo.setDEM_DATE(NOWDATE);
                    mVo.setDEM_QTY(TMP_QTY);
                    mVo.setDEM_USER(USERID);
                    mVo.setDEM_STS("1");
                    mVo.setCMN_MAK_PROG(PROG);
                    mVo.setCMN_MAK_ID(USERID);
                    mVo.setCMN_UPD_PROG(PROG);
                    mVo.setCMN_UPD_ID(USERID);
                    result = inOutDao.insertEVENTMODEL(mVo);
        			if(result <= 0)
        			{
        				androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 저장실패! 상품코드가 이미 출고되었는 지 확인해주세요.";
        				transaction.end();
        				return -8;
        			}
        			result = inOutDao.insertInOut(vo.field1, MODEL, "2", NOWDATE, TMP_QTY, "1", CENTER, PROG, USERID);
        			if(result <= 0)
        			{
        				androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 저장실패! 상품코드가 이미 출고되었는 지 확인해주세요.";
        				transaction.end();
        				return -9;
        			}
        			result = inOutDao.updateChulhaJego(MODEL,CENTER, TMP_QTY, NOWDATE);
        			if(result <= 0)
        			{
        				androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 저장실패! 재고가 충분한지 확인해주세요.";
        				transaction.end();
        				return -10;
        			}
        			result = insertPriceOut(vo.field1, CENTER, MODEL, TMP_QTY, USERID, PROG);
        			if(result <= 0)
        			{
        				androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 저장실패! 재고 등록 실패";
        				transaction.end();
        				return -11;
        			}
				}
			}
			else {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "출고 상품코드 내역을 가져오지 못했습니다.";
				
				transaction.end();
				return -2;
			}
			
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "알수없는 에러가 발생하였습니다.";
			
			transaction.end();
			return -12;
		}
	}
	
	public LinkedHashMap<String, Object> getAndroidEventDetail(String DOCNO) throws Exception{
		return eventDao.getAndroidEventDetail(DOCNO);
	}
	
	public int updateEVENT(AndroidVo androidVo, AndroidVo vo, String USERID, String PROG) {
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		Date time = new Date();
		String NOWDATE = format1.format(time);
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			EventVo vo1 = new EventVo();
			vo1.setDEO_DOCNO(vo.field1);
			vo1.setDEO_CMNT(vo.field2);
			vo1.setCMN_UPD_PROG(PROG);
			vo1.setCMN_UPD_ID(USERID);
			int result = inOutDao.updateEVENT(vo1);
			if(result <= 0)
			{
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "행사출고 저장실패! 다시 시도해주세요.";
				transaction.end();
				return -1;
			}
			if(vo.TABSIZE > 0 && vo.IT_TAB1 != null && vo.IT_TAB1.size() > 0) {
				List<LinkedHashMap<String, Object>> ModelList = getAndroidEventModel(vo.field1);
				if(ModelList == null) {
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = "상품코드 정보를 가져오지 못했습니다.";
					transaction.end();
					return -2;
				}
				for (int i = 0; i < vo.TABSIZE; i++) {
					EventModelVo mVo = new EventModelVo();
					LinkedHashMap<String, Object> dr = vo.IT_TAB1.get(i);
					String MODEL = dr.get("MODEL").toString();
					if(MODEL.length() != 11)
					{
						androidVo.returnCode = "XXXX";
						androidVo.returnMsg = "출고 상품코드가 잘못되었습니다.";
						transaction.end();
						return -2;
					}
					String CENTER = dr.get("CENTER").toString();
					if(CENTER.length() == 0)
					{
						androidVo.returnCode = "XXXX";
						androidVo.returnMsg = "출고 본부를 가져오지 못했습니다.";
						transaction.end();
						return -2;
					}
					int TMP_QTY = 0;
                    try {
                    	TMP_QTY = Integer.parseInt(dr.get("QTY").toString());
                    }
                    catch (Exception ex){
                    	androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 출고 수량이 잘못되었습니다.";
                    	transaction.end();
        				return -3;
                    }
                    if(TMP_QTY < 1)
                    {
                    	androidVo.returnCode = "XXXX";
        				androidVo.returnMsg = MODEL + " 출고 수량을 입력해주세요.";
        				transaction.end();
        				return -4;
        			}
                    String KINDS = dr.get("KINDS").toString();
                    String REDATE = KINDS.equals("3") ? dr.get("REDATE").toString() : "";
                    if(REDATE.length() > 0) {
                    	SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                    	try 
                    	{
                    		Date D_REDATE = transFormat.parse(REDATE);
                    		mVo.setDEM_RETEMPDATE(REDATE);
                    	}
                    	catch (Exception e) {
                    		androidVo.returnCode = "XXXX";
                    		androidVo.returnMsg = MODEL + " 회수예정일이 잘못되었습니다.";
                    		transaction.end();
                    		return -5;
                    	}
                    }
                    else {
                    	mVo.setDEM_RETEMPDATE(null);
                    }
        			MoveVo JegoVo = getJegoModel(MODEL, CENTER, NOWDATE);
        			
					
					mVo.setDEM_DOCNO(vo.field1);
					mVo.setDEM_CENTER(CENTER);
					mVo.setDEM_MODEL(MODEL);
					mVo.setDEM_DATE(NOWDATE);
                    mVo.setDEM_QTY(TMP_QTY);
                    mVo.setDEM_USER(USERID);
                    mVo.setDEM_STS("1");
                    mVo.setCMN_MAK_PROG(PROG);
                    mVo.setCMN_MAK_ID(USERID);
                    mVo.setCMN_UPD_PROG(PROG);
                    mVo.setCMN_UPD_ID(USERID);
                    
					boolean MCHK = false;
					for (int j = 0; j < ModelList.size(); j++) {
						LinkedHashMap<String, Object> ModelVo = ModelList.get(j);
						if(ModelVo.get("DEM_CENTER").toString().equals(CENTER) && ModelVo.get("DEM_MODEL").toString().equals(MODEL)) {
							MCHK = true;
							if(!KINDS.equals("3")) {
								int QTY = (Integer.parseInt(ModelVo.get("DEM_QTY").toString())) - TMP_QTY;
								if(QTY != 0) {
									if(QTY < 0)
									{
										if(JegoVo == null)
										{
											androidVo.returnCode = "XXXX";
											androidVo.returnMsg = MODEL + " 재고가 없습니다.";
											transaction.end();
											return -5;
										}
										int NOWJEGO = JegoVo.getDMJ_JEGO();
										int OUTJEGO = JegoVo.getOUTJEGO();
										if(OUTJEGO < (QTY * (-1)))
										{
											androidVo.returnCode = "XXXX";
											androidVo.returnMsg = "상품코드 : " + MODEL + ", " + NOWDATE + "일 기준 재고보다 출고수량이 많습니다(현재고:" + OUTJEGO + ")";
											transaction.end();
											return -6;
										}
										if(NOWJEGO < (QTY * (-1)))
										{
											androidVo.returnCode = "XXXX";
											androidVo.returnMsg = "상품코드 : " + MODEL + " 현재고보다 출고수량이 많습니다(현재고:" + NOWJEGO + ")";
											transaction.end();
											return -6;
										}
									}

									result = inOutDao.updateEVENTMODEL(mVo);
									if(result <= 0)
									{
										androidVo.returnCode = "XXXX";
										androidVo.returnMsg = MODEL + " 저장실패! 다시 시도해주세요.";
										transaction.end();
										return -7;
									}

									result = inOutDao.updateInOut(vo.field1, "2", CENTER, MODEL, NOWDATE, TMP_QTY, PROG, USERID);
									if(result <= 0)
									{
										androidVo.returnCode = "XXXX";
										androidVo.returnMsg = MODEL + " 저장실패! 다시 시도해주세요.";
										transaction.end();
										return -8;
									}

									result = updatePriceJego(vo.field1, CENTER, MODEL, QTY, USERID, PROG);
									if(result <= 0)
									{
										androidVo.returnCode = "XXXX";
										androidVo.returnMsg = MODEL + " 저장실패! 재고 반영 실패!";
										transaction.end();
										return -8;
									}

									if(QTY < 0) {
										QTY = QTY * (-1);
										result = inOutDao.updateChulhaJego(MODEL,CENTER, QTY, NOWDATE);
										if(result <= 0)
										{
											androidVo.returnCode = "XXXX";
											androidVo.returnMsg = MODEL + " 저장실패! 재고가 충분한지 확인해주세요.";
											transaction.end();
											return -9;
										}
									}
									else {
										result = inOutDao.updateJego(MODEL,CENTER, QTY);
										if(result <= 0)
										{
											androidVo.returnCode = "XXXX";
											androidVo.returnMsg = MODEL + " 저장실패! 다시 시도해주세요.";
											transaction.end();
											return -9;
										}
									}
								}
							}
							else {
								result = inOutDao.updateEVENTMODEL_REDATE(mVo);
								if(result <= 0)
								{
									androidVo.returnCode = "XXXX";
									androidVo.returnMsg = MODEL + " 저장실패! 다시 시도해주세요.";
									transaction.end();
									return -7;
								}
							}
							break;
						}
					}
					if(!MCHK) {
						if(JegoVo == null)
						{
							androidVo.returnCode = "XXXX";
							androidVo.returnMsg = MODEL + " 재고가 없습니다.";
							transaction.end();
							return -5;
						}
						int NOWJEGO = JegoVo.getDMJ_JEGO();
						int OUTJEGO = JegoVo.getOUTJEGO();
						if(OUTJEGO < TMP_QTY)
						{
							androidVo.returnCode = "XXXX";
							androidVo.returnMsg = "상품코드 : " + MODEL + ", " + NOWDATE + "일 기준 재고보다 출고수량이 많습니다(현재고:" + OUTJEGO + ")";
							transaction.end();
							return -6;
						}
						if(NOWJEGO < TMP_QTY)
						{
							androidVo.returnCode = "XXXX";
							androidVo.returnMsg = "상품코드 : " + MODEL + " 현재고보다 출고수량이 많습니다(현재고:" + NOWJEGO + ")";
							transaction.end();
							return -6;
						}
						
	                    result = inOutDao.insertEVENTMODEL(mVo);
	                    if(result <= 0)
	        			{
	        				androidVo.returnCode = "XXXX";
	        				androidVo.returnMsg = MODEL + " 저장실패! 상품코드가 이미 출고되었는 지 확인해주세요.";
	        				transaction.end();
	        				return -7;
	        			}
	                    
	        			result = inOutDao.insertInOut(vo.field1, MODEL, "2", NOWDATE, TMP_QTY, "1", CENTER, PROG, USERID);
	        			if(result <= 0)
	        			{
	        				androidVo.returnCode = "XXXX";
	        				androidVo.returnMsg = MODEL + " 저장실패! 상품코드가 이미 출고되었는 지 확인해주세요.";
	        				transaction.end();
	        				return -8;
	        			}
	        			
	        			result = inOutDao.updateChulhaJego(MODEL,CENTER, TMP_QTY, NOWDATE);
	        			if(result <= 0)
	        			{
	        				androidVo.returnCode = "XXXX";
	        				androidVo.returnMsg = MODEL + " 저장실패! 재고가 충분한지 확인해주세요.";
	        				transaction.end();
	        				return -9;
	        			}
	        			
	        			result = insertPriceOut(vo.field1, CENTER, MODEL, TMP_QTY, USERID, PROG);
	        			if(result <= 0)
	        			{
	        				androidVo.returnCode = "XXXX";
	        				androidVo.returnMsg = MODEL + " 저장실패! 재고 등록 실패";
	        				transaction.end();
	        				return -10;
	        			}
					}
				}
			}
			else {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "출고 상품코드 내역을 가져오지 못했습니다.";
				
				transaction.end();
				return -2;
			}
			
			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "알수없는 에러가 발생하였습니다.";
			
			transaction.end();
			return -10;
		}
	}
	
	public int deleteEVENT(AndroidVo androidVo, String DOCNO) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			int result = inOutDao.deleteEVENT(DOCNO);
			if(result <= 0)
			{
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "행사출고 삭제실패! 다시 시도해주세요.";
				transaction.end();
				return -1;
			}
			List<LinkedHashMap<String, Object>> ModelList = getAndroidEventModel(DOCNO);
			if(ModelList == null) {
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = "상품코드 정보를 가져오지 못했습니다.";
				transaction.end();
				return -2;
			}
			
			for (int j = 0; j < ModelList.size(); j++) {
				LinkedHashMap<String, Object> ModelVo = ModelList.get(j);
				String CENTER = ModelVo.get("DEM_CENTER").toString();
				String MODEL = ModelVo.get("DEM_MODEL").toString();
				String STS = ModelVo.get("DEM_STS").toString();
				int QTY = Integer.parseInt(ModelVo.get("DEM_QTY").toString());
				result = inOutDao.deleteEVENTMODEL(DOCNO, CENTER, MODEL);
				if(result <= 0)
				{
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = MODEL + " 삭제실패! 다시 시도해주세요.";
					transaction.end();
					return -3;
				}
				result = inOutDao.deleteInOut(DOCNO, "2", CENTER, MODEL);
				if(result <= 0)
				{
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = MODEL + " 삭제실패! 다시 시도해주세요.";
					transaction.end();
					return -4;
				}

				result = inOutDao.updateJego(MODEL, CENTER, QTY);
				if(result <= 0)
				{
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = MODEL + "재고 업데이트 실패! 다시 시도해주세요.";
					transaction.end();
					return -5;
				}
				
				result = deletePriceJego(DOCNO, CENTER, MODEL);
				if(result <= 0)
				{
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = MODEL + "저장실패! 재고 반영 실패";
					transaction.end();
					return -6;
				}
				
				if(STS.equals("2") || STS.equals("4")) {
					result = inOutDao.deleteInOut(DOCNO, "1", CENTER, MODEL);
					if(result <= 0)
					{
						androidVo.returnCode = "XXXX";
						androidVo.returnMsg = MODEL + " 삭제실패! 다시 시도해주세요.";
						transaction.end();
						return -4;
					}
					result = inOutDao.updateChulhaJego(MODEL, CENTER, QTY, "");
					if(result <= 0)
					{
						androidVo.returnCode = "XXXX";
						androidVo.returnMsg = MODEL + "재고 업데이트 실패! 다시 시도해주세요.";
						transaction.end();
						return -5;
					}
				}
			}

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "알수없는 에러가 발생하였습니다.";
			
			transaction.end();
			return -6;
		}
	}
	
	public int deleteEVENTMODEL(AndroidVo androidVo, String DOCNO, String CENTER, String MODEL) {
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			EventModelVo MVO = inOutDao.getEventModel(DOCNO, CENTER, MODEL);
			if(MVO == null) {
				androidVo.returnCode = "0000";
				androidVo.returnMsg = "";
				transaction.end();
				return 1;
			}
			String STS = MVO.getDEM_STS();
			int result = inOutDao.deleteEVENTMODEL(DOCNO, CENTER, MODEL);
			if(result <= 0)
			{
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = MODEL + " 삭제실패! 다시 시도해주세요.";
				transaction.end();
				return -3;
			}
			result = inOutDao.deleteInOut(DOCNO, "2", CENTER, MODEL);
			if(result <= 0)
			{
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = MODEL + " 삭제실패! 다시 시도해주세요.";
				transaction.end();
				return -4;
			}

			result = inOutDao.updateJego(MODEL, CENTER, MVO.getDEM_QTY());
			if(result <= 0)
			{
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = MODEL + "재고 업데이트 실패! 다시 시도해주세요.";
				transaction.end();
				return -5;
			}
			
			result = deletePriceJego(DOCNO, CENTER, MODEL);
			if(result <= 0)
			{
				androidVo.returnCode = "XXXX";
				androidVo.returnMsg = MODEL + "저장실패! 재고 반영 실패";
				transaction.end();
				return -6;
			}
			if(STS.equals("2") || STS.equals("4")) {
				result = inOutDao.deleteInOut(DOCNO, "1", CENTER, MODEL);
				if(result <= 0)
				{
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = MODEL + " 삭제실패! 다시 시도해주세요.";
					transaction.end();
					return -4;
				}
				result = inOutDao.updateChulhaJego(MODEL, CENTER, MVO.getDEM_QTY(), "");
				if(result <= 0)
				{
					androidVo.returnCode = "XXXX";
					androidVo.returnMsg = MODEL + "재고 업데이트 실패! 다시 시도해주세요.";
					transaction.end();
					return -5;
				}
			}

			androidVo.returnCode = "0000";
			androidVo.returnMsg = "";
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			androidVo.returnCode = "XXXX";
			androidVo.returnMsg = "알수없는 에러가 발생하였습니다.";
			
			transaction.end();
			return -6;
		}
	}
	public int insertPriceOut(String DOCNO, String CENTER, String MODEL, int QTY, String USERID, String PROG) throws Exception{
		List<PriceInVo> PRICELIST = inOutDao.getPriceList(MODEL, CENTER);
		if(PRICELIST == null)
			return -1;
		ModelVo MVO = modelDao.getModelInfo(MODEL);
		if(MVO == null)
			return -1;
		
		if(MVO.getMMC_KINDS().equals("3"))
			return 1;
		
		int TQTY = QTY;
		for (PriceInVo priceVo : PRICELIST) {
			int JEGO = priceVo.getDPI_OUTQTY();
			if(TQTY >= JEGO) {
				TQTY -= JEGO;
				if(inOutDao.updatePriceJego(priceVo.getDPI_DOCNO(), MODEL, CENTER, priceVo.getDPI_LOGCNO(), (JEGO*(-1))) <= 0) {
					return -1;
				}
				PriceOutVo vo = new PriceOutVo();
				vo.setDPO_DOCNO(DOCNO);
				vo.setDPO_INDOCNO(priceVo.getDPI_DOCNO());
				vo.setDPO_CENTER(CENTER);
				vo.setDPO_MODEL(MODEL);
				vo.setDPO_LOGCNO(priceVo.getDPI_LOGCNO());
				vo.setDPO_QTY(JEGO);
				vo.setDPO_PRICE(priceVo.getDPI_PRICE());
				vo.setCMN_MAK_ID(USERID);
				vo.setCMN_MAK_PROG(PROG);
				vo.setCMN_UPD_ID(USERID);
				vo.setCMN_UPD_PROG(PROG);
				if(inOutDao.insertPriceMoveOut(vo) <= 0) {
					return -1;
				}
			}
			else {
				if(inOutDao.updatePriceJego(priceVo.getDPI_DOCNO(), MODEL, CENTER, priceVo.getDPI_LOGCNO(), (TQTY*(-1))) <= 0) {
					return -1;
				}
				PriceOutVo vo = new PriceOutVo();
				vo.setDPO_DOCNO(DOCNO);
				vo.setDPO_INDOCNO(priceVo.getDPI_DOCNO());
				vo.setDPO_CENTER(CENTER);
				vo.setDPO_MODEL(MODEL);
				vo.setDPO_LOGCNO(priceVo.getDPI_LOGCNO());
				vo.setDPO_QTY(TQTY);
				vo.setDPO_PRICE(priceVo.getDPI_PRICE());
				vo.setCMN_MAK_ID(USERID);
				vo.setCMN_MAK_PROG(PROG);
				vo.setCMN_UPD_ID(USERID);
				vo.setCMN_UPD_PROG(PROG);
				if(inOutDao.insertPriceMoveOut(vo) <= 0) {
					return -1;
				}
				return 1;
			}
			if(TQTY == 0)
				return 1;
		}
		if(TQTY > 0)
			return -1;
		else
			return 1;
	}
	public int updatePriceJego(String DOCNO, String CENTER, String MODEL, int QTY, String USERID, String PROG) throws Exception{
		List<PriceInVo> priceList = inOutDao.getPriceJego(DOCNO, CENTER, MODEL);
		if(priceList == null)
			return -1;
		if(QTY < 0) {
			int TEMP_QTY = QTY*(-1);
			for (PriceInVo priceVo : priceList) {
				if(priceVo.getDPI_OUTQTY() > 0) {
					if(TEMP_QTY >= priceVo.getDPI_OUTQTY()) {
						TEMP_QTY -= priceVo.getDPI_OUTQTY();
						if(inOutDao.updatePriceJego(priceVo.getDPI_DOCNO(), MODEL, CENTER, priceVo.getDPI_LOGCNO(), (priceVo.getDPI_OUTQTY()*(-1))) <= 0) {
							return -1;
						}
						if(inOutDao.updatePriceOutJego(priceVo.getDPI_DOCNO(), DOCNO, CENTER, MODEL, priceVo.getDPI_LOGCNO(), priceVo.getDPI_OUTQTY()) <= 0) {
							return -1;
						}
					}
					else {
						if(inOutDao.updatePriceJego(priceVo.getDPI_DOCNO(), MODEL, CENTER, priceVo.getDPI_LOGCNO(), (TEMP_QTY*(-1))) <= 0) {
							return -1;
						}
						if(inOutDao.updatePriceOutJego(priceVo.getDPI_DOCNO(), DOCNO, CENTER, MODEL, priceVo.getDPI_LOGCNO(), TEMP_QTY) <= 0) {
							return -1;
						}
						return 1;
					}
					if(TEMP_QTY == 0)
						return 1;
				}
			}
			if(TEMP_QTY > 0) {
				return insertPriceOut(DOCNO, CENTER, MODEL, TEMP_QTY, USERID, PROG);
			}
		}
		else {
			int TEMP_QTY = QTY;
			for (PriceInVo priceVo : priceList) {
				if(TEMP_QTY >= priceVo.getJEGO()) {
					TEMP_QTY -= priceVo.getJEGO();
					if(inOutDao.deletePriceOutJego(priceVo.getDPI_DOCNO(), DOCNO, CENTER, MODEL, priceVo.getDPI_LOGCNO()) <= 0) {
						return -1;
					}
					if(inOutDao.updatePriceJego(priceVo.getDPI_DOCNO(), MODEL, CENTER, priceVo.getDPI_LOGCNO(), priceVo.getJEGO()) <= 0) {
						return -1;
					}
				}
				else {
					if(inOutDao.updatePriceOutJego(priceVo.getDPI_DOCNO(), DOCNO, CENTER, MODEL, priceVo.getDPI_LOGCNO(), (TEMP_QTY*(-1))) <= 0) {
						return -1;
					}
					if(inOutDao.updatePriceJego(priceVo.getDPI_DOCNO(), MODEL, CENTER, priceVo.getDPI_LOGCNO(), TEMP_QTY) <= 0) {
						return -1;
					}
					return 1;
				}
				if(TEMP_QTY == 0)
					return 1;
			}
			if(TEMP_QTY > 0)
				return -1;
		}
		return 1;
	}
	public int deletePriceJego(String DOCNO, String CENTER, String MODEL) throws Exception{
		ModelVo MVO = modelDao.getModelInfo(MODEL);
		if(MVO == null)
			return -1;
		
		if(MVO.getMMC_KINDS().equals("3"))
			return 1;
		
		List<PriceInVo> priceList = inOutDao.getPriceJego(DOCNO, CENTER, MODEL);
		if(priceList == null)
			return -1;
		for (PriceInVo priceVo : priceList) {
			if(inOutDao.updatePriceJego(priceVo.getDPI_DOCNO(), MODEL, CENTER, priceVo.getDPI_LOGCNO(), priceVo.getJEGO()) <= 0) {
				return -1;
			}
			
			if(inOutDao.deletePriceOutJego(priceVo.getDPI_DOCNO(), DOCNO, CENTER, MODEL, priceVo.getDPI_LOGCNO()) <= 0) {
				return -1;
			}
		}
		return 1;
	}
	
	public List<EventVo> getEventReturn(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String ENAME, String BISSTS, int PAGE, int PSIZE) throws Exception{
		return inOutDao.getEventReturn(StartDay, EndDay, LOGC, BONBU, CENTER, ENAME, BISSTS, PAGE, PSIZE);
	}
	
	public String updateEventReturn(String ECHECK, String CMNT, String ETYPE, String USERID){
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
		Calendar nowDate = Calendar.getInstance();
		String NOWDATE = format1.format(nowDate.getTime());
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			String[] ECHECK_LIST = ECHECK.split(",");
			int DATACNT = ECHECK_LIST.length;
			int SUSCNT = 0;
			for (String TEMP : ECHECK_LIST) {
				String[] TEMP_LIST = TEMP.split("\\^");
				if(TEMP_LIST.length == 3) {
					String DOCNO = TEMP_LIST[0];
					String CENTER = TEMP_LIST[1];
					String MODEL = TEMP_LIST[2];
					EventModelVo EVO = null;
					try {
						EVO = inOutDao.getEventModel(DOCNO, CENTER, MODEL);
					} catch (Exception e1) {
						throw new Exception("상품코드 : " + MODEL + "에 정보를 가져오지 못했습니다.");
					}
					if(EVO == null)
						continue;
					if(ETYPE.equals("3")) {
						if(!EVO.getDEM_STS().equals("1")) {
							SUSCNT++;
							continue;
						}
					}
					else {
						if(EVO.getDEM_STS().equals("3")) {
							int result = inOutDao.insertInOut(DOCNO, MODEL, "1", NOWDATE, EVO.getDEM_QTY(), "3", CENTER, "EVENT_UPDATE", USERID);
							if(result <= 0)
								throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");

							result = inOutDao.updateJego(MODEL, CENTER, EVO.getDEM_QTY());
							if(result <= 0)
								throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");
						}
						else {
							SUSCNT++;
							continue;
						}
					}
					EventVo vo = new EventVo();
					vo.setDEM_DOCNO(DOCNO);
					vo.setDEM_CENTER(CENTER);
					vo.setDEM_MODEL(MODEL);
					vo.setDEM_REUESR(USERID);
					vo.setDEM_REDATE(NOWDATE);
					vo.setDEM_CMNT(CMNT);
					vo.setDEM_STS(ETYPE);
					vo.setCMN_UPD_ID(USERID);
					vo.setCMN_UPD_PROG("EVENT_UPDATE");
					int result;
					try {
						result = inOutDao.updateEventReturn(vo);
						if(result > 0)
							SUSCNT++;
					} catch (Exception e) {
					}
				}
			}
			if(DATACNT == SUSCNT) {
				transaction.commit();
			}
			else {
				throw new Exception("업데이트 실패! 다시 시도해주세요.");
			}

			return "";
		}
		catch (Exception e) {
			transaction.end();
			return e.getMessage();
		}
	}
	
	public List<ModelBepumVo> getModelBepum(String LOGC, String BONBU, String CENTER, String MNAME, String BISSTS, int PAGE, int PSIZE) throws Exception{
		return inOutDao.getModelBepum(LOGC, BONBU, CENTER, MNAME, BISSTS, PAGE, PSIZE);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidModelRetuen(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String ENAME, String BISSTS) throws Exception{
		return inOutDao.getAndroidModelRetuen(StartDay, EndDay, LOGC, BONBU, CENTER, ENAME, BISSTS);
	}
	
	public String updateAndroidReturn(String DOCNO, String CENTER, String MODEL, String BISSTS, String USERID){
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
		Calendar nowDate = Calendar.getInstance();
		String NOWDATE = format1.format(nowDate.getTime());
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			EventModelVo EVO = null;
			try {
				EVO = inOutDao.getEventModel(DOCNO, CENTER, MODEL);
			} catch (Exception e1) {
				throw new Exception("상품코드 : " + MODEL + "에 정보를 가져오지 못했습니다.");
			}
			if(EVO == null)
				throw new Exception("상품코드 : " + MODEL + "에 정보를 가져오지 못했습니다.");
			if(BISSTS.equals("2")) {
				if(EVO.getDEM_STS().equals("1")) {
					int result = inOutDao.insertInOut(DOCNO, MODEL, "1", NOWDATE, EVO.getDEM_QTY(), "3", CENTER, "EVENT_UPDATE", USERID);
					if(result <= 0)
						throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");

					result = inOutDao.updateJego(MODEL, CENTER, EVO.getDEM_QTY());
					if(result <= 0)
						throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");
				}
				else {
					throw new Exception("상품코드 : " + MODEL + " 설치중인 상품이 아닙니다.");
				}
				EventVo vo = new EventVo();
				vo.setDEM_DOCNO(DOCNO);
				vo.setDEM_CENTER(CENTER);
				vo.setDEM_MODEL(MODEL);
				vo.setDEM_REUESR(USERID);
				vo.setDEM_REDATE(NOWDATE);
				vo.setDEM_CMNT("");
				vo.setDEM_STS(BISSTS);
				vo.setCMN_UPD_ID(USERID);
				vo.setCMN_UPD_PROG("EVENT_UPDATE");
				int result = inOutDao.updateAndroidReturn(vo);
				if(result <= 0)
					throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");
			}
			else {
				if(EVO.getDEM_STS().equals("2")) {
					MoveVo moveVo = getJegoModel(MODEL, CENTER, NOWDATE);
					if(moveVo == null) {
						throw new Exception("상품코드 : " + MODEL + "에 재고정보를 가져오지 못했습니다.");
					}
					int NOWJEGO = moveVo.getDMJ_JEGO();
					int OUTJEGO = moveVo.getOUTJEGO();
					if(OUTJEGO < EVO.getDEM_QTY()) {
						throw new Exception("상품코드 : " + MODEL + " 재고가 부족합니다. 현 재고 : " + OUTJEGO);
					}
					else if(NOWJEGO < EVO.getDEM_QTY()) {
						throw new Exception("상품코드 : " + MODEL + " 재고가 부족합니다. 현 재고 : " + NOWJEGO);
					}
					int result = inOutDao.deleteInOut(DOCNO, "1", CENTER, MODEL);
					if(result <= 0)
						throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");
					result = inOutDao.updateChulhaJego(MODEL, CENTER, EVO.getDEM_QTY(), NOWDATE);
					if(result <= 0)
						throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");
					
					EventVo vo = new EventVo();
					vo.setDEM_DOCNO(DOCNO);
					vo.setDEM_CENTER(CENTER);
					vo.setDEM_MODEL(MODEL);
					vo.setDEM_REUESR("");
					vo.setDEM_REDATE(null);
					vo.setDEM_CMNT("");
					vo.setDEM_STS(BISSTS);
					vo.setCMN_UPD_ID(USERID);
					vo.setCMN_UPD_PROG("EVENT_UPDATE");
					result = inOutDao.updateAndroidReturn(vo);
					if(result <= 0)
						throw new Exception("상품코드 : " + MODEL + " 업데이트 실패! 다시 시도해주세요.");
				}
				else {
					throw new Exception("상품코드 : " + MODEL + " 회수된 상품이 아닙니다.");
				}
			}
			transaction.commit();
			return "";
		}
		catch (Exception e) {
			transaction.end();
			return e.getMessage();
		}
	}
	
	public List<JegoMagamReportVo> getJegoMagamReport(int YEAR, int MONTH, String LOGC, String BONBU, String CENTER, String LGROUP, String KINDS){
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar cal = Calendar.getInstance();
	        cal.set(YEAR, MONTH-1, 1); //월은 -1해줘야 해당월로 인식
	        int LastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        String StartDay = dateFormat.format(cal.getTime());
	        cal.set(YEAR, MONTH-1, LastDay); //월은 -1해줘야 해당월로 인식
	        String EndDay = dateFormat.format(cal.getTime());
	        
			List<JegoMagamReportVo> JEGOLIST = inOutDao.getJegoMagamReport(LOGC, BONBU, CENTER, LGROUP, KINDS, StartDay, EndDay);
			if(JEGOLIST == null || JEGOLIST.size() == 0)
				return JEGOLIST;
			List<JegoMagamReportVo> return_list = new ArrayList<JegoMagamReportVo>();
			
			int pqty = 0;
			int qprice = 0;
			int logcqty = 0;
			int logcprice = 0;
			int moveinqty = 0;
			int moveinprice = 0;
			int moveoutqty = 0;
			int moveoutprice = 0;
			int otherqty = 0;
			int otherprice = 0;
			int eventqty = 0;
			int eventprice = 0;
			int nowqty = 0;
			int nowprice = 0;
			
			LinkedHashMap<String, JegoMagamReportVo> TOTALLIST = new LinkedHashMap<String, JegoMagamReportVo>();
			for (int i = 0; i < JEGOLIST.size(); ++i) {
				JegoMagamReportVo jegoVo = (JegoMagamReportVo)JEGOLIST.get(i);
				if(TOTALLIST.containsKey(jegoVo.getMMC_CODE())) {
					JegoMagamReportVo vo = TOTALLIST.get(jegoVo.getMMC_CODE());
					vo.setPqty(vo.getPqty() + jegoVo.getPqty());
					vo.setPprice(vo.getPprice() + jegoVo.getPprice());
					vo.setLogcqty(vo.getLogcqty() + jegoVo.getLogcqty());
					vo.setLogcprice(vo.getLogcprice() + jegoVo.getLogcprice());
					vo.setMoveinqty(vo.getMoveinqty() + jegoVo.getMoveinqty());
					vo.setMoveinprice(vo.getMoveinprice() + jegoVo.getMoveinprice());
					vo.setMoveoutqty(vo.getMoveoutqty() + jegoVo.getMoveoutqty());
					vo.setMoveoutprice(vo.getMoveoutprice() + jegoVo.getMoveoutprice());
					vo.setOtherqty(vo.getOtherqty() + jegoVo.getOtherqty());
					vo.setOtherprice(vo.getOtherprice() + jegoVo.getOtherprice());
					vo.setEventqty(vo.getEventqty() + jegoVo.getEventqty());
					vo.setEventprice(vo.getEventprice() + jegoVo.getEventprice());
					vo.setNowqty(vo.getNowqty() + jegoVo.getNowqty());
					vo.setNowprice(vo.getNowprice() + jegoVo.getNowprice());
				}
				else {
					JegoMagamReportVo vo = (JegoMagamReportVo)jegoVo.clone();
					vo.setMCI_CODE("");
					vo.setMCI_LOGC("");
					vo.setMCI_LOGCNAME("전체");
					vo.setMCI_Bonbu("");
					vo.setMCI_BonbuNAME("전체");
					vo.setMCI_Center("");
					vo.setMCI_CenterName("전체");
					TOTALLIST.put(jegoVo.getMMC_CODE(), vo);
				}
			}
			Iterator keyData = TOTALLIST.keySet().iterator();
            while (keyData.hasNext()) {
            	String key = (String) keyData.next();
            	JegoMagamReportVo v = (JegoMagamReportVo) TOTALLIST.get(key);
            	JEGOLIST.add(v);
            } // while
			for (int i = 0; i < JEGOLIST.size(); ++i) {
				JegoMagamReportVo jegoVo = (JegoMagamReportVo)JEGOLIST.get(i).clone();
				if(i == 0 ) {
					pqty = jegoVo.getPqty();
					qprice = jegoVo.getPprice();
					logcqty = jegoVo.getLogcqty();
					logcprice = jegoVo.getLogcprice();
					moveinqty = jegoVo.getMoveinqty();
					moveinprice = jegoVo.getMoveinprice();
					moveoutqty = jegoVo.getMoveoutqty();
					moveoutprice = jegoVo.getMoveoutprice();
					otherqty = jegoVo.getOtherqty();
					otherprice = jegoVo.getOtherprice();
					eventqty = jegoVo.getEventqty();
					eventprice = jegoVo.getEventprice();
					nowqty = jegoVo.getNowqty();
					nowprice = jegoVo.getNowprice();
					return_list.add(jegoVo);
					continue;
				}
				JegoMagamReportVo backjegoVo = (JegoMagamReportVo)JEGOLIST.get(i - 1).clone();
				if(jegoVo.getMCI_CODE().equals(backjegoVo.getMCI_CODE()) && jegoVo.getMMC_LGROUP().equals(backjegoVo.getMMC_LGROUP())) {
					pqty += jegoVo.getPqty();
					qprice += jegoVo.getPprice();
					logcqty += jegoVo.getLogcqty();
					logcprice += jegoVo.getLogcprice();
					moveinqty += jegoVo.getMoveinqty();
					moveinprice += jegoVo.getMoveinprice();
					moveoutqty += jegoVo.getMoveoutqty();
					moveoutprice += jegoVo.getMoveoutprice();
					otherqty += jegoVo.getOtherqty();
					otherprice += jegoVo.getOtherprice();
					eventqty += jegoVo.getEventqty();
					eventprice += jegoVo.getEventprice();
					nowqty += jegoVo.getNowqty();
					nowprice += jegoVo.getNowprice();
					return_list.add(jegoVo);
					if(i == (JEGOLIST.size() - 1)) {
						if(return_list.get(return_list.size() - 1).getSumtype() == 0) {
							JegoMagamReportVo vo = (JegoMagamReportVo)jegoVo.clone();
							vo.setSumtype(1);
							vo.setPqty(pqty);
							vo.setPprice(qprice);
							vo.setLogcqty(logcqty);
							vo.setLogcprice(logcprice);
							vo.setMoveinqty(moveinqty);
							vo.setMoveinprice(moveinprice);
							vo.setMoveoutqty(moveoutqty);
							vo.setMoveoutprice(moveoutprice);
							vo.setOtherqty(otherqty);
							vo.setOtherprice(otherprice);
							vo.setEventqty(eventqty);
							vo.setEventprice(eventprice);
							vo.setNowqty(nowqty);
							vo.setNowprice(nowprice);
							return_list.add(vo);
						}
						
						pqty = 0;
						qprice = 0;
						logcqty = 0;
						logcprice = 0;
						moveinqty = 0;
						moveinprice = 0;
						moveoutqty = 0;
						moveoutprice = 0;
						otherqty = 0;
						otherprice = 0;
						eventqty = 0;
						eventprice = 0;
						nowqty = 0;
						nowprice = 0;
						
						for (JegoMagamReportVo returnVal : return_list) {
							if(jegoVo.getMCI_CODE().equals(returnVal.getMCI_CODE()) && returnVal.getSumtype() == 1) {
								pqty += returnVal.getPqty();
								qprice += returnVal.getPprice();
								logcqty += returnVal.getLogcqty();
								logcprice += returnVal.getLogcprice();
								moveinqty += returnVal.getMoveinqty();
								moveinprice += returnVal.getMoveinprice();
								moveoutqty += returnVal.getMoveoutqty();
								moveoutprice += returnVal.getMoveoutprice();
								otherqty += returnVal.getOtherqty();
								otherprice += returnVal.getOtherprice();
								eventqty += returnVal.getEventqty();
								eventprice += returnVal.getEventprice();
								nowqty += returnVal.getNowqty();
								nowprice += returnVal.getNowprice();
							}
						}
						JegoMagamReportVo vo = (JegoMagamReportVo)jegoVo.clone();
						vo.setSumtype(2);
						vo.setPqty(pqty);
						vo.setPprice(qprice);
						vo.setLogcqty(logcqty);
						vo.setLogcprice(logcprice);
						vo.setMoveinqty(moveinqty);
						vo.setMoveinprice(moveinprice);
						vo.setMoveoutqty(moveoutqty);
						vo.setMoveoutprice(moveoutprice);
						vo.setOtherqty(otherqty);
						vo.setOtherprice(otherprice);
						vo.setEventqty(eventqty);
						vo.setEventprice(eventprice);
						vo.setNowqty(nowqty);
						vo.setNowprice(nowprice);
						return_list.add(vo);
					}
				}
				else {
					if(!jegoVo.getMMC_LGROUP().equals(backjegoVo.getMMC_LGROUP())) {
						JegoMagamReportVo vo = (JegoMagamReportVo)backjegoVo.clone();
						vo.setSumtype(1);
						vo.setPqty(pqty);
						vo.setPprice(qprice);
						vo.setLogcqty(logcqty);
						vo.setLogcprice(logcprice);
						vo.setMoveinqty(moveinqty);
						vo.setMoveinprice(moveinprice);
						vo.setMoveoutqty(moveoutqty);
						vo.setMoveoutprice(moveoutprice);
						vo.setOtherqty(otherqty);
						vo.setOtherprice(otherprice);
						vo.setEventqty(eventqty);
						vo.setEventprice(eventprice);
						vo.setNowqty(nowqty);
						vo.setNowprice(nowprice);
						return_list.add(vo);
						
						pqty = 0;
						qprice = 0;
						logcqty = 0;
						logcprice = 0;
						moveinqty = 0;
						moveinprice = 0;
						moveoutqty = 0;
						moveoutprice = 0;
						otherqty = 0;
						otherprice = 0;
						eventqty = 0;
						eventprice = 0;
						nowqty = 0;
						nowprice = 0;
					}
					if(!jegoVo.getMCI_CODE().equals(backjegoVo.getMCI_CODE())){
						if(return_list.get(return_list.size() - 1).getSumtype() == 0) {
							JegoMagamReportVo vo = (JegoMagamReportVo)backjegoVo.clone();
							vo.setSumtype(1);
							vo.setPqty(pqty);
							vo.setPprice(qprice);
							vo.setLogcqty(logcqty);
							vo.setLogcprice(logcprice);
							vo.setMoveinqty(moveinqty);
							vo.setMoveinprice(moveinprice);
							vo.setMoveoutqty(moveoutqty);
							vo.setMoveoutprice(moveoutprice);
							vo.setOtherqty(otherqty);
							vo.setOtherprice(otherprice);
							vo.setEventqty(eventqty);
							vo.setEventprice(eventprice);
							vo.setNowqty(nowqty);
							vo.setNowprice(nowprice);
							return_list.add(vo);
							
							pqty = 0;
							qprice = 0;
							logcqty = 0;
							logcprice = 0;
							moveinqty = 0;
							moveinprice = 0;
							moveoutqty = 0;
							moveoutprice = 0;
							otherqty = 0;
							otherprice = 0;
							eventqty = 0;
							eventprice = 0;
							nowqty = 0;
							nowprice = 0;
						}
						pqty = 0;
						qprice = 0;
						logcqty = 0;
						logcprice = 0;
						moveinqty = 0;
						moveinprice = 0;
						moveoutqty = 0;
						moveoutprice = 0;
						otherqty = 0;
						otherprice = 0;
						eventqty = 0;
						eventprice = 0;
						nowqty = 0;
						nowprice = 0;
						for (JegoMagamReportVo returnVal : return_list) {
							if(backjegoVo.getMCI_CODE().equals(returnVal.getMCI_CODE()) && returnVal.getSumtype() == 1) {
								pqty += returnVal.getPqty();
								qprice += returnVal.getPprice();
								logcqty += returnVal.getLogcqty();
								logcprice += returnVal.getLogcprice();
								moveinqty += returnVal.getMoveinqty();
								moveinprice += returnVal.getMoveinprice();
								moveoutqty += returnVal.getMoveoutqty();
								moveoutprice += returnVal.getMoveoutprice();
								otherqty += returnVal.getOtherqty();
								otherprice += returnVal.getOtherprice();
								eventqty += returnVal.getEventqty();
								eventprice += returnVal.getEventprice();
								nowqty += returnVal.getNowqty();
								nowprice += returnVal.getNowprice();
							}
						}
						JegoMagamReportVo vo = (JegoMagamReportVo)backjegoVo.clone();
						vo.setSumtype(2);
						vo.setPqty(pqty);
						vo.setPprice(qprice);
						vo.setLogcqty(logcqty);
						vo.setLogcprice(logcprice);
						vo.setMoveinqty(moveinqty);
						vo.setMoveinprice(moveinprice);
						vo.setMoveoutqty(moveoutqty);
						vo.setMoveoutprice(moveoutprice);
						vo.setOtherqty(otherqty);
						vo.setOtherprice(otherprice);
						vo.setEventqty(eventqty);
						vo.setEventprice(eventprice);
						vo.setNowqty(nowqty);
						vo.setNowprice(nowprice);
						return_list.add(vo);
					}
					
					pqty = jegoVo.getPqty();
					qprice = jegoVo.getPprice();
					logcqty = jegoVo.getLogcqty();
					logcprice = jegoVo.getLogcprice();
					moveinqty = jegoVo.getMoveinqty();
					moveinprice = jegoVo.getMoveinprice();
					moveoutqty = jegoVo.getMoveoutqty();
					moveoutprice = jegoVo.getMoveoutprice();
					otherqty = jegoVo.getOtherqty();
					otherprice = jegoVo.getOtherprice();
					eventqty = jegoVo.getEventqty();
					eventprice = jegoVo.getEventprice();
					nowqty = jegoVo.getNowqty();
					nowprice = jegoVo.getNowprice();
					return_list.add(jegoVo);
					
					if(i == (JEGOLIST.size() - 1)) {
						if(return_list.get(return_list.size() - 1).getSumtype() == 0) {
							JegoMagamReportVo vo = (JegoMagamReportVo)jegoVo.clone();
							vo.setSumtype(1);
							vo.setPqty(pqty);
							vo.setPprice(qprice);
							vo.setLogcqty(logcqty);
							vo.setLogcprice(logcprice);
							vo.setMoveinqty(moveinqty);
							vo.setMoveinprice(moveinprice);
							vo.setMoveoutqty(moveoutqty);
							vo.setMoveoutprice(moveoutprice);
							vo.setOtherqty(otherqty);
							vo.setOtherprice(otherprice);
							vo.setEventqty(eventqty);
							vo.setEventprice(eventprice);
							vo.setNowqty(nowqty);
							vo.setNowprice(nowprice);
							return_list.add(vo);
						}
						
						pqty = 0;
						qprice = 0;
						logcqty = 0;
						logcprice = 0;
						moveinqty = 0;
						moveinprice = 0;
						moveoutqty = 0;
						moveoutprice = 0;
						otherqty = 0;
						otherprice = 0;
						eventqty = 0;
						eventprice = 0;
						nowqty = 0;
						nowprice = 0;
						
						for (JegoMagamReportVo returnVal : return_list) {
							if(jegoVo.getMCI_CODE().equals(returnVal.getMCI_CODE()) && returnVal.getSumtype() == 1) {
								pqty += returnVal.getPqty();
								qprice += returnVal.getPprice();
								logcqty += returnVal.getLogcqty();
								logcprice += returnVal.getLogcprice();
								moveinqty += returnVal.getMoveinqty();
								moveinprice += returnVal.getMoveinprice();
								moveoutqty += returnVal.getMoveoutqty();
								moveoutprice += returnVal.getMoveoutprice();
								otherqty += returnVal.getOtherqty();
								otherprice += returnVal.getOtherprice();
								eventqty += returnVal.getEventqty();
								eventprice += returnVal.getEventprice();
								nowqty += returnVal.getNowqty();
								nowprice += returnVal.getNowprice();
							}
						}
						JegoMagamReportVo vo = (JegoMagamReportVo)jegoVo.clone();
						vo.setSumtype(2);
						vo.setPqty(pqty);
						vo.setPprice(qprice);
						vo.setLogcqty(logcqty);
						vo.setLogcprice(logcprice);
						vo.setMoveinqty(moveinqty);
						vo.setMoveinprice(moveinprice);
						vo.setMoveoutqty(moveoutqty);
						vo.setMoveoutprice(moveoutprice);
						vo.setOtherqty(otherqty);
						vo.setOtherprice(otherprice);
						vo.setEventqty(eventqty);
						vo.setEventprice(eventprice);
						vo.setNowqty(nowqty);
						vo.setNowprice(nowprice);
						return_list.add(vo);
					}
				}
			}
			
			
			return return_list;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public int createJegoMagamExcel(List<JegoMagamReportVo> JEGO_LIST, HttpServletRequest request, String FILENAME) {
		try {
			List<CellRangeAddress> passCell = new ArrayList<CellRangeAddress>();
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow row = null;
			XSSFCell cell = null;
			int rowNo = 0;
			
			sheet.setDefaultColumnWidth(8);
			sheet.setDefaultRowHeight((short)355);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(4, 6000);
			XSSFFont Headerfont = workbook.createFont();
			Headerfont.setFontName("맑은 고딕"); //폰트명
			Headerfont.setFontHeightInPoints((short)10); //폰트크기
			Headerfont.setBold(true); //볼드 
			XSSFCellStyle cellStyle_Header = workbook.createCellStyle(); 
			cellStyle_Header.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Header.setFont(Headerfont);
			cellStyle_Header.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_Header.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Header.setBorderTop(BorderStyle.THIN);
			cellStyle_Header.setBorderBottom(BorderStyle.THIN);
			cellStyle_Header.setBorderLeft(BorderStyle.THIN);
			cellStyle_Header.setBorderRight(BorderStyle.THIN);
			cellStyle_Header.setFillForegroundColor(new XSSFColor(new java.awt.Color(235, 247, 255)));
			cellStyle_Header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			XSSFFont bodyfont = workbook.createFont();
			bodyfont.setFontName("맑은 고딕"); //폰트명
			bodyfont.setFontHeightInPoints((short)10); //폰트크기
			bodyfont.setBold(false); //볼드 
			XSSFCellStyle cellStyle_Lgroup = workbook.createCellStyle(); 
			cellStyle_Lgroup.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Lgroup.setFont(bodyfont);
			cellStyle_Lgroup.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_Lgroup.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Lgroup.setBorderTop(BorderStyle.THIN);
			cellStyle_Lgroup.setBorderBottom(BorderStyle.THIN);
			cellStyle_Lgroup.setBorderLeft(BorderStyle.THIN);
			cellStyle_Lgroup.setBorderRight(BorderStyle.THIN);
			cellStyle_Lgroup.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 228)));
			cellStyle_Lgroup.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			XSSFCellStyle cellStyle_Sgroup = workbook.createCellStyle(); 
			cellStyle_Sgroup.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Sgroup.setFont(bodyfont);
			cellStyle_Sgroup.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_Sgroup.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Sgroup.setBorderTop(BorderStyle.THIN);
			cellStyle_Sgroup.setBorderBottom(BorderStyle.THIN);
			cellStyle_Sgroup.setBorderLeft(BorderStyle.THIN);
			cellStyle_Sgroup.setBorderRight(BorderStyle.THIN);
			cellStyle_Sgroup.setFillForegroundColor(new XSSFColor(new java.awt.Color(242, 255, 237)));
			cellStyle_Sgroup.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			XSSFCellStyle cellStyle_body = workbook.createCellStyle(); 
			cellStyle_body.setFont(bodyfont);
			cellStyle_body.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_body.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_body.setBorderTop(BorderStyle.THIN);
			cellStyle_body.setBorderBottom(BorderStyle.THIN);
			cellStyle_body.setBorderLeft(BorderStyle.THIN);
			cellStyle_body.setBorderRight(BorderStyle.THIN);
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo + 1, 0, 0));
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short)0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("본 부");
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, 1, 1));
			cell = row.createCell((short)1);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("센 터");
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, 2, 2));
			cell = row.createCell((short)2);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("상품코드");
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, 3, 3));
			cell = row.createCell((short)3);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("대분류");
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, 4, 4));
			cell = row.createCell((short)4);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("중분류");
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, 5, 5));
			cell = row.createCell((short)5);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("소분류");
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 6, 7));
			cell = row.createCell((short)6);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("전월 재고");
			
			cell = row.createCell((short)7);
			cell.setCellStyle(cellStyle_Header);
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 8, 9));
			cell = row.createCell((short)8);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("물류센터 입고");
			
			cell = row.createCell((short)9);
			cell.setCellStyle(cellStyle_Header);
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 10, 11));
			cell = row.createCell((short)10);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("본부별 이동(입고)");
			
			cell = row.createCell((short)11);
			cell.setCellStyle(cellStyle_Header);
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 12, 13));
			cell = row.createCell((short)12);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("본부별 이동(출고)");
			
			cell = row.createCell((short)13);
			cell.setCellStyle(cellStyle_Header);
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 14, 15));
			cell = row.createCell((short)14);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("기타 출고");
			
			cell = row.createCell((short)15);
			cell.setCellStyle(cellStyle_Header);
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 16, 17));
			cell = row.createCell((short)16);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("당월 행사 출고");
			
			cell = row.createCell((short)17);
			cell.setCellStyle(cellStyle_Header);
			
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 18, 19));
			cell = row.createCell((short)18);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("당월 말일 재고");
			
			cell = row.createCell((short)19);
			cell.setCellStyle(cellStyle_Header);
			
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short)0);
			cell.setCellStyle(cellStyle_Header);
			cell = row.createCell((short)1);
			cell.setCellStyle(cellStyle_Header);
			cell = row.createCell((short)2);
			cell.setCellStyle(cellStyle_Header);
			cell = row.createCell((short)3);
			cell.setCellStyle(cellStyle_Header);
			cell = row.createCell((short)4);
			cell.setCellStyle(cellStyle_Header);
			cell = row.createCell((short)5);
			cell.setCellStyle(cellStyle_Header);
			cell = row.createCell((short)6);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수량");
			cell = row.createCell((short)7);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("금액");
			
			cell = row.createCell((short)8);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수량");
			cell = row.createCell((short)9);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("금액");
			
			cell = row.createCell((short)10);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수량");
			cell = row.createCell((short)11);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("금액");
			
			cell = row.createCell((short)12);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수량");
			cell = row.createCell((short)13);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("금액");
			
			cell = row.createCell((short)14);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수량");
			cell = row.createCell((short)15);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("금액");
			
			cell = row.createCell((short)16);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수량");
			cell = row.createCell((short)17);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("금액");
			
			cell = row.createCell((short)18);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수량");
			cell = row.createCell((short)19);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("금액");
			
			DecimalFormat formatter = new DecimalFormat("###,###");
			for (JegoMagamReportVo vo : JEGO_LIST) {
				XSSFCellStyle cellStyle = null;
				if(vo.getSumtype() == 1) {
					cellStyle = cellStyle_Sgroup;
				}
				else if(vo.getSumtype() == 2) {
					cellStyle = cellStyle_Lgroup;
				}
				else {
					cellStyle = cellStyle_body;
				}
				row = sheet.createRow(rowNo++);
				cell = row.createCell((short)0);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getMCI_BonbuNAME());

				cell = row.createCell((short)1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getMCI_CenterName());
				if(vo.getSumtype() == 1) {
					cell = row.createCell((short)2);
					cell.setCellStyle(cellStyle);

					cell = row.createCell((short)3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(vo.getMMC_LGROUPNAME());

					sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 4, 5));
					cell = row.createCell((short)4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue("소계");
					
					cell = row.createCell((short)5);
					cell.setCellStyle(cellStyle);
				}
				else if(vo.getSumtype() == 2) {
					sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 2, 5));
					cell = row.createCell((short)2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue("합계");
					
					cell = row.createCell((short)3);
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell((short)4);
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell((short)5);
					cell.setCellStyle(cellStyle);
				}
				else {
					cell = row.createCell((short)2);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(vo.getMMC_CODE());

					cell = row.createCell((short)3);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(vo.getMMC_LGROUPNAME());

					cell = row.createCell((short)4);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(vo.getMMC_MGROUP());

					cell = row.createCell((short)5);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(vo.getMMC_SGROUP());
				}
				cell = row.createCell((short)6);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getPqty());

				cell = row.createCell((short)7);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(formatter.format(vo.getPprice()));

				cell = row.createCell((short)8);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getLogcqty());

				cell = row.createCell((short)9);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(formatter.format(vo.getLogcprice()));

				cell = row.createCell((short)10);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getMoveinqty());

				cell = row.createCell((short)11);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(formatter.format(vo.getMoveinprice()));

				cell = row.createCell((short)12);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getMoveoutqty());

				cell = row.createCell((short)13);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(formatter.format(vo.getMoveoutprice()));

				cell = row.createCell((short)14);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getOtherqty());

				cell = row.createCell((short)15);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(formatter.format(vo.getOtherprice()));

				cell = row.createCell((short)16);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getEventqty());

				cell = row.createCell((short)17);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(formatter.format(vo.getEventprice()));

				cell = row.createCell((short)18);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(vo.getNowqty());

				cell = row.createCell((short)19);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(formatter.format(vo.getNowprice()));
			}
			
			//setBordersToMergedCells(sheet);
			
			String uploadPath = fileService.filePath(request, "TEMP");
			
			File file = new File(uploadPath, FILENAME);
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			workbook.write(fos);

			if (workbook != null)	workbook.close();
			if (fos != null) fos.close();
			return 1;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int createInOutReportExcel(List<InOutListVo> INOUTLIST, HttpServletRequest request, String FILENAME) {
		try {
			List<CellRangeAddress> passCell = new ArrayList<CellRangeAddress>();
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow row = null;
			XSSFCell cell = null;
			int rowNo = 0;
			
			sheet.setDefaultColumnWidth(8);
			sheet.setDefaultRowHeight((short)355);
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 4000);
			sheet.setColumnWidth(9, 6000);
			XSSFFont Headerfont = workbook.createFont();
			Headerfont.setFontName("맑은 고딕"); //폰트명
			Headerfont.setFontHeightInPoints((short)10); //폰트크기
			Headerfont.setBold(true); //볼드 
			XSSFCellStyle cellStyle_Header = workbook.createCellStyle(); 
			cellStyle_Header.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Header.setFont(Headerfont);
			cellStyle_Header.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_Header.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Header.setBorderTop(BorderStyle.THIN);
			cellStyle_Header.setBorderBottom(BorderStyle.THIN);
			cellStyle_Header.setBorderLeft(BorderStyle.THIN);
			cellStyle_Header.setBorderRight(BorderStyle.THIN);
			cellStyle_Header.setFillForegroundColor(new XSSFColor(new java.awt.Color(235, 247, 255)));
			cellStyle_Header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			XSSFFont bodyfont = workbook.createFont();
			bodyfont.setFontName("맑은 고딕"); //폰트명
			bodyfont.setFontHeightInPoints((short)10); //폰트크기
			bodyfont.setBold(false); //볼드 
			
			XSSFCellStyle cellStyle_body = workbook.createCellStyle(); 
			cellStyle_body.setFont(bodyfont);
			cellStyle_body.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_body.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_body.setBorderTop(BorderStyle.THIN);
			cellStyle_body.setBorderBottom(BorderStyle.THIN);
			cellStyle_body.setBorderLeft(BorderStyle.THIN);
			cellStyle_body.setBorderRight(BorderStyle.THIN);
			
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short)0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("입출고 구분");
			
			cell = row.createCell((short)1);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("구 분");
			
			cell = row.createCell((short)2);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("접수번호");
			
			cell = row.createCell((short)3);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("날 짜");
			
			cell = row.createCell((short)4);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("물류센터");
			
			cell = row.createCell((short)5);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("본 부");
			
			cell = row.createCell((short)6);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("센 터");
			
			cell = row.createCell((short)7);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("상품코드");
			
			cell = row.createCell((short)8);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("대분류");
			
			cell = row.createCell((short)9);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("중분류");
			
			cell = row.createCell((short)10);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("소분류");
			
			cell = row.createCell((short)11);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("수 량");
			
			for (InOutListVo vo : INOUTLIST) {
				row = sheet.createRow(rowNo++);
				cell = row.createCell((short)0);
				cell.setCellStyle(cellStyle_body);
				if(vo.getIOTYPE().equals("1"))
					cell.setCellValue("입고");
				else if(vo.getIOTYPE().equals("2"))
					cell.setCellValue("출고");

				cell = row.createCell((short)1);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getRESNNAME());
				
				cell = row.createCell((short)2);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getDOCNO());

				cell = row.createCell((short)3);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getIODATE());

				cell = row.createCell((short)4);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getLOGCNAME());

				cell = row.createCell((short)5);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getBONBUNAME());

				cell = row.createCell((short)6);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getCENTERNAME());

				cell = row.createCell((short)7);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getMODEL());

				cell = row.createCell((short)8);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getMMC_LGROUPNAME());

				cell = row.createCell((short)9);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getMMC_MGROUP());

				cell = row.createCell((short)10);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getMMC_SGROUP());

				cell = row.createCell((short)11);
				cell.setCellStyle(cellStyle_body);
				cell.setCellValue(vo.getQTY());
			}
			
			//setBordersToMergedCells(sheet);
			
			String uploadPath = fileService.filePath(request, "TEMP");
			
			File file = new File(uploadPath, FILENAME);
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			workbook.write(fos);

			if (workbook != null)	workbook.close();
			if (fos != null) fos.close();
			return 1;
		}
		catch (Exception e) {
			return -1;
		}
	}
}
