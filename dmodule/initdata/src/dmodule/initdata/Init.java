package dmodule.initdata;

import dmodule.SDK.SdkCenter;
import dmodule.SDK.inner.IBeans;
import demon.service.http.Env;
import demon.utils.XProperties;

public class Init {
	
	public static final String MODULE_NAME = "initdata";
	
	public static void init(String moduleDir) throws Exception {
		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
		
		Env env = new Env(Init.MODULE_NAME);
		IBeans beans = (IBeans) SdkCenter.getInst().queryInterface(IBeans.name, "demon1.0InnerKeyP@ssw0rd");
		
		// 初始化用户
		InitUser.initUser(env, properties, beans);
		// 初始化商品分类
		InitClassed.initClassed(env, properties, beans, moduleDir);
	}
    
	
}
