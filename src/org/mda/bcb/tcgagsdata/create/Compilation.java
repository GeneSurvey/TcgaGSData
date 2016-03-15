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
import java.util.Collection;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.mda.bcb.tcgagsdata.GSStringUtils;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class Compilation
{
	public String mClinicalDir = null;
	public String mInputFiles = null;
	public String mOutputFile = null;

	public Compilation(String theClinicalDir, String theInputFiles, String theOutputFile)
	{
		mClinicalDir = theClinicalDir;
		mInputFiles = theInputFiles;
		mOutputFile = theOutputFile;
	}
	
	protected ArrayList<String> getHeaders(Collection<File> theFiles) throws IOException
	{
		TcgaGSData.printWithFlag("Compilation::getHeaders");
		ArrayList<String> headers = new ArrayList<>();
		for(File clinFile : theFiles)
		{
			try (BufferedReader br = Files.newBufferedReader(
							Paths.get(clinFile.getAbsolutePath()),
							Charset.availableCharsets().get("ISO-8859-1")))
			{
				String line = br.readLine();
				String [] hdrs = line.split("\t", -1);
				for(String hdr : hdrs)
				{
					if ((null!=hdr)&&(false=="".equals(hdr)))
					{
						if (false==headers.contains(hdr))
						{
							headers.add(hdr);
						}
					}
				}
			}
		}
		return headers;
	}

	public void process() throws IOException, Exception
	{
		TcgaGSData.printWithFlag("Compilation::process - mClinicalDir=" + mClinicalDir);
		TcgaGSData.printWithFlag("Compilation::process - mInputFiles=" + mInputFiles);
		TcgaGSData.printWithFlag("Compilation::process - mOutputFile=" + mOutputFile);
		Collection<File> results = FileUtils.listFiles(new File(mClinicalDir), FileFilterUtils.nameFileFilter(mInputFiles), TrueFileFilter.INSTANCE);
		ArrayList<String> headers = getHeaders(results);
		TreeSet<String> patients = new TreeSet<>();
		TreeSet<String> lines = new TreeSet<>();
		String headerLine = null;
		for(String header : headers)
		{
			if (null==headerLine)
			{
				headerLine = header;
			}
			else
			{
				headerLine = headerLine + "\t" + header;
			}
		}
		boolean headersNeeded = true;
		for(File clinFile : results)
		{
			TcgaGSData.printWithFlag("Compilation::process - clinFile=" + clinFile.getAbsolutePath());
			try (BufferedReader br = Files.newBufferedReader(
							Paths.get(clinFile.getAbsolutePath()),
							Charset.availableCharsets().get("ISO-8859-1")))
			{
				String line = br.readLine();
				ArrayList<String> currentHeaders = new ArrayList<>();
				currentHeaders.addAll(Arrays.asList(line.split("\t", -1)));
				for (line = br.readLine(); null != line; line = br.readLine())
				{
					String newLine = null;
					String [] splitted = line.split("\t", -1);
					for(String header : headers)
					{
						String token = "NA";
						int index = currentHeaders.indexOf(header);
						if (index>-1)
						{
							token = splitted[index];
						}
						if (null==newLine)
						{
							newLine = token;
						}
						else
						{
							newLine = newLine + "\t" + token;
						}
					}
					lines.add(newLine);
					String patient = GSStringUtils.beforeTab(newLine);
					if (false==patients.add(patient))
					{
						throw new Exception("ERROR - patient duplicated " + patient);
					}
				}
			}
		}
		try (BufferedWriter bw = Files.newBufferedWriter(
						Paths.get(mOutputFile),
						Charset.availableCharsets().get("ISO-8859-1")))
		{
			bw.write(headerLine);
			bw.newLine();
			for(String line : lines)
			{
				bw.write(line);
				bw.newLine();
			}
		}
	}
}
