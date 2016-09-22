package demon.SDK.event;

import demon.service.http.Env;

//@javadoc

public interface IListener {
	public void onEvent(EventType type, Event e, Env env);
}
