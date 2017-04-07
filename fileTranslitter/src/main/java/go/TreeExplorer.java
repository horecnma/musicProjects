package go;

import java.io.File;

/**
 * @author Mikhail
 */
public interface TreeExplorer {

    /**
     * выполнить обход дерева файлов и для каждой папки и файла применить NodeFunction
     * @param startDir стартовая директория
     */
    void explore(File startDir);
}
