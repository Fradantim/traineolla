#!/bin/bash

MAVEN_OPTS=-DskipTests=true

cd ../app/oreport-parent
chmod +x mvnw
./mvnw clean install $MAVEN_OPTS
retVal=$?
if [ $retVal -ne 0 ]; then
    echo "Error!!"
    exit $retVal
fi
cd ..

for project in *
do 
    if [ ! "$project" = "oreport-parent" ]; then
        # echo $project
        cd "$project"
        chmod +x mvnw
        ./mvnw clean package $MAVEN_OPTS
        retVal=$?
        if [ $retVal -ne 0 ]; then
            echo "Error!!"
            exit $retVal
        fi
        cd ..
    fi
done

exit 0