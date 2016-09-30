package demon.SDK.inner;

import java.sql.SQLException;

import demon.SDK.demoinfo.LoginInfo;
import demon.SDK.demoinfo.TokenInfo;
import demon.service.http.Env;


public interface IAuthApi {

	public static final String name = "IAuthApi";
	
//	public LoginInfo login(Env env, String account, String password, String type, Long tokenAge) throws Exception;
	public Long checkLoginId(String type, String value) throws SQLException;
	
	public IAuthModel getAuthModel();
    public interface IAuthModel {
    	/**
    	 * 验证登录 Id
    	 * @return 用户 uid
    	 */
    	public Long checkLoginId(String type, String value) throws SQLException;
    	public boolean addToken(TokenInfo tokenInfo) throws SQLException;
    }
}
