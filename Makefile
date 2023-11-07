# Define the main class for the client and server
CLIENT_MAIN_CLASS = client.PantryPalsApp
SERVER_MAIN_CLASS = server.PanetryPalsServer

# Java source and test directories
SRC_DIR = src
TEST_DIR = test

# Java compiler and flags
JAVAC = javac
CLASSPATH = lib:src/client/components
VMARGS = --module-path './lib' --add-modules javafx.controls,javafx.fxml,org.json

# Define the source and test files
CLIENT_SRC_FILES = $(wildcard $(SRC_DIR)/client/*.java) $(wildcard $(SRC_DIR)/client/*/*.java)
SERVER_SRC_FILES = $(wildcard $(SRC_DIR)/server/*.java) $(wildcard $(SRC_DIR)/server/*/*.java)
CLIENT_TEST_FILES = $(wildcard $(TEST_DIR)/client/*.java)
SERVER_TEST_FILES = $(wildcard $(TEST_DIR)/server/*.java)

# Default target
all: compile test

# Compile the source code
compile:
	# @mkdir -p bin/server
	# @mkdir -p bin/client
	@echo "Compiling client source files..."
	$(JAVAC) $(VMARGS) -cp $(CLASSPATH) $(CLIENT_SRC_FILES)
	@echo "Compiling server source files..."
	$(JAVAC)  $(VMARGS) -cp $(CLASSPATH) $(SERVER_SRC_FILES)

# Run tests using JUnit
test: compile
	@echo "Running client tests..."
	java -cp $(CLASSPATH) org.junit.runner.JUnitCore client.SampleTest
	@echo "Running server tests..."
	java -cp $(CLASSPATH) org.junit.runner.JUnitCore server.SampleTest

# Run the client application
run-client: compile
	@echo "Running the client application..."
	java -cp src $(VMARGS) $(CLIENT_MAIN_CLASS)

# Run the server application
run-server: compile
	@echo "Running the server application..."
	java -cp src $(SERVER_MAIN_CLASS)

# Clean compiled files
clean:
	@echo "Cleaning up..."
	@rm -f src/client/*.class
	@rm -f src/client/components/*.class
	@rm -f src/server/*.class

.PHONY: all compile test run-client run-server clean
