package demon.goods;

import demon.utils.XProperties;

public class Init {
	
	public static final String MODULE_NAME = "goods";
	
	public static void init(String moduleDir) throws Exception {
		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
		
		GoodsModel model = new GoodsModel();
		
	}

}
