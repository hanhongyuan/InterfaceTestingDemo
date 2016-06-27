package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DataReader {
	// protected static final Logger logger =
	// LoggerFactory.getLogger(DataReader.class);

	private HashMap<String, RecordHandler> map = new HashMap<String, RecordHandler>();
	private Boolean booleanColumnName = false;
	private Boolean booleanRowKey = false;
	private List<String> headers = new ArrayList<String>();
	private Integer size = 0;

	public DataReader() {
	}

	/**
	 * Primary constructor. Uses Apache POI XSSF to pull data from given excel
	 * workbook sheet. Data is stored in a structure depending on the options
	 * from other parameters.
	 * 
	 * @param sheet
	 *            Given excel sheet.
	 * @param has_headers
	 *            Boolean used to specify if the data has a header or not. The
	 *            headers will be used as field keys.
	 * @param has_key_column
	 *            Boolean used to specify if the data has a column that should
	 *            be used for record keys.
	 * @param key_column
	 *            Integer used to specify the key column for record keys.
	 */
	public DataReader(XSSFSheet sheet, Boolean has_headers, Boolean has_key_column, Integer key_column) {

		XSSFRow myRow = null;
		HashMap<String, String> myList;
		size = 0;

		this.booleanColumnName = has_headers;
		this.booleanRowKey = has_key_column;

		// 如果有表头,获取当前的表头信息,得到表头的list,并且设置Size=1
		try {
			if (booleanColumnName) {
				myRow = sheet.getRow(0);
				for (Cell cell : myRow) {
					headers.add(cell.getStringCellValue());
				}
				size = 1;
			}

			// 每一行代表一个Record，对于每一个Record可以获取长度，读写或者是更新
			// size作为指针往下读数据，size++一直到sheet.getRow的值为空，停止按行读入数据
			for (; (myRow = sheet.getRow(size)) != null; size++) {
				myList = new HashMap<String, String>();
				// 判断是否有表头，然后读取内容；如果当前的列内容小于表头的列，则直接保持为空
				if (booleanColumnName) {
					for (int col = 0; col < headers.size(); col++) {
						if (col < myRow.getLastCellNum()) {
							myList.put(headers.get(col), getSheetCellValue(myRow.getCell(col))); // myRow.getCell(col).getStringCellValue());
						} else {
							myList.put(headers.get(col), "");
						}
					}
				} else {
					for (int col = 0; col < myRow.getLastCellNum(); col++) {
						myList.put(Integer.toString(col), getSheetCellValue(myRow.getCell(col)));
					}
				}

				// booleanRowKey：是否存在此列内容的Key，如果有，指出key_column;如果没有，直接按index的顺序存储record
				if (booleanRowKey) {
					if (myList.size() == 2 && key_column == 0) {
						map.put(getSheetCellValue(myRow.getCell(key_column)), new RecordHandler(myList.get(1)));
					} else if (myList.size() == 2 && key_column == 1) {
						map.put(getSheetCellValue(myRow.getCell(key_column)), new RecordHandler(myList.get(0)));
					} else {
						map.put(getSheetCellValue(myRow.getCell(key_column)), new RecordHandler(myList));
					}
				} else {
					map.put(Integer.toString(size), new RecordHandler(myList));
				}
			}
			
		} catch (Exception e) {
			// logger.error("Exception while loading data from Excel
			// sheet:"+e.getMessage());
		}
	}

	/**
	 * 将指定cell的内容转化为String
	 */
	private String getSheetCellValue(XSSFCell cell) {

		String value = "";
		try {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			value = cell.getStringCellValue();
		} catch (NullPointerException npe) {
			return "";
		}
		return value;
	}

	/**
	 * 获取代表整个sheet页面的HashMap
	 */
	public HashMap<String, RecordHandler> get_map() {

		return map;
	}

	/**
	 * Gets an entire record.
	 * 
	 * @param record
	 *            String key value for record to be returned.
	 * @return HashMap of key-value pairs representing the specified record.
	 */
	public RecordHandler get_record(String record) {
		RecordHandler result = new RecordHandler();
		if (map.containsKey(record)) {
			result = map.get(record);
		}
		return result;
	}
}
