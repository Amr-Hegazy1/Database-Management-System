
/** * @author Wael Abouelsaadat */ 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;



public class DBApp {
	
	private Metadata metadata;
	private Hashtable<String, Hashtable<String, bplustree>> htbltrees; //Table Name, Index Name, B+Tree


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

	private Page getPageByClusteringKey(String strTableName, String strClusteringKey, Object objClusteringKeyValue) throws IOException, ClassNotFoundException, DBAppException{
	Table table=new Table(strTableName);
    int intTableSize = table.getNumberOfPages();
    int intTopPageIndex = 0;
    int intBottomPageIndex = intTableSize - 1;

    while(intTopPageIndex <= intBottomPageIndex){

        int intMiddlePageIndex = intTopPageIndex + (intBottomPageIndex - intTopPageIndex) / 2;

        String strMiddlePage = table.getPageAtIndex(intMiddlePageIndex);

        System.out.println(intMiddlePageIndex);

        Page pageMiddlePage = Page.deserialize(strMiddlePage + ".class");

        int intMiddlePageSize = pageMiddlePage.getSize();

        Tuple tupleMiddlePageTopTuple = pageMiddlePage.getTupleWithIndex(0);

        Tuple tupleMiddlePageBottomTuple = pageMiddlePage.getTupleWithIndex(intMiddlePageSize - 1);

		// convert the clustering key value to appropriate type for valid comparison
		// for example, if the clustering key is of type integer, then convert the string value to integer
		// if the clustering key is of type double, then convert the string value to double
		// if the clustering key is of type string, then no conversion is needed

		// TODO: REFACTOR
		
		if(tupleMiddlePageTopTuple.getColumnValue(strClusteringKey) instanceof Integer){
			objClusteringKeyValue = (Integer) objClusteringKeyValue;
			Integer topTupleValue = (Integer) tupleMiddlePageTopTuple.getColumnValue(strClusteringKey);
			Integer bottomTupleValue = (Integer) tupleMiddlePageBottomTuple.getColumnValue(strClusteringKey);
			System.out.println("Integer");

			// check if the clustering key is in the page by checking if the value is between the top and bottom tuple
			if((Integer) objClusteringKeyValue >= topTupleValue && (Integer) objClusteringKeyValue <= bottomTupleValue){
				return pageMiddlePage;

			}else if((Integer) objClusteringKeyValue < topTupleValue){
				intBottomPageIndex = intMiddlePageIndex - 1;
			}else{
				intTopPageIndex = intMiddlePageIndex + 1;
			}

		}else if(tupleMiddlePageTopTuple.getColumnValue(strClusteringKey) instanceof Double){
			objClusteringKeyValue = (Double) objClusteringKeyValue;
			Double topTupleValue = (Double) tupleMiddlePageTopTuple.getColumnValue(strClusteringKey);
			Double bottomTupleValue = (Double) tupleMiddlePageBottomTuple.getColumnValue(strClusteringKey);
			System.out.println("Double");

			// check if the clustering key is in the page by checking if the value is between the top and bottom tuple

			if((Double) objClusteringKeyValue >= topTupleValue && (Double) objClusteringKeyValue <= bottomTupleValue){
				return pageMiddlePage;

			}else if((Double) objClusteringKeyValue < topTupleValue){
				intBottomPageIndex = intMiddlePageIndex - 1;
			}else{
				intTopPageIndex = intMiddlePageIndex + 1;
			}


		}else{
			objClusteringKeyValue = (String) objClusteringKeyValue;
			String topTupleValue = (String) tupleMiddlePageTopTuple.getColumnValue(strClusteringKey);
			String bottomTupleValue = (String) tupleMiddlePageBottomTuple.getColumnValue(strClusteringKey);
			System.out.println("String");
			
			// check if the clustering key is in the page by checking if the value is between the top and bottom tuple
			if(((String) objClusteringKeyValue).compareTo(topTupleValue) >= 0 && ((String) objClusteringKeyValue).compareTo(bottomTupleValue) <= 0){
				return pageMiddlePage;
			}else if(((String) objClusteringKeyValue).compareTo(topTupleValue) < 0){
				intBottomPageIndex = intMiddlePageIndex - 1;
			}else{
				intTopPageIndex = intMiddlePageIndex + 1;
			}
		}
		}
	return null;

    }

