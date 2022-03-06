package com.task.elasticsearch.logic;

import com.task.elasticsearch.datasource.EsClient;
import com.task.elasticsearch.models.FileType;
import com.task.elasticsearch.models.ImportFile;
import com.task.elasticsearch.scanner.FileScanner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.Files;

public class EmailLogic {

    public static void processEmailFileNotExist(String[] filesNotExisting) throws Exception {
        List<ImportFile> alImportFiles = new ArrayList<>();
        for (int j = 0; j < filesNotExisting.length; j++) {
            String file = filesNotExisting[j];
            if (file != null) {
                String content = FileScanner.readFile(file, StandardCharsets.US_ASCII);
                ImportFile importFile = new ImportFile(
                        UUID.randomUUID().toString(),
                        file,
                        content,
                        FileType.EMAIL
                );
                alImportFiles.add(importFile);
            }
        }
        EsClient.importFile(alImportFiles);
    }

}
