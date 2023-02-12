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

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class JsonProcessor implements Processor {
    ObjectMapper objectMapper =new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        String yamlFileName ="";
        String jsonBody="";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String df= formatter.format(new Date());

        logger.info("-------->In JsonProcessor body :" +exchange.getIn().getBody());
        try{
            yamlFileName= convertToYaml(exchange.getIn().getBody());
            jsonBody=objectMapper.writeValueAsString(exchange.getIn().getBody());
        }catch(Exception exp){
            logger.error(exp.getMessage());
        }

        if(StringUtils.isNotEmpty(yamlFileName)) jsonBody+=" file saved at output/"+ yamlFileName;
        if(StringUtils.isNotEmpty(jsonBody)) jsonBody+="_"+df;
        exchange.getIn().setBody(jsonBody);
        exchange.getOut().setHeader("Content-Type", MediaType.APPLICATION_JSON);
        exchange.getOut().setHeader("currentTimeStamp",df);
        exchange.getOut().setBody("json msg processed successfully at "+df);
        logger.info("In JsonProcessor processed successfully"+df);
    }

    public static String convertToYaml(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        String yaml = mapper.writeValueAsString(obj);
        return yaml;
    }
}
