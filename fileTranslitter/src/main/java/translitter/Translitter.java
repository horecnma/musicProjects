package translitter;

import java.io.File;

public class Translitter {
    TranslitHelper h = new TranslitHelper();

    void translitFile(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                translitFile(f);
            }
        }
        renameFile(file);
    }

    void renameFile(File file) {
        String eng = h.transliterate(file.getPath());
        boolean b = file.renameTo(new File(eng));
        System.out.println("file.getPath() = " + eng);

//        File file1 = new File(file.getPath());
//            file.renameTo(file1);
    }
}
