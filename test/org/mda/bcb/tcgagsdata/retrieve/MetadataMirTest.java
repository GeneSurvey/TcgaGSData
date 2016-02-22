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
import org.mda.bcb.tcgagsdata.CallFromR;

/**
 *
 * @author linux
 */
public class MetadataMirTest
{
	public String mDir = "/mnt/hgfs/code/development/GENE_REPORT_2015_12_11.zip";
	public CallFromR mTest = null;
	
	public MetadataMirTest()
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
		mTest = new CallFromR(mDir);
	}
	
	@After
	public void tearDown()
	{
	}

	/**
	 * Test of getMirList method, of class MetadataMir.
	 */
	@Test
	public void testGetMirList() throws Exception
	{
		System.out.println("getMirList");
		String[] result = mTest.getMirList();
		assertTrue(result.length>0);
	}

	/**
	 * Test of getMimatList method, of class MetadataMir.
	 */
	@Test
	public void testGetMimatList() throws Exception
	{
		System.out.println("getMimatList");
		String[] result = mTest.getMimatList();
		assertTrue(result.length>0);
	}

	/**
	 * Test of getMetadata_miRNA_mir method, of class MetadataMir.
	 */
	@Test
	public void testGetMetadata_miRNA_mir() throws Exception
	{
		System.out.println("getMetadata_miRNA_mir");
		{
			String theMirId = "hsa-let-7d";
			String expResult = "MI0000065";
			MetadataMir[] result = mTest.getMetadata_miRNA_mir(theMirId);
			assertEquals(expResult, result[0].mMimatId);
		}
		{
			String theMirId = "hsa-let-7a-1";
			String expResult = "MI0000060";
			MetadataMir[] result = mTest.getMetadata_miRNA_mir(theMirId);
			assertEquals(expResult, result[0].mMimatId);
		}
		{
			String theMirId = "hsa-mir-99b";
			String expResult = "MI0000746";
			MetadataMir[] result = mTest.getMetadata_miRNA_mir(theMirId);
			assertEquals(expResult, result[0].mMimatId);
		}
		{
			String theMirId = "hsa-let-7a-5p";
			String expResult = "MIMAT0000062";
			MetadataMir[] result = mTest.getMetadata_miRNA_mir(theMirId);
			assertEquals(expResult, result[0].mMimatId);
		}
		
	}

	/**
	 * Test of getMetadata_miRNA_mimat method, of class MetadataMir.
	 */
	@Test
	public void testGetMetadata_miRNA_mimat() throws Exception
	{
		System.out.println("getMetadata_miRNA_mimat");
		{
			String theMimatId = "MIMAT0000098";
			String expResult = "hsa-miR-100-5p";
			MetadataMir[] result = mTest.getMetadata_miRNA_mimat(theMimatId);
			assertEquals(expResult, result[0].mMirId);
		}
		{
			String theMimatId = "MIMAT0000062_1";
			String expResult = "hsa-let-7a-5p";
			MetadataMir[] result = mTest.getMetadata_miRNA_mimat(theMimatId);
			assertEquals(expResult, result[0].mMirId);
		}
	}
}
