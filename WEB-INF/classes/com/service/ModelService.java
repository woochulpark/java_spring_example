package com.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.ModelDao;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vo.ModelVo;

@Service
public class ModelService {
	@Autowired
	private CommonService commonService;
	@Autowired
	private ModelDao modelDao;
	@Autowired
	private FileService fileService;
	
	public ModelVo getModelInfo(String MCODE){
		return modelDao.getModelInfo(MCODE);
	}
	
	public List<ModelVo> getModelList(String LOGC, String LCODE, String MCODE, String SCODE, String MNAME, String MCODENAME, String SCODENAME, String BISSTS, int PAGE, int PSIZE){
		return modelDao.getModelList(LOGC, LCODE, MCODE, SCODE, MNAME, MCODENAME, SCODENAME, BISSTS, PAGE, PSIZE);
	}
	
	public List<ModelVo> ModelIlyeog(String MCODE){
		return modelDao.ModelIlyeog(MCODE);
	}
	
	public List<ModelVo> MgroupList(String LOGC, String LCODE){
		return modelDao.MgroupList(LOGC, LCODE);
	}
	
	public List<ModelVo> SgroupList(String LOGC, String LCODE, String MCODE){
		return modelDao.SgroupList(LOGC, LCODE, MCODE);
	}
	
	public int insertModel(ModelVo vo){
		try {
			int SEQ = modelDao.getLastModelDocNo(vo.getMMC_LOGC(), vo.getMMC_LGROUP(), vo.getMMC_KINDS()) + 1;
			String BASE = vo.getMMC_LOGC() + vo.getMMC_KINDS() + vo.getMMC_LGROUP();
			String DOCNO = BASE + commonService.leftPad(String.valueOf(SEQ), 5, "0");
			System.out.println("MODEL SEQ = " + SEQ + ", BASE = " + BASE + " DOCNO  = " + DOCNO);
			vo.setMMC_CODE(DOCNO);
			vo.setMMC_SEQ(SEQ);
			int result = modelDao.insertModel(vo);
			if(result > 0) {
				ModelVo IlyeogVo = new ModelVo();
				IlyeogVo.setMML_CODE(vo.getMMC_CODE());
				IlyeogVo.setMML_USER(vo.getCMN_MAK_ID());
				IlyeogVo.setMML_CMNT("최초등록");
				modelDao.insertModelIlyeog(IlyeogVo);
			}
			return result;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int updateModel(ModelVo vo){
		try {
			ModelVo minfo = modelDao.getModelInfo(vo.getMMC_CODE());
			if(minfo == null || minfo.getMMC_CODE().length() == 0)
				return -1;
			
			String CMNT = "";
			if(!minfo.getMMC_MGROUP().equals(vo.getMMC_MGROUP()))
				CMNT = "중분류 수정 : " + minfo.getMMC_MGROUP() + " > " + vo.getMMC_MGROUP() + "\r\n";
			if(!minfo.getMMC_SGROUP().equals(vo.getMMC_SGROUP()))
				CMNT += "소분류 수정 : " + minfo.getMMC_SGROUP() + " > " + vo.getMMC_SGROUP() + "\r\n";
			if(!minfo.getMMC_UNIT().equals(vo.getMMC_UNIT()))
				CMNT += "단위 수정 : " + minfo.getMMC_UNIT() + " > " + vo.getMMC_UNIT() + "\r\n";
			if(!minfo.getMMC_StartDay().equals(vo.getMMC_StartDay()))
				CMNT += "시작일 수정 : " + minfo.getMMC_StartDay() + " > " + vo.getMMC_StartDay() + "\r\n";
			if(!minfo.getMMC_EndDay().equals(vo.getMMC_EndDay()))
				CMNT += "종료일 수정 : " + minfo.getMMC_EndDay() + " > " + vo.getMMC_EndDay() + "\r\n";
			if(!minfo.getMMC_BizSts().equals(vo.getMMC_BizSts()))
				CMNT += "운영여부 수정 : " + minfo.getMMC_BizSts() + " > " + vo.getMMC_BizSts() + "\r\n";
			if(!minfo.getMMC_CMNT().equals(vo.getMMC_CMNT()))
				CMNT += "비고 수정 : " + minfo.getMMC_CMNT() + " > " + vo.getMMC_CMNT() + "\r\n";
			if(!minfo.getMMC_ERPMODEL().equals(vo.getMMC_ERPMODEL()))
				CMNT += "ERP상품코드 수정 : " + minfo.getMMC_ERPMODEL() + " > " + vo.getMMC_ERPMODEL() + "\r\n";
			
			if(CMNT.length() > 0) {
				CMNT = CMNT.substring(0, CMNT.length() - 1);
				ModelVo IlyeogVo = new ModelVo();
				IlyeogVo.setMML_CODE(vo.getMMC_CODE());
				IlyeogVo.setMML_USER(vo.getCMN_UPD_ID());
				IlyeogVo.setMML_CMNT(CMNT);
				modelDao.insertModelIlyeog(IlyeogVo);
			}
			return modelDao.updateModel(vo);
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public int deleteModel(String MCODE){
		try {
			int result = modelDao.deleteModel(MCODE);
			if(result > 0)
				modelDao.deleteModelIlyeog(MCODE);
			
			return result;
		}
		catch (Exception e) {
			return -1;
		}
	}
	
	public void WriteQrCode(String MCODE, String LGROUP, String MGROUP, String SGROUP, int width, int height,  HttpServletRequest request)  throws Exception {
		QRCodeWriter codeWriter = new QRCodeWriter();
		BitMatrix bitMatrix  = codeWriter.encode(MCODE, BarcodeFormat.QR_CODE, width, height);
		BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		BufferedImage temp_img = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		Graphics g = temp_img.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 600, 400);
		g.drawImage(image, 400, 0, image.getWidth(), image.getHeight(), null);
		g.setColor(Color.black);
		g.setFont(new Font("나눔고딕",Font.BOLD, 50));
		g.drawString(MCODE, 50, 80);
		
		Font font = new Font("나눔고딕", Font.BOLD, 30);
		g.setFont(font);
		g.drawString("대분류 : " + LGROUP, 50, 180);
		g.drawString("중분류 : " + MGROUP, 50, 260);
		g.drawString("소분류 : " + SGROUP, 50, 340);
		String uploadPath = fileService.filePath(request, "LABEL");
        ImageIO.write( temp_img  , "jpg", new File(uploadPath, MCODE + ".jpg"));
	}
	
	public List<LinkedHashMap<String, Object>> MgroupAndroidList(String LOGC, String LCODE)  throws Exception{
		return modelDao.MgroupAndroidList(LOGC, LCODE);
	}
	
	public List<LinkedHashMap<String, Object>> SgroupAndroidList(String LOGC, String LCODE, String MCODE) throws Exception{
		return modelDao.SgroupAndroidList(LOGC, LCODE, MCODE);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidModelList(String LOGC, String LCODE, String MCODE, String SCODE, String MNAME) throws Exception{
		return modelDao.getAndroidModelList(LOGC, LCODE, MCODE, SCODE, MNAME);
	}
}
