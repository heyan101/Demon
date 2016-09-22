package demon.user;

import demon.SDK.SdkCenter;
import demon.SDK.inner.IBeans;
import demon.service.db.MySql;
import demon.utils.XProperties;
import demon.utils.unit.TimeUnit;

public class Init {
	
	public static final String MODULE_NAME = "user";
	
	public static void init(String moduleDir, IBeans beans, XProperties properties) throws Exception {
		MySql mysql = MySql.getInst(MODULE_NAME);
		UserModel userModel = new UserModel(mysql);
		// 设置管理员账号
		userModel.setAdminDefaultInfo();
		UserApi.defaultTokenAge = new TimeUnit(properties.getProperty(UserConfig.CONF_USER_TOKEN_AGE)).value.longValue();
		
		
	    UserApi.init(beans, userModel);
	    UserHttpApi.init(UserApi.getInst());
	    
	    SdkCenter.getInst().registHttpApi(MODULE_NAME, UserHttpApi.getInst());
	}
}
