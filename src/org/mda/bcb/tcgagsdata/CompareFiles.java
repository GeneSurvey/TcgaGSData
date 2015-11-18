/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata;

import org.mda.bcb.tcgagsdata.create.GN_RNASeqV2;
import org.mda.bcb.tcgagsdata.create.GN_SNP6;
import org.mda.bcb.tcgagsdata.create.GN_Meth450;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.mda.bcb.tcgagsdata.create.GN_Meth27;
import org.mda.bcb.tcgagsdata.create.GN_RNASeq;
import org.mda.bcb.tcgagsdata.create.GN_miRNASeq;

/**
 *
 * @author tdcasasent
 */
public class CompareFiles
{
	public ArrayList<String> mSamples = null;
	public HashMap<String,HashMap<String,String>> mOriginal = null;
	public HashMap<String,HashMap<String,String>> mNew = null;
	//
	public String mConvertedDir = null;
	public String mCombinedDir = null;
	public String mType = null;
	public String mPlatform = null;
	public String mLevel = null;
	//
	protected String mIdPath = null;

	public CompareFiles(String theConvertedDir, String theCombinedDir,
			String theType, String thePlatform, String theLevel, String theIdPath)
	{
		TcgaGSData.printVersion();
		mSamples = new ArrayList<>();
		mOriginal = new HashMap<>();
		mNew = new HashMap<>();
		mConvertedDir = theConvertedDir;
		mCombinedDir = theCombinedDir;
		mType = theType;
		mPlatform = thePlatform;
		mLevel = theLevel;
		//
		mIdPath = theIdPath;
	}

	public void process() throws IOException, Exception
	{
		TcgaGSData.printWithFlag("CompareFiles::process - Start");
		TcgaGSData.printWithFlag("CompareFiles::process - readOriginalFiles");
		readOriginalFiles();
		TcgaGSData.printWithFlag("CompareFiles::process - readNewFiles");
		readNewFiles();
		TcgaGSData.printWithFlag("CompareFiles::process - compareFiles");
		compareFiles();
		TcgaGSData.printWithFlag("CompareFiles::process - Finish");
	}

	/////////////////////////////////////////////////////////////////

	protected void compareFiles() throws IOException
	{
		TcgaGSData.printWithFlag("CompareFiles::compareFiles - Start");
		for(String sample : mSamples)
		{
			TcgaGSData.printWithFlag("sample=" + sample);
			HashMap<String, String> oriValues = mOriginal.get(sample);
			HashMap<String, String> newValues = mNew.get(sample);
			//
			int oriSize = oriValues.size();
			int newSize = newValues.size();
			if (oriSize!=newSize)
			{
				TcgaGSData.printWithFlag("different number of genes ori=" + oriSize + " and new=" + newSize);
			}
			//
			Set<String> keyList = oriValues.keySet();
			for(String gene : keyList)
			{
				String oriV = oriValues.get(gene);
				String newV = newValues.get(gene);
				if(!(oriV.equals(newV)))
				{
					TcgaGSData.printWithFlag("Gene '" + gene + "' ori=" + oriV + " and new=" + newV);
				}
			}
		}
		TcgaGSData.printWithFlag("CompareFiles::compareFiles - finish");
	}

	/////////////////////////////////////////////////////////////////

	protected void readNewFiles() throws IOException, Exception
	{
		TcgaGSData.printWithFlag("CompareFiles::readNewFiles - Start");
		File [] fileList = new File(mCombinedDir, mPlatform).listFiles(new PatternFilenameFilter("matrix_data_.*tsv"));
		boolean indexed = false;
		int [] indexes = new int[mSamples.size()];
		for (File file : fileList)
		{
			TcgaGSData.printWithFlag("CompareFiles::readNewFiles - file=" + file.getName());
			try(BufferedReader br = Files.newBufferedReader(
					Paths.get(file.getAbsolutePath()),
					Charset.availableCharsets().get("ISO-8859-1")))
			{
				// first line samples
				String line = br.readLine();
				TcgaGSData.printWithFlag("CompareFiles::readNewFiles - indexes");
				// populate indexes with indexes of mSamples in new file
				if (indexed==false)
				{
					String [] samples = GSStringUtils.afterTab(line).split("\t", -1);
					for(int x=0;x<samples.length;x++)
					{
						int myInd = mSamples.indexOf(samples[x]);
						if (myInd>=0)
						{
							indexes[myInd] = x;
						}
					}
					indexed = true;
				}
				// do rest
				line = br.readLine();
				TcgaGSData.printWithFlag("CompareFiles::readNewFiles - before lines");
				while(null!=line)
				{
					String geneEq = GSStringUtils.beforeTab(line);
					String [] data = GSStringUtils.afterTab(line).split("\t", -1);
					for (int x=0;x<indexes.length;x++)
					{
						String curSample = mSamples.get(x);
						if (null==curSample)
						{
							throw new Exception("Sample index " + x + " is null");
						}
						HashMap<String,String> geneValue = mNew.get(curSample);
						if (null==geneValue)
						{
							geneValue = new HashMap<>();
						}
						String curData = data[indexes[x]];
						geneValue.put(geneEq, curData);
						mNew.put(curSample, geneValue);
					}
					line = br.readLine();
				}
				TcgaGSData.printWithFlag("CompareFiles::readNewFiles - after lines");
			}
		}
		TcgaGSData.printWithFlag("CompareFiles::readNewFiles - Finish");
	}

