package demon.license;

import systeminfo.OS;
import xserver.service.http.Env;

public class Test {
    public static void main(String[] args) throws Exception {
    	String hardware = OS.getSystemInfo();
    	System.out.println(hardware);
    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), hardware));
    	

//    	System.out.println(StringUtils.sortString("jintianshigehaotianqi"));
//    	System.out.println(StringUtils.reverseString("GV8sJVkCJiMYMyYWXiM-RAopPgEzLj0QuwAAAAA"));
    	
//    	System.out.println(StringUtils.setString("1"));
//    	System.out.println(StringUtils.setString("1"));
//    	System.out.println(StringUtils.setString("1"));
//    	System.out.println(StringUtils.setString("1"));
//    	System.out.println(StringUtils.setString("12345"));
//    	System.out.println(StringUtils.setString("12345"));
//    	System.out.println(StringUtils.setString("12345"));
//    	System.out.println(StringUtils.setString("12345"));
//    	System.out.println(StringUtils.setString("1234567890"));
//    	System.out.println(StringUtils.setString("1234567890"));
//    	System.out.println(StringUtils.setString("1234567890"));
//    	System.out.println(StringUtils.setString("1234567890"));
//
//    	System.out.println(LicenseUtil.genOuterSecurityKey("cloudhua_test"));
//    	System.out.println(LicenseUtil.genOuterSecurityKey("cloudhua_test"));
//    	System.out.println(LicenseUtil.genOuterSecurityKey("cloudhua_test"));
//    	System.out.println(LicenseUtil.genOuterSecurityKey("cloudhua_test"));
//    	
//    	System.out.println(StringUtils.setString("cloudhua_test" + "cloudhua_test".hashCode()));
//    	System.out.println(StringUtils.setString("cloudhua_test" + "cloudhua_test".hashCode()));
//    	System.out.println(StringUtils.setString("cloudhua_test" + "cloudhua_test".hashCode()));
//    	System.out.println(StringUtils.setString("cloudhua_test" + "cloudhua_test".hashCode()));
    	
    	
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
//    	System.out.println(LicenseApi.createTestlicense(new Env("Test"), "zhezhishiyiciceshi"));
    }

}
