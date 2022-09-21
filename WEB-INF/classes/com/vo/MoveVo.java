package com.vo;

public class MoveVo {
	private String DBM_DOCNO;
	private String DBM_CENTER;
	private String DBM_DATE;
	private int DBM_QTY;
	private String DBM_UESR;
	private String DBM_HOWMOVE;
	private String DBM_CMNT;
	private String DBM_CFM;
	private String DBM_CFMUSER;
	private String DBM_CFMDATE;
	private String DBM_Sts;
	private String CMN_MAK_PROG;
	private String CMN_MAK_ID;
	private String CMN_UPD_PROG;
	private String CMN_UPD_ID;
	
	private String DBO_DOCNO;
	private String DBO_DATE;
	private String DBO_BisSts;
	private String DBO_CFMDATE;
	private String DBO_MODEL;
	
	private String OUTLOGC;
	private String OUTLOGCNAME;
	private String OUTBONBU;
	private String OUTBONBUNAME;
	private String OUTCENTER;
	private String OUTCENTERNAME;
	private String DBO_CENTER;
	private String INLOGC;
	private String INLOGCNAME;
	private String INBONBUNAME;
	private String INCENTERNAME;
	private String MMC_LGROUP;
	private String MMC_LGROUPNAME;
	private String MMC_MGROUP;
	private String MMC_SGROUP;
	private String MUT_UserName;
	private String DBO_CMNT;
	private int DBO_QTY;
	private int DMJ_JEGO;
	private int OUTJEGO;
	
	private int rownum;
	private int totalCnt;
	
