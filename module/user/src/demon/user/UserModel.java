package demon.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import demon.SDK.classinfo.TokenInfo;
import demon.SDK.classinfo.UserInfo;
import demon.service.db.MySql;
import demon.utils.Time;

public class UserModel {

	protected MySql mysql;
	
	private static final String TABLE_USER = "user";
	private static final String TABLE_LOGIN_ID = "login_id";
	private static final String TABLE_ADDRESS = "address";
	private static final String TABLE_TOKEN = "token";
	private static final String TABLE_ID_CARD = "id_card";
//	private static final String TABLE_USER_IMAGE = "user_image";
	private static final String TABLE_USER_RECYCLE = "user_recycle";
	
	public UserModel(MySql mysql) throws SQLException {
		this.mysql = mysql;
		
		initTable();
	}
	
	private void initTable() throws SQLException {
        Connection conn = this.mysql.getConnection();
        try {
			String sqlUser = "CREATE TABLE IF NOT EXISTS `" + TABLE_USER + "` ("
				+ "`uid` bigint(11) NOT NULL AUTO_INCREMENT,"
				+ "`name` varchar(20) DEFAULT NULL,"
				+ "`phone` int(11) DEFAULT NULL,"
				+ "`email` varchar(64) DEFAULT NULL,"
				+ "`nick` varchar(64) DEFAULT NULL,"
				+ "`password` varchar(20) NOT NULL,"
				+ "`qq` int(13) DEFAULT NULL,"
				+ "`type` int(1) NOT NULL DEFAULT 1,"
				+ "`status` int(1) NOT NULL DEFAULT 1,"
				+ "`exattr` varchar(10240) DEFAULT NULL,"
				+ "`ctime` datetime NOT NULL,"
				+ "`mtime` datetime DEFAULT NULL,"
				+ "`load_time` datetime DEFAULT NULL,"
				+ "PRIMARY KEY (`uid`)"
	            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlUser);
			
			String sqlLoginId = "CREATE TABLE IF NOT EXISTS `" + TABLE_LOGIN_ID + "` ("
				+ "`uid` bigint(20) NOT NULL,"
				+ "`type` varchar(16) NOT NULL,"
				+ "`value` varchar(16) NOT NULL"
	            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlLoginId);

			String sqlAddress = "CREATE TABLE IF NOT EXISTS `" + TABLE_ADDRESS + "` ("
				+ "`uid` bigint(20) NOT NULL,"
				+ "`address_1` varchar(255) DEFAULT NULL,"
				+ "`address_2` varchar(255) DEFAULT NULL,"
				+ "`address_3` varchar(255) DEFAULT NULL,"
				+ "`address_4` varchar(255) DEFAULT NULL,"
				+ "`address_5` varchar(255) DEFAULT NULL"
	            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlAddress);

			String sqlToken = "CREATE TABLE IF NOT EXISTS `" + TABLE_TOKEN + "` ("
                + "`token` varchar(128) PRIMARY KEY,"
                + "`uid` bigint(20) UNSIGNED NOT NULL,"
                + "`expires` datetime NOT NULL,"
                + "`ctime` datetime NOT NULL,"
                + "`ip` varchar(32) DEFAULT NULL,"
                + "`device` varchar(8) DEFAULT NULL"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeUpdate(sqlToken);

            String sqlIdCard = "CREATE TABLE IF NOT EXISTS `" + TABLE_ID_CARD + "` ("
                + "`uid` bigint(20) UNSIGNED NOT NULL,"
                + "`age` int(3) DEFAULT NULL,"
				+ "`sex` tinyint(1) DEFAULT NULL,"
				+ "`city` varchar(32) DEFAULT NULL,"
				+ "`postcode` int(6) DEFAULT NULL,"
				+ "`true_name` varchar(32) NOT NULL,"
				+ "`card_code` varchar(20) NOT NULL,"
				+ "`card_positive_img` varchar(256) NOT NULL,"
				+ "`card_back_img` varchar(256) NOT NULL"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            conn.createStatement().executeUpdate(sqlIdCard);
	            
