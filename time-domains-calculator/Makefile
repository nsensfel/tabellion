## Parameters ##################################################################
#### Where to find the model
ifndef MODEL_DIR
MODEL_DIR =
endif

#### Where to store the Instance model
ifndef MODEL_TIME_DOMAINS_DIR
MODEL_TIME_DOMAINS_DIR =
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

ifeq ($(strip $(MODEL_TIME_DOMAINS_DIR)),)
$(error No MODEL_TIME_DOMAINS_DIR defined as parameter.)
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
MODEL_FILES = $(wildcard $(MODEL_DIR)/*.mod)
OUTPUT_FILE = $(MODEL_TIME_DOMAINS_DIR)/time_domains.mod

## Makefile Rules ##############################################################
compile: $(CLASSES)

model: $(CLASSES) $(OUTPUT_FILE)

solutions:

clean:
	rm -f $(CLASSES)
	rm -f $(MODEL_TIME_DOMAINS_DIR)/*

clean_model:
	rm -f $(MODEL_TIME_DOMAINS_DIR)/*

clean_solutions:

########
%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

$(OUTPUT_FILE): $(MODEL_FILES) $(CLASSES) $(MODEL_TIME_DOMAINS_DIR)
	$(JAVA) -cp $(CLASSPATH) Main $(OUTPUT_FILE) $(MODEL_FILES)

$(MODEL_TIME_DOMAINS_DIR):
	mkdir -p $(MODEL_TIME_DOMAINS_DIR)

