package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView {

	public DownloadView(){
		setContentType("application/download; utf-8");
	}
	
    @Override
    protected void renderMergedOutputModel(Map model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
         
        File file = (File)model.get("downloadFile");
        String fileNameOrg = model.containsKey("orgFile") ? (String)model.get("orgFile") : file.getName();
        
        response.setContentType(getContentType());
        response.setContentLength((int)file.length());
         
        String header = request.getHeader("User-Agent");
         
        if (header.contains("MSIE") || header.contains("Trident")) {
            fileNameOrg = URLEncoder.encode(fileNameOrg,"UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileNameOrg + ";");
        } else {
            fileNameOrg = new String(fileNameOrg.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNameOrg + "\"");
        }
         
        //response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
         
        OutputStream out = response.getOutputStream();
         
        FileInputStream fis = null;
         
        try {
             
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
             
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            if(fis != null){
                try{
                    fis.close();
                }catch(Exception e){}
            }
        }// try end;
        out.flush();
         
    }// render() end;
}
