package jpacker.spring;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import jpacker.JdbcExecutor;
import jpacker.factory.JdbcExecutorFactory;
import jpacker.model.SqlParameters;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;


public class BaseService {
	
	@Resource(name="jdbcExecutorFactory")
	private JdbcExecutorFactory jdbcExecutorFactory;

	@Resource(name="cacheManager")
	private EhCacheCacheManager cacheManager;
	
	
	public EhCacheCacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setJdbcExecutorFactory(JdbcExecutorFactory jdbcFactory) {
		this.jdbcExecutorFactory = jdbcFactory;
	}
	
	public JdbcExecutorFactory getJdbcExecutorFactory(){
		return jdbcExecutorFactory;
	}
	
	public JdbcExecutor getJdbcExecutor(){
		return jdbcExecutorFactory.getJdbcExecutor();
	}
	
	public void beginThreadLocal() throws SQLException{
		jdbcExecutorFactory.beginThreadLocal();
	}
	
	public void endThreadLocal(){
		jdbcExecutorFactory.endThreadLocal();
	}
	
	public <T> Page<T> pageQuery(Class<T> type,String sql,int pageNo,int pageSize, SqlParameters parameters) throws SQLException{
		String countSql = new StringBuilder("select count(*) from (").append(sql).append(") as temp_count_table").toString();
		JdbcExecutor jdbc = getJdbcExecutor();
		try{
			int count = jdbc.queryOne(Integer.class, countSql, parameters);
			int start = Page.getStartOfPage(pageNo, pageSize);
			
			if(count == 0 || count <= start ){
				return Page.EMPTY_PAGE;
			}
			
			List<T> results = jdbc.queryForLimit(type, sql, start, pageSize, parameters);
			return new Page<T>(start,count,pageSize,results);
			
		}finally{
			jdbc.close();
		}
		
	}
	
	public Object getCacheValue(String name,String key){
		Cache cache = cacheManager.getCache(name);
		if(cache == null){
			return null;
		}
		ValueWrapper vWrapper =  cache.get(key);
		if(vWrapper == null){
			return null;
		}
		return vWrapper.get();
	}
	
	public void putCacheValue(String name,String key,Object value){
		Cache cache = cacheManager.getCache(name);
		if(cache == null){
			return;
		}
		cache.put(key, value);
	}
}
