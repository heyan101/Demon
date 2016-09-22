package demon.SDK;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import demon.Config;
import demon.SDK.event.EventType;
import demon.SDK.event.IListener;
import demon.SDK.inner.IBeans;
import demon.SDK.inner.IEventHub;
import demon.exception.LogicalException;
import demon.service.http.ApiGateway;
import demon.service.http.HttpServer;
import demon.service.log.Logger;
import demon.utils.StringUtils;
    
public class SdkCenter {
    private static SdkCenter s_instance;
    public static SdkCenter getInst() {
        if (null == s_instance) {
        	s_instance = new SdkCenter();
        }
        return s_instance;
    }

    private EventHub mEventHub;
    public SdkCenter() {
        try {
            mEventHub = new EventHub();
            Sdk.sdk.mBeans = new Beans();
            Sdk.sdk.mInterfacePool = new HashMap<String, Object>();
            Sdk.sdk.mOuterInterface = new HashSet<String>();
            this.addInterface(IEventHub.name, mEventHub);
            addOuterInterface();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void addOuterInterface() {
        Sdk sdk = Sdk.sdk;
//            sdk.mOuterInterface.add(IAcl.name);
    }

    public static String ToString() {   // 此方法不许修改，如需重构请申请
        return "demon2.0";
    }

	public Object queryInterface(String interfaceName, String securityKey) throws LogicalException {
		if (null == interfaceName || 0 == interfaceName.length()) {
			throw new LogicalException("INAVLIDATE INTERFACE NAME", "SdkCenter queryInterface error"); 
		}

		String innerKey = SecurityKey.innerKey;
		String outerKey = SecurityKey.outerKey;
		
//    		if (false == innerKey.equals(securityKey))
//                throw new LogicalException("NOT SUPPORTED INTERFACE", interfaceName);

		if (false == innerKey.equals(securityKey) && false == outerKey.equals(securityKey))
		    throw new LogicalException("NOT SUPPORTED INTERFACE", interfaceName);

		if (true == outerKey.equals(securityKey) && false == Sdk.sdk.mOuterInterface.contains(interfaceName))
		    throw new LogicalException("NOT SUPPORTED INTERFACE", interfaceName);

		if (IBeans.name.equals(interfaceName)) {
			return Sdk.sdk.mBeans;
		}

		if (true == Sdk.sdk.mInterfacePool.containsKey(interfaceName))
			return Sdk.sdk.mInterfacePool.get(interfaceName);

		throw new LogicalException("NOT SUPPORTED INTERFACE", interfaceName);
	}

	public void addInterface(String interfaceName, Object interfaceObject) throws LogicalException {
		if (null == interfaceName || 0 == interfaceName.length()) {
			throw new LogicalException("INAVLIDATE INTERFACE NAME", "SdkCenter addInterface error"); 
		}
		if (null == interfaceObject) {
	        System.err.println(interfaceName);
			throw new LogicalException("NULL INSTANCE OBJECT", "SdkCenter addInterface error"); 
		}
		if (true == Sdk.sdk.mInterfacePool.containsKey(interfaceName)) {
	        System.err.println(interfaceName + " " + interfaceObject.getClass().getName());
			throw new LogicalException("INSTANCE OBJECT EXIST", "SdkCenter addInterface error");
		}
		Sdk.sdk.mInterfacePool.put(interfaceName, interfaceObject);
	}

	public void registListener(EventType eventType, IListener listener) {
		mEventHub.registListener(eventType, listener);
	}
	
	public void registHttpApi(String moduleName, Object apiObject) throws Exception {
		if (null != moduleName && moduleName.length() > 0 && null != apiObject)
			HttpServer.getInst(moduleName).registApiService("api", new ApiGateway(apiObject, moduleName, Logger.getInst(moduleName)));
	}
}

// 内部获取对象实例的方法
class Beans implements IBeans {      
//    private IAdApi adApi = null;
//    public IAdApi getAdApi() throws LogicalException {
//        if (null == this.adApi) {
//            this.adApi = (IAdApi) SdkCenter.getInst().queryInterface(IAdApi.name, SecurityKey.innerKey);
//        }
//        return this.adApi;
//    }
}

class Sdk {
    public static Sdk sdk = new Sdk();

    public HashMap<String, Object> mInterfacePool;
    public HashSet<String> mOuterInterface;
    public IBeans mBeans;
}

class SecurityKey {
    public final static String innerKey = "demon2.0" + _inner("middle") + "P@ssw0rd";
    public final static String outerKey = _outer();
    
    private static String _inner(String key) {
        return "InnerKey";
    }

    private static String _outer() {
        String license = Config.get("demon.license");
        return LicenseUtil.genOuterSecurityKey(LicenseUtil.parseLicense(license).get(LicenseUtil.s_company).toString());
    }
}

class LicenseUtil {
    //此类不要轻易修改
	public static final String s_endDate = "endDate";
	public static final String s_quotaLimit = "quotaLimit";
	public static final String s_userLimit = "userLimit";
	public static final String s_company = "company";
	public static final String s_hardware = "hardware";
	
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseLicense(String license) {
        List<Object> list = (List<Object>) JSONObject.parse(StringUtils.getString(license));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(s_endDate, list.get(0));
        map.put(s_quotaLimit, list.get(1));
        map.put(s_userLimit, list.get(2));
        map.put(s_company, list.get(3));
        map.put(s_hardware, list.get(4));
        list.clear();

        return map;
    }

    public static String genOuterSecurityKey(String company) {
        String key = StringUtils.setString(company.hashCode() + company);
        return _subString(key, 4, 10);
    }
    
    private static String _subString(String str, int begin, int len) {
    	int end = begin + len;
    	if (end >= str.length())
    		end = str.length() - 1;
    	return str.substring(begin, end);
    }
}

