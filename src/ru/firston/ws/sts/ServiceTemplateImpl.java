package ru.firston.ws.sts;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.firston.ms.core.jdbc.JdbcHandler;
import ru.firston.ws.sts.model.ServiceEnclosure;
import ru.firston.ws.sts.model.ServiceTemplate;
import ru.firston.ws.sts.model.Template;

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
		
		return (Template) JdbcHandler.builder(ConnectionSTS.class, new Template(id)).executeDelete();
	}

	@Override
	public Template save(Template template) throws Exception {
		
		JdbcHandler jh = JdbcHandler.builder(ConnectionSTS.class, template);
		
		return (Template) ((template.getId() == null) ? jh.executeInsert() : jh.executeUpdate());
	}

	@Override
	public Template saveFile(int id, MultipartFile file) throws Exception {

		Template template = new Template(id);		
		if(se_template.save(file).isSave()){
			template.setPath(se_template.getFullPath());
			JdbcHandler.builder(ConnectionSTS.class, template).executeUpdate();
		}else template.setParam(ControllerSTS.ERROR, se_template.getError());
		
		return template;
	}
	
	public void setSe_template(ServiceEnclosure se_template) {
		this.se_template = se_template;
	}
}
