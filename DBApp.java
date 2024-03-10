
/** * @author Wael Abouelsaadat */ 

import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;



public class DBApp  {
	
	private Metadata metadata;
	/*The hashtable htbIndex is used to store the indicies where the key of this hashtable is the index name
	and the value is the index itself*/
	private Hashtable<String, bplustree> htbIndex ;

	public DBApp( ) throws FileNotFoundException, IOException{
		metadata = new Metadata();
		htbIndex = new Hashtable<>();
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


	// following method creates one table only
	// strClusteringKeyColumn is the name of the column that will be the primary
	// key and the clustering column as well. The data type of that column will
	// be passed in htblColNameType
	// htblColNameValue will have the column name as key and the data 
	// type as value
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
	/** 
	 * @param strTableName The `strTableName` parameter represents the name of the table to which you
     * want to create an index.
     * @param strColName The `strColName` parameter represents the name of the column for which you
     * want to create an index in the specified table.
	 * @param strIndexName The strIndexName parameter represents the name of the index which we will add
	 * First we check whether the table exists and if not we throw an appropriate exeption
	 * Then we check whether the column we want to index exists and if not we throw an appropriate exeption
	 * Thirdly we check whether the index already exisits then we throw the exeption accordingly
	 * If no exeptions are thrown we can now create the index by firstadding the index data to the metadata file
	 * Then creating The index b+tree , then getting the table object by deserializing the table name.
	 * Using this table object we get the vector of pages that encapsulate this table as strings
	 * Like the table we deserializ the pages to get the page object and get every tuplein the page and extract the values of 
	 * the column needed to be indexed from tuples hence inserting them in the tree.Lastly we serialize the tree to store it 
	 * in the memory and put the  tree in the htbIndex so it can be accessed if needed anytime in the other code segments
	 */
	
	public void createIndex(String strTableName,String strColName,String strIndexName) throws DBAppException, IOException, ClassNotFoundException{
        if(!metadata.checkTableName(strTableName)){
            throw new DBAppException("This table does not exist");

        }
        else if(!metadata.checkColumnName(strTableName,strColName)){
             throw new DBAppException("This column does not exist");
        }
        else if (!(metadata.getIndexName(strTableName,strColName).equals("N/A"))){
            throw new DBAppException("An index for this column already exists");

        }
        else{
        
            metadata.addIndex(strTableName,strColName,"B+Tree",strIndexName);

			Table tblTable= Table.deserialize("tables/" +strTableName + "/" +strTableName  +".ser");
            Vector<String> vecPages = tblTable.getPages();
			bplustree bplsBplustree;

			if (metadata.getColumnType(strTableName,strColName).equals("java.lang.Integer")){
				 bplsBplustree = new bplustree<Integer, Tuple>(100);
			}
			else if (metadata.getColumnType(strTableName,strColName).equals("java.lang.Double")){
				 bplsBplustree = new bplustree<Double, Tuple>(100);
			}
			else {
				 bplsBplustree = new bplustree<String, Tuple>(100);
			}


            
            
           // Loop through the column values
            for (String pgPage_name : vecPages) {
                Page pgPage = Page.deserialize("tables/" +strTableName + "/"  + pgPage_name + ".ser");
                Vector<Tuple> vecTuples = pgPage.getTuples();
                for (Tuple tplTuple : vecTuples) {
					if(tplTuple.getColumnValue(strColName) instanceof Integer){
						int key = (int) tplTuple.getColumnValue(strColName); 
						bplsBplustree.insert(key,tplTuple);
					}
					else if(tplTuple.getColumnValue(strColName) instanceof String) {
						String key = (String) tplTuple.getColumnValue(strColName);
						bplsBplustree.insert(key,tplTuple);
					}
					else{
						double key = (double) tplTuple.getColumnValue(strColName);
						bplsBplustree.insert(key,tplTuple);
					}
                    
                    }
                }
			bplsBplustree.serialize("Indicies/" + strIndexName + ".ser");
		    htbIndex.put(strIndexName ,bplsBplustree);
		
        }
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

			bplustree tree = new bplustree(3);

			tree.insert(1, "amr");
			tree.insert(2, "mohamed");
			tree.insert(3, "wael");
			tree.insert(4, "ahmed");

			// tree.printTree(tree.root);

			

			

			  String strTableName = "Student";
			//  Hashtable htblColNameValue = new Hashtable( );
			//  htblColNameValue.put("ID", new Integer( 2343432 ));
			//  htblColNameValue.put("name", new String("Ahmed Noor" ) );
			//  htblColNameValue.put("gpa", new Double( 0.95 ) );
			  DBApp	dbApp = new DBApp( );
			// dbApp.deleteFromTable("CityShop", htblColNameValue);
			
			 Hashtable htblColNameType = new Hashtable( );
			 htblColNameType.put("id", "java.lang.Integer");
			 htblColNameType.put("name", "java.lang.String");
			 htblColNameType.put("gpa", "java.lang.double");
			 dbApp.createTable( strTableName, "id", htblColNameType );
			 dbApp.createIndex( strTableName, "gpa", "gpaIndex" );

			// Hashtable htblColNameValue = new Hashtable( );
			// htblColNameValue.put("id", new Integer( 2343432 ));
			// htblColNameValue.put("name", new String("Ahmed Noor" ) );
			// htblColNameValue.put("gpa", new Double( 0.95 ) );
			// dbApp.insertIntoTable( strTableName , htblColNameValue );

			// htblColNameValue.clear( );
			// htblColNameValue.put("id", new Integer( 453455 ));
			// htblColNameValue.put("name", new String("Ahmed Noor" ) );
			// htblColNameValue.put("gpa", new Double( 0.95 ) );
			// dbApp.insertIntoTable( strTableName , htblColNameValue );

			// htblColNameValue.clear( );
			// htblColNameValue.put("id", new Integer( 5674567 ));
			// htblColNameValue.put("name", new String("Dalia Noor" ) );
			// htblColNameValue.put("gpa", new Double( 1.25 ) );
			// dbApp.insertIntoTable( strTableName , htblColNameValue );

			// htblColNameValue.clear( );
			// htblColNameValue.put("id", new Integer( 23498 ));
			// htblColNameValue.put("name", new String("John Noor" ) );
			// htblColNameValue.put("gpa", new Double( 1.5 ) );
			// dbApp.insertIntoTable( strTableName , htblColNameValue );

			// htblColNameValue.clear( );
			// htblColNameValue.put("id", new Integer( 78452 ));
			// htblColNameValue.put("name", new String("Zaky Noor" ) );
			// htblColNameValue.put("gpa", new Double( 0.88 ) );
			// dbApp.insertIntoTable( strTableName , htblColNameValue );


			// SQLTerm[] arrSQLTerms;
			// arrSQLTerms = new SQLTerm[2];
			// arrSQLTerms[0]._strTableName =  "Student";
			// arrSQLTerms[0]._strColumnName=  "name";
			// arrSQLTerms[0]._strOperator  =  "=";
			// arrSQLTerms[0]._objValue     =  "John Noor";

			// arrSQLTerms[1]._strTableName =  "Student";
			// arrSQLTerms[1]._strColumnName=  "gpa";
			// arrSQLTerms[1]._strOperator  =  "=";
			// arrSQLTerms[1]._objValue     =  new Double( 1.5 );

			// String[]strarrOperators = new String[1];
			// strarrOperators[0] = "OR";
			// // select * from Student where name = "John Noor" or gpa = 1.5;
			// Iterator resultSet = dbApp.selectFromTable(arrSQLTerms , strarrOperators);
		}
		catch(Exception exp){
			exp.printStackTrace( );
		}
	}

}