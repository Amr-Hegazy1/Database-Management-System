
/** * @author Wael Abouelsaadat */


import java.util.Iterator;
import java.util.List;

import java.io.*;

import java.util.*;








public class DBApp  {
	
	private Metadata metadata;
	/*The hashtable htbIndex is used to store the indicies where the key of this hashtable is the index name
	and the value is the index itself*/
	private Hashtable<String, bplustree> htbIndex ;

	public DBApp( ) throws FileNotFoundException, IOException, ClassNotFoundException{
		metadata = new Metadata();
		htbIndex = new Hashtable<>();

	}

	// this does whatever initialization you would like
	// or leave it empty if there is no code you want to
	// execute at application startup
	public void init() {

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
	 * @param strTableName           The `strTableName` is the name of the table
	 *                               needed be created.
	 * 
	 * @param strClusteringKeyColumn The `strClusteringKeyColumn` is the name of the
	 *                               column
	 *                               that will be the primary key and the clustering
	 *                               column as well.
	 *                               The data type of that column will be passed in
	 *                               htblColNameType.
	 * 
	 * @param htblColNameValue       The `htblColNameValue` will have the column
	 *                               name as key and
	 *                               the data type as value.
	 * 
	 * @throws DBAppException The `DBAppException` will be thrown if there exists a
	 *                        table with
	 *                        the same name.
	 * 
	 * @throws IOExecption    The `IOExecption` will be thrown if the function fails
	 *                        to create the
	 *                        folder or fails to serialize the table.
	 */
	public void createTable(String strTableName,
			String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType) throws DBAppException, IOException {

		metadata.addTable(strTableName, strClusteringKeyColumn, htblColNameType);
		metadata.save();
		Table tblTable = new Table(strTableName);
		File fileTableFolder = new File("tables/" + strTableName);
		if (!fileTableFolder.exists()) {
			boolean boolSuccess = fileTableFolder.mkdir();
			if (boolSuccess) {
				tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".ser");
			} else {
				throw new IOException("Can't create a Folder!");
			}
		} else {
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
			Hashtable<String, Object> htblColNameValue) throws DBAppException, ClassNotFoundException, IOException {

		HashSet<String> hsIndexedColumns = new HashSet<String>();// hashset to store indexed column names
		Tuple tupleNewTuple = new Tuple(); // New Tuple to be filled with input data and added to Table.
		String strClustKeyName = ""; // Name of Clustering Key Column
		int intColCounter = 0;
		// TODO

		// Checking Table Existence, Correct Col Names, Correct Col Data Types
		if (metadata.checkTableName(strTableName)) { // table existence check
			throw new DBAppException("Table Doesnt Exist");
		} else {
			for (Map.Entry<String, Object> entry : htblColNameValue.entrySet()) {
				if (!metadata.checkColumnName(strTableName, entry.getKey())) { // column name existence check
					throw new DBAppException("Column Doesnt Exist");

				} else {
					String strCorrectDataType = metadata.getColumnType(strTableName, entry.getKey());
					String strActualDataType = entry.getValue().getClass().getName();
					intColCounter++;
					if (!strCorrectDataType.equals(strActualDataType)) { // data type check
						throw new DBAppException("Data Type Mismatch");
					}
					if (metadata.isClusteringKey(strTableName, entry.getKey())) {
						if (entry.getValue() == null) { // clustering key cannot be null check
							throw new DBAppException("Clustering Key Cannot Be Null");
						}
						strClustKeyName = entry.getKey();
						tupleNewTuple.setColumnValue(entry.getKey(), entry.getValue());
					} else if (entry.getValue() == null) {// other columns cannot be null check
						throw new DBAppException("Value Cannot Be Null, Please Enter Appropriate Value");
					}
					// chcecking which columns are indexed
					// storing indexed column names in a hashset to adjust their indicies later
					else if (!(metadata.getIndexType(strTableName, entry.getKey())).equals("null")) {
						hsIndexedColumns.add(entry.getKey());
						tupleNewTuple.setColumnValue(entry.getKey(), entry.getValue());
					} else {
						tupleNewTuple.setColumnValue(entry.getKey(), entry.getValue());
					}

				}

			}
		}
		List<String> listColNames = metadata.getColumnNames(strTableName); // fetching correct column names
		if (intColCounter != listColNames.size()) { // checking if all columns were included in insert
			throw new DBAppException("Column(s) Missing, Please Enter All Columns");
		}

		// Table object that i ill use to add the tuple to it
		Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
		Vector<String> vecPages = tblTable.getPageVector(); // Vector of Page names inside of Table object

		Properties prop = new Properties();
		String fileName = "DBApp.config";
		FileInputStream fis = new FileInputStream(fileName);
		prop.load(fis);

		// Inserting w/o any Indicies

		int intMaxSize = Integer.parseInt(prop.getProperty("MaximumRowsCountinPage")); // Max allowed Tuples per page.
		int intpageNum = tblTable.getNumofPages(); // Only used to check if Table has existing pages or not
		boolean boolMaxValueisPassed = false; // to check if page overflowed
		boolean boolSkipPage = false; // to check if page is in range of clustering key, if not skip it
		Tuple tupleLastTuple = new Tuple(); // tuple i remove incase of overflow
		String strFinalPageName = ""; // the name of the page that the tuple was inserted in

		if (intpageNum != 0) {// if table already has pages, traverse them while checking ranges of clust key
			for (String strPageName : vecPages) {
				boolSkipPage = false;
				boolMaxValueisPassed = false;
				Page page = Page.deserialize("tables/" + strTableName + "/" + strPageName + ".class");

				// checking if page has correct range for the clustering key
				Tuple firstTuple = page.getFirstTuple(); // tuple i use to check range(min)
				Tuple lastTuple = page.getLastTuple(); // tuple i use to check range(max)

				// checking range of clustering key(whether its int, double or string)
				if (firstTuple.getColumnValue(strClustKeyName) instanceof Integer) {
					int first = (int) firstTuple.getColumnValue(strClustKeyName);
					int last = (int) lastTuple.getColumnValue(strClustKeyName);
					int input = (int) htblColNameValue.get(strClustKeyName);
					if (input < first || input > last) {
						boolSkipPage = true;
					}
				} else if (firstTuple.getColumnValue(strClustKeyName) instanceof Double) {
					double first = (double) firstTuple.getColumnValue(strClustKeyName);
					double last = (double) lastTuple.getColumnValue(strClustKeyName);
					double input = (double) htblColNameValue.get(strClustKeyName);
					if (input < first || input > last) {
						boolSkipPage = true;
					}
				} else {
					String first = (String) firstTuple.getColumnValue(strClustKeyName);
					String last = (String) lastTuple.getColumnValue(strClustKeyName);
					String input = (String) htblColNameValue.get(strClustKeyName);
					if (input.compareTo(first) < 0 || input.compareTo(last) > 0) {
						boolSkipPage = true;
					}
				}

				if (!boolSkipPage) { // if page's Tuples are in range of my new tuple's clustering key, then i can
										// insert
					int index = page.binarySearchTuples(strClustKeyName,
							htblColNameValue.get(strClustKeyName));
					Tuple tuplecheckTuple = page.getTuple(index);

					// make sure new clustering key is not a duplicate
					if (tuplecheckTuple.getColumnValue(strClustKeyName) == htblColNameValue.get(strClustKeyName)
							|| tuplecheckTuple.getColumnValue(strClustKeyName)
									.equals(htblColNameValue.get(strClustKeyName))) {
						throw new DBAppException("Primary Key Already Exists");
					}
					page.addTuple(index, tupleNewTuple);
					if (page.getSize() > intMaxSize) {
						boolMaxValueisPassed = true;
						tupleLastTuple = page.removeLastTuple(); // getLastTuple() returns the last tuple and
																	// removes it
																	// from the page
						page.serialize("tables/" + strTableName + "/" + strPageName + ".class");
					} else {
						page.serialize("tables/" + strTableName + "/" + strPageName + ".class");
					}

					if (boolMaxValueisPassed) { // if overflow occurs, create a new page with the last tuple removed
												// from the previous page
						Page newPage = new Page();
						newPage.addTuple(0, tupleLastTuple);
						String strNewPageName = tblTable.addPage(); // addPage() adds page to the table Vector and
																	// assigns
																	// it an automated Page Name & returns that name

						if (!tupleLastTuple.equals(tupleNewTuple)) {// ensure which page name my tuple was inserted into
							strFinalPageName = strPageName;
							tupleNewTuple.setPageName(strFinalPageName);
						} else {
							strFinalPageName = strNewPageName;
							tupleNewTuple.setPageName(strFinalPageName);
						}
						newPage.serialize("tables/" + strTableName + "/" + strNewPageName + ".class");
					}
				}

			}

		} else {// if table has no pages to begin with
			Page newPage = new Page();
			String strPageName = tblTable.addPage(); // addPage() adds page to the table Vector and assigns it an
														// automated Page Name & returns that name
			tupleNewTuple.setPageName(strPageName);
			newPage.addTuple(0, tupleNewTuple);
			newPage.serialize("tables/" + strTableName + "/" + strPageName + ".class");
		}

		// Adjusting Indicies (If any)
		if (hsIndexedColumns.size() > 0) {
			for (String strColName : hsIndexedColumns) {
				String strIndexName = metadata.getIndexName(strTableName, strColName); // getting index name
				bplustree bptTree = bplustree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class"); // getting
																														// the
																														// tree
																														// object
				Comparable colValue = (Comparable) tupleNewTuple.getColumnValue(strColName); // cast column value to
																								// Comparable
				bptTree.insert(colValue, tupleNewTuple); // inserting col value(key) and tuple object(value) into bTree

				bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class"); // serializing the tree
			}
		}

		// Serializing Table
		tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".class");

	}

