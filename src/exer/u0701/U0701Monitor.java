package exer.u0701;

import os.Sys;
import os.Util;
import java.util.Vector;


public class U0701Monitor {
    private static final int TH_PERIOD = 4000;
    private static final int MAINPERIOD = 500;
    // per fermare i thread
    private boolean toRun = true;
    // stringa d'ingresso condivisa
    static final int MAXQUEUE = 3;
    static final int NUM_READERS = 2;
    private Vector messages = new Vector();

    private synchronized void putMessage(String msg) throws InterruptedException {
        while (messages.size() == MAXQUEUE) {
            Sys.out.println("The buffer is full! I'm waiting...");
            wait();
        }
        messages.addElement(msg);
        Sys.out.println(messages);
        notify();
    }

    private synchronized String getMessage() throws InterruptedException {

        while (messages.size() == 0) {
            wait(); //By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }
        String message = (String) messages.firstElement();
        messages.removeElement(message);

        notify();

        return message;
    }

    /**{c}
     * primo thread, input
     */
    public class WriterTest extends Thread {
        public void run() {
            while(toRun) {
                Sys.in.prompt("Battere stringa:");
                try {
                    putMessage(Sys.in.readLine());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }// while(true)
        }
    } //{c} Writer

    /**{c}
     * secondo thread, output
     */
    public class ReaderTest extends Thread {

        public ReaderTest(String name) {
            super(name);
        }

        public void run(){
            int cnt = 1;
            String old = "", s;

            while(toRun){
                try {
                    s = getMessage(); // per ridurre l'interferenza
                    if (s.equals("exit")) {
                        toRun = false;
                        Sys.out.println("GIOCO FINITO! by Thread "+this.getName());
                    }
                    else {  // cambiata stringa
                        old = s;
                        Sys.out.println("("+(cnt++)+") ->"+s+"<- by Thread "+this.getName());
                        Util.sleep(TH_PERIOD);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } // while(true)
        }
    } //{c} Reader

    /**[m][s]
     * main di collaudo
     */
    public static void main(String[] args) {
        U0701Monitor one = new U0701Monitor();
        Thread writer = one.new WriterTest();
        // i due thread sono classi interne
        // le loro istanze sono 'collegate' all'istanza
        // della classe U0701
        writer.start();
        for(int i=0; i<NUM_READERS; i++) {
            Thread reader = one.new ReaderTest(Integer.toString(i));
            reader.start();
        }
//        while(one.toRun){
//            String s = one.input; // per ridurre l'interferenza
//            Util.sleep(MAINPERIOD);
//            if (s.equals("exit")) {// fine
//                one.toRun = false;
//            }
//        } // while(toRun)
//        Sys.out.println(one.toRun);
//        Sys.out.println("!!! Programma terminato: battere return !!!");

    } //[m][s] main
} //{c} U0701
