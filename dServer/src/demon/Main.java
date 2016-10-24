package dmodule;

import java.lang.management.ManagementFactory;
import java.sql.SQLException;

import dmodule.service.cron.GlobalTimerTask;
import dmodule.service.db.MySql;
import dmodule.service.db.MysqlConfig;
import dmodule.service.db.PoolInfo;
import dmodule.service.http.HttpConfig;
import dmodule.service.http.HttpServer;
import dmodule.service.http.protocol.ErrTextFormatter;
import dmodule.service.http.protocol.RetStat;
import dmodule.service.log.LogConfig;
import dmodule.service.log.Logger;

public class Main {

    public static void main(String[] args) throws Exception {
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        System.err.println("Demon pid: " + pid);
        
        if (args.length < 1) {
            System.err.println("Please specify configuration file path on starting up.");
            System.exit(-1);
        }
        try {
            Config.init(args[0]); // 加载配置文件
        } catch (Exception e) {
            System.err.println("Load configuration file exception.");
            e.printStackTrace();
            System.exit(-2);
        }

        try {
            ModuleMgr.init(Config.get(Config.CONF_DEMON_MODULE_DIR), Config.get(Config.CONF_DEMON_MODULE_DIR_THIRD));
        } catch (Exception e) {
            System.err.println("Init modules exception.");
            e.printStackTrace();
            System.exit(-3);
        }

        // 初始化demon的服务
        initService();

        try {
            ModuleMgr.loadModules();
        } catch (Exception e) {
            System.err.println("Load modules exception.");
            e.printStackTrace();
            System.exit(-3);
        }

        // 启动http服务
        HttpServer.startService();
    }

    public static void initService() throws Exception {
        try {
            // 初始化日志服务
            Logger.init(Config.get(LogConfig.CONF_DEMON_LOG_PATH), Config.get(LogConfig.CONF_DEMON_LOG_LEVEL),
                    Config.get(LogConfig.CONF_DEMON_LOG_ROTATED), Config.get(LogConfig.CONF_DEMON_LOG_MAX_FILE_SIZE));
        } catch (Exception e) {
            System.err.println("Initialize logger exception.");
            e.printStackTrace();
            System.exit(-3);
        }

        try {
            // 初始化http服务
            HttpServer.init(null, HttpConfig.load());
        } catch (Exception e) {
            System.err.println("Initialize HttpServer exception.");
            e.printStackTrace();
            System.exit(-4);
        }

        try {
            // 初始化mysql数据库链接池
            PoolInfo pool = new PoolInfo(Config.get(MysqlConfig.CONF_DEMON_MYSQL_HOST),
            Integer.parseInt(Config.get(MysqlConfig.CONF_DEMON_MYSQL_PORT)),
            Config.get(MysqlConfig.CONF_DEMON_MYSQL_USER),
            Config.get(MysqlConfig.CONF_DEMON_MYSQL_PSW),
            Config.get(MysqlConfig.CONF_DEMON_MYSQL_PARAMS),
            Integer.parseInt(Config.get(MysqlConfig.CONF_DEMON_MYSQL_POOL_MAX_ACTIVE)),
            Integer.parseInt(Config.get(MysqlConfig.CONF_DEMON_MYSQL_POOL_MAX_IDLE)),
            Long.parseLong(Config.get(MysqlConfig.CONF_DEMON_MYSQL_POOL_MAX_WAIT)),
            Long.parseLong(Config.get(MysqlConfig.CONF_DEMON_MYSQL_POOL_TIME_BETWEEN_EVICTION_RUNS_MILLIS)),
            Integer.parseInt(Config.get(MysqlConfig.CONF_DEMON_MYSQL_POOL_NUM_TEST_PER_EVICTION_RUN)),
            Long.parseLong(Config.get(MysqlConfig.CONF_DEMON_MYSQL_POOL_MIN_EVICTABLE_TIME_MILLIS)));
            
            MySql.init(pool);
        } catch (Exception e) {
            System.err.println("Initialize MySql exception.");
            e.printStackTrace();
            System.exit(-5);
        }
        
        try {
            // 初始化全局任务服务
            GlobalTimerTask.init(MySql.getInst("cron"));
        } catch (SQLException e) {
            System.err.println("Init GlobalTimerTask exception.");
            e.printStackTrace();
            System.exit(-3);
        }
        
//        try {
//            // 初始化license管理器
//            LicenseManager.init(Config.get(LicenseManager.CONF_DEMON_LICENSE), Logger.getInst());
//            new Thread(LicenseManager.getInst()).start();
//        } catch (Exception e) {
//            System.err.println("Init LicenseManager exception.");
//            e.printStackTrace();
//            System.exit(-3);
//        }
        
        // 注册demon通用错误反回码
        ErrTextFormatter.registerErrTextFormat(null, new RetStat());
    }    
}
