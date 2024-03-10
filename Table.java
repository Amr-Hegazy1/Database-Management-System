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

    public Table(String strTableName) {
        this.strTableName = strTableName;
        this.vecPages = new Vector<String>();
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
    public void serialize(String strFileName) throws IOException {

        // TODO: Exception Handling

        FileOutputStream fos = new FileOutputStream(strFileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);

        oos.close();
        fos.close();

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
    public static Table deserialize(String strFileName) throws IOException, ClassNotFoundException {

        // TODO: Exception Handling

        FileInputStream fis = new FileInputStream(strFileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Table table = (Table) ois.readObject();

        ois.close();
        fis.close();

        return table;

    }

}
