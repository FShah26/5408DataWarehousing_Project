package metadata_ds;

public interface ITablePropertiesDS {
    void insertProp(TableProperties prop);
    void deleteProp(String propName);
    void updateProp(TableProperties prop);
}
