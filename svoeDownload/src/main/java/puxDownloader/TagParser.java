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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author mihnik
 */
@SuppressWarnings("CallToPrintStackTrace")
public class TagParser {
    /**
     * @param args
     */
    public static void main(String[] args)
            throws IOException, TikaException, SAXException {
        String fileLocation = "C:\\temp\\Например - День Черной Звезды_1.mp3";
        String url = "http://myz.sngtorrent.ru/mp3/cs5011/u105010/audios/3ac5f02773dc/myz_Naprimer-Den_CHernoi_Zvezdy.mp3?hash=zu3MmfFyw-nuqdKKTmY9OQ&ip=213.172.15.186";
//		try(InputStream input = new URL(url).openStream())
        parse(new File(fileLocation));
    }

    private static Metadata parse(File fileLocation)
            throws TikaException, SAXException, IOException {
        try (InputStream input = new FileInputStream(fileLocation)) {
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);

            return metadata;
        }
    }

    private static void print(File file)
            throws IOException, TikaException, SAXException {
        Metadata metadata = parse(file);
        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            System.out.println(name + ": " + metadata.get(name));
        }

        // Retrieve the necessary info from metadata
        // Names - title, xmpDM:artist etc. - mentioned below may differ based
        System.out.println("----------------------------------------------");
        System.out.println("Title: " + metadata.get("title"));
        System.out.println("Artists: " + metadata.get("xmpDM:artist"));
        System.out.println("Composer : " + metadata.get("xmpDM:composer"));
        System.out.println("Genre : " + metadata.get("xmpDM:genre"));
        System.out.println("Album : " + metadata.get("xmpDM:album"));

    }

    public int getDuration(File exampleFile) {
        try {
            String s = parse(exampleFile).get("xmpDM:duration");
            return Float.valueOf(s).intValue() / 1000;
        } catch (TikaException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
