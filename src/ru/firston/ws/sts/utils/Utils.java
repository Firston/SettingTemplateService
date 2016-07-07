package ru.firston.ws.sts.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ru.firston.ms.api.ContentObject;
import ru.firston.ms.api.an.DescriptionField;
import ru.firston.ms.api.an.Ignore;

/**
 * 
 * @author Anton Arefyev
 * @version 16.06.23
 *
 */
public final class Utils {

	/**
	 * ДОБАВИТЬ МЕТОД В ЯДРО ms-core
	 * @param co
	 * @return
	 */
	public static Map<String, Object> convertObjectToMap(ContentObject co){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			String columnName = null;
			Field[] fields = co.getClass().getDeclaredFields();
		    for(Field field : fields){
			  if(!field.isAnnotationPresent(Ignore.class)){
				field.setAccessible(true);
				if(field.isAnnotationPresent(DescriptionField.class))
					columnName = field.getAnnotation(DescriptionField.class).columnName();	
				columnName = (columnName == null || columnName.equals("")) ? field.getName() : columnName;		
				map.put(columnName, field.get(co));
				columnName = null;
			} 	
		  }
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
}
