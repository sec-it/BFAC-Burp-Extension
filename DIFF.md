# Difference between BFAC - Burp Extension, BFAC and  Burp Scanner

The following table are the tests performed for the file `index.php`. The benchmark was made the 09 august 2021.

Test file | Extension | BFAC | Burp
 -------- | --------- | ---- | ---
%20index.php | X | X |   |
%23index.php%23 | X | X |   |
.~index.php | X | X |   |
.~lock.index.php%23 | X | X |   |
Copy%20index.php | X | X |   |
Copy%20of%20index.php | X | X | X |
Copy_(1)_of_index.php | X | X |   |
Copy_(2)_of_index.php | X | X |   |
Copy_index.php | X | X |   |
Copy_of_index.php | X | X |   |
_index.php | X | X |   |
_index.php.swo | X | X |   |
_index.php.swp | X | X |   |
backup-index.php | X | X | X |
backup_index.php | X |   |   |
bak-index.php | X |   |   |
bak_index.php | X |   | X |
copy%20of%20index.php | X | X |   |
index%20%28copy%29.php | X | X | X |
index%20-%20Copy.php | X | X | X |
index%20copy.php | X | X |   |
index-backup.php | X | X |   |
index-bkp.php | X | X |   |
index.1 | X |   | X |
index.7z | X |   | X |
index.a |   |   | X |
index.ar | X |   | X |
index.bac |   |   | X |
index.back | X | X |   |
index.backup | X | X | X |
index.bak | X | X | X |
index.bak.sql | X | X |   |
index.bak.sql.bz2 | X | X |   |
index.bak.sql.gz | X | X |   |
index.bak.sql.tar.gz | X | X |   |
index.bak1 | X | X |   |
index.bakup | X | X |   |
index.bakup1 | X | X |   |
index.bck | X | X |   |
index.bkp | X | X |   |
index.bz2 | X |   | X |
index.cbz | X |   | X |
index.ear | X |   | X |
index.exe | X |   | X |
index.gz | X |   | X |
index.inc | X |   | X |
index.include | X |   | X |
index.jar | X |   | X |
index.lzma | X |   | X |
index.old | X | X | X |
index.orig | X | X |   |
index.original | X | X |   |
index.php%00 | X | X |   |
index.php%01 | X | X |   |
index.php%23 | X | X |   |
index.php-bak | X | X |   |
index.php. | X | X |   |
index.php.0 | X | X |   |
index.php.1 | X | X | X |
index.php.2 | X | X |   |
index.php.7z |   |   | X |
index.php.a |   |   | X |
index.php.ar | X |   | X |
index.php.arc | X | X |   |
index.php.bac | X | X | X |
index.php.back | X | X |   |
index.php.backup | X | X | X |
index.php.bak | X | X | X |
index.php.bakup | X | X |   |
index.php.bck | X | X |   |
index.php.bk | X | X |   |
index.php.bkp | X | X |   |
index.php.bz2 | X |   | X |
index.php.cbz | X |   | X |
index.php.conf | X | X |   |
index.php.copy | X | X |   |
index.php.cs | X | X |   |
index.php.csproj | X | X |   |
index.php.default | X | X |   |
index.php.ear | X |   | X |
index.php.exe | X |   | X |
index.php.gz | X |   | X |
index.php.inc | X | X |   |
index.php.jar | X |   | X |
index.php.lst | X | X |   |
index.php.lzma | X |   | X |
index.php.nsx | X | X |   |
index.php.old | X | X | X |
index.php.org | X | X |   |
index.php.orig | X | X |   |
index.php.original | X | X |   |
index.php.rar | X | X | X |
index.php.sav | X | X |   |
index.php.save | X | X |   |
index.php.saved | X | X |   |
index.php.swo | X | X |   |
index.php.swp | X | X |   |
index.php.tar | X | X | X |
index.php.tar.7z | X |   | X |
index.php.tar.bz2 | X |   | X |
index.php.tar.gz | X | X | X |
index.php.tar.lzma | X |   | X |
index.php.tar.xz | X |   | X |
index.php.temp | X | X |   |
index.php.tmp | X | X |   |
index.php.tpl | X | X |   |
index.php.txt | X | X |   |
index.php.vb | X | X |   |
index.php.war | X |   | X |
index.php.wim | X |   | X |
index.php.xz | X |   | X |
index.php.zip | X | X | X |
index.php::$DATA | X | X |   |
index.php1 | X |   | X |
index.php_ | X | X |   |
index.php_backup | X |   | X |
index.php_bak | X | X | X |
index.php_old | X | X | X |
index.phpbak | X |   | X |
index.phpinc | X |   | X |
index.phpold | X |   | X |
index.php~ | X | X | X |
index.php~~ | X | X |   |
index.rar | X | X | X |
index.save | X | X |   |
index.saved | X | X |   |
index.sql | X | X |   |
index.sql.gz | X | X |   |
index.tar | X | X | X |
index.tar.7z | X |   | X |
index.tar.bz2 | X |   | X |
index.tar.gz | X | X | X |
index.tar.lzma | X |   | X |
index.tar.xz | X |   | X |
index.temp | X | X |   |
index.tmp | X | X |   |
index.tpl | X | X |   |
index.txt | X | X |   |
index.war | X |   | X |
index.wim | X |   | X |
index.xz | X |   | X |
index.zip | X | X | X |
index1 | X |   | X |
index1.php | X |   | X |
index_backup | X |   | X |
index_backup.php | X |   | X |
index_bak | X |   | X |
index_bak.php | X |   | X |
index_old | X |   | X |
index_old.php | X |   | X |
indexbak | X |   | X |
indexinc | X |   | X |
indexold | X |   | X |
~%24index.php | X | X |   |
~index.php | X | X |   |
~index.tmp | X | X |   |
.index.php.swo | X | X |   |
.index.php.swp | X | X |   |

