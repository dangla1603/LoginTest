package com.example.logintest;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import android.content.Context;
import android.os.Environment;

import java.io.OutputStream;
import java.net.Socket;

import android.widget.TextView;

import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import android.net.Uri;

import static android.content.ContentValues.TAG;


public class TCPClient extends AsyncTask<Object, Void, Object> {
    // I dont know if this class can function in here or just use assynTask to connect straight to python code
    private String hostname = "localhost";
    private int port = 0;

    private Socket clientSocket;

    private String value = null;
    private String command = "-1";


    //private ImageView img;
    private Context context;

    //constructor
    public TCPClient(String hostname, int port, Context ctx) {
        this.hostname = hostname;
        this.port = port;
        this.context = ctx;
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
            byte[] buffer = command.getBytes();
            clientSocket.getOutputStream().write(buffer); // send command

            while ((c = clientSocket.getInputStream().read()) != -1) {
                received_data += (char) c;
            }
            clientSocket.close();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return received_data;

    }

    // take in command, a filename which will resolve into a file path in sdcard and download the videofile to it
    // return the filepath to the recorded video.
    public String send_command_video(String command,String file_name, Context ctx) {

        String filepath = "/sdcard/"+ file_name;
        File sdCard = Environment.getExternalStorageDirectory();
        //filepath = filepath.replace("/sdcard", sdCard.getAbsolutePath());

        File videoFile;
        videoFile = new File(filepath);
        System.out.println(filepath);
        // check if file exist.
        /*
        if(videoFile.exists() && !videoFile.isDirectory()) {
            Log.d("Video:","File exists. Loading from there.");
            return filepath;
        }
        */
        try {
            //File video = new File(ctx.getCacheDir(), "cacheFileAppeal.srl");

            OutputStream output = new FileOutputStream(videoFile);

            Socket client = new Socket(hostname, port);
            Log.d("Connection", client.getInetAddress() + " connected on port " + client.getPort());

            int read;
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            byte[] c = command.getBytes();
            client.getOutputStream().write(c); // send command

                while ((read = client.getInputStream().read(buffer)) != -1) {
                    output.write(buffer, 0, read); // write to file
                }

            output.flush();
            client.close();

        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return filepath;
    }

    // sepapate method for sending image. Will be merged
    public Bitmap send_command_img(String command) {
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
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return mIcon11;

    }

    @Override
    protected Object doInBackground(Object... voids) {

        //this.context = (Activity)voids[1];
        Object result = null;
        command = (String) voids[0];
        System.out.println(command);
        if (command.equals("2") || command.equals("1") || command.equals("7") ){
            // received data is string value
            result = send_command(command);
            value = (String)result;
        }
        if (command.equals("6")){
            //received data is video
            String filename = (String)voids[1];
            result = send_command_video(command,"turtle.mp4",context);
        }
        if (command.equals("5")){
            // received data is bitmap
            System.out.println("bitmap!");
        }
        if (command.equals("16") ){
            //received data is video. (get request)
            String filename = (String)voids[1];
            String REQUEST = "GET /videos/"+filename;
            result = send_command_video(REQUEST, filename,context);
        }

        // handle get request
        if (command.length()> 3){
            // get request
            if (command.substring(0,3).equals("GET") ) {
                String filename = (String) voids[1];
                result = send_command_video(command, filename, context);
            }

        }

        return result;
    }

    protected void onPostExecute(Object result) {
        if (command.equals("2") ){
            TextView txtView = (TextView) ((Activity)context).findViewById(R.id.editText);
            txtView.setText((String)result);
        }
        if (command.equals("7") ){
            // render list
            ArrayList<String> videoList = new ArrayList<>();
            String[] video_elements = ((String)result).split(" ");
            for (String e : video_elements){
                videoList.add(e);
            }
            ArrayAdapter adapter;
            adapter = new ArrayAdapter(((Activity)context),android.R.layout.simple_list_item_1,videoList);
            ListView listView = (ListView) ((Activity)context).findViewById(R.id.lvideo);
            listView.setAdapter(adapter);
        }
        if (command.equals("6") ){
            // prepare uri for videoview
            String filepath = (String) result;
            VideoView videoView = ((Activity)context).findViewById(R.id.videoview);
            videoView.setVideoPath(filepath);
        }
        if (command.equals("16") ){
            // prepare uri for videoview
            String filepath = (String) result;
            System.out.println("preparing:"+ filepath);
            VideoView videoView = ((Activity)context).findViewById(R.id.videoview);
            videoView.setVideoPath(filepath);
        }

        // handle get request
        if (command.length()> 3){
            // get request
            if (command.substring(0,3).equals("GET") ) {
                String filepath = (String) result;
                VideoView videoView = ((Activity)context).findViewById(R.id.videoview);
                videoView.setVideoPath(filepath);
            }

        }


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

    public boolean isReady(){
        return (value != null);
    }

    public String get_value(){
        return value;
    }

}


