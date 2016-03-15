/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import edu.mda.bioinfo.ids.TcgaIdConverter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author tdcasasent
 */
public abstract class GeneNames_Mixin
{
	public TreeMap<String, Integer> mGeneEqTreeMap = null;
	public String mDataName = null;

	protected HashMap<String, Integer> mHashMap = null;
	protected TcgaIdConverter mTic = null;

	public TreeMap<String, String> mProbeToGeneSymbolMap = null;
	public TreeMap<String, String> mProbeToGenomicLocationMap = null;
	public TreeMap<String, String> mProbeToChromosomeMap = null;

	public GeneNames_Mixin(HashMap<String, Integer> theHashMap, String theIdPath, String theDataName) throws IOException, Exception
	{
		mDataName = theDataName;
		mHashMap = theHashMap;
		mGeneEqTreeMap = new TreeMap<>();
		mTic = new TcgaIdConverter(theIdPath);
		mTic.loadFiles();
		mProbeToGeneSymbolMap = null;
		mProbeToGenomicLocationMap = null;
		mProbeToChromosomeMap = null;
	}

	public void processList() throws Exception
	{
		for(String geneEq : mHashMap.keySet())
		{
			Integer indexVal = mHashMap.get(geneEq);
			Integer oldVal = null;
			try
			{
				oldVal = mGeneEqTreeMap.put(convertGeneEq(geneEq), indexVal);
			}
			catch(Exception exp)
			{
				System.err.println("GeneNames_Mixin::processList geneEq="+geneEq + " threw exception " + exp.getMessage());
				System.err.println("GeneNames_Mixin::processList oldVal="+oldVal);
				throw exp;
			}
			if (null!=oldVal)
			{
				System.err.println("Duplicate key found  convertGeneEq(geneEq)="+convertGeneEq(geneEq));
				System.err.println("Duplicate key found  geneEq="+geneEq);
				System.err.println("Duplicate key found  oldVal="+oldVal);
				System.err.println("Duplicate key found  indexVal="+indexVal);
				throw new Exception("Duplicate key found " + geneEq);
			}
			populateExtraMaps(geneEq);
		}
	}
	
	public TreeMap<File, String> filterFiles(TreeMap<File, String> theFiles)
	{
		return theFiles;
	}

	abstract public String convertGeneEq(String theGeneEq) throws Exception;
	abstract public void populateExtraMaps(String theGeneEq) throws Exception;

}
