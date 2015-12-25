package go;

import java.io.File;

/**
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class SoutNodeFunction
        implements NodeFunction {
    int i = 0;

    @Override
    public void handleNode(File f) {
        if (!f.isDirectory()) {
            boolean isMusic = f.getName().toLowerCase().endsWith(".mp3") ||
                    f.getName().toLowerCase().endsWith(".wma");
            if (isMusic) {
                soutFile(f, ++i);
            }
        } else {
            soutDir(f);
        }
    }

    public void soutDir(File f) {
        int starCount = 6 - f.getAbsolutePath().split("\\\\").length;
        if(starCount == 3)
        {
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
