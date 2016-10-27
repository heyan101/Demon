package dmodule.SDK.demoinfo;

/**
 * 登录信息包括 TokenInfo 以及 UserInfo
 * 
 * @author Demon
 * @see TokenInfo
 * @see UserInfo
 */
public class LoginInfo {
    
    /** token信息对象 */
    public TokenInfo tokenInfo;
    /** 用户信息对象 */
	public UserInfo userInfo;
	
	public LoginInfo(TokenInfo tokenInfo, UserInfo userInfo) {
		this.tokenInfo = tokenInfo;
		this.userInfo = userInfo;
	}
}