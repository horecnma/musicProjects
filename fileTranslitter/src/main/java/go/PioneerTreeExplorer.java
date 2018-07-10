package go;

import java.io.File;

import myPackage.IsDirFileFilter;
import myPackage.IsFileFileFilter;

/**
 * обойти дерево файлов в порядке проигрывания записей в плеере Pioneer
 * <p>
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class PioneerTreeExplorer implements TreeExplorer {
    private final NodeFunction worker;

    public PioneerTreeExplorer(NodeFunction worker) {
        this.worker = worker;
    }

    public static void main(String[] args) {
        // df -aTh
        // sudo fatsort -D MUSIC /dev/sdb1
        File startDir = new File("/media/mnikolaev/FLASH_8_GB/music/");

        RememberFilesFunction remember = new RememberFilesFunction();

        FilesBeforeDirsTreeExplorer rememberExplorer = new FilesBeforeDirsTreeExplorer(remember);
        rememberExplorer.explore(startDir);

        FilesBeforeDirsTreeExplorer soutExplorer = new FilesBeforeDirsTreeExplorer(new NodePrinter(new MusicIndexProvider(remember), new SoutNodeFunction(3)));
        soutExplorer.explore(startDir);
    }

    @Override
    public void explore(File startDir) {
        for (int i = 0; i < 7; i++) {
            explore(startDir, 0, i);
        }
    }

    private void explore(File startDir, int current, int maxTotal) {
        if (current == maxTotal) {
            File[] files = startDir.listFiles(new IsFileFileFilter());
            if (startDir.isDirectory() && files != null && files.length > 0) {
                worker.handleNode(startDir);
                for (File file : files) {
                    worker.handleNode(file);
                }
            }
        }
        if (current < maxTotal) {
            ++current;
            for (File file : startDir.listFiles(new IsDirFileFilter())) {
                explore(file, current, maxTotal);
            }
        }
    }
}
