package ru.firston.ws.sts;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.firston.ms.core.jdbc.JdbcHandler;
import ru.firston.ws.sts.model.Link;
import ru.firston.ws.sts.model.ServiceLink;

/**
 * 
 * @author Anton Arefyev
 * @version 16.06.24
 *
 */
@Service
public class ServiceLinkImpl implements ServiceLink{

	@Override
	public List<Link> list(int template_id) throws Exception {
		
		Link link = new Link();
		link.setTemplate_id(template_id);
		return JdbcHandler.builder(ConnectionSTS.class, link).executeSelect();
	}

	@Override
	public Link findById(int id) throws Exception {

		return JdbcHandler.builder(ConnectionSTS.class, new Link()).get(id);
	}

	@Override
	public Link delete(int id) throws Exception {
		
		return (Link) JdbcHandler.builder(ConnectionSTS.class, new Link(id)).executeDelete();
	}

	@Override
	public Link save(Link link) throws Exception {
		
		JdbcHandler jh = JdbcHandler.builder(ConnectionSTS.class, link);
		
		return (Link) ((link.getId() == null) ? jh.executeInsert() : jh.executeUpdate());
	}

}
