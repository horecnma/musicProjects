package go;

import java.io.File;

/**
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class SoutByRememberedNodeFunction
        implements NodeFunction {

    private final SoutNodeFunction soutNodeFunction;
    private final MusicIndexProvider musicIndexProvider;

    public SoutByRememberedNodeFunction(MusicIndexProvider musicIndexProvider, int musicRootDepth) {
        this.musicIndexProvider = musicIndexProvider;
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
                int fileNumber = musicIndexProvider.getIndexOffFile(f);
                soutNodeFunction.soutFile(f, fileNumber);
            }
        }

    }
}
