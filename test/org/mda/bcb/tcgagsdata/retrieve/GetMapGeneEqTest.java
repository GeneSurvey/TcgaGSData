/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class GetMapGeneEqTest
{
	public String mDir = "/mnt/hgfs/code/development/GENE_REPORT_2015_12_11.zip";
	public CallFromR mTest = null;
	
	public GetMapGeneEqTest()
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
	 * Test of getMapping_Meth450 method, of class GetMapGeneEq.
	 */
	@Test
	public void testGetMapping_Meth450() throws Exception
	{
		System.out.println("getMapping_Meth450");
		String [] results = mTest.getMapping_Meth450("TP53");
		assertNotNull(results);
	}

	/**
	 * Test of getMapping_Meth27 method, of class GetMapGeneEq.
	 */
	@Test
	public void testGetMapping_Meth27() throws Exception
	{
		System.out.println("getMapping_Meth27");
		String [] results = mTest.getMapping_Meth27("TP53");
		assertNotNull(results);
	}

	/**
	 * Test of getMappingGeneSymbols_Meth450 method, of class GetMapGeneEq.
	 */
	@Test
	public void testGetMappingGeneSymbols_Meth450() throws Exception
	{
		System.out.println("getMapping_Meth450");
		String [] results = mTest.getMappingGeneSymbols_Meth450();
		assertNotNull(results);
	}

	/**
	 * Test of getMappingGeneSymbols_Meth27 method, of class GetMapGeneEq.
	 */
	@Test
	public void testGetMappingGeneSymbols_Meth27() throws Exception
	{
		System.out.println("getMapping_Meth450");
		String [] results = mTest.getMappingGeneSymbols_Meth27();
		assertNotNull(results);
	}
	
}
