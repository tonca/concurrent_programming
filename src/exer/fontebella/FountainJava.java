package exer.fontebella;

import os.Util;

/**
 * Created by tonca on 29/04/17.
 */
public class FountainJava {

    private static final int FILLING_TIME = 15000;
    private int freeSpouts = 8;
    private int lastSpoutOccupied = 7;
    // protezione della sezione critica
    private int stat = 2;

    private int clientsA, clientsB;

    private int ticketA=0, ticketB=0;
    private int servizioA=0, servizioB=0;


    public static void main(String[] args) {

        FountainJava fou = new FountainJava();

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

        private synchronized int enterQueueA() {

            clientsA++;

            System.out.println("vvv Il cliente "+Thread.currentThread().getName()+
                    " di tipo A attende in coda (clientiA="+clientsA+")");

            int ticket = ticketA++;
            // ripete l'attesa su condizione
            while(freeSpouts==0 || (clientsB>0 && stat!=0) || servizioA != ticket)
                try { wait(); } catch (InterruptedException e) {}

            clientsA--;

            System.out.println("^^^ Il cliente "+Thread.currentThread().getName()+
                    " di tipo A termina l'attesa in coda (clientiA="+clientsA+")");

            freeSpouts--;
            // assegna zampillo
            stat=2;
            // reset di stat
            System.out.println("************ zampilli liberi = "+freeSpouts);

            servizioA++;
            int spout = (lastSpoutOccupied = (lastSpoutOccupied+1)%8)+1;

            return spout;
        }

        private synchronized void fillingDone() {

            freeSpouts++;

            System.out.println("************ zampilli liberi = "+
                    freeSpouts);

            notifyAll();

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

        private synchronized int enterQueueB() {

            clientsB++;

            System.out.println("vvv Il cliente "+Thread.currentThread().getName()+
                    " di tipo B attende in coda (clientsB="+clientsB+")");

            int ticket = ticketB++;
            // ripete l'attesa su condizione
            while(freeSpouts==0 || (clientsA>0 && stat==0) || servizioB != ticket)
                try { wait(); } catch (InterruptedException e) {}

            clientsB--;

            System.out.println("^^^ Il cliente "+Thread.currentThread().getName()+
                    " di tipo B termina l'attesa in coda (clientiB="+clientsB+")");

            freeSpouts--;
            // assegna zampillo
            stat=2;
            // reset di stat
            System.out.println("************ zampilli liberi = "+freeSpouts);

            servizioB++;

            int spout = (lastSpoutOccupied = (lastSpoutOccupied+1)%8)+1;

            return spout;
        }

        private synchronized void fillingDone() {

            freeSpouts++;

            System.out.println("************ zampilli liberi = "+
                    freeSpouts);

            notifyAll();


        }

    }
}