The following checks are also provided by BFAC or Burp scanner but might be not related to backup files (mostly DVCS and random files).

Test file | Extension | BFAC | Burp
 -------- | --------- | ---- | ---
aozokrj.php.tar.gz |   |   | X |
oxnilea.php.tar.gz |   |   | X |
txstvxx.php.tar.gz |   |   | X |
yhxxluv.php.tar.gz |   |   | X |
composer.lock | X | X |   |
CVS/Entries | X | X |   |
.DS_Store | X | X |   |
.bzr/README | X | X |   |
.bzr/checkout/dirstate | X | X |   |
.cvsignore | X | X |   |
.git-credentials | X | X |   |
.git/HEAD | X | X |   |
.git/config | X | X |   |
.git/index | X | X |   |
.gitignore | X | X |   |
.hg/requires | X | X |   |
.hg/store/fncache | X | X |   |
.idea/misc.xml | X | X |   |
.idea/workspace.xml | X | X |   |
.svn/all-wcprops | X | X |   |
.svn/entries | X | X |   |
.svn/wc.db | X | X |   |
.svnignore | X | X |   |
/.DS_Store | X | X |   |
/.bzr/README | X | X |   |
/.bzr/checkout/dirstate | X | X |   |
/.cvsignore | X | X |   |
/.git-credentials | X | X |   |
/.git/HEAD | X | X |   |
/.git/config | X | X |   |
/.git/index | X | X |   |
/.gitignore | X | X |   |
/.hg/requires | X | X |   |
/.hg/store/fncache | X | X |   |
/.idea/misc.xml | X | X |   |
/.idea/workspace.xml | X | X |   |
/.svn/all-wcprops | X | X |   |
/.svn/entries | X | X |   |
/.svn/wc.db | X | X |   |
/.svnignore | X | X |   |
/CVS/Entries | X | X |   |
/composer.lock | X | X |   |
