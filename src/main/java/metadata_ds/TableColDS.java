package metadata_ds;

import java.util.HashMap;
import java.util.Map;

public class TableColDS implements ITableColDS{

    Map<String,TableColumn> lstColumns;

    public  TableColDS(){
        lstColumns = new HashMap<>();
    }

    @Override
    public void insertCol(TableColumn column) {
        lstColumns.put(column.getColumnName(),column);
    }

    @Override
    public void removeCol(String columnName) {
        lstColumns.remove(columnName);
    }

    @Override
    public void updateCol(TableColumn column) {
        lstColumns.replace(column.getColumnName(),column);

    }

    public TableColumn get(String columnName)
    {
        return this.lstColumns.get(columnName);
    }

    public java.util.Set<String> keys(){
        return lstColumns.keySet();
    }

    public java.util.Collection<TableColumn> values(){
        return lstColumns.values();
    }
}
