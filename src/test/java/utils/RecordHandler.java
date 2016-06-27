package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Record处理器分三种，分别是Value/Map/Index_List
 * @author admin
 *
 */
public class RecordHandler {
	private enum RecordType {
		VALUE, NAMED_MAP, INDEXED_LIST
	}

	private String single_value = "";
	private HashMap<String, String> named_value_map = new HashMap<String, String>();
	private List<String> indexed_value_list = new ArrayList<String>();
	private RecordType recordType;
	
	
    //----------------------不同类型的构造函数-------------------------------------------------------
	public RecordHandler() {
		this("");
	}

	public RecordHandler(String value) {
		this.recordType = RecordType.VALUE;
		this.single_value = value;
	}

	public RecordHandler(HashMap<String, String> map) {
		this.recordType = RecordType.NAMED_MAP;
		this.named_value_map = map;
	}

	public RecordHandler(List<String> list) {

		this.recordType = RecordType.INDEXED_LIST;
		this.indexed_value_list = list;

	}

	public HashMap<String, String> get_map() {
		return named_value_map;
	}

	//--------------------获取不同Record的长度-----------------------------------------------------
	public int size() {
		int result = 0;

		if (recordType.equals(RecordType.VALUE)) {
			result = 1;
		} else if (recordType.equals(RecordType.NAMED_MAP)) {
			result = named_value_map.size();
		} else if (recordType.equals(RecordType.INDEXED_LIST)) {
			result = indexed_value_list.size();
		}

		return result;
	}

	//----------------------获取不同类型的Record的内容------------------------------------------
	//如果是Value，直接获取值；如果是Map，通过Key获取；如果是List，通过Index获取
	public String get() {
		String result = "";
		if (recordType.equals(RecordType.VALUE))
			result = single_value;
		else {
			System.out.println("Called get() on wrong type:" + recordType.toString());
		}
		return result;
	}

	public String get(String key) {
		String result = "";

		if (recordType.equals(RecordType.NAMED_MAP))
			result = named_value_map.get(key);

		return result;
	}

	public String get(Integer index) {
		String result = "";

		if (recordType.equals(RecordType.INDEXED_LIST))
			result = indexed_value_list.get(index);

		return result;
	}

	public Boolean set(String value) {
		Boolean result = false;

		if (recordType.equals(RecordType.VALUE)) {
			this.single_value = value;
			result = true;
		} else if (recordType.equals(RecordType.INDEXED_LIST)) {
			this.indexed_value_list.add(value);
			result = true;
		}

		return result;
	}

	//--------------------------设置Record的值--------------------------------------
	//如果是value直接设置，list可以添加或者是改变指定index的值，map添加键值对
	public Boolean set(String key, String value) {
		Boolean result = false;

		if (recordType.equals(RecordType.NAMED_MAP)) {
			this.named_value_map.put(key, value);
			result = true;
		}

		return result;
	}

	public Boolean set(Integer index, String value) {
		Boolean result = false;

		if (recordType.equals(RecordType.INDEXED_LIST)) {
			if (this.indexed_value_list.size() > index)
				this.indexed_value_list.set(index, value);

			result = true;
		}

		return result;
	}

	//判断是否有值
	public Boolean has(String value) {
		Boolean result = false;

		if (recordType.equals(RecordType.VALUE) && this.single_value.equals(value)) {
			result = true;
		} else if (recordType.equals(RecordType.NAMED_MAP) && this.named_value_map.containsKey(value)) {
			result = true;
		} else if (recordType.equals(RecordType.INDEXED_LIST) && this.indexed_value_list.contains(value)) {
			result = true;
		}

		return result;
	}

	//判断是否删除值
	public Boolean remove(String value) {
		Boolean result = false;

		if (recordType.equals(RecordType.VALUE) && this.single_value.equals(value)) {
			this.single_value = "";
			result = true;
		}
		if (recordType.equals(RecordType.NAMED_MAP) && this.named_value_map.containsKey(value)) {
			this.named_value_map.remove(value);
			result = true;
		} else if (recordType.equals(RecordType.INDEXED_LIST) && this.indexed_value_list.contains(value)) {
			this.indexed_value_list.remove(value);
			result = true;
		}

		return result;
	}

	public Boolean remove(Integer index) {
		Boolean result = false;

		if (recordType.equals(RecordType.INDEXED_LIST) && this.indexed_value_list.contains(index)) {
			this.indexed_value_list.remove(index);
			result = true;
		}

		return result;
	}
}
