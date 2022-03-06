package com.task.elasticsearch;

import com.task.elasticsearch.datasource.EsClient;
import com.task.elasticsearch.scanner.FileScanner;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.*;

public class ThreadRunner {
    private static final ThreadFactoryUtil tfu = new ThreadFactoryUtil();
    private static final HashSet<String> hsFoldersToScan = new HashSet<>(
            Arrays.asList(
                    BusinessLogic.archives,
                    BusinessLogic.emails,
                    BusinessLogic.images,
                    BusinessLogic.text
            )
    );
    private static Runnable runnable = null;

//    @todo should be environment variables
    private static final int limitThreadPerFolder = 2;
    private static final int limitFileBatchQueries = 2;


    public static void runApplication() throws Exception {
        createIndex();
        initializeThreadFactory();
        createThreads();

    }

    private static void createIndex() throws Exception {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(EsClient.INDEX);
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024)
        );
        RequestOptions requestOptions = builder.build();

        boolean exists = EsClient.instance().indices().exists(request, requestOptions);

        if (!exists) {
            if (!EsClient.createIndexWithMapping()) {
                throw new Exception("index not created");
            }
        }
    }

    private static void createThreads() {
        for (final String location : hsFoldersToScan) {
            String[] files = FileScanner.scanFolder(BusinessLogic.baseLocationFolder +  location);
            for (int i = 0; i < limitThreadPerFolder; i++) {
                String[] filesPart = Arrays.copyOfRange(files, i * files.length/limitThreadPerFolder, i == limitThreadPerFolder -1 ? ((i + 1) * files.length/limitThreadPerFolder + files.length % limitThreadPerFolder) : (i + 1) * files.length/limitThreadPerFolder);
                runnable = () -> {
                    try {
                        BusinessLogic.processFiles(filesPart, location, limitFileBatchQueries);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                if (filesPart.length > 0) {
                    tfu.setLocation(location);
                    tfu.setiPriorityThreadLevel(0);
                    tfu.newThread(runnable);
                }
            }
        }

        HashMap<Integer, ArrayList<Thread>> hmThreads = tfu.getHmThreads();

        for (Map.Entry<Integer, ArrayList<Thread>> entry : hmThreads.entrySet()) {
            ArrayList<Thread> alThreads = entry.getValue();
            alThreads.forEach(Thread::start);
            for (Thread thread : alThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Completed");
        System.exit(0);
    }

    private static void initializeThreadFactory() {
        ThreadFactoryUtil tfu = new ThreadFactoryUtil();
        tfu.setDaemon(true);
        tfu.setPriority(Thread.NORM_PRIORITY);
        tfu.setStackSize(1024);
        tfu.setThreadGroup(new ThreadGroup("FilesToProcess"));
        tfu.setWrapRunnable(true);
        tfu.setiPriorityThreadLevel(1);
    }
}
