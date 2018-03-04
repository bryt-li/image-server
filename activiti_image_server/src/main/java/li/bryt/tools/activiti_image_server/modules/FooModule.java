package li.bryt.tools.activiti_image_server.modules;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import li.bryt.tools.activiti_image_server.modules.utils.baseModule;
import li.bryt.tools.activiti_image_server.services.ApplicationService;

@At("/foo")
@Ok("json")
@Fail("http:500")
@IocBean
public class FooModule extends baseModule {

	protected final Log LOG = Logs.getLog(this.getClass());

	@Inject
	private ApplicationService appService;

	@At
	public Object test() {
		NutMap result = new NutMap();
		result.setv("url", appService.getFooUrl());
		return ok(result);
	}}
