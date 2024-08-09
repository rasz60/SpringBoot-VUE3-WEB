package com.example.rmfr.result;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Data
public class RestResults {

    private String resultCode;
    private List<Map<String, Object>> resultMessage = new ArrayList<>();

    public RestResults() {}
}
