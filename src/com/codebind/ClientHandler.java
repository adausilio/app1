package com.codebind;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.Date;


public class ClientHandler extends Thread {

    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;


    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true)
        {
            try {



                // Ask user what he wants
                dos.writeUTF("Please Enter Input (e.g.RSP;E;6;(0,22)..\n"+
                        "Type Exit to terminate connection.");

                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                } else
                {
                    List<String> list = new ArrayList<String>(Arrays.asList(received.split(";")));

                    // Example Input   RSP;E;5;(6,7)

                    //System.out.println(list.get(0));  // RSP
                    //System.out.println(list.get(1));  // E or H
                    //System.out.println(list.get(2));  // StudentNo
                    //System.out.println(list.get(3));  // Student Response

                    String classID = list.get(1);
                    Integer studentNo = Integer.parseInt(list.get(2));
                    String response = list.get(3);

                    String correctAnswer = getCorrectAnswer(classID,studentNo);
                    String completed;

                    if(response.equals(correctAnswer)){
                        completed = "Yes";
                    } else
                        completed = "No";


                    updateResponse(classID,studentNo,response, completed);

                    if (response.equals(correctAnswer)) {
                        toreturn = "Answer is Correct!";
                        dos.writeUTF(toreturn);
                    } else
                    {
                        toreturn = "Incorrect, Please Try Again";
                        dos.writeUTF(toreturn);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private Connection connect() {
        // SQLite connection String
        String url = "jdbc:sqlite:C://sqlite3//test1.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public void updateResponse(String classID,Integer studentNo,String responseAnswer, String completed) {
        String sql = "UPDATE StudentResponse Set ResponseAnswer = ?, Attempts = Attempts + 1, Completed = ? WHERE studentNo = ? AND classID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, responseAnswer);
            pstmt.setString(2,completed);
            pstmt.setInt(3, studentNo);
            pstmt.setString(4, classID);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getCorrectAnswer(String classID, Integer studentNo) {

        String sql = "Select CorrectAnswer FROM StudentResponse WHERE studentNo = ? AND classID = ?";
        String answer = "";


        try ( Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,studentNo);
            pstmt.setString(2,classID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                answer = rs.getString("CorrectAnswer");
                //System.out.println(rs.getString("firstName"));
            }} catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return answer;
    }


}
