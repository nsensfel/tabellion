## Parameters ##################################################################
#### Where to find the model
ifndef MODEL_DIR
MODEL_DIR =
endif

#### Where to store the Instance model
ifndef MODEL_INSTANCES_DIR
MODEL_INSTANCES_DIR =
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
OUTPUT_FILE = $(MODEL_INSTANCES_DIR)/instances.mod

## Makefile Rules ##############################################################
compile: $(CLASSES)

model: $(CLASSES) $(MODEL_INSTANCES_DIR) $(OUTPUT_FILE)

solutions:

clean:
	rm -f $(CLASSES)
	rm -f $(PATH_MODEL_DIR)/*.mod

clean_model:
	rm -f $(PATH_MODEL_DIR)/*.mod

clean_solutions:

########
%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

$(OUTPUT_FILE): $(MODEL_FILE) $(CLASSES)
	$(JAVA) -cp $(CLASSPATH) Main $(MODEL_FILE) "inst_" $(MODEL_INSTANCES_DIR)

$(MODEL_INSTANCES_DIR):
	mkdir -p $(MODEL_INSTANCES_DIR)

