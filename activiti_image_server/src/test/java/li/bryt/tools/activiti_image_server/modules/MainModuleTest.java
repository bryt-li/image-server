package li.bryt.tools.activiti_image_server.modules;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nutz.json.Json;

import li.bryt.tools.activiti_image_server.modules.utils.MockClient;
import li.bryt.tools.activiti_image_server.modules.utils.RESTfulResponse;

public class MainModuleTest extends Assert {
	private static ServletTester server;
	private static MockClient client;

	@BeforeClass
	public static void initClass() throws Exception {
		server = new ServletTester();
		server.setContextPath("/");

		// enabled nutz
		FilterHolder filter = server.addFilter(org.nutz.mvc.NutFilter.class, "/*",
				EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST));
		
		filter.setInitParameter("modules", "li.bryt.tools.activiti_image_server.modules.MainModule");
		server.addServlet(org.nutz.mvc.NutServlet.class, "/");

		// start
		server.start();
		System.out.println("test server started.");

		// init client
		client = new MockClient(server, "/");
	}

	@AfterClass
	public static void destroyClass() throws Exception {
		server.stop();
		System.out.println("test server stopped.");
	}
	
	@Test
	public void testVersion() throws Exception {
		HttpTester response = client.get("/version", "");
		assertEquals(200, response.getStatus());
		
		RESTfulResponse resp = Json.fromJson(RESTfulResponse.class, response.getContent());
		assert(resp.isOk());
		assertEquals("0.0.1_test",resp.getPayload().get("version"));
	}
}
