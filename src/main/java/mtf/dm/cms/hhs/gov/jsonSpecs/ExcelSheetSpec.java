package mtf.dm.cms.hhs.gov.jsonSpecs;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
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

    public void validateSheetData(List<List<String>> dataByColumn) {
        if (dataByColumn.size() != columnSpecs.size()) {
            throw new RuntimeException(String.format(
                    "Column specs size: %s & Data Columns size: %s do not match.\nSpec: %s\nData: %s",
                    columnSpecs.size(),
                    dataByColumn.size(),
                    this.toString(),
                    dataByColumn
            ));
        }

        System.out.println("Columns size matches spec file");

        for (int i = 0; i < dataByColumn.size(); i++) {
            List<String> columnData = dataByColumn.get(i);

            // The first row is the header
            String header = columnData.get(0).trim();

            if (!columnSpecs.containsKey(header)) {
                throw new RuntimeException(String.format(
                        "Unexpected or missing header: '%s'. Expected headers: %s",
                        header,
                        columnSpecs.keySet()
                ));
            }
            System.out.println("Column headers match for header: " + header);

            // Validate each cell in the column (skip the header row)
            ExcelColumnSpec spec = columnSpecs.get(header);
            for (int j = 1; j < columnData.size(); j++) {
                String cellValue = columnData.get(j);
                spec.validateCell(cellValue, header, j);
            }
            System.out.println("Column values match for column: " + header);
        }
    }

}
