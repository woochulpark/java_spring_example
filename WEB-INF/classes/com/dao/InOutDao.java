package com.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.service.MyBatisTransactionManager;
import com.vo.BonbuStatusVo;
import com.vo.BonbuVo;
import com.vo.CfmVo;
import com.vo.EventModelVo;
import com.vo.EventVo;
import com.vo.InOutListVo;
import com.vo.InOutVo;
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

@Repository("inOutDao")
public class InOutDao {
	
	@Resource(name="sqlSession")
	private SqlSessionTemplate session;

	public LogcInVo getLogcInInfo(String DOCNO){
		return session.selectOne("getLogcInInfo", DOCNO);
	}
	
	public List<LogcInVo> getLogcInList(String StartDay, String EndDay, String LOGC, String MNAME, String BISSTS, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("MNAME", MNAME);
		map.put("BISSTS", BISSTS);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return session.selectList("getLogcInList", map);
	}
	
	public LogcInVo getLastLogcInDocNo() {
		return session.selectOne("getLastLogcInDocNo");
	}
	
	public int insertLogcIn(LogcInVo vo) throws Exception{
		return session.insert("insertLogcIn", vo);
	}
	
	public int updateLogcIn(LogcInVo vo) throws Exception{
		return session.update("updateLogcIn", vo);
	}
	
	public int deleteLogcIn(String DOCNO) throws Exception{
		return session.delete("deleteLogcIn", DOCNO);
	}
	
	public List<LogcInVo> getBonbuInList(Map<String, Object> map){
		return session.selectList("getBonbuInList", map);
	}
	
	public List<BonbuVo> getBonbuInInfo(String LOGC, String DOCNO, String MODEL){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("DOCNO", DOCNO);
		map.put("MODEL", MODEL);
		return session.selectList("getBonbuInInfo", map);
	}
	
	public BonbuVo getLastBonbuInDocNo() {
		return session.selectOne("getLastBonbuInDocNo");
	}
	
	public int getBonbuInCount(String DOCNO) {
		return session.selectOne("getBonbuInCount", DOCNO);
	}
	
	public List<BonbuVo> getBonbuInCheck(String DOCNO) {
		return session.selectList("getBonbuInCheck", DOCNO);
	}
	
