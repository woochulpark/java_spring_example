package com.vo;

import java.util.ArrayList;
import java.util.List;

public class DealFormatVo {
	private int rownum;
	private String DDT_DOCNO;
	private int DDT_SEQ;
	private String DDT_DATE;
	private String DDT_RESN;
	private String DDT_STARTDAY;
	private String DDT_ENDDAY;
	private String DDT_INCENTER;
	private String DDT_OUTCENTER;
	private String DDT_INSTS;
	private String DDT_INSTSDATE;
	private String DDT_INSTSUSER;
	private String DDT_OUTSTS;
	private String DDT_OUTSTSDATE;
	private String DDT_OUTSTSUSER;
	private String DDT_CMNT;
	private String INLOGCNM;
	private String INBONBUNM;
	private String INCENTERNM;
	private String OUTLOGCNM;
	private String OUTBONBUNM;
	private String OUTCENTERNM; 
	private int totalCnt;
	private String CMN_MAK_DATE;
	private String CMN_MAK_PROG;
	private String CMN_MAK_ID;
	private String CMN_UPD_PROG;
	private String CMN_UPD_ID;
	
	private String DOCNO;
	private String INDATE;
	private String OUTDATE;
	private String INCENTER;
	private String OUTCENTER;
	private String MMC_LGROUP;
	private String MMC_LGROUPNAME;
	private String MMC_MGROUP;
	private String MMC_SGROUP;
	private String MODEL;
	private int QTY;
	
	private String OUTBRN;
	private String OUTCONAME;
	private String OUTCEO;
	private String OUTADDR;
	private String OUTCOTYPE;
	private String OUTJM;
	private String INBRN;
	private String INCONAME;
	private String INCEO;
	private String INADDR;
	private String INCOTYPE;
	private String INJM;
	 
	private String OUTCENTERNAME;
	private String OUTSTSUSERNM;
	private String OUTSIGN;
	private String INCENTERNAME;
	private String INSTSUSERNM;
	private String INSIGN;
	  
