package exer.fontebella;

import os.Semaphore;
import os.Util;
/**
 * Created by tonca on 29/04/17.
 */
public class FountainSem {

    private static final int FILLING_TIME = 15000;
    private int freeSpouts = 8;
    private int lastSpoutOccupied = 7;
    private Semaphore mutex = new Semaphore(true);
    private Semaphore queueA = new Semaphore(false);
    private Semaphore queueB = new Semaphore(false);
    private int stat = 2;

    public static void main(String[] args) {

        FountainSem fou = new FountainSem();

        int cnt = 1;
        for (; ; ) {
            Util.rsleep(500, 2000);

            if (Util.randVal(1, 2) == 1)
                fou.new ClientATh("num" + (cnt++)).start();
            else
                fou.new ClientBTh("num" + (cnt++)).start();
        }
    }

    private class ClientATh extends Thread {

        public ClientATh(String name) {
            super(name);
        }

        public void run() {
            System.out.println("!!! Il cliente " +
                    getName() + " di tipo A va in coda");
            int zamp = enterQueueA();

            // arriva e attende
            System.out.println("+++ Il cliente " +
                    getName() + " di tipo A va a bere allo zampillo " + zamp);
            Util.sleep(FILLING_TIME); // beve
            System.out.println("--- Il cliente " +
                    getName() + " di tipo A lascia lo zampillo " + zamp);
            fillingDone();

        }

        private int enterQueueA() {

            mutex.p();
            // If the sync condition is not verified wait for it
            // Only after exiting the mutex
            if (queueA.queue() > 0 || freeSpouts == 0 ||
                    (queueB.queue() > 0 && stat != 0)) {
                mutex.v();
                queueA.p();
            }
            // After the waiting you can use a spout
            freeSpouts--;
            stat = 2;
            int spout = (lastSpoutOccupied = (lastSpoutOccupied + 1) % 8) + 1;
            mutex.v();

            return spout;
        }

        private void fillingDone() {

            mutex.p();

            freeSpouts++;

            if (queueB.queue() > 0 && (queueA.queue() == 0 || stat == 0))
                queueB.v();
            else if (queueA.queue() > 0)
                queueA.v();

            mutex.v();

        }
    }

    private class ClientBTh extends Thread {

        public ClientBTh(String name) {
            super(name);
        }

        public void run() {
            System.out.println("!!! Il cliente " +
                    getName() + " di tipo B va in coda");
            int zamp = enterQueueB();

            // arriva e attende
            System.out.println("+++ Il cliente " +
                    getName() + " di tipo B va a bere allo zampillo " + zamp);
            Util.sleep(FILLING_TIME); // beve
            System.out.println("--- Il cliente " +
                    getName() + " di tipo B lascia lo zampillo " + zamp);
            fillingDone();

        }

        private int enterQueueB() {

            mutex.p();
            // If the sync condition is not verified wait for it
            // Only after exiting the mutex
            if (queueB.queue() > 0 || freeSpouts == 0 ||
                    (queueA.queue() > 0 && stat == 0)) {
                mutex.v();
                queueA.p();
            }
            freeSpouts--;
            int spout = (lastSpoutOccupied = (lastSpoutOccupied + 1) % 8) + 1;
            mutex.v();

            return spout;
        }

        private void fillingDone() {

            mutex.p();

            freeSpouts++;

            if (queueB.queue() > 0 && (queueA.queue() == 0 || stat == 0))
                queueB.v();
            else if (queueA.queue() > 0)
                queueB.v();

            mutex.v();

        }
    }
}
