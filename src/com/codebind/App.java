package com.codebind;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.time.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.codebind.AppDetail;


public class App {
    private JPanel panelMain;
    private JButton btn_32;
    private JButton btn_30;
    private JButton btn_22;
    private JButton btn_20;
    private JButton btn_03;
    private JButton btn_11;
    private JButton btn_21;
    private JButton btn_31;
    private JButton btn_02;
    public JButton btn_00;
    private JButton btn_10;
    private JButton btn_01;
    private JButton btn_12;
    private JButton btn_33;
    private JButton btn_40;
    private JButton btn_41;
    private JButton btn_42;
    private JButton btn_43;
    private JButton btn_50;
    private JButton btn_51;
    private JButton btn_52;
    private JButton btn_53;
    private JButton btn_13;
    private JButton btn_23;
    private JComboBox comboBox1;
    private JCheckBox echoServerCheckBox;
    private JRadioButton radE;


    public static void main(String[] args) {

        //Get current date time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);

        new Thread(new Runnable() {
            public void run() {
                try {
                    new App().startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();




        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().panelMain);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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

    public void bubbaButton(){
        btn_00.setBackground(Color.magenta);
   }



    public void insertStudent(String classID,Integer studentNo,Integer seatNo, String studentName) {
            String sql = "INSERT INTO StudentTable(studentNo,seatNo,studentName,classID) VALUES(?,?,?,?)";
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, studentNo);
                pstmt.setInt(2, seatNo);
                pstmt.setString(3, studentName);
                pstmt.setString(4,classID);
                pstmt.executeUpdate();

                System.out.printf("%d %d %s\n", studentNo, seatNo, studentName);
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }



    public void populateStudentTableFromCSV() {

        try {
            Scanner x = new Scanner(new File("c:\\sqlite3\\e.txt"));
             while (x.hasNext()) {
                 String a = x.nextLine();

                 String[] parts = a.split(",");

                 String studentName = parts[2];
                 Integer studentNo = Integer.parseInt(parts[0]);
                 Integer seatNo = Integer.parseInt(parts[1]);

                 insertStudent("E",studentNo,seatNo,studentName);
             }

                x.close();

        }
        catch (Exception e)
        {
            System.out.println("Exception");
            System.out.println(e.getMessage());
        }

    }


    public void studentDetail(Integer studentNo, String classID) {
        //JOptionPane.showMessageDialog(null, fullName );
        new AppDetail(studentNo,classID).studentForm(studentNo,classID);

    }

    public void selectAll() {

        String sql = "Select firstName, lastName, studentNo FROM Students";

        try ( Connection conn = this.connect();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(rs.getString("firstName"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void selectStudent(Integer PstudentNo) {

        String sql = "Select firstName, lastName, studentNo FROM Students WHERE studentNo = ?";

        try ( Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,PstudentNo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("firstName"));
            }} catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public void startServer() throws IOException {
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

    public App() {




      //  bubbaButton();

        ArrayList<Integer> studentNoArray = new ArrayList<Integer>(30);

        studentNoArray = setButtonNames("E");


        btn_00.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_02.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_03.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });


        btn_10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_13.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });


        btn_20.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_21.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_22.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_23.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });


        btn_30.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_31.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_32.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_33.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });


