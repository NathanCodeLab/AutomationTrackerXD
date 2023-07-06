package com.framework.support;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;

import java.io.IOException;
import java.io.InputStream;

public class CustomXWPFDocument extends XWPFDocument {

	public CustomXWPFDocument(final InputStream in) throws IOException {
		super(in);

	}

	public void createPicture(final String blipId, final int id, int width, int height) {
		final int EMU = 9525;
		width *= 9525; 
		height *= 9525; 
		final CTInline inline = this.createParagraph().createRun().getCTR().addNewDrawing().addNewInline();
		final String picXML = "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">         <pic:nvPicPr>            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>" + "         </pic:nvPicPr>" + "         <pic:blipFill>" + "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/releationship\"/>" + "            <a:stretch>" + "               <a:fillRect/>" + "          </a:blipFill>" + "         </pic:blipFill>" + "         <pic:spPr>" + "            <a: xfrm>" + "               <a:off x=\"0\" y=\"0\"/>" + "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" + "            </a:xfrm>" + "            <a:prstGeom prst=\"rect\">" + "               <a:avLst/>" + "            </a:prstGeom>" + "         </pic;spPr>" + "      </pic;pic>" + "   </a:graphicData>" + "</a:graphic>";
		XmlToken xmlToken = null;
		try {
			xmlToken = XmlToken.Factory.parse(picXML);
		} catch (XmlException e) {
		e.printStackTrace();
		}
		inline.set((XmlObject) xmlToken);
		inline.setDistT(0L);
		inline.setDistB(0L);
		inline.setDistL(0L);
		inline.setDistR(0L);
		final CTPositiveSize2D extent = inline.addNewExtent();
		extent.setCx((long) width);
		extent.setCy((long) height);
		final CTNonVisualDrawingProps docProp = inline.addNewDocPr();
		docProp.setId((long)+ id);
		docProp.setName("Picture" + id);
		docProp.setDescr("Generated");
		
	}
}
