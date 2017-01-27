package io.abdulklarapl.neural.example.domain.digit;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.abdulklarapl.neural.train.TrainData;

import java.util.stream.IntStream;

/**
 * @author Patryk Szlagowski <patryksz@lsnova.pl>
 */
public class Request {

    private Integer rows;
    private Integer cols;
    private String payload;
    @JsonProperty("expected_output")
    private Integer expected;

    public Request() {
    }

    public Request(Integer expected, String payload) {
        this.expected = expected;
        this.payload = payload;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Integer getExpected() {
        return expected;
    }

    public void setExpected(Integer expected) {
        this.expected = expected;
    }

    public TrainData toTrainData() {
        return new TrainData(getInputAsDoubleArray(), getExpectedOutputAsDoubleArray());
    }

    public double[] getInputAsDoubleArray() {
        char[] characters = payload.toCharArray();
        double[] input = new double[characters.length];

        for (int pos = 0; pos < characters.length; pos++) {
            input[pos] = (double) new Integer(String.valueOf(characters[pos]));
        }

        return input;
    }

    public double[] getExpectedOutputAsDoubleArray() {
        double[] expectedOutput = new double[10];
        IntStream.range(0, 9).forEach(index -> {
            double value = 0;
            if (index == expected) {
                value = 1;
            }
            expectedOutput[index] = value;
        });

        return expectedOutput;
    }

}
