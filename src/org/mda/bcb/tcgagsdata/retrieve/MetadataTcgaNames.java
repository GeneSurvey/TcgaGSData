/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.retrieve;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class MetadataTcgaNames
{
	static protected String M_PATH = null;
	static protected TreeMap<String, String> M_DATASETNAMES = new TreeMap<>();
	static protected TreeMap<String, String> M_DISEASENAMES = new TreeMap<>();
	static protected TreeMap<String, String> M_SAMPLETYPENAMES = new TreeMap<>();
	static public String M_UNKNOWN = "UNK";

	public MetadataTcgaNames(String thePath)
	{
		if (false==thePath.equals(M_PATH))
		{
			M_PATH = thePath;
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////

	public String getMetadataTcga_DatasetName(String theId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataTcga(theId, "dataset_names.tsv", M_DATASETNAMES);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	public String getMetadataTcga_DiseaseName(String theId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataTcga(theId, "disease_names.tsv", M_DISEASENAMES);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	public String getMetadataTcga_SampleTypeName(String theId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataTcga(theId, "sampletype_names.tsv", M_SAMPLETYPENAMES);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	////////////////////////////////////////////////////////////////////////////

	public String [] getMetadataTcga_Ids_DatasetName() throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			if (M_DATASETNAMES.isEmpty())
			{
				getMetadataTcga("", "dataset_names.tsv", M_DATASETNAMES);
			}
			return M_DATASETNAMES.keySet().toArray(new String[0]);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	public String [] getMetadataTcga_Names_DatasetName() throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			if (M_DATASETNAMES.isEmpty())
			{
				getMetadataTcga("", "dataset_names.tsv", M_DATASETNAMES);
			}
			return M_DATASETNAMES.values().toArray(new String[0]);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	public String [] getMetadataTcga_Ids_DiseaseName() throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			if (M_DISEASENAMES.isEmpty())
			{
				getMetadataTcga("", "disease_names.tsv", M_DISEASENAMES);
			}
			return M_DISEASENAMES.keySet().toArray(new String[0]);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	public String [] getMetadataTcga_Names_DiseaseName() throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			if (M_DISEASENAMES.isEmpty())
			{
				getMetadataTcga("", "disease_names.tsv", M_DISEASENAMES);
			}
			return M_DISEASENAMES.values().toArray(new String[0]);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	public String [] getMetadataTcga_Ids_SampleTypeName() throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			if (M_SAMPLETYPENAMES.isEmpty())
			{
				getMetadataTcga("", "sampletype_names.tsv", M_SAMPLETYPENAMES);
			}
			return M_SAMPLETYPENAMES.keySet().toArray(new String[0]);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	public String [] getMetadataTcga_Names_SampleTypeName() throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			if (M_SAMPLETYPENAMES.isEmpty())
			{
				getMetadataTcga("", "sampletype_names.tsv", M_SAMPLETYPENAMES);
			}
			return M_SAMPLETYPENAMES.values().toArray(new String[0]);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_DATASETNAMES = new TreeMap<>();
			M_DISEASENAMES = new TreeMap<>();
			M_SAMPLETYPENAMES = new TreeMap<>();
			throw exp;
		}
	}

	////////////////////////////////////////////////////////////////////////////

	private String getMetadataTcga(String theId, String theFile, TreeMap<String, String> theMap) throws IOException
	{
		String name = "";
		long start = System.currentTimeMillis();
		try
		{
			if (theMap.isEmpty())
			{
				TcgaGSData.printWithFlag("getMetadataTcga map is empty");
				File input = new File(M_PATH, theFile);
				if (input.exists())
				{
					TcgaGSData.printWithFlag("getMetadataTcga reading file");
					try (BufferedReader br = Files.newBufferedReader(
							Paths.get(input.getAbsolutePath()),
							Charset.availableCharsets().get("ISO-8859-1")))
					{
						String line = br.readLine();
						while (null != line)
						{
							String[] splitted = line.split("\t", -1);
							TcgaGSData.printWithFlag("getMetadataTcga adding " + splitted[0] + " and " + splitted[1]);
							theMap.put(splitted[0].toUpperCase(), splitted[1]);
							line = br.readLine();
						}
					}
				}
				else
				{
					throw new IOException("File not found " + input.getAbsolutePath());
				}
			}
			name = theMap.get(theId.toUpperCase());
		}
		catch(IOException exp)
		{
			exp.printStackTrace(System.err);
			exp.printStackTrace(System.out);
			throw exp;
		}
		catch(java.lang.NullPointerException exp)
		{
			exp.printStackTrace(System.err);
			exp.printStackTrace(System.out);
			throw exp;
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			exp.printStackTrace(System.out);
			throw exp;
		}
		long finish = System.currentTimeMillis();
		if (null!=name)
		{
			TcgaGSData.printWithFlag("getMetadataTcga theId " + theId + " -> " + name + " retrieved for " + theFile + " in " + ((finish - start) / 1000.0) + " seconds");
		}
		else
		{
			name = M_UNKNOWN;
			TcgaGSData.printWithFlag("getMetadataTcga theId " + theId + " not found for " + theFile + " (using UNK) in " + ((finish - start) / 1000.0) + " seconds");
		}
		return name;
	}
}
