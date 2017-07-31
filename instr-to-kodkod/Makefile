## Target(s) Configuration #####################################################
#MODEL_FILES = $(wildcard ../data/instructions/*.mod)
MODEL_FILES = \
	../ast-to-instr/structural.mod \
	../ast-to-instr/depths.mod \
	$(wildcard ../ast-to-instr/cfg_*.mod) \
	$(wildcard ../cfg-to-paths/*.mod)
MAP_FILES = $(wildcard ../ast-to-instr/*.map)
LEVEL_DIR = $(wildcard ../data/level/*.lvl)

PROPERTIES = \
	../data/property/combinational_processes.pro \
	../data/property/likely_a_clock.pro

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
GLOBAL_INPUT_FILES = $(MODEL_FILES) $(LEVEL_DIR) $(MAP_FILES)
SOURCES = $(wildcard src/*.java parser/*.java)
GRAMMARS = $(wildcard parser/*.g4)
CLASSES = $(SOURCES:.java=.class)
SOLUTIONS = $(PROPERTIES:.pro=.sol)

## Makefile Rules ##############################################################
run: $(SOLUTIONS)

all: parser/PropertyParser.java $(CLASSES)
	$(MAKE) -C parser
	$(MAKE) -C ../ast-to-instr
	$(MAKE) -C ../cfg-to-paths

clean:
	rm -f $(CLASSES)
	$(MAKE) -C parser clean
	$(MAKE) -C ../ast-to-instr clean
	$(MAKE) -C ../cfg-to-paths clean

%.sol: %.pro parser/PropertyParser.java $(CLASSES) $(REQUIRED_JARS)
	echo "Solving \"$<\"..."
	$(MAKE) -C ../ast-to-instr
	$(MAKE) -C ../cfg-to-paths
	$(JAVA) -cp $(CLASSPATH) Main $@ $< $(GLOBAL_INPUT_FILES)

%.class: %.java $(REQUIRED_JARS) parser/PropertyParser.java
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'"
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"

parser/PropertyParser.java: antlr-4.7-complete.jar $(GRAMMAR)
	$(MAKE) -C parser
