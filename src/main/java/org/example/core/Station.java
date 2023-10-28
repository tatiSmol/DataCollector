package org.example.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Station {
    @NonNull
    private String name;
    @NonNull
    private String line;
    private String date;
    private long depth;
    @NonNull
    private boolean hasConnection;

    @Override
    public String toString() {
        return name;
    }
}
