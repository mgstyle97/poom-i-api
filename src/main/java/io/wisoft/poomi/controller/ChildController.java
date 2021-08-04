package io.wisoft.poomi.controller;

import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.ChildAddDto;
import io.wisoft.poomi.bind.request.ChildAddRequest;
import io.wisoft.poomi.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @PutMapping("/child")
    public ApiResponse<ChildAddDto> addChildren(@RequestBody @Valid List<ChildAddRequest> childAddRequest,
                                                HttpServletRequest request) {
        return ApiResponse.succeed(childService.addChildren(childAddRequest, request));
    }

}
