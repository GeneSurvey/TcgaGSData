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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author linux
 */
public class MetadataGene
{
	//
	public String mGeneSymbol = null;
	public String mGeneId = null;
	public String mVersionIndex = null;
	public String mChromosome = null;
	public String mLocationStart = null;
	public String mLocationEnd = null;
	public String mStrand = null;
	//
	public int mIndexGeneSymbol = -1;
	public int mIndexGeneId = -1;
	public int mIndexVersionIndex = -1;
	public int mIndexChromosome = -1;
	public int mIndexLocationStart = -1;
	public int mIndexLocationEnd = -1;
	public int mIndexStrand = -1;
	//
	protected static String M_PATH = null;
	protected static HashMap<String, ArrayList<MetadataGene>> M_RNASEQ_ID_TO_METADATA = null;
	protected static HashMap<String, ArrayList<MetadataGene>> M_RNASEQ_SYMBOL_TO_METADATA = null;
	protected static HashMap<String, ArrayList<MetadataGene>> M_HG18_SYMBOL_TO_METADATA = null;
	protected static HashMap<String, ArrayList<MetadataGene>> M_HG19_SYMBOL_TO_METADATA = null;

	public MetadataGene(String thePath)
	{
		if (false==thePath.equals(M_PATH))
		{
			M_PATH = thePath;
			M_RNASEQ_ID_TO_METADATA = null;
			M_RNASEQ_SYMBOL_TO_METADATA = null;
			M_HG18_SYMBOL_TO_METADATA = null;
			M_HG19_SYMBOL_TO_METADATA = null;
		}
		mGeneSymbol = null;
		mGeneId = null;
		mVersionIndex = null;
		mChromosome = null;
		mLocationStart = null;
		mLocationEnd = null;
		mStrand = null;
	}

