package go;

import java.io.File;

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
        System.out.println(number + "\t " + f.getName());
    }
}
