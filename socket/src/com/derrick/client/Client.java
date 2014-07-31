package com.derrick.client;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public Client(){
		
		Socket socket;
		PrintWriter out;
		InputStreamReader in ;
		System.out.println("Starting....");
		try{
			socket = new Socket("127.0.0.1",7501);
			out = new PrintWriter(socket.getOutputStream());
			StringBuffer sbr = new StringBuffer("===BEGIN===");
			//Append the message you want to send here
			sbr.append("Client : hello server , can you give me a reply ?");
			sbr.append("===END===");
			//Send the request 
			out.print(sbr.toString());
			out.flush();
			
			//Receive message from server
			in = new InputStreamReader(socket.getInputStream());
			StringBuffer response = new StringBuffer();
			@SuppressWarnings("unused")
			int len = 0;
			char[] c = new char[1];
			while((len = in.read(c)) != -1){
				response.append(c);
			}
			String str = response.toString();
			System.out.println(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		new Client();
	}
}