	private List<DealModelVo> modelList;
	
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public String getDDT_DOCNO() {
		return DDT_DOCNO;
	}
	public void setDDT_DOCNO(String dDT_DOCNO) {
		DDT_DOCNO = dDT_DOCNO;
	}
	public int getDDT_SEQ() {
		return DDT_SEQ;
	}
	public void setDDT_SEQ(int dDT_SEQ) {
		DDT_SEQ = dDT_SEQ;
	}
	public String getDDT_DATE() {
		return DDT_DATE;
	}
	public void setDDT_DATE(String dDT_DATE) {
		DDT_DATE = dDT_DATE;
	}
	public String getDDT_RESN() {
		return DDT_RESN;
	}
	public void setDDT_RESN(String dDT_RESN) {
		DDT_RESN = dDT_RESN;
	}
	public String getDDT_STARTDAY() {
		return DDT_STARTDAY;
	}
	public void setDDT_STARTDAY(String dDT_STARTDAY) {
		DDT_STARTDAY = dDT_STARTDAY;
	}
	public String getDDT_ENDDAY() {
		return DDT_ENDDAY;
	}
	public void setDDT_ENDDAY(String dDT_ENDDAY) {
		DDT_ENDDAY = dDT_ENDDAY;
	}
	public String getDDT_INCENTER() {
		return DDT_INCENTER;
	}
	public void setDDT_INCENTER(String dDT_INCENTER) {
		DDT_INCENTER = dDT_INCENTER;
	}
	public String getDDT_OUTCENTER() {
		return DDT_OUTCENTER;
	}
	public void setDDT_OUTCENTER(String dDT_OUTCENTER) {
		DDT_OUTCENTER = dDT_OUTCENTER;
	}
	public String getDDT_INSTS() {
		return DDT_INSTS;
	}
	public void setDDT_INSTS(String dDT_INSTS) {
		DDT_INSTS = dDT_INSTS;
	}
	public String getDDT_INSTSDATE() {
		return DDT_INSTSDATE;
	}
	public void setDDT_INSTSDATE(String dDT_INSTSDATE) {
		DDT_INSTSDATE = dDT_INSTSDATE;
	}
	public String getDDT_INSTSUSER() {
		return DDT_INSTSUSER;
	}
	public void setDDT_INSTSUSER(String dDT_INSTSUSER) {
		DDT_INSTSUSER = dDT_INSTSUSER;
	}
	public String getDDT_OUTSTS() {
		return DDT_OUTSTS;
	}
	public void setDDT_OUTSTS(String dDT_OUTSTS) {
		DDT_OUTSTS = dDT_OUTSTS;
	}
	public String getDDT_OUTSTSDATE() {
		return DDT_OUTSTSDATE;
	}
	public void setDDT_OUTSTSDATE(String dDT_OUTSTSDATE) {
		DDT_OUTSTSDATE = dDT_OUTSTSDATE;
	}
	public String getDDT_OUTSTSUSER() {
		return DDT_OUTSTSUSER;
	}
	public void setDDT_OUTSTSUSER(String dDT_OUTSTSUSER) {
		DDT_OUTSTSUSER = dDT_OUTSTSUSER;
	}
	public String getDDT_CMNT() {
		return DDT_CMNT;
	}
	public void setDDT_CMNT(String dDT_CMNT) {
		DDT_CMNT = dDT_CMNT;
	}
	public String getINLOGCNM() {
		return INLOGCNM;
	}
	public void setINLOGCNM(String iNLOGCNM) {
		INLOGCNM = iNLOGCNM;
	}
	public String getINBONBUNM() {
		return INBONBUNM;
	}
	public void setINBONBUNM(String iNBONBUNM) {
		INBONBUNM = iNBONBUNM;
	}
	public String getINCENTERNM() {
		return INCENTERNM;
	}
	public void setINCENTERNM(String iNCENTERNM) {
		INCENTERNM = iNCENTERNM;
	}
	public String getOUTLOGCNM() {
		return OUTLOGCNM;
	}
	public void setOUTLOGCNM(String oUTLOGCNM) {
		OUTLOGCNM = oUTLOGCNM;
	}
	public String getOUTBONBUNM() {
		return OUTBONBUNM;
	}
	public void setOUTBONBUNM(String oUTBONBUNM) {
		OUTBONBUNM = oUTBONBUNM;
	}
	public String getOUTCENTERNM() {
		return OUTCENTERNM;
	}
	public void setOUTCENTERNM(String oUTCENTERNM) {
		OUTCENTERNM = oUTCENTERNM;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getCMN_MAK_DATE() {
		return CMN_MAK_DATE;
	}
	public void setCMN_MAK_DATE(String cMN_MAK_DATE) {
		CMN_MAK_DATE = cMN_MAK_DATE;
	}
	public String getCMN_MAK_PROG() {
		return CMN_MAK_PROG;
	}
	public void setCMN_MAK_PROG(String cMN_MAK_PROG) {
		CMN_MAK_PROG = cMN_MAK_PROG;
	}
	public String getCMN_MAK_ID() {
		return CMN_MAK_ID;
	}
	public void setCMN_MAK_ID(String cMN_MAK_ID) {
		CMN_MAK_ID = cMN_MAK_ID;
	}
	public String getCMN_UPD_PROG() {
		return CMN_UPD_PROG;
	}
	public void setCMN_UPD_PROG(String cMN_UPD_PROG) {
		CMN_UPD_PROG = cMN_UPD_PROG;
	}
	public String getCMN_UPD_ID() {
		return CMN_UPD_ID;
	}
	public void setCMN_UPD_ID(String cMN_UPD_ID) {
		CMN_UPD_ID = cMN_UPD_ID;
	}
	public String getDOCNO() {
		return DOCNO;
	}
	public void setDOCNO(String dOCNO) {
		DOCNO = dOCNO;
	}
	public String getINDATE() {
		return INDATE;
	}
	public void setINDATE(String iNDATE) {
		INDATE = iNDATE;
	}
	public String getOUTDATE() {
		return OUTDATE;
	}
	public void setOUTDATE(String oUTDATE) {
		OUTDATE = oUTDATE;
	}
	public String getINCENTER() {
		return INCENTER;
	}
	public void setINCENTER(String iNCENTER) {
		INCENTER = iNCENTER;
	}
	public String getOUTCENTER() {
		return OUTCENTER;
	}
	public void setOUTCENTER(String oUTCENTER) {
		OUTCENTER = oUTCENTER;
	}
	public String getMMC_LGROUP() {
		return MMC_LGROUP;
	}
	public void setMMC_LGROUP(String mMC_LGROUP) {
		MMC_LGROUP = mMC_LGROUP;
	}
	public String getMMC_LGROUPNAME() {
		return MMC_LGROUPNAME;
	}
	public void setMMC_LGROUPNAME(String mMC_LGROUPNAME) {
		MMC_LGROUPNAME = mMC_LGROUPNAME;
	}
	public String getMMC_MGROUP() {
		return MMC_MGROUP;
	}
	public void setMMC_MGROUP(String mMC_MGROUP) {
		MMC_MGROUP = mMC_MGROUP;
	}
	public String getMMC_SGROUP() {
		return MMC_SGROUP;
	}
	public void setMMC_SGROUP(String mMC_SGROUP) {
		MMC_SGROUP = mMC_SGROUP;
	}
	public String getMODEL() {
		return MODEL;
	}
	public void setMODEL(String mODEL) {
		MODEL = mODEL;
	}
	public int getQTY() {
		return QTY;
	}
	public void setQTY(int qTY) {
		QTY = qTY;
	}
	public List<DealModelVo> getModelList() {
		return modelList;
	}
	public void setModelList(DealModelVo vo) {
		if(this.modelList == null)
			this.modelList = new ArrayList<DealModelVo>();
		this.modelList.add(vo);
	}
	public String getOUTBRN() {
		return OUTBRN;
	}
	public void setOUTBRN(String oUTBRN) {
		OUTBRN = oUTBRN;
	}
	public String getOUTCONAME() {
		return OUTCONAME;
	}
	public void setOUTCONAME(String oUTCONAME) {
		OUTCONAME = oUTCONAME;
	}
	public String getOUTCEO() {
		return OUTCEO;
	}
	public void setOUTCEO(String oUTCEO) {
		OUTCEO = oUTCEO;
	}
	public String getOUTADDR() {
		return OUTADDR;
	}
	public void setOUTADDR(String oUTADDR) {
		OUTADDR = oUTADDR;
	}
	public String getOUTCOTYPE() {
		return OUTCOTYPE;
	}
	public void setOUTCOTYPE(String oUTCOTYPE) {
		OUTCOTYPE = oUTCOTYPE;
	}
	public String getOUTJM() {
		return OUTJM;
	}
	public void setOUTJM(String oUTJM) {
		OUTJM = oUTJM;
	}
	public String getINBRN() {
		return INBRN;
	}
	public void setINBRN(String iNBRN) {
		INBRN = iNBRN;
	}
	public String getINCONAME() {
		return INCONAME;
	}
	public void setINCONAME(String iNCONAME) {
		INCONAME = iNCONAME;
	}
	public String getINCEO() {
		return INCEO;
	}
	public void setINCEO(String iNCEO) {
		INCEO = iNCEO;
	}
	public String getINADDR() {
		return INADDR;
	}
	public void setINADDR(String iNADDR) {
		INADDR = iNADDR;
	}
	public String getINCOTYPE() {
		return INCOTYPE;
	}
	public void setINCOTYPE(String iNCOTYPE) {
		INCOTYPE = iNCOTYPE;
	}
	public String getINJM() {
		return INJM;
	}
	public void setINJM(String iNJM) {
		INJM = iNJM;
	}
	public void setModelList(List<DealModelVo> modelList) {
		this.modelList = modelList;
	}
	public String getOUTCENTERNAME() {
		return OUTCENTERNAME;
	}
	public void setOUTCENTERNAME(String oUTCENTERNAME) {
		OUTCENTERNAME = oUTCENTERNAME;
	}
	public String getOUTSTSUSERNM() {
		return OUTSTSUSERNM;
	}
	public void setOUTSTSUSERNM(String oUTSTSUSERNM) {
		OUTSTSUSERNM = oUTSTSUSERNM;
	}
	public String getINCENTERNAME() {
		return INCENTERNAME;
	}
	public void setINCENTERNAME(String iNCENTERNAME) {
		INCENTERNAME = iNCENTERNAME;
	}
	public String getINSTSUSERNM() {
		return INSTSUSERNM;
	}
	public void setINSTSUSERNM(String iNSTSUSERNM) {
		INSTSUSERNM = iNSTSUSERNM;
	}
	public String getOUTSIGN() {
		return OUTSIGN;
	}
	public void setOUTSIGN(String oUTSIGN) {
		OUTSIGN = oUTSIGN;
	}
	public String getINSIGN() {
		return INSIGN;
	}
	public void setINSIGN(String iNSIGN) {
		INSIGN = iNSIGN;
	}
}
