package mtf.dm.cms.hhs.gov.utilities;

import com.github.javafaker.Faker;

public class DataGenerator {

    private static final Faker FAKER = new Faker();

    private DataGenerator() {}

    public static String getFirstName() {
        return FAKER.name().firstName();
    }

    public static String getLastName() {
        return FAKER.name().lastName();
    }

    public static String getMiddleName() {
        return FAKER.name().nameWithMiddle().split(" ")[1];
    }

    public static String getUsername() {
        return FAKER.name().username();
    }
    
    public static String getNumber(int num) {
        return FAKER.number().digits(num);
    }

}
