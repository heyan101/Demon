package demon.user;

import demon.SDK.classinfo.LoginInfo;
import demon.exception.UnInitilized;
import demon.service.http.ApiGateway;
import demon.service.http.protocol.JsonProtocol;
import demon.service.http.protocol.JsonReq;
import demon.service.http.protocol.JsonResp;
import demon.service.http.protocol.RetStat;

public class UserHttpApi {

	protected UserApi userApi;
	
	public UserHttpApi(UserApi userApi){
		this.userApi = userApi;
	}
	
	private static UserHttpApi userHttpApi;
	public static void init(UserApi userApi) {
		userHttpApi = new UserHttpApi(userApi);
	}
	
	public static UserHttpApi getInst() throws UnInitilized {
		if (userHttpApi == null) {
			throw new UnInitilized();
		}
		return userHttpApi;
	}
	
	/********************************************     对外接口               ********************************************/
	
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp adminLogin(JsonReq req) throws Exception {
		String name = req.paramGetString("name", true);
		String password = req.paramGetString("password", true);
		Long tokenAge = req.paramGetNumber("tokenAge", false, true);
		
		LoginInfo loginInfo = userApi.login(req.env, name, password, UserApi.LOGINID_PHONE, tokenAge);
		req.env.sticker.put("uid", loginInfo.userInfo.uid);
		req.env.sticker.put("account", loginInfo.userInfo);
		JsonResp resp = new JsonResp(RetStat.OK);
        resp.resultMap.put("token", loginInfo.tokenInfo.token);
        
		return resp;
	}
	
	/**
	 * 用户注册
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp userRegister(JsonReq req) throws Exception {
		String phone = req.paramGetString("phone", true);
		String password = req.paramGetString("password", true);
		String codes = req.paramGetString("codes", true);
		
		UserApi.checkAccount(UserApi.LOGINID_PHONE, phone);
		
		
		
		return null;
	}
	/**
	 * 用户登录(使用手机号)
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp phoneLogin(JsonReq req) throws Exception {
		String phone = req.paramGetString("phone", true);
		UserApi.checkAccount(UserApi.LOGINID_PHONE, phone);
		
//		JsonResp result = login(req, AuthApi.LOGINID_PHONE, phone);
		
		return null;
	}
	/**
	 * 用户登录(使用邮箱)
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp emailLogin(JsonReq req) throws Exception {
		return null;
	}
//	private JsonResp login(JsonReq req, String type, String account) throws Exception {
//        String password = req.paramGetString("password", true, false);
//        Long tokenAge = req.paramGetNumber("tokenAge", false, true);
//
//        LoginInfo loginInfo = this.userApi.login(req.env, type, account, password, tokenAge);
//
//        req.env.sticker.put("uid", loginInfo.userInfo.uid);
//        req.env.sticker.put("account", UserCoreApi.getAccount(loginInfo.userInfo));
//
//        JsonResp resp = new JsonResp(RetStat.OK);
//        resp.resultMap.put("token", loginInfo.tokenInfo.token);
//
//        return resp;
//    }
	
	/**
	 * 获取用户信息
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp getUserInfo(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 设置用户信息
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp setUserInfo(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 更新用户昵称
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp updateNickName(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 更新密码
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp updatePassword(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 找回密码
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp retrievePassword(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 添加用户收货地址
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp addDeliveryAddress(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 修改用户收货地址
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp updateDeliveryAddress(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 获取用户收货地址
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp getDeliveryAddress(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 设置用户头像，上传用户头像数据
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp setUserImage(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 裁减用户头像
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp cutUserImage(JsonReq req) throws Exception {
		return null;
	}
	/**
	 * 获取用户头像数据
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp getUserImage(JsonReq req) throws Exception {
		return null;
	}
	
}
