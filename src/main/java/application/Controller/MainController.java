package application.Controller;



import application.Main;
import application.Model.Record;
import application.Model.syncSpeech;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextArea transcriptionArea;

    @FXML
    TextArea translationArea;

    @FXML
    Button startButton;

    @FXML
    Button cancelButton;

    @FXML
    Text currentAudioSource;

    private static TargetDataLine targetLine;
    private static Mixer mixer;
    private static boolean recording = false;
    private Record recAudio;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setTargetLine(getMic());

        try {
            SettingsController.getSettings();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(SettingsController.getProjectID());
        System.out.println(SettingsController.getSourceLanguage());
        System.out.println(SettingsController.getTargetLanguage());
    }


    public void startButton(ActionEvent arg0) throws Exception {

        recAudio = new Record();
        //cancelButton.setVisible(true);
        cancelButton.setDisable(false);

        if (recording) {
            recAudio.stop(targetLine);
            startButton.setText("Stop");

            System.out.println("Starting transcription...");
            application.Model.syncSpeech syncT = new syncSpeech();
            String transcription = syncT.transcribe("src/main/resources/audio/recording.wav", SettingsController.getSourceLanguage());
            // String transcription = "ただ様で始める前にさちょっとね昨日ね愛おしいねファミリーベッド混じる小学校のジル君の写真をねあの送ってねやつぶやいたのみんなに見てもらいたくてつぶやいたらさ";

            System.out.println("Starting translation...");
            String translation = deeplTranslate(transcription);

            transcriptionArea.setText(transcription);
            translationArea.setText(translation);
            startButton.setText("Start");
            System.out.println("done");

        } else {
            recording = true;
            Record.getAudio(targetLine);
            System.out.println("Starting record...");
            startButton.setText("Stop");
        }

    }

    public String deeplTranslate(String transcription) throws Exception {
        Translator translator = new Translator(SettingsController.getAuthKey());
        TextResult result = translator.translateText(transcription, SettingsController.getSourceLanguage(), SettingsController.getTargetLanguage());
        System.out.println("translation: " + result.getText());
        return result.getText();

    }

    public void cancelButton(ActionEvent arg0) {

        recAudio.stop(targetLine);
        recording = false;
        startButton.setText("Start");
        cancelButton.setDisable(true);

    }

    public void settingsButton(ActionEvent arg0) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Settings.fxml"));
            System.out.println("Switching user to Settings view");
            Main.stage.setScene( new Scene(root, 800, 400) );
            Main.stage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void swapButton(ActionEvent arg0) throws Exception {

        if (mixer != null && mixer.getMixerInfo().getName().contains("Stereo Mix")) {
            setTargetLine(getMic());
            System.out.println("mic");
        } else {
            setTargetLine(getStereo());
            System.out.println(mixer.getMixerInfo().getName());
        }

    }

    public TargetDataLine getMic() {

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = null;

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            currentAudioSource.setText("Current: Default Mic");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        mixer = null;
        return line;

    }

    public TargetDataLine getStereo() throws LineUnavailableException {

        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

        for (Mixer.Info mix : mixers) {
            if (mix.getName().contains("Stereo Mix")) {
                mixer = AudioSystem.getMixer(mix);
                currentAudioSource.setText("Current: Stereo Mix");
            }
        }

        TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
        return line;

    }

    public static void setTargetLine(TargetDataLine tLine) {
        targetLine = tLine;
    }

    public static Mixer getMixer() {
        return mixer;
    }

    public void handle(ActionEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("Switching user to ??? view");
    }

}
