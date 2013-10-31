#!/bin/sh

$DEVICE=$1 # /dev/rdisk1 # rdisk is supposed to be 20x faster

sudo dd bs=4m if=$DEVICE | gzip > ./image`date +%Y%m%d`.gz
