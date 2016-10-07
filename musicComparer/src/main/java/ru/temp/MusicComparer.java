/*********************************************************************
 * The Initial Developer of the content of this file is NOVARDIS.
 * All portions of the code written by NOVARDIS are property of
 * NOVARDIS. All Rights Reserved.
 * <p/>
 * NOVARDIS
 * Detskaya st. 5A, 199106
 * Saint Petersburg, Russian Federation
 * Tel: +7 (812) 331 01 71
 * Fax: +7 (812) 331 01 70
 * www.novardis.com
 * <p/>
 * (c) 2016 by NOVARDIS
 *********************************************************************/

package ru.temp;

import java.io.File;

/**
 * @author mihnik
 */
public class MusicComparer
{
  public float compare(File file1, File file2)
  {
    checkFileExists(file1);
    checkFileExists(file2);

    return 0;
  }

  private void checkFileExists(File file1)
  {
    if (!file1.exists())
    {
      throw new IllegalArgumentException(file1.getPath());
    }
  }
}
