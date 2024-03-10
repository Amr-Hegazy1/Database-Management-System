import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.Vector;

public class Page implements Serializable {

    private Vector<Tuple> vecTuples;
    public static int intPageSize;

    // The `public Page()` constructor in the `Page` class is initializing a new
    // instance of the `Page`
    // class. Inside the constructor, it initializes the `vecTuples` member variable
    // as a new `Vector`
    // object that can hold elements of type `Tuple`. This means that when a new
    // `Page` object is
    // created, it will have an empty vector `vecTuples` ready to store `Tuple`
    // objects.
    public Page() {

        vecTuples = new Vector<Tuple>();
    }

    /**
     * The addTuple function adds a Tuple object to a list of tuples.
     * 
     * @param tupleTuple The parameter `tupleTuple` in the `addTuple` method is of
     *                   type `Tuple`, which
     *                   is the type of object that you are adding to the
     *                   `vecTuples` collection.
     */
    public void addTuple(int index, Tuple tupleTuple) {
        vecTuples.add(index, tupleTuple);
    }

    /**
     * The getSize function returns number of tuples in the page.
     * 
     * @return number of tuples in the page.
     */
    public int getSize() {
        return vecTuples.size();
    }

    /**
     * The getFirstTuple function returns first tuple in the page.
     * 
     * @return First tuple in the page.
     */
    public Tuple getFirstTuple() {
        return vecTuples.get(0);
    }

    /**
     * The getLastTuple function returns Last tuple in the page.
     * 
     * @return Last tuple in the page.
     */
    public Tuple getLastTuple() {
        return vecTuples.get(vecTuples.size() - 1);
    }

    /**
     * The removeLastTuple function returns Last tuple in the page, tyhen removes it
     * from the page.
     * 
     * @return Last tuple in the page after removing it.
     */
    public Tuple removeLastTuple() {
        Tuple tupleRemovedTuple = vecTuples.remove(vecTuples.size() - 1);
        return tupleRemovedTuple;
    }

    /**
     * The getTuple function returns tuple in the page at index i.
     * 
     * @param i The `i` parameter in the `getTuple` method is an integer that is the
     *          index of teh tuple that you want to retrieve from the `vecTuples`
     * 
     * @return Tuple at index 'i'.
     */
    public Tuple getTuple(int i) {
        return vecTuples.get(i);
    }

    // // Returns the Tuples Vector (All Tuples in the Page)
    // public Vector<Tuple> getTuples() {
    // return vecTuples;
    // }

    /**
     * The `toString` function iterates through a list of `Tuple` objects and
     * concatenates their string
     * representations with a newline character.
     * 
     * @return The `toString` method is returning a string representation of the
     *         `vecTuples` list. It
     *         concatenates each `Tuple` object in the list with a newline character
     *         and returns the resulting
     *         string.
     */
    public String toString() {
        String res = "";

        for (Tuple tupleTuple : vecTuples) {
            res += tupleTuple + "\n";
        }

        return res;
    }

    /**
     * This Java function searches for tuples by a given clustering key name and
     * value using binary
     * search.
     * 
     * @param strClusteringKeyName  The `strClusteringKeyName` parameter is the name
     *                              of the clustering
     *                              key that you want to search for in the list of
     *                              tuples. It is used to identify the specific
     *                              attribute or column in the tuple that serves as
     *                              the clustering key for the data structure.
     * @param strClusteringKeyValue The `strClusteringKeyValue` parameter represents
     *                              the value of the
     *                              clustering key that you are searching for within
     *                              the list of tuples. The method
     *                              `searchTuplesByClusteringKey` is designed to
     *                              search for a specific tuple within the list of
     *                              tuples based on the provided clustering key name
     *                              and value.
     * @return The method is returning the index of the tuple in the `vecTuples`
     *         list that matches the
     *         provided clustering key name and value. If a match is found, the
     *         method returns the index of
     *         that tuple. If no match is found after the binary search, it throws a
     *         `DBAppException` with the
     *         message "Column Value doesn't exist".
     */
    public int searchTuplesByClusteringKey(String strClusteringKeyName, Object objClusteringKeyValue)
            throws DBAppException {

        int n = vecTuples.size();

        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            String strMidClusteringKeyValue = vecTuples.get(mid).getColumnValue(strClusteringKeyName).toString();

            System.out.println(mid + " " + strMidClusteringKeyValue + " " + objClusteringKeyValue);

            // convert the clustering key value to appropriate type for valid comparison
            // for example, if the clustering key is of type integer, then convert the
            // string value to integer
            // if the clustering key is of type double, then convert the string value to
            // double
            // if the clustering key is of type string, then no conversion is needed

            // TODO: REFACTOR THIS CODE

            if (objClusteringKeyValue instanceof Integer) {
                int intMidClusteringKeyValue = Integer.parseInt(strMidClusteringKeyValue);
                int intClusteringKeyValue = (int) objClusteringKeyValue;

                if (intMidClusteringKeyValue == intClusteringKeyValue) {
                    return mid;
                } else if (intMidClusteringKeyValue < intClusteringKeyValue) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else if (objClusteringKeyValue instanceof Double) {
                double dblMidClusteringKeyValue = Double.parseDouble(strMidClusteringKeyValue);
                double dblClusteringKeyValue = (double) objClusteringKeyValue;

                if (dblMidClusteringKeyValue == dblClusteringKeyValue) {
                    return mid;
                } else if (dblMidClusteringKeyValue < dblClusteringKeyValue) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                String strClusteringKeyValue = (String) objClusteringKeyValue;

                if (strMidClusteringKeyValue.equals(strClusteringKeyValue)) {
                    return mid;
                } else if (strMidClusteringKeyValue.compareTo(strClusteringKeyValue) < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

        }

        throw new DBAppException("Column Value doesn't exist");

    }

    /**
     * The `serialize` method writes the current object to a file using Java
     * serialization.
     * 
     * @param strFileName The `strFileName` parameter in the `serialize` method is a
     *                    `String` that
     *                    represents the name of the file to which the object will
     *                    be serialized. This parameter specifies
     *                    the file path where the serialized object will be saved.
     */
    public void serialize(String strFileName) throws IOException {

        // TODO: Exception Handling

        FileOutputStream fos = new FileOutputStream(strFileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);

        oos.close();
        fos.close();

    }

    /**
     * The `deserialize` function reads a serialized `Page` object from a file and
     * returns it.
     * 
     * @param strFileName The `strFileName` parameter in the `deserialize` method is
     *                    a `String` that
     *                    represents the file name of the file from which the `Page`
     *                    object will be deserialized.
     * @return The `deserialize` method is returning an object of type `Page` that
     *         has been
     *         deserialized from the file specified by the `strFileName` parameter.
     */
    public static Page deserialize(String strFileName) throws IOException, ClassNotFoundException {

        // TODO: Exception Handling

        FileInputStream fis = new FileInputStream(strFileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Page page = (Page) ois.readObject();

        ois.close();
        fis.close();

        return page;

    }

}
