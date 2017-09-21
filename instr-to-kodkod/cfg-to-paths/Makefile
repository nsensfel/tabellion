## Parameters ##################################################################
#### Where to find the model
MODEL_DIR ?=

#### Where to store the CFG models
PATH_MODEL_DIR ?=

#### Binaries
###### JRE binary
JAVA ?= java

###### JDK binary
JAVAC ?= javac

## Parameters Sanity Check #####################################################
ifeq ($(strip $(MODEL_DIR)),)
$(error No MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(PATH_MODEL_DIR)),)
$(error No PATH_MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

################################################################################
CLASSPATH = "./src/"
PATTERN = 's/\(is_start_node ([0-9]+).*/\1/p'

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)
CFG_MODEL_FILES = $(wildcard $(MODEL_DIR)/cfg_*.mod)
PATH_MODEL_FILES = $(addprefix $(PATH_MODEL_DIR)/,$(notdir $(CFG_MODEL_FILES)))


## Makefile Rules ##############################################################
compile: $(CLASSES)

model: $(CLASSES)  $(PATH_MODEL_DIR) $(PATH_MODEL_FILES)

clean:
	rm -f $(CLASSES)
	rm -f $(PATH_MODEL_DIR)/*.mod

clean_model:
	rm -f $(PATH_MODEL_DIR)/*.mod
########
%.class: %.java
	$(JAVAC) -cp $(CLASSPATH) $<

$(PATH_MODEL_DIR)/%: $(MODEL_DIR)/% $(CLASSES)
	$(eval ROOT_NODE := $(shell cat $< | grep 'is_start_node' | sed -En $(PATTERN)))
	$(JAVA) -cp $(CLASSPATH) Main $< $(ROOT_NODE) "p$(ROOT_NODE)_" $@

$(PATH_MODEL_DIR):
	mkdir -p $(PATH_MODEL_DIR)