	public int updateModelJego(String MODEL, String CENTER, int JEGO, String PROG, String USERID) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MODEL", MODEL);
		map.put("CENTER", CENTER);
		map.put("JEGO", JEGO);
		map.put("PROG", PROG);
		map.put("USERID", USERID);
		return session.update("updateModelJego", map);
	}
	
	public int insertBonbuIn(BonbuVo vo, String MODEL, String OutDATE) throws Exception{
		return session.insert("insertBonbuIn", vo);
		/*
		MyBatisTransactionManager transaction = applicationContext.getBean(MyBatisTransactionManager.class);
		
		try {
			transaction.start();
			session.insert("insertBonbuIn", vo);
			//입고승인 하지 않으면 반출입 테이블에 쓰지 않음.
			//insertInOut(vo.getDBI_DOCNO(), MODEL, "1", OutDATE, vo.getDBI_QTY(), "1", vo.getDBI_CENTER(), "BonbuIn_Add", vo.getCMN_MAK_ID());
			updateOutLogcIn(vo.getDBI_TOPDOCNO(),"Y",OutDATE,"BonbuIn_Add",vo.getCMN_UPD_ID());
			transaction.commit();
		}
		catch (Exception e) {
			transaction.end();
		}*/
	}
	
	public int insertInOut(String DOCNO, String MODEL, String IOTYPE, String IODATE, int QTY, String RESN, String CENTER, String PROG, String USER) throws Exception{
		InOutVo inOutVo = new InOutVo();
		inOutVo.setDIB_DOCNO(DOCNO);
		inOutVo.setDIB_MODEL(MODEL);
		inOutVo.setDIB_IOTYPE(IOTYPE);
		inOutVo.setDIB_IODATE(IODATE);
		inOutVo.setDIB_QTY(QTY);
		inOutVo.setDIB_RESN(RESN);
		inOutVo.setDIB_CENTER(CENTER);
		inOutVo.setCMN_MAK_PROG(PROG);
		inOutVo.setCMN_MAK_ID(USER);
		inOutVo.setCMN_UPD_PROG(PROG);
		inOutVo.setCMN_UPD_ID(USER);
		return session.insert("insertInOut", inOutVo);
	}
	
	public int updateOutLogcIn(String DOCNO, String OUTSTS, String OUTDATE, String PROG, String USERID)  throws Exception{
		LogcInVo logcInVo = new LogcInVo();
		logcInVo.setDIL_DOCNO(DOCNO);
		logcInVo.setDIL_OUTSts(OUTSTS);
		logcInVo.setDIL_OUTDATE(OUTDATE);
		logcInVo.setCMN_UPD_PROG(PROG);
		logcInVo.setCMN_UPD_ID(USERID);
		return session.update("updateOutLogcIn", logcInVo);
	}
	
    public int updateBonbuIn(String TOPDOCNO, String CENTER, int QTY, String BisSts, String DOCNO, int Seq, String PROG, String USERID) throws Exception{
    	BonbuVo vo = new BonbuVo();
    	vo.setDBI_TOPDOCNO(TOPDOCNO);
    	vo.setDBI_CENTER(CENTER);
    	vo.setDBI_QTY(QTY);
    	vo.setDBI_BisSts(BisSts);
    	vo.setDBI_DOCNO(DOCNO);
    	vo.setDBI_SEQ(Seq);
		vo.setCMN_MAK_PROG(PROG);
		vo.setCMN_MAK_ID(USERID);
    	vo.setCMN_UPD_PROG(PROG);
    	vo.setCMN_UPD_ID(USERID);
    	return session.update("updateBonbuIn", vo);
	}
    
	public int deleteBonbuIn(String DOCNO) throws Exception{
		return session.delete("deleteBonbuIn", DOCNO);
	}
	
	public OrderVo getOrderInfo(String DOCNO) {
		return session.selectOne("getOrderInfo", DOCNO);
	}
	
	public List<OrderVo> getOrderList(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String MNAME, int PAGE, int PSIZE){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("MNAME", MNAME);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return session.selectList("getOrderList", map);
	}
	
	public OrderVo getLastOrderDocNo() {
		return session.selectOne("getLastOrderDocNo");
	}
	
	public int insertOrder(OrderVo orderVo) throws Exception{
		return session.insert("insertOrder", orderVo);
	}
	
	public int insertMove(OrderVo orderVo) throws Exception{
		return session.insert("insertMove", orderVo);
	}
	
	public int updateOrder(OrderVo orderVo) throws Exception{
		return session.update("updateOrder", orderVo);
	}
	
	public int deleteOrder(Map<String, Object> map) throws Exception{
		return session.delete("deleteOrder", map);
	}
	
	public int deleteMOVE(Map<String, Object> map) throws Exception{
		return session.delete("deleteMOVE", map);
	}
	
	public List<MoveVo> getBonbuMOVEList(Map<String, Object> map){
		return session.selectList("getBonbuMOVEList", map);
	}
	
	public List<MoveVo> getBonbuMOVEList2(Map<String, Object> map){
		return session.selectList("getBonbuMOVEList2", map);
	}
	
	public MoveVo getBonbuMOVEInfo(String DOCNO) {
		return session.selectOne("getBonbuMOVEInfo", DOCNO);
	}
	
	public MoveVo getJegoModel(Map<String, Object> map) {
		return session.selectOne("getJegoModel", map);
	}
	
	public int updateMoveCFM(Map<String, Object> map) throws Exception{
		return session.update("updateMoveCFM", map);
	}
	
	public int updateMove(MoveVo vo) throws Exception{
		return session.update("updateMove", vo);
	}
	
	public int updateChulhaJego(String MODEL, String CENTER, int JEGO, String NOWDATE) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MODEL", MODEL);
		map.put("CENTER", CENTER);
		map.put("JEGO", JEGO);
		map.put("NOWDATE", NOWDATE);
		return session.update("updateChulhaJego", map);
	}
	
	public int updateInLogcJego(String DOCNO) throws Exception{
		return session.update("updateInLogcJego", DOCNO);
	}
	
	public int updateInBonbuJego(String DOCNO) throws Exception{
		return session.update("updateInBonbuJego", DOCNO);
	}
	
	public List<CfmVo> getInCFMList(Map<String, Object> map){
		return session.selectList("getInCFMList", map);
	}
	
	public CfmVo getInCFMLogcInfo(String DOCNO) throws Exception{
		return session.selectOne("getInCFMLogcInfo", DOCNO);
	}
	
	public CfmVo getInCFMBonbuInfo(String DOCNO) throws Exception{
		return session.selectOne("getInCFMBonbuInfo", DOCNO);
	}
	
	public int insertCfm(CfmVo vo) throws Exception{
		return session.insert("insertCfm", vo);
	}
	
	public int updateCfm(CfmVo vo) throws Exception{
		return session.update("updateCfm", vo);
	}
	
	public int insertCfmLogcInOut(String DOCNO, String USERID) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("USERID", USERID);
		return session.insert("insertCfmLogcInOut", map);
	}
	
	public int insertCfmBonbuInOut(String DOCNO, String USERID) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("USERID", USERID);
		return session.insert("insertCfmBonbuInOut", map);
	}
	
	public int updateOrderCfm(String DOCNO) throws Exception{
		return session.update("updateOrderCfm", DOCNO);
	}
	
	public int updateInBonbuCfm(String DOCNO) throws Exception{
		return session.update("updateInBonbuCfm", DOCNO);
	}
	
	public List<OtherVo> getOtherList(Map<String, Object> map){
		return session.selectList("getOtherList", map);
	}
	
	public OtherVo getLastOtherDocNo() {
		return session.selectOne("getLastOtherDocNo");
	}
	
	public int insertOther(OtherVo vo) throws Exception{
		return session.insert("insertOther", vo);
	}
	
	public OtherVo getOtherInfo(String DOCNO){
		return session.selectOne("getOtherInfo", DOCNO);
	}
	
	public List<InoutReportVo> getJegoModelReport(Map<String, Object> map){
		return session.selectList("getJegoModelReport", map);
	}
	
	public List<InoutReportVo> getInoutReportCenter(Map<String, Object> map){
		return session.selectList("getInoutReportCenter", map);
	}
	
	public List<JegoVo> getNowJegoReport(Map<String, Object> map){
		return session.selectList("getNowJegoReport", map);
	}
	
	public List<InOutListVo> getInOutListReport(Map<String, Object> map){
		return session.selectList("getInOutListReport", map);
	}
	
	public List<InOutListVo> getInOutListReport_Excel(Map<String, Object> map){
		return session.selectList("getInOutListReport_Excel", map);
	}
	
	public List<BonbuStatusVo> getBonbuStatusReport(Map<String, Object> map){
		return session.selectList("getBonbuStatusReport", map);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidInCFMList(Map<String, Object> map) throws Exception{
		return session.selectList("getAndroidInCFMList", map);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidEventList(Map<String, Object> map) throws Exception{
		return session.selectList("getAndroidEventList", map);
	}
	
	public LinkedHashMap<String, Object> getAndroidJegoModel(Map<String, Object> map) throws Exception{
		return session.selectOne("getAndroidJegoModel", map);
	}
	
	public int insertEVENT(EventVo vo) throws Exception{
		return session.insert("insertEVENT", vo);
	}
	
	public int insertEVENTMODEL(EventModelVo vo) throws Exception{
		return session.insert("insertEVENTMODEL", vo);
	}
	
	public LinkedHashMap<String, Object> getAndroidEvent(String DOCNO) throws Exception{
		return session.selectOne("getAndroidEvent", DOCNO);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidEventModel(Map<String, Object> map) throws Exception{
		return session.selectList("getAndroidEventModel", map);
	}
	
	public int updateEVENT(EventVo vo) throws Exception{
		return session.update("updateEVENT", vo);
	}
	
	public int updateEVENTMODEL(EventModelVo vo) throws Exception{
		return session.update("updateEVENTMODEL", vo);
	}
	
	public int updateEVENTMODEL_REDATE(EventModelVo vo) throws Exception{
		return session.update("updateEVENTMODEL_REDATE", vo);
	}

	public int updateInOut(String DOCNO, String IOTYPE, String CENTER, String MODEL, String DIB_IODATE, int QTY, String PROG, String USERID ) throws Exception{
		InOutVo vo = new InOutVo();
		vo.setDIB_DOCNO(DOCNO);
		vo.setDIB_IOTYPE(IOTYPE);
		vo.setDIB_CENTER(CENTER);
		vo.setDIB_MODEL(MODEL);
		vo.setDIB_IODATE(DIB_IODATE);
		vo.setDIB_QTY(QTY);
		vo.setCMN_UPD_PROG(PROG);
		vo.setCMN_UPD_ID(USERID);
		return session.update("updateInOut", vo);
	}

	public int deleteEVENT(String DOCNO) throws Exception{
		return session.delete("deleteEVENT", DOCNO);
	}
	
	public int deleteEVENTMODEL(String DOCNO, String CENTER, String MODEL) throws Exception{
		EventModelVo vo = new EventModelVo();
		vo.setDEM_DOCNO(DOCNO);
		vo.setDEM_CENTER(CENTER);
		vo.setDEM_MODEL(MODEL);
		return session.delete("deleteEVENTMODEL", vo);
	}
	
	public int deleteInOut(String DOCNO, String IOTYPE, String CENTER, String MODEL) throws Exception{
		InOutVo vo = new InOutVo();
		vo.setDIB_DOCNO(DOCNO);
		vo.setDIB_IOTYPE(IOTYPE);
		vo.setDIB_CENTER(CENTER);
		vo.setDIB_MODEL(MODEL);
		return session.delete("deleteInOut", vo);
	}
	
	public int updateJego(String MODEL, String CENTER, int JEGO) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MODEL", MODEL);
		map.put("CENTER", CENTER);
		map.put("JEGO", JEGO);
		return session.update("updateJego", map);
	}
	
	public List<EventVo> getEventReturn(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String ENAME, String BISSTS, int PAGE, int PSIZE) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("ENAME", ENAME);
		map.put("BISSTS", BISSTS);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return session.selectList("getEventReturn", map);
	}
	
	public int updateEventReturn(EventVo vo) throws Exception{
		return session.update("updateEventReturn", vo);
	}
	public EventModelVo getEventModel(String DOCNO, String CENTER, String MODEL) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("CENTER", CENTER);
		map.put("MODEL", MODEL);
		return session.selectOne("getEventModel", map);
	}
	
	public List<ModelBepumVo> getModelBepum(String LOGC, String BONBU, String CENTER, String MNAME, String BISSTS, int PAGE, int PSIZE) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("MNAME", MNAME);
		map.put("BISSTS", BISSTS);
		map.put("PAGE", PAGE);
		map.put("PSIZE", PSIZE);
		return session.selectList("getModelBepum", map);
	}
	
	public List<LinkedHashMap<String, Object>> getAndroidModelRetuen(String StartDay, String EndDay, String LOGC, String BONBU, String CENTER, String ENAME, String BISSTS) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("ENAME", ENAME);
		map.put("BISSTS", BISSTS);
		return session.selectList("getAndroidModelRetuen", map);
	}
	
	public int updateAndroidReturn(EventVo vo) throws Exception{
		return session.update("updateAndroidReturn", vo);
	}
	
	public List<PriceInVo> getPriceList(String MODEL, String CENTER) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("MODEL", MODEL);
		map.put("CENTER", CENTER);
		return session.selectList("getPriceList", map);
	}
	
	public List<PriceInVo> getPriceJego(String OUTDOCNO, String CENTER, String MODEL) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("OUTDOCNO", OUTDOCNO);
		map.put("CENTER", CENTER);
		map.put("MODEL", MODEL);
		return session.selectList("getPriceJego", map);
	}
	
	public int deletePriceOutJego(String DOCNO, String OUTDOCNO, String CENTER, String MODEL, String LOGCNO) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("OUTDOCNO", OUTDOCNO);
		map.put("CENTER", CENTER);
		map.put("MODEL", MODEL);
		map.put("LOGCNO", LOGCNO);
		return session.delete("deletePriceOutJego", map);
	}
	
	public int insertPriceBonbuIn(PriceInVo vo) throws Exception{
		return session.insert("insertPriceBonbuIn", vo);
	}
	
	public int insertPriceMoveIn(PriceOutVo vo) throws Exception{
		return session.insert("insertPriceMoveIn", vo);
	}
	
	public int insertPriceMoveOut(PriceOutVo vo) throws Exception{
		return session.insert("insertPriceMoveOut", vo);
	}
	
	public int getCheckPrice(String DOCNO) throws Exception{
		return session.selectOne("getCheckPrice", DOCNO);
	}
	
	public int updatePriceJego(String DOCNO, String MODEL, String CENTER, String LOGCNO, int JEGO) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("CENTER", CENTER);
		map.put("MODEL", MODEL);
		map.put("LOGCNO", LOGCNO);
		map.put("JEGO", JEGO);
		return session.update("updatePriceJego", map);
	}
	
	public int updatePriceOutJego(String DOCNO, String OUTDOCNO, String CENTER, String MODEL, String LOGCNO, int JEGO) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("DOCNO", DOCNO);
		map.put("OUTDOCNO", OUTDOCNO);
		map.put("CENTER", CENTER);
		map.put("MODEL", MODEL);
		map.put("LOGCNO", LOGCNO);
		map.put("JEGO", JEGO);
		return session.update("updatePriceOutJego", map);
	}
	
	public List<JegoMagamReportVo> getJegoMagamReport(String LOGC, String BONBU, String CENTER, String LGROUP, String KINDS, String StartDay, String EndDay) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LOGC", LOGC);
		map.put("BONBU", BONBU);
		map.put("CENTER", CENTER);
		map.put("LGROUP", LGROUP);
		map.put("KINDS", KINDS);
		map.put("StartDay", StartDay);
		map.put("EndDay", EndDay);
		return session.selectList("getJegoMagamReport", map);
	}
}
