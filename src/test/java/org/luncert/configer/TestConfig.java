package org.luncert.configer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestConfig {

    @Test
    public void test1() {
        Configure config = new ConfigureFactory("/home/luncert/Project/configer/src/main/resources/config")
            .monitorFileChange(false)
            .build();
        System.out.println(config.getAttribute("serverConfig"));
        System.out.println(config.getAttribute("clientConfig"));
        System.out.println(config.getAttribute("1.name"));
        System.out.println(config.getAttribute("student.0"));
        System.out.println(config.getAttribute("teacher.1"));
    }

    @Test
    public void test2() {
        Configure config = new ConfigureFactory("/home/luncert/Project/configer/src/main/resources/config").build();
        System.out.println(config.getAttribute("1.name"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(config.getAttribute("1.name"));
    }
}