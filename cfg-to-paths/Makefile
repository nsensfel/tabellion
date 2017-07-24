## Target(s) Configuration #####################################################
MODEL_FILES = $(wildcard ../ast-to-instr/cfg_*.mod)
OUTPUT_FILES = $(patsubst ../ast-to-inst/%.mod,%.cfg.mod,$(MODEL_FILES))

## Executables #################################################################
JAVAC = javac
JAVA = java

## Java Config #################################################################
CLASSPATH = "./src/"

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)

## Makefile Rules ##############################################################

all: $(CLASSES) $(OUTPUT_FILES)

clean:
	rm -f $(CLASSES)
	rm -f *.mod

#run: $(CLASSES)
#	$(JAVA) -cp $(CLASSPATH) Main $(MODEL_FILE) $(ROOT_NODE) $(ID_PREFIX) \
#		$(OUTPUT_FILE)

%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

%.cfg.mod: ../ast-to-instr/%.mod $(CLASSES)
	$(eval ROOT_NODE := $(shell head -n 2 $< | tail -n 1 | grep "[0-9]\+" -o))
	$(JAVA) -cp $(CLASSPATH) Main $< $(ROOT_NODE) "p$(ROOT_NODE)_" $@
