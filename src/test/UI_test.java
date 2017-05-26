package test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class UI_test extends JFrame {

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

    public UI_test() {
        initUI();
    }

    private void initUI() {

        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        System.out.println("init applet");
        tl = new TextField("Fra poco parte il conteggio", 20);
        bt = new Button("Azzera");
        // azzera il contatore
        bt.addActionListener(
                // ascoltatore del bottone
                (ActionEvent dummy) -> {
                    cnt = 0;
                }
        );
        add(tl); add(bt);
        setBackground(BACKGROUND_COLOR);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            UI_test ex = new UI_test();
            ex.setVisible(true);
        });
    }
}