package io.abdulklarapl.neural.example.domain.digit;

import io.abdulklarapl.neural.element.Network;
import io.abdulklarapl.neural.train.Trainer;
import org.springframework.stereotype.Service;

/**
 * @author Patryk Szlagowski <patryksz@lsnova.pl>
 */
@Service
public class DigitNetwork {

    public static Integer SQUARE = 25;

    private Network network;

    public DigitNetwork() {
        network = new Network("digit", SQUARE*SQUARE, 10);
    }

    public void train(Request request) throws Exception {
        Trainer trainer = new Trainer(network);
        trainer.train(request.toTrainData(), 0.005);
    }

    public Response recognize(Request request) throws Exception {
        network.input(request.getInputAsDoubleArray());
        return new Response(network.process());
    }
}
