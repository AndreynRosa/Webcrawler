# Webcrawler
This project is a webcrawler with java



# To Run Project
docker build . -t axreng/backend
docker run
-e BASE_URL=http://hiring.axreng.com/
-p 4567:4567 --rm axreng/backend
