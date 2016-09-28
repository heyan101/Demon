package demon.SDK;

import demon.SDK.db.DBConnector;
import demon.SDK.instances.SdkInit;

public class Init {

    public static final String MODULE_NAME = "SDK";

    public static void init(String moduleDir) throws Exception {
    	DBConnector.init();
    	SdkInit.init();
    }
}
