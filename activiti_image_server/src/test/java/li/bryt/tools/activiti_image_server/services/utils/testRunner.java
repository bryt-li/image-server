package li.bryt.tools.activiti_image_server.services.utils;

import org.junit.runners.model.InitializationError;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.mock.NutTestRunner;

import li.bryt.tools.activiti_image_server.modules.MainModule;

public class testRunner extends NutTestRunner {

    public testRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    public Class<?> getMainModule() {
        return MainModule.class;
    }
    
    /**
     * can overide createIoc to overwrite test args
     */
    protected Ioc createIoc() {
        Ioc ioc = super.createIoc();
        //PropertiesProxy conf = ioc.get(PropertiesProxy.class, "conf");
        //conf.put("db.url", "jdbc:h2:~/test");
        return ioc;
    }
}