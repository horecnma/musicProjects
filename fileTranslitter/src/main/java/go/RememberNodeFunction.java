package go;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class RememberNodeFunction
        implements NodeFunction {

    private final List<File> absFilePaths = new ArrayList<File>();
    private final List<File> absDirPaths = new ArrayList<File>();

    @Override
    public void handleNode(File f) {
        if (!f.isDirectory()) {
            boolean isMusic = f.getName().toLowerCase().endsWith(".mp3") ||
                    f.getName().toLowerCase().endsWith(".wma");
            if (isMusic) {
                absFilePaths.add(f);
            }
        } else {
            absDirPaths.add(f);
        }
    }

    public List<File> getOrderedMp3Files() {
        return absFilePaths;
    }
}
