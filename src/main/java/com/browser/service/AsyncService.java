package com.browser.service;

import java.util.concurrent.Future;

public interface AsyncService {
    Future<String> asyncMethod();
}
