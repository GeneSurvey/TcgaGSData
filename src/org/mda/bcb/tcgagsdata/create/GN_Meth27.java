/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author tdcasasent
 */

/*

*/
public class GN_Meth27 extends GeneNames_Mixin
{
	public GN_Meth27(HashMap<String, Integer> theGeneEq, String theIdPath) throws IOException, Exception
	{
		super(theGeneEq, theIdPath, "Meth27");
		// issue Meth27Map Sources: Use HudsonAlpha file for methylation 27 mapping.
		//mProbeToGeneSymbolMap = new TreeMap<>();
		//mProbeToGenomicLocationMap = new TreeMap<>();
		//mProbeToChromosomeMap = new TreeMap<>();
	}

	public static String [] breakdown(String theGeneEq) throws Exception
	{
		String [] results = { "", "", "", "" };
		// "Gene_Symbol", "Composite Element REF", "Chromosome", "Genomic_Coordinate"
		int indexGenomicCoord = theGeneEq.lastIndexOf("-");
		String strGenomicCoord = theGeneEq.substring(indexGenomicCoord+1);
		//TcgaGSData.printWithFlag("theGeneEq="+theGeneEq);
		//TcgaGSData.printWithFlag("strGenomicCoord="+strGenomicCoord);
		theGeneEq = theGeneEq.substring(0, indexGenomicCoord);
		//TcgaGSData.printWithFlag("theGeneEq="+theGeneEq);
		//
		int indexChromosome = theGeneEq.lastIndexOf("-");
		String strChromosome = theGeneEq.substring(indexChromosome+1);
		//TcgaGSData.printWithFlag("strChromosome="+strChromosome);
		theGeneEq = theGeneEq.substring(0, indexChromosome);
		//TcgaGSData.printWithFlag("theGeneEq="+theGeneEq);
		//
		int indexCompositeElementREF = theGeneEq.lastIndexOf("-");
		String strCompositeElementREF = theGeneEq.substring(indexCompositeElementREF+1);
		//TcgaGSData.printWithFlag("strCompositeElementREF="+strCompositeElementREF);
		if (indexCompositeElementREF<0)
		{
			theGeneEq = "";
		}
		else
		{
			theGeneEq = theGeneEq.substring(0, indexCompositeElementREF);
		}
		//TcgaGSData.printWithFlag("theGeneEq="+theGeneEq);
		//
		String geneSymbols = theGeneEq;
		//TcgaGSData.printWithFlag("geneSymbols="+geneSymbols);
		results[0] = geneSymbols;
		results[1] = strCompositeElementREF;
		results[2] = strChromosome;
		results[3] = strGenomicCoord;
		return results;
	}

	@Override
	public String convertGeneEq(String theGeneEq) throws Exception
	{
		String [] breakdown = { "", "", "", "" };
		try
		{
			breakdown = breakdown(theGeneEq);
		}
		catch(Exception exp)
		{
			System.err.println("GN_Meth27::convertGeneEq theGeneEq="+theGeneEq + " threw exception " + exp.getMessage());
			throw exp;
		}
		// "Gene_Symbol", "Composite Element REF", "Chromosome", "Genomic_Coordinate"
		return breakdown[1];
	}

	@Override
	public void populateExtraMaps(String theGeneEq) throws Exception
	{
		// "Gene_Symbol", "Composite Element REF", "Chromosome", "Genomic_Coordinate"
		String [] breakdown = { "", "", "", "" };
		try
		{
			breakdown = breakdown(theGeneEq);
		}
		catch(Exception exp)
		{
			System.err.println("GN_Meth27::populateExtraMaps theGeneEq="+theGeneEq + " threw exception " + exp.getMessage());
			throw exp;
		}
		String geneSymbol = breakdown[0];
		String probeId = breakdown[1];
		String chromosome = breakdown[2];
		String genomicCoord = breakdown[3];
		// issue Meth27Map Sources: Use HudsonAlpha file for methylation 27 mapping.
		//mProbeToGeneSymbolMap.put(probeId, geneSymbol);
		//mProbeToGenomicLocationMap.put(probeId, genomicCoord);
		//mProbeToChromosomeMap.put(probeId, chromosome);
	}

}
