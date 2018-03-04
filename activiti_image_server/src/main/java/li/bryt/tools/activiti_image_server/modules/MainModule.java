package li.bryt.tools.activiti_image_server.modules;

import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.mvc.view.DefaultViewMaker;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;

import li.bryt.tools.activiti_image_server.MainSetup;
import li.bryt.tools.activiti_image_server.services.ApplicationService;
import li.bryt.tools.activiti_image_server.modules.utils.baseModule;

@SetupBy(value = MainSetup.class, args = "ioc:mainSetup")
@At("/")
@Ok("json")
@Fail("http:500")
@IocBy(type = ComboIocProvider.class,args={
		"*org.nutz.ioc.loader.json.JsonLoader",
		"*js",
		"ioc.js",
		"*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
		"li.bryt.tools.activiti_image_server"
        })
@Modules(scanPackage = true)
@Encoding(input = "UTF-8", output = "UTF-8")
@IocBean
public class MainModule extends baseModule {

	@Inject
	private ApplicationService appService;

	@At
	public Object version() {
		String version = appService.getVersion();
		return ok(new NutMap().setv("version", version));
	}
}
