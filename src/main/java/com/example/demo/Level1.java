package com.example.demo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Level1")
public class Level1 {

    @Id
    private String id;

    private String name;

    @DBRef
    private List<Level2> subElement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Level2> getSubElement() {
        return subElement;
    }

    public void setSubElement(List<Level2> subElement) {
        this.subElement = subElement;
    }
}
