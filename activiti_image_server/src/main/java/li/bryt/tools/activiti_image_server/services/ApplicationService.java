package li.bryt.tools.activiti_image_server.services;

import java.sql.SQLException;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.h2.tools.Server;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;


@IocBean
public class ApplicationService {

	protected final Log LOG;
	public ApplicationService() {
		this.LOG = Logs.get();
	}
	
	private ProcessEngine engine;
	private Server server;

	@Inject("java:$config.get('version')")
	private String version;
	
	@Inject("java:$config.get('buildType')")
	private String buildType;

	@Inject("java:$activiti.get('jdbcDriver')")
	private String jdbcDriver;
	@Inject("java:$activiti.get('jdbcUrl')")
	private String jdbcUrl;
	@Inject("java:$activiti.get('jdbcUsername')")
	private String jdbcUsername;
	@Inject("java:$activiti.get('jdbcPassword')")
	private String jdbcPassword;
	@Inject("java:$activiti.get('databaseSchemaUpdate')")
	private String databaseSchemaUpdate;
	@Inject("java:$activiti.get('asyncExecutorActivate')")
	private boolean asyncExecutorActivate;
	
	public void init(NutConfig config) {
		LOG.info("init");
		
		try {
			this.server = Server.createTcpServer("-tcpPort", "9123", "-tcpPassword", jdbcPassword);
			if(this.server!=null)
				this.server.start();
		} catch (SQLException e) {
			this.server = null;
			LOG.debug(e);
		}
		
		StandaloneProcessEngineConfiguration conf = new StandaloneProcessEngineConfiguration();
		conf.setJdbcDriver(jdbcDriver);
		conf.setJdbcUrl(jdbcUrl);
		conf.setJdbcUsername(jdbcUsername);
		conf.setJdbcPassword(jdbcPassword);
		conf.setDatabaseSchemaUpdate(databaseSchemaUpdate);
		conf.setAsyncExecutorActivate(asyncExecutorActivate);
		this.engine = conf.buildProcessEngine();
	}

	public void destroy(NutConfig config) {
		LOG.info("destroy");
		this.engine.close();
		if(this.server!=null)
			this.server.stop();
	}
	
	public String getVersion() {
		return version + "_" + buildType;
	}
	
	public String getFooUrl() {
		return "http://foo.oriente.com";
	}

	public ProcessEngine getEngine() {
		return engine;
	}
}
