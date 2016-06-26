package ru.firston.ws.sts.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import ru.firston.ms.api.an.DescriptionObject;
import ru.firston.ms.api.an.Ignore;
import ru.firston.ms.core.ContentObject_rev160409;
import ru.firston.ms.core.jdbc.BuilderSQLQuery;
import ru.firston.ws.sts.utils.Utils;

/**
 * 
 * @author Anton Arefyev
 * @version 16.06.23 pre 16.04.20
 *
 */
@DescriptionObject(tableName="links")
public class Link implements ContentObject_rev160409{

	private Integer id;
	
	private Integer template_id;
	
	private String procedure_id;
	
	private Integer organization_id;
	
	private String task_id;
	
	private Date datemodified;
	
	@Ignore
	BuilderSQLQuery qh;
	
	public Link() {
		
		qh = BuilderSQLQuery.builder(this);
		params = new HashMap<String, Object>();
	}

	public Link(Integer id) {
		
		this.id = id;
		qh = BuilderSQLQuery.builder(this);
		params = new HashMap<String, Object>();
	}

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public String getQueryForDelete() {

		return "DELETE FROM links WHERE id=" + id;
	}

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public String getQueryForSelect() {
		
		return new StringBuffer().append("SELECT ")
								 .append(qh.getPartSelect())
								 .append(" FROM links ")
								 .append(qh.getPartWhereForSelect())
								 .append(" ORDER BY id")
								 .toString();
	}

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public String getQueryForUpdate() {

		return new StringBuffer().append("UPDATE links SET ")
								 .append(qh.getPartUpdate())
								 .append(" WHERE id=")
								 .append(id)
								 .toString();
	}

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public MapSqlParameterSource getQueryForInsert() {

		this.datemodified = new Date();
		
		return new MapSqlParameterSource(Utils.convertObjectToMap(this));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}

	public String getProcedure_id() {
		return procedure_id;
	}

	public void setProcedure_id(String procedure_id) {
		this.procedure_id = procedure_id;
	}

	public Integer getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(Integer organization_id) {
		this.organization_id = organization_id;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public Date getDatemodified() {
		return datemodified;
	}
	
	@Override
	public void setParam(String key, Object value) {

		params.put(key, value);
	}	
	
	@Override
	public Object getParam(String key) {
		
		if(key.equals("tableName"))
			return getClass().getAnnotation(DescriptionObject.class).tableName();
		
		return params.get(key);
	}

	@Ignore
	private Map<String, Object> params;
	
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
}
