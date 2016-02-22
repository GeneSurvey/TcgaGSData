/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.retrieve;

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
public class OneToOneUcscHgncTest
{
	public String mDir = "/mnt/hgfs/code/development/GENE_REPORT_2015_12_11.zip";
	public CallFromR mTest = null;
	
	public OneToOneUcscHgncTest()
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
	 * Test of getOneToOne_UCSC_List method, of class OneToOneUcscHgnc.
	 */
	@Test
	public void testGetOneToOne_UCSC_List() throws Exception
	{
		System.out.println("getOneToOne_UCSC_List");
		String[] result = mTest.getOneToOne_UCSC_List();
		assertEquals(28829, result.length);
		assertEquals("uc001aak.3", result[0]);
		assertEquals("uc033fgu.1", result[result.length-1]);
	}

	/**
	 * Test of getOneToOne_GeneSymbol_List method, of class OneToOneUcscHgnc.
	 */
	@Test
	public void testGetOneToOne_GeneSymbol_List() throws Exception
	{
		System.out.println("getOneToOne_GeneSymbol_List");
		String[] result = mTest.getOneToOne_GeneSymbol_List();
		assertTrue(result.length >= 28829);
		assertEquals("6M1-18", result[0]);
		assertEquals("ZZZ3", result[result.length-1]);
	}

	/**
	 * Test of getOneToOne_GeneSymbol_UCID method, of class OneToOneUcscHgnc.
	 */
	@Test
	public void testGetOneToOne_GeneSymbol_UCID() throws Exception
	{
		System.out.println("getOneToOne_GeneSymbol_UCID");
		{
			String theId = "uc001aak.3";
			String expResult = "FAM138A";
			String result = mTest.getOneToOne_GeneSymbol_UCID(theId);
			assertEquals(expResult, result);
		}
		{
			String theId = "uc001amp.2";
			String expResult = "GPR153";
			String result = mTest.getOneToOne_GeneSymbol_UCID(theId);
			assertEquals(expResult, result);
		}
	}

	/**
	 * Test of getOneToOne_UCID_GeneSymbol method, of class OneToOneUcscHgnc.
	 */
	@Test
	public void testGetOneToOne_UCID_GeneSymbol() throws Exception
	{
		System.out.println("getOneToOne_UCID_GeneSymbol");
		{
			String theId = "JA202330";
			String expResult = "uc031til.1";
			String result = mTest.getOneToOne_UCID_GeneSymbol(theId);
			assertEquals(expResult, result);
		}
//		{
//			String theId = "ABCB8";
//			String expResult = "uc003wil.5";
//			String result = mTest.getOneToOne_UCID_GeneSymbol(theId);
//			assertEquals(expResult, result);
//		}
		{
			String theId = "ABCC5";
			String expResult = "uc003fmg.3";
			String result = mTest.getOneToOne_UCID_GeneSymbol(theId);
			assertEquals(expResult, result);
		}
	}
}
