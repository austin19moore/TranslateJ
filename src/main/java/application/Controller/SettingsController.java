package application.Controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SettingsController implements Initializable {

    @FXML
    javafx.scene.control.TextField projectIDField;

    @FXML
    javafx.scene.control.TextField deeplKeyField;

    @FXML
    javafx.scene.control.TextField sourceField;

    @FXML
    javafx.scene.control.TextField targetField;

    private static String tLanguage;
    private static String sLanguage;
    private static String projectID;
    private static String authKey = "bc532c7c-dcda-6754-3458-3758bdf0ede6:fx";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            getSettings();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        projectIDField.setText(getProjectID());
        deeplKeyField.setText(getAuthKey());
        targetField.setText(getTargetLanguage());
        sourceField.setText(getSourceLanguage());

    }

    public void backButton(ActionEvent arg0) throws Exception {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
            System.out.println("Switching user to Main view");
            Main.stage.setScene( new Scene(root, 800, 400) );
            Main.stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void getSettings() throws FileNotFoundException {

        File f = new File("src/main/resources/data/savedCred.TXT");
        Scanner scan = new Scanner(f);

        if (!scan.hasNextLine()) {
            System.out.println("No file, defaulting...");
            setSourceLanguage("en-US");
            setTargetLanguage("en-US");
            return;
        } else {

            setProjectID(scan.nextLine());
            setAuthKey(scan.nextLine());
            setSourceLanguage(scan.nextLine());
            setTargetLanguage(scan.nextLine());

        }

    }

    public static void writeSettings() throws IOException {

        File f = new File("src/main/resources/data/savedCred.TXT");

        PrintWriter pw = new PrintWriter(f);
        pw.close();

        FileOutputStream fos = new FileOutputStream(f);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        if (getProjectID() == null || getProjectID().isEmpty()) {
            System.out.println("project id NULL");
            bw.write("NULL");
        } else {
            // System.out.println("project id " + getProjectID());
            bw.write(getProjectID());
        }
        bw.newLine();
        if (getAuthKey() == null || getAuthKey().isEmpty()) {
            bw.write("null");
        } else {
            bw.write(getAuthKey());
        }
        bw.newLine();
        if (getSourceLanguage() == null || getTargetLanguage().isEmpty()) {
            bw.write("en-US");
        } else {
            bw.write(getSourceLanguage());
        }
        bw.newLine();
        if (getTargetLanguage() == null || getSourceLanguage().isEmpty()) {
            bw.write("en-US");
        } else {
            bw.write(getTargetLanguage());
        }
        bw.newLine();
        bw.close();

    }

    public void projectIDEnter(ActionEvent arg0) throws Exception {
        System.out.println("Setting project ID: " + projectIDField.getText());
        setProjectID(projectIDField.getText());
        writeSettings();

    }

    public void sourceEnter(ActionEvent arg0) throws Exception {
        System.out.println("Setting source language: " + sourceField.getText());
        setSourceLanguage(sourceField.getText());
        writeSettings();

    }

    public void targetEnter(ActionEvent arg0) throws Exception {
        System.out.println("Setting target language: " + targetField.getText());
        setTargetLanguage(targetField.getText());
        writeSettings();

    }

    public void deeplKeyEnter() throws IOException {
        System.out.println("Setting auth key: " + targetField.getText());
        setAuthKey(deeplKeyField.getText());
        writeSettings();
    }

    public static void setProjectID(String ID) {
        projectID = ID;
    }

    public static String getProjectID() {
        return projectID;
    }

    public static void setTargetLanguage(String Language) {
        tLanguage = Language;
    }

    public static String getTargetLanguage() {
        return tLanguage;
    }

    public static void setSourceLanguage(String Language) {
        sLanguage = Language;
    }

    public static String getSourceLanguage() {
        return sLanguage;
    }

    public static void setAuthKey(String key) {
        authKey = key;
    }

    public static String getAuthKey() {
        return authKey;
    }

}
