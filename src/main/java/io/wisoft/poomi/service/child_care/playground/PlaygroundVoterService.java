package io.wisoft.poomi.service.child_care.playground;

import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.PlaygroundVoter;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.PlaygroundVoterRepository;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.AddressDetailResponse;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.AddressSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlaygroundVoterService {

    private final PlaygroundVoterRepository playgroundVoterRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${address.search}")
    private String addressSearchKey;
    @Value("${address.detail}")
    private String addressDetailKey;

    @Value("${address.searchURI}")
    private String addressSearchURI;
    @Value("${address.detailURI}")
    private String addressDetailURI;

    @Transactional
    public void loadVoterInfoAndSave(final PlaygroundVote playgroundVote) {
        AddressSearchResponse addressSearchResponse = loadAddressSearch(playgroundVote.getAddress().getAddress());

        AddressDetailResponse dongAddressDetailResponse = loadDongAddressDetail(addressSearchResponse);
        List<AddressDetailResponse.Juso> jusoList = loadFloorAddressDetail(dongAddressDetailResponse);

        Set<PlaygroundVoter> voters = jusoList.stream()
                .map(juso -> PlaygroundVoter.of(juso, playgroundVote))
                .collect(Collectors.toSet());
        playgroundVoterRepository.saveAll(voters);
        playgroundVote.setVoters(voters);
    }

    private AddressSearchResponse loadAddressSearch(final String address) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("confmKey", addressSearchKey);
        params.add("currentPage", "1");
        params.add("countPerPage", "10");
        params.add("keyword", address);
        params.add("resultType", "json");

        HttpEntity httpEntity = generateRequestEntity(params);

        return restTemplate.postForObject(addressSearchURI, httpEntity, AddressSearchResponse.class);
    }

    private AddressDetailResponse loadDongAddressDetail(final AddressSearchResponse addressSearchResponse) {
        final AddressSearchResponse.Juso juso = addressSearchResponse
                .getResults()
                .getJusos()
                .get(0);

        MultiValueMap<String, Object> params =
                generateAddressDetailParameter(
                        juso.getAdmCd(), juso.getRnMgtSn(), juso.getUdrtYn(), juso.getBuldMnnm(), juso.getBuldSlno(),
                        "dong", ""
                );

        HttpEntity httpEntity = generateRequestEntity(params);

        return restTemplate.postForObject(addressDetailURI, httpEntity, AddressDetailResponse.class);
    }

    private List<AddressDetailResponse.Juso> loadFloorAddressDetail(final AddressDetailResponse addressDetailResponse) {
        List<AddressDetailResponse.Juso> result = new ArrayList<>();
        addressDetailResponse.getResults().getJusoList().stream()
                .map(this::requestFloorDetail)
                .forEach(result::addAll);

        return result;
    }

    private List<AddressDetailResponse.Juso> requestFloorDetail(final AddressDetailResponse.Juso juso) {
        MultiValueMap<String, Object> params =
                generateAddressDetailParameter(
                        juso.getAdmCd(), juso.getRnMgtSn(), juso.getUdrtYn(), juso.getBuldMnnm(), juso.getBuldSlno(),
                        "floorho", juso.getDongNm()
                );

        HttpEntity httpEntity = generateRequestEntity(params);
        AddressDetailResponse floorDetail = restTemplate
                .postForObject(addressDetailURI, httpEntity, AddressDetailResponse.class);
        return floorDetail.getResults().getJusoList();
    }

    private MultiValueMap<String, Object> generateAddressDetailParameter(
            String admCd, String rnMgtSn,
            Integer udrtYn, Integer buldMnnm, Integer buldSlno,
            String searchType, String dongNm) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("confmKey", addressDetailKey);
        params.add("admCd", admCd);
        params.add("rnMgtSn", rnMgtSn);
        params.add("udrtYn", udrtYn);
        params.add("buldMnnm", buldMnnm);
        params.add("buldSlno", buldSlno);
        params.add("searchType", searchType);
        params.add("dongNm", dongNm);
        params.add("resultType", "json");

        return params;
    }

    private HttpEntity generateRequestEntity(final MultiValueMap<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity(params, headers);
    }

}
