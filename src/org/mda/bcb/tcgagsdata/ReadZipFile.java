/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
