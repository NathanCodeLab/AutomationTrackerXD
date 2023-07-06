package com.framework.support;

public class FrameworkParameters {

	private String relativePath;
	private String runConfiguration;
	private boolean stopExecution;
	private static final FrameworkParameters frameworkParameters;

	static {
		frameworkParameters = new FrameworkParameters();
	}

	public String getReleativePath() {
		return this.relativePath;
	}

	public void setReleativePath(final String relativePath) {
		this.relativePath = relativePath;
	}

	public String getRunConfiguration() {
		return runConfiguration;
	}

	public void setRunConfirguration(final String runConfiguration) {
		this.runConfiguration = runConfiguration;
	}

	public boolean getStopExecution() {
		return this.stopExecution;
	}

	public void setStopExecution(final boolean stopExecution) {
		this.stopExecution = stopExecution;
	}

	public static FrameworkParameters getInstance() {
		return FrameworkParameters.frameworkParameters;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
