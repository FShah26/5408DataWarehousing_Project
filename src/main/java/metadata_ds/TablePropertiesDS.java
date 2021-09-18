package metadata_ds;

import ds.Table;

import java.util.HashMap;
import java.util.Map;

public class TablePropertiesDS implements ITablePropertiesDS {

    Map<String,TableProperties> properties;
    TableColDS colDs = new TableColDS();

    public TablePropertiesDS(){
        properties = new HashMap<>();
    }

    @Override
    public void insertProp(TableProperties prop) {
        properties.put(prop.getTableName(),prop);

    }

    @Override
    public void deleteProp(String propName) {
        properties.remove(propName);

    }

    public java.util.Set<String> keys()
    {
        return properties.keySet();
    }

    public java.util.Collection<TableProperties> values()
    {
        return properties.values();
    }

    public TableProperties get(String tablename)
    {
        return this.properties.get(tablename);
    }

    @Override
    public void updateProp(TableProperties prop) {
        properties.replace(prop.getTableName(),prop);
    }
}
