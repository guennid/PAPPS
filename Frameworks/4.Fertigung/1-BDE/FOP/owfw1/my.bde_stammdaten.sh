#!/bin/sh
#
# Der Aufruf muss in einem Mandantenverzeichnis erfolgen.
# F�r diesen Mandanten wird dann der Export der Stammdaten gestartet.
#
umask 006
eval `sh denv.sh`
#
# Die untenstehend Zeile ist zu pflegen und zu aktivieren, wenn ein abas-ERP-Passwort zum Starten ben�tigt wird.
# EKSPASSWORT=batchpasswort; export EKSPASSWORT
#
batchlg.sh owfw1/BDE.EXPORT.MASCH
batchlg.sh owfw1/BDE.EXPORT.ARBG 

