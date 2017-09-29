package com.horecnma.music.svoe;

import java.io.InputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.horecnma.music.svoe.svoeDownloader.FileSaver;
import com.horecnma.music.svoe.svoeDownloader.SingleTrackDownloader;

/**
 * @author mnikolaev
 */
public class SingleTreckDownloads
{
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
        FileSaver f = ctx.getBean(FileSaver.class);
        SingleTrackDownloader t = ctx.getBean(SingleTrackDownloader.class);

        String bandName = "Flёur";
        String trackName = "Взрывная Волна";
        String fileName = t.getFileNameUrl(bandName, trackName);
        InputStream inputStream = t.loadFile(fileName);
        f.saveFile(inputStream, bandName, trackName);
//        InputStream inputStream = svoeDownloader.loadFile(fileName);
//        inputStream.close();
    }

    @Configuration
    @ComponentScan(value = "com.horecnma.music.svoe.svoeDownloader")
    @PropertySource("classpath:download.properties")
    public static class Config {
    }
}
