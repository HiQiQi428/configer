package org.luncert.configer.configParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.luncert.configer.ConfigureException;
import org.luncert.configer.configObject.ConfigObject;

public final class ConfigParser {

    private List<Parser> parsers = new LinkedList<>();

    /**
     * 添加解析器
     * @param parser
     */
    public void addParser(Parser parser) {
        parsers.add(parser);
    }

    /**
     * 使用预先添加的解析器解析配置文件
     * @param configFile
     * @throws FileNotFoundException
     */
    public ConfigObject parse(File configFile) throws FileNotFoundException {
        if (parsers.size() == 0)
            throw new ConfigureException("no parser added");
            
        String name = configFile.getName();
        int i = name.lastIndexOf(".");
        if (i >= 0) {
            String type = name.substring(i + 1).toLowerCase();
            InputStream in = new FileInputStream(configFile);
            ConfigObject ret;
            for (Parser p : parsers) {
                if ((ret = p.parser(type, in)) != null)
                    return ret; // 解析成功,直接返回
            }
        }
        throw new ConfigureException("unrecognized file type: " + configFile);
    }

}