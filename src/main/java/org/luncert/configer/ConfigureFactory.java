package org.luncert.configer;

import java.security.InvalidParameterException;

public class ConfigureFactory {

    String configPath;

    boolean allowLog = true;

    boolean monitorFileChange = true;

    static final int DEFAULT_INTERVAL = 1000;

    long interval = DEFAULT_INTERVAL;

    boolean supportXml = true;

    boolean supportJson = true;

    boolean supportProps = true;

    public ConfigureFactory(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 是否允许日志输出,如果否将只输出ERROR级别的日志,默认true
     * @param allowLog
     * @return
     */
    public ConfigureFactory allowLog(boolean allowLog) {
        this.allowLog = allowLog;
        return this;
    }

    /**
     * 是否监视配置文件或目录的变化,默认true
     * @param monitorFileChange
     * @return ConfigureFactory
     */
    public ConfigureFactory monitorFileChange(boolean monitorFileChange) {
        this.monitorFileChange = monitorFileChange;
        return this;
    }

    /**
     * @param interval 查询文件系统间隔时间,单位ms
     * @return ConfigureFactory
     */
    public ConfigureFactory interval(long interval) {
        if (interval <= 0)
            throw new InvalidParameterException("interval must be positive, but i got " + interval);
        this.interval = interval;
        return this;
    }

    /**
     * 是否解析xml文件,默认true
     * @param supportXml
     * @return ConfigureFactory
     */
    public ConfigureFactory supportXml(boolean supportXml) {
        this.supportXml = supportXml;
        return this;
    }

    /**
     * 是否解析json文件,默认true
     * @param supportJson
     * @return ConfigureFactory
     */
    public ConfigureFactory supportJson(boolean supportJson) {
        this.supportJson = supportJson;
        return this;
    }

    /**
     * 是否解析properties文件,默认true
     * @param supportProps
     * @return ConfigureFactory
     */
    public ConfigureFactory supportProps(boolean supportProps) {
        this.supportProps = supportProps;
        return this;
    }

    /**
     * @return Configer
     */
    public Configure build() {
        return new Configure(this);
    }
    
}