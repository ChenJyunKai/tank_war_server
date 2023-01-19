/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankwarserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class TankwarServer {

    protected static  List<Socket> sockets = new Vector<>();
    static int socketID=0;
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5200);
        boolean flag = true;
        //接受客戶端請求
        while(flag){
            try {
             //阻塞等待客戶端的連線
            Socket accept = server.accept();
            synchronized (sockets){
                sockets.add(accept);
            }
            socketID++;
            //多個伺服器執行緒進行對客戶端的響應
            Thread thread = new Thread(new Serverthread(accept,socketID));
            thread.start();
            }catch (Exception e){
                flag = false;
                e.printStackTrace();
            }
        }
        //關閉伺服器
        server.close();
    }
    
}
