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
cg00000165
cg00000236
cg00000289
cg00000292
cg00000321
cg00000363

ch.1.101940785F
ch.1.1021960F
ch.X.97133160R
ch.X.97651759F
ch.X.97737721F
ch.X.98007042R
rs10033147
rs1019916
rs1040870
rs10457834
*/

public class GN_Meth450 extends GeneNames_Mixin
{
	public GN_Meth450(HashMap<String, Integer> theGeneEq, String theIdPath) throws IOException, Exception
	{
		super(theGeneEq, theIdPath, "Meth450");
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
