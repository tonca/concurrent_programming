package exer.u0701;
import os.Util;
import os.Sys;

/**{c}
 * unita' 7 esercizio 01
 * creazione di thread
 * @author M.Moro DEI UNIPD
 * @version 1.00 2004-10-26
 */

public class U0701 {
    private static final int TH2PERIOD = 2000;
    private static final int MAINPERIOD = 500;
    // periodo del secondo thread
    private String input = "";
    // stringa d'ingresso condivisa
    private boolean toRun = true;
    // per fermare i thread

    /**{c}
     * primo thread, input
     */
    public class Th1 extends Thread {
        public void run() {
            while(toRun){
                Sys.in.prompt("Battere stringa:");
                input = Sys.in.readLine();} // while(true)
        }
    } //{c} Th1

    /**{c}
     * secondo thread, output
     */
    public class Th2 extends Thread{
        public void run(){
            int cnt = 1;
            String old = "", s;

            while(toRun){
                Util.sleep(TH2PERIOD);
                s = input; // per ridurre l'interferenza
                if (!s.equals(old)){  // cambiata stringa
                    old = s;
                    Sys.out.println("("+(cnt++)+") ->"+s+"<-");}
            } // while(true)
        }
    } //{c} Th2

    /**[m][s]
     * main di collaudo
     */
    public static void main(String[] args) {
        U0701 one = new U0701();
        Thread t1 = one.new Th1();
        Thread t2 = one.new Th2();
        // i due thread sono classi interne
        // le loro istanze sono 'collegate' all'istanza
        // della classe U0701
        t1.start(); t2.start();
        while(one.toRun){
            String s = one.input; // per ridurre l'interferenza
            Util.sleep(MAINPERIOD);
            if (s.equals("exit")) {// fine
                one.toRun = false;
            }
        } // while(toRun)
        Sys.out.println(one.toRun);
        Sys.out.println("!!! Programma terminato: battere return !!!");
    } //[m][s] main
} //{c} U0701
