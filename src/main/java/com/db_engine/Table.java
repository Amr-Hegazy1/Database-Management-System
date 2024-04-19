package com.db_engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Table implements Serializable {
    private String strTableName;

    private Vector<String> vecPages;
    private Vector<Comparable> vecMin;
    private Vector<Comparable> vecMax;

    public Table(String strTableName) {
        this.strTableName = strTableName;
        this.vecPages = new Vector<String>();
        this.vecMin = new Vector<Comparable>();
        this.vecMax = new Vector<Comparable>();
    }

    /**
     * This Java function returns the number of pages in a vector.
     * 
     * @return The method `getNumberOfPages` returns the number of elements in the
     *         `vecPages` vector.
     */
    public int getNumberOfPages() {
        return vecPages.size();
    }

    /**
     * The addPage function adds an auto-generated page name to the Table
     * (vecPages).
     * 
     * @return The method `addPage` is returning the name of the new page that was
     *         added to the 'vecpages' Vector of the Table.
     */
    public String addPage() {
        int newPageNum = vecPages.size();
        String newPage = strTableName + "_" + newPageNum;

        vecPages.add(newPage);
        return newPage;
    }

    /**
     * The getPageVector returns vector of page names 'vecPages'.
     * 
     * @return The method `getPageVector` is returning the vector of page names.
     */
    public Vector<String> getPageVector() {
        return vecPages;
    }

    /**
     * The getNumofPages returns Number of Pages in the Table.
     * 
     * @return The method `getNumofPages` is returning the number of pages in the
     *         Table.
     */
    public int getNumofPages() {
        return vecPages.size();
    }

    /**
     * The `serialize` method writes the current object to a file using Java
     * serialization.
     * 
     * @param strFileName The `strFileName` parameter in the `serialize` method is a
     *                    `String` that
     *                    represents the name of the file to which the object will
     *                    be serialized. This parameter specifies
     *                    the file path where the object will be written in
     *                    serialized form.
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
     * The function `deserialize` reads a serialized `Table` object from a file and
     * returns it.
     * 
     * @param strFileName The `strFileName` parameter in the `deserialize` method is
     *                    a `String` that
     *                    represents the file name of the file from which the
     *                    `Table` object will be deserialized.
     * @return The `deserialize` method is returning an object of type `Table`.
     */
    public static Table deserialize(String strFileName) throws DBAppException {

        try {

            FileInputStream fis = new FileInputStream(strFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Table table = (Table) ois.readObject();

            ois.close();
            fis.close();
            return table;
        } catch (IOException ioe) {
            throw new DBAppException("Error in Deserialization");
        } catch (ClassNotFoundException c) {
            throw new DBAppException("Class not found");
        }

    }

    // get all the pages used to store the table's data
    public Vector<String> getPages() {
        return this.vecPages;
    }

    /**
     * The function `getMin` retrieves the minimum value associated with a given
     * page name.
     * 
     * @param pageName The `pageName` parameter is a string that represents the name
     *                 of a page for
     *                 which you want to retrieve the minimum value.
     * @return The `getMin` method is returning the minimum value associated with
     *         the given `pageName`.
     *         It retrieves this value from the `vecMin` list using the index of the
     *         `pageName` in the
     *         `vecPages` list.
     */
    public Comparable getMin(String pageName) {
        // get the minimum value of a page
        return this.vecMin.get(this.vecPages.indexOf(pageName));
    }

    /**
     * The function `getMax` retrieves the maximum value associated with a given
     * page name.
     * 
     * @param pageName The `pageName` parameter is a string that represents the name
     *                 of a page for
     *                 which you want to retrieve the maximum value.
     * @return The `getMax` method is returning the maximum value associated with
     *         the given `pageName`.
     *         It retrieves this value from the `vecMax` list using the index of the
     *         `pageName` in the
     *         `vecPages` list.
     */
    public Comparable getMax(String pageName) {
        // get the maximum value of a page
        return this.vecMax.get(this.vecPages.indexOf(pageName));
    }

    public Vector<Comparable> getMaxVec(String pageName) {
        // get the maximum value of a page
        return this.vecMax;
    }

    public int getMaxIndex(String pageName) {
        // get the maximum value of a page
        return this.vecPages.indexOf(pageName);
    }

    /**
     * The function `setMin` sets the minimum value associated with a given page
     * name.
     * 
     * @param pageName The `pageName` parameter is a string that represents the name
     *                 of a page for
     *                 which you want to set the minimum value.
     * @param min      The `min` parameter is a `Comparable` object that represents
     *                 the minimum value you
     *                 want to set for the given `pageName`.
     */
    public void setMin(String pageName, Comparable min) {
        // set the minimum value of a page
        int index = this.vecPages.indexOf(pageName);
        if (index < vecMin.size()) { // If the index is less than the size of the vector, replace the existing value
            this.vecMin.set(index, min);
        } else { // If the index is greater than the size of the vector, add the value to the
                 // vector
            this.vecMin.add(min);
        }
    }

    public Vector<Comparable> getMinVec(String pageName) {
        // get the maximum value of a page
        return this.vecMin;
    }

    /**
     * The setMin function updates the minimum value of a page at a specified index in a vector, either
     * by replacing the existing value if the index is within the vector's size or by adding the value
     * to the vector if the index is greater.
     * 
     * @param index The `index` parameter represents the position at which you want to set the minimum
     * value in the vector `vecMin`. If the index is within the current size of the vector, the
     * existing value at that index will be replaced with the new minimum value. If the index is
     * greater than the current size
     * @param min The `min` parameter in the `setMin` method represents the minimum value that you want
     * to set for a specific index in a vector or list. It is of type `Comparable`, which means it can
     * hold any object that implements the `Comparable` interface, allowing for comparison between
     * objects.
     */
    public void setMin (int index, Comparable min) {
        // set the minimum value of a page
        if (index < vecMin.size()) { // If the index is less than the size of the vector, replace the existing value
            this.vecMin.set(index, min);
        } else { // If the index is greater than the size of the vector, add the value to the
                 // vector
            this.vecMin.add(min);
        }
    }

    /**
     * The function `setMax` sets the maximum value associated with a given page
     * name.
     * 
     * @param pageName The `pageName` parameter is a string that represents the name
     *                 of a page for
     *                 which you want to set the maximum value.
     * @param max      The `max` parameter is a `Comparable` object that represents
     *                 the maximum value you
     *                 want to set for the given `pageName`.
     */

    public void setMax(String pageName, Comparable max) {
        // set the maximum value of a page
        int index = this.vecPages.indexOf(pageName);
        if (index < vecMax.size()) { // If the index is less than the size of the vector, replace the existing value
            this.vecMax.set(index, max);
        } else { // If the index is greater than the size of the vector, add the value to the
                 // vector
            this.vecMax.add(max);
        }
    }

    /**
     * The `setMax` function in Java sets the maximum value of a page at a specified index in a vector,
     * replacing the existing value if the index is within the vector's size or adding the value to the
     * vector if the index is greater.
     * 
     * @param index Index is the position at which the maximum value needs to be set or updated in the
     * vector.
     * @param max The `max` parameter in the `setMax` method represents the maximum value that you want
     * to set for a specific index in a vector or list. It is of type `Comparable`, which means it can
     * be any object that implements the `Comparable` interface, allowing for comparison with other
     * objects of the
     */
    public void setMax(int index, Comparable max) {
        // set the maximum value of a page
        if (index < vecMax.size()) { // If the index is less than the size of the vector, replace the existing value
            this.vecMax.set(index, max);
        } else { // If the index is greater than the size of the vector, add the value to the
                 // vector
            this.vecMax.add(max);
        }
    }

    /**
     * The `removeMin` function removes the minimum value from a list based on the
     * provided page name.
     * 
     * @param pageName The `pageName` parameter is a string that represents the name
     *                 of the page from
     *                 which the minimum value needs to be removed.
     */
    public void removeMin(String pageName) {
        // remove the minimum value of a page
        System.out.println(vecPages);
        System.out.println(pageName);
        this.vecMin.remove(this.vecPages.indexOf(pageName));
    }


    /**
     * The `removeMin` function removes the minimum value at a specified index from a vector.
     * 
     * @param index The `index` parameter specifies the position of the minimum value that you want to
     * remove from the `vecMin` vector.
     */
    public void removeMin(int index){
        //remove the minimum value of a page
        this.vecMin.remove(index);
    }

    /**
     * The `removeMax` function removes the maximum value from a page in a Java
     * program.
     * 
     * @param pageName The `pageName` parameter is a string that represents the name
     *                 of the page from
     *                 which you want to remove the maximum value.
     */
    public void removeMax(String pageName) {
        // remove the maximum value of a page
        this.vecMax.remove(this.vecPages.indexOf(pageName));
    }

    

   /**
    * The `removeMax` function removes the maximum value at a specified index from a list.
    * 
    * @param index The `index` parameter specifies the position of the maximum value that you want to
    * remove from the `vecMax` list.
    */
    public void removeMax(int index){
        //remove the maximum value of a page
        this.vecMax.remove(index);
    }


    
    /**
     * The function `getPageAtIndex` returns the page at the specified index from a
     * vector of pages.
     * 
     * @param i The parameter `i` in the `getPageAtIndex` method represents the
     *          index of the page you
     *          want to retrieve from the `vecPages` vector.
     * @return The method `getPageAtIndex` is returning the page at the specified
     *         index `i` from the
     *         `vecPages` vector.
     */
    public String getPageAtIndex(int i) {
        return vecPages.get(i);
    }

    /**
     * The `removePage` function removes a page with the specified name from a
     * vector of pages.
     * 
     * @param strPageName The parameter `strPageName` is a String representing the
     *                    name of the page
     *                    that you want to remove from the `vecPages` vector.
     */
    public void removePage(String strPageName) {

        vecPages.remove(strPageName);

    }

    public void removePage(int index) {
        vecPages.remove(index);
    }

   
    /**
     * The function `printAllPages` iterates through all pages in a table,
     * deserializes each page, and
     * prints it to the console.
     * 
     */
    public void printAllPages() throws DBAppException {
        for (int i = 0; i < this.getNumberOfPages(); i++) {

            String strPage = this.getPageAtIndex(i);
            Page page = Page.deserialize(strPage + ".class");
            System.out.println(
                    "#################################### PAGE " + i + " ########################################");
            System.out.println(page);
        }
    }

    /**
     * The function getTableName() returns the name of the table as a String.
     * 
     * @return The method getTableName() returns the value of the variable
     *         strTableName, which is the
     *         name of the table.
     */
    public String getTableName() {
        return strTableName;
    }

    public HashSet<Tuple> greaterthan(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {

        HashSet<Tuple> hmtup = new HashSet<>();
        boolean noneed = false;
        for (int i = 0; i < vecPages.size(); i++) {
            if (val instanceof Integer) {
                Integer temp = (Integer) val;

                if (((Integer) getMin(vecPages.get(i))) <= temp && ((Integer) getMax(vecPages.get(i))) > temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.gtrsearch(col, val, true, index));
                    noneed = true;
                } else if (noneed || ((Integer) getMin(vecPages.get(i)) < temp)) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                }
            } else if (val instanceof Double) {
                Double temp = (Double) val;
                if (noneed || ((Double) getMin(vecPages.get(i))) < temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if ((Double) getMin(vecPages.get(i)) <= temp && ((Double) getMax(vecPages.get(i))) > temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.gtrsearch(col, val, true, index));
                    noneed = true;
                }
            } else {
                String temp = (String) val;
                if (noneed || ((String) getMin(vecPages.get(i))).compareTo(temp) < 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if (((String) getMin(vecPages.get(i))).compareTo(temp) <= 0
                        && ((String) getMax(vecPages.get(i))).compareTo(temp) > 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.gtrsearch(col, val, true, index));
                    noneed = true;
                }
            }
        }
        return hmtup;
    }

    public HashSet<Tuple> greaterthannc(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        for (int i = 0; i < vecPages.size(); i++) {
            Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
            hmtup.addAll(page1.gtrsearch(col, val, false, 201));
        }
        return hmtup;
    }

    // greater than equal
    public HashSet<Tuple> greaterthaneq(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        boolean noneed = false;
        for (int i = 0; i < vecPages.size(); i++) {
            if (val instanceof Integer) {
                Integer temp = (Integer) val;
                if (noneed || ((Integer) getMin(vecPages.get(i))) <= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if ((Integer) getMin(vecPages.get(i)) <= temp && ((Integer) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.gtreqsearch(col, val, true, index));
                    noneed = true;
                }
            } else if (val instanceof Double) {
                Double temp = (Double) val;
                if (noneed || ((Double) getMin(vecPages.get(i))) <= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if ((Double) getMin(vecPages.get(i)) <= temp && ((Double) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.gtreqsearch(col, val, true, index));
                    noneed = true;
                }
            } else {
                String temp = (String) val;
                if (noneed || ((String) getMin(vecPages.get(i))).compareTo(temp) <= 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if (((String) getMin(vecPages.get(i))).compareTo(temp) <= 0
                        && ((String) getMax(vecPages.get(i))).compareTo(temp) >= 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.gtreqsearch(col, val, true, index));
                    noneed = true;
                }
            }
        }
        return hmtup;
    }

    public HashSet<Tuple> greaterthaneqnc(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        for (int i = 0; i < vecPages.size(); i++) {
            Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
            hmtup.addAll(page1.gtreqsearch(col, val, false, 201));
        }
        return hmtup;
    }

    public HashSet<Tuple> lesserthan(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        boolean noneed = false;
        for (int i = vecPages.size() - 1; i >= 0; i--) {
            if (val instanceof Integer) {
                Integer temp = (Integer) val;
                if (noneed || ((Integer) getMax(vecPages.get(i))) < temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if ((Integer) getMin(vecPages.get(i)) < temp && ((Integer) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed = true;
                }
            } else if (val instanceof Double) {
                Double temp = (Double) val;
                if (noneed || ((Double) getMax(vecPages.get(i))) < temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if ((Double) getMin(vecPages.get(i)) < temp && ((Double) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed = true;
                }
            } else {
                String temp = (String) val;
                if (noneed || ((String) getMax(vecPages.get(i))).compareTo(temp) < 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if (((String) getMin(vecPages.get(i))).compareTo(temp) < 0
                        && ((String) getMax(vecPages.get(i))).compareTo(temp) >= 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed = true;
                }
            }
        }
        return hmtup;
    }

    public HashSet<Tuple> lesserthannc(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        for (int i = 0; i < vecPages.size(); i++) {
            Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
            hmtup.addAll(page1.lessearch(col, val, false, 201));
        }
        return hmtup;
    }

    public HashSet<Tuple> lesserthaneq(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        boolean noneed = false;
        for (int i = vecPages.size() - 1; i >= 0; i--) {

            if (val instanceof Integer) {
                Integer temp = (Integer) val;
                if (noneed || ((Integer) getMax(vecPages.get(i))) <= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if ((Integer) getMin(vecPages.get(i)) <= temp && ((Integer) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.leseqsearch(col, val, true, index));
                    noneed = true;
                }
            } else if (val instanceof Double) {
                Double temp = (Double) val;
                if (noneed || ((Double) getMax(vecPages.get(i))) <= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if ((Double) getMin(vecPages.get(i)) <= temp && ((Double) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.leseqsearch(col, val, true, index));
                    noneed = true;
                }
            } else {
                String temp = (String) val;
                if (noneed || ((String) getMax(vecPages.get(i))).compareTo(temp) <= 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    hmtup.addAll(page1.allTup());
                } else if (((String) getMin(vecPages.get(i))).compareTo(temp) <= 0
                        && ((String) getMax(vecPages.get(i))).compareTo(temp) >= 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed = true;
                }
            }
        }
        return hmtup;
    }

    public HashSet<Tuple> lesserthannceq(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        for (int i = 0; i < vecPages.size(); i++) {
            Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
            hmtup.addAll(page1.leseqsearch(col, val, false, 201));
        }
        return hmtup;
    }

    public HashSet<Tuple> cleqsearch(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        for (int i = 0; i < vecPages.size(); i++) {
            if (val instanceof Integer) {
                Integer temp = (Integer) val;

                if ((Integer) getMin(vecPages.get(i)) <= temp && ((Integer) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.eqsearch(col, val, true, index));
                    break;
                }
            } else if (val instanceof Double) {
                Double temp = (Double) val;
                if ((Double) getMin(vecPages.get(i)) <= temp && ((Double) getMax(vecPages.get(i))) >= temp) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.eqsearch(col, val, true, index));
                    break;
                }
            } else {
                String temp = (String) val;
                if (((String) getMin(vecPages.get(i))).compareTo(temp) <= 0
                        && ((String) getMax(vecPages.get(i))).compareTo(temp) >= 0) {
                    Page page1 = Page.deserialize("tables/" + strTableName + "/" + vecPages.get(i) + ".class");
                    int index = page1.binarySearchTuples(col, val);
                    hmtup.addAll(page1.eqsearch(col, val, true, index));
                    break;
                }
            }
        }
        return hmtup;
    }

    public HashSet<Tuple> eqsearch(String col, Object val) throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        for (String e : vecPages) {
            Page page = Page.deserialize("tables/" + strTableName + "/" + e + ".class");
            hmtup.addAll(page.eqsearch(col, val, false, 201));
        }
        return hmtup;
    }

    public HashSet<Tuple> noteqsearch(String col, Object val)
            throws ClassNotFoundException, IOException, DBAppException {
        HashSet<Tuple> hmtup = new HashSet<>();
        for (String e : vecPages) {
            Page page = Page.deserialize("tables/" + strTableName + "/" + e + ".class");
            hmtup.addAll(page.noteqsearch(col, val));
        }
        return hmtup;
    }

}