package ru.syncra.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.net.httpserver.Authenticator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {

    @JsonProperty("failures")
    private List<Failure> failures = Collections.emptyList();

    @JsonProperty("value")
    private T value;

    @JsonProperty("isSuccess")
    private boolean isSuccess;

    @JsonProperty("isFailure")
    private boolean isFailure;

    public ApiResponse(List<Failure> failures, T value, boolean isSuccess, boolean isFailure) {
        this.failures = failures != null ? failures : Collections.emptyList();
        this.value = value;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Failure {

        @JsonProperty("id")
        private String id;

        @JsonProperty("description")
        private String description;

        public Failure(String id, String description) {
            this.id = id;
            this.description = description;
        }

        @Override
        public String toString() {
            return "Failure{" +
                    "id='" + id + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}