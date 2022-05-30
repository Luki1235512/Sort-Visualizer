package sortvisualizer;

import javax.sound.midi.*;
import java.util.ArrayList;

public class MidiSoundPlayer {

    private final ArrayList<Integer> keys;
    private Synthesizer synth;
    private final MidiChannel channel;

    private final int inputValueMaximum;
    private static int CACHED_INDEX = -1;

    public MidiSoundPlayer(int maxValue) {
        try {
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        this.inputValueMaximum = maxValue;

        this.channel = this.synth.getChannels()[0];

        Instrument[] instruments = this.synth.getDefaultSoundbank().getInstruments();

        if (CACHED_INDEX == -1) {
            boolean found = false;
            int index;
            for (index = 0; index < instruments.length; index++) {
                Instrument i = instruments[index];
                if (i.getName().equals("Electric Grand Piano")) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                index = 2;
            }
            CACHED_INDEX = index;
        }
        this.channel.programChange(instruments[CACHED_INDEX].getPatch().getProgram());

        this.keys = new ArrayList<>();

        for (int i = 24; i < 108; i += 12) {
            this.keys.add(i);
            this.keys.add(i + 2);
            this.keys.add(i + 4);
            this.keys.add(i + 5);
            this.keys.add(i + 7);
            this.keys.add(i + 9);
            this.keys.add(i + 11);
        }
    }

    private int convertToMajor(int v) {
        float n = ((float)v / (float)inputValueMaximum);
        int index = (int)(n * (float)this.keys.size());
        index = Math.max(1, Math.min(107, index));
        return this.keys.get(index);
    }

    public void makeSound(int value) {
        int note = convertToMajor(value);
        this.channel.noteOn(note, 25);
    }

}
