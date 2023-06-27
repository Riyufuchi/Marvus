package riyufuchi.marvus.marvusData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import riyufuchi.marvus.app.utils.MarvusConfig;
import riyufuchi.sufuLib.gui.DialogHelper;

/**
 * Created On: 30.05.2021<br>
 * Last Edit: 12.06.2023
 * <hr>
 * @author Riyufuchi
 * @version 1.5
 * @since 1.3 
 */

public class TransactionXML extends org.xml.sax.helpers.DefaultHandler
{
	private XMLOutputFactory xof;
	private XMLStreamWriter xsw;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private SAXParser parser;
	private Document document;
	private Transformer xformer;
	private String path/*, mainElement, subElement*/;
	private String name, value, note, date;
	private boolean writeValue, writeDate, writeName, writeNote;
	private LinkedList<Transaction> list;
	
	/*
	public TransactionXML(String path, String mainElement, String subElement)
	{
		this.xof = XMLOutputFactory.newInstance();
		this.xsw = null;
		this.path = path;
		this.mainElement = mainElement;
		this.subElement = subElement;
		init();
	}*/
	
	public TransactionXML(String path)
	{
		this.xof = XMLOutputFactory.newInstance();
		this.xsw = null;
		this.path = path;
		//this.mainElement = MarvusConfig.MAIN_ELEMENT;
		//this.subElement = MarvusConfig.SUB_ELEMENT;
		init();
	}
	
	// UTILS
	
	private void init()
	{
		this.writeDate = false;
		this.writeName = false;
		this.writeValue = false;
		this.writeNote = false;
	}
	
	private void format() throws IOException, ParserConfigurationException, TransformerException, SAXException
	{
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		document = builder.parse(new InputSource(new InputStreamReader(new FileInputStream(path))));
		xformer = TransformerFactory.newInstance().newTransformer();
		xformer.setOutputProperty(OutputKeys.METHOD, "xml");
		xformer.setOutputProperty(OutputKeys.INDENT, "yes");
		Source source = new DOMSource(document);
		Result result = new StreamResult(new File(path));
		xformer.transform(source, result);
	}
	
	private void closeXMLFile()
	{
		try
		{
			if (xsw != null) 
			{
				xsw.close();
			}
		} 
		catch (XMLStreamException e) 
		{
			DialogHelper.exceptionDialog(null, e);
		}
	}
	
	// PUBLIC METHODS
	
	public void exportXML(List<Transaction> data)
	{
		try
		{
			xsw = xof.createXMLStreamWriter(new FileWriter(path));
			xsw.writeStartDocument();
			xsw.writeStartElement(MarvusConfig.MAIN_ELEMENT);
			for (Transaction t  : data) 
			{
				xsw.writeStartElement(MarvusConfig.SUB_ELEMENT);
				// name
				xsw.writeStartElement(MarvusConfig.NAME);
				xsw.writeCharacters(t.getName());
				xsw.writeEndElement();
				// sum
				xsw.writeStartElement(MarvusConfig.VALUE);
				xsw.writeCharacters(t.getValue().toString());
				xsw.writeEndElement();
				// date
				xsw.writeStartElement(MarvusConfig.DATE);
				xsw.writeCharacters(t.getDate().toString());
				xsw.writeEndElement();
				// note
				xsw.writeStartElement(MarvusConfig.NOTE);
				if(t.getNote().isBlank())
					xsw.writeCharacters(" ");
				else
					xsw.writeCharacters(t.getNote());
				xsw.writeEndElement();
				// endFile
				xsw.writeEndElement();
			}
			xsw.writeEndElement();
			xsw.writeEndDocument();
			xsw.flush();
		}
		catch (NullPointerException | IOException | XMLStreamException e) 
		{
			DialogHelper.exceptionDialog(null, e);
		}
		finally
		{
			closeXMLFile();
		}
		try 
		{
			format();
		} 
		catch (IOException | ParserConfigurationException | TransformerException | SAXException e) 
		{
			DialogHelper.exceptionDialog(null, e);
		}
	}
	
	/*
	public void writeXML(int numOfItems, String[] valueNames, String[][] values)
	{
		if(valueNames.length != values.length)
		{
			DialogHelper.errorDialog(null, "ValueNames is not equal to values \n" + "ValueNames: " + valueNames.length + "\n Values: " + values.length, "Input error");
			return;
		}
		try
		{
			xsw = xof.createXMLStreamWriter(new FileWriter(path));
			xsw.writeStartDocument();
			xsw.writeStartElement(mainElement);
			for (int i = 0; i < numOfItems; i++)
			{
				xsw.writeStartElement(subElement);
				for (int l = 0; l < valueNames.length; l++)
				{
					xsw.writeStartElement(valueNames[l]);
					xsw.writeCharacters(values[i][l]);
					xsw.writeEndElement();
				}
				xsw.writeEndElement();
			}
			xsw.writeEndElement();
			xsw.writeEndDocument();
			xsw.flush();
		}
		catch (IOException | XMLStreamException e)
		{
			DialogHelper.exceptionDialog(null, e);
		} 
		finally
		{
			closeXMLFile();
		}
		try
		{
			format();
		}
		catch (IOException | ParserConfigurationException | TransformerException | SAXException e) 
		{
			DialogHelper.exceptionDialog(null, e);
		}
	}*/
	
	public void parseTransaction()
	{
		list = new LinkedList<Transaction>();
		try 
		{
			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new File(path), this);
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			DialogHelper.exceptionDialog(null, e);
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		switch (qName) 
		{
			case MarvusConfig.NAME -> writeName = true;
			case MarvusConfig.VALUE -> writeValue = true;
			case MarvusConfig.DATE -> writeDate = true;
			case MarvusConfig.NOTE -> writeNote = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
		switch (qName) 
		{
			case MarvusConfig.NAME -> writeName = false;
			case MarvusConfig.VALUE -> writeValue = false;
			case MarvusConfig.DATE -> writeDate = false;
			case MarvusConfig.NOTE -> writeNote = false;
			case MarvusConfig.SUB_ELEMENT -> list.add(new Transaction(name, value, date, note));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		String text = new String(ch, start, length);
		if (writeName)
		{
			name = text;
		}
		else if (writeValue) 
		{ 
			value = text;
		} 
		else if (writeDate) 
		{ 
			date = text;
		}
		else if (writeNote)
		{
			note = text;
		}
	}
	
	// GETTERS
	
	public LinkedList<Transaction> getList()
	{
		return list;
	}
}