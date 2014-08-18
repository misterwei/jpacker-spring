package jpacker.spring;

import javax.annotation.Resource;

import jpacker.spring.test.model.TestModel;
import jpacker.spring.test.service.TestService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration({"classpath*:application-*.xml","classpath*:spring/application-*.xml"})
public class SpringTest extends AbstractJUnit4SpringContextTests{
	
	@Resource(name="testService")
	private TestService test;
	
	@Test
	public void testSpringFactory() throws Exception{
		test.save("abc", "def");
	}
	
	@Test
	public void testGet() throws Exception{
		TestModel m = test.get(2);
		m.getTestArray();
	}
}
