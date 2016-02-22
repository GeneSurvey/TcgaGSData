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
public class MetadataProbeTest
{
	public String mDir = "/mnt/hgfs/code/development/GENE_REPORT_2015_12_11.zip";
	public CallFromR mTest = null;
	
	public MetadataProbeTest()
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
	 * Test of getMetadata_Meth27 method, of class MetadataProbe.
	 */
	@Test
	public void testGetMetadata_Meth27() throws Exception
	{
		System.out.println("getMetadata_Meth27");
		String theProbe = "cg00000292";
		MetadataProbe result = mTest.getMetadata_Meth27(theProbe);
		assertEquals(theProbe, result.mName);
	}

	/**
	 * Test of getMetadata_Meth450 method, of class MetadataProbe.
	 */
	@Test
	public void testGetMetadata_Meth450() throws Exception
	{
		System.out.println("getMetadata_Meth450");
		String theProbe = "cg00000029";
		MetadataProbe result = mTest.getMetadata_Meth450(theProbe);
		assertEquals(theProbe, result.mName);
	}
}
