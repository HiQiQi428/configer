package org.luncert.configer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.luncert.configer.configObject.ConfigObject;
import org.luncert.configer.configObject.StandardConfigObject;
import org.luncert.configer.configParser.ConfigParser;
import org.luncert.configer.configParser.JsonParser;
import org.luncert.configer.configParser.PropsParser;
import org.luncert.configer.configParser.XmlParser;
import org.luncert.mullog.LogLevel;
import org.luncert.mullog.Mullog;

/**
 * 注意,如果同级目录下有"同名文件",例如:config.json和config.xml,其命名空间相同,name均为config,所以两个文件中相同的字段将被合并成一个字段,也就是后解析的覆盖先解析的.
 */
public class Configure implements ConfigObject {

    private static Mullog mullog = new Mullog("console");

    private String configPath;

    private ConfigObject config;

    private ConfigParser parser;

    private FileAlterationMonitor monitor;

    Configure(ConfigureFactory factory) {
        if (!factory.allowLog) {
            mullog.getUsingAppender().setLogLevel(LogLevel.ERROR);
        }

        configPath = factory.configPath;
        if (!new File(configPath).exists())
            throw new ConfigureException("not exists - " + configPath);

        parser = new ConfigParser();
        IOFileFilter fileFilter = FileFilterUtils.directoryFileFilter();
        if (factory.supportXml) {
            parser.addParser(new XmlParser());
            fileFilter = FileFilterUtils.or(fileFilter,
                FileFilterUtils.suffixFileFilter(".xml"));
        }
        if (factory.supportJson) {
            parser.addParser(new JsonParser());
            fileFilter = FileFilterUtils.or(fileFilter,
                FileFilterUtils.suffixFileFilter(".json"));
        }
        if (factory.supportProps) {
            parser.addParser(new PropsParser());
            fileFilter = FileFilterUtils.or(fileFilter,
                FileFilterUtils.suffixFileFilter(".properties"));
        }
        refresh();

        if (factory.monitorFileChange) {
            FileAlterationObserver observer = new FileAlterationObserver(configPath, fileFilter);
            observer.addListener(new FileEventHandler(this));
            monitor = new FileAlterationMonitor(factory.interval, observer);
            try {
                monitor.start();
            } catch (Exception e) {
                throw new ConfigureException(e);
            }
        }
    }

    /**
     * 停止FileMonitor
     * @throws Exception
     */
    public void stop() throws Exception {
        if (monitor != null)
            monitor.stop();
    }

    /**
     * 停止FileMonitor
     * @param stopInterval
     * @throws Exception
     */
    public void stop(long stopInterval) throws Exception {
        if (monitor != null)
            monitor.stop(stopInterval);
    }

    @Override
    public void finalize() throws Exception {
        stop();
    }

    private String getBaseName(String fileName) {
        int i = fileName.lastIndexOf(".");
        return i >= 0 ? fileName.substring(0, i) : fileName;
    }

    private String getNamespace(File file) {
        try {
            String path = file.getCanonicalPath();
            int i = path.lastIndexOf(".");
            if (i >= 0)
                path = path.substring(0, i);
            String namespace = path.replaceAll(configPath, "").replaceAll(File.separator, "\\.");
            if (namespace.startsWith("."))
                namespace = namespace.substring(1);
            if (namespace.endsWith("."))
                namespace = namespace.substring(0, namespace.length() - 1);
            return namespace;
        } catch (IOException e) {
            throw new ConfigureException(e);
        }
    }

    private ConfigObject loadConfig(File root) throws FileNotFoundException {
        ConfigObject config;
        if (root.isDirectory()) {
            config = new StandardConfigObject();
            for (File file : root.listFiles()) {
                config.setAttribute(getBaseName(file.getName()), loadConfig(file));
            }
        }
        else config = parser.parse(root);
        return config;
    }

    /**
     * <p>重新解析所有配置文件</p>
     * @throws ConfigureException
     */
    public void refresh() {
        File root = new File(configPath);
        try {
            config = loadConfig(root);
        } catch (FileNotFoundException e) {
            throw new ConfigureException(e);
        }
    }

    /**
     * <p>重新解析指定的配置文件,如果配置文件是configPath下的文件不解析</p>
     * <ul>
     * <li>配置文件不存在命名空间存在,则删除该命名空间</li>
     * <li>配置文件存在命名空间不存在,则创建命名空间</li>
     * <li>配置文件存在命名空间也存在,则更新命名空间</li>
     * </ul>
     * @param namespace 命名空间
     * @throws ConfigureException
     */
    public void refresh(File file) {
        String namespace = getNamespace(file);
        if (!file.exists()) {
            config.delAttribute(namespace);
        }
        else {
            try {
                config.setAttribute(namespace, loadConfig(file));
            } catch (FileNotFoundException e) {
                throw new ConfigureException(e);
            }
        }
    }

    // 存取方法

    public void setAttribute(String namespace, Object value) {
        config.setAttribute(namespace, value);
    }

    public Object getAttribute(String namespace) {
        return config.getAttribute(namespace);
    }

    public <T> T getAttribute(String namespace, Class<T> clazz) {
        return config.getAttribute(namespace, clazz);
    }

    public Object delAttribute(String namespace) {
        return config.delAttribute(namespace);
    }

    public String getString(String namespace) {
        return config.getString(namespace);
    }

    public Boolean getBoolean(String namespace) {
        return config.getBoolean(namespace);
    }

    public Integer getInteger(String namespace) {
        return config.getInteger(namespace);
    }

    public Long getLong(String namespace) {
        return config.getLong(namespace);
    }

    public Double getDouble(String namespace) {
        return config.getDouble(namespace);
    }

    public Float getFloat(String namespace) {
        return config.getFloat(namespace);
    }

    public void set(String name, Object value) {
        config.set(name, value);
    }

    public Object get(String name) {
        return config.get(name);
    }

    public Object del(String name) {
        return config.del(name);
    }

    public String toString() {
        return config.toString();
    }


}