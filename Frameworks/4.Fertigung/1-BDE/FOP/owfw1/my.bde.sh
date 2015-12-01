#!/bin/sh
umask 006
eval `sh denv.sh`
#
#
# Wenn ein Mandant eingetragen wird, dann muss auch ein Passwort
# eingetragen werden
#
MANDANT=" "
PASSWD=" "
#
#
if [ "$MANDANT" == " " ]
then
   echo "DOCH  leer"
#  edpinfosys.sh -N BDEIMPORT -s'yimport,yrueckmeld' -w'1,1'
#  edpinfosys.sh -N BDEEXPORT -s'yladen,yexport' -w'1,1'
else
   echo "NICHT leer"
#  edpinfosys.sh -m $MANDANT -p $PASSWD -N BDEIMPORT -s'yimport,yrueckmeld' -w'1,1'
#  edpinfosys.sh -m $MANDANT -p $PASSWD -N BDEEXPORT -s'yladen,yexport' -w'1,1'
fi
