package com.framework.support;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelDataAccess {

	private final String filePath;
	private final String fileName;
	private String dataSheetName;

	public String getDataSheetName() {
		return this.dataSheetName;
	}

	public void setDataSheetName(final String dataSheetName) {
		this.dataSheetName = dataSheetName;
	}

	public ExcelDataAccess(final String filePath, final String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}

	private void checkPreRequisities() {
		if (this.dataSheetName == null) {
			throw new FrameworkException("ExcellDataAcess.datasheetName is not set!!!");
		}
	}

	private HSSFWorkbook openFileforReading() {
		final String absoluteFilePath = String.valueOf(this.filePath) + Util.getFileSeparator() + this.fileName
				+ ".xls";

		FileInputStream fileInputStream;

		try {
			fileInputStream = new FileInputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			throw new FrameworkException("The specified file \"" + absoluteFilePath + "\" does not exist");
		}
		HSSFWorkbook workbook;

		try {
			workbook = new HSSFWorkbook((InputStream) fileInputStream);

		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error While Opening the Specified Excel workbook \"" + absoluteFilePath + "\"");
		}
		return workbook;
	}

	private void writeIntoFile(final HSSFWorkbook workbook) {
		final String absoluteFilePath = String.valueOf(this.filePath) + Util.getFileSeparator() + this.fileName
				+ ".xls";
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified file \"" + absoluteFilePath + "\" does not exist");
		}
		try {
			workbook.write((OutputStream) fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e2) {
			e2.printStackTrace();
			throw new FrameworkException(
					"Error While Opening the Specified Excel workbook \"" + absoluteFilePath + "\"");
		}
	}

	private HSSFSheet getWorksheet(final HSSFWorkbook workbook) {

		final HSSFSheet worksheet = workbook.getSheet(this.dataSheetName);
		if (worksheet == null) {
			throw new FrameworkException(
					"The Specified sheet \"" + this.dataSheetName + "\"" + "does not Exist within the workbook");
		}
		return worksheet;
	}

	public int getRowNum(final String key, final int columnNum, final int startRowNum) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final FormulaEvaluator formulaEvaluator = (FormulaEvaluator) workbook.getCreationHelper()
				.createFormulaEvaluator();
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet.getLastRowNum(); ++currentRowNum) {
			final HSSFRow row = worksheet.getRow(currentRowNum);
			final HSSFCell cell = row.getCell(columnNum);
			final String currentValue = this.getCellValueasString(cell, formulaEvaluator);
			if (currentValue.equals(key)) {
				return currentRowNum;
			}
		}
		return -1;
	}

	private String getCellValueasString(final HSSFCell cell, final FormulaEvaluator formulaEvaluator) {
		if (cell == null || cell.getCellType() == 3) {
			return "";
		}
		if (formulaEvaluator.evaluate((Cell) cell).getCellType() == 5) {
			throw new FrameworkException("Error in formula within this cell! Error code: " + cell.getErrorCellValue());
		}
		final DataFormatter dataFormatter = new DataFormatter();
		return dataFormatter.formatCellValue(formulaEvaluator.evaluateInCell((Cell) cell));
	}

	public int getRowNum(final String key, final int columnNum) {
		return this.getRowNum(key, columnNum, 0);

	}

	public int getLastRowNum() {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		return worksheet.getLastRowNum();
	}

	public int getRowCount(final String key, final int columnNum, final int startRowNum) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final FormulaEvaluator formulaEvaluator = (FormulaEvaluator) workbook.getCreationHelper()
				.createFormulaEvaluator();
		int rowCount = 0;
		boolean keyFound = false;
		for (int currentRowNum = startRowNum; currentRowNum <= worksheet.getLastRowNum(); ++currentRowNum) {
			final HSSFRow row = worksheet.getRow(currentRowNum);
			final HSSFCell cell = row.getCell(columnNum);
			final String currentValue = this.getCellValueasString(cell, formulaEvaluator);
			if (currentValue.equals(key)) {
				++rowCount;
				keyFound = true;
			} else if (keyFound) {
				break;
			}

		}
		return rowCount;
	}

	public int getRowCount(final String key, final int columnNum) {
		return this.getRowCount(key, columnNum, 0);

	}

	public int getColumnNum(final String key, final int rowNum) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final FormulaEvaluator formulaEvaluator = (FormulaEvaluator) workbook.getCreationHelper()
				.createFormulaEvaluator();
		final HSSFRow row = worksheet.getRow(rowNum);

		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); ++currentColumnNum) {
			final HSSFCell cell = row.getCell(currentColumnNum);
			final String currentValue = this.getCellValueasString(cell, formulaEvaluator);
			if (currentValue.equals(key)) {
				return currentColumnNum;
			}
		}
		return -1;
	}

	public String getValue(final int rowNum, final int columnNum) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final FormulaEvaluator formulaEvaluator = (FormulaEvaluator) workbook.getCreationHelper()
				.createFormulaEvaluator();
		final HSSFRow row = worksheet.getRow(rowNum);
		final HSSFCell cell = row.getCell(columnNum);

		return this.getCellValueasString(cell, formulaEvaluator);

	}

	public String getValue(final int rowNum, final String columnHeader) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final FormulaEvaluator formulaEvaluator = (FormulaEvaluator) workbook.getCreationHelper()
				.createFormulaEvaluator();
		HSSFRow row = worksheet.getRow(0);
		int columnNum = -1;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); ++currentColumnNum) {
			final HSSFCell cell = row.getCell(currentColumnNum);
			final String currentValue = this.getCellValueasString(cell, formulaEvaluator);
			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}
		if (columnNum == -1) {
			throw new FrameworkException("The specified column header \"" + columnHeader + "\""
					+ " is not found in sheet \"" + this.dataSheetName + "\"!!!");
		}
		row = worksheet.getRow(rowNum);
		final HSSFCell cell2 = row.getCell(columnNum);
		return this.getCellValueasString(cell2, formulaEvaluator);
	}

	private HSSFCellStyle applyCellStyle(final HSSFWorkbook workbook, final ExcellCellFormatting cellFormatting) {
		final HSSFCellStyle cellStyle = workbook.createCellStyle();
		if (cellFormatting.centred) {
			cellStyle.setAlignment((short) 2);
		}

		cellStyle.setFillForegroundColor(cellFormatting.getBackColorIndex());
		cellStyle.setFillPattern((short) 1);
		final HSSFFont font = workbook.createFont();
		font.setFontName(cellFormatting.getFontName());
		font.setFontHeightInPoints(cellFormatting.getFontSize());
		if (cellFormatting.bold) {
			font.setBoldweight((short) 700);
		}
		font.setColor(cellFormatting.getForeColorIndex());
		cellStyle.setFont(font);
		return cellStyle;
	}

	public void setValue(final int rowNum, final int columnNum, final String value) {
		this.setValue(rowNum, columnNum, value, null);

	}

	public void setValue(final int rowNum, final int columnNum, final String value,
			final ExcellCellFormatting cellFormatting) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final HSSFRow row = worksheet.getRow(rowNum);
		final HSSFCell cell = row.createCell(columnNum);
		cell.setCellType(1);
		cell.setCellValue(value);
		if (cellFormatting != null) {
			final HSSFCellStyle cellStyle = this.applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellStyle);
		}
		this.writeIntoFile(workbook);

	}

	public void setValue(final int rowNum, final String columnHeader, final String value) {
		this.setValue(rowNum, columnHeader, value, null);

	}

	public void setValue(final int rowNum, final String columnHeader, final String value,
			final ExcellCellFormatting cellFormatting) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final FormulaEvaluator formulaEvaluator = (FormulaEvaluator) workbook.getCreationHelper()
				.createFormulaEvaluator();
		HSSFRow row = worksheet.getRow(0);

		int columnNum = -1;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); ++currentColumnNum) {
			final HSSFCell cell = row.getCell(currentColumnNum);
			final String currentValue = this.getCellValueasString(cell, formulaEvaluator);
			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}
		if (columnNum == -1) {
			throw new FrameworkException("The specified column header \"" + columnHeader + "\""
					+ " is not found in sheet \"" + this.dataSheetName + "\"!!!");
		}
		row = worksheet.getRow(rowNum);
		final HSSFCell cell2 = row.createCell(columnNum);
		cell2.setCellType(1);
		cell2.setCellValue(value);
		if (cellFormatting != null) {
			final HSSFCellStyle cellStyle = this.applyCellStyle(workbook, cellFormatting);
			cell2.setCellStyle(cellStyle);
		}
		this.writeIntoFile(workbook);

	}

	public void setHyperLink(final int rowNum, final int columnNum, final String linkAddress) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final HSSFRow row = worksheet.getRow(rowNum);
		final HSSFCell cell = row.getCell(columnNum);

		if (cell == null) {
			throw new FrameworkException(
					"Specified cell is empty !!! Please set a value before including a hyperlink....");
		}
		this.setCellHyperLink(workbook, cell, linkAddress);
		this.writeIntoFile(workbook);

	}

	private void setCellHyperLink(HSSFWorkbook workbook, HSSFCell cell, String linkAddress) {
		final HSSFCellStyle cellStyle = cell.getCellStyle();
		final HSSFFont font = cellStyle.getFont((Workbook) workbook);
		font.setUnderline((byte) 1);
		cellStyle.setFont(font);
		final CreationHelper creationHelper = (CreationHelper) workbook.getCreationHelper();
		final Hyperlink hyperLink = creationHelper.createHyperlink(1);
		hyperLink.setAddress(linkAddress);
		cell.setCellStyle(cellStyle);
		cell.setHyperlink(hyperLink);
	}

	public void setHyperLink(final int rowNum, final String columnHeader, final String linkAddress) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final FormulaEvaluator formulaEvaluator = (FormulaEvaluator) workbook.getCreationHelper()
				.createFormulaEvaluator();
		HSSFRow row = worksheet.getRow(0);

		int columnNum = -1;
		for (int currentColumnNum = 0; currentColumnNum < row.getLastCellNum(); ++currentColumnNum) {
			final HSSFCell cell = row.getCell(currentColumnNum);
			final String currentValue = this.getCellValueasString(cell, formulaEvaluator);
			if (currentValue.equals(columnHeader)) {
				columnNum = currentColumnNum;
				break;
			}
		}
		if (columnNum == -1) {
			throw new FrameworkException("The specified column header \"" + columnHeader + "\""
					+ " is not found in sheet \"" + this.dataSheetName + "\"!!!");
		}
		row = worksheet.getRow(rowNum);
		final HSSFCell cell2 = row.getCell(columnNum);
		if (cell2 == null) {
			throw new FrameworkException(
					"Specified cell is empty! Please set a value before including a hyperlink....");
		}
		this.setCellHyperLink(workbook, cell2, linkAddress);
		this.writeIntoFile(workbook);
	}

	public void createWorkBook() {
		final HSSFWorkbook workbook = new HSSFWorkbook();
		this.writeIntoFile(workbook);
	}

	public void addsheet(final String sheetName) {
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = workbook.createSheet(sheetName);
		worksheet.createRow(0);
		this.writeIntoFile(workbook);
		this.dataSheetName = sheetName;
	}

	public int addRow() {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final int newRowNum = worksheet.getLastRowNum() + 1;
		worksheet.createRow(newRowNum);
		this.writeIntoFile(workbook);
		return newRowNum;
	}

	public void addColumn(final String columnHeader) {
		this.addColumn(columnHeader, null);
	}

	public void addColumn(String columnHeader, ExcellCellFormatting cellFormatting) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		HSSFRow row = worksheet.getRow(0);
		int lastCellNum = row.getLastCellNum();

		if (lastCellNum == -1) {
			lastCellNum = 0;
		}
		final HSSFCell cell = row.createCell(lastCellNum);
		cell.setCellType(1);
		cell.setCellValue(columnHeader);
		if (cellFormatting != null) {
			final HSSFCellStyle cellstyle = this.applyCellStyle(workbook, cellFormatting);
			cell.setCellStyle(cellstyle);
		}
		this.writeIntoFile(workbook);

	}

	public void setCustomPaletteColor(final short index, final String hexColor) {
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFPalette palette = workbook.getCustomPalette();
		if (index < 8 || index > 64) {
			throw new FrameworkException(
					"Valid indexed for the excel custom palette are from 0X8 to 0X40 (inclusive) !!!");
		}
		final Color color = Color.decode(hexColor);
		palette.setColorAtIndex(index, (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
		this.writeIntoFile(workbook);

	}

	public void mergeCells(final int firstRow, final int lastRow, final int firstCol, final int lastCol) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		worksheet.addMergedRegion(cellRangeAddress);
		this.writeIntoFile(workbook);
	}

	public void setRowSumBelow(final boolean rowSumBelow) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		worksheet.setRowSumsBelow(rowSumBelow);
		this.writeIntoFile(workbook);
	}

	public void groupRows(final int firstRow, final int lastRow) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		worksheet.groupRow(firstRow, lastRow);
		this.writeIntoFile(workbook);
	}

	public void autoFitContents(int firstCol, final int lastCol) {
		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		if (firstCol < 0) {
			firstCol = 0;
		}
		if (firstCol > lastCol) {
			throw new FrameworkException("First Column cannot be greater than Last Column!");
		}
		for (int currentCol = firstCol; currentCol <= lastCol; ++currentCol) {
			worksheet.autoSizeColumn(currentCol);
		}
		this.writeIntoFile(workbook);
	}

	public void addOuterBorder(final int firstCol, final int lastCol) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final CellRangeAddress cellRangeAddress = new CellRangeAddress(0, worksheet.getLastRowNum(), firstCol, lastCol);
		HSSFRegionUtil.setBorderBottom(1, cellRangeAddress, worksheet, workbook);
		HSSFRegionUtil.setBorderRight(1, cellRangeAddress, worksheet, workbook);
		this.writeIntoFile(workbook);
	}

	public void addOuterBorder(final int firstRow, final int lastRow, final int firstCol, final int lastCol) {

		this.checkPreRequisities();
		final HSSFWorkbook workbook = this.openFileforReading();
		final HSSFSheet worksheet = this.getWorksheet(workbook);
		final CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		HSSFRegionUtil.setBorderBottom(1, cellRangeAddress, worksheet, workbook);
		HSSFRegionUtil.setBorderTop(1, cellRangeAddress, worksheet, workbook);
		HSSFRegionUtil.setBorderRight(1, cellRangeAddress, worksheet, workbook);
		this.writeIntoFile(workbook);
	}

}
