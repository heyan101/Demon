package demon.license.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenseInfo {
	public long id;
	public String license;
	public String cname;	// 公司名称
	public String status;	// OK INVALID EXPIRED EXPIRE_SOON
	public String persistence;	// 有效期
	public String create_time;	// 生效期
	public String end_time;		// 到期时间 = 生效期 + 有效期
	public long creater_id;		// uid
	public String creater_name;	// uid name
	public String space_quota;	// 空间
	public int users_max;		// 人数
	public int machines_max;	// 最大xserver数量
	public List<String> machine_codes;	// 机器码
	public String security_key;	// 密钥
	public String db_user;	// 数据库用户名
	public String db_password;	// 数据库密码

	@Override
	public String toString() {
		return "LicenseInfo [id=" + id + ", license="
				+ license + ", cname = "+ cname +", status = "+ status +", persistence = "+ persistence +", creater_id = "+ creater_id +", creater_name = "
				+ creater_name +", space_quota = "+ space_quota +", users_max = "+ users_max +", machines_max = "+ machines_max +", machine_codes = "
				+ machine_codes.toString() +", create_time=" + create_time + ", end_time="+ end_time + ", db_user = "+ db_user +", db_password = "
				+ db_password +", security_key = "+ security_key + "]";
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("cname", cname);
		map.put("license", license);
		map.put("status", status);
		map.put("persistence", persistence);
		map.put("create_time", create_time);
		map.put("end_time", end_time);
        map.put("space_quota", space_quota);
        map.put("users_max", users_max);
        map.put("machines_max", machines_max);
        map.put("machine_codes", machine_codes);
        map.put("creater_id", creater_id);
        map.put("creater_name", creater_name);
	    map.put("space_quota", space_quota);
	    map.put("security_key", security_key);
	    map.put("db_user", db_user);
	    map.put("db_password", db_password);
		return map;
		
	}
}
