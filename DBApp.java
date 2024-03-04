
/** * @author Wael Abouelsaadat */ 

import java.util.Iterator;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;




public class DBApp {
	
	private Metadata metadata;
    private Table table;
	private Hashtable<String, bplustree> htblIndices;

	public DBApp( ) throws FileNotFoundException, IOException, ClassNotFoundException{
		metadata = new Metadata();

        // TESTING PURPOSES ONLY
        table = new Table("City Shop");
        table.addPage("CityShop_0");
        table.addPage("CityShop_1");
        table.addPage("CityShop_2");
        table.addPage("CityShop_3");
        table.addPage("CityShop_4");
        
		table.serialize("CityShop.class");

		table.printAllPages();

		htblIndices = new Hashtable<>();

		htblIndices.put("CityShop", new bplustree(3));



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

	private Page getPageByClusteringKey(String strTableName, String strClusteringKey, Object objClusteringKeyValue) throws IOException, ClassNotFoundException, DBAppException{

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


	private void deleteWithClusteringKey(String strTableName, String strClusteringKey, Hashtable<String,Object> htblColNameValue) throws IOException,ClassNotFoundException, DBAppException{
		
        Object objClusteringKeyValue = htblColNameValue.get(strClusteringKey);
		Page pagePage = getPageByClusteringKey(strTableName, strClusteringKey, objClusteringKeyValue);
        int intTupleIndex = pagePage.searchTuplesByClusteringKey(strClusteringKey, objClusteringKeyValue);
		pagePage.deleteTupleWithIndex(intTupleIndex);
		pagePage.serialize(pagePage.getPageName());
		if(pagePage.getSize() == 0){
			pagePage.deletePage();
			table.removePage(pagePage.getPageName());
			table.serialize(table.getTableName());
		}
	}

	private void deleteNonClusterinKeyWithIndex(String strTableName, String strIndexedColumn, Hashtable<String,Object> htblColNameValue){

		bplustree bplustreeIndex = htblIndices.get(strTableName);
		Integer intIndexedColumnValue = (Integer) htblColNameValue.get(strIndexedColumn);

		bplustreeIndex.search(intIndexedColumnValue);

		

	}

	private void deleteNonClusterinKeyWithoutIndex(String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException, ClassNotFoundException, DBAppException{

		// Linear search

		int intNumberOfPages = table.getNumberOfPages();

		for(int intPageIndex = 0; intPageIndex < intNumberOfPages; intPageIndex++){
			String pageCurrentPageName = table.getPageAtIndex(intPageIndex);
			Page pageCurrentPage = Page.deserialize(pageCurrentPageName + ".class");
			
			
			// Linearly search every page

			int intCurrentPageSize = pageCurrentPage.getSize();

			for(int intCurrentPageIndex = 0; intCurrentPageIndex < intCurrentPageSize; intCurrentPageIndex++){
				Tuple tupleCurrentTuple = pageCurrentPage.getTupleWithIndex(intCurrentPageIndex);
				boolean boolToBeDeletedTuple = false;
				// AND all columns
				for(String strCol : htblColNameValue.keySet()){
					boolToBeDeletedTuple = true;
					if(!tupleCurrentTuple.getColumnValue(strCol).equals(htblColNameValue.get(strCol))){
						boolToBeDeletedTuple = false;
						break;
					}
				}

				if(boolToBeDeletedTuple){
					pageCurrentPage.deleteTupleWithIndex(intCurrentPageIndex);
				}

				
				
			}

			// TODO: Don't do this step if not necessary

			pageCurrentPage.serialize(pageCurrentPageName + ".class");
			

		}
		
	}

	private void deleteWithoutClusteringKey(String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException, ClassNotFoundException, DBAppException{
		

		for(String col : htblColNameValue.keySet()){
			if(metadata.isColumnIndexed(strTableName, col)){
				deleteNonClusterinKeyWithIndex(strTableName, col, htblColNameValue);
				return;
			}
		}

		deleteNonClusterinKeyWithoutIndex(strTableName, htblColNameValue);

	}

	


	// following method could be used to delete one or more rows.
	// htblColNameValue holds the key and value. This will be used in search 
	// to identify which rows/tuples to delete. 	
	// htblColNameValue enteries are ANDED together
	public void deleteFromTable(String strTableName, 
								Hashtable<String,Object> htblColNameValue) throws DBAppException, IOException,ClassNotFoundException{

		
		String strClusteringKey = metadata.getClusteringKey(strTableName);

		if(htblColNameValue.containsKey(strClusteringKey)){
			deleteWithClusteringKey(strTableName, strClusteringKey, htblColNameValue);
		}else{
			deleteWithoutClusteringKey(strTableName, htblColNameValue);
		}
		
		


		
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

			tree.displayTree();



			

			// String strTableName = "Student";
			Hashtable htblColNameValue = new Hashtable( );
			htblColNameValue.put("ID", new Integer( 3 ));
			htblColNameValue.put("name", new String("Ahmed Noor" ) );
			htblColNameValue.put("gpa", new Double( 0.95 ) );
			DBApp	dbApp = new DBApp( );
			dbApp.deleteFromTable("CityShop", htblColNameValue);
			
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