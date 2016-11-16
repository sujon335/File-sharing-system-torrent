/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author srs
 */
public class List implements Serializable {

    public int t;
    public  HashMap<String,ArrayList<String>> files;
    public String client_name;

   List(int k, HashMap<String,ArrayList<String>> m,String name)
    {
        t=k;
        files=(HashMap<String, ArrayList<String>>) m.clone();
        
        client_name=name;
    }


//  public void print(){
//      System.out.println("in list ---------------");
//        for ( String key : files.keySet()){
//
//
//                System.out.printf("%s ",key);
//                ArrayList arr=files.get(key);
//                for(int s=0;s<arr.size();s++)
//                {
//
//                System.out.printf("%s ",arr.get(s) );
//                }
//
//                System.out.println("");
//        }
//        System.out.println("in list ---------------");
//  }

}
