package ru.firston.ws.sts.utils;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import ru.firston.ws.sts.model.ServiceEnclosure;

/**
 * 
 * @author Anton Arefyev
 * @version 16.05.12
 *
 */
@Component
public class Enclosure implements ServiceEnclosure {

	private String dir;
	/**
	 * Статус сохранения файла.
	 * Результат вызова метода ServiceAdmin.saveFile();
	 */
	private boolean isSave = false;	
	/**
	 * Расположение копии сохраненого файла
	 */
	private String fileName;
	/**
	 *  Содержимое файла
	 */
	private byte[] content;
	
	/**
	 * Сообщение об ошибке
	 */
	private String error;
	
	public Boolean isSave() {
		return isSave;
	}
	public void setIsSave(Boolean isSave) {
		this.isSave = isSave;
	}	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		
		this.fileName = new StringBuffer().append(new Date().getTime())
										  .append("_")
										  .append(fileName).toString();
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	@Override
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	@Override
	public Enclosure save(MultipartFile file) {
		
		if (file != null && !file.isEmpty()) {	
			setFileName(file.getOriginalFilename());
			try {				
				BufferedOutputStream stream = new BufferedOutputStream(
													new FileOutputStream(
															new File(getFullPath())
													)
											  );
	            FileCopyUtils.copy(file.getInputStream(), stream);
			    stream.close();					
			    setIsSave(true);
			    setContent(file.getBytes());					
			}catch (Exception e) {
				setError(e.getMessage());
			}
		}else setError("Отправлен пустой файл");
		
		return this;
	}
	@Override
	public String getFullPath() {
		
		return getDir() + getFileName();
	}
	
	
	
}
