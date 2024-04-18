package com.db_engine;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

public class Page implements Serializable {

    private Vector<Tuple> vecTuples;
    public static int intPageSize;

    private String strPageName;

    // The above code is a constructor for a Java class named `Page`. It takes a
    // `String` parameter
    // `strTableName` and initializes the `strPageName` attribute of the `Page`
    // object with the value
    // of `strTableName`. It also initializes a `Vector` named `vecTuples` to store
    // `Tuple` objects.
    public Page(String strTableName) {
        this.strPageName = strTableName;
        vecTuples = new Vector<Tuple>();
    }

    /**
     * The addTuple function adds a Tuple object at a specified index in a list of
     * Tuple objects.
     * 
     * @param index      The `index` parameter specifies the position at which the
     *                   `Tuple` object should be
     *                   added to the `vecTuples` vector.
     * @param tupleTuple The parameter `tupleTuple` is an object of type `Tuple`. It
     *                   represents a tuple
     *                   that you want to add to a list of tuples at a specific
     *                   index.
     */
    public void addTuple(int index, Tuple tupleTuple) {
        vecTuples.add(index, tupleTuple);
    }

    /**
     * The addTuple function adds a Tuple object to a list of tuples.
     * 
     * @param tupleTuple The parameter `tupleTuple` in the `addTuple` method is of
     *                   type `Tuple`, which
     *                   is the type of object that you are adding to the
     *                   `vecTuples` collection.
     */
    public void addTuple(Tuple tupleTuple) {
        vecTuples.add(tupleTuple);

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
        return vecTuples.remove(vecTuples.size() - 1);
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

    /**
     * The getSize() function returns the size of a vector of tuples.
     * 
     * @return The `getSize` method is returning the size of the `vecTuples`
     *         collection.
     */
    public int getSize() {
        return vecTuples.size();
    }

    /**
     * This Java function retrieves a Tuple object from a list based on a specified
     * index, throwing an
     * exception if the index is out of bounds.
     * 
     * @param i The parameter `i` represents the index of the tuple that you want to
     *          retrieve from the
     *          `vecTuples` list. If `i` is within the valid range of indices in the
     *          list, the method will
     *          return the tuple at that index. Otherwise, it will throw a
     *          `DBAppException`
     * @return A Tuple object is being returned.
     */
    public Tuple getTupleWithIndex(int i) throws DBAppException {

        if (i >= vecTuples.size())
            throw new DBAppException("Attempting to access a wrong tuple index");

        return vecTuples.get(i);
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

            Comparable midClusteringKeyValue = (Comparable) vecTuples.get(mid).getColumnValue(strClusteringKeyName);

            Comparable compClusteringKeyValue = (Comparable) objClusteringKeyValue;

            if (midClusteringKeyValue.compareTo(compClusteringKeyValue) == 0) {
                return mid;
            } else if (midClusteringKeyValue.compareTo(compClusteringKeyValue) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

        }

        throw new DBAppException("Column Value doesn't exist");

    }

    public void deleteTupleWithIndex(int i) throws DBAppException {
        if (i >= vecTuples.size())
            throw new DBAppException("Attempting to access a wrong tuple index");

        vecTuples.remove(i);
    }

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

    /*
     * The function `getPageName` returns the value of the `strPageName` variable as
     * a String.
     * 
     * @return The method `getPageName()` is returning the value of the variable
     * `strPageName`, which
     * presumably holds the name of a page.
     */
    public String getPageName() {
        return strPageName;
    }

    /**
     * The function `setPageName` sets the value of the `strPageName` variable to
     * the specified
     * parameter `strPageName`.
     * 
     * @param strPageName The `strPageName` parameter is a `String` that represents
     *                    the name of a page.
     */
    public static void deletePage(String strPageName) throws DBAppException {

        File filePage = new File(strPageName);

        if (filePage.delete()) {
            System.out.println(filePage.getName() + " is deleted!");
        } else {
            System.out.println("Delete operation is failed.");
        }

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
     * @return The method is returning the correct index position that the tuple
     *         should be inserted in. (In vecTuples)
     */

    public int binarySearchTuples(String strClusteringKeyName, Object objClusteringKeyValue)
            throws DBAppException {

        int n = vecTuples.size();

        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            String strMidClusteringKeyValue = vecTuples.get(mid).getColumnValue(strClusteringKeyName).toString();

            Comparable midClusteringKeyValue = (Comparable) vecTuples.get(mid).getColumnValue(strClusteringKeyName);

            Comparable compClusteringKeyValue = (Comparable) objClusteringKeyValue;

            if (midClusteringKeyValue.compareTo(compClusteringKeyValue) == 0) {
                return mid;
            } else if (midClusteringKeyValue.compareTo(compClusteringKeyValue) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

        }

        return left;

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
    public void serialize(String strFileName) throws DBAppException {

        try {

            FileOutputStream fos = new FileOutputStream(strFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);

            oos.close();
            fos.close();
        } catch (IOException ioe) {
            throw new DBAppException("Error in Serialization");
        }

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
    public static Page deserialize(String strFileName) throws DBAppException {

        try {

            FileInputStream fis = new FileInputStream(strFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Page page = (Page) ois.readObject();

            ois.close();
            fis.close();

            return page;
        } catch (IOException ioe) {
            throw new DBAppException("Error in Deserialization");
        } catch (ClassNotFoundException c) {
            throw new DBAppException("Class not found");
        }

    }

    // get all tuples in the page
    public Vector<Tuple> getTuples() {
        return this.vecTuples;
    }

    /**
     * The `deleteTuple` function removes a specified tuple from a vector of tuples.
     * 
     * @param tuple The `deleteTuple` method takes a `Tuple` object as a parameter.
     *              This `Tuple` object
     *              is used to identify and remove a specific tuple from the
     *              `vecTuples` collection.
     */
    public void deleteTuple(Tuple tuple) {
        // TODO: make vecTuple.remove() work

        for (int i = 0; i < vecTuples.size(); i++) {

            
            if (vecTuples.get(i).equals(tuple)) {
                
                vecTuples.remove(i);
                return;
            }
        }
    }

    public static void main(String[] args) {
        // create 5 page objects with 5 tuples and serialize them
        String strTableName = "Student";
        int intPageSize = 200;

        for (int i = 0; i < 5; i++) {
            Page page = new Page(strTableName + "_" + i);
            for (int j = 0; j < intPageSize; j++) {
                Tuple tuple = new Tuple();
                for (int k = 0; k < 5; k++) {
                    tuple.setColumnValue("id", i * intPageSize + j);
                    tuple.setColumnValue("name", "Ahmed" + i + j);
                    tuple.setColumnValue("gpa", 20);

                }
                page.addTuple(tuple);
            }
            try {
                page.serialize("tables/" + strTableName + "/" + strTableName + "_" + i + ".class");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The function `getVecTuples` returns a Vector of Tuple objects.
     * 
     * @return A Vector of Tuple objects named vecTuples is being returned.
     */
    public Vector<Tuple> getVecTuples() {

        return vecTuples;
    }

    public HashSet<Tuple> allTup() {
        HashSet<Tuple> hstups = new HashSet<>();
        for (Tuple tu : vecTuples) {
            hstups.add(tu);
        }
        return hstups;
    }

    public Object Min(String col) throws DBAppException {
        return vecTuples.get(0).getColumnValue(col);
    }

    public Object Max(String col) throws DBAppException {
        return vecTuples.get(vecTuples.size() - 1).getColumnValue(col);
    }

    public HashSet<Tuple> eqsearch(String col, Object val, boolean isclu, int index) throws DBAppException {
        HashSet<Tuple> hstups = new HashSet<>();
        if (isclu) {

            if (val instanceof Integer) {
                Integer te = (Integer) val;
                System.out.println(te);
                System.out.println((Integer) vecTuples.get(index).getColumnValue(col));
                if (((Integer) vecTuples.get(index).getColumnValue(col)).equals(te)) {
                    hstups.add(vecTuples.get(index));
                }
            } else if (val instanceof Double) {
                Double te = (Double) val;
                if (((Double) vecTuples.get(index).getColumnValue(col)).equals(te)) {
                    hstups.add(vecTuples.get(index));
                }
             else {
             else {
                String te = (String) val;
                if (((String) vecTuples.get(index).getColumnValue(col)).compareTo(te) == 0) {
                    hstups.add(vecTuples.get(index));
                }
            }
        }
        else {
            for (Tuple tu : vecTuples) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) tu.getColumnValue(col)).equals(te)) {
                        hstups.add(tu);
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) tu.getColumnValue(col)).equals(te)) {
                        hstups.add(tu);
                    }
                } else {
                    String te = (String) val;
                    if (((String) tu.getColumnValue(col)).compareTo(te) == 0) {
                        hstups.add(tu);
                    }
                }

            }
        }
        return hstups;
    }

    public HashSet<Tuple> gtrsearch(String col, Object val, boolean isclu, int index) throws DBAppException {
        HashSet<Tuple> hstups = new HashSet<>();
        if (isclu) {
            for (int i = index; i < vecTuples.size(); i++) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) vecTuples.get(i).getColumnValue(col)) > (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) vecTuples.get(i).getColumnValue(col)) > (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else {
                    String te = (String) val;
                    if (((String) vecTuples.get(i).getColumnValue(col)).compareTo(te) > 0) {
                        hstups.add(vecTuples.get(i));
                    }
                }

            }
        } else {
            for (Tuple tu : vecTuples) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) tu.getColumnValue(col)) > (te)) {
                        hstups.add(tu);
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) tu.getColumnValue(col)) > (te)) {
                        hstups.add(tu);
                    }
                } else {
                    String te = (String) val;
                    if (((String) tu.getColumnValue(col)).compareTo(te) > 0) {
                        hstups.add(tu);
                    }
                }

            }
        }
        return hstups;
    }

    public HashSet<Tuple> gtreqsearch(String col, Object val, boolean isclu, int index) throws DBAppException {
        HashSet<Tuple> hstups = new HashSet<>();
        if (isclu) {
            for (int i = index; i < vecTuples.size(); i++) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) vecTuples.get(i).getColumnValue(col)) >= (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) vecTuples.get(i).getColumnValue(col)) >= (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else {
                    String te = (String) val;
                    if (((String) vecTuples.get(i).getColumnValue(col)).compareTo(te) >= 0) {
                        hstups.add(vecTuples.get(i));
                    }
                }

            }
        } else {
            for (Tuple tu : vecTuples) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) tu.getColumnValue(col)) >= (te)) {
                        hstups.add(tu);
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) tu.getColumnValue(col)) >= (te)) {
                        hstups.add(tu);
                    }
                } else {
                    String te = (String) val;
                    if (((String) tu.getColumnValue(col)).compareTo(te) >= 0) {
                        hstups.add(tu);
                    }
                }

            }
        }
        return hstups;
    }

    public HashSet<Tuple> lessearch(String col, Object val, boolean isclu, int index) throws DBAppException {
        HashSet<Tuple> hstups = new HashSet<>();
        if (isclu) {
            for (int i = 0; i <= index; i++) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) vecTuples.get(i).getColumnValue(col)) < (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) vecTuples.get(i).getColumnValue(col)) < (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else {
                    String te = (String) val;
                    if (((String) vecTuples.get(i).getColumnValue(col)).compareTo(te) < 0) {
                        hstups.add(vecTuples.get(i));
                    }
                }

            }
        } else {
            for (Tuple tu : vecTuples) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) tu.getColumnValue(col)) < (te)) {
                        hstups.add(tu);
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) tu.getColumnValue(col)) < (te)) {
                        hstups.add(tu);
                    }
                } else {
                    String te = (String) val;
                    if (((String) tu.getColumnValue(col)).compareTo(te) < 0) {
                        hstups.add(tu);
                    }
                }

            }
        }
        return hstups;
    }

    public HashSet<Tuple> leseqsearch(String col, Object val, boolean isclu, int index) throws DBAppException {
        HashSet<Tuple> hstups = new HashSet<>();
        if (isclu) {
            for (int i = 0; i <= index; i++) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) vecTuples.get(i).getColumnValue(col)) <= (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) vecTuples.get(i).getColumnValue(col)) < (te)) {
                        hstups.add(vecTuples.get(i));
                    }
                } else {
                    String te = (String) val;
                    if (((String) vecTuples.get(i).getColumnValue(col)).compareTo(te) < 0) {
                        hstups.add(vecTuples.get(i));
                    }
                }

            }
        } else {
            for (Tuple tu : vecTuples) {
                if (val instanceof Integer) {
                    Integer te = (Integer) val;
                    if (((Integer) tu.getColumnValue(col)) <= (te)) {
                        hstups.add(tu);
                    }
                } else if (val instanceof Double) {
                    Double te = (Double) val;
                    if (((Double) tu.getColumnValue(col)) <= (te)) {
                        hstups.add(tu);
                    }
                } else {
                    String te = (String) val;
                    if (((String) tu.getColumnValue(col)).compareTo(te) <= 0) {
                        hstups.add(tu);
                    }
                }

            }
        }
        return hstups;
    }

    public HashSet<Tuple> noteqsearch(String col, Object val) throws DBAppException {
        HashSet<Tuple> hstups = new HashSet<>();
        for (Tuple tu : vecTuples) {
            if (val instanceof Integer) {
                Integer te = (Integer) val;
                if (!((Integer) tu.getColumnValue(col)).equals(te)) {
                    hstups.add(tu);
                }
            } else if (val instanceof Double) {
                Double te = (Double) val;
                if (!((Double) tu.getColumnValue(col)).equals(te)) {
                    hstups.add(tu);
                }
            } else {
                String te = (String) val;
                if (((String) tu.getColumnValue(col)).compareTo(te) != 0) {
                    hstups.add(tu);
                }
            }

        }
        return hstups;
    }

}