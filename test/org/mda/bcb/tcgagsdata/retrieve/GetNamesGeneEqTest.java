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
public class GetNamesGeneEqTest
{
	public String mDir = "/mnt/hgfs/code/development/TcgaGSData/";
	public GetNamesGeneEq mTest = null;

	public GetNamesGeneEqTest()
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
		mTest = new GetNamesGeneEq(new File(mDir, "combined").getAbsolutePath());
	}
	
	@After
	public void tearDown()
	{
	}

	/**
	 * Test of getNames_RnaSeq2 method, of class GetNamesGeneEq.
	 */
	@Test
	public void testGetNames_RnaSeq2() throws Exception
	{
		System.out.println("getNames_RnaSeq2");
		GetNamesGeneEq instance = mTest;
		boolean expResult = true;
		boolean result = instance.getNames_RnaSeq2();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getNames_RnaSeq method, of class GetNamesGeneEq.
	 */
	@Test
	public void testGetNames_RnaSeq() throws Exception
	{
		System.out.println("getNames_RnaSeq");
		GetNamesGeneEq instance = mTest;
		boolean expResult = true;
		boolean result = instance.getNames_RnaSeq();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getNames_SNP6 method, of class GetNamesGeneEq.
	 */
	@Test
	public void testGetNames_SNP6() throws Exception
	{
		System.out.println("getNames_SNP6");
		GetNamesGeneEq instance = mTest;
		boolean expResult = true;
		boolean result = instance.getNames_SNP6();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getNames_Meth450 method, of class GetNamesGeneEq.
	 */
	@Test
	public void testGetNames_Meth450() throws Exception
	{
		System.out.println("getNames_Meth450");
		GetNamesGeneEq instance = mTest;
		boolean expResult = true;
		boolean result = instance.getNames_Meth450();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getNames_Meth27 method, of class GetNamesGeneEq.
	 */
	@Test
	public void testGetNames_Meth27() throws Exception
	{
		System.out.println("getNames_Meth27");
		GetNamesGeneEq instance = mTest;
		boolean expResult = true;
		boolean result = instance.getNames_Meth27();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getNames_miRNASeq method, of class GetNamesGeneEq.
	 */
	@Test
	public void testGetNames_miRNASeq() throws Exception
	{
		System.out.println("getNames_miRNASeq");
		GetNamesGeneEq instance = mTest;
		boolean expResult = true;
		boolean result = instance.getNames_miRNASeq();
		assertEquals(expResult, result);
	}
}
