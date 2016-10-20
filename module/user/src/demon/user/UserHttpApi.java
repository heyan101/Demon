package demon.user;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import demon.SDK.demoinfo.UserInfo;
import demon.SDK.http.AuthedJsonProtocol;
import demon.SDK.http.AuthedJsonReq;
import demon.SDK.stat.AclRetStat;
import demon.SDK.stat.UserRetStat;
import demon.exception.LogicalException;
import demon.exception.UnInitilized;
import demon.service.http.ApiGateway;
import demon.service.http.protocol.JsonProtocol;
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
	
	/*************************************************************************************************/
	/**
	 * 用户注册
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 * @param name
	 * <blockquote>
     * 		类型：字符串<br/>
     * 		描述：用户名<br/>
     * 		必需：NO
     * </blockquote>
	 * @param email
	 * <blockquote>
     * 		类型：字符串<br/>
     * 		描述：邮箱<br/>
     * 		必需：NO
     * </blockquote>
	 * @param phone
	 * <blockquote>
     * 		类型：字符串<br/>
     * 		描述：手机号<br/>
     * 		必需：NO
     * </blockquote>
	 * @param password 
	 * <blockquote>
     * 		类型：字符串<br/>
     * 		描述：密码<br/>
     * 		必需：YES
     * </blockquote>
	 * @param nick 
	 * <blockquote>
     * 		类型：字符串<br/>
     * 		描述：昵称<br/>
     * 		必需：NO
     * </blockquote>
	 * @param qq 
	 * <blockquote>
     * 		类型：字符串<br/>
     * 		描述：QQ<br/>
     * 		必需：NO
     * </blockquote>
	 * @param exattr 
	 * <blockquote>
     * 		类型：Map<br/>
     * 		描述：额外属性<br/>
     * 		必需：NO
     * </blockquote> 
	 * @return UserInfo
	 * @exception ERR_ADD_LOGIN_ID_FAILED
	 */
	@ApiGateway.ApiMethod(protocol = JsonProtocol.class)
	public JsonResp userRegister(AuthedJsonReq req) throws Exception {
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
	 * 通过 uid 获取用户信息
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 * @param uid
	 * <blockquote>
     * 		类型：整形<br/>
     * 		描述：用户 uid<br/>
     * 		必需：YES
     * </blockquote> 
     * @exception ERR_USER_NOT_FOUND,ERR_ACL_NOT_GET_USERINFO
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp getUserInfo(AuthedJsonReq req) throws Exception {
		Long uid = req.paramGetNumber("uid", true, true);
		if (uid == null || uid.longValue() < 1) {
			throw new LogicalException(UserRetStat.ERR_USER_NOT_FOUND, "uid == null || uid < 1");
		}
		// 只有管理员和用户自己可以查看用户信息，其他人没有权限
		if (2 != req.loginInfo.userInfo.type || uid.longValue() != req.loginInfo.userInfo.uid.longValue()) {
			throw new LogicalException(AclRetStat.ERR_ACL_NOT_GET_USERINFO, 
					AclRetStat.getMsgByStat(AclRetStat.ERR_ACL_NOT_GET_USERINFO, String.valueOf(uid)));
		}
		
		UserInfo userInfo = this.userApi.getUserInfoByUid(uid);
		JsonResp resp = new JsonResp(RetStat.OK);

		resp.resultMap.put("UserInfo", userInfo);
		return resp;
	}
	/**
	 * 设置用户信息
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp setUserInfo(AuthedJsonReq req) throws Exception {
		return null;
	}
	/**
	 * 更新用户昵称
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp updateNickName(AuthedJsonReq req) throws Exception {
		return null;
	}
	/**
	 * 更新密码
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp updatePassword(AuthedJsonReq req) throws Exception {
		return null;
	}
	/**
	 * 找回密码
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp retrievePassword(AuthedJsonReq req) throws Exception {
		return null;
	}
	/**
	 * 添加用户收货地址
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp addDeliveryAddress(AuthedJsonReq req) throws Exception {
		return null;
	}
	/**
	 * 修改用户收货地址
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp updateDeliveryAddress(AuthedJsonReq req) throws Exception {
		return null;
	}
	/**
	 * 获取用户收货地址
	 * 
	 * @param token
	 * <blockquote>
     * 		类型：String<br/>
     * 		描述：token<br/>
     * 		必需：YES
     * </blockquote>
	 */
	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
	public JsonResp getDeliveryAddress(AuthedJsonReq req) throws Exception {
		return null;
	}
	
//	/**
//	 * 设置用户头像，上传用户头像数据
//	 */
//	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
//	public JsonResp setUserImage(AuthedJsonReq req) throws Exception {
//		return null;
//	}
//	
//	/**
//	 * 裁减用户头像
//	 */
//	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
//	public JsonResp cutUserImage(AuthedJsonReq req) throws Exception {
//		return null;
//	}
//	
//	/**
//	 * 获取用户头像数据
//	 */
//	@ApiGateway.ApiMethod(protocol = AuthedJsonProtocol.class)
//	public JsonResp getUserImage(AuthedJsonReq req) throws Exception {
//		return null;
//	}
	
}
