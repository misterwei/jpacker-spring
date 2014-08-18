package jpacker.spring.factory;

import java.sql.SQLException;

import jpacker.Jpacker;
import jpacker.factory.Configuration;
import jpacker.factory.JpackerFactory;
import jpacker.spring.SpringJpacker;
import jpacker.spring.connection.SpringConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringJpackerFactory implements JpackerFactory{
	static Logger log = LoggerFactory.getLogger(SpringJpackerFactory.class);

	protected SpringConnectionManager cm;
	protected Configuration config; 
	
	public SpringJpackerFactory(Configuration config){
		this.config = config;
		this.cm = new SpringConnectionManager(config.getDataSource());
		this.config.initLocalExecutor(cm);
	}
	
	@Override
	public Jpacker getJpacker() {
		return newJpacker();
	}

	protected Jpacker newJpacker(){
		try {
			log.debug("Returns a new SpringJpacker");
			return new SpringJpacker(config,cm);
		} catch (Exception e) {
			log.error("instance error",e);
		}
		return null;
	}
	
	@Override
	public void beginThreadLocal() throws SQLException {
		throw new RuntimeException("Unsupport beginThreadLocal");
	}

	@Override
	public void endThreadLocal() {
		throw new RuntimeException("Unsupport endThreadLocal");
	}

}
