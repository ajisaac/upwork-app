#!/bin/zsh

rsync -avz -e "ssh -i ~/.ssh/JobScraperKey.pem" ./upwork.service ubuntu@server-host:~/upwork/upwork.service
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo cp ~/upwork/upwork.service /etc/systemd/system/upwork.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl stop upwork.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl disable upwork.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl start upwork.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl enable upwork.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl daemon-reload"

