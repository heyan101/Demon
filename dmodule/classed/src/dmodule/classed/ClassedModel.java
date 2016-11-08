package dmodule.classed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import demon.service.db.MySql;
import dmodule.SDK.inner.IClassedApi.IClassedModel;

public class ClassedModel implements IClassedModel{

	private MySql mysql;
	
	// 商品类目
    private static final String TABLE_CLASSED = "classed";
    // 类目 SKU
    private static final String TABLE_SKU = "sku";
    private static final String TABLE_SKU_OPTION = "sku_option";
	
	public ClassedModel(MySql mysql) throws Exception {
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
            String sqlClass = "CREATE TABLE IF NOT EXISTS `" + TABLE_CLASSED + "` ("
                + "`classed_id` bigint(20) NOT NULL AUTO_INCREMENT,"
                + "`parent_id` bigint(20) NOT NULL,"
                + "`name` varchar(32) NOT NULL,"
                + "`sort` int(3) NOT NULL DEFAULT '0',"
                + "PRIMARY KEY (`classed_id`)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeUpdate(sqlClass);

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
                + "PRIMARY KEY (`sku_id`)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeUpdate(sqlSKU);
                
			String sqlSkuOption = "CREATE TABLE IF NOT EXISTS `" + TABLE_SKU_OPTION + "` ("
			    + "`sku_option_id` bigint(20) NOT NULL AUTO_INCREMENT,"
			    + "`sku_id` bigint(20) NOT NULL,"
			    + "`classed_id` bigint(20) NOT NULL,"
			    + "`name` varchar(32) NOT NULL,"
			    + "`status` tinyint(1) NOT NULL DEFAULT '0',"
			    + "`sort` int(3) NOT NULL DEFAULT '0',"
			    + "`ctime` datetime NOT NULL,"
			    + "PRIMARY KEY (`sku_option_id`)"
			    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlSkuOption);
        } catch (SQLException e) {
            throw new SQLException("SQL create failed...");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

	@Override
	public Long insertClassed(String classedName, Long parentId) throws SQLException {
		if (classedName == null || classedName.length() < 1) {
			throw new IllegalArgumentException();
		}
		Connection conn = this.mysql.getConnection();
		try {
			// TODO 这里希望返回要插入数据的生成的ID，目前还在测试...
			String sql = "INSERT INTO `" + TABLE_CLASSED + "` (`name`,parent_id) values (?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, classedName);
			pstmt.setLong(2, parentId);
			pstmt.executeUpdate();
			ResultSet result = pstmt.getGeneratedKeys();
			if (result.next()) {
				return result.getLong(1);
			}
			return 0L;
		} finally {
            if (conn != null) {
                conn.close();
            }
        }
	}

	@Override
	public void insertClasseds(List<String> classedNames, Long parentId) throws SQLException {
		if (null == classedNames || classedNames.size() < 1) {
			throw new IllegalArgumentException();
		}
		Connection conn = this.mysql.getConnection();
		conn.setAutoCommit(false); 
		try {
			// TODO 这里希望返回要插入数据的生成的ID，目前还在测试...
			String sql = "INSERT INTO `" + TABLE_CLASSED + "` (`name`,parent_id) values (?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.clearBatch();
			for (String name : classedNames) {
				pstmt.setString(1, name);
				pstmt.setLong(2, parentId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} finally {
            if (conn != null) {
                conn.close();
            }
        }
	}
}
