package org.luncert.configer.configObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.luncert.configer.ConfigureException;

public class StandardConfigObject implements ConfigObject {

    private Map<String, Object> data = new HashMap<>();

    public void set(String name, Object value) {
        data.put(name, value);
    }

    public Object get(String name) {
        return data.get(name);
    }

    public Object del(String name) {
        return data.remove(name);
    }

    protected String[] splitNamespace(String ns) {
        if (ns.startsWith(".") || ns.endsWith("."))
            throw new ConfigureException("invalid namespace string: " + ns);
        return ns.split("\\."); // 正则表达式, not '.' !!!
    }

    protected String concatNamespace(String[] ns, int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, limit = length - 1; i < limit; i++)
            builder.append(ns[i]).append('.');
        return builder.append(ns[length - 1]).toString();
    }

    public void setAttribute(String namespace, Object value) {
        String[] ns = splitNamespace(namespace);
        if (ns.length == 0)
            return;
        int i = 0, limit = ns.length - 1;
        String name;
        Object obj;
        ConfigObject tmp = this;
        while (i < limit) {
            name = ns[i];
            obj = tmp.get(name);
            if (obj == null) break;
            else if (obj instanceof ConfigObject){
                tmp = (ConfigObject) obj;
                i++;
            }
            else {
                throw new ConfigureException(concatNamespace(ns, i + 1)
                    + " reference to a object which is not a instance of ConfigObject: "
                    + obj);
            }
        }
        while (i < limit) {
            name = ns[i++];
            obj = new StandardConfigObject();
            tmp.set(name, obj);
            tmp = (ConfigObject) obj;
        }
        tmp.set(ns[i], value);
    }

    public Object getAttribute(String namespace) {
        String[] ns = splitNamespace(namespace);
        ConfigObject tmp = this;
        try {
            for (int i = 0, limit = ns.length - 1; i < limit; i++)
                // 会抛出异常的地方
                tmp = (ConfigObject) tmp.get(ns[i]);
            return tmp.get(ns[ns.length - 1]);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T getAttribute(String namespace, Class<T> clazz) {
        Object obj = getAttribute(namespace);
        return clazz.isInstance(obj) ? clazz.cast(obj) : null;
        
    }

    public Object delAttribute(String namespace) {
        String[] ns = splitNamespace(namespace);
        ConfigObject tmp = this;
        try {
            for (int i = 0, limit = ns.length - 1; i < limit; i++)
                tmp = (ConfigObject) tmp.get(ns[i]);
            return tmp.del(ns[ns.length - 1]);
        } catch (Exception e) {
            return null;
        }
    }

    public String getString(String namespace) {
        Object obj = getAttribute(namespace);
        return (obj != null && obj instanceof String) ? (String) obj : null;
    }

    public Boolean getBoolean(String namespace) {
        Object obj = getAttribute(namespace);
        return (obj != null && obj instanceof Boolean) ? (Boolean) obj : null;
    }

    public Integer getInteger(String namespace) {
        Object obj = getAttribute(namespace);
        return (obj != null && obj instanceof Integer) ? (Integer) obj : null;
    }

    public Long getLong(String namespace) {
        Object obj = getAttribute(namespace);
        return (obj != null && obj instanceof Long) ? (Long) obj : null;
    }

    public Double getDouble(String namespace) {
        Object obj = getAttribute(namespace);
        return (obj != null && obj instanceof Double) ? (Double) obj : null;
    }

    public Float getFloat(String namespace) {
        Object obj = getAttribute(namespace);
        return (obj != null && obj instanceof Float) ? (Float) obj : null;
    }

    public String toString() {
        String name;
        Object obj;
        StringBuilder builder = new StringBuilder().append("(");
        for (Entry<String, Object> entry : data.entrySet()) {
            name = entry.getKey();
            obj = entry.getValue();
            builder.append(name).append("=").append(obj).append(",");
        }
        if (data.size() > 0) {
            int len = builder.length();
            builder.replace(len - 1, len, ""); // remove ','
        }
        return builder.append(')').toString();
    }
    
}