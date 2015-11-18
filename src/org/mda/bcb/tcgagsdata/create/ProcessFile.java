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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.codec.digest.DigestUtils;
import org.mda.bcb.tcgagsdata.GSStringUtils;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class ProcessFile
{
	protected int M_BARCODES;
	protected int M_GENES;
	public double [][] mCombinedData;
	public HashMap<String, Integer> mGeneEqMap = new HashMap<>();
	public ArrayList<String> mSampleList = new ArrayList<>();
	public ArrayList< ArrayList<String> > mDiseaseToSampleLists = new ArrayList<>();
	public int mCurrentColumn = 0;
	//
	public String mConvertedDir = null;
	public String mCombinedDir = null;
	public String mDataDir = null;
	public String mType = null;
	public String mPlatform = null;
	public String mLevel = null;
	//
	//protected TcgaIdConverter mTic = null;
	protected String mIdPath = null;

	public ProcessFile(String theConvertedDir, String theCombinedDir,
			String theType, String thePlatform, String theLevel,
			String theIdPath, String theDataDir)
	{
		TcgaGSData.printVersion();
		mConvertedDir = theConvertedDir;
		mCombinedDir = theCombinedDir;
		mType = theType;
		mPlatform = thePlatform;
		mLevel = theLevel;
		//
		//mTic = new TcgaIdConverter(theIdPath);
		//mTic.loadFiles();
		mIdPath = theIdPath;
		mDataDir = theDataDir;
	}

	protected void countBarcodesAndGenes(TreeMap<File, String> theFiles) throws FileNotFoundException, IOException
	{
		M_BARCODES = 0;
		M_GENES = 0;
		TreeSet<String> genes = new TreeSet<>();
		for (File file : theFiles.keySet())
		{
			long start = System.currentTimeMillis();
			int numOfCol = 0;
			try (BufferedReader br = new BufferedReader(new FileReader(file)))
			{
				// header
				String line = br.readLine();
				numOfCol = line.split("\t", -1).length;
				while(null!=(line = br.readLine()))
				{
					
					genes.add(line.substring(0, line.indexOf("\t")));
				}
			}
			M_BARCODES = M_BARCODES + numOfCol;
			long finish = System.currentTimeMillis();
			System.out.println("file " + ((finish - start) / 1000.0) + " seconds");
		}
		M_GENES = genes.size();
	}

	protected void setupArray(TreeMap<File, String> theFiles, double theDefault) throws FileNotFoundException, IOException
	{
		countBarcodesAndGenes(theFiles);
		TcgaGSData.printWithFlag("ProcessFile::setupArray - M_BARCODES=" + M_BARCODES);
		TcgaGSData.printWithFlag("ProcessFile::setupArray - M_GENES=" + M_GENES);
		mCombinedData = new double[M_BARCODES][M_GENES];
		mCurrentColumn = 0;
		for (int x = 0; x<M_BARCODES; x++)
		{
			for (int y = 0; y<M_GENES; y++)
			{
				mCombinedData[x][y] = theDefault;
			}
		}
	}
	
	public void process(Double theDefault) throws IOException, Exception
	{
		TcgaGSData.printWithFlag("ProcessFile::process - Start");
		TcgaGSData.printWithFlag("ProcessFile::process - get GeneNames_Mixin");
		GeneNames_Mixin gnm = getGeneNamesMixin();
		TcgaGSData.printWithFlag("ProcessFile::process - thePlatform=" + mPlatform);
		TreeMap<File, String> myFiles = getListOfFiles();
		TcgaGSData.printWithFlag("ProcessFile::process - filter files");
		myFiles = gnm.filterFiles(myFiles);
		TcgaGSData.printWithFlag("ProcessFile::process - setupArray");
		setupArray(myFiles, theDefault);
		TcgaGSData.printWithFlag("ProcessFile::process - M_BARCODES=" + M_BARCODES);
		TcgaGSData.printWithFlag("ProcessFile::process - M_GENES=" + M_GENES);
		TcgaGSData.printWithFlag("ProcessFile::process - scanAndProcessDirs");
		scanAndProcessDirs(myFiles);
		TcgaGSData.printWithFlag("ProcessFile::process - processList");
		gnm.processList();
		TreeMap<String, Integer> genes = gnm.mGeneEqTreeMap;
		TcgaGSData.printWithFlag("ProcessFile::process - writeGeneListFile");
		writeGeneListFile(genes);
		TcgaGSData.printWithFlag("ProcessFile::process - writeCombinedFiles");
		writeCombinedFiles(genes);
		TcgaGSData.printWithFlag("ProcessFile::process - writeDiseaseToSampleFile");
		writeDiseaseToSampleFile();
		if (null!=gnm.mProbeToGeneSymbolMap)
		{
			TcgaGSData.printWithFlag("ProcessFile::process - writeMapFiles");
			writeMapFile(gnm);
		}
		TcgaGSData.printWithFlag("ProcessFile::process - Finish");
	}

	protected void writeMapFile(GeneNames_Mixin theGnm) throws IOException
	{
		File mapFile = new File(mDataDir, theGnm.mDataName.toLowerCase() + "map.tsv");
		try(BufferedWriter bw = Files.newBufferedWriter(
				Paths.get(mapFile.getAbsolutePath()),
				Charset.availableCharsets().get("ISO-8859-1")))
		{
			bw.write("probe_id\tchromosome\tprobe_location\tgene_id");
			bw.newLine();
			for(String probe : theGnm.mProbeToGeneSymbolMap.keySet())
			{
				bw.write(probe);
				bw.write("\t");
				bw.write(theGnm.mProbeToChromosomeMap.get(probe));
				bw.write("\t");
				bw.write(theGnm.mProbeToGenomicLocationMap.get(probe));
				bw.write("\t");
				bw.write(theGnm.mProbeToGeneSymbolMap.get(probe));
				bw.write("\t");
				bw.newLine();
			}
		}

	}
	
	protected File getOutputDir()
	{
		String myDir = mPlatform;
		if ("*".equals(myDir))
		{
			myDir = mType.toLowerCase();
		}
		File outputDir = new File(mCombinedDir, myDir);
		outputDir.mkdirs();
		return outputDir;
	}

	protected void writeGeneListFile(TreeMap<String, Integer> theGeneEqList) throws IOException
	{
		File outputDir = getOutputDir();
		File outputFile = new File(outputDir, "gene_list.tsv");
		try(BufferedWriter bw = Files.newBufferedWriter(
				Paths.get(outputFile.getAbsolutePath()),
				Charset.availableCharsets().get("ISO-8859-1")))
		{
			boolean first = true;
			for (String gene : theGeneEqList.keySet())
			{
				if (false==first)
				{
					bw.write("\t");
				}
				else
				{
					first = false;
				}
				bw.write(gene);
			}
			bw.newLine();
		}
	}

	protected void writeDiseaseToSampleFile() throws IOException
	{
		File outputDir = getOutputDir();
		File outputFile = new File(outputDir, "disease_sample.tsv");
		try(BufferedWriter bw = Files.newBufferedWriter(
				Paths.get(outputFile.getAbsolutePath()),
				Charset.availableCharsets().get("ISO-8859-1")))
		{
			for (ArrayList<String> outputList : mDiseaseToSampleLists)
			{
				boolean first = true;
				for(String data : outputList)
				{
					if (false==first)
					{
						bw.write("\t");
					}
					else
					{
						first = false;
					}
					bw.write(data);
				}
				bw.newLine();
			}
		}
	}

	protected void writeToMD5File(String theMd5prefix,
			TreeMap<String, Integer> theGeneEqList,
			ArrayList<String> theGenesublist) throws IOException
	{
		File outputDir = getOutputDir();
		File outputFile = new File(outputDir, "matrix_data_" + theMd5prefix + ".tsv");
		TcgaGSData.printWithFlag("ProcessFile::writeToMD5File - outputFile=" + outputFile.getName());
		try(BufferedWriter bw = Files.newBufferedWriter(
				Paths.get(outputFile.getAbsolutePath()),
				Charset.availableCharsets().get("ISO-8859-1")))
		{
			// header
			for(String sample : mSampleList)
			{
				bw.write("\t");
				bw.write(sample);
			}
			bw.newLine();
			boolean foundNAN = false;
			for (String geneEq : theGenesublist)
			{
				// gene eq
				//TcgaGSData.printWithFlag(y + "="+mGeneEqList.get(y));
				bw.write(geneEq);
				// data
				int y = theGeneEqList.get(geneEq).intValue();
				for(int x=0;x<mSampleList.size();x++)
				{
					bw.write("\t");
					String dVal = "NaN";
					if (Double.NaN!=mCombinedData[x][y])
					{
						dVal = Double.toString(mCombinedData[x][y]);
						if ("NaN".equalsIgnoreCase(dVal))
						{
							//TcgaGSData.printWithFlag("NaN <-- " + mCombinedData[x][y]);
							foundNAN = true;
						}
					}
					bw.write(dVal);
				}
				bw.newLine();
			}
			if (true==foundNAN)
			{
				TcgaGSData.printWithFlag("Found NaN writing " + outputFile.getAbsolutePath());
			}
		}
	}

	protected void writeCombinedFiles(TreeMap<String, Integer> theGeneEqList) throws IOException
	{
		File outputDir = getOutputDir();
		TcgaGSData.printWithFlag("mGeneEqMap.size()=" + theGeneEqList.size());
		TcgaGSData.printWithFlag("mSampleList.size()=" + mSampleList.size());
		// gene lines
		HashMap<String, ArrayList<String>> hashprefixToGeneList = new HashMap<>();
		for (String geneEq : theGeneEqList.keySet())
		{
			String md5prefix = DigestUtils.md5Hex(geneEq).substring(0, 2);
			ArrayList<String> genesublist = hashprefixToGeneList.get(md5prefix);
			if (null==genesublist)
			{
				genesublist = new ArrayList<>();
			}
			genesublist.add(geneEq);
			hashprefixToGeneList.put(md5prefix, genesublist);
		}
		for(String md5prefix : hashprefixToGeneList.keySet())
		{
			ArrayList<String> genesublist = hashprefixToGeneList.get(md5prefix);
			writeToMD5File(md5prefix, theGeneEqList, genesublist);
		}
	}
	
	protected GeneNames_Mixin getGeneNamesMixin() throws Exception
	{
		GeneNames_Mixin result = null;
		TcgaGSData.printWithFlag("ProcessFile::processGeneEq - Start");
		if ("genome_wide_snp_6_hg19nocnvWxy".equalsIgnoreCase(mPlatform))
		{
			result = new GN_SNP6(mGeneEqMap, mIdPath);
		}
		else if ("illuminahiseq_rnaseqv2_gene".equalsIgnoreCase(mPlatform))
		{
			result = new GN_RNASeqV2(mGeneEqMap, mIdPath);
		}
		else if ("illuminahiseq_rnaseq_uncGeneRPKM".equalsIgnoreCase(mPlatform))
		{
			result = new GN_RNASeq(mGeneEqMap, mIdPath);
		}
		else if ("humanmethylation450_level3".equalsIgnoreCase(mPlatform))
		{
			result = new GN_Meth450(mGeneEqMap, mIdPath);
		}
		else if ("humanmethylation27_hg19Wxy".equalsIgnoreCase(mPlatform))
		{
			result = new GN_Meth27(mGeneEqMap, mIdPath);
		}
		else if ("illuminahiseq_mirnaseq_isoform".equalsIgnoreCase(mPlatform))
		{
			result = new GN_miRNASeq(mGeneEqMap, mIdPath);
		}
		else if ("mutations".equalsIgnoreCase(mType))
		{
			result = new GN_Mutations(mGeneEqMap, mIdPath);
		}
		else
		{
			TcgaGSData.printWithFlag("Unrecognized directory " + mPlatform);
			throw new Exception("Unrecognized directory " + mPlatform);
		}
		TcgaGSData.printWithFlag("ProcessFile::processGeneEq - Finish");
		return result;
	}

	protected TreeMap<File, String> getListOfFiles()
	{
		String skip = ".DS_Store";
		TreeMap<File, String> myFiles = new TreeMap<>();
		for(File diseaseDir : new File(mConvertedDir).listFiles())
		{
			String disease = diseaseDir.getName();
			if ( (diseaseDir.isDirectory()) &&
				 (!skip.equals(disease)) )
			{
				TcgaGSData.printWithFlag("ProcessFile::scanAndProcessDirs - disease=" + disease);
				for(File typeDir : diseaseDir.listFiles())
				{
					String type = typeDir.getName();
					if ( (typeDir.isDirectory()) &&
						 (!skip.equals(type)) &&
							((mType.equals(type))||("*".equals(mType))) )
					{
						TcgaGSData.printWithFlag("ProcessFile::scanAndProcessDirs - type=" + type);
						for(File platformDir : typeDir.listFiles())
						{
							String platform = platformDir.getName();
							if ( (platformDir.isDirectory()) &&
								 (!skip.equals(platform)) &&
								 ((mPlatform.equals(platform))||("*".equals(mPlatform))) )
							{
								TcgaGSData.printWithFlag("ProcessFile::scanAndProcessDirs - platform=" + platform);
								for(File levelDir : platformDir.listFiles())
								{
									String level = levelDir.getName();
									if ( (levelDir.isDirectory()) &&
										 (!skip.equals(level)) &&
										 ((mLevel.equals(level))||("*".equals(mLevel))) )
									{
										TcgaGSData.printWithFlag("ProcessFile::scanAndProcessDirs - level=" + level);
										myFiles.put(new File(levelDir, "matrix_data.tsv"), disease);
									}
								}
							}
						}
					}
				}
			}
		}
		return myFiles;
	}
	
	protected void scanAndProcessDirs(TreeMap<File, String> theFiles) throws IOException
	{
		TcgaGSData.printWithFlag("ProcessFile::scanAndProcessDirs - Start");
		for(Entry<File, String> entry : theFiles.entrySet())
		{
			processFile(entry.getKey(), entry.getValue());
		}
		TcgaGSData.printWithFlag("ProcessFile::scanAndProcessDirs - Finished");
	}
	
	protected Double getPointOnePercentile(File theDir) throws IOException
	{
		Double pointOnePercentile = null;
		File annotFile = new File(theDir, "annotations.tsv");
		if (annotFile.exists())
		{
			List<String> lines = Files.readAllLines(Paths.get(annotFile.getAbsolutePath()));
			for(String line : lines)
			{
				if (line.startsWith("POINT_ONE_PERCENTILE"))
				{
					line = line.replace("POINT_ONE_PERCENTILE\t", "");
					pointOnePercentile = Double.valueOf(line);
				}
			}
		}
		if (null!=pointOnePercentile)
		{
			TcgaGSData.printWithFlag("ProcessFile::getPointOnePercentile - found point one percentile for " + theDir.getAbsolutePath());
			TcgaGSData.printWithFlag("ProcessFile::getPointOnePercentile - point one percentile " + pointOnePercentile);
		}
		return pointOnePercentile;
	}

	protected void processFile(File theFile, String theDisease) throws IOException
	{
		TcgaGSData.printWithFlag("ProcessFile::processFile - Start");
		TcgaGSData.printWithFlag("ProcessFile::processFile - theFile.getAbsolutePath()="+theFile.getAbsolutePath());
		Double pointOnePercentile = getPointOnePercentile(theFile.getParentFile());
		try(BufferedReader br = Files.newBufferedReader(
				Paths.get(theFile.getAbsolutePath()),
				Charset.availableCharsets().get("ISO-8859-1")))
		{
			// first line samples
			String line = br.readLine();
			TcgaGSData.printWithFlag("ProcessFile::processFile - populateSampleLists");
			populateSampleLists(theDisease, GSStringUtils.afterTab(line).split("\t", -1));
			// do rest
			int nextIndex = 0;
			line = br.readLine();
			int lineCnt = 0;
			TcgaGSData.printWithFlag("ProcessFile::processFile - before lines");
			while(null!=line)
			{
				String geneEq = GSStringUtils.beforeTab(line);
				String data = GSStringUtils.afterTab(line);
				nextIndex = populateGeneAndData(geneEq, data.split("\t", -1), mCurrentColumn, pointOnePercentile);
				line = br.readLine();
				lineCnt = lineCnt+1;
				if (0== (lineCnt % 1000))
				{
					System.out.print(" " + lineCnt);
				}
				if (0== (lineCnt % 10000))
				{
					TcgaGSData.printWithFlag("");
				}
			}
			TcgaGSData.printWithFlag(" -");
			TcgaGSData.printWithFlag("ProcessFile::processFile - after lines");
			mCurrentColumn = nextIndex;
		}
		TcgaGSData.printWithFlag("ProcessFile::processFile - Finish");
	}

	protected int populateGeneAndData(String theGeneEq, String [] theData, int theStart, Double thePointOnePercentile)
	{
		//TcgaGSData.printWithFlag("ProcessFile::populateGeneAndData - Start");
		Integer intGE = mGeneEqMap.get(theGeneEq);
		if (null==intGE)
		{
			int newIndex = mGeneEqMap.size();
			mGeneEqMap.put(theGeneEq, newIndex);
			intGE = mGeneEqMap.get(theGeneEq);
		}
		int indexGE = intGE.intValue();
		for(String value : theData)
		{
			double dVal = Double.NaN;
			if (!"NA".equalsIgnoreCase(value))
			{
				if (!"NaN".equalsIgnoreCase(value))
				{
					if (!"".equals(value))
					{
						dVal = Double.parseDouble(value);
						if (null!=thePointOnePercentile)
						{
							dVal = convertToPlusOne(dVal, thePointOnePercentile);
						}
					}
				}
			}
			mCombinedData[theStart][indexGE] = dVal;
			theStart = theStart + 1;
		}
		//TcgaGSData.printWithFlag("ProcessFile::populateGeneAndData - Finish");
		return theStart;
	}
	
	protected double convertToPlusOne(double theValue, double thePointOnePercentile)
	{
		// theValue = Math.log(val + thePointOnePercentile) / Math.log(2.0);
		// theValue*Math.log(2.0) = Math.log(val + thePointOnePercentile)
		// Math.exp(theValue*Math.log(2.0)) = val + thePointOnePercentile
		// Math.exp(theValue*Math.log(2.0)) - thePointOnePercentile = val
		double value = Math.exp(theValue*Math.log(2.0)) - thePointOnePercentile;
		value = Math.log(value+1)/Math.log(2.0);
		return value;
	}

	protected void populateSampleLists(String theDisease, String [] theSamples)
	{
		TcgaGSData.printWithFlag("ProcessFile::populateSampleLists - Start");
		ArrayList<String> diseaseAndSamples = new ArrayList<>();
		mSampleList.addAll(Arrays.asList(theSamples));
		diseaseAndSamples.add(theDisease);
		diseaseAndSamples.addAll(Arrays.asList(theSamples));
		mDiseaseToSampleLists.add(diseaseAndSamples);
		TcgaGSData.printWithFlag("ProcessFile::populateSampleLists - Finish");
	}
}
