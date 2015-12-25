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

    private final List<String> absFilePaths = new ArrayList<String>();
    private final List<String> absDirPaths = new ArrayList<String>();

    @Override
    public void handleNode(File f) {
        if (!f.isDirectory()) {
            boolean isMusic = f.getName().toLowerCase().endsWith(".mp3") ||
                    f.getName().toLowerCase().endsWith(".wma");
            if (isMusic) {
                absFilePaths.add(f.getAbsolutePath());
            }
        } else {
            absDirPaths.add(f.getAbsolutePath());
        }
    }

    public List<String> getAbsFilePaths() {
        return absFilePaths;
    }

    public List<String> getAbsDirPaths() {
        return absDirPaths;
    }
}
