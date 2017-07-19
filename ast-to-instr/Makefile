INPUT_FILE = ../data/ast/*.xml

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

all: $(CLASSES)

clean:
	rm -f $(CLASSES)

run: $(CLASSES) $(REQUIRED_JARS)
	$(JAVA) -cp $(CLASSPATH) Main $(INPUT_FILE)

%.class: %.java $(REQUIRED_JARS)
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'"
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"
