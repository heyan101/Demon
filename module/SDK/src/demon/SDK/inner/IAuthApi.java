package demon.SDK.inner;

import java.sql.SQLException;

import demon.SDK.demoinfo.LoginInfo;
import demon.SDK.demoinfo.TokenInfo;
import demon.exception.LogicalException;
import demon.service.http.Env;


public interface IAuthApi {

	public static final String name = "IAuthApi";
	
	public LoginInfo login(Env env, String account, String password, String type, Long tokenAge) throws Exception;
	/**
	 * 验证用户是否已登录
	 * @param env
	 * @param token
	 * @return
	 * @throws SQLException
	 * @throws LogicalException
	 */
	public LoginInfo checkLogin(Env env, String token) throws SQLException, LogicalException;
	/**
	 * 重新 fork 一个新的 token
	 * @param token
	 * @param tokenAge 新建token的寿命，单位毫秒
	 * @return TokenInfo
	 */
	public TokenInfo forkToken(Env env, String token, Long tokenAge) throws SQLException, LogicalException;
	
	public IAuthModel getAuthModel();
    public interface IAuthModel {
    	
    	public boolean addToken(TokenInfo tokenInfo) throws SQLException;
    	public TokenInfo getTokenInfo(String token) throws SQLException;
    	
    	/***********************************************************************/
    	/**
    	 * 验证登录 Id
    	 * @return 用户 uid
    	 */
    	public Long checkLoginId(String type, String value) throws SQLException;
    	public boolean setLoginId(String type, String value, Long uid) throws SQLException;
    }
}
