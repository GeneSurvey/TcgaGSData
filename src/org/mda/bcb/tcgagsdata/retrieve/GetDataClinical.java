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
public class GetDataClinical
{
	protected static String M_PATH = null;
	protected static String [][] M_GENES_BY_SAMPLES_VALUES = null;
	protected static String [] M_COLUMN_LABELS = null;
	protected static String [] M_PATIENT_IDS = null;
	public String [][] mGenesBySamplesValues = null;
	public String [] mPatientIds = null;
	public String [] mColumnLabels = null;
		
	public GetDataClinical(String thePath)
	{
		if (false==thePath.equals(M_PATH))
		{
			M_PATH = thePath;
			M_GENES_BY_SAMPLES_VALUES = null;
			M_COLUMN_LABELS = null;
			M_PATIENT_IDS = null;
		}
	}

	public boolean getDataClinical() throws IOException, Exception
	{
		TcgaGSData.printVersion();
		boolean found = false;
		try
		{
			long start = System.currentTimeMillis();
			mGenesBySamplesValues = null;
			mPatientIds = null;
			mColumnLabels = null;
			if (null==M_GENES_BY_SAMPLES_VALUES)
			{
				File input = new File(M_PATH, "combined_clinical.tsv");
				if (input.exists())
				{
					ArrayList<String> lines = new ArrayList<>();
					try(BufferedReader br = Files.newBufferedReader(
							Paths.get(input.getAbsolutePath()),
							Charset.availableCharsets().get("ISO-8859-1")))
					{
						found = true;
						String [] columns = br.readLine().split("\t", -1);
						for (String line = br.readLine(); null != line; line = br.readLine())
						{
							lines.add(line);
						}
						int patientCount = lines.size();
						int columnCount = columns.length - 1;
						M_COLUMN_LABELS = new String[columnCount];
						for(int x=1;x<=columnCount;x++)
						{
							M_COLUMN_LABELS[x-1] = columns[x];
						}
						M_PATIENT_IDS = new String[patientCount];
						M_GENES_BY_SAMPLES_VALUES = new String[patientCount][columnCount];
						for(int x = 0; x<patientCount; x++)
						{
							String [] splitted = lines.get(x).split("\t", -1);
							if (splitted.length<=columnCount)
							{
								throw new Exception("Column count does not match. Found " + splitted.length + 
										" but expected " + columnCount +". For line\n" + lines.get(x));
							}
							M_PATIENT_IDS[x] = splitted[0];
							for(int y=1;y<=columnCount;y++)
							{
								System.out.println("y is " + y + " and splitted is " + splitted.length + " long");
								String myY = splitted[y];
								M_GENES_BY_SAMPLES_VALUES[x][y-1] = myY;
							}
						}
					}
				}
			}
			else
			{
				found = true;
			}
			long finish = System.currentTimeMillis();
			mGenesBySamplesValues = M_GENES_BY_SAMPLES_VALUES;
			mPatientIds = M_PATIENT_IDS;
			mColumnLabels = M_COLUMN_LABELS;
			if(true==found)
			{
				TcgaGSData.printWithFlag("Clincial file retrieved in " + ((finish-start)/1000.0) + " seconds");
			}
			else
			{
				TcgaGSData.printWithFlag("Clincial file not found combined_clinical.tsv at " + M_PATH  + " in "+ ((finish-start)/1000.0) + " seconds");
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_GENES_BY_SAMPLES_VALUES = null;
			M_COLUMN_LABELS = null;
			M_PATIENT_IDS = null;
			throw exp;
		}
		return found;
	}
}
