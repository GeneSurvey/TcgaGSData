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
public class GetDataMatrixTest
{
	public String mDir = "/mnt/hgfs/code/development/GENE_REPORT_2015_12_11.zip";
	public CallFromR mTest = null;
	
	public GetDataMatrixTest()
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
	 * Test of getDataMatrix method, of class GetDataMatrix.
	 */
	@Test
	public void testGetDataMatrix() throws Exception
	{
		System.out.println("getDataMatrix_Meth27");
		String [] geneEq = { "cg00578575" };
		GetDataMatrix results = mTest.getDataMatrix_Meth27(geneEq);
		assertNotNull(results);
		assertTrue(results.mGenesSize>0);
		geneEq[0] = "cg00000321";
		results = mTest.getDataMatrix_Meth450(geneEq);
		assertNotNull(results);
		assertTrue(results.mGenesSize>0);
		geneEq[0] = "TP53";
		results = mTest.getDataMatrix_Mutations(geneEq);
		assertNotNull(results);
		assertTrue(results.mGenesSize>0);
		geneEq[0] = "TP53";
		results = mTest.getDataMatrix_RnaSeq(geneEq);
		assertNotNull(results);
		assertTrue(results.mGenesSize>0);
		geneEq[0] = "TP53";
		results = mTest.getDataMatrix_RnaSeq2(geneEq);
		assertNotNull(results);
		assertTrue(results.mGenesSize>0);
		geneEq[0] = "hsa-let-7e.MIMAT0000066";
		results = mTest.getDataMatrix_miRNASeq(geneEq);
		assertNotNull(results);
		assertTrue(results.mGenesSize>0);
	}
	
}
