/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.mda.bcb.tcgagsdata.GSStringUtils;
import org.mda.bcb.tcgagsdata.TcgaGSData;
import org.mda.bcb.tcgagsdata.retrieve.GetNamesGeneEq;

/**
 *
 * @author tdcasasent
 */
public class ReadPlatform
{
	public String mReadPath = null;
	public String mWriteFile = null;
	public int mNumberOfGenes = 0;
	public int mNumberOfSamples = 0;
	public double [][] mGenesBySamplesValues = null;
	public String [] mSamples = null;
	public String [] mGenes = null;

	public ReadPlatform(String theReadPath, String theWriteFile)
	{
		TcgaGSData.printVersion();
		mReadPath = theReadPath;
		mWriteFile = theWriteFile;
	}

	public void readPlatform(String thePlatform) throws IOException, Exception
	{
		TcgaGSData.printWithFlag("readPlatform for " + thePlatform + " beginning");
		TcgaGSData.printWithFlag("readPlatform can consume excessive amounts of time and memory");
		mNumberOfGenes = 0;
		TreeMap<String, Integer> geneMap = null;
		{
			GetNamesGeneEq lg = new GetNamesGeneEq(mReadPath);
			lg.getNamesGenes(thePlatform);
			mGenes = lg.mGenes;
			geneMap = TcgaGSData.buildIndexMap(lg.mGenes);
			mNumberOfGenes = lg.mSize;
		}
		mNumberOfSamples = 0;
		boolean found = false;
		long start = System.currentTimeMillis();
		TreeSet<File> dataFileList = new TreeSet<>();
		dataFileList.addAll(FileUtils.listFiles(new File(mReadPath, thePlatform), new WildcardFileFilter("matrix_data_*.tsv"), TrueFileFilter.INSTANCE));
		for (File input : dataFileList)
		{
			try(BufferedReader br = Files.newBufferedReader(
					Paths.get(input.getAbsolutePath()),
					Charset.availableCharsets().get("ISO-8859-1")))
			{
				found = true;
				TcgaGSData.printWithFlag("readPlatform file=" + input.getAbsolutePath());
				// first line samples
				String line = br.readLine();
				TreeMap<String, Integer> sampleMap = TcgaGSData.buildIndexMap(GSStringUtils.afterTab(line).split("\t", -1));
				if (0==mNumberOfSamples)
				{
					mSamples = GSStringUtils.afterTab(line).split("\t", -1);
					mNumberOfSamples = mSamples.length;
					mGenesBySamplesValues = new double[mNumberOfGenes][mNumberOfSamples];
				}
				else
				{
					if (mNumberOfSamples!=GSStringUtils.afterTab(line).split("\t", -1).length)
					{
						throw new Exception("Expected sample count of " + mNumberOfSamples + " but found different " + input.getAbsolutePath());
					}
				}
				line = br.readLine();
				while(null!=line)
				{
					String gene = GSStringUtils.beforeTab(line);
					Integer intIndex = geneMap.get(gene);
					if (null!=intIndex)
					{
						int geneIndex = intIndex;
						double [] valueList = convertToDouble(GSStringUtils.afterTab(line).split("\t", -1));
						for(int x = 0; x<valueList.length;x++)
						{
							int sampleIndex = sampleMap.get(mSamples[x]);
							mGenesBySamplesValues[geneIndex][sampleIndex] = valueList[x];
						}
					}
					else
					{
						throw new Exception("readPlatform for " + thePlatform + " found unexpected 'gene' = " + gene);
					}
					line = br.readLine();
				}
			}
		}
		long finish = System.currentTimeMillis();
		TcgaGSData.printWithFlag("readPlatform for " + thePlatform + " retrieved in " + ((finish-start)/1000.0) + " seconds");
		if (false==found)
		{
			throw new Exception("readPlatform for " + thePlatform + " not found");
		}
		else
		{
			TcgaGSData.printWithFlag("write " + thePlatform + " to " + mWriteFile);
			writeFile();
		}
	}

	protected void writeFile() throws IOException
	{
		TcgaGSData.printWithFlag("writeFile " + mWriteFile);
		try (BufferedWriter bw = Files.newBufferedWriter(
						Paths.get(mWriteFile),
						Charset.availableCharsets().get("ISO-8859-1")))
		{
			// write header of samples
			for(String sample : mSamples)
			{
				bw.write("\t" + sample);
			}
			bw.newLine();
			// write rows
			for(int index = 0;index<mNumberOfGenes;index++)
			{
				// write gene
				bw.write(mGenes[index]);
				// write data
				for(double data : mGenesBySamplesValues[index])
				{
					bw.write("\t" + data);
				}
				bw.newLine();
			}
		}
		TcgaGSData.printWithFlag("writeFile finished");
	}

	protected double [] convertToDouble(String [] theStrings)
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
