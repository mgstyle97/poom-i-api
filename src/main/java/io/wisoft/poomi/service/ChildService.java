package io.wisoft.poomi.service;

import io.wisoft.poomi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final MemberRepository memberRepository;

}
