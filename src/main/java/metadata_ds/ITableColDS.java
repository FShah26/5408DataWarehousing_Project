package metadata_ds;

public interface ITableColDS {

    void insertCol(TableColumn column);
    void removeCol(String columnName);
    void updateCol(TableColumn column);
}
