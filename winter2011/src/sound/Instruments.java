package sound;

import jm.music.data.*;
import jm.JMC;
import jm.util.*;

import pitchLab.reference.DynmVar;
 
public class Instruments implements JMC {
	//private boolean random;
	//private int singleNote;
	private int instrumentName;
    private int noteLength = 100;
	
	public static void main(String[] args) {	
		new Instruments();
	}
	//THIS ALL WORKS!
	public Instruments() {


        if(DynmVar.instrument == "Piano"){
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");

            this.noteLength = 2;
            this.instrumentName = PIANO;

        } else if (DynmVar.instrument == "Harpsicord") {
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");
            
        	this.noteLength = 2;
            this.instrumentName = HARPSICHORD;

        } else if (DynmVar.instrument == "Organ") {
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");

            this.instrumentName = CHURCH_ORGAN;

        } else if (DynmVar.instrument == "Oboe") {
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");

            this.instrumentName = OBOE;

        } else if (DynmVar.instrument == "Clarinet") {
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");

            this.instrumentName = CLARINET;

        } else if (DynmVar.instrument == "Guitar") {
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");

        	this.noteLength = 3;
            this.instrumentName = NYLON_GUITAR;

        } else if (DynmVar.instrument == "Strings") {
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");

            this.instrumentName = STRINGS;
        } else if (DynmVar.instrument == "Flute") {
        	System.out.println("Currently using " + DynmVar.instrument + " instrument");

            this.instrumentName = FLUTE;
	
        }

	}

    /**
     * When a custom instrument has been selected that isn't the default 
     * 
     * @return 
     */
    public void playRandomSequence(double f) {
    	System.out.println("Beginning " + DynmVar.instrument + " sequence");

        Score score = new Score();
        Part inst = new Part("Instrument", this.instrumentName, 0);
        Phrase phr = new Phrase(0.0); 
        
        for(int i=0; i<5; i++){
            Note note = new Note((int)(Math.random()*20+70), EIGHTH_NOTE);
            phr.addNote(note);
        }
        
        Note finalNote = new Note((double)f, this.noteLength);
        for(int i=0; i<20; i++) {
        	phr.addNote(finalNote);
        }

        inst.addPhrase(phr);
        score.addPart(inst);

        Play.stopMidi();
		Play.midi(score, false);
    }
}	
