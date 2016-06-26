package ru.firston.ws.sts.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import ru.firston.ms.api.an.DescriptionField;
import ru.firston.ms.api.an.DescriptionObject;
import ru.firston.ms.api.an.Ignore;
import ru.firston.ms.core.ContentObject_rev160409;
import ru.firston.ms.core.jdbc.BuilderSQLQuery;
import ru.firston.ws.sts.utils.Utils;

/**
 * 
 * @author Anton Arefyev
 * @version 16.04.23 pre 16.04.20
 *
 */

@DescriptionObject(tableName="templates")
public class Template implements ContentObject_rev160409{
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private String path;
	
	@DescriptionField(columnName="codeinterface")
	private Integer code_interface;
	
	@Ignore
	BuilderSQLQuery qh;

	public Template(){		
		qh = BuilderSQLQuery.builder(this);
		params = new HashMap<String, Object>();
	}

	public Template(int id){
		
		this.id = id;
		qh = BuilderSQLQuery.builder(this);
		params = new HashMap<String, Object>();
	}		

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public String getQueryForDelete() {

		return "DELETE FROM templates WHERE id=" + id;
	}

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public String getQueryForSelect() {
		
		return new StringBuffer().append("SELECT ")
		 						 .append(qh.getPartSelect())
		 						 .append(" FROM templates ")
		 						 .append(qh.getPartWhereForSelect())
		 						 .append(" ORDER BY id")
		 						 .toString();
	}

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public String getQueryForUpdate() {
		/*
		 * Обновление данных по идентификатору объекта
		 */
		return new StringBuffer().append("UPDATE templates SET ")
								 .append(qh.getPartUpdate())
								 .append(" WHERE id=")
								 .append(id)
								 .toString();
	}

	@Override
	public Object getParam(String key) {

		if(key.equals("tableName"))
			return getClass().getAnnotation(DescriptionObject.class).tableName();
		
		return params.get(key);
	}
	
	@Override
	public void setParam(String key, Object value) {
		
		params.put(key, value);
	}

	@com.fasterxml.jackson.annotation.JsonIgnore
	@Override
	public MapSqlParameterSource getQueryForInsert() {
				
		return new MapSqlParameterSource(Utils.convertObjectToMap(this));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getCode_interface() {
		return code_interface;
	}

	public void setCode_interface(Integer code_interface) {
		this.code_interface = code_interface;
	}
	
	@Ignore
	private Map<String, Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public boolean isEmpty(){
		
		return (id == null && name == null && 
				description == null && path == null && 
				code_interface == null && params.size() == 0);
	}
}
