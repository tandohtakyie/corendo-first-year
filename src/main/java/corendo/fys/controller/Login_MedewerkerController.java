package corendo.fys.controller;

import corendo.fys.jdbcDBconnection;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import corendo.fys.Password;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class Login_MedewerkerController implements Initializable {

    /**
     * Initializes the controller class .
     */
    Connection conn = jdbcDBconnection.ConnectDB();
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Label lblMessage;

    private ResourceBundle resources;

    private static String email;

    @FXML
    void on_Login_medewerker(ActionEvent event) throws IOException {
        Password password = new Password();

        String sql = "Select * from employee where Email=? and Password=?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtEmail.getText());
            pstmt.setString(2, password.getHashedPassword(txtPassword.getText()));
            email = txtEmail.getText();

            rs = pstmt.executeQuery();

            FXMLLoader loader = new FXMLLoader();
            
            if (rs.next()) {
                if (function_id() == 1) {

                    
                    loader.setLocation(getClass().getResource("/corendo/fys/Navigatie_medewerker.fxml"));
                    try {
                        loader.load();

                    } catch (IOException ex) {

                    }

                    Navigatie_medewerkerController displayEmployeeBalieDetails = loader.getController();
                    displayEmployeeBalieDetails.setInfo(medewerkerName(), medewerkerCountry());

                    Parent parent1 = loader.getRoot();
                    Scene scene = new Scene(parent1);
                    Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(scene);
                    app_stage.show();
                } else if (function_id() == 2) {
                    
                    
                    loader.setLocation(getClass().getResource("/corendo/fys/Navigatie_supervisor.fxml"));
                    try {
                        loader.load();

                    } catch (IOException ex) {

                    }

                    Navigatie_supervisorController displayEmployeeSupervisorDetails = loader.getController();
                    displayEmployeeSupervisorDetails.setInfo(medewerkerName(), medewerkerCountry());
                    
                    Parent parent1 = loader.getRoot();
                    Scene scene = new Scene(parent1);
                    Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    app_stage.setScene(scene);
                    app_stage.show();
                    
                    
                    
                }
            } else {
                lblMessage.setVisible(true);
                lblMessage.setText("Invalid Email or Password!");
            }
        } catch (SQLException e) {
            e.getMessage();
        }

    }
// get firstname of employee

    public String medewerkerName() {
        String firstname = null;
        try {
            String query = "select Firstname from employee where Email='" + txtEmail.getText() + "'";
            pstmt = conn.prepareStatement(query);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                firstname = rs.getString("Firstname");
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return firstname;
    }

    public String medewerkerCountry() {
        String country = null;
        String query = "SELECT Country_name FROM employee INNER JOIN country ON employee.Country_id = country.Country_id WHERE Email='" + txtEmail.getText() + "'";

        try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                country = rs.getString("Country_name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login_MedewerkerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return country;
    }

    public String getEmail() {
        return email;
    }

    public int function_id() {
        int id = 0;
        try {
            String query = "select Function_id from employee where Email='" + txtEmail.getText() + "'";
            pstmt = conn.prepareStatement(query);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("Function_id");
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
