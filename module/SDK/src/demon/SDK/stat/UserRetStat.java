package demon.SDK.stat;

import java.util.HashMap;
import java.util.Map;

import demon.service.http.Env;
import demon.service.http.protocol.ErrTextFormat;

/**
 * 实现ErrTextFormat接口<br>
 * 提供错误返回码，及其相应的解析文本
 * 
 * @author Demon
 *
 */
public class UserRetStat implements ErrTextFormat {

	/**
	 * 该账号已被使用
	 */
    public static final String ERR_ACCOUNT_EXIST = "ERR_ACCOUNT_EXIST";

    /**
     * 手机号已经被绑定
     */
    public static final String ERR_PHONE_ALREADY_BOUND = "ERR_PHONE_ALREADY_BOUND";
    
    /**
     * 请不要重复发送验证码
     */
    public static final String ERR_SEND_CODE_FREQUENTLY = "ERR_SEND_CODE_FREQUENTLY";
    
    /**
     * 验证码错误
     */
    public static final String ERR_CODE = "ERR_CODE";
    
    /**
     * 清除用户数据失败
     */
    public static final String ERR_CLEAN_ACCOUNT_FAILED = "ERR_CLEAN_ACCOUNT_FAILED";
    
    /**
     * 注册登录信息失败
     */
    public static final String ERR_ADD_LOGIN_ID_FAILED = "ERR_ADD_LOGIN_ID_FAILED";
    
    /**
     * 账号不存在
     */
    public static final String ERR_NO_SUCH_ACCOUNT = "ERR_NO_SUCH_ACCOUNT";
    
    /**
     * 用户不存在
     */
    public static final String ERR_USER_NOT_FOUND = "ERR_USER_NOT_FOUND";
    
    /**
     * 用户信息损坏
     */
    public static final String ERR_USER_INFO_BROKEN = "ERR_USER_INFO_BROKEN";
    
    /**
     * 密码错误
     */
    public static final String ERR_INVALID_PASSWORD = "ERR_INVALID_PASSWORD";
    
    /**
     * 密码过期
     */
    public static final String ERR_PASSWORD_EXPIRED = "ERR_PASSWORD_EXPIRED";
    
    /**
     * 非法账号类型
     */
    public static final String ERR_ILLEGAL_ACCOUNT_TYPE = "ERR_ILLEGAL_ACCOUNT_TYPE";
    
    /**
     * 非法邮箱
     */
    public static final String ERR_ILLEGAL_EMAIL_ACCOUNT = "ERR_ILLEGAL_EMAIL_ACCOUNT";
    
    /**
     * 非法手机号
     */
    public static final String ERR_ILLEGAL_PHONE_ACCOUNT = "ERR_ILLEGAL_PHONE_ACCOUNT";
    
    /**
     * 错误码：非法密码
     */
    public static final String ERR_ILLEGAL_PASSWORD = "ERR_ILLEGAL_PASSWORD";
    
    /**
     * 用户还未登录
     */
    public static final String ERR_TOKEN_NOT_FOUND = "ERR_TOKEN_NOT_FOUND";
    
    /**
     * 用户登录超时
     */
    public static final String ERR_TOKEN_EXPIRED = "ERR_TOKEN_EXPIRED";
    
    /**
     * 验证码过期
     */
    public static final String ERR_VALIDATE_CODE_EXPIRED = "ERR_VALIDATE_CODE_EXPIRED";
    
    /**
     * 验证码错误
     */
    public static final String ERR_INVALID_VALIDATE_CODE = "ERR_INVALID_VALIDATE_CODE";
    
    /**
     * 登录的用户与新建token用户不为一个用户
     */
    public static final String ERR_TOKEN_UID_MISMATCHING = "ERR_TOKEN_UID_MISMATCHING";
    
