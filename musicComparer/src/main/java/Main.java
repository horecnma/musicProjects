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

import java.io.File;

import ru.temp.MusicComparer;

/**
 * @author mihnik
 */
public class Main
{
  public static void main(String[] args)
  {
    String fileName1 = "";
    String fileName2 = "";
    float compareValue = new MusicComparer().compare(new File(fileName1), new File(fileName2));
  }
}
