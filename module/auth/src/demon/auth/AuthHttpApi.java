package demon.auth;

import javax.servlet.http.Cookie;

import demon.SDK.demoinfo.LoginInfo;
import demon.SDK.stat.UserRetStat;
import demon.exception.UnInitilized;
import demon.service.http.ApiGateway;
import demon.service.http.protocol.JsonProtocol;
import demon.service.http.protocol.JsonReq;
import demon.service.http.protocol.JsonResp;
import demon.service.http.protocol.RetStat;

public class AuthHttpApi {
	private AuthApi authApi;
	
	private static AuthHttpApi authHttpApi;
	private AuthHttpApi(AuthApi authApi) {
		this.authApi = authApi;
	}
	
	public static void init(AuthApi authApi) {
		authHttpApi = new AuthHttpApi(authApi);
	}
	
	public static AuthHttpApi getInst() {
		if (null == authHttpApi) {
			new UnInitilized();
		}
		return authHttpApi;
	}
	
	/********************************************     对外接口               ********************************************/
	/**
	 * 用户登录
	 * @param account 手机号/用户名/邮箱
	 * @param password 密码
	 * @param type 账号类型：手机号/用户名/邮箱
	 * @param tokenAge token 过期时间(单位：毫秒)
	 * @param isCookie 是否写入cookie(no,yes)
	 * @return
	 * @throws Exception
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp login(JsonReq req) throws Exception {
		String account = req.paramGetString("name", true);
		String password = req.paramGetString("password", true);
		String type = req.paramGetString("type", true);
		Long tokenAge = req.paramGetNumber("tokenAge", false, true);
		String isCookie = req.paramGetString("isCookie", false);
		
		JsonResp resp = new JsonResp(RetStat.OK);
		// 非法账号类型
		if (!type.equals("name") && !type.equals("email") && !type.equals("phone")) {
			resp.stat = UserRetStat.ERR_ILLEGAL_ACCOUNT_TYPE;
			return resp;
		}

		AuthUtils.checkAccount(type, account);
		
		LoginInfo loginInfo = null;
		loginInfo = authApi.login(req.env, account, password, type, tokenAge);
		
        resp.resultMap.put("token", loginInfo.tokenInfo.token);

        Cookie cookie = new Cookie("token", loginInfo.tokenInfo.token);
        cookie.setPath("/");
        if ("yes".equals(isCookie)) {
            cookie.setMaxAge((int) (loginInfo.tokenInfo.expires.getTime() - loginInfo.tokenInfo.ctime.getTime()));
        }
        resp.addCookie(cookie);
        return resp;
	}

}
