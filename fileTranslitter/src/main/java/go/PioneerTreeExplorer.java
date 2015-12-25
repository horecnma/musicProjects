package go;

import java.io.File;

import myPackage.IsDirFileFilter;
import myPackage.IsFileFileFilter;

/**
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class PioneerTreeExplorer {
    private final NodeFunction worker;

    public PioneerTreeExplorer(NodeFunction worker) {
        this.worker = worker;
    }

    public static void main(String[] args) {
        File startDir = new File("g:\\music\\");

        RememberNodeFunction remember = new RememberNodeFunction();
        PioneerTreeExplorer pioneerTreeExplorer = new PioneerTreeExplorer(remember);
        pioneerTreeExplorer.explore(startDir);

        SoutByRememberedNodeFunction nodeFunction = new SoutByRememberedNodeFunction(remember);

        //        FilesBeforeDirsTreeExplorer byTreeExplorer = new FilesBeforeDirsTreeExplorer(nodeFunction);
        //        byTreeExplorer.explore(startDir);

        PioneerTreeExplorer pionerSour = new PioneerTreeExplorer(nodeFunction);
        pionerSour.explore(startDir);
    }

    /**
     * обойти дерево файлов в порядке проигрывания записей в плеере и для каждой папки и файла применить NodeFunction
     * @param startDir стартовая директория
     */
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
