package demon.license;

public class StringUtil {
	
	//判断一个Str 是否是null或者空。是空为true。否则是false
	public static boolean isEmpty(String str){
		if("".equals(str) || str==null){
			return true;
		}else{
			return false;
		}
	}

	//判断一个str是否不是null和空，既不是null，也不是空的时候，返回true
	public static boolean isNotEmpty(String str){
		if(!"".equals(str)&&str!=null){
			return true;
		}else{
			return false;
		}
	}
}
