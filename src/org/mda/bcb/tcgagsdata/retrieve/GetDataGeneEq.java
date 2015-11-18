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
import org.apache.commons.codec.digest.DigestUtils;
import org.mda.bcb.tcgagsdata.GSStringUtils;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class GetDataGeneEq
{
	public String mPath = null;
	public int mValueSize = 0;
	public int mSampleSize = 0;
	public double [] mValues = null;
	public String [] mSamples = null;

	public GetDataGeneEq(String thePath)
	{
		TcgaGSData.printVersion();
		mPath = thePath;
	}

	public boolean getData_RnaSeq2(String theGene) throws IOException
	{
		return getData(theGene, "illuminahiseq_rnaseqv2_gene");
	}

	public boolean getData_RnaSeq(String theGene) throws IOException
	{
		return getData(theGene, "illuminahiseq_rnaseq_uncGeneRPKM");
	}

	public boolean getData_SNP6(String theGene) throws IOException
	{
		return getData(theGene, "genome_wide_snp_6_hg19nocnvWxy");
	}

	public boolean getData_Meth450(String theGene) throws IOException
	{
		return getData(theGene, "humanmethylation450_level3");
	}

	public boolean getData_Meth27(String theGene) throws IOException
	{
		return getData(theGene, "humanmethylation27_hg19Wxy");
	}

	public boolean getData_miRNASeq(String theGene) throws IOException
	{
		return getData(theGene, "illuminahiseq_mirnaseq_isoform");
	}

	public boolean getData(String theRequestedGene, String thePlatform) throws IOException
	{
		mValueSize = 0;
		mSampleSize = 0;
		mValues = null;
		mSamples = null;
		boolean found = false;
		long start = System.currentTimeMillis();
		File input = new File(new File(mPath, thePlatform), "matrix_data_" + DigestUtils.md5Hex(theRequestedGene).substring(0, 2) + ".tsv");
		if (input.exists())
		{
			//String platform = "genome_wide_snp_6_hg19nocnvWxy";
			//String platform = "humanmethylation450_level3";
			try(BufferedReader br = Files.newBufferedReader(
					Paths.get(input.getAbsolutePath()),
					Charset.availableCharsets().get("ISO-8859-1")))
			{
				// first line samples
				String line = br.readLine();
				{
					mSamples = GSStringUtils.afterTab(line).split("\t", -1);
					mSampleSize = mSamples.length;
				}
				line = br.readLine();
				while(null!=line)
				{
					String gene = GSStringUtils.beforeTab(line);
					if (theRequestedGene.equals(gene))
					{
						found = true;
						mValues = convertToDouble(GSStringUtils.afterTab(line).split("\t", -1));
						mValueSize = mValues.length;
						line = null;
					}
					else
					{
						line = br.readLine();
					}
				}
			}
		}
		long finish = System.currentTimeMillis();
		if(true==found)
		{
			TcgaGSData.printWithFlag("Gene " + theRequestedGene + " retrieved for " + thePlatform + " in " + ((finish-start)/1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("Gene " + theRequestedGene + " not found for " + thePlatform + " in " + ((finish-start)/1000.0) + " seconds");
		}
		return found;
	}

	double [] convertToDouble(String [] theStrings)
	{
		double [] doubleArray = new double[theStrings.length];
		for(int x=0;x<theStrings.length;x++)
		{
			String myStr = theStrings[x];
			if ("NA".equalsIgnoreCase(myStr))
			{
				doubleArray[x] = Double.NaN;
			}
			else if ("NaN".equalsIgnoreCase(myStr))
			{
				doubleArray[x] = Double.NaN;
			}
			else 
			{
				doubleArray[x] = Double.parseDouble(myStr);
			}
		}
		return doubleArray;
	}
}
