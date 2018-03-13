package com.codebind;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.datatransfer.*;
import java.awt.Toolkit;


import java.sql.*;


public class AppDetail {
    private JTextField studentNameTextField;
    private JPanel panelMain;
    private JButton btnRecordBehavior;
    private JList behaviorList;
    private JButton btnRemoveLastBehavior;
    private JList selectBehaviorList;


    public AppDetail(Integer StudentNo, String classID) {


        studentNameTextField.setText(getStudentName(StudentNo, classID));
        updateBehaviorList(StudentNo, classID);
        populateBehaviorSelection();


        btnRecordBehavior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Integer behaviorID = (Integer)((JButton)e.getSource()).getClientProperty( "behaviorID" );
                logBehavior(classID,StudentNo,getDateTime(),getBehaviorDesc(behaviorID));


            System.out.println(behaviorID);

            }
        });
        btnRemoveLastBehavior.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeLastBehavior(StudentNo, classID);
            }
        });
        selectBehaviorList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                btnRecordBehavior.setText(selectBehaviorList.getSelectedValue().toString());
               // System.out.println(selectBehaviorList.getSelectedIndex());
                btnRecordBehavior.putClientProperty("behaviorID",selectBehaviorList.getSelectedIndex());
            }
        });
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

    // Refresh behavior list from SQL Database. Generate Clipboard Data

    private void updateBehaviorList(Integer studentNo, String classID ) {

        String sql = "Select dateTime, behavior FROM StudentBehavior WHERE studentNo = ? and classID = ? ORDER BY epoch DESC";
        String clipboardString = "";

        clipboardString += "TO:      " + getStudentName(studentNo, classID) + "(" + getStudentEmail(studentNo, classID)+ ")\n";
        clipboardString += "FROM:    Mr. D\'Ausilio (adausilio@myimmaculatehs.org)\n";
        clipboardString += "SUBJECT: Notification of Disruptive Behavior and/or repeated violations of school policy\n";
        clipboardString += "";
        clipboardString += "CC:      Mr. DeMaida (ihsdean@myimmaculatehs.org)\n\n";

        clipboardString += "This notice is to inform you that you have either repeatedly disrupted the class and/or violated school policy. I am asking that you please consider your behavior, and kindly respond to this email to let me know how you plan to address this problem.\n\n";
        clipboardString += "Behavior Log\n\n";




        DefaultListModel listModel;
        listModel = new DefaultListModel();

        try ( Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,studentNo);
            pstmt.setString(2,classID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                listModel.addElement(rs.getString("dateTime") + " " + rs.getString("behavior"));
                clipboardString += rs.getString("dateTime") + " " + rs.getString("behavior") + "\n";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        behaviorList.setModel(listModel);

        // Push to clipboard
        StringSelection stringSelection = new StringSelection(clipboardString);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection,null);


    }
    // Refresh behavior list from SQL Database. Generate Clipboard Data

    private void populateBehaviorSelection() {

        String sql = "Select behaviorLabel FROM BehaviorTable ORDER BY behaviorID ASC";

        DefaultListModel listModel2;
        listModel2 = new DefaultListModel();

        try ( Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                listModel2.addElement(rs.getString("behaviorLabel"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        selectBehaviorList.setModel(listModel2);

    }

    private String getDateTime() {

        //Get current date time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLL-dd hh:mma");
        return now.format(formatter);

    }

    private void logBehavior(String classID,Integer studentNo,String dateTime, String behavior ) {
        String sql = "INSERT INTO StudentBehavior(classID,studentNo,dateTime,behavior,epoch) VALUES(?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            Long epochL = java.time.Instant.now().getEpochSecond();
            String epoch = Long.toString(epochL);

            pstmt.setString(1, classID);
            pstmt.setInt(2, studentNo);
            pstmt.setString(3, dateTime);
            pstmt.setString(4,behavior);
            pstmt.setString(5,epoch);
            pstmt.executeUpdate();

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        updateBehaviorList(studentNo, classID);
    }

    private void removeLastBehavior(Integer studentNo, String classID) {
        String sql = "delete from StudentBehavior where epoch IN (SELECT b.epoch FROM StudentBehavior b WHERE b.studentNo = ? and b.classID = ? ORDER BY b.epoch desc Limit 1) AND studentNo = ? and classID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentNo);
            pstmt.setString(2,classID);
            pstmt.setInt(3, studentNo);
            pstmt.setString(4,classID);
            pstmt.executeUpdate();

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        updateBehaviorList(studentNo, classID);
    }

    public String getBehaviorDesc(Integer behaviorID) {

        String sql = "Select behaviorDesc FROM BehaviorTable WHERE behaviorID = ?";
        String behaviorDesc = "";

        try ( Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,behaviorID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                behaviorDesc = rs.getString("behaviorDesc");
            }} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return behaviorDesc;
    }



    public String getStudentName(Integer studentNo, String classID) {

        String sql = "Select studentName FROM StudentTable WHERE studentNo = ? AND classID = ?";
        String studentName = "";

        try ( Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,studentNo);
            pstmt.setString(2,classID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                studentName = rs.getString("studentName");
            }} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    return studentName;
    }

    public String getStudentEmail(Integer studentNo, String classID) {

        String sql = "Select email FROM StudentTable WHERE studentNo = ? AND classID = ?";
        String studentEmail = "";

        try ( Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,studentNo);
            pstmt.setString(2,classID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                studentEmail = rs.getString("email");
            }} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return studentEmail;
    }


    public void studentForm(Integer studentNo, String classID) {

        JFrame frame = new JFrame("AppDetail");
        frame.setContentPane(new AppDetail(studentNo, classID).panelMain);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200,500));
        frame.pack();
        frame.setVisible(true);
    }
}


