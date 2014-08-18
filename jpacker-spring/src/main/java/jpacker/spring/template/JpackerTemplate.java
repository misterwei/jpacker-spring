package jpacker.spring.template;

import java.io.Serializable;
import java.util.List;

import jpacker.Jpacker;
import jpacker.ResultSetHandler;
import jpacker.exception.JpackerException;
import jpacker.factory.JpackerFactory;
import jpacker.model.SqlParameters;

public class JpackerTemplate implements Jpacker {

	private Jpacker jpacker;
	public void setJpackerFactory(JpackerFactory factory){
		this.jpacker = factory.getJpacker();
	}
	
	@Override
	public <T> T queryOne(Class<T> target, String sql,
			SqlParameters... parameters) throws JpackerException {
		return jpacker.queryOne(target, sql, parameters);
	}

	@Override
	public <T> List<T> queryForList(Class<T> target, String sql,
			SqlParameters... parameters) throws JpackerException {
		return jpacker.queryForList(target, sql, parameters);
	}

	@Override
	public <T> List<T> queryForLimit(Class<T> target, String sql, int start,
			int rows, SqlParameters... parameters) throws JpackerException {
		return jpacker.queryForLimit(target, sql, start, rows, parameters);
	}

	@Override
	public <T> T queryForObject(ResultSetHandler<T> handler, String sql,
			SqlParameters... parameters) throws JpackerException {
		return jpacker.queryForObject(handler, sql, parameters);
	}

	@Override
	public <T> List<T> queryForLimit(ResultSetHandler<List<T>> handler,
			String sql, int start, int rows, SqlParameters... parameters)
			throws JpackerException {
		return jpacker.queryForLimit(handler, sql, start, rows, parameters);
	}

	@Override
	public int execute(String sql, SqlParameters... parameters)
			throws JpackerException {
		return jpacker.execute(sql, parameters);
	}

	@Override
	public int save(Object obj) throws JpackerException {
		return jpacker.save(obj);
	}

	@Override
	public int update(Object obj) throws JpackerException {
		return jpacker.update(obj);
	}

	@Override
	public int delete(Class<?> type, Serializable id) throws JpackerException {
		return jpacker.delete(type, id);
	}

	@Override
	public <T> T get(Class<T> type, Serializable id) throws JpackerException {
		return jpacker.get(type, id);
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
