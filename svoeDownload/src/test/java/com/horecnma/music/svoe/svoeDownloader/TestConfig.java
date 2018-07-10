package com.horecnma.music.svoe.svoeDownloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.horecnma.music.svoe.svoeDownloader.tracks.TrackProvider;

/**
 * @author Mikhail
 */
@Configuration
@ComponentScan("com.horecnma.music.svoe.svoeDownloader")
@PropertySource("classpath:download.properties")
public class TestConfig {
    public static class Config {
        @Bean("archives")
        String[] archives() {
            return new String[]{};
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
        AllTracksDownloader allTracksDownloader(TrackProvider premiereTrackProvider) {
          return new AllTracksDownloader(premiereTrackProvider);
        }
    }
}
