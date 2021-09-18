package metadata_ds;

import java.util.List;

public class TableColumn {
    private int ColumnIndex;
    private String ColumnName;
    private String DataType;
    private List<String> Constraints;

    public List<String> getConstraints() {
        return Constraints;
    }

    public void setConstraints(List<String> constraints) {
        Constraints = constraints;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }

    public int getColumnIndex() {
        return ColumnIndex;
    }

    public void setColumnIndex(int  columnIndex) {
        ColumnIndex = columnIndex;
    }
}
