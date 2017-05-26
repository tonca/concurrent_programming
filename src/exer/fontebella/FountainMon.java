package exer.fontebella;

import os.Monitor;
import os.Util;

import java.util.concurrent.locks.Condition;


/**
 * Created by tonca on 29/04/17.
 */
public class FountainMon extends Monitor {

    private static final int FILLING_TIME = 15000;
    private int freeSpouts = 8;
    private int lastSpoutOccupied = 7;
    // protezione della sezione critica
    private int stat = 2;

    private int clientsA, clientsB;

    // Monitor conditions
    private Condition queueA = new Condition();
    private Condition queueB = new Condition();

    public static void main(String[] args) {

        FountainMon fou = new FountainMon();

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

            mEnter();

            if (clientsA > 0 || freeSpouts == 0 || (clientsB>0 && stat!=0)) {
                clientsA++;

                System.out.println("vvv Il cliente "+
                        Thread.currentThread().getName()+
                        " di tipo A attende in coda (clientiA="+clientsA+")");
                queueA.cWait();
                clientsA--;
                System.out.println("^^^ Il cliente "+
                        Thread.currentThread().getName()+
                        " di tipo A termina l'attesa in coda (clientiA="+clientsA+")");

            }

            freeSpouts--;

            stat=2;
            // reset stat
            System.out.println("************ zampilli liberi = "+freeSpouts);
            int spout = (freeSpouts = (freeSpouts+1) %8)+1;

            mExit();

            return spout;

        }

        private void fillingDone() {
            mEnter();

            freeSpouts++;

            System.out.println("************ zampilli liberi = "+
                    freeSpouts);

            if (clientsB>0 && (clientsA==0 || stat!=0) )
                queueB.cSignal();
            else if (clientsA>0)
                queueA.cSignal();

            mExit();

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

            mEnter();

            if (clientsB > 0 || freeSpouts == 0 || (clientsA>0 && stat==0)) {
                clientsB++;

                System.out.println("vvv Il cliente "+
                        Thread.currentThread().getName()+
                        " di tipo A attende in coda (clientiA="+clientsA+")");
                queueA.cWait();
                clientsB--;
                System.out.println("^^^ Il cliente "+
                        Thread.currentThread().getName()+
                        " di tipo A termina l'attesa in coda (clientiA="+clientsA+")");

            }

            freeSpouts--;

            stat=2;
            // reset stat
            System.out.println("************ zampilli liberi = "+freeSpouts);
            int spout = (freeSpouts = (freeSpouts+1) %8)+1;

            mExit();

            return spout;

        }

        private void fillingDone() {
            mEnter();

            freeSpouts++;

            System.out.println("************ zampilli liberi = "+
                    freeSpouts);

            if (clientsB>0 && (clientsA==0 || stat!=0) )
                queueB.cSignal();
            else if (clientsA>0)
                queueA.cSignal();

            mExit();

        }

    }
}
