package li.bryt.tools.image_server.modules;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
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

import li.bryt.tools.image_server.modules.utils.baseModule;
import li.bryt.tools.image_server.services.ImageService;

@At("/")
@IocBean
@Ok("json")
@Fail("http:500")
public class ImageModule extends baseModule {

	@Inject
	private ImageService imageService;

	@At("/client")
	@Ok("raw:js")
	public String client() {
		return imageService.getClientJs();
	}
	
	@At("/url")
	@Ok("raw:png")
	public Object url(String src) {
		URL resource;
		try {
			resource = new URL(src);
			String text = this.getSource(resource);
			return imageService.getImageFromText(text);
		} catch (Exception e) {
			LOG.debug(e);
			return null;
		}
	}

	@At("/text")
	@POST
	@AdaptBy(type = VoidAdaptor.class)
	public Object text(HttpServletRequest req, HttpServletResponse resp) {
		try {
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int reads = req.getInputStream().read();

			while (reads != -1) {
				baos.write(reads);
				reads = req.getInputStream().read();
			}
			String text = baos.toString();
			String url = imageService.getImageUrlFromText(text);
			NutMap obj = new NutMap();
			obj.setv("url", url);
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

	private String getSource(final URL url) throws IOException {
		String line;
		BufferedReader rd;
		StringBuilder sb;
		try {
			HttpURLConnection con = getConnection(url);
			rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}
			rd.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			rd = null;
		}
		return "";
	}

	private HttpURLConnection getConnection(final URL url) throws IOException {
		if (url.getProtocol().startsWith("https")) {
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setReadTimeout(10000); // 10 seconds
			// printHttpsCert(con);
			con.connect();
			return con;
		} else {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setReadTimeout(10000); // 10 seconds
			con.connect();
			return con;
		}
	}
}
