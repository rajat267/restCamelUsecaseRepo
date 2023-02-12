package com.ps.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DeadLettersProcessor implements Processor {
    ObjectMapper objectMapper =new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("-------->In DeadLettersProcessor body :" +exchange.getIn().getBody());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String df= formatter.format(new Date());
        String jsonBody ="";
        String yamlFileName ="";
        try {
             jsonBody = objectMapper.writeValueAsString(exchange.getIn().getBody());
        }catch(Exception exp) {
             jsonBody = "Error Processing DeadLetter at "+df+ " error msg saved in output/deadlettersInYaml/"+ yamlFileName;
            jsonBody+="\n "+exp.getMessage();
        }
        exchange.getIn().setBody(jsonBody);
        yamlFileName=convertToYaml(jsonBody);
        exchange.getOut().setHeader("Content-Type", MediaType.APPLICATION_JSON);
        exchange.getOut().setHeader("currentTimeStamp",df);
        exchange.getOut().setBody("DeadLetters msg processed successfully at "+df);
        logger.info("In DeadLettersProcessor processed successfully"+df);
    }

    public static String convertToYaml(Object obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        String yaml = mapper.writeValueAsString(obj);
        return yaml;
    }
}
