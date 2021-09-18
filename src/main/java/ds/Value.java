package ds;

public interface Value {
    int getInt() throws Exception;

    String getString() throws Exception;

    boolean isInt();

    String getType();
}
