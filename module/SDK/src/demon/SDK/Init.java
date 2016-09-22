package demon.SDK;

import demon.SDK.instances.SdkInit;

//import demon.SDK.db.DBConnector;

public class Init {

    public static final String MODULE_NAME = "SDK";

    public static void init(String moduleDir) throws Exception {
//    	DBConnector.init();
    	SdkInit.init();
    }
}
