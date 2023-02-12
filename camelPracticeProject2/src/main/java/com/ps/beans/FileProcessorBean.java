package com.ps.beans;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Map;

@Setter
@Getter
public class FileProcessorBean {
    private Integer id;
    private String fileName;
    private Map<String, String> fileContent;
    private Timestamp beanCreatedOn;

    @Override
    public String toString() {
        String abc = getId()+"_"+getFileName()+"_"+getBeanCreatedOn().toString();
        return abc;
    }
}
