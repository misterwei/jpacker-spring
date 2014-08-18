package jpacker.spring.factory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import jpacker.factory.Configuration;
import jpacker.factory.JpackerFactory;
import jpacker.local.LocalExecutor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

public class JpackerFactoryBean implements FactoryBean<JpackerFactory> , InitializingBean{

	private String[] directoryLocations;
	
	private JpackerFactory factory;
	
	private DataSource dataSource;
	
	private LocalExecutor localExecutor;
	
	@Override
	public JpackerFactory getObject() throws Exception {
		return factory;
	}

	@Override
	public Class<JpackerFactory> getObjectType() {
		return JpackerFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(directoryLocations, "directoryLocations can not be null");
		Assert.notNull(dataSource, "dataSource can not be null");
		Assert.notNull(localExecutor, "localExecutor can not be null");
		
		Thread currentThread = Thread.currentThread();
		ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
		
		List<Class<?>> clazzs = new ArrayList<Class<?>>();
		
		for(String modelpath : directoryLocations){
			modelpath = modelpath.replace("classpath:", "");
			ClassPathResource cpr = new ClassPathResource(modelpath,threadContextClassLoader);
			if(cpr.exists()){
				File path = cpr.getFile();
				if(!path.isDirectory()){
					throw new Exception("model directory location [" + modelpath + "] does not denote a directory");
				}
				String classpath = "."+modelpath.replace("/", ".").replaceFirst("\\.$", "")+".";
				File[] fs = path.listFiles();
				if(fs != null){
					for(File f : fs){
						if(f.isFile()){
							String name = f.getAbsolutePath().replaceAll("/|\\\\", ".").replaceFirst("\\.class$", "");
							int index = name.indexOf(classpath);
							name = name.substring(index).replaceFirst("^\\.", "");
							
							clazzs.add(threadContextClassLoader.loadClass(name));
							
						}
					}
				}
			}
		}
		
		Configuration cfg = new Configuration(dataSource, clazzs, localExecutor);
		factory = new SpringJpackerFactory(cfg);
	}

	public void setDirectoryLocations(String[] res){
		this.directoryLocations = res;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setLocalExecutor(LocalExecutor localExecutor) {
		this.localExecutor = localExecutor;
	}
	
	
	
}
