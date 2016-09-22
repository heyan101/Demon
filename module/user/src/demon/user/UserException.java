package demon.user;

import demon.exception.LogicalException;

public class UserException extends LogicalException {
	private static final long serialVersionUID = 5818387456754426902L;

	public UserException(String stat, String errMsg) {
		super(stat, errMsg);
	}
}
