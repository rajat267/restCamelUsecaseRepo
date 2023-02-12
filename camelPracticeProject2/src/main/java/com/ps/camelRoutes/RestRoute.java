package com.ps.camelRoutes;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.ps.beans.MyBean;
import com.ps.processors.*;
import com.ps.processors.XMLProcessor;
import com.ps.services.CSVtoBeanConverterService;
import com.ps.services.ExampleServices;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.YAMLDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;


@Component("restRoute")
public class RestRoute extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;

    @Value("${project.api.path}")
    String contextPath;

    @Value("${rabbitmq.server}")
    private String rabbitServer;
    @Value("${rest.webserver}")
    private String restWebServer;
    @Value("${rest.serverhost}")
    private String restServerHost;
    @Value("${rest.serverport}")
    private String restServerPort;

    @Override
    public void configure() {

        CamelContext context = new DefaultCamelContext();
        YAMLDataFormat yaml = new YAMLDataFormat();


        rest("/api/file")
                .description("Test REST Service with Camel")
                .id("api-fileRoute").post()
                .skipBindingOnErrorCode(false)
                .bindingMode(RestBindingMode.auto)
                .enableCORS(true)
                .to("direct:remoteService");

        from("direct:remoteService").routeId("direct-route")
                .tracing()
                //.log(">>> processing ${routeId}     ${headers}") name=${headers[name]}
                .log(" processing ${routeId}  BODY: ${in.body}  file:${file:ext}") //TODO update Routeid
                    .choice()
                        .when(header("Content-Type").contains("csv"))
                                .log("routing ${header.name} to csvProcessor")
                                .to("direct:csvProcessor")
                        .when(header("Content-Type").contains("json"))
                                .log("routing ${header.name} to jsonProcessor")
                                .to("direct:jsonProcessor")
                        .when(header("Content-Type").contains("xml"))
                                .log("routing ${header.name} to xmlProcessor")
                                .to("direct:xmlProcessor")
                        .otherwise()
                                    .log("routing ${header.name} to deadletters")
                                    .to("direct:deadLetters")
                        .end();

        from("direct:csvProcessor").routeId("csvProcessor")
                .doTry()
                    .log("----> ${routeId} started   name=${headers[name]} BODY:${body}")
                    .unmarshal().csv()
                    .process(new CsvProcessor()).marshal(yaml)
                    .log("----> ${routeId}  msg processed success BODY: ${body}")
                    .to("rabbitmq://" + rabbitServer + "/ex.camelRest?queue=csvMessageQueue&routingKey=csvMessageQueueRK&autoDelete=false&exchangePattern=InOnly")
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                    .log("----> ${routeId} msg processed success by RabbitMQ")
                .process(new CsvProcessor()).marshal(yaml)
                    .to("file:target/output?fileName=${headers[name]}_${date:now:yyyyMMddHHmmss}_csvToYaml.yaml")
                .doCatch(Exception.class)
                    .log("----> ${routeId}   exception: ${exception.stacktrace}")
                    .to("direct:deadletters")
                .end();

        from("direct:jsonProcessor").routeId("jsonProcessor")//TODO: body is empty, which leads to exception
                .doTry()
                    .log("----> ${routeId} started   name=${headers[name]} BODY:${body}")
                    .setBody(body())
                    .process(new JsonProcessor())
                    .log("----> ${routeId}  msg processed success BODY: ${body}")
                    .to("rabbitmq://" + rabbitServer + "/ex.camelRest?queue=jsonMessageQueue&routingKey=jsonMessageQueueRK&autoDelete=false&exchangePattern=InOnly")
                    .log("----> ${routeId}  msg processed success by RabbitMQ BODY: ${body}")
                    .to("file:target/output?fileName=${headers[name]}_${date:now:yyyyMMddHHmmss}_jsonToYaml.yaml")
                .doCatch(Exception.class)
                    .log("----> ${routeId}  exception: ${exception.stacktrace}")
                    .to("direct:deadletters")
                .end();

        JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
        jacksonDataFormat.setPrettyPrint(true);
        jacksonDataFormat.enableFeature(SerializationFeature.WRAP_ROOT_VALUE);
        //jacksonDataFormat.setPrettyPrint(true);
//        from("direct:xmlProcessor").routeId("xmlProcessor")
//                .doTry()
//                    .log("----> ${routeId} started ")
//                    .unmarshal().jacksonXml()
//                    .log("----> ${routeId} unmarshalled BODY: ${body}")
//                    .marshal(jacksonDataFormat)
//                    .process(new XMLProcessor()).marshal(yaml)
//                    .log("----> ${routeId}  msg processed success BODY: ${body}")
//                    .to("rabbitmq://" + rabbitServer + "/ex.camelRest?queue=xmlMessageQueue&routingKey=xmlMessageQueueRK&autoDelete=false&exchangePattern=InOnly")
//                    .log("----> ${routeId}  msg processed success by RabbitMQ BODY: ${body}")
//                .doCatch(Exception.class)
//                    .log("----> ${routeId}  exception:  ${exception.stacktrace}")
//                    //.to("direct:deadletters")
//                .end();

        from("direct:xmlProcessor").routeId("xmlProcessor") //TODO: XML piece not working error response.
                .doTry()
                    .log("----> ${routeId} started ")
                    . to("xj:identity?transformDirection=XML2JSON")
                    .log("----> ${routeId} unmarshalled BODY: ${body}")
                    //.marshal(jacksonDataFormat)
                    .process(new XMLProcessor())//.marshal(yaml)
                    .log("----> ${routeId}  msg processed success BODY: ${body}")
                    .to("rabbitmq://" + rabbitServer + "/ex.camelRest?queue=xmlMessageQueue&routingKey=xmlMessageQueueRK&autoDelete=false&exchangePattern=InOnly")
                    .log("----> ${routeId}  msg processed success by RabbitMQ BODY: ${body}")
                    .to("file:target/output?fileName=${headers[name]}_${date:now:yyyyMMddHHmmss}_xmlToYaml.yaml")
                .doCatch(Exception.class)
                    .log("----> ${routeId}  exception:  ${exception.stacktrace}")
                    .to("direct:deadletters")
                .end();

        from("direct:deadletters").routeId("deadlettersQueue")//TODO: body is empty, which leads to exception
                .doTry()
                    .log("----> ${routeId} started   name=${headers[name]} BODY:${body}")
                    .setBody(body())
                    .process(new DeadLettersProcessor())
                    .log("----> ${routeId}  msg processed success BODY: ${body}")
                    .to("rabbitmq://" + rabbitServer + "/ex.camelRest?queue=deadLetterMessageQueue2&routingKey=deadLetterMessageQueue2RK&autoDelete=false&exchangePattern=InOnly")
                    .log("----> ${routeId}  msg processed success by RabbitMQ BODY: ${body}")
                    .to("file:target/output/deadlettersInYaml?fileName=${headers[name]}_${date:now:yyyyMMddHHmmss}_error.yaml")
                    .log("----> ${routeId}  failed msg saved to output/fileName=${headers[name]}_${date:now:yyyyMMddHHmmss}_error.yaml")
                .doCatch(Exception.class)
                    .log("----> ${routeId}  exception: ${exception.stacktrace}")
                .end();

    }
}
