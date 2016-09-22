package demon.pack;

import java.sql.Connection;
import java.sql.SQLException;

import demon.service.db.MySql;

public class PackModel {
	
	private MySql mysql;
	
	// 打包
    private static final String TABLE_PACK = "pack";
    private static final String TABLE_PACK_CHILD = "pack_child";
	
	public PackModel() throws Exception {
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
			String sqlPack = "CREATE TABLE IF NOT EXISTS `" + TABLE_PACK + "` ("
			    + "`pack_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`type` tinyint(1) NOT NULL DEFAULT '0',"
			    + "`is_brand` tinyint NOT NULL DEFAULT '0',"
			    + "`ctime` datetime NOT NULL,"
			    + "PRIMARY KEY (`pack_id`),"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeQuery(sqlPack);
			
			String sqlPackChild = "CREATE TABLE IF NOT EXISTS `" + TABLE_PACK_CHILD + "` ("
			    + "`pack_child_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`pack_id` bigint(20) NOT NULL,"
			    + "`goods_sku_id` bigint(20) NOT NULL,"
			    + "`num` int(8) NOT NULL,"
			    + "`price` decimal(10,4) NOT NULL,"
			    + "`ctime` datetime NOT NULL,"
			    + "PRIMARY KEY (`pack_child_id`),"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeQuery(sqlPackChild);
        } catch (SQLException e) {
            throw new SQLException("SQL create failed...");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
