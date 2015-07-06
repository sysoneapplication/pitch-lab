# Introduction #

This will act as a guide for anyone who wants to put additional instruments into PitchLab
for testing purposes. This tutorial will assume that you want to add an `"Trumpet"` as an
instrument.


# Files #

There are 2 files that will need to be edited in order to add an instrument:
  * Constants.java
  * Instruments.java

# Constants.java #

The Constants.java file is located in the following location:

```
pitchLab.reference.Constants
```

and behaves as a class that shares global variables within the PitchLab application.

The following code is a global variable within this class that governs the Strings that will appear in the dropdown
menu in PitchLab's main application window.

```
36    public static final String[] AVAILABLE_INSTRUMENTS = {
37          "Sine", "Piano", "Harpsicord", "Organ", "Oboe", "Flute", "Clarinet", "Guitar", "Strings"
38    };
```

Find this and code and add the string `"Trumpet"` to `AVAILABLE_INSTRUMENTS` as shown below:

```
36    public static final String[] AVAILABLE_INSTRUMENTS = {
37          "Sine", "Piano", "Harpsicord", "Organ", "Oboe", "Flute", "Clarinet", "Guitar", "Strings", "Trumpet"
38    };
```

If you recompile PitchLab now, you will notice that `"Trumpet"` is listed in the dropdown menu of the main window
just after Strings.

Once this is done, we must move onto the instruments.java file to officially integrate a Trumpet into PitchLab.


# Instruments.java #

The Instruments.java file is located in the following location:

```
sound.Instruments
```

This class, when instantiated, determines which instrument (if any) to play during the test.
The logic is fairly straight forward, and is governed by the `AVAILABLE_INSTRUMENTS` that
we have just edited in the Constants.java file.

Looking at the code in Instruments.java we see the following logic:

```

39        if(DynmVar.instrument == "Piano"){     	
40        	System.out.println("Currently using " + DynmVar.instrument + " instrument");
41           this.noteLength = 2;
42           this.instrumentName = PIANO;
43       } else if (DynmVar.instrument == "Harpsicord") {
44       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
45       	this.noteLength = 2;
46           this.instrumentName = HARPSICHORD;
47       } else if (DynmVar.instrument == "Organ") {
48       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
49           this.instrumentName = CHURCH_ORGAN;
50       } else if (DynmVar.instrument == "Oboe") {
51       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
52           this.instrumentName = OBOE;
53       } else if (DynmVar.instrument == "Clarinet") {
54       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
55           this.instrumentName = CLARINET;
56       } else if (DynmVar.instrument == "Guitar") {
57       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
58       	this.noteLength = 3;
59           this.instrumentName = NYLON_GUITAR;
60       } else if (DynmVar.instrument == "Strings") {
61       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
62           this.instrumentName = STRINGS;
63       } else if (DynmVar.instrument == "Flute") {
64       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
65           this.instrumentName = FLUTE;
66       }

```

This simply determines what instrument to use based on a string match. Notice that the strings being matched are
the strings in the `AVAILABLE_INSTRUMENTS` global variable.  Therefore, in order to add a the "Trumpet" instrument
and integrate it with PitchLab, we must add additional logic. If we focus on the following code:

```

63       } else if (DynmVar.instrument == "Flute") {
64       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
65           this.instrumentName = FLUTE;
66       }

```

and add the following:

```

63       } else if (DynmVar.instrument == "Flute") {
64       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
65           this.instrumentName = FLUTE;
66       } else if (DynmVar.instrument == "Trumpet") {
67       	System.out.println("Currently using " + DynmVar.instrument + " instrument");
68           this.instrumentName = TRUMPET;
69       }

```

we have told PitchLab that if "Trumpet" has been selected from the dropdown menu in the main window, then use the instrument with name `TRUMPET`.

The `TRUMPET` constant is being brought in by jMusic and is one of the default instruments built into the jMusic library.  A complete list of
the default instruments can be found here: http://jmusic.ci.qut.edu.au/jmtutorial/JMC_Instruments.html

Now if we recompile PitchLab with these changes to Instruments.java and Constants.java, Trumpet should be available to select from the dropdown.
If we select it and begin a test, then we should hear 5 random notes followed by a sustained random note, and the tests should continue as normal.
That's it!