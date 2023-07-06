package com.framework.support;

public class TestParameters {
	private final String currentScenario;
	private final String currentTestCase;
	private String currentTestDescription;
	private IterationOptions iterationMode;
	private int startIteration;
	private int endIteration;

	public TestParameters(final String currentSenario, final String currentTestCase) {
		this.startIteration = 1;
		this.endIteration = 1;
		this.currentScenario = currentSenario;
		this.currentTestCase = currentTestCase;
	}

	public String getCurrentTestDescription() {
		return this.currentTestDescription;
	}

	public void setCurrentTestDescription(final String currentTestDescription) {
		this.currentTestDescription = currentTestDescription;
	}

	public IterationOptions getIterationMode() {
		return this.iterationMode;
	}

	public void setIterationMode(final IterationOptions iterationMode) {
		this.iterationMode = iterationMode;
	}

	public int getStartIteration() {
		return this.startIteration;
	}

	public void setStartIteration(int startIteration) {
		if (startIteration > 0) {
			this.startIteration = startIteration;

		}
	}

	public int getEndIteration() {
		return this.endIteration;
	}

	public void setEndIteration(final int endIteration) {
		if (endIteration > 0) {
			this.endIteration = endIteration;
		}
	}

	public String getCurrentScenario() {
		return this.currentScenario;
	}

	public String getCurrentTestCase() {
		return this.currentTestCase;
	}

}
