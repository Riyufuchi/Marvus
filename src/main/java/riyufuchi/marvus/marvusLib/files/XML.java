package riyufuchi.marvus.marvusLib.files;

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
import riyufuchi.marvus.marvusData.MoneySum;
import riyufuchi.sufuLib.gui.ErrorWindow;

/**
 * Created On: 30.05.2021<br>
 * Last Edit: 17.04.2023
 * <hr>
 * @author Riyufuchi
 * @version 1.4
 * @since 1.3 
 */

public class XML extends org.xml.sax.helpers.DefaultHandler
{
	private XMLOutputFactory xof;
	private XMLStreamWriter xsw;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private SAXParser parser;
	private Document document;
	private Transformer xformer;
	private String path, mainElement, subElement, sum, date = "1.1.2018";
	private boolean writeSum = false, writeDate = false;
	private LinkedList<MoneySum> list;
	
	public XML(String path, String mainElement, String subElement)
	{
		this.xof = XMLOutputFactory.newInstance();
		this.xsw = null;
		this.path = path;
		this.mainElement = mainElement;
		this.subElement = subElement;
	}
	
	public XML(String path)
	{
		this.xof = XMLOutputFactory.newInstance();
		this.xsw = null;
		this.path = path;
	}
	
	public void exportXML(List<MoneySum> data)
	{
		try
		{
			xsw = xof.createXMLStreamWriter(new FileWriter(path));
			xsw.writeStartDocument();
			xsw.writeStartElement(mainElement);
			for (MoneySum m  : data) 
			{
				xsw.writeStartElement(subElement);
				//sum
				xsw.writeStartElement("sum");
				xsw.writeCharacters(String.valueOf(m.getMoneySum()));
				xsw.writeEndElement();
				//date
				xsw.writeStartElement("date");
				xsw.writeCharacters(m.getStringDate());
				xsw.writeEndElement();
				//endFile
				xsw.writeEndElement();
			}
			xsw.writeEndElement();
			xsw.writeEndDocument();
			xsw.flush();
		}
		catch (NullPointerException | IOException | XMLStreamException e) 
		{
			new ErrorWindow("XML error", e.getMessage());
		}
		finally
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
				new ErrorWindow("XML closing error", e.getMessage());
			}
		}
		try 
		{
			format();
		} 
		catch (IOException | ParserConfigurationException | TransformerException | SAXException e) 
		{
			new ErrorWindow("XML format error", e.getMessage());
		}
	}
	
	public void writeXML(int numOfItems, String[] valueNames, String[][] values)
	{
		if(valueNames.length == values.length)
		{
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
				new ErrorWindow("XML error", e.getMessage());
			} 
			finally 
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
					new ErrorWindow("XML closing error", e.getMessage());
				}
			}
			try
			{
				format();
			} 
			catch (IOException | ParserConfigurationException | TransformerException | SAXException e) 
			{
				new ErrorWindow("XML format error", e.getMessage());
			}
		}
		else
		{
			new ErrorWindow("Input error", "ValueNames is not equal to values \n" + "ValueNames: " + valueNames.length + "\n Values: " + values.length);
		}
	}
	
	public void parsujMoney()
	{
		list = new LinkedList<MoneySum>();
		try 
		{
			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new File(path), this);
		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			new ErrorWindow("XML read error", e.getMessage());
		}
	}
	
	public LinkedList<MoneySum> getList()
	{
		return list;
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
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{
		switch (qName) 
		{
			case MarvusConfig.SUM: writeSum = true; break;
			case MarvusConfig.DATE: writeDate = true; break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
		switch (qName) 
		{
			case MarvusConfig.SUM:
				writeSum = false;
				break;
			case MarvusConfig.DATE:
				writeDate = false;
				break;
			case MarvusConfig.SUB_ELEMENT:
				list.add(new MoneySum(sum, date));
				break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		String text = new String(ch, start, length);
		if (writeSum) 
		{ 
			sum = text;
		} 
		else if (writeDate) 
		{ 
			date = text;
		}
	}
}