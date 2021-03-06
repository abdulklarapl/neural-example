package io.abdulklarapl.neural.example.domain.digit;

import io.abdulklarapl.neural.activator.LinearActivationFunction;
import io.abdulklarapl.neural.activator.ThresholdActivatorFunction;
import io.abdulklarapl.neural.element.Network;
import io.abdulklarapl.neural.element.Neuron;
import io.abdulklarapl.neural.train.Trainer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Patryk Szlagowski <patryksz@lsnova.pl>
 */
@Service
public class DigitNetwork {

    private static Logger logger = Logger.getLogger(DigitNetwork.class);
    public static Integer SQUARE = 25;

    private Network network;

    public DigitNetwork() {
        network = new Network("digit", SQUARE*SQUARE, 10, SQUARE*10+10);
    }

    /**
     * train with data file
     * @param filename
     * @throws Exception
     */
    public void train(String filename) throws Exception {
        URL path = DigitNetwork.class.getResource(filename);
        String content = new String(Files.readAllBytes(Paths.get(path.toURI())));

        Arrays.stream(content.split("\n")).forEach(line -> {
            if (line == null || line.isEmpty()) {
                return;
            }

            String[] parts = line.split(Pattern.quote(";"));

            logger.info("line "+parts[0]);
            Request request = new Request(Integer.parseInt(parts[0]), parts[1]);
            try {
                train(request);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });

        network.persist();
    }

    /**
     * train from request
     * @param request
     * @throws Exception
     */
    public void train(Request request) throws Exception {
        if (request.getExpected() == null || request.getPayload() == null || request.getPayload().isEmpty()) {
            return;
        }
        Trainer trainer = new Trainer(network);
        trainer.train(request.toTrainData(), 0.001);
    }

    /**
     * recognize given drawing
     *
     * they see me rollin', they hatin'
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Response recognize(Request request) throws Exception {
        network.input(request.getInputAsDoubleArray());
        return new Response(network.process());
    }

    /**
     * persist network
     *
     * @return
     */
    public String persist() throws IOException {
        return network.persist();
    }

    /**
     * wakeup network from frozen version
     *
     * @param timestamp
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void wakeup(String timestamp) throws IOException, ClassNotFoundException {
        String fileName = "NeuralNetwork-digit".concat(timestamp).concat(".bin");
        URL path = DigitNetwork.class.getResource(fileName);
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fileName));
        network = (Network) stream.readObject();
    }

    /**
     * return most important input neurons for a specific output
     *
     * @return
     */
    public Map<Integer, List<Neuron>> dump() {
        Map<Integer, List<Neuron>> important = new HashMap<>();
        Integer index = 0;
        for (Neuron outputNeuron : network.getOutput().getNeurons()) {
            List<Neuron> neuronsInHiddenLayer = outputNeuron.getSynapses().stream().sorted((syn1, syn2) -> Double.compare(syn1.getWeight(), syn2.getWeight())).map(synapse -> synapse.getSource()).collect(Collectors.toList());
            List<Neuron> inputNeurons = new ArrayList<>();
            for (Neuron hiddenNeuron : neuronsInHiddenLayer) {
                hiddenNeuron.getSynapses().stream().sorted((syn11, syn22) -> Double.compare(syn11.getWeight(), syn22.getWeight())).map(synapse -> synapse.getSource()).forEach(neuron -> {
                    if (!inputNeurons.contains(neuron)) {
                        inputNeurons.add(neuron);
                    }
                });
            }
            important.put(index, inputNeurons.subList(0, 40));
            index++;
        }

        return important;
    }

    /**
     * dump simple:
     * output number -> 0001001001001 = sequence of characters
     *
     * @return
     */
    public Map<Integer, String> dumpSimple() {
        Map<Integer, List<Neuron>> important = dump();
        Map<Integer, String> simple = new HashMap<>();
        for (Integer index : important.keySet()) {
            Map<Long, Neuron> neurons =  important.get(index).stream().collect(Collectors.toMap(Neuron::getId, neuron -> neuron));
            String value = IntStream.range(0, network.getInput().getNeurons().size()).mapToObj(in -> {
                if (neurons.containsKey(Integer.valueOf(in).longValue())) {
                    return "1";
                }
                return "0";
            }).collect(Collectors.joining());

            simple.put(index, value);
        }

        return simple;
    }
}
