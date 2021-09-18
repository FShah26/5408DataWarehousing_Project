package ds;

import constants.FileConstants;

public class ValueImpl implements Value {

    private final String value;
    private final boolean isInt;

    public ValueImpl(String value, boolean isInt) {
        this.isInt = isInt;
        this.value = value;
    }

    @Override
    public int getInt() throws Exception {
        if (isInt) {
            return Integer.parseInt(value);
        }
        throw new Exception("It's a String, tried to access as Integer");
    }

    @Override
    public String getString() throws Exception {
        if (isInt) {
            throw new Exception("It's a integer, tried to access String");
        }
        return value;
    }

    @Override
    public boolean isInt() {
        return isInt;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Value)) {
            return false;
        }
        try {
            Value check = (Value) obj;
            if (check.isInt() != isInt) {
                return false;
            }
            if (check.isInt()) {
                if (check.getInt() != Integer.parseInt(value)) {
                    return false;
                }
            } else {
                if (!check.getString().equals(value)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getType() {
        if (isInt) {
            return FileConstants.INTEGER_ATTRIBUTE;
        } else {
            return FileConstants.STRING_ATTRIBUTE;
        }
    }

    @Override
    public int hashCode() {
        if (isInt) {
            return Integer.parseInt(value);
        } else {
            return value.hashCode();
        }
    }
}
