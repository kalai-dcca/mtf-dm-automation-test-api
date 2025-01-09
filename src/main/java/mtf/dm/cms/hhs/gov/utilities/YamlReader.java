package mtf.dm.cms.hhs.gov.utilities;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class YamlReader {

    public static String getYamlProperties(String project, String env, String key) {
        String yamlFilePath = "src/test."+project+"/resources/environment/"+env+"/application.yml";


        try(InputStream inputStream = new FileInputStream(new File(yamlFilePath))){

            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(inputStream);


            if(yamlData.containsKey(key)){
                return yamlData.get(key).toString();
            }else{
                throw new IllegalArgumentException(key + " properties not found in Yaml file");
            }

        }
        catch (Exception e){
            throw new RuntimeException("Error reading YAML file ", e);
        }

    }

}
