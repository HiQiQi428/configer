package org.luncert.configer.configObject;

public interface ConfigObject {

    /**
     * 设置当前类的属性
     * @param name
     * @param value
     */
    void set(String name, Object value);

    /**
     * 根据name获取当前类的属性
     * @param name
     * @return Object
     */
    Object get(String name);

    /**
     * 从当前类删除属性
     * @param name
     * @return Object
     */
    Object del(String name);

    /**
     * @param namespace eg: server.addr.ip, namespace中的数字会被视为index,例如student.1.name
     * @param value 值
     * @throws ConfigerException
     */
    void setAttribute(String namespace, Object value);
    
    /**
     * @param namespace 命名空间
     * @return Object
     */
    Object getAttribute(String namespace);

    /**
     * 返回指定类型的对象
     * @param namespace 命名空间
     * @param clazz 类型
     * @return T
     */
    <T> T getAttribute(String namespace, Class<T> clazz);

    /**
     * 从ConfigObject中删除属性
     * @param namespace 命名空间
     * @return Object 命名空间不存在时返回null
     */
    Object delAttribute(String namespace);

    /**
     * 获取String类型的值
     * @param namespace 命名空间
     * @return String
     */
    String getString(String namespace);
    
    /**
     * 获取Boolean类型的值
     * @param namespace 命名空间
     * @return Boolean
     */
    Boolean getBoolean(String namespace);

    /**
     * 获取Integer类型的值
     * @param namespace 命名空间
     * @return Integer
     */
    Integer getInteger(String namespace);

    /**
     * 获取Long类型的值
     * @param namespace 命名空间
     * @return Long
     */
    Long getLong(String namespace);

    /**
     * 获取Double类型的值
     * @param namespace 命名空间
     * @return Double
     */
    Double getDouble(String namespace);

    /**
     * 获取Float类型的值
     * @param namespace 命名空间
     * @return Float
     */
    Float getFloat(String namespace);

}