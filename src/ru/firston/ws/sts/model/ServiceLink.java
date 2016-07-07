package ru.firston.ws.sts.model;

import java.util.List;

/**
 * 
 * @author Anton Arefyev
 * @version 16.06.23
 *
 */
public interface ServiceLink {
	
	public List<Link> list(int template_id) throws Exception;
	
	public Link findById(int id) throws Exception;
	
	public Link delete(int id) throws Exception;
	
	public Link save(Link link) throws Exception;
}
