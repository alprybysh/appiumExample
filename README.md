## Demo project of Appium Framework usage

#### Requires:
Appium Java client  >= 6.1.0
Running instance of Appium server >= 1.7

#### Test Subject
Test application sharingApp.apk (added to /resources folder)

#### Functionality covered
1. New item can be created and added to the list.
2. Already existing item can be deleted from the list (shell backdoor is used to create an item before test starts).

#### How To Run 
1) Local run
``mvn clean test -DappiumUrl=appium_url``<br>
(by default for local runs -Dappium=http://127.0.0.1:4723/wd/hub)
2) Remote run (Kobiton):
``mvn clean verify -PkobitonRun -DkobitonRemotePath=<kobiton_app> -DappiumUrl=<appium_url>``<br>
in which
* <kobiton_app> (required) is the name of the app that is loaded in Kobiton Server
* <appium_url>(optional) is the remote url to Kobiton

3) Remote run (AWS Device farm): <br>
```mvn clean package -PawsRun -DskipTests``` to configure zip file of your project.
Then go to aws device farm and run tests there.
4) Remote run (Bitbar):<br ><br>
4.1 Create a zip file containing the project, which will be uploaded to Bitbar Cloud.
<br />On OSX/Linux machines you can just run the following command at the project's root directory:
  ``./zip_project_for_bitbar.sh`` 
  <br /> This creates a zip package called: tests.zip
<br><br>
4.2 Run tests on Jenkins via plugin "Bitbar Run-in-Cloud Plugin".
5) Local run (Bitbar):<br><br>
5.1 Run mvn command on root directory:
```mvn clean test -PbitbarLocalRun -DapiKey=<bitbar_apiKey> ```<br>
in which
* <bitbar_apiKey> is apiKey to Bitbar cloud. The apiKey is available under 'My account' in Bitbar cloud. <br><br>
NOTE: You can upload your application manually in .apk(for Android) or in .ipa (for iOS) format with command:
```curl -H "Accept: application/json" -u <bitbar_apiKey>: -F myAppFile=@"<path_to_the_app_on_local_machine>" http://appium.bitbar.com/upload```<br>
in which
* <bitbar_apiKey> is apiKey to Bitbar cloud. The apiKey is available under 'My account' in Bitbar cloud.
* <path_to_the_app_on_local_machine> path to the application on your local machine(e.g myAppFile=@"/Users/anna/Desktop/sharingApp.apk")<br><br>

6) Remote run on Browserstack cloud:
6.1 Run mvn command on root directory:
```mvn clean test -PbrowserstackRun -DappiumUrl=<browserstack_appium_url> -DuserName=<browserstack_user_name> -DaccessKey=<apiKey>```<br>
in which
* <browserstack_appium_url> is url to Browserstackthat has format: https://"+userName+":"+accessKey+"@hub-cloud.browserstack.com
* <browserstack_user_name> is user name
* <apiKey> secret accessKey

7) Remote run on Saucelabs(TestObject) cloud:
7.1 Run mvn command on root directory:
```mvn clean test -PsaucelabsRealDeviceRun -DapiKey=<saucelabs_api_key> -DuserName=<userName>```
in which
* <saucelabs_api_key> is user secret key that can be found in Account->profile
* <userName> is user name that can be found in Account->profile