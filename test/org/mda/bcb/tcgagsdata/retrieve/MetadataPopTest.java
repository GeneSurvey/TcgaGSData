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
public class MetadataPopTest
{
	public String mDir = "/mnt/hgfs/code/development/TcgaGSData/";
	public MetadataPop mTest = null;
	
	public MetadataPopTest()
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
		mTest = new MetadataPop(new File(mDir, "data").getAbsolutePath());
	}
	
	@After
	public void tearDown()
	{
	}

	protected String getValue(String theId)
	{
		int index = java.util.Arrays.asList(mTest.mIds).indexOf(theId);
		return mTest.mValues[index];
	}
	
	/**
	 * Test of getMetadataPop_BarcodeDisease method, of class MetadataPop.
	 */
	@Test
	public void testGetMetadataPop_BarcodeDisease() throws Exception
	{
		System.out.println("getMetadataPop_BarcodeDisease");
		MetadataPop instance = mTest;
		boolean expResult = true;
		boolean result = instance.getMetadataPop_BarcodeDisease();
		assertEquals(expResult, result);
		assertEquals("OV", getValue("TCGA-01-0628-11A-01D-0356-01"));
		assertEquals("CESC", getValue("TCGA-ZX-AA5X-10A-01D-A42R-09"));
	}

	/**
	 * Test of getMetadataPop_BarcodeSamplecode method, of class MetadataPop.
	 */
	@Test
	public void testGetMetadataPop_BarcodeSamplecode() throws Exception
	{
		System.out.println("getMetadataPop_BarcodeSamplecode");
		MetadataPop instance = mTest;
		boolean expResult = true;
		boolean result = instance.getMetadataPop_BarcodeSamplecode();
		assertEquals(expResult, result);
		assertEquals("11", getValue("TCGA-01-0628-11A-01D-0356-01"));
		assertEquals("10", getValue("TCGA-ZX-AA5X-10A-01D-A42R-09"));
	}

	/**
	 * Test of getMetadataPop_PatientDisease method, of class MetadataPop.
	 */
	@Test
	public void testGetMetadataPop_PatientDisease() throws Exception
	{
		System.out.println("getMetadataPop_PatientDisease");
		MetadataPop instance = mTest;
		boolean expResult = true;
		boolean result = instance.getMetadataPop_PatientDisease();
		assertEquals(expResult, result);
		assertEquals("OV", getValue("TCGA-01-0628"));
		assertEquals("CESC", getValue("TCGA-ZX-AA5X"));
	}
}
