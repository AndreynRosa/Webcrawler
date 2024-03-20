# Webcrawler
Introduction
This README provides an overview of our Java-based web crawler. The web crawler is designed to handle over 100 requests per second efficiently. Additionally, it offers runtime monitoring capabilities to track processing progress in real-time.</br>

Features
High Throughput: The web crawler is optimized to handle more than 100 requests per second, ensuring efficient performance even under heavy loads.</br>

Real-time Monitoring: During runtime, users can monitor the processing status to track the progress of requests being executed.

# Features
High Throughput: The web crawler is optimized to handle more than 100 requests per second, ensuring efficient performance even under heavy loads.</br>

Real-time Monitoring: During runtime, users can monitor the processing status to track the progress of requests being executed.



# To Run Project
docker build . -t webcrawler/backend </br>
docker run -e BASE_URL=https://manpages.courier-mta.org -p 4567:4567 --rm webcrawler/backend</br>
