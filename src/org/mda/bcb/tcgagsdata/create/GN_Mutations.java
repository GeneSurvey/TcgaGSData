/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import org.mda.bcb.tcgagsdata.GSStringUtils;

/**
 *
 * @author tdcasasent
 */

public class GN_Mutations extends GeneNames_Mixin
{
	public GN_Mutations(HashMap<String, Integer> theGeneEq, String theIdPath) throws IOException, Exception
	{
		super(theGeneEq, theIdPath, "Mutations");
	}

	@Override
	public String convertGeneEq(String theGeneEq) throws Exception
	{
		/*String geneEq = theGeneEq;
		String entrz = GSStringUtils.afterPipe(theGeneEq);
		if ("0".equals(entrz))
		{
			geneEq = GSStringUtils.beforePipe(theGeneEq);
		}
		else
		{
			geneEq = mTic.convert_entreznum_TO_genesymbol(entrz);
			if (null==geneEq)
			{
				geneEq = theGeneEq;
			}
		}
		return geneEq;*/
		return theGeneEq;
	}

	@Override
	public void populateExtraMaps(String theGeneEq) throws Exception
	{
		// nothing to do here
	}
	
	@Override
	public TreeMap<File, String> filterFiles(TreeMap<File, String> theFiles)
	{
		TreeMap<File, String> mutationFiles = new TreeMap<>();
		//
		TreeSet<String> disList = new TreeSet<>();
		disList.addAll(theFiles.values());
		for (String disease : disList)
		{
			// get files for disease
			TreeSet<File> disFiles = new TreeSet<>();
			for(Entry<File, String> entry : theFiles.entrySet())
			{
				if (entry.getValue().equals(disease))
				{
					disFiles.add(entry.getKey());
				}
			}
			// if only one, use it
			if (1==disFiles.size())
			{
				mutationFiles.put(disFiles.first(), disease);
			}
			else 
			{
				File myFile = GSStringUtils.getFileWith("illuminaga_dnaseq_curated", disFiles);
				if (null==myFile)
				{
					myFile = GSStringUtils.getFileWith("illuminaga_dnaseq_broad", disFiles);
					if (null==myFile)
					{
						myFile = GSStringUtils.getFileWith("illuminaga_dnaseq_genome", disFiles);
						if (null==myFile)
						{
							myFile = GSStringUtils.getFileWith("illuminaga_dnaseq_hgsc", disFiles);
							if (null==myFile)
							{
								myFile = GSStringUtils.getFileWith("mixed_dnaseq_curated", disFiles);
								if (null==myFile)
								{
									myFile = GSStringUtils.getFileWith("solid_dnaseq_hgsc", disFiles);
									if (null==myFile)
									{
										myFile = GSStringUtils.getFileWith("mixed_dnaseq_hgsc", disFiles);
									}
								}
							}
						}
					}
				}
				mutationFiles.put(myFile, disease);
			}
		}
		//
		return mutationFiles;
	}

}
