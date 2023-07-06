package com.framework.support;

public interface ReportType {

	void initializeTestLog();

	void addTestLogHeading(final String p0);

	void addTestLogSubHeading(final String p0, final String p1, final String p2, final String p3);

	void addTestLogTableHeading();

	void addTestLogSection(final String p0);

	void addTestLogSubSection(final String p0);

	void updateTestLog(final String p0, final String p1, final String p2, final Status p3, final String p4);

	void addTestLogFooter(final String p0, final int p1, final int p2);

	void initializeResultSummary();

	void addResultSummaryHeading(final String p0);

	void addResultSummarySubHeading(final String p0, final String p1, final String p2, final String p3);

	void addResultSummaryTableHeading();

	void updateResultSummary(final String p0, final String p1, final String p2, final String p3, final String p4);

	void addResultSummaryFooter(String totalExecutionTime, int nTestsPassed, int nTesetFailed);

}
