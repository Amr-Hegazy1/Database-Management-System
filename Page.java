import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.Vector;


public class Page implements Serializable{

    private Vector<Tuple> vecTuples;
    public static int intPageSize;
    



    // The `public Page()` constructor in the `Page` class is initializing a new instance of the `Page`
    // class. Inside the constructor, it initializes the `vecTuples` member variable as a new `Vector`
    // object that can hold elements of type `Tuple`. This means that when a new `Page` object is
    // created, it will have an empty vector `vecTuples` ready to store `Tuple` objects.
    public Page(){

        


        

        vecTuples = new Vector<Tuple>();
    }

    /**
     * The addTuple function adds a Tuple object to a list of tuples.
     * 
     * @param tupleTuple The parameter `tupleTuple` in the `addTuple` method is of type `Tuple`, which
     * is the type of object that you are adding to the `vecTuples` collection.
     */
    public void addTuple(Tuple tupleTuple){
        vecTuples.add(tupleTuple);
        
    }

    


    /**
     * The `toString` function iterates through a list of `Tuple` objects and concatenates their string
     * representations with a newline character.
     * 
     * @return The `toString` method is returning a string representation of the `vecTuples` list. It
     * concatenates each `Tuple` object in the list with a newline character and returns the resulting
     * string.
     */
    public String toString(){
        String res = "";

        for(Tuple tupleTuple : vecTuples){
            res += tupleTuple + "\n";
        }

        return res;
    }

    

    /**
     * The `serialize` method writes the current object to a file using Java serialization.
     * 
     * @param strFileName The `strFileName` parameter in the `serialize` method is a `String` that
     * represents the name of the file to which the object will be serialized. This parameter specifies
     * the file path where the serialized object will be saved.
     */
    public void serialize(String strFileName) throws IOException{
        
        // TODO: Exception Handling


        FileOutputStream fos= new FileOutputStream(strFileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this);


        oos.close();
        fos.close();
        
        
    }



    /**
     * The `deserialize` function reads a serialized `Page` object from a file and returns it.
     * 
     * @param strFileName The `strFileName` parameter in the `deserialize` method is a `String` that
     * represents the file name of the file from which the `Page` object will be deserialized.
     * @return The `deserialize` method is returning an object of type `Page` that has been
     * deserialized from the file specified by the `strFileName` parameter.
     */
    public static Page deserialize(String strFileName) throws IOException, ClassNotFoundException{
        
        // TODO: Exception Handling
        
        FileInputStream fis=new FileInputStream(strFileName);
        ObjectInputStream ois= new ObjectInputStream(fis);
        Page page = (Page) ois.readObject();

        ois.close();
        fis.close();
       
        return page;

    }



    
    
}