	// following method updates one row only
	// htblColNameValue holds the key and new value
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName,
			
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		throw new DBAppException("not implemented yet");
	}
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
			String clusteringKeyType = metadata.getColumnType(strTableName, strClusteringKeyValue);
			Object objclusteringKeyValue;
			
			if (clusteringKeyType.equals("java.lang.Integer")) {
				objclusteringKeyValue = Integer.parseInt(strClusteringKeyValue);}
			else if (clusteringKeyType.equals("java.lang.Double")) {
    			objclusteringKeyValue = Double.parseDouble(strClusteringKeyValue);}
			else 
			 	objclusteringKeyValue = strClusteringKeyValue.toString();

			Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
			String key=metadata.getClusteringkey(strTableName);

			Page page = getPageByClusteringKey(strTableName,key, objclusteringKeyValue,tblTable);
			int tupleIndex = page.searchTuplesByClusteringKey(key, objclusteringKeyValue);
	
			if (page != null) {
				
				Vector<Tuple> tuples = page.getVecTuples();
				Tuple tuple = tuples.get(tupleIndex);
				Hashtable<String, Hashtable<String, String>> htblMetadata = metadata.getTableMetadata(strTableName);
				
				for (String columnName : htblColNameValue.keySet()) {
					
					Object columnValue = htblColNameValue.get(columnName);
					tuple.setColumnValue(columnName, columnValue);
	
				
	 
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
					
				String strindexName = metadata.getIndexName(strTableName, columnName);
				bplustree bptTree = bplustree.deserialize("tables/" + strTableName + "/" + strindexName + ".class");
					
				  //Comparable ComVar=(Comparable) value; 
				  	Comparable compClusteringKeyValue = (Comparable) objclusteringKeyValue;
					bptTree.delete(compClusteringKeyValue);
					bptTree.insert(compClusteringKeyValue, htblColNameValue.get(objclusteringKeyValue));
					
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
	private Page getPageByClusteringKey(String strTableName, String strClusteringKey, Object objClusteringKeyValue, Table table) throws IOException, ClassNotFoundException, DBAppException{

        int intTableSize = table.getNumberOfPages();
        int intTopPageIndex = 0;
        int intBottomPageIndex = intTableSize - 1;

        while(intTopPageIndex <= intBottomPageIndex){

            int intMiddlePageIndex = intTopPageIndex + (intBottomPageIndex - intTopPageIndex) / 2;

            String strMiddlePage = table.getPageAtIndex(intMiddlePageIndex);

            

            Page pageMiddlePage = Page.deserialize("tables/" + strTableName + "/" + strMiddlePage + ".class");

            int intMiddlePageSize = pageMiddlePage.getSize();

            Tuple tupleMiddlePageTopTuple = pageMiddlePage.getTupleWithIndex(0);

            Tuple tupleMiddlePageBottomTuple = pageMiddlePage.getTupleWithIndex(intMiddlePageSize - 1);

			

			
			// convert the object to comparable to compare it with the clustering key
			
			Comparable cmpClusteringKeyValue = (Comparable) objClusteringKeyValue;

			// check if the clustering key is in the page by checking if the value is between the top and bottom tuple
			if(cmpClusteringKeyValue.compareTo(tupleMiddlePageTopTuple.getColumnValue(strClusteringKey)) >= 0 && cmpClusteringKeyValue.compareTo(tupleMiddlePageBottomTuple.getColumnValue(strClusteringKey)) <= 0){
				return pageMiddlePage;
			}else if(cmpClusteringKeyValue.compareTo(tupleMiddlePageTopTuple.getColumnValue(strClusteringKey)) < 0){
				intBottomPageIndex = intMiddlePageIndex - 1;
			}else{
				intTopPageIndex = intMiddlePageIndex + 1;
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
	 * @param table 
	 */
	private void deleteWithClusteringKey(String strTableName, String strClusteringKey, Hashtable<String,Object> htblColNameValue, Table table) throws IOException,ClassNotFoundException, DBAppException{
		
        Object objClusteringKeyValue = htblColNameValue.get(strClusteringKey);
		Page pagePage = getPageByClusteringKey(strTableName, strClusteringKey, objClusteringKeyValue, table);

		// if the page is null, then the tuple does not exist
		if(pagePage == null){
			return;
		}


        int intTupleIndex = pagePage.searchTuplesByClusteringKey(strClusteringKey, objClusteringKeyValue);
		Tuple tupleTuple = pagePage.getTupleWithIndex(intTupleIndex);
		if(!tupleSatisfiesAndedConditions(tupleTuple, htblColNameValue)){
			return;
		}
		pagePage.deleteTupleWithIndex(intTupleIndex);
		pagePage.serialize(pagePage.getPageName());
		if(pagePage.getSize() == 0){
			pagePage.deletePage();
			table.removePage(pagePage.getPageName());
			table.serialize(table.getTableName());
		}

		// delete from all relevant indices

		for(String col : htblColNameValue.keySet()){
			if(metadata.isColumnIndexed(strTableName, col)){
				String strIndexName = metadata.getIndexName(strTableName, col);
				bplustree bptTree = bplustree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");
				bptTree.delete((Comparable) tupleTuple.getColumnValue(col));
				bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class");
			}
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

		bplustree bplustreeIndex = htbIndex.get(strIndexName);
		
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
	private void deleteNonClusterinKeyWithoutIndex(String strTableName, Hashtable<String,Object> htblColNameValue, Table table) throws IOException, ClassNotFoundException, DBAppException{

		// Linear search

		int intNumberOfPages = table.getNumberOfPages();

		for(int intPageIndex = 0; intPageIndex < intNumberOfPages; intPageIndex++){
			String pageCurrentPageName = table.getPageAtIndex(intPageIndex);
			Page pageCurrentPage = Page.deserialize("tables/" + strTableName + "/" + pageCurrentPageName + ".class");
			
			
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

			if(pageCurrentPage.getSize() == 0){
				pageCurrentPage.deletePage();
				table.removePage(pageCurrentPage.getPageName());
				table.serialize(table.getTableName());
			}else{
				// TODO: Don't do this step if no tuples were deleted
				pageCurrentPage.serialize(strTableName + "/" + pageCurrentPageName + ".class");
			}

			



			
			

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
	 * @param table 
	 */
	private void deleteWithoutClusteringKey(String strTableName, Hashtable<String,Object> htblColNameValue, Table table) throws IOException, ClassNotFoundException, DBAppException{
		
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
				
				Page pagePage = Page.deserialize("tables/" + strTableName + "/" + strPageName + ".class");
				
				
				pagePage.deleteTuple(tuple);
				pagePage.serialize(pagePage.getPageName());
				if(pagePage.getSize() == 0){
					pagePage.deletePage();
					table.removePage(pagePage.getPageName());
					table.serialize(table.getTableName());
				}

				// delete from all relevant indices

				for(String col : htblColNameValue.keySet()){
					if(metadata.isColumnIndexed(strTableName, col)){
						String strIndexName = metadata.getIndexName(strTableName, col);
						bplustree bptTree = bplustree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");
						bptTree.delete((Comparable) tuple.getColumnValue(col));
						bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class");
					}
				}
			}
			return;
		}

		deleteNonClusterinKeyWithoutIndex(strTableName, htblColNameValue, table);

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

		if (!metadata.checkTableName(strTableName)){
			throw new DBAppException("Table does not exist");
		}

		if(htblColNameValue.isEmpty()){
			throw new DBAppException("No columns to delete");
		}

		for(String strCol : htblColNameValue.keySet()){
			if(!metadata.checkColumnName(strTableName, strCol)){
				throw new DBAppException("Column does not exist");
			}
		}
		
		Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".ser");
		String strClusteringKey = metadata.getClusteringKey(strTableName);

		if(htblColNameValue.containsKey(strClusteringKey)){
			deleteWithClusteringKey(strTableName, strClusteringKey, htblColNameValue, table);
		}else{
			
			deleteWithoutClusteringKey(strTableName, htblColNameValue, table);
		}
		
		



	}

	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
			String[] strarrOperators) throws DBAppException {

		return null;
	}

	
	public static void main( String[] args ){
	

	try{

			DBApp dbApp = new DBApp( );

			dbApp.init( );

			Hashtable<String,String> htblColNameType = new Hashtable( );
			htblColNameType.put("id", "java.lang.Integer");
			htblColNameType.put("name", "java.lang.String");
			htblColNameType.put("gpa", "java.lang.double");

			// dbApp.createTable("Student", "id", htblColNameType );

			Hashtable<String,Object> htblColNameValue = new Hashtable( );

			htblColNameValue.put("id", new Integer( 0 ));
			// htblColNameValue.put("name", new String("Ahmed Noor" ) );
			// htblColNameValue.put("gpa", new Double( 0.95 ) );


			dbApp.deleteFromTable("Student", htblColNameValue);

			

			

			
			
			 

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



		}
		catch(Exception exp){

			exp.printStackTrace( );

		}
	}
}
