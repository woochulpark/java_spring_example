package com.vo;

public class JegoMagamReportVo  implements Cloneable{
	private int sumtype;
	private String MCI_CODE;
	private String MCI_LOGC;
	private String MCI_LOGCNAME;
	private String MCI_Bonbu;
	private String MCI_BonbuNAME;
	private String MCI_Center;
	private String MCI_CenterName;
	private String MMC_CODE;
	private String MMC_LGROUP;
	private String MMC_LGROUPNAME;
	private String MMC_MGROUP; 
	private String MMC_SGROUP;
	private int pqty;
	private int pprice; 
	private int logcqty; 
	private int logcprice;
	private int moveinqty;
	private int moveinprice;
	private int moveoutqty;
	private int moveoutprice;
	private int otherqty;
	private int otherprice;
	private int eventqty;
	private int eventprice;
	private int nowqty;
	private int nowprice;
	public int getSumtype() {
		return sumtype;
	}
	public void setSumtype(int sumtype) {
		this.sumtype = sumtype;
	}
	public String getMCI_CODE() {
		return MCI_CODE;
	}
	public void setMCI_CODE(String mCI_CODE) {
		MCI_CODE = mCI_CODE;
	}
	public String getMCI_LOGC() {
		return MCI_LOGC;
	}
	public void setMCI_LOGC(String mCI_LOGC) {
		MCI_LOGC = mCI_LOGC;
	}
	public String getMCI_LOGCNAME() {
		return MCI_LOGCNAME;
	}
	public void setMCI_LOGCNAME(String mCI_LOGCNAME) {
		MCI_LOGCNAME = mCI_LOGCNAME;
	}
	public String getMCI_Bonbu() {
		return MCI_Bonbu;
	}
	public void setMCI_Bonbu(String mCI_Bonbu) {
		MCI_Bonbu = mCI_Bonbu;
	}
	public String getMCI_BonbuNAME() {
		return MCI_BonbuNAME;
	}
	public void setMCI_BonbuNAME(String mCI_BonbuNAME) {
		MCI_BonbuNAME = mCI_BonbuNAME;
	}
	public String getMCI_Center() {
		return MCI_Center;
	}
	public void setMCI_Center(String mCI_Center) {
		MCI_Center = mCI_Center;
	}
	public String getMCI_CenterName() {
		return MCI_CenterName;
	}
	public void setMCI_CenterName(String mCI_CenterName) {
		MCI_CenterName = mCI_CenterName;
	}
	public String getMMC_CODE() {
		return MMC_CODE;
	}
	public void setMMC_CODE(String mMC_CODE) {
		MMC_CODE = mMC_CODE;
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
	public int getPqty() {
		return pqty;
	}
	public void setPqty(int pqty) {
		this.pqty = pqty;
	}
	public int getPprice() {
		return pprice;
	}
	public void setPprice(int pprice) {
		this.pprice = pprice;
	}
	public int getLogcqty() {
		return logcqty;
	}
	public void setLogcqty(int logcqty) {
		this.logcqty = logcqty;
	}
	public int getLogcprice() {
		return logcprice;
	}
	public void setLogcprice(int logcprice) {
		this.logcprice = logcprice;
	}
	public int getMoveinqty() {
		return moveinqty;
	}
	public void setMoveinqty(int moveinqty) {
		this.moveinqty = moveinqty;
	}
	public int getMoveinprice() {
		return moveinprice;
	}
	public void setMoveinprice(int moveinprice) {
		this.moveinprice = moveinprice;
	}
	public int getMoveoutqty() {
		return moveoutqty;
	}
	public void setMoveoutqty(int moveoutqty) {
		this.moveoutqty = moveoutqty;
	}
	public int getMoveoutprice() {
		return moveoutprice;
	}
	public void setMoveoutprice(int moveoutprice) {
		this.moveoutprice = moveoutprice;
	}
	public int getOtherqty() {
		return otherqty;
	}
	public void setOtherqty(int otherqty) {
		this.otherqty = otherqty;
	}
	public int getOtherprice() {
		return otherprice;
	}
	public void setOtherprice(int otherprice) {
		this.otherprice = otherprice;
	}
	public int getEventqty() {
		return eventqty;
	}
	public void setEventqty(int eventqty) {
		this.eventqty = eventqty;
	}
	public int getEventprice() {
		return eventprice;
	}
	public void setEventprice(int eventprice) {
		this.eventprice = eventprice;
	}
	public int getNowqty() {
		return nowqty;
	}
	public void setNowqty(int nowqty) {
		this.nowqty = nowqty;
	}
	public int getNowprice() {
		return nowprice;
	}
	public void setNowprice(int nowprice) {
		this.nowprice = nowprice;
	}
	
	public Object clone()
	{ 
		Object obj = null; 
		try{ 
			obj = super.clone(); 
		}
		catch(Exception e)
		{
			
		} 
		return obj;
	}
}
