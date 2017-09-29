/*********************************************************************
 * The Initial Developer of the content of this file is NOVARDIS.
 * All portions of the code written by NOVARDIS are property of
 * NOVARDIS. All Rights Reserved.
 *
 * NOVARDIS
 * Detskaya st. 5A, 199106 
 * Saint Petersburg, Russian Federation 
 * Tel: +7 (812) 331 01 71
 * Fax: +7 (812) 331 01 70
 * www.novardis.com
 *
 * (c) 2016 by NOVARDIS
 *********************************************************************/

package com.horecnma.music.puxDownloader;

import java.net.URL;

/**
 * @author mihnik
 */
public class PuxSong
{
	private final URL downloadHref;
	private final int duration;
	private final String fullTitle;
	private final int byteCount;
	private int ratio = 0;

	public PuxSong(URL downloadHref, int duration, String fullTitle, int byteCount)
	{
		this.downloadHref = downloadHref;
		this.duration = duration;
		this.fullTitle = fullTitle;
		this.byteCount = byteCount;
		if (duration != 0)
		{
			this.ratio = byteCount/1000/duration*8;
		}
	}

	public int getRatio()
	{
		return ratio;
	}

	public int getByteCount()
	{
		return byteCount;
	}

	public URL getDownloadHref()
	{
		return downloadHref;
	}

	public int getDuration()
	{
		return duration;
	}

	public String getFullTitle()
	{
		return fullTitle;
	}

	@Override
	public String toString()
	{
		return "PuxSong{" +
//						"downloadHref=" + downloadHref +
						", duration=" + duration +
						", fullTitle='" + fullTitle + '\'' +
						", byteCount=" + byteCount +
						", ratio=" + ratio +
						'}';
	}
}
