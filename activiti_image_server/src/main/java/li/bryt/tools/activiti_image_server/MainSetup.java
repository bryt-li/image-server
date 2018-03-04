package li.bryt.tools.activiti_image_server;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import li.bryt.tools.activiti_image_server.services.ApplicationService;

@IocBean
public class MainSetup implements Setup {
	
	@Inject
	private ApplicationService app;
	
	public void init(NutConfig config) {
		app.init(config);
	}

	public void destroy(NutConfig config) {
		app.destroy(config);
	}
}
