package dmodule.service.http;

import dmodule.Config;

public class HttpConfig {

    public static final String CONF_DEMON_HTTP_HOST = "dmodule.http.host";
    public static final String CONF_DEMON_HTTP_PORT = "dmodule.http.port";

    public static final String CONF_DEMON_HTTP_WEB_CONTEXT_PATH = "dmodule.http.webContextPath";
    public static final String CONF_DEMON_HTTP_WEB_WELCOM_PAGE = "dmodule.http.webWelcomePage";

    public static final String CONF_DEMON_HTTPS_PORT = "dmodule.http.ssl.port";
    public static final String CONF_DEMON_HTTPS_KEY_PATH = "dmodule.http.ssl.kspath";
    public static final String CONF_DEMON_HTTPS_KEY_PWD = "dmodule.http.ssl.kspwd";

    public static final String CONF_DEMON_THREAD_MAX = "dmodule.http.thread.max";
    public static final String CONF_DEMON_THREAD_MIN = "dmodule.http.thread.min";
    public static final String CONF_DEMON_THREAD_IDLE = "dmodule.http.thread.idleTimeout";
    
    public static final String CONF_DEMON_IO_TIMEOUT = "dmodule.http.io.timeout";
    
    public static HttpServer.ServerConfig load() {
        HttpServer.ServerConfig conf = new HttpServer.ServerConfig();
        
        conf.port = Integer.parseInt(Config.get(CONF_DEMON_HTTP_PORT));
        conf.host = Config.get(CONF_DEMON_HTTP_HOST);
        
        if (Config.get(CONF_DEMON_HTTPS_PORT) != null && 
            Config.get(CONF_DEMON_HTTPS_KEY_PATH) != null &&
            Config.get(CONF_DEMON_HTTPS_KEY_PWD) != null) {
            conf.useSSL = true;
            conf.sslPort = Integer.parseInt(Config.get(CONF_DEMON_HTTPS_PORT));
            conf.sslKeyStorePath = Config.get(CONF_DEMON_HTTPS_KEY_PATH);
            conf.sslKeyStorePwd = Config.get(CONF_DEMON_HTTPS_KEY_PWD);
        }
        
        if (Config.get(CONF_DEMON_THREAD_MAX) != null && 
            Config.get(CONF_DEMON_THREAD_MIN) != null &&
            Config.get(CONF_DEMON_THREAD_IDLE) != null) {
                conf.customizeThreadPool = true;
                conf.maxThread = Integer.parseInt(Config.get(CONF_DEMON_THREAD_MAX));
                conf.minThread = Integer.parseInt(Config.get(CONF_DEMON_THREAD_MIN));
                conf.threadIdleTimeout = Integer.parseInt(Config.get(CONF_DEMON_THREAD_IDLE));
        }
        
        if (Config.get(CONF_DEMON_IO_TIMEOUT) != null) {
            conf.ioTimeout = Integer.parseInt(Config.get(CONF_DEMON_IO_TIMEOUT));
        }
        
        return conf;
    }
}
