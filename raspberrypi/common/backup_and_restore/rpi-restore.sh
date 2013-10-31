#!/bin/sh

# @TODO this script has not been verified to work.
# diskutil umount /dev/disk1 # is this necessary

$IMAGE=$2
$DEVICE=$1 # /dev/rdisk1 # rdisk is supposed to be 20x faster

sudo gzip -dc $IMAGE | dd bs=4m of=$DEVICE
