package io.wisoft.poomi.service;

import io.wisoft.poomi.bind.dto.AddressDto;
import io.wisoft.poomi.bind.request.AddressRegisterRequest;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
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

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;

    @Transactional
    public AddressDto registerAddress(HttpServletRequest request, AddressRegisterRequest addressRegisterRequest) {
        String loginId = jwtTokenProvider.getUsernameFromToken(jwtTokenProvider.resolveToken(request));

        Member member = memberRepository
                .getMemberByLoginId(loginId);
        AddressTag addressTag = addressTagRepository
                .getAddressTagByExtraAddress(addressRegisterRequest.getExtraAddress());
        Address address = Address
                .builder()
                .postCode(addressRegisterRequest.getPostCode())
                .detailAddress(addressRegisterRequest.getDetailAddress())
                .addressTag(addressTag)
                .build();
        addressRepository.save(address);

        member.setAddress(address);
        memberRepository.save(member);

        return AddressDto.from(loginId, addressTag.getExtraAddress());
    }

}
