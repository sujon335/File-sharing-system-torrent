/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;



import client.Data;
import client.Down_data;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.*;



/**
 *
 * @author srs
 */
public class SERVER {

    private ServerSocket server;
    private Socket sock;
    String user[]=new String[100];
    String password[]=new String[100];;
    int size=0;
    
    ArrayList<Threadhandler> T;
      public  HashMap<String,ArrayList<String>> files = new HashMap<String,ArrayList<String>>();
      public  HashMap<String,ArrayList<String>> file_sizes = new HashMap<String,ArrayList<String>>();
     public ArrayList<String> active_users=new ArrayList<String>();
  

    SERVER() throws IOException {

     T = new ArrayList();



     getusers();
     getFileClient();
     getFileSize();


    }


    public void runserver() throws IOException
    {
        server=new ServerSocket(12345);
        while(true)
        {
            sock=server.accept();
            Threadhandler tmp = new Threadhandler(sock,this,T.size(),"");
            T.add(tmp);
            tmp.start();
        }
        
    }





    
    public void getusers() throws IOException
    {


        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader("users.txt"));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        String line = null;
        int j=0;
        while ((line = inputStream .readLine()) != null) {

            String[] l =line.split(" ");
            user[j]=l[0];
            password[j]=l[1];

           // System.out.println(user[j]);
          //  System.out.println(password[j]);
            j++;


        }

        size=j;


    }

   public void getFileClient() throws IOException
    {


        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader("file_client.txt"));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        String line = null;
       

   
        while ((line = inputStream .readLine()) != null) {

            
            String[] l =line.split(" ");
            ArrayList<String> clients = new ArrayList<String>();
            for(int k=1;k<l.length;k++)
            {
                clients .add(l[k]);
            }
            files.put(l[0],clients);

           


        }

        


    }


    public void getFileSize() throws IOException
    {


        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader("file_size.txt"));
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        String line = null;



        while ((line = inputStream .readLine()) != null) {


            String[] l =line.split(" ");
            ArrayList<String> sizes = new ArrayList<String>();
            for(int k=1;k<l.length;k++)
            {
                sizes .add(l[k]);
            }
            file_sizes.put(l[0],sizes);




        }




    }




     public void sendFileListtoAll() throws IOException, ClassNotFoundException
        {

        for(int i=0;i<T.size();i++){

            T.get(i).sendFileList(files,active_users);
        }


     }

     public void sendFileRequest(byte[] a,String cl,long off,String fl,String rec_cl,long sz) throws IOException, ClassNotFoundException
        {

         

        for(int i=0;i<T.size();i++){


            if(T.get(i).thread_client_name.compareTo(cl)==0)
            {
                T.get(i).sendFileReq(a,fl,off,rec_cl,sz);
            }
            
        }


     }


       public void sendFile(String rec_client,FileClass f) throws IOException, ClassNotFoundException
        {



        for(int i=0;i<T.size();i++){


            if(T.get(i).thread_client_name.compareTo(rec_client)==0)
            {
                T.get(i).sendFileClient(f.arr,f.file_name,f.off,f.f_size);
            }

        }


     }






}



class Threadhandler extends Thread{

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
     String tname="a";
     String tpass="b";
     long  file_size= 0;
    String fileClient="";
    String filename="";
    String clientname="";
    int t=0;
    SERVER ser;
    int index,i=0;
    int com=0;
    Object o;
    String thread_client_name="";


    Threadhandler(Socket sock,SERVER s, int in,String so) {
       
        connection=sock;
        ser=s;
        index= in;
        thread_client_name=so;
    }
    
    public void run()
    {
        try{
              getStreams();
            try {
                getinput();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }

        catch ( IOException ioException )
        {
            ioException.printStackTrace();

        }
    }
    
    
    
    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream(  connection.getOutputStream() );
        
        output.flush(); // flush output buffer to send header information
        input = new ObjectInputStream(  connection.getInputStream() );
    }
    
    public void closeConnection()throws IOException
    {
            output.close();
            input.close();
            connection .close();
    }



 







    public void getinput() throws IOException, ClassNotFoundException
    {

        do{

            o = input.readObject();


             if(o instanceof String )
             {
                 String ss= o.toString();
                 if(ss.compareTo("cnctnClosed")==0)
                 {
                    ser.active_users.remove(tname);
                    for(int s=0;s<ser.active_users.size();s++)
                        {
                            System.out.printf("%s ",ser.active_users.get(s));
                        }
                    ser.sendFileListtoAll();
                 }

                else{
                 

                           fileClient= o.toString();
                           String[] l =fileClient.split(" ");
                           filename=l[0];
                           clientname=l[1];
                           file_size=Long.parseLong(l[2]);
                           int count=0,sig=0;;
                            for ( String key : ser.files.keySet()){

                                if(filename.compareTo(key)==0)
                                {
                                    count=1;
                                    ArrayList arr=ser.files.get(key);
                                    for(int s=0;s<arr.size();s++)
                                    {
                                         if(clientname.compareTo((String) arr.get(s))==0)
                                         {
                                             sig=1;
                                            break;
                                         }
                                    }
                                    if(sig==0)
                                    {

                                        arr.add(clientname);
                                        ser.files.put(filename,arr);
                                    }


                                    break;
                                }

                             }

                            if(count==0)
                            {
                                     ArrayList<String> clients = new ArrayList<String>();
                                    clients .add(clientname);
                                    ser.files.put(filename,clients);

                                    ArrayList<String> f_size = new ArrayList<String>();
                                    f_size.add(l[2]);
                                    ser.file_sizes.put(filename,f_size);

                            }






                            try {
                                BufferedWriter out= new BufferedWriter(new FileWriter("file_client.txt"));


                                    for ( String key : ser.files.keySet()){

                                         out.write(key+" ");
//                                        System.out.printf("%s ",key);
                                        ArrayList arr=ser.files.get(key);
                                        for(int s=0;s<arr.size();s++)
                                        {
                                            out.write((String)arr.get(s)+" ");
//                                            System.out.printf("%s ",arr.get(s) );
                                        }
                                        out.newLine();
//                                        System.out.println("");
                                     }
                                    out.close();
                                }
                            catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }


                             try {
                                BufferedWriter out= new BufferedWriter(new FileWriter("file_size.txt"));


                                    for ( String key : ser.file_sizes.keySet()){

                                         out.write(key+" ");
//                                        System.out.printf("%s ",key);
                                        ArrayList arr=ser.file_sizes.get(key);
                                        for(int s=0;s<arr.size();s++)
                                        {
                                            out.write((String)arr.get(s)+" ");
//                                            System.out.printf("%s ",arr.get(s) );
                                        }
                                        out.newLine();
//                                        System.out.println("");
                                     }
                                    out.close();
                                }
                            catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }




                           ser.sendFileListtoAll();

                 }

                        
             }

