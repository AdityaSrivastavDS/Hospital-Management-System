package HospitalManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "enter url of your MySql workbench here, for example: jdbc:mysql://localhost:3306/hospita";
    private static final String username = "enter your username here";
    private static final String password = "enter your MySql password here";

    public static void main(String[] args) {
        try{
            //load the driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        //create the connection
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            
            //create the objects
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");

                System.out.println("1. Patient");
                System.out.println("2. View patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch(choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                    default:
                        System.out.println("Invalid choice");
                }
            }
        
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient's ID: ");
        int id = scanner.nextInt();

        System.out.print("Enter Doctor's ID: ");
        int doctorId = scanner.nextInt();

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(id) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
               String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appontment_date) VALUES(?, ?, ?)";

               try{
                  PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                  preparedStatement.setInt(1, id);
                  preparedStatement.setInt(2, doctorId);
                  preparedStatement.setString(3, appointmentDate);

                  int affectedRows = preparedStatement.executeUpdate();
                  if(affectedRows > 0){
                      System.out.println("Appointment booked successfully");
                    }else{
                        System.out.println("Error: Appointment not booked");
                    }
               }catch(SQLException e){
                   System.out.println("Error: " + e.getMessage());
               }
            }else{
                System.out.println("Doctor is not available on this date");
            }
        }else{
            System.out.println("Either patient or doctor does not exist");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
          String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";

          try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);

            //execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()){
                //get the count of rows
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }
            }
          }catch(SQLException e){
              System.out.println("Error: " + e.getMessage());
          }
          return false;
    }
}
