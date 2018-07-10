package com.horecnma.music.svoe.svoeDownloader;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mnikolaev
 */
@Service
public class SingleTrackDownloader {

    @Autowired
    private FileSaver fileSaver;
    @Autowired
    public SvoeConnector svoeConnector;

    /**
     * загрузить и сохранить файл
     *
     * @param bandName  имя группы (как на сайте)
     * @param trackName имя трека (как на сайте)
     * @throws Exception ошибка
     */
    void doLoadFile(String bandName, String trackName) throws Exception {
        String fileNameUrl = svoeConnector.getFileNameUrl(bandName, trackName);
        InputStream inputStream = svoeConnector.loadFile(fileNameUrl);
        fileSaver.saveFile(inputStream, bandName, trackName.trim());
    }

    void delete(String bandName, String trackName) {
        fileSaver.delete(bandName, trackName);
    }
}
