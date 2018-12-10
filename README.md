# configer

![Shippable branch](https://img.shields.io/shippable/5444c5ecb904a4b21567b0ff/master.svg) ![Maven metadata URI](https://img.shields.io/maven-metadata/v/http/central.maven.org/maven2/com/google/code/gson/gson/maven-metadata.xml.svg)

-------

## 特点

* 支持从xml、json、properties读取配置
* 支持监控配置文件变动

--------

## 使用

### 从```ConfigureFactory```构造

```java
Configure config = new ConfigureFactory("./configDir")
    .monitorFileChange(true)
    .allowLog(false)
    .supportProps(false)
    .build();
```

使用```ConfigureFactory```构造完后配置就已经加载完了，如果启用了文件监视器，配置文件的后续变动都能被```Configure```获得。需要注意，```org.luncert.configer.Configure```类才能捕获变动，```org.luncert.configer.configObject.ConfigObject```不行。虽然前者实现了后者。

这里对```ConfigureFactory```的配置进行说明：

* ```allowLog```：是否允许输出日志，如果不允许将只会输出```Error```级别的消息。
* ```monitorFileChange```：是否启用文件监视器。
* ```interval```：文件监视器查询文件系统间隔时间，单位 ms。
* ```supportXml```：是否解析 xml 文件。
* ```supportJson```：是否解析 json 文件。
* ```supportProps```：是否解析 properties 文件。

### 使用```Configure```

```Configure```的关键方法：

```java
void setAttribute(String namespace, Object value);

Object getAttribute(String namespace);

<T> T getAttribute(String namespace, Class<T> clazz);

Object delAttribute(String namespace);

void stop() throw Exception; // 停止文件监视器
```

```Configure```还有其他便易使用的接口，这里就不提了。主要是：

#### 命名空间！！！

命名空间是由配置文件和文件在文件系统中的层次共同决定的，举个例子，有如下配置文件结构：

```
- config
	- config1
		- names.json
	- server.xml
```

 name.json 内容：

```json
[{ 'realName': '李四', 'neekName': '小李'}]
```

server.xml 内容：

```xml
<root>
    <server>
        <addr>
            <host>111.111.111.111</host>
            <port>8800</port>
        </addr>
    </server>
    <server>
        <addr>
            <host>111.111.111.111</host>
            <port>8801</port>
        </addr>
    </server>
</root>
```

那么读取配置时可能是这样：

```java
configure.getAttribute("server.root.server.0.addr");
configure.getAttribute("config1.names.0.realName")
```

为了使数组和字典采用同样的接口读写，我把数组的索引视为键值，和其他字典元素一起存放在```Map```里了：-）。在 server.xml 里，发现同层次重复标签 server ，则视为数组元素了，需要将原先的命名空间```server.root.server.addr```改为```server.root.server.0.addr```。抛开索引变键值这个特殊规则后，命名空间就 = 文件系统的树结构 + 配置文件的数结构。其实实现不是很复杂，但是实现的时候我却想得很复杂。。。

### em。。。没了

需要用到的类就这两个。

如果要用maven安装到本地的话，嗯，这个包还依赖了另一个包见 [mullog](https://github.com/HiQiQi428/mullog)。

じゃね：-）