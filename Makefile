.PHONY: all

all: Stalker.class UpdateTwitterStatus.class

Stalker.class: src/Stalker.java
	javac -classpath ~/twitter4j-4.0.4/lib/twitter4j-core-4.0.4.jar src/Stalker.java

UpdateTwitterStatus.class: src/UpdateTwitterStatus.java
	javac -classpath ~/twitter4j-4.0.4/lib/twitter4j-core-4.0.4.jar src/UpdateTwitterStatus.java