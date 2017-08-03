## Parameters ##################################################################
#### GHDL's xml AST output.
ifndef AST_FILE
AST_FILE =
endif

#### Where to output the models.
ifndef MODEL_DIR
MODEL_DIR =
endif

#### Binaries
###### JRE binary
ifndef JAVA
JAVA = java
endif

###### JDK binary
ifndef JAVAC
JAVAC = javac
endif

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

$(MODEL_DIR)/structural.mod: $(CLASSES) $(AST_FILE)
	$(JAVA) -cp $(CLASSPATH) Main $(AST_FILE) $(MODEL_DIR)

clean:
	rm -f $(CLASSES)
	rm -f $(MODEL_DIR)/*.mod

%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

$(MODEL_DIR):
	mkdir -p $@
