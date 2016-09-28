package demon.SDK.inner;

import java.sql.SQLException;
import java.util.Map;

import demon.SDK.classinfo.LoginInfo;
import demon.SDK.classinfo.UserInfo;
import demon.exception.LogicalException;
import demon.service.http.Env;

public interface IUserApi {
    public static final String name = "IUserApi";
    
    public LoginInfo login(Env env, String name, String password, String type, Long tokenAge) throws Exception;
    public Long checkLoginId(String type, String value) throws SQLException;
    public UserInfo createUser(Env env, String type, String name, String password, Map<String, Object> attrs) throws Exception;
    
    public IUserModel getUserModel();
    
    public interface IUserModel {
        public Long checkLoginId(String type, String value) throws SQLException;
        public boolean createUser(UserInfo user) throws SQLException;
    }
}
