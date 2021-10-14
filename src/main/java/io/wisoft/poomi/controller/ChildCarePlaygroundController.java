package io.wisoft.poomi.controller;

import io.wisoft.poomi.service.child_care.playground.ChildCarePlaygroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/playground")
public class ChildCarePlaygroundController {

    private final ChildCarePlaygroundService childCarePlaygroundService;

}
