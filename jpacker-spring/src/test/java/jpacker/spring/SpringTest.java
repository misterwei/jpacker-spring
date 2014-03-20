package jpacker.spring;

import jpacker.local.MSSQL2005Executor;
import junit.framework.TestCase;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SpringTest extends TestCase{
	
	
	public void testSpringFactory() throws Exception{
		ComboPooledDataSource cpds =  new ComboPooledDataSource();
		cpds.setDriverClass("net.sourceforge.jtds.jdbc.Driver");
		cpds.setUser("sa");
		cpds.setPassword("123456");
		cpds.setJdbcUrl("jdbc:jtds:sqlserver://127.0.0.1:1433/ivrdb");
		cpds.setMinPoolSize(20);
		cpds.setMaxPoolSize(100);
		cpds.setMaxStatements(500);
		cpds.setCheckoutTimeout(1800);
		
		JdbcExecutorFactoryBean fb = new JdbcExecutorFactoryBean();
		fb.setDataSource(cpds);
		fb.setDirectoryLocations(new String[]{"classpath:jpacker/"});
		fb.setLocalExecutor(new MSSQL2005Executor());
		
		fb.afterPropertiesSet();
		
	}
}
