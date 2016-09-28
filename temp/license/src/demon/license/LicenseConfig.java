package demon.license;
import java.util.HashMap;
import java.util.Map;

/**
 * 这块的代码是admin账户登录到后台进行授权的时候使用的。
 */
import org.javatuples.Pair;

import xserver.utils.LanguageUtil;
import xserver.utils.unit.TimeUnit;

public class LicenseConfig {
	
    /**
     * 生成license需要该权限
     */
    public static final Pair<String, String> RIGHT_LICENSE_CREATE = new Pair<String, String>("RIGHT_LICENSE_CREATE", "生成license");
   
    /**
     * 查询license需要该权限
     */
    public static final Pair<String, String> RIGHT_LICENSE_QUERY = new Pair<String, String>("RIGHT_LICENSE_QUERY", "查询license");
    
    
    /**
     * 导出license需要该权限
     */
    public static final Pair<String, String> RIGHT_LICENSE_EXPORT = new Pair<String, String>("RIGHT_LICENSE_EXPORT", "导出license");

    /**
     * license作废
     */
	public static final String INVALID = "INVALID";
	
	 /**
     * license正常
     */
	public static final String OK = "OK";
	
	 /**
     * license过期
     */
	public static final String EXPIRED = "EXPIRED";
	
	 /**
     * license即将过期
     */
	public static final String EXPIRE_SOON = "EXPIRE_SOON";

	/**
     * license状态正常
     */

	public static final long TEN_DAY = new TimeUnit("10d").value.longValue();

	public static final int USERS_MAX = 10000000;	// 1000万

	public static final int MACHINES_MAX = 20;

	public static final String LICENSE_EXPORTOR = "LICENSE_EXPORTOR";

	public static final String LICENSE_EXPORT_TIME = "LICENSE_EXPORT_TIME";

	public static final String LICENSE_EXPORT_KEYWORD = "LICENSE_EXPORT_KEYWORD";

	public static final String LICENSE_EXPORT_COUNT = "LICENSE_EXPORT_COUNT";

	public static final String LICENSE_INDEX = "LICENSE_INDEX";

	public static final String LICENSE_CNAME = "LICENSE_CNAME";

	public static final String LICENSE_STATUS = "LICENSE_STATUS";

	public static final String LICENSE_PERSISTENCE = "LICENSE_PERSISTENCE";

	public static final String LICENSE_CREATE_TIME = "LICENSE_CREATE_TIME";

	public static final String LICENSE_END_TIME = "LICENSE_END_TIME";
	
	public static final String LICENSE_SPACE = "LICENSE_SPACE";

	public static final String LICENSE_USERS = "LICENSE_USERS";

	public static final String LICENSE_MACHINES = "LICENSE_MACHINES";

	public static final String LICENSE_MACHINE_CODES = "LICENSE_MACHINE_CODES";

	public static final String LICENSE_CREATER_ID = "LICENSE_CREATER_ID";

	public static final String LICENSE_CREATER_NAME = "LICENSE_CREATER_NAME";

	public static final String LICENSE_LICENSE = "LICENSE_LICENSE";

	public static final String LICENSE_SECURITY_KEY = "LICENSE_SECURITY_KEY";

	public static final String LICENSE_DBUSER = "LICENSE_DBUSER";

	public static final String LICENSE_DBPASSWORD = "LICENSE_DBPASSWORD";

	public static final String LICENSE_STATUS_EXPIRED = "LICENSE_STATUS_EXPIRED";

	public static final String LICENSE_STATUS_INVALID = "LICENSE_STATUS_INVALID";

	public static final String LICENSE_STATUS_EXPIRE_SOON = "LICENSE_STATUS_EXPIRE_SOON";

	public static final String LICENSE_STATUS_OK = "LICENSE_STATUS_OK";

	public static final String EXPORT_CSV_LICENSE = "EXPORT_CSV_LICENSE"; 
	
	public static final String EXPORT_LICENSE_CSV_FILE = "EXPORT_LICENSE_CSV_FILE";
	
	 public static String getParamMeanings(String item, String Language, Object... params) {
	        switch (item) {
	        
	        case LICENSE_EXPORTOR :
	        case LICENSE_EXPORT_TIME :
	        case LICENSE_EXPORT_KEYWORD :
	        case LICENSE_EXPORT_COUNT : 
	        	return String.format(LanguageUtil.getInst().getText(item, Language), params);
	        
	        case EXPORT_CSV_LICENSE :
	        case LICENSE_INDEX :
	        case LICENSE_CNAME :
	        case LICENSE_STATUS:
	        case LICENSE_PERSISTENCE:
	        case LICENSE_CREATE_TIME:
	        case LICENSE_END_TIME:
	        case LICENSE_SPACE:
	        case LICENSE_USERS:
	        case LICENSE_MACHINES:
	        case LICENSE_MACHINE_CODES:
	        case LICENSE_CREATER_ID:
	        case LICENSE_CREATER_NAME:
	        case LICENSE_LICENSE:
	        case LICENSE_SECURITY_KEY:
	        case LICENSE_DBUSER:
	        case LICENSE_DBPASSWORD:
	        case LICENSE_STATUS_EXPIRED :
	        case LICENSE_STATUS_INVALID:
	        case LICENSE_STATUS_EXPIRE_SOON:
	        case LICENSE_STATUS_OK:
	        case EXPORT_LICENSE_CSV_FILE: 
	        	return LanguageUtil.getInst().getText(item, Language);
	        
	        default : return null;
	        }
	    }
	 
	 //审计信息
	 public static final String AUDIT_LICENSE_EXPORT = "adminExportExcel";

     public static final Map<String, String> AUDIT_TYPES;
     static {
    	 AUDIT_TYPES = new HashMap<String, String>();
    	 AUDIT_TYPES.put(AUDIT_LICENSE_EXPORT, "license_adminExportExcel");
     }
	    
}
