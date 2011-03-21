package sound;

import javax.sound.sampled.*;   
import java.util.*;

/**
 * The Sine class produces a sine wave based on a series of parameters
 * that are used to generate the application notes.
 *
 * @author Gavin Shriver
 * @version 0.6 April 27, 2009
 *
 *
 * Copyright 2011 Vladimir Chaloupka. All rights reserved.
 *
 */
public class Sine
{
	//******************************************************************************
	// BEGIN VARIABLES (defaults and more!)
	//******************************************************************************
	//   -- note these values will be immediately changed
	// 		when defaults are loaded at the start of pitchLab


	// - array of the next set of random frequencies
	private double[] randFreqs;
	private static int numRandTones = 4;
	
	// - random (and all) tone constraints
	public static double minFreqBounds = 80.0; //Hz
	public static double maxFreqBounds = 4000.0; //Hz
	public static double minFreqSeperation = 50.0; //Hz
	
	// - random tone constraints IN OCTAVES
	public static int minOctave = 3; //
	public static int numberOfOctaves = 3; //
	private static final double C0 = 16.35; //hz (bottom octave)
	
	// - control variable
	private static boolean finiteTimes = false;
	
	// - tone times
	private static double finiteToneTime = 0.6; //seconds
	private static double minToneTime = 0.4; //seconds
	private static double maxToneTime = 1.0; //seconds
	
	// - rest times
	private static double finiteRestTime = 0.3; //seconds
	private static double minRestTime = 0.25; //seconds
	private static double maxRestTime = 0.75; //seconds
	
	
	// --- Math Constructs
	private static final double RAD = 2.0 * Math.PI;
	private static Random random = new Random();
	private static boolean useDynAmp  = false;
	private static double amplitude = 75.0;
	
	
	// --- Audio Signal & Line Variables
	private static int sampleRate = 48000; // 48 kbit/s sampling rate
	private static int sampleSizeInBits = 16; //bits
	private static int sampleSizeInBytes = 2;
	private static int lineBufferSize = 48000;

	// --- Audio Line Constructs
	private static final AudioFormat AF = new AudioFormat(sampleRate, sampleSizeInBits ,1, true, true);
	private static final DataLine.Info INFO = new DataLine.Info(SourceDataLine.class, AF);
	private static SourceDataLine line;
	
	

	//******************************************************************************
	// END VARIABLES
	// BEGIN CONSTRUCTORS
	//******************************************************************************
	
