package com.example.rob.lernapp;


import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;


public class SocketHandler{

    private SocketHandler() { }

    private static SocketHandler instance;

    private static Socket learnappSocket;
    private static final String URL = "http://learnapp.enif.uberspace.de:61042/";

    public static SocketHandler getInstance () {
        if (SocketHandler.instance == null) {
            SocketHandler.instance = new SocketHandler ();
            try {
                if(learnappSocket != null){
                    learnappSocket.disconnect();
                }

                learnappSocket = IO.socket(URL);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return SocketHandler.instance;
    }

    public Socket getlearnappSocket(){
        return learnappSocket;
    }


}
