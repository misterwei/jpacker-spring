package jpacker.spring.suport;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import jpacker.Jpacker;
import jpacker.model.SqlParameters;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;


public class BaseService {
	
	@Resource(name="cacheManager")
	private EhCacheCacheManager cacheManager;
	
	@Resource(name="jpacker")
	private Jpacker jpacker;
	
	public EhCacheCacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public Jpacker getJpacker(){
		return jpacker;
	}
	
	public void setJpacker(Jpacker jpacker){
		this.jpacker = jpacker;
	}
	
	public <T> Page<T> pageQuery(Class<T> type,String sql,int pageNo,int pageSize, SqlParameters parameters) throws SQLException{
		String removeOrderbySql = removeOrders(sql);
		String countSql = new StringBuilder("select count(*) from (").append(removeOrderbySql).append(") as temp_count_table").toString();
		Jpacker jdbc = getJpacker();

		int count = jdbc.queryOne(Integer.class, countSql, parameters);
		int start = Page.getStartOfPage(pageNo, pageSize);
		
		if(count == 0 || count <= start ){
			return new Page<T>();
		}
		
		List<T> results = jdbc.queryForLimit(type, sql, start, pageSize, parameters);
		return new Page<T>(start,count,pageSize,results);
	}
	
	private static String removeOrders(String sql) {
      Pattern p = Pattern.compile("order\\s+by(\\s*\\,?\\s*[\\w\\._-]+\\s*(desc|asc)?)+\\s*", 
          Pattern.CASE_INSENSITIVE);
      Matcher m = p.matcher(sql);
      StringBuffer sb = new StringBuffer();
      while (m.find()) {
          m.appendReplacement(sb, "");
      }
      m.appendTail(sb);
      return sb.toString();
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
