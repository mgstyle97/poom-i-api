package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.childminder.urgent.ChildminderUrgentRegisterDto;
import io.wisoft.poomi.bind.request.childminder.urgent.ChildminderUrgentRegisterRequest;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgent;
import io.wisoft.poomi.domain.childminder.urgent.ChildminderUrgentRepository;
import io.wisoft.poomi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildminderUrgentService {

    private final ChildminderUrgentRepository childminderUrgentRepository;

    @Transactional
    public ChildminderUrgentRegisterDto registerChildminderUrgent(
            final ChildminderUrgentRegisterRequest childminderUrgentRegisterRequest,
            final Member member) {
        ChildminderUrgent childminderUrgent = ChildminderUrgent.of(childminderUrgentRegisterRequest, member);
        log.info("Generate childminder urgent entity");

        childminderUrgentRepository.save(childminderUrgent);
        log.info("Save childminder urgent id: {}", childminderUrgent.getId());


        return ChildminderUrgentRegisterDto.of(childminderUrgent);
    }

}
