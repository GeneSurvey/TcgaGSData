/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.retrieve;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.mda.bcb.tcgagsdata.TcgaGSData;

/**
 *
 * @author tdcasasent
 */
public class GeneSynonyms
{

	static protected String M_PATH = null;
	static protected TreeMap<String, TreeSet<String>> M_SYNONYMS = null;

	public GeneSynonyms(String thePath)
	{
		if (false==thePath.equals(M_PATH))
		{
			M_PATH = thePath;
			M_SYNONYMS = null;
		}
	}

	public String [] getList_GeneSymbol_Synonym(String theId) throws IOException
	{
		TreeSet<String> synonymList = new TreeSet<>();
		try
		{
			TcgaGSData.printVersion();
			if (null==M_SYNONYMS)
			{
				loadSynonyms(new File(M_PATH, "geneSynonyms.tsv").getAbsolutePath());
			}
			theId = theId.toUpperCase();
			for(Map.Entry<String, TreeSet<String>> entry : M_SYNONYMS.entrySet())
			{
				if (entry.getValue().contains(theId))
				{
					synonymList.add(entry.getKey());
				}
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace(System.err);
			M_SYNONYMS = null;
		}
		return synonymList.toArray(new String[0]);
	}
	
	protected void loadSynonyms(String thePreppedFile) throws FileNotFoundException, IOException
	{
		M_SYNONYMS = new TreeMap<>();
		try(BufferedReader br = new BufferedReader(new FileReader(thePreppedFile)))
		{
			String inLine = br.readLine();
			while(null!=inLine)
			{
				inLine = inLine.toUpperCase();
				String [] tabSplit = inLine.split("\t", -1);
				String symbol = tabSplit[0];
				TreeSet<String> treeset = new TreeSet<>();
				treeset.addAll(Arrays.asList(tabSplit));
				M_SYNONYMS.put(symbol, treeset);
				inLine = br.readLine();
			}
		}
	}
}
