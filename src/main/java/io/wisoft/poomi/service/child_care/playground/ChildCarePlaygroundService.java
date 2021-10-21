package io.wisoft.poomi.service.child_care.playground;

import io.wisoft.poomi.domain.auth.residence.ResidenceCertification;
import io.wisoft.poomi.domain.auth.residence.ResidenceCertificationRepository;
import io.wisoft.poomi.domain.child_care.playground.ChildCarePlaygroundRepository;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.global.dto.request.child_care.playground.ResidenceCertificationRegisterRequest;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildCarePlaygroundService {

    private final ChildCarePlaygroundRepository childCarePlaygroundRepository;
    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;


}
