package io.wisoft.poomi.service.child_care.playground;

import io.wisoft.poomi.domain.child_care.playground.ChildCarePlaygroundRepository;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChildCarePlaygroundService {

    private final ChildCarePlaygroundRepository childCarePlaygroundRepository;
    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;

}
