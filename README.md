# BFAC - Burp Extension

Burp Extension for BFAC (Advanced Backup-File Artifacts Testing for Web-Applications).

![Screenshot](static/screenshot_light.png)

## What is BFAC - Burp Extension ?

Backup files are too often overlooked by web application auditors. With the objective of democratizing the backup file tests and integrating these tests into the most used tool for web auditors, SEC-IT auditors worked on the integration of the BFAC checks as a BurpSuite plugin.

[BFAC](https://github.com/mazen160/bfac) is an automated tool that checks for backup artifacts that may disclose the web-application's source code. The artifacts can also lead to leakage of sensitive information, such as passwords, directory structure, etc. This a tool provided by [@mazen160](https://twitter.com/mazen160).

[BurpSuite](https://portswigger.net/burp) is a well known pentesting tool used in web application assessment.

The pluggin is written in Java for better integration with BurpSuite Extender API.

## Acknowledgments

The [Site-map-extractor](https://github.com/swright573/site-map-extractor)  BurpSuite plugin written by [swright573](https://github.com/swright573) has been a great source of inspiration and helped us to better understand BurpSuite Extender API from a "site-map" point of view.
