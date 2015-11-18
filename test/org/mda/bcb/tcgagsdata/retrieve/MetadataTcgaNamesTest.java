/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.retrieve;

import java.io.File;
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
public class MetadataTcgaNamesTest
{
	public String mDir = "/mnt/hgfs/code/development/TcgaGSData/";
	public MetadataTcgaNames mTest = null;
	
	public MetadataTcgaNamesTest()
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
		mTest = new MetadataTcgaNames(new File(mDir, "data").getAbsolutePath());
	}
	
	@After
	public void tearDown()
	{
	}

	/**
	 * Test of getMetadataTcga_DatasetName method, of class MetadataTcgaNames.
	 */
	@Test
	public void testGetMetadataTcga_DatasetName() throws Exception
	{
		System.out.println("getMetadataTcga_DatasetName");
		{
			String theId = "h-mirna_8x15kv2_gene";
			MetadataTcgaNames instance = mTest;
			String expResult = "H-miRNA 8x15kv2 ( genes )";
			String result = instance.getMetadataTcga_DatasetName(theId);
			assertEquals(expResult, result);
		}
		{
			String theId = "genome_wide_snp_6_hg19nocnvWxy";
			MetadataTcgaNames instance = mTest;
			String expResult = "SNP 6 ( HG19 - no CNV - with sex chromosomes )";
			String result = instance.getMetadataTcga_DatasetName(theId);
			assertEquals(expResult, result);
		}
	}

	/**
	 * Test of getMetadataTcga_DiseaseName method, of class MetadataTcgaNames.
	 */
	@Test
	public void testGetMetadataTcga_DiseaseName() throws Exception
	{
		System.out.println("getMetadataTcga_DiseaseName");
		{
			String theId = "ACC";
			MetadataTcgaNames instance = mTest;
			String expResult = "Adrenocortical carcinoma";
			String result = instance.getMetadataTcga_DiseaseName(theId);
			assertEquals(expResult, result);
		}
		{
			String theId = "UVM";
			MetadataTcgaNames instance = mTest;
			String expResult = "Uveal Melanoma";
			String result = instance.getMetadataTcga_DiseaseName(theId);
			assertEquals(expResult, result);
		}
	}

	/**
	 * Test of getMetadataTcga_SampleTypeName method, of class MetadataTcgaNames.
	 */
	@Test
	public void testGetMetadataTcga_SampleTypeName() throws Exception
	{
		System.out.println("getMetadataTcga_SampleTypeName");
		{
			String theId = "01";
			MetadataTcgaNames instance = mTest;
			String expResult = "Primary solid Tumor";
			String result = instance.getMetadataTcga_SampleTypeName(theId);
			assertEquals(expResult, result);
		}
		{
			String theId = "61";
			MetadataTcgaNames instance = mTest;
			String expResult = "Cell Line Derived Xenograft Tissue";
			String result = instance.getMetadataTcga_SampleTypeName(theId);
			assertEquals(expResult, result);
		}
	}	
}
