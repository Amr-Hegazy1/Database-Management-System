
/** * @author Wael Abouelsaadat */

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
// import javafx.util.Pair;

public class DBApp {

	private Metadata metadata;
	private Hashtable<String, Hashtable<String, bplustree>> htbltrees; // Table Name, Index Name, B+Tree

	public DBApp() throws FileNotFoundException, IOException {
		metadata = new Metadata();
		htbltrees = new Hashtable();
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
	public void createIndex(String strTableName,
			String strColName,
			String strIndexName) throws DBAppException {

		throw new DBAppException("not implemented yet");
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
			String strClusteringKeyValue,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		throw new DBAppException("not implemented yet");
	}

	// following method could be used to delete one or more rows.
	// htblColNameValue holds the key and value. This will be used in search
	// to identify which rows/tuples to delete.
	// htblColNameValue enteries are ANDED together
	public void deleteFromTable(String strTableName,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {

		throw new DBAppException("not implemented yet");

	}

	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
			String[] strarrOperators) throws DBAppException {

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

			int i = p.binarySearchTuples("id", 6);

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

}