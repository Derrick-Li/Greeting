package com.derrick.server;

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
		InputStreamReader in;
		PrintWriter out;
		String username;
		
		private Statement prestat = null;
		private ResultSet rs = null;
		
		public ServerDemo(Socket s){
			this.socket = s;
		}
		@SuppressWarnings("unused")
		@Override
		public void run() {
			System.out.println("Run now!");
			char[] bytes = new char[64];
			try{
				in = new InputStreamReader(socket.getInputStream());
				int len = 0;
				StringBuffer sbr = new StringBuffer();
				
				while((len = in.read(bytes))!= -1){
					sbr.append(bytes);
					if(sbr.toString().lastIndexOf("===END===")>0) break;
				}
				String str = sbr.toString();
				if(null != str){
					str = str.substring(str.lastIndexOf("===BEGIN===")+11, str.lastIndexOf("===END==="));
				}
				System.out.println(str);
				if(null != str){
					prestat = conn.createStatement();
					rs = prestat.executeQuery("select * from tb_user");
					if(rs.next()){
						username = rs.getString("username");
					}
				}
				out = new PrintWriter(socket.getOutputStream(),true);
				out.print(Server.class.getSimpleName()+" : " + username);
				out.flush();
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try {
					in.close();
					out.close();
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
