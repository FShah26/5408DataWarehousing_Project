package ds;

import java.util.List;
import java.util.Map;

public interface Table {
    /**
     * Insert a new row
     *
     * @param row Row to be inserted
     */
    void insert(Row row);

    /**
     * Update a row
     *
     * @param keyValue          Key for the row in which things should be updated. For Ex in 'RollNo = 5' RollNo is the Key.
     * @param attributeLocation Location of the attribute, meaning, Ex. Attributes in table are 'RollNo(0), Name(1), ID(2) RollNo is location 0
     * @param replaceValues     Map list of values to replace in the row.
     * @return Whether the Row update is True or false.
     */
    boolean update(Value keyValue, int attributeLocation, Map<Integer, Value> replaceValues);

    /**
     * Delete a row
     *
     * @param keyValue          keyValue for which the entire row should be deleted.
     * @param attributeLocation Location of the attribute, meaning, Ex. Attributes in table are 'RollNo(0), Name(1), ID(2) RollNo is location 0
     * @return status of delete.
     */
    boolean delete(Value keyValue, int attributeLocation);

    List<Row> get(Value keyValue, int attributeLocation);

    String getAttributeTypes();

    String getTableName();

    void setAttributeTypes(String attributeTypes);

    String toString(List<Integer> onlyPrint);

    void cleanTable();
}
