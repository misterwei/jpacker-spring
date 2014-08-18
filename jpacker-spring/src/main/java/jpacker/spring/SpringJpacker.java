package jpacker.spring;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import jpacker.Jpacker;
import jpacker.ResultSetHandler;
import jpacker.connection.ConnectionHolder;
import jpacker.connection.ConnectionManager;
import jpacker.exception.JpackerException;
import jpacker.factory.Configuration;
import jpacker.factory.TableFactory;
import jpacker.local.DeleteContext;
import jpacker.local.HandlerContext;
import jpacker.local.InsertContext;
import jpacker.local.LocalExecutor;
import jpacker.local.SelectContext;
import jpacker.local.SelectListContext;
import jpacker.local.UpdateContext;
import jpacker.model.SqlParameters;
import jpacker.model.TableModel;

/**
 * 只能在当前线程中使用，不可传入其他线程中调用。数据库处理类。
 * @author cool
 *
 */
public class SpringJpacker  implements Jpacker{
	private LocalExecutor localExecutor = null;
	private TableFactory tableFactory = null;
	private ConnectionManager connManager = null;
	
	public SpringJpacker(Configuration config,ConnectionManager cm) throws SQLException{
		this.connManager = cm;
		this.localExecutor = config.getLocalExecutor();
		this.tableFactory = config.getTableFactory();
	}
	
	private Object[] getParameters(SqlParameters parameters[]){
		if(parameters == null || parameters.length == 0){
			return null;
		}
		
		SqlParameters first = parameters[0];
		if(first == null)
			return null;
		
		for(int i=1;i<parameters.length;i++){
			first.add(parameters[i]);
		}
		
		return first.getArray();
	}
	
	@Override
	public <T> T queryOne(Class<T> target, String sql,
			SqlParameters... parameters) throws JpackerException {
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			Object[] array = getParameters(parameters);
			return localExecutor.selectOne(new SelectContext<T>(target, sql, array),holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public <T> List<T> queryForList(Class<T> target, String sql,
			SqlParameters... parameters) throws JpackerException {
		
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			Object[] array = getParameters(parameters);
			return localExecutor.selectList( new SelectListContext<T>(target, sql,array),holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public <T> List<T> queryForLimit(Class<T> target, String sql, int start,
			int rows, SqlParameters... parameters) throws JpackerException {
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			Object[] array = getParameters(parameters);
			return localExecutor.selectList( new SelectListContext<T>(target, sql,start,rows,array),holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public <T> T queryForObject(ResultSetHandler<T> handler, String sql,
			SqlParameters... parameters) throws JpackerException {
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			Object[] array = getParameters(parameters);
			return localExecutor.selectHandler(new HandlerContext<T>(handler,sql,array), holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public <T> List<T> queryForLimit(ResultSetHandler<List<T>> handler,
			String sql, int start, int rows, SqlParameters... parameters)
			throws JpackerException {
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			Object[] array = getParameters(parameters);
			return localExecutor.selectHandler(new HandlerContext<List<T>>(handler, sql, start, rows, array), holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public int execute(String sql, SqlParameters... parameters)
			throws JpackerException {
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			Object[] array = getParameters(parameters);
			return localExecutor.update( new UpdateContext(sql, array),holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public int save(Object obj) throws JpackerException {
		TableModel tableModel = tableFactory.get(obj.getClass());
		ConnectionHolder holder = null;
		try {
			holder = connManager.getConnection();
			return localExecutor.insert( new InsertContext(tableModel, obj),holder);
		} catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public int update(Object obj) throws JpackerException {
		TableModel tableModel = tableFactory.get(obj.getClass());
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			return localExecutor.update( new UpdateContext(tableModel, obj),holder);
		} catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public int delete(Class<?> type, Serializable id) throws JpackerException {
		TableModel tableModel = tableFactory.get(type);
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			return localExecutor.delete( new DeleteContext(tableModel, id),holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public <T> T get(Class<T> type, Serializable id) throws JpackerException {
		ConnectionHolder holder = null;
		try{
			holder = connManager.getConnection();
			TableModel tableModel = tableFactory.get(type);
			return localExecutor.selectOne( new SelectContext<T>(tableModel, id),holder);
		}catch(Exception e){
			throw new JpackerException(e);
		}finally{
			if(holder != null)
				connManager.releaseConnection(holder);
		}
	}

	@Override
	public void commit() throws JpackerException {
		
	}

	@Override
	public void begin() throws JpackerException {
		
	}

	@Override
	public void rollback() {
		
	}

	@Override
	public void close() {
		
	}

	
	
	
}
