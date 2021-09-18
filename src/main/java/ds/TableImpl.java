package ds;

import java.util.*;

public class TableImpl implements Table {
    Map<Value, Row> rows;
    String tableName;
    String attributeTypes;

    public TableImpl(String tableName) {
        this.tableName = tableName;
        rows = new LinkedHashMap<>();
    }

    @Override
    public void insert(Row row) {
        attributeTypes = row.getAttributeTypes();
        rows.put(row.getKey(), row);
    }

    private Row generateUpdatedRow(Row oldRow, Map<Integer, Value> replaceValues) {
        for (Map.Entry<Integer, Value> valueRowEntry : replaceValues.entrySet()) {
            int location = (int) ((Map.Entry) valueRowEntry).getKey();
            Value valueToReplace = (Value) ((Map.Entry) valueRowEntry).getValue();
            oldRow.putValue(location, valueToReplace);
        }
        return oldRow;
    }

    @Override
    public boolean update(Value keyValue, int attributeLocation, Map<Integer, Value> replaceValues) {
        // Key attribute
        if (attributeLocation == 0) {
            Row newRow = rows.get(keyValue);
            newRow = generateUpdatedRow(newRow, replaceValues);
            rows.put(keyValue, newRow);
        } else {
            for (Value rowKey : rows.keySet()) {
                Row tableRow = rows.get(rowKey);
                if (tableRow.getValues().get(attributeLocation).equals(keyValue)) {
                    rows.put(rowKey, generateUpdatedRow(tableRow, replaceValues));
                }
            }
        }
        return true;
    }

    @Override
    public boolean delete(Value keyValue, int attributeLocation) {
        boolean deleteHappened = false;

        /*try {
            System.out.println(keyValue.getString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (attributeLocation == 0) {
            if (rows.containsKey(keyValue)) {
                rows.remove(keyValue);
                deleteHappened = true;
            }
        } else {
            Iterator it = rows.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Row row = (Row) entry.getValue();
                if (row.getValues().get(attributeLocation).equals(keyValue)) {
                    it.remove();
                    deleteHappened = true;
                }
            }
        }
       // if(deleteHappened){
         //   System.out.println("Delete success");
        //}
        return deleteHappened;
    }

    @Override
    public List<Row> get(Value keyValue, int attributeLocation) {
        ArrayList<Row> returnRows = new ArrayList<>();

        if (attributeLocation == 0) {
            returnRows.add(rows.get(keyValue));
        } else {
            for (Value rowKey : rows.keySet()) {
                Row tableRow = rows.get(rowKey);
                if (tableRow.getValues().get(attributeLocation).equals(keyValue)) {
                    returnRows.add(tableRow);
                }
            }
        }
        return returnRows;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        StringBuilder rowsString = new StringBuilder();
        for (Value value : rows.keySet()) {
            rowsString.append(rows.get(value).toString());
        }
        return rowsString.toString();
    }

    @Override
    public String getAttributeTypes() {
        return attributeTypes;
    }

    @Override
    public void setAttributeTypes(String attributeTypes) {
        this.attributeTypes = attributeTypes;
    }

    @Override
    public String toString(List<Integer> onlyPrint) {
        StringBuilder rowsString = new StringBuilder();
        for (Value value : rows.keySet()) {
            rowsString.append(rows.get(value).toString(onlyPrint));
        }
        return rowsString.toString();
    }

    @Override
    public void cleanTable() {
        rows.clear();
    }
}
