package com.horecnma.music.svoe;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.horecnma.music.svoe.svoeDownloader.AllTracksDownloader;
import com.horecnma.music.svoe.svoeDownloader.tracks.TrackProvider;

/**
 * @author Mikhail
 */
public class Application {

    public static void main(String[] args)
            throws IOException, InterruptedException {
        BasicConfigurator.configure();
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);

        AllTracksDownloader allTracksDownloader = ctx.getBean(AllTracksDownloader.class);
        allTracksDownloader.downloadAllTracks();

        tearDown(ctx);
    }

    private static void tearDown(ApplicationContext ctx)
            throws InterruptedException {
        Map<String, ExecutorService> beansOfType = ctx.getBeansOfType(ExecutorService.class);
        for (ExecutorService exec: beansOfType.values()) {
            exec.shutdown();
            exec.awaitTermination(100, TimeUnit.MINUTES);
        }
        System.out.println();
    }

    @Configuration
    @ComponentScan("com.horecnma.music.svoe.svoeDownloader")
    @PropertySource("classpath:download.properties")
    public static class Config {
        @Bean("archives")
        String[] archives() {
            return new String[]{
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_01_14_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_02_26_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_06_29_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2015_12_01_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2016_03_03_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2016_07_13_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2017_01_13_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2017_03_20_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2017_08_29_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2018_02_14_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2018_07_10_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/fromSite/2019_02_19_update",
                    "/home/mnikolaev/yandex.Disk/я_radio/trash/",
                    "/home/mnikolaev/temp/"};
        }

        @Bean("destination")
        String destination() {
            return "/home/mnikolaev/temp/";
        }

        @Bean
        ExecutorService trackProviderExecutor() {
            return Executors.newSingleThreadExecutor();
        }

        @Bean
        ExecutorService trackDownloadExecutor() {
            return Executors.newFixedThreadPool(10);
        }

        @Bean
        AllTracksDownloader allTracksDownloader(TrackProvider premiereTrackProvider) {
            return new AllTracksDownloader(premiereTrackProvider);
        }
    }
}
