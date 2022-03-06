package com.task.elasticsearch.scanner;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileScanner {
    public static String[] scanFolder(String location) {
        List<String> files = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(location);
        String path = url.getPath();
        final File folder = new File(location);
        for (final File fileEntry : Objects.requireNonNull(new File(path).listFiles())) {
            if (!fileEntry.isDirectory()) {
                files.add(fileEntry.getAbsolutePath());
            }
        }
        return files.toArray(new String[]{});
    }

    public static void clearFiles(String[] files) {
        for (int i = 0; i < files.length; i++) {
            String filePath = files[i];
            File file = new File(filePath);
            if (file.exists() && !file.isDirectory()) {
                file.delete();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
