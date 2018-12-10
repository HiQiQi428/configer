package org.luncert.configer.configParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map.Entry;

import org.luncert.configer.ConfigureException;
import org.luncert.configer.configObject.ConfigObject;
import org.luncert.configer.configObject.StandardConfigObject;

public class PropsParser implements Parser {

	private ConfigObject parseProperties(Properties props) {
		ConfigObject config = new StandardConfigObject();
		for (Entry<Object, Object> entry : props.entrySet()) {
			config.setAttribute((String) entry.getKey(), entry.getValue());
		}
		return config;
	}

	@Override
    public ConfigObject parser(String type, InputStream in) {
		if (type.equals("properties")) {
			Properties props = new Properties();
			try {
				props.load(in);
				return parseProperties(props);
			} catch (IOException e) {
				throw new ConfigureException(e);
			}
		}
		return null;
	}

}