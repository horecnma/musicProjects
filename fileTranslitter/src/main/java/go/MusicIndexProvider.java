package go;

import java.io.File;

/**
 * @author Mikhail
 */
public class MusicIndexProvider {

    private final RememberNodeFunction provider;

    public MusicIndexProvider(RememberNodeFunction provider) {
        this.provider = provider;
    }

    public int getIndexOffFile(File f) {
        return provider.getOrderedMp3Files().indexOf(f) + 1;
    }
}
