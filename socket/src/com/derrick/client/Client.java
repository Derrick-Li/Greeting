package com.derrick.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public Client(){
		
		Socket socket;
		BufferedReader bfer;
		PrintWriter out;
		System.out.println("Starting....");
		try{
			socket = new Socket("127.0.0.1",7501);
			out = new PrintWriter(socket.getOutputStream());
			bfer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//send the request 
			out.println("the first message\r\n ");
			out.print("the second message\r\n");
			out.flush();
			
			String str = bfer.readLine();
			System.out.println("the message return from server :");
			System.out.println(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		new Client();
	}
}
