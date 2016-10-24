package dmodule.SDK.inner;

import dmodule.exception.LogicalException;

public interface IBeans {
	public static final String name = "IBeans";

	public IEventHub getEventHub() throws LogicalException;
	public IUserApi getUserApi() throws LogicalException;
	public IAuthApi getAuthApi() throws LogicalException;
}
