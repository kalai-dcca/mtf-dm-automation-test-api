package mtf.dm.cms.hhs.gov.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public abstract class BaseClass {

    public static final String reportPath = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());

    private static TestScenarioClass testScenarioClass;

    public static final String TEST_DATA_PATH = "src/test.demoApi/resources/testData/";

    public static TestScenarioClass getTestScenarioClass(){
        if(Objects.isNull(testScenarioClass)){
            testScenarioClass = new TestScenarioClass();
            setTestScenarioClass(testScenarioClass);
        }
        return testScenarioClass;
    }

    public static void setTestScenarioClass(TestScenarioClass testScenarioClass) {
        BaseClass.testScenarioClass = testScenarioClass;
    }
}