	public String getDBM_DOCNO() {
		return DBM_DOCNO;
	}
	public void setDBM_DOCNO(String dBM_DOCNO) {
		DBM_DOCNO = dBM_DOCNO;
	}
	public String getDBM_CENTER() {
		return DBM_CENTER;
	}
	public void setDBM_CENTER(String dBM_CENTER) {
		DBM_CENTER = dBM_CENTER;
	}
	public String getDBM_DATE() {
		return DBM_DATE;
	}
	public void setDBM_DATE(String dBM_DATE) {
		DBM_DATE = dBM_DATE;
	}
	public int getDBM_QTY() {
		return DBM_QTY;
	}
	public void setDBM_QTY(int dBM_QTY) {
		DBM_QTY = dBM_QTY;
	}
	public String getDBM_UESR() {
		return DBM_UESR;
	}
	public void setDBM_UESR(String dBM_UESR) {
		DBM_UESR = dBM_UESR;
	}
	public String getDBM_HOWMOVE() {
		return DBM_HOWMOVE;
	}
	public void setDBM_HOWMOVE(String dBM_HOWMOVE) {
		DBM_HOWMOVE = dBM_HOWMOVE;
	}
	public String getDBM_CMNT() {
		return DBM_CMNT;
	}
	public void setDBM_CMNT(String dBM_CMNT) {
		DBM_CMNT = dBM_CMNT;
	}
	public String getDBM_CFM() {
		return DBM_CFM;
	}
	public void setDBM_CFM(String dBM_CFM) {
		DBM_CFM = dBM_CFM;
	}
	public String getDBM_CFMUSER() {
		return DBM_CFMUSER;
	}
	public void setDBM_CFMUSER(String dBM_CFMUSER) {
		DBM_CFMUSER = dBM_CFMUSER;
	}
	public String getDBM_CFMDATE() {
		return DBM_CFMDATE;
	}
	public void setDBM_CFMDATE(String dBM_CFMDATE) {
		DBM_CFMDATE = dBM_CFMDATE;
	}
	public String getDBM_Sts() {
		return DBM_Sts;
	}
	public void setDBM_Sts(String dBM_Sts) {
		DBM_Sts = dBM_Sts;
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
	public String getDBO_DOCNO() {
		return DBO_DOCNO;
	}
	public void setDBO_DOCNO(String dBO_DOCNO) {
		DBO_DOCNO = dBO_DOCNO;
	}
	public String getDBO_DATE() {
		return DBO_DATE;
	}
	public void setDBO_DATE(String dBO_DATE) {
		DBO_DATE = dBO_DATE;
	}
	public String getDBO_BisSts() {
		return DBO_BisSts;
	}
	public void setDBO_BisSts(String dBO_BisSts) {
		DBO_BisSts = dBO_BisSts;
	}
	public String getDBO_CFMDATE() {
		return DBO_CFMDATE;
	}
	public void setDBO_CFMDATE(String dBO_CFMDATE) {
		DBO_CFMDATE = dBO_CFMDATE;
	}
	public String getDBO_MODEL() {
		return DBO_MODEL;
	}
	public void setDBO_MODEL(String dBO_MODEL) {
		DBO_MODEL = dBO_MODEL;
	}
	public String getOUTLOGC() {
		return OUTLOGC;
	}
	public void setOUTLOGC(String oUTLOGC) {
		OUTLOGC = oUTLOGC;
	}
	public String getOUTLOGCNAME() {
		return OUTLOGCNAME;
	}
	public void setOUTLOGCNAME(String oUTLOGCNAME) {
		OUTLOGCNAME = oUTLOGCNAME;
	}
	public String getOUTBONBU() {
		return OUTBONBU;
	}
	public void setOUTBONBU(String oUTBONBU) {
		OUTBONBU = oUTBONBU;
	}
	public String getOUTBONBUNAME() {
		return OUTBONBUNAME;
	}
	public void setOUTBONBUNAME(String oUTBONBUNAME) {
		OUTBONBUNAME = oUTBONBUNAME;
	}
	public String getOUTCENTER() {
		return OUTCENTER;
	}
	public void setOUTCENTER(String oUTCENTER) {
		OUTCENTER = oUTCENTER;
	}
	public String getOUTCENTERNAME() {
		return OUTCENTERNAME;
	}
	public void setOUTCENTERNAME(String oUTCENTERNAME) {
		OUTCENTERNAME = oUTCENTERNAME;
	}
	public String getDBO_CENTER() {
		return DBO_CENTER;
	}
	public void setDBO_CENTER(String dBO_CENTER) {
		DBO_CENTER = dBO_CENTER;
	}
	public String getINLOGC() {
		return INLOGC;
	}
	public void setINLOGC(String iNLOGC) {
		INLOGC = iNLOGC;
	}
	public String getINLOGCNAME() {
		return INLOGCNAME;
	}
	public void setINLOGCNAME(String iNLOGCNAME) {
		INLOGCNAME = iNLOGCNAME;
	}
	
	public String getINBONBUNAME() {
		return INBONBUNAME;
	}
	public void setINBONBUNAME(String iNBONBUNAME) {
		INBONBUNAME = iNBONBUNAME;
	}
	public String getINCENTERNAME() {
		return INCENTERNAME;
	}
	public void setINCENTERNAME(String iNCENTERNAME) {
		INCENTERNAME = iNCENTERNAME;
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
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public int getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getMUT_UserName() {
		return MUT_UserName;
	}
	public void setMUT_UserName(String mUT_UserName) {
		MUT_UserName = mUT_UserName;
	}
	public String getDBO_CMNT() {
		return DBO_CMNT;
	}
	public void setDBO_CMNT(String dBO_CMNT) {
		DBO_CMNT = dBO_CMNT;
	}
	public int getDBO_QTY() {
		return DBO_QTY;
	}
	public void setDBO_QTY(int dBO_QTY) {
		DBO_QTY = dBO_QTY;
	}
	public int getDMJ_JEGO() {
		return DMJ_JEGO;
	}
	public void setDMJ_JEGO(int dMJ_JEGO) {
		DMJ_JEGO = dMJ_JEGO;
	}
	public int getOUTJEGO() {
		return OUTJEGO;
	}
	public void setOUTJEGO(int oUTJEGO) {
		OUTJEGO = oUTJEGO;
	}
}
