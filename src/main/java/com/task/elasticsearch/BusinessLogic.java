package com.task.elasticsearch;

import com.task.elasticsearch.logic.ArchivesLogic;
import com.task.elasticsearch.logic.EmailLogic;
import com.task.elasticsearch.logic.FileLogic;
import com.task.elasticsearch.logic.ImageLogic;
import com.task.elasticsearch.scanner.FileScanner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class BusinessLogic {
//    @todo should be environment variables or configuration
    public static final String baseLocationFolder = "files/";
    public static final String baseDestinationFolder = "processed/";
    public static final String archives = "archives";
    public static final String emails = "emails";
    public static final String images = "images";
    public static final String text = "text";


    public static void processFiles(String[] filesPart, String location, int limitFileBatchQueries) throws Exception {
        switch (location) {
            case archives:
                processArchives(filesPart, limitFileBatchQueries);
                break;
            case emails:
                processEmails(filesPart, limitFileBatchQueries);
                break;
            case images:
                processImages(filesPart, limitFileBatchQueries);
                break;
            case text:
                processText(filesPart, limitFileBatchQueries);
                break;
            default:
               break;
        }
//        FileScanner.clearFiles(filesPart);
    }

    private static void processText(String[] filesPart, int limitFileBatchQueries) throws Exception {
        boolean elementExist = Boolean.TRUE;
        for (int i = 0; elementExist ; i++) {
            String[] batchFiles = Arrays.copyOfRange(filesPart, i * filesPart.length/limitFileBatchQueries, i == filesPart.length - 1 ? filesPart.length  : (i + 1) * limitFileBatchQueries);
            FileLogic.processTextFileNotExist(batchFiles);
            if ( i == filesPart.length - 1 || filesPart.length == 0 ) {
                elementExist = Boolean.FALSE;
            }
        }
    }

    private static void processImages(String[] filesPart, int limitFileBatchQueries) throws Exception {
        boolean elementExist = Boolean.TRUE;
        for (int i = 0; elementExist ; i++) {
            String[] batchFiles = Arrays.copyOfRange(filesPart, i * filesPart.length/limitFileBatchQueries, i == filesPart.length - 1 ? filesPart.length  : (i + 1) * limitFileBatchQueries);
            ImageLogic.processImageFileNotExist(batchFiles);
            if ( i == filesPart.length - 1 || filesPart.length == 0) {
                elementExist = Boolean.FALSE;
            }
        }
    }

    private static void processEmails(String[] filesPart, int limitFileBatchQueries) throws Exception {
        boolean elementExist = Boolean.TRUE;
        for (int i = 0; elementExist ; i++) {
            String[] filesPartTemp = new String[]{};
            String[] batchFiles = Arrays.copyOfRange(filesPart, i * filesPart.length/limitFileBatchQueries, i == filesPart.length - 1 ? filesPart.length  : (i + 1) * limitFileBatchQueries);
            EmailLogic.processEmailFileNotExist(batchFiles);
            if ( i == filesPart.length - 1 || filesPart.length == 0) {
                elementExist = Boolean.FALSE;
            }
        }
    }

    private static void processArchives(String[] filesPart, int limitFileBatchQueries) throws Exception {
        boolean elementExist = Boolean.TRUE;
        for (int i = 0; elementExist ; i++) {
            String[] batchFiles = Arrays.copyOfRange(filesPart, i * filesPart.length/limitFileBatchQueries, i == filesPart.length - 1 ? filesPart.length  : (i + 1) * limitFileBatchQueries);
            ArchivesLogic.processArchivesFileNotExist(batchFiles);
            if ( i == filesPart.length - 1 || filesPart.length == 0) {
                elementExist = Boolean.FALSE;
            }
        }
    }
}
