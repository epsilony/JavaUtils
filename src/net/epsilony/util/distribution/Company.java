/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.distribution;

import com.sun.org.apache.xml.internal.serializer.utils.Messages;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import javax.net.ssl.SSLSocket;
import org.apache.log4j.Logger;

/**
 *
 * @author epsilon
 */
public class Company<PRODUCT> {

    Logger log = Logger.getLogger(Company.class);
    AtomicInteger companyProcessedUnit = new AtomicInteger();
    int id;
    int processStart;
    int processEnd;
    int taskStart;
    ReentrantLock taskRangeLock = new ReentrantLock();
    InetAddress rootServerAddress;
    int rootServerPort;
    boolean reachEnd;
    ReentrantLock processRangeLock = new ReentrantLock();
    LinkedList<int[]> processRangeBuffer = new LinkedList<int[]>();
    private boolean forceLocalCore;
    WorkpieceFactory<PRODUCT> workpieceFactory;
    ArrayList<Worker> workers;
    private int retryCount = 3;
    private int defaultRetryTime = 10;
    
    Thread mainThread;

    public void stop(){
        try {
            mainThread.interrupt();
            Socket socket = new Socket(rootServerAddress, rootServerPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeInt(MessageFlags.ERROR);
        } catch (IOException ex) {
            log.error(ex+" :at stop()");
        }
    }

    private boolean freshCompanyTaskRange() {
        try {
            processRangeLock.lock();
            if (processRangeBuffer.isEmpty()) {
                int[] newRange = waitNewRangeFromRootServer();
                if (newRange == null) {
                    reachEnd = true;
                    return false;
                } else {
                    processRangeBuffer.add(newRange);
                    return true;
                }
            } else {
                int[] range = processRangeBuffer.getFirst();
                processEnd = range[1];
                processStart = range[0];
                return true;
            }
        } finally {
            processRangeLock.unlock();
        }
    }

    private int[] waitNewRangeFromRootServer() {
        int retry = retryCount;
        while (retry > 0) {
            try {
                double speed = 0;
                int processed = 0;
                for (int i = 0; i < workers.size(); i++) {
                    speed += workers.get(i).getActionSpeed();
                    processed += workers.get(i).processedUnit.get();
                }
                Socket socket = new Socket(rootServerAddress, rootServerPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeInt(MessageFlags.NEW_TASK_RANGE);
                out.writeInt(processed);
                out.writeDouble(speed);
                out.flush();
                int flag = in.readInt();
                if ((flag & MessageFlags.FINISH) != 0) {
                    socket.close();
                    return null;
                } else if ((flag & MessageFlags.NEW_TASK_RANGE) != 0) {
                    int[] range = new int[2];
                    range[0] = in.readInt();
                    range[1] = in.readInt();
                    socket.close();
                    return range;
                } else if ((flag & MessageFlags.WAIT_AND_TRY_AGAIN) != 0) {
                    socket.close();
                    try {
                        Thread.sleep(defaultRetryTime * 1000);
                    } catch (InterruptedException ex) {
                        log.error(ex);
                        break;
                    }
                } else {
                    log.error("Wrong!");
                    return null;
                }
            } catch (IOException ex) {
                log.error(ex);
                retry--;
                if(retry<=0){
                    log.error("retry over!");
                }
            }     
        }
        return null;
    }

    private Range nextWorkerRange() {
        try {
            taskRangeLock.lock();
            if (reachEnd) {
                return null;
            }
            if (taskStart >= processEnd) {
                if (forceLocalCore) {
                    reachEnd = true;
                    return null;
                } else {
                    if (freshCompanyTaskRange()) {
                        taskStart = processStart;
                        range[0] = taskStart;
                        taskStart += gap;
                        taskStart = taskStart > processEnd ? processEnd : taskStart;
                        range[1] = taskStart;
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                range[0] = taskStart;
                taskStart += gap;
                taskStart = taskStart > processEnd ? processEnd : taskStart;
                range[1] = taskStart;
                return true;
            }
        } finally {
            taskRangeLock.unlock();
        }
    }

    public int getWorkersNum() {
        return Runtime.getRuntime().availableProcessors();
    }
    int gap;
    PRODUCT processMatrix;

    public class RangeFectchTask implements Runnable {

        @Override
        public void run() {
            int retry = retryCount;
            while (!Thread.interrupted()) {
                try {
                    double speed = 0;
                    int processed = 0;
                    for (int i = 0; i < workers.size(); i++) {
                        speed += workers.get(i).getActionSpeed();
                        processed += workers.get(i).processedUnit.get();
                    }
                    Socket socket = new Socket(rootServerAddress, rootServerPort);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    out.writeInt(MessageFlags.NEW_TASK_RANGE);
                    out.writeInt(processed);
                    out.writeDouble(speed);
                    out.flush();
                    int flag = in.readInt();
                    if ((flag & MessageFlags.FINISH) != 0) {
                        socket.close();
                        break;
                    } else if ((flag & MessageFlags.NEW_TASK_RANGE) != 0) {
                        int[] range = new int[2];
                        range[0] = in.readInt();
                        range[1] = in.readInt();
                        socket.close();
                        try {
                            processRangeLock.lock();
                            processRangeBuffer.add(range);
                        } finally {
                            processRangeLock.unlock();
                        }
                        break;
                    } else if ((flag & MessageFlags.WAIT_AND_TRY_AGAIN) != 0) {
                        socket.close();
                        try {
                            Thread.sleep(defaultRetryTime * 1000);
                        } catch (InterruptedException ex) {
                            log.error(ex);
                            break;
                        }
                    } else {
                        log.error("Wrong!");
                        return;
                    }
                } catch (IOException ex) {
                    log.error(ex);
                    retry--;
                    if(retry<=0){
                        log.error("Range task error!");
                        stop();
                        break;
                    }
                }
                
            }
        }
    }

    class MainTask implements Runnable {

        @Override
        public void run() {
            companyProcessedUnit.set(0);

            log.info("Start quadrateDomains with multi threads");
            ArrayList<PRODUCT> workPieces = new ArrayList<PRODUCT>(getWorkersNum());

            for (int i = 0; i < getWorkersNum(); i++) {
                workPieces.add(workpieceFactory.newWorkpiece());
            }

            ExecutorService es = Executors.newFixedThreadPool(workPieces.size());

            workers = new ArrayList<Worker>(workPieces.size());
            for (int i = 0; i < workPieces.size(); i++) {
                workers.add(new Worker(i));
                es.submit(workers.get(i));
            }
            es.shutdown();
            boolean allDone = false;
            while (!allDone) {
                log.info(String.format("Company Processed %d Unit", companyProcessedUnit.get()));
                try {
                    allDone = es.awaitTermination(1, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {

                    log.error(ex);
                    break;
                }
            }
            log.info("All tasks' done! Assembling");
            PRODUCT companyProduct = workpieceFactory.assemblyWorkpieces(workPieces);
            sendBackProduct(companyProduct);
        }
    }

    public void sendBackProduct(PRODUCT companyProduct) {
    }

    public class Worker implements Runnable {

        int id;
        long actionTime;
        AtomicInteger processedUnit = new AtomicInteger();
        private WorkerActionFactory workerActionFactory;

        public Worker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            
            WorkerAction workerAction = workerActionFactory.newAction(processedUnit);

            while (nextWorkerRange()) {
                long time = System.nanoTime();
                workerAction.setRange(range);
                workerAction.action();
                time = time - System.nanoTime();
                setActionSpeed((range[1] - range[0]) / (double) time);
            }
        }
        double actionSpeed = 0;
        ReentrantLock actionSpeedLock = new ReentrantLock();

        public void setActionSpeed(double d) {
            try {
                actionSpeedLock.lock();
                actionSpeed = d;

            } finally {
                actionSpeedLock.unlock();
            }
        }

        public double getActionSpeed() {
            try {
                actionSpeedLock.lock();
                return actionSpeed;
            } finally {
                actionSpeedLock.unlock();
            }
        }
    }
}