	public MetadataGene [] getMetadataList_Mutations(String theStandardizedDataId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataListHg19(theStandardizedDataId, "HG19map.tsv");
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_RNASEQ_ID_TO_METADATA = null;
			M_RNASEQ_SYMBOL_TO_METADATA = null;
			M_HG18_SYMBOL_TO_METADATA = null;
			M_HG19_SYMBOL_TO_METADATA = null;
			throw exp;
		}
	}

	public MetadataGene [] getMetadataList_RNASeq(String theStandardizedDataId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataListRnaSeq(theStandardizedDataId, "rnaseqMap.tsv");
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_RNASEQ_ID_TO_METADATA = null;
			M_RNASEQ_SYMBOL_TO_METADATA = null;
			M_HG18_SYMBOL_TO_METADATA = null;
			M_HG19_SYMBOL_TO_METADATA = null;
			throw exp;
		}
	}

	public MetadataGene [] getMetadataList_RNASeqV2(String theStandardizedDataId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataListRnaSeq(theStandardizedDataId, "rnaseqMap.tsv");
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_RNASEQ_ID_TO_METADATA = null;
			M_RNASEQ_SYMBOL_TO_METADATA = null;
			M_HG18_SYMBOL_TO_METADATA = null;
			M_HG19_SYMBOL_TO_METADATA = null;
			throw exp;
		}
	}

	public MetadataGene [] getMetadataList_HG18(String theStandardizedDataId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataListHg18(theStandardizedDataId, "HG18map.tsv");
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_RNASEQ_ID_TO_METADATA = null;
			M_RNASEQ_SYMBOL_TO_METADATA = null;
			M_HG18_SYMBOL_TO_METADATA = null;
			M_HG19_SYMBOL_TO_METADATA = null;
			throw exp;
		}
	}

	public MetadataGene [] getMetadataList_HG19(String theStandardizedDataId) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			return getMetadataListHg19(theStandardizedDataId, "HG19map.tsv");
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_RNASEQ_ID_TO_METADATA = null;
			M_RNASEQ_SYMBOL_TO_METADATA = null;
			M_HG18_SYMBOL_TO_METADATA = null;
			M_HG19_SYMBOL_TO_METADATA = null;
			throw exp;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	protected void addToLists(MetadataGene theMG, 
			HashMap<String, ArrayList<MetadataGene>> theIdToMetadata,  
			HashMap<String, ArrayList<MetadataGene>> theGeneSymbolToMetadata )
	{
		if (null!=theIdToMetadata)
		{
			ArrayList<MetadataGene> al = theIdToMetadata.get(theMG.mGeneId);
			if (null==al)
			{
				al = new ArrayList<>();
			}
			al.add(theMG);
			theIdToMetadata.put(theMG.mGeneId, al);
		}
		//
		if (null!=theGeneSymbolToMetadata)
		{
			ArrayList<MetadataGene> al = theGeneSymbolToMetadata.get(theMG.mGeneSymbol);
			if (null==al)
			{
				al = new ArrayList<>();
			}
			al.add(theMG);
			theGeneSymbolToMetadata.put(theMG.mGeneSymbol, al);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	//// RNASEQ functions
	////////////////////////////////////////////////////////////////////////////

	protected void populateHeaderLinesRnaSeq(String theLine)
	{
		// first line headers
		ArrayList<String> headers = new ArrayList<>();
		headers.addAll(Arrays.asList(theLine.split("\t", -1)));
		mIndexGeneSymbol = headers.indexOf("gene_symbol");
		mIndexGeneId = headers.indexOf("gene_id");
		mIndexVersionIndex = headers.indexOf("version_index");
		mIndexChromosome = headers.indexOf("chromosome");
		mIndexLocationStart = headers.indexOf("location_start");
		mIndexLocationEnd = headers.indexOf("location_end");
		mIndexStrand = headers.indexOf("strand");
	}

	protected void loadRNASEQdata(String theFile) throws IOException
	{
		if (null==M_RNASEQ_ID_TO_METADATA)
		{
			M_RNASEQ_ID_TO_METADATA = new HashMap<>();
			M_RNASEQ_SYMBOL_TO_METADATA = new HashMap<>();
			File input = new File(M_PATH, theFile);
			if (input.exists())
			{
				try (BufferedReader br = Files.newBufferedReader(
						Paths.get(input.getAbsolutePath()),
						Charset.availableCharsets().get("ISO-8859-1")))
				{
					String line = br.readLine();
					populateHeaderLinesRnaSeq(line);
					line = br.readLine();
					while (null != line)
					{
						String[] splitted = line.split("\t", -1);
						MetadataGene mg = makeMetadataHg(splitted);
						addToLists(mg, M_RNASEQ_ID_TO_METADATA, M_RNASEQ_SYMBOL_TO_METADATA);
						line = br.readLine();
					}
				}
			}
		}
	}

	protected MetadataGene [] getMetadataListRnaSeq(String theStandardizedDataId, String theFile) throws IOException
	{
		ArrayList<MetadataGene> list = new ArrayList<>();
		String [] splittedId = theStandardizedDataId.split("\\|", -1);
		String geneSymbol = splittedId[0];
		String geneId = (splittedId.length==1)?null:splittedId[1];
		boolean found = false;
		long start = System.currentTimeMillis();
		loadRNASEQdata(theFile);
		if (null==geneId)
		{
			list = M_RNASEQ_SYMBOL_TO_METADATA.get(geneSymbol);
			if (null!=list)
			{
				found = true;
			}
		}
		else
		{
			list = M_RNASEQ_ID_TO_METADATA.get(geneId);
			if (null!=list)
			{
				found = true;
			}
		}
		if (null==list)
		{
			 list = new ArrayList<>();
		}
		long finish = System.currentTimeMillis();
		if (true == found)
		{
			TcgaGSData.printWithFlag("List StandardizedDataId " + theStandardizedDataId + " retrieved " + list.size() + " elements for " + theFile + " in " + ((finish - start) / 1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("List StandardizedDataId " + theStandardizedDataId + " not found for " + theFile + " in " + ((finish - start) / 1000.0) + " seconds");
		}
		return list.toArray(new MetadataGene[0]);
	}

	////////////////////////////////////////////////////////////////////////////
	//// HG18 functions
	////////////////////////////////////////////////////////////////////////////

	protected MetadataGene [] getMetadataListHg18(String theGeneSymbol, String theFile) throws IOException
	{
		ArrayList<MetadataGene> list = new ArrayList<>();
		boolean found = false;
		long start = System.currentTimeMillis();
		if (null==M_HG18_SYMBOL_TO_METADATA)
		{
			M_HG18_SYMBOL_TO_METADATA = new HashMap<>();
			loadHGdata(theFile, M_HG18_SYMBOL_TO_METADATA);
		}
		list = M_HG18_SYMBOL_TO_METADATA.get(theGeneSymbol);
		if (null!=list)
		{
			found = true;
		}
		else
		{
			 list = new ArrayList<>();
		}
		long finish = System.currentTimeMillis();
		if (true == found)
		{
			TcgaGSData.printWithFlag("List getMetadataListHg18 " + theGeneSymbol + " retrieved " + list.size() + " elements for " + theFile + " in " + ((finish - start) / 1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("List getMetadataListHg18 " + theGeneSymbol + " not found for " + theFile + " in " + ((finish - start) / 1000.0) + " seconds");
		}
		return list.toArray(new MetadataGene[0]);
	}

	////////////////////////////////////////////////////////////////////////////
	//// HG19 functions
	////////////////////////////////////////////////////////////////////////////

	protected MetadataGene [] getMetadataListHg19(String theGeneSymbol, String theFile) throws IOException
	{
		ArrayList<MetadataGene> list = new ArrayList<>();
		boolean found = false;
		long start = System.currentTimeMillis();
		if (null==M_HG19_SYMBOL_TO_METADATA)
		{
			M_HG19_SYMBOL_TO_METADATA = new HashMap<>();
			loadHGdata(theFile, M_HG19_SYMBOL_TO_METADATA);
		}
		list = M_HG19_SYMBOL_TO_METADATA.get(theGeneSymbol);
		if (null!=list)
		{
			found = true;
		}
		else
		{
			 list = new ArrayList<>();
		}
		long finish = System.currentTimeMillis();
		if (true == found)
		{
			TcgaGSData.printWithFlag("List getMetadataListHg19 " + theGeneSymbol + " retrieved " + list.size() + " elements for " + theFile + " in " + ((finish - start) / 1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("List getMetadataListHg19 " + theGeneSymbol + " not found for " + theFile + " in " + ((finish - start) / 1000.0) + " seconds");
		}
		return list.toArray(new MetadataGene[0]);
	}

	////////////////////////////////////////////////////////////////////////////
	//// HG shared
	////////////////////////////////////////////////////////////////////////////
	
	protected void loadHGdata(String theFile, HashMap<String, ArrayList<MetadataGene>> theGeneSymbolToMetadata) throws IOException
	{
		File input = new File(M_PATH, theFile);
		if (input.exists())
		{
			try (BufferedReader br = Files.newBufferedReader(
					Paths.get(input.getAbsolutePath()),
					Charset.availableCharsets().get("ISO-8859-1")))
			{
				String line = br.readLine();
				populateHeaderLinesHg(line);
				line = br.readLine();
				while (null != line)
				{
					String[] splitted = line.split("\t", -1);
					MetadataGene mg = makeMetadataHg(splitted);
					addToLists(mg, null, theGeneSymbolToMetadata);
					line = br.readLine();
				}
			}
			catch(IOException exp)
			{
				exp.printStackTrace(System.out);
				throw exp;
			}
		}
	}

	protected void populateHeaderLinesHg(String theLine)
	{
		// first line headers
		ArrayList<String> headers = new ArrayList<>();
		headers.addAll(Arrays.asList(theLine.split("\t", -1)));
		mIndexGeneSymbol = headers.indexOf("gene_symbol");
		mIndexGeneId = -1;
		mIndexVersionIndex = -1;
		mIndexChromosome = headers.indexOf("chromosome");
		mIndexLocationStart = headers.indexOf("start_loc");
		mIndexLocationEnd = headers.indexOf("stop_loc");
		mIndexStrand = headers.indexOf("strand");
	}

	protected MetadataGene makeMetadataHg(String[] theSplitted)
	{
		MetadataGene mdG = new MetadataGene(M_PATH);
		mdG.mGeneSymbol = theSplitted[mIndexGeneSymbol];
		if (-1!=mIndexGeneId)
		{
			mdG.mGeneId = theSplitted[mIndexGeneId];
		}
		else
		{
			mdG.mGeneId = "none";
		}
		if (-1!=mIndexVersionIndex)
		{
			mdG.mVersionIndex = theSplitted[mIndexVersionIndex];
		}
		else
		{
			mdG.mVersionIndex = "none";
		}
		mdG.mChromosome = theSplitted[mIndexChromosome];
		mdG.mLocationStart = theSplitted[mIndexLocationStart];
		mdG.mLocationEnd = theSplitted[mIndexLocationEnd];
		mdG.mStrand = theSplitted[mIndexStrand];
		return mdG;
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

}
