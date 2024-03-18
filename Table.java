import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Table implements Serializable{
    private String strTableName;

    private static Vector<String> vecPages;

    public Table(String strTableName){
        this.strTableName = strTableName;
        this.vecPages = new Vector<String>();
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


        FileOutputStream fos= new FileOutputStream(strFileName);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this);


        oos.close();
        fos.close();
        
        
    }
    // greater than
    public HashSet<Tuple> greaterthan(String col, Object val) throws ClassNotFoundException, IOException, DBAppException{
        
        HashSet<Tuple> hmtup = new HashSet<>();   
        boolean noneed = false;
        for(int i=0;i<vecPages.size();i++){
            Page page1= Page.deserialize(vecPages.get(i));
            if(val instanceof Integer){
                Integer temp = (Integer)val;
                if(noneed || ((Integer)page1.Min(col))< temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Integer)page1.Min(col)<= temp && ((Integer)page1.Max(col))>temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.gtrsearch(col, val, true, index));
                    noneed=true;
                }
            }
            else if(val instanceof Double){
                Double temp = (Double)val;
                if(noneed || ((Double)page1.Min(col))<temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Double)page1.Min(col)<=temp && ((Double)page1.Max(col))>temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.gtrsearch(col, val, true, index));
                    noneed=true;
                }            
            }
            else{
                String temp = (String)val;
                if(noneed || ((String)page1.Min(col)).compareTo(temp)<0 ){
                    hmtup.addAll(page1.allTup());
                }
                else if(((String)page1.Min(col)).compareTo(temp)<=0&& ((String)page1.Max(col)).compareTo(temp)>0){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.gtrsearch(col, val, true, index));
                    noneed=true;
                }     
            }
        }  
        return hmtup;
    }
    public HashSet<Tuple> greaterthannc(String col, Object val) throws ClassNotFoundException, IOException{
        HashSet<Tuple> hmtup = new HashSet<>();
        for(int i=0;i<vecPages.size();i++){
            Page page1= Page.deserialize(vecPages.elementAt(i));
            hmtup.addAll(page1.gtrsearch(col, val, false, 201));
        }
        return hmtup;
    }


    // greater than equal
    public HashSet<Tuple> greaterthaneq(String col, Object val) throws ClassNotFoundException, IOException, DBAppException{
        HashSet<Tuple> hmtup = new HashSet<>();   
        boolean noneed = false;
        for(int i=0;i<vecPages.size();i++){
            Page page1= Page.deserialize(vecPages.get(i));
            if(val instanceof Integer){
                Integer temp = (Integer)val;
                if(noneed || ((Integer)page1.Min(col))<=temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Integer)page1.Min(col)<= temp && ((Integer)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.gtreqsearch(col, val, true, index));
                    noneed=true;
                }
            }
            else if(val instanceof Double){
                Double temp = (Double)val;
                if(noneed || ((Double)page1.Min(col))<=temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Double)page1.Min(col)<=temp && ((Double)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.gtreqsearch(col, val, true, index));
                    noneed=true;
                }            
            }
            else{
                String temp = (String)val;
                if(noneed || ((String)page1.Min(col)).compareTo(temp)<=0 ){
                    hmtup.addAll(page1.allTup());
                }
                else if(((String)page1.Min(col)).compareTo(temp)<=0&& ((String)page1.Max(col)).compareTo(temp)>=0){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.gtreqsearch(col, val, true, index));
                    noneed=true;
                }     
            }
        }  
        return hmtup;
    }
    public HashSet<Tuple> greaterthaneqnc(String col, Object val) throws ClassNotFoundException, IOException{
        HashSet<Tuple> hmtup = new HashSet<>();
        for(int i=0;i<vecPages.size();i++){
            Page page1= Page.deserialize(vecPages.elementAt(i));
            hmtup.addAll(page1.gtreqsearch(col, val, false, 201));
        }
        return hmtup;
    }


    public HashSet<Tuple> lesserthan(String col, Object val) throws ClassNotFoundException, IOException, DBAppException{
        HashSet<Tuple> hmtup = new HashSet<>();   
        boolean noneed = false;
        for(int i=vecPages.size()-1;i>=0;i--){
            Page page1= Page.deserialize(vecPages.get(i));
            if(val instanceof Integer){
                Integer temp = (Integer)val;
                if(noneed || ((Integer)page1.Max(col))<temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Integer)page1.Min(col)<temp && ((Integer)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed=true;
                }
            }
            else if(val instanceof Double){
                Double temp = (Double)val;
                if(noneed || ((Double)page1.Max(col))<temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Double)page1.Min(col)<temp && ((Double)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed=true;
                }            
            }
            else{
                String temp = (String)val;
                if(noneed || ((String)page1.Max(col)).compareTo(temp)<0 ){
                    hmtup.addAll(page1.allTup());
                }
                else if(((String)page1.Min(col)).compareTo(temp)<0&& ((String)page1.Max(col)).compareTo(temp)>=0){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed=true;
                }     
            }
        }
        return hmtup;
    }
    
    public HashSet<Tuple> lesserthannc(String col, Object val) throws ClassNotFoundException, IOException{
        HashSet<Tuple> hmtup = new HashSet<>();
        for(int i=0;i<vecPages.size();i++){  
            Page page1= Page.deserialize(vecPages.elementAt(i));
            hmtup.addAll(page1.lessearch(col, val, false, 201));
        }
        return hmtup;
    }

    public HashSet<Tuple> lesserthaneq(String col, Object val) throws ClassNotFoundException, IOException, DBAppException{
        HashSet<Tuple> hmtup = new HashSet<>();   
        boolean noneed = false;
        for(int i=vecPages.size()-1;i>=0;i--){
            Page page1= Page.deserialize(vecPages.get(i));
            if(val instanceof Integer){
                Integer temp = (Integer)val;
                if(noneed || ((Integer)page1.Max(col))<=temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Integer)page1.Min(col)<=temp && ((Integer)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.leseqsearch(col, val, true, index));
                    noneed=true;
                }
            } 
            else if(val instanceof Double){
                Double temp = (Double)val;
                if(noneed || ((Double)page1.Max(col))<=temp ){
                    hmtup.addAll(page1.allTup());
                }
                else if((Double)page1.Min(col)<=temp && ((Double)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.leseqsearch(col, val, true, index));
                    noneed=true;
                }            
            }
            else{
                String temp = (String)val;
                if(noneed || ((String)page1.Max(col)).compareTo(temp)<=0 ){
                    hmtup.addAll(page1.allTup());
                }
                else if(((String)page1.Min(col)).compareTo(temp)<=0&& ((String)page1.Max(col)).compareTo(temp)>=0){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.lessearch(col, val, true, index));
                    noneed=true;
                }     
            }
        }
        return hmtup;
    }
    
    public HashSet<Tuple> lesserthannceq(String col, Object val) throws ClassNotFoundException, IOException{
        HashSet<Tuple> hmtup = new HashSet<>();
        for(int i=0;i<vecPages.size();i++){
            Page page1= Page.deserialize(vecPages.elementAt(i));
            hmtup.addAll(page1.leseqsearch(col, val, false, 201));
        }
        return hmtup;
    }
    public HashSet<Tuple> cleqsearch(String col, Object val) throws ClassNotFoundException, IOException, DBAppException{
        HashSet<Tuple> hmtup = new HashSet<>();   
        for(int i=0;i<vecPages.size();i++){
            Page page1= Page.deserialize(vecPages.get(i));
            if(val instanceof Integer){
                Integer temp = (Integer)val;
        
                 if((Integer)page1.Min(col)<=temp && ((Integer)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.eqsearch(col, val, true, index));
                    break;
                }
            }
            else if(val instanceof Double){
                Double temp = (Double)val;
                if((Double)page1.Min(col)<=temp && ((Double)page1.Max(col))>=temp){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.eqsearch(col, val, true, index));
                    break;
                }            
            }
            else{
                String temp = (String)val;
                if(((String)page1.Min(col)).compareTo(temp)<=0&& ((String)page1.Max(col)).compareTo(temp)>=0){
                    int index = page1.findindByClusteringKey(col, val);
                    hmtup.addAll(page1.eqsearch(col, val, true, index));
                    break;
                }     
            }
        }
        return hmtup;
    }
    public HashSet<Tuple> eqsearch(String col, Object val) throws ClassNotFoundException, IOException{
        HashSet<Tuple> hmtup = new HashSet<>();
        for(String e: vecPages){
            Page page= Page.deserialize(e);
            hmtup.addAll(page.eqsearch(col, val, false, 201));
        }
        return hmtup;
    }

    public HashSet<Tuple> noteqsearch(String col, Object val) throws ClassNotFoundException, IOException{
        HashSet<Tuple> hmtup = new HashSet<>();
        for(String e: vecPages){
            Page page= Page.deserialize(e);
            hmtup.addAll(page.noteqsearch(col, val));
        }
        return hmtup;
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
    

    

}
