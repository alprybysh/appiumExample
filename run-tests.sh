#!/bin/bash


startAppium(){
	if [ "$(uname)" == "Darwin" ]; then
		startAppiumOSX
	elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
		startAppiumLinux
	else
		echo "Unknown OS system, exiting..."
		exit 1
	fi
}

startAppiumOSX(){
	if [ -z ${UDID} ] ; then
		export UDID=${IOS_UDID}
	fi
		echo "UDID is ${UDID}"
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
    echo "Starting Appium on Mac for IOS tests..."
    export AUTOMATION_NAME="XCUITest"
	appium -U ${UDID} --log-no-colors --log-timestamp --relaxed-security
}

startAppiumLinux(){
	# Create the screenshots directory, if it doesn't exist'
	mkdir -p .screenshots
    echo "Starting Appium on Linux for Android tests..."
	appium --log-no-colors --log-timestamp --relaxed-security
}

executeTests(){
	echo "Extracting tests.zip..."
	unzip tests.zip
	echo "Running Android Tests..."
	mvn clean test -PbitbarRemoteRun
	echo "Finished Running Tests!"
	cp target/surefire-reports/junitreports/TEST-*.xml TEST-all.xml
}

startAppium

executeTests