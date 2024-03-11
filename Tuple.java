import java.io.Serializable;
import java.util.*;

public class Tuple implements Serializable {

    private HashMap<String, Object> hmTuple;
    private String strpageName;

    // The `public Tuple()` constructor in the `Tuple` class is initializing a new
    // instance of the
    // class by creating a new HashMap named `hmTuple`. This HashMap is used to
    // store key-value pairs
    // where the key is a String representing the column name and the value is an
    // Object representing
    // the column value.
    public Tuple() {
        hmTuple = new HashMap<String, Object>();
        strpageName = "";
    }

    /**
     * The function getColumnValue retrieves the value of a specified column from a
     * HashMap.
     * 
     * @param strColumnName The `strColumnName` parameter in the `getColumnValue`
     *                      method represents the
     *                      name of the column for which you want to retrieve the
     *                      value from the `hmTuple` HashMap.
     * @return The method `getColumnValue` is returning the value associated with
     *         the specified column
     *         name from the `hmTuple` HashMap.
     */
    public Object getColumnValue(String strColumnName) {
        // TODO: What if column does not exist?
        return hmTuple.get(strColumnName);
    }

    public void setPageName(String strpageName) {
        this.strpageName = strpageName;
    }

    public String getPageName() {
        return strpageName;
    }

    /**
     * The function equals determines whether two tuples are equal or not.
     * 
     * 
     * @param obj The `obj` parameter in the `equals`
     *            method represents the object to compare with the current Tuple
     *            object.
     * 
     * @return The method `equals` is returning boolean value, whether the two
     *         objects are equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tuple other = (Tuple) obj;
        return this.hmTuple.equals(other.hmTuple);
    }

    /**
     * The function `setColumnValue` sets a column value in a HashMap using the
     * column name as the key
     * and the column value as the value.
     * 
     * @param strColumnName  The `strColumnName` parameter is a String that
     *                       represents the name of the
     *                       column where the value will be set in the `hmTuple`
     *                       HashMap.
     * @param objColumnValue The `objColumnValue` parameter is the value that you
     *                       want to set for the
     *                       specified column in the `hmTuple` map. It can be of any
     *                       data type since it is defined as an
     *                       `Object` type in the method signature.
     */
    public void setColumnValue(String strColumnName, Object objColumnValue) {
        hmTuple.put(strColumnName, objColumnValue);
    }

    /**
     * The `toString` function concatenates the values of a HashMap into a
     * comma-separated string.
     * 
     * @return The `toString` method is returning a string representation of the
     *         values stored in the
     *         `hmTuple` map, with each value separated by a comma.
     */
    public String toString() {

        // TODO: remove last comma

        String res = "";

        for (String column : hmTuple.keySet()) {

            res += hmTuple.get(column) + ",";

        }

        return res;

    }

}
