#!/bin/bash

./install.sh
retVal=$?
if [ $retVal -ne 0 ]; then
    echo "Error!!"
    exit $retVal
fi

./start.sh