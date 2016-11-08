package dmodule.brand;

import java.sql.Connection;
import java.sql.SQLException;

import demon.service.db.MySql;

public class BrandModel {

	private MySql mysql;
	
	// 品牌
    private static final String TABLE_BRAND = "brand";
	
	public BrandModel(MySql mysql) throws Exception {
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
            String sqlBrand = "CREATE TABLE IF NOT EXISTS `" + TABLE_BRAND + "` ("
                + "`brand_id` bigint(20) NOT NULL AUTO_INCREMENT,"
                + "`class_id` bigint(20) NOT NULL,"
                + "`name_zh` varchar(64) DEFAULT NULL,"
                + "`name_en` varchar(64) DEFAULT NULL,"
                + "`logo` varchar(128) NOT NULL,"
                + "`describe` varchar(1024) DEFAULT NULL,"
                + "`offical_address` varchar(128) DEFAULT NULL,"
                + "`ctime` datetime NOT NULL,"
                + "PRIMARY KEY (`brand_id`)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeUpdate(sqlBrand);
        } catch (SQLException e) {
            throw new SQLException("SQL create failed...");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
