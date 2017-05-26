package exer.fontebella;

import os.Region;
import os.RegionCondition;
import os.Util;

/**
 * Created by tonca on 29/04/17.
 */
public class FountainReg {

    private static final int FILLING_TIME = 15000;
    private int freeSpouts = 8;
    private int lastSpoutOccupied = 7;
    // clients in each queue
    private int clientsA = 0, clientsB = 0;
    private Region ass = new Region(0);
    // protezione della sezione critica
    private int stat = 2;

    public static void main(String[] args) {

        FountainReg fou = new FountainReg();

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

            ass.enterWhen(); // enter queue in mutex through the critical region
            clientsA++;
            System.out.println(
                    "vvv Il cliente "+Thread.currentThread().getName()+
                            " di tipo A attende in coda (clientiA="+clientsA+")");
            ass.leave();
            // Wait for sync condition
            ass.enterWhen(new RegionCondition() {
                public boolean evaluate() {
                    // the condition:
                    return ! (freeSpouts==0 || (clientsB>0 && stat!=0) );
                }
            });

            clientsA--;
            System.out.println("^^^ Il cliente "+
                    Thread.currentThread().getName()+
                    " di tipo A termina l'attesa in coda (clientiA="+clientsA+")");
            // assign spout
            freeSpouts--;
            // reset stat
            stat=2;
            System.out.println("************ zampilli liberi = "+
                    freeSpouts);
            int spout = (lastSpoutOccupied = (lastSpoutOccupied+1)%8)+1;
            ass.leave();

            return spout;
        }

        private void fillingDone() {
            ass.enterWhen();

            freeSpouts++;

            System.out.println("************ zampilli liberi = "+
                    freeSpouts);

            ass.leave();

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

            ass.enterWhen(); // enter queue in mutex through the critical region
            clientsB++;
            System.out.println(
                    "vvv Il cliente "+Thread.currentThread().getName()+
                            " di tipo B attende in coda (clientiA="+clientsA+")");
            ass.leave();
            // Wait for sync condition
            ass.enterWhen(new RegionCondition() {
                public boolean evaluate() {
                    // the condition:
                    return ! (freeSpouts==0 || (clientsA>0 && stat==0) );
                }
            });

            clientsB--;
            System.out.println("^^^ Il cliente "+
                    Thread.currentThread().getName()+
                    " di tipo B termina l'attesa in coda (clientiA="+clientsB+")");
            // assign spout
            freeSpouts--;
            // reset stat
            stat=2;
            System.out.println("************ zampilli liberi = "+
                    freeSpouts);
            int spout = (lastSpoutOccupied = (lastSpoutOccupied+1)%8)+1;
            ass.leave();

            return spout;
        }

        private void fillingDone() {
            ass.enterWhen();

            freeSpouts++;

            System.out.println("************ zampilli liberi = "+
                    freeSpouts);

            ass.leave();

        }
    }
}
