package io.wisoft.poomi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.configures.security.jwt.JwtToken;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.child_care.RecruitmentStatus;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroup;
import io.wisoft.poomi.domain.child_care.group.ChildCareGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ChildCareGroupControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private AddressTagRepository addressTagRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChildCareGroupRepository childCareGroupRepository;

    private Member member;
    private JwtToken token;
    private HttpHeaders headers;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        // given
        AddressTag addressTag = addressTagRepository.saveAddressTagWithExtraAddress("city");

        Address address = Address.builder()
                .addressTag(addressTag)
                .postCode("12345")
                .detailAddress("Seoul city")
                .build();
        addressRepository.save(address);

        member = Member.builder()
                .name("test")
                .phoneNumber("01011111111")
                .email("test@gmail.com")
                .password("1234")
                .authorities(Collections.singleton(authorityRepository.getUserAuthority()))
                .address(address)
                .nick("test")
                .gender(Gender.FEMALE)
                .build();
        memberRepository.save(member);

        token = jwtTokenProvider.generateToken(
                member.toAuthentication()
        );

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        ChildCareGroup childCareGroup = ChildCareGroup.builder()
                .title("테스트")
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .writer(member)
                .build();

        ChildCareGroup childCareGroup2 = ChildCareGroup.builder()
                .title("테스트2")
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .writer(member)
                .build();
        childCareGroupRepository.saveAll(List.of(childCareGroup, childCareGroup2));
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 전체 조회 api")
    void get_all_class_program() throws JsonProcessingException {
        String url = "http://localhost:" + port + "/api/class";
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

    }

}