package com.ps.services;

import org.apache.camel.Exchange;
import org.apache.camel.language.xpath.XPath;
import org.springframework.stereotype.Service;

@Service
public class CSVtoBeanConverterService {

    public static void convertToBean(Exchange exchange) throws Exception{

        Object body=exchange.getIn().getBody();
    }
}
