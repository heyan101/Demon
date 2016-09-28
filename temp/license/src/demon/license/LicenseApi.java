package demon.license;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import demon.service.timer.TimerTaskCenter.ITask;

/* 生成License的规则
 * 
 * 
 * 
 * 
 * endDate 过期时间：精确到天
 * quotaLimit 空间限制：精确到GB
 * userLimit 用户数限制
 * company 公司名称
 * hardware 硬件信息
 * 
 */
class LicenseApi implements ITask {

	private LicenseModel licenseModel;
    final String mWARNING = "LICENSE_WARNING";
	final String mPROMPT = "LICENSE_PROMPT";
	public LicenseApi() throws Exception {
		this.licenseModel = new LicenseModel();
		
		TimerTaskCenter.getInst().setTask(this, 24 * 60 * 60 , "warner");
		warner();
	}
	
	// 判断是否为数字
//	public static boolean isNumeric(String str) {
//		Pattern pattern = Pattern.compile("[0-9]*");
//		Matcher isNum = pattern.matcher(str);
//		return isNum.matches();
//	}

	public static Map<String, Object> createTestlicense(Env env, String hardware) {
		long endDate = Time.currentTimeMillis() / TimeUnit.VALUE_DAY + 10000;
		String company = "cloudhua_test";
		String license = LicenseUtil.createLicense(endDate, 1024 * 1024, 100000, company, LicenseUtil.genMachineCode(hardware));

        Pair<String, String> pair = LicenseUtil.genDatabaseInfo(company);
        String dbUser = pair.getValue0();
        String dbPwd = pair.getValue1();
        String outerKey = LicenseUtil.genOuterSecurityKey(company);
        String expire = new SimpleDateFormat("yyyy-MM-dd").format(endDate * TimeUnit.VALUE_DAY);
        
        Map<String, Object> map = new HashMap<String, Object>();        
        map.put("license", license);
        map.put("dbUser", dbUser);
        map.put("dbPwd", dbPwd);
        map.put("securityKey", outerKey);
        map.put("expries", expire);
        map.put("user_id", company);
        return map;
    }
	
	public LicenseInfo create(Env env, LicenseInfo params) throws Exception{

		long createTime = Time.parseTimeStr(params.create_time, Time.DEFAULT_DATE_FORMAT);
		long interval = new TimeUnit(params.persistence).value.longValue();
        long endDate = (createTime + interval) / TimeUnit.VALUE_DAY;
        long quota = new BitUnit(params.space_quota).value.longValue() / BitUnit.VALUE_GB;
        if (quota <= 0) {
        	throw new LogicalException(LicenseRetStat.ERR_SPACE_INVALID, "ERR_SPACE_INVALID");        	
        }
		if (params.users_max <= 0 || params.users_max > LicenseConfig.USERS_MAX) {
			throw new LogicalException(LicenseRetStat.ERR_USERS_QUOTA_INVALID, "ERR_USERS_QUOTA_INVALID");
		}
		if (params.machines_max <= 0 || params.machines_max > LicenseConfig.MACHINES_MAX) {
			throw new LogicalException(LicenseRetStat.ERR_MACHINES_QUOTA_INVALID, "ERR_MACHINEs_QUOTA_INVALID");
		}
        // 判断机器码是否合理
        for(String code : params.machine_codes){
            if (code.length() <= 5 ) {
    			throw new LogicalException(LicenseRetStat.ERR_MACHINE_CODE_INVALID, "ERR_MACHINE_CODE_INVALID");
            }
        }
        
        // 判断生成license时，是否过期或即将到期
        if (endDate * TimeUnit.VALUE_DAY <= Time.currentTimeMillis()) {
        	params.status = LicenseConfig.EXPIRED;
        	throw new LogicalException(LicenseRetStat.ERR_CREATE_TIME_INVALID, "ERR_CREATE_TIME_INVALID");
        } else if ( endDate * TimeUnit.VALUE_DAY - LicenseConfig.TEN_DAY <= Time.currentTimeMillis()) {
        	params.status = LicenseConfig.EXPIRE_SOON;
        } else {
        	params.status = LicenseConfig.OK;
        }
        
        // 判断是否有相同License参数
		String hardware = LicenseUtil.genHardware(params.machine_codes);
        LicenseInfo old = this.licenseModel.getActiveLicenseInfoByCname(params.cname);
        if (null != old && params.create_time.equals(old.create_time) && params.persistence.equals(old.persistence) && 
        		params.users_max == old.users_max && params.machines_max == old.machines_max) {
    		String oldHardware = LicenseUtil.genHardware(old.machine_codes);
    		if (hardware.equals(oldHardware)) {
    			return old;
    		}        	
        }
    	
        LicenseInfo info = new LicenseInfo();        
        info.license = LicenseUtil.createLicense(endDate, quota, params.users_max, params.cname, hardware);
        info.cname = params.cname;
		info.status = params.status;
		info.persistence = params.persistence;
		info.create_time =params.create_time;
		info.end_time = genDateString(endDate * TimeUnit.VALUE_DAY);
		info.creater_id = params.creater_id;
		info.creater_name = params.creater_name;
		info.space_quota = params.space_quota;
		info.users_max = params.users_max;
		info.machines_max = params.machines_max;
		info.machine_codes = params.machine_codes;
                
		Pair<String, String> pair = LicenseUtil.genDatabaseInfo(info.cname);
		info.db_user = pair.getValue0();
		info.db_password = pair.getValue1();
		info.security_key = LicenseUtil.genOuterSecurityKey(info.cname);

        this.licenseModel.add(info);//在数据库中插入一条记录
        if (old != null) {
            this.licenseModel.invalidOld(old);
        }    
		return info;
	}

