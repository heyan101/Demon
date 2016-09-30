package demon.SDK.demoinfo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UserInfo {

	/**
	 * 用户 Id
	 */
	public Long uid;
	/**
	 * 用户名,网页端注册的账号,同时会绑定邮箱
	 */
	public String name;
	/**
	 * 手机号
	 */
	public String phone;
	/**
	 * 昵称
	 */
	public String nick;
	/**
	 * 密码
	 */
	public String password;
	/**
	 * 邮箱
	 */
	public String email;
	/**
	 * QQ 号
	 */
	public String qq;
	/**
	 * 用户状态:1-正常,2-锁定,3-删除,4-未实名
	 */
	public int status;
	/**
	 * 用户类型:1-客户,2-管理员
	 */
	public int type;
	/**
	 * 扩展属性集合
	 */
	public Map<String, Object> exattr;
	/**
     * 创建时间
     */
    public Timestamp ctime;
    /**
     * 修改时间
     */
    public Timestamp mtime;
    /**
     * 最后一次登录时间
     */
    public Timestamp load_time;
    /**
	 * 年龄 [1-100]
	 */
	public int age;
	/**
	 * 性别:1-男,2-女
	 */
	public int sex;
	/**
	 * 所在城市
	 */
    public String city;
    /**
     * 邮政编码
     */
    public int postcode;
    /**
     * 真实姓名
     */
    public String true_name;
    /**
     * 身份证号
     */
    public String card_code;
    /**
     * 身份证正面照保存路径
     */
    public String card_positive_img;
    /**
     * 身份证背面照保存路径
     */
    public String card_back_img;
    
    public UserInfo() {
    }
	
	/**
	 * 用户基本信息
	 */
	public UserInfo(Long uid, String name, String phone, String email, String nick, String password, String qq, int type, 
			int status, Map<String, Object> exattr, Timestamp ctime, Timestamp mtime, Timestamp load_time) {
		super();
		this.uid = uid;
		this.phone = phone;
		this.name = name;
		this.nick = nick;
		this.email = email;
		this.qq = qq;
		this.status = status;
		this.type = type;
		this.exattr = exattr;
		this.ctime = ctime;
		this.mtime = mtime;
		this.load_time = load_time;
	}
	
	/**
	 * 用户注册
	 */
	public UserInfo(String name, String phone, String email, String nick, String password, String qq, int type, 
			Map<String, Object> exattr) {
		super();
		this.name = name;
		this.phone = phone;
		this.nick = nick;
		this.email = email;
		this.qq = qq;
		this.type = type;
		this.exattr = exattr;
	}
	
	/**
	 * 用户详细信息
	 */
	public UserInfo(Long uid, String name, String phone, String email, String nick, String password, String qq, int type, 
			int status, Map<String, Object> exattr, Timestamp ctime, Timestamp mtime, Timestamp load_time, int age, 
			int sex, int postcode, String true_name, String card_code, String card_positive_img, String card_back_img) {
		super();
		this.uid = uid;
		this.phone = phone;
		this.name = name;
		this.nick = nick;
		this.email = email;
		this.qq = qq;
		this.status = status;
		this.type = type;
		this.exattr = exattr;
		this.ctime = ctime;
		this.mtime = mtime;
		this.load_time = load_time;
		
		this.age = age;
		this.sex = sex;
		this.postcode = postcode;
		this.true_name = true_name;
		this.card_code = card_code;
		this.card_positive_img = card_positive_img;
		this.card_back_img = card_back_img;
	}
	
	/**
     * 将用户上传的未知属性写入 exattr
     */
    public void setAttr(String key, Object value) {
	    if (null == exattr) {
	    	exattr = new HashMap<String, Object>();
	    }
	    exattr.put(key, value);
    }
    /**
     * 删除未知属性集合中的某个属性
     */
    public Object delAttr(String key) {
        if (null == exattr) {
            return null;
        }
        return exattr.remove(key);
    }
    /**
     * 得到未知属性集合中的某个属性
     */
    public Object getAttr(String key) {
        if (null == exattr) {
            return null;
        }
        return exattr.get(key);
    }

	
}
