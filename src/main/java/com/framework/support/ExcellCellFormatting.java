package com.framework.support;

public class ExcellCellFormatting {

	private String fontName;
	private short fontSize;
	private short backColorIndex;
	private short foreColorIndex;
	public boolean bold;
	public boolean italics;
	public boolean centred;

	public ExcellCellFormatting() {
		this.bold = false;
		this.italics = false;
		this.centred = false;
	}

	public String getFontName() {
		return this.fontName;
	}

	public void setFontName(final String fontName) {
		this.fontName = fontName;
	}

	public short getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(final short fontSize) {
		this.fontSize = fontSize;
	}

	public short getBackColorIndex() {
		return this.backColorIndex;
	}

	public void setBackColorIndex(final short backColorIndex) {
		if (backColorIndex < 8 || backColorIndex > 64) {
			throw new FrameworkException(
					"Valid indexed for the excel custom palette are from 0X8 to 0X40 (inclusive) !!!");
		}
		this.backColorIndex = backColorIndex;
	}

	public short getForeColorIndex() {
		return this.foreColorIndex;
	}

	public void setForeColorIndex(short foreColorIndex) {
		if (foreColorIndex < 8 || foreColorIndex > 64) {
			throw new FrameworkException(
					"Valid indexed for the excel custom palette are from 0X8 to 0X40 (inclusive) !!!");
		}
		this.foreColorIndex = foreColorIndex;
	}

}
