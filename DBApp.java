
/** * @author Wael Abouelsaadat */ 


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;





public class DBApp {
	
	private Metadata metadata;
    private Table table;
	private Hashtable<String, bplustree> htblIndices;

	public DBApp( ) throws FileNotFoundException, IOException, ClassNotFoundException{
		metadata = new Metadata();

		
		
		
		

        // TESTING PURPOSES ONLY

		Hashtable htblColNameType = new Hashtable( );

		htblColNameType.put("ID", "java.lang.Integer");
		htblColNameType.put("Name", "java.lang.String");
		htblColNameType.put("Age", "java.lang.Integer");
		// metadata.addTable("CityShop", "ID", htblColNameType);
		// metadata.addIndex("CityShop", "Name", "B+Tree");

		

		metadata.save();

		
        table = new Table("City Shop");
        table.addPage("CityShop_0");
        table.addPage("CityShop_1");
        table.addPage("CityShop_2");
        table.addPage("CityShop_3");
        table.addPage("CityShop_4");
        
		table.serialize("CityShop.class");

		// table.printAllPages();

		htblIndices = new Hashtable<>();

		bplustree<String, Pair<Tuple, String>> tree = new bplustree<String, Pair<Tuple, String>>(3);

		Tuple tuple = new Tuple();

		tuple.setColumnValue("ID", 0);
		tuple.setColumnValue("Name", "Ahmed00");
		tuple.setColumnValue("Age", 20);

		Pair pairPair = new Pair(tuple, "CityShop_0");

		tree.insert("Ahmed00", pairPair);

		htblIndices.put("NameIndex",tree);



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

	/**
	 * The function `getPageByClusteringKey` searches for a specific page in a table based on a given
	 * clustering key value.
	 * 
	 * @param strTableName It seems like you were about to provide some information about the parameters,
	 * but the message got cut off. Could you please provide more details about the parameters
	 * `strTableName`, `strClusteringKey`, and `objClusteringKeyValue` so that I can assist you further
	 * with the `getPageByCl
	 * @param strClusteringKey It seems like you were about to provide some information about the
	 * parameter `strClusteringKey` but the message got cut off. How can I assist you further with this
	 * parameter?
	 * @param objClusteringKeyValue It seems like the method `getPageByClusteringKey` is trying to find a
	 * specific page in a table based on a clustering key value. The method uses binary search to locate
	 * the page efficiently.
	 * @return The method `getPageByClusteringKey` is returning a `Page` object. If the clustering key
	 * value is found within the specified range in the page, then that page is returned. If the key is
	 * not found within the range, the method will continue searching through the pages until it either
	 * finds the key or exhausts all pages, in which case it will return `null`.
	 */
	private Page getPageByClusteringKey(String strTableName, String strClusteringKey, Object objClusteringKeyValue) throws IOException, ClassNotFoundException, DBAppException{

        int intTableSize = table.getNumberOfPages();
        int intTopPageIndex = 0;
        int intBottomPageIndex = intTableSize - 1;

        while(intTopPageIndex <= intBottomPageIndex){

            int intMiddlePageIndex = intTopPageIndex + (intBottomPageIndex - intTopPageIndex) / 2;

            String strMiddlePage = table.getPageAtIndex(intMiddlePageIndex);

            

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


	/**
	 * This Java function deletes a tuple with a specified clustering key from a table and handles page
	 * deletion if necessary.
	 * 
	 * @param strTableName The `strTableName` parameter is a `String` representing the name of the table
	 * from which you want to delete a record.
	 * @param strClusteringKey The `strClusteringKey` parameter in the `deleteWithClusteringKey` method
	 * refers to the name of the clustering key column in the table from which you want to delete a
	 * record.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that contains the column
	 * names as keys and their corresponding values as values. This Hashtable represents the values of the
	 * columns for the record that you want to delete.
	 */
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

	/**
	 * This Java function retrieves tuples from an index based on a specified table name, indexed column,
	 * and column values.
	 * 
	 * @param strTableName The `strTableName` parameter represents the name of the table from which you
	 * want to retrieve tuples based on the indexed column value.
	 * @param strIndexedColumn The `strIndexedColumn` parameter in the `getTuplesFromIndex` method refers
	 * to the name of the column that is indexed in the B+ tree for a specific table. This column is used
	 * to efficiently retrieve tuples based on their values in that column.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that contains the column
	 * names as keys and their corresponding values as values. In the context of your method
	 * `getTuplesFromIndex`, it is used to specify the column name and value that you want to search for
	 * in the index.
	 * @return This method returns a Hashtable containing tuples and their corresponding page names that
	 * match the given indexed column value in the specified table.
	 */
	private Hashtable<Tuple, String> getTuplesFromIndex(String strTableName, String strIndexedColumn, Hashtable<String,Object> htblColNameValue) throws IOException, ClassNotFoundException, DBAppException{

		String strIndexName = metadata.getIndexName(strTableName, strIndexedColumn);
		System.out.println(strIndexName);

		bplustree bplustreeIndex = htblIndices.get(strIndexName);
		
		String strIndexedColumnType = metadata.getColumnType(strTableName, strIndexedColumn);

	
		
		
		Comparable cmpIndexedColumnValue = (Comparable) htblColNameValue.get(strIndexedColumn);
			

		
		Hashtable<Tuple, String> htblTuples = new Hashtable<>(); // Tuple, page name

		ArrayList<Pair> arrayTuples = bplustreeIndex.search(cmpIndexedColumnValue, cmpIndexedColumnValue); // first index is the page name, second index is the tuple object

		for (Pair pair : arrayTuples){
			String strPageName = (String) pair.getValue();
			Tuple tupleTuple = (Tuple) pair.getKey();
			
			htblTuples.put(tupleTuple, strPageName);
			
		}
		

		return htblTuples;

		

	}

	/**
	 * The function checks if a tuple satisfies a set of conditions specified in a Hashtable by comparing
	 * column values.
	 * 
	 * @param tupleTuple The `tupleTuple` parameter is an object of type `Tuple`, which likely represents
	 * a tuple or a row in a database table. It contains column values that can be accessed using the
	 * `getColumnValue` method.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that stores column names as
	 * keys and their corresponding values as values.
	 * @return The method `tupleSatisfiesAndedConditions` returns a boolean value - `true` if the tuple
	 * satisfies all the conditions specified in the `htblColNameValue` hashtable, and `false` otherwise.
	 */
	
	private boolean tupleSatisfiesAndedConditions(Tuple tupleTuple, Hashtable<String, Object> htblColNameValue) throws DBAppException {

		for(String strCol : htblColNameValue.keySet()){
			String strTupleColumnValue = tupleTuple.getColumnValue(strCol).toString();
			String strHtbleColumnValue = htblColNameValue.get(strCol).toString();
			if(!strTupleColumnValue.equals(strHtbleColumnValue)) return false;

		}

		return true;
		
	}



	/**
	 * The function `deleteNonClusterinKeyWithoutIndex` deletes tuples from a table without an index based
	 * on the provided column values using linear search.
	 * 
	 * @param strTableName The `strTableName` parameter in the `deleteNonClusterinKeyWithoutIndex` method
	 * represents the name of the table from which you want to delete tuples.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that contains the column
	 * names as keys and the corresponding values to be matched as values. The method iterates through all
	 * pages in a table, then iterates through all tuples in each page. For each tuple, it checks if the
	 * values in
	 */
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

	/**
	 * The `deleteWithoutClusteringKey` function in Java deletes tuples from a table without a clustering
	 * key, handling indexed and non-indexed columns.
	 * 
	 * @param strTableName The `strTableName` parameter in the `deleteWithoutClusteringKey` method refers
	 * to the name of the table from which you want to delete records. This method is used to delete
	 * records from a table without a clustering key.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that contains the column
	 * names and their corresponding values that you want to use as criteria for deleting records from a
	 * table. Each entry in the Hashtable represents a column name-value pair that specifies the condition
	 * for deleting records.
	 */
	private void deleteWithoutClusteringKey(String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException, ClassNotFoundException, DBAppException{
		
		Hashtable<Tuple, String> htblTuples = null;
		for(String col : htblColNameValue.keySet()){
			if(metadata.isColumnIndexed(strTableName, col)){
				System.out.println("Indexed column: " + col + " found.");
				if(htblTuples == null){
					htblTuples = getTuplesFromIndex(strTableName, col, htblColNameValue);
				}else{
					// get the intersection hashmap entries
					
					htblTuples = getHashMapIntersection(htblTuples, getTuplesFromIndex(strTableName, col, htblColNameValue));
					
				}
				
			}
		}

		if (htblTuples != null){
			// TODO: delete from index
			for(Tuple tuple : htblTuples.keySet()){
				String strPageName = htblTuples.get(tuple);
				
				Page pagePage = Page.deserialize(strPageName + ".class");
				
				
				pagePage.deleteTuple(tuple);
				pagePage.serialize(pagePage.getPageName());
				if(pagePage.getSize() == 0){
					pagePage.deletePage();
					table.removePage(pagePage.getPageName());
					table.serialize(table.getTableName());
				}
			}
			return;
		}

		deleteNonClusterinKeyWithoutIndex(strTableName, htblColNameValue);

	}

	


	
	/**
	 * The function `getHashMapIntersection` takes two Hashtables of type Tuple and String, and returns a
	 * new Hashtable containing the intersection of key-value pairs present in both input Hashtables.
	 * 
	 * @param htbl1 The `htbl1` parameter is a Hashtable that maps Tuple objects to String values. It
	 * contains key-value pairs where the key is a Tuple object and the value is a String.
	 * @param htbl2 The `htbl2` parameter in the `getHashMapIntersection` method is a Hashtable that
	 * contains key-value pairs where the key is of type `Tuple` and the value is of type `String`. This
	 * method compares the keys of `htbl1` and `htbl2` and creates
	 * @return The method `getHashMapIntersection` returns a Hashtable containing the intersection of keys
	 * between the two input Hashtables `htbl1` and `htbl2`.
	 */
	private Hashtable<Tuple, String> getHashMapIntersection(Hashtable<Tuple, String> htbl1, Hashtable<Tuple, String> htbl2) {
		Hashtable<Tuple, String> htblIntersection = new Hashtable<>();

		for(Tuple tuple : htbl1.keySet()){
			if(htbl2.containsKey(tuple)){
				htblIntersection.put(tuple, htbl2.get(tuple));
			}
		}

		return htblIntersection;
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
			System.out.println("here");
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

			// tree.displayTree();

			

			

			// String strTableName = "Student";
			Hashtable htblColNameValue = new Hashtable( );
			// htblColNameValue.put("ID", new Integer( 3 ));
			htblColNameValue.put("Name", new String("Ahmed00" ) );
			htblColNameValue.put("Age", new Double( 20 ) );
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