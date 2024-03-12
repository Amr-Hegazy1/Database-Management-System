import java.io.File;
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
    private String strPageName;
    



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
     * The getSize() function returns the size of a vector of tuples.
     * 
     * @return The `getSize` method is returning the size of the `vecTuples` collection.
     */
    public int getSize(){
        return vecTuples.size();
    }

   
    /**
     * This Java function retrieves a Tuple object from a list based on a specified index, throwing an
     * exception if the index is out of bounds.
     * 
     * @param i The parameter `i` represents the index of the tuple that you want to retrieve from the
     * `vecTuples` list. If `i` is within the valid range of indices in the list, the method will
     * return the tuple at that index. Otherwise, it will throw a `DBAppException`
     * @return A Tuple object is being returned.
     */
    public Tuple getTupleWithIndex(int i) throws DBAppException{

        if(i >= vecTuples.size()) 
            throw new DBAppException("Attempting to access a wrong tuple index");

        return vecTuples.get(i);
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
    public int searchTuplesByClusteringKey(String strClusteringKeyName, Object objClusteringKeyValue) throws DBAppException{

        int n = vecTuples.size();

        int left = 0, right = n - 1;

        while(left <= right){
            int mid = left + (right - left) / 2;

            

            String strMidClusteringKeyValue = vecTuples.get(mid).getColumnValue(strClusteringKeyName).toString();

            

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

        throw new DBAppException("Column Value doesn't exist");

    }

    public void deleteTupleWithIndex(int i) throws DBAppException{
        if(i >= vecTuples.size()) 
            throw new DBAppException("Attempting to access a wrong tuple index");
            
        vecTuples.remove(i);
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
     * The function `getPageName` returns the value of the `strPageName` variable as a String.
     * 
     * @return The method `getPageName()` is returning the value of the variable `strPageName`, which
     * presumably holds the name of a page.
     */
    public String getPageName(){
        return strPageName;
    }

    /**
     * The `deletePage` function deletes a file with the given name in Java and throws an IOException
     */
    public void deletePage() throws IOException{
        
        

        File filePage = new File(strPageName);

        if(filePage.delete()){
            System.out.println(filePage.getName() + " is deleted!");
        }else{
            System.out.println("Delete operation is failed.");
        }
        
        
    

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

        strPageName = strFileName;
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
    //get all tuples in the page
    public Vector<Tuple> getTuples(){
        return this.vecTuples;
    }

    /**
     * The `deleteTuple` function removes a specified tuple from a vector of tuples.
     * 
     * @param tuple The `deleteTuple` method takes a `Tuple` object as a parameter. This `Tuple` object
     * is used to identify and remove a specific tuple from the `vecTuples` collection.
     */
    public void deleteTuple(Tuple tuple) {
        // TODO: make vecTuple.remove() work
        
        for(int i = 0; i < vecTuples.size(); i++){
            if(vecTuples.get(i).equals(tuple)){
                
                vecTuples.remove(i);
                return;
            }
        }
    }



    public static void main(String[] args) {
        // create 5 page objects with 5 tuples and serialize them

        int intPageSize = 200;
        
        for(int i = 0; i < 5; i++){
            Page page = new Page();
            for(int j = 0; j < intPageSize; j++){
                Tuple tuple = new Tuple();
                for(int k = 0; k < 5; k++){
                    tuple.setColumnValue("ID", i * intPageSize + j);
                    tuple.setColumnValue("Name", "Ahmed" + i + j);
                    tuple.setColumnValue("Age", 20);

                }
                page.addTuple(tuple);
            }
            try {
                page.serialize("CityShop_" + i + ".class");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	public Vector<Tuple> getVecTuples() {
		// TODO Auto-generated method stub
		return vecTuples;
	}

    


    
    
}