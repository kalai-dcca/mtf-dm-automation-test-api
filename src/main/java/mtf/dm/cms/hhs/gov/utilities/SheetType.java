package mtf.dm.cms.hhs.gov.utilities;

public enum SheetType {
    CREATE("Create"),
    UPDATE_PUT("Update-PUT"),
    UPDATE_PATCH("Update-PATCH"),
    DELETE("Delete"),
    SINGLE_RESOURCE("Single Resource"),
    LIST_USERS("List users"),
    SINGLE_USER("Single User");

    public String enumData;

    SheetType(String value) {
        enumData = value;
    }

    public String getEnumData() {
        return enumData;
    }

    public static SheetType getSheetTypeEnum(String value){
        for(final SheetType each : SheetType.values()){
            if(each.enumData.equals(value)){
                return each;
            }
        }
        return null;
    }

}
