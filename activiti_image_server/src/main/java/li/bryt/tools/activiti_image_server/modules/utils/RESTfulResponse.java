package li.bryt.tools.activiti_image_server.modules.utils;

import org.nutz.lang.util.NutMap;

public class RESTfulResponse {
	private boolean ok;
	private NutMap payload;
	
	public RESTfulResponse() {
	}
		
	public RESTfulResponse(boolean ok, NutMap payload) {
		this.ok = ok;
		this.payload = payload;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public NutMap getPayload() {
		return payload;
	}

	public void setPayload(NutMap payload) {
		this.payload = payload;
	}
}