	/////////////////////////////////////////////////////////////////

	protected void readOriginalFiles() throws IOException, Exception
	{
		String skip = ".DS_Store";
		TcgaGSData.printWithFlag("CompareFiles::readOriginalFiles - Start");
		for(File diseaseDir : new File(mConvertedDir).listFiles())
		{
			String disease = diseaseDir.getName();
			if ( (diseaseDir.isDirectory()) &&
				 (!skip.equals(disease)) )
			{
				TcgaGSData.printWithFlag("CompareFiles::readOriginalFiles - disease=" + disease);
				for(File typeDir : diseaseDir.listFiles())
				{
					String type = typeDir.getName();
					if ( (typeDir.isDirectory()) &&
						 (!skip.equals(type)) &&
						 (mType.equals(type)) )
					{
						TcgaGSData.printWithFlag("CompareFiles::readOriginalFiles - type=" + type);
						for(File platformDir : typeDir.listFiles())
						{
							String platform = platformDir.getName();
							if ( (platformDir.isDirectory()) &&
								 (!skip.equals(platform)) &&
								 (mPlatform.equals(platform)) )
							{
								TcgaGSData.printWithFlag("CompareFiles::readOriginalFiles - platform=" + platform);
								for(File levelDir : platformDir.listFiles())
								{
									String level = levelDir.getName();
									if ( (levelDir.isDirectory()) &&
										 (!skip.equals(level)) &&
										 (mLevel.equals(level)) )
									{
										TcgaGSData.printWithFlag("CompareFiles::readOriginalFiles - level=" + level);
										processFile(new File(levelDir, "matrix_data.tsv"), disease);
									}
								}
							}
						}
					}
				}
			}
		}
		TcgaGSData.printWithFlag("CompareFiles::scanAndProcessDirs - Finished");
	}

	protected void processFile(File theFile, String theDisease) throws IOException, Exception
	{
		TcgaGSData.printWithFlag("CompareFiles::processFile - Start");
		try(BufferedReader br = Files.newBufferedReader(
				Paths.get(theFile.getAbsolutePath()),
				Charset.availableCharsets().get("ISO-8859-1")))
		{
			// first line samples
			String line = br.readLine();
			TcgaGSData.printWithFlag("CompareFiles::processFile - populateSampleLists");
			String [] samples = GSStringUtils.afterTab(line).split("\t", -1);
			// get 5 samples
			System.out.print(theDisease + "\t");
			for (int x=0;x<5;x++)
			{
				System.out.print(samples[x] + "\t");
				mSamples.add(samples[x]);
			}
			TcgaGSData.printWithFlag("");
			// do rest
			line = br.readLine();
			TcgaGSData.printWithFlag("CompareFiles::processFile - before lines");
			while(null!=line)
			{
				String geneEq = convertGeneEq(GSStringUtils.beforeTab(line));
				String [] data = GSStringUtils.afterTab(line).split("\t", -1);
				for (int x=0;x<5;x++)
				{
					HashMap<String,String> geneValue = mOriginal.get(samples[x]);
					if (null==geneValue)
					{
						geneValue = new HashMap<>();
					}
					geneValue.put(geneEq, data[x]);
					mOriginal.put(samples[x], geneValue);
				}
				line = br.readLine();
			}
			TcgaGSData.printWithFlag("CompareFiles::processFile - after lines");
		}
		TcgaGSData.printWithFlag("CompareFiles::processFile - Finish");
	}

	protected String convertGeneEq(String theGene) throws Exception
	{
		String result = null;
		//TcgaGSData.printWithFlag("CompareFiles::convertGeneEq - Start");
		if ("genome_wide_snp_6_hg19nocnvWxy".equalsIgnoreCase(mPlatform))
		{
			GN_SNP6 gn = new GN_SNP6(null, mIdPath);
			result = gn.convertGeneEq(theGene);
		}
		else if ("illuminahiseq_rnaseqv2_gene".equalsIgnoreCase(mPlatform))
		{
			GN_RNASeqV2 gn = new GN_RNASeqV2(null, mIdPath);
			result = gn.convertGeneEq(theGene);
		}
		else if ("illuminahiseq_rnaseq_uncGeneRPKM".equalsIgnoreCase(mPlatform))
		{
			GN_RNASeq gn = new GN_RNASeq(null, mIdPath);
			result = gn.convertGeneEq(theGene);
		}
		else if ("humanmethylation450_level3".equalsIgnoreCase(mPlatform))
		{
			GN_Meth450 gn = new GN_Meth450(null, mIdPath);
			result = gn.convertGeneEq(theGene);
		}
		else if ("humanmethylation27_hg19Wxy".equalsIgnoreCase(mPlatform))
		{
			GN_Meth27 gn = new GN_Meth27(null, mIdPath);
			result = gn.convertGeneEq(theGene);
		}
		else if ("illuminahiseq_mirnaseq_isoform".equalsIgnoreCase(mPlatform))
		{
			GN_miRNASeq gn = new GN_miRNASeq(null, mIdPath);
			result = gn.convertGeneEq(theGene);
		}
		else
		{
			TcgaGSData.printWithFlag("Unrecognized directory " + mPlatform);
		}
		//TcgaGSData.printWithFlag("CompareFiles::convertGeneEq - Finish");
		return result;
	}
	/////////////////////////////////////////////////////////////////

}
