package demon.SDK.demoinfo;

/**
 * 登录信息包括token信息以及用户信息
 * @author sucun
 *
 */
public class LoginInfo {
    
    /**
     * token信息对象
     */
    public TokenInfo tokenInfo;
    
    /**
     * 用户信息对象
     */
	public UserInfo userInfo;
	
	public LoginInfo(TokenInfo tokenInfo, UserInfo userInfo) {
		this.tokenInfo = tokenInfo;
		this.userInfo = userInfo;
	}
}
