package demon.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import demon.SDK.demoinfo.TokenInfo;
import demon.SDK.inner.IAuthApi;
import demon.service.db.MySql;

public class AuthModel implements IAuthApi.IAuthModel {
	
	private MySql mysql;
	
	private static final String TABLE_LOGIN_ID = "login_id";
	private static final String TABLE_TOKEN = "token";
	
	public AuthModel(MySql mysql) throws SQLException {
		this.mysql = mysql;
		
		initTable();
	}
	
	private void initTable() throws SQLException {
        Connection conn = this.mysql.getConnection();
        try {
			String sqlLoginId = "CREATE TABLE IF NOT EXISTS `" + TABLE_LOGIN_ID + "` ("
				+ "`uid` bigint(20) NOT NULL,"
				+ "`type` varchar(16) NOT NULL,"
				+ "`value` varchar(16) NOT NULL"
	            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlLoginId);

			String sqlToken = "CREATE TABLE IF NOT EXISTS `" + TABLE_TOKEN + "` ("
                + "`token` varchar(128) PRIMARY KEY,"
                + "`uid` bigint(20) UNSIGNED NOT NULL,"
                + "`expires` datetime NOT NULL,"
                + "`ctime` datetime NOT NULL,"
                + "`ip` varchar(32) DEFAULT NULL,"
                + "`device` varchar(8) DEFAULT NULL"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeUpdate(sqlToken);
		} catch (SQLException e) {
			throw new SQLException("SQL create failed...");
		} finally {
		    if (conn != null) {
		        conn.close();
		    }
		}
	}
	
	/**********************************************************************************************************/
	/**
	 * 验证登录 Id
	 * @return 用户 uid
	 */
	public Long checkLoginId(String type, String value) throws SQLException {
		if (null == type || value == null) {
            throw new IllegalArgumentException();
        }
        Connection conn = null;
        try {
            conn = this.mysql.getConnection();

            String sql = "SELECT `uid` FROM `" + TABLE_LOGIN_ID + "login_id` WHERE `type` = ? and `value` = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            pstmt.setString(2, value);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
	}
	
	public boolean addToken(TokenInfo tokenInfo) throws SQLException {
        if (tokenInfo == null) {
            throw new IllegalArgumentException();
        }
        Connection conn = null;
        try {
            conn = this.mysql.getConnection();
            String sql = "INSERT INTO `" + TABLE_TOKEN + "` (`token`, `uid`, `expires`, `ctime`, `ip`, `device`) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tokenInfo.token);
            pstmt.setLong(2, tokenInfo.uid);
            pstmt.setTimestamp(3, tokenInfo.expires);
            pstmt.setTimestamp(4, tokenInfo.ctime);
            pstmt.setString(5, tokenInfo.ip);
            pstmt.setString(6, tokenInfo.device);
            
            return pstmt.executeUpdate() == 1 ? true : false;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
	
}
