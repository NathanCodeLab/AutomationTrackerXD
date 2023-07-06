package com.framework.support;

public class AutomatorDataTable {

	private final String datatablePath;
	private final String datatableName;
	private String dataReferenceIdentifer;
	private String currentTestcase;
	private int currentIteration;
	private int currentSubIteration;

	public AutomatorDataTable(final String datatablePath, final String datatableName) {
		this.dataReferenceIdentifer = "#";
		this.currentIteration = 0;
		this.currentSubIteration = 0;
		this.datatablePath = datatablePath;
		this.datatableName = datatableName;
	}

	public void setDataReferenceIdentifier(final String dataReferenceIdentifier) {
		if (dataReferenceIdentifier.length() != 1) {
			throw new FrameworkException("The data reference identifer must be a single character!");
		}
		this.dataReferenceIdentifer = dataReferenceIdentifier;
	}

	public void setCurrentRow(final String currentTestcase, final int currentIteration, final int currentSubIteration) {
		this.currentTestcase = currentTestcase;
		this.currentIteration = currentIteration;
		this.currentSubIteration = currentSubIteration;
	}

	private void checkPreRequisities() {
		if (this.currentTestcase == null) {
			throw new FrameworkException("CraftDatatable.currentTestcase is not set!!!");
		}
		if (this.currentIteration == 0) {
			throw new FrameworkException("CraftDatatable.currentIteration is not set!!!");
		}
		if (this.currentSubIteration == 0) {
			throw new FrameworkException("CraftDatatable.currentSubIteration is not set!!!");
		}
	}

	public String getData(final String datasheetName, final String fieldName) {
		this.checkPreRequisities();
		final ExcelDataAccess testDataAccess = new ExcelDataAccess(this.datatablePath, this.datatableName);
		testDataAccess.setDataSheetName(datasheetName);
		int rowNum = testDataAccess.getRowNum(this.currentTestcase, 0, 1);
		if (rowNum == -1) {
			throw new FrameworkException(
					"The Test case \"" + this.currentTestcase + "\"" + "is not found in the test data sheet");
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(this.currentIteration), 1, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The Test case \"" + this.currentIteration + "\""
					+ " of the current test case \"" + this.currentTestcase + " is not found in the test data sheet");
		}
		rowNum = testDataAccess.getRowNum(Integer.toString(this.currentSubIteration), 2, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The Sub-Iteration number \"" + this.currentSubIteration + "\""
					+ " under iteration number \"" + this.currentIteration + "\"" + "of the test case \""
					+ " is not found in the test data sheet");
		}
		String dataValue = testDataAccess.getValue(rowNum, fieldName);
		if (dataValue.startsWith(this.dataReferenceIdentifer)) {
			dataValue = this.getCommonData(fieldName, dataValue);
		}
		return dataValue;
	}

	private String getCommonData(final String fieldName, String dataValue) {
		final ExcelDataAccess commonDataAccess = new ExcelDataAccess(this.datatablePath, "Common Testdata");
		commonDataAccess.setDataSheetName("Common_Testdata");
		final String dataReferenceId = dataValue.split(this.dataReferenceIdentifer)[1];
		int rowNum = commonDataAccess.getRowNum(dataReferenceId, 0, 1);
		if (rowNum == -1) {
			throw new FrameworkException("The common test data row identifed by \"" + dataReferenceId + "\""
					+ " is not found in the common test data sheet!");
		}
		dataValue = commonDataAccess.getValue(rowNum, fieldName);

		return dataValue;
	}

	public void puData(final String datasheetName, final String fieldName, final String dataValue) {
		this.checkPreRequisities();
		final ExcelDataAccess putDataAccess = new ExcelDataAccess(this.datatablePath, this.datatableName);
		putDataAccess.setDataSheetName(datasheetName);
		int rowNum = putDataAccess.getRowNum(this.currentTestcase, 0, 1);
		if (rowNum == -1) {
			throw new FrameworkException(
					"The Test case \"" + this.currentTestcase + "\"" + "is not found in the test data sheet");
		}
		rowNum = putDataAccess.getRowNum(Integer.toString(this.currentIteration), 1, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The Test case \"" + this.currentIteration + "\""
					+ " of the current test case \"" + this.currentTestcase + " is not found in the test data sheet");
		}
		rowNum = putDataAccess.getRowNum(Integer.toString(this.currentSubIteration), 2, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The Sub-Iteration number \"" + this.currentSubIteration + "\""
					+ " under iteration number \"" + this.currentIteration + "\"" + "of the test case \""
					+ " is not found in the test data sheet");
		}
		synchronized (AutomatorDataTable.class) {
			putDataAccess.setValue(rowNum, fieldName, dataValue);
		}

	}

	public String getExcepectedResult(final String fieldName) {
		this.checkPreRequisities();
		final ExcelDataAccess expectedResultAccess = new ExcelDataAccess(this.datatablePath, this.datatableName);
		expectedResultAccess.setDataSheetName("Parametrized_Checkpoints");
		int rowNum = expectedResultAccess.getRowNum(this.currentTestcase, 0, 1);
		if (rowNum == -1) {
			throw new FrameworkException("The Test case \"" + this.currentTestcase + "\""
					+ "is not found in the parameterized checkpoints sheet!");
		}
		rowNum = expectedResultAccess.getRowNum(Integer.toString(this.currentIteration), 1, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException(
					"The Test case \"" + this.currentIteration + "\"" + " of the current test case \""
							+ this.currentTestcase + " is not found in the parameterized checkpoints sheet!");
		}
		rowNum = expectedResultAccess.getRowNum(Integer.toString(this.currentSubIteration), 2, rowNum);
		if (rowNum == -1) {
			throw new FrameworkException("The Sub-Iteration number \"" + this.currentSubIteration + "\""
					+ " under iteration number \"" + this.currentIteration + "\"" + "of the test case \""
					+ " is not found in the parameterized checkpoints sheet!");
		}
		final String dataValue = expectedResultAccess.getValue(rowNum, fieldName);
		return dataValue;
	}

}
