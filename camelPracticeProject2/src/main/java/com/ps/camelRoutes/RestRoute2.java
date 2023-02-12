package com.ps.camelRoutes;


import com.ps.processors.CsvProcessor;
import com.ps.processors.JsonProcessor;
import com.ps.processors.XMLProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.YAMLDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//@Component("restRoute")
public class RestRoute2 extends RouteBuilder {

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

//        CamelContext context = new DefaultCamelContext();
//        YAMLDataFormat yaml = new YAMLDataFormat();
//
//
//        //rest service configuration
//        restConfiguration()
//                .host(restServerHost)
//                .port(serverPort)
//                .component(restWebServer)
//                .bindingMode(RestBindingMode.auto);
//
//        //Invoking rest API with route id as rest-router
//        rest("/api/file")
//                .post()
//                .routeId("rest-router")
//                .to("direct:contentBasedRouter");
//
//        from("direct:contentBasedRouter")
//                .routeId("content-route")
//                .log("-----> started ${routeId} ")
//                .choice()
//                    .when(header("Content-Type").contains("csv"))
//                        .log("----->routing to direct:csv")
//                        .to("direct:csv")
//                    .when(header("Content-Type").contains("xml"))
//                        .log("----->routing to direct:xml")
//                        .to("direct:xml")
//                    .when(header("Content-Type").contains("json"))
//                        .log("----->routing to direct:json")
//                        .to("direct:json")
//                    .otherwise()
//                        .log("----->routing to direct: content-error")
//                        .log("block routing to direct:error")
//                    .to("direct:error");
//
//
//        //CSV Content
//        from("direct:csv").routeId("csv-route")
//                .doTry()
//                    .log("csv-route")
//                    .unmarshal().csv()
//                    .bean("FileTransformProcessor", "csvJavaTransformation")
//                    //.process(new CsvProcessor())
//                    .marshal(yaml)
//                    .to("rabbitmq://" + rabbitServer + "/jsonExchange?queue=jsonQueue&routingKey=jsonRoutingKey&autoDelete=false")
//                .doCatch(Exception.class)
//                    .log("csv-route-error")
//                    .process(exchange -> {
//                        String fileName = (String) exchange.getIn().getHeader("fileName");
//                        String errorFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + System.currentTimeMillis() + "-error.txt";
//                        exchange.getIn().setHeader("errFileName", errorFileName);
//                    })
//                    .to("rabbitmq://" + rabbitServer + "/deadLetterExchange?queue=deadLetterQueue&routingKey=deadLetterRoutingKey")
//                .doFinally()
//                    .log("csv-route-finally")
//                    .to("file:outputs?fileName=${header.fileName}-${header.currentTimeStamp}.yaml")
//                .end();
//
//        //XML Content
//        from("direct:xml").routeId("xml-route")
//                .doTry()
//                    .log("xml-route")
//                    .unmarshal().jacksonXml()
//                    .bean("FileTransformProcessor", "xmlJavaTransformation")
//                    //.process(new XMLProcessor())
//                    .marshal(yaml)
//                    .to("rabbitmq://"+ rabbitServer + "/jsonExchange?queue=jsonQueue&routingKey=jsonRoutingKey&autoDelete=false")
//                .doCatch(Exception.class)
//                    .log("xml-route-error")
//                    .process(exchange -> {
//                        String fileName = (String) exchange.getIn().getHeader("fileName");
//                        String errorFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + System.currentTimeMillis() + "-error.txt";
//                        exchange.getIn().setHeader("errFileName", errorFileName);
//                    })
//                    .to("rabbitmq://"+ rabbitServer +"/deadLetterExchange?queue=deadLetterQueue&routingKey=deadLetterRoutingKey")
//                .doFinally()
//                    .log("xml-route-finally")
//                    .to("file:outputs?fileName=${header.fileName}-${header.currentTimeStamp}.yaml")
//                .end();
//
//        //JSON Content
//        from("direct:json").routeId("json-route")
//                .doTry()
//                    .log("json-route")
//                    .bean("FileTransformProcessor", "jsonJavaTransformation")
//                    .process(new JsonProcessor())
//                    .marshal(yaml)
//                    .to("rabbitmq://"+ rabbitServer +"/jsonExchange?queue=jsonQueue&routingKey=jsonRoutingKey&autoDelete=false")
//                .doCatch(Exception.class)
//                    .log("json-route-error")
//                    .process(exchange -> {
//                        String fileName = (String) exchange.getIn().getHeader("fileName");
//                        String errorFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + System.currentTimeMillis() + "-error.txt";
//                        exchange.getIn().setHeader("errFileName", errorFileName);
//                    })
//                    .to("rabbitmq://"+ rabbitServer +"/deadLetterExchange?queue=deadLetterQueue&routingKey=deadLetterRoutingKey")
//                .doFinally()
//                    .log("json-route-finally")
//                    .to("file:outputs?fileName=${header.fileName}-${header.currentTimeStamp}.yaml")
//                .end();

    }
}
