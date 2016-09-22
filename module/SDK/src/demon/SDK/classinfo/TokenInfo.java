package demon.SDK.classinfo;

//@javadoc
import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import demon.utils.Time;

public class TokenInfo {
    
    /**
     * 用户登录凭据
     */
    public String token;
    /**
     * 用户ID
     */
	public long uid;
	
	/**
     * token的有效期限
     */
	public long expires;
	/**
     * 创建时间
     */
	public long ctime;
	/**
     * 客户端IP
     */
	public String ip;
	/**
     * 客户端设备
     */
	public String device;
	
	public TokenInfo(String token, long uid, long expires, long ctime, String ip) {
		this.token = token;
		this.uid = uid;
		this.expires = expires;
		this.ctime = ctime;
		this.ip = ip;
	}
	
	public TokenInfo(String token, long uid, long expires, long ctime, String ip, String device) {
        this.token = token;
        this.uid = uid;
        this.expires = expires;
        this.ctime = ctime;
        this.ip = ip;
        this.device = device;
    }
	
	/**
	 * 新建token信息
	 * @param uid 用户id
	 * @param age token寿命
	 * @param ip 用户的ip地址
	 * @return
	 */
	public static TokenInfo newToken(long uid, long age, String ip) {
		long now = Time.currentTimeMillis();
		return new TokenInfo(makeToken(), uid, now + age, now, ip);
	}
	/**
	 * 新建token信息
	 * @param uid 用户id
	 * @param age token寿命
	 * @param ip 用户的ip地址
	 * @param device 用户登录设备
	 * @param userAgent 用户访问使用的代理
	 * @return
	 */
	public static TokenInfo newToken(long uid, long age, String ip, String device) {
	    long now = Time.currentTimeMillis();
        return new TokenInfo(makeToken() + "@" + uid, uid, now + age, now, ip, device);
    }
	
	/**
	 * 生成token字符串
	 * @return
	 */
	private static String makeToken() {
		long uuid = UUID.randomUUID().getMostSignificantBits();
		byte[] uuidBytes = ByteBuffer.allocate(8).putLong(uuid).array();
		return Base64.encodeBase64URLSafeString(uuidBytes);		
	}
}
