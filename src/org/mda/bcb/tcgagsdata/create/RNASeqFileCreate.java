/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author linux
 */
public class RNASeqFileCreate
{
	protected TreeMap<String, String> mColMustEqual = null;
	protected String mLineHeaders = null;
	protected TreeSet<String> mLineData = null;
	
	public RNASeqFileCreate()
	{
		mColMustEqual = new TreeMap<>();
		mColMustEqual.put("FeatureType", "gene");
		mColMustEqual.put("CompositeDBVersion", "GRCh37");
		mLineData = new TreeSet<>();
	}
	
	protected TreeMap<String, Integer> processHeaders(String theHeaderLine)
	{
		TreeMap<String, Integer> headers = new TreeMap<>();
		String [] splitted = theHeaderLine.split("\t", -1);
		for(int index=0;index<splitted.length;index++)
		{
			headers.put(splitted[index], index);
		}
		return headers;
	}
	
	protected String makeLine(String theGeneSymbol, String theGeneId, String theIndex,
			String theChromosome, String theStartingCoord, String theEndingCoord, String theStrand)
	{
		return theGeneSymbol + "\t" + theGeneId + "\t" + theIndex + "\t" + theChromosome + "\t" + theStartingCoord + "\t" + theEndingCoord + "\t" + theStrand;
	}
	
	protected boolean columnsEqual(String [] theSplitted, TreeMap<String, Integer> theHeaders)
	{
		boolean equal = true;
		for(String srcCol : mColMustEqual.keySet())
		{
			String reqVal = mColMustEqual.get(srcCol);
			int index = theHeaders.get(srcCol);
			String foundVal = theSplitted[index];
			if (false == reqVal.equals(foundVal))
			{
				equal = false;
			}
		}
		return equal;
	}
	
	protected void parseAndMakeLine(String theGene, String theGeneLocus)
	{
		//Example values gene and geneLocus
		// ARSJ|79642	chr4:114900878-114821440:-
		// CT45A4|441520|1of2	chrX:134866214-134891518:+;chrX:134936735-134928698:-
		// CT45A4|441520|2of2	chrX:134866214-134891518:+;chrX:134936735-134928698:-
		String [] geneSplit = theGene.split("\\|", -1);
		String geneSymbol = geneSplit[0];
		String geneId = geneSplit[1];
		String index = (geneSplit.length<3)?"1of1":geneSplit[2];
		String [] locusVersions = theGeneLocus.split("\\;", -1);
		String [] indexSplit = index.split("of", -1);
		String locus = locusVersions[Integer.parseInt(indexSplit[0])-1];
		String [] locusSplit = locus.split("\\:", -1);
		String chromosome = locusSplit[0].replace("chr", "");
		String [] coordSplit = locusSplit[1].split("\\-",-1);
		String startingCoord = null;
		String endingCoord = null;
		if (Integer.parseInt(coordSplit[0])>Integer.parseInt(coordSplit[0]))
		{
			startingCoord = coordSplit[1];
			endingCoord = coordSplit[0];
		}
		else
		{
			startingCoord = coordSplit[0];
			endingCoord = coordSplit[1];
		}
		String strand = locusSplit[2];
		mLineData.add(makeLine(geneSymbol, geneId, index, chromosome, startingCoord, endingCoord, strand));
	}
	
	public void fileConvert(String theSourceZip, String theFile, String theDest) throws IOException, Exception
	{
		System.out.println("Convert theSourceZip = " + theSourceZip);
		System.out.println("Convert theFile = " + theFile);
		System.out.println("Convert dest = " + theDest);
		mLineData.clear();
		mLineHeaders = null;
		TreeMap<String, Integer> headers = null;
		// open file for reading
		int line = 0;
		try (ZipFile zf = new ZipFile(theSourceZip))
		{
			ZipEntry ze = zf.getEntry(theFile);
			try (InputStream is = zf.getInputStream(ze))
			{
				try(BufferedReader br = new BufferedReader(new InputStreamReader(is)))
				{
					headers = processHeaders(br.readLine());
					line = line + 1;
					// make headers
					mLineHeaders = makeLine("gene_symbol", "gene_id", "version_index", "chromosome", "location_start", "location_end", "strand");
					String strLine = br.readLine();
					while(null!=strLine)
					{
						//System.out.println("processing line " + line);
						// process lines
						String [] splitted = strLine.split("\t", -1);
						if(columnsEqual(splitted, headers))
						{
							String gene = splitted[headers.get("FeatureID")];
							String geneLocus = splitted[headers.get("GeneLocus")];
							parseAndMakeLine(gene, geneLocus);
						}
						strLine = br.readLine();
						line = line + 1;
					}
					System.out.println("processed " + line + " lines");
				}
			}
			// open file for writing
			try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(theDest), Charset.availableCharsets().get("ISO-8859-1")))
			{
				bw.write(mLineHeaders);
				bw.newLine();
				for(String myLine : mLineData)
				{
					bw.write(myLine);
					bw.newLine();
				}
			}
		}
	}
}
