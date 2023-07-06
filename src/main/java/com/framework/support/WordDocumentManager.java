package com.framework.support;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class WordDocumentManager {

	private final String filePath;
	private final String fileName;

	public WordDocumentManager(final String filePath, final String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;

	}

	public void createDocument() {
		final XWPFDocument wordDocument = new XWPFDocument();
		this.writeIntoFile(wordDocument);

	}

	private void writeIntoFile(final XWPFDocument wordDocument) {
		final String absoluteFilePath = String.valueOf(this.filePath) + Util.getFileSeparator() + this.fileName
				+ ".docx";
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(absoluteFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("The Specified file \"" + absoluteFilePath + "\"does not exist!");
		}
		try {
			wordDocument.write((OutputStream)fileOutputStream);
			fileOutputStream.close();
		} catch (IOException ew) {
			ew.printStackTrace();
			throw new FrameworkException("Error while writing in the specified document! \"" + absoluteFilePath + "\"");
		}

	}

	public void addPicture(final File pictureFile) {
		final CustomXWPFDocument document = this.openFileForReading();
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun run = paragraph.createRun();
		run.setText(pictureFile.getName());
		try {
			String id="";
		//	String d = document.addPictureData((InputStream) new FileInputStream(pictureFile), 6);
		//	final String id = document.addPictureData((InputStream) new FileInputStream(pictureFile), 6);
			BufferedImage image = ImageIO.read(pictureFile);
			document.createPicture(id, document.getNextPicNameNumber(6), image.getWidth(), image.getHeight());

		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Exception thrown while adding a picture file to a word document!");
		}
		paragraph = document.createParagraph();
		run = paragraph.createRun();
		run.addBreak(BreakType.PAGE);
		this.writeIntoFile(document);
	}

	private CustomXWPFDocument openFileForReading() {
		final String absoultePath = String.valueOf(this.filePath) + Util.getFileSeparator() + this.fileName + ".docx";
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(absoultePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		CustomXWPFDocument document;
		try {
			document = new CustomXWPFDocument(fileInputStream);
		} catch (IOException e2) {
			e2.printStackTrace();
			throw new FrameworkException("Error while opening the specified word document \"" + absoultePath + "");
		}

		return document;
	}

}
