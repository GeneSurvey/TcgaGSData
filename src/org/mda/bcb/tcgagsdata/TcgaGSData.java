/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata;

import edu.mda.bioinfo.ids.TcgaIdConverter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.mda.bcb.tcgagsdata.create.Compilation;
import org.mda.bcb.tcgagsdata.create.RNASeqFileCreate;
import org.mda.bcb.tcgagsdata.create.Metadata;
import org.mda.bcb.tcgagsdata.create.ProcessFile;
import org.mda.bcb.tcgagsdata.create.ReadPlatform;
import org.mda.bcb.tcgagsdata.create.miRnaCreate;
import org.mda.bcb.tcgagsdata.retrieve.GetMatrixPlatform;

/**
 *
 * @author tdcasasent
 */
public class TcgaGSData
{

	public static boolean mVerboseFlag = true;

	public static void setVerboseFlag(Boolean theVerboseFlag)
	{
		mVerboseFlag = theVerboseFlag;
	}

	public static void printWithFlag(String theString)
	{
		if (true == mVerboseFlag)
		{
			System.out.println(theString);
		}
	}

	public static String printVersion()
	{
		TcgaGSData.printWithFlag("TcgaGSData 2016-03-17-1632");
		return "TcgaGSData 2016-03-17-1632";
	}

	//TESTmain main
	public static void TESTmain(String[] args) throws IOException
	{
		System.out.println("If you see this message in production, it is an ERROR and TcgaGSData was compiled wrong");
		try
		{
			TcgaGSData.printVersion();
			String converted = "/mnt/hgfs/rsrch1_bea/DEV/GenSurveyDev/converted";
			String clinical = "/mnt/hgfs/rsrch1_bea/DEV/GenSurveyDev/clinical";
			String combined = "/mnt/hgfs/rsrch1_bea/DEV/GenSurveyDev/combined";
			String clinicalOutputFile = "/mnt/hgfs/rsrch1_bea/DEV/GenSurveyDev/combined/combined_clinical.tsv";
			String iddata = "/mnt/hgfs/rsrch1_bea/DEV/GenSurveyDev/iddata";
			String data = "/mnt/hgfs/rsrch1_bea/DEV/GenSurveyDev/data";
			String metadata = "/mnt/hgfs/rsrch1_bea/DEV/GenSurveyDev/metadata";
			boolean download = false;
			boolean others = true;
			//long mutations = combineFiles(converted, combined, "mutations", "*", "Level_2", iddata, data, 0.0);
			//long platform_mutations = platformFiles(combined, "mutations");
			//long genome_wide_snp_6_hg19nocnvWxy = combineFiles(converted, combined, "snp", "genome_wide_snp_6_hg19nocnvWxy", "Level_3", iddata, data, Double.NaN);
			//long platform_genome_wide_snp_6_hg19nocnvWxy = platformFiles(combined, "genome_wide_snp_6_hg19nocnvWxy");
			
			String zipFile = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/GeneSurvey.zip";
			CallFromR cfr = new CallFromR(zipFile);
			System.out.println("Timestamp = " + cfr.getValue_Time());
			GetMatrixPlatform gdm = cfr.getDataMatrix_RnaSeq2Platform();
			System.out.println("gene count = " + gdm.mGenes.length);
			System.out.println("sample count = " + gdm.mSamples.length);
			for(int x=0;x<6;x++)
			{
				System.out.print("\t" + gdm.mSamples[x]);
			}
			System.out.println("");
			for(int y=0;y<6;y++)
			{
				System.out.println(gdm.mGenes[y]);
				for(int x=0;x<6;x++)
				{
					System.out.print("\t" + gdm.mGenesBySamplesValues[y][x]);
				}
				System.out.println("");
			}			
		}
		catch (Exception exp)
		{
			exp.printStackTrace(System.err);
		}
	}

