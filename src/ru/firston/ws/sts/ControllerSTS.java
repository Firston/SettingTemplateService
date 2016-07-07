package ru.firston.ws.sts;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ru.firston.ws.sts.model.Link;
import ru.firston.ws.sts.model.ServiceLink;
import ru.firston.ws.sts.model.ServiceTemplate;
import ru.firston.ws.sts.model.Template;

/**
 * 
 * @author Anton Arefyev
 * @version 16.07.07 pre 16.06.24
 *
 */
@EnableWebMvc
@RestController
public class ControllerSTS {

	/**
	 * Ключ параметра передающего ошибку в процессе выполнения
	 */
	public static final String ERROR = "error";
	
	public static WebApplicationContext context = ContextLoaderListener.getCurrentWebApplicationContext();
	
	@Autowired
	ServiceTemplate st;
	
	@Autowired
	ServiceLink sl;		

	/**
	 * ===============================================
	 * TEMPLATES
	 * ===============================================
	 */
	@RequestMapping(value="/", produces="application/json;charset=UTF-8", headers="Accept=*/*")
	public void start(HttpServletResponse response) throws IOException{
		
		System.out.println("Конфигурационный сервис шаблонов успешно стартанул");
		System.out.println("context : " + context);
		response.getWriter().write("Конфигурационный сервис шаблонов успешно стартанул");		
	}
	
	@RequestMapping(value="/templates", headers = {"Accept=*/*"}, produces="application/json")
	public ResponseEntity<Template[]> templates(){

		HttpStatus httpStatus;
		Template[] templates = null;
		try {						
			List<Template> lt = st.list();
			int size = lt.size();
			templates = lt.toArray(new Template[size]);
			httpStatus = size == 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;			
		} catch (Exception e) {
			e.printStackTrace();
			Template template = new Template();
			template.setParam(ERROR, e.getMessage());
			templates = new Template[1];
			templates[0] = template;
			httpStatus = HttpStatus.PARTIAL_CONTENT;
		}
		return new ResponseEntity<Template[]>(templates, httpStatus);				
	}
	
	
	@RequestMapping(value="/template/{id}", headers = {"Accept=*/*"}, produces="application/json")
	public ResponseEntity<Template> template(@PathVariable("id") int id){
		
		HttpStatus httpStatus;
		Template template = null;
		try {
			template = st.findById(id);
			httpStatus = template.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			template = new Template();
			template.setParam(ERROR, e.getMessage());
			httpStatus = HttpStatus.PARTIAL_CONTENT;
		}

		return new ResponseEntity<Template>(template, httpStatus);
	}
	
	@RequestMapping(value="/template/{id}/del", headers="Accept=*/*", produces = "application/json")
	public ResponseEntity<Template> template_delete(@PathVariable("id") int id){		
		
		HttpStatus httpStatus;
		Template template = null;
		try {
			template = st.delete(id);
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			template = new Template(id);
			template.setParam(ERROR, e.getMessage());			
			httpStatus = HttpStatus.NOT_MODIFIED;
		}
		return new ResponseEntity<Template>(template, httpStatus);
	}

	@RequestMapping(value="/template/save", method=RequestMethod.POST, headers="Accept=*/*", produces = "application/json")
	public ResponseEntity<Template> template_save(@RequestBody Template template){		
		
		HttpStatus httpStatus;
		try {
			st.save(template);
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			template.setParam(ERROR, e.getMessage());
			httpStatus = HttpStatus.NOT_MODIFIED;			
		}
		return new ResponseEntity<Template>(template, httpStatus);
	}
	
	@RequestMapping(value="/template/{id}/file/save", 
					method=RequestMethod.POST, 
					headers={"Accept=*/*", "Content-Type=multipart/form-data"}, 
					produces = "application/json")
	public ResponseEntity<Template> template_save_file(@PathVariable("id") int id, 
													   @RequestParam MultipartFile file){		

		Template template;
		HttpStatus httpStatus;
		try {
			template = st.saveFile(id, file);
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {			
			template = new Template(id);
			template.setParam(ERROR, e.getMessage());
			httpStatus = HttpStatus.NOT_MODIFIED;
		}
		return new ResponseEntity<Template>(template, httpStatus);
	}

	/**
	 * ===============================================
	 * LINKS
	 * ===============================================
	 */
	
	@RequestMapping(value="/template/{id}/links", headers = {"Accept=*/*"}, produces="application/json")
	public ResponseEntity<Link[]> template_links(@PathVariable("id") int id){

		HttpStatus httpStatus;
		Link[] links = null;
		try {			
			List<Link> ll = sl.list(id);
			int size = ll.size();
			links = ll.toArray(new Link[size]);
			httpStatus = size < 1 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			Link link = new Link();
			link.setParam(ERROR, e.getMessage());
			links  = new Link[1];
			links[0] = link;
			httpStatus = HttpStatus.PARTIAL_CONTENT;
		}
															
		return new ResponseEntity<Link[]>(links, httpStatus);
	}
	
	@RequestMapping(value="/link/{id}", headers="Accept=*/*", produces = "application/json")
	public ResponseEntity<Link> link(@PathVariable("id") int id){
		
		HttpStatus httpStatus;
		Link link = null;
		try {
			link = sl.findById(id);
			httpStatus = (link.getDatemodified() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			link.setParam(ERROR, e.getMessage());
			httpStatus = HttpStatus.NO_CONTENT; 			
		}
		
		return new ResponseEntity<Link>(link, httpStatus);
	}
	
	@RequestMapping(value="/link/{id}/del", headers="Accept=*/*", produces = "application/json")
	public ResponseEntity<Link> link_delete(@PathVariable("id") int id){		
		
		HttpStatus httpStatus;
		Link link = null;
		try {
			link = sl.delete(id);
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			link = new Link(id);
			link.setParam(ERROR, e.getMessage());
			httpStatus = HttpStatus.NOT_MODIFIED;
		}
		
		return new ResponseEntity<Link>(link, httpStatus);
	}
	
	@RequestMapping(value="/link/save", method=RequestMethod.POST, headers="Accept=*/*", produces = "application/json")
	public ResponseEntity<Link> link_save(@RequestBody Link link){		
		
		HttpStatus httpStatus;
		if(link.getTemplate_id() == null){			
			link.setParam(ERROR, "Не передан обязательный параметр template_id");
			httpStatus = HttpStatus.PARTIAL_CONTENT;			 
		}else{
			try {
				sl.save(link);
				httpStatus = HttpStatus.OK;
			} catch (Exception e) {
				e.printStackTrace();
				link.setParam(ERROR, e.getMessage());
				httpStatus = HttpStatus.PARTIAL_CONTENT;
			}	
		}

		return new ResponseEntity<Link>(link, httpStatus);
	}
}
