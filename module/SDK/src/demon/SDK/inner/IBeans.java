package demon.SDK.inner;

import demon.exception.LogicalException;

public interface IBeans {
	public static final String name = "IBeans";

	public IEventHub getEventHub() throws LogicalException;
}
