package demon.SDK.classinfo;

import java.sql.Timestamp;
import java.util.List;

/**
 * 类目 SKU 属性
 * @author Demon
 *
 */
public class ClassedSKUInfo {
	
	/**
	 * SKU ID
	 */
	public Long sku_id;
	/**
	 * 属性名
	 */
	public String name;
	/**
	 * 类目 SKU 属性值集合
	 */
	public List<ClassedSKUOptionInfo> sku_option;
	/**
	 * 是否颜色属性
	 */
	public int is_color;
	/**
	 * 是否枚举属性
	 */
	public int is_enum;
	/**
	 * 是否输入属性
	 */
	public int is_input;
	/**
	 * 是否关键属性
	 */
	public int is_crux;
	/**
	 * 是否销售属性
	 */
	public int is_sale;
	/**
	 * 是否搜索属性
	 */
	public int is_search;
	/**
	 * 是否必须
	 */
	public int is_must;
	/**
	 * 是否多选
	 */
	public int is_checkbox;
	/**
	 * 属性状态
	 */
	public int status;
	/**
	 * 排序：[0-100]
	 */
	public int sort;
	/**
	 * 创建时间
	 */
	public Timestamp ctime;
}

/**
 * 类目 SKU 属性值
 * 
 * @author Demon
 *
 */
class ClassedSKUOptionInfo {
	
	/**
	 * SKU 属性值ID
	 */
	public Long sku_id;
	/**
	 * SKU 属性值名
	 */
	public String name;
	/**
	 * SKU 属性值状态<br>
	 */
	public int status;
	/**
	 * 排序:[0-100]
	 */
	public int sort;
	/**
	 * 创建时间
	 */
	public Timestamp ctime;
}
