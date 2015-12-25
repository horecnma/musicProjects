package myPackage;

import com.google.common.base.Predicate;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: horec
 * Date: 19.10.13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class MainClass {
    static int a = 0;
    static int level = 5;
    static long curTime = System.currentTimeMillis();

    public static void main(String[] args) {
        File a = new File("D:\\music");
//        new Translitter().translitFile(a);

//        updateMp3Dates(a.listFiles());
//        updateFolderDates(a.listFiles());

        showFiles(a);

        System.out.println(a.exists());
    }

    public static void showFiles(File dir) {
        level--;
        soutDir(dir);
        List<File> mp3 = Arrays.asList(dir.listFiles(new IsFileFileFilter()));
        Collections.sort(mp3);
        for (File file : mp3) {
            System.out.println(a++ + "\t\t" + file.getName());
        }

        List<File> dirs = Arrays.asList(dir.listFiles(new IsDirFileFilter()));
        Collections.sort(dirs);
        for (File file : dirs) {
            showFiles(file);
        }
        level++;
    }

    private static void soutDir(File dir) {
        System.out.print("    ");
        for (int i = 0; i < 4; i++) {
            System.out.print(i<level? "*": " ");
        }
        System.out.println(" " + dir.getName());
    }

    private static void updateMp3Dates(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                updateMp3Dates(file.listFiles());
            }
        }

        reorderChilds(newArrayList(filter(Arrays.asList(files), new Predicate<File>() {
            @Override
            public boolean apply(java.io.File file) {
                return file.getName().toLowerCase().endsWith(".mp3");
            }
        })));
    }

    private static void updateFolderDates(File[] dirs) {
        for (File dir : dirs) {
            if (dir.isDirectory()) {
                updateMp3Dates(dir.listFiles());
            }
        }

        reorderChilds(newArrayList(filter(Arrays.asList(dirs), new Predicate<File>() {
            @Override
            public boolean apply(java.io.File file) {
                return file.isDirectory();
            }
        })));
    }


    private static void reorderChilds(List<File> list) {
        Collections.sort(list);
        for (File file : list) {
            file.setLastModified(curTime + a);
            a += 1000;
            System.out.println(file);
        }
    }

}
