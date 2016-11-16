/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;
import java.util.Timer;
import java.io.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.*;
import server.List;
import java.util.ArrayList;
import server.FileClass;
import server.FileRecClass;





public class Client extends Thread{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String chatServer;
    private Socket client;

    NewJFrame2 f2;
    clientView frm;
    Object o;
    int verified=0;
    public String client_name="";
    
    public Client( String host ,NewJFrame2 x,clientView y){
        chatServer = host;
        f2 =x;
        frm=y;
    }
    public void run()
    {
       try{
          connectToServer();
          getStreams();
          getdata();


        }

        catch ( IOException ioException )
        {
            ioException.printStackTrace();

        }

    }

    private void connectToServer() throws IOException
    {

        client = new Socket( InetAddress.getByName( chatServer ), 12345 );

    }

    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream( client.getOutputStream() );
        
        output.flush(); // flush output buffer to send header information
        input = new ObjectInputStream( client.getInputStream() );
    }

    public void closeConnection()throws IOException
    {
            frm.setVisible(false);
            
            output.writeObject("cnctnClosed");
            output.flush();
            output.close();
            input.close();
            client.close();
    }



    public void process() throws Exception{

          Data d=new Data(f2);
          output.writeObject(d);
          output.flush();

    }
  



    public synchronized void getdata() throws IOException         //input nei msg
    {
       

        do{

            try {
                o = input.readObject();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }


            if(o instanceof List)
            {


                     List l=(List)o;


                        verified=l.t;
                        client_name=l.client_name;

                        if(verified==1)
                        {
                            f2.setVisible(false);



                                frm.setVisible(true);

                            frm.setClient(this,l.client_name,l.files);



                            for ( String key : l.files.keySet()){

                               System.out.printf("%s ",key);
                               ArrayList arr=l.files.get(key);
                               for(int s=0;s<arr.size();s++)
                                {
                                     //System.out.println(l.arr[k].get(s));
                                    System.out.printf("%s ",arr.get(s) );
                                }
                                System.out.println("");


                            }


                        }
                        else
                        {

                            JOptionPane.showMessageDialog(null,"Username and password combination mismatch");
                        }

                }
                else if (o instanceof FileClass)
                {
                    FileClass l=(FileClass)o;
                    String fileName=l.file_name;
                    long off=l.off;
                    long len=512;
                    
                    if((off+len)>l.f_size)
                    {
                        len=l.f_size-off;
                    }



                    System.out.printf("reading file: %d byte to %d byte \n",off,off+len);
                    System.out.println(fileName);
                    
                    RandomAccessFile f=new RandomAccessFile(fileName,"r");
                    f.seek(off);
                    f.read(l.arr);

                    
                    output.writeObject(l);
                    output.flush();
                    
                }
                else if (o instanceof FileRecClass)
                {
                    FileRecClass l=(FileRecClass)o;
                    String fileName=l.file_name;
                    long off=l.off;
                    int len=512;
                    
                    if((off+len)>=l.f_size)
                    {
                        len=(int) (l.f_size - off);
                        this.sendFileName(l.file_name+" "+client_name);
                    }

                    System.out.printf("writing file: %d byte to %d byte \n",off,off+len);
                    System.out.println(fileName);

                    RandomAccessFile f=new RandomAccessFile("C:/Users/Sujon/Documents/torrent/downloads/"+fileName,"rw");
                    
                    f.seek(off);
                    f.write(l.arr,0,len);

                   System.out.println("file written successfully");

                }


                

        }while(true);

    }


    public void sendFileName( String message ) throws IOException  //msg pathai frm team
    {

            output.writeObject( message );
            output.flush();
                   

    }




    public void downloadRequest(String f,String c) throws IOException  //msg pathai frm team
    {

            Down_data dd=new Down_data(f,c);
            output.writeObject(dd);
            output.flush();


    }


 


}
