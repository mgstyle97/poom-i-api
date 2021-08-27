package io.wisoft.poomi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.poomi.bind.ApiResponse;
import io.wisoft.poomi.bind.dto.ClassProgramLookupDto;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.member.Gender;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.MemberRepository;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.domain.member.authority.AuthorityRepository;
import io.wisoft.poomi.domain.program.classes.ClassProgram;
import io.wisoft.poomi.domain.program.classes.ClassProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ClassProgramControllerTest {

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
    private ClassProgramRepository classProgramRepository;

    private Member member;
    private String token;
    private HttpHeaders headers;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        // given
        AddressTag addressTag = new AddressTag("city");
        addressTagRepository.save(addressTag);

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

        ClassProgram classProgram = ClassProgram.builder()
                .title("테스트")
                .contents("테스트입니다.")
                .capacity(15L)
                .isBoard(false)
                .isRecruit(false)
                .writer(member)
                .build();

        ClassProgram classProgram2 = ClassProgram.builder()
                .title("테스트2")
                .contents("테스트2입니다.")
                .capacity(12L)
                .isBoard(false)
                .isRecruit(false)
                .writer(member)
                .build();
        classProgramRepository.saveAll(List.of(classProgram, classProgram2));
    }

    @Test
    @DisplayName("테스트: 클래스 프로그램 전체 조회 api")
    void get_all_class_program() throws JsonProcessingException {
        String url = "http://localhost:" + port + "/api/class";
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<? extends ApiResponse> responseEntity =
                restTemplate.exchange(
                        url, HttpMethod.GET, httpEntity,
                        new ApiResponse<List<ClassProgramLookupDto>>().getClass()
                );
        ApiResponse<List<ClassProgramLookupDto>> result = responseEntity.getBody();
        List<ClassProgramLookupDto> classPrograms = objectMapper
                .readValue(objectMapper.writeValueAsString(
                        result.getData()), new TypeReference<>() {});

        classPrograms.forEach(classProgramLookupDto -> System.out.println(classProgramLookupDto.getTitle()));
    }

}