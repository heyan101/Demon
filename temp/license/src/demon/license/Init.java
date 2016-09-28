package demon.license;

import demon.SDK.SdkCenter;
import demon.service.http.Env;

public class Init {
    public static final String MODULE_NAME = "license";
    public static void init(String moduleDir) throws Exception {
        SdkCenter.getInst().registHttpApi(MODULE_NAME, new LicenseHttpApi());
        
        // 初始化license权限
        initRight();

    }
    
    public static void initRight() throws Exception {
        Env env = new Env(MODULE_NAME);
                
    }
}
