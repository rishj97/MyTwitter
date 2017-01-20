.PHONY: all

all: TwitterLikes.class UpdateTwitterStatus.class

TwitterLikes.class: src/TwitterLikes.java
	javac -classpath ~/twitter4j-4.0.4/lib/twitter4j-core-4.0.4.jar src/TwitterLikes.java

UpdateTwitterStatus.class: src/UpdateTwitterStatus.java
	javac -classpath ~/twitter4j-4.0.4/lib/twitter4j-core-4.0.4.jar src/UpdateTwitterStatus.java