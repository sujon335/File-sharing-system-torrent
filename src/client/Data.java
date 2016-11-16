/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;
import java.io.*;

/**
 *
 * @author srs
 */
public class Data implements Serializable {

    public String Name;
     public String pass;

   Data(NewJFrame2 f3)
    {
        Name=f3.n;
        pass=f3.p;

    }
  
}
