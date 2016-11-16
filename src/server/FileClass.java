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
public class FileClass implements Serializable {


    public String file_name;
    public String rec_client;
    public long off;
    public byte[] arr;
    public long f_size;

   FileClass(String k,long g,String c,long l,byte[] b)
    {
        file_name=k;
        off=g;
        rec_client=c;
        f_size=l;
        arr=b.clone();
    }




}
