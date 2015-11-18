/*
TCGAGeneReport Copyright 2014, 2015 University of Texas MD Anderson Cancer Center

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
Uses Gene Symbols
ZNF93
ZNF962P
ZNF965P
ZNF98
ZNF99
ZNFX1

And mirs
hsa-mir-107.1
hsa-mir-10a
hsa-mir-10a.1
hsa-mir-10b
hsa-mir-1178
hsa-mir-1179
hsa-mir-1180
*/
public class GN_SNP6 extends GeneNames_Mixin
{
	public GN_SNP6(HashMap<String, Integer> theGeneEq, String theIdPath) throws IOException, Exception
	{
		super(theGeneEq, theIdPath, "SNP6");
	}

	@Override
	public String convertGeneEq(String theGeneEq) throws Exception
	{
		return theGeneEq;
	}

	@Override
	public void populateExtraMaps(String theGeneEq) throws Exception
	{
		// nothing to do here
	}

}
