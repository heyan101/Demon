package dmodule.user;

import dmodule.SDK.SdkCenter;
import dmodule.SDK.inner.IBeans;
import dmodule.service.db.MySql;

public class Init {
	
	public static final String MODULE_NAME = "user";
	
	public static void init(String moduleDir) throws Exception {
//	    XProperties properties = new XProperties(MODULE_NAME, moduleDir);
	    
		MySql mysql = MySql.getInst(MODULE_NAME);
		UserModel userModel = new UserModel(mysql);
		
		// 设置管理员账号
//		userModel.setAdminDefaultInfo(properties);
		
		IBeans beans = (IBeans) SdkCenter.getInst().queryInterface(IBeans.name, "demon1.0InnerKeyP@ssw0rd");
		
	    UserApi.init(beans, userModel);
	    UserHttpApi.init(UserApi.getInst());
	    
	    SdkCenter.getInst().registHttpApi(MODULE_NAME, UserHttpApi.getInst());
	}
}
