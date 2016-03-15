/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author linux
 */
public class ReadPlatform_GeneEq
{
	public String mPath = null;
	public int mSize = 0;
	public String [] mGenes = null;
	// static data for GetNamesGeneEq
	static public TreeMap<String, String> M_PATH = null;
	static public TreeMap<String, TreeSet<String>> M_GENES = null;

	public ReadPlatform_GeneEq(String thePath)
	{
		mPath = thePath;
		if (null==M_PATH)
		{
			M_PATH = new TreeMap<>();
			M_GENES = new TreeMap<>();
		}
	}

	public boolean getNames_Mutations() throws IOException
	{
		return getNamesGenes("mutations");
	}

	public boolean getNames_RnaSeq2() throws IOException
	{
		return getNamesGenes("illuminahiseq_rnaseqv2_gene");
	}

	public boolean getNames_RnaSeq() throws IOException
	{
		return getNamesGenes("illuminahiseq_rnaseq_uncGeneRPKM");
	}

	public boolean getNames_SNP6() throws IOException
	{
		return getNamesGenes("genome_wide_snp_6_hg19nocnvWxy");
	}

	public boolean getNames_Meth450() throws IOException
	{
		return getNamesGenes("humanmethylation450_level3");
	}

	public boolean getNames_Meth27() throws IOException
	{
		return getNamesGenes("humanmethylation27_hg19Wxy");
	}

	public boolean getNames_miRNASeq() throws IOException
	{
		return getNamesGenes("illuminahiseq_mirnaseq_isoform");
	}

	public boolean getNamesGenes(String thePlatform) throws IOException
	{
		String path = M_PATH.get(thePlatform);
		TreeSet<String> genes = M_GENES.get(thePlatform);
		boolean found = false;
		if ((null==path)||(false==mPath.equals(path))||(null==genes))
		{
			found = getNamesGenesFromFiles(thePlatform);
			M_PATH.put(thePlatform, mPath);
			M_GENES.put(thePlatform, new TreeSet<>(Arrays.asList(mGenes)));
		}
		else
		{
			mGenes = genes.toArray(new String[0]);
			mSize = mGenes.length;
			found = true;
		}
		return found;
	}

	protected boolean getNamesGenesFromFiles(String thePlatform) throws IOException
	{
		TcgaGSData.printVersion();
		mSize = 0;
		mGenes = null;
		boolean found = false;
		long start = System.currentTimeMillis();
		File input = new File(new File(mPath, thePlatform), "gene_list.tsv");
		if (input.exists())
		{
			try(BufferedReader br = Files.newBufferedReader(
					Paths.get(input.getAbsolutePath()),
					Charset.availableCharsets().get("ISO-8859-1")))
			{
				// first line samples
				String line = br.readLine();
				{
					mGenes = line.split("\t", -1);
					mSize = mGenes.length;
				}
				found = true;
			}
		}
		long finish = System.currentTimeMillis();
		if(true==found)
		{
			TcgaGSData.printWithFlag("Gene list (" + mGenes.length + ") retrieved for " + thePlatform + " in " + ((finish-start)/1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("Gene list not found for " + thePlatform + " in " + ((finish-start)/1000.0) + " seconds");
		}
		return found;
	}
}
