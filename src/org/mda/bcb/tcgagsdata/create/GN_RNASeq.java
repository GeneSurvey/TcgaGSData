/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata.create;

import java.io.IOException;
import java.util.HashMap;
import org.mda.bcb.tcgagsdata.GSStringUtils;

/**
 *
 * @author tdcasasent
 */

/*
Uses GeneSymbol | gene number
?|729884	-1
?|8225	10
?|90288	2.
A1BG|1	4.
A1CF|29974
A2BP1|54715
A2LD1|87769
A2ML1|144568
*/
public class GN_RNASeq extends GeneNames_Mixin
{
	public GN_RNASeq(HashMap<String, Integer> theGeneEq, String theIdPath) throws IOException, Exception
	{
		super(theGeneEq, theIdPath, "RNASeq");
	}

	@Override
	public String convertGeneEq(String theGeneEq) throws Exception
	{
		String geneEq = mTic.convert_entreznum_TO_genesymbol(GSStringUtils.afterPipe(theGeneEq));
		if (null==geneEq)
		{
			geneEq = theGeneEq;
		}
		return geneEq;
	}

	@Override
	public void populateExtraMaps(String theGeneEq) throws Exception
	{
		// nothing to do here
	}

}
