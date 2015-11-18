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
import java.util.Arrays;
import org.mda.bcb.tcgagsdata.TcgaGSData;
import org.mda.bcb.tcgagsdata.retrieve.MetadataGene;

/**
 *
 * @author linux
 */
public class FindGeneScoreNeighbors_Mixin
{
	protected String mMapFile = null;
	protected String mPath = null;
	//
	public int mIndexGeneSymbol = -1;
	public int mIndexChromosome = -1;
	public int mIndexLocationStart = -1;
	public int mIndexLocationEnd = -1;
	public int mIndexStrand = -1;
	
	public FindGeneScoreNeighbors_Mixin(String theMapFile, String theDirectory)
	{
		mMapFile = theMapFile;
		mPath = theDirectory;
	}
	
	public void populateHeaderLinesGeneric(String theLine)
	{
		// first line headers
		ArrayList<String> headers = new ArrayList<>();
		headers.addAll(Arrays.asList(theLine.split("\t", -1)));
		mIndexGeneSymbol = headers.indexOf("gene_symbol");
		mIndexChromosome = headers.indexOf("chromosome");
		mIndexLocationStart = headers.indexOf("start_loc");
		mIndexLocationEnd = headers.indexOf("stop_loc");
		mIndexStrand = headers.indexOf("strand");
	}

	public MetadataGene makeMetadataGeneric(String[] theSplitted)
	{
		MetadataGene mdG = new MetadataGene(mPath);
		mdG.mGeneSymbol = theSplitted[mIndexGeneSymbol];
		mdG.mGeneId = "none";
		mdG.mVersionIndex = "none";
		mdG.mChromosome = theSplitted[mIndexChromosome];
		mdG.mLocationStart = theSplitted[mIndexLocationStart];
		mdG.mLocationEnd = theSplitted[mIndexLocationEnd];
		mdG.mStrand = theSplitted[mIndexStrand];
		return mdG;
	}
	
	public MetadataGene matchesParameters(String[] theSplitted, long theMin, long theMax, String theChromosome, String theStrand)	
	{
		MetadataGene result = null;
		MetadataGene mdg = makeMetadataGeneric(theSplitted);
		if ((null==theChromosome)||(theChromosome.equals(mdg.mChromosome)))
		{
			if ((null==theStrand)||(theStrand.equals(mdg.mStrand)))
			{
				int start = Integer.parseInt(mdg.mLocationStart);
				int end = Integer.parseInt(mdg.mLocationEnd);
				if ((start<=theMax)&&(theMin<=end))
				{
					result = mdg;
				}
			}	
		}
		return result;
	}
	
	public MetadataGene [] findNeighbors(long theMin, long theMax, String theChromosome, String theStrand) throws IOException
	{
		ArrayList<MetadataGene> list = new ArrayList<>();
		long start = System.currentTimeMillis();
		File input = new File(mPath, mMapFile);
		if (input.exists())
		{
			try (BufferedReader br = Files.newBufferedReader(
					Paths.get(input.getAbsolutePath()),
					Charset.availableCharsets().get("ISO-8859-1")))
			{
				String line = br.readLine();
				populateHeaderLinesGeneric(line);
				line = br.readLine();
				while (null != line)
				{
					String[] splitted = line.split("\t", -1);
					MetadataGene mdg = matchesParameters(splitted, theMin, theMax, theChromosome, theStrand);
					if (null!=mdg)
					{
						list.add(mdg);
					}
					line = br.readLine();
				}
			}
		}
		long finish = System.currentTimeMillis();
		TcgaGSData.printWithFlag("FindGeneScoreNeighbors_Mixin findNeighbors retrieved " + list.size() + " elements for in " + ((finish - start) / 1000.0) + " seconds");
		return list.toArray(new MetadataGene[0]);
	}
}
