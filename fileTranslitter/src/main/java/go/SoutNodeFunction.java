package go;

import java.io.File;

import org.apache.commons.lang.StringUtils;

/**
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class SoutNodeFunction {

    private int musicRootDepth;

    /**
     * @param musicRootDepth linux - 3, windows = 1
     */
    public SoutNodeFunction(int musicRootDepth) {
        this.musicRootDepth = musicRootDepth;
    }

    public void soutDir(File f) {
        int starCount = musicRootDepth + 6 - f.getAbsolutePath().split(File.separator).length;
        if (starCount == 3) {
            System.out.println("-------------------------------------------");
        }
        System.out.print("   ");
        for (int q = 0; q < 6; q++) {
            if (q < starCount) {
                System.out.print("*");
            } else {
                System.out.print(" ");
            }
        }
        System.out.println(f.getName());
    }

    public void soutFile(File f, int number) {
        soutDirNameForYaRadio(f);     // hack for big directory
        System.out.println(StringUtils.rightPad(""+number,4) + " " + f.getName());
    }

    String currentBand = "";
    
    private void soutDirNameForYaRadio(File f) {
        if (f.getAbsolutePath().contains("я_radio")) {
            String bandName = getBandName(f);
            if (!isEqualsBandName(currentBand, bandName)) {
                currentBand = bandName;
                System.out.println("   **   " + currentBand);
            }
        }
    }

    private boolean isEqualsBandName(String bandName, String currentBand) {
        String s1 = currentBand.toLowerCase().replaceAll(" ", "");
        String s2 = bandName.toLowerCase().replaceAll(" ", "");
        return s1.equals(s2);
    }

    private String getBandName(File f) {
        return f.getName().split(" -")[0];
    }
}