    public static String getMsgByStat(String stat, Object... params) {
        
        switch (stat) {
        case ERR_TOKEN_NOT_FOUND : return null;
        case ERR_TOKEN_EXPIRED : return null;
        case ERR_NO_SUCH_ACCOUNT : return String.format("Account '%s' doesn't exist.", params);
        case ERR_USER_NOT_FOUND : return String.format("User '%s' not found.", params);
        case ERR_USER_INFO_BROKEN : return String.format("%s's needed attrs not found.", params);
        case ERR_INVALID_PASSWORD : return String.format("Password is illegal.", params);
        case ERR_ILLEGAL_ACCOUNT_TYPE : return String.format(" Account type '%s' illegal.", params);
        case ERR_ILLEGAL_EMAIL_ACCOUNT : return String.format("Email '%s' is illegal.", params);
        case ERR_ACCOUNT_EXIST : return String.format("Account '%s' has exist.", params);
        case ERR_ADD_LOGIN_ID_FAILED : return String.format("Allocate identity for '%s' failed.", params);
        case ERR_CLEAN_ACCOUNT_FAILED : return String.format("Clean %s's login ids failed.", params);
        case ERR_TOKEN_UID_MISMATCHING : return String.format("auth user '%s' and token '%s' mismatching", params);
        case ERR_ILLEGAL_PHONE_ACCOUNT: return String.format("Phone '%s' is illegal.", params);
        default : return null;
        }
        
    }
    
    public static final Map<String, String> ERR_TEXT;
    static {
        ERR_TEXT = new HashMap<String, String>();
        
        ERR_TEXT.put(ERR_ACCOUNT_EXIST, "该账号已被使用");
        ERR_TEXT.put(ERR_CLEAN_ACCOUNT_FAILED, "清除用户数据失败");
        ERR_TEXT.put(ERR_ADD_LOGIN_ID_FAILED, "注册登录信息失败");
        ERR_TEXT.put(ERR_NO_SUCH_ACCOUNT, "账号不存在");
        ERR_TEXT.put(ERR_USER_NOT_FOUND, "用户不存在");
        ERR_TEXT.put(ERR_USER_INFO_BROKEN, "用户信息损坏");
        ERR_TEXT.put(ERR_INVALID_PASSWORD, "密码错误");
        ERR_TEXT.put(ERR_PASSWORD_EXPIRED, "密码过期");
        ERR_TEXT.put(ERR_ILLEGAL_ACCOUNT_TYPE, "非法账号类型");
        ERR_TEXT.put(ERR_ILLEGAL_EMAIL_ACCOUNT, "非法邮箱");
        ERR_TEXT.put(ERR_TOKEN_NOT_FOUND, "用户还未登录");
        ERR_TEXT.put(ERR_TOKEN_EXPIRED, "用户登录超时");
        ERR_TEXT.put(ERR_VALIDATE_CODE_EXPIRED, "验证码过期");
        ERR_TEXT.put(ERR_INVALID_VALIDATE_CODE, "验证码错误");
        ERR_TEXT.put(ERR_TOKEN_UID_MISMATCHING, "登录用户与创建新的token用户不相同");
        ERR_TEXT.put(ERR_ILLEGAL_PHONE_ACCOUNT, "非法手机号");
        ERR_TEXT.put(ERR_ILLEGAL_PASSWORD, "非法密码");
        ERR_TEXT.put(ERR_SEND_CODE_FREQUENTLY, "发送验证码太频繁");
        ERR_TEXT.put(ERR_PHONE_ALREADY_BOUND, "手机号已经被绑定");
        ERR_TEXT.put(ERR_CODE, "验证码错误");
    }

    public String getErrText(Env env, String stat) {
        if (ERR_INVALID_PASSWORD.equals(stat)) {
            Integer rest = (Integer)env.logParams.get("rest");
            if (null != rest && rest > 0) {
                return String.format("密码错误,还有%s次机会", rest);
            }
        }
        return ERR_TEXT.get(stat);
    }
}
