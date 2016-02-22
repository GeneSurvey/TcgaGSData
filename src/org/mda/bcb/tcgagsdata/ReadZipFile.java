/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mda.bcb.tcgagsdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author linux
 */
public abstract class ReadZipFile
{
	protected String mZipFile = null;
	
	public ReadZipFile(String theZipFile)
	{
		mZipFile = theZipFile;
	}
	
	public boolean processFile(String theInternalFile) throws IOException
	{
		TcgaGSData.printWithFlag("ReadZipFile::processFile mZipFile='" + mZipFile + "'");
		TcgaGSData.printWithFlag("ReadZipFile::processFile theInternalFile='" + theInternalFile + "'");
		try (ZipFile zf = new ZipFile(mZipFile))
		{
			ZipEntry ze = zf.getEntry(theInternalFile);
			try (InputStream is = zf.getInputStream(ze))
			{
				try(BufferedReader bfr = new BufferedReader(new InputStreamReader(is)))
				{
					boolean keepLooking = true;
					for(String line=bfr.readLine() ; ((null!=line)&&(true==keepLooking)); line=bfr.readLine())
					{
						keepLooking = processLine(line);
					}
				}
			}
		}
		return true;
	}
	
	abstract protected boolean processLine(String theLine);
}
