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

		// ����б�ͷ,��ȡ��ǰ�ı�ͷ��Ϣ,�õ���ͷ��list,��������Size=1
		try {
			if (booleanColumnName) {
				myRow = sheet.getRow(0);
				for (Cell cell : myRow) {
					headers.add(cell.getStringCellValue());
				}
				size = 1;
			}

			// ÿһ�д���һ��Record������ÿһ��Record���Ի�ȡ���ȣ���д�����Ǹ���
			// size��Ϊָ�����¶����ݣ�size++һֱ��sheet.getRow��ֵΪ�գ�ֹͣ���ж�������
			for (; (myRow = sheet.getRow(size)) != null; size++) {
				myList = new HashMap<String, String>();
				// �ж��Ƿ��б�ͷ��Ȼ���ȡ���ݣ������ǰ��������С�ڱ�ͷ���У���ֱ�ӱ���Ϊ��
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

				// booleanRowKey���Ƿ���ڴ������ݵ�Key������У�ָ��key_column;���û�У�ֱ�Ӱ�index��˳��洢record
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
	 * ��ָ��cell������ת��ΪString
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
	 * ��ȡ��������sheetҳ���HashMap
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