//			sql = "CREATE TABLE IF NOT EXISTS `" + TABLE_USER_IMAGE + "` ("
//                + "`uid` bigint(20) NOT NULL,"
//                + "`image` mediumblob DEFAULT NULL,"
//                + "`ctime` bigint(20) NOT NULL,"
//                + "`mtime` bigint(20) DEFAULT NULL,"
//                + "KEY (`uid`)"
//	            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
//            conn.createStatement().executeUpdate(sql);
            
            String sqlUserRecycle = "CREATE TABLE IF NOT EXISTS `" + TABLE_USER_RECYCLE + "` ("
				+ "`uid` bigint(11) NOT NULL AUTO_INCREMENT,"
				+ "`name` varchar(20) DEFAULT NULL,"
				+ "`phone` int(11) DEFAULT NULL,"
				+ "`email` varchar(64) DEFAULT NULL,"
				+ "`nick` varchar(64) DEFAULT NULL,"
				+ "`password` varchar(20) NOT NULL,"
				+ "`qq` int(13) DEFAULT NULL,"
				+ "`type` int(1) NOT NULL DEFAULT 1,"
				+ "`status` int(1) NOT NULL DEFAULT 1,"
				+ "`exattr` varchar(10240) DEFAULT NULL,"
				+ "`ctime` datetime NOT NULL,"
				+ "`mtime` datetime DEFAULT NULL,"
				+ "`load_time` datetime DEFAULT NULL,"
				+ "PRIMARY KEY (`uid`)"
	            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			conn.createStatement().executeUpdate(sqlUserRecycle);
				
		} catch (SQLException e) {
			throw new SQLException("SQL create failed...");
		} finally {
		    if (conn != null) {
		        conn.close();
		    }
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public UserInfo getUserInfoByUid(Long uid) throws SQLException {
		if (uid <= 0) {
            throw new IllegalArgumentException();
        }
		Connection conn = this.mysql.getConnection();
		try {

            String sqlGetUser = "SELECT `uid`,`phone`,`nickname`,`password`,`age`,`sex`,`email`,`qq`,`status`,"
            		+ "`type`,`ctime`,`exattr`,`mtime`,`load_time` FROM `" + TABLE_USER + "` WHERE `uid` = ?";
			PreparedStatement pstmt = conn.prepareStatement(sqlGetUser);
            pstmt.setLong(1, uid);
            ResultSet rs = pstmt.executeQuery();

            UserInfo user = null;
            if (rs.next()) {
                String attrStr = rs.getString(11);
                Map<String, Object> exattr = null;
                if (null != attrStr) {
                	exattr = JSONObject.parseObject(attrStr, Map.class);
                }
                
//                user = new UserInfo(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
//                        rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getLong(8), rs.getInt(9), 
//                        rs.getString(10), exattr, rs.getLong(12), rs.getLong(13));
            }

            return user;

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public UserInfo queryUserInfoByNamePassword(String name, String password) throws SQLException {
		Connection conn = this.mysql.getConnection();
		try {
			String sql = "SELECT * FROM `" + TABLE_USER + "` WHERE `name` = ? and `password` = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			
			UserInfo user = null;
            if (rs.next()) {
                String attrStr = rs.getString(11);
                Map<String, Object> exattr = null;
                if (null != attrStr) {
                	exattr = JSONObject.parseObject(attrStr, Map.class);
                }
                
//                user = new UserInfo(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
//                        rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getLong(8), rs.getInt(9), 
//                        rs.getString(10), exattr, rs.getLong(12), rs.getLong(13));
            }

            return user;
		} finally {
            if (conn != null) {
                conn.close();
            }
        }
	}
	/**
	 * 设置管理员默认密码
	 */
	public boolean setAdminDefaultInfo() throws SQLException {
		Long uid = checkLoginId("admin");
		if (uid < 1) {
			Connection conn = this.mysql.getConnection();
			try {
				String sql = "insert into `" + TABLE_USER + "` (`phone`,`nickName`,`email`,`password`,`uuid`,`ctime`) "
						+ "values (?, ?, ?, ?, ?, ?);";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, "admin");
				ps.setString(2, "管理员");
				ps.setString(3, "1764496637@qq.com");
				ps.setString(4, "P@ssw0rd");
				ps.setLong(5, 0);
				ps.setLong(6, Time.currentTimeMillis());
				
				return ps.executeUpdate() == 1 ? true : false;
			} finally {
	            if (conn != null) {
	                conn.close();
	            }
	        }
		}
		return false;
	}

	/**
	 * 验证登录 Id
	 * @return 用户 uid
	 */
	public Long checkLoginId(String name) throws SQLException {
		Connection conn = this.mysql.getConnection();
		Long uid = -1L;
		try {
			String sql = "SELECT `uid` FROM `" + TABLE_USER + "` WHERE `phone` = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			
            if (rs.next()) {
                uid = rs.getLong(1);
            }

            return uid;
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
        Connection conn = this.mysql.getConnection();
        try {

            String sql = "INSERT INTO `token` (`token`, `uid`, `expires`, `ctime`, `ip`, `device`) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tokenInfo.token);
            pstmt.setLong(2, tokenInfo.uid);
            pstmt.setLong(3, tokenInfo.expires);
            pstmt.setLong(4, tokenInfo.ctime);
            pstmt.setString(5, tokenInfo.ip);
            pstmt.setString(6, tokenInfo.device);
            
            int flag = pstmt.executeUpdate();

            if (flag == 1) {
                return true;
            }
            return false;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
