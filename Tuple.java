import java.util.*;


public class Tuple {
    
    private HashMap<String, Object> hmTuple;

    public Tuple(){
        hmTuple = new HashMap<String, Object>();
    }


    /*
     * method for getting 
     */
    // TODO: What if column does not exist?

    public Object getColumnValue(String strColumnName){
        return hmTuple.get(strColumnName);
    }

    public void setColumnValue(String strColumnName, Object objColumnValue){
        hmTuple.put(strColumnName, objColumnValue);
    }


    // TODO: remove last comma
    public String toString(){

        String res = "";

        for(String column : hmTuple.keySet()){

            res += hmTuple.get(column) + ",";

        }

        return res;

    }

}
