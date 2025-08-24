package com.codewithmosh.store.payments;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebHookRequest {
    private Map<String, String> headers;
    private String payload;
}
