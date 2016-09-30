package demon.SDK.inner;

import java.sql.SQLException;

import demon.SDK.demoinfo.LoginInfo;
import demon.SDK.demoinfo.UserInfo;
import demon.exception.LogicalException;
import demon.service.http.Env;

public interface IUserApi {
	
    public static final String name = "IUserApi";
    
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
    public void setUserAttr(Env env, Long uid, String key, Object value)throws LogicalException, SQLException;
    
    
    public IUserModel getUserModel();
    public interface IUserModel {
        /**
         * 创建用户
         * @param user
         * @return
         * @throws SQLException
         */
        public boolean createUser(UserInfo user) throws SQLException;
        /**
         * 设置用户单一属性
         * @param uid
         * @param key
         * @param value
         * @return
         * @throws SQLException
         */
        public boolean setUserAttr(Long uid, String key, Object value) throws SQLException;
        /**
         * 批量设置用户属性
         * @param userInfo
         * @return
         * @throws SQLException
         */
        public boolean setUserAttr(UserInfo userInfo) throws SQLException;
        public UserInfo getUserInfoByUid(Long uid) throws SQLException;
    }

}
