package li.bryt.tools.image_server.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import li.bryt.tools.image_server.services.ApplicationService;
import li.bryt.tools.image_server.services.utils.testRunner;

@RunWith(testRunner.class)
@IocBean 
public class ApplicationServiceTest extends Assert {

	@Inject
	private ApplicationService appService;
	
	@Test
	public void testFoo() {
		assertEquals(appService.getFooUrl(), "http://foo.oriente.com");
	}
	
	//@Test
	public void test() {
		fail("Not yet implemented");
	}
}
