package demon.utils;

import java.util.HashMap;
import java.util.Map;

import demon.service.http.Env;
import demon.service.http.protocol.ErrTextFormat;

public class MyRetStat implements ErrTextFormat {

	
	/**
     * 错误码集合
     */
    public static final Map<String, String> ERR_TEXT;
    
    static {
        ERR_TEXT = new HashMap<String, String>();
    }
	
	@Override
	public String getErrText(Env env, String stat) {
		return ERR_TEXT.get(stat);
	}

}
