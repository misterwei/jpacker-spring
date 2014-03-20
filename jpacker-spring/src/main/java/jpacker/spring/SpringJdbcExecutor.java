package jpacker.spring;

import jpacker.impl.DefaultJdbcExecutor;

/**
 * 只能在当前线程中使用，不可传入其他线程中调用。数据库处理类。
 * @author cool
 *
 */
public class SpringJdbcExecutor extends DefaultJdbcExecutor {
	
}
