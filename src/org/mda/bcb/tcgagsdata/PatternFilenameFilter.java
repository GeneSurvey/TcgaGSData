/*
TcgaGSData Copyright 2014, 2015, 2016 University of Texas MD Anderson Cancer Center

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mda.bcb.tcgagsdata;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tdcasasent
 */
public class PatternFilenameFilter implements FilenameFilter
{
	public Pattern mPattern = null;

	public PatternFilenameFilter(String thePattern)
	{
		mPattern = Pattern.compile(thePattern);
	}

	@Override
	public boolean accept(File dir, String name)
	{
		Matcher m = mPattern.matcher(name);
		return m.matches();
	}

}
