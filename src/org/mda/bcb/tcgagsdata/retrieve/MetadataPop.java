/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.retrieve;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class MetadataPop
{
	public String mPath = null;
	public int mLength = 0;
	public String [] mValues = null;
	public String [] mIds = null;
	// BARCODEDISEASE
	static protected String M_PATH_BARCODEDISEASE = null;
	static protected int M_LENGTH_BARCODEDISEASE = 0;
	static protected String [] M_VALUES_BARCODEDISEASE = null;
	static protected String [] M_IDS_BARCODEDISEASE = null;
	// BARCODESAMPLECODE
	static protected String M_PATH_BARCODESAMPLECODE = null;
	static protected int M_LENGTH_BARCODESAMPLECODE = 0;
	static protected String [] M_VALUES_BARCODESAMPLECODE = null;
	static protected String [] M_IDS_BARCODESAMPLECODE = null;
	// PATIENTDISEASE
	static protected String M_PATH_PATIENTDISEASE = null;
	static protected int M_LENGTH_PATIENTDISEASE = 0;
	static protected String [] M_VALUES_PATIENTDISEASE = null;
	static protected String [] M_IDS_PATIENTDISEASE = null;

	public MetadataPop(String thePath)
	{
		TcgaGSData.printVersion();
		mPath = thePath;
	}

	public boolean getMetadataPop_BarcodeDisease() throws IOException
	{
		boolean loaded = false;
		if ((null==M_PATH_BARCODEDISEASE)||(false==mPath.equals(M_PATH_BARCODEDISEASE)))
		{
			loaded = getMetadataPop("barcode_disease.tsv");
			if (true==loaded)
			{
				M_PATH_BARCODEDISEASE = mPath;
				M_LENGTH_BARCODEDISEASE = mLength;
				M_VALUES_BARCODEDISEASE = mValues;
				M_IDS_BARCODEDISEASE = mIds;
			}
		}
		else
		{
			loaded = true;
			mLength = M_LENGTH_BARCODEDISEASE;
			mValues = M_VALUES_BARCODEDISEASE;
			mIds = M_IDS_BARCODEDISEASE;
		}
		return loaded;
	}

	public boolean getMetadataPop_BarcodeSamplecode() throws IOException
	{
		boolean loaded = false;
		if ((null==M_PATH_BARCODESAMPLECODE)||(false==mPath.equals(M_PATH_BARCODESAMPLECODE)))
		{
			loaded = getMetadataPop("barcode_samplecode.tsv");
			if (true==loaded)
			{
				M_PATH_BARCODESAMPLECODE = mPath;
				M_LENGTH_BARCODESAMPLECODE = mLength;
				M_VALUES_BARCODESAMPLECODE = mValues;
				M_IDS_BARCODESAMPLECODE = mIds;
			}
		}
		else
		{
			loaded = true;
			mLength = M_LENGTH_BARCODESAMPLECODE;
			mValues = M_VALUES_BARCODESAMPLECODE;
			mIds = M_IDS_BARCODESAMPLECODE;
		}
		return loaded;
	}

	public boolean getMetadataPop_PatientDisease() throws IOException
	{
		boolean loaded = false;
		if ((null==M_PATH_PATIENTDISEASE)||(false==mPath.equals(M_PATH_PATIENTDISEASE)))
		{
			loaded = getMetadataPop("patient_disease.tsv");
			if (true==loaded)
			{
				M_PATH_PATIENTDISEASE = mPath;
				M_LENGTH_PATIENTDISEASE = mLength;
				M_VALUES_PATIENTDISEASE = mValues;
				M_IDS_PATIENTDISEASE = mIds;
			}
		}
		else
		{
			loaded = true;
			mLength = M_LENGTH_PATIENTDISEASE;
			mValues = M_VALUES_PATIENTDISEASE;
			mIds = M_IDS_PATIENTDISEASE;
		}
		return loaded;
	}

	public boolean getMetadataPop(String theFile) throws IOException
	{
		boolean found = false;
		try
		{
			mLength = 0;
			mValues = null;
			mIds = null;
			long start = System.currentTimeMillis();
			File input = new File(mPath, theFile);
			if (input.exists())
			{
				try(BufferedReader br = Files.newBufferedReader(
						Paths.get(input.getAbsolutePath()),
						Charset.availableCharsets().get("ISO-8859-1")))
				{
					// first line header (ID\tDATA)
					br.readLine();
					ArrayList<String> ids = new ArrayList<>();
					ArrayList<String> data = new ArrayList<>();
					for(String line=br.readLine();null!=line;line=br.readLine())
					{
						String [] splitted = line.split("\t", -1);
						ids.add(splitted[0]);
						data.add(splitted[1]);
					}
					found = true;
					mLength = ids.size();
					mValues = data.toArray(new String[0]);
					mIds = ids.toArray(new String[0]);
				}
			}
			long finish = System.currentTimeMillis();
			TcgaGSData.printWithFlag("ListMetadata " + theFile + " in " + ((finish-start)/1000.0) + " seconds");
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
		}
		return found;
	}
}
