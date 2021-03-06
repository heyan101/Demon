package dmodule.SDK.inner;

import demon.exception.LogicalException;

public interface IBeans {
	public static final String name = "IBeans";

	public IEventHub getEventHub() throws LogicalException;
	public IUserApi getUserApi() throws LogicalException;
	public IAuthApi getAuthApi() throws LogicalException;
	public IClassedApi getClassedApi() throws LogicalException;
}
