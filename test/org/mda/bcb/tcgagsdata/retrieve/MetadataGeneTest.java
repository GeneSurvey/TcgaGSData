/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.retrieve;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author linux
 */
public class MetadataGeneTest
{
	public String mDir = "/mnt/hgfs/code/development/TcgaGSData/";
	public MetadataGene mTest = null;
	
	public MetadataGeneTest()
	{
	}
	
	@BeforeClass
	public static void setUpClass()
	{
	}
	
	@AfterClass
	public static void tearDownClass()
	{
	}
	
	@Before
	public void setUp()
	{
		mTest = new MetadataGene(new File(mDir, "data").getAbsolutePath());
	}
	
	@After
	public void tearDown()
	{
	}

	/**
	 * Test of getMetadataList_RNASeq method, of class MetadataGene.
	 */
	@Test
	public void testGetMetadataList_RNASeq() throws Exception
	{
		System.out.println("getMetadataList_RNASeq");
		for (String theStandardizedDataId : Arrays.asList("SNORD116-19" , "CT45A4" ))
		{
			MetadataGene instance = mTest;
			MetadataGene[] result = instance.getMetadataList_RNASeq(theStandardizedDataId);
			assertNotNull(result);
			assertTrue(result.length>0);
			for(MetadataGene mdg : result)
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
		}
	}

	/**
	 * Test of getMetadataList_RNASeqV2 method, of class MetadataGene.
	 */
	@Test
	public void testGetMetadataList_RNASeqV2() throws Exception
	{
		System.out.println("getMetadataList_RNASeqV2");
		for (String theStandardizedDataId : Arrays.asList("SNORD116-19" , "CT45A4" ))
		{
			MetadataGene instance = mTest;
			MetadataGene[] result = instance.getMetadataList_RNASeqV2(theStandardizedDataId);
			assertNotNull(result);
			assertTrue(result.length>0);
			for(MetadataGene mdg : result)
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
		}
	}

	/**
	 * Test of getMetadataList_HG18 method, of class MetadataGene.
	 */
	@Test
	public void testGetMetadataList_HG18() throws Exception
	{
		System.out.println("getMetadataList_HG18");
		for (String theStandardizedDataId : Arrays.asList("5S_rRNA|17|ENSG00000222811" , "TP53" ))
		{
			MetadataGene instance = mTest;
			MetadataGene[] result = instance.getMetadataList_HG18(theStandardizedDataId);
			assertNotNull(result);
			assertTrue(result.length>0);
			for(MetadataGene mdg : result)
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
		}
	}

	/**
	 * Test of getMetadataList_HG19 method, of class MetadataGene.
	 */
	@Test
	public void testGetMetadataList_HG19() throws Exception
	{
		System.out.println("getMetadataList_HG19");
		for (String theStandardizedDataId : Arrays.asList("5S_rRNA|2" , "TP53" ))
		{
			MetadataGene instance = mTest;
			MetadataGene[] result = instance.getMetadataList_HG19(theStandardizedDataId);
			assertNotNull(result);
			assertTrue(result.length>0);
			for(MetadataGene mdg : result)
			{
				System.out.println("GeneSymbol=" + mdg.mGeneSymbol + " LocationStart=" + mdg.mLocationStart + " VersionIndex=" + mdg.mVersionIndex );
			}
		}
	}
}
