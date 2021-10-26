package io.wisoft.poomi.service.child_care.playground;

import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.PlaygroundVoterRepository;
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


    }

    private AddressSearchResponse loadAddressSearch(final String address) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("confmKey", addressSearchKey);
        params.add("currentPage", "1");
        params.add("countPerPage", "10");
        params.add("keyword", address);
        params.add("resultType", "json");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(addressSearchURI, httpEntity, AddressSearchResponse.class);
    }

}
