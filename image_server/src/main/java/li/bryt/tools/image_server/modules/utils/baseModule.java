package li.bryt.tools.image_server.modules.utils;

import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public abstract class baseModule {

	protected final Log LOG = Logs.getLog(this.getClass());

	protected RESTfulResponse ok(NutMap data) {
		LOG.info(data);
		return new RESTfulResponse(true, data);
	}
		
	protected RESTfulResponse err(String errmsg){
		LOG.info(errmsg);
		return new RESTfulResponse(false, new NutMap().setv("error", errmsg));
	}
	
	protected RESTfulResponse err(Exception e){
		LOG.info(e);
		return new RESTfulResponse(false, new NutMap().setv("error", e.getMessage()));
	}

}
