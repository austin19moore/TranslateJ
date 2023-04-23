import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

public class syncSpeech {

    public static String transcribe(String f) throws Exception {

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
                            .setSampleRateHertz(16000)
                            .setLanguageCode("LANGUAGE CODE HERE")
                            .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // execute speech recognition on file
            RecognizeResponse response = speech.recognize(config, audio);

            for (SpeechRecognitionResult result : response.getResultsList()) {

                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.println("Transcription: " + alternative.getTranscript());
                System.out.println("debug: " + result.getLanguageCode());
                System.out.println("Billed: " + response.getTotalBilledTime());
                r += alternative.getTranscript();

            }

        }

        // return result
        return r;

    }

}
