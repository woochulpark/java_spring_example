package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.dao.FormatDao;
import com.vo.DealFormatVo;
import com.vo.DealModelVo;
import com.vo.JegoFormatVo;
import com.vo.JegoMagamReportVo;
import com.vo.JegoModelVo;

@Service
public class FormatService {
	
	@Autowired
	private FormatDao formatDao;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private FileService fileService;
	
	
	public DealFormatVo getDealFormatInfo(String DOCNO){
		return formatDao.getDealFormatInfo(DOCNO);
	}
	
	public List<DealFormatVo> getDealFormatList(String StartDay, String EndDay, String INLOGC, String INBONBU, String INCENTER, String OUTLOGC, String OUTBONBU, String OUTCENTER, String INSTSTYPE, String OUTSTSTYPE, String CTYPE, int PAGE, int PSIZE){
		EndDay = EndDay.length() > 0 ? EndDay + " 23:59:59" : EndDay;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("INLOGC", INLOGC);
		map.put("INBONBU", INBONBU);
		map.put("INCENTER", INCENTER);
		map.put("OUTLOGC", OUTLOGC);
		map.put("OUTBONBU", OUTBONBU);
		map.put("OUTCENTER", OUTCENTER);
		map.put("INSTSTYPE", INSTSTYPE);
		map.put("OUTSTSTYPE", OUTSTSTYPE);
		map.put("CTYPE", CTYPE);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return formatDao.getDealFormatList(map);
	}
	
	public List<DealFormatVo> getDealInoutInfo(String StartDay, String EndDay, String INLOGC, String INBONBU, String INCENTER, String OUTLOGC, String OUTBONBU, String OUTCENTER, 
											   String MNAME, int PTYPE, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("INLOGC", INLOGC);
		map.put("INBONBU", INBONBU);
		map.put("INCENTER", INCENTER);
		map.put("OUTLOGC", OUTLOGC);
		map.put("OUTBONBU", OUTBONBU);
		map.put("OUTCENTER", OUTCENTER);
		map.put("MNAME", MNAME);
		map.put("PTYPE", PTYPE);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return formatDao.getDealInoutInfo(map);
	}
	
	public int insertFormat(String DOCNO, String USERID, String StartDay, String EndDay) {
		String[] TEMP_LIST = DOCNO.split(",");
		List<DealFormatVo> DOCLIST = new ArrayList<DealFormatVo>();
		for(int i = 0; i < TEMP_LIST.length;++i) {
			DealFormatVo vo = null;
			if(TEMP_LIST[i].length() != 13) {
				return -1;
			}
			if(TEMP_LIST[i].substring(8, 10).equals("02")) {
				vo = formatDao.getFormatLogcInfo(TEMP_LIST[i]);
			}
			else if(TEMP_LIST[i].substring(8, 10).equals("03")) {
				vo = formatDao.getFormatBonbuInfo(TEMP_LIST[i]);
			}
			else {
				return -1;
			}
			if(vo == null) {
				return -2;
			}
			DealModelVo modelVo = new DealModelVo();
			modelVo.setDDM_SUBDOCNO(vo.getDOCNO());
			modelVo.setDDM_MODEL(vo.getMODEL());
			modelVo.setDDM_DATE(vo.getOUTDATE());
			modelVo.setDDM_QTY(vo.getQTY());
			boolean Chk = true;
			for (DealFormatVo dealFormatVo : DOCLIST) {
				if(dealFormatVo.getINCENTER().equals(vo.getINCENTER()) && dealFormatVo.getOUTCENTER().equals(vo.getOUTCENTER())) {
					dealFormatVo.getModelList().add(modelVo);
					Chk = false;
					break;
				}
			}
			if(!Chk)
				continue;
			else
			{
				vo.setModelList(modelVo);
				DOCLIST.add(vo);
			}
		}
		if(DOCLIST.size() == 0)
			return -3;
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			for (DealFormatVo dealFormatVo : DOCLIST) {
				DealFormatVo SeqVo = formatDao.getLastFormatDocNo();
				int SEQ = SeqVo.getDDT_SEQ() + 1;
				String NOWDATE = SeqVo.getCMN_MAK_DATE();
				String BASE = NOWDATE + "05";
				String DDT_DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");
				
				DealFormatVo vo = new DealFormatVo();
				vo.setDDT_DOCNO(DDT_DOCNO);
				vo.setDDT_SEQ(SEQ);
				vo.setDDT_RESN(dealFormatVo.getDDT_RESN());
				vo.setDDT_STARTDAY(StartDay);
				vo.setDDT_ENDDAY(EndDay);
				vo.setDDT_INCENTER(dealFormatVo.getINCENTER());
				vo.setDDT_OUTCENTER(dealFormatVo.getOUTCENTER());
				vo.setCMN_MAK_PROG("DealFormat_Add");
				vo.setCMN_MAK_ID(USERID);
				vo.setCMN_UPD_PROG("DealFormat_Add");
				vo.setCMN_UPD_ID(USERID);
				if(formatDao.insertDocTRAN(vo) <= 0)
					throw new Exception();
				
				for (DealModelVo modelVo : dealFormatVo.getModelList()) {
					modelVo.setDDM_DOCNO(DDT_DOCNO);
					modelVo.setCMN_MAK_PROG("DealFormat_Add");
					modelVo.setCMN_MAK_ID(USERID);
					modelVo.setCMN_UPD_PROG("DealFormat_Add");
					modelVo.setCMN_UPD_ID(USERID);
					if(formatDao.insertDocTRANModel(modelVo) <= 0)
						throw new Exception();
				}
			}
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -4;
		}
	}
	
	public int updateDocTRANCfm(String DOCNO, String INSTS, String OUTSTS, String CMNT, String USERID) {
		try {
			DealFormatVo vo = new DealFormatVo();
			vo.setDDT_DOCNO(DOCNO);
			vo.setDDT_INSTS(INSTS);
			vo.setDDT_OUTSTS(OUTSTS);
			vo.setDDT_CMNT(CMNT);
			vo.setCMN_UPD_PROG("DealFormat_Cfm");
			vo.setCMN_UPD_ID(USERID);
			return formatDao.updateDocTRANCfm(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public String createExcel(DealFormatVo vo, HttpServletRequest request) {		
		List<DealModelVo> ModelList = formatDao.getFormatModelList(vo.getDDT_DOCNO());
		if(ModelList == null || ModelList.size() == 0)
			return "-3";
		
		try {
			List<CellRangeAddress> passCell = new ArrayList<CellRangeAddress>();
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow row = null;
			XSSFCell cell = null;
			int rowNo = 0;
			//TITLE 글꼴설정
			XSSFFont Titlefont = workbook.createFont();
			Titlefont.setFontName("맑은 고딕"); //폰트명
			Titlefont.setFontHeightInPoints((short)20); //폰트크기
			Titlefont.setBold(true); //볼드 
			
			sheet.setDefaultColumnWidth(2);
			sheet.setDefaultRowHeight((short)355);
			sheet.setMargin(XSSFSheet.TopMargin, 0.6);
			sheet.setMargin(XSSFSheet.BottomMargin, 0.6);
			sheet.setMargin(XSSFSheet.LeftMargin, 0.2);
			sheet.setMargin(XSSFSheet.RightMargin, 0.2);
			sheet.setMargin(XSSFSheet.HeaderMargin, 0.3);
			sheet.setMargin(XSSFSheet.FooterMargin, 0.3);
			
			CellStyle cellStyle_Title = workbook.createCellStyle();
			cellStyle_Title.setFont(Titlefont);
			cellStyle_Title.setAlignment(HorizontalAlignment.CENTER);
			cellStyle_Title.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Title.setBorderTop(BorderStyle.THIN);
			cellStyle_Title.setBorderLeft(BorderStyle.THIN);
			cellStyle_Title.setBorderRight(BorderStyle.THIN);
			CellRangeAddress MergeRow1 = new CellRangeAddress(0, 1, 0, 27);
			sheet.addMergedRegion(MergeRow1);
			row = sheet.createRow(rowNo);
			row.setHeight((short)396);
			cell = row.createCell((short)0);
			cell.setCellStyle(cellStyle_Title);
			cell.setCellValue("거 래 내 역 서");
			row = sheet.createRow(1);
			row.setHeight((short)396);
			rowNo = 2;
			
			XSSFFont Headerfont = workbook.createFont();
			Headerfont.setFontName("맑은 고딕"); //폰트명
			Headerfont.setFontHeightInPoints((short)10); //폰트크기
			Headerfont.setBold(true); //볼드 
			CellStyle cellStyle_Header = workbook.createCellStyle(); 
			cellStyle_Header.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Header.setFont(Headerfont);
			cellStyle_Header.setAlignment(HorizontalAlignment.LEFT);
			
			CellRangeAddress MergeRow3 = new CellRangeAddress(rowNo, rowNo, 0, 18);
			sheet.addMergedRegion(MergeRow3);
			passCell.add(MergeRow3);
			
			row = sheet.createRow(rowNo++); 
			row.setHeight((short)481);
			//row.setHeightInPoints((float)Units.pixelToPoints(40));
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("거래내역서 번호  :  " + vo.getDDT_DOCNO());
			
			XSSFFont fontSIGN = workbook.createFont();
			fontSIGN.setFontName("맑은 고딕"); //폰트명
			fontSIGN.setFontHeightInPoints((short)10); //폰트크기
			fontSIGN.setBold(true); //볼드 
			CellStyle cellStyle_SIGN = workbook.createCellStyle(); 
			cellStyle_SIGN.setFont(fontSIGN);
			cellStyle_SIGN.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_SIGN.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_SIGN.setWrapText(true);
			cellStyle_SIGN.setBorderTop(BorderStyle.THIN);
			cellStyle_SIGN.setBorderBottom(BorderStyle.THIN);
			cellStyle_SIGN.setBorderLeft(BorderStyle.THIN);
			cellStyle_SIGN.setBorderRight(BorderStyle.THIN);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 2, 19, 19));
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 20, 23));
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 24, 27));
			sheet.addMergedRegion(new CellRangeAddress(3, 4, 20, 23));
			sheet.addMergedRegion(new CellRangeAddress(3, 4, 24, 27));
			cell = row.createCell((short) 19);
			cell.setCellStyle(cellStyle_SIGN);
			cell.setCellValue("확\r\n\r\n인");
			cell = row.createCell((short) 20);
			cell.setCellStyle(cellStyle_SIGN);
			cell.setCellValue("출고본부");
			cell = row.createCell((short) 24);
			cell.setCellStyle(cellStyle_SIGN);
			cell.setCellValue("입고본부");
			
			CellRangeAddress MergeRow4 = new CellRangeAddress(rowNo, rowNo, 0, 18);
			sheet.addMergedRegion(MergeRow4);
			passCell.add(MergeRow4);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
			Date TEMP_START = format.parse(vo.getDDT_STARTDAY());
			String STARTDAY = format2.format(TEMP_START);
			Date TEMP_END = format.parse(vo.getDDT_ENDDAY());
			String ENDDAY = format2.format(TEMP_END);
			
			row = sheet.createRow(rowNo++);
			row.setHeight((short)481);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("거래내역 기간     :  " + STARTDAY + " ~ " + ENDDAY);
			
			int TotalPage = 0;
			if(ModelList.size() > 25)
			{
				int tempPage = ModelList.size() - 25;
				TotalPage = (int)Math.ceil(tempPage*1.0/40);
				TotalPage = TotalPage + 1;
			}
			else {
				TotalPage = 1;
			}
			
			CellRangeAddress MergeRow5 = new CellRangeAddress(rowNo, rowNo, 0, 18);
			sheet.addMergedRegion(MergeRow5);
			passCell.add(MergeRow5);
			
			row = sheet.createRow(rowNo++);
			row.setHeight((short)481);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("페이지              :  1page / " + TotalPage + "page");
			
			
			//--------------이미지 삽입---------------
			String uploadPath = fileService.filePath(request, "SIGN");
			File INFile = null;
			File OUTFile = null;		
			if(vo.getINSIGN().length() > 0) INFile = new File(uploadPath, vo.getINSIGN());
			if(vo.getOUTSIGN().length() > 0) OUTFile = new File(uploadPath, vo.getOUTSIGN());
			if(INFile != null && INFile.exists()) {
				ExcelFileinput(INFile, workbook, sheet, 24, 28, 3, 5);
			}
			if(OUTFile != null && OUTFile.exists()) {
				ExcelFileinput(OUTFile, workbook, sheet, 20, 24, 3, 5);
			}
			
			CellRangeAddress MergeRow6 = new CellRangeAddress(rowNo, rowNo, 0, 27);
			sheet.addMergedRegion(MergeRow6);
			row = sheet.createRow(rowNo++);
			
			XSSFFont bodyfont10 = workbook.createFont();
			bodyfont10.setFontName("맑은 고딕"); //폰트명
			bodyfont10.setFontHeightInPoints((short)10); //폰트크기
			bodyfont10.setBold(false); //볼드 
			CellStyle cellStyle_body10 = workbook.createCellStyle(); 
			cellStyle_body10.setFont(bodyfont10);
			cellStyle_body10.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_body10.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_body10.setBorderTop(BorderStyle.THIN);
			cellStyle_body10.setBorderBottom(BorderStyle.THIN);
			cellStyle_body10.setBorderLeft(BorderStyle.THIN);
			cellStyle_body10.setBorderRight(BorderStyle.THIN);
			
			XSSFFont bodyfont11 = workbook.createFont();
			bodyfont11.setFontName("맑은 고딕"); //폰트명
			bodyfont11.setFontHeightInPoints((short)11); //폰트크기
			bodyfont11.setBold(false); //볼드 
			CellStyle cellStyle_body11 = workbook.createCellStyle(); 
			cellStyle_body11.setFont(bodyfont11);
			cellStyle_body11.setAlignment(HorizontalAlignment.CENTER);
			cellStyle_body11.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_body11.setBorderTop(BorderStyle.THIN);
			cellStyle_body11.setBorderBottom(BorderStyle.THIN);
			cellStyle_body11.setBorderLeft(BorderStyle.THIN);
			cellStyle_body11.setBorderRight(BorderStyle.THIN);
			cellStyle_body11.setWrapText(true);
			
			XSSFFont bodyfontBold = workbook.createFont();
			bodyfontBold.setFontName("맑은 고딕"); //폰트명
			bodyfontBold.setFontHeightInPoints((short)11); //폰트크기
			bodyfontBold.setBold(true); //볼드 
			CellStyle cellStyle_bodyBold = workbook.createCellStyle(); 
			cellStyle_bodyBold.setFont(bodyfontBold);
			cellStyle_bodyBold.setAlignment(HorizontalAlignment.CENTER);
			cellStyle_bodyBold.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_bodyBold.setBorderTop(BorderStyle.THIN);
			cellStyle_bodyBold.setBorderBottom(BorderStyle.THIN);
			cellStyle_bodyBold.setBorderLeft(BorderStyle.THIN);
			cellStyle_bodyBold.setBorderRight(BorderStyle.THIN);
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo + 4, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo + 4, 14, 14));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1, 3));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 4, 8));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 9, 10));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 13));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 22));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 23, 24));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 25, 27));
			
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("출\r\n고\r\n본\r\n부");

			cell = row.createCell((short) 1);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("승인일자");
			
			cell = row.createCell((short) 4);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getDDT_OUTSTSDATE());
			
			cell = row.createCell((short) 9);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("승인자");
			
			cell = row.createCell((short) 11);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getOUTSTSUSERNM());
			
			cell = row.createCell((short) 14);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("입\r\n고\r\n본\r\n부");
			
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("승인일자");
			
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getDDT_INSTSDATE());
			
			cell = row.createCell((short) 23);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("승인자");
			
			cell = row.createCell((short) 25);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getINSTSUSERNM());
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1, 3));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 4, 13));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 27));
			
			row = sheet.createRow(rowNo++);
			
			cell = row.createCell((short) 1);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("등록번호");
			
			cell = row.createCell((short) 4);
			cell.setCellStyle(cellStyle_bodyBold);
			cell.setCellValue(vo.getOUTBRN());
			
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("등록번호");
			
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_bodyBold);
			cell.setCellValue(vo.getINBRN());
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1, 3));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 4, 8));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 9, 10));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 13));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 22));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 23, 24));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 25, 27));
			
			row = sheet.createRow(rowNo++);
			
			cell = row.createCell((short) 1);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("상 호");
			
			cell = row.createCell((short) 4);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getOUTCONAME());
			
			cell = row.createCell((short) 9);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("성명");
			
			cell = row.createCell((short) 11);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getOUTCEO());
			
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("상 호");
			
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getINCONAME());
			
			cell = row.createCell((short) 23);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("성명");
			
			cell = row.createCell((short) 25);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getINCEO());
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1, 3));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 4, 13));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 27));
			
			row = sheet.createRow(rowNo++);
			
			cell = row.createCell((short) 1);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("주 소");
			
			cell = row.createCell((short) 4);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getOUTADDR());
			
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("주 소");
			
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getINADDR());
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 1, 3));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 4, 8));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 9, 10));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 13));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 22));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 23, 24));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 25, 27));
			
			row = sheet.createRow(rowNo++);

			cell = row.createCell((short) 1);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("업 태");
			
			cell = row.createCell((short) 4);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getOUTCOTYPE());
			
			cell = row.createCell((short) 9);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("종목");
			
			cell = row.createCell((short) 11);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getOUTJM());
			
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("업 태");
			
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getINCOTYPE());
			
			cell = row.createCell((short) 23);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("종목");
			
			cell = row.createCell((short) 25);
			cell.setCellStyle(cellStyle_body10);
			cell.setCellValue(vo.getINJM());
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
			
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("거 래 내 역");
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 6));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 7, 10));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 21));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 22, 23));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 24, 25));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 26, 27));
			
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("월");
			
			cell = row.createCell((short) 1);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("일");
			
			cell = row.createCell((short) 2);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("접수번호");
			
			cell = row.createCell((short) 7);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("상품코드");
			
			cell = row.createCell((short) 11);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("대 중 소 분류");
			
			cell = row.createCell((short) 22);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("단위");
			
			cell = row.createCell((short) 24);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("수량");
			
			cell = row.createCell((short) 26);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("비고");		
			
			int size = ModelList.size();
			if(size < 25) {
				size = 25;
			}
			else {
				int tempVal = (size - 25) % 40;
				if(tempVal != 0) {
					size += (40 - tempVal);
				}
			}
			
			if(size == 25) {
				sheet.addMergedRegion(new CellRangeAddress(38, 38, 0, 27));
				row = sheet.createRow(38);
				cell = row.createCell((short) 0);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue("전달 및 특이사항");
				
				CellStyle cellStyle_Cmnt = workbook.createCellStyle(); 
				cellStyle_Cmnt.setFont(bodyfont11);
				cellStyle_Cmnt.setAlignment(HorizontalAlignment.LEFT);
				cellStyle_Cmnt.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle_Cmnt.setBorderTop(BorderStyle.THIN);
				cellStyle_Cmnt.setBorderBottom(BorderStyle.THIN);
				cellStyle_Cmnt.setBorderLeft(BorderStyle.THIN);
				cellStyle_Cmnt.setBorderRight(BorderStyle.THIN);
				cellStyle_Cmnt.setWrapText(true);
				
				sheet.addMergedRegion(new CellRangeAddress(39, 42, 0, 27));
				row = sheet.createRow(39);
				cell = row.createCell((short) 0);
				cell.setCellStyle(cellStyle_Cmnt);
				cell.setCellValue(vo.getDDT_CMNT());
			}
			
			int index = 0;
			for (int i = 0; i < size; ++i) {
				if(rowNo == 38) {
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("전달 및 특이사항");
					size++;
					continue;
				}
				else if(rowNo == 39) {
					CellStyle cellStyle_Cmnt = workbook.createCellStyle(); 
					cellStyle_Cmnt.setFont(bodyfont11);
					cellStyle_Cmnt.setAlignment(HorizontalAlignment.LEFT);
					cellStyle_Cmnt.setVerticalAlignment(VerticalAlignment.CENTER);
					cellStyle_Cmnt.setBorderTop(BorderStyle.THIN);
					cellStyle_Cmnt.setBorderBottom(BorderStyle.THIN);
					cellStyle_Cmnt.setBorderLeft(BorderStyle.THIN);
					cellStyle_Cmnt.setBorderRight(BorderStyle.THIN);
					cellStyle_Cmnt.setWrapText(true);
					
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo + 3, 0, 27));
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_Cmnt);
					cell.setCellValue(vo.getDDT_CMNT());
					size++;
					continue;
				}
				else if(rowNo >= 40 && rowNo <= 42) {
					rowNo++;
					size++;
					continue;
				}
				
				if(rowNo >= 43 && ((rowNo - 43) % 44) == 0) {
					int page = ((rowNo - 43) / 44) + 2;
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					row.setHeight((short)481);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_Header);
					cell.setCellValue("거래내역서 번호  :  " + vo.getDDT_DOCNO());

					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					row.setHeight((short)481);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_Header);
					cell.setCellValue("페이지              :  " + page + "page / " + TotalPage + "page");
					
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("거 래 내 역");
					
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 6));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 7, 10));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 21));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 22, 23));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 24, 25));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 26, 27));
					
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("월");
					
					cell = row.createCell((short) 1);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("일");
					
					cell = row.createCell((short) 2);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("접수번호");
					
					cell = row.createCell((short) 7);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("상품코드");
					
					cell = row.createCell((short) 11);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("대 중 소 분류");
					
					cell = row.createCell((short) 22);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("단위");
					
					cell = row.createCell((short) 24);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("수량");
					
					cell = row.createCell((short) 26);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("비고");
					size++;
					continue;
				}
				
				DealModelVo modelVo = index <  ModelList.size() ? ModelList.get(index) : null;
				index++;
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 6));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 7, 10));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 21));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 22, 23));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 24, 25));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 26, 27));

				row = sheet.createRow(rowNo++);
				cell = row.createCell((short) 0);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getMMDATE() : "");

				cell = row.createCell((short) 1);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getDDDATE() : "");

				cell = row.createCell((short) 2);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getDDM_SUBDOCNO() : "");

				cell = row.createCell((short) 7);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getDDM_MODEL() : "");

				cell = row.createCell((short) 11);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getMMC_LGROUPNAME() + " " + modelVo.getMMC_MGROUP() + " " + modelVo.getMMC_SGROUP() : "");

				cell = row.createCell((short) 22);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getMMC_UNIT() : "");

				cell = row.createCell((short) 24);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? String.valueOf(modelVo.getDDM_QTY()) : "");
			}
			
			setBordersToMergedCells(sheet, passCell, MergeRow1, MergeRow6);
			
			uploadPath = fileService.filePath(request, "EXCEL");
			
			File file = new File(uploadPath, vo.getDDT_DOCNO() + ".xlsx");
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			workbook.write(fos);

			if (workbook != null)	workbook.close();
			if (fos != null) fos.close();
			return "1";
		}
		catch (Exception e) {
			return "-4";
		}
	}
	
	private void ExcelFileinput(File INFile, XSSFWorkbook workbook, XSSFSheet sheet, int col1, int col2, int row1, int row2) throws Exception{
		InputStream inputStream = new FileInputStream(INFile);
		int picIdx = workbook.addPicture(inputStream, XSSFWorkbook.PICTURE_TYPE_JPEG);
		inputStream.close();
		
		XSSFCreationHelper helper = workbook.getCreationHelper();
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(col1);
		anchor.setCol2(col2);
		anchor.setRow1(row1);
		anchor.setRow2(row2);
		drawing.createPicture(anchor, picIdx);
	}
	
	private void setBordersToMergedCells(XSSFSheet sheet, List<CellRangeAddress> passList, CellRangeAddress Headercell, CellRangeAddress MergeRow6) {
		int numMerged = sheet.getNumMergedRegions();

		for(int i= 0; i<numMerged;i++){
			CellRangeAddress mergedRegions = sheet.getMergedRegion(i);
			if(passList.contains(mergedRegions))
			{
				RegionUtil.setBorderTop(BorderStyle.NONE, mergedRegions, sheet);
				RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegions, sheet);
				RegionUtil.setBorderRight(BorderStyle.NONE, mergedRegions, sheet);
				RegionUtil.setBorderBottom(BorderStyle.NONE, mergedRegions, sheet);
				continue;
			}
			if(Headercell.equals(mergedRegions)) {
				RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegions, sheet);
				RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegions, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegions, sheet);
				RegionUtil.setBorderBottom(BorderStyle.NONE, mergedRegions, sheet);
				continue;
			}
			if(MergeRow6.equals(mergedRegions)) {
				RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegions, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegions, sheet);
				continue;
			}
			RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegions, sheet);
			RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegions, sheet);
			RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegions, sheet);
			RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegions, sheet);
		}
	}
	
	public List<JegoFormatVo> getJegoFormatList(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String STSTYPE, int PAGE, int PSIZE){
		EndDay = EndDay.length() > 0 ? EndDay + " 23:59:59" : EndDay;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("STSTYPE", STSTYPE);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return formatDao.getJegoFormatList(map);
	}
	
	public int insertJegoFormat(String CENTER, String JEGOCHK, String USERID, int YEAR, int MONTH) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(YEAR, MONTH-1, 1); //월은 -1해줘야 해당월로 인식
        int LastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(YEAR, MONTH-1, LastDay); //월은 -1해줘야 해당월로 인식
        String DDJ_INFODATE = dateFormat.format(cal.getTime());
        
		String[] TEMP_LIST = JEGOCHK.split(",");
		List<JegoModelVo> modelVoList = new ArrayList<JegoModelVo>();
		for(int i = 0; i < TEMP_LIST.length;++i) {
			if(TEMP_LIST[i].length() == 0) {
				return -1;
			}
			String[] TEMP_VAL = TEMP_LIST[i].split("\\^");
			if(TEMP_VAL.length != 2) {
				return -1;
			}
			String MODEL = TEMP_VAL[0];
			String S_JEGO = TEMP_VAL[1];
			if(MODEL.length() != 11) {
				return -2;
			}
			int JEGO = 0;
			try {
				JEGO = Integer.parseInt(S_JEGO);
			}
			catch (Exception e) {
				return -3;
			}
			JegoModelVo modelVo = new JegoModelVo();
			modelVo.setDDM_MODEL(MODEL);
			modelVo.setDDM_JEGO(JEGO);
			modelVoList.add(modelVo);
		}
		if(modelVoList.size() == 0)
			return -4;
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		try {
			transaction.start();
			JegoFormatVo SeqVo = formatDao.getLastJegoFormatDocNo();
			int SEQ = SeqVo.getDDJ_SEQ() + 1;
			String NOWDATE = SeqVo.getCMN_MAK_DATE();
			String BASE = NOWDATE + "06";
			String DDJ_DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 3, "0");

			JegoFormatVo vo = new JegoFormatVo();
			vo.setDDJ_DOCNO(DDJ_DOCNO);
			vo.setDDJ_SEQ(SEQ);
			vo.setDDJ_INFODATE(DDJ_INFODATE);
			vo.setDDJ_CENTER(CENTER);
			vo.setCMN_MAK_PROG("JegoFormat_Add");
			vo.setCMN_MAK_ID(USERID);
			vo.setCMN_UPD_PROG("JegoFormat_Add");
			vo.setCMN_UPD_ID(USERID);
			if(formatDao.insertJegoDocTRAN(vo) <= 0)
				throw new Exception();

			for (JegoModelVo modelVo : modelVoList) {
				modelVo.setDDM_DOCNO(DDJ_DOCNO);
				modelVo.setCMN_MAK_PROG("JegoFormat_Add");
				modelVo.setCMN_MAK_ID(USERID);
				modelVo.setCMN_UPD_PROG("JegoFormat_Add");
				modelVo.setCMN_UPD_ID(USERID);
				if(formatDao.insertJegoDocTRANModel(modelVo) <= 0)
					throw new Exception();
			}
			transaction.commit();
			return 1;
		}
		catch (Exception e) {
			transaction.end();
			return -5;
		}
	}
	
	public JegoFormatVo getJegoFormatInfo(String DOCNO){
		return formatDao.getJegoFormatInfo(DOCNO);
	}
	
	public int updateJegoDocTRANCfm(String DOCNO, String STS, String CMNT, String USERID) {
		try {
			JegoFormatVo vo = new JegoFormatVo();
			vo.setDDJ_DOCNO(DOCNO);
			vo.setDDJ_STS(STS);
			vo.setDDJ_CMNT(CMNT);
			vo.setCMN_UPD_PROG("JegoFormat_Cfm");
			vo.setCMN_UPD_ID(USERID);
			return formatDao.updateJegoDocTRANCfm(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public String createJegoExcel(JegoFormatVo vo, HttpServletRequest request) {		
		List<JegoModelVo> ModelList = formatDao.getJegoFormatModelList(vo.getDDJ_DOCNO());
		if(ModelList == null || ModelList.size() == 0)
			return "-3";
		
		try {
			List<CellRangeAddress> passCell = new ArrayList<CellRangeAddress>();
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow row = null;
			XSSFCell cell = null;
			int rowNo = 0;
			
			sheet.setDefaultColumnWidth(2);
			sheet.setDefaultRowHeight((short)355);
			sheet.setMargin(XSSFSheet.TopMargin, 0.6);
			sheet.setMargin(XSSFSheet.BottomMargin, 0.6);
			sheet.setMargin(XSSFSheet.LeftMargin, 0.2);
			sheet.setMargin(XSSFSheet.RightMargin, 0.2);
			sheet.setMargin(XSSFSheet.HeaderMargin, 0.3);
			sheet.setMargin(XSSFSheet.FooterMargin, 0.3);
			
			XSSFFont Titlefont = workbook.createFont();
			Titlefont.setFontName("맑은 고딕"); //폰트명
			Titlefont.setFontHeightInPoints((short)20); //폰트크기
			Titlefont.setBold(true); //볼드 
			CellStyle cellStyle_Title = workbook.createCellStyle();
			cellStyle_Title.setFont(Titlefont);
			cellStyle_Title.setAlignment(HorizontalAlignment.CENTER);
			cellStyle_Title.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Title.setBorderTop(BorderStyle.THIN);
			cellStyle_Title.setBorderLeft(BorderStyle.THIN);
			cellStyle_Title.setBorderRight(BorderStyle.THIN);
			
			XSSFFont Headerfont = workbook.createFont();
			Headerfont.setFontName("맑은 고딕"); //폰트명
			Headerfont.setFontHeightInPoints((short)10); //폰트크기
			Headerfont.setBold(true); //볼드 
			CellStyle cellStyle_Header = workbook.createCellStyle(); 
			cellStyle_Header.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_Header.setFont(Headerfont);
			cellStyle_Header.setAlignment(HorizontalAlignment.LEFT);
			
			XSSFFont bodyfont10 = workbook.createFont();
			bodyfont10.setFontName("맑은 고딕"); //폰트명
			bodyfont10.setFontHeightInPoints((short)10); //폰트크기
			bodyfont10.setBold(false); //볼드 
			CellStyle cellStyle_body10 = workbook.createCellStyle(); 
			cellStyle_body10.setFont(bodyfont10);
			cellStyle_body10.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_body10.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_body10.setBorderTop(BorderStyle.THIN);
			cellStyle_body10.setBorderBottom(BorderStyle.THIN);
			cellStyle_body10.setBorderLeft(BorderStyle.THIN);
			cellStyle_body10.setBorderRight(BorderStyle.THIN);
			
			XSSFFont bodyfont11 = workbook.createFont();
			bodyfont11.setFontName("맑은 고딕"); //폰트명
			bodyfont11.setFontHeightInPoints((short)11); //폰트크기
			bodyfont11.setBold(false); //볼드 
			CellStyle cellStyle_body11 = workbook.createCellStyle(); 
			cellStyle_body11.setFont(bodyfont11);
			cellStyle_body11.setAlignment(HorizontalAlignment.CENTER);
			cellStyle_body11.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_body11.setBorderTop(BorderStyle.THIN);
			cellStyle_body11.setBorderBottom(BorderStyle.THIN);
			cellStyle_body11.setBorderLeft(BorderStyle.THIN);
			cellStyle_body11.setBorderRight(BorderStyle.THIN);
			cellStyle_body11.setWrapText(true);
			
			XSSFFont bodyfontBold = workbook.createFont();
			bodyfontBold.setFontName("맑은 고딕"); //폰트명
			bodyfontBold.setFontHeightInPoints((short)11); //폰트크기
			bodyfontBold.setBold(true); //볼드 
			CellStyle cellStyle_bodyBold = workbook.createCellStyle(); 
			cellStyle_bodyBold.setFont(bodyfontBold);
			cellStyle_bodyBold.setAlignment(HorizontalAlignment.CENTER);
			cellStyle_bodyBold.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_bodyBold.setBorderTop(BorderStyle.THIN);
			cellStyle_bodyBold.setBorderBottom(BorderStyle.THIN);
			cellStyle_bodyBold.setBorderLeft(BorderStyle.THIN);
			cellStyle_bodyBold.setBorderRight(BorderStyle.THIN);
			
			XSSFFont fontSIGN = workbook.createFont();
			fontSIGN.setFontName("맑은 고딕"); //폰트명
			fontSIGN.setFontHeightInPoints((short)10); //폰트크기
			fontSIGN.setBold(true); //볼드 
			CellStyle cellStyle_SIGN = workbook.createCellStyle(); 
			cellStyle_SIGN.setFont(fontSIGN);
			cellStyle_SIGN.setAlignment(HorizontalAlignment.CENTER); 
			cellStyle_SIGN.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle_SIGN.setWrapText(true);
			cellStyle_SIGN.setBorderTop(BorderStyle.THIN);
			cellStyle_SIGN.setBorderBottom(BorderStyle.THIN);
			cellStyle_SIGN.setBorderLeft(BorderStyle.THIN);
			cellStyle_SIGN.setBorderRight(BorderStyle.THIN);
			
			CellRangeAddress MergeRow1 = new CellRangeAddress(rowNo, rowNo + 1, 0, 23);
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 24, 27));
			sheet.addMergedRegion(MergeRow1);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)396);
			cell = row.createCell((short)0);
			cell.setCellStyle(cellStyle_Title);
			cell.setCellValue("재 고 명 세 서");
			cell = row.createCell((short)24);
			cell.setCellStyle(cellStyle_SIGN);
			cell.setCellValue("작성자");
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo + 1, 24, 27));
			//--------------이미지 삽입---------------
			String uploadPath = fileService.filePath(request, "SIGN");
			File SIGNFile = null;	
			if(vo.getUSERSIGN() != null && vo.getUSERSIGN().length() > 0) SIGNFile = new File(uploadPath, vo.getUSERSIGN());
			if(SIGNFile != null && SIGNFile.exists()) {
				ExcelFileinput(SIGNFile, workbook, sheet, 24, 28, 1, 3);
			}
			row = sheet.createRow(rowNo++);
			row.setHeight((short)481);
			
			CellRangeAddress MergeRow3 = new CellRangeAddress(rowNo, rowNo, 0, 23);
			sheet.addMergedRegion(MergeRow3);
			passCell.add(MergeRow3);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)481);
			cell = row.createCell((short)0);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("아래의 재고 현황의 품목과 수량이 틀림없음을 확인합니다.");
			
			CellRangeAddress MergeRow4 = new CellRangeAddress(rowNo, rowNo, 0, 27);
			sheet.addMergedRegion(MergeRow4);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)396);
			
			CellRangeAddress MergeRow5 = new CellRangeAddress(rowNo, rowNo, 0, 13);
			sheet.addMergedRegion(MergeRow5);
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo + 4, 14, 14));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 27));
			passCell.add(MergeRow5);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)396);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("재고명세서 번호  :  " + vo.getDDJ_DOCNO());
			cell = row.createCell((short) 14);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("제\r\n출\r\n본\r\n부");
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("본 부");
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue(vo.getCENTERNAME());
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
			Date TEMP_INFODATE = format.parse(vo.getDDJ_INFODATE());
			String INFODATE = format2.format(TEMP_INFODATE);
			CellRangeAddress MergeRow6 = new CellRangeAddress(rowNo, rowNo, 0, 13);
			sheet.addMergedRegion(MergeRow6);
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 27));
			passCell.add(MergeRow6);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)396);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("기준일              :  " + INFODATE);
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("등록번호");
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_bodyBold);
			cell.setCellValue(vo.getBRN());
			
			int TotalPage = 0;
			if(ModelList.size() > 27)
			{
				int tempPage = ModelList.size() - 27;
				TotalPage = (int)Math.ceil(tempPage*1.0/40);
				TotalPage = TotalPage + 1;
			}
			else {
				TotalPage = 1;
			}
			CellRangeAddress MergeRow7 = new CellRangeAddress(rowNo, rowNo, 0, 13);
			sheet.addMergedRegion(MergeRow7);
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 22));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 23, 24));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 25, 27));
			passCell.add(MergeRow7);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)396);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_Header);
			cell.setCellValue("페이지              :  1page / " + TotalPage + "page");
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("상 호");
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue(vo.getCONAME());
			cell = row.createCell((short) 23);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("성명");
			cell = row.createCell((short) 25);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue(vo.getCEO());
			
			CellRangeAddress MergeRow8 = new CellRangeAddress(rowNo, rowNo, 0, 13);
			sheet.addMergedRegion(MergeRow8);
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 27));
			passCell.add(MergeRow8);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)396);
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("주 소");
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue(vo.getADDR());
			
			CellRangeAddress MergeRow9 = new CellRangeAddress(rowNo, rowNo, 0, 13);
			sheet.addMergedRegion(MergeRow9);
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 17));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 22));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 23, 24));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 25, 27));
			passCell.add(MergeRow9);
			row = sheet.createRow(rowNo++);
			row.setHeight((short)396);
			cell = row.createCell((short) 15);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("업 태");
			cell = row.createCell((short) 18);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue(vo.getCOTYPE());
			cell = row.createCell((short) 23);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("종목");
			cell = row.createCell((short) 25);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue(vo.getJM());
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
			
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("재고 현황");
			
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 1));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 6));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 7, 10));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 15));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 16, 19));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 20, 21));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 22, 23));
			sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 24, 27));
			
			row = sheet.createRow(rowNo++);
			cell = row.createCell((short) 0);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("순번");
			
			cell = row.createCell((short) 2);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("상품코드");
			
			cell = row.createCell((short) 7);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("대분류");
			
			cell = row.createCell((short) 11);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("중분류");
			
			cell = row.createCell((short) 16);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("소분류");
			
			cell = row.createCell((short) 20);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("단위");
			
			cell = row.createCell((short) 22);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("수량");
			
			cell = row.createCell((short) 24);
			cell.setCellStyle(cellStyle_body11);
			cell.setCellValue("비고");		
			
			int size = ModelList.size();
			if(size < 27) {
				size = 27;
			}
			else {
				int tempVal = (size - 27) % 40;
				if(tempVal != 0) {
					size += (40 - tempVal);
				}
			}
			
			if(size == 27) {
				sheet.addMergedRegion(new CellRangeAddress(38, 38, 0, 27));
				row = sheet.createRow(38);
				cell = row.createCell((short) 0);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue("전달 및 특이사항");
				
				CellStyle cellStyle_Cmnt = workbook.createCellStyle(); 
				cellStyle_Cmnt.setFont(bodyfont11);
				cellStyle_Cmnt.setAlignment(HorizontalAlignment.LEFT);
				cellStyle_Cmnt.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle_Cmnt.setBorderTop(BorderStyle.THIN);
				cellStyle_Cmnt.setBorderBottom(BorderStyle.THIN);
				cellStyle_Cmnt.setBorderLeft(BorderStyle.THIN);
				cellStyle_Cmnt.setBorderRight(BorderStyle.THIN);
				cellStyle_Cmnt.setWrapText(true);
				
				sheet.addMergedRegion(new CellRangeAddress(39, 42, 0, 27));
				row = sheet.createRow(39);
				cell = row.createCell((short) 0);
				cell.setCellStyle(cellStyle_Cmnt);
				cell.setCellValue(vo.getDDJ_CMNT());
			}
			int index = 0;
			for (int i = 0; i < size; ++i) {
				if(rowNo == 38) {
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("전달 및 특이사항");
					size++;
					continue;
				}
				else if(rowNo == 39) {
					CellStyle cellStyle_Cmnt = workbook.createCellStyle(); 
					cellStyle_Cmnt.setFont(bodyfont11);
					cellStyle_Cmnt.setAlignment(HorizontalAlignment.LEFT);
					cellStyle_Cmnt.setVerticalAlignment(VerticalAlignment.CENTER);
					cellStyle_Cmnt.setBorderTop(BorderStyle.THIN);
					cellStyle_Cmnt.setBorderBottom(BorderStyle.THIN);
					cellStyle_Cmnt.setBorderLeft(BorderStyle.THIN);
					cellStyle_Cmnt.setBorderRight(BorderStyle.THIN);
					cellStyle_Cmnt.setWrapText(true);
					
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo + 3, 0, 27));
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_Cmnt);
					cell.setCellValue(vo.getDDJ_CMNT());
					size++;
					continue;
				}
				else if(rowNo >= 40 && rowNo <= 42) {
					rowNo++;
					size++;
					continue;
				}
				
				if(rowNo >= 43 && ((rowNo - 43) % 44) == 0) {
					int page = ((rowNo - 43) / 44) + 2;
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					row.setHeight((short)481);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_Header);
					cell.setCellValue("재고명세서 번호  :  " + vo.getDDJ_DOCNO());

					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					row.setHeight((short)481);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_Header);
					cell.setCellValue("페이지              :  " + page + "page / " + TotalPage + "page");
					
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 27));
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("재고 현황");
					
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 1));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 6));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 7, 10));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 15));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 16, 19));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 20, 21));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 22, 23));
					sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 24, 27));
					
					row = sheet.createRow(rowNo++);
					cell = row.createCell((short) 0);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("순번");
					
					cell = row.createCell((short) 2);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("상품코드");
					
					cell = row.createCell((short) 7);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("대분류");
					
					cell = row.createCell((short) 11);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("중분류");
					
					cell = row.createCell((short) 16);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("소분류");
					
					cell = row.createCell((short) 20);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("단위");
					
					cell = row.createCell((short) 22);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("수량");
					
					cell = row.createCell((short) 24);
					cell.setCellStyle(cellStyle_body11);
					cell.setCellValue("비고");		
					size++;
					continue;
				}
				
				JegoModelVo modelVo = index <  ModelList.size() ? ModelList.get(index) : null;
				index++;
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 1));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 6));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 7, 10));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 11, 15));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 16, 19));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 20, 21));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 22, 23));
				sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 24, 27));

				row = sheet.createRow(rowNo++);
				cell = row.createCell((short) 0);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? String.valueOf(index) : "");

				cell = row.createCell((short) 2);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getDDM_MODEL() : "");

				cell = row.createCell((short) 7);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getMMC_LGROUPNAME() : "");

				cell = row.createCell((short) 11);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getMMC_MGROUP() : "");

				cell = row.createCell((short) 16);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getMMC_SGROUP() : "");

				cell = row.createCell((short) 20);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? modelVo.getMMC_UNIT() : "");

				cell = row.createCell((short) 22);
				cell.setCellStyle(cellStyle_body11);
				cell.setCellValue(modelVo != null ? String.valueOf(modelVo.getDDM_JEGO()) : "");
				
				cell = row.createCell((short) 24);
				cell.setCellStyle(cellStyle_body11);
			}
			
			setBordersToMergedCells(sheet, passCell, MergeRow1, MergeRow4);
			
			uploadPath = fileService.filePath(request, "EXCEL");
			
			File file = new File(uploadPath, vo.getDDJ_DOCNO() + ".xlsx");
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			workbook.write(fos);

			if (workbook != null)	workbook.close();
			if (fos != null) fos.close();
			return "1";
		}
		catch (Exception e) {
			return "-4";
		}
	}
}
