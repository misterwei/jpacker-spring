package jpacker.spring.test.service;

import java.sql.SQLException;

import jpacker.Jpacker;
import jpacker.spring.test.model.TestModel;

public class TestService {
	private Jpacker jpacker;
	
	public void setJpacker(Jpacker jp){
		this.jpacker = jp;
	}
	
	public void update() throws SQLException{
		TestModel test = new TestModel();
		test.setName("aaa");
		test.setPassword("ccc");
		
		jpacker.save(test);
		
	}
	
	public void save(String a,String b) throws SQLException{
		TestModel test = new TestModel();
		test.setName(a);
		test.setPassword(b);
		
		jpacker.save(test);
		
	}
	
	public TestModel get(int id) throws SQLException{
		return jpacker.get(TestModel.class, id);
	}
}
