## Parameters ##################################################################
#### Where to find the model
MODEL_DIR ?=

#### Where to store the Instance model
MODEL_INSTANCES_DIR ?=

#### Binaries
###### JRE binary
JAVA ?= java

###### JDK binary
JAVAC ?= javac

## Parameters Sanity Check #####################################################
ifeq ($(strip $(MODEL_DIR)),)
$(error No MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(MODEL_INSTANCES_DIR)),)
$(error No MODEL_INSTANCES_DIR defined as parameter.)
endif

ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

################################################################################
CLASSPATH = "./src/"

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)
MODEL_FILE = $(MODEL_DIR)/structural.mod
OUTPUT_FILE = $(MODEL_INSTANCES_DIR)/witness

## Makefile Rules ##############################################################
compile: $(CLASSES)

model: $(CLASSES) $(MODEL_INSTANCES_DIR) $(OUTPUT_FILE)

solutions:

clean:
	rm -f $(CLASSES)
	rm -f $(MODEL_INSTANCES_DIR)/*

clean_model:
	rm -f $(MODEL_INSTANCES_DIR)/*

clean_solutions:

########
%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

$(OUTPUT_FILE): $(MODEL_FILE) $(CLASSES)
	$(JAVA) -cp $(CLASSPATH) Main $(MODEL_FILE) "inst_" $(MODEL_INSTANCES_DIR)
	touch $(OUTPUT_FILE)

$(MODEL_INSTANCES_DIR):
	mkdir -p $(MODEL_INSTANCES_DIR)

