package demon.license;


import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import demon.license.pojo.LicenseInfo;
import demon.license.pojo.PageBean;
import xmodule.SDK.SdkCenter;
import xmodule.SDK.http.AuthedJsonProtocol;
import xmodule.SDK.http.AuthedJsonReq;
import xmodule.SDK.inner.IBeans;
import xmodule.core.CoreConfig;
import xserver.Config;
import xserver.exception.LogicalException;
import xserver.service.http.ApiGateway;
import xserver.service.http.protocol.JsonResp;
import xserver.service.http.protocol.RetStat;
import xserver.utils.ServletUtil;
import xserver.utils.StringUtils;
import xserver.utils.Time;

public class LicenseHttpApi {
	private LicenseApi licenseApi;
	private IBeans beans;

	public LicenseHttpApi() throws Exception {
	    this.licenseApi = new LicenseApi();
	    this.beans = (IBeans)SdkCenter.getInst().queryInterface(IBeans.name, SdkCenter.ToString() + CoreConfig.ToString() + Config.ToString());
	}
	
	

	 /**
     * 生成license
     * @param req
     * @return License
     * @throws Exception
     */
    @ApiGateway.ApiMethod(protocol=AuthedJsonProtocol.class)
    public JsonResp createLicense(AuthedJsonReq req) throws Exception{        
        // 检查权限
        this.beans.getRoleCore().checkRight(req.env, Init.MODULE_NAME, LicenseConfig.RIGHT_LICENSE_CREATE.getValue0(), req.loginInfo.userInfo.uid, req.loginInfo.userInfo.cid);

        LicenseInfo params = new LicenseInfo();
        params.cname = req.paramGetString("cname", true, true);
        params.create_time = req.paramGetString("start_time", true, true);
        params.persistence = req.paramGetString("persistence", true, true);
        params.space_quota = req.paramGetString("space_quota", true, true);
        params.users_max = req.paramGetInteger("users_max", true);
        params.machines_max = req.paramGetInteger("machines_max", true);
        params.machine_codes = req.paramGetStrList("machine_codes", true, true, true);
        params.creater_id = req.loginInfo.userInfo.uid;
        params.creater_name = req.loginInfo.userInfo.name;
       
        LicenseInfo info = this.licenseApi.create(req.env, params);
        
        JsonResp resp = new JsonResp(RetStat.OK);
        resp.resultMap.putAll(info.toMap());
		return resp;
    }


    /**
     * 查询 license
     * @param req
     * @return License
     * @throws Exception
     */
    @ApiGateway.ApiMethod(protocol=AuthedJsonProtocol.class)
    public JsonResp searchLicense(AuthedJsonReq req) throws Exception{
    	// 检查权限
    	this.beans.getRoleCore().checkRight(req.env, Init.MODULE_NAME, LicenseConfig.RIGHT_LICENSE_QUERY.getValue0(), req.loginInfo.userInfo.uid, req.loginInfo.userInfo.cid);

    	Long pageIndex = req.paramGetNumber("pageIndex", true,false);
    	Long pageSize = req.paramGetNumber("pageSize", true,false);
    	PageBean pageBean=new PageBean();
    	pageBean.setPageIndex(pageIndex);
    	pageBean.setPageSize(pageSize);
    	
    	String license = req.paramGetString("license", false);
    	String status = req.paramGetString("status", false);
    	String creater_name = req.paramGetString("creater_name", false);
    	String space_quota = req.paramGetString("space_quota", false);
    	Integer machines_max = req.paramGetInteger("machines_max", false);
    	String machine_code = req.paramGetString("machine_code", false);
    	String cname = req.paramGetString("cname", false);
    	String create_time = req.paramGetString("create_time", false);
    	String end_time_lower = req.paramGetString("end_time_lower", false);
    	String end_time_upper = req.paramGetString("end_time_upper", false);
    	Integer users_max = req.paramGetInteger("users_max", false);

    	LinkedList<LicenseInfo> licenseList = new LinkedList<>();
    	licenseList = this.licenseApi.query(req.env,pageBean,  license, status,  creater_name, space_quota, machines_max, machine_code, cname, create_time, end_time_lower, end_time_upper, users_max);
    	JsonResp resp = new JsonResp(RetStat.OK);
    	resp.resultMap.put("result", licenseList);//把查询到的list放到JsonResp中。
    	return resp;   	
    }
    
