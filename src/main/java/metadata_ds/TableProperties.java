package metadata_ds;

import java.util.Map;

public class TableProperties {
    private String TableName;
    private boolean IsStructureUpdated;
    private TableColDS Columns;
    private String Location;
    private String LocationType;


    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public boolean isStructureUpdated() {
        return IsStructureUpdated;
    }

    public void setStructureUpdated(boolean structureUpdated) {
        IsStructureUpdated = structureUpdated;
    }

    public TableColDS getColumns() {
        return Columns;
    }

    public void setColumns(TableColDS columns) {
        Columns = columns;
    }


    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }
}
