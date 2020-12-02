package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;

import java.util.ArrayList;
import java.util.Random;

public class InputThing {
    InputSource stringInput;
    ArrayList<Character> characters;
    boolean startedAtN;
    String seedString;
    Random rand;
    long seed;
    ArrayList<Character> commands;
    boolean startedAtL;
    ArrayList<Character> commandsAtL;

    public InputThing(String inputSeed) {
        stringInput = new StringInputDevice(inputSeed);
        Character startChar = stringInput.getNextKey();
        startedAtN = startChar.equals('n') || startChar.equals('N');
        startedAtL = startChar.equals('l') || startChar.equals('L');
        characters = new ArrayList<>();
        commands = new ArrayList<>();
        commandsAtL = new ArrayList<>();
        seedString = "";
        if (startedAtN) {
            convertInputToSeed();
        }
        if (startedAtL) {

        }
    }

    private void convertInputToSeed() {
        while (stringInput.possibleNextInput()) {
            Character seedDigit = stringInput.getNextKey();
            if (seedDigit.equals('S') || seedDigit.equals('s')) {
                while (stringInput.possibleNextInput()) {
                    seedDigit = stringInput.getNextKey();
                    commands.add(seedDigit);
                }
                break;
            }
            characters.add(seedDigit);
        }
        for (Character c : characters) {
            seedString += Character.toString(c);
        }
        seed = Long.parseLong(seedString);
        rand = new Random(seed);
    }
}
