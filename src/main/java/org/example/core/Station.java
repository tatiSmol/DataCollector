package org.example.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Station {
    private String name;
    private String lineNumber;

    @Override
    public String toString() {
        return name;
    }
}
