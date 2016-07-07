package ru.firston.ws.sts;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.firston.ms.core.jdbc.JdbcHandler;
import ru.firston.ws.sts.model.ServiceEnclosure;
import ru.firston.ws.sts.model.ServiceTemplate;
import ru.firston.ws.sts.model.Template;

/**
 * 
 * @author Anton Arefyev
 * @version 16.07.07 pre 16.06.24
 *
 */
@Service
public class ServiceTemplateImpl implements ServiceTemplate{
	
	ServiceEnclosure se_template;
	
	@Override
	public List<Template> list() throws Exception {
		
		return JdbcHandler.builder(ConnectionSTS.class, new Template()).executeSelect();
	}

	@Override
	public Template findById(int id) throws Exception {
		
		return JdbcHandler.builder(ConnectionSTS.class, new Template()).get(id);
	}

	@Override
	public Template delete(int id) throws Exception {
		
		JdbcHandler jdbcHandler = JdbcHandler.builder(ConnectionSTS.class, new Template());
		Template template = (Template) jdbcHandler.get(id);		
		try{
		  se_template.exists(template.getPath());
		  se_template.delete();		  
		}catch (FileNotFoundException e) {
		  if(!template.getPath().equals(""))
			template.setParam(ControllerSTS.ERROR, e.getMessage());
		}		
		jdbcHandler.executeDelete();
		return template;
	}

	@Override
	public Template save(Template template) throws Exception {
		
		JdbcHandler jh = JdbcHandler.builder(ConnectionSTS.class, template);
		
		return (Template) ((template.getId() == null) ? jh.executeInsert() : jh.executeUpdate());
	}

	@Override
	public Template saveFile(int id, MultipartFile file) throws Exception {

		JdbcHandler jdbcHandler = JdbcHandler.builder(ConnectionSTS.class, new Template());				
		Template template = (Template) jdbcHandler.get(id);
		try{
		  se_template.exists(template.getPath());
		  se_template.delete();
		}catch (FileNotFoundException e) {
		  if(!template.getPath().equals(""))
		    template.setParam(ControllerSTS.ERROR, e.getMessage());
		}		
		if(se_template.save(file).isSave()){
			template.setPath(se_template.getFullPath());
			jdbcHandler.executeUpdate();
		}else template.setParam(ControllerSTS.ERROR, se_template.getError());
		
		return template;
	}
	
	public void setSe_template(ServiceEnclosure se_template) {
		this.se_template = se_template;
	}
}
