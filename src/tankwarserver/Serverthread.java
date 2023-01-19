
package tankwarserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Serverthread extends TankwarServer implements Runnable{
    Socket socket;
    int socketID;

    public Serverthread(Socket socket,int socketID){
        this.socket = socket;
        this.socketID=socketID;
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("P"+socketID+" join");
            print(Integer.toString(socketID));
            boolean flag = true;
            while (flag)
            {
                //阻塞，等待該客戶端的輸出流
                String line = reader.readLine();
                //若客戶端退出，則退出連線。
                if (line == null){
                    flag = false;
                    continue;
                }
                String msg = "P"+socketID+":"+line;
                System.out.println(msg);
                //向線上客戶端輸出資訊
                print(msg);
            }

            closeConnect();
        } catch (IOException e) {
            try {
                closeConnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    /**
     * 向所有線上客戶端socket轉發訊息
     * @param msg
     * @throws IOException
     */
    private void print(String msg) throws IOException {
        PrintWriter out = null;
            synchronized (sockets){
            for (Socket sc : sockets){
                out = new PrintWriter(sc.getOutputStream());
                out.println(msg);
                out.flush();
            }
        }
    }
    /**
     * 關閉該socket的連線
     * @throws IOException
     */
    public void closeConnect() throws IOException {
        System.out.println("P"+socketID+" leave");
        print("P"+socketID+" leave");
        //移除沒連線上的客戶端
        synchronized (sockets){
            sockets.remove(socket);
        }
        socket.close();
    }
}
