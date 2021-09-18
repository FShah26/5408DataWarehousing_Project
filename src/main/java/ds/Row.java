package ds;

import java.util.ArrayList;
import java.util.List;

public interface Row {
    ArrayList<Value> getValues();

    void insertAttribute(int value);

    void insertAttribute(String value);

    Value getKey();

    String getAttributeTypes();

    void putValue(int attributeLocation, Value value);

    String toString(List<Integer> onlyPrint);
}
