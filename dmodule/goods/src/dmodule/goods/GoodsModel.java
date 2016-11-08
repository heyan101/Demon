package dmodule.goods;

import java.sql.Connection;
import java.sql.SQLException;

import demon.service.db.MySql;

public class GoodsModel {
	private MySql mysql;
    
    // 商品
    private static final String TABLE_GOODS = "goods";
    private static final String TABLE_GOODS_IMAGE = "goods_image";
    private static final String TABLE_GOODS_INFO = "goods_info";
    private static final String TABLE_GOODS_SKU = "goods_sku";
    
    private static final String TABLE_GOODS_RECYCLE = "goods_recycle";

    public GoodsModel(MySql mysql) throws Exception{
    	this.mysql = mysql;
        initTable();
    }

    /**
     * 创建数据库表
     * @throws SQLException
     */
    private void initTable() throws SQLException {
        Connection conn = this.mysql.getConnection();
        try {
            String sqlGoods = "CREATE TABLE IF NOT EXISTS `" + TABLE_GOODS + "` ("
			    + "`goods_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`brand_id` bigint(20) NOT NULL,"
			    + "`classed_id` bigint(20) NOT NULL,"
			    + "`name` varchar(64) NOT NULL,"
			    + "`code` varchar(64) DEFAULT NULL,"
			    + "`status` tinyint(1) NOT NULL DEFAULT '0',"
			    + "`click` int(8) NOT NULL DEFAULT '0',"
			    + "`sort` int(3) NOT NULL DEFAULT '0',"
			    + "`exattr` varchar(1024) DEFAULT NULL,"
			    + "`ctime` datetime NOT NULL,"
			    + "`mtime` datetime NOT NULL,"
			    + "PRIMARY KEY (`goods_id`)"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeUpdate(sqlGoods);
			
			String sqlGoodsImage = "CREATE TABLE IF NOT EXISTS `" + TABLE_GOODS_IMAGE + "` ("
			    + "`goods_id` bigint(20) NOT NULL,"
			    + "`image_name` varchar(32) DEFAULT NULL,"
			    + "`image_path` varchar(32) NOT NULL,"
			    + "`image_is_first` tinyint(1) NOT NULL DEFAULT '0',"
			    + "`image_ctime` datetime NOT NULL"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlGoodsImage);
			
			String sqlGoodsInfo = "CREATE TABLE IF NOT EXISTS `" + TABLE_GOODS_INFO + "` ("
			    + "`goods_info_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`goods_id` bigint(20) NOT NULL,"
			    + "`sku_id` bigint(20) NOT NULL,"
			    + "`sku_option_id` bigint(20) NOT NULL,"
			    + "`is_sku` tinyint(1) NOT NULL DEFAULT '0',"
			    + "`goods_sku_id` bigint(20) NOT NULL,"
			    + "PRIMARY KEY (`goods_info_id`)"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlGoodsInfo);
			
			String sqlGoodsSku = "CREATE TABLE IF NOT EXISTS `" + TABLE_GOODS_SKU + "` ("
			    + "`goods_sku_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`goods_id` bigint(20) NOT NULL,"
			    + "`num` int(8) NOT NULL,"
			    + "`price` decimal(10,4) NOT NULL,"
			    + "`sku_code` varchar(64) NOT NULL,"
			    + "`sku_ctime` datetime NOT NULL,"
			    + "PRIMARY KEY (`goods_sku_id`)"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlGoodsSku);
			
			String sqlGoodsRecycle = "CREATE TABLE IF NOT EXISTS `" + TABLE_GOODS_RECYCLE + "` ("
			    + "`goods_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`brand_id` bigint(20) NOT NULL,"
			    + "`classed_id` bigint(20) NOT NULL,"
			    + "`name` varchar(64) NOT NULL,"
			    + "`code` varchar(64) DEFAULT NULL,"
			    + "`status` tinyint(1) NOT NULL DEFAULT '0',"
			    + "`click` int(8) NOT NULL DEFAULT '0',"
			    + "`sort` int(3) NOT NULL DEFAULT '0',"
			    + "`exattr` varchar(1024) DEFAULT NULL,"
			    + "`ctime` datetime NOT NULL,"
			    + "`mtime` datetime NOT NULL,"
			    + "PRIMARY KEY (`goods_id`)"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlGoodsRecycle);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("SQL create failed...");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}