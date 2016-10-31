package dmodule.initdata;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import demon.service.http.Env;
import demon.utils.XProperties;
import dmodule.SDK.demoinfo.UserInfo;
import dmodule.SDK.inner.IBeans;

public class InitUser {

	public static void initUser(Env env, XProperties properties, IBeans beans) throws Exception {
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
                uid = user.uid;
            } else {
                Object status = attrs.get("status");
                if (status != null) {
                    beans.getUserApi().setUserAttr(env, uid, "status", status);
                }
            }
        }
    }
}
