package dmodule.classed;

import java.sql.Connection;
import java.sql.SQLException;

import demon.service.db.MySql;

public class ClassedModel {

	private MySql mysql;
	
	// 商品类目
    private static final String TABLE_CLASSED = "classed";
    // 类目 SKU
    private static final String TABLE_SKU = "sku";
    private static final String TABLE_SKU_OPTION = "sku_option";
	
	public ClassedModel() throws Exception {
		this.mysql = MySql.getInst(Init.MODULE_NAME);
		initTable();
	}
	
	/**
     * 创建数据库表
     * @throws SQLException
     */
    private void initTable() throws SQLException {
        Connection conn = this.mysql.getConnection();
        try {
            String sqlClass = "CREATE TABLE IF NOT EXISTS `" + TABLE_CLASSED + "` ("
                + "`classed_id` bigint(20) NOT NULL AUTO_INCREMENT,"
                + "`parent_id` bigint(20) NOT NULL,"
                + "`name` varchar(32) NOT NULL,"
                + "`sort` int(3) NOT NULL,"
                + "PRIMARY KEY (`classed_id`),"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeQuery(sqlClass);

            String sqlSKU = "CREATE TABLE IF NOT EXISTS `" + TABLE_SKU + "` ("
                + "`sku_id` bigint(20) NOT NULL AUTO_INCREMENT,"
                + "`classed_id` bigint(20) NOT NULL,"
                + "`name` varchar(32) NOT NULL,"
                + "`is_color` tinyint(1) NOT NULL DEFAULT '0',"
                + "`is_enum` tinyint(1) NOT NULL DEFAULT '0',"
                + "`is_input` tinyint(1) NOT NULL DEFAULT '0',"
                + "`is_crux` tinyint(1) NOT NULL DEFAULT '0',"
                + "`is_sale` tinyint(1) NOT NULL DEFAULT '0',"
                + "`is_search` tinyint(1) NOT NULL DEFAULT '0',"
                + "`is_must` tinyint(1) NOT NULL DEFAULT '0',"
                + "`is_checkbox` tinyint(1) NOT NULL DEFAULT '0',"
                + "`status` tinyint(1) NOT NULL DEFAULT '0',"
                + "`sort` int(3) NOT NULL DEFAULT '0',"
                + "`ctime` datetime NOT NULL,"
                + "PRIMARY KEY (`sku_id`),"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeQuery(sqlSKU);
                
			String sqlSkuOption = "CREATE TABLE IF NOT EXISTS `" + TABLE_SKU_OPTION + "` ("
			    + "`sku_option_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`sku_id` bigint(20) NOT NULL,"
			    + "`classed_id` bigint(20) NOT NULL,"
			    + "`name` varchar(32) NOT NULL,"
			    + "`status` tinyint(1) NOT NULL DEFAULT '0',"
			    + "`sort` int(3) NOT NULL DEFAULT '0',"
			    + "`ctime` datetime NOT NULL,"
			    + "PRIMARY KEY (`sku_option_id`),"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeQuery(sqlSkuOption);
        } catch (SQLException e) {
            throw new SQLException("SQL create failed...");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
