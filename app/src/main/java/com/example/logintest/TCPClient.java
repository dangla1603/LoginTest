package com.example.logintest;


import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;

import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import static android.content.ContentValues.TAG;


public class TCPClient extends AsyncTask<String, Void, String> {
    // I dont know if this class can function in here or just use assynTask to connect straight to python code
    private String hostname = "localhost";
    private int port = 0;

    private Socket clientSocket;
    private PrintWriter outToClient;
    private BufferedReader inFromServer;

    private String FILE_PATH = "./data.txt";
    private String value = null;

    //TODO: pass view or something from the main activity to the class to process
    private ImageView img;

    //constructor
    public TCPClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String get_address() {
        // concentrate hostname and port and return them as one
        return hostname + ":" + port;
    }

    public void change_address(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
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
                //Log.d("Data", c+"");
                received_data += (char) c;
            }
            clientSocket.close();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return received_data;

    }

    // Not Tested. Sepapate method for sending video. Will be merged
    public File send_command_video(String command, Context ctx) {
        File video;
        try {
            video = new File(ctx.getCacheDir(), "cacheFileAppeal.srl");
            try (OutputStream output = new FileOutputStream(video)) {  // api19 min to work
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = clientSocket.getInputStream().read(buffer)) != -1) {
                    output.write(buffer, 0, read); // write to file
                }

                output.flush();
                clientSocket.close();
            }
            catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
        finally {
            //clientSocket.close();
        }
        return video;
    }

    // sepapate method for sending image. Will be merged
    public Bitmap send_command_img(String command) {
        Bitmap mIcon11 = null;
        try {
            //create connection
            clientSocket = new Socket(hostname, port);
            System.out.println(clientSocket.getInetAddress() + " connected on port " + clientSocket.getPort());

            byte buffer[] = command.getBytes();
            clientSocket.getOutputStream().write(buffer); // send command
            mIcon11 = BitmapFactory.decodeStream(clientSocket.getInputStream());

            clientSocket.close();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return mIcon11;

    }

    // Not tested. Writefile for android (internal storage)
    // for "ctx" pass: context.getApplicationContext(), Application.instance() or Application.instance().getApplicationContext()
    private boolean writefile(String data, String file_name, Context ctx) {
        try {

            //FileOutputStream fOut = openFileOutput(file_name,Context.MODE_WORLD_READABLE);
            FileOutputStream fOut = ctx.openFileOutput(file_name,Context.MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
            return true;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    //  Not tested. Android writefile to sd card.
    private boolean writefile_sd(String data, String file_name, Context ctx) {
        String filename = "/sdcard/"+ file_name;
        File sdCard = Environment.getExternalStorageDirectory();
        filename = filename.replace("/sdcard", sdCard.getAbsolutePath());
        File tempFile = new File(filename);
        try
        {
            FileOutputStream fOut = new FileOutputStream(tempFile);
            //fOut.write(); // takes byte form of data
            // fOut.getChannel();
            fOut.close();
            return true;
        }catch (Exception e)
        {
            Log.w(TAG, "FileOutputStream exception: - " + e.toString());
            return false;
        }
    }

    @Override
    protected String doInBackground(String... voids) {

        String data;
        data = send_command(voids[0]);

        value = data;
        return data;
    }

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