	public LinkedList<LicenseInfo> query(Env env, PageBean pageBean, String license, String status,  String creater_name, String space_quota, Integer machines_max,String machine_code, String cname,String create_time,String end_time_lower,String end_time_upper,Integer users_max) throws Exception {
    	return this.licenseModel.query(pageBean, license, status,  creater_name, space_quota, machines_max, machine_code, cname, create_time, end_time_lower, end_time_upper, users_max );
    }

	public List<LicenseInfo> list(PageBean pageBean) throws Exception {
		return this.licenseModel.list(pageBean);
	}

	private void warner() throws Exception {
		Env env = new Env(Init.MODULE_NAME);
		List<LicenseInfo> checkList = this.licenseModel.getActiveLicenseInfo();
		Long end_time = null;
		IUser iUser = (IUser) SdkCenter.getInst().queryInterface(IUser.name, SdkCenter.ToString() + CoreConfig.ToString() + Config.ToString());
		MsgctrApi msgctrApi = MsgctrApi.getInst();
		Map<String, Object> map_content = new LinkedHashMap<String, Object>();
		for(LicenseInfo licenseInfo : checkList){
			end_time = Time.parseTimeStr(licenseInfo.end_time, Time.DEFAULT_DATE_FORMAT);
			if (end_time <= Time.currentTimeMillis()) {
				if (this.licenseModel.setStatus(licenseInfo, LicenseConfig.EXPIRED)) {
					String subject = "到期提示！";
			        String content = "<p>您所申请的用户："+licenseInfo.cname+" 的license:"+licenseInfo.license +"已到期</p>";
			        Mailbox mailbox = Mailbox.getInst();
			        MailItem mail = new MailItem(subject, content);
			        UserInfo userInfo = iUser.getUserByUid(env, licenseInfo.creater_id);
			        mail.addToEmails(userInfo.email);
			        mailbox.addMail(mail);
			        map_content.put("cname", licenseInfo.cname);
			        map_content.put("end_time", licenseInfo.end_time);
			        String msgContent = JSON.toJSONString(map_content);
			        msgctrApi.saveMessage(env, userInfo.uid, Time.currentTimeMillis(), "system", mPROMPT, msgContent, null);
				}else {
					throw new Exception("Set status failed!");
				}
			} else if (end_time - LicenseConfig.TEN_DAY <= Time.currentTimeMillis()) {//还有10天到期
				if (this.licenseModel.setStatus(licenseInfo, LicenseConfig.EXPIRE_SOON)) {
			        String subject = "到期预警";
			        String content = "<p>您申请的用户："+ licenseInfo.cname +"的license："+ licenseInfo.license +"即将到期。</p>";
			        Mailbox mailbox = Mailbox.getInst();
			        MailItem mail = new MailItem(subject, content);
			        UserInfo userInfo = iUser.getUserByUid(env, licenseInfo.creater_id);
			        mail.addToEmails(userInfo.email);
			        mailbox.addMail(mail);
			        map_content.put("cname", licenseInfo.cname);
			        map_content.put("end_time", licenseInfo.end_time);
			        String msgContent = JSON.toJSONString(map_content);
			        msgctrApi.saveMessage(env, userInfo.uid, Time.currentTimeMillis(), "system", mWARNING, msgContent, null);
				}else {
					throw new Exception("Set status failed!");
				}
			}
		}
	}
	
	private static String genDateString(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	@Override
	public void run() {
		try {
			warner();
		} catch (Exception e) {
			Logger.getInst().acc(e.toString());
		}
	}
}

class LicenseUtil {
    //此类不要轻易修改
	public static final String s_endDate = "endDate";
	public static final String s_quotaLimit = "quotaLimit";
	public static final String s_userLimit = "userLimit";
	public static final String s_company = "company";
	public static final String s_hardware = "hardware";
	
    public static String createLicense(long endDate, long quotaLimit, int userLimit, String company, String hardware) {
        List<Object> strList = new LinkedList<Object>();
        strList.add(endDate);
        strList.add(quotaLimit);
        strList.add(userLimit);
        strList.add(company);
        strList.add(hardware);
        strList.add(Time.currentTimeMillis() % 1000);
//        System.out.println(strList);

        return StringUtils.setString(JSONObject.toJSONString(strList));
    }
    
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
    public static Pair<String, String> genDatabaseInfo(String company) {
        String dbUser = _subString(StringUtils.setString(company), 4, 10);
        String dbPwd = _subString(StringUtils.setString(dbUser), 4, 10);
        return new Pair<String, String>(dbUser, dbPwd);
    }    
    public static String genMachineCode(String machine) {
		return (machine.hashCode() + "").substring(3, 5);    	
    }
    public static String genHardware(List<String> machines) {
		String hardware = "";
		for (String machine : machines) {
			hardware = hardware + LicenseUtil.genMachineCode(machine);
		}
		return hardware;
    }
    private static String _subString(String str, int begin, int len) {
    	int end = begin + len;
    	if (end >= str.length())
    		end = str.length() - 1;
    	return str.substring(begin, end);
    }
    
}
