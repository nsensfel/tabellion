## Parameters ##################################################################
#### Where to find the model
ifndef MODEL_DIR
MODEL_DIR =
endif

#### Where to store the Instance model
ifndef INSTANCE_MODEL_DIR
INSTANCE_MODEL_DIR =
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

ifeq ($(strip $(INSTANCE_MODEL_DIR)),)
$(error No INSTANCE_MODEL_DIR defined as parameter.)
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
MODEL_FILES = $(MODEL_DIR)/structural.mod
OUTPUT_FILE = $(INSTANCE_MODEL_DIR)/instances.mod

## Makefile Rules ##############################################################
compile: $(CLASSES)

model: $(CLASSES) $(INSTANCE_MODEL_DIR) $(OUTPUT_FILE)

clean:
	rm -f $(CLASSES)
	rm -f $(PATH_MODEL_DIR)/*.mod

clean_model:
	rm -f $(PATH_MODEL_DIR)/*.mod
########
%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

$(OUTPUT_FILE): $(MODEL_FILE) $(CLASSES)
	$(JAVA) -cp $(CLASSPATH) Main $< $@

$(INSTANCE_MODEL_DIR):
	mkdir -p $(INSTANCE_MODEL_DIR)

