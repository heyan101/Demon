package demon.auth;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import demon.SDK.SdkCenter;
import demon.SDK.demoinfo.LoginInfo;
import demon.SDK.demoinfo.TokenInfo;
import demon.SDK.demoinfo.UserInfo;
import demon.SDK.event.type.PostLoginEvent;
import demon.SDK.event.type.PreLoginEvent;
import demon.SDK.inner.IAuthApi;
import demon.SDK.inner.IBeans;
import demon.SDK.stat.UserRetStat;
import demon.exception.LogicalException;
import demon.exception.UnInitilized;
import demon.service.http.Env;
import demon.service.http.protocol.RetStat;

public class AuthApi implements IAuthApi {

	protected IBeans beans;
	protected AuthModel authModel;
	
	private static AuthApi authApi;
	private AuthApi(IBeans beans, AuthModel authModel) throws LogicalException {
		this.beans = beans;
		this.authModel = authModel;
		
		SdkCenter.getInst().addInterface(IAuthApi.name, this);
	}
	
	public static void init(IBeans beans, AuthModel authModel) throws LogicalException {
		if (null == authApi) {
			new AuthApi(beans, authModel);
		}
	}
	
	public static AuthApi getInst() throws UnInitilized {
		if (null == authApi) {
			throw new UnInitilized();
		}
		return authApi;
	}
	
	/*******************************************************************************************/
	
	public IAuthModel getAuthModel() {
		return this.authModel;
	}
	/**
	 * 用户登录
	 * @param env
	 * @param account
	 * @param password
	 * @param type 账号类型
	 * @param tokenAge
	 * @return
	 * @throws Exception
	 */
	public LoginInfo login(Env env, String account, String password, String type, Long tokenAge) throws Exception {
		if (tokenAge == null) {
            tokenAge = (long) AuthConfig.defaultTokenAge;
        }
		
		LoginInfo loginInfo = null;
		LogicalException logincalException = null;
		
		try {
			// 发送登录前事件
			PreLoginEvent preLoginEvent = new PreLoginEvent(env, account,
			        password, type, tokenAge);
			this.beans.getEventHub().dispatchEvent(PreLoginEvent.Type.PRE_LOGIN, preLoginEvent);

			if (!preLoginEvent.isContinue) {
			    throw new LoginException(preLoginEvent.breakReason);
			}
			if (preLoginEvent.loginInfo == null && preLoginEvent.logicalException == null) {
				Long uid = this.authModel.checkLoginId(type, account);
				if (uid == null) {
                    throw new LogicalException(UserRetStat.ERR_NO_SUCH_ACCOUNT,
                    		UserRetStat.getMsgByStat(
                            		UserRetStat.ERR_NO_SUCH_ACCOUNT, account));
                }
				loginInfo = login(env, uid, account, password, type, tokenAge);
			} else {
                loginInfo = preLoginEvent.loginInfo;
                logincalException = preLoginEvent.logicalException;
            }
			
		} catch (LogicalException e) {
            logincalException = e;
        }
		// 发送登录后事件
		PostLoginEvent postLoginEvent = new PostLoginEvent(env, account,
                password, type, tokenAge, logincalException, loginInfo);
		this.beans.getEventHub().dispatchEvent(PostLoginEvent.Type.POST_LOGIN, postLoginEvent);
        
        if (postLoginEvent.loginInfo != null) {
            return postLoginEvent.loginInfo;
        } else if (postLoginEvent.logicalException != null) {
            throw postLoginEvent.logicalException;
        } else {
            throw new LogicalException(RetStat.ERR_SERVER_EXCEPTION,
                    "Logical exception not set.");
        }
		
	}

	private LoginInfo login(Env env, Long uid, String name, String password, String type, Long tokenAge) throws SQLException, LogicalException {
		UserInfo user = this.beans.getUserApi().getUserModel().getUserInfoByUid(uid);
		if (user == null) {
			throw new LogicalException(UserRetStat.ERR_NO_SUCH_ACCOUNT,
					UserRetStat.getMsgByStat(UserRetStat.ERR_NO_SUCH_ACCOUNT, name));
		}
		if (!user.password.equals(password)) {
			throw new LogicalException(UserRetStat.ERR_INVALID_PASSWORD,
					UserRetStat.getMsgByStat(UserRetStat.ERR_INVALID_PASSWORD, name));
		}
        if (tokenAge == null) {
            tokenAge = (long) AuthConfig.defaultTokenAge;
        }
        TokenInfo tokenInfo = TokenInfo.newToken(uid, tokenAge, env.ip,
                env.device);
        this.authModel.addToken(tokenInfo);
		
        return new LoginInfo(tokenInfo, user);
	}
	
    public Long checkLoginId(String type, String value) throws SQLException {
        return null;
    }
    
    /**
     * 检查登录账号
     * 
     * @param type 账号类型
     * @param account 账号
     * @return
     * @throws LogicalException
     */
    public static boolean checkAccount(String type, String account) throws LogicalException {
        if (!(AuthConfig.LOGINID_NAME.equals(type) || AuthConfig.LOGINID_PHONE.equals(type) || 
        		AuthConfig.LOGINID_EMAIL.equals(type))) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_ACCOUNT_TYPE,
                    UserRetStat.getMsgByStat(UserRetStat.ERR_ILLEGAL_ACCOUNT_TYPE, type));
        }
        if (AuthConfig.LOGINID_EMAIL.equals(type)&& !account.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_EMAIL_ACCOUNT,
                    UserRetStat.getMsgByStat(UserRetStat.ERR_ILLEGAL_EMAIL_ACCOUNT, account));
        }
        if (AuthConfig.LOGINID_PHONE.equals(type)
                && !account.matches("^1[3458][0-9]{9}")) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_PHONE_ACCOUNT,
            		UserRetStat.getMsgByStat(UserRetStat.ERR_ILLEGAL_PHONE_ACCOUNT, account));
        }
        return true;
    }
}
