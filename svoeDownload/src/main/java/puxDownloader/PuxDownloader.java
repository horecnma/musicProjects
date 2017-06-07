/*********************************************************************
 * The Initial Developer of the content of this file is NOVARDIS.
 * All portions of the code written by NOVARDIS are property of
 * NOVARDIS. All Rights Reserved.
 *
 * NOVARDIS
 * Detskaya st. 5A, 199106 
 * Saint Petersburg, Russian Federation 
 * Tel: +7 (812) 331 01 71
 * Fax: +7 (812) 331 01 70
 * www.novardis.com
 *
 * (c) 2016 by NOVARDIS
 *********************************************************************/

package puxDownloader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author mihnik
 */
public class PuxDownloader {
    private static final Logger LOG = Logger.getLogger(PuxDownloader.class);

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final TagParser tagParser = new TagParser();
    public static final int DURATION_DIFFERENCE_SEC = 10;

    /**
     * @param args аргументы
     */
    public static void main(String[] args)
            throws IOException {
        BasicConfigurator.configure();
        //		for (File file : new File("C:\\RELAXATION\\radio\\fromSite\\2015_01_14_update\\").listFiles())
        //		{
        //        new PuxDownloader().download(file);
        //		}
        new PuxDownloader().download(new File("C:\\RELAXATION\\radio\\fromSite\\НедРа - Люди.mp3"));
    }

    void download(File exampleFile)
            throws IOException {
        List<PuxSong> songs = getSongList(exampleFile)
                .filter(song -> !isLiveRecord(song, exampleFile.getName()))
                .filter(song -> !isCoverRecord(song, exampleFile.getName()))
                .filter(song -> song.getRatio() > 180)
                .filter(song -> Math.abs(song.getDuration() - getDuration(exampleFile)) < DURATION_DIFFERENCE_SEC)
                .collect(Collectors.toList());
        if (songs.isEmpty()) {
            System.err.println("не найден файл " + exampleFile.getName());
        }
        for (PuxSong song : songs) {
            System.out.println("song = " + song);
        }
        System.out.println();
        //		downloadSongs(songs);
    }

    private Stream<PuxSong> getSongList(File exampleFile)
            throws IOException {
        return loadFindPage(FilenameUtils.getBaseName(exampleFile.getPath()))
                .select("#list_of_songs")
                .select(".song_div").parallelStream()
                .map(this::toPuxSong);
    }

    private int getDuration(File exampleFile) {
        int duration = tagParser.getDuration(exampleFile);
        System.out.println("duration = " + duration);
        return duration;
    }

    private void downloadSongs(List<PuxSong> songs)
            throws IOException {
        for (int i = 0; i < songs.size(); i++) {
            PuxSong song = songs.get(i);
            downloadSong(song, i);
        }
    }

    private void downloadSong(PuxSong puxSong, int i)
            throws IOException {
        String fileSize = FileUtils.byteCountToDisplaySize(getFileSize(puxSong.getDownloadHref()));
        LOG.debug("start download " + fileSize + ":    " + puxSong.getFullTitle());
        URL website = puxSong.getDownloadHref();
        FileUtils.copyURLToFile(website, new File("C:\\temp\\" + puxSong.getFullTitle() + "_" + i + ".mp3"));
        LOG.debug("  end download " + puxSong.getFullTitle());
    }

    private PuxSong toPuxSong(Element song_div) {
        URL url = extractUrl(song_div);
        int time = extractTime(song_div);
        String title = extractTitle(song_div);
        int byteCount = getFileSize(url);
        return new PuxSong(url, time, title, byteCount);
    }

    private int getFileSize(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            return -1;
        } finally {
            conn.disconnect();
        }
    }

    private int extractTime(Element song_div) {
        String time = song_div.select("span").text().trim(); // 5:50
        String[] split = time.split(":");
        return Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
    }

    private String extractTitle(Element song_div) {
        return song_div.select("b").text();
    }

    private URL extractUrl(Element song_div) {
        String href = song_div.select("a").stream()
                              .filter(a -> a.text().contains("Скачать"))
                              .findFirst().get().attr("href");
        return createUrl(href);
    }

    private URL createUrl(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isCoverRecord(PuxSong song, String songName) {
        return song.getFullTitle().toLowerCase().contains("концерт в") && !songName.toLowerCase().contains("концерт в");

    }

    private boolean isLiveRecord(PuxSong song, String songName) {
        return song.getFullTitle().toLowerCase().contains("live") && !songName.toLowerCase().contains("live");
    }

    private Document loadFindPage(String exampleFileName)
            throws IOException {
        HttpURLConnection con = openConnection("http://www.myz.su/", "POST");
        addParameter(con, exampleFileName.replace("-", " ").replace("  ", " "));
        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("загрузка файла  respcode = " + responseCode);
        }
        return Jsoup.parse(IOUtils.toString(con.getInputStream()));
    }

    private void addParameter(HttpURLConnection con, String s)
            throws IOException {
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes("name=" + URLEncoder.encode(s, "UTF-8"));
        wr.flush();
        wr.close();
    }

    public HttpURLConnection openConnection(String url, String methodType)
            throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(methodType);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Referer", "http://www.myz.su/");
        con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
        return con;
    }
}