	// following method updates one row only
	// htblColNameValue holds the key and new value 
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName, String strClusteringKeyValue, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		try {
			if (metadata.checkTableName(strTableName)) 
				throw new DBAppException("Table does not exist");
			
			if (strClusteringKeyValue == null) {
				throw new DBAppException("Clustering key value is null");
			}
			if (htblColNameValue.isEmpty()) {
				throw new DBAppException("no column to update");
			}
			
			if (!metadata.checkColumnName(strTableName, strClusteringKeyValue)) {
				throw new DBAppException("Clustering key column does not exist");
			}
			
			for (String columnName : htblColNameValue.keySet()) {
				
				String columnTypeMetadata = metadata.getColumnType(strTableName, columnName);
				Object columnValue = htblColNameValue.get(columnName);
				String strdatatype = columnValue.getClass().getSimpleName();
				if (!columnTypeMetadata.equals(strdatatype)) {
					throw new DBAppException("Datatypes do not match for the column");
				}
			}
	
			Page page = getPageByClusteringKey(strTableName, strClusteringKeyValue, htblColNameValue.get(strClusteringKeyValue));
			int tupleIndex = page.searchTuplesByClusteringKey(strClusteringKeyValue, htblColNameValue.get(strClusteringKeyValue));
	
			if (page != null) {
				
				Vector<Tuple> tuples = page.getVecTuples();
				Tuple tuple = tuples.get(tupleIndex);
				Hashtable<String, Hashtable<String, String>> htblMetadata = metadata.getTableMetadata(strTableName);
				
				for (String columnName : htblColNameValue.keySet()) {
					
					Object columnValue = htblColNameValue.get(columnName);
					tuple.setColumnValue(columnName, columnValue);
	
				   // if (columnName.equals(strClusteringKeyValue)) {
					 //   htblMetadata.get(strClusteringKeyValue).put("ClusteringKey", columnValue.toString()); 
					//}
				
	 
				page.serialize("tables/" + strTableName + "/" + page + ".class"); //notsure
	
				boolean boolindexorno = false;
				if (htblMetadata.containsKey(columnName) && metadata.getIndexName(strTableName, columnName) != null) {
					
					String strindexName = metadata.getIndexName(strTableName,columnName);
					if(strindexName!=null){
						
						boolindexorno=true;
						}
					 else
						 
						 boolindexorno=false;        
			}
			   
				if (boolindexorno) {
					
					String strindexName = metadata.getIndexName(strTableName, strClusteringKeyValue);
					bplustree bptree = htbltrees.get(strTableName).get(strindexName);
					
				  //Comparable ComVar=(Comparable) value; 
					
					bptree.delete(strClusteringKeyValue);
					bptree.insert(strClusteringKeyValue, htblColNameValue.get(strClusteringKeyValue));
					
					//tree.insert(key, ComVar); //typecast el value comparable
	
				}
				}
			} else {
				throw new DBAppException("Page not found for the given clustering key.");
			}
			
		} catch (IOException | ClassNotFoundException e) {
			throw new DBAppException("Exception occurred: " + e.getMessage());
		}
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


	public static void main(String[] args) {

		try {
			Tuple t1 = new Tuple();
			t1.setColumnValue("id", "1"); // 0

			Tuple t2 = new Tuple();
			t2.setColumnValue("id", "3"); // 1

			Tuple t3 = new Tuple();
			t3.setColumnValue("id", "4"); // 2

			Page p = new Page();
			p.addTuple(0, t1);
			p.addTuple(1, t2);
			p.addTuple(2, t3);

			int i = p.searchTuplesByClusteringKey("id", 6);

			System.out.println(i);
			// Tuple t1 = new Tuple();
			// Tuple t2 = new Tuple();
			// t1.setColumnValue("name", "Ahmed");
			// t1.setColumnValue("age", 20);
			// t2.setColumnValue("name", "Ahmed");
			// t2.setColumnValue("age", 20);

			// HashMap<String, Object> hm = new HashMap<String, Object>();
			// hm.put("name", "Ahmed");
			// hm.put("age", 20);

			// HashMap<String, Object> hm2 = new HashMap<String, Object>();
			// hm2.put("name", "Ahmed");
			// hm2.put("age", 20);

			// // System.out.println(hm.equals(hm2));

			// System.out.println(t1.equals(t2));

			// Page page = new Page();
			// Tuple tuple = new Tuple();
			// tuple.setColumnValue("name", "Ahmed");
			// tuple.setColumnValue("age", 20);
			// page.addTuple(0, tuple);

			// page.serialize("serializedPage.class"); // try to make it .class

			// Later, you can deserialize the Page object from the file
			// Page deserializedPage = Page
			// .deserialize("serializedPage.class");

			// // Now you can access the tuples
			// System.out.print(deserializedPage);
			// bplustree tree = new bplustree(3);

			// tree.insert(1, "amr");
			// tree.insert(2, "mohamed");
			// tree.insert(3, "wael");
			// tree.insert(4, "ahmed");

			// // tree.printTree(tree.root);

			// tree.displayTree();

			// String strTableName = "Student";
			// Hashtable htblColNameValue = new Hashtable( );
			// htblColNameValue.put("ID", new Integer( 2343432 ));
			// htblColNameValue.put("name", new String("Ahmed Noor" ) );
			// htblColNameValue.put("gpa", new Double( 0.95 ) );
			// DBApp dbApp = new DBApp( );
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
			// arrSQLTerms[0]._strTableName = "Student";
			// arrSQLTerms[0]._strColumnName= "name";
			// arrSQLTerms[0]._strOperator = "=";
			// arrSQLTerms[0]._objValue = "John Noor";

			// arrSQLTerms[1]._strTableName = "Student";
			// arrSQLTerms[1]._strColumnName= "gpa";
			// arrSQLTerms[1]._strOperator = "=";
			// arrSQLTerms[1]._objValue = new Double( 1.5 );

			// String[]strarrOperators = new String[1];
			// strarrOperators[0] = "OR";
			// // select * from Student where name = "John Noor" or gpa = 1.5;
			// Iterator resultSet = dbApp.selectFromTable(arrSQLTerms , strarrOperators);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}