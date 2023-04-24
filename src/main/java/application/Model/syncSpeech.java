package application.Model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import application.Controller.MainController;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

public class syncSpeech {

    public static String transcribe(String f, String sLanguage) throws Exception {

        String r = "";

        try (SpeechClient speech = SpeechClient.create()) {

            // path to file to transcribe
            Path path = Paths.get(f);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // build request
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(44100)
                            .setLanguageCode(sLanguage)
                            .setAudioChannelCount(2)
                            .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // execute speech recognition on file
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();


            for (SpeechRecognitionResult result : results) {

                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.println("Transcription: " + alternative.getTranscript());
                //System.out.println("debug: " + result.getLanguageCode());
                System.out.println("Billed " + response.getTotalBilledTime());
                System.out.println("Confidence: " + alternative.getConfidence());
                System.out.println("Count: " + alternative.getWordsCount());
                r += alternative.getTranscript();

            }

        }

        // return result
        return r;

    }

}
