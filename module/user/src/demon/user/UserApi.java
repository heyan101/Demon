package demon.user;

import java.sql.SQLException;

import demon.SDK.SdkCenter;
import demon.SDK.demoinfo.UserInfo;
import demon.SDK.event.type.UserEvent;
import demon.SDK.inner.IBeans;
import demon.SDK.inner.IUserApi;
import demon.SDK.stat.UserRetStat;
import demon.exception.LogicalException;
import demon.exception.UnInitilized;
import demon.service.http.Env;
import demon.utils.SSHA;

public class UserApi implements IUserApi {
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
    
    /**********************************************************************************************/
    public UserInfo userRegister(Env env, UserInfo userInfo) throws Exception {
        // check username and password
        UserApi.checkPasswordIsLegal(userInfo.password);
        // sha1
        userInfo.password = SSHA.getSaltedPassword(userInfo.password);
        
        UserEvent userEvent = new UserEvent(env, userInfo);
        beans.getEventHub().dispatchEvent(UserEvent.Type.PRE_REGISTER_USER, userEvent);
        
        boolean result = userModel.createUser(userInfo);
        if (result) {
        	userInfo = userModel.findUser(userInfo.name, userInfo.email, userInfo.phone);
        	// login_id
        } else {
        	return null;
        }
        
        userEvent = new UserEvent(env, userInfo);
        beans.getEventHub().dispatchEvent(UserEvent.Type.POST_REGISTER_USER, userEvent);
        
        return userInfo;
    }
    
    /**
     * 验证密码有效性
     * @param password
     * @return
     * @throws Exception
     */
    public static boolean checkPasswordIsLegal(String password) throws Exception {
        if(password == null || password.length() == 0 || password.matches("[u4e00-u9fa5]"))
            throw new LogicalException(UserRetStat.ERR_ILLEGAL_PASSWORD, "illegal password :" + password);
        return true;
    }

    /**
     * 设置用户单一属性
     * @param env
     * @param uid
     * @param key 属性的 KEY
     * @param value 属性值
     * @return 
     * @throws LogicalException 
     * @throws SQLException 
     */
	public void setUserAttr(Env env, Long uid, String key, Object value) throws LogicalException, SQLException {
		UserInfo userInfo = getUserInfoByUid(uid);
		UserEvent ev = new UserEvent(env, userInfo, key, value);
        this.beans.getEventHub().dispatchEvent(UserEvent.Type.PRE_SET_USER_ATTR, ev);
        if (!ev.isContinue) {
            throw new LogicalException(ev.stat, ev.breakReason);
        }
        
        boolean flag = userModel.setUserAttr(uid, key, value);

        ev.setAttrFlag = flag; // 设置属性是否成功
        this.beans.getEventHub().dispatchEvent(UserEvent.Type.POST_SET_USER_ATTR, ev);
	}
	
	public void setUserAttr(Env env, UserInfo userInfo) throws LogicalException, SQLException {
		UserEvent ev = new UserEvent(env, userInfo);
        this.beans.getEventHub().dispatchEvent(UserEvent.Type.PRE_SET_USER_ATTR, ev);
        if (!ev.isContinue) {
            throw new LogicalException(ev.stat, ev.breakReason);
        }
        
        boolean flag = userModel.setUserAttr(userInfo);

        ev.setAttrFlag = flag; // 设置属性是否成功
        this.beans.getEventHub().dispatchEvent(UserEvent.Type.POST_SET_USER_ATTR, ev);
	}

	public UserInfo getUserInfoByUid(Long uid) throws SQLException {
		UserInfo userInfo = userModel.getUserInfoByUid(uid);
		return userInfo;
	}
}
