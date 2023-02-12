package com.ps.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class CsvProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("In CsvProcessor body :" +exchange.getIn().getBody());
        List<List<String>> csvRecordList = (List<List<String>>) exchange.getIn().getBody();
        exchange.getIn().setBody(csvRecordList);
        String yamlFileName ="";
        try{
            yamlFileName= convertToYaml(csvRecordList);
        }catch(Exception exp){
            logger.error(exp.getMessage());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String df= formatter.format(new Date());
            String bodyStr="csv msg processed successfully at "+df;
            if(StringUtils.isNotEmpty(yamlFileName)) bodyStr+=" file saved at output/"+ yamlFileName;
        exchange.getOut().setHeader("currentTimeStamp",df);
        exchange.getOut().setBody(bodyStr);
        logger.info("In CsvProcessor processed successfully"+df);
        if(csvRecordList!=null && csvRecordList.isEmpty()){
            List<String> sampleCsvMessage= new ArrayList<>();
            sampleCsvMessage.add("Hello World"+df);
            csvRecordList.add(sampleCsvMessage);
        }
    }

    public static String convertToYaml(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        String yaml = mapper.writeValueAsString(obj);
        return yaml;
    }
}
