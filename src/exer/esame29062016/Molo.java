package exer.esame29062016;

import os.Semaphore;

/**
 * Created by tonca on 08/05/17.
 */
public class Molo {

    private int truckD, truckS;
    private boolean[] isPointFree = new boolean[3];
    private Semaphore mutex = new Semaphore(false);
    private Semaphore camionWaitD = new Semaphore();
    private Semaphore camionWaitS = new Semaphore();
    private Semaphore served = new Semaphore(true);
    private int[] stopGru = new int[2];

    private class Camion extends Thread {

        private boolean isDep;
        private int index;

        public Camion(boolean type, int index) {
            this.isDep = type;
            this.index = index;
        }

        private int enter() {

            int pointIndex = -1;
            mutex.p();

            if(isDep) {

                if(camionWaitD.queue() > 0 || !isPointFree[0]) {
                    mutex.v();
                    camionWaitD.p();
                }

                isPointFree[0] = false;

                mutex.v();

                pointIndex = 0;
            }
            else {

                if(camionWaitS.queue() > 0 || !isPointFree[2] || !isPointFree[1]) {
                    mutex.v();
                    camionWaitS.p();
                }

                if(isPointFree[2]) {
                    isPointFree[2] = false;
                    pointIndex = 2;
                }
                else {
                    isPointFree[1] = false;
                    pointIndex = 1;
                }
                mutex.v();

            }

            return pointIndex;
        }

        public void exit(int point) {

            mutex.p();

            isPointFree[point] = true;

            if(isDep)
                camionWaitD.v();
            else
                camionWaitS.v();

            mutex.v();
        }

        public void run() {

            int point = enter();

            System.out.println(this+" enters point "+point);

            served.p();

            System.out.println(this+" leaves point "+point);

            exit(point);

        }

        public String toString() {
            return format();
        }

        private String format() {
            return "Camion ("+ isDep +","+index+")";
        }

    }


    public static void main(String[] args) {


    }


}
