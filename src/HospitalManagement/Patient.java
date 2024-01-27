package HospitalManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient{

    //connection to the database
    private Connection connection;

    //scanner to take input from the user
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        //initialize the connection and scanner within constructor
        this.connection = connection;
        this.scanner = scanner;
    }

    //method to add a new patient
    public void addPatient(){
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();

        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();

        System.out.print("Enter patient Gender: ");
        String gender = scanner.next();


        try{
            //query to insert data into the database
            String query = "INSERT INTO patient(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement PreparedStatement = connection.prepareStatement(query);  
            
            //set the values to the query
            PreparedStatement.setString(1, name);
            PreparedStatement.setInt(2, age);
            PreparedStatement.setString(3, gender); 

            //execute the query
            int affectedRows = PreparedStatement.executeUpdate();

            //check if the query is executed successfully
            if(affectedRows > 0){
                System.out.println("Patient added successfully");
            }else{
                System.out.println("Error: Patient not added");
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //method to update patient details
    
    public void viewPatient(){
        String query = "select * from patients";

        //Handling the exception
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            //execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            //display the result
            System.out.println("Patients:");
            System.out.println("ID\t    Name\t      Age\t    Gender");

            //loop through the result set
            while(resultSet.next()){
                //get the values from the result set
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

        //Get the patient by id
    public boolean getPatientById(int id) {
            // code goes here
        
                String query = "select * from patients where id = ?";

                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, id);
    
                    //execute the query
                    ResultSet resultSet = preparedStatement.executeQuery();
    
                    //check if the patient exists
                    if(resultSet.next()){
                        return true;
                    }else{
                        return false;
                    }
                }catch(SQLException e){
                    System.out.println("Error: " + e.getMessage());
                }
                return false;
    }    
}