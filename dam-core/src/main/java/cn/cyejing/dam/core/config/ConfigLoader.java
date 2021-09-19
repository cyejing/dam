package cn.cyejing.dam.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


@Slf4j
public class ConfigLoader {

    private final static String CONFIG_ENV_PREFIX = "DAM_";
    private final static String CONFIG_JVM_PREFIX = "dam.";
    private final static String CONFIG_FILE = "dam.properties";
    private final static String CONFIG_OUTFILE = "config.file";

    private final static ConfigLoader INSTANCE = new ConfigLoader();

    private Config config;

    private ConfigLoader() {
    }

    public static ConfigLoader getInstance() {
        return INSTANCE;
    }

    public static Config getConfig() {
        return INSTANCE.config;
    }

    public Config load(String[] args) {
        if (this.config != null) {
            return this.config;
        }

        Config config = new Config();

        {
            InputStream configStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (configStream != null) {
                try {
                    Properties properties = new Properties();
                    properties.load(configStream);
                    properties2Object(properties, config);
                } catch (Exception e) {
                    log.warn("load config file error:{}", CONFIG_FILE, e);
                }
            }
        }

        {
            String outConfigFile = System.getProperty(CONFIG_JVM_PREFIX + CONFIG_OUTFILE);
            if (outConfigFile == null) {
                outConfigFile = System.getenv(CONFIG_ENV_PREFIX + CONFIG_OUTFILE);
            }
            if (outConfigFile == null) {
                String userDir = System.getProperty("user.dir");
                outConfigFile = userDir + File.separator + CONFIG_FILE;
            }
            File file = new File(outConfigFile);
            if (file.exists()) {
                log.info("load config file:{}", outConfigFile);
                try (FileInputStream configStream = new FileInputStream(file)) {
                    Properties properties = new Properties();
                    properties.load(configStream);
                    properties2Object(properties, config);
                } catch (Exception e) {
                    log.warn("load config file error:{}", file, e);
                }
            }
        }


        {
            Map<String, String> env = System.getenv();
            Properties properties = new Properties();
            properties.putAll(env);
            properties2Object(properties, config, CONFIG_ENV_PREFIX);
        }

        {
            Properties properties = System.getProperties();
            properties2Object(properties, config, CONFIG_JVM_PREFIX);
        }

        if (args != null && args.length > 0) {
            Properties properties = new Properties();
            for (String arg : args) {
                if (arg.startsWith("--") && arg.contains("=")) {
                    properties.put(arg.substring(2, arg.indexOf('=')), arg.substring(arg.indexOf('=') + 1));
                }
            }
            properties2Object(properties, config);
        }

        this.config = config;

        if (StringUtils.isNotEmpty(config.getRegistry())) {
            DynamicConfigInitializer.getInstance().watchRegistry(config.getNamespace(), config.getRegistry());
        } else {
            DynamicConfigInitializer.getInstance().loadFile(config.getRoutePath());
        }
        return this.config;
    }

    private void properties2Object(Properties properties, Config config) {

    }

    private void properties2Object(Properties properties, Config config, String prefix) {

    }
}
