package org.luncert.configer.configParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.luncert.configer.ConfigureException;
import org.luncert.configer.configObject.ConfigObject;
import org.luncert.configer.configObject.StandardConfigObject;
import org.luncert.configer.util.IOHelper;

public class JsonParser implements Parser {

	private ConfigObject parseJSONArray(JSONArray json) {
		Object value;
		ConfigObject ret = new StandardConfigObject();
		for (int i = 0, limit = json.size(); i < limit; i++) {
			value = json.get(i);
			if (value instanceof JSONObject)
				value = parseJSONObject((JSONObject) value);
			else if (value instanceof JSONArray)
				value = parseJSONArray((JSONArray) value);
			ret.set(String.valueOf(i), value);
		}
		return ret;
	}

	private ConfigObject parseJSONObject(JSONObject json) {
		String name;
		Object value;
		ConfigObject ret = new StandardConfigObject();
		for (Entry<String, Object> entry : json.entrySet()) {
			name = entry.getKey();
			value = entry.getValue();
			if (value instanceof JSONObject)
				value = parseJSONObject((JSONObject) value);
			else if (value instanceof JSONArray)
				value = parseJSONArray((JSONArray) value);
			ret.set(name, value);
		}
		return ret;
	}


	@Override
    public ConfigObject parser(String type, InputStream in) {
		if (type.equals("json")) {
			try {
				String raw = new String(IOHelper.read(in));
				Object json = JSON.parse(raw);
				if (JSONObject.class.isInstance(json))
					return parseJSONObject((JSONObject) json);
				else
					return parseJSONArray((JSONArray) json);
			} catch (IOException e) {
				throw new ConfigureException(e);
			}
		}
		return null;
	}

}