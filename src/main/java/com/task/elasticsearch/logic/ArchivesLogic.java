package com.task.elasticsearch.logic;

import com.task.elasticsearch.datasource.EsClient;
import com.task.elasticsearch.models.FileType;
import com.task.elasticsearch.models.ImportFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArchivesLogic {

    public static void processArchivesFileNotExist(String[] filesNotExisting) throws Exception {
        List<ImportFile> alImportFiles = new ArrayList<>();
        for (int j = 0; j < filesNotExisting.length; j++) {
            String file = filesNotExisting[j];
            if (file != null) {
                ImportFile importFile = new ImportFile(
                        UUID.randomUUID().toString(),
                        file,
                        "",
                        FileType.ARCHIVE
                );
                alImportFiles.add(importFile);
            }
        }
        EsClient.importFile(alImportFiles);
    }

}
