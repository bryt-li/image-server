package li.bryt.tools.activiti_image_server.modules;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.adaptor.VoidAdaptor;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;

import li.bryt.tools.activiti_image_server.modules.utils.baseModule;
import li.bryt.tools.activiti_image_server.services.ImageService;

@At("/")
@IocBean
@Ok("json")
@Fail("http:500")
public class ImageModule extends baseModule {

	@Inject
	private ImageService imageService;

	@At("/url")
	@Ok("raw:png")
	public Object url(String src) throws Exception {
		return imageService.deployFromUrlReturnImage(src);
	}

	@At("/text")
	@POST
	@AdaptBy(type=VoidAdaptor.class)
	public Object text(HttpServletRequest req,HttpServletResponse resp) {		
		try {
			resp.setHeader("Access-Control-Allow-Origin", "*");
		    resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		    
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int reads = req.getInputStream().read();
	
			while (reads != -1) {
				baos.write(reads);
				reads = req.getInputStream().read();
			}
			String xml = baos.toString();
			String hash = imageService.getActivitiPngHash(xml);
			NutMap obj = new NutMap();
			obj.setv("hash", hash);
			return ok(obj);
		} catch (Exception e) {
			return err(e);
		}
	}
	
	@At("/png/?")
	@GET
	@Ok("raw:png")
	public byte[] png(String hash) {
		try {
			return imageService.getHashPng(hash);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
	}
}
