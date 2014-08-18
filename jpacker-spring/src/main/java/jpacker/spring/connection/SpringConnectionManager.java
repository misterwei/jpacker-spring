package jpacker.spring.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import jpacker.connection.ConnectionHolder;
import jpacker.connection.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class SpringConnectionManager implements ConnectionManager {
	static Logger log = LoggerFactory.getLogger(SpringConnectionManager.class);
	private volatile Integer global_id = 0;
	
	private DataSource ds;
	public SpringConnectionManager(DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public ConnectionHolder getConnection() throws SQLException {
		Connection conn =  DataSourceUtils.getConnection(ds);
		synchronized (global_id) {
			global_id++;
			if(global_id == Integer.MAX_VALUE){
				global_id = 0;
			}
			return new ConnectionHolder(global_id,conn);
		}
	}

	@Override
	public void releaseConnection(ConnectionHolder holder) {
		log.debug("release connection holder id:{}",holder.getId());
		DataSourceUtils.releaseConnection(holder.getConnection(), ds);
	}

	@Override
	public ConnectionHolder getNewConnection() throws SQLException {
		return getConnection();
	}

}
