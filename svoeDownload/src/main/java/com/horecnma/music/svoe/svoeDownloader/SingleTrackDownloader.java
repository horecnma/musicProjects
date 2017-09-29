package com.horecnma.music.svoe.svoeDownloader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mnikolaev
 */
@Service
public class SingleTrackDownloader {
    private static final Logger log = Logger.getLogger(SingleTrackDownloader.class);
    private static final String USER_AGENT = "Mozilla/5.0";

    @Autowired
    private FileSaver fileSaver;

    /**
     * загрузить и сохранить файл
     *
     * @param bandName  имя группы (как на сайте)
     * @param trackName имя трека (как на сайте)
     * @throws Exception ошибка
     */
    void doLoadFile(String bandName, String trackName) throws Exception {
        String fileNameUrl = getFileNameUrl(bandName, trackName);
        InputStream inputStream = loadFile(fileNameUrl);
        fileSaver.saveFile(inputStream, bandName, trackName.trim());
    }

    public InputStream loadFile(String fileNameUrl) throws IOException {
        String fullUrl = "https://svoeradio.fm" + fileNameUrl;
        HttpsURLConnection con = openConnection(fullUrl, "GET");

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("загрузка файла " + fullUrl + " respcode = " + responseCode);
        }
        return con.getInputStream();
    }

    public String getFileNameUrl(String bandName, String trackName) throws Exception {

        HttpsURLConnection con = openConnection("https://svoeradio.fm/api/get_file_for_track", "POST");

        String requestParams = "";
        requestParams += "art=" + wrap(Base64.getEncoder().encodeToString(bandName.getBytes("UTF-8")));
        requestParams += "&trk=" + wrap(Base64.getEncoder().encodeToString(trackName.getBytes("UTF-8")));
//        requestParams += "art=" + wrap(DatatypeConverter.printBase64Binary(bandName.getBytes("UTF-8")));
//        requestParams += "&trk=" + wrap(DatatypeConverter.printBase64Binary(trackName.getBytes("UTF-8")));
        requestParams += "&high=" + "yes";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(requestParams);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            log.error("\nSending 'POST' request to URL : " + "https://svoeradio.fm/api/get_file_for_track");
            log.error("Post parameters : " + requestParams);
            log.error("Response Code : " + responseCode);
        }

        try (InputStream inputStream = con.getInputStream()) {
            return IOUtils.toString(inputStream).trim();
        }
    }

    private String wrap(String s) {
        return s.replaceAll("=", "%3D").replaceAll("\\+", "%2B");
    }

    private HttpsURLConnection openConnection(String url, String methodType) throws IOException {
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod(methodType);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
        return con;
    }

    void delete(String bandName, String trackName) {
        fileSaver.delete(bandName, trackName);
    }
}
