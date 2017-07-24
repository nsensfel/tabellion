INPUT_FILE = ../data/ast/best_chronometer_ever.xml

## Executables #################################################################
JAVAC = javac
JAVA = java
DOWNLOADER = wget

## Java Config #################################################################
CLASSPATH = "./src/"

## Dependencies ################################################################
JAR_SOURCE = https://noot-noot.org/onera_2017/jar/
REQUIRED_JARS =

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)

## Makefile Rules ##############################################################

all: structural.mod

clean:
	rm -f $(CLASSES)
	rm -f *.mod

structural.mod: $(CLASSES) $(REQUIRED_JARS) $(INPUT_FILE)
	$(JAVA) -cp $(CLASSPATH) Main $(INPUT_FILE)

%.class: %.java $(REQUIRED_JARS)
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'"
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"
