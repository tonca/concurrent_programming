package exer.u0703;

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import os.Util;

/**{c}
 * unita' 7 esercizio 02
 * Runnable, Applet
 * @author M.Moro DEI UNIPD
 * @version 1.00 2004-10-27
 */

public class CounterUI extends Applet implements Runnable {
    private final static Color BACKGROUND_COLOR =
            new Color(192, 10, 18);
    private final int THPERIOD = 1000;
    private int cnt = 1;
    // il contatore
    private boolean stopFlag = false;
    private Thread runner;
    // il thread creato, null quando interrotto
    private TextField tl;
    private Button bt;
    // elementi grafici

    /**[m]
     * @see Applet
     */
    public void init() {
        System.out.println("init applet");
        tl = new TextField("Fra poco parte il conteggio", 20);
        bt = new Button("Azzera");
        bt.addActionListener(
            // ascoltatore del bottone
            new ActionListener()  {
                public void actionPerformed(ActionEvent dummy)  { cnt = 0; } // azzera il contatore
            }
        );

        add(tl); add(bt);
        setBackground(BACKGROUND_COLOR);
    } //[m] init

    /**[m]
     * @see Applet
     */
    public void start() {
        System.out.println("start");
        if (runner == null) {
            // crea il thread che aggiorna il contatore
            stopFlag = false;
            runner = new Thread(this);
            runner.start();
            // attiva il thread (il metodo run())
        }
    } //[m] start

    /**[m]
     * @see Applet
     */
    public void stop()  {
        System.out.println("stop");
        if (runner != null)  {
            // distrugge il thread di aggiornamento
            stopFlag = true;
            runner = null; }
    } //[m] stop

    /**[m]
     * @see Runnable
     */
    public void run() {
        // metodo principale del thread
        while (!stopFlag)  {
            Util.sleep(THPERIOD);
            System.out.println("repaint cnt=" + cnt);
            tl.setText("count="+(cnt++));
            repaint(); } // while
    } //[m] run

} //{c} U0702