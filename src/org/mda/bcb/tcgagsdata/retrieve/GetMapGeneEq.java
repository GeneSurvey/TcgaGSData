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
import java.util.TreeSet;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class GetMapGeneEq
{
	protected static String M_PATH = null;
	protected static int M_M450_SIZE = 0;
	protected static String [] M_M450_GENES = null;
	protected static String [] M_M450_GENE_EQ = null;
	protected static int M_M27_SIZE = 0;
	protected static String [] M_M27_GENES = null;
	protected static String [] M_M27_GENE_EQ = null;

	public GetMapGeneEq(String thePath)
	{
		TcgaGSData.printVersion();
		if (false==thePath.equals(M_PATH))
		{
			M_PATH = thePath;
			M_M450_SIZE = 0;
			M_M450_GENES = null;
			M_M450_GENE_EQ = null;
			M_M27_SIZE = 0;
			M_M27_GENES = null;
			M_M27_GENE_EQ = null;
		}
	}

	public String [] getMapping_Meth450(String theGene) throws IOException
	{
		try
		{
			return getMapping450("meth450map.tsv", "IlmnID", "UCSC_RefGene_Name", theGene);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_M450_SIZE = 0;
			M_M450_GENES = null;
			M_M450_GENE_EQ = null;
			M_M27_SIZE = 0;
			M_M27_GENES = null;
			M_M27_GENE_EQ = null;
			throw exp;
		}
	}

	public String [] getMapping_Meth27(String theGene) throws IOException
	{
		try
		{
			return getMapping27("meth27map.tsv", "probe_id", "gene_id", theGene);
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_M450_SIZE = 0;
			M_M450_GENES = null;
			M_M450_GENE_EQ = null;
			M_M27_SIZE = 0;
			M_M27_GENES = null;
			M_M27_GENE_EQ = null;
			throw exp;
		}
	}

	public String [] getMappingGeneSymbols_Meth450() throws IOException
	{
		try
		{
			if (null==M_M450_GENES)
			{
				ArrayList<ArrayList<String>> results = readFile("meth450map.tsv", "IlmnID", "UCSC_RefGene_Name");
				M_M450_SIZE = results.get(0).size();
				M_M450_GENES = results.get(1).toArray(new String[0]);
				M_M450_GENE_EQ = results.get(0).toArray(new String[0]);
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_M450_SIZE = 0;
			M_M450_GENES = null;
			M_M450_GENE_EQ = null;
			M_M27_SIZE = 0;
			M_M27_GENES = null;
			M_M27_GENE_EQ = null;
			throw exp;
		}
		return M_M450_GENES;
	}

	public String [] getMappingGeneSymbols_Meth27() throws IOException
	{
		try
		{
			if (null==M_M27_GENES)
			{
				ArrayList<ArrayList<String>> results = readFile("meth27map.tsv", "probe_id", "gene_id");
				M_M27_SIZE = results.get(0).size();
				M_M27_GENES = results.get(1).toArray(new String[0]);
				M_M27_GENE_EQ = results.get(0).toArray(new String[0]);
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_M450_SIZE = 0;
			M_M450_GENES = null;
			M_M450_GENE_EQ = null;
			M_M27_SIZE = 0;
			M_M27_GENES = null;
			M_M27_GENE_EQ = null;
			throw exp;
		}
		return M_M27_GENES;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	protected ArrayList<ArrayList<String>> readFile(String theFile, 
			String theProbeHeader, String theGeneHeader) throws IOException
	{
		ArrayList<String> probeList = new ArrayList<>();
		ArrayList<String> geneList = new ArrayList<>();
		File input = new File(M_PATH, theFile);
		try(BufferedReader br = Files.newBufferedReader(
				Paths.get(input.getAbsolutePath()),
				Charset.availableCharsets().get("ISO-8859-1")))
		{
			String line = br.readLine();
			String [] headers = line.split("\t", -1);
			int indexGene = Arrays.asList(headers).indexOf(theGeneHeader);
			int indexProbe = Arrays.asList(headers).indexOf(theProbeHeader);
			line = br.readLine();
			while(null!=line)
			{
				String [] value = line.split("\t", -1);
				String probe = value[indexProbe];
				String genes = value[indexGene];
				if ((!"".equals(probe)) && (!"".equals(genes)))
				{
					for(String myGene : genes.split(";", -1))
					{
						if (!"".equals(myGene))
						{
							probeList.add(probe);
							geneList.add(myGene);
						}
					}
				}
				line = br.readLine();
			}
		}
		ArrayList<ArrayList<String>> results = new ArrayList<>();
		results.add(probeList);
		results.add(geneList);
		return results;
	}

	protected String [] getMapping450(String theFile, 
			String theProbeHeader, String theGeneHeader, String theGene) throws IOException
	{
		long start = System.currentTimeMillis();
		if (null==M_M450_GENES)
		{
			ArrayList<ArrayList<String>> results = readFile(theFile, theProbeHeader, theGeneHeader);
			M_M450_SIZE = results.get(0).size();
			M_M450_GENES = results.get(1).toArray(new String[0]);
			M_M450_GENE_EQ = results.get(0).toArray(new String[0]);
		}
		String [] results = getResults(theGene, M_M450_SIZE, M_M450_GENES, M_M450_GENE_EQ);
		long finish = System.currentTimeMillis();
		if(null!=results)
		{
			TcgaGSData.printWithFlag("getMapping450 retrieved for " + theGene + " in " + ((finish-start)/1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("getMapping450 not found for " + theGene + " in " + ((finish-start)/1000.0) + " seconds");
		}
		return results;
	}
	
	protected String [] getMapping27(String theFile, 
			String theProbeHeader, String theGeneHeader, String theGene) throws IOException
	{
		long start = System.currentTimeMillis();
		if (null==M_M27_GENES)
		{
			ArrayList<ArrayList<String>> results = readFile(theFile, theProbeHeader, theGeneHeader);
			M_M27_SIZE = results.get(0).size();
			M_M27_GENES = results.get(1).toArray(new String[0]);
			M_M27_GENE_EQ = results.get(0).toArray(new String[0]);
		}
		String [] results = getResults(theGene, M_M27_SIZE, M_M27_GENES, M_M27_GENE_EQ);
		long finish = System.currentTimeMillis();
		if(null!=results)
		{
			TcgaGSData.printWithFlag("getMapping27 retrieved for " + theGene + " in " + ((finish-start)/1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("getMapping27 not found for " + theGene + " in " + ((finish-start)/1000.0) + " seconds");
		}
		return results;
	}
	
	////////////////////////////////////////////////////////////////////////////

	protected String [] getResults(String theGene, int theSize, String [] theGenes, String [] theGeneEq)
	{
		String [] results = null;
		if((theSize>0)&&(null!=theGene))
		{
			TreeSet<String> probes = new TreeSet<>();
			for(int x=0;x<theSize;x++)
			{
				if(theGene.equalsIgnoreCase(theGenes[x]))
				{
					probes.add(theGeneEq[x]);
				}
			}
			results = probes.toArray(new String[0]);
		}
		return results;
	}

	////////////////////////////////////////////////////////////////////////////

}
