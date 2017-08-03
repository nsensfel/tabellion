## Parameters ##################################################################
#### ANTLR's Jar
ifndef ANTLR_JAR
ANTLR_JAR = ../antlr-4.7-complete.jar
endif

#### KODKOD's Jar
ifndef KODKOD_JAR
KODKOD_JAR = ../kodkod.jar
endif

#### Main program's sources
ifndef MAIN_PROGRAM_SRC
MAIN_PROGRAM_SRC = ../src/
endif

#### JRE binary
ifndef JAVA
JAVA = java
endif

#### JDK binary
ifndef JAVAC
JAVAC = javac
endif

## Parameters Sanity Check #####################################################
ifeq ($(strip $(ANTLR_JAR)),)
$(error No ANTLR_JAR defined as parameter.)
endif

ifeq ($(strip $(KODKOD_JAR)),)
$(error No ANTLR_JAR defined as parameter.)
endif

ifeq ($(strip $(MAIN_PROGRAM_SRC)),)
$(error No MAIN_PROGRAM_SRC defined as parameter.)
endif

ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

################################################################################
ANTLR_SOURCES = $(wildcard *.g4)
JAVA_SOURCES = $(ANTLR_SOURCES:.g4=.java)
CLASSES = $(JAVA_SOURCES:.java=.class)
CLASSPATH = ".:$(MAIN_PROGRAM_SRC):$(KODKOD_JAR):$(ANTLR_JAR)"

compile: $(CLASSES)

PropertyParser.java: PropertyLexer.g4  PropertyParser.g4

clean:
	rm -f *.java *.class *.tokens

# Pattern rules can be used to generate multiple target in a single action.
PropertyLexer%java PropertyParser%java: $(ANTLR_SOURCES)
	$(JAVA) -jar $(ANTLR_JAR) $^

%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<
