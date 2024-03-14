import java.io.*;
import java.util.*;


public class Tuple implements Serializable, Comparable<Tuple>{
    

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

    public Object getColumnValue(String strColumnName) throws DBAppException{
        
        if(!hmTuple.containsKey(strColumnName)){
            throw new DBAppException("Column does not exist");
        }

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

    /**
     * The `compareTo` method compares the values of two tuples based on the values of their columns.
     * 
     * @param t The `t` parameter is a Tuple object that you want to compare to the current Tuple
     * object.
     * @return The `compareTo` method returns an integer value that represents the result of the
     * comparison between the two Tuple objects. If the current Tuple object is less than the Tuple
     * object `t`, the method returns a negative integer. If the current Tuple object is greater than
     * the Tuple object `t`, the method returns a positive integer. If the two Tuple objects are equal,
     * the method returns 0.
     */
    public int compareTo(Tuple t){
        for(String column : hmTuple.keySet()){
            if(!hmTuple.get(column).equals(t.hmTuple.get(column))){
                return ((Comparable)hmTuple.get(column)).compareTo(t.hmTuple.get(column));
            }
        }
        return 0;

    }

    /**
     * The `equals` function compares two Tuple objects based on their key-value pairs in a HashMap.
     * 
     * @param t The `equals` method you provided is comparing two `Tuple` objects based on their
     * key-value pairs in the `hmTuple` HashMap. The method iterates through each key in the HashMap
     * and checks if the corresponding values in both `Tuple` objects are equal.
     * @return The `equals` method is returning a boolean value. It will return `true` if all the
     * values in the `hmTuple` map of the current `Tuple` object are equal to the corresponding values
     * in the `hmTuple` map of the `Tuple` object passed as a parameter (`t`). If any of the values are
     * not equal, it will return `false`.
     */
    public boolean equals(Tuple t){
        
        for(String column : hmTuple.keySet()){
            if(!hmTuple.get(column).equals(t.hmTuple.get(column))){
                
                return false;
            }
        }

        
        return true;
    }

    

    /**
     * This Java hashCode function returns the hash code of the hmTuple object.
     * 
     * @return The `hashCode()` method is returning the hash code of the `hmTuple` object.
     */
    public int hashCode(){
        System.out.println(hmTuple.hashCode());
        return hmTuple.hashCode();
    }

}
