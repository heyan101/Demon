package demon.user;

import demon.service.db.MySql;
import demon.service.event.EventHub;
import demon.service.http.ApiGateway;
import demon.service.http.HttpServer;
import demon.service.http.protocol.ErrTextFormatter;
import demon.service.log.Logger;
import demon.utils.XProperties;
import demon.utils.unit.TimeUnit;

public class Init {
	
	public static final String MODULE_NAME = "user";
	
	public static void init(String moduleDir) throws Exception {
		MySql mysql = MySql.getInst(MODULE_NAME);
		UserModel userModel = new UserModel(mysql);
		// 设置管理员账号
		userModel.setAdminDefaultInfo();
		XProperties properties = new XProperties(MODULE_NAME, moduleDir);
		UserApi.defaultTokenAge = new TimeUnit(properties.getProperty(UserConfig.CONF_USER_TOKEN_AGE)).value.longValue();
		
	    EventHub eventHub = EventHub.getInst(MODULE_NAME);
	    
	    UserApi.init(eventHub, userModel);
	    UserHttpApi.init(UserApi.getInst());
	    
	    HttpServer server = HttpServer.getInst(MODULE_NAME);
        server.registApiService("api", new ApiGateway(UserHttpApi.getInst(), MODULE_NAME, Logger.getInst(MODULE_NAME)));
        
        ErrTextFormatter.registerErrTextFormat(MODULE_NAME, new UserRetStat());
	}
}
