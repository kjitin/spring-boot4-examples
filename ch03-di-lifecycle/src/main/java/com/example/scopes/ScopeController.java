package com.example.scopes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ScopeController {

    private final SingletonService singletonService;
    private final RequestScopedBean requestScopedBean;
    private final SessionScopedBean sessionScopedBean;

    public ScopeController(SingletonService singletonService, RequestScopedBean requestScopedBean,
                           SessionScopedBean sessionScopedBean) {
        this.singletonService = singletonService;
        this.requestScopedBean = requestScopedBean;
        this.sessionScopedBean = sessionScopedBean;
    }

    @GetMapping("/scopes")
    public Map<String, String> scopes() {
        return Map.of(
                "singleton", singletonService.getInstanceId(),
                "request", requestScopedBean.getInstanceId(),
                "session", sessionScopedBean.getInstanceId());
    }
}
