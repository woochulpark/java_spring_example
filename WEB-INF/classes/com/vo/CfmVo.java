package com.vo;

public class CfmVo {
	private String DIC_DOCNO;
	private String DIC_USER;
	private String DIC_DATE;
	private String DIC_RESN;
	private String DIC_USERNAME;
	private String DIC_CMNT;
	private String CMN_MAK_PROG;
	private String CMN_MAK_ID;
	private String CMN_UPD_PROG;
	private String CMN_UPD_ID;
	
	private int rownum;
	private String INTYPE;
	private String DOCNO;
	private String INLOGC; 
	private String INBONBU;
	private String INCENTER; 
	private String INBONBU_NAME;
	private String INCENTER_NAME;
	private String OUT_BONBU;
	private String MODEL;
	private String MMC_LGROUP;
	private String MMC_LGROUPNAME;
	private String MMC_MGROUP;
	private String MMC_SGROUP;
	private String INDATE;
	private int INQTY;
	private String DBO_USERNAME;
	private String OUTDATE;
	private int OUTQTY;
	private String DBO_CMNT;
	private String CFMsts;
	private int totalCnt;
	
	
	private String StartDay;
	private String EndDay;
	private String LOGC;
	private String BONBU;
	private String CENTER;
	private String INSTS;
	private String CFMSTS;
	private String MNAME;
	private int PAGE;
	
	public String getDIC_DOCNO() {
		return DIC_DOCNO;
	}
	public void setDIC_DOCNO(String dIC_DOCNO) {
		DIC_DOCNO = dIC_DOCNO;
	}
	public String getDIC_USER() {
		return DIC_USER;
	}
	public void setDIC_USER(String dIC_USER) {
		DIC_USER = dIC_USER;
	}
	public String getDIC_RESN() {
		return DIC_RESN;
	}
	public void setDIC_RESN(String dIC_RESN) {
		DIC_RESN = dIC_RESN;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public String getINTYPE() {
		return INTYPE;
	}
	public void setINTYPE(String iNTYPE) {
		INTYPE = iNTYPE;
	}
	public String getDOCNO() {
		return DOCNO;
	}
	public void setDOCNO(String dOCNO) {
		DOCNO = dOCNO;
	}
	public String getINLOGC() {
		return INLOGC;
	}
	public void setINLOGC(String iNLOGC) {
		INLOGC = iNLOGC;
	}
	public String getINBONBU() {
		return INBONBU;
	}
	public void setINBONBU(String iNBONBU) {
		INBONBU = iNBONBU;
	}
	public String getINCENTER() {
		return INCENTER;
	}
	public void setINCENTER(String iNCENTER) {
		INCENTER = iNCENTER;
	}
	public String getINBONBU_NAME() {
		return INBONBU_NAME;
	}
	public void setINBONBU_NAME(String iNBONBU_NAME) {
		INBONBU_NAME = iNBONBU_NAME;
	}
	public String getINCENTER_NAME() {
		return INCENTER_NAME;
	}
	public void setINCENTER_NAME(String iNCENTER_NAME) {
		INCENTER_NAME = iNCENTER_NAME;
	}
	public String getOUT_BONBU() {
		return OUT_BONBU;
	}
	public void setOUT_BONBU(String oUT_BONBU) {
		OUT_BONBU = oUT_BONBU;
	}
	public String getMODEL() {
		return MODEL;
	}
	public void setMODEL(String mODEL) {
		MODEL = mODEL;
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
	public String getINDATE() {
		return INDATE;
	}
	public void setINDATE(String iNDATE) {
		INDATE = iNDATE;
	}
	public int getINQTY() {
		return INQTY;
	}
	public void setINQTY(int iNQTY) {
		INQTY = iNQTY;
	}
	public String getDBO_USERNAME() {
		return DBO_USERNAME;
	}
	public void setDBO_USERNAME(String dBO_USERNAME) {
		DBO_USERNAME = dBO_USERNAME;
	}
	public String getOUTDATE() {
		return OUTDATE;
	}
	public void setOUTDATE(String oUTDATE) {
		OUTDATE = oUTDATE;
	}
	public int getOUTQTY() {
		return OUTQTY;
	}
	public void setOUTQTY(int oUTQTY) {
		OUTQTY = oUTQTY;
	}
	public String getDBO_CMNT() {
		return DBO_CMNT;
	}
	public void setDBO_CMNT(String dBO_CMNT) {
		DBO_CMNT = dBO_CMNT;
	}
	public String getDIC_DATE() {
		return DIC_DATE;
	}
	public void setDIC_DATE(String dIC_DATE) {
		DIC_DATE = dIC_DATE;
	}
	public String getDIC_USERNAME() {
		return DIC_USERNAME;
	}
	public void setDIC_USERNAME(String dIC_USERNAME) {
		DIC_USERNAME = dIC_USERNAME;
	}
	public String getDIC_CMNT() {
		return DIC_CMNT;
	}
	public void setDIC_CMNT(String dIC_CMNT) {
		DIC_CMNT = dIC_CMNT;
	}
	public String getCFMsts() {
		return CFMsts;
	}
	public void setCFMsts(String cFMsts) {
		CFMsts = cFMsts;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
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
	public String getStartDay() {
		return StartDay;
	}
	public void setStartDay(String startDay) {
		StartDay = startDay;
	}
	public String getEndDay() {
		return EndDay;
	}
	public void setEndDay(String endDay) {
		EndDay = endDay;
	}
	public String getLOGC() {
		return LOGC;
	}
	public void setLOGC(String lOGC) {
		LOGC = lOGC;
	}
	public String getBONBU() {
		return BONBU;
	}
	public void setBONBU(String bONBU) {
		BONBU = bONBU;
	}
	public String getCENTER() {
		return CENTER;
	}
	public void setCENTER(String cENTER) {
		CENTER = cENTER;
	}
	public String getINSTS() {
		return INSTS;
	}
	public void setINSTS(String iNSTS) {
		INSTS = iNSTS;
	}
	public String getCFMSTS() {
		return CFMSTS;
	}
	public void setCFMSTS(String cFMSTS) {
		CFMSTS = cFMSTS;
	}
	public String getMNAME() {
		return MNAME;
	}
	public void setMNAME(String mNAME) {
		MNAME = mNAME;
	}
	public int getPAGE() {
		return PAGE;
	}
	public void setPAGE(int pAGE) {
		PAGE = pAGE;
	}
}
