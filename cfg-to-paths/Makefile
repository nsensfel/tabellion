## Target(s) Configuration #####################################################
MODEL_FILE = "../data/instructions/example_process.pl"
ROOT_NODE = "237"
ID_PREFIX = "p237_"
OUTPUT_FILE = "$(MODEL_FILE).kk"

## Executables #################################################################
JAVAC = javac
JAVA = java

## Java Config #################################################################
CLASSPATH = "./src/"

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)

## Makefile Rules ##############################################################

all: $(CLASSES)

clean:
	rm -f $(CLASSES)

run: $(CLASSES)
	$(JAVA) -cp $(CLASSPATH) Main $(MODEL_FILE) $(ROOT_NODE) $(ID_PREFIX) \
		$(OUTPUT_FILE)

%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<
