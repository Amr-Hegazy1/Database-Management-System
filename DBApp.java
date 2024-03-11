
/** * @author Wael Abouelsaadat */ 

import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Hashtable;



public class DBApp {
	
	private Metadata metadata;
	private bplustree bpt= new bplustree(9);
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


	// following method creates one table only
	// strClusteringKeyColumn is the name of the column that will be the primary
	// key and the clustering column as well. The data type of that column will
	// be passed in htblColNameType
	// htblColNameValue will have the column name as key and the data 
	// type as value
	public void createTable(String strTableName, 
							String strClusteringKeyColumn,  
							Hashtable<String,String> htblColNameType) throws DBAppException{
								
		throw new DBAppException("not implemented yet");
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

	//TODO handle law empty arr

	public Iterator selectFromTable(SQLTerm[] arrSQLTerms, 
									String[]  strarrOperators) throws DBAppException, ClassNotFoundException, IOException{
		if(arrSQLTerms.length!= strarrOperators.length+1){
			throw new DBAppException("something wrong in the input");
		}
		HashSet<String> hmkilobatata = new HashSet<>();
		hmkilobatata.add("=");hmkilobatata.add("!=");hmkilobatata.add(">=");hmkilobatata.add("<=");hmkilobatata.add(">");;hmkilobatata.add("<");
		for(int i=0;i<arrSQLTerms.length;i++){
		if(metadata.checkTableName(arrSQLTerms[i]._strTableName)){
			if(metadata.checkColumnName(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName)){
				String strType= metadata.getColumnType(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName);
				if( hmkilobatata.contains(arrSQLTerms[i]._strOperator)){	
					if(!strType.equals(arrSQLTerms[i]._objValue)){
						throw new DBAppException("Datatype doesn't match");
					}
				}
				else 
				throw new DBAppException("This Operator isn't supported");
			}
			else
			throw new DBAppException("Column doesn't exist");
		}
		else
		throw new DBAppException("Table doesn't exist");
	}
	    // el hashes at end of if statements are stand by
		Stack<SQLTerm> st = new Stack<>();
		Stack<String> st2 = new Stack<>();
		st.push(arrSQLTerms[0]);
		if(strarrOperators.length==0){
			if(arrSQLTerms[0]._strOperator.equals("=")){
				if(metadata.getIndexType(arrSQLTerms[0]._strTableName,arrSQLTerms[0]._strColumnName)!=null){
					
					String strpage= bpt.search((int)arrSQLTerms[0]._objValue); //for now
					Page deserializedpage = Page.deserialize(strpage);
					HashSet<Tuple> hstup=deserializedpage.eqsearch(strpage, arrSQLTerms[0]._objValue);
					
				}
				else{
					
					Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
					if( ! metadata.isClusteringKey(arrSQLTerms[0]._strTableName, arrSQLTerms[0]._strColumnName)){
						HashSet<Tuple> hstup= table.eqsearch(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue); 
					}
					else{
						HashSet<Tuple> hstup= table.cleqsearch(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
					}
				}
				
			}
			else if(arrSQLTerms[0]._strOperator.equals(">")){
				if(metadata.getIndexType(arrSQLTerms[0]._strTableName,arrSQLTerms[0]._strColumnName)!=null){
					
					String strpage= bpt.search((int)arrSQLTerms[0]._objValue); //for now
					Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
					HashSet<Tuple> hstup=table.greaterthan(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
					
					
				}
				else{
			
					Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
					if( ! metadata.isClusteringKey(arrSQLTerms[0]._strTableName, arrSQLTerms[0]._strColumnName)){
						HashSet<Tuple> hstup= table.greaterthannc(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue); 
					}
					else{
						HashSet<Tuple> hstup= table.greaterthan(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
					}
				}	
			}else if(arrSQLTerms[0]._strOperator.equals(">=")){
			if(metadata.getIndexType(arrSQLTerms[0]._strTableName,arrSQLTerms[0]._strColumnName)!=null){
				
				String strpage= bpt.search((int)arrSQLTerms[0]._objValue); //for now
				
				Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
				HashSet<Tuple> hstup=table.greaterthaneq(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
				
			}
			else{
				Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
				if( ! metadata.isClusteringKey(arrSQLTerms[0]._strTableName, arrSQLTerms[0]._strColumnName)){
					HashSet<Tuple> hstup= table.greaterthaneqnc(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue); 
				}
				else{
					HashSet<Tuple> hstup= table.greaterthaneq(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
				}
			}	
		}else if(arrSQLTerms[0]._strOperator.equals("<")){
			if(metadata.getIndexType(arrSQLTerms[0]._strTableName,arrSQLTerms[0]._strColumnName)!=null){
				
				String strpage= bpt.search((int)arrSQLTerms[0]._objValue); //for now
				
				Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
				HashSet<Tuple> hstup=table.lesserthan(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
				}
			else{
				Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
				if( ! metadata.isClusteringKey(arrSQLTerms[0]._strTableName, arrSQLTerms[0]._strColumnName)){
					HashSet<Tuple> hstup= table.lesserthannc(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue); 
				}
				else{
					HashSet<Tuple> hstup= table.lesserthan(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
				}
			}	
		}else if(arrSQLTerms[0]._strOperator.equals("<=")){
			if(metadata.getIndexType(arrSQLTerms[0]._strTableName,arrSQLTerms[0]._strColumnName)!=null){
				
				String strpage= bpt.search((int)arrSQLTerms[0]._objValue); //for now
				
				Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
				HashSet<Tuple> hstup=table.lesserthaneq(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
				}
			else{
				Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
				if( ! metadata.isClusteringKey(arrSQLTerms[0]._strTableName, arrSQLTerms[0]._strColumnName)){
					HashSet<Tuple> hstup= table.lesserthannceq(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue); 
				}
				else{
					HashSet<Tuple> hstup= table.lesserthaneq(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
				}
			}	
		} else{   
			Table table = Table.deserialize(arrSQLTerms[0]._strTableName);
			HashSet<Tuple> hstup= table.noteqsearch(arrSQLTerms[0]._strColumnName, arrSQLTerms[0]._objValue);
		}

	// i have 2 ideas hena hab2a discuss ma3ako in meeting incase i didnt finish on my own
	for(int i=0;i<strarrOperators.length;i++){
		
	}
	int left=0;
	int right=0;
	
	}

}


	public static void main( String[] args ){
	
	try{

			bplustree tree = new bplustree(3);

			tree.insert(1, "amr");
			tree.insert(2, "mohamed");
			tree.insert(3, "wael");
			tree.insert(4, "ahmed");

			// tree.printTree(tree.root);

			tree.displayTree();

		
			 
			

			// String strTableName = "Student";
			// Hashtable htblColNameValue = new Hashtable( );
			// htblColNameValue.put("ID", new Integer( 2343432 ));
			// htblColNameValue.put("name", new String("Ahmed Noor" ) );
			// htblColNameValue.put("gpa", new Double( 0.95 ) );
			// DBApp	dbApp = new DBApp( );
			// dbApp.deleteFromTable("CityShop", htblColNameValue);
			
			// Hashtable htblColNameType = new Hashtable( );
			// htblColNameType.put("id", "java.lang.Integer");
			// htblColNameType.put("name", "java.lang.String");
			// htblColNameType.put("gpa", "java.lang.double");
			// dbApp.createTable( strTableName, "id", htblColNameType );
			// dbApp.createIndex( strTableName, "gpa", "gpaIndex" );

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