package com.ps.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Component
public class XMLProcessor implements Processor {
    ObjectMapper objectMapper =new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("In XMLProcessor body :" +exchange.getIn().getBody());
        HashMap<String, Object> xmlRecordMap = (HashMap<String, Object>) exchange.getIn().getBody();
        exchange.getIn().setBody(xmlRecordMap);
    }
}
