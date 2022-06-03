package dev._2lstudios.advancedauth.services;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

import com.maxmind.geoip.LookupService;

public class GeoIPService {
    static String FILE_URL = "https://github.com/P3TERX/GeoLite.mmdb/raw/download/GeoLite2-Country.mmdb";
    static String FILE_NAME = "GeoLite2-Country.mmdb";

    static LookupService service;

    public static void downloadFile(final File file) throws Exception {
        URL url = new URL(FILE_URL);
        try (InputStream in = url.openStream()) {
            Files.copy(in, file.toPath());
        }
    }
    
    public static void start(final File workingDirectory) throws Exception {
        final File dbFile = new File(workingDirectory, FILE_NAME);

        if (!dbFile.exists()) {
            downloadFile(dbFile);
        }

        GeoIPService.service = new LookupService(dbFile.getAbsolutePath(), LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE); 
    }

    public static String getCountryCode(final String ip) {
        String code = GeoIPService.service.getCountry(ip).getCode();
        if (code == null) {
            code = "unknown";
        } else {
            code = code.toLowerCase();
        }
        return code;
    }

    public static String getCountry(final String ip) {
        String name = GeoIPService.service.getCountry(ip).getName();
        if (name == null) {
            name = "unknown";
        } else {
            name = name.toLowerCase();
        }
        return name;
    }
}
