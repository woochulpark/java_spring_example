package com.vo;

public class ModelVo {
	private String MMC_CODE = "";
	private String MMC_LOGC = "";
	private String MMC_LOGCNAME = "";
	private String MMC_LGROUP = "";
	private String MMC_LGROUPNAME = "";
	private String MMC_MGROUP = "";
	private String MMC_SGROUP = "";
	private String MMC_KINDS = "";
	private String MMC_KINDSNAME = "";
	private String MMC_UNIT = "";
	private String MMC_StartDay = "";
	private String MMC_EndDay = "";
	private String MMC_BizSts = "";
	private String MMC_CMNT = "";
	private String MMC_ERPMODEL = "";
	private String CMN_MAK_PROG = "";
	private String CMN_MAK_ID = "";
	private String CMN_UPD_PROG = "";
	private String CMN_UPD_ID = "";
	private int rownum;
	private int totalCnt;
	
	private String MML_CODE = "";
	private String MML_DATE = "";
	private String MML_USER = "";
	private String MML_USERNAME = "";
	private String MML_CMNT = "";
	private int MMC_SEQ;
	
	public String getMMC_CODE() {
		return MMC_CODE;
	}
	public void setMMC_CODE(String mMC_CODE) {
		MMC_CODE = mMC_CODE;
	}
	public String getMMC_LOGC() {
		return MMC_LOGC;
	}
	public void setMMC_LOGC(String mMC_LOGC) {
		MMC_LOGC = mMC_LOGC;
	}
	public String getMMC_LOGCNAME() {
		return MMC_LOGCNAME;
	}
	public void setMMC_LOGCNAME(String mMC_LOGCNAME) {
		MMC_LOGCNAME = mMC_LOGCNAME;
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
		return MMC_MGROUP == null ? "" : MMC_MGROUP;
	}
	public void setMMC_MGROUP(String mMC_MGROUP) {
		MMC_MGROUP = mMC_MGROUP;
	}
	public String getMMC_SGROUP() {
		return MMC_SGROUP == null ? "" : MMC_SGROUP;
	}
	public void setMMC_SGROUP(String mMC_SGROUP) {
		MMC_SGROUP = mMC_SGROUP;
	}
	public String getMMC_KINDS() {
		return MMC_KINDS;
	}
	public void setMMC_KINDS(String mMC_KINDS) {
		MMC_KINDS = mMC_KINDS;
	}
	public String getMMC_KINDSNAME() {
		return MMC_KINDSNAME;
	}
	public void setMMC_KINDSNAME(String mMC_KINDSNAME) {
		MMC_KINDSNAME = mMC_KINDSNAME;
	}
	public String getMMC_UNIT() {
		return MMC_UNIT == null ? "" : MMC_UNIT;
	}
	public void setMMC_UNIT(String mMC_UNIT) {
		MMC_UNIT = mMC_UNIT;
	}
	public String getMMC_StartDay() {
		return MMC_StartDay;
	}
	public void setMMC_StartDay(String mMC_StartDay) {
		MMC_StartDay = mMC_StartDay;
	}
	public String getMMC_EndDay() {
		return MMC_EndDay;
	}
	public void setMMC_EndDay(String mMC_EndDay) {
		MMC_EndDay = mMC_EndDay;
	}
	public String getMMC_BizSts() {
		return MMC_BizSts;
	}
	public void setMMC_BizSts(String mMC_BizSts) {
		MMC_BizSts = mMC_BizSts;
	}
	public String getMMC_CMNT() {
		return MMC_CMNT == null ? "" : MMC_CMNT;
	}
	public void setMMC_CMNT(String mMC_CMNT) {
		MMC_CMNT = mMC_CMNT;
	}
	public String getMMC_ERPMODEL() {
		return MMC_ERPMODEL;
	}
	public void setMMC_ERPMODEL(String mMC_ERPMODEL) {
		MMC_ERPMODEL = mMC_ERPMODEL;
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
	public String getMML_CODE() {
		return MML_CODE;
	}
	public void setMML_CODE(String mML_CODE) {
		MML_CODE = mML_CODE;
	}
	public String getMML_DATE() {
		return MML_DATE;
	}
	public void setMML_DATE(String mML_DATE) {
		MML_DATE = mML_DATE;
	}
	public String getMML_USER() {
		return MML_USER;
	}
	public void setMML_USER(String mML_USER) {
		MML_USER = mML_USER;
	}
	public String getMML_USERNAME() {
		return MML_USERNAME;
	}
	public void setMML_USERNAME(String mML_USERNAME) {
		MML_USERNAME = mML_USERNAME;
	}
	public String getMML_CMNT() {
		return MML_CMNT.replace("\r\n","<br>");
	}
	public void setMML_CMNT(String mML_CMNT) {
		MML_CMNT = mML_CMNT;
	}
	public int getMMC_SEQ() {
		return MMC_SEQ;
	}
	public void setMMC_SEQ(int mMC_SEQ) {
		MMC_SEQ = mMC_SEQ;
	}
	
}
