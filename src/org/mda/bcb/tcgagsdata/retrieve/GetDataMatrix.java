/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.retrieve;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class GetDataMatrix
{
	public String mPath = null;
	public int mSampleSize = 0;
	public int mGenesSize = 0;
	public double [][] mGenesBySamplesValues = null;
	public String [] mSamples = null;
	public String [] mGenes = null;

	public GetDataMatrix(String thePath)
	{
		TcgaGSData.printVersion();
		mPath = thePath;
	}

	public boolean getDataMatrix_Mutations(String [] theGenes) throws IOException
	{
		return getDataMatrix(theGenes, "mutations");
	}

	public boolean getDataMatrix_RnaSeq2(String [] theGenes) throws IOException
	{
		return getDataMatrix(theGenes, "illuminahiseq_rnaseqv2_gene");
	}

	public boolean getDataMatrix_RnaSeq(String [] theGenes) throws IOException
	{
		return getDataMatrix(theGenes, "illuminahiseq_rnaseq_uncGeneRPKM");
	}

	public boolean getDataMatrix_SNP6(String [] theGenes) throws IOException
	{
		return getDataMatrix(theGenes, "genome_wide_snp_6_hg19nocnvWxy");
	}

	public boolean getDataMatrix_Meth450(String [] theGenes) throws IOException
	{
		return getDataMatrix(theGenes, "humanmethylation450_level3");
	}

	public boolean getDataMatrix_Meth27(String [] theGenes) throws IOException
	{
		return getDataMatrix(theGenes, "humanmethylation27_hg19Wxy");
	}

	public boolean getDataMatrix_miRNASeq(String [] theGenes) throws IOException
	{
		return getDataMatrix(theGenes, "illuminahiseq_mirnaseq_isoform");
	}

	public boolean getDataMatrix(String [] theGenes, String thePlatform) throws IOException
	{
		mSampleSize = 0;
		mGenesSize = 0;
		mGenesBySamplesValues = null;
		mSamples = null;
		mGenes = null;
		boolean found = false;
		long start = System.currentTimeMillis();
		// get list of genes
		{
			GetNamesGeneEq lg = new GetNamesGeneEq(mPath);
			lg.getNamesGenes(thePlatform);
			TreeSet<String> allGenes = new TreeSet<>();
			allGenes.addAll(Arrays.asList(lg.mGenes));
			TreeSet<String> ts = new TreeSet<>();
			for(String gene : theGenes)
			{
				if (allGenes.contains(gene))
				{
					TcgaGSData.printWithFlag("Gene " + gene + " found in " + thePlatform);
					ts.add(gene);
				}
				else
				{
					TcgaGSData.printWithFlag("Gene " + gene + " not found in " + thePlatform);
				}
			}
			mGenes = ts.toArray(new String[0]);
			mGenesSize = mGenes.length;
		}
		// get data
		for(int counter=0;counter<mGenes.length;counter++)
		{
			String requestedGene = mGenes[counter];
			GetDataGeneEq rg = new GetDataGeneEq(mPath);
			if (true==rg.getData(requestedGene, thePlatform))
			{
				if (null==mGenesBySamplesValues)
				{
					mSampleSize = rg.mSampleSize;
					mSamples = rg.mSamples;
					mGenesBySamplesValues = new double[mGenesSize][mSampleSize];
				}
				System.arraycopy(rg.mValues, 0, mGenesBySamplesValues[counter], 0, mSampleSize);
				found = true;
			}
		}
		long finish = System.currentTimeMillis();
		if(true==found)
		{
			TcgaGSData.printWithFlag(mGenesSize + " genes retrieved for " + thePlatform + " in " + ((finish-start)/1000.0) + " seconds");
		}
		else
		{
			TcgaGSData.printWithFlag("No genes retrieved for " + thePlatform + " in " + ((finish-start)/1000.0) + " seconds");
		}
		return found;
	}
}
