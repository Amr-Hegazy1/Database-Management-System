
/** * @author Wael Abouelsaadat */ 

import java.util.Iterator;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;



public class DBApp {
	
	private Metadata metadata;

	public DBApp( ) throws FileNotFoundException, IOException{
		metadata = new Metadata();
	}

	 

	// this does whatever initialization you would like 
	// or leave it empty if there is no code you want to 
	// execute at application startup 
	public void init( ){

		// TODO: LOAD INDICES
		// TODO: LOAD METADATA FILE


		Properties prop = new Properties();
        String fileName = "DBApp.config";
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (FileNotFoundException ex) {
            // TODO
        } catch (IOException ex) {
           // TODO
        }
        System.out.println(prop.getProperty("MaximumRowsCountinPage"));
		

		

		
	}


	/**
     * The `createTable` function creates one tabel only.
	 * 
	 * @param strTableName The `strTableName` is the name of the table needed be created.
	 * 
	 * @param strClusteringKeyColumn The `strClusteringKeyColumn` is the name of the column 
	 * that will be the primary key and the clustering column as well.
	 * The data type of that column will be passed in htblColNameType.
     * 
     * @param htblColNameValue The `htblColNameValue` will have the column name as key and
	 * the data type as value.
	 * 
	 * @throws DBAppException The `DBAppException` will be thrown if there exists a table with
	 * the same name.
	 * 
	 * @throws IOExecption The `IOExecption` will be thrown if the function fails to create the
	 * folder or fails to serialize the table.
     */
	public void createTable(String strTableName, 
							String strClusteringKeyColumn,  
							Hashtable<String,String> htblColNameType) throws DBAppException , IOException{
		
		metadata.addTable(strTableName, strClusteringKeyColumn, htblColNameType);
		Table tblTable = new Table(strTableName);
		File fileTableFolder = new File("tables/" + strTableName);
		if(!fileTableFolder.exists()){
			boolean boolSuccess = fileTableFolder.mkdir();
			if (boolSuccess){
				tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".ser");
			}
			else{
				throw new IOException("Can't create a Folder!");
			}
		}
		else {
			throw new DBAppException("Table already exists!");
		}
	}


	// following method creates a B+tree index 
	public void createIndex(String   strTableName,
							String   strColName,
							String   strIndexName) throws DBAppException{
		
		throw new DBAppException("not implemented yet");
	}


	// following method inserts one row only. 
	// htblColNameValue must include a value for the primary key
	public void insertIntoTable(String strTableName, 
								Hashtable<String,Object>  htblColNameValue) throws DBAppException{
	
		throw new DBAppException("not implemented yet");
	}


	// following method updates one row only
	// htblColNameValue holds the key and new value 
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName, 
							String strClusteringKeyValue,
							Hashtable<String,Object> htblColNameValue   )  throws DBAppException{
	
		throw new DBAppException("not implemented yet");
	}


	// following method could be used to delete one or more rows.
	// htblColNameValue holds the key and value. This will be used in search 
	// to identify which rows/tuples to delete. 	
	// htblColNameValue enteries are ANDED together
	public void deleteFromTable(String strTableName, 
								Hashtable<String,Object> htblColNameValue) throws DBAppException{

		
		throw new DBAppException("not implemented yet");
		
	}


	public Iterator selectFromTable(SQLTerm[] arrSQLTerms, 
									String[]  strarrOperators) throws DBAppException{
										
		return null;
	}


	public static void main( String[] args ){
	
		try{
			DBApp myDbApp = new DBApp();
			String strTableName = "Studen1t";
			String strClustringKeyColumn = "ID";
			Hashtable<String,String> htblColNameType = new Hashtable<>();
			htblColNameType.put("ID", "java.lang.Integer");
			htblColNameType.put("Name", "java.lang.String");
			htblColNameType.put("GPA", "java.lang.Double");
			myDbApp.createTable(strTableName , strClustringKeyColumn , htblColNameType);
			// Table myTable = Table.deserialize("tables/"+strTableName+"/"+strTableName+".ser");
			// System.out.println(myTable.strTableName);

		} catch(Exception exp){
			exp.printStackTrace( );
		}
	}

}