package demon.license;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.asm.Type;

import demon.exception.UnInitilized;
import demon.license.pojo.LicenseInfo;
import demon.service.db.MySql;

class LicenseModel {
    private MySql mysql;
    
    public LicenseModel() throws SQLException, UnInitilized {
        this.mysql = MySql.getInst(Init.MODULE_NAME);
        initTable();
    }

	private void initTable() throws SQLException {
        Connection conn = this.mysql.getConnection();
        try {
            String sql = "CREATE TABLE IF NOT EXISTS `license` ("
            		+ "`id` bigint(10) UNSIGNED NOT NULL AUTO_INCREMENT,"
            		+ "`status` varchar(32) NOT NULL,"
            		+ "`persistence` varchar(32) DEFAULT NULL,"
            		+ "`create_time` bigint(20) DEFAULT NULL,"
            		+ "`end_time` bigint(20) NOT NULL,"
            		+ "`company` varchar(255) NOT NULL,"
            		+ "`license` varchar(128) NOT NULL,"
            		+ "`security_key` varchar(128) NOT NULL,"
                    + "PRIMARY KEY (`id`),"
                    + "KEY `uk_et` (`end_time`) USING BTREE,"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

            conn.createStatement().executeUpdate(sql);

        } finally { if (conn != null) conn.close(); }	
	}
	
}
