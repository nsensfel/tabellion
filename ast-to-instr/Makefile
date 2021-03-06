## Parameters ##################################################################
#### GHDL's xml AST output.
AST_FILE ?=

#### Where to output the models.
MODEL_DIR ?=

#### Binaries
###### JRE binary
JAVA ?= java

###### JDK binary
JAVAC ?= javac

## Parameters Sanity Check #####################################################
ifeq ($(strip $(AST_FILE)),)
$(error No AST_FILE defined as parameter.)
endif

ifeq ($(strip $(MODEL_DIR)),)
$(error No output directory defined for the models.)
endif

ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

## Java Config #################################################################
CLASSPATH = "./src/"

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)

## Makefile Rules ##############################################################

compile: $(CLASSES)

model: $(MODEL_DIR)/structural.mod

solutions: $(MODEL_DIR)/structural.mod

clean:
	rm -f $(CLASSES)
	rm -f $(MODEL_DIR)/*.mod
	rm -f $(MODEL_DIR)/*.map

clean_model:
	rm -f $(MODEL_DIR)/*.mod
	rm -f $(MODEL_DIR)/*.map

clean_solutions:

$(MODEL_DIR)/structural.mod: $(CLASSES) $(AST_FILE)
	$(JAVA) -cp $(CLASSPATH) Main $(AST_FILE) $(MODEL_DIR)

%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

$(MODEL_DIR):
	mkdir -p $@
