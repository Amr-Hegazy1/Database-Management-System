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
    



    public Page(){

        


        

        vecTuples = new Vector<Tuple>();
    }

    public void addTuple(Tuple tupleTuple){
        vecTuples.add(tupleTuple);
        
    }

    


    public String toString(){
        String res = "";

        for(Tuple tupleTuple : vecTuples){
            res += tupleTuple + "\n";
        }

        return res;
    }

    

    public void serialize(String strFileName) throws IOException{
        
        // TODO: Exception Handling


        FileOutputStream fos= new FileOutputStream(strFileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this);


        oos.close();
        fos.close();
        
        
    }


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
