package jpacker.spring;

import jpacker.JdbcExecutor;
import jpacker.factory.Configuration;
import jpacker.factory.JdbcExecutorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringJdbcExecutorFactory extends JdbcExecutorFactory{
	static Logger log = LoggerFactory.getLogger(SpringJdbcExecutorFactory.class);
	
	public SpringJdbcExecutorFactory(Configuration config){
		super(config);
	}
	
	protected JdbcExecutor newJdbcExecutor(){
		try {
			log.debug("Returns a new SpringJdbcExecutor");
			JdbcExecutor jdbc = new SpringJdbcExecutor();
			
			jdbc.init(cm,config.getTableFactory(),config.getLocalExecutor());
			return jdbc;
		} catch (Exception e) {
			log.error("instance error",e);
		}
		return null;
	}
	
}
