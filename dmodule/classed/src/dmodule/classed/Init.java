package dmodule.classed;

import demon.service.db.MySql;
import dmodule.SDK.SdkCenter;
import dmodule.SDK.inner.IBeans;

public class Init {
	
	public static final String MODULE_NAME = "classed";
	
	public static void init(String moduleDir) throws Exception {
//		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
		
		IBeans beans = (IBeans) SdkCenter.getInst().queryInterface(IBeans.name, "demon1.0InnerKeyP@ssw0rd");
		MySql mysql = MySql.getInst(MODULE_NAME);
		ClassedModel model = new ClassedModel(mysql);
		ClassedApi.init(beans, model);
	}

}
