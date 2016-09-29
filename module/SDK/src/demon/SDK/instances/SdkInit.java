package demon.SDK.instances;

import demon.Config;
import demon.SDK.SdkCenter;
import demon.SDK.inner.IBeans;

public class SdkInit {
	public static void init() throws Exception {
		SdkCenter sdk = SdkCenter.getInst();
		IBeans beans = (IBeans) SdkCenter.getInst().queryInterface(IBeans.name, SdkCenter.ToString() + "InnerKey" + Config.ToString());
		
		// 注册对外接口
//		sdk.addInterface(IUser.name, new User(beans));
	}
}
