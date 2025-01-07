package mtf.dm.cms.hhs.gov.utilities;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private final Map<String, Object> scenarioContext;
    private static ScenarioContext ctx;
    private ScenarioContext(){
        scenarioContext = new HashMap<>();
    }
    public static ScenarioContext getScenarioContext(){
        if(ctx == null){
            return new ScenarioContext();
        }
        return null;
    }

    public void setContext(Context key, Object value) {
        scenarioContext.put(key.toString(), value);
    }

    public void setContext(String key, @Nullable Object value) {
        scenarioContext.put(key, value);
    }

    public Object getContext(Context key){
        return scenarioContext.get(key.toString());
    }

    public void removeContext(Context key) { scenarioContext.remove(key);}

    public Boolean isContains(Context key){
        return scenarioContext.containsKey(key.toString());
    }
}
