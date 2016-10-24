package dmodule.SDK;

import dmodule.SDK.db.DBConnector;
import dmodule.SDK.instances.SdkInit;

public class Init {

    public static final String MODULE_NAME = "SDK";

    public static void init(String moduleDir) throws Exception {
    	DBConnector.init();
    	SdkInit.init();
    }
}
