package sound;

import jm.music.data.*;
import jm.JMC;
import jm.util.*;

import pitchLab.reference.DynmVar;

/**
 * A class that determines what instrument to play during the PitchLab testing
 * procedure - only instantiated during the Passive Pitch test.
 * 
 * @author Dane Odekirk
 * @version 0.2 March 20, 2011
 */

public class Instruments implements JMC {

	private int instrumentName;
    private int noteLength = 100;
	
	public static void main(String[] args) {	
		new Instruments();
	}
	
	/**
	 * This basically determines which global instrument to use based on the input selected
	 * from the MainWindow drop-down selector. The instrument is set to an constant imported 
	 * from jMusic. 
	 * 
	 * Some instruments do not sounds correct when sustained for a long period of time - 
	 * such as the Piano, Guitar or Harpsicord. Therefore, a note-length is defined for 
	 * each of these instruments which were arbitrarily chosen for aesthetic purposes.
	 * 
	 * The function sets the Instruments object's instrument and note duration attributes.
	 * 
	 */
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
     * Sine wave, then this function is called.  It will generate 5 random 
     * tones with the instrument selected and then play either a single, extended
     * note or a repeated note of the same frequency.
     * 
     * The reason some instruments have a repeated note and others have a sustained
     * one is either the instrument sounded incorrect when sustained or the instrument
     * was not build for an infinitely continuous note. 
     * 
     * The Piano, Harpsicord and Guitar are the instruments that do not play a 
     * sustained note, but instead repeat a single note a the 'noteLength' interval.
     * 
     *  @param f The frequency requested to play after a 5 random notes have been play.
     *  	   This will be the frequency that the testee needs to match on the Piano Window.
     *   
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
