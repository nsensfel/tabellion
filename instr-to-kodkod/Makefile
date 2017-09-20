## Parameters ##################################################################
#### Where to find the level files
ifndef LEVEL_FILES
LEVEL_FILES =
endif

#### Where to find the properties to verify
ifndef PROPERTY_FILES
PROPERTY_FILES =
endif

#### Where to find the model
ifndef MODEL_DIR
MODEL_DIR =
endif

#### Where to store & find the PATH models
ifndef PATH_MODEL_DIR
PATH_MODEL_DIR = $(MODEL_DIR)/path/
endif

#### Where to output the solutions.
ifndef SOL_DIR
SOL_DIR =
endif

#### Where to get the missing Jar files.
ifndef JAR_SOURCE
JAR_SOURCE = "https://noot-noot.org/tabellion/jar/"
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

##### Downloader
ifndef DOWNLOADER
DOWNLOADER = wget
endif

## Parameters Sanity Check #####################################################
ifeq ($(strip $(LEVEL_FILES)),)
$(error No LEVEL_FILES defined as parameter.)
endif

ifeq ($(strip $(PROPERTY_FILES)),)
$(error No PROPERTY_FILES defined as parameter.)
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
SOLUTION_FILES = $(addprefix $(SOL_DIR)/,$(notdir $(PROPERTY_FILES:.pro=.sol)))
DEPENDENCY_FILES = \
	$(addprefix $(DEPENDENCIES_DIR)/,$(notdir $(PROPERTY_FILES:.pro=.deps)))
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
	rm -f $(DEPENDENCY_FILES)

clean_model:
	$(MAKE) -C cfg-to-paths clean_model

clean_solutions:
	rm -f $(SOL_DIR)/*.sol
	rm -f $(DEPENDENCY_FILES)

########
cfg-generator:
	$(MAKE) -C cfg-to-paths model

$(PARSER_CLASSES): antlr-4.7-complete.jar kodkod.jar $(PARSER_SOURCES)
	$(MAKE) -C parser

$(SOL_DIR)/%.sol: \
	$(DEPENDENCIES_DIR)/%.deps $(PROPERTY_FILES) $(MODEL_FILES) $(LEVEL_FILES)
	touch $@
	$(JAVA) -cp $(CLASSPATH) Main $@ -v \
		$(filter %$(basename $(notdir $@)).pro,$(PROPERTY_FILES)) \
		$(LEVEL_FILES) \
		$(MODEL_FILES) \
		$(wildcard $(PATH_MODEL_DIR)/*.mod)

$(DEPENDENCY_FILES): $(PROPERTY_FILES)
	sed -n 's/^#require \"\(.*\)\"$$/\1/p' \
		$(filter %$(basename $(notdir $@)).pro,$(PROPERTY_FILES)) > $@

src/%.class: src/%.java $(PARSER_CLASSES) $(REQUIRED_JARS)
	$(JAVAC) -cp $(CLASSPATH) $<

%.jar:
	echo "Attempting to download missing jar '$@'..."
	$(DOWNLOADER) "$(JAR_SOURCE)/$@"
