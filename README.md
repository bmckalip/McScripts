# McScripts_Public
Public Repository containing code from certain McScripts that have been selectively open sourced.

# Installation instructions

1. download the desired script binaries found in the jars directory above.
2. Then, navigate to your dreambot installation location, add the script to the scripts directory. You may need to add a Folder with the name of the script and place the jar file inside of it for the script to be recognized.
3. Check back here occasionally or monitor the appropriate script's thread for updates and new feature.

# Compilation instructions
1. clone the repository, and add it to intellij
2. open the project settings and add the McLib module as a module dependency to script you are trying to compile, aswell as the dreambot client.jar found in the botdata directory of your dreambot installation.
3. add an artifact in the artifacts tab of the project settings set to compile to the scripts folder of your dreambot installation
4. open the build -> build artifacts tab and click on the script you want to compile
5. launch dreambot and navigate to local scripts, refresh and run the script.
