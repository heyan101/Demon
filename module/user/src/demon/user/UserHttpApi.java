package demon.user;

import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.codec.binary.Base64;

import demon.SDK.demoinfo.LoginInfo;
import demon.SDK.demoinfo.UserInfo;
import demon.SDK.stat.UserRetStat;
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
	
	
	
	/**
	 * 用户注册
	 * @param name 手机号
	 * @param email 邮箱
	 * @param phone 手机号
	 * @param password 密码
	 * @param nick 昵称
	 * @param qq QQ
	 * @param exattr 额外属性 
	 * 
	 * @return UserInfo
	 * @exception ERR_ADD_LOGIN_ID_FAILED
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp userRegister(JsonReq req) throws Exception {
		String name = req.paramGetString("name", false);
		String email = req.paramGetString("eamil", false);
		String phone = req.paramGetString("phone", false);
		String password = req.paramGetString("password", true);
		password = new String(Base64.decodeBase64(password));
		String nick = req.paramGetString("name", false);
		String qq = req.paramGetString("qq", false);
		Map<String, Object> exattr = req.paramGetMap("exattr", false, String.class, Object.class, true);
		
		JsonResp resp = new JsonResp(RetStat.OK);
		UserInfo userInfo = new UserInfo(name, phone, email, nick, password, qq, UserConfig.defaultUserType, exattr);
		userInfo = this.userApi.userRegister(req.env, userInfo);
		if (userInfo == null) {
			resp.stat = UserRetStat.ERR_ADD_LOGIN_ID_FAILED;
		}
		
        resp.resultMap.put("UserInfo", userInfo);
		return resp;
	}
	
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
