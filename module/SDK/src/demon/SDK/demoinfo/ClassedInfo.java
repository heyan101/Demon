package demon.SDK.demoinfo;

/**
 * 商品类目属性
 * 
 * @author Administrator
 *
 */
public class ClassedInfo {
	
	/**
	 * 类目 ID
	 */
	public Long classed_id;
	/**
	 * 父类目 ID
	 */
	public Long parent_id;
	/**
	 * 类目名
	 */
	public String name;
	/**
	 * 排序，取值范围从小到大[0-100]
	 */
	public int sort;
	/**
	 * 类目访问路径
	 */
	public String link;
}
