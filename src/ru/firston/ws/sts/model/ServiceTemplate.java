package ru.firston.ws.sts.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Anton Arefyev
 * @version 16.06.24
 *
 */
public interface ServiceTemplate {

	public List<Template> list()throws Exception;
	
	public Template findById(int id)throws Exception;
	
	public Template delete(int id)throws Exception;
	
	public Template save(Template template)throws Exception;
	
	public Template saveFile(int id, MultipartFile file)throws Exception;
}
