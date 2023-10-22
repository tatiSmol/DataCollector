package org.example.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Line {
    private String number;
    private String name;

    public Line(String number, String name) {
        this.number = number;
        this.name = name;
    }
}
