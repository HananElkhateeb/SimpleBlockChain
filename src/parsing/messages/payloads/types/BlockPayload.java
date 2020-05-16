package parsing.messages.payloads.types;

import parsing.messages.payloads.Payload;

import java.util.ArrayList;

public class BlockPayload implements Payload {

    private ArrayList<Integer> input;
    private ArrayList<Integer> output;
    public BlockPayload() {}

    public ArrayList<Integer> getInput() {
        return input;
    }

    public void setInput(ArrayList<Integer> input) {
        this.input = input;
    }

    public ArrayList<Integer> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<Integer> output) {
        this.output = output;
    }
}
