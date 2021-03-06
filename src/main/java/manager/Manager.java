package manager;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Manager {

    static Ec2Client ec2Client;
    static final String L2M_QUEUE = "L2M_Queue"; // local to manager
    static final String M2W_QUEUE = "M2W_Queue"; // manager to workers
    static final String W2M_QUEUE = "W2M_Queue"; // workers to manager
    static ExecutorService downloadPool;
    static ExecutorService distributionPool;
    static ExecutorService nodesCreationPool;
    static ExecutorService ConvertPDFPool;
    static ExecutorService filesPool;
    static final int DOWNLOAD_THREADS = 5;
    static final int DISTRIBUTION_THREADS = 100; //we go hard
    static final int CREATION_THREADS = 1; // this will create workers
    static final int CONVERT_THREADS = 10;

    static FileManager fileManager;

    private static QueueConnection connectionToLocalApp;
    private static QueueConnection connectionToWorkers;
    private static QueueConnection connectionToWorkers2;
    private static QueueConnection connectionToWorkers3;
    private static QueueConnection connectionToWorkers4;
    private static QueueConnection connectionToWorkers5;
    private static QueueConnection connectionToWorkers6;
    private static QueueConnection connectionToWorkers7;
    private static QueueConnection connectionToWorkers8;
    private static Manager_sqsOPS manager_SQS;

    public static void main(String[] args) {
        manager_SQS = new Manager_sqsOPS();
        //Manager to worker Queue
        final String M2W_QUEUE_URL = manager_SQS.createSQS(M2W_QUEUE);
        final String W2M_QUEUE_URL = manager_SQS.createSQS(W2M_QUEUE);
        initialize();
        connectionToLocalApp = new QueueConnection(L2M_QUEUE, new LocalListener(M2W_QUEUE_URL));
        connectionToLocalApp.start();

        connectionToWorkers = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers2 = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers3 = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers4 = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers5 = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers6 = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers7 = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers8 = new QueueConnection(W2M_QUEUE, new WorkerListner(M2W_QUEUE_URL));
        connectionToWorkers.start();
        connectionToWorkers2.start();
        connectionToWorkers3.start();
        connectionToWorkers4.start();
        connectionToWorkers5.start();
       /* connectionToWorkers6.start();
        connectionToWorkers7.start();
        connectionToWorkers8.start();*/
        System.out.println(Thread.currentThread().getName());

    }

    private static void initialize() {
        ec2Client = Ec2Client.builder()
                .region(Region.US_EAST_1)
                .build();
        // Create thread pools for manager's tasks
        downloadPool = Executors.newFixedThreadPool(DOWNLOAD_THREADS);
        distributionPool = Executors.newFixedThreadPool(DISTRIBUTION_THREADS);
        nodesCreationPool = Executors.newFixedThreadPool(CREATION_THREADS);
        ConvertPDFPool = Executors.newFixedThreadPool(CONVERT_THREADS);
        filesPool = Executors.newCachedThreadPool();

        fileManager = new FileManager();
    }
}