        btn_40.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_41.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_42.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_43.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });


        btn_50.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_51.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_52.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        btn_53.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentName = (String) ((JButton) e.getSource()).getClientProperty("studentName");
                Integer studentNo = (Integer) ((JButton) e.getSource()).getClientProperty("studentNo");
                Integer seatNo = (Integer) ((JButton) e.getSource()).getClientProperty("seatNo");
                String classID = (String) ((JButton) e.getSource()).getClientProperty("classID");

                studentDetail(studentNo, classID);
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox combo1 = (JComboBox) e.getSource();
                System.out.println(combo1.getSelectedItem().toString());
                setButtonNames(combo1.getSelectedItem().toString());
            }
        });

        echoServerCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int portNumber = 4444;

                System.out.println("Bubba is listening");

                try (
                        ServerSocket serverSocket =
                                new ServerSocket(Integer.parseInt("4444"));
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out =
                                new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        out.println("Bubba " + inputLine);
                        System.out.println(inputLine);
                    }
                } catch (IOException ee) {
                    System.out.println("Exception caught when trying to listen on port "
                            + portNumber + " or listening for a connection");
                    System.out.println(ee.getMessage());
                }
            }
        });
    }


    public ArrayList<Integer> setButtonNames(String pClassID){

        ArrayList<String> studentNamesArray = new ArrayList<String>(30);
        ArrayList<Integer> studentNoArray = new ArrayList<Integer>(30);
        ArrayList<Integer> studentSeatNoArray = new ArrayList<Integer>(30);

        String sql = "Select studentNo,seatNo,studentName,classID FROM StudentTable WHERE classID = \"" + pClassID + "\" ORDER BY seatNo ASC ";

        try ( Connection conn = this.connect();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                studentNamesArray.add(rs.getString("studentName"));
                studentNoArray.add(rs.getInt("studentNo"));
                studentSeatNoArray.add(rs.getInt("seatNo"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        btn_00.setText(studentNamesArray.get(0));
        btn_00.putClientProperty("studentName",studentNamesArray.get(0));
        btn_00.putClientProperty("studentNo",studentNoArray.get(0));
        btn_00.putClientProperty("seatNo",studentSeatNoArray.get(0));
        btn_00.putClientProperty("classID",pClassID);

        btn_01.setText(studentNamesArray.get(1));
        btn_01.putClientProperty("studentName",studentNamesArray.get(1));
        btn_01.putClientProperty("studentNo",studentNoArray.get(1));
        btn_01.putClientProperty("seatNo",studentSeatNoArray.get(1));
        btn_01.putClientProperty("classID",pClassID);

        btn_02.setText(studentNamesArray.get(2));
        btn_02.putClientProperty("studentName",studentNamesArray.get(2));
        btn_02.putClientProperty("studentNo",studentNoArray.get(2));
        btn_02.putClientProperty("seatNo",studentSeatNoArray.get(2));
        btn_02.putClientProperty("classID",pClassID);

        btn_03.setText(studentNamesArray.get(3));
        btn_03.putClientProperty("studentName",studentNamesArray.get(3));
        btn_03.putClientProperty("studentNo",studentNoArray.get(3));
        btn_03.putClientProperty("seatNo",studentSeatNoArray.get(3));
        btn_03.putClientProperty("classID",pClassID);


        btn_10.setText(studentNamesArray.get(4));
        btn_10.putClientProperty("studentName",studentNamesArray.get(4));
        btn_10.putClientProperty("studentNo",studentNoArray.get(4));
        btn_10.putClientProperty("seatNo",studentSeatNoArray.get(4));
        btn_10.putClientProperty("classID",pClassID);

        btn_11.setText(studentNamesArray.get(5));
        btn_11.putClientProperty("studentName",studentNamesArray.get(5));
        btn_11.putClientProperty("studentNo",studentNoArray.get(5));
        btn_11.putClientProperty("seatNo",studentSeatNoArray.get(5));
        btn_11.putClientProperty("classID",pClassID);

        btn_12.setText(studentNamesArray.get(6));
        btn_12.putClientProperty("studentName",studentNamesArray.get(6));
        btn_12.putClientProperty("studentNo",studentNoArray.get(6));
        btn_12.putClientProperty("seatNo",studentSeatNoArray.get(6));
        btn_12.putClientProperty("classID",pClassID);

        btn_13.setText(studentNamesArray.get(7));
        btn_13.putClientProperty("studentName",studentNamesArray.get(7));
        btn_13.putClientProperty("studentNo",studentNoArray.get(7));
        btn_13.putClientProperty("seatNo",studentSeatNoArray.get(7));
        btn_13.putClientProperty("classID",pClassID);



        btn_20.setText(studentNamesArray.get(8));
        btn_20.putClientProperty("studentName",studentNamesArray.get(8));
        btn_20.putClientProperty("studentNo",studentNoArray.get(8));
        btn_20.putClientProperty("seatNo",studentSeatNoArray.get(8));
        btn_20.putClientProperty("classID",pClassID);

        btn_21.setText(studentNamesArray.get(9));
        btn_21.putClientProperty("studentName",studentNamesArray.get(9));
        btn_21.putClientProperty("studentNo",studentNoArray.get(9));
        btn_21.putClientProperty("seatNo",studentSeatNoArray.get(9));
        btn_21.putClientProperty("classID",pClassID);

        btn_22.setText(studentNamesArray.get(10));
        btn_22.putClientProperty("studentName",studentNamesArray.get(10));
        btn_22.putClientProperty("studentNo",studentNoArray.get(10));
        btn_22.putClientProperty("seatNo",studentSeatNoArray.get(10));
        btn_22.putClientProperty("classID",pClassID);

        btn_23.setText(studentNamesArray.get(11));
        btn_23.putClientProperty("studentName",studentNamesArray.get(11));
        btn_23.putClientProperty("studentNo",studentNoArray.get(11));
        btn_23.putClientProperty("seatNo",studentSeatNoArray.get(11));
        btn_23.putClientProperty("classID",pClassID);



        btn_30.setText(studentNamesArray.get(12));
        btn_30.putClientProperty("studentName",studentNamesArray.get(12));
        btn_30.putClientProperty("studentNo",studentNoArray.get(12));
        btn_30.putClientProperty("seatNo",studentSeatNoArray.get(12));
        btn_30.putClientProperty("classID",pClassID);

        btn_31.setText(studentNamesArray.get(13));
        btn_31.putClientProperty("studentName",studentNamesArray.get(13));
        btn_31.putClientProperty("studentNo",studentNoArray.get(13));
        btn_31.putClientProperty("seatNo",studentSeatNoArray.get(13));
        btn_31.putClientProperty("classID",pClassID);

        btn_32.setText(studentNamesArray.get(14));
        btn_32.putClientProperty("studentName",studentNamesArray.get(14));
        btn_32.putClientProperty("studentNo",studentNoArray.get(14));
        btn_32.putClientProperty("seatNo",studentSeatNoArray.get(14));
        btn_32.putClientProperty("classID",pClassID);

        btn_33.setText(studentNamesArray.get(15));
        btn_33.putClientProperty("studentName",studentNamesArray.get(15));
        btn_33.putClientProperty("studentNo",studentNoArray.get(15));
        btn_33.putClientProperty("seatNo",studentSeatNoArray.get(15));
        btn_33.putClientProperty("classID",pClassID);



        btn_40.setText(studentNamesArray.get(16));
        btn_40.putClientProperty("studentName",studentNamesArray.get(16));
        btn_40.putClientProperty("studentNo",studentNoArray.get(16));
        btn_40.putClientProperty("seatNo",studentSeatNoArray.get(16));
        btn_40.putClientProperty("classID",pClassID);

        btn_41.setText(studentNamesArray.get(17));
        btn_41.putClientProperty("studentName",studentNamesArray.get(17));
        btn_41.putClientProperty("studentNo",studentNoArray.get(17));
        btn_41.putClientProperty("seatNo",studentSeatNoArray.get(17));
        btn_41.putClientProperty("classID",pClassID);

        btn_42.setText(studentNamesArray.get(18));
        btn_42.putClientProperty("studentName",studentNamesArray.get(18));
        btn_42.putClientProperty("studentNo",studentNoArray.get(18));
        btn_42.putClientProperty("seatNo",studentSeatNoArray.get(18));
        btn_42.putClientProperty("classID",pClassID);

        btn_43.setText(studentNamesArray.get(19));
        btn_43.putClientProperty("studentName",studentNamesArray.get(19));
        btn_43.putClientProperty("studentNo",studentNoArray.get(19));
        btn_43.putClientProperty("seatNo",studentSeatNoArray.get(19));
        btn_43.putClientProperty("classID",pClassID);



        btn_50.setText(studentNamesArray.get(20));
        btn_50.putClientProperty("studentName",studentNamesArray.get(20));
        btn_50.putClientProperty("studentNo",studentNoArray.get(20));
        btn_50.putClientProperty("seatNo",studentSeatNoArray.get(20));
        btn_50.putClientProperty("classID",pClassID);

        btn_51.setText(studentNamesArray.get(21));
        btn_51.putClientProperty("studentName",studentNamesArray.get(21));
        btn_51.putClientProperty("studentNo",studentNoArray.get(21));
        btn_51.putClientProperty("seatNo",studentSeatNoArray.get(21));
        btn_51.putClientProperty("classID",pClassID);


        btn_52.setText(studentNamesArray.get(22));
        btn_52.putClientProperty("studentName",studentNamesArray.get(22));
        btn_52.putClientProperty("studentNo",studentNoArray.get(22));
        btn_52.putClientProperty("seatNo",studentSeatNoArray.get(22));
        btn_52.putClientProperty("classID",pClassID);

        btn_53.setText(studentNamesArray.get(23));
        btn_53.putClientProperty("studentName",studentNamesArray.get(23));
        btn_53.putClientProperty("studentNo",studentNoArray.get(23));
        btn_53.putClientProperty("seatNo",studentSeatNoArray.get(23));
        btn_53.putClientProperty("classID",pClassID);


    return studentNoArray;
    }

}
