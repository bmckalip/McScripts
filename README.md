# McScripts_Public
Public Repository containing code from certain McScripts for the Dreambot OSRS botting client that have been selectively open sourced.

## Support, releases and license

Status reviewed November 5, 2026. This repository contains selectively published Java source and bundled JAR files for the DreamBot Old School RuneScape client. Treat the installation and compilation notes below as historical instructions. Compatibility with the current DreamBot client, a clean build and the bundled binaries have not been verified in this review. No game client or script was run, and no binary was rebuilt.

There are currently no GitHub releases or tags. The default branch is named `release`; that name does not establish a tested release or a support commitment. The existing source and JAR files are not a newly validated distribution. Future compatibility claims or a new release require a separate build and representative runtime check.

The existing [MIT license](LICENSE) remains unchanged. Retain its copyright and permission notice when copying covered source. The DreamBot client is an external dependency; this repository's license does not grant rights to that client or to other third-party material. No license terms or attribution were added or replaced in this documentation update.

# Historical installation instructions

1. download the desired script binaries found in the jars directory above.
2. Then, navigate to your dreambot installation location, add the script to the scripts directory. You may need to add a Folder with the name of the script and place the jar file inside of it for the script to be recognized.
3. The support status above describes the verification limits; these historical notes do not promise updates.

# Historical compilation instructions
1. clone the repository, and add it to intellij
2. open the project settings and add the McLib module as a module dependency to script you are trying to compile, aswell as the dreambot client.jar found in the botdata directory of your dreambot installation.
3. add an artifact in the artifacts tab of the project settings set to compile to the scripts folder of your dreambot installation
4. open the build -> build artifacts tab and click on the script you want to compile
5. launch dreambot and navigate to local scripts, refresh and run the script.
