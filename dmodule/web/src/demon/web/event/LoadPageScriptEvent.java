package demon.web.event;

import dmodule.SDK.event.Event;
import dmodule.SDK.event.EventType;
import dmodule.service.http.Env;

//@javadoc

import java.util.LinkedList;
import java.util.List;

public class LoadPageScriptEvent extends Event{
	
	public Env env;
	
    public final List<String> scripts = new LinkedList<String>();

    public final String page;
    
    public LoadPageScriptEvent(String page) {
    	this.page = page;
	}

	public void addScript(String script) {
        
    	scripts.add(script);
    }

    public static enum Type implements EventType {
    	/**
    	 * 加载静态文件
    	 */
    	LoadPageScript
    }
}