    @ApiGateway.ApiMethod(protocol=AuthedJsonProtocol.class)
    public JsonResp createTestLicense(AuthedJsonReq req) throws LogicalException, Exception {
    	// 检查权限
        this.beans.getRoleCore().checkRight(req.env, Init.MODULE_NAME, LicenseConfig.RIGHT_LICENSE_CREATE.getValue0(), req.loginInfo.userInfo.uid, req.loginInfo.userInfo.cid);
        
        String hardware = req.paramGetString("hardware", true);
        Map<String, Object> map = LicenseApi.createTestlicense(req.env, hardware);
        JsonResp resp = new JsonResp(RetStat.OK);
        resp.resultMap = map;
        return resp;
    }
    
    /**
     * 列举 license
     * @param req
     * @return License
     * @throws Exception
     */
    @ApiGateway.ApiMethod(protocol=AuthedJsonProtocol.class)
    public JsonResp list (AuthedJsonReq req) throws LogicalException, Exception{
    	// 检查权限
    	this.beans.getRoleCore().checkRight(req.env, Init.MODULE_NAME, LicenseConfig.RIGHT_LICENSE_QUERY.getValue0(), req.loginInfo.userInfo.uid, req.loginInfo.userInfo.cid);
    	
    	Long pageIndex = req.paramGetNumber("pageIndex", true,false);
    	Long pageSize = req.paramGetNumber("pageSize", true,false);
    	PageBean pageBean=new PageBean();
    	pageBean.setPageIndex(pageIndex);
    	pageBean.setPageSize(pageSize);
    	List<LicenseInfo> list = this.licenseApi.list(pageBean);
    	// System.out.println("list:" + list.size());
    	JsonResp resp = new JsonResp(RetStat.OK);
    	resp.resultMap.put("result", list);
    	return resp;
    }
    
    /**
     * 导出EXCEL表格
     * @param req
     * @return
     * @throws Exception 
     */
    @ApiGateway.ApiMethod(protocol=AuthedJsonProtocol.class, needAudit=true)
    public JsonResp adminExportExcel(AuthedJsonReq req) throws Exception{
    	// 检查权限
        this.beans.getRoleCore().checkRight(req.env, Init.MODULE_NAME, LicenseConfig.RIGHT_LICENSE_EXPORT.getValue0(), req.loginInfo.userInfo.uid, req.loginInfo.userInfo.cid);
        
    	String userName = req.loginInfo.userInfo.name;
		
		PageBean pageBean=null ;
    	List<LicenseInfo> list = this.licenseApi.list(pageBean);
    	String contDisp = ServletUtil.makeContDisp(req.env.request.getHeader("User-Agent"), LicenseConfig.getParamMeanings(LicenseConfig.EXPORT_CSV_LICENSE, ""));
        req.env.response.addHeader("Content-Disposition", contDisp);
        req.env.response.setContentType("application/x-download");
        req.env.response.setHeader("Accept-Ranges", "bytes");
        
        StringBuffer sb = new StringBuffer();
        sb.append(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
        
        String t1 = LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_EXPORTOR, req.env.Language, userName);
        String t2 = LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_EXPORT_TIME, req.env.Language, Time.getDateTimeStr());
        String t3 = LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_EXPORT_COUNT, req.env.Language, list != null ? list.size() : 1 );
        sb.append(t1 + "\n");
        sb.append(t2 + "\n");
        sb.append(t3 + "\n");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_INDEX, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_CNAME, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_STATUS, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_PERSISTENCE, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_CREATE_TIME, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_END_TIME, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_SPACE, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_USERS, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_MACHINES, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_MACHINE_CODES, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_CREATER_ID, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_CREATER_NAME, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_LICENSE, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_SECURITY_KEY, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_DBUSER, req.env.Language) + ",");
        sb.append(LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_DBPASSWORD, req.env.Language) + "\n");
        
        OutputStream os = req.env.response.getOutputStream();
        
        os.write(sb.toString().getBytes());
        LicenseInfo licenseInfo = null;
        for (int i = 0; i < list.size(); i++) {
            licenseInfo = list.get(i);
            String id = Integer.toString(i + 1);
            String cname = licenseInfo.cname;
            String status = licenseInfo.status;
            switch (licenseInfo.status) {
			case LicenseConfig.EXPIRED:
				status = LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_STATUS_EXPIRED, req.env.Language, "");
				break;

			case LicenseConfig.EXPIRE_SOON:
				status = LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_STATUS_EXPIRE_SOON, req.env.Language, "");
				break;
				
			case LicenseConfig.INVALID:
				status = LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_STATUS_INVALID, req.env.Language, "");
				break;
				
			case LicenseConfig.OK:
				status = LicenseConfig.getParamMeanings(LicenseConfig.LICENSE_STATUS_OK, req.env.Language, "");
				break;
				
			default:
				break;
			}
            String persistence = licenseInfo.persistence;
            String create_time = "1970-01-01 ".equals(licenseInfo.create_time)?null:licenseInfo.create_time;
            String end_time = licenseInfo.end_time;
            String space_quota = licenseInfo.space_quota;
            int users_max = licenseInfo.users_max;
            int machines_max = licenseInfo.machines_max;
            String codes = licenseInfo.machine_codes.toString().substring(1, licenseInfo.machine_codes.toString().length() - 1);
    		String[] str = codes.split(",");
    		String machine_codes = str[0];
    		if (str.length > 1) {
    			for (int j = 1; j < str.length; j++) {
    				machine_codes = String.format("%s%s%s", machine_codes, ";", str[j]);
    			}
    		}
            Long creater_id = licenseInfo.creater_id;
            String careter_name = licenseInfo.creater_name;
            String license = licenseInfo.license;
            String security_key = licenseInfo.security_key;
            String db_user = licenseInfo.db_user;
            String db_password = licenseInfo.db_password;
            
            sb = sb.delete(0, sb.length());
            sb.append(id + ",");
            sb.append(cname + ",");
            sb.append(status + ",");
            sb.append(persistence + ",");
            sb.append(create_time + ",");
            sb.append(end_time + ",");
            sb.append(space_quota + ",");
            sb.append(users_max + ",");
            sb.append(machines_max + ",");
            sb.append(machine_codes + ",");
            sb.append(creater_id + ",");
            sb.append(careter_name + ",");
            sb.append(license + ",");
            sb.append(security_key + ",");
            sb.append(db_user + ",");
            sb.append(db_password + "\n");
            
            
            os.write(sb.toString().getBytes());
        }
        os.flush();
        os.close();
        req.env.stat = RetStat.OK;
        return null;
    }
    
    @ApiGateway.ApiMethod(protocol=AuthedJsonProtocol.class)
    public JsonResp encryptKey(AuthedJsonReq req) throws Exception{ 
        String key = req.paramGetString("key", true, true);
        JsonResp resp = new JsonResp(RetStat.OK);
        resp.resultMap.put("value", StringUtils.sortString(key));
        return resp;
    }
    
    @ApiGateway.ApiMethod(protocol=AuthedJsonProtocol.class)
    public JsonResp decryptKey(AuthedJsonReq req) throws Exception{ 
        String key = req.paramGetString("key", true, true);
        JsonResp resp = new JsonResp(RetStat.OK);
        resp.resultMap.put("value", StringUtils.reverseString(key));
        return resp;
    }
  
}
