package dmodule.goods;

import demon.service.db.MySql;

public class Init {
	
	public static final String MODULE_NAME = "goods";
	
	public static void init(String moduleDir) throws Exception {
//		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
//		
		MySql mysql = MySql.getInst(MODULE_NAME);
		GoodsModel model = new GoodsModel(mysql);
		
	}

}
