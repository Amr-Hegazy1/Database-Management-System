  import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Vector;


public class Page implements Serializable{

    private static Vector<Tuple> vecTuples;
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
     * This Java function searches for tuples by a given clustering key name and value using binary
     * search.
     * 
     * @param strClusteringKeyName The `strClusteringKeyName` parameter is the name of the clustering
     * key that you want to search for in the list of tuples. It is used to identify the specific
     * attribute or column in the tuple that serves as the clustering key for the data structure.
     * @param strClusteringKeyValue The `strClusteringKeyValue` parameter represents the value of the
     * clustering key that you are searching for within the list of tuples. The method
     * `searchTuplesByClusteringKey` is designed to search for a specific tuple within the list of
     * tuples based on the provided clustering key name and value.
     * @return The method is returning the index of the tuple in the `vecTuples` list that matches the
     * provided clustering key name and value. If a match is found, the method returns the index of
     * that tuple. If no match is found after the binary search, it throws a `DBAppException` with the
     * message "Column Value doesn't exist".
     */
    public int findindByClusteringKey(String strClusteringKeyName, Object objClusteringKeyValue) throws DBAppException{

        int n = vecTuples.size();

        int left = 0, right = n - 1;

        while(left <= right){
            int mid = left + (right - left) / 2;

            

            String strMidClusteringKeyValue = vecTuples.get(mid).getColumnValue(strClusteringKeyName).toString();

            System.out.println(mid + " " + strMidClusteringKeyValue + " " + objClusteringKeyValue);

            // convert the clustering key value to appropriate type for valid comparison
			// for example, if the clustering key is of type integer, then convert the string value to integer
			// if the clustering key is of type double, then convert the string value to double
			// if the clustering key is of type string, then no conversion is needed

            // TODO: REFACTOR THIS CODE

            if(objClusteringKeyValue instanceof Integer){
                int intMidClusteringKeyValue = Integer.parseInt(strMidClusteringKeyValue);
                int intClusteringKeyValue = (int) objClusteringKeyValue;

                if(intMidClusteringKeyValue == intClusteringKeyValue){
                    return mid;
                }else if(intMidClusteringKeyValue < intClusteringKeyValue){
                    left = mid + 1;
                }else{
                    right = mid - 1;
                }
            }else if(objClusteringKeyValue instanceof Double){
                double dblMidClusteringKeyValue = Double.parseDouble(strMidClusteringKeyValue);
                double dblClusteringKeyValue = (double) objClusteringKeyValue;

                if(dblMidClusteringKeyValue == dblClusteringKeyValue){
                    return mid;
                }else if(dblMidClusteringKeyValue < dblClusteringKeyValue){
                    left = mid + 1;
                }else{
                    right = mid - 1;
                }
            }else{
                String strClusteringKeyValue = (String) objClusteringKeyValue;

                if(strMidClusteringKeyValue.equals(strClusteringKeyValue)){
                    return mid;
                }else if(strMidClusteringKeyValue.compareTo(strClusteringKeyValue) < 0){
                    left = mid + 1;
                }else{
                    right = mid - 1;
                }
            }
            

            

        }

       return right+1;

    }
    public HashSet<Tuple> allTup(){
        HashSet<Tuple> hstups= new HashSet<>();
        for(Tuple tu: vecTuples){
                hstups.add(tu);
        }
        return hstups;
    }
    public Object Min(String col){
        return vecTuples.get(0).getColumnValue(col);
    }


    public Object Max(String col){
        return vecTuples.get(vecTuples.size()-1).getColumnValue(col);
    }
    public  HashSet<Tuple> eqsearch(String col, Object val, boolean isclu, int index){
        HashSet<Tuple> hstups= new HashSet<>();
        if(isclu){
            
                if(val instanceof Integer){
                    Integer te = (Integer) val;
                    if(((Integer)vecTuples.get(index).getColumnValue(col))==(te)){
                        hstups.add(vecTuples.get(index));
                    }
                }
                else if(val instanceof Double){
                    Double te = (Double) val;
                    if(((Double)vecTuples.get(index).getColumnValue(col))==(te)){
                        hstups.add(vecTuples.get(index));
                    }
                }
                else{
                    String te = (String) val;
                    if(((String)vecTuples.get(index).getColumnValue(col)).compareTo(te)==0){
                        hstups.add(vecTuples.get(index));
                    }
                }
            
            
        }
        else{
            for(Tuple tu: vecTuples){
                if(val instanceof Integer){
                    Integer te = (Integer) val;
                    if(((Integer)tu.getColumnValue(col))==(te)){
                        hstups.add(tu);
                    }
                }
                else if(val instanceof Double){
                    Double te = (Double) val;
                    if(((Double)tu.getColumnValue(col))==(te)){
                        hstups.add(tu);
                    }
                }
                else{
                    String te = (String) val;
                    if(((String)tu.getColumnValue(col)).compareTo(te)==0){
                        hstups.add(tu);
                    }
                }
            
            }
        }
        return hstups;
    }  

    public HashSet<Tuple> gtrsearch(String col, Object val, boolean isclu, int index){
        HashSet<Tuple> hstups= new HashSet<>();
        if(isclu){
            for(int i=index; i<vecTuples.size();i++){
                if(val instanceof Integer){
                    Integer te = (Integer) val;
                    if(((Integer)vecTuples.get(i).getColumnValue(col))>(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else if(val instanceof Double){
                    Double te = (Double) val;
                    if(((Double)vecTuples.get(i).getColumnValue(col))>(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else{
                    String te = (String) val;
                    if(((String)vecTuples.get(i).getColumnValue(col)).compareTo(te)>0){
                        hstups.add(vecTuples.get(i));
                    }
                }
            
            }
        }
        else{
            for(Tuple tu: vecTuples){
                if(val instanceof Integer){
                    Integer te = (Integer) val;
                    if(((Integer)tu.getColumnValue(col))>(te)){
                        hstups.add(tu);
                    }
                }
                else if(val instanceof Double){
                    Double te = (Double) val;
                    if(((Double)tu.getColumnValue(col))>(te)){
                        hstups.add(tu);
                    }
                }
                else{
                    String te = (String) val;
                    if(((String)tu.getColumnValue(col)).compareTo(te)>0){
                        hstups.add(tu);
                    }
                }
            
            }
        }
        return hstups;
    }
    public HashSet<Tuple> gtreqsearch(String col, Object val, boolean isclu, int index){
        HashSet<Tuple> hstups= new HashSet<>();
        if(isclu){
            for(int i=index; i<vecTuples.size();i++){
                if(val instanceof Integer){
                    Integer te = (Integer) val;
                    if(((Integer)vecTuples.get(i).getColumnValue(col))>=(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else if(val instanceof Double){
                    Double te = (Double) val;
                    if(((Double)vecTuples.get(i).getColumnValue(col))>=(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else{
                    String te = (String) val;
                    if(((String)vecTuples.get(i).getColumnValue(col)).compareTo(te)>=0){
                        hstups.add(vecTuples.get(i));
                    }
                }
            
            }
        }
        else {for(Tuple tu: vecTuples){
            if(val instanceof Integer){
                Integer te = (Integer) val;
                if(((Integer)tu.getColumnValue(col))>=(te)){
                    hstups.add(tu);
                }
            }
            else if(val instanceof Double){
                Double te = (Double) val;
                if(((Double)tu.getColumnValue(col))>=(te)){
                    hstups.add(tu);
                }
            }
            else{
                String te = (String) val;
                if(((String)tu.getColumnValue(col)).compareTo(te)>=0){
                    hstups.add(tu);
                }
            }
            
        }
    }
        return hstups;
    }

    public HashSet<Tuple> lessearch(String col, Object val, boolean isclu, int index){
        HashSet<Tuple> hstups= new HashSet<>();
        if(isclu){
            for(int i=0; i<=index;i++){
                if(val instanceof Integer){
                    Integer te = (Integer) val;
                    if(((Integer)vecTuples.get(i).getColumnValue(col))<(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else if(val instanceof Double){
                    Double te = (Double) val;
                    if(((Double)vecTuples.get(i).getColumnValue(col))<(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else{
                    String te = (String) val;
                    if(((String)vecTuples.get(i).getColumnValue(col)).compareTo(te)<0){
                        hstups.add(vecTuples.get(i));
                    }
                }
            
            }
        }
        else {for(Tuple tu: vecTuples){
            if(val instanceof Integer){
                Integer te = (Integer) val;
                if(((Integer)tu.getColumnValue(col))<(te)){
                    hstups.add(tu);
                }
            }
            else if(val instanceof Double){
                Double te = (Double) val;
                if(((Double)tu.getColumnValue(col))<(te)){
                    hstups.add(tu);
                }
            }
            else{
                String te = (String) val;
                if(((String)tu.getColumnValue(col)).compareTo(te)<0){
                    hstups.add(tu);
                }
            }
            
        }
    }
        return hstups;
    }

    public HashSet<Tuple> leseqsearch(String col, Object val, boolean isclu, int index){
        HashSet<Tuple> hstups= new HashSet<>();
        if(isclu){
            for(int i=0; i<=index;i++){
                if(val instanceof Integer){
                    Integer te = (Integer) val;
                    if(((Integer)vecTuples.get(i).getColumnValue(col))<=(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else if(val instanceof Double){
                    Double te = (Double) val;
                    if(((Double)vecTuples.get(i).getColumnValue(col))<(te)){
                        hstups.add(vecTuples.get(i));
                    }
                }
                else{
                    String te = (String) val;
                    if(((String)vecTuples.get(i).getColumnValue(col)).compareTo(te)<0){
                        hstups.add(vecTuples.get(i));
                    }
                }
            
            }
        }
        else{
        for(Tuple tu: vecTuples){
            if(val instanceof Integer){
                Integer te = (Integer) val;
                if(((Integer)tu.getColumnValue(col))<=(te)){
                    hstups.add(tu);
                }
            }
            else if(val instanceof Double){
                Double te = (Double) val;
                if(((Double)tu.getColumnValue(col))<=(te)){
                    hstups.add(tu);
                }
            }
            else{
                String te = (String) val;
                if(((String)tu.getColumnValue(col)).compareTo(te)<=0){
                    hstups.add(tu);
                }
            }
            
        }
    }
        return hstups;
    }
    public  HashSet<Tuple> noteqsearch(String col, Object val){
        HashSet<Tuple> hstups= new HashSet<>();
        for(Tuple tu: vecTuples){
            if(val instanceof Integer){
                Integer te = (Integer) val;
                if(((Integer)tu.getColumnValue(col))!=(te)){
                    hstups.add(tu);
                }
            }
            else if(val instanceof Double){
                Double te = (Double) val;
                if(((Double)tu.getColumnValue(col))!=(te)){
                    hstups.add(tu);
                }
            }
            else{
                String te = (String) val;
                if(((String)tu.getColumnValue(col)).compareTo(te)!=0){
                    hstups.add(tu);
                }
            }
            
        }
        return hstups;
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
