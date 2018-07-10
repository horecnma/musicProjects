package com.horecnma.music.svoe.svoeDownloader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.horecnma.music.svoe.svoeDownloader.dto.BandLink;
import com.horecnma.music.svoe.svoeDownloader.dto.Track;

import static java.util.stream.Collectors.toList;

/**
 * @author Mikhail
 */
@Service
public class SvoeConnector {
    private static final Logger LOG = LoggerFactory.getLogger(SvoeConnector.class);

    private static final String USER_AGENT = "Mozilla/5.0";

    public List<BandLink> getBandLinksFromPage(int i)
            throws IOException {
        Document doc = Jsoup.connect("https://svoeradio.fm/air/artists/?paged=" + (i + 1)).get();
        return doc.select("li.artist-list-point a").stream()
                                      .map(it->new BandLink(it.attr("href")))
                                      .collect(toList());
    }

    public List<Track> getBandTracks(BandLink bandLink)
            throws IOException {
        List<Track> result = new ArrayList<>();
        Document bandDoc = Jsoup.connect(bandLink.getBandHref()).get();
        Elements tracks = bandDoc.select("div.track-list-wrapper div.track-list-artist-track");
        for (Element track : tracks) {
            String bandName = track.select("div.track-list-artist").text();
            String trackName = track.select("div.track-list-track").text();
            result.add(new Track(bandName, trackName));
        }
        return result;
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

    public String getFileNameUrl(String bandName, String trackName)
            throws Exception {

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
            LOG.error("\nSending 'POST' request to URL : " + "https://svoeradio.fm/api/get_file_for_track");
            LOG.error("Post parameters : " + requestParams);
            LOG.error("Response Code : " + responseCode);
        }

        try (InputStream inputStream = con.getInputStream()) {
            return IOUtils.toString(inputStream).trim();
        }
    }

    private String wrap(String s) {
        return s.replaceAll("=", "%3D").replaceAll("\\+", "%2B");
    }

    private HttpsURLConnection openConnection(String url, String methodType)
            throws IOException {
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod(methodType);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
        return con;
    }
}
