package ru.firston.ws.sts;

import java.util.LinkedList;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.firston.ms.api.jdbc.PoolFactory;
import ru.firston.ms.core.ex.SpringException;
import ru.firston.ms.core.jdbc.SpringConnectionDAO;
import ru.firston.ms.core.utils.Logger;

/**
 * 
 * @author Anton Arefyev
 * Connection to db for Service Report
 * @version 16.05.15
 *
 */
public class ConnectionSTS  extends PoolFactory implements SpringConnectionDAO{
		
	/**
	 * Описание параметров соединения	
	 */
	private static final String NAME_DS = "ds";
	/**
	 * Количество соединений с БД
	 * Вынести в конфиг 
	 */
	private static final int countConnection = 5;
	/**
	 * Лимит попыток установить соединение при  отсутсвии
	 * Вынести в конфиг
	 */
	private static final int limitGetConnection = 10;
	/**
	 * Текущая попытка установить соединение
	 */
	private int attemptConnection;

	private DataSource ds;
	
	private static LinkedList<JdbcTemplate> pool;
	
	private ConnectionSTS() {}
		
	@Override
	public void returnConnection(Object o) {

		if(pool.size() < countConnection)
		  pool.add((JdbcTemplate) o);
		Logger.addLog("Return Connection : " + o);		
	}
	
	@Override
	public void setDataSource(DataSource ds) {		
		
		this.ds = ds;
	}

	@Override
	public JdbcTemplate getConnection() {

		JdbcTemplate jdbcTemplate = getJdbcTemplate_1(1);
		Logger.addLog("Get connection : " + jdbcTemplate);
		if(checkConnection(jdbcTemplate))
		  return jdbcTemplate;
		else return getConnection();
	}

	@Override
	protected SpringConnectionDAO initPool() {
		
		if(pool == null)
			pool = new LinkedList<JdbcTemplate>();

		if(pool.isEmpty()){
			  synchronized (ConnectionSTS.class) {
				if(pool.isEmpty()){					
				  this.ds = (DataSource) ControllerSTS.context.getBean(NAME_DS);
					for(int i = 0; i < countConnection; i++)
					  pool.addLast(new JdbcTemplate(this.ds));	
				}
			}
		}
		return this;
	}


	private JdbcTemplate getJdbcTemplate_1(int i){
		
		if(pool.size() < 1){
			/*
			 * Количество попыток получить соединение
			 */
			if(i <= countConnection){
			  try {
				Thread.sleep(2000);
			  } catch (InterruptedException e) {
				Logger.addLog("ERROR. Ошибка получения соединения с БД : " + e.getMessage());
			  }
			  return getJdbcTemplate_1(++i);
			}else{
			  Logger.addLog("TIME OUT. Превышен лимит времени");	
			  return null;
			}
		}
		return pool.removeFirst();
	}
	
	private boolean checkConnection(JdbcTemplate jdbcTemplate) {
		
		try{
			jdbcTemplate.queryForObject("SELECT 1", Integer.class);
			Logger.addLog("Connection is success");
			attemptConnection = 1;
		}catch(CannotGetJdbcConnectionException cgce){
			Logger.addLog("Connection is refuse");
			if(attemptConnection <= limitGetConnection){
				attemptConnection++;
				createNewConnection();	
			} else{
				attemptConnection = 1;
				Logger.addLog("Превышен лимит попыток установить соединение с БД.");
				throw new SpringException();
			}
			return false;			
		}		
		return true;
	}
	
	private void createNewConnection(){
				
		if(pool.size() < countConnection){
			Logger.addLog("Create new connection");
			pool.addLast(new JdbcTemplate(this.ds));
			createNewConnection();
		}		
	}

}
