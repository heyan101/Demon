package demon.user;

import java.sql.SQLException;
import java.util.Map;

import javax.security.auth.login.LoginException;

import demon.SDK.SdkCenter;
import demon.SDK.classinfo.LoginInfo;
import demon.SDK.classinfo.TokenInfo;
import demon.SDK.classinfo.UserInfo;
import demon.SDK.event.type.PostLoginEvent;
import demon.SDK.event.type.PreLoginEvent;
import demon.SDK.inner.IBeans;
import demon.SDK.inner.IUserApi;
import demon.SDK.stat.UserRetStat;
import demon.exception.LogicalException;
import demon.exception.UnInitilized;
import demon.service.http.Env;
import demon.service.http.protocol.RetStat;

public class UserApi implements IUserApi {
	/**
     * 默认token的寿命
     */
    public static long defaultTokenAge;
    public static final String LOGINID_EMAIL = "email";
    public static final String LOGINID_PHONE = "phone";
    public static final String LOGINID_NAME = "name";
    
	protected IBeans beans;
	protected UserModel userModel;
    
    public UserApi(IBeans beans, UserModel userModel) throws LogicalException {
    	this.beans = beans;
    	this.userModel = userModel;
    	
    	SdkCenter.getInst().addInterface(IUserApi.name, this);
    }
    
    private static UserApi userApi;
    public static void init(IBeans beans, UserModel userModel) throws LogicalException {
        userApi = new UserApi(beans, userModel);
    }
    
    public static UserApi getInst() throws UnInitilized {
        if (userApi == null) {
            throw new UnInitilized();
        }
        return userApi;
    }
    
    @Override
    public IUserModel getUserModel() {
        return this.userModel;
    }
    
    /**
     * @throws Exception ********************************************************************************************/

    @SuppressWarnings("unchecked")
    public UserInfo createUser(Env env, String type, String name, String password, Map<String, Object> attrs) throws Exception {
        // check username and password
        UserApi.checkAccount(type, name);
        UserApi.checkPasswordIsLegal(password);
        
        UserInfo userInfo = new UserInfo();
        userModel.createUser(userInfo);
        
        return userInfo;
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
    public static boolean checkAccount(String type, String account) throws LogicalException {
        if (!(UserApi.LOGINID_EMAIL.equals(type) || UserApi.LOGINID_PHONE.equals(type) || UserApi.LOGINID_NAME.equals(type))) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_ACCOUNT_TYPE,
                    UserRetStat.getMsgByStat(UserRetStat.ERR_ILLEGAL_ACCOUNT_TYPE, type));
        }
        if (UserApi.LOGINID_EMAIL.equals(type)&& !account.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$")) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_EMAIL_ACCOUNT,
                    UserRetStat.getMsgByStat(UserRetStat.ERR_ILLEGAL_EMAIL_ACCOUNT, account));
        }
        if (UserApi.LOGINID_PHONE.equals(type)
                && !account.matches("^1[3458][0-9]{9}")) {
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_PHONE_ACCOUNT,
            		UserRetStat.getMsgByStat(UserRetStat.ERR_ILLEGAL_PHONE_ACCOUNT, account));
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
			this.beans.getEventHub().dispatchEvent(PreLoginEvent.Type.PRE_LOGIN, preLoginEvent);

			if (!preLoginEvent.isContinue) {
			    throw new LoginException(preLoginEvent.breakReason);
			}
			if (preLoginEvent.loginInfo == null && preLoginEvent.logicalException == null) {
				Long uid = this.userModel.checkLoginId(type, name);
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

    @Override
    public Long checkLoginId(String type, String value) throws SQLException {
        return null;
    }
    
    public static boolean checkPasswordIsLegal(String password) throws Exception {
        if(password == null || password.length() == 0 || password.matches("[u4e00-u9fa5]"))
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_PASSWORD, "illegal password :" + password);
        return true;
    }

}
