package demon.user;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import demon.exception.LogicalException;
import demon.exception.UnInitilized;
import demon.service.event.EventHub;
import demon.service.http.Env;
import demon.service.http.protocol.RetStat;
import demon.user.event.PostLoginEvent;
import demon.user.event.PreLoginEvent;
import demon.user.pojo.LoginInfo;
import demon.user.pojo.TokenInfo;
import demon.user.pojo.UserInfo;

public class UserApi {
	/**
     * 默认token的寿命
     */
    public static long defaultTokenAge;
    public static final String LOGINID_EMAIL = "email";
    public static final String LOGINID_PHONE = "phone";
    
	protected EventHub eventHub;
	protected UserModel userModel;
    
    public UserApi(EventHub eventHub, UserModel userModel) {
    	this.eventHub = eventHub;
    	this.userModel = userModel;
    }
    
    private static UserApi userApi;
    public static void init(EventHub eventHub, UserModel userModel) {
        userApi = new UserApi(eventHub, userModel);
    }
    
    public static UserApi getInst() throws UnInitilized {
        if (userApi == null) {
            throw new UnInitilized();
        }
        return userApi;
    }

    /**
     * 检查登录账号
     * 
     * @param type
     *            账号类型
     * @param account
     *            账号
     * @return
     * @throws LogicalException
     */
    public static boolean checkAccount(String type, String account)
            throws LogicalException {
        if (!(UserApi.LOGINID_EMAIL.equals(type) || UserApi.LOGINID_PHONE
                    .equals(type))) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_ACCOUNT_TYPE,
            		UserRetStat.getMsgByStat(
                    		UserRetStat.ERR_ILLEGAL_ACCOUNT_TYPE, type));
        }

        if (UserApi.LOGINID_EMAIL.equals(type)
                && !account.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_EMAIL_ACCOUNT,
                    UserRetStat.getMsgByStat(
                    		UserRetStat.ERR_ILLEGAL_EMAIL_ACCOUNT, account));
        }

        if (UserApi.LOGINID_PHONE.equals(type)
                && !account.matches("^1[3458][0-9]{9}")) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_PHONE_ACCOUNT,
            		UserRetStat.getMsgByStat(
            				UserRetStat.ERR_ILLEGAL_PHONE_ACCOUNT, account));
        }

        return true;
    }

    /**
     * 用户登录
     * @param name
     * @param password
     * @throws LogicalException 
     * @throws SQLException 
     */
	public LoginInfo login(Env env, String name, String password, String type, Long tokenAge) throws Exception {
		if (tokenAge == null) {
            tokenAge = (long) defaultTokenAge;
        }
		
		LoginInfo loginInfo = null;
		LogicalException logincalException = null;
		
		try {
			// 发送登录前事件
			PreLoginEvent preLoginEvent = new PreLoginEvent(env, name,
			        password, type, tokenAge);
			this.eventHub.dispatchEvent(PreLoginEvent.Type.PRE_LOGIN,
			        preLoginEvent);

			if (!preLoginEvent.isContinue) {
			    throw new LoginException(preLoginEvent.breakReason);
			}
			if (preLoginEvent.loginInfo == null && preLoginEvent.logicalException == null) {
				Long uid = this.userModel.checkLoginId(name);
				if (uid == null) {
                    throw new UserException(UserRetStat.ERR_NO_SUCH_ACCOUNT,
                    		UserRetStat.getMsgByStat(
                            		UserRetStat.ERR_NO_SUCH_ACCOUNT, name));
                }
				loginInfo = login(env, uid, name, password, type, tokenAge);
			} else {
                loginInfo = preLoginEvent.loginInfo;
                logincalException = preLoginEvent.logicalException;
            }
			
		} catch (LogicalException e) {
            logincalException = e;
        }
		// 发送登录后事件
		PostLoginEvent postLoginEvent = new PostLoginEvent(env, name,
                password, type, tokenAge, logincalException, loginInfo);
        this.eventHub.dispatchEvent(PostLoginEvent.Type.POST_LOGIN,
                postLoginEvent);
        
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
		UserInfo user = userModel.getUserInfoByUid(uid);
		if (user == null) {
			throw new LogicalException(UserRetStat.ERR_NO_SUCH_ACCOUNT,
					UserRetStat.getMsgByStat(UserRetStat.ERR_NO_SUCH_ACCOUNT, name));
		}
		if (!user.password.equals(password)) {
			throw new LogicalException(UserRetStat.ERR_INVALID_PASSWORD,
					UserRetStat.getMsgByStat(UserRetStat.ERR_INVALID_PASSWORD, name));
		}

        if (tokenAge == null) {
            tokenAge = (long) defaultTokenAge;
        }

        TokenInfo tokenInfo = TokenInfo.newToken(uid, tokenAge, env.ip,
                env.device);
        this.userModel.addToken(tokenInfo);
		
        return new LoginInfo(tokenInfo, user);
	}
    
}
