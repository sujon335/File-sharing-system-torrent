/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.io.*;

/**
 *
 * @author srs
 */
public class FileRecClass implements Serializable {


    public String file_name;
    public long off;
    public byte[] arr;
    public long f_size;

   FileRecClass(String k,long g,long l,byte[] b)
    {
      
        file_name=k;
        off=g;
        f_size=l;
        arr=b.clone();
    }




}
