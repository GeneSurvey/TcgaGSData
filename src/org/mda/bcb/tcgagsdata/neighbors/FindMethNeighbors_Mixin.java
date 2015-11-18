/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.neighbors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.mda.bcb.tcgagsdata.TcgaGSData;
import org.mda.bcb.tcgagsdata.retrieve.MetadataProbe;

/**
 *
 * @author tdcasasent
 */
public abstract class FindMethNeighbors_Mixin
{
	protected String mMapFile = null;
	protected String mPath = null;
	protected MetadataProbe mReadProbe = null;

	public FindMethNeighbors_Mixin(String theMapFile, String theDirectory)
	{
		mMapFile = theMapFile;
		mPath = theDirectory;
	}

	abstract protected MetadataProbe getReadProbe(String theHeaderLine, String[] theSplitted);
	abstract protected void addProbe(MetadataProbe theProbe);
	abstract protected ArrayList<MetadataProbe> getProbes();

	public MetadataProbe [] findNeighbors(long theMin, long theMax, String theChromosome) throws IOException
	{
		TcgaGSData.printVersion();
		long start = System.currentTimeMillis();
		ArrayList<MetadataProbe> probeList = getProbes();
		if (null==probeList)
		{
			File input = new File(mPath, mMapFile);
			if (input.exists())
			{
				try(BufferedReader br = Files.newBufferedReader(
						Paths.get(input.getAbsolutePath()),
						Charset.availableCharsets().get("ISO-8859-1")))
				{
					String headers=br.readLine();
					// get line after headers
					String line=br.readLine();
					while(null!=line)
					{
						String [] splitted = line.split("\t", -1);
						addProbe(getReadProbe(headers, splitted));
						line=br.readLine();
					}
				}
			}
			probeList = getProbes();
		}
		ArrayList<MetadataProbe> results = new ArrayList<>();
		for(MetadataProbe probe : probeList)
		{
			if ((probe.mProbeLocation<=theMax)&&(probe.mProbeLocation>=theMin))
			{
				if(theChromosome.equalsIgnoreCase(probe.mChromosome))
				{
					results.add(probe);
				}
			}
		}
		long finish = System.currentTimeMillis();
		TcgaGSData.printWithFlag("Find " + results.size() + " Neighbors between " + theMin + " and " + theMax + " for " + mMapFile + " in " + ((finish-start)/1000.0) + " seconds");
		MetadataProbe [] retArray = results.toArray(new MetadataProbe[0]);
		TcgaGSData.printWithFlag("made array");
		return retArray;
	}

}
