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
public class Down_data implements Serializable {

    public String file_Name;
     public String client_name;

   Down_data(String f,String c)
    {
        file_Name=f;
        client_name=c;

    }
  
}
