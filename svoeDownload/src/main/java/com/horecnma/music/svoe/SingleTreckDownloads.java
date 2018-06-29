package com.horecnma.music.svoe;

import java.io.InputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
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
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SingleTrackDownloadConfig.class);
        FileSaver f = ctx.getBean(FileSaver.class);
        SingleTrackDownloader singleTrackDownloader = ctx.getBean(SingleTrackDownloader.class);

        String bandName = "Харакири";
        String trackName = "Космос Станет Ближе";
        String fileName = singleTrackDownloader.getFileNameUrl(bandName, trackName);
        InputStream inputStream = singleTrackDownloader.loadFile(fileName);
        f.saveFile(inputStream, bandName, trackName);
//        InputStream inputStream = svoeDownloader.loadFile(fileName);
//        inputStream.close();
    }

    @Configuration
    @ComponentScan(value = "com.horecnma.music.svoe.svoeDownloader")
    @PropertySource("classpath:download.properties")
    public static class SingleTrackDownloadConfig {
        @Bean("archives")
        String[] archives() {
            return new String[]{};
        }
        @Bean("destination")
        String destination() {
            return "/home/mnikolaev/temp/";
        }
    }
}
