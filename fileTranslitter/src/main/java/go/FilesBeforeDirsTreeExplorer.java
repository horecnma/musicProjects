package go;

import myPackage.IsDirFileFilter;
import myPackage.IsFileFileFilter;

import java.io.File;

/**
 * User: mnikolaev<br>
 * Date: 06.11.13<br>
 */
public class FilesBeforeDirsTreeExplorer {

    private final NodeFunction worker;

    public FilesBeforeDirsTreeExplorer(NodeFunction worker) {
        this.worker = worker;
    }

    public void explore(File startDir) {
        worker.handleNode(startDir);

        if (startDir.isDirectory()) {
            for (File file : startDir.listFiles(new IsFileFileFilter())) {
                worker.handleNode(file);
            }
            for (File file : startDir.listFiles(new IsDirFileFilter())) {
                explore(file);
            }
        }
    }
}
