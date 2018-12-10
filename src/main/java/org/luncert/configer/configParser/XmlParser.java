package org.luncert.configer.configParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.luncert.configer.configObject.ConfigObject;
import org.luncert.configer.configObject.StandardConfigObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

public class XmlParser implements Parser {

	private static class XmlHandler extends DefaultHandler2 {

		private ConfigObject config = new StandardConfigObject();

		/**
		 * namespace,方便添加或删除name
		 */
		private List<String> path = new LinkedList<>();

		/**
		 * 记录namespace是否已经出现过,如果是则将重复的元素编号
		 * 命名空间将发生变化,eg: server.port -> server.port.0
		 */
		private Map<String, Integer> record = new HashMap<>();

		/**
		 * 正在处理的元素的namespace
		 */
		private String ns;

		/**
		 * 将path连接为namespace
		 */
		private String stringifyPath() {
			StringBuilder builder = new StringBuilder();
			Iterator<String> iterator = path.iterator();
			if (iterator.hasNext()) {
				while (true) {
					builder.append(iterator.next());
					if (iterator.hasNext())
						builder.append('.');
					else
						break;
				}
			}
			return builder.toString();
		}

		private boolean isNumber(String s) {
			for (char c : s.toCharArray())
				if (c < '0' || c > '9')
					return false;
			return true;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
			// 添加name,获取新的namespace
			path.add(qName);
			ns = stringifyPath();
			if (record.containsKey(ns)) {
				// 获取计数
				Integer i = record.get(ns);
				if (i == 0) {
					// 第一次重复时改变第一个元素的namespace
					// eg: server.addr -> server.addr.0
					Object tmp = config.delAttribute(ns);
					config.setAttribute(ns + "." + i, tmp);
				}
				record.put(ns, ++i);
				path.add(String.valueOf(i));
				ns += "." + i;
			}
			else record.put(ns, 0);
			// 解析元素属性
			for (int i = 0, limit = attrs.getLength(); i < limit; i++)
				config.setAttribute(ns + "." + attrs.getQName(i), attrs.getValue(i));
		}

		@Override
		public void characters(char ch[], int start, int length) throws SAXException {
			String content = String.valueOf(ch, start, length).trim();
			// content作为元素内文本内容的name
			if (content.length() > 0) {
				config.setAttribute(ns + ".content",
					String.valueOf(ch, start, length));
			}
		}


		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			String name = path.remove(path.size() - 1);
			// 如果name是数字,说明这是一个计数
			// 需要把namespace改回去 eg: server.addr.1 -> server.addr
			if (isNumber(name))
				path.remove(path.size() - 1);
		}

	}
	
	@Override
    public ConfigObject parser(String type, InputStream in) {
		if (type.equals("xml")) {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser;
			XmlHandler handler = new XmlHandler();
			try {
				parser = factory.newSAXParser();
				parser.parse(in, handler);
				return handler.config;
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}