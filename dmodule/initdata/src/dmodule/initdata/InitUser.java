package dmodule.initdata;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import demon.exception.LogicalException;
import demon.service.http.Env;
import demon.utils.XProperties;
import dmodule.SDK.demoinfo.UserInfo;
import dmodule.SDK.inner.IBeans;

/**
 * 初始化用户
 * 		a. 根据配置文件中指定的参数，创建用户，默认是一个超级管理员(admin)和普通用户(test)
 * 
 * @author Demon
 */
public class InitUser {

	public static void initUser(Env env, XProperties properties, IBeans beans) 
			throws SQLException, LogicalException, NoSuchAlgorithmException {
        String _userNames = properties.getProperty("init.user");
        String[] names = _userNames.split(",");
        for (String name : names) {
            if (name.trim().length() == 0) {
                continue;
            }
            String password = properties.getProperty(String.format("init.user.%s.password", name));
            String attrStr = properties.getProperty(String.format("init.user.%s.attrs", name));
            
            @SuppressWarnings("unchecked")
            Map<String, Object> attrs = (Map<String, Object>) JSONObject.parse(attrStr);
            
            Long uid = beans.getAuthApi().getAuthModel().checkLoginId("name", name);
            UserInfo user = new UserInfo();
            user.name = name;
            user.password = password;
            user.nick = (String) attrs.get("nick");
            user.email = (String) attrs.get("email");
            user.status = (int) attrs.get("status");
            user.type = (int) attrs.get("type");
            if (null == uid) {
                user = beans.getUserApi().userRegister(env, user);
            } else {
                Object status = attrs.get("status");
                if (status != null) {
                    beans.getUserApi().setUserAttr(env, uid, "status", status);
                }
            }
        }
    }
}
