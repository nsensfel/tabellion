## Target(s) Configuration #####################################################
MODEL_FILE = "../data/instructions/example_1.sl"
LEVEL_DIR = "../data/level/"
## Executables #################################################################
JAVAC = javac
JAVA = java
DOWNLOADER = wget

## Java Config #################################################################
CLASSPATH = "kodkod.jar:./src/:./parser/:org.sat4j.core.jar:antlr-4.7-complete.jar"

## Dependencies ################################################################
JAR_SOURCE = https://noot-noot.org/onera_2017/jar/
REQUIRED_JARS = kodkod.jar org.sat4j.core.jar antlr-4.7-complete.jar

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java parser/*.java)
CLASSES = $(SOURCES:.java=.class)

## Makefile Rules ##############################################################

all: parser/PropertyParser.java $(CLASSES)

clean:
	rm -f $(CLASSES)
	$(MAKE) -C parser clean

run: parser/PropertyParser.java $(CLASSES) $(REQUIRED_JARS)
	$(JAVA) -cp $(CLASSPATH) Main $(LEVEL_DIR) $(MODEL_FILE)

%.class: %.java $(REQUIRED_JARS)
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'"
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"

parser/PropertyParser.java: antlr-4.7-complete.jar
	$(MAKE) -C parser
