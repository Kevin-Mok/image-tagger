#!/bin/bash
# Execute from the phase1/src directory
mkdir ../out/class_files
javac -cp /local/packages/idea-IC-172.4155.36/plugins/junit/lib/junit-jupiter-api-5.0.0.jar:/local/packages/idea-IC-172.4155.36/plugins/junit/lib/opentest4j-1.0.0.jar  @sources.txt -d ../out/class_files/
cp -a fx/resources/ ../out/class_files/fx/
cp fx/layout.fxml ../out/class_files/fx/
java -cp ../out/class_files fx.Main
