## Parameters ##################################################################
#### Where to find the level files
LEVEL_FILES ?= $(wildcard $(LEVELS_DIR)/*.lvl)

#### Where to find the properties to verify
PROPERTIES_DIR ?=

#### What are the targetted properties
PROPERTIES ?=

#### Where to find the properties to verify
ALL_PROPERTY_FILES ?= $(wildcard $(PROPERTIES_DIR)/*.pro)

MODEL_DIR ?=

#### Where to store & find the PATH models
PATH_MODEL_DIR ?= $(MODEL_DIR)/path/

#### Where to output the solutions.
SOL_DIR ?=

#### Where to get the missing Jar files.
JAR_SOURCE ?= "https://noot-noot.org/tabellion/jar/"

#### Binaries
###### JRE binary
JAVA = java

###### JDK binary
JAVAC = javac

##### Downloader
DOWNLOADER = wget

## Parameters Sanity Check #####################################################
ifeq ($(strip $(LEVEL_FILES)),)
$(error No LEVEL_FILES defined as parameter.)
endif

ifeq ($(strip $(PROPERTIES_DIR)),)
$(error No PROPERTIES_DIR defined as parameter.)
endif

ifeq ($(strip $(PROPERTIES)),)
$(error No PROPERTIES defined as parameter.)
endif

ifeq ($(strip $(MODEL_DIR)),)
$(error No MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(PATH_MODEL_DIR)),)
$(error No PATH_MODEL_DIR defined as parameter.)
endif

ifeq ($(strip $(SOL_DIR)),)
$(error No SOL_DIR defined as parameter.)
endif

ifeq ($(strip $(JAR_SOURCE)),)
$(error No JAR_SOURCE defined as parameter.)
endif

ifeq ($(strip $(JAVA)),)
$(error No Java executable defined as parameter.)
endif

ifeq ($(strip $(JAVAC)),)
$(error No Java compiler defined as parameter.)
endif

ifeq ($(strip $(DOWNLOADER)),)
$(error No Java executable defined as parameter.)
endif

################################################################################
CLASSPATH = "kodkod.jar:./src/:./parser/:org.sat4j.core.jar:antlr-4.7-complete.jar"
REQUIRED_JARS = kodkod.jar org.sat4j.core.jar antlr-4.7-complete.jar
ANTLR_JAR = ${CURDIR}/antlr-4.7-complete.jar

## Makefile Magic ##############################################################
SOURCES = $(wildcard src/*.java)
CLASSES = $(SOURCES:.java=.class)
SOLUTION_FILES = $(addsuffix .sol,$(addprefix $(SOL_DIR)/,$(notdir $(PROPERTIES))))
ALL_SOLUTION_FILES = $(addsuffix .sol,$(addprefix $(SOL_DIR)/,$(notdir $(basename $(ALL_PROPERTY_FILES)))))
DEPENDENCY_FILES = \
	$(addprefix $(DEPENDENCIES_DIR)/,$(notdir $(ALL_PROPERTY_FILES:.pro=.deps)))
MODEL_FILES = \
	$(MODEL_DIR)/structural.mod \
	$(MODEL_DIR)/depths.mod \
	$(MODEL_DIR)/string_to_instr.map \
	$(wildcard $(MODEL_DIR)/cfg_*.mod) \
	$(wildcard $(MODEL_INSTANCES_DIR)/*.mod)
PARSER_SOURCES = $(wildcard parser/*.g4)
PARSER_CLASSES = $(PARSER_SOURCES:.g4=.class)

export
## Makefile Rules ##############################################################
compile: $(PARSER_CLASSES) $(CLASSES)
	$(MAKE) -C cfg-to-paths compile

model: cfg-generator

solutions: cfg-generator $(REQUIRED_JARS) $(PARSER_CLASSES) $(CLASSES) \
	$(SOLUTION_FILES)

clean:
	$(MAKE) -C parser clean
	$(MAKE) -C cfg-to-paths clean
	rm -f $(CLASSES)
	rm -f $(SOL_DIR)/*.sol
	rm -f $(SOL_DIR)/*.sol.ready
	rm -f $(DEPENDENCY_FILES)
	rm -f $(wildcard $(MODEL_INFERRED_DIR)/*.mod)

clean_model:
	$(MAKE) -C cfg-to-paths clean_model
	rm -f $(wildcard $(MODEL_INFERRED_DIR)/*.mod)

clean_solutions:
	rm -f $(SOL_DIR)/*.sol
	rm -f $(SOL_DIR)/*.sol.ready
	rm -f $(DEPENDENCY_FILES)

########
cfg-generator:
	$(MAKE) -C cfg-to-paths model

$(PARSER_CLASSES): antlr-4.7-complete.jar kodkod.jar $(PARSER_SOURCES)
	$(MAKE) -C parser

-include $(wildcard $(DEPENDENCIES_DIR)/*)

$(ALL_SOLUTION_FILES): $(SOL_DIR)/%.sol: $(PROPERTIES_DIR)/%.pro \
	$(SOL_DIR)/%.sol.ready $(MODEL_FILES) $(LEVEL_FILES)
#	$(addprefix $(SOL_DIR)/,$(addsuffix .sol,$($(DEPENDENCIES_DIR)/%.deps)))
	echo "$^"
	$(JAVA) -cp $(CLASSPATH) Main $@ -v \
		$< \
		$(LEVEL_FILES) \
		$(MODEL_FILES) \
		`ls $(MODEL_INFERRED_DIR)/*.mod 2>/dev/null`\
		$(wildcard $(PATH_MODEL_DIR)/*.mod)
	echo "" >> $@
	$(MAKE) -C .. \
		PROPERTIES=$(basename $(notdir $@)) \
		TEMPLATE_DIR=$(TO_PRED_TEMPLATE_DIR) \
		NICE_MESSAGE=$(MODEL_INFERRED_DIR)/$(basename $(notdir $@)).mod

src/%.class: src/%.java $(PARSER_CLASSES) $(REQUIRED_JARS)
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'..."
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"

