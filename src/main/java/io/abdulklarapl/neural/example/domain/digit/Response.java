package io.abdulklarapl.neural.example.domain.digit;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Patryk Szlagowski <patryksz@lsnova.pl>
 */
public class Response {

    private double[] output;

    public Response(double[] output) {
        this.output = output;
    }

    public double[] getOutput() {
        return output;
    }

    @JsonProperty("recognized_int")
    public int getRecognizedInt() {
        int found = 0;
        double last = 0;
        for (int i = 0; i < output.length; i++) {
            if (output[i] > last) {
                found = i;
                last = output[i];
            }
        }

        return found;
    }
}
