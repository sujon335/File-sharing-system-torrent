/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;
import java.io.*;

/**
 *
 * @author Sujon
 */


public class client_main {


        public static void main(String[] args) throws IOException {

        NewJFrame2 frm = new NewJFrame2();
        clientView cv = new clientView();
        Client c1;
        c1 = new Client( "127.0.0.2",frm ,cv);

        frm.setClient(c1);
        frm.setVisible(true);
        cv.setVisible(false);
        frm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        cv.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        c1.start();



    }

}
