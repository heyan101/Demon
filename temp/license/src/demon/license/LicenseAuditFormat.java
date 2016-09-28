package demon.license;

import java.util.Map;

import xserver.service.audit.AuditFormat;

public class LicenseAuditFormat implements AuditFormat{

	public Map<String, String> formatAuditApiName() {
		
		return LicenseConfig.AUDIT_TYPES;
	}

	public String formatAuditParam(String apiName, String params, String Language) {
		String audit = "";
		
		switch (apiName) {
		case LicenseConfig.AUDIT_LICENSE_EXPORT:
			audit += LicenseConfig.getParamMeanings(LicenseConfig.EXPORT_LICENSE_CSV_FILE, Language);
			break;
		}
		
		return audit;
	}

}
