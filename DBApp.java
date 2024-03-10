
/** * @author Wael Abouelsaadat */

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Hashtable;

public class DBApp {

	private Metadata metadata;
	private Hashtable<String, Table> tables; // TODO: probably will be removed, because this way it loads *ALL* tables
												// into memory

	public DBApp() throws FileNotFoundException, IOException {
		metadata = new Metadata();
		tables = new Hashtable<>();
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

		HashSet<String> strClusteredColumns = new HashSet<String>();
		HashSet<String> strNonClusteredColumns = new HashSet<String>();
		Tuple tupleNewTuple = new Tuple();
		String strClustKeyName = "";
		// Checking Table Existence, Correct Col Names, Correct Col Data Types

		// TODO; Checking clustering key duplicate inputs (Hashset??)
		// TODO: Do i deserialize the table?
		if (metadata.checkTableName(strTableName)) {
			throw new DBAppException("Table Doesnt Exist");
		} else {
			for (Map.Entry<String, Object> entry : htblColNameValue.entrySet()) {
				if (!metadata.checkColumnName(strTableName, entry.getKey())) {
					throw new DBAppException("Column Doesnt Exist");

				} else {
					String strCorrectDataType = metadata.getColumnType(strTableName, entry.getKey());
					String strActualDataType = entry.getValue().getClass().getName();
					if (!strCorrectDataType.equals(strActualDataType)) {
						throw new DBAppException("Data Type Mismatch");
					}
					if (metadata.isClusteringKey(strTableName, entry.getKey())) {
						if (entry.getValue() == null) {
							throw new DBAppException("Clustering Key Cannot Be Null");
						}
						strClustKeyName = entry.getKey();
					} else if (!(metadata.getIndexType(strTableName, entry.getKey())).equals("null")) {
						strClusteredColumns.add(entry.getKey());
						tupleNewTuple.setColumnValue(entry.getKey(), entry.getValue());
					} else {
						strNonClusteredColumns.add(entry.getKey());
						tupleNewTuple.setColumnValue(entry.getKey(), entry.getValue());
					}

				}

			}
		}

		Table tblTable = Table.deserialize(strTableName);
		Vector<String> vecPages = tblTable.getPageVector();
		Properties prop = new Properties();
		String fileName = "DBApp.config";
		FileInputStream fis = new FileInputStream(fileName);
		prop.load(fis);

		// Inserting w/o any Indicies

		int intMaxSize = Integer.parseInt(prop.getProperty("MaximumRowsCountinPage"));
		int intpageNum = tblTable.getNumofPages();
		boolean boolMaxValueisPassed = false;
		boolean boolSkipPage = false;
		Tuple tupleLastTuple = new Tuple();

		// TODO: check in page that its in range of your pk value.
		// TODO: what if overflow happens in more than one page?
		if (intpageNum != 0) {
			for (String strPageName : vecPages) {
				boolSkipPage = false;
				boolMaxValueisPassed = false;
				Page page = Page.deserialize(strPageName);
				// checking if page has correct range for the clustering key
				Tuple firstTuple = page.getFirstTuple();
				Tuple lastTuple = page.getLastTuple();

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

				if (!boolSkipPage) {
					int index = page.searchTuplesByClusteringKey(strClustKeyName,
							htblColNameValue.get(strClustKeyName));
					page.addTuple(index, tupleNewTuple);
					if (page.getSize() > intMaxSize) {
						boolMaxValueisPassed = true;
						tupleLastTuple = page.removeLastTuple(); // getLastTuple() returns the last tuple and removes it
																	// from the page
						page.serialize(strPageName);
					} else {
						page.serialize(strPageName);
					}

					if (boolMaxValueisPassed) {
						Page newPage = new Page();
						newPage.addTuple(0, tupleLastTuple);
						String strNewPageName = tblTable.addPage(); // addPage() adds page to the table Vector and
																	// assigns
																	// it an automated Page Name & returns that name
						newPage.serialize(strNewPageName);
					}
				}

			}

		} else {
			Page newPage = new Page();
			newPage.addTuple(0, tupleNewTuple);
			String strPageName = tblTable.addPage(); // addPage() adds page to the table Vector and assigns it an
														// automated Page Name & returns that name
			newPage.serialize(strPageName);
		}

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

			Page page = new Page();
			Tuple tuple = new Tuple();
			tuple.setColumnValue("name", "Ahmed");
			tuple.setColumnValue("age", 20);
			// page.addTuple(tuple);

			page.serialize("serializedPage.ser"); // try to make it .class

			// Later, you can deserialize the Page object from the file
			Page deserializedPage = Page
					.deserialize("serializedPage.ser");

			// Now you can access the tuples
			System.out.print(deserializedPage);
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