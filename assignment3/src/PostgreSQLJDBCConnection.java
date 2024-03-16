package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet; 
import java.util.Date; 

public class PostgreSQLJDBCConnection {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/assignment3";
        String user = "postgres";
        String password = "urbA493s";

        try { 
            Class.forName("org.postgresql.Driver"); // Load PostgreSQL JDBC Driver
            Connection conn = DriverManager.getConnection(url, user, password); //Connect to the database

            if(conn != null) {
                System.out.println("Connected to PostgreSQL successfully!");

                getAllStudents(conn);

                //addStudent("Adilet" , "Ishenbekov" , "ai@cmail.carleton.ca" , new Date() , conn);
                // System.out.println("ADDED STUDENT");
                // getAllStudents(conn); //Test if Student got added

                //updateStudentEmail(4 , "adiletishenbekov2004@gmail.com" , conn);
                // System.out.println("UPDATED STUDENT");
                // getAllStudents(conn); //Test if Student got updated

                getAllStudents(conn);
            } 
            else {
                System.out.println("Failed to establish connection.");
            } 

            conn.close();

        } catch(ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAllStudents(Connection conn) {
        String retrieveQuery = "SELECT * FROM Students";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(retrieveQuery);

            while(rs.next()) {
                // Extract student information from ResultSet and print

                int studentId = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                Date enrollmentDate = rs.getDate("enrollment_date");

                System.out.println("Student ID: " + studentId);
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Enrollment Date: " + enrollmentDate);
                System.out.println("----------------------------------");
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addStudent(String first_name, String last_name, String email, Date enrollment_date, Connection conn) {
        String addQuery = "INSERT INTO Students(first_name , last_name , email , enrollment_date) VALUES (? , ? , ? , ?)";

        try(PreparedStatement pstmt = conn.prepareStatement(addQuery)){
            java.sql.Date sqlEnrollmentDate = new java.sql.Date(enrollment_date.getTime()); //Convert from "java util Date" object to a "java SQL date" object

            // Set parameters for the PreparedStatement - changes the (? ? ? ?) to the set values below
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setDate(4, sqlEnrollmentDate);

            pstmt.executeUpdate(); //FOR ALL DATA MANIPULATION SUCH AS INSERT, DELETE, UPDATe, etc need to call executeUpdate()
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStudentEmail(int student_id, String new_email , Connection conn) {
        String updateQuery = "UPDATE Students SET email = ? WHERE student_id = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(updateQuery)){
            // Set parameters for the PreparedStatement - changes the (? ?) to the set values below
            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);

            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int student_id , Connection conn) {
        String deleteQuery = "DELETE FROM Students WHERE student_id = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(deleteQuery)){
            pstmt.setInt(1, student_id);

            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}