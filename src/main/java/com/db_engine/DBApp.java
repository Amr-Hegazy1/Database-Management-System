package com.db_engine;

/** * @author Wael Abouelsaadat */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.bplustree.*;

public class DBApp {

	private Metadata metadata;
	final int MAX_ROWS_COUNT_IN_PAGE;

	public DBApp() throws DBAppException {

		try {
			metadata = new Metadata();

			Properties prop = new Properties();
			String fileName = "/DBApp.config";

			InputStream s = DBApp.class.getResourceAsStream(fileName);

			prop.load(s);
			MAX_ROWS_COUNT_IN_PAGE = Integer.parseInt(prop.getProperty("MaximumRowsCountinPage"));

		} catch (FileNotFoundException ex) {
			throw new DBAppException("Config file not found");
		} catch (IOException ex) {
			throw new DBAppException("Error reading config file");
		}

	}

	// this does whatever initialization you would like
	// or leave it empty if there is no code you want to
	// execute at application startup
	public void init() {

		// TODO: LOAD INDICES
		// TODO: LOAD METADATA FILE

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
	 *                        the same name, or if it cannot create a folder for the
	 *                        table.
	 * 
	 */
	public void createTable(String strTableName,
			String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType) throws DBAppException {

		metadata.addTable(strTableName, strClusteringKeyColumn, htblColNameType);
		metadata.save();
		Table tblTable = new Table(strTableName);
		File fileTableFolder = new File("tables/" + strTableName);
		if (!fileTableFolder.exists()) {
			boolean boolSuccess = fileTableFolder.mkdirs();
			if (boolSuccess) {
				tblTable.serialize("tables//" + strTableName + "//" + strTableName + ".class");
			} else {
				throw new DBAppException("Can't create a Folder!");
			}
		} else {
			throw new DBAppException("Table already exists!");
		}
	}

	// following method creates a B+tree index
	/**
	 * @param strTableName The `strTableName` parameter represents the name of the
	 *                     table to which you
	 *                     want to create an index.
	 * @param strColName   The `strColName` parameter represents the name of the
	 *                     column for which you
	 *                     want to create an index in the specified table.
	 * @param strIndexName The strIndexName parameter represents the name of the
	 *                     index which we will add
	 *                     First we check whether the table exists and if not we
	 *                     throw an appropriate exeption
	 *                     Then we check whether the column we want to index exists
	 *                     and if not we throw an appropriate exeption
	 *                     Thirdly we check whether the index already exisits then
	 *                     we throw the exeption accordingly
	 *                     If no exeptions are thrown we can now create the index by
	 *                     firstadding the index data to the metadata file
	 *                     Then creating The index b+tree , then getting the table
	 *                     object by deserializing the table name.
	 *                     Using this table object we get the vector of pages that
	 *                     encapsulate this table as strings
	 *                     Like the table we deserializ the pages to get the page
	 *                     object and get every tuplein the page and extract the
	 *                     values of
	 *                     the column needed to be indexed from tuples hence
	 *                     inserting them in the tree.Lastly we serialize the tree
	 *                     to store it
	 *                     in the memory and put the tree in the htbIndex so it can
	 *                     be accessed if needed anytime in the other code segments
	 */

	public void createIndex(String strTableName, String strColName, String strIndexName) throws DBAppException {
		if (!metadata.checkTableName(strTableName)) {
			throw new DBAppException("This table does not exist");

		} else if (!metadata.checkColumnName(strTableName, strColName)) {
			throw new DBAppException("This column does not exist");
		} else if (!(metadata.getIndexName(strTableName, strColName).equals("null"))) {
			throw new DBAppException("An index for this column already exists");

		} else {

			metadata.addIndex(strTableName, strColName, "B+Tree", strIndexName);

			Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
			Vector<String> vecPages = tblTable.getPages();
			BPlusTree bplsBplustree;

			if (metadata.getColumnType(strTableName, strColName).equals("java.lang.Integer")) {
				bplsBplustree = new BPlusTree<Integer, Tuple>();
			} else if (metadata.getColumnType(strTableName, strColName).equals("java.lang.Double")) {
				bplsBplustree = new BPlusTree<Double, Tuple>();
			} else {
				bplsBplustree = new BPlusTree<String, Tuple>();
			}

			// Loop through the column values
			for (String pgPage_name : vecPages) {
				Page pgPage = Page.deserialize("tables/" + strTableName + "/" + pgPage_name + ".class");
				Vector<Tuple> vecTuples = pgPage.getTuples();
				for (Tuple tplTuple : vecTuples) {
					if (tplTuple.getColumnValue(strColName) instanceof Integer) {
						int key = (int) tplTuple.getColumnValue(strColName);
						bplsBplustree.insert(key, tplTuple);
					} else if (tplTuple.getColumnValue(strColName) instanceof String) {
						String key = (String) tplTuple.getColumnValue(strColName);
						bplsBplustree.insert(key, tplTuple);
					} else {
						double key = (double) tplTuple.getColumnValue(strColName);
						bplsBplustree.insert(key, tplTuple);
					}

				}
			}

			// TODO: Consider creating an indices folder inside each table folder

			bplsBplustree.serialize("tables/" + strTableName + "/" + strIndexName + ".class");

			metadata.save();
			

		}
	}

	// following method inserts one row only.
	// htblColNameValue must include a value for the primary key
	public void insertIntoTable(String strTableName,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		HashSet<String> hsIndexedColumns = new HashSet<String>();// hashset to store indexed column names
		Tuple tupleNewTuple = new Tuple(); // New Tuple to be filled with input data and added to Table.
		String strClustKeyName = ""; // Name of Clustering Key Column
		int intColCounter = 0;
		// TODO

		// Checking Table Existence, Correct Col Names, Correct Col Data Types
		if (!metadata.checkTableName(strTableName)) { // table existence check
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

						if (!((metadata.getIndexType(strTableName, entry.getKey())).equals("null"))) {
							hsIndexedColumns.add(entry.getKey());
						}
					} else if (entry.getValue() == null) {// other columns cannot be null check
						throw new DBAppException("Value Cannot Be Null, Please Enter Appropriate Value");
					}
					// checking which columns are indexed
					// storing indexed column names in a hashset to adjust their indicies later
					else if (!((metadata.getIndexType(strTableName, entry.getKey())).equals("null"))) {
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

		// Inserting w/o any Indicies

		int intMaxSize = MAX_ROWS_COUNT_IN_PAGE; // Max allowed Tuples per page.
		int intpageNum = tblTable.getNumofPages(); // Only used to check if Table has existing pages or not
		Page pgInsertPage; // page i will insert in

		if (intpageNum != 0) {// if table already has pages, binary search on correct page to insert into
			pgInsertPage = getPageByClusteringKey(strTableName, strClustKeyName,
					htblColNameValue.get(strClustKeyName), tblTable); // get the page that the tuple will be inserted
																		// into
			if (pgInsertPage == null) {// if tuple is out of range, check first and last page

				String strFirstPage = vecPages.get(0); // get first page name
				String strLastPage = vecPages.get(vecPages.size() - 1); // get last page name

				// get 1st and last page objects
				Page pgFirstPage = Page.deserialize("tables/" + strTableName + "/" + strFirstPage + ".class");
				Page pgLastPage = Page.deserialize("tables/" + strTableName + "/" + strLastPage + ".class");

				Tuple tupleFirstTuple = pgFirstPage.getTupleWithIndex(0); // get first tuple in 1st page(smallest clust
																			// key)

				Comparable inputClustKey = (Comparable) htblColNameValue.get(strClustKeyName); // clust key in my insert
																								// tuple

				if (inputClustKey.compareTo((Comparable) tupleFirstTuple // if my insert key is smaller than first page
																			// key, insert in index 0
						.getColumnValue(strClustKeyName)) < 0) {
					pgFirstPage.addTuple(0, tupleNewTuple);
					tupleNewTuple.setPageName(pgFirstPage.getPageName());

					if (pgFirstPage.getSize() > intMaxSize) { // check for overflow and handle it
						handleInsertOverflow(tblTable, pgFirstPage, intMaxSize, strClustKeyName);
					} else {
						pgFirstPage.serialize("tables/" + strTableName + "/" + pgFirstPage.getPageName() + ".class");
					}

				} else {// only option left is that its bigger than last key in last page, so insert in
						// last index in last page
					pgLastPage.addTuple(pgLastPage.getSize(), tupleNewTuple);
					tupleNewTuple.setPageName(pgLastPage.getPageName());

					if (pgLastPage.getSize() > intMaxSize) {// check for overflow and handle it
						handleInsertOverflow(tblTable, pgLastPage, intMaxSize, strClustKeyName);
					} else {
						pgLastPage.serialize("tables/" + strTableName + "/" + pgLastPage.getPageName() + ".class");
					}
				}
			}

			else { // if tuple is in range of existing pages, insert and check for overflow
				int index = pgInsertPage.binarySearchTuples(strClustKeyName,
						htblColNameValue.get(strClustKeyName)); // binary search to find the correct index to insert in
																// page
				Tuple tuplecheckTuple = pgInsertPage.getTuple(index); // Tuple that i check clust key duplication with

				// make sure new clustering key is not a duplicate
				if (tuplecheckTuple.getColumnValue(strClustKeyName) == htblColNameValue.get(strClustKeyName)
						|| tuplecheckTuple.getColumnValue(strClustKeyName)
								.equals(htblColNameValue.get(strClustKeyName))) {
					throw new DBAppException("Primary Key Already Exists");
				}

				tupleNewTuple.setPageName(pgInsertPage.getPageName());
				pgInsertPage.addTuple(index, tupleNewTuple);

				if (pgInsertPage.getSize() > intMaxSize) { // check for overflow
					handleInsertOverflow(tblTable, pgInsertPage, intMaxSize, strClustKeyName); // handle overflow
				} else { // if no overflow, serialize page
					pgInsertPage.serialize("tables/" + strTableName + "/" + pgInsertPage.getPageName() + ".class");
				}
			}

		} else {// if table has no pages to begin with

			String strPageName = tblTable.addPage(); // addPage() adds page to the table Vector and assigns it an
														// automated Page Name & returns that name
			Page newPage = new Page(strPageName);
			tupleNewTuple.setPageName(strPageName);
			newPage.addTuple(0, tupleNewTuple);

			newPage.serialize("tables/" + strTableName + "/" + strPageName + ".class");
		}

		// Adjusting Indicies (If any)
		
		if (hsIndexedColumns.size() > 0) {
			for (String strColName : hsIndexedColumns) {
				String strIndexName = metadata.getIndexName(strTableName, strColName); // getting index name
				BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class"); // getting
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

	public void handleInsertOverflow(Table tblTable, Page pgFirstOverflowPage, int intMaxRowsPerPage,
			String strClustKeyName)
			throws DBAppException {

		Vector<String> vecPages = tblTable.getPages(); // Vector of Page names in table
		Page overflowPage = pgFirstOverflowPage; // page that caused first overflow
		int intStartIndex = vecPages.indexOf(overflowPage.getPageName()); // get index of first overflowed page in the
																			// vecPages Vector
		Tuple tupleLastTuple = overflowPage.removeLastTuple(); // last tuple in overflow page
		overflowPage.serialize("tables/" + tblTable.getTableName() + "/" + overflowPage.getPageName() + ".class"); // serialize
																													// the
																													// overflow
																													// page

		// Loop through the rest of the pages to handle the overflow
		for (int i = intStartIndex + 1; i < vecPages.size(); i++) {
			Page page = Page.deserialize("tables/" + tblTable.getTableName() + "/" + vecPages.get(i) + ".class"); // get
																													// page
			int index = page.binarySearchTuples(strClustKeyName,
					tupleLastTuple.getColumnValue(strClustKeyName)); // get correct index to insert tuple in page
			Tuple tuplecheckTuple = page.getTuple(index); // tuple used to check for duplicate clustering key

			// make sure new clustering key is not a duplicate
			if (tuplecheckTuple.getColumnValue(strClustKeyName) == tupleLastTuple.getColumnValue(strClustKeyName)
					|| tuplecheckTuple.getColumnValue(strClustKeyName)
							.equals(tupleLastTuple.getColumnValue(strClustKeyName))) {
				throw new DBAppException("Primary Key Already Exists");
			}
			// if unique clustering key, insert in page and adjust page name in tuple object
			tupleLastTuple.setPageName(page.getPageName());
			page.addTuple(index, tupleLastTuple);

			// check for overflow
			if (page.getSize() > intMaxRowsPerPage) { // if overflowed, remove last tuple and serialize current page
				tupleLastTuple = page.removeLastTuple();
				page.serialize("tables/" + tblTable.getTableName() + "/" + page.getPageName() + ".class");
			} else { // if no overflow, serialize page and break the loop
				page.serialize("tables/" + tblTable.getTableName() + "/" + page.getPageName() + ".class");
				return;
			}
		}

		// if we reach this point, this means that we overflowed in all pages until no
		// pages were left, therefore a new page is needed
		String newPageName = tblTable.addPage();
		Page newPage = new Page(newPageName);
		newPage.addTuple(0, tupleLastTuple);
		newPage.serialize("tables/" + tblTable.getTableName() + "/" + newPageName + ".class");

	}

	// following method updates one row only
	// htblColNameValue holds the key and new value
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName, String strClusteringKeyValue, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		
		if (!metadata.checkTableName(strTableName)) 
			throw new DBAppException("Table does not exist");

		if (strClusteringKeyValue == null) {
			throw new DBAppException("Clustering key value is null");
		}
		if (htblColNameValue.isEmpty()) {
			throw new DBAppException("no column to update");
		}

		for (String columnName : htblColNameValue.keySet()) {

			String columnTypeMetadata = metadata.getColumnType(strTableName, columnName);
			Object columnValue = htblColNameValue.get(columnName);
			String strdatatype = columnValue.getClass().getName();

			if (!columnTypeMetadata.equals(strdatatype)) {
				throw new DBAppException("Datatypes do not match for the column");
			}
		}

		String strClusteringKey = metadata.getClusteringkey(strTableName);

		String clusteringKeyType = metadata.getColumnType(strTableName, strClusteringKey);
		Comparable cmpClusteringKeyValue;

		if (clusteringKeyType.equals("java.lang.Integer")) {
			cmpClusteringKeyValue = Integer.parseInt(strClusteringKeyValue);
		} else if (clusteringKeyType.equals("java.lang.Double")) {
			cmpClusteringKeyValue = Double.parseDouble(strClusteringKeyValue);
		} else
		cmpClusteringKeyValue = strClusteringKeyValue.toString();

		Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
		String key = metadata.getClusteringkey(strTableName);

		Page page = getPageByClusteringKey(strTableName, key, cmpClusteringKeyValue, tblTable);
		int tupleIndex = page.searchTuplesByClusteringKey(key, cmpClusteringKeyValue);

		if (page != null) {

			Vector<Tuple> tuples = page.getVecTuples();
			Tuple tuple = tuples.get(tupleIndex);
			Tuple tupleOriginalTuple = tuple.clone();
			
			//Hashtable<String, Hashtable<String, String>> htblMetadata = metadata.getTableMetadata(strTableName);
			List<String> metaCol = metadata.getColumnNames(strTableName);
			HashSet<String> indexedonly = new HashSet<String>();
			for(String name : metaCol){
				String indexName = metadata.getIndexName(strTableName, name);
				if (!(indexName.equals("null"))){
					indexedonly.add(name);
				}

			}
			for (String columnName : indexedonly) {
					Comparable temp = (Comparable)tuple.getColumnValue(columnName);
					if(htblColNameValue.containsKey(columnName)){
					temp = (Comparable)tuple.getColumnValue(columnName);
					Comparable columnValue = (Comparable)htblColNameValue.get(columnName);
					tuple.setColumnValue(columnName, columnValue);
				
					String indexName = metadata.getIndexName(strTableName, columnName);
					BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + indexName + ".class");
					bptTree.remove(temp, (Comparable) tupleOriginalTuple);
					bptTree.insert(columnValue, (Comparable) tuple);
					bptTree.serialize("tables/" + strTableName + "/" + indexName + ".class");	
					}
					else{
					String indexName = metadata.getIndexName(strTableName, columnName);
					BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + indexName + ".class");
					bptTree.remove(temp, (Comparable) tupleOriginalTuple);
					bptTree.insert(temp, (Comparable) tuple);
					bptTree.serialize("tables/" + strTableName + "/" + indexName + ".class");
					}
					

				
			}
			//System.out.println(tuples);
		}
		
			page.serialize("tables/" + strTableName + "/" + page.getPageName() + ".class");
			tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".class");
			
	}
	

				/*if(metadata.isColumnIndexed(strTableName, columnName) && !(htblMetadata.containsKey(columnName))){
					String indexName = metadata.getIndexName(strTableName, columnName);
					if (!indexName.equals("null")) {
						BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + indexName + ".class");
						bptTree.remove((Comparable) temp, (Comparable) tupleOriginalTuple);
						bptTree.insert((Comparable) columnValue, (Comparable) tuple);
						bptTree.serialize("tables/" + strTableName + "/" + indexName + ".class");
					}
				}
				//bptTree.serialize("tables/" + strTableName + "/" + indexName + ".class");	
				//System.out.print(bptTree.query((Comparable)columnName)+columnName);


				List<String> columnNames = metadata.getColumnNames(strTableName);
				
				if(metadata.isColumnIndexed(strTableName, columnName) && !(htblMetadata.containsKey(columnName))){
					String indexName = metadata.getIndexName(strTableName, columnName);
					if (!indexName.equals("null")) {
						BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + indexName + ".class");
						bptTree.remove((Comparable) temp, (Comparable) tupleOriginalTuple);
						bptTree.insert((Comparable) columnValue, (Comparable) tuple);
						bptTree.serialize("tables/" + strTableName + "/" + indexName + ".class");
					}
				}*/
				
				
			//tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".class");
			//page.serialize("tables/" + strTableName + "/" + page.getPageName() + ".class");
			//System.out.print(tuples);
		
			
	
	

	/**
	 * The function `getPageByClusteringKey` searches for a specific page in a table
	 * based on a given
	 * clustering key value.
	 * 
	 * @param strTableName          It seems like you were about to provide some
	 *                              information about the parameters,
	 *                              but the message got cut off. Could you please
	 *                              provide more details about the parameters
	 *                              `strTableName`, `strClusteringKey`, and
	 *                              `objClusteringKeyValue` so that I can assist you
	 *                              further
	 *                              with the `getPageByCl
	 * @param strClusteringKey      It seems like you were about to provide some
	 *                              information about the
	 *                              parameter `strClusteringKey` but the message got
	 *                              cut off. How can I assist you further with this
	 *                              parameter?
	 * @param objClusteringKeyValue It seems like the method
	 *                              `getPageByClusteringKey` is trying to find a
	 *                              specific page in a table based on a clustering
	 *                              key value. The method uses binary search to
	 *                              locate
	 *                              the page efficiently.
	 * @return The method `getPageByClusteringKey` is returning a `Page` object. If
	 *         the clustering key
	 *         value is found within the specified range in the page, then that page
	 *         is returned. If the key is
	 *         not found within the range, the method will continue searching
	 *         through the pages until it either
	 *         finds the key or exhausts all pages, in which case it will return
	 *         `null`.
	 */
	private Page getPageByClusteringKey(String strTableName, String strClusteringKey, Object objClusteringKeyValue,
			Table table) throws DBAppException {

		int intTableSize = table.getNumberOfPages();
		int intTopPageIndex = 0;
		int intBottomPageIndex = intTableSize - 1;

		while (intTopPageIndex <= intBottomPageIndex) {

			int intMiddlePageIndex = intTopPageIndex + (intBottomPageIndex - intTopPageIndex) / 2;

			String strMiddlePage = table.getPageAtIndex(intMiddlePageIndex);

			Page pageMiddlePage = Page.deserialize("tables/" + strTableName + "/" + strMiddlePage + ".class");

			int intMiddlePageSize = pageMiddlePage.getSize();

			Tuple tupleMiddlePageTopTuple = pageMiddlePage.getTupleWithIndex(0);

			Tuple tupleMiddlePageBottomTuple = pageMiddlePage.getTupleWithIndex(intMiddlePageSize - 1);

			// convert the object to comparable to compare it with the clustering key

			Comparable cmpClusteringKeyValue = (Comparable) objClusteringKeyValue;

			// check if the clustering key is in the page by checking if the value is
			// between the top and bottom tuple
			if (cmpClusteringKeyValue.compareTo(tupleMiddlePageTopTuple.getColumnValue(strClusteringKey)) >= 0
					&& cmpClusteringKeyValue
							.compareTo(tupleMiddlePageBottomTuple.getColumnValue(strClusteringKey)) <= 0) {
				return pageMiddlePage;
			} else if (cmpClusteringKeyValue.compareTo(tupleMiddlePageTopTuple.getColumnValue(strClusteringKey)) < 0) {
				intBottomPageIndex = intMiddlePageIndex - 1;
			} else {
				intTopPageIndex = intMiddlePageIndex + 1;
			}
		}

		return null;

	}

	/**
	 * This Java function deletes a tuple with a specified clustering key from a
	 * table and handles page
	 * deletion if necessary.
	 * 
	 * @param strTableName     The `strTableName` parameter is a `String`
	 *                         representing the name of the table
	 *                         from which you want to delete a record.
	 * @param strClusteringKey The `strClusteringKey` parameter in the
	 *                         `deleteWithClusteringKey` method
	 *                         refers to the name of the clustering key column in
	 *                         the table from which you want to delete a
	 *                         record.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that
	 *                         contains the column
	 *                         names as keys and their corresponding values as
	 *                         values. This Hashtable represents the values of the
	 *                         columns for the record that you want to delete.
	 * @param table
	 */
	private void deleteWithClusteringKey(String strTableName, String strClusteringKey,
			Hashtable<String, Object> htblColNameValue, Table table) throws DBAppException {

		Object objClusteringKeyValue = htblColNameValue.get(strClusteringKey);
		Page pagePage = getPageByClusteringKey(strTableName, strClusteringKey, objClusteringKeyValue, table);

		// if the page is null, then the tuple does not exist
		if (pagePage == null) {
			return;
		}

		int intTupleIndex = pagePage.searchTuplesByClusteringKey(strClusteringKey, objClusteringKeyValue);
		Tuple tupleTuple = pagePage.getTupleWithIndex(intTupleIndex);
		if (!tupleSatisfiesAndedConditions(tupleTuple, htblColNameValue)) {
			return;
		}
		pagePage.deleteTupleWithIndex(intTupleIndex);

		pagePage.serialize("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");
		if (pagePage.getSize() == 0) {
			Page.deletePage("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");
			table.removePage(pagePage.getPageName());
			table.serialize("tables/" + strTableName + "/" + strTableName + ".class");
		}

		// delete from all relevant indices

		for (String col : htblColNameValue.keySet()) {
			if (metadata.isColumnIndexed(strTableName, col)) {
				String strIndexName = metadata.getIndexName(strTableName, col);
				BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");
				bptTree.remove((Comparable) tupleTuple.getColumnValue(col), tupleTuple);
				bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class");
			}
		}

	}

	/**
	 * This Java function retrieves tuples from an index based on a specified table
	 * name, indexed column,
	 * and column values.
	 * 
	 * @param strTableName     The `strTableName` parameter represents the name of
	 *                         the table from which you
	 *                         want to retrieve tuples based on the indexed column
	 *                         value.
	 * @param strIndexedColumn The `strIndexedColumn` parameter in the
	 *                         `getTuplesFromIndex` method refers
	 *                         to the name of the column that is indexed in the B+
	 *                         tree for a specific table. This column is used
	 *                         to efficiently retrieve tuples based on their values
	 *                         in that column.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that
	 *                         contains the column
	 *                         names as keys and their corresponding values as
	 *                         values. In the context of your method
	 *                         `getTuplesFromIndex`, it is used to specify the
	 *                         column name and value that you want to search for
	 *                         in the index.
	 * @return This method returns a Hashtable containing tuples and their
	 *         corresponding page names that
	 *         match the given indexed column value in the specified table.
	 */
	private Hashtable<Tuple, String> getTuplesFromIndex(String strTableName, String strIndexedColumn,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		String strIndexName = metadata.getIndexName(strTableName, strIndexedColumn);

		BPlusTree bplustreeIndex = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");

		String strIndexedColumnType = metadata.getColumnType(strTableName, strIndexedColumn);

		Comparable cmpIndexedColumnValue = (Comparable) htblColNameValue.get(strIndexedColumn);

		Hashtable<Tuple, String> htblTuples = new Hashtable<>(); // Tuple, page name

		List<Tuple> arrayTuples = bplustreeIndex.query(cmpIndexedColumnValue); // first
																				// index is
																				// the page
																				// name,
																				// second
																				// index is
																				// the tuple
																				// object

		for (Tuple tupleTuple : arrayTuples) {
			String strPageName = tupleTuple.getPageName();

			htblTuples.put(tupleTuple, strPageName);

		}

		return htblTuples;

	}

	/**
	 * The function checks if a tuple satisfies a set of conditions specified in a
	 * Hashtable by comparing
	 * column values.
	 * 
	 * @param tupleTuple       The `tupleTuple` parameter is an object of type
	 *                         `Tuple`, which likely represents
	 *                         a tuple or a row in a database table. It contains
	 *                         column values that can be accessed using the
	 *                         `getColumnValue` method.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that
	 *                         stores column names as
	 *                         keys and their corresponding values as values.
	 * @return The method `tupleSatisfiesAndedConditions` returns a boolean value -
	 *         `true` if the tuple
	 *         satisfies all the conditions specified in the `htblColNameValue`
	 *         hashtable, and `false` otherwise.
	 */

	private boolean tupleSatisfiesAndedConditions(Tuple tupleTuple, Hashtable<String, Object> htblColNameValue)
			throws DBAppException {

		for (String strCol : htblColNameValue.keySet()) {
			String strTupleColumnValue = tupleTuple.getColumnValue(strCol).toString();
			String strHtbleColumnValue = htblColNameValue.get(strCol).toString();
			if (!strTupleColumnValue.equals(strHtbleColumnValue))
				return false;

		}

		return true;

	}

	/**
	 * The function `deleteNonClusterinKeyWithoutIndex` deletes tuples from a table
	 * without an index based
	 * on the provided column values using linear search.
	 * 
	 * @param strTableName     The `strTableName` parameter in the
	 *                         `deleteNonClusterinKeyWithoutIndex` method
	 *                         represents the name of the table from which you want
	 *                         to delete tuples.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that
	 *                         contains the column
	 *                         names as keys and the corresponding values to be
	 *                         matched as values. The method iterates through all
	 *                         pages in a table, then iterates through all tuples in
	 *                         each page. For each tuple, it checks if the
	 *                         values in
	 */
	private void deleteNonClusterinKeyWithoutIndex(String strTableName, Hashtable<String, Object> htblColNameValue,
			Table table) throws DBAppException {

		// Linear search

		int intNumberOfPages = table.getNumberOfPages();
		int intPageIndex = 0;

		while(intPageIndex < intNumberOfPages) {
			String pageCurrentPageName = table.getPageAtIndex(intPageIndex);
			Page pageCurrentPage = Page.deserialize("tables/" + strTableName + "/" + pageCurrentPageName + ".class");

			// Linearly search every page

			int intCurrentPageSize = pageCurrentPage.getSize();
			int intCurrentPageIndex = 0;

			while(intCurrentPageIndex < intCurrentPageSize) {
				Tuple tupleCurrentTuple = pageCurrentPage.getTupleWithIndex(intCurrentPageIndex);
				boolean boolToBeDeletedTuple = false;
				// AND all columns
				for (String strCol : htblColNameValue.keySet()) {
					boolToBeDeletedTuple = true;
					if (!tupleCurrentTuple.getColumnValue(strCol).equals(htblColNameValue.get(strCol))) {
						boolToBeDeletedTuple = false;
						break;
					}
				}

				if (boolToBeDeletedTuple) {
					pageCurrentPage.deleteTupleWithIndex(intCurrentPageIndex);
				}else{
					intCurrentPageIndex++;
				}
				intCurrentPageSize = pageCurrentPage.getSize();

			}

			if (pageCurrentPage.getSize() == 0) {
				Page.deletePage("tables/" + strTableName + "/" + pageCurrentPageName + ".class");
				table.removePage(pageCurrentPage.getPageName());
				table.serialize("tables/" + strTableName + "/" + strTableName + ".class");
			} else {
				// TODO: Don't do this step if no tuples were deleted
				pageCurrentPage.serialize("tables/" + strTableName + "/" + pageCurrentPageName + ".class");
				intPageIndex++;
			}
			intNumberOfPages = table.getNumberOfPages();

		}

	}

	/**
	 * The `deleteWithoutClusteringKey` function in Java deletes tuples from a table
	 * without a clustering
	 * key, handling indexed and non-indexed columns.
	 * 
	 * @param strTableName     The `strTableName` parameter in the
	 *                         `deleteWithoutClusteringKey` method refers
	 *                         to the name of the table from which you want to
	 *                         delete records. This method is used to delete
	 *                         records from a table without a clustering key.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that
	 *                         contains the column
	 *                         names and their corresponding values that you want to
	 *                         use as criteria for deleting records from a
	 *                         table. Each entry in the Hashtable represents a
	 *                         column name-value pair that specifies the condition
	 *                         for deleting records.
	 * @param table
	 */
	private void deleteWithoutClusteringKey(String strTableName, Hashtable<String, Object> htblColNameValue,
			Table table) throws DBAppException {

		Hashtable<Tuple, String> htblTuples = null;
		for (String col : htblColNameValue.keySet()) {
			if (metadata.isColumnIndexed(strTableName, col)) {
				System.out.println("Indexed column: " + col + " found.");
				if (htblTuples == null) {
					htblTuples = getTuplesFromIndex(strTableName, col, htblColNameValue);
				} else {
					// get the intersection hashmap entries

					htblTuples = getHashMapIntersection(htblTuples,
							getTuplesFromIndex(strTableName, col, htblColNameValue));

				}

			}
		}

		if (htblTuples != null) {

			for (Tuple tuple : htblTuples.keySet()) {
				String strPageName = htblTuples.get(tuple);

				Page pagePage = Page.deserialize("tables/" + strTableName + "/" + strPageName + ".class");

				pagePage.deleteTuple(tuple);
				pagePage.serialize(pagePage.getPageName());
				if (pagePage.getSize() == 0) {
					Page.deletePage("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");
					table.removePage(pagePage.getPageName());
					table.serialize("tables/" + strTableName + "/" + strTableName + ".class");
				}

				// delete from all relevant indices

				for (String col : htblColNameValue.keySet()) {
					if (metadata.isColumnIndexed(strTableName, col)) {
						String strIndexName = metadata.getIndexName(strTableName, col);
						BPlusTree bptTree = BPlusTree
								.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");
						bptTree.remove((Comparable) tuple.getColumnValue(col), tuple);
						bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class");
					}
				}
			}
			return;
		}

		deleteNonClusterinKeyWithoutIndex(strTableName, htblColNameValue, table);

	}

	/**
	 * The function `getHashMapIntersection` takes two Hashtables of type Tuple and
	 * String, and returns a
	 * new Hashtable containing the intersection of key-value pairs present in both
	 * input Hashtables.
	 * 
	 * @param htbl1 The `htbl1` parameter is a Hashtable that maps Tuple objects to
	 *              String values. It
	 *              contains key-value pairs where the key is a Tuple object and the
	 *              value is a String.
	 * @param htbl2 The `htbl2` parameter in the `getHashMapIntersection` method is
	 *              a Hashtable that
	 *              contains key-value pairs where the key is of type `Tuple` and
	 *              the value is of type `String`. This
	 *              method compares the keys of `htbl1` and `htbl2` and creates
	 * @return The method `getHashMapIntersection` returns a Hashtable containing
	 *         the intersection of keys
	 *         between the two input Hashtables `htbl1` and `htbl2`.
	 */
	private Hashtable<Tuple, String> getHashMapIntersection(Hashtable<Tuple, String> htbl1,
			Hashtable<Tuple, String> htbl2) {
		Hashtable<Tuple, String> htblIntersection = new Hashtable<>();

		for (Tuple tuple : htbl1.keySet()) {
			if (htbl2.containsKey(tuple)) {
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
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		if (!metadata.checkTableName(strTableName)) {
			throw new DBAppException("Table does not exist");
		}

		if (htblColNameValue.isEmpty()) {
			throw new DBAppException("No columns to delete");
		}

		for (String strCol : htblColNameValue.keySet()) {
			if (!metadata.checkColumnName(strTableName, strCol)) {
				throw new DBAppException("Column does not exist");
			}
		}

		Table table = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
		String strClusteringKey = metadata.getClusteringKey(strTableName);

		if (htblColNameValue.containsKey(strClusteringKey)) {
			deleteWithClusteringKey(strTableName, strClusteringKey, htblColNameValue, table);
		} else {

			deleteWithoutClusteringKey(strTableName, htblColNameValue, table);
		}

	}

	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
			String[] strarrOperators) throws DBAppException, ClassNotFoundException, IOException{
				if(arrSQLTerms.length==0){
					throw new DBAppException("Nothing to query on");
				}
				if(arrSQLTerms.length!= strarrOperators.length+1){
					throw new DBAppException("something wrong in the input");
				}
				HashSet<String> hmkilobatata = new HashSet<>();
				hmkilobatata.add("=");hmkilobatata.add("!=");hmkilobatata.add(">=");hmkilobatata.add("<=");hmkilobatata.add(">");;hmkilobatata.add("<");
				for(int i=0;i<arrSQLTerms.length;i++){
				if(metadata.checkTableName(arrSQLTerms[i]._strTableName)&&arrSQLTerms[0]._strTableName.equals(arrSQLTerms[i]._strTableName)){
					if(metadata.checkColumnName(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName)){
						String strType= metadata.getColumnType(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName);
						if( hmkilobatata.contains(arrSQLTerms[i]._strOperator)){	
							if(!strType.equals(arrSQLTerms[i]._objValue.getClass().getName())){
								throw new DBAppException("Datatype doesn't match");
							}
						}
						else 
						throw new DBAppException("This Operator isn't supported");
				} else
					throw new DBAppException("Column doesn't exist");
			} else
				throw new DBAppException("Table doesn't exist or inconsistent");
			}
			boolean indexhelp=true;
			for(int i=0;i<strarrOperators.length;i++){
				if((!strarrOperators[i].toLowerCase().equals("and"))&&(!strarrOperators[i].toLowerCase().equals("or"))&&(!strarrOperators[i].toLowerCase().equals("xor")))
				throw new DBAppException("Undefined operator is being used");
				if(!(strarrOperators[i].toLowerCase().equals("and")))
				indexhelp=false;
			}
				// el hashes at end of if statements are stand by
				if(strarrOperators.length==0){
					HashSet <Tuple> tut=getTuple(arrSQLTerms[0]);
					System.out.println(tut.size());
					return tut.iterator();
			}
			if(indexhelp){
				boolean firstornot=true;
				HashSet<String> hmpage= new HashSet<>();
				Vector<SQLTerm> indexsql= new Vector<>();
				for(int i=0; i<arrSQLTerms.length;i++){
					if(!metadata.getIndexType(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName).equals("null")){
						if(firstornot){
							hmpage=getPage(arrSQLTerms[i]);
							firstornot= false;
						}   
						else{
							hmpage=and2bp(hmpage,getPage(arrSQLTerms[i]));
						}
					indexsql.add(arrSQLTerms[i]);
					} 
				}
				HashSet<Tuple> hmtup= new HashSet<>();
				hmtup=pagestotuples(hmpage, indexsql);
				for(int i=0; i<arrSQLTerms.length;i++){
					if(metadata.getIndexType(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName).equals("null")){
						hmtup=and1bp(hmtup,arrSQLTerms[i]);
					}
				}
				return hmtup.iterator();
			}
			
			Stack<Object> stack = new Stack<>();
			Stack<String> stack2 = new Stack<>();
			Vector <Object> vec = new Vector<>();
			vec.add(arrSQLTerms[0]);
			for (int i=0;i<strarrOperators.length;i++) {
				if (stack2.isEmpty()) {
					stack2.push(strarrOperators[i]);
					vec.add(arrSQLTerms[i+1]);
				} else if (precedence(strarrOperators[i].toLowerCase()) >= precedence(stack2.peek().toLowerCase())) {
					stack2.push(strarrOperators[i]);
					vec.add(arrSQLTerms[i+1]);
					}
				else{	
					while (!stack2.isEmpty() && precedence(strarrOperators[i].toLowerCase()) < precedence(stack2.peek().toLowerCase())) {
						vec.add(stack2.pop());
					}
					stack2.push(strarrOperators[i]);
					vec.add(arrSQLTerms[i+1]);
				}
			}
			while(!stack2.isEmpty()){
				 vec.add(stack2.pop());
			}
			
			for(int i=0;i<vec.size();i++){
				if(vec.get(i) instanceof SQLTerm){
					stack.push((SQLTerm) vec.get(i));
				}
				else{
					HashSet <Tuple> tm= null;
					HashSet <Tuple> tm2= null;
					if(stack.peek() instanceof SQLTerm){
						 tm=getTuple((SQLTerm) stack.pop());
						if(stack.peek() instanceof SQLTerm){
							tm2=getTuple((SQLTerm) stack.pop());
						}
						else{
							tm2= (HashSet<Tuple>) stack.pop();
						}
					}
					else{
						tm= (HashSet<Tuple>) stack.pop();
						if(stack.peek() instanceof SQLTerm){
							tm2=getTuple((SQLTerm) stack.pop());
						}
						else{
							tm2= (HashSet<Tuple>) stack.pop();
						}
					}
					if(((String) vec.get(i)).toLowerCase().equals("and")){
						stack.add(and2hs(tm, tm2));
					}
					else if(((String) vec.get(i)).toLowerCase().equals("or")){
						stack.add(or2hs(tm, tm2));
					}
					else{
						stack.add(xor2hs(tm, tm2));
					}
		
				}
			}
			HashSet<Tuple> tm= (HashSet<Tuple>) stack.pop();
			
			return tm.iterator();
			}
		
		public static int findIndex(SQLTerm arr[], SQLTerm t) 
			{ 
		  

				if (arr == null) { 
					return -1; 
				} 
		  
				
				int len = arr.length; 
				int i = 0; 
		  
 
				while (i < len) { 
		  

					if (arr[i]._strTableName.equals(t._strTableName) && arr[i]._strColumnName.equals(t._strColumnName)&& arr[i]._strOperator.equals(t._strOperator)) { 
					   if(t._objValue instanceof Integer){
					   Integer te = (Integer) t._objValue;
						if(te==((Integer)arr[i]._objValue))
						return i; 
						else
						i++;
				} else if (t._objValue instanceof Double) {
					Double te = (Double) t._objValue;
					if (te == ((Double) arr[i]._objValue))
						return i;
					else
						i++;
				} else {
					String te = (String) t._objValue;
					if (te.compareTo(((String) arr[i]._objValue)) == 0)
						return i;
					else
						i++;
					   }
					   }
					else { 
						i = i + 1; 
					} 
				} 
				return -1; 
			} 
		private static HashSet<Tuple> and2hs(HashSet<Tuple> hs1,HashSet<Tuple> hs2){
			System.out.println(hs1.size());
			System.out.println(hs2.size());
			for(Tuple tm: hs1){
				if(! hs2.contains(tm)){
					hs1.remove(tm);
				}
			}
			return hs1;
		}
		private static HashSet<String> and2bp(HashSet<String> hs1,HashSet<String> hs2){
			for(String tm: hs1){
				if(! hs2.contains(tm)){
					hs1.remove(tm);
				}
			}
			return hs1;
		}
		private static HashSet<Tuple> or2hs(HashSet<Tuple> hs1,HashSet<Tuple> hs2){
			hs1.addAll(hs2);
			return hs1;
		}
		private static HashSet<Tuple> xor2hs(HashSet<Tuple> hs1,HashSet<Tuple> hs2){
			if(hs1.size()==0){
				return hs2;
			}
			if(hs2.size()==0){
				return hs1;
			}
			for(Tuple tm: hs1){
				if(hs2.contains(tm)){
					hs2.remove(tm);
				}else{
					hs2.add(tm);
				}
			}
			return hs2;
		}
		private static HashSet<Tuple> and1bp(HashSet<Tuple> hs1, SQLTerm sql) throws DBAppException{
			for(Tuple tm:hs1){
				if(sql._strOperator.equals("=")){
					if(sql._objValue instanceof Integer){
						Integer te = (Integer) sql._objValue;
						if(!((Integer)tm.getColumnValue(sql._strColumnName)).equals(te)){
							hs1.remove(tm);
						}
					}
					else if(sql._objValue instanceof Double){
						Double te = (Double)sql._objValue;
						if(!((Double)tm.getColumnValue(sql._strColumnName)).equals(te)){
							hs1.remove(tm);
						}
					}
					else{
						String te = (String) sql._objValue;
						if(((String)tm.getColumnValue(sql._strColumnName)).compareTo(te)!=0){
							hs1.remove(tm);
						}
					}
				}
				else if(sql._strOperator.equals(">")){
					if(sql._objValue instanceof Integer){
						Integer te = (Integer) sql._objValue;
						if(((Integer)tm.getColumnValue(sql._strColumnName))<=(te)){
							hs1.remove(tm);
						}
					}
					else if(sql._objValue instanceof Double){
						Double te = (Double)sql._objValue;
						if(((Double)tm.getColumnValue(sql._strColumnName))<=(te)){
							hs1.remove(tm);
						}
					}
					else{
						String te = (String) sql._objValue;
						if(((String)tm.getColumnValue(sql._strColumnName)).compareTo(te)<=0){
							hs1.remove(tm);
						}
					}
				} 
				else if(sql._strOperator.equals(">=")){
					if(sql._objValue instanceof Integer){
						Integer te = (Integer) sql._objValue;
						if(((Integer)tm.getColumnValue(sql._strColumnName))<(te)){
							hs1.remove(tm);
						}
					}
					else if(sql._objValue instanceof Double){
						Double te = (Double)sql._objValue;
						if(((Double)tm.getColumnValue(sql._strColumnName))<(te)){
							hs1.remove(tm);
						}
					}
					else{
						String te = (String) sql._objValue;
						if(((String)tm.getColumnValue(sql._strColumnName)).compareTo(te)<0){
							hs1.remove(tm);
						}
					}
				}
				else if (sql._strOperator.equals("<")){
					if(sql._objValue instanceof Integer){
						Integer te = (Integer) sql._objValue;
						if(((Integer)tm.getColumnValue(sql._strColumnName))>=(te)){
							hs1.remove(tm);
						}
					}
					else if(sql._objValue instanceof Double){
						Double te = (Double)sql._objValue;
						if(((Double)tm.getColumnValue(sql._strColumnName))>=(te)){
							hs1.remove(tm);
						}
					}
					else{
						String te = (String) sql._objValue;
						if(((String)tm.getColumnValue(sql._strColumnName)).compareTo(te)>=0){
							hs1.remove(tm);
						}
					}
				}
				else if (sql._strOperator.equals("<=")){
					if(sql._objValue instanceof Integer){
						Integer te = (Integer) sql._objValue;
						if(((Integer)tm.getColumnValue(sql._strColumnName))>(te)){
							hs1.remove(tm);
						}
					}
					else if(sql._objValue instanceof Double){
						Double te = (Double)sql._objValue;
						if(((Double)tm.getColumnValue(sql._strColumnName))>(te)){
							hs1.remove(tm);
						}
					}
					else{
						String te = (String) sql._objValue;
						if(((String)tm.getColumnValue(sql._strColumnName)).compareTo(te)>0){
							hs1.remove(tm);
						}
					}
				}
				else{
					if(sql._objValue instanceof Integer){
						Integer te = (Integer) sql._objValue;
						if(((Integer)tm.getColumnValue(sql._strColumnName)).equals(te)){
							hs1.remove(tm);
						}
					}
					else if(sql._objValue instanceof Double){
						Double te = (Double)sql._objValue;
						if(((Double)tm.getColumnValue(sql._strColumnName)).equals(te)){
							hs1.remove(tm);
						}
					}
					else{
						String te = (String) sql._objValue;
						if(((String)tm.getColumnValue(sql._strColumnName)).compareTo(te)==0){
							hs1.remove(tm);
						}
					}
				} 
			}
			return hs1;
		}
		private static HashSet<Tuple> pagestotuples(HashSet<String> hs1, Vector<SQLTerm> vec) throws DBAppException{
			HashSet<Tuple> hstup=new HashSet<>();
			for(String tm:hs1){
				HashSet<Tuple> temp=new HashSet<>();
				Page page= Page.deserialize("tables/"+vec.get(0)._strTableName+"/"+tm+".class");
				Vector<Tuple> listtmp = page.getTuples();
				for(Tuple tup:listtmp){
					temp.add(tup);
				} 
				for(SQLTerm sql:vec){
					temp=and1bp(temp,sql);
				}
				hstup.addAll(temp);
			}
			return hstup;
		}
		private HashSet<Tuple> getTuple(SQLTerm sql) throws ClassNotFoundException, IOException, DBAppException{
			HashSet<Tuple> hstup = new HashSet<>();
			if(sql._strOperator.equals("=")){
				if(! metadata.getIndexType(sql._strTableName,sql._strColumnName).equals("null")){
					BPlusTree bptmp = BPlusTree.deserialize("tables/" +sql._strTableName+"/"+  metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
					Vector<String> listtmp = (Vector<String>) bptmp.rangeQuery((Comparable)sql._objValue, (Comparable)sql._objValue);
					HashSet <String> hspage = new HashSet<>();
					for(String tup:listtmp){
						hspage.add(tup);
					} 
					Vector<SQLTerm> vec = new Vector<>();
					vec.add(sql);
					return pagestotuples(hspage, vec);
				}
				else{

					Table table = Table.deserialize("tables/"+sql._strTableName+"/"+sql._strTableName+".class");
					if( ! metadata.isClusteringKey(sql._strTableName, sql._strColumnName)){
						 hstup= table.eqsearch(sql._strColumnName, sql._objValue); 
						return hstup;
					}
					else{
						hstup= table.cleqsearch(sql._strColumnName, sql._objValue);
						return hstup;
					}
				}
				
			}
			else if(sql._strOperator.equals(">")){
				if(! metadata.getIndexType(sql._strTableName,sql._strColumnName).equals("null")){
					
					BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName+"/"+metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
					Vector<String> listtmp;
					if(sql._objValue instanceof Integer){
						listtmp = (Vector<String>) bptmp.rangeQuery((Comparable)sql._objValue,Integer.MAX_VALUE );
					}	
					else if(sql._objValue instanceof Double){
						listtmp = (Vector<String>)bptmp.rangeQuery((Comparable)sql._objValue,Double.MAX_VALUE );	
					}
					else{
						listtmp =(Vector<String>) bptmp.rangeQuery((Comparable)sql._objValue,"{" );
					}
					HashSet <String> hspage = new HashSet<>();
					for(String tup:listtmp){
						hspage.add(tup);
					} 
					Vector<SQLTerm> vec = new Vector<>();
					vec.add(sql);
					return pagestotuples(hspage, vec); 
				}
				else{
			
					Table table = Table.deserialize("tables/"+sql._strTableName+"/"+sql._strTableName+".class");
					if( ! metadata.isClusteringKey(sql._strTableName, sql._strColumnName)){
						hstup= table.greaterthannc(sql._strColumnName, sql._objValue);
						return hstup; 
					}
					else{
						hstup= table.greaterthan(sql._strColumnName, sql._objValue);
						return hstup;
					}
				}	
			}else if(sql._strOperator.equals(">=")){
			if(! metadata.getIndexType(sql._strTableName,sql._strColumnName).equals("null")){
				
				BPlusTree bptmp = BPlusTree.deserialize("tables/" +sql._strTableName+"/"+ metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
				Vector<String> listtmp;
				if(sql._objValue instanceof Integer){
					listtmp = (Vector<String>)bptmp.rangeQuery((Comparable)sql._objValue,Integer.MAX_VALUE );
				}	
				else if(sql._objValue instanceof Double){
					listtmp = (Vector<String>)bptmp.rangeQuery((Comparable)sql._objValue,Double.MAX_VALUE );
				}
				else{
					listtmp = (Vector<String>)bptmp.rangeQuery((Comparable)sql._objValue,"{" ); 
				}
				HashSet <String> hspage = new HashSet<>();
				for(String tup:listtmp){
					hspage.add(tup);
				} 
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			}
			else{
				Table table = Table.deserialize("tables/"+sql._strTableName+"/"+sql._strTableName+".class");
				if( ! metadata.isClusteringKey(sql._strTableName, sql._strColumnName)){
					hstup= table.greaterthaneqnc(sql._strColumnName, sql._objValue); 
					return hstup;
				} else {
					hstup = table.greaterthaneq(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}	
		}else if(sql._strOperator.equals("<")){
			if(! metadata.getIndexType(sql._strTableName,sql._strColumnName).equals("null")){
				
				BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName+"/"+metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
				Vector<String>  listtmp;
				if(sql._objValue instanceof Integer){
					listtmp = (Vector<String>)bptmp.rangeQuery(Integer.MIN_VALUE,(Comparable)sql._objValue ); 
				}	
				else if(sql._objValue instanceof Double){
					listtmp = (Vector<String>)bptmp.rangeQuery(Double.MIN_VALUE,(Comparable)sql._objValue );	
				}
				else{
					listtmp = (Vector<String>)bptmp.rangeQuery("",(Comparable)sql._objValue );
				}
				HashSet <String> hspage = new HashSet<>();
				for(String tup:listtmp){
					hspage.add(tup);
				} 
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			}
			else{
				Table table = Table.deserialize("tables/"+sql._strTableName+"/"+sql._strTableName+".class");
				if( ! metadata.isClusteringKey(sql._strTableName, sql._strColumnName)){
					hstup= table.lesserthannc(sql._strColumnName, sql._objValue); 
					return hstup;
				} else {
					hstup = table.lesserthan(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}	
		}else if(sql._strOperator.equals("<=")){
			if(! metadata.getIndexType(sql._strTableName,sql._strColumnName).equals("null")){
				
				BPlusTree bptmp = BPlusTree.deserialize("tables/" +sql._strTableName+"/"+ metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
				Vector<String> listtmp;
				if(sql._objValue instanceof Integer){
					listtmp = (Vector<String>)bptmp.rangeQuery(Integer.MIN_VALUE,(Comparable)sql._objValue ); 
				}	
				else if(sql._objValue instanceof Double){
					listtmp = (Vector<String>)bptmp.rangeQuery(Double.MIN_VALUE,(Comparable)sql._objValue );	
				}
				else{
					listtmp = (Vector<String>)bptmp.rangeQuery("",(Comparable)sql._objValue );
				}
				HashSet <String> hspage = new HashSet<>();
				for(String tup:listtmp){
					hspage.add(tup);
				} 
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			}
			else{
				Table table = Table.deserialize("tables/"+sql._strTableName+"/"+sql._strTableName+".class");
				if( ! metadata.isClusteringKey(sql._strTableName, sql._strColumnName)){
					hstup= table.lesserthannceq(sql._strColumnName, sql._objValue); 
					return hstup;
				} else {
					hstup = table.lesserthaneq(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}	
		} else{   
			Table table = Table.deserialize("tables/"+sql._strTableName+"/"+sql._strTableName+".class");
			hstup= table.noteqsearch(sql._strColumnName, sql._objValue);
			return hstup;
		}

	}

	private HashSet<String> getPage(SQLTerm sql) throws ClassNotFoundException, IOException, DBAppException{
		HashSet<String> hstup = new HashSet<>();
		if(sql._strOperator.equals("=")){
				BPlusTree bptmp = BPlusTree.deserialize("tables/" +sql._strTableName+"/"+  metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
				Vector <Pair> listtmp = (Vector<Pair>) bptmp.rangeQuery((Comparable)sql._objValue, (Comparable)sql._objValue);
				for(Pair tup:listtmp){
					hstup.add((String)tup.getValue());
				} 
				return hstup;
		}
		
		else if(sql._strOperator.equals(">=")||sql._strOperator.equals(">")){
			BPlusTree bptmp = BPlusTree.deserialize("tables/" +sql._strTableName+"/"+ metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
			Vector <Pair> listtmp;
			if(sql._objValue instanceof Integer){
				listtmp = (Vector<Pair>) bptmp.rangeQuery((Comparable)sql._objValue,Integer.MAX_VALUE );
				for(Pair tup:listtmp){
					hstup.add((String)tup.getValue());
				} 
			}	
			else if(sql._objValue instanceof Double){
				listtmp = (Vector<Pair>)bptmp.rangeQuery((Comparable)sql._objValue,Double.MAX_VALUE );
				for(Pair tup:listtmp){
					hstup.add((String)tup.getValue());
				} 	
			}
			else{
				listtmp = (Vector<Pair>)bptmp.rangeQuery((Comparable)sql._objValue,"{" );
				for(Pair tup:listtmp){
					hstup.add((String)tup.getValue());
				} 
			}
			return hstup;
		
		
	}
	else {
			BPlusTree bptmp = BPlusTree.deserialize("tables/" +sql._strTableName+"/"+ metadata.getIndexName(sql._strTableName,sql._strColumnName)+ ".class");
			Vector<Pair> listtmp;
			if(sql._objValue instanceof Integer){
				listtmp = (Vector<Pair>)bptmp.rangeQuery(Integer.MIN_VALUE,(Comparable)sql._objValue );
				for(Pair tup:listtmp){
					hstup.add((String)tup.getValue());
				} 
			}	
			else if(sql._objValue instanceof Double){
				listtmp = (Vector<Pair>)bptmp.rangeQuery(Double.MIN_VALUE,(Comparable)sql._objValue );
				for(Pair tup:listtmp){
					hstup.add((String)tup.getValue());
				} 	
			}
			else{
				listtmp = (Vector<Pair>)bptmp.rangeQuery("",(Comparable)sql._objValue );
				for(Pair tup:listtmp){
					hstup.add((String)tup.getValue());
				}  
			}
			return hstup;
		
	}
		

}
	private static int precedence(String operator) {
		switch (operator) {

			case "and":
				return 3;
			case "or":
				return 2;
			case "xor":
				return 1;
			default:
				return -1;
		}
	}

	private static void cleanUp() throws IOException{
        try{
            // delete tables directory

            String tablesPath = "tables/";

            

            Path dir = Paths.get(tablesPath); //path to the directory  
            Files
                .walk(dir) // Traverse the file tree in depth-first order
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        
                        Files.delete(path);  //delete each file or directory
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }catch(Exception e){
        
        }

        try{
            
            // delete Indicies directory

            String indiciesPath = "Indicies/";

            Path dir2 = Paths.get(indiciesPath); //path to the directory
            Files
                .walk(dir2) // Traverse the file tree in depth-first order
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                       
                        Files.delete(path);  //delete each file or directory
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }catch(Exception e){
        
        }
        try{
            // delete metadata.csv

            File metadata = new File("metadata.csv");
            metadata.delete();
        }catch(Exception e){
            
        }



    }

	public static void main(String[] args) throws DBAppException, IOException {

		try{
            DBApp dbApp = new DBApp();

            dbApp.init();

            String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert 20 rows

            for(int i = 0; i < 20; i++){
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }
			// select all rows

            SQLTerm[] arrSQLTerms = new SQLTerm[4];
            String[] strarrOperators = new String[3];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = strTableName;
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "=";
            arrSQLTerms[0]._objValue = 1;
			arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = strTableName;
            arrSQLTerms[1]._strColumnName = "id";
            arrSQLTerms[1]._strOperator = "=";
            arrSQLTerms[1]._objValue = 2;
			arrSQLTerms[2] = new SQLTerm();
            arrSQLTerms[2]._strTableName = strTableName;
            arrSQLTerms[2]._strColumnName = "id";
            arrSQLTerms[2]._strOperator = "=";
            arrSQLTerms[2]._objValue = 3;
			arrSQLTerms[3] = new SQLTerm();
            arrSQLTerms[3]._strTableName = strTableName;
            arrSQLTerms[3]._strColumnName = "id";
            arrSQLTerms[3]._strOperator = "=";
            arrSQLTerms[3]._objValue = 4;
			strarrOperators[0]="xor";
			strarrOperators[1]="or";
			strarrOperators[2]="and";
			//System.out.println(dbApp.selectFromTable(arrSQLTerms, strarrOperators));
            Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
			
            // for(int i = 0; i < 20; i++){
            //     if(i == 5){
            //         assert iterator.hasNext();
            //         Tuple tuple = (Tuple) iterator.next();
            //         assert tuple.getColumnValue("id").equals(5);
            //         assert tuple.getColumnValue("name").equals("Student5");
            //         assert tuple.getColumnValue("gpa").equals(3.0 + 5);
            //     }else{
            //         assert !iterator.hasNext();
            //     }
            // }
			while(iterator.hasNext()){
				Tuple tuple = (Tuple) iterator.next();
				System.out.println(tuple);
			}
		}catch(Exception e){
			e.printStackTrace();
            
        }finally{
        	cleanUp();
        }
	}
}
