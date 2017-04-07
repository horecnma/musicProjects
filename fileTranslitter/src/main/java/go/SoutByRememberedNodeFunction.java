package go;

import java.io.File;
import java.util.List;

/**
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class SoutByRememberedNodeFunction
        implements NodeFunction {

    private final List<String> absPath;
    private final SoutNodeFunction soutNodeFunction;

    public SoutByRememberedNodeFunction(RememberNodeFunction absPath, int musicRootDepth) {
        this.absPath = absPath.getAbsFilePaths();
        soutNodeFunction = new SoutNodeFunction(musicRootDepth);
    }

    @Override
    public void handleNode(File f) {
        if (f.isDirectory()) {
            soutNodeFunction.soutDir(f);
        } else {
            boolean isMusic = f.getName().toLowerCase().endsWith(".mp3") ||
                    f.getName().toLowerCase().endsWith(".wma");
            if (isMusic) {
                int fileNumber = absPath.indexOf(f.getAbsolutePath()) + 1;
                soutNodeFunction.soutFile(f, fileNumber);
            }
        }

    }
}
