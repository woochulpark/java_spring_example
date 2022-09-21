<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- START TABLE -->
			<div class="scroll_table">
				<table id='nTbl'>
					<thead>
						<tr>
							<th style="width: 10%;">물류센터</th>
							<th style="width: 10%;">상품코드</th>
							<th style="width: 10%;">대분류</th>
							<th style="width: 13%">중분류</th>
							<th style="width: 13%">소분류</th>
							<th style="width: 10%">ERP코드</th>
							<th style="width: 12%">시작일</th>
							<th style="width: 12%">종료일</th>
							<th style="width: 10%">운영여부</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${ModelListSize > 0 }">
					<c:forEach var="ModelList" items="${ModelList}" varStatus="st">
						<tr style="height: 40px;" onclick="javascript:MODEL_VAL('${ModelList.MMC_KINDS}','${ModelList.MMC_LOGC}','${ModelList.MMC_LGROUP}','${ModelList.MMC_MGROUP}','${ModelList.MMC_SGROUP}','${ModelList.MMC_UNIT}','${ModelList.MMC_ERPMODEL}','${ModelList.MMC_StartDay}','${ModelList.MMC_EndDay}','${ModelList.MMC_BizSts}','${ModelList.MMC_CMNT}')">
							<td>${ModelList.MMC_LOGCNAME}</td>
							<td>${ModelList.MMC_CODE}</td>
							<td>${ModelList.MMC_LGROUPNAME}</td> 	
							<td>${ModelList.MMC_MGROUP}</td>
							<td>${ModelList.MMC_SGROUP}</td> 	 
							<td>${ModelList.MMC_ERPMODEL}</td> 	 	 
							<td>${ModelList.MMC_StartDay}</td> 	
							<td>${ModelList.MMC_EndDay}</td>
							<td>
								<c:if test="${ModelList.MMC_BizSts == 'Y'}">
									운영
								</c:if>
								<c:if test="${ModelList.MMC_BizSts == 'N'}">
									종료
								</c:if>
							</td> 	  	 		 	
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
				</table>
			</div>
			<div style="text-align: center;">
            <c:if test="${pagination.curRange ne 1 }">
            <a href="#" onClick="Ajax_ModelList_search(1)">[처음]</a> 
            </c:if>
            <c:if test="${pagination.curPage ne 1}">
            <a href="#" onClick="Ajax_ModelList_search('${pagination.prevPage }')">[이전]</a> 
            </c:if>
            <c:forEach var="pageNum" begin="${pagination.startPage }" end="${pagination.endPage }">
            <c:choose>
            <c:when test="${pageNum eq  pagination.curPage}">
            <span style="font-weight: bold;"><a href="#" onClick="Ajax_ModelList_search('${pageNum }')">${pageNum }</a></span> 
            </c:when>
            <c:otherwise>
            <a href="#" onClick="Ajax_ModelList_search('${pageNum }')">${pageNum }</a> 
            </c:otherwise>
            </c:choose>
            </c:forEach>
            <c:if test="${pagination.curPage ne pagination.pageCnt && pagination.pageCnt > 0}">
            <a href="#" onClick="Ajax_ModelList_search('${pagination.nextPage }')">[다음]</a> 
            </c:if>
            <c:if test="${pagination.curRange ne pagination.rangeCnt && pagination.rangeCnt > 0}">
            <a href="#" onClick="Ajax_ModelList_search('${pagination.pageCnt }')">[끝]</a> 
            </c:if>
            </div>