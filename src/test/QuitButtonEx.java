package test;

import os.Util;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class QuitButtonEx extends JFrame {

    private CountRunner runner;

    public QuitButtonEx() {
        runner = new CountRunner();
        initUI();
    }

    private void initUI() {

        JButton quitButton = new JButton("Reset!");

        // crea il thread che aggiorna il contatore
        runner.start();

        quitButton.addActionListener((ActionEvent event) -> {
            runner.reset();
        });

        createLayout(quitButton);

        setTitle("Quit button");
        setSize(100, 50);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("start!");

        // attiva il thread (il metodo run())

    }

    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            QuitButtonEx ex = new QuitButtonEx();
            ex.setVisible(true);
        });
    }
}

class CountRunner extends Thread {

    private final int THPERIOD = 2;
    private int cnt;
    private boolean stopFlag = false;
    private PrintRunner printer;

    public CountRunner() {
        cnt = 0;
        printer = new PrintRunner(this);
        printer.start();
    }

    public void run() {
        while (!stopFlag) {
            Util.sleep(THPERIOD);
            cnt++;
        } // while
    }

    public void reset() {
        cnt = 0;
    }

    public int getCount() {
        return cnt;
    }
}

class PrintRunner extends Thread {
    CountRunner counter;
    private final int THPERIOD = 1000;
    public PrintRunner(CountRunner counter) {
        this.counter = counter;
    }

    public void run() {
        while (true) {
            Util.sleep(THPERIOD);
            System.out.println("repaint cnt="+ counter.getCount());
        } // while
    }
}