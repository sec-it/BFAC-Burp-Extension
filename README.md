# BFAC - Burp Extension

![Build](https://img.shields.io/badge/Built%20with-Java-Blue)
[![GitHub forks](https://img.shields.io/github/forks/sec-it/BFAC-Burp-Extension)](https://github.com/sec-it/BFAC-Burp-Extension/network)
[![GitHub stars](https://img.shields.io/github/stars/sec-it/BFAC-Burp-Extension)](https://github.com/sec-it/BFAC-Burp-Extension/stargazers)
[![GitHub](https://img.shields.io/github/license/sec-it/BFAC-Burp-Extension)](https://github.com/sec-it/BFAC-Burp-Extension/blob/master/LICENSE)

Burp Extension for [BFAC][bfac] (Advanced Backup-File Artifacts Testing for Web-Applications).

![Screenshot](static/screenshot_light.png)

## What is BFAC - Burp Extension ?

Backup files are too often overlooked by web application auditors. With the objective of democratizing the backup file tests and integrating these tests into the most used tool for web auditors, SEC-IT auditors worked on the integration of the BFAC checks as a BurpSuite plugin.

[BFAC][bfac] is an automated tool that checks for backup artifacts that may disclose the web-application's source code. The artifacts can also lead to leakage of sensitive information, such as passwords, directory structure, etc. This a tool provided by [@mazen160][mazen160].

[BurpSuite](https://portswigger.net/burp) is a well known pentesting tool used in web application assessment.

The pluggin is written in Java for better integration with BurpSuite Extender API.

## How to install ?

Download [BFAC.jar][jar] on your computer. Then import the jar file as a Burp plugin :

1. Open Burp
2. Click on the "Extender" tab
3. Click on the "Add" button
4. Set extension type to Java and load [BFAC.jar][jar] by clicking on "Select file..."

![Install step 1](static/install_step_1.png)

Once loaded, you should see a "BFAC" tab :

![Install step 2](static/install_step_2.png)

## How to use BFAC plugin ?

BFAC Burp Extension is designed to look for backup files from the Burpsuite [sitemap].
Therefore, **it is better to run BFAC Burp extension when the sitemap is full enough**.
Sitemap fulfillment will not be covered here, however it can be accomplish using active scanners.

Once your sitemap is full, you have 2 options.

### The old way

[BFAC][bfac] original tool may provide interesting options that are not provided by this extension. That why you can extract interesting URLs from the sitemap using the "Extract URL".

![Extract URL](static/extract_urls.png)

Then, you only have to provide the extracted URLs to the [BFAC][bfac] tool :

![BFAC Console](static/bfac_console.png)

### The Burp way

If you do not have [BFAC][bfac] on your computer or want to gain time, you can just click on "Run BFAC" to run a Java implementation of BFAC :

![BFAC Console](static/screenshot_dark.png)

## Why not using Burp scanner ?

Burp Scanner already provides a backup file feature for the Burp Suite Enterprise and Burp Suite Professional ([see #006000d8_backup-file](https://portswigger.net/kb/issues/006000d8_backup-file)).

1. Performing a backup file check only will require more manual configuration on Burp scanner.
2. Burp scanner will not cover multiple backup file formats (see [Benchmark][DIFF]).

> Note : This plugin has been considered by its author and PortSwigger as an alternative of the Burp scanner feature, that's why you won't find the extension on [BApp store][BApp].

## Build the JAR

If you want to contribute or modify the existing plugin, you may need to build an edited JAR. In order to accomplish that task, you may have a look at PortSwigger website : [Writing your first Burp Suite extension][devburp].

To build the jar, create new Java project under your Java IDE (i.e. Eclipse). Create new package named `burp`. Import "burp interface files" in this package. Theses files can be found in Burp tool : `Extender` tab > `APIs` > `Save interfaces files` (at the bottom left of the pane). Then import Java classes provided in the [src](src/) folder, into the `burp` package. You can now modify the plugin as needed, and generate a JAR plugin (on Eclipse: `File` > `Export` > `Java` > `JAR file` > Select project and click on `Finish`).

![Java IDE](static/java_ide.png)

## Acknowledgments

The [Site-map-extractor][sitemapextactor] BurpSuite plugin written by [@swright573][swright573] has been a great source of inspiration and helped us to better understand BurpSuite Extender API from a "sitemap" point of view.

## Author

Made by Alex G. ([@zeecka_](https://twitter.com/Zeecka_)), pentester at SEC-IT.

[bfac]:https://github.com/mazen160/bfac
[mazen160]:https://twitter.com/mazen160
[sitemapextactor]:https://github.com/swright573/site-map-extractor
[swright573]:https://github.com/swright573
[jar]:bin/BFAC.jar
[sitemap]:https://portswigger.net/burp/documentation/desktop/tools/target/site-map
[devburp]:https://portswigger.net/burp/extender/writing-your-first-burp-suite-extension
[BApp]:https://portswigger.net/bappstore
[DIFF]:DIFF.md
