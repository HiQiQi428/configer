package org.luncert.configer.configParser;

import java.io.InputStream;

import org.luncert.configer.configObject.ConfigObject;

public interface Parser {


	/**
	 * 根据type决定是否解析
	 * @param type 配置文件拓展名
	 * @param in 配置文件输入流
     * @return boolean 是否解析
	 */
    ConfigObject parser(String type, InputStream in);
    
}