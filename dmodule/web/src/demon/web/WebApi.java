package demon.web;

import java.util.List;

import demon.web.event.LoadPageScriptEvent;
import dmodule.Config;
import dmodule.SDK.SdkCenter;
import dmodule.SDK.inner.IEventHub;
import dmodule.exception.LogicalException;
import dmodule.exception.UnInitilized;
import dmodule.service.http.Env;

public class WebApi {    
    public WebApi() {
    }
    
    private static WebApi webApi;

    public static void init() {
        webApi = new WebApi();
    }
    public static WebApi getInst() throws UnInitilized {
        if (webApi == null) {
            throw new UnInitilized();
        }
        return webApi;
    }

    /*-------------------------------------------*/
    
	public List<String> loadPageScript(Env env, String page) throws LogicalException {
		
		LoadPageScriptEvent lps = new LoadPageScriptEvent(page);
		lps.env = env;
		IEventHub eventHub = (IEventHub) SdkCenter.getInst().queryInterface(IEventHub.name, SdkCenter.ToString() + "InnerKey" + Config.ToString());
		eventHub.dispatchEvent(LoadPageScriptEvent.Type.LoadPageScript, lps);
		
		return null;
	}
    
}
