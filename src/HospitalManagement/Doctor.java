package HospitalManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Doctor {
    //connection to the database
    private Connection connection;

  

    public Doctor(Connection connection){
        //initialize the connection and scanner within constructor
        this.connection = connection;
        
    }

    
    //method to update patient details
    
    public void viewDoctors(){
        String query = "select * from doctors";

        //Handling the exception
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            //execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            //display the result
            System.out.println("Doctors: ");
            System.out.println("Doctor_ID\t    Name\t      Specialization\t  ");

            //loop through the result set
            while(resultSet.next()){
                //get the values from the result set
                int id = resultSet.getInt("doctor_id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
            }
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

        //Get the doctor by id
    public boolean getDoctorById(int id) {
            // code goes here
        
                String query = "select * from doctors where id = ?";

                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, id);
    
                    //execute the query
                    ResultSet resultSet = preparedStatement.executeQuery();
    
                    //check if the doctor exists
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
