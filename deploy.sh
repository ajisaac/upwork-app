#!/bin/zsh
mvn clean package
rsync -avz -e "ssh -i ~/.ssh/JobScraperKey.pem" ./target/upwork-0.0.1.jar ubuntu@server-host:/home/ubuntu/upwork/upwork-0.0.1.jar
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl restart upwork.service"