             else if(o instanceof Data )
            {


                    Data d=(Data)o;
                    tname=d.Name;
                    tpass=d.pass;

                    for(int v=0;v<=ser.size;v++)
                    {
                        if(ser.user[v].compareTo(tname)==0)
                        {
                            if(ser.password[v].compareTo(tpass)==0)
                            {
                                com = 5;
                            }
                            break;
                        }
                    }


                    if(com==5)
                    {
                        t=1;

                        thread_client_name=tname;
                        ser.active_users.add(tname);
                        for(int s=0;s<ser.active_users.size();s++)
                        {
                            System.out.printf("%s ",ser.active_users.get(s));
                        }

                    }

                    else
                    {
                        t=0;
                    }


                    ser.sendFileListtoAll();


            }

            else if(o instanceof Down_data )
            {
                    Down_data dd=(Down_data)o;
                    filename=dd.file_Name;
                    clientname=dd.client_name;

                    BufferedReader inputStream = null;
                    try {
                        inputStream = new BufferedReader(new FileReader("file_size.txt"));
                    }
                    catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    String line = null;
                 
                    while ((line = inputStream .readLine()) != null) {

                        String[] l =line.split(" ");

                        if(l[0].compareTo(filename)==0)
                        {
                            file_size=Long.parseLong(l[1]);
                            break;
                        }

                    }

//                    System.out.println(file_size);

                    ArrayList clients=ser.files.get(filename);

                   for(int s=0;s<clients.size();s++)
                   {
                       int l=1;
                       for(int k=0;k<ser.active_users.size();k++)
                       {
                           String a=(String) clients.get(s);
                           String b=(String)ser.active_users.get(k);
                           if((a.compareTo(b)==0))
                           {
                               l=0;
                               break;
                           }
                       }
                       if(l==1)
                           clients.remove(s);

                   }


                    

                    long of=0;

                    while(of<file_size)
                    {
                       

                            for(int s=0;s<clients.size();s++)
                            {
                                byte[] arr=new byte[512];
                                ser.sendFileRequest(arr,(String) clients.get(s),of,filename,clientname,file_size);
                                
                                of=of+512;
                                if(of>file_size) break;
                            }
                            

                    }


        //                        System.out.printf("%s ",aaa.get(s));
                            


                    
                    
                    


            }

            else if (o instanceof FileClass)
            {
                FileClass l=(FileClass)o;
                
                ser.sendFile(l.rec_client,l);

            }



            else
            {
                
            }

    
        }while(true);



    }




// public void sendFileList(HashMap<String,ArrayList<String>> ff,ArrayList<String> active_users) throws IOException
// {
//              List lu=new List(t,ff,tname);
//        output.writeObject(lu);
//        output.flush();
//
//  }

 public void sendFileList(HashMap<String,ArrayList<String>> ff,ArrayList<String> active_users) throws IOException
 {

    HashMap<String,ArrayList<String>> f=(HashMap<String, ArrayList<String>>) ff.clone();
    ArrayList<String> ac =(ArrayList<String>) active_users.clone();
    ArrayList<String> del_files=new ArrayList<String>();

      for ( String key : f.keySet()){
          int p=0;


            ArrayList arr=f.get(key);
            for(int s=0;s<arr.size();s++)
            {
                        for(int k=0;k<ac.size();k++)
                        {
                            String a=(String)arr.get(s);
                            String b=(String)ac.get(k);

                             if(a.equals(b))
                                {

                                    p=1;
                                    break;
                                }
                        }
                        if(p==1)
                        {
                            break;
                        }

             }
            if(p==0)
            {
               del_files.add(key);
            }

      }

      for(int k=0;k<del_files.size();k++)
      {
          f.remove(del_files.get(k));
      }




        List lu=new List(t,f,tname);
        output.writeObject(lu);
        output.flush();

 }
    
    


 public void sendFileReq(byte[] b,String file,long off,String rec,long size) throws IOException{

        
        FileClass lu=new FileClass(file,off,rec,size,b);
        output.writeObject(lu);
        output.flush();
     
 }


 public void sendFileClient(byte[] b,String file,long off,long size) throws IOException{


        FileRecClass lu=new FileRecClass(file,off,size,b);
        output.writeObject(lu);
        output.flush();

 }




  
    
}