    /**
     * Contstructs a blank Sine Object and prints out <code>Exception</code> if anything 
     * goes wrong.
     */
	public Sine()
	{
		try
		{
			line = (SourceDataLine)AudioSystem.getLine(INFO);
			generateRands();
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
	}
	
	
	/** 
     * Constructs a Sine Object with defaults for
	 * multi-random tone generation with random play
	 * and rest times
     *
     * @param numRandTones The <code>int</code> amount of random tones to  to generate.
	 */
	public Sine(int numRandTones)
	{
		setNumRandTones(numRandTones);
		generateRands();			
		
		try 
		{
			line = (SourceDataLine)AudioSystem.getLine(INFO);
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
	}
	
	/**
     * Constructs a Sine Object with defaults for
     * multi-random tone generation with finite play and rest times
     *
     * @param numRandTones The <code>int</code> number of random tones
     * @param finiteToneTime The <code>double</code> duration of the tone 
     * @param finiteRestTime The <code>double</code> duration of the rest between tones
     */
	public Sine(int numRandTones, double finiteToneTime, double finiteRestTime)
	{
		setNumRandTones(numRandTones);
		
		setFiniteTimes(true);
		setFiniteToneTime(finiteToneTime);
		setFiniteRestTime(finiteRestTime);
		generateRands();
		
		try 
		{
			line = (SourceDataLine)AudioSystem.getLine(INFO);
		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
	}

	//******************************************************************************
	// END CONSTRUCTORS METHOD
	// BEGIN PLAY METHODS
	//******************************************************************************
	
	
	/**
     * Plays a frequency for a particular duration 
     * without creating a Sine object
     * @param frequency The frequency to be generated
     * @param seconds The duration the generated frequency should last
     */
	public static void play(double frequency, double seconds)
	{
		byte[] toneBuffer = buildBuffer(frequency, seconds);
		
		try 
		{
			line.open(AF, lineBufferSize);
			line.start();
			line.write(toneBuffer, 0, toneBuffer.length);
			line.drain();
			line.close();
		}
		catch (Exception e) 
		{
			System.out.println("Error Processing Sound in Sine: ");
			e.printStackTrace();
		}
	}
	
			
	/**
     * Plays a number of random frequencies for random
     * durations with random intervals!
     */
	public void play()
	 {
		 byte[] toneBuffer;
		 byte[] restBuffer;
		 
		 try 
		 {
			 line.open(AF, lineBufferSize); //opens line
			 line.start(); //starts line
			 
			 // - for random lengths of tone and rest
			 for (int i = 0; i < randFreqs.length; i++) 
			 {
				 
				 // --- Writes rest to the line buffer
				 restBuffer = buildRest(restTimeGenerator());
				 line.write(restBuffer, 0, restBuffer.length);
				 
				 // --- Writes tone to the line buffer
				 toneBuffer = buildBuffer(randFreqs[i], toneTimeGenerator());
				 line.write(toneBuffer, 0, toneBuffer.length);

			 }
			 
			 // --- final rest before next test
			 restBuffer = buildRest(restTimeGenerator());
			 line.write(restBuffer, 0, restBuffer.length);
			 
			 
			 line.drain();
			 line.close();
			 line.flush();
		 }
		 catch (Exception e) 
		 {
			 System.out.println("Error Processing Sound in Sine: ");
			 e.printStackTrace();
			 System.exit(0);
		 }
		 generateRands();
	 }
	
	
	/**
     * This is a failed experiment left here for future reference
     *
     * @deprecated Never used.
     */
	public void blend()
	 {
		 byte[] toneBuffer = new byte[sampleRate]; //so it will play for one second
		 byte[] restBuffer;
		 
		double amp1 = 80;
		double amp2 = 60;
		
        for (int i=0; i < toneBuffer.length; i++) 
		{
        	toneBuffer[i] = (byte)(Math.sin(i*RAD*randFreqs[0]/sampleRate)*amp1 +
        			Math.sin(i*RAD*randFreqs[1]/sampleRate)*amp2);
		}	
        
		 try 
		 {
			 line.open(AF, lineBufferSize); //opens line
			 line.start(); //starts line
			 
			 line.write(toneBuffer, 0, toneBuffer.length);
			 
			 // --- final rest before next test
			 restBuffer = buildRest(restTimeGenerator());
			 line.write(restBuffer, 0, restBuffer.length);
			 
			 
			 line.drain();
			 line.close();
			 line.flush();
		 }
		 catch (Exception e) 
		 {
			 System.out.println("Error Processing Sound in Sine: ");
			 e.printStackTrace();
			 System.exit(0);
		 }
		 
		 generateRands();
		 
	 }

	//******************************************************************************
	// END PLAY METHOD
	// BEGIN SOUND METHODS
	//******************************************************************************
	
	/**
     * Builds any tone given a frequency and duration with some fancy mathematics
     * @param frequency The frequency to generate
     * @param seconds The duration of the generated frequency
     */
	private static byte[] buildBuffer(double frequency, double seconds)
	{
		int bufferSize = (int)(seconds*sampleRate*sampleSizeInBytes);
		//ensures there is no remainder so that a complete sample is written & no error occurs.
		bufferSize += bufferSize%sampleSizeInBytes; 
				
		byte[] buffer = new byte[bufferSize];
		
		double amp;
		if (useDynAmp )
			amp = sineAmplitude(Math.log(frequency));
		else
			amp = amplitude;
		
        for (int i=0; i < buffer.length/sampleSizeInBytes; i++) 
		{
			int wave = (int)((amp/100.0)*(32767.0 * Math.sin(((i)*RAD* frequency) / sampleRate)));

            byte msb = (byte)(wave >>> 8);
            
            buffer[i*sampleSizeInBytes] = msb;
	        if (getSampleSizeInBytes() >1)
	        {
	        	byte lsb = (byte) wave;
	        	buffer[i*sampleSizeInBytes+1] = lsb;
	        }
		}			
        
        return buffer;
		
	}
	

	/**
     * Builds a rest given a duration between generated frequencies.
     * @param seconds The duration of the rest
     */
	private static byte[] buildRest(double seconds)
	{
		int restSize =  (int)(seconds*sampleRate*sampleSizeInBytes);
		restSize += restSize%sampleSizeInBytes;
		byte[] buildRest = new byte[restSize];
		Arrays.fill(buildRest, (byte)0);
		return buildRest;
	}

	
	/**
     * Builds arrays of random values
     */
	private void generateRands()
	{
		randFreqs = new double[numRandTones];
		for (int i = 0; i < numRandTones ; i++) 
		{
			do 
			{
				randFreqs[i] = randFreqGenerator();
			} while (i > 0 && Math.abs(randFreqs[i] - randFreqs[i-1]) < minFreqSeperation);				
		}
	}
	
	/**
     * Returns a random frequency within specifications.
     * Defaults to global min and max bounds
     */
	public static double randFreqGenerator()
	{		
		return randFreqGenerator(Sine.minFreqBounds,Sine.maxFreqBounds);
	}
	public static double randFreqGenerator(double minFreq, double maxFreq)
	{
		return (maxFreq-minFreq)*random.nextDouble() + minFreq;
	}
	
	/*public static double randFreqGenerator(double minFreq, double maxFreq)
	{
		double randFreqGenerator;			
		do 
		{
			randFreqGenerator = maxFreq*random.nextDouble();
		} while (randFreqGenerator < minFreq);
		
		return randFreqGenerator;
	}//END randFreqGenerator*/
	
	/**
     * Returns 'finiteToneTime' if finiteTimes is set
     * otherwise returns random duration for a tone within specifications.
     */
	private double toneTimeGenerator()
	{
		if (finiteTimes)
			return finiteToneTime;
		else
		{
			double toneTimeGenerator = 0;
			do 
			{
				toneTimeGenerator = maxToneTime*random.nextDouble();
			} while (toneTimeGenerator < minToneTime);
			return toneTimeGenerator;
		}
	}
	
	
	/**
     * Returns 'finiteRestTime' if finiteTimes is set
     * otherwise returns random duration for a rest within specifications.
     */
	private double restTimeGenerator()
	{
		//returns the "finite time" if (finiteTimes== true)
		// or otherwise, constructs a random duration
		if (finiteTimes)
			return finiteRestTime; 
		else
		{
			double restTimeGenerator = 0;
			do 
			{
				restTimeGenerator = maxRestTime*random.nextDouble();
			} while (restTimeGenerator < minRestTime);
			
			return restTimeGenerator;
		}
	}
	
	/**
     * Returns frequency of octave +/- base octave
     * @param relOctave Determines whether to move up or down an octive.
     */
	public static double getRelativeOctaveFrequency(int relOctave)
	{
		return getRelativeOctaveFrequency((double)relOctave);
	}
	public static double getRelativeOctaveFrequency(double relOctave)
	{
		if (relOctave < 0)
			return Sine.minFreqBounds*Math.pow(2.0, 1/Math.abs(relOctave));
		else if (relOctave > 0)
			return Sine.minFreqBounds*Math.pow(2.0, relOctave);
		else
			return Sine.minFreqBounds*Math.pow(2.0, relOctave);

	}
	
	//******************************************************************************
	// END SOUND METHODS
	// BEGIN SET/GET METHODS
	//******************************************************************************
	
	/**
     * Set number of random tones
     * @param numRandTones The <code>int</code> number of random tones
     */
	public static void setNumRandTones(int numRandTones)
	{
		Sine.numRandTones = numRandTones;
	}
    /**
     * Return the number of random tones.
     */
	public static int getNumRandTones()
	{
		return Sine.numRandTones;
	}
	
	/**
     * Set the minimum frequency bounds 
     * @param minFreqBounds The minimum frequency allowed
     */
	private static void  setMinFreqBounds(double minFreqBounds)
	{
		Sine.minFreqBounds = minFreqBounds;
	}
    /**
     * Returns the minimum frequency allowed
     */
	public static double  getMinFreqBounds()
	{
		return Sine.minFreqBounds;
	}
	
	/**
     * Set the maximum frequency bounds 
     * @param maxFreqBounds The maximum frequency allowed
     */
	private static void  setMaxFreqBounds(double maxFreqBounds)
	{
		Sine.maxFreqBounds = maxFreqBounds;
	}
	/**
     * Return the maximum frequency bounds 
     */
	public static double  getMaxFreqBounds()
	{
		return Sine.maxFreqBounds;
	}
	
    /**
     * Set the minimum frequency seperation between the maximum and minimum 
     * frequencies allowed.
     * @param minFreqSeperation The minimum frequency seperation allowed.
     */
	public static void setMinFreqSeperation(double minFreqSeperation)
	{
		Sine.minFreqSeperation = minFreqSeperation;
	}
    /**
     * Return minimum frequency seperation allowed
     */
	public static double  getMinFreqSeperation()
	{
		return Sine.minFreqSeperation;
	}
	
    /**
     * Set the finite times 
     * @param finiteTimes The <code>boolean</code> that sets finiteTimes
     */
	public static void setFiniteTimes(boolean finiteTimes)
	{
		Sine.finiteTimes = finiteTimes;
	}
    /**
     * Returns the finite times boolean
     */
	public static boolean  getFiniteTimes()
	{
		return Sine.finiteTimes;
	}
	
	//------------------------------------------------------
	// --- TONE TIMES
	//------------------------------------------------------ 
	public static void setFiniteToneTime(double finiteToneTime)
	{
		Sine.finiteToneTime = finiteToneTime;
	}
	public static double getFiniteToneTime()
	{
		return Sine.finiteToneTime;
	}
	
	public static void setMinToneTime(double minToneTime)
	{
		Sine.minToneTime = minToneTime;
	}
	public static double getMinToneTime()
	{
		return Sine.minToneTime;
	}
	
	public static void setMaxToneTime(double maxToneTime)
	{
		Sine.maxToneTime = maxToneTime;
	}
	public static double getMaxToneTime()
	{
		return Sine.maxToneTime;
	}
	
	//------------------------------------------------------
	// --- REST TIMES
	//------------------------------------------------------ 
	public static void setFiniteRestTime(double finiteRestTime)
	{
		Sine.finiteRestTime = finiteRestTime;
	}
	public static double getFiniteRestTime()
	{
		return Sine.finiteRestTime;
	}
	
	public static void setMinRestTime(double minRestTime)
	{
		Sine.minRestTime = minRestTime;
	}
	public static double getMinRestTime()
	{
		return Sine.minRestTime;
	}
	
	public static void setMaxRestTime(double maxRestTime)
	{
		Sine.maxRestTime = maxRestTime;
	}
	public static double getMaxRestTime()
	{
		return Sine.maxRestTime;
	}
	
	//------------------------------------------------------
	// --- ADVANCED VARIABLES
	//------------------------------------------------------ 
	public static void setSampleRate(int sampleRate)
	{
		Sine.sampleRate = sampleRate;
	}
	public static int getSampleRate()
	{
		return Sine.sampleRate;
	}
	
	public static void setSampleSizeInBits(int sampleSizeInBits)
	{
		Sine.sampleSizeInBits = sampleSizeInBits;
		Sine.setSampleSizeInBytes((int)sampleSizeInBits/8);
	}
	public static int getSampleSizeInBits()
	{
		return Sine.sampleSizeInBits; 
	}
	
	public static void setLineBufferSize(int lineBufferSize)
	{
		Sine.lineBufferSize = lineBufferSize;
	}
	public static int getLineBufferSize()
	{
		return Sine.lineBufferSize;
	}
	
	public static void setAmplitude(double amplitude)
	{
		Sine.amplitude = amplitude;
	}
	public static double getAmplitude()
	{
		return Sine.amplitude;
	}
	
	public static void setUseDynAmp(boolean useDynAmp )
	{
		Sine.useDynAmp  = useDynAmp ;
	}
	public static boolean getUseDynAmp()
	{
		return Sine.useDynAmp ;
	}
	
    /**
     * Set the Sine object default values
     */
	public static void setDefaults()
	{
		// - random tone constraints
		Sine.setMinOctave(3); //Hz
		Sine.setNumberOfOctaves(3); //Hz
		Sine.setMinFreqSeperation(50.0); //Hz
		
		// - tone times
		Sine.setFiniteToneTime(0.6); //seconds
		Sine.setMinToneTime(0.25); //seconds
		Sine.setMaxToneTime(0.70); //seconds
		
		// - rest times
		Sine.setFiniteRestTime(0.3); //seconds
		Sine.setMinRestTime(0.15); //seconds
		Sine.setMaxRestTime(0.55); //seconds
		
		
		// --- Audio Signal & Line Variables
		Sine.setSampleRate(48000); // 48 kbit/s sampling rate
		Sine.setSampleSizeInBits(16); //bits
		Sine.setLineBufferSize(48000); //bytes
		Sine.setAmplitude(70.0);
		Sine.setUseDynAmp(false);
		
		// --- Other control variables
		Sine.setFiniteTimes(false);
		
	}
	
	
	public static void setMinOctave(int minOctave)
	{
		Sine.minOctave = minOctave;
		double minfreq = C0*Math.pow(2, Sine.minOctave);
		double maxfreq = minfreq*Math.pow(2, Sine.numberOfOctaves);
		Sine.setMinFreqBounds(minfreq);
		Sine.setMaxFreqBounds(maxfreq);
		
	}
	public static int getMinOctave()
	{
		return Sine.minOctave;
	}
	
	public static void setNumberOfOctaves(int numberOfOctaves)
	{
		Sine.numberOfOctaves = numberOfOctaves;
		double minfreq = C0*Math.pow(2, Sine.minOctave);
		double maxfreq = minfreq*Math.pow(2, Sine.numberOfOctaves);
		Sine.setMinFreqBounds(minfreq);
		Sine.setMaxFreqBounds(maxfreq);
		
	}
	public static int getNumberOfOctaves()
	{
		return Sine.numberOfOctaves;
	}
	
	public static void setSampleSizeInBytes(int sampleSizeInBytes)
	{
		Sine.sampleSizeInBytes = sampleSizeInBytes;
	}


	public static int getSampleSizeInBytes()
	{
		return Sine.sampleSizeInBytes;
	}
	
	//******************************************************************************
	// END SET/GET METHODS
	// START (odd) amplitude shift
	//******************************************************************************
	
	//-- mk's method:
    /**
     * Set the Sine amplitude based on its frequency and fancy mathematics
     * @param freqNlog The <code>double</code> that determines what amplitude value is returned.
     *
     */
	private static double sineAmplitude(double freqNlog)
	{
		double	amp, spl;
		
		spl=0.0;
	/*	
		if (freqNlog<=5.05) {
		  spl=50.0+(freqNlog-4.1)*(50.0-43.3)/(4.1-5.05);
		}
		else if (freqNlog<=6.0) {
		  spl=43.3+(freqNlog-5.05)*(43.3-36.0)/(5.05-6.0);
		}
		else if (freqNlog<=6.5) {
		  spl=36.0;
		}
		else if (freqNlog<=7.75) {
		  spl=40.0-4/pow((7.75-7.125),2)*pow((freqNlog-7.125),2);
		}
		else if (freqNlog<=8.6) {
		  spl=32.5+3.5/pow((8.6-8.175),2)*pow((freqNlog-8.175),2);
		}
	*/

		if (freqNlog<=4.1) 
		{
		  spl=Math.pow(freqNlog,2)*(-285)+1187*(freqNlog);
		}
		if (freqNlog<=4.8) 
		{
		  spl=83.0+(freqNlog-4.1)*(83.0-80.0)/(4.1-4.8);
		}
		else if (freqNlog<=5.5) 
		{
		  spl=80.0+(freqNlog-4.8)*(80.0-72.0)/(4.8-5.5);
		}
		else if (freqNlog<=6.2) 
		{
		  spl=72.0+(freqNlog-5.5)*(72.0-68.0)/(5.5-6.2);
		}
		else if (freqNlog<=6.9) 
		{
		  spl=68.0+(freqNlog-6.2)*(68.0-70.0)/(6.2-6.9);
		}
		else if (freqNlog<=7.6) 
		{
		  spl=70.0+(freqNlog-6.9)*(70.0-65.0)/(6.9-7.6);
		}
		else /*if (freqNlog<=9.3)*/ 
		{
		  spl=65.0+(freqNlog-7.6)*(65.0-62.5)/(7.6-8.3);
		}
		
		amp=(.75*Math.pow(Math.log(20*spl)/Math.log(10),.5)/Math.pow(Math.log(20*83.0)/Math.log(10),.5))*amplitude;

		return (amp);
	}


	
	
	
	
	
	//******************************************************************************
	// END amplitude shift
	// MAIN (for testing purposes)
	//******************************************************************************
	
	
	
	
	
    /**
     * Create the Sine object
     */
	public static void main(String[] args) 
	{
		
		Scanner chooser = new Scanner(System.in);
		
		int choice=1;
		
		try 
		{
			Sine test = new Sine(3);
			do 
			{
				System.out.print("Enter Number of Random Tones: ");
				choice = chooser.nextInt();
				if (choice > 0)
				{
					Sine.setNumRandTones(choice);
					test.play();
				}
				else if (choice == -3)
				{
					test.blend();
				}
				else if (choice == -2)
				{
				//	test.blending();
				}
			} while (choice != 0);
		} 		
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		
		double sec, freq;
		do 
		{
			System.out.print("Enter Frequency: ");
			freq = chooser.nextDouble();
			System.out.print("Enter Duration: ");
			sec = chooser.nextDouble();
			if (sec > 0 && freq > 0)
			{
				Sine.play(freq,sec);
				
			}
		} while (sec > 0 && freq>0);
		System.exit(0);
		
	}

	
}
