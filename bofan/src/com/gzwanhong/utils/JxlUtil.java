package com.gzwanhong.utils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jxl.Cell;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class JxlUtil {
	/**
	 * 用指定的模板创建一个EXCEL
	 * 
	 * @param templatePath
	 *            模板路径
	 * @param excelPath
	 *            生成的excel要放的路径
	 * @param startRow
	 *            从哪行开始写数据
	 * @param writeSize
	 *            多少行保存一次，以防内存溢出
	 * @param fixedDataList
	 *            最开始加载的一些数据
	 * @param firstRowName
	 *            第一行的名字，即表头
	 * @param columnList
	 *            列名的集合，跟dataList里对应，用于在dataList里取值用
	 * @param dataList
	 *            数据集体
	 * @param lastDataList
	 *            加载完中间数据后，最后填写的数据
	 * @param cellFormatMap
	 *            每列的格式化信息
	 * @param valMaps
	 *            值的对应转换
	 * @throws Exception
	 */
	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			List<Map<String, Object>> fixedDataList, String[] firstRowName, String[] columnList,
			List<Map<String, Object>> dataList, List<Map<String, Object>> lastDataList,
			Map<String, CellFormat> cellFormatMap, Map<String, Map<String, Object>> valMaps) throws Exception {
		WritableWorkbook wb = null;
		Workbook template = null;

		if (!WhUtil.isEmpty(templatePath)) {
			// 加载模板
			template = Workbook.getWorkbook(new File(templatePath));
			// 创建目标excel
			wb = Workbook.createWorkbook(new File(excelPath), template);
		} else {
			File aa = new File(excelPath);
			wb = Workbook.createWorkbook(new File(excelPath));
		}

		// 获取sheet
		wb.createSheet("表1", 0);
		WritableSheet sheet = wb.getSheet(0);

		// 设置固定位置的数据
		Map<String, Object> map = null;
		Label label = null;
		if (!WhUtil.isEmpty(fixedDataList) && fixedDataList.size() > 0) {
			for (int i = 0; i < fixedDataList.size(); i++) {
				map = fixedDataList.get(i);
				label = new Label(WhUtil.toInteger(map.get("column")), WhUtil.toInteger(map.get("row")),
						WhUtil.toString(map.get("data")));
				sheet.addCell(label);
			}
		}

		// 设置表头
		if (!WhUtil.isEmpty(firstRowName)) {
			for (int i = 0; i < firstRowName.length; i++) {
				label = new Label(i, 0, firstRowName[i]);
				sheet.addCell(label);
			}
		}

		// 开始循环数据
		Map<String, Object> dataMap = null;
		jxl.write.Number num = null;
		Object val = null;
		String columnName = null;
		String[] columnNames = null;
		Workbook workbookTemp = null;
		CellFormat cellFormat = null;
		Map<String, Object> valMap = null;
		int rowIndex = startRow;

		if (!WhUtil.isEmpty(dataList)) {
			int dataCount = dataList.size();
			int columnCount = columnList.length;
			for (int dataIndex = 0; dataIndex < dataCount; dataIndex++) {
				dataMap = dataList.get(dataIndex);

				// 循环列
				for (int columIndex = 0; columIndex < columnCount; columIndex++) {
					columnName = columnList[columIndex];
					val = dataMap.get(columnName);

					// 如果有加号的话就是多列的值加一起
					if (columnName.contains("+")) {
						columnNames = columnName.split("\\+");
						for (int i = 0; i < columnNames.length; i++) {
							if (i == 0) {
								val = dataMap.get(columnNames[i]);
							} else {
								if (val instanceof Integer || val instanceof Long || val instanceof Float
										|| val instanceof Double || val instanceof BigDecimal) {
									val = WhUtil.toDouble(val) + WhUtil.toDouble(dataMap.get(columnNames[i]));
								} else {
									val = WhUtil.toString(val) + WhUtil.toString(dataMap.get(columnNames[i]));
								}
							}
						}
					}

					// 如果是有大于号的话，就是处理出最大的那个值
					if (columnName.contains(">")) {
						columnNames = columnName.split("\\>");
						for (int i = 0; i < columnNames.length; i++) {
							if (i == 0) {
								val = dataMap.get(columnNames[i]);
							} else {
								if (WhUtil.toDouble(dataMap.get(columnNames[i])).doubleValue() > WhUtil.toDouble(val)
										.doubleValue()) {
									val = dataMap.get(columnNames[i]);
								}
							}
						}
					}

					// 值的转换
					if (!WhUtil.isEmpty(valMaps) && !WhUtil.isEmpty(valMaps.get(columnName))) {
						valMap = valMaps.get(columnName);
						val = valMap.get(WhUtil.toString(val));
					}

					if (!WhUtil.isEmpty(cellFormatMap)) {
						cellFormat = cellFormatMap.get(columnName);
					}

					if ("@seq".equals(columnName)) {
						// 序号列
						if (WhUtil.isEmpty(cellFormat)) {
							num = new Number(columIndex, rowIndex, dataIndex + 1);
						} else {
							num = new Number(columIndex, rowIndex, dataIndex + 1, cellFormat);
						}
						sheet.addCell(num);
					} else if (!WhUtil.isEmpty(val)) {
						if (val instanceof Integer || val instanceof Long || val instanceof Float
								|| val instanceof Double || val instanceof BigDecimal) {
							if (WhUtil.isEmpty(cellFormat)) {
								num = new Number(columIndex, rowIndex, WhUtil.toDouble(val));
							} else {
								num = new Number(columIndex, rowIndex, WhUtil.toDouble(val), cellFormat);
							}
							sheet.addCell(num);
						} else {
							if (WhUtil.isEmpty(cellFormat)) {
								label = new Label(columIndex, rowIndex, WhUtil.toString(val));
							} else {
								label = new Label(columIndex, rowIndex, WhUtil.toString(val), cellFormat);

							}
							sheet.addCell(label);
						}
					} else {
						// 值为空的时候，也要看一下格式要不要设置
						if (!WhUtil.isEmpty(cellFormat)) {
							label = new Label(columIndex, rowIndex, "", cellFormat);
							sheet.addCell(label);
						}
					}
				}

				// 根据设定的每多少条数据就写一次，以免内存溢出
				if ((dataIndex + 1) % writeSize == 0) {
					wb.write();
					wb.close();

					if (!WhUtil.isEmpty(workbookTemp)) {
						workbookTemp.close();
					}

					workbookTemp = Workbook.getWorkbook(new File(excelPath));
					wb = Workbook.createWorkbook(new File(excelPath), workbookTemp);
					sheet = wb.getSheet(0);
				}

				rowIndex++;
			}
		}

		// 设置最后面的数据
		if (!WhUtil.isEmpty(lastDataList) && lastDataList.size() > 0) {
			int rowCount = sheet.getRows();
			for (int i = 0; i < lastDataList.size(); i++) {
				map = lastDataList.get(i);
				label = new Label(WhUtil.toInteger(map.get("column")), WhUtil.toInteger(map.get("row")) + rowCount,
						WhUtil.toString(map.get("data")));
				sheet.addCell(label);
			}
		}

		wb.write();
		wb.close();
		if (!WhUtil.isEmpty(workbookTemp)) {
			workbookTemp.close();
		}

		if (!WhUtil.isEmpty(template)) {
			template.close();
		}
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			String[] columnList, List<Map<String, Object>> dataList) throws Exception {
		JxlUtil.createExcel(templatePath, excelPath, startRow, writeSize, null, null, columnList, dataList, null, null,
				null);
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			List<Map<String, Object>> fixedDataList, String[] columnList, List<Map<String, Object>> dataList)
			throws Exception {
		JxlUtil.createExcel(templatePath, excelPath, startRow, writeSize, fixedDataList, null, columnList, dataList,
				null, null, null);
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			String[] columnList, List<Map<String, Object>> dataList, List<Map<String, Object>> lastDataList)
			throws Exception {
		JxlUtil.createExcel(templatePath, excelPath, startRow, writeSize, null, null, columnList, dataList,
				lastDataList, null, null);
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			List<Map<String, Object>> fixedDataList, String[] columnList, List<Map<String, Object>> dataList,
			List<Map<String, Object>> lastDataList) throws Exception {
		JxlUtil.createExcel(templatePath, excelPath, startRow, writeSize, null, null, columnList, dataList,
				lastDataList, null, null);
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			String[] columnList, List<Map<String, Object>> dataList, Map<String, CellFormat> cellFormatMap)
			throws Exception {
		JxlUtil.createExcel(templatePath, excelPath, startRow, writeSize, null, null, columnList, dataList, null,
				cellFormatMap, null);
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			List<Map<String, Object>> fixedDataList, String[] columnList, List<Map<String, Object>> dataList,
			Map<String, CellFormat> cellFormatMap) throws Exception {
		JxlUtil.createExcel(templatePath, excelPath, startRow, writeSize, fixedDataList, null, columnList, dataList,
				null, cellFormatMap, null);
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			String[] columnList, List<Map<String, Object>> dataList, List<Map<String, Object>> lastDataList,
			Map<String, CellFormat> cellFormatMap) throws Exception {
		JxlUtil.createExcel(templatePath, excelPath, startRow, writeSize, null, null, columnList, dataList,
				lastDataList, cellFormatMap, null);
	}

	public static void createExcel(String excelPath, int startRow, int writeSize, String[] firstRowName,
			String[] columnList, List<Map<String, Object>> dataList,

			Map<String, CellFormat> cellFormatMap) throws Exception {

		createExcel(null, excelPath, startRow, writeSize, null, firstRowName, columnList, dataList, null, cellFormatMap,
				null);
	}

	public static void createExcel(String excelPath, int startRow, int writeSize, String[] firstRowName,
			String[] columnList, List<Map<String, Object>> dataList,

			Map<String, CellFormat> cellFormatMap, Map<String, Map<String, Object>> valMaps) throws Exception {

		createExcel(null, excelPath, startRow, writeSize, null, firstRowName, columnList, dataList, null, cellFormatMap,
				valMaps);
	}

	// json配置文件的格式:
	// {
	// "classMsg":{ //类型信息字段，里面说明结果返回的对象名称跟它对应的类型，这将在翻译数据跟结果返回中起到重要作用
	// "billList":"com.gzwanhong.domain.Bill",
	// "billDetailList":"com.gzwanhong.domain.BillDetail"
	// },
	// "startRow":1, //说明从excel的哪行开始读取数据
	// "startSheet":0, //说明从excel的第几个工作表读取数据
	// "emptyClass":{ //判断是否为空的对象，比如这里，就是读每一行的数据时，如果第0列为空，则billList这个对象不取那一行的数据
	// "billList":0
	// },
	// "columnMsg":[ //每一列的信息
	// {
	// "objectName":"billList", //说明该列的值将会保存在billList这个对象集合里
	// "columnIndex":0, //说明该字段对应到excel中的第几列
	// "methodName":"setBillCode", //说明该字段在billList对象类型里对应的set方法
	// "argClass":"java.lang.String" //set方法的参数类型
	// },
	// {
	// "objectName":"billList",
	// "columnIndex":1,
	// "methodName":"setBillNumber",
	// "argClass":"java.lang.Integer"
	// },
	// {
	// "objectName":"billList",
	// "columnIndex":2,
	// "methodName":"setMoneyLower",
	// "argClass":"java.lang.Double"
	// },
	// {
	// "objectName":"billList",
	// "columnIndex":3,
	// "methodName":"setCreateDate",
	// "argClass":"java.util.Date"
	// },
	// {
	// "objectName":"billDetailList",
	// "columnIndex":4,
	// "methodName":"setInfo",
	// "argClass":"java.lang.String"
	// },
	// {
	// "objectName":"billDetailList",
	// "columnIndex":5,
	// "methodName":"setPrice",
	// "argClass":"java.lang.Double"
	// }
	// ]
	// }

	/**
	 * 把excel转成对象集合
	 * 
	 * @param excelPath
	 *            上传的excel路径
	 * @param optionList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertToBean(File excelFile, Map<String, Object> optionMap, Boolean trimFlag)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int rowIndex = -1;
		int sheetIndex = 0;
		int columnIndex = 0;

		try {
			// 定义要返回的对象集合，并定义类跟对象的map，方便待会组装数据用
			List<Object> dataList = null;
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			Map<String, Object> objectMap = new HashMap<String, Object>();
			Map<String, String> classMsg = (Map<String, String>) optionMap.get("classMsg");

			for (String objectName : classMsg.keySet()) {
				dataList = new ArrayList<Object>();
				resultMap.put(objectName, dataList);
				classMap.put(objectName, Class.forName(classMsg.get(objectName)));
			}

			// 读取上传的excel
			Workbook excel;
			try {
				excel = Workbook.getWorkbook(excelFile);
			} catch (Exception e) {
				throw new WhException("文件读取出错");
			}

			// 默认获取第一个工作表，如果有配置取第几个的话就取指定的那个
			Sheet sheet = null;
			if (!WhUtil.isEmpty(optionMap.get("startSheet"))) {
				sheetIndex = WhUtil.toInteger(optionMap.get("startSheet"));

				// 如果是大于工作表总数的话，就不读取
				if (sheetIndex > excel.getNumberOfSheets() - 1) {
					return resultMap;
				}

				sheet = excel.getSheet(sheetIndex);
			} else {
				sheet = excel.getSheet(0);
			}
			// 获取工作表的行数跟列数
			int rowSize = sheet.getRows();
			int columnSize = sheet.getColumns();

			// 开始循环数据表，装载成对象集合
			List<Map<String, Object>> columnMsg = (List<Map<String, Object>>) optionMap.get("columnMsg");
			// 取出从第几行开始取数据
			rowIndex = WhUtil.toInteger(optionMap.get("startRow"));
			// 取出指定列为空的话就不取那行数据的信息
			Map<String, Integer> emptyClass = (Map<String, Integer>) optionMap.get("emptyClass");
			// 取出指定列为标识，相同标识的只取一条发票头
			Map<String, Integer> flagCol = (Map<String, Integer>) optionMap.get("flagCol");

			Map<String, Boolean> flagMap = new HashMap<String, Boolean>(); // 记录标识用
			String objectName = null;
			Object object = null;
			Object val = null;
			String contentStr = null;
			Map<String, Object> valMap = new HashMap<String, Object>();
			Class<?> clzz = null;
			String argClass = null;
			Cell cell = null;
			Method method = null;
			for (; rowIndex < rowSize; rowIndex++) {
				// 定义对象，循环classMap来设定
				objectMap = new HashMap<String, Object>();
				for (String name : classMap.keySet()) {
					// 先看下该行要不要添加数据，根据emptyClass，指定看哪列的数据是否为空，空的话该类型这行就不取值
					if (!WhUtil.isEmpty(emptyClass) && !WhUtil.isEmpty(emptyClass.get(name))) {
						// 取出该行指定列的值
						cell = sheet.getCell(WhUtil.toInteger(emptyClass.get(name)), rowIndex);

						// 如果该单元格的值为空的话就跳出本次循环，则该类型不取本行的数据
						if (WhUtil.isEmpty(cell.getContents().trim())) {
							continue;
						}
					}

					// 再看下该行要不要添加数据，根据flagCol，指定看哪列的标识是否已处理过,处理过的标识都会记录在flagMap里，处理过的则不再处理
					if (!WhUtil.isEmpty(flagCol) && !WhUtil.isEmpty(flagCol.get(name))) {
						// 取出该行指定列的值
						cell = sheet.getCell(WhUtil.toInteger(flagCol.get(name)), rowIndex);

						// 如果该单元的标识在flagMap里已有，则该类型不取本行的数据
						if (!WhUtil.isEmpty(cell.getContents()) && !WhUtil.isEmpty(flagMap.get(cell.getContents()))) {
							continue;
						}
						flagMap.put(cell.getContents(), true);
					}

					clzz = classMap.get(name);
					object = clzz.newInstance();

					// 放到objectMap里去
					objectMap.put(name, object);
				}

				// 循环列信息
				for (Map<String, Object> columnMap : columnMsg) {
					// 得到该列的对象名
					objectName = columnMap.get("objectName").toString();

					// 看该类型的该行要不要取数据
					if (WhUtil.isEmpty(objectMap.get(objectName))) {
						continue;
					}

					// 得到类
					clzz = classMap.get(objectName);
					// 得到函数的参数类型
					argClass = columnMap.get("argClass").toString();
					// 定义方法
					method = clzz.getMethod(columnMap.get("methodName").toString(), Class.forName(argClass));

					// 取出该列的cell,如果columnIndex为空的话，则看是否有设默认值
					if (!WhUtil.isEmpty(columnMap.get("columnIndex"))) {
						columnIndex = WhUtil.toInteger(columnMap.get("columnIndex"));
						if (columnIndex < columnSize) {
							cell = sheet.getCell(WhUtil.toInteger(columnMap.get("columnIndex")), rowIndex);
							contentStr = cell.getContents();
						} else {
							contentStr = "";
						}
					} else if (!WhUtil.isEmpty(columnMap.get("defaultVal"))) {
						contentStr = WhUtil.toString(columnMap.get("defaultVal"));
					}

					// 看该单元格的值是否为空，为空的话看是否要向上面的行取值
					if (WhUtil.isEmpty(contentStr) && !WhUtil.isEmpty(columnMap.get("preRow"))
							&& WhUtil.toBoolean(columnMap.get("preRow"))) {
						// 循环的往上找数据
						for (int i = rowIndex - 1; i >= 0; i--) {
							cell = sheet.getCell(WhUtil.toInteger(columnMap.get("columnIndex")), i);
							if (!WhUtil.isEmpty(cell.getContents())) {
								contentStr = cell.getContents();
								break;
							}
						}
					}

					// 看是否需要值的转换
					if (!WhUtil.isEmpty(columnMap.get("valMap")) && !WhUtil.isEmpty(contentStr)) {
						valMap = (Map<String, Object>) columnMap.get("valMap");
						contentStr = WhUtil.toString(valMap.get(contentStr));
					}

					if (!WhUtil.isEmpty(contentStr)) {
						if ("java.lang.Long".equals(argClass)) {
							val = WhUtil.toLong(contentStr);
						} else if ("java.lang.Integer".equals(argClass)) {
							val = WhUtil.toInteger(contentStr);
						} else if ("java.lang.Double".equals(argClass)) {
							val = WhUtil.toDouble(contentStr);
						} else if ("java.util.Date".equals(argClass)) {
							val = ((DateCell) cell).getDate();
						} else {
							if (trimFlag) {
								val = contentStr.replace(" ", "").trim();
							} else {
								val = contentStr;
							}
						}

						// 把值设进对象里去
						method.invoke(objectMap.get(objectName), val);
					}
				}

				// 把经过处理后的对象放进结果集里去
				for (String name : objectMap.keySet()) {
					dataList = (List<Object>) resultMap.get(name);
					dataList.add(objectMap.get(name));
					resultMap.put(name, dataList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (rowIndex == -1) {
				throw new WhException("Excel数据第 " + (sheetIndex + 1) + " 个表，读取Excel文件出错");
			} else {
				throw new WhException("Excel数据第 " + (sheetIndex + 1) + " 个表，第 " + (rowIndex + 1) + " 行，第 "
						+ (columnIndex + 1) + " 列有误\n" + e.getMessage());
			}
		}

		return resultMap;
	}

	/**
	 * 把excel转成对象集合
	 * 
	 * @param excelPath
	 *            上传的excel路径
	 * @param optionList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertToBeanPoi(File excelFile, Map<String, Object> optionMap, Boolean trimFlag)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int rowIndex = -1;
		int sheetIndex = 0;
		int columnIndex = 0;

		try {
			// 定义要返回的对象集合，并定义类跟对象的map，方便待会组装数据用
			List<Object> dataList = null;
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			Map<String, Object> objectMap = new HashMap<String, Object>();
			Map<String, String> classMsg = (Map<String, String>) optionMap.get("classMsg");

			for (String objectName : classMsg.keySet()) {
				dataList = new ArrayList<Object>();
				resultMap.put(objectName, dataList);
				classMap.put(objectName, Class.forName(classMsg.get(objectName)));
			}

			// 读取上传的excel
			org.apache.poi.ss.usermodel.Workbook excel = null;

			try {
				// 2007版本
				excel = new XSSFWorkbook(new FileInputStream(excelFile));
			} catch (Exception e) {
				excel = new HSSFWorkbook(new FileInputStream(excelFile));
			}

			// 默认获取第一个工作表，如果有配置取第几个的话就取指定的那个
			org.apache.poi.ss.usermodel.Sheet sheet = null;
			if (!WhUtil.isEmpty(optionMap.get("startSheet"))) {
				sheetIndex = WhUtil.toInteger(optionMap.get("startSheet"));

				// 如果是大于工作表总数的话，就不读取
				if (sheetIndex > excel.getNumberOfSheets() - 1) {
					return resultMap;
				}

				sheet = excel.getSheetAt(sheetIndex);
			} else {
				sheet = excel.getSheetAt(0);
			}
			// 获取工作表的行数跟列数
//			int rowSize = sheet.getPhysicalNumberOfRows();
			int rowSize = sheet.getLastRowNum()+1;
			int columnSize = 0;

			// 开始循环数据表，装载成对象集合
			List<Map<String, Object>> columnMsg = (List<Map<String, Object>>) optionMap.get("columnMsg");
			// 取出从第几行开始取数据
			rowIndex = WhUtil.toInteger(optionMap.get("startRow"));
			// 取出指定列为空的话就不取那行数据的信息
			Map<String, Integer> emptyClass = (Map<String, Integer>) optionMap.get("emptyClass");
			// 取出指定列为标识，相同标识的只取一条发票头
			Map<String, Integer> flagCol = (Map<String, Integer>) optionMap.get("flagCol");

			Map<String, Boolean> flagMap = new HashMap<String, Boolean>(); // 记录标识用
			String objectName = null;
			Object object = null;
			Object val = null;
			String contentStr = null;
			Map<String, Object> valMap = new HashMap<String, Object>();
			Class<?> clzz = null;
			String argClass = null;
			Row row = null;
			org.apache.poi.ss.usermodel.Cell cell = null;
			Comment comment = null;
			Method method = null;
			for (; rowIndex < rowSize; rowIndex++) {
				// 取出当前行
				row = sheet.getRow(rowIndex);
				
				if(WhUtil.isEmpty(row)){
					continue;
				}
				
				columnSize = row.getPhysicalNumberOfCells();

				// 定义对象，循环classMap来设定
				objectMap = new HashMap<String, Object>();
				for (String name : classMap.keySet()) {
					// 先看下该行要不要添加数据，根据emptyClass，指定看哪列的数据是否为空，空的话该类型这行就不取值
					if (!WhUtil.isEmpty(emptyClass) && !WhUtil.isEmpty(emptyClass.get(name))) {
						// 取出该行指定列的值
						cell = row.getCell(WhUtil.toInteger(emptyClass.get(name)));

						// 如果该单元格的值为空的话就跳出本次循环，则该类型不取本行的数据
						if (WhUtil.isEmpty(cell) || WhUtil.isEmpty(cell.getStringCellValue())) {
							continue;
						}
					}

					// 再看下该行要不要添加数据，根据flagCol，指定看哪列的标识是否已处理过,处理过的标识都会记录在flagMap里，处理过的则不再处理
					if (!WhUtil.isEmpty(flagCol) && !WhUtil.isEmpty(flagCol.get(name))) {
						// 取出该行指定列的值
						cell = row.getCell(WhUtil.toInteger(flagCol.get(name)));

						// 如果该单元的标识在flagMap里已有，则该类型不取本行的数据
						if (!WhUtil.isEmpty(cell.getStringCellValue())
								&& !WhUtil.isEmpty(flagMap.get(cell.getStringCellValue()))) {
							continue;
						}
						flagMap.put(cell.getStringCellValue(), true);
					}

					clzz = classMap.get(name);
					object = clzz.newInstance();

					// 放到objectMap里去
					objectMap.put(name, object);
				}

				// 循环列信息
				for (Map<String, Object> columnMap : columnMsg) {
					// 得到该列的对象名
					objectName = columnMap.get("objectName").toString();

					// 看该类型的该行要不要取数据
					if (WhUtil.isEmpty(objectMap.get(objectName))) {
						continue;
					}

					// 得到类
					clzz = classMap.get(objectName);
					// 得到函数的参数类型
					argClass = columnMap.get("argClass").toString();
					// 定义方法
					method = clzz.getMethod(columnMap.get("methodName").toString(), Class.forName(argClass));

					// 取出该列的cell,如果columnIndex为空的话，则看是否有设默认值
					if (!WhUtil.isEmpty(columnMap.get("columnIndex"))) {
						columnIndex = WhUtil.toInteger(columnMap.get("columnIndex"));
						if (columnIndex < columnSize) {
							cell = row.getCell(WhUtil.toInteger(columnMap.get("columnIndex")));

							if(!WhUtil.isEmpty(columnMap.get("isComment")) && WhUtil.toBoolean(columnMap.get("isComment"))){
								//如果是要在附注里读内容的话
								comment = cell.getCellComment();
								if(!WhUtil.isEmpty(comment) && !WhUtil.isEmpty(comment.getString()) && !WhUtil.isEmpty(comment.getString().getString())){
									contentStr = comment.getString().getString();
								}else {
									contentStr = "";
								}
							} else {
								//不是在附注里读内容
								if (org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
									contentStr = WhUtil.toString(cell.getNumericCellValue());
								} else {
									contentStr = cell.getStringCellValue();
								}
							}
						} else {
							contentStr = "";
						}
					} else if (!WhUtil.isEmpty(columnMap.get("defaultVal"))) {
						contentStr = WhUtil.toString(columnMap.get("defaultVal"));
					}

					// 看该单元格的值是否为空，为空的话看是否要向上面的行取值
					if (WhUtil.isEmpty(contentStr) && !WhUtil.isEmpty(columnMap.get("preRow"))
							&& WhUtil.toBoolean(columnMap.get("preRow"))) {
						// 循环的往上找数据
						for (int i = rowIndex - 1; i >= 0; i--) {
							row = sheet.getRow(i);
							cell = row.getCell(WhUtil.toInteger(columnMap.get("columnIndex")));
							if (!WhUtil.isEmpty(cell.getStringCellValue())) {
								contentStr = cell.getStringCellValue();
								break;
							}
						}
					}

					// 看是否需要值的转换
					if (!WhUtil.isEmpty(columnMap.get("valMap")) && !WhUtil.isEmpty(contentStr)) {
						valMap = (Map<String, Object>) columnMap.get("valMap");
						contentStr = WhUtil.toString(valMap.get(contentStr));
					}

					if (!WhUtil.isEmpty(contentStr)) {
						if ("java.lang.Long".equals(argClass)) {
							val = WhUtil.toLong(contentStr);
						} else if ("java.lang.Integer".equals(argClass)) {
							val = WhUtil.toInteger(contentStr);
						} else if ("java.lang.Double".equals(argClass)) {
							val = WhUtil.toDouble(contentStr);
						} else if ("java.util.Date".equals(argClass)) {
							val = cell.getDateCellValue();
						} else {
							if (trimFlag) {
								val = contentStr.replace(" ", "").trim();
							} else {
								val = contentStr;
							}
						}

						// 把值设进对象里去
						method.invoke(objectMap.get(objectName), val);
					}
				}

				// 把经过处理后的对象放进结果集里去
				for (String name : objectMap.keySet()) {
					dataList = (List<Object>) resultMap.get(name);
					dataList.add(objectMap.get(name));
					resultMap.put(name, dataList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (rowIndex == -1) {
				throw new WhException("Excel数据第 " + (sheetIndex + 1) + " 个表，读取Excel文件出错");
			} else {
				throw new WhException("Excel数据第 " + (sheetIndex + 1) + " 个表，第 " + (rowIndex + 1) + " 行，第 "
						+ (columnIndex + 1) + " 列有误\n" + e.getMessage());
			}
		}

		return resultMap;
	}

	/**
	 * 把excel转成对象集合
	 * 
	 * @param excelPath
	 *            上传的excel路径
	 * @param optionList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertToBean2(File excelPath, Map<String, Object> optionMap, Boolean trimFlag)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int rowIndex = 0;
		int columnIndex = 0;

		try {
			// 定义要返回的对象集合，并定义类跟对象的map，方便待会组装数据用
			List<Object> dataList = null;
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			Map<String, Object> objectMap = new HashMap<String, Object>();
			Map<String, String> classMsg = (Map<String, String>) optionMap.get("classMsg");

			for (String objectName : classMsg.keySet()) {
				dataList = new ArrayList<Object>();
				resultMap.put(objectName, dataList);
				classMap.put(objectName, Class.forName(classMsg.get(objectName)));
			}

			// 读取上传的excel
			org.apache.poi.ss.usermodel.Workbook excel = null;

			try {
				// 2007版本
				excel = new XSSFWorkbook(new FileInputStream(excelPath));
			} catch (Exception e) {
				excel = new HSSFWorkbook(new FileInputStream(excelPath));
			}

			// 获取第一个工作表
			org.apache.poi.ss.usermodel.Sheet sheet = excel.getSheetAt(0);

			// 获取工作表的行数跟列数
			int rowSize = sheet.getLastRowNum() + 1;

			// 开始循环数据表，装载成对象集合
			List<Map<String, Object>> columnMsg = (List<Map<String, Object>>) optionMap.get("columnMsg");
			// 取出从第几行开始取数据
			rowIndex = WhUtil.toInteger(optionMap.get("startRow"));
			// 取出指定列为空的话就不取那行数据的信息
			Map<String, Integer> emptyClass = (Map<String, Integer>) optionMap.get("emptyClass");
			// 取出指定列为标识，相同标识的只取一条发票头
			Map<String, Integer> flagCol = (Map<String, Integer>) optionMap.get("flagCol");

			Map<String, Boolean> flagMap = new HashMap<String, Boolean>(); // 记录标识用
			String objectName = null;
			Object object = null;
			Object val = null;
			String contentStr = null;
			Map<String, Object> valMap = new HashMap<String, Object>();
			Class<?> clzz = null;
			String argClass = null;
			Row row = null;
			org.apache.poi.ss.usermodel.Cell cell = null;
			Method method = null;
			for (; rowIndex < rowSize; rowIndex++) {
				// 定义对象，循环classMap来设定
				objectMap = new HashMap<String, Object>();
				for (String name : classMap.keySet()) {
					// 先看下该行要不要添加数据，根据emptyClass，指定看哪列的数据是否为空，空的话该类型这行就不取值
					if (!WhUtil.isEmpty(emptyClass) && !WhUtil.isEmpty(emptyClass.get(name))) {
						// 取出行
						row = sheet.getRow(rowIndex);

						// 取出该行指定列的值
						cell = row.getCell(WhUtil.toInteger(emptyClass.get(name)));

						// 如果该单元格的值为空的话就跳出本次循环，则该类型不取本行的数据
						if (WhUtil.isEmpty(cell.getStringCellValue())) {
							continue;
						}
					}

					// 再看下该行要不要添加数据，根据flagCol，指定看哪列的标识是否已处理过,处理过的标识都会记录在flagMap里，处理过的则不再处理
					if (!WhUtil.isEmpty(flagCol) && !WhUtil.isEmpty(flagCol.get(name))) {
						// 取出行
						row = sheet.getRow(rowIndex);

						// 取出该行指定列的值
						cell = row.getCell(WhUtil.toInteger(flagCol.get(name)));

						// 如果该单元的标识在flagMap里已有，则该类型不取本行的数据
						if (!WhUtil.isEmpty(cell.getStringCellValue())
								&& !WhUtil.isEmpty(flagMap.get(cell.getStringCellValue()))) {
							continue;
						}
						flagMap.put(cell.getStringCellValue(), true);
					}

					clzz = classMap.get(name);
					object = clzz.newInstance();

					// 放到objectMap里去
					objectMap.put(name, object);
				}

				// 循环列信息
				for (Map<String, Object> columnMap : columnMsg) {
					// 得到该列的对象名
					objectName = columnMap.get("objectName").toString();

					// 看该类型的该行要不要取数据
					if (WhUtil.isEmpty(objectMap.get(objectName))) {
						continue;
					}

					// 得到类
					clzz = classMap.get(objectName);
					// 得到函数的参数类型
					argClass = columnMap.get("argClass").toString();
					// 定义方法
					method = clzz.getMethod(columnMap.get("methodName").toString(), Class.forName(argClass));

					// 取出该列的cell,如果columnIndex为空的话，则看是否有设默认值
					if (!WhUtil.isEmpty(columnMap.get("columnIndex"))) {
						columnIndex = WhUtil.toInteger(columnMap.get("columnIndex"));

						// 取出行
						row = sheet.getRow(rowIndex);

						cell = row.getCell(WhUtil.toInteger(columnMap.get("columnIndex")));
						contentStr = cell.getStringCellValue();
					} else if (!WhUtil.isEmpty(columnMap.get("defaultVal"))) {
						contentStr = WhUtil.toString(columnMap.get("defaultVal"));
					}

					// 看该单元格的值是否为空，为空的话看是否要向上面的行取值
					if (WhUtil.isEmpty(contentStr) && !WhUtil.isEmpty(columnMap.get("preRow"))
							&& WhUtil.toBoolean(columnMap.get("preRow"))) {
						// 循环的往上找数据
						for (int i = rowIndex - 1; i >= 0; i--) {
							row = sheet.getRow(i);
							cell = row.getCell(WhUtil.toInteger(columnMap.get("columnIndex")));
							if (!WhUtil.isEmpty(cell.getStringCellValue())) {
								contentStr = cell.getStringCellValue();
								break;
							}
						}
					}

					// 看是否需要值的转换
					if (!WhUtil.isEmpty(columnMap.get("valMap")) && !WhUtil.isEmpty(contentStr)) {
						valMap = (Map<String, Object>) columnMap.get("valMap");
						contentStr = WhUtil.toString(valMap.get(contentStr));
					}

					if (!WhUtil.isEmpty(contentStr)) {
						if ("java.lang.Long".equals(argClass)) {
							val = WhUtil.toLong(contentStr);
						} else if ("java.lang.Integer".equals(argClass)) {
							val = WhUtil.toInteger(contentStr);
						} else if ("java.lang.Double".equals(argClass)) {
							val = WhUtil.toDouble(contentStr);
						} else if ("java.util.Date".equals(argClass)) {
							val = cell.getDateCellValue();
						} else {
							val = contentStr;
							val = trimFlag ? contentStr.trim() : contentStr;
						}

						// 把值设进对象里去
						method.invoke(objectMap.get(objectName), val);
					}
				}

				// 把经过处理后的对象放进结果集里去
				for (String name : objectMap.keySet()) {
					dataList = (List<Object>) resultMap.get(name);
					dataList.add(objectMap.get(name));
					resultMap.put(name, dataList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WhException(
					"Excel数据第 " + (rowIndex + 1) + " 行，第 " + (columnIndex + 1) + " 列有误\n" + e.getMessage());
		}

		return resultMap;
	}

	public static void createExcel(String templatePath, String excelPath, int startRow, int writeSize,
			List<List<Map<String, Object>>> dataList) throws Exception {
		// 加载模板
		Workbook template = Workbook.getWorkbook(new File(templatePath));
		// 创建目标excel
		WritableWorkbook wb = Workbook.createWorkbook(new File(excelPath), template);
		// 获取sheet
		WritableSheet sheet = wb.getSheet(0);

		Workbook workbookTemp = null;

		// 循环数据
		if (!WhUtil.isEmpty(dataList) && dataList.size() > 0) {
			int rowSize = dataList.size();
			int colSize = 0;
			int rowIndex = startRow;
			int columIndex = 0;
			List<Map<String, Object>> columnList = null;
			Map<String, Object> colMap = null;
			jxl.write.Number num = null;
			Label label = null;
			CellFormat cellFormat = null;
			Object val = null;

			// 外循环，行的循环
			for (int dataIndex = 0; dataIndex < rowSize; dataIndex++) {
				columnList = dataList.get(dataIndex);
				colSize = columnList.size();

				// 内循环，列的循环
				for (int columDataIndex = 0; columDataIndex < colSize; columDataIndex++) {
					colMap = columnList.get(columDataIndex);
					val = colMap.get("value");
					columIndex = WhUtil.isEmpty(colMap.get("columnIndex")) ? columDataIndex
							: WhUtil.toInteger(colMap.get("columnIndex"));

					if (!WhUtil.isEmpty(colMap.get("cellFormat"))) {
						cellFormat = (CellFormat) colMap.get("cellFormat");
					} else {
						cellFormat = null;
					}

					if (!WhUtil.isEmpty(val)) {
						if (val instanceof Integer || val instanceof Long || val instanceof Float
								|| val instanceof Double || val instanceof BigDecimal) {
							if (WhUtil.isEmpty(cellFormat)) {
								num = new Number(columIndex, rowIndex, WhUtil.toDouble(val));
							} else {
								num = new Number(columIndex, rowIndex, WhUtil.toDouble(val), cellFormat);
							}
							sheet.addCell(num);
						} else {
							if (WhUtil.isEmpty(cellFormat)) {
								label = new Label(columIndex, rowIndex, WhUtil.toString(val));
							} else {
								label = new Label(columIndex, rowIndex, WhUtil.toString(val), cellFormat);

							}
							sheet.addCell(label);
						}
					} else {
						// 值为空的时候，也要看一下格式要不要设置
						if (!WhUtil.isEmpty(cellFormat)) {
							label = new Label(columIndex, rowIndex, "", cellFormat);
							sheet.addCell(label);
						}
					}

					// 看有没要合并单元格
					if (!WhUtil.isEmpty(colMap.get("mergeCells"))) {
						sheet.mergeCells(WhUtil.toInteger(colMap.get("mergeCol1")), rowIndex,
								WhUtil.toInteger(colMap.get("mergeCol2")), rowIndex);
					}
				}

				// 根据设定的每多少条数据就写一次，以免内存溢出
				if ((dataIndex + 1) % writeSize == 0) {
					wb.write();
					wb.close();

					if (!WhUtil.isEmpty(workbookTemp)) {
						workbookTemp.close();
					}

					workbookTemp = Workbook.getWorkbook(new File(excelPath));
					wb = Workbook.createWorkbook(new File(excelPath), workbookTemp);
					sheet = wb.getSheet(0);
				}

				rowIndex++;
			}
		}

		wb.write();
		wb.close();
		if (!WhUtil.isEmpty(workbookTemp)) {
			workbookTemp.close();
		}
		template.close();
	}
}
