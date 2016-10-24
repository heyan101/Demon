package dmodule.SDK.demoinfo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class GoodsInfo {

	/**
	 * 品牌 ID
	 */
	public Long brand_id;
	/**
	 * 商品类目 ID
	 */
	public Long calssed_id;
	/**
	 * 商品 ID
	 */
	public Long goods_id;
	/**
	 * 商品名
	 */
	public String name;
	/**
	 * 商品编号
	 */
	public String code;
	/**
	 * 商品状态
	 */
	public int status;
	/**
	 * 点击次数
	 */
	public int click;
	/**
	 * 排序:[0-100]
	 */
	public int sort;
	/**
	 * 备注
	 */
	public Map<String, String> exattr;
	/**
	 * 入库时间
	 */
	public Timestamp ctime;
	/**
	 * 修改时间
	 */
	public Timestamp mtime;
	/**
	 * 商品图片信息
	 */
	public List<GoodsImageInfo> imageInfo;
}

/**
 * 商品图片
 * 
 * @author Demon
 *
 */
class GoodsImageInfo {
	/**
	 * 图片名
	 */
	public String image_name;
	/**
	 * 图片路径
	 */
	public String image_path;
	/**
	 * 是否主图
	 */
	public int is_first;
	/**
	 * 上传时间
	 */
	public Timestamp ctime;
}
