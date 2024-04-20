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
import com.grammar.Main;
import com.grammar.main2;

public class DBApp {

	private Metadata metadata;
	final int MAX_ROWS_COUNT_IN_PAGE;

	public DBApp() throws DBAppException {

		try {

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

	
	/**
	 * The `init()` function initializes a `Metadata` object within a try-catch block to handle
	 * `DBAppException`.
	 */
	public void init() {

		try {
			metadata = new Metadata();

		} catch (DBAppException e) {
			e.printStackTrace();
		}

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
	 * @param htblColNameType        The `htblColNameType` will have the column
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

		boolean boolValidTypes = true;
		Vector<String> vecValidTypes = new Vector<>(
				Arrays.asList("java.lang.Integer", "java.lang.Double", "java.lang.String"));
		for (String strColumnName : htblColNameType.keySet()) {
			boolValidTypes &= vecValidTypes.contains(htblColNameType.get(strColumnName));
		}
		if (!boolValidTypes) {
			throw new DBAppException("Unsupported Data Types!");
		}

		if (!htblColNameType.containsKey(strClusteringKeyColumn)) {
			throw new DBAppException("Clustering Key Column not found in Column Types!");
		}

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

			if (strIndexName.equals(strTableName))
				strIndexName += "Index";

			for (Pair pair : metadata.getIndexedColumns(strTableName)) {

				if (pair.getValue().equals(strIndexName)) {
					throw new DBAppException("An index with this name already exists");
				}
			}

			String strClusteringKeyColName = metadata.getClusteringkey(strTableName);
			if (strIndexName.equals(strTableName))
				strIndexName += "Index";

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

					Comparable key = (Comparable) tplTuple.getColumnValue(strColName);
					Comparable cmpClusteringKeyValue = (Comparable) tplTuple.getColumnValue(strClusteringKeyColName);
					Pair pair = new Pair(cmpClusteringKeyValue, pgPage_name);
					bplsBplustree.insert(key, pair);

				}
			}

			// TODO: Consider creating an indices folder inside each table folder

			bplsBplustree.serialize("tables/" + strTableName + "/" + strIndexName + ".class");

			metadata.save();

		}
	}

	
	/**
	 * The `insertIntoTable` function inserts a new tuple into a table, handling clustering key, column
	 * existence, data type, indexing, and overflow scenarios.
	 * 
	 * @param strTableName The `strTableName` parameter in the `insertIntoTable` method represents the
	 * name of the table into which you want to insert a new tuple. It is a `String` variable that should
	 * contain the name of the target table where the insertion will take place.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that contains the column
	 * names as keys and their corresponding values as values. This Hashtable represents the data that you
	 * want to insert into the specified table. Each key in the Hashtable is a column name in the table,
	 * and the corresponding value is the
	 */
	public void insertIntoTable(String strTableName,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		HashSet<String> hsIndexedColumns = new HashSet<>();// hashset to store indexed column names
		Tuple tupleNewTuple = new Tuple(); // New Tuple to be filled with input data and added to Table.
		String strClustKeyName = ""; // Name of Clustering Key Column
		int intColCounter = 0;
		boolean overflowOccured = false;
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
			pgInsertPage = Page.getPageByClusteringKey(strTableName,
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

				Tuple tupleLastTuple = pgLastPage.getTupleWithIndex(pgLastPage.getSize() - 1); // get last tuple in
																								// 1st
																								// page(largest
																								// clust
																								// key

				Comparable inputClustKey = (Comparable) htblColNameValue.get(strClustKeyName); // clust key in my insert
																								// tuple

				if (inputClustKey.compareTo((Comparable) tupleFirstTuple // if my insert key is smaller than first page
																			// key, insert in index 0
						.getColumnValue(strClustKeyName)) < 0) {

					pgFirstPage.addTuple(0, tupleNewTuple);
					tblTable.setMin(pgFirstPage.getPageName(),
							(Comparable) tupleNewTuple.getColumnValue(strClustKeyName));
					tupleNewTuple.setPageName(pgFirstPage.getPageName());

					if (pgFirstPage.getSize() > intMaxSize) { // check for overflow and handle it
						handleInsertOverflow(tblTable, pgFirstPage, intMaxSize, strClustKeyName, hsIndexedColumns,
								tupleNewTuple);
						overflowOccured = true;
					} else {

						pgFirstPage.serialize("tables/" + strTableName + "/" + pgFirstPage.getPageName() + ".class");

					}

				} else if (inputClustKey.compareTo((Comparable) tupleLastTuple
						.getColumnValue(strClustKeyName)) > 0) {// only option left is that its bigger than last key in
																// last page, so insert in
					// last index in last page
					pgLastPage.addTuple(pgLastPage.getSize(), tupleNewTuple);
					tblTable.setMax(pgLastPage.getPageName(),
							(Comparable) tupleNewTuple.getColumnValue(strClustKeyName));
					tupleNewTuple.setPageName(pgLastPage.getPageName());

					if (pgLastPage.getSize() > intMaxSize) {// check for overflow and handle it
						handleInsertOverflow(tblTable, pgLastPage, intMaxSize, strClustKeyName, hsIndexedColumns,
								tupleNewTuple);
						overflowOccured = true;
					} else {

						pgLastPage.serialize("tables/" + strTableName + "/" + pgLastPage.getPageName() + ".class");

					}
				} else {

					int pageIndex = tblTable.getPageIndex(inputClustKey);
					pgInsertPage = Page
							.deserialize("tables/" + strTableName + "/" + vecPages.get(pageIndex) + ".class");
					Comparable currMax = tblTable.getMax(pgInsertPage.getPageName());
					Vector<Tuple> vecInsertPageVector = pgInsertPage.getTuples();
					int insertIndex = vecInsertPageVector.indexOf(currMax);
					insertIndex++;
					pgInsertPage.addTuple(insertIndex, tupleNewTuple);
					tblTable.setMin(pgInsertPage.getPageName(),
							(Comparable) pgInsertPage.getTuples().get(0).getColumnValue(strClustKeyName));
					tblTable.setMax(pgLastPage.getPageName(),
							(Comparable) pgInsertPage.getTuples().get(pgInsertPage.getSize()-1)
									.getColumnValue(strClustKeyName));
					if (pgInsertPage.getSize() > intMaxSize) {
						handleInsertOverflow(tblTable, pgInsertPage, intMaxSize, strClustKeyName, hsIndexedColumns,
								tupleNewTuple);
						overflowOccured = true;
					} else {
						pgInsertPage.serialize("tables/" + strTableName + "/" + pgInsertPage.getPageName() + ".class");
					}
				}
			}

			else { // if tuple is in range of existing pages, insert and check for overflow
				int index = pgInsertPage.binarySearchInsertTuples(strClustKeyName,
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
					handleInsertOverflow(tblTable, pgInsertPage, intMaxSize, strClustKeyName, hsIndexedColumns,
							tupleNewTuple); // handle overflow
					overflowOccured = true;
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
			tblTable.setMin(strPageName, (Comparable) tupleNewTuple.getColumnValue(strClustKeyName));
			tblTable.setMax(strPageName, (Comparable) tupleNewTuple.getColumnValue(strClustKeyName));

			newPage.serialize("tables/" + strTableName + "/" + strPageName + ".class");
		}

		// Adjusting Indicies (If any)

		if (hsIndexedColumns.size() > 0) {
			for (String strColName : hsIndexedColumns) {
				String strIndexName = metadata.getIndexName(strTableName, strColName); // getting index name
				BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class"); // getting
																														// the
																														// tree

				Comparable colValue = (Comparable) tupleNewTuple.getColumnValue(strColName); // cast column value to
																								// Comparable
				Pair pairIndexPair = new Pair(tupleNewTuple.getColumnValue(strClustKeyName),
						tupleNewTuple.getPageName());

				if (bptTree.query(colValue).size() > 0) {
					Pair PairOldPair = ((Pair) bptTree.query(colValue).get(0));
					bptTree.remove(colValue, PairOldPair);
					bptTree.insert(colValue, pairIndexPair);
				} else {
					bptTree.insert(colValue, pairIndexPair);
				}

				// bptTree.insert(colValue, pairIndexPair); // inserting col value(key) and
				// tuple
				// // object(value) into bTree

				bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class"); // serializing the tree
			}
		}

		// Serializing Table
		tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".class");

	}

	/**
	 * The `handleInsertOverflow` function manages overflow during tuple insertion in a table by
	 * redistributing tuples across pages and adjusting indices if necessary.
	 * 
	 * @param tblTable `tblTable` is an object representing a table in a database.
	 * @param pgFirstOverflowPage The `pgFirstOverflowPage` parameter in the `handleInsertOverflow` method
	 * represents the first page that caused an overflow when inserting a new tuple into a table. This
	 * page is where the overflow occurred, and the method handles redistributing the tuples to other
	 * pages to resolve the overflow situation.
	 * @param intMaxRowsPerPage The `intMaxRowsPerPage` parameter represents the maximum number of rows
	 * allowed per page in a table before overflow occurs. This value is used to determine when a page
	 * needs to be split during an insert operation to maintain the page size limit.
	 * @param strClustKeyName The parameter `strClustKeyName` represents the name of the clustering key
	 * column in the table. It is used to identify and manipulate the clustering key values within the
	 * tuples during the overflow handling process in the `handleInsertOverflow` method.
	 * @param hsIndexedCols The `hsIndexedCols` parameter is a `HashSet` that contains the names of
	 * columns that are indexed in the table. It is used to determine which columns have associated B+
	 * trees that need to be updated when handling an insert overflow situation.
	 * @param tupleNewTupleFromInsert The `tupleNewTupleFromInsert` parameter in the
	 * `handleInsertOverflow` method represents the new tuple that needs to be inserted into the table.
	 * This tuple is generated during an insert operation and is the tuple that caused the overflow in the
	 * first place. It contains the data values that need to be
	 */
	public void handleInsertOverflow(Table tblTable, Page pgFirstOverflowPage, int intMaxRowsPerPage,
			String strClustKeyName, HashSet hsIndexedCols, Tuple tupleNewTupleFromInsert)
			throws DBAppException {

		boolean adjustIndicies = true;
		Vector<String> vecPages = tblTable.getPages(); // Vector of Page names in table
		// Page pgFirstOverflowPage = pgFirstpgFirstOverflowPage; // page that caused
		// first overflow
		int intStartIndex = vecPages.indexOf(pgFirstOverflowPage.getPageName()); // get index of first overflowed page
																					// in the
		// vecPages Vector
		Tuple tupleLastTuple = pgFirstOverflowPage.removeLastTuple(); // last tuple in overflow page
		tblTable.setMax(pgFirstOverflowPage.getPageName(),
				(Comparable) pgFirstOverflowPage.getLastTuple().getColumnValue(strClustKeyName)); // adjusting max value
																									// in
		// overflow
		// page

		pgFirstOverflowPage
				.serialize("tables/" + tblTable.getTableName() + "/" + pgFirstOverflowPage.getPageName() + ".class"); // serialize
		// the
		// overflow
		// page

		// Loop through the rest of the pages to handle the overflow
		for (int i = intStartIndex + 1; i < vecPages.size(); i++) {
			Page page = Page.deserialize("tables/" + tblTable.getTableName() + "/" + vecPages.get(i) + ".class"); // get
																													// page
			tblTable.setMax(page.getPageName(),
					(Comparable) page.getLastTuple().getColumnValue(strClustKeyName));
			int index = 0;
			Comparable inputClustKey = (Comparable) tupleLastTuple.getColumnValue(strClustKeyName); // clust key in my
																									// last tuple (thats
																									// gonna move pages)
			Comparable pageClustKey = (Comparable) page.getTuple(0).getColumnValue(strClustKeyName); // clust key in the
																										// page i want
																										// to insert
																										// into

			if (inputClustKey.compareTo((Comparable) pageClustKey) > 0) {

				index = page.binarySearchTuples(strClustKeyName,
						tupleLastTuple.getColumnValue(strClustKeyName)); // get correct index to insert tuple in page
			} else if (inputClustKey.compareTo((Comparable) pageClustKey) == 0) {

				throw new DBAppException("Primary Key Already Exists");
			}
			// if lastTuple is inserted into first row of next page, then next page's min
			// needs to be changed
			if (index == 0) {
				tblTable.setMin(page.getPageName(), (Comparable) tupleLastTuple.getColumnValue(strClustKeyName));
			}

			// if unique clustering key, insert in page and adjust page name in tuple object
			// and in B+ Tree(if present)
			page.addTuple(index, tupleLastTuple); // add tuple to new/next page
			// TODO add same if statement here ? (duplicate index values)
			if (hsIndexedCols.size() > 0) {
				for (Object objColName : hsIndexedCols) {
					String strColName = (String) objColName;
					String strTableName = tblTable.getTableName();
					String strIndexName = metadata.getIndexName(strTableName, strColName); // getting index name
					BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class"); // getting
																															// the
																															// tree

					Comparable colValue = (Comparable) tupleLastTuple.getColumnValue(strColName);

					// Update Tree Page Name Value
					Pair pairOldIndexPair = new Pair(tupleLastTuple.getColumnValue(strClustKeyName),
							tupleLastTuple.getPageName());

					Pair pairNewIndexPair = new Pair(tupleLastTuple.getColumnValue(strClustKeyName),
							page.getPageName());

					bptTree.remove(colValue, pairOldIndexPair);
					bptTree.insert(colValue, pairNewIndexPair);

					tupleLastTuple.setPageName(page.getPageName()); // update page name in tuple obj

					bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class"); // serializing the tree
				}
			} else {
				tupleLastTuple.setPageName(page.getPageName()); // update page name in tuple obj
			}

			// check for overflow
			if (page.getSize() > intMaxRowsPerPage) { // if overflowed, remove last tuple, adjust max value and
														// serialize current page
				tupleLastTuple = page.removeLastTuple();
				tblTable.setMax(page.getPageName(),
						(Comparable) page.getLastTuple().getColumnValue(strClustKeyName)); // adjust max value in page

				page.serialize("tables/" + tblTable.getTableName() + "/" + page.getPageName() + ".class");
			} else { // if no overflow, serialize page and break the loop
				page.serialize("tables/" + tblTable.getTableName() + "/" + page.getPageName() + ".class");
				return;
			}
		}

		// if we reach this point, this means that we overflowed in all pages until no
		// pages were left, therefore a new page is needed

		String newPageName = tblTable.addPage(); // creating new page
		Page newPage = new Page(newPageName); // creating new page object with new page name

		tblTable.setMin(newPageName, (Comparable) tupleLastTuple.getColumnValue(strClustKeyName)); // adjust min value
																									// of new page
		tblTable.setMax(newPageName, (Comparable) tupleLastTuple.getColumnValue(strClustKeyName)); // adjust max value
																									// of new page

		// Adjust indicies only if there is any indexed columns *AND* newtuple being
		// inserted from "insertIntoTable"
		// method is not the same as the last tuple that was removed from the last page
		// (Because if they are the same, we already handle indicies at the end of
		// "insertIntoTable" method)
		// (avoid duplicate pairs in index & improve performance because BPTree(s) are
		// only deserialized once for the same one tuple)
		if (hsIndexedCols.size() > 0) {
			for (Object objColName : hsIndexedCols) {
				String strColName = (String) objColName;
				String strTableName = tblTable.getTableName();
				String strIndexName = metadata.getIndexName(strTableName, strColName); // getting index name
				BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class"); // getting
																														// the
																														// tree

				Comparable colValue = (Comparable) tupleLastTuple.getColumnValue(strColName); // cast column value
																								// to
																								// Comparable

				Pair pairOldIndexPair = new Pair(tupleLastTuple.getColumnValue(strClustKeyName),
						tupleLastTuple.getPageName());
				Pair pairNewIndexPair = new Pair(tupleLastTuple.getColumnValue(strClustKeyName),
						newPageName);

				bptTree.remove(colValue, pairOldIndexPair);
				bptTree.insert(colValue, pairNewIndexPair); // inserting col value[key] and Pair(clust key val, page
															// name) [value] into bTree

				tupleLastTuple.setPageName(newPageName); // adjusting page name in tuple obj

				bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class"); // serializing the tree
			}
		} else {// TODO LastTuple == NewTuple from "InsertIntoTable" , so update the newTuple's
				// value
			// because we will adjust its indicies in InsertIntoTable method
			tupleLastTuple.setPageName(newPageName); // adjusting page name in tuple obj
		}
		newPage.addTuple(0, tupleLastTuple); // inserting tuple in new object
		newPage.serialize("tables/" + tblTable.getTableName() + "/" + newPageName + ".class");

	}

	
	/**
	 * The `updateTable` function in Java updates a record in a table by validating input values, handling
	 * data types, and updating corresponding indexes.
	 * 
	 * @param strTableName The `strTableName` parameter represents the name of the table that you want to
	 * update. It is a String value that should match the name of an existing table in your database.
	 * @param strClusteringKeyValue The `strClusteringKeyValue` parameter in the `updateTable` method
	 * represents the value of the clustering key that is used to identify the specific tuple to update in
	 * the table. This value is used to locate the tuple within the table based on the clustering key
	 * column.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that contains the column
	 * names as keys and the corresponding values to be updated in the table. Each key represents a column
	 * name, and the associated value is the new value that should replace the existing value in that
	 * column for the specified row.
	 */
	public void updateTable(String strTableName, String strClusteringKeyValue,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {
		if (strTableName == null)
			throw new DBAppException("Table does not exist.");

		if (!metadata.checkTableName(strTableName))
			throw new DBAppException("Table does not exist.");

		if (strClusteringKeyValue == null)
			throw new DBAppException("Clustering key value is null.");

		if (htblColNameValue == null)
			throw new DBAppException("Column values are null.");

		if (htblColNameValue.isEmpty())
			throw new DBAppException("No column to update.");

		for (String strColumnName : htblColNameValue.keySet()) {
			if (!metadata.checkColumnName(strTableName, strColumnName)) {
				throw new DBAppException("Column doesn't exist.");
			} else {
				String strColumnTypeMetadata = metadata.getColumnType(strTableName, strColumnName);
				Object objColumnValue = htblColNameValue.get(strColumnName);
				if (objColumnValue == null) {
					throw new DBAppException("Column value for " + strColumnName + " is null.");
				}
				String strDataType = objColumnValue.getClass().getName();
				if (!strColumnTypeMetadata.equals(strDataType)) {
					throw new DBAppException("Datatypes do not match for the column " + strColumnName);
				}
			}
		}
		String strClusteringKey = metadata.getClusteringkey(strTableName);
		String strclusteringKeyType = metadata.getColumnType(strTableName, strClusteringKey);
		Comparable cmpClusteringKeyValue;

		if (htblColNameValue.containsKey(strClusteringKey))
			throw new DBAppException("Clustering key cannot be updated.");

		try {
			if (strclusteringKeyType.equals("java.lang.Integer")) {
				cmpClusteringKeyValue = Integer.parseInt(strClusteringKeyValue);
			} else if (strclusteringKeyType.equals("java.lang.Double")) {
				cmpClusteringKeyValue = Double.parseDouble(strClusteringKeyValue);
			} else if (strclusteringKeyType.equals("java.lang.String")) {
				cmpClusteringKeyValue = strClusteringKeyValue;
			} else {
				throw new DBAppException("Unsupported data type for clustering key.");
			}
		} catch (NumberFormatException e) {
			throw new DBAppException("Clustering key value does not match expected data type.");
		}

		Table tblTable = Table.deserialize("tables/" + strTableName + "/" + strTableName + ".class");
		String strKey = metadata.getClusteringkey(strTableName);

		Page pagePage = Page.getPageByClusteringKey(strTableName, cmpClusteringKeyValue, tblTable);
		if (pagePage == null) {
			System.out.println("Page for clustering key value not found.");
			return;
		}
		int intTupleIndex = pagePage.searchTuplesByClusteringKey(strKey, cmpClusteringKeyValue);
		if (intTupleIndex < 0) {
			System.out.println("No matching tuple found for clustering key value.");
			return;
		}
		Vector<Tuple> vecTuples = pagePage.getVecTuples();
		if (vecTuples == null || vecTuples.isEmpty()) {
			System.out.println("No tuples found in the page.");
			return;
		}

		Tuple tplTuple = vecTuples.get(intTupleIndex);
		if (tplTuple == null) {
			System.out.println("Tuple is null.");
			return;
		}

		Tuple tupleOriginalTuple = tplTuple.clone();
		List<String> listMetaCol = metadata.getColumnNames(strTableName);
		HashSet<String> hashsetIndexedonly = new HashSet<String>();
		for (String strName : listMetaCol) {
			String strIndexName = metadata.getIndexName(strTableName, strName);
			if (!(strIndexName.equals("null"))) {
				hashsetIndexedonly.add(strName);
			}
		}

		for (String strColumnName : htblColNameValue.keySet()) {
			Comparable cmpColumnValue = (Comparable) htblColNameValue.get(strColumnName);
			tplTuple.setColumnValue(strColumnName, cmpColumnValue);

		}

		for (String strColumnName : hashsetIndexedonly) {
			Comparable cmpTemp = (Comparable) tupleOriginalTuple.getColumnValue(strColumnName);

			if (htblColNameValue.containsKey(strColumnName)) {
				cmpTemp = (Comparable) tupleOriginalTuple.getColumnValue(strColumnName);
				Comparable cmpColumnValue = (Comparable) htblColNameValue.get(strColumnName);
				tplTuple.setColumnValue(strColumnName, cmpColumnValue);

				String indexName = metadata.getIndexName(strTableName, strColumnName);
				BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + indexName + ".class");

				Pair<Object, String> keyPair = new Pair<>(tplTuple.getColumnValue(strClusteringKey),
						tplTuple.getPageName());
				bptTree.remove(cmpTemp, keyPair);
				bptTree.insert(cmpColumnValue, keyPair);
				bptTree.serialize("tables/" + strTableName + "/" + indexName + ".class");

				pagePage.serialize("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");
				tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".class");
			} else {
				String strIndexName = metadata.getIndexName(strTableName, strColumnName);
				BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");

				Pair<Object, String> keyPair = new Pair<>(tplTuple.getColumnValue(strClusteringKey),
						tplTuple.getPageName());
				bptTree.remove(cmpTemp, keyPair);
				bptTree.insert(cmpTemp, keyPair);
				bptTree.serialize("tables/" + strTableName + "/" + strIndexName + ".class");
			}

		}
		// }
		pagePage.serialize("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");
		tblTable.serialize("tables/" + strTableName + "/" + strTableName + ".class");

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
		
		String strClusteringKeyColName = metadata.getClusteringkey(strTableName);

		Object objClusteringKeyValue = htblColNameValue.get(strClusteringKey);
		Page pagePage = Page.getPageByClusteringKey(strTableName, objClusteringKeyValue, table);
		
		
		// if the page is null, then the tuple does not exist
		if (pagePage == null) {
			return;
		}

		int intTupleIndex = pagePage.searchTuplesByClusteringKey(strClusteringKey, objClusteringKeyValue);

		

		if (intTupleIndex < 0) {
			return;
		}

		Tuple tupleTuple = pagePage.getTupleWithIndex(intTupleIndex);
		if (!tupleSatisfiesAndedConditions(tupleTuple, htblColNameValue)) {
			return;
		}

		pagePage.deleteTupleWithIndex(intTupleIndex);
		String strPageName = pagePage.getPageName();
		if (pagePage.getSize() == 0) {
			
			Page.deletePage("tables/" + strTableName + "/" + strPageName + ".class");

			table.removeMin(strPageName);
			table.removeMax(strPageName);
			table.removePage(strPageName);

		} else {
			// set min and max values for the page
			Comparable cmpMin = (Comparable) pagePage.getTupleWithIndex(0).getColumnValue(strClusteringKeyColName);
			Comparable cmpMax = (Comparable) pagePage.getTupleWithIndex(pagePage.getSize() - 1)
					.getColumnValue(strClusteringKeyColName);
			table.setMin(strPageName, cmpMin);
			table.setMax(strPageName, cmpMax);

			pagePage.serialize("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");

		}

		table.serialize("tables/" + strTableName + "/" + strTableName + ".class");

		// delete from all relevant indices

		for (String col : metadata.getColumnNames(strTableName)) {
			if (metadata.isColumnIndexed(strTableName, col)) {
				String strIndexName = metadata.getIndexName(strTableName, col);
				BPlusTree bptTree = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");
				Pair<Object, String> pair = new Pair<>(tupleTuple.getColumnValue(strClusteringKeyColName),
						tupleTuple.getPageName());
				bptTree.remove((Comparable) tupleTuple.getColumnValue(col), pair);
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
	 * @param table            The `table` parameter is an object of type `Table`
	 *                         that represents a table in a database. It contains
	 *                         information about the table, such as the number of
	 *                         pages and the pages themselves.
	 * @return This method returns a Hashtable containing tuples and their
	 *         corresponding page names that
	 *         match the given indexed column value in the specified table.
	 */
	private Hashtable<Tuple, String> getTuplesFromIndex(String strTableName, String strIndexedColumn,
			Hashtable<String, Object> htblColNameValue, Table table) throws DBAppException {

		String strClusteringKeyColName = metadata.getClusteringkey(strTableName);

		String strIndexName = metadata.getIndexName(strTableName, strIndexedColumn);

		BPlusTree bplustreeIndex = BPlusTree.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");

		String strIndexedColumnType = metadata.getColumnType(strTableName, strIndexedColumn);

		Comparable cmpIndexedColumnValue = (Comparable) htblColNameValue.get(strIndexedColumn);

		Hashtable<Tuple, String> htblTuples = new Hashtable<>(); // Tuple, page name

		List<Pair> arrayPairs = bplustreeIndex.query(cmpIndexedColumnValue);

		for (Pair pairPair : arrayPairs) {

			String strPageName = (String) pairPair.getValue();

			Comparable cmpClusteringKeyValue = (Comparable) pairPair.getKey();

			Page pagePage = Page.getPageByClusteringKey(strTableName, cmpClusteringKeyValue, table);

			int intTupleIndex = pagePage.searchTuplesByClusteringKey(strClusteringKeyColName, cmpClusteringKeyValue);

			if (intTupleIndex < 0) {
				continue;
			}

			Tuple tupleTuple = pagePage.getTupleWithIndex(intTupleIndex);

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

		String strClusteringKeyColName = metadata.getClusteringkey(strTableName);

		// Linear search

		int intNumberOfPages = table.getNumberOfPages();
		int intPageIndex = 0;

		while (intPageIndex < intNumberOfPages) {
			String pageCurrentPageName = table.getPageAtIndex(intPageIndex);
			Page pageCurrentPage = Page.deserialize("tables/" + strTableName + "/" + pageCurrentPageName + ".class");

			// Linearly search every page

			int intCurrentPageSize = pageCurrentPage.getSize();
			int intCurrentPageIndex = 0;

			while (intCurrentPageIndex < intCurrentPageSize) {
				Tuple tupleCurrentTuple = pageCurrentPage.getTupleWithIndex(intCurrentPageIndex);
				boolean boolToBeDeletedTuple = false;

				if (tupleSatisfiesAndedConditions(tupleCurrentTuple, htblColNameValue)) {
					pageCurrentPage.deleteTupleWithIndex(intCurrentPageIndex);
				} else {
					intCurrentPageIndex++;
				}
				intCurrentPageSize = pageCurrentPage.getSize();

			}

			if (pageCurrentPage.getSize() == 0) {
				Page.deletePage("tables/" + strTableName + "/" + pageCurrentPageName + ".class");

				table.removeMin(pageCurrentPageName);
				table.removeMax(pageCurrentPageName);
				table.removePage(pageCurrentPage.getPageName());

			} else {
				// set min and max values for the page
				Comparable cmpMin = (Comparable) pageCurrentPage.getTupleWithIndex(0)
						.getColumnValue(strClusteringKeyColName);
				Comparable cmpMax = (Comparable) pageCurrentPage.getTupleWithIndex(pageCurrentPage.getSize() - 1)
						.getColumnValue(strClusteringKeyColName);
				table.setMin(pageCurrentPageName, cmpMin);
				table.setMax(pageCurrentPageName, cmpMax);

				// TODO: Don't do this step if no tuples were deleted
				pageCurrentPage.serialize("tables/" + strTableName + "/" + pageCurrentPageName + ".class");
				intPageIndex++;

			}
			intNumberOfPages = table.getNumberOfPages();

		}

		table.serialize("tables/" + strTableName + "/" + strTableName + ".class");
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

		String strClusteringKeyColName = metadata.getClusteringkey(strTableName);

		Hashtable<Tuple, String> htblTuples = null;
		for (String col : htblColNameValue.keySet()) {
			if (metadata.isColumnIndexed(strTableName, col)) {
				if (htblTuples == null) {
					htblTuples = getTuplesFromIndex(strTableName, col, htblColNameValue, table);

				} else {
					// get the intersection hashmap entries

					htblTuples = getHashTableIntersection(htblTuples,
							getTuplesFromIndex(strTableName, col, htblColNameValue, table));

				}

			}
		}

		if (htblTuples != null) {

			for (Tuple tuple : htblTuples.keySet()) {
				if (!tupleSatisfiesAndedConditions(tuple, htblColNameValue)) {
					continue;
				}

				String strPageName = htblTuples.get(tuple);

				Page pagePage = Page.deserialize("tables/" + strTableName + "/" + strPageName + ".class");

				pagePage.deleteTuple(tuple);

				if (pagePage.getSize() == 0) {
					Page.deletePage("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");

					table.removeMin(strPageName);
					table.removeMax(strPageName);
					table.removePage(pagePage.getPageName());

				} else {

					// set min and max values for the page
					Comparable cmpMin = (Comparable) pagePage.getTupleWithIndex(0)
							.getColumnValue(strClusteringKeyColName);
					Comparable cmpMax = (Comparable) pagePage.getTupleWithIndex(pagePage.getSize() - 1)
							.getColumnValue(strClusteringKeyColName);
					table.setMin(strPageName, cmpMin);
					table.setMax(strPageName, cmpMax);

					pagePage.serialize("tables/" + strTableName + "/" + pagePage.getPageName() + ".class");
				}

				table.serialize("tables/" + strTableName + "/" + strTableName + ".class");

				// delete from all relevant indices

				for (String col : metadata.getColumnNames(strTableName)) {
					if (metadata.isColumnIndexed(strTableName, col)) {
						String strIndexName = metadata.getIndexName(strTableName, col);
						BPlusTree bptTree = BPlusTree
								.deserialize("tables/" + strTableName + "/" + strIndexName + ".class");
						Pair<Object, String> pair = new Pair<>(tuple.getColumnValue(strClusteringKeyColName),
								tuple.getPageName());
						bptTree.remove((Comparable) tuple.getColumnValue(col), pair);
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
	private Hashtable<Tuple, String> getHashTableIntersection(Hashtable<Tuple, String> htbl1,
			Hashtable<Tuple, String> htbl2) {
		Hashtable<Tuple, String> htblIntersection = new Hashtable<>();

		// swap the two hashtables if the first one is larger

		if (htbl1.size() > htbl2.size()) {
			Hashtable<Tuple, String> temp = htbl1;
			htbl1 = htbl2;
			htbl2 = temp;
		}

		for (Tuple tuple : htbl1.keySet()) {
			if (htbl2.containsKey(tuple)) {
				htblIntersection.put(tuple, htbl2.get(tuple));
			}
		}

		return htblIntersection;
	}

	
	/**
	 * The `deleteFromTable` function in Java deletes records from a table based on specified column
	 * values after performing necessary checks.
	 * 
	 * @param strTableName The `strTableName` parameter is a `String` that represents the name of the
	 * table from which you want to delete a record.
	 * @param htblColNameValue The `htblColNameValue` parameter is a Hashtable that contains the column
	 * names as keys and the corresponding values to be deleted as values. This method is used to delete
	 * records from a table based on the specified column values.
	 */
	public void deleteFromTable(String strTableName,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		if (!metadata.checkTableName(strTableName)) {
			throw new DBAppException("Table does not exist");
		}

		// check if the column names exist & if the column values are of the correct
		// type
		for (String strCol : htblColNameValue.keySet()) {

			// check if the column name exists
			if (!metadata.checkColumnName(strTableName, strCol)) {
				throw new DBAppException("Column does not exist");
			}
			// check if the column value is of the correct type
			String strColType = metadata.getColumnType(strTableName, strCol);
			Object objValue = htblColNameValue.get(strCol);
			if (!objValue.getClass().getName().equals(strColType)) {
				throw new DBAppException("Data type mismatch");
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

	/**
	 * The `selectFromTable` function in Java processes SQL terms and operators to query a table and
	 * return an iterator of the selected tuples.
	 * 
	 * @param arrSQLTerms The `arrSQLTerms` parameter is an array of SQLTerm objects, which represent
	 * individual conditions to be applied in the query. Each SQLTerm object contains information about a
	 * specific condition, such as the table name, column name, operator, and value to compare against.
	 * @param strarrOperators The `strarrOperators` parameter is an array of strings that represent the
	 * logical operators to be used between the SQL terms in the query. These operators can be "AND",
	 * "OR", or "XOR" and are used to combine multiple SQL terms in the query.
	 * @return An `Iterator` is being returned, which allows iterating over a collection of elements, in
	 * this case, tuples retrieved from the database based on the provided SQL terms and operators.
	 */
	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
			String[] strarrOperators) throws DBAppException {

		try {
			if (arrSQLTerms == null || strarrOperators == null) {
				throw new DBAppException("Nothing to query on");
			}
			if (arrSQLTerms.length == 0) {
				throw new DBAppException("Nothing to query on");
			}
			if (arrSQLTerms.length != strarrOperators.length + 1) {
				throw new DBAppException("something wrong in the input");
			}
			if(arrSQLTerms.length==1){
				if(arrSQLTerms[0]._strTableName!=null&&arrSQLTerms[0]._strColumnName==null&&arrSQLTerms[0]._strOperator==null&&arrSQLTerms[0]._objValue==null){
					if(metadata.checkTableName(arrSQLTerms[0]._strTableName)){
						Table table = Table.deserialize("tables/" + arrSQLTerms[0]._strTableName + "/" + arrSQLTerms[0]._strTableName + ".class");
						Vector<String>pages=  table.getPageVector();
						HashSet<Tuple>hstup= new HashSet<>();
						for(String page:pages){
							Page page2 = Page.deserialize("tables/" + arrSQLTerms[0]._strTableName + "/" + page + ".class");
							hstup.addAll(page2.allTup());
						}
						return hstup.iterator();
					}
					else{
						throw new DBAppException("Table doesn't exist");
					}
				}
			}
			HashSet<String> checkoperators = new HashSet<>();
			checkoperators.add("=");
			checkoperators.add("!=");
			checkoperators.add(">=");
			checkoperators.add("<=");
			checkoperators.add(">");
			;
			checkoperators.add("<");
			boolean indexhelp2 = false;
			boolean indexhelp3 = false;
			int ind =0;
			for (int i = 0; i < arrSQLTerms.length; i++) {
				if (arrSQLTerms[i]._strTableName != null && metadata.checkTableName(arrSQLTerms[i]._strTableName)
						&& arrSQLTerms[0]._strTableName.equals(arrSQLTerms[i]._strTableName)) {
					if (arrSQLTerms[i]._strColumnName != null
							&& metadata.checkColumnName(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName)) {
						String strType = metadata.getColumnType(arrSQLTerms[i]._strTableName,
								arrSQLTerms[i]._strColumnName);
						if (arrSQLTerms[i]._strOperator != null
								&& checkoperators.contains(arrSQLTerms[i]._strOperator)) {
							if (!metadata.getIndexType(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName)
									.equals("null") && (!arrSQLTerms[i]._strOperator.equals("!="))) {
								indexhelp2 = true;
							}
							
							if (metadata.isClusteringKey(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName)
								 && (!arrSQLTerms[i]._strOperator.equals("!="))) {
									System.out.println(i);
									indexhelp3 = true;
								ind=i;
							}
							if (arrSQLTerms[i]._strOperator == null
									|| !strType.equals(arrSQLTerms[i]._objValue.getClass().getName())) {
								throw new DBAppException("Datatype doesn't match");
							}
						} else
							throw new DBAppException("This Operator isn't supported");
					} else
						throw new DBAppException("Column doesn't exist");
				} else
					throw new DBAppException("Table doesn't exist or inconsistent");
			}
			boolean indexhelp = true;
			for (int i = 0; i < strarrOperators.length; i++) {
				if (Objects.isNull(strarrOperators[i])) {
					throw new DBAppException("null in operator");
				}
				if ((!strarrOperators[i].toLowerCase().equals("and"))
						&& (!strarrOperators[i].toLowerCase().equals("or"))
						&& (!strarrOperators[i].toLowerCase().equals("xor")))
					throw new DBAppException("Undefined operator is being used");
				if (!(strarrOperators[i].toLowerCase().equals("and")))
					indexhelp = false;
			}
			// el hashes at end of if statements are stand by
			if (strarrOperators.length == 0) {
				HashSet<Tuple> tut = getTuple(arrSQLTerms[0]);
				
				return tut.iterator();
			}
			if (indexhelp && indexhelp2) {
				System.out.println("Index ya bsha");
				boolean firstornot = true;
				HashSet<String> hmpage = new HashSet<>();
				Vector<SQLTerm> indexsql = new Vector<>();
				for (int i = 0; i < arrSQLTerms.length; i++) {
					if (!metadata.getIndexType(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName)
							.equals("null") && (!arrSQLTerms[i]._strOperator.equals("!="))) {
						if (firstornot) {
							hmpage = getPage(arrSQLTerms[i]);
							firstornot = false;
						} else {
							hmpage = and2hs(hmpage, getPage(arrSQLTerms[i]));
						}
						indexsql.add(arrSQLTerms[i]);
					}
				}
				
				HashSet<Tuple> hmtup = new HashSet<>();
				hmtup = pagestotuples(hmpage, indexsql);
				for (int i = 0; i < arrSQLTerms.length; i++) {
					if (metadata.getIndexType(arrSQLTerms[i]._strTableName, arrSQLTerms[i]._strColumnName)
							.equals("null") || (arrSQLTerms[i]._strOperator.equals("!="))) {
						hmtup = and1bp(hmtup, arrSQLTerms[i]);
					}
				}
				return hmtup.iterator();
			}
			if(indexhelp&&indexhelp3){
				System.out.println("cluster ya bsha");
				HashSet<Tuple> hstup = getTuple(arrSQLTerms[ind]);
				for(int i=0;i<arrSQLTerms.length;i++){
					if(i!=ind){
						hstup=and1bp(hstup, arrSQLTerms[i]);
					}
				}
				return hstup.iterator();
			}
			Stack<Object> stack = new Stack<>();
			Stack<String> stack2 = new Stack<>();
			Vector<Object> vec = new Vector<>();
			vec.add(arrSQLTerms[0]);
			for (int i = 0; i < strarrOperators.length; i++) {
				if (stack2.isEmpty()) {
					stack2.push(strarrOperators[i]);
					vec.add(arrSQLTerms[i + 1]);
				} else if (precedence(strarrOperators[i].toLowerCase()) >= precedence(stack2.peek().toLowerCase())) {
					stack2.push(strarrOperators[i]);
					vec.add(arrSQLTerms[i + 1]);
				} else {
					while (!stack2.isEmpty()
							&& precedence(strarrOperators[i].toLowerCase()) < precedence(stack2.peek().toLowerCase())) {
						vec.add(stack2.pop());
					}
					stack2.push(strarrOperators[i]);
					vec.add(arrSQLTerms[i + 1]);
				}
			}
			while (!stack2.isEmpty()) {
				vec.add(stack2.pop());
			}
			
			for (int i = 0; i < vec.size(); i++) {
				if (vec.get(i) instanceof SQLTerm) {
					stack.push((SQLTerm) vec.get(i));
				} else {
					HashSet<Tuple> tm = null;
					HashSet<Tuple> tm2 = null;
					
					if (stack.peek() instanceof SQLTerm) {
						tm = getTuple((SQLTerm) stack.pop());
						if (stack.peek() instanceof SQLTerm) {
							tm2 = getTuple((SQLTerm) stack.pop());
						} else {
							tm2 = (HashSet<Tuple>) stack.pop();
						}
					} else {
						tm = (HashSet<Tuple>) stack.pop();
						if (stack.peek() instanceof SQLTerm) {
							tm2 = getTuple((SQLTerm) stack.pop());
						} else {
							tm2 = (HashSet<Tuple>) stack.pop();
						}
					}
					if (((String) vec.get(i)).toLowerCase().equals("and")) {

						stack.add(and2hs(tm, tm2));
					} else if (((String) vec.get(i)).toLowerCase().equals("or")) {
						stack.add(or2hs(tm, tm2));
					} else {
						
						stack.add(xor2hs(tm, tm2));
					}

				}
			}
			HashSet<Tuple> tm = (HashSet<Tuple>) stack.pop();

			return tm.iterator();
		} catch (ClassNotFoundException | IOException e) {
			throw new DBAppException("Something went wrong");
		}
	}

	

	/**
	 * The function `and2hs` takes two HashSet inputs and returns a new HashSet containing elements that
	 * are common to both input HashSets.
	 * 
	 * @param hs1 A HashSet containing elements of type T.
	 * @param hs2 The `hs2` parameter in the `and2hs` method represents a `HashSet` containing elements of
	 * type `T`. This method takes two `HashSet` objects (`hs1` and `hs2`) as input and returns a new
	 * `HashSet` containing the intersection of elements present in both
	 * @return The method `and2hs` returns a `HashSet` containing the elements that are common between the
	 * two input `HashSet` objects `hs1` and `hs2`.
	 */
	private static <T> HashSet<T> and2hs(HashSet<T> hs1, HashSet<T> hs2) {

		HashSet<T> hsResult = new HashSet<>(), hsToBeLoopedOver = null, hsAnotherHashSet = null;
		if (hs1.size() > hs2.size()) {
			hsToBeLoopedOver = hs2;
			hsAnotherHashSet = hs1;
		} else {
			hsToBeLoopedOver = hs1;
			hsAnotherHashSet = hs2;
		}
		for (T element : hsToBeLoopedOver) {
			if (hsAnotherHashSet.contains(element))
				hsResult.add(element);
		}

		return hsResult;
	}

	/**
	 * The function `or2hs` takes two HashSet objects, combines them into one HashSet, and returns the
	 * combined HashSet.
	 * 
	 * @param hs1 A HashSet of Tuple objects.
	 * @param hs2 The `or2hs` method takes two HashSet objects of type Tuple as parameters: hs1 and hs2.
	 * The method combines the elements of hs1 and hs2 into hs1 and returns the updated hs1 HashSet.
	 * @return The method `or2hs` returns a `HashSet` that contains the union of the elements from both
	 * input `HashSet` objects `hs1` and `hs2`.
	 */
	private static HashSet<Tuple> or2hs(HashSet<Tuple> hs1, HashSet<Tuple> hs2) {

		if (hs1.size() < hs2.size()) {
			HashSet<Tuple> hsTemp = hs1;
			hs1 = hs2;
			hs2 = hsTemp;
		}
		hs1.addAll(hs2);

		return hs1;
	}

	/**
	 * The function `xor2hs` takes two HashSet of Tuple objects, performs an XOR operation between them,
	 * and returns the resulting HashSet.
	 * 
	 * @param hs1 `hs1` and `hs2` are both HashSet collections containing Tuple objects. The method
	 * `xor2hs` takes these two HashSet collections as input parameters and performs an XOR operation on
	 * them.
	 * @param hs2 The `hs2` parameter is a HashSet of Tuple objects.
	 * @return The method `xor2hs` is returning a `HashSet` containing the result of performing an XOR
	 * operation on the two input `HashSet` objects `hs1` and `hs2`.
	 */
	private static HashSet<Tuple> xor2hs(HashSet<Tuple> hs1, HashSet<Tuple> hs2) {

		if (hs1.size() > hs2.size()) {
			HashSet<Tuple> hsTemp = hs1;
			hs1 = hs2;
			hs2 = hsTemp;
		}
		for (Tuple tm : hs1) {
			if (hs2.contains(tm)) {
				hs2.remove(tm);
			} else {
				hs2.add(tm);
			}
		}

		return hs2;
	}

	/**
	 * The function `and1bp` filters a HashSet of tuples based on a given SQLTerm condition.
	 * 
	 * @param hs1 The `hs1` parameter in the `and1bp` method is a HashSet of Tuple objects. Each Tuple
	 * object represents a row in a database table, with columns and their corresponding values. The
	 * method is designed to filter out tuples from `hs1` based on a given SQLTerm condition.
	 * @param sql The `sql` parameter in the `and1bp` method represents a SQL term that includes the
	 * column name, comparison operator, and value to be used for filtering the tuples in the `HashSet`.
	 * The method applies the specified comparison operator to each tuple in the `HashSet` based on the
	 * column value
	 * @return The method `and1bp` is returning the modified `HashSet<Tuple> hs1` after removing elements
	 * that do not satisfy the given SQLTerm condition.
	 */
	private static HashSet<Tuple> and1bp(HashSet<Tuple> hs1, SQLTerm sql) throws DBAppException {
		HashSet<Tuple> temp = new HashSet<>();
		for (Tuple tm : hs1) {
			if (sql._strOperator.equals("=")) {
				if (sql._objValue instanceof Integer) {
					Integer te = (Integer) sql._objValue;
					if (!((Integer) tm.getColumnValue(sql._strColumnName)).equals(te)) {
						temp.add(tm);
					}
				} else if (sql._objValue instanceof Double) {
					Double te = (Double) sql._objValue;
					if (!((Double) tm.getColumnValue(sql._strColumnName)).equals(te)) {
						temp.add(tm);
					}
				} else {
					String te = (String) sql._objValue;
					if (((String) tm.getColumnValue(sql._strColumnName)).compareTo(te) != 0) {
						temp.add(tm);
					}
				}
			} else if (sql._strOperator.equals(">")) {
				if (sql._objValue instanceof Integer) {
					Integer te = (Integer) sql._objValue;
					if (((Integer) tm.getColumnValue(sql._strColumnName)) <= (te)) {
						temp.add(tm);
					}
				} else if (sql._objValue instanceof Double) {
					Double te = (Double) sql._objValue;
					if (((Double) tm.getColumnValue(sql._strColumnName)) <= (te)) {
						temp.add(tm);
					}
				} else {
					String te = (String) sql._objValue;
					if (((String) tm.getColumnValue(sql._strColumnName)).compareTo(te) <= 0) {
						temp.add(tm);
					}
				}
			} else if (sql._strOperator.equals(">=")) {
				if (sql._objValue instanceof Integer) {
					Integer te = (Integer) sql._objValue;
					if (((Integer) tm.getColumnValue(sql._strColumnName)) < (te)) {
						temp.add(tm);
					}
				} else if (sql._objValue instanceof Double) {
					Double te = (Double) sql._objValue;
					if (((Double) tm.getColumnValue(sql._strColumnName)) < (te)) {
						temp.add(tm);
					}
				} else {
					String te = (String) sql._objValue;
					if (((String) tm.getColumnValue(sql._strColumnName)).compareTo(te) < 0) {
						temp.add(tm);
					}
				}
			} else if (sql._strOperator.equals("<")) {
				if (sql._objValue instanceof Integer) {
					Integer te = (Integer) sql._objValue;
					if (((Integer) tm.getColumnValue(sql._strColumnName)) >= (te)) {
						temp.add(tm);
					}
				} else if (sql._objValue instanceof Double) {
					Double te = (Double) sql._objValue;
					if (((Double) tm.getColumnValue(sql._strColumnName)) >= (te)) {
						temp.add(tm);
					}
				} else {
					String te = (String) sql._objValue;
					if (((String) tm.getColumnValue(sql._strColumnName)).compareTo(te) >= 0) {
						temp.add(tm);
					}
				}
			} else if (sql._strOperator.equals("<=")) {
				if (sql._objValue instanceof Integer) {
					Integer te = (Integer) sql._objValue;
					if (((Integer) tm.getColumnValue(sql._strColumnName)) > (te)) {
						temp.add(tm);
					}
				} else if (sql._objValue instanceof Double) {
					Double te = (Double) sql._objValue;
					if (((Double) tm.getColumnValue(sql._strColumnName)) > (te)) {
						temp.add(tm);
					}
				} else {
					String te = (String) sql._objValue;
					if (((String) tm.getColumnValue(sql._strColumnName)).compareTo(te) > 0) {
						temp.add(tm);
					}
				}
			} else {
				if (sql._objValue instanceof Integer) {
					Integer te = (Integer) sql._objValue;
					if (((Integer) tm.getColumnValue(sql._strColumnName)).equals(te)) {
						temp.add(tm);
					}
				} else if (sql._objValue instanceof Double) {
					Double te = (Double) sql._objValue;
					if (((Double) tm.getColumnValue(sql._strColumnName)).equals(te)) {
						temp.add(tm);
					}
				} else {
					String te = (String) sql._objValue;
					if (((String) tm.getColumnValue(sql._strColumnName)).compareTo(te) == 0) {
						temp.add(tm);
					}
				}
			}
		}
		for (Tuple tm : temp) {
			hs1.remove(tm);
		}
		return hs1;
	}

	/**
	 * The function `pagestotuples` takes a HashSet of page names and a Vector of SQLTerms, retrieves tuples
	 * from corresponding pages, applies SQLTerms filtering, and returns a HashSet of tuples.
	 * 
	 * @param hs1 The `hs1` parameter is a HashSet of page names values.
	 * @param vec The `vec` parameter is a Vector of SQLTerm objects. Each SQLTerm object
	 * likely represents a condition that needs to be applied to filter the tuples in the HashSet of
	 * page names `hs1`. The method `pagestotuples` processes each page represented by the page names in `hs1`
	 * @return The method `pagestotuples` returns a `HashSet` of `Tuple` objects.
	 */
	private static HashSet<Tuple> pagestotuples(HashSet<String> hs1, Vector<SQLTerm> vec) throws DBAppException {
		HashSet<Tuple> hstup = new HashSet<>();
		for (String tm : hs1) {
			HashSet<Tuple> temp = new HashSet<>();
			Page page = Page.deserialize("tables/" + vec.get(0)._strTableName + "/" + tm + ".class");
			Vector<Tuple> listtmp = page.getTuples();
			for (Tuple tup : listtmp) {
				temp.add(tup);
			}
			for (SQLTerm sql : vec) {
				temp = and1bp(temp, sql);
			}
			hstup.addAll(temp);
		}
		return hstup;
	}

	/**
	 * The function `getTuple` retrieves tuples based on the specified SQL term and operator, utilizing
	 * indexing if available.
	 * 
	 * @param sql The `getTuple` method you provided handles different SQL terms to retrieve
	 * tuples from a table based on the specified conditions. The method checks the operator in the SQL
	 * term and performs different actions accordingly.
	 * @return A HashSet of Tuple objects is being returned.
	 */
	private HashSet<Tuple> getTuple(SQLTerm sql) throws ClassNotFoundException, IOException, DBAppException {
		HashSet<Tuple> hstup = new HashSet<>();
		if (sql._strOperator.equals("=")) {
			if (!metadata.getIndexType(sql._strTableName, sql._strColumnName).equals("null")) {
				BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
						+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
				Vector<Pair> listtmp = new Vector<>(bptmp.query((Comparable) sql._objValue));
				HashSet<String> hspage = new HashSet<>();
				for (Pair tup : listtmp) {
					hspage.add((String) tup.getValue());
				}
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			} else {

				Table table = Table.deserialize("tables/" + sql._strTableName + "/" + sql._strTableName + ".class");
				if (!metadata.isClusteringKey(sql._strTableName, sql._strColumnName)) {
					hstup = table.eqsearch(sql._strColumnName, sql._objValue);
					return hstup;
				} else {
					hstup = table.cleqsearch(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}

		} else if (sql._strOperator.equals(">")) {
			if (!metadata.getIndexType(sql._strTableName, sql._strColumnName).equals("null")) {

				BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
						+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
				Vector<Pair> listtmp;
				if (sql._objValue instanceof Integer) {
					listtmp = new Vector<>(bptmp.rangeQuery((Integer) sql._objValue,
							Integer.MAX_VALUE));
				} else if (sql._objValue instanceof Double) {
					listtmp = new Vector<>(bptmp.rangeQuery((Comparable) sql._objValue,
							Double.MAX_VALUE));
				} else {
					listtmp = new Vector<>(bptmp.rangeQuery((Comparable) sql._objValue, "{"));
				}
				HashSet<String> hspage = new HashSet<>();
				for (Pair tup : listtmp) {
					hspage.add((String) tup.getValue());
				}
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			} else {

				Table table = Table.deserialize("tables/" + sql._strTableName + "/" + sql._strTableName + ".class");
				if (!metadata.isClusteringKey(sql._strTableName, sql._strColumnName)) {
					hstup = table.greaterthannc(sql._strColumnName, sql._objValue);
					return hstup;
				} else {
					hstup = table.greaterthan(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}
		} else if (sql._strOperator.equals(">=")) {
			if (!metadata.getIndexType(sql._strTableName, sql._strColumnName).equals("null")) {

				BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
						+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
				Vector<Pair> listtmp;
				if (sql._objValue instanceof Integer) {
					listtmp = new Vector<>(bptmp.rangeQuery((Comparable) sql._objValue,
							Integer.MAX_VALUE));
				} else if (sql._objValue instanceof Double) {
					listtmp = new Vector<>(bptmp.rangeQuery((Comparable) sql._objValue,
							Double.MAX_VALUE));
				} else {
					listtmp = new Vector<>(bptmp.rangeQuery((Comparable) sql._objValue, "{"));
				}
				HashSet<String> hspage = new HashSet<>();
				for (Pair tup : listtmp) {
					hspage.add((String) tup.getValue());
				}
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			} else {
				Table table = Table.deserialize("tables/" + sql._strTableName + "/" + sql._strTableName + ".class");
				if (!metadata.isClusteringKey(sql._strTableName, sql._strColumnName)) {
					hstup = table.greaterthaneqnc(sql._strColumnName, sql._objValue);
					return hstup;
				} else {
					hstup = table.greaterthaneq(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}
		} else if (sql._strOperator.equals("<")) {
			if (!metadata.getIndexType(sql._strTableName, sql._strColumnName).equals("null")) {

				BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
						+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
				Vector<Pair> listtmp;
				if (sql._objValue instanceof Integer) {
					listtmp = new Vector<>(bptmp.rangeQuery(Integer.MIN_VALUE,
							(Comparable) sql._objValue));
				} else if (sql._objValue instanceof Double) {
					listtmp = new Vector<>(bptmp.rangeQuery(Double.MIN_VALUE,
							(Comparable) sql._objValue));
				} else {
					listtmp = new Vector<>(bptmp.rangeQuery("", (Comparable) sql._objValue));
				}
				HashSet<String> hspage = new HashSet<>();
				for (Pair tup : listtmp) {
					hspage.add((String) tup.getValue());
				}
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			} else {
				Table table = Table.deserialize("tables/" + sql._strTableName + "/" + sql._strTableName + ".class");
				if (!metadata.isClusteringKey(sql._strTableName, sql._strColumnName)) {
					hstup = table.lesserthannc(sql._strColumnName, sql._objValue);
					return hstup;
				} else {
					hstup = table.lesserthan(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}
		} else if (sql._strOperator.equals("<=")) {
			if (!metadata.getIndexType(sql._strTableName, sql._strColumnName).equals("null")) {

				BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
						+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
				Vector<Pair> listtmp;
				if (sql._objValue instanceof Integer) {
					listtmp = new Vector<>(bptmp.rangeQuery(Integer.MIN_VALUE,
							(Comparable) sql._objValue));
					listtmp.addAll(bptmp.query((Integer) sql._objValue));
				} else if (sql._objValue instanceof Double) {
					listtmp = new Vector<>(bptmp.rangeQuery(Double.MIN_VALUE,
							(Comparable) sql._objValue));
					listtmp.addAll(bptmp.query((Double) sql._objValue));
				} else {
					listtmp = new Vector<>(bptmp.rangeQuery("", (Comparable) sql._objValue));
					listtmp.addAll(bptmp.query((String) sql._objValue));
				}
				HashSet<String> hspage = new HashSet<>();
				for (Pair tup : listtmp) {
					hspage.add((String) tup.getValue());
				}
				Vector<SQLTerm> vec = new Vector<>();
				vec.add(sql);
				return pagestotuples(hspage, vec);
			} else {
				Table table = Table.deserialize("tables/" + sql._strTableName + "/" +
						sql._strTableName + ".class");
				if (!metadata.isClusteringKey(sql._strTableName, sql._strColumnName)) {
					hstup = table.lesserthannceq(sql._strColumnName, sql._objValue);
					return hstup;
				} else {
					hstup = table.lesserthaneq(sql._strColumnName, sql._objValue);
					return hstup;
				}
			}
		} else {
			Table table = Table.deserialize("tables/" + sql._strTableName + "/" +
					sql._strTableName + ".class");
			hstup = table.noteqsearch(sql._strColumnName, sql._objValue);
			return hstup;
		}

	}

	/**
	 * The function `getPage` retrieves records from a B+ tree index based on a given SQL term.
	 * 
	 * @param sql The `getPage` method takes an `SQLTerm` object as a parameter. The `SQLTerm` class
	 * represents a single SQL condition in a query, containing information such as the table name,
	 * column name, comparison operator, and comparison value.
	 * @return The method `getPage` is returning a `HashSet<String>` containing the page names retrieved based
	 * on the SQLTerm provided. The method performs different operations based on the comparison operator
	 * specified in the SQLTerm and populates the HashSet with the corresponding page names from the BPlusTree
	 * index.
	 */
	private HashSet<String> getPage(SQLTerm sql) throws ClassNotFoundException, IOException, DBAppException {
		HashSet<String> hstup = new HashSet<>();
		if (sql._strOperator.equals("=")) {
			BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
					+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
			Vector<Pair> listtmp = new Vector(bptmp.query((Comparable) sql._objValue));
			for (Pair tup : listtmp) {
				hstup.add((String) tup.getValue());
			}
			return hstup;
		}

		else if (sql._strOperator.equals(">=") || sql._strOperator.equals(">")) {
			BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
					+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
			Vector<Pair> listtmp;
			if (sql._objValue instanceof Integer) {
				listtmp = new Vector(bptmp.rangeQuery((Comparable) sql._objValue, Integer.MAX_VALUE));
				for (Pair tup : listtmp) {
					hstup.add((String) tup.getValue());
				}
			} else if (sql._objValue instanceof Double) {
				listtmp = new Vector(bptmp.rangeQuery((Comparable) sql._objValue, Double.MAX_VALUE));
				for (Pair tup : listtmp) {
					hstup.add((String) tup.getValue());
				}
			} else {
				listtmp = new Vector(bptmp.rangeQuery((Comparable) sql._objValue, "{"));
				for (Pair tup : listtmp) {
					hstup.add((String) tup.getValue());
				}
			}
			return hstup;

		} else {
			BPlusTree bptmp = BPlusTree.deserialize("tables/" + sql._strTableName + "/"
					+ metadata.getIndexName(sql._strTableName, sql._strColumnName) + ".class");
			Vector<Pair> listtmp;
			if (sql._objValue instanceof Integer) {
				listtmp = new Vector(bptmp.rangeQuery(Integer.MIN_VALUE, (Comparable) sql._objValue));
				listtmp.addAll(bptmp.query((Integer) sql._objValue));
				for (Pair tup : listtmp) {
					hstup.add((String) tup.getValue());
				}
			} else if (sql._objValue instanceof Double) {
				listtmp = new Vector(bptmp.rangeQuery(Double.MIN_VALUE, (Comparable) sql._objValue));
				listtmp.addAll(bptmp.query((Comparable) sql._objValue));
				for (Pair tup : listtmp) {
					hstup.add((String) tup.getValue());
				}
			} else {
				listtmp = new Vector(bptmp.rangeQuery("", (Comparable) sql._objValue));
				listtmp.addAll(bptmp.query((Comparable) sql._objValue));
				for (Pair tup : listtmp) {
					hstup.add((String) tup.getValue());
				}
			}
			return hstup;

		}

	}

	/**
	 * The function `precedence` returns the precedence level of a logical operator in Java.
	 * 
	 * @param operator The `precedence` method takes a string `operator` as a parameter and returns an
	 * integer value based on the precedence of the operator. The method uses a switch statement to
	 * determine the precedence value for the given operator.
	 * @return The `precedence` method returns an integer value representing the precedence of the given
	 * operator. The method uses a switch statement to determine the precedence based on the operator
	 * provided as a parameter.
	 */
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

	
	/**
	 * The `parseSQL` function writes the contents of a `StringBuffer` to a file, calls another method to
	 * process the file, and returns an `Iterator` from the result.
	 * 
	 * @param strbufSQL The `strbufSQL` parameter is a `StringBuffer` object that contains the SQL query
	 * to be parsed.
	 * @return The `parseSQL` method is returning an Iterator object. The Iterator is obtained from the
	 * `Main` class, specifically from the static `iterator` field of the `Main` class.
	 */
	public Iterator parseSQL( StringBuffer strbufSQL ) throws
	DBAppException, IOException {
		// below method returns Iterator with result set if passed
		// strbufSQL is a select, otherwise returns null.
		String filePath = "test.txt";

		FileWriter fileWriter = new FileWriter(filePath);

		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write(strbufSQL.toString());

		bufferedWriter.close();

		main2 obj = new main2();
		obj.main(new String[]{});

		//		Iterator iterator = Main.getIterator();
		return Main.iterator;


	}

	private static void cleanUp() throws IOException {
		try {
			// delete tables directory

			String tablesPath = "tables/";

			Path dir = Paths.get(tablesPath); // path to the directory
			Files
					.walk(dir) // Traverse the file tree in depth-first order
					.sorted(Comparator.reverseOrder())
					.forEach(path -> {
						try {

							Files.delete(path); // delete each file or directory
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
		} catch (Exception e) {

		}

		try {

			// delete Indicies directory

			String indiciesPath = "Indicies/";

			Path dir2 = Paths.get(indiciesPath); // path to the directory
			Files
					.walk(dir2) // Traverse the file tree in depth-first order
					.sorted(Comparator.reverseOrder())
					.forEach(path -> {
						try {

							Files.delete(path); // delete each file or directory
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
		} catch (Exception e) {

		}
		try {
			// delete metadata.csv

			File metadata = new File("metadata.csv");
			metadata.delete();
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) throws IOException {

		try {
			// cleanUp();
			DBApp dbApp = new DBApp();
            dbApp.init();

			String strTableName = "Student";

            Hashtable<String, String> htblColNameType = new Hashtable<String, String>();

            htblColNameType.put("id", "java.lang.Integer");

            htblColNameType.put("name", "java.lang.String");

            htblColNameType.put("gpa", "java.lang.Double");

            dbApp.createTable(strTableName, "id", htblColNameType);

            // insert n rows

            for (int i = 0; i < 20; i++) {
                Hashtable<String, Object> htblColNameValue = new Hashtable<String, Object>();
                htblColNameValue.put("id", i);
                htblColNameValue.put("name", "Student" + i);
                htblColNameValue.put("gpa", 3.0 + i);
                dbApp.insertIntoTable(strTableName, htblColNameValue);
            }
			
			SQLTerm [] arrSQLTerms = new SQLTerm[2];
            String []strarrOperators = new String[1];

            arrSQLTerms[0] = new SQLTerm();
            arrSQLTerms[0]._strTableName = "Student";
            arrSQLTerms[0]._strColumnName = "id";
            arrSQLTerms[0]._strOperator = "!=";
            arrSQLTerms[0]._objValue = 10;

            strarrOperators[0] = "AND";

            arrSQLTerms[1] = new SQLTerm();
            arrSQLTerms[1]._strTableName = "Student";
            arrSQLTerms[1]._strColumnName = "name";
            arrSQLTerms[1]._strOperator = ">";
            arrSQLTerms[1]._objValue = "Student1";
			Iterator iterator = dbApp.selectFromTable(arrSQLTerms, strarrOperators); // Select * from Student where id < 10 AND
                                                                            // id > 5;

            while (iterator.hasNext()) {
                Tuple tuple = (Tuple) iterator.next();
                System.out.println(tuple);
            }

		} catch (Exception e) {
			e.printStackTrace();

		}finally{
			cleanUp();
		}

		
	}
}
