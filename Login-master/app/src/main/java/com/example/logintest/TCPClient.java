package com.example.logintest;


import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient extends AsyncTask<String, Void, String> {
    // I dont know if this class can function in here or just use assynTask to connect
    // straight to python code
    private String hostname = "localhost";
    private int port = 0;
    public TextView txt;
    Socket s;
    private Socket clientSocket;
    private PrintWriter outToClient;
    private BufferedReader inFromServer;

    private String FILE_PATH = "./data.txt";

    //constructor
    public TCPClient(String hostname, int port) {

        this.hostname = hostname;
        this.port = port;
    }

    public String get_address() {
        // concentrate hostname and port and return them as one
        return hostname + ":" + port;
    }

    public boolean change_address(String hostname, int port) {

        this.hostname = hostname;
        this.port = port;
        return true;

    }

    public String send_command(String command) {

        String received_data = "";

        try {
            //create connection
            clientSocket = new Socket(hostname, port);
            System.out.println(clientSocket.getInetAddress() + " connected on port " + clientSocket.getPort());

            int c = 0;
            byte buffer[] = command.getBytes();
            clientSocket.getOutputStream().write(buffer); // send command

            while ((c = clientSocket.getInputStream().read()) != -1) {
                //System.out.print((char) c );
                received_data += (char) c;
            }
            clientSocket.close();

        } catch (Exception e) {
            System.out.println(e);
        }


        return received_data;

    }

    @Override
    protected String doInBackground(String... voids) {

        String data;
        data = send_command(voids[0]);

        try{
            s = new Socket("192.168.1.2",8000);

        }catch(IOException e){
            e.printStackTrace();
        }
        value = data;
        return data;
    }
    //variable rieng
    private String value = null;
    public boolean isReady(){
        if (value != null){
            return true;
        }
        else{
            return false;
        }
    }
    public String get_value(){
        return value;
    }
    protected void onPostExecute(String result) {
        //txt.setText(result); log data ready
    }
}


