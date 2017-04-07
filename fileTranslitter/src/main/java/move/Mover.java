package move;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * @author mnikolaev
 */
public final class Mover {
    private Mover() {
    }

    public static void main(String[] args) throws IOException {
//        groupFiles("f:\\music\\я_radio\\", 100);
        groupFilesByArtist("/media/mnikolaev/FLASH_8_GB/music/я_radio/");
    }

    /**
     * перемещает файлы в папки по артистам
     * @param path путь
     * @throws IOException
     */
    private static void groupFilesByArtist(String path) throws IOException {
        File dir = new File(path);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                String artistName = getArtistName(file);
                File artistDir = createDir(dir, artistName);
                File destFile = new File(artistDir, file.getName());
                System.out.println("moveTo " + destFile);
                FileUtils.moveFile(file, destFile);
            }
        }
    }

    private static File createDir(File parent, String artistName) {
        File artistDir = new File(FilenameUtils.concat(parent.getPath(), artistName));
        if (!artistDir.exists()) {
            artistDir.mkdir();
        }
        return artistDir;
    }

    private static String getArtistName(File file) {
        return Lists.newArrayList(Splitter.on(" - ").limit(2).split(file.getName())).get(0);
    }


    private static void groupFiles(String path, int filesInDir) throws IOException {
        File dir = new File(path);
        int i=0;
        File numberDir =null;
        for (File track : dir.listFiles()) {
            if(i% filesInDir ==0){
                numberDir = new File(dir, i/ filesInDir +"");
                if(!numberDir.exists()){
                    numberDir.mkdir();
                }
            }
            FileUtils.moveFile(track, new File(numberDir, track.getName()));
            System.out.println(i +"   " +new File(numberDir, track.getName()) );
            i++;
        }
    }
}
