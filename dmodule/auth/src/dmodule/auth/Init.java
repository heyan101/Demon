package dmodule.auth;

import dmodule.SDK.SdkCenter;
import dmodule.SDK.http.AuthedJsonProtocol;
import dmodule.SDK.inner.IBeans;
import demon.service.db.MySql;
import demon.utils.XProperties;
import demon.utils.unit.TimeUnit;

public class Init {
	
	public static final String MODULE_NAME = "auth";
	
	public static void init(String moduleDir) throws Exception {
		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
		
		AuthConfig.defaultTokenAge = new TimeUnit(properties.getProperty(AuthConfig.CONF_USER_TOKEN_AGE)).value.longValue();
		
		IBeans beans = (IBeans) SdkCenter.getInst().queryInterface(IBeans.name, "demon1.0InnerKeyP@ssw0rd");
		MySql mysql = MySql.getInst(MODULE_NAME);
		
		AuthModel authModel = new AuthModel(mysql);
		AuthApi.init(beans, authModel);
		AuthHttpApi.init(AuthApi.getInst());
		
		SdkCenter.getInst().registHttpApi(MODULE_NAME, AuthHttpApi.getInst());
		
		AuthedJsonProtocol.init();
	}
}
