package org.finra.datagenerator.engine.scxml.tags;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.opencsv.CSVReader;

public class CsvSetAssignExtension extends AbstractSetAssignExtension implements CustomTagExtension<CsvSetAssignExtension.CsvSetAssignTag>
{
	public static class CsvSetAssignTag extends Action
	{
		// generated automatically 
		private static final long serialVersionUID = 3510773384746443908L;
		
		private String name;
		private String fileName;
		private String rowKey;

		public String getName()
		{
			return name;
		}
		
		public void setName(String name)
		{
			this.name = name;
		}
		
		public String getFileName()
		{
			return fileName;
		}
		
		public void setFileName(String fileName)
		{
			this.fileName = fileName;
		}
		
		public String getRowKey()
		{
			return rowKey;
		}

		public void setRowKey(String rowKey)
		{
			this.rowKey = rowKey;
		}
		
		@Override
		public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep, SCInstance scInstance, Log appLog, Collection derivedEvents) throws ModelException, SCXMLExpressionException
		{
			// Handled manually
		}
	}
	
	@Override
	public Class<CsvSetAssignTag> getTagActionClass()
	{
		return CsvSetAssignTag.class;
	}
	
	@Override
	public String getTagName()
	{
		return "csvset";
	}
	
	@Override
	public String getTagNameSpace()
	{
		return "org.finra.rad";
	}

	@Override
	public List<Map<String, String>> pipelinePossibleStates(CsvSetAssignTag action, List<Map<String, String>> possibleStateList)
	{
		String variable = action.getName();
		String fileName = action.getFileName();
		String rowKey = action.getRowKey();
		
		List<String> domain = null;
		try
		{
			domain = readSetValues(fileName, rowKey);
		}
		catch(Exception exception)
		{
			throw new RuntimeException("Problem reading from CSV file. File name: " + fileName + " row key: " + rowKey, exception);
		}
		if(domain == null)
			throw new RuntimeException("Couldn't find row: " + rowKey + " in file " + fileName);
		
		return performCartesianProduct(variable, possibleStateList, domain);
	}
	
	private List<String> readSetValues(String fileName, String rowKey) throws Exception
	{
		List<String[]> lines = null;
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(fileName));
			lines = csvReader.readAll();
		} finally {
			if (csvReader != null)
				csvReader.close();
		}
		for(String[] line : lines)
		{
			if(line[0].equals(rowKey))
				return Arrays.asList(line).subList(1, line.length);
		}
		return null;
	}
}
