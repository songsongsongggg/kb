#!/bin/bash
echo "publish----------"

process_id=`ps -ef | grep kb.jar | grep -v grep |awk '{print $2}'`
if [ $process_id ] ; then
sudo kill -9 $process_id
fi

source /etc/profile
nohup java -jar -Dspring.profiles.active=prod ~/kb/kb.jar > /dev/null 2>&1 &

echo "end publish"
