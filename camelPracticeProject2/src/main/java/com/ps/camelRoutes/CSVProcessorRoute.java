package com.ps.camelRoutes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ps.services.CSVtoBeanConverterService;

@Component("csvProcessorRoute")
public class CSVProcessorRoute extends RouteBuilder {
    @Autowired
    CSVtoBeanConverterService csvConverterService;

    @Override
    public void configure() {

    }
}
