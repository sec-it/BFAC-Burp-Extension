# BFAC - Burp Extension

Burp Extension for [BFAC][bfac] (Advanced Backup-File Artifacts Testing for Web-Applications).

![Screenshot](static/screenshot_light.png)

## What is BFAC - Burp Extension ?

Backup files are too often overlooked by web application auditors. With the objective of democratizing the backup file tests and integrating these tests into the most used tool for web auditors, SEC-IT auditors worked on the integration of the BFAC checks as a BurpSuite plugin.

[BFAC][bfac] is an automated tool that checks for backup artifacts that may disclose the web-application's source code. The artifacts can also lead to leakage of sensitive information, such as passwords, directory structure, etc. This a tool provided by [@mazen160][mazen160].

[BurpSuite](https://portswigger.net/burp) is a well known pentesting tool used in web application assessment.

The pluggin is written in Java for better integration with BurpSuite Extender API.

## Acknowledgments

The [Site-map-extractor][sitemapextactor] BurpSuite plugin written by [@swright573][swright573] has been a great source of inspiration and helped us to better understand BurpSuite Extender API from a "site-map" point of view.

[bfac]:https://github.com/mazen160/bfac
[mazen160]:https://twitter.com/mazen160
[sitemapextactor]:https://github.com/swright573/site-map-extractor
[swright573]:https://github.com/swright573