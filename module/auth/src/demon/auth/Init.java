package demon.auth;

import demon.utils.XProperties;
import demon.utils.unit.TimeUnit;

public class Init {
	
	public static final String MODULE_NAME = "auth";
	
	public static void init(String moduleDir) throws Exception {
		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
		
		AuthConfig.defaultTokenAge = new TimeUnit(properties.getProperty(AuthConfig.CONF_USER_TOKEN_AGE)).value.longValue();
		
	}

}
