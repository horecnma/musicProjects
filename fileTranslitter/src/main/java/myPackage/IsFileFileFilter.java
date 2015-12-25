package myPackage;

import java.io.File;
import java.io.FileFilter;

/**
 * User: mnikolaev<br>
 * Date: 05.11.13<br>
 */
public class IsFileFileFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return !pathname.isDirectory();
    }
}
