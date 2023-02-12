Case Study: Rest APIs interacting with RabbitMQ using Apache Camel and some File operations 

This Project integrates SpringBoot-REST, Apache Camel and Rabbit-MQ to create a service which can process different request types, publish them as messages to Rabbit-MQ Queues, and log then in /target/output folder in fomr of yaml files. Exceptions are treated as Deadletters and they too, are treated in the above-mentioned fashion.  



Problem Statement:

_Create a rest API using Apache camel that accepts a file as an input and does the following: POST /file?name=currency.csv to create a file.

Based on the type of file they must be routed to XML or CSV or JSON in Rabbit MQ appropriate queues: File format for CSV: USD, INR 1, 81

JSON: { USD: 1, INR: 81 }

XML: 1 81

Convert them to Java bean and handle any errors in conversion using try catch and finally blocks and the output to be written in YAML format to a folder named “outputs” without as filename-{timestamp}.yaml and if the files is not processable use a Dead letter queue to handle the posted content as “filename-{timestamp}-error.txt"._


Features

a) Apache Camel Integration with ESB Rabbit MQ

b) Fallbacks to deadletter exchanges and queue.

c) File Handling IO Operations with the use of exchanges

d) Integration of Rest API with Apache Camel



API sample in form of CURL commands 

1. Request with CSV payload  

curl --location --request POST 'http://localhost:8080/camelRest/api/file?name=currency.csv ' \
--header 'Content-Type: application/csv' \
--data-raw 'USD,INR
1,81'

2. Request with Json payload 

curl --location --request POST 'http://localhost:8080/camelRest/api/file?name=currency.json' \
--header 'Content-Type: application/json' \
--data-raw '{
    "usd": "1",
    "inr": "82"
}'

3. Request with XML payload 

curl --location --request POST 'http://localhost:8080/camelRest/api/file?name=currency.json' \
--header 'Content-Type: application/xml' \
--data-raw '<?xml version="1.0" ?>
<CONVERSION>
    <USD>1</USD>
    <INR>81</INR>
</CONVERSION>'




