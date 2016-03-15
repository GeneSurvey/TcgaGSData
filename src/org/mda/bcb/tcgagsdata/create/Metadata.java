/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.mda.bcb.tcgagsdata.TcgaGSData;
import org.mda.bcb.tcgagsdata.retrieve.MetadataTcgaNames;

/**
 *
 * @author tdcasasent
 */
public class Metadata
{

	protected String mMetadataFile = null;

	public Metadata(String theMetadataFile)
	{
		mMetadataFile = theMetadataFile;
	}

	protected String trimToPatientId(String theBarcode)
	{
		return theBarcode.substring(0, 12);
	}

	public void writePatientDataFile(String theIdColumn, String theDataColumn, 
			String theOutputFile, File [] theDiseaseSamplesFiles) throws IOException
	{
		// TODO: theDiseaseSampleFile - disease in first column, rest of row is SAMPLE barcode
		TcgaGSData.printWithFlag("Metadata::writePatientDataFile - start " + theOutputFile);
		TreeMap<String, String> patientDisease = new TreeMap<>();
		try (BufferedReader br = Files.newBufferedReader(
						Paths.get(mMetadataFile),
						Charset.availableCharsets().get("ISO-8859-1")))
		{
			// read header/write header
			int indexId = -1;
			int indexData = -1;
			{
				String line = br.readLine();
				ArrayList<String> headerArray = new ArrayList<>();
				headerArray.addAll(Arrays.asList(line.split("\t", -1)));
				indexId = headerArray.indexOf(theIdColumn);
				indexData = headerArray.indexOf(theDataColumn);
			}
			//
			for (String line = br.readLine(); null != line; line = br.readLine())
			{
				String[] splitted = line.split("\t", -1);
				patientDisease.put(trimToPatientId(splitted[indexId]), splitted[indexData]);
			}
			for (File file : theDiseaseSamplesFiles)
			{
				TreeSet<String> barcodes = getDiseaseSampleData(file, true);
				for (String barcode : barcodes)
				{
					String patientId = trimToPatientId(barcode);
					if(false==patientDisease.keySet().contains(patientId))
					{
						patientDisease.put(patientId, MetadataTcgaNames.M_UNKNOWN);
					}
				}
			}
		}
		try (BufferedWriter bw = Files.newBufferedWriter(
						Paths.get(theOutputFile),
						Charset.availableCharsets().get("ISO-8859-1")))
		{
			bw.write("ID\tDATA");
			bw.newLine();
			for(String key: patientDisease.keySet())
			{
				bw.write(key + "\t" + patientDisease.get(key));
				bw.newLine();
			}
		}
		TcgaGSData.printWithFlag("Metadata::writePatientDataFile - finished " + theOutputFile);
	}

	public void writeBarcodeDataFile(String theIdColumn, String theDataColumn, 
			String theOutputFile, File [] theDiseaseSamplesFiles) throws IOException
	{
		// TODO: theDiseaseSampleFile - disease in first column, rest of row is SAMPLE barcode
		TcgaGSData.printWithFlag("Metadata::writeBarcodeDataFile - start " + theOutputFile);
		TreeSet<String> processedBarcode = new TreeSet<>();
		try (BufferedReader br = Files.newBufferedReader(
						Paths.get(mMetadataFile),
						Charset.availableCharsets().get("ISO-8859-1")))
		{
			try (BufferedWriter bw = Files.newBufferedWriter(
							Paths.get(theOutputFile),
							Charset.availableCharsets().get("ISO-8859-1")))
			{
				// read header/write header
				int indexId = -1;
				int indexData = -1;
				{
					String line = br.readLine();
					ArrayList<String> headerArray = new ArrayList<>();
					headerArray.addAll(Arrays.asList(line.split("\t", -1)));
					indexId = headerArray.indexOf(theIdColumn);
					indexData = headerArray.indexOf(theDataColumn);
				}
				bw.write("ID\tDATA");
				bw.newLine();
				//
				for (String line = br.readLine(); null != line; line = br.readLine())
				{
					String[] splitted = line.split("\t", -1);
					bw.write(splitted[indexId] + "\t" + splitted[indexData]);
					processedBarcode.add(splitted[indexId]);
					bw.newLine();
				}
				TcgaGSData.printWithFlag("Metadata::writeBarcodeDataFile - processed file " + theOutputFile);
				for (File file : theDiseaseSamplesFiles)
				{
					TreeSet<String> barcodes = getDiseaseSampleData(file, true);
					for (String barcode : barcodes)
					{
						if(false==processedBarcode.contains(barcode))
						{
							bw.write(barcode + "\t" + MetadataTcgaNames.M_UNKNOWN);
							processedBarcode.add(barcode);
							bw.newLine();
						}
					}
				}
			}
		}
		TcgaGSData.printWithFlag("Metadata::writeBarcodeDataFile - finished");
	}
	
	public TreeSet<String> getDiseaseSampleData(File theFile, boolean theBarcodeFlag) throws IOException
	{
		TreeSet<String> results = new TreeSet<>();
		try (BufferedReader br = Files.newBufferedReader(
						Paths.get(theFile.getAbsolutePath()),
						Charset.availableCharsets().get("ISO-8859-1")))
		{
			for (String line = br.readLine(); null != line; line = br.readLine())
			{
				String[] splitted = line.split("\t", -1);
				for(int index=1;index<splitted.length;index++)
				{
					if (true==theBarcodeFlag)
					{
						results.add(splitted[index]);
					}
					else
					{
						results.add(trimToPatientId(splitted[index]));
					}
				}
			}
		}
		return results;
	}

	static public String findMetadataFile(File theDir)
	{
		File metadataFile = FileUtils.listFiles(theDir, new WildcardFileFilter("metadata*.tsv"), TrueFileFilter.INSTANCE).iterator().next();
		return metadataFile.getAbsolutePath();
	}
}
