# Overview #

There have been three major additions to the PitchLab application during the course
of this project. Firstly, PitchLab and its source code are now accessible via Google Code.
Secondly, all the source code has been documented in accordance to Javadoc standards. This
documentation is also available [here](http://pitch-lab.googlecode.com/svn/trunk/doc/index.html).
Thirdly, 8 instruments have been added in addition to the default Sine sound used during tests.
More importantly, however, PitchLab has been integrated with jMusic, allowing additional instruments
to be developed and/or added to PitchLab with little code manipulation.
Instructions for this can be found here [Adding Instruments](https://code.google.com/p/pitch-lab/wiki/Instruments)

PitchLab has also been integrated with jMusic. For more information about jMusic visit this url http://jmusic.ci.qut.edu.au/


# Details #

PitchLab is designed around the work of Vladimir Chaloupka. Details about the project can be found
at http://www.phys.washington.edu/users/vladi/pitch/JASA_94/abs_pitch.html.

[Launch PitchLab](http://pitch-lab.googlecode.com/svn/trunk/PitchLab.jnlp) - this is a BETA jnlp file.

If the above does not work you can download the PitchLab application via the "Downloads" tab above or by
clicking here [Download PitchLab with Instruments](http://pitch-lab.googlecode.com/files/!PitchLab-Instruments.jar)

# Accomplishments #

Time was spent cleaning up and documenting the PitchLab code, making it more efficient and legible for any future developer.
The fact that PitchLab is now under version control, hosted online and integrated with jMusic are major enhancements to the entire PitchLab workflow.
These improvements should lower the barrier of entry for anyone interested in using PitchLab to access the code and/or download the application.

Now that PitchLab is integrated with jMusic, it has created a way to add custom instruments to PitchLab.
Since jMusic is a Java standard for musical programming, there should be greater opportunity to customize how PitchLab behaves now.
This could possibly lead to new applications based on PitchLab that are more tightly integrated with jMusic and developed on top of the jMusic library.
For the time being, extending PitchLab through the jMusic library is an apt approach to enhancing PitchLab's capabilities.


# TODO #

The following are the list of where PitchLab improvements can be made:
  * Add instruments to all tests - currently instruments are only addable for a Passive Pitch Test.
  * Solidify a mechanism for sharing results via the web through a secure and stable web service.
  * Integrate PitchLab with Google App Engine for stable support, network, and data storage capabilities.
  * Overall User Interface enhancements.
  * Add additional desired enhancements here...