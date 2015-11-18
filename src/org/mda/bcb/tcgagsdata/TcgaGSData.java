/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

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

	public static void printVersion()
	{
		TcgaGSData.printWithFlag("TcgaGSData 2015-09-30-1325");
	}

	//TESTmain main
	public static void TESTmain(String[] args) throws IOException
	{
		System.out.println("If you see this message in production, it is an ERROR and TcgaGSData was compiled wrong");
		try
		{
			String converted = "/mnt/hgfs/code/development/TCGAOpenAccessConvert/converted";
			String combined = "/mnt/hgfs/code/development/TcgaGSData/combined";
			String iddata = "/mnt/hgfs/code/development/TcgaGSData/iddata";
			String data = "/mnt/hgfs/code/development/TcgaGSData/data";
			String clinical = "/mnt/hgfs/rsrch1_bea/STD_DATA/CLINICAL_STANDARD/current";
			String clinicalOutputFile = new File(combined, "combined_clinical.tsv").getAbsolutePath();
			// Setup Combined Files (fast access to pan-cancer gene data)
			long clinicalTime = combineClinical(clinical, "standardized_clinical.tsv", clinicalOutputFile);
			TcgaGSData.printWithFlag("clinicalTime= " + (clinicalTime / 1000.0 / 60.0 / 60.0) + " hours");
			//long mutations = combineFiles(converted, combined, "mutations", "*", "Level_2", iddata, data, 0.0);
			//TcgaGSData.printWithFlag("mutations= " + (mutations / 1000.0 / 60.0 / 60.0) + " hours");
			/*
			String gsDir = "/mnt/hgfs/code/development/TcgaGSData/";
			String converted = "/mnt/hgfs/rsrch1_bea/STD_DATA/STANDARDIZED/current";
			String metadata = "/mnt/hgfs/rsrch1_bea/STD_DATA/DOWNLOADED/current/metadata";
			String combined = "/mnt/hgfs/code/development/TcgaGSData/combined";
			String iddata = "/mnt/hgfs/code/development/TcgaGSData/iddata";
			String iddataDownloads = "/mnt/hgfs/code/development/TcgaGSData/iddata/downloads";
			String data = "/mnt/hgfs/code/development/TcgaGSData/data";
			*/
			/*
			String gsDir = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/";
			//String converted = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/current";
			String metadata = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/current/metadata";
			String combined = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/combined";
			String iddata = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/iddata";
			String iddataDownloads = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/iddata/downloads";
			String data = "/mnt/hgfs/rsrch1_bea/GENE_REPORT/STAGE/data";
			*/
			//
			/*
			TcgaIdConverter tic = new TcgaIdConverter(iddata);
			tic.getAndPrepFiles();
			tic.loadFiles();
			*/
			/*
			GeneSynonyms gs = new GeneSynonyms(iddataDownloads);
			System.out.println("M-ABC2");
			for (String syn : gs.getList_GeneSymbol_Synonym("M-ABC2"))
			{
				System.out.print("\t" + syn);
			}
			System.out.println("");
			System.out.println("P53");
			for (String syn : gs.getList_GeneSymbol_Synonym("P53"))
			{
				System.out.print("\t" + syn);
			}
			System.out.println("");
			*/
			/*
			// setup barcode/patient metadata
			File [] combinedDiseaseSamplesFiles = findFiles(combined, "disease_sample.tsv");
			String metadataFile = Metadata.findMetadataFile(new File(metadata));
			Metadata md = new Metadata(metadataFile);
			md.writePatientDataFile("BARCODE", "DISEASE_STUDY", new File(data, "patient_disease.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
			md.writeBarcodeDataFile("BARCODE", "DISEASE_STUDY", new File(data, "barcode_disease.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
			md.writeBarcodeDataFile("BARCODE", "SAMPLE_CODE", new File(data, "barcode_samplecode.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
			*/
			/*
			// Setup Combined Files (fast access to pan-cancer gene data)
			long illuminahiseq_rnaseq_uncGeneRPKM = combineFiles(converted, combined, "rnaseq", "illuminahiseq_rnaseq_uncGeneRPKM", "Level_3", 10000, 21000, iddata, data);
			long illuminahiseq_rnaseqv2_gene = combineFiles(converted, combined, "rnaseqv2", "illuminahiseq_rnaseqv2_gene", "Level_3", 10000, 21000, iddata, data);
			long genome_wide_snp_6_hg19nocnvWxy = combineFiles(converted, combined, "snp", "genome_wide_snp_6_hg19nocnvWxy", "Level_3", 25000, 43000, iddata, data);
			long humanmethylation450_level3 = combineFiles(converted, combined, "methylation", "humanmethylation450_level3", "Level_2", 20000, 490000, iddata, data);
			long humanmethylation27_hg19Wxy = combineFiles(converted, combined, "methylation", "humanmethylation27_hg19Wxy", "Level_3", 20000, 30000, iddata, data);
			long illuminahiseq_mirnaseq_isoform = combineFiles(converted, combined, "mirnaseq", "illuminahiseq_mirnaseq_isoform", "Level_3", 10000, 10000, iddata, data);
			TcgaGSData.printWithFlag("illuminahiseq_rnaseq_uncGeneRPKM= " + (illuminahiseq_rnaseq_uncGeneRPKM / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("illuminahiseq_rnaseqv2_gene= " + (illuminahiseq_rnaseqv2_gene / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("genome_wide_snp_6_hg19nocnvWxy= " + (genome_wide_snp_6_hg19nocnvWxy / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("humanmethylation450_level3= " + (humanmethylation450_level3 / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("humanmethylation27_hg19Wxy= " + (humanmethylation27_hg19Wxy / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("illuminahiseq_mirnaseq_isoform= " + (illuminahiseq_mirnaseq_isoform / 1000.0 / 60.0 / 60.0) + " hours");
			// build platform file
			long platform_illuminahiseq_rnaseq_uncGeneRPKM = platformFiles(combined, "illuminahiseq_rnaseq_uncGeneRPKM");
			long platform_illuminahiseq_rnaseqv2_gene = platformFiles(combined, "illuminahiseq_rnaseqv2_gene");
			long platform_genome_wide_snp_6_hg19nocnvWxy = platformFiles(combined, "genome_wide_snp_6_hg19nocnvWxy");
			long platform_humanmethylation450_level3 = platformFiles(combined, "humanmethylation450_level3");
			long platform_humanmethylation27_hg19Wxy = platformFiles(combined, "humanmethylation27_hg19Wxy");
			long platform_illuminahiseq_mirnaseq_isoform = platformFiles(combined, "illuminahiseq_mirnaseq_isoform");
			TcgaGSData.printWithFlag("platform_illuminahiseq_rnaseq_uncGeneRPKM= " + (platform_illuminahiseq_rnaseq_uncGeneRPKM / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("platform_illuminahiseq_rnaseqv2_gene= " + (platform_illuminahiseq_rnaseqv2_gene / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("platform_genome_wide_snp_6_hg19nocnvWxy= " + (platform_genome_wide_snp_6_hg19nocnvWxy / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("platform_humanmethylation450_level3= " + (platform_humanmethylation450_level3 / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("platform_humanmethylation27_hg19Wxy= " + (platform_humanmethylation27_hg19Wxy / 1000.0 / 60.0 / 60.0) + " hours");
			TcgaGSData.printWithFlag("platform_illuminahiseq_mirnaseq_isoform= " + (platform_illuminahiseq_mirnaseq_isoform / 1000.0 / 60.0 / 60.0) + " hours");
			*/
			/*
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running GetDataClinical");
			GetDataClinical gdc = new GetDataClinical(new File(gsDir, "combined").getAbsolutePath());
			if (false==gdc.getDataClinical())
			{
				System.err.println("Clincal NOT found");
			}
			else
			{
				System.out.println("Clincal loaded");
			}
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running MetadataMir");
			MetadataMir mm = new MetadataMir(new File(gsDir, "data").getAbsolutePath());
			mm.getMirList();
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running MetadataTcgaNames");
			MetadataTcgaNames mtn = new MetadataTcgaNames(new File(gsDir, "data").getAbsolutePath());
			mtn.getMetadataTcga_DatasetName("foo");
			mtn.getMetadataTcga_DiseaseName("foo");
			mtn.getMetadataTcga_SampleTypeName("foo");
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running OneToOneUcscHgnc");
			OneToOneUcscHgnc oouh = new OneToOneUcscHgnc(new File(new File(gsDir, "iddata"), "downloads").getAbsolutePath());
			oouh.getOneToOne_GeneSymbol_List();
			oouh.getOneToOne_UCSC_List();
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running MetadataProbe");
			MetadataProbe mp = new MetadataProbe(new File(gsDir, "data").getAbsolutePath());
			mp.getMetadata_Meth27("foo");
			mp.getMetadata_Meth450("foo");
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running MetadataGene");
			MetadataGene mg = new MetadataGene(new File(gsDir, "data").getAbsolutePath());
			mg.getMetadataList_HG18("foo");
			mg.getMetadataList_HG19("foo");
			mg.getMetadataList_RNASeq("foo");
			mg.getMetadataList_RNASeqV2("foo");
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running GetMapGeneEq");
			GetMapGeneEq gmge = new GetMapGeneEq(new File(gsDir, "data").getAbsolutePath());
			gmge.getMapping_Meth27("foo");
			gmge.getMapping_Meth450("foo");			
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running GetMapGeneEq");
			FN_Meth27 fnm27 = new FN_Meth27(new File(gsDir, "data").getAbsolutePath());
			fnm27.findNeighbors(100000, 200000, "20");
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("Running GetMapGeneEq");
			FN_Meth450 fnm450 = new FN_Meth450(new File(gsDir, "data").getAbsolutePath());
			fnm450.findNeighbors(1000000, 7005000, "X");
			////////////////////////////////////////////////////////////////////
			logMemory();
			System.out.println("////////////////////////////////////////////////////////////////////");
			*/
			////////////////////////////////////////////////////////////////////
			/*
			MetadataGene mg = new MetadataGene(new File(gsDir, "data").getAbsolutePath());
			for(MetadataGene mdg : mg.getMetadataList_RNASeq("SNORD116-19"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_RNASeq("CT45A4"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_RNASeqV2("SNORD116-19"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_RNASeqV2("CT45A4"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_HG18("5S_rRNA"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_HG18("TP53"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_HG19("5S_rRNA"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_HG19("TP53"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			*/
			/*
			MetadataMir mm = new MetadataMir(data);
			String [] mirs = mm.getMirList();
			System.out.println("mirs="+mirs.length);
			String [] mimats = mm.getMimatList();
			System.out.println("mimats="+mimats.length);
			MetadataMir [] m1 = mm.getMetadata_miRNA_mir(mirs[13]);
			System.out.println("mirs[13]="+mirs[13]);
			for (MetadataMir m : m1)
			{
				System.out.println("m="+m);
			}
			MetadataMir [] m2 = mm.getMetadata_miRNA_mimat(mimats[13]);
			System.out.println("mimats[13]="+mimats[13]);
			for (MetadataMir m : m2)
			{
				System.out.println("m="+m);
			}*/
			/*
			String data = "/mnt/hgfs/code/development/TcgaGSData/data";
			String miRNAhg19input = new File(data, "hsa_mb20_hg19.gff3").getAbsolutePath();
			String miRNAhg18input = new File(data, "hsa_mb13_hg18.gff").getAbsolutePath();
			String miRNAhg19output = new File(data, "mirHG19map.tsv").getAbsolutePath();
			String miRNAhg18output = new File(data, "mirHG18map.tsv").getAbsolutePath();
			miRnaCreate mirna = new miRnaCreate();
			mirna.fileConvert(miRNAhg19input, miRNAhg19output);
			mirna.fileConvert(miRNAhg18input, miRNAhg18output);
			*/
			/*
			String iddata = "/mnt/hgfs/code/development/TcgaGSData/iddata/downloads";
			OneToOneUcscHgnc uh = new OneToOneUcscHgnc(iddata);
			System.out.println("uc001acl.1 -> " + uh.getOneToOne_GeneSymbol_UCID("uc001acl.1"));
			System.out.println("BC033949 -> " + uh.getOneToOne_UCID_GeneSymbol("BC033949"));
			System.out.println("length UCID -> " + uh.getOneToOne_UCSC_List().length);
			System.out.println("length GeneSymbol -> " + uh.getOneToOne_GeneSymbol_List().length);
			*/
			/*
			String iddata = "/mnt/hgfs/code/development/TcgaGSData/iddata";
			new File(iddata).mkdir();
			TcgaIdConverter tic = new TcgaIdConverter(iddata);
			tic.getAndPrepFiles();
			tic.loadFiles();*/
			// setup rnaseq/rnaseqv2 genomics
			/*
			String data = "/mnt/hgfs/code/development/TcgaGSData/data";
			String rnaseqGafFile = new File(data, "TCGA.Sept2010.09202010.gaf").getAbsolutePath();
			String rnaseqMap = new File(data, "rnaseqMap.tsv").getAbsolutePath();
			RNASeqFileCreate gfc = new RNASeqFileCreate();
			gfc.fileConvert(rnaseqGafFile, rnaseqMap);
			MetadataGene mg = new MetadataGene(data);
			mg.getMetadata_RNASeq("?|100130426");
			System.out.println("GeneSymbol=" + mg.mGeneSymbol + " GeneId=" + mg.mGeneId + " VersionIndex=" + mg.mVersionIndex );
			mg.getMetadata_RNASeq("?|26823");
			System.out.println("GeneSymbol=" + mg.mGeneSymbol + " GeneId=" + mg.mGeneId + " VersionIndex=" + mg.mVersionIndex );
			mg.getMetadata_RNASeq("A1BG");
			System.out.println("GeneSymbol=" + mg.mGeneSymbol + " GeneId=" + mg.mGeneId + " VersionIndex=" + mg.mVersionIndex );
			mg.getMetadata_RNASeq("tAKR");
			System.out.println("GeneSymbol=" + mg.mGeneSymbol + " GeneId=" + mg.mGeneId + " VersionIndex=" + mg.mVersionIndex );
			mg.getMetadata_RNASeq("foo");
			System.out.println("GeneSymbol=" + mg.mGeneSymbol + " GeneId=" + mg.mGeneId + " VersionIndex=" + mg.mVersionIndex );
			for(MetadataGene mdg : mg.getMetadataList_RNASeq("SNORD116-19"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			for(MetadataGene mdg : mg.getMetadataList_RNASeq("CT45A4"))
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
			FN_RNASeq rnaseq = new FN_RNASeq(data);
			for(MetadataGene mdg : rnaseq.findNeighbors(53700000, 53800000, "12", "-"))
			{
				System.out.println("Find V1 GeneSymbol=" + mdg.mGeneSymbol + " Gene Id=" + mdg.mGeneId + " VersionIndex=" + mdg.mVersionIndex );
			}
			FN_RNASeqV2 rnaseqv2 = new FN_RNASeqV2(data);
			for(MetadataGene mdg : rnaseqv2.findNeighbors(53700000, 53800000, "12", "+"))
			{
				System.out.println("Find V2 GeneSymbol=" + mdg.mGeneSymbol + " Gene Id=" + mdg.mGeneId + " VersionIndex=" + mdg.mVersionIndex );
			}*/
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
				TcgaGSData.printWithFlag("platform_illuminahiseq_rnaseq_uncGeneRPKM= " + (platform_illuminahiseq_rnaseq_uncGeneRPKM / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_illuminahiseq_rnaseqv2_gene= " + (platform_illuminahiseq_rnaseqv2_gene / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_genome_wide_snp_6_hg19nocnvWxy= " + (platform_genome_wide_snp_6_hg19nocnvWxy / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_humanmethylation450_level3= " + (platform_humanmethylation450_level3 / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_humanmethylation27_hg19Wxy= " + (platform_humanmethylation27_hg19Wxy / 1000.0 / 60.0 / 60.0) + " hours");
				TcgaGSData.printWithFlag("platform_illuminahiseq_mirnaseq_isoform= " + (platform_illuminahiseq_mirnaseq_isoform / 1000.0 / 60.0 / 60.0) + " hours");
				// setup barcode/patient metadata
				File [] combinedDiseaseSamplesFiles = findFiles(combined, "disease_sample.tsv");
				String metadataFile = Metadata.findMetadataFile(new File(metadata));
				Metadata md = new Metadata(metadataFile);
				md.writePatientDataFile("BARCODE", "DISEASE_STUDY", new File(data, "patient_disease.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
				md.writeBarcodeDataFile("BARCODE", "DISEASE_STUDY", new File(data, "barcode_disease.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
				md.writeBarcodeDataFile("BARCODE", "SAMPLE_CODE", new File(data, "barcode_samplecode.tsv").getAbsolutePath(), combinedDiseaseSamplesFiles);
				// setup rnaseq/rnaseqv2 genomics
				String rnaseqGafFile = new File(data, "TCGA.Sept2010.09202010.gaf").getAbsolutePath();
				String rnaseqMap = new File(data, "rnaseqMap.tsv").getAbsolutePath();
				RNASeqFileCreate gfc = new RNASeqFileCreate();
				gfc.fileConvert(rnaseqGafFile, rnaseqMap);
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
