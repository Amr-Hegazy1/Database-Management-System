import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Table implements Serializable{
    private String strTableName;

    private Vector<String> vecPages;

    public Table(String strTableName){
        this.strTableName = strTableName;
        this.vecPages = new Vector<String>();
    }

    /**
     * This Java function returns the number of pages in a vector.
     * 
     * @return The method `getNumberOfPages` returns the number of elements in the `vecPages` vector.
     */
    public int getNumberOfPages(){
        return vecPages.size();
    }

    /**
     * The `serialize` method writes the current object to a file using Java serialization.
     * 
     * @param strFileName The `strFileName` parameter in the `serialize` method is a `String` that
     * represents the name of the file to which the object will be serialized. This parameter specifies
     * the file path where the object will be written in serialized form.
     */
    public void serialize(String strFileName) throws IOException{
        
        // TODO: Exception Handling

        strTableName = strFileName;
        FileOutputStream fos= new FileOutputStream(strFileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this);


        oos.close();
        fos.close();
        
        
    }


    /**
     * The function `deserialize` reads a serialized `Table` object from a file and returns it.
     * 
     * @param strFileName The `strFileName` parameter in the `deserialize` method is a `String` that
     * represents the file name of the file from which the `Table` object will be deserialized.
     * @return The `deserialize` method is returning an object of type `Table`.
     */
    public static Table deserialize(String strFileName) throws IOException, ClassNotFoundException{
        
        // TODO: Exception Handling
        
        FileInputStream fis=new FileInputStream(strFileName);
        ObjectInputStream ois= new ObjectInputStream(fis);
        Table table = (Table) ois.readObject();

        ois.close();
        fis.close();
        
        return table;

    }
    //get all the pages used to store the table's data
    public Vector<String> getPages (){
        return this.vecPages;
    }

    /**
     * The `addPage` function in Java adds a page name to a vector.
     * 
     * @param strPageName The `strPageName` parameter is a `String` representing the name of the page
     * that you want to add to the `vecPages` vector.
     */
    public void addPage(String strPageName){
        vecPages.add(strPageName);
    }

    
    /**
     * The function `getPageAtIndex` returns the page at the specified index from a vector of pages.
     * 
     * @param i The parameter `i` in the `getPageAtIndex` method represents the index of the page you
     * want to retrieve from the `vecPages` vector.
     * @return The method `getPageAtIndex` is returning the page at the specified index `i` from the
     * `vecPages` vector.
     */
    public String getPageAtIndex(int i){
        return vecPages.get(i);
    }

    /**
     * The `removePage` function removes a page with the specified name from a vector of pages.
     * 
     * @param strPageName The parameter `strPageName` is a String representing the name of the page
     * that you want to remove from the `vecPages` vector.
     */
    public void removePage(String strPageName){
        vecPages.remove(strPageName);
    }

    /**
     * The function `printAllPages` iterates through all pages in a table, deserializes each page, and
     * prints it to the console.
     * 
     */
    public void printAllPages() throws IOException, ClassNotFoundException{
        for(int i = 0; i < this.getNumberOfPages(); i++){

            
            String strPage = this.getPageAtIndex(i);
            Page page = Page.deserialize(strPage + ".class");
            System.out.println("#################################### PAGE " + i + " ########################################");
            System.out.println(page);
        }
    }

    /**
     * The function getTableName() returns the name of the table as a String.
     * 
     * @return The method getTableName() returns the value of the variable strTableName, which is the
     * name of the table.
     */
    public String getTableName(){
        return strTableName;
    }
    

}
