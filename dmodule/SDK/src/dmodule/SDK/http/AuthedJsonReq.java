package dmodule.SDK.http;

import dmodule.SDK.demoinfo.LoginInfo;
import dmodule.service.http.Env;
import dmodule.service.http.protocol.JsonReq;

// zzy TODO: 这个类的位置可能还会换
public class AuthedJsonReq extends JsonReq {
    
    public LoginInfo loginInfo;
    
    public AuthedJsonReq(Env env) {
        super(env);
    }
}
