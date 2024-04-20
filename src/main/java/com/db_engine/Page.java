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
     * @param objClusteringKeyValue The `objClusteringKeyValue` parameter represents
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

        return -1;

    }

    /**
     * The function `deleteTupleWithIndex` removes a tuple from a list at a specified index in Java,
     * throwing an exception if the index is out of bounds.
     * 
     * @param i The parameter `i` in the `deleteTupleWithIndex` method represents the index of the
     * tuple that needs to be deleted from the `vecTuples` vector. If the index `i` is within the valid
     * range of the vector, the tuple at that index will be removed.
     */
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
     * @param objClusteringKeyValue The `objClusteringKeyValue` parameter represents
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

        Comparable cmpClusteringKeyValue = (Comparable) objClusteringKeyValue;

        // Base cases
        if (cmpClusteringKeyValue.compareTo(vecTuples.get(0).getColumnValue(strClusteringKeyName)) < 0)
            return 0;
        else if (cmpClusteringKeyValue.compareTo(vecTuples.get(n - 1).getColumnValue(strClusteringKeyName)) > 0)
            return n - 1;

        int lowerPnt = 0;
        int i = 1;

        while (i < n && ((Comparable) vecTuples.get(i).getColumnValue(strClusteringKeyName))
                .compareTo(cmpClusteringKeyValue) > 0) {
            lowerPnt = i;
            i = i * 2;
        }

        // Final check for the remaining elements which are < X
        while (lowerPnt < n && ((Comparable) vecTuples.get(lowerPnt).getColumnValue(strClusteringKeyName))
                .compareTo(cmpClusteringKeyValue) > 0)
            lowerPnt++;

        return lowerPnt;

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
     * @param objClusteringKeyValue The `objClusteringKeyValue` parameter represents
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

    public int binarySearchInsertTuples(String strClusteringKeyName, Object objClusteringKeyValue)
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

        Comparable cmpClusteringKeyValue = (Comparable) objClusteringKeyValue;

        // Base cases
        if (cmpClusteringKeyValue.compareTo(vecTuples.get(0).getColumnValue(strClusteringKeyName)) < 0)
            return 0;
        else if (cmpClusteringKeyValue.compareTo(vecTuples.get(n - 1).getColumnValue(strClusteringKeyName)) > 0)
            return n - 1;

        int lowerPnt = 0;
        int i = 1;

        while (i < n && ((Comparable) vecTuples.get(i).getColumnValue(strClusteringKeyName))
                .compareTo(cmpClusteringKeyValue) < 0) {
            lowerPnt = i;
            i = i * 2;
        }

        // Final check for the remaining elements which are < X
        while (lowerPnt < n && ((Comparable) vecTuples.get(lowerPnt).getColumnValue(strClusteringKeyName))
                .compareTo(cmpClusteringKeyValue) < 0)
            lowerPnt++;

        return lowerPnt;

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

    /**
     * The Min function returns the minimum value of a specified column from the first tuple in a
     * vector of tuples.
     * 
     * @param col The parameter "col" in the method Min(String col) represents the name of the column
     * for which you want to find the minimum value.
     * @return The `Min` method is returning the value of the specified column (`col`) from the first
     * tuple in the `vecTuples` vector.
     */
    public Object Min(String col) throws DBAppException {
        return vecTuples.get(0).getColumnValue(col);
    }

    /**
     * The Max function retrieves the value of a specified column from the last tuple in a vector of
     * tuples.
     * 
     * @param col The parameter `col` in the `Max` method represents the name of the column for which
     * you want to find the maximum value. The method retrieves the value of this column from the last
     * tuple in the `vecTuples` list and returns it as an `Object`.
     * @return The `Max` method is returning the value of the specified column (`col`) from the last
     * tuple in the `vecTuples` vector.
     */
    public Object Max(String col) throws DBAppException {
        return vecTuples.get(vecTuples.size() - 1).getColumnValue(col);
    }

    /**
     * This Java function `eqsearch` searches for tuples in a HashSet based on a specified column value
     * and data type.
     * 
     * @param col The `col` parameter in the `eqsearch` method represents the name of the column you
     * want to search for equality.
     * @param val The `val` parameter in the `eqsearch` method represents the value that you are
     * searching for in the specified column (`col`). The method searches for tuples in a collection
     * (`vecTuples`) where the value in the specified column matches the provided `val`.
     * @param isclu The `isclu` parameter in the `eqsearch` method stands for "is clustered". It is a
     * boolean flag that indicates whether the search is being performed on a clustered index or not.
     * If `isclu` is true, the search will be optimized for the clustered index, otherwise
     * @param index The `index` parameter in the `eqsearch` method represents the index of the tuple
     * within the `vecTuples` list that you want to search for equality with the specified column value
     * and object value. It is used to access a specific tuple within the list for comparison.
     * @return The method `eqsearch` returns a `HashSet` of `Tuple` objects that match the specified
     * criteria based on the input parameters `col`, `val`, `isclu`, and `index`.
     */
    public HashSet<Tuple> eqsearch(String col, Object val, boolean isclu, int index) throws DBAppException {
        HashSet<Tuple> hstups = new HashSet<>();
        if (isclu) {
            if (val instanceof Integer) {
                Integer te = (Integer) val;
                if (((Integer) vecTuples.get(index).getColumnValue(col)).equals(te)) {
                    hstups.add(vecTuples.get(index));
                }
            } else if (val instanceof Double) {
                Double te = (Double) val;
                if (((Double) vecTuples.get(index).getColumnValue(col)).equals(te)) {
                    hstups.add(vecTuples.get(index));
                }
            } else {
                String te = (String) val;
                if (((String) vecTuples.get(index).getColumnValue(col)).compareTo(te) == 0) {
                    hstups.add(vecTuples.get(index));
                }
            }
        } else {
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

    /**
     * The function `getPageByClusteringKey` searches for a specific page in a table
     * based on a given
     * clustering key value.
     *
     * @param strTableName          It seems like you were about to provide some
     *                              information about the parameters,
     *                              but the message got cut off. Could you please
     *                              provide more details about the parameters
     *                              `strTableName`, `strClusteringKey`, and
     *                              `objClusteringKeyValue` so that I can assist you
     *                              further
     *                              with the `getPageByCl
     * @param strClusteringKey      It seems like you were about to provide some
     *                              information about the
     *                              parameter `strClusteringKey` but the message got
     *                              cut off. How can I assist you further with this
     *                              parameter?
     * @param objClusteringKeyValue It seems like the method
     *                              `getPageByClusteringKey` is trying to find a
     *                              specific page in a table based on a clustering
     *                              key value. The method uses binary search to
     *                              locate
     *                              the page efficiently.
     * @return The method `getPageByClusteringKey` is returning a `Page` object. If
     *         the clustering key
     *         value is found within the specified range in the page, then that page
     *         is returned. If the key is
     *         not found within the range, the method will continue searching
     *         through the pages until it either
     *         finds the key or exhausts all pages, in which case it will return
     *         `null`.
     */
    public static Page getPageByClusteringKey(String strTableName, Object objClusteringKeyValue,
            Table table) throws DBAppException {

        int intTableSize = table.getNumberOfPages();
        int intTopPageIndex = 0;
        int intBottomPageIndex = intTableSize - 1;

        while (intTopPageIndex <= intBottomPageIndex) {

            int intMiddlePageIndex = intTopPageIndex + (intBottomPageIndex - intTopPageIndex) / 2;

            String strMiddlePage = table.getPageAtIndex(intMiddlePageIndex);

            Comparable cmpPageMinValue = table.getMin(strMiddlePage);

            Comparable cmpPageMaxValue = table.getMax(strMiddlePage);

            // convert the object to comparable to compare it with the clustering key

            Comparable cmpClusteringKeyValue = (Comparable) objClusteringKeyValue;

            // check if the clustering key is in the page by checking if the value is
            // between the top and bottom tuple
            if (cmpClusteringKeyValue.compareTo(cmpPageMinValue) >= 0
                    && cmpClusteringKeyValue
                            .compareTo(cmpPageMaxValue) <= 0) {
                Page pageMiddlePage = Page.deserialize("tables/" + strTableName + "/" + strMiddlePage + ".class");
                return pageMiddlePage;
            } else if (cmpClusteringKeyValue.compareTo(cmpPageMinValue) < 0) {
                intBottomPageIndex = intMiddlePageIndex - 1;
            } else {
                intTopPageIndex = intMiddlePageIndex + 1;
            }
        }

        return null;

    }

    /**
     * The function `getPageIndexByClusteringKey` searches for a specific clustering key within a table
     * using binary search.
     * 
     * @param objClusteringKeyValue The `objClusteringKeyValue` parameter is the value of the
     * clustering key that you want to find the corresponding page index for in the given table. This
     * value is used to determine the page where the clustering key resides within the table's
     * clustered index.
     * @param table The `table` parameter in the `getPageIndexByClusteringKey` method represents a
     * table in a database. It contains information about the table structure, such as the number of
     * pages, minimum values for each page (stored in `table.getMinVec()`), and maximum values for each
     * page (stored
     * @return The method `getPageIndexByClusteringKey` returns the index of the page in the table
     * where the given clustering key falls within the range of values defined by the minimum and
     * maximum values of the page. If the clustering key is found within a page, the method returns the
     * index of that page. If the clustering key is not found within any page, the method returns -1.
     */
    public static int getPageIndexByClusteringKey(Object objClusteringKeyValue , Table table) {
        int intTableSize = table.getNumberOfPages();
        int intTopPageIndex = 0;
        int intBottomPageIndex = intTableSize - 1;

        while (intTopPageIndex <= intBottomPageIndex) {

            int intMiddlePageIndex = intTopPageIndex + (intBottomPageIndex - intTopPageIndex) / 2;

            Comparable cmpPageMinValue = table.getMinVec().get(intMiddlePageIndex);

            Comparable cmpPageMaxValue = table.getMaxVec().get(intMiddlePageIndex);

            // convert the object to comparable to compare it with the clustering key

            Comparable cmpClusteringKeyValue = (Comparable) objClusteringKeyValue;

            // check if the clustering key is in the page by checking if the value is
            // between the top and bottom tuple
            if (cmpClusteringKeyValue.compareTo(cmpPageMinValue) >= 0
                    && cmpClusteringKeyValue
                    .compareTo(cmpPageMaxValue) <= 0) {
                return intMiddlePageIndex;
            } else if (cmpClusteringKeyValue.compareTo(cmpPageMinValue) < 0) {
                intBottomPageIndex = intMiddlePageIndex - 1;
            } else {
                intTopPageIndex = intMiddlePageIndex + 1;
            }
        }

        return -1;
    }

    /**
     * The `gtrsearch` function searches for tuples in a HashSet based on a specified column value
     * being greater than a given value.
     * 
     * @param col The `col` parameter in the `gtrsearch` method represents the name of the column in
     * the database table on which you want to perform the greater than search.
     * @param val The `val` parameter in the `gtrsearch` method represents the value that you want to
     * compare against the column values in the tuples. It can be of type Integer, Double, or String
     * based on the data type of the column you are comparing it with. The method will return a HashSet
     * @param isclu The `isclu` parameter in the `gtrsearch` method indicates whether the search is
     * being performed on a clustered index or not. If `isclu` is `true`, the method will start the
     * search from the specified `index` in the `vecTuples` list.
     * @param index The `index` parameter in the `gtrsearch` method represents the starting index from
     * which the search for tuples will begin in the `vecTuples` list. The method will iterate over the
     * tuples starting from this index to the end of the list if the `isclu` parameter is
     * @return The method `gtrsearch` returns a `HashSet` containing `Tuple` objects that meet the
     * specified condition based on the input parameters `col`, `val`, `isclu`, and `index`.
     */
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

    /**
     * This Java function `gtreqsearch` searches for tuples in a HashSet based on a specified column
     * and value, considering whether the column is clustered or not.
     * 
     * @param col The `col` parameter in the `gtreqsearch` method represents the column name on which
     * you want to perform the greater than or equal to search. This method searches for tuples in a
     * collection where the value in the specified column is greater than or equal to the provided
     * value.
     * @param val The `val` parameter in the `gtreqsearch` method represents the value that you want to
     * compare against in the greater than or equal search operation. It can be of type Integer,
     * Double, or String depending on the data type of the column you are comparing it with.
     * @param isclu The `isclu` parameter in the `gtreqsearch` method indicates whether the search is
     * being performed on a clustered index or not. If `isclu` is `true`, the method will start the
     * search from the specified `index` in the `vecTuples` list.
     * @param index The `index` parameter in the `gtreqsearch` method represents the starting index
     * from which the search for values greater than or equal to the specified value (`val`) should
     * begin in the `vecTuples` list. The method iterates over the tuples in `vecTuples` starting from
     * @return This method `gtreqsearch` returns a `HashSet` of `Tuple` objects that meet the specified
     * criteria based on the input parameters `col`, `val`, `isclu`, and `index`.
     */
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

    /**
     * This Java function searches for tuples in a HashSet based on a specified column value and
     * comparison criteria.
     * 
     * @param col The `col` parameter in the `lessearch` method represents the name of the column you
     * want to search in the tuples. It is used to specify the column on which the comparison will be
     * based when filtering the tuples.
     * @param val The `val` parameter in the `lessearch` method represents the value that you want to
     * compare with the column values in the tuples. It can be of type Integer, Double, or String
     * depending on the data type of the column you are comparing it with. The method then checks if
     * the column
     * @param isclu The `isclu` parameter in the `lessearch` method stands for "is clustered." It is a
     * boolean flag that indicates whether the search should be limited to a specific index or not. If
     * `isclu` is true, the search will only consider tuples up to the specified index
     * @param index The `index` parameter in the `lessearch` method represents the index up to which
     * you want to search for tuples in the `vecTuples` list. The method will iterate through the
     * tuples in the list up to this index and check if the specified column value is less than the
     * given value
     * @return This method `lessearch` returns a `HashSet` of `Tuple` objects that meet the specified
     * condition based on the input parameters `col`, `val`, `isclu`, and `index`.
     */
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

    /**
     * The `leseqsearch` function searches for tuples in a vector based on a specified column and
     * value, considering whether the search is inclusive or exclusive.
     * 
     * @param col The `col` parameter in the `leseqsearch` method represents the name of the column in
     * the database table that you want to search for. It is used to specify the column on which the
     * comparison operation will be performed.
     * @param val The `val` parameter in the `leseqsearch` method represents the value that you are
     * searching for in the specified column. It can be of type Integer, Double, or String based on the
     * data type of the column you are searching in. The method then iterates through the tuples in the
     * @param isclu The `isclu` parameter in the `leseqsearch` method indicates whether the search is
     * inclusive or not. If `isclu` is `true`, the search will include the value being compared (i.e.,
     * it will be less than or equal to). If `isclu
     * @param index The `index` parameter in the `leseqsearch` method represents the index up to which
     * you want to search in the `vecTuples` list. The method iterates through the tuples in
     * `vecTuples` up to this index to perform the comparison with the specified value based on the
     * @return The method `leseqsearch` returns a `HashSet` containing `Tuple` objects that meet the
     * specified condition based on the input parameters `col`, `val`, `isclu`, and `index`.
     */
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

    /**
     * The `noteqsearch` function searches for tuples in a collection based on a specified column and
     * value, returning a HashSet of tuples that do not match the value in that column.
     * 
     * @param col The `col` parameter in the `noteqsearch` method represents the column name in the
     * database table on which you want to perform the inequality search. This method searches for
     * tuples in the `vecTuples` collection where the value in the specified column is not equal to the
     * provided value (`val`).
     * @param val The `val` parameter in the `noteqsearch` method represents the value that you want to
     * search for in the specified column `col`. Depending on the type of `val` (Integer, Double, or
     * String), the method iterates through the `vecTuples` collection of tuples and adds
     * @return The method `noteqsearch` returns a `HashSet` containing `Tuple` objects that have a
     * column value different from the specified value `val` for the given column `col`.
     */
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