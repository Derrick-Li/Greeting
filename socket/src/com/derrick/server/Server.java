package com.derrick.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {
	private static Connection conn = null;
	
	public Server(){
		System.out.println("Server is running !");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/snjxc", "root", "root");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class ServerDemo extends Thread{

		Socket socket;
		BufferedReader bfer;
		PrintWriter out;
		
		private Statement prestat = null;
		private ResultSet rs = null;
		
		public ServerDemo(Socket s){
			this.socket = s;
		}
		@Override
		public void run() {
			System.out.println("Run now!");
			try{
				InputStreamReader in = new InputStreamReader(socket.getInputStream());
				bfer = new BufferedReader(in);
				
				String str = bfer.readLine();
				System.out.println("the message is " + str);
				if(null != str){
					prestat = conn.createStatement();
					rs = prestat.executeQuery("select * from tb_user");
					if(rs.next()){
						String username = rs.getString("username");
						System.out.println(username);
					}
				}
				out = new PrintWriter(socket.getOutputStream(),true);
				out.print("message recieved");
				out.flush();
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try {
					out.close();
					bfer.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	@SuppressWarnings("resource")
	public static void main(String[] args){
		ServerSocket ss;
		Server server;
		try {
			ss = new ServerSocket(7501);
			server = new Server();
			while(true){
				try{
					Socket socket = ss.accept();
					server.new ServerDemo(socket).start();
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
