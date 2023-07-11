package com.xiaodao.endpoint;


import lombok.Data;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "custom")
public class CustomEndpoints {

    @ReadOperation
    public CustomData getData() {
        return new CustomData("test", 5);
    }


    @Data

    private static class CustomData {
        private final String name;
        private final int value;

        public CustomData(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}
