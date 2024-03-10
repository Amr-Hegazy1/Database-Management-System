import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class Metadata {


    // The `htblMetadata` variable is a Hashtable that stores the metadata for the database. The keys
    // of the `htblMetadata` table are the names of the tables in the database, and the values are
    // Hashtables containing the metadata for each table. The keys of the inner Hashtables are the
    // column names, and the values are Hashtables containing the column type, clustering key, index
    // name, and index type for each column.
    private Hashtable<String, Hashtable<String, Hashtable<String, String>>> htblMetadata;
    
    // The `public Metadata() throws FileNotFoundException, IOException{}` constructor in the
    // `Metadata` class is responsible for initializing a new instance of the `Metadata` class. Here's
    // a breakdown of what it does:
    // 1. It creates a new file called `metadata.csv` if it does not already exist.
    // 2. It initializes a new `Hashtable` called `htblMetadata` to store the metadata.
    // 3. It calls the `readFile` method to read the metadata from the `metadata.csv` file and populate
    // the `htblMetadata` table with the extracted information.
    public Metadata() throws FileNotFoundException, IOException{


        // Create metadata file if it does not exist

        File fileMetadataFile = new File("metadata.csv");
        if(!fileMetadataFile.exists())
            fileMetadataFile.createNewFile(); 


        htblMetadata  = new Hashtable<>();

        readFile();
    }

    /**
     * The `readFile` function reads data from a CSV file and populates a metadata table with the
     * extracted information.
     */
    private void readFile() throws FileNotFoundException{
        
        try (Scanner scanner = new Scanner(new File("metadata.csv"))) {
            while (scanner.hasNextLine()) {
                String[] arrstrRecord = scanner.nextLine().split(",", 2);
                String strTableName = arrstrRecord[0].replaceAll("\\s+","");
                if(!htblMetadata.containsKey(strTableName)){
                    htblMetadata.put(strTableName, getRecordFromLine(arrstrRecord[1]));
                }
                else{
                    htblMetadata.get(strTableName).putAll(getRecordFromLine(arrstrRecord[1]));
                }
                
            }
        }

        
    }

    /**
     * The function `getRecordFromLine` parses a line of input data and stores the values in a
     * Hashtable with specific keys.
     * 
     * @param line A string representing a line of data that needs to be parsed and stored in a
     * Hashtable.
     * @return The method `getRecordFromLine` is returning a Hashtable containing key-value pairs for
     * the column type, clustering key, index name, and index type extracted from the input line.
     */
    private Hashtable<String, Hashtable<String, String>> getRecordFromLine(String line) throws IndexOutOfBoundsException {
        Hashtable<String, String> htblValues = new Hashtable<>();
        
        String[] arrstrRecord = line.split(",");

        
        String strColumnName = arrstrRecord[0].replaceAll("\\s+","");
        String strColumnType = arrstrRecord[1].replaceAll("\\s+","");
        String strClusteringKey = arrstrRecord[2].replaceAll("\\s+","");
        String strIndexName = arrstrRecord[3].replaceAll("\\s+","");
        String strIndexType = arrstrRecord[4].replaceAll("\\s+","");

        
        
        htblValues.put("Column Type", strColumnType);
        htblValues.put("ClusteringKey", strClusteringKey);
        htblValues.put("IndexName", strIndexName);
        htblValues.put("IndexType", strIndexType);

        Hashtable<String, Hashtable<String, String>> htblColumnDetails = new Hashtable<>();

        htblColumnDetails.put(strColumnName, htblValues);

        return htblColumnDetails;

        
        
    }

    /**
     * The function `getColumnType` retrieves the column type of a specified column in a given table
     * from a metadata hash table.
     * 
     * @param strTableName The `strTableName` parameter represents the name of the table for which you
     * want to retrieve the column type.
     * @param strColumnName The `strColumnName` parameter represents the name of the column for which
     * you want to retrieve the column type from the metadata.
     * @return The method `getColumnType` returns the column type of the specified column
     * (`strColumnName`) in the specified table (`strTableName`). It retrieves this information from
     * the `htblMetadata` map using the key formed by concatenating the table name and column name.
     */
    public String getColumnType(String strTableName, String strColumnName){
        return htblMetadata.get(strTableName).get(strColumnName).get("Column Type");
    }


    /**
     * The function `isColumnIndexed` checks if a specified column in a table has an index associated
     * with it.
     * 
     * @param strTableName The `strTableName` parameter represents the name of the table for which you
     * want to check if a specific column is indexed.
     * @param strColumnName The `strColumnName` parameter in the `isColumnIndexed` method refers to the
     * name of the column for which you want to check if an index is present in the metadata of a
     * specified table.
     * @return The method `isColumnIndexed` is returning a boolean value indicating whether the
     * specified column in the given table is indexed or not. It checks if the "IndexName" attribute in
     * the metadata for the specified table and column is not empty, and returns true if it is indexed,
     * and false if it is not indexed.
     */
    public boolean isColumnIndexed(String strTableName, String strColumnName) throws DBAppException{
        if(!htblMetadata.containsKey(strTableName))
            throw new DBAppException("Table does not exist");
        if(!htblMetadata.get(strTableName).containsKey(strColumnName))
            throw new DBAppException("Column does not exist");
        
        return !htblMetadata.get(strTableName).get(strColumnName).get("IndexName").equals("N/A");
    }

    /**
     * This Java function retrieves the clustering key for a given table from metadata stored in a
     * Hashtable.
     * 
     * @param strTableName The `strTableName` parameter is a `String` representing the name of the
     * table for which you want to retrieve the clustering key.
     * @return The `getClusteringKey` method is returning the name of the column that is designated as
     * the clustering key for the specified table `strTableName`. If the table exists in the metadata
     * and has a clustering key defined, the method will return the name of that column. If the table
     * does not have a clustering key defined, it will throw a `DBAppException` with the message "Table
     * doesn't have a clustering key"
     */
    public String getClusteringKey(String strTableName) throws DBAppException{
        
        if(!htblMetadata.containsKey(strTableName))
            throw new DBAppException("Table does not exist");
        

        Hashtable<String, Hashtable<String, String>> htblColNames = htblMetadata.get(strTableName);

        for(String col : htblColNames.keySet()){
            if(isClusteringKey(strTableName, col)) return col;
        }

        throw new DBAppException("Table doesn't have a clustering key");
    }

    /**
     * The function `getClusteringKey` retrieves the clustering key for a specified table and column
     * from a metadata hash table.
     * 
     * @param strTableName The `strTableName` parameter represents the name of the table for which you
     * want to retrieve the clustering key.
     * @param strColumnName The `strColumnName` parameter is a String representing the name of a column
     * in a table.
     * @return The method `getClusteringKey` returns the clustering key for a given table name and
     * column name. It retrieves this information from the metadata stored in the `htblMetadata` map
     * using the concatenated key of `strTableName` and `strColumnName`.
     */
    public Boolean isClusteringKey(String strTableName, String strColumnName){
        return Boolean.parseBoolean(htblMetadata.get(strTableName).get(strColumnName).get("ClusteringKey"));
    }

    /**
     * The function `getIndexName` retrieves the index name associated with a given table name and
     * column name from a metadata hash table.
     * 
     * @param strTableName The `strTableName` parameter represents the name of a table in a database.
     * @param strColumnName The `strColumnName` parameter represents the name of the column for which
     * you want to retrieve the index name.
     * @return The method `getIndexName` returns the value of the "IndexName" attribute from the
     * metadata stored in the `htblMetadata` map for the given `strTableName` and `strColumnName`.
     */
    public String getIndexName(String strTableName, String strColumnName){
        return htblMetadata.get(strTableName).get(strColumnName).get("IndexName");
    }

    /**
     * This Java function retrieves the index type of a specified column in a given table from a
     * metadata hash table.
     * 
     * @param strTableName The `strTableName` parameter represents the name of the table for which you
     * want to retrieve the index type.
     * @param strColumnName The `strColumnName` parameter represents the name of the column for which
     * you want to retrieve the index type in the `htblMetadata` map.
     * @return The method `getIndexType` returns the index type of the specified column in the
     * specified table.
     */
    public String getIndexType(String strTableName, String strColumnName){
        return htblMetadata.get(strTableName).get(strColumnName).get("IndexType");
    }

   

    /**
     * The function `getTableNames` returns a list of table names from a metadata hash table.
     * 
     * @return A List of String objects containing the table names from the htblMetadata map.
     */
    public List<String> getTableNames(){
        return new ArrayList<>(htblMetadata.keySet());
    }

    /**
     * The function `checkTableName` checks if a given table name exists in a metadata hash table.
     * 
     * @param str The `checkTableName` method takes a String parameter `str`, which represents the
     * table name to be checked. The method checks if the `htblMetadata` map contains the provided
     * table name `str` as a key and returns a boolean value indicating whether the table name exists
     * in the metadata map or
     * @return The method `checkTableName` returns a boolean value indicating whether the
     * `htblMetadata` map contains the key specified by the input string `str`.
     */
    public boolean checkTableName(String str){
        return htblMetadata.containsKey(str);
    }


    /**
     * The function getColumnNames returns a list of column names for a given table name.
     * 
     * @param strTableName The `strTableName` parameter is a `String` representing the name of a table
     * in a database.
     * @return A List of String values representing the column names of the specified table name.
     */
    public List<String> getColumnNames(String strTableName){
        return new ArrayList<>(htblMetadata.get(strTableName).keySet());
    }


    /**
     * The function `checkColumnName` checks if a specified column exists in the metadata for a given
     * table.
     * 
     * @param strTableName The `strTableName` parameter represents the name of the table for which you
     * want to check the existence of a column.
     * @param strColumnName The `strColumnName` parameter is a `String` representing the name of a
     * column in a table.
     * @return The method `checkColumnName` returns a boolean value indicating whether the specified
     * column name exists in the metadata for the given table name.
     */
    public boolean checkColumnName(String strTableName, String strColumnName){
        return htblMetadata.get(strTableName).containsKey(strColumnName);
    } 


    /**
     * The `addTable` function adds a new table to the metadata with specified columns and clustering
     * key.
     * 
     * @param strTableName The `strTableName` parameter is a String that represents the name of the
     * table that you want to add to the metadata.
     * @param strClusteringKey The `strClusteringKey` parameter in the `addTable` method is used to
     * specify the column name that will be used as the clustering key for the table being added. The
     * clustering key determines the physical order of data in the table and is important for efficient
     * querying and retrieval of data.
     * @param htblColNameType The `htblColNameType` parameter is a Hashtable that stores the column
     * names as keys and their corresponding data types as values. It is used to define the columns and
     * their data types for a new table being added to the metadata.
     */
    public void addTable(String strTableName, String strClusteringKey, Hashtable<String, String> htblColNameType) throws DBAppException{
        if(htblMetadata.containsKey(strTableName)){
            throw new DBAppException("Table already exists");
        }
        else{
            htblMetadata.put(strTableName, new Hashtable<>());
        
            for (String strColumnName : htblColNameType.keySet()) {
                htblMetadata.get(strTableName).put(strColumnName, new Hashtable<>());
                htblMetadata.get(strTableName).get(strColumnName).put("Column Type", htblColNameType.get(strColumnName));
                htblMetadata.get(strTableName).get(strColumnName).put("ClusteringKey", strColumnName.equals(strClusteringKey) ? "true" : "false");
                htblMetadata.get(strTableName).get(strColumnName).put("IndexName", "N/A");
                htblMetadata.get(strTableName).get(strColumnName).put("IndexType", "N/A");
            }
        }
        
    }

    /**
     * The `deleteTable` function deletes a table from the metadata if it exists, otherwise it throws
     * an IOException indicating that the table does not exist.
     * 
     * @param strTableName The `strTableName` parameter is a `String` representing the name of the
     * table that you want to delete.
     */
    public void deleteTable(String strTableName) throws IOException{
        if(htblMetadata.containsKey(strTableName)){
            htblMetadata.remove(strTableName);
        }
        else{
            throw new IOException("Table does not exist");
        }
    }

    /**
     * The `addIndex` function adds an index to a specified column in a table if the table exists in
     * the metadata.
     * 
     * @param strTableName The `strTableName` parameter represents the name of the table to which you
     * want to add an index.
     * @param strColName The `strColName` parameter represents the name of the column for which you
     * want to add an index in the specified table.
     * @param strIndexType The `strIndexType` parameter in the `addIndex` method represents the type of
     * index that will be created for the specified column in the given table. This could be a B-tree
     * index, hash index, bitmap index, etc. The method uses this parameter to store the index type in
     * the metadata table
     * @param strIndexName The strIndexName parameter represents the name of the index which we will add
     */
    public void addIndex(String strTableName, String strColName, String strIndexType,String strIndexName) throws IOException{
        if(htblMetadata.containsKey(strTableName)){
            htblMetadata.get(strTableName).get(strColName).put("IndexName", strIndexName);
            htblMetadata.get(strTableName).get(strColName).put("IndexType", strIndexType);
        }
        else{
            throw new IOException("Table does not exist");//should be index exists
        }
    }

    /**
     * The `deleteIndex` function updates the index information for a specified column in a table or
     * throws an exception if the table does not exist.
     * 
     * @param strTableName The `strTableName` parameter represents the name of the table from which you
     * want to delete an index.
     * @param strColName The `strColName` parameter in the `deleteIndex` method represents the name of
     * the column for which you want to delete the index in the specified table (`strTableName`).
     */
    public void deleteIndex(String strTableName, String strColName) throws IOException{
        if(htblMetadata.containsKey(strTableName)){
            htblMetadata.get(strTableName).get(strColName).put("IndexName", "N/A");
            htblMetadata.get(strTableName).get(strColName).put("IndexType", "N/A");
        }
        else{
            throw new IOException("Table does not exist");
        }
    }

    /**
     * The `save` method writes metadata information to a CSV file for each table and its columns in a
     * specific format.
     */
    public void save() throws IOException{
        try (FileOutputStream fileOutputStream = new FileOutputStream("metadata.csv")) {
            for (String strTableName : htblMetadata.keySet()) {
                
                for (String strColumnName : htblMetadata.get(strTableName).keySet()) {
                    fileOutputStream.write((strTableName + ", ").getBytes());
                    fileOutputStream.write((strColumnName + ", ").getBytes());
                    fileOutputStream.write((htblMetadata.get(strTableName).get(strColumnName).get("Column Type") + ", ").getBytes());
                    fileOutputStream.write((htblMetadata.get(strTableName).get(strColumnName).get("ClusteringKey") + ", ").getBytes());
                    fileOutputStream.write((htblMetadata.get(strTableName).get(strColumnName).get("IndexName") + ", ").getBytes());
                    fileOutputStream.write((htblMetadata.get(strTableName).get(strColumnName).get("IndexType") + "\n").getBytes());
                }
            }
        }
    }


    /**
     * The `toString` function iterates over a map of metadata and returns a formatted string
     * representation of the metadata.
     * 
     * @return The `toString` method is returning a string representation of the metadata stored in the
     * `htblMetadata` map. It concatenates each table name along with its corresponding metadata
     * separated by a comma and a newline character.
     */
    public String toString(){

        String strMetadata = "";

        for (String strTableName : htblMetadata.keySet()) {
            strMetadata += strTableName + ", " + htblMetadata.get(strTableName) + "\n";
        }

        return strMetadata;
        

    }


    
    }

    
