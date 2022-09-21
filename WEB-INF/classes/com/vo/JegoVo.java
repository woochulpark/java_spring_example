package com.vo;

public class JegoVo implements Cloneable{
	private String LOGC;
	private String LOGCNAME;
	private String BONBU;
	private String BONBUNAME;
	private String CENTER;
	private String CENTERNAME;
	private String MCI_CODE;
	private int SUMTYPE = 0; 
	private int PJEGO = 0;
	private int NOWJEGO = 0;
	private int MMJ_JEGO = 0;
	private int M1JEGO = 0;
	private int M2JEGO = 0;
	private int M3JEGO = 0;
	private int M4JEGO = 0;
	private int M5JEGO = 0;
	private int M6JEGO = 0;
	private int M7JEGO = 0;
	private int M8JEGO = 0;
	private int M9JEGO = 0;
	private int M10JEGO = 0;
	private int M11JEGO = 0;
	private int M12JEGO = 0;
	public String getLOGC() {
		return LOGC;
	}
	public void setLOGC(String lOGC) {
		LOGC = lOGC;
	}
	public String getLOGCNAME() {
		return LOGCNAME;
	}
	public void setLOGCNAME(String lOGCNAME) {
		LOGCNAME = lOGCNAME;
	}
	public String getBONBU() {
		return BONBU;
	}
	public void setBONBU(String bONBU) {
		BONBU = bONBU;
	}
	public String getBONBUNAME() {
		return BONBUNAME;
	}
	public void setBONBUNAME(String bONBUNAME) {
		BONBUNAME = bONBUNAME;
	}
	public String getCENTER() {
		return CENTER;
	}
	public void setCENTER(String cENTER) {
		CENTER = cENTER;
	}
	public String getCENTERNAME() {
		return CENTERNAME;
	}
	public void setCENTERNAME(String cENTERNAME) {
		CENTERNAME = cENTERNAME;
	}
	public String getMCI_CODE() {
		return MCI_CODE;
	}
	public void setMCI_CODE(String mCI_CODE) {
		MCI_CODE = mCI_CODE;
	}
	public int getSUMTYPE() {
		return SUMTYPE;
	}
	public void setSUMTYPE(int sUMTYPE) {
		SUMTYPE = sUMTYPE;
	}
	public int getPJEGO() {
		return PJEGO;
	}
	public void setPJEGO(int pJEGO) {
		PJEGO = pJEGO;
	}
	public int getNOWJEGO() {
		return NOWJEGO;
	}
	public void setNOWJEGO(int nOWJEGO) {
		NOWJEGO = nOWJEGO;
	}
	public int getMMJ_JEGO() {
		return MMJ_JEGO;
	}
	public void setMMJ_JEGO(int mMJ_JEGO) {
		MMJ_JEGO = mMJ_JEGO;
	}
	public int getM1JEGO() {
		return M1JEGO;
	}
	public void setM1JEGO(int m1jego) {
		M1JEGO = m1jego;
	}
	public int getM2JEGO() {
		return M2JEGO;
	}
	public void setM2JEGO(int m2jego) {
		M2JEGO = m2jego;
	}
	public int getM3JEGO() {
		return M3JEGO;
	}
	public void setM3JEGO(int m3jego) {
		M3JEGO = m3jego;
	}
	public int getM4JEGO() {
		return M4JEGO;
	}
	public void setM4JEGO(int m4jego) {
		M4JEGO = m4jego;
	}
	public int getM5JEGO() {
		return M5JEGO;
	}
	public void setM5JEGO(int m5jego) {
		M5JEGO = m5jego;
	}
	public int getM6JEGO() {
		return M6JEGO;
	}
	public void setM6JEGO(int m6jego) {
		M6JEGO = m6jego;
	}
	public int getM7JEGO() {
		return M7JEGO;
	}
	public void setM7JEGO(int m7jego) {
		M7JEGO = m7jego;
	}
	public int getM8JEGO() {
		return M8JEGO;
	}
	public void setM8JEGO(int m8jego) {
		M8JEGO = m8jego;
	}
	public int getM9JEGO() {
		return M9JEGO;
	}
	public void setM9JEGO(int m9jego) {
		M9JEGO = m9jego;
	}
	public int getM10JEGO() {
		return M10JEGO;
	}
	public void setM10JEGO(int m10jego) {
		M10JEGO = m10jego;
	}
	public int getM11JEGO() {
		return M11JEGO;
	}
	public void setM11JEGO(int m11jego) {
		M11JEGO = m11jego;
	}
	public int getM12JEGO() {
		return M12JEGO;
	}
	public void setM12JEGO(int m12jego) {
		M12JEGO = m12jego;
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
