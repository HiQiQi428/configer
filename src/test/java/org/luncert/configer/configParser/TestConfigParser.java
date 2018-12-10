package org.luncert.configer.configParser;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.luncert.configer.configObject.ConfigObject;

@RunWith(JUnit4.class)
public class TestConfigParser {

    ConfigParser parser = new ConfigParser();

    @Test
    public void testXmlParser() throws FileNotFoundException {
        parser.addParser(new XmlParser());
        
        ConfigObject config =  parser.parse(new File("/home/luncert/Project/configer/src/main/resources/config/serverConfig.xml"));
        System.out.println(config.getAttribute("servers.server.0"));
        System.out.println(config.getAttribute("servers.server.1"));
        System.out.println(config.getAttribute("servers.server.2"));
    }

    @Test
    public void testJsonParser() throws FileNotFoundException {
        parser.addParser(new JsonParser());
        ConfigObject config =  parser.parse(new File("/home/luncert/Project/configer/src/main/resources/config/people.json"));
        System.out.println(config);
    }

    @Test
    public void testPropsParser() throws FileNotFoundException {
        parser.addParser(new PropsParser());
        ConfigObject config =  parser.parse(new File("/home/luncert/Project/configer/src/main/resources/config/people.properties"));
        System.out.println(config);
    }

}