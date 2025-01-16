package com.grok.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface BaseController<T, Id> {
    ResponseEntity<T> create(T details);

    List<T> index();

    T show(Id id);

    ResponseEntity<T> update(Id id, T details);

    void delete(Id id);

}
