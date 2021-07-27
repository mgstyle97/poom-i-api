package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.AddressDto;
import io.wisoft.poomi.bind.request.AddressRegisterRequest;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.repository.AddressRepository;
import io.wisoft.poomi.repository.AddressTagRepository;
import io.wisoft.poomi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;

    @Transactional
    public AddressDto registerAddress(Authentication authInfo,
                                      AddressRegisterRequest addressRegisterRequest) {
        Member member = memberRepository.findMemberByLoginId(authInfo.getName()).orElseThrow(
                () -> new UsernameNotFoundException("No member data about login id")
        );
        AddressTag addressTag = addressTagRepository.findByName(addressRegisterRequest.getTagName())
                .orElseThrow(() -> new IllegalArgumentException());
        Address address = Address
                .builder()
                .addressTag(addressTag)
                .detailAddress(addressRegisterRequest.getDetailAddress())
                .build();
        addressRepository.save(address);

        member.setAddress(address);
        memberRepository.save(member);

        return AddressDto.from(authInfo.getName(), addressTag.getName());
    }

}
