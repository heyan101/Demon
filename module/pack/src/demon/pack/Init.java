package demon.pack;

import demon.utils.XProperties;

public class Init {
	
	public static final String MODULE_NAME = "pack";
	
	public static void init(String moduleDir) throws Exception {
		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
		
	}

}
