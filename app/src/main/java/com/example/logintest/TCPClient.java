package com.example.logintest;

import android.util.Log;
import android.content.Context;
import android.os.Environment;
import static android.content.ContentValues.TAG;

import java.io.OutputStream;
import java.net.Socket;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class TCPClient {

    private String hostname = "localhost";
    private int port = 0;
    private Socket clientSocket;
    private String value = null;

    private Context context;


    TCPClient(String hostname, int port, Context ctx) { // no modifier  = package-private
        this.hostname = hostname;
        this.port = port;
        this.context = ctx;
    }


    // concentrate hostname and port and return them as one
    public String get_address() {
        return hostname + ":" + port;
    }


    public void change_address(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }


    String send_command(String command) {

        String received_data = "";

        try {
            //create connection
            clientSocket = new Socket(hostname, port);
            System.out.println(clientSocket.getInetAddress() + " connected on port " + clientSocket.getPort());

            int c;
            byte[] buffer = command.getBytes();
            clientSocket.getOutputStream().write(buffer); // send command

            while ((c = clientSocket.getInputStream().read()) != -1) {
                received_data += (char) c;
            }
            clientSocket.close();

        } catch (Exception e) {
            Log.e("Error", "Function: send_command(); " + "command: "+ command + e.getMessage());
            e.printStackTrace();
        }

        return received_data;

    }

    // Take in a command, a filename which will resolve into a file path in sdcard and download the videofile to it.
    // Return the filepath to the recorded video.
    String send_command_video(String command,String file_name, Context ctx) {

        String filepath = "/sdcard/"+ file_name;
        File sdCard = Environment.getExternalStorageDirectory();
        filepath = filepath.replace("/sdcard", sdCard.getAbsolutePath());

        File videoFile;
        videoFile = new File(filepath);
        System.out.println(filepath);

        /*
        // check if file exist.
        if(videoFile.exists() && !videoFile.isDirectory()) {
            Log.d("Video:","File exists. Loading from there.");
            return filepath;
        }
        */
        try {
            //File video = new File(ctx.getCacheDir(), "cacheFileAppeal.srl"); // cache ?

            OutputStream output = new FileOutputStream(videoFile);

            clientSocket = new Socket(hostname, port);
            Log.d("Connection", clientSocket.getInetAddress() + " connected on port " + clientSocket.getPort());

            int read;
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            byte[] c = command.getBytes();
            clientSocket.getOutputStream().write(c); // send command

                while ((read = clientSocket.getInputStream().read(buffer)) != -1) {
                    output.write(buffer, 0, read); // write to file
                }

            output.flush();
            clientSocket.close();

        }
        catch (Exception e) {
            Log.e("Error", "Function: send_command_video(); " + "command: "+ command + e.getMessage());
            e.printStackTrace();
        }

        return filepath;
    }

    // sepapate method for sending image.
    Bitmap send_command_img(String command) {
        Bitmap mIcon11 = null;
        try {
            //create connection
            clientSocket = new Socket(hostname, port);
            System.out.println(clientSocket.getInetAddress() + " connected on port " + clientSocket.getPort());

            byte[] buffer = command.getBytes();
            clientSocket.getOutputStream().write(buffer); // send command
            mIcon11 = BitmapFactory.decodeStream(clientSocket.getInputStream());

            clientSocket.close();

        } catch (Exception e) {
            Log.e("Error", "Function: send_command_img(); " + "command: "+ command + e.getMessage());
            e.printStackTrace();
        }

        return mIcon11;

    }

    // Not tested. Writefile for android (internal storage)
    private boolean writefile(String data, String file_name, Context ctx) {
        try {

            //FileOutputStream fOut = openFileOutput(file_name,Context.MODE_WORLD_READABLE);
            FileOutputStream fOut = ctx.openFileOutput(file_name,Context.MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
            return true;
        } catch (Exception e) {
            Log.e("Error", "Function: writefile(); " + e.getMessage());
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
            fOut.write(data.getBytes()); // takes byte form of data
            // fOut.getChannel();
            fOut.close();
            return true;
        }catch (Exception e)
        {
            Log.w(TAG, "FileOutputStream exception: - " + e.toString());
            return false;
        }
    }

    public boolean isReady(){
        return (value != null);
    }

    public String get_value(){
        return value;
    }

}


