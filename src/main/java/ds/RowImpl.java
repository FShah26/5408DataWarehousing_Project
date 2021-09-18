package ds;

import java.util.ArrayList;
import java.util.List;

public class RowImpl implements Row {
    ArrayList<Value> row;

    public RowImpl() {
        row = new ArrayList<>();
    }

    @Override
    public ArrayList<Value> getValues() {
        return row;
    }

    @Override
    public void insertAttribute(int value) {
        row.add(new ValueImpl(Integer.toString(value), true));
    }

    @Override
    public void insertAttribute(String value) {
        row.add(new ValueImpl(value, false));
    }

    @Override
    public Value getKey() {
        return row.get(0);
    }

    @Override
    public String toString() {
        StringBuilder buildRow = new StringBuilder();

        for (Value value : row) {
            try {
                buildRow.append(value.isInt() ? value.getInt() : value.getString());
                buildRow.append(",");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (buildRow.length() > 0) {
            buildRow.setCharAt(buildRow.length() - 1, '\n');
        }
        return buildRow.toString();
    }

    @Override
    public String getAttributeTypes() {
        StringBuilder builder = new StringBuilder();

        for (Value value : row) {
            builder.append(value.getType() + ",");
        }
        builder.setCharAt(builder.length() - 1, '\n');
        return builder.toString();
    }

    public void putValue(int attributeLocation, Value value) {
        row.set(attributeLocation, value);
    }

    @Override
    public String toString(List<Integer> onlyPrint) {
        StringBuilder buildRow = new StringBuilder();

        for (int i = 0; i < row.size(); i++) {
            if (onlyPrint.contains(i)) {
                Value value = row.get(i);
                try {
                    buildRow.append(value.isInt() ? value.getInt() : value.getString());
                    buildRow.append(",");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (buildRow.length() > 0) {
            buildRow.setCharAt(buildRow.length() - 1, '\n');
        }

        return buildRow.toString();
    }
}
