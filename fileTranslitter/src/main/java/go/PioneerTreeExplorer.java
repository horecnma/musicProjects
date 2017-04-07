package go;

import java.io.File;

import myPackage.IsDirFileFilter;
import myPackage.IsFileFileFilter;

/**
 * обойти дерево файлов в порядке проигрывания записей в плеере Pioneer
 * 
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class PioneerTreeExplorer implements TreeExplorer {
    private final NodeFunction worker;

    public PioneerTreeExplorer(NodeFunction worker) {
        this.worker = worker;
    }

    public static void main(String[] args) {
        // sudo fatsort -D MUSIC /dev/sdb1
        File startDir = new File("/media/mnikolaev/FLASH_8_GB/music/");

        RememberNodeFunction remember = new RememberNodeFunction();
        FilesBeforeDirsTreeExplorer pioneerTreeExplorer = new FilesBeforeDirsTreeExplorer(remember);
        pioneerTreeExplorer.explore(startDir);

        SoutByRememberedNodeFunction nodeFunction = new SoutByRememberedNodeFunction(remember, 3);

        //        FilesBeforeDirsTreeExplorer byTreeExplorer = new FilesBeforeDirsTreeExplorer(nodeFunction);
        //        byTreeExplorer.explore(startDir);

        FilesBeforeDirsTreeExplorer pionerSour = new FilesBeforeDirsTreeExplorer(nodeFunction);
        pionerSour.explore(startDir);
    }

    @Override
    public void explore(File startDir) {
        for (int i=0; i<7; i++)
        {
            explore(startDir, 0, i);
        }
    }

    private void explore(File startDir, int current, int maxTotal) {
        if (current == maxTotal ) {
            File[] files = startDir.listFiles(new IsFileFileFilter());
            if (startDir.isDirectory() && files!=null && files.length>0) {
                worker.handleNode(startDir);
                for (File file : files) {
                    worker.handleNode(file);
                }
            }
        }
        if(current < maxTotal)
        {
            ++current;
            for (File file : startDir.listFiles(new IsDirFileFilter())) {
                explore(file, current, maxTotal);
            }
        }
    }
}