	//REALmain main
	public static void main(String[] args) throws IOException
	{
		try
		{
			TcgaGSData.printVersion();
			TcgaGSData.printWithFlag("args[0]=" + args[0]);
			TcgaGSData.printWithFlag("args[1]=" + args[1]);
			String converted = new File(args[0], "converted").getAbsolutePath();
			String clinical = new File(args[0], "clinical").getAbsolutePath();
			String combined = new File(args[0], "combined").getAbsolutePath();
			String clinicalOutputFile = new File(combined, "combined_clinical.tsv").getAbsolutePath();
			String iddata = new File(args[0], "iddata").getAbsolutePath();
			String data = new File(args[0], "data").getAbsolutePath();
			String metadata = new File(args[0], "metadata").getAbsolutePath();
			boolean download = args[1].equalsIgnoreCase("ALL") || args[1].equalsIgnoreCase("DOWNLOAD");
			boolean others = args[1].equalsIgnoreCase("ALL") || args[1].equalsIgnoreCase("OTHERS");
			//
			new File(combined).mkdirs();
			new File(iddata).mkdirs();

			// Setup Id Converter Data
			if (true == download)
			{
				TcgaIdConverter tic = new TcgaIdConverter(iddata);
				tic.getAndPrepFiles();
				tic.loadFiles();
			}

			if (true == others)
			{
				// clinical files
				long clinicalTime = combineClinical(clinical, "standardized_clinical.tsv", clinicalOutputFile);
				TcgaGSData.printWithFlag("clinicalTime= " + (clinicalTime / 1000.0 / 60.0 / 60.0) + " hours");
				// Setup Combined Files (fast access to pan-cancer gene data)
				long illuminahiseq_rnaseq_uncGeneRPKM = combineFiles(converted, combined, "rnaseq", "illuminahiseq_rnaseq_uncGeneRPKM", "Level_3", iddata, data, Double.NaN);
				long illuminahiseq_rnaseqv2_gene = combineFiles(converted, combined, "rnaseqv2", "illuminahiseq_rnaseqv2_gene", "Level_3", iddata, data, Double.NaN);
				long genome_wide_snp_6_hg19nocnvWxy = combineFiles(converted, combined, "snp", "genome_wide_snp_6_hg19nocnvWxy", "Level_3", iddata, data, Double.NaN);
				long humanmethylation450_level3 = combineFiles(converted, combined, "methylation", "humanmethylation450_level3", "Level_2", iddata, data, Double.NaN);
				long humanmethylation27_hg19Wxy = combineFiles(converted, combined, "methylation", "humanmethylation27_hg19Wxy", "Level_3", iddata, data, Double.NaN);
				long illuminahiseq_mirnaseq_isoform = combineFiles(converted, combined, "mirnaseq", "illuminahiseq_mirnaseq_isoform", "Level_3", iddata, data, Double.NaN);
				long mutations = combineFiles(converted, combined, "mutations", "*", "Level_2", iddata, data, 0.0);
				TcgaGSData.printWithFlag("illuminahiseq_rnaseq_uncGeneRPKM= " + (illuminahiseq_rnaseq_uncGeneRPKM / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("illuminahiseq_rnaseqv2_gene= " + (illuminahiseq_rnaseqv2_gene / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("genome_wide_snp_6_hg19nocnvWxy= " + (genome_wide_snp_6_hg19nocnvWxy / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("humanmethylation450_level3= " + (humanmethylation450_level3 / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("humanmethylation27_hg19Wxy= " + (humanmethylation27_hg19Wxy / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("illuminahiseq_mirnaseq_isoform= " + (illuminahiseq_mirnaseq_isoform / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("mutations= " + (mutations / 1000.0 / 60.0 / 60.0) + " hours");
				// build platform file
				long platform_illuminahiseq_rnaseq_uncGeneRPKM = platformFiles(combined, "illuminahiseq_rnaseq_uncGeneRPKM");
				long platform_illuminahiseq_rnaseqv2_gene = platformFiles(combined, "illuminahiseq_rnaseqv2_gene");
				long platform_genome_wide_snp_6_hg19nocnvWxy = platformFiles(combined, "genome_wide_snp_6_hg19nocnvWxy");
				long platform_humanmethylation450_level3 = platformFiles(combined, "humanmethylation450_level3");
				long platform_humanmethylation27_hg19Wxy = platformFiles(combined, "humanmethylation27_hg19Wxy");
				long platform_illuminahiseq_mirnaseq_isoform = platformFiles(combined, "illuminahiseq_mirnaseq_isoform");
				long platform_mutations = platformFiles(combined, "mutations");
				TcgaGSData.printWithFlag("platform_illuminahiseq_rnaseq_uncGeneRPKM= " + (platform_illuminahiseq_rnaseq_uncGeneRPKM / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_illuminahiseq_rnaseqv2_gene= " + (platform_illuminahiseq_rnaseqv2_gene / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_genome_wide_snp_6_hg19nocnvWxy= " + (platform_genome_wide_snp_6_hg19nocnvWxy / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_humanmethylation450_level3= " + (platform_humanmethylation450_level3 / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_humanmethylation27_hg19Wxy= " + (platform_humanmethylation27_hg19Wxy / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_illuminahiseq_mirnaseq_isoform= " + (platform_illuminahiseq_mirnaseq_isoform / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_mutations= " + (platform_mutations / 1000.0 / 60.0 / 60.0) + " hours");
				// setup barcode/patient metadata
				File [] combinedDiseaseSamplesFiles = findFiles(combined, "disease_sample.tsv");
				String metadataFile = Metadata.findMetadataFile(new File(metadata));
				Metadata md = new Metadata(metadataFile);
				md.writePatientDataFile("BARCODE", "DISEASE_STUDY", new File(data, "patient_disease.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
				md.writeBarcodeDataFile("BARCODE", "DISEASE_STUDY", new File(data, "barcode_disease.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
				md.writeBarcodeDataFile("BARCODE", "SAMPLE_CODE", new File(data, "barcode_samplecode.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
				// setup rnaseq/rnaseqv2 genomics
				String rnaseqGafFileZip = new File(data, "TCGA.Sept2010.09202010.gaf.zip").getAbsolutePath();
				String rnaseqGafFile = "TCGA.Sept2010.09202010.gaf";
				String rnaseqMap = new File(data, "rnaseqMap.tsv").getAbsolutePath();
				RNASeqFileCreate gfc = new RNASeqFileCreate();
				gfc.fileConvert(rnaseqGafFileZip, rnaseqGafFile, rnaseqMap);
				// setup miRNA genomics
				String miRNAhg19input = new File(data, "hsa_mb20_hg19.gff3").getAbsolutePath();
				String miRNAhg18input = new File(data, "hsa_mb13_hg18.gff").getAbsolutePath();
				String miRNAhg19output = new File(data, "mirHG19map.tsv").getAbsolutePath();
				String miRNAhg18output = new File(data, "mirHG18map.tsv").getAbsolutePath();
				miRnaCreate mirna = new miRnaCreate();
				mirna.fileConvert(miRNAhg19input, miRNAhg19output);
				mirna.fileConvert(miRNAhg18input, miRNAhg18output);
				//
				validate(args);
			}
			//
			TcgaGSData.printWithFlag("TcgaGSData finished");
		}
		catch (Exception exp)
		{
			exp.printStackTrace(System.err);
		}
	}

	protected static File [] findFiles(String theCombinedDir, String theDiseaseSampleFile)
	{
		// theDiseaseSampleFile - disease in first column, rest of row is SAMPLE barcode
		Collection<File> files = FileUtils.listFiles(new File(theCombinedDir), new WildcardFileFilter(theDiseaseSampleFile), TrueFileFilter.INSTANCE);
		return files.toArray(new File[0]);

	}
	
	protected static long platformFiles(String theCombinedDir, String thePlatform) throws IOException, Exception
	{
		long start = System.currentTimeMillis();
		TcgaGSData.printWithFlag("platformFiles Start");
		TcgaGSData.printWithFlag("platformFiles pre gc");
		System.gc();
		TcgaGSData.printWithFlag("platformFiles call readPlatform");
		ReadPlatform rp = new ReadPlatform(theCombinedDir,
				new File(new File(theCombinedDir, thePlatform), "platform.tsv").getAbsolutePath());
		rp.readPlatform(thePlatform);
		TcgaGSData.printWithFlag("platformFiles post gc");
		System.gc();
		TcgaGSData.printWithFlag("platformFiles Finish");
		long finish = System.currentTimeMillis();
		return (finish - start);
	}

	protected static long combineFiles(String theConvertedDir, String theCombinedDir,
			String theType, String thePlatform, String theLevel,
			String theIdDir, String theDataDir, double theDefault) throws IOException, Exception
	{
		long start = System.currentTimeMillis();
		TcgaGSData.printWithFlag("combineFiles Start");
		TcgaGSData.printWithFlag("combineFiles pre gc");
		System.gc();
		TcgaGSData.printWithFlag("combineFiles call process files");
		ProcessFile pf = new ProcessFile(theConvertedDir, theCombinedDir,
				theType, thePlatform, theLevel, theIdDir, theDataDir);
		pf.process(theDefault);
		TcgaGSData.printWithFlag("combineFiles post gc");
		System.gc();
		TcgaGSData.printWithFlag("combineFiles Finish");
		long finish = System.currentTimeMillis();
		return (finish - start);
	}

	protected static long combineClinical(String theClinicalDir, String theInputFiles, String theOutputFile) throws IOException, Exception
	{
		long start = System.currentTimeMillis();
		TcgaGSData.printWithFlag("combineClinical Start");
		TcgaGSData.printWithFlag("combineClinical pre gc");
		System.gc();
		TcgaGSData.printWithFlag("combineClinical call process files");
		Compilation pf = new Compilation(theClinicalDir, theInputFiles, theOutputFile);
		pf.process();
		TcgaGSData.printWithFlag("combineClinical post gc");
		System.gc();
		TcgaGSData.printWithFlag("combineClinical Finish");
		long finish = System.currentTimeMillis();
		return (finish - start);
	}

	public static void validate(String[] args) throws IOException
	{
		try
		{
			String converted = new File(args[0], "converted").getAbsolutePath();
			String combined = new File(args[0], "combined").getAbsolutePath();
			String iddata = new File(args[0], "iddata").getAbsolutePath();
			String data = new File(args[0], "data").getAbsolutePath();

			TcgaGSData.printWithFlag("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		}
		catch (Exception exp)
		{
			exp.printStackTrace(System.err);
		}
	}

	public static TreeMap<String, Integer> buildIndexMap(String[] theStrings)
	{
		TreeMap<String, Integer> indexMap = new TreeMap<>();
		for (int x = 0; x < theStrings.length; x++)
		{
			indexMap.put(theStrings[x], x);
		}
		return indexMap;
	}

	static public void logMemory()
	{
		/*System.gc();
		 try
		 {
		 Thread.sleep(1000);
		 }
		 catch (Exception ignore)
		 {
		 // ignore
		 }*/
		long usedMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
		long maxMB = (Runtime.getRuntime().totalMemory()) / 1024 / 1024;
		System.out.println("memory used = " + usedMB + "/" + maxMB);
		System.out.flush();
	}

}
