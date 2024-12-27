package mtf.dm.cms.hhs.gov.jsonSpecs;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class ExcelSheetSpec {
    private String sheetName;
    private Map<String, ExcelColumnSpec> columnSpecs;

    // Constructor
    public ExcelSheetSpec(String sheetName, JsonNode sheetNode) {
        this.sheetName = sheetName;
        this.columnSpecs = new HashMap<String, ExcelColumnSpec>();

        JsonNode columnsNode = sheetNode.get("columns");
        columnsNode.fieldNames().forEachRemaining(columnName -> {
            JsonNode columnSpecNode = columnsNode.get(columnName);
            ExcelColumnSpec columnSpec = new ExcelColumnSpec(
                    columnSpecNode.get("type").asText(),
                    columnSpecNode.get("format").asText(),
                    columnSpecNode.get("pattern").asText(),
                    columnSpecNode.get("errorMessage").asText()
            );
            columnSpecs.put(columnName, columnSpec);
        });
    }

    public String getSheetName() {
        return sheetName;
    }

    public Map<String, ExcelColumnSpec> getColumnSpecs() {
        return columnSpecs;
    }

    @Override
    public String toString() {
        return String.format("Sheet: %s, Columns: %s", sheetName, columnSpecs.toString());
    }
}
