package com.framework.support;

public class ReportThemeFactory {
	
	public ReportThemeFactory() {
	}
	
	public static ReportTheme getReportsTheme(final Theme theme){
		final ReportTheme reportTheme = new ReportTheme();
		
		switch (theme) {
		case CLASSIC:
			reportTheme.setHeadingBackColor("#495758");
			reportTheme.setHeadingForeColor("#000000");
			reportTheme.setSelectionBackColor("#8B9292");
			reportTheme.setSelectionForeColor("#000000");
			reportTheme.setContentBackColor("#282A2A");
			reportTheme.setContentForeColor("#000000");
			break;
		case MYSTIC:
			reportTheme.setHeadingBackColor("#4D7C7B");
			reportTheme.setHeadingForeColor("#FFFF95");
			reportTheme.setSelectionBackColor("#89B6B5");
			reportTheme.setSelectionForeColor("#333300");
			reportTheme.setContentBackColor("#FAFAC5");
			reportTheme.setContentForeColor("#000000");
			break;
		case AUTUMN:
			reportTheme.setHeadingBackColor("#fbc2eb");
			reportTheme.setHeadingForeColor("#000000");
			reportTheme.setSelectionBackColor("#e6e9f0");
			reportTheme.setSelectionForeColor("#000000");
			reportTheme.setContentBackColor("#fdfcfb");
			reportTheme.setContentForeColor("#000000");
			break;

		default:
			break;
		}
		return reportTheme;
	}
	public enum Theme{
		CLASSIC("CLASSIC", 0),
		MYSTIC("MYSTIC", 1),
		AUTUMN("AUTUMN", 2),
		OLIVE("OLIVE", 3),
		REBEL("REBEL", 4),
		RETRO("RETRO", 5),
		SERENE("SERENE", 6);
		
		private Theme(final String name, final int ordinal) {

		
		
		}
	}
	
}
