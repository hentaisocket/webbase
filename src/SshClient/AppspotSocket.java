/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/*
 

 
 */
package SshClient;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import networktool.DNSQ;

/**
 *
 * @author sick
 */
public class AppspotSocket {
    String webhost;
    String appid;
    private int hostport;
    SSLSocketFactory sslsocketfactory;
    private SSLSocket sock;
    String headerstr; 
    String ipadrs[];
    private String Errormsg;
    private OutputStream ost;
    private InputStream ist;
    private byte b[]=new byte[1024];
    public AppspotSocket(String appid) throws Exception
    {
    this.appid=appid;
    this.webhost=appid+".appspot.com";
    this.hostport=443;
    
    sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        DNSQ dq=new DNSQ();
     
    String googlelist[]={
       "www.google.com.hk","www.google.com.tw","www.google.com.sg","www.google.co.jp","www.google.sg","www.google.cat"
    };
    
    int effecnt=0;
    String effelist[]=new String[googlelist.length];
    for(int i=0;i<googlelist.length;i++)
    {
        try{
        String tempresult=dq.Getip("114.114.114.114", googlelist[i]);
        effelist[effecnt]=tempresult.split("\\|")[0];
        effecnt++;
        }catch(Exception e)
        {
        System.out.println(e);
        }
    }
    
    System.out.println("------------");
    for(int i=0;i<effecnt;i++)
    {
    System.out.println(effelist[i]);
    }
    System.out.println("------------");
    /*
    String tempresult=dq.Getip("114.114.114.114", "www.google.com.cat");
    ipadrs= tempresult.split("\\|");
    if (ipadrs==null )
    {
        Errormsg= "Error: not DNS IP found!";
    }
    
          */
    
    
 
System.out.println(effelist[3]);
//System.out.println(webhost);
   //sock = (SSLSocket) sslsocketfactory.createSocket();
   // sock = (SSLSocket) sslsocketfactory.createSocket(ipadrs[0],hostport);
  SocketAddress socketAddress = new InetSocketAddress(effelist[3],hostport);
  
  boolean loopflag = true;
  int loopcnt=0;
  while(loopflag){
  
  try{
      sock = (SSLSocket) sslsocketfactory.createSocket();
  sock.connect(socketAddress,1600);
  loopflag=false;
  }catch (SocketTimeoutException se){
  loopcnt++;
  System.out.println("Time out Retry connect : "+ loopcnt);
 
  }
  }
  
 System.out.println("connected!!");
    sock.setSoTimeout(8000);
 //System.out.println("2222");
    ost=sock.getOutputStream();
    ist=sock.getInputStream();
    
    setheader();
    
    }
    
    public String GteErrormsg(){return Errormsg;}
    
    public void setheader()
    {
    headerstr="GET /"+" HTTP/1.1"+"\n"      
            +"Host: "+webhost+"\n"
            +"Connection: close"+"\n\n";
    }
    
    public void setheader(String URLparameter)
    {
    headerstr="GET /"+URLparameter+" HTTP/1.1"+"\n"      
            +"Host: "+webhost+"\n"
            +"Connection: close"+"\n\n";
    }
    
    public String URLConmunicate(String URLparameter) throws Exception
    {
    if (URLparameter==null)
    {setheader();}
    else
    {setheader(URLparameter);}
    
    ost.write(headerstr.getBytes());
    ost.flush();
    ist.read(b);
    
    return new String(b);
    }
    
    public void closeappsocket() throws IOException{
        ist.close();   
        ost.close();
       sock.close();
    
    }
    
    public static void main(String arg[])
    {
        try {
            AppspotSocket appsock= new AppspotSocket("aiworkserver");
            String restr=appsock.URLConmunicate("kukuana?qtype=123&qvalue=我");
            System.out.println(restr);
            appsock.closeappsocket();
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    
    }
    
    
}
