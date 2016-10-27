package dmodule.imageCode;

/**
 * 实现ErrTextFormat接口<br>
 * 提供错误返回码，及其相应的解析文本
 * 
 * @author Demon
 */
public class ImageCodeRetStat {
    
    /**
     * 验证码过期
     */
    public static final String ERR_VALIDATE_CODE_EXPIRED = "ERR_VALIDATE_CODE_EXPIRED";
    
    /**
     * 验证码错误
     */
    public static final String ERR_INVALID_VALIDATE_CODE = "ERR_INVALID_VALIDATE_CODE";
    
    public static String getMsgByStat(String stat, Object... params) {
        
        switch (stat) {
        default : return null;
        }
        
    }
    
//    public static final Map<String, String> ERR_TEXT;
//    static {
//        ERR_TEXT = new HashMap<String, String>();
//        
//        ERR_TEXT.put(ERR_VALIDATE_CODE_EXPIRED, "验证码过期");
//        ERR_TEXT.put(ERR_INVALID_VALIDATE_CODE, "验证码错误");
//    }
    
}
