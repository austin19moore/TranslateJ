package application.Controller;

import application.Main;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.sound.sampled.*;
import javax.swing.*;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    ListView<String> audioOptions;
    ObservableList<String> audioList = FXCollections.observableArrayList();
    int audioIndex;

    @FXML
    Button backButton;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

        for (int i = 0; i < mixerInfo.length; i++) {
            audioList.add(mixerInfo[i].getName());
        }

        audioOptions.setItems(audioList);

    }

    public void backButton(ActionEvent arg0) throws Exception {

        // move the user to the Decks view
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
            System.out.println("Switching user to Main view");
            Main.stage.setScene( new Scene(root, 800, 400) );
            Main.stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void audioListViewClick() throws LineUnavailableException {

        audioIndex = audioOptions.getSelectionModel().getSelectedIndex();
        System.out.println(audioIndex);

        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();    //get available mixers
        System.out.println("Available mixers:");
        Mixer mixer = null;

            mixer = AudioSystem.getMixer(mixerInfo[audioIndex]);

            Line.Info[] lineInfos = mixer.getSourceLineInfo();
            if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(SourceDataLine.class)) {
                System.out.println(audioIndex + " line is supported!");

                AudioFormat audioFormat = new AudioFormat(48000, 16, 2, true, false);     //get the audio format
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                MainController.setTargetLine(targetLine);

            }
    }



}
