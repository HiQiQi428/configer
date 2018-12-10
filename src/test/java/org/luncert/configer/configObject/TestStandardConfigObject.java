package org.luncert.configer.configObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestStandardConfigObject {

    @Test
    public void test() {
        ConfigObject configObject = new StandardConfigObject();
        configObject.setAttribute("school.class.student.1.name", "Luncert");
        configObject.setAttribute("school.class.student.1.age", 13);
        configObject.setAttribute("school.class.student.2.name", "Lmanic");
        configObject.setAttribute("school.class.student.2.age", 15);
        System.out.println(configObject);
    }

}