package com.service;

import java.io.File;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.io.Resources;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	public String filePath(HttpServletRequest request, String PATH) {
		try {
			String resource = "com/setting/db.properties";
			Reader reader = Resources.getResourceAsReader(resource);
			Properties properties = new Properties();
			properties.load(reader);
			String uploadPath = properties.getProperty("UPLOAD_PATH") + "/" + PATH;
			File Folder = new File(uploadPath);
			if (!Folder.exists()) {
				Folder.mkdir(); //폴더 생성합니다.       
			}
			return uploadPath;
		}
		catch (Exception e) {
			return "";
		}
	}
	public int fileUpload(MultipartFile file, String delFileName, String PATH, String sysFile, HttpServletRequest request){
		try {
			String uploadPath = filePath(request, PATH);
			if(uploadPath.length() == 0)
				return -1;
			int maxSize = 10 * 1024 * 1024; 

			if (file.getSize() < maxSize){
				try{
					File newFile = new File(uploadPath, sysFile);
					if(!newFile.exists()) {
						newFile.createNewFile();
					}
					file.transferTo(newFile);

					if (!sysFile.equals(delFileName) && !delFileName.equals("")){
						File delFile = new File(uploadPath, delFileName);
						delFile.delete();
					}

				}catch (Exception e){
					return -1;
				}
			}else{
				return -2;
			}
		}
		catch (Exception e) {
			return -1;
		}
		return 1;
	}

	public int fileDelete (String delFileName, String PATH, HttpServletRequest request){
		String uploadPath = filePath(request, PATH);
		try{
			if (!delFileName.equals("")){
				File delFile = new File(uploadPath, delFileName);
				delFile.delete();
			}
			return 1;
		}catch (Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean Isfile (String FileName, String PATH, HttpServletRequest request){
		String uploadPath = filePath(request, PATH);
		File newFile = new File(uploadPath, FileName);
		return newFile.exists();
	}
	public MediaType getType(String type) {
		if(type.toUpperCase().equals("JPG"))
			return MediaType.IMAGE_JPEG;
		else if(type.toUpperCase().equals("GIF"))
			return MediaType.IMAGE_GIF;
		else if(type.toUpperCase().equals("PNG"))
			return MediaType.IMAGE_PNG;
		else 
			return null;
	}
}
