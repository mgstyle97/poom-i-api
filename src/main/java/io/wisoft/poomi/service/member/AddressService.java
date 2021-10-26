package io.wisoft.poomi.service.member;

import io.wisoft.poomi.global.dto.response.member.AddressResponse;
import io.wisoft.poomi.global.dto.request.member.AddressRegisterRequest;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class AddressService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;

    @Transactional
    public AddressResponse registerAddress(final Member member, final AddressRegisterRequest addressRegisterRequest) {
        AddressTag addressTag = addressTagRepository
                .saveAddressTagWithExtraAddress(addressRegisterRequest.getExtraAddress());
        log.info("Generate address tag: {}", addressTag.getExtraAddress());

        Address address = generateAddressAndSave(addressRegisterRequest, addressTag);
        log.info("Save address info: {}", address.getId());

        member.updateAddressInfo(address);
        memberRepository.save(member);
        log.info("Modify member address info");

        return AddressResponse.from(member.getEmail(), addressTag.getExtraAddress());
    }

    private Address generateAddressAndSave(final AddressRegisterRequest addressRegisterRequest, AddressTag addressTag) {
        Address address = Address
                .builder()
                .postCode(addressRegisterRequest.getPostCode())
                .address(addressRegisterRequest.getAddress())
                .detailAddress(addressRegisterRequest.getDetailAddress())
                .addressTag(addressTag)
                .build();

        return addressRepository.save(address);
    }

}
