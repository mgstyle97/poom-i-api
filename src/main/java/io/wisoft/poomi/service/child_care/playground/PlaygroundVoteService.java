package io.wisoft.poomi.service.child_care.playground;

import io.jsonwebtoken.ExpiredJwtException;
import io.wisoft.poomi.configures.security.jwt.JwtTokenProvider;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVoteRepository;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.PlaygroundVoter;
import io.wisoft.poomi.domain.child_care.playground.vote.voter.PlaygroundVoterRepository;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.global.dto.request.child_care.playground.PlaygroundVoteRegisterRequest;
import io.wisoft.poomi.global.dto.request.child_care.playground.PlaygroundVoteVotingRequest;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.PlaygroundVoteLookupResponse;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.PlaygroundVoteRealtimeInfoResponse;
import io.wisoft.poomi.global.exception.exceptions.NotFoundEntityDataException;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlaygroundVoteService {

    private final AddressRepository addressRepository;
    private final AddressTagRepository addressTagRepository;
    private final UploadFileRepository uploadFileRepository;
    private final PlaygroundVoteRepository playgroundVoteRepository;
    private final PlaygroundVoterRepository playgroundVoterRepository;

    private final UploadFileUtils uploadFileUtils;
    private final JwtTokenProvider jwtTokenProvider;

    private final PlaygroundVoterService playgroundVoterService;

    @Transactional
    public void registerPlaygroundVote(final PlaygroundVoteRegisterRequest registerRequest,
                                       final Member member) {
        Address address = generateAddress(registerRequest);

        PlaygroundVote playgroundVote = PlaygroundVote.builder()
                .purposeUsing(registerRequest.getPurposeUsing())
                .address(address)
                .registrant(member)
                .build();
        playgroundVoteRepository.save(playgroundVote);

        Set<UploadFile> images = generateImages(registerRequest.getImages());
        playgroundVote.setImages(images);

        member.addPlaygroundVote(playgroundVote);
    }

    @Transactional(readOnly = true)
    public List<PlaygroundVoteRealtimeInfoResponse> lookupPlaygroundVoteList(final Member member) {
        Set<PlaygroundVote> votes = member.getRegisteredPlaygroundVotes();
        votes.stream()
                .filter(vote -> vote.getNotVotingDongAndHo().size() == 0)
                .forEach(PlaygroundVote::expired);

        return votes.stream()
                .map(PlaygroundVoteRealtimeInfoResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approvePlaygroundVote(final Long voteId) {
        PlaygroundVote playgroundVote = generatePlaygroundVote(voteId);

        playgroundVote.approveState(jwtTokenProvider.generateVoteExpiredToken());
        playgroundVoteRepository.save(playgroundVote);

        playgroundVoterService.loadVoterInfoAndSave(playgroundVote);
    }

    @Transactional(readOnly = true)
    public PlaygroundVoteLookupResponse lookupPlaygroundVote(final Long voteId) {
        PlaygroundVote playgroundVote = generatePlaygroundVote(voteId);
        playgroundVote.checkAccessToExpiredVote();

        return PlaygroundVoteLookupResponse.of(
                playgroundVote, jwtTokenProvider.getExpirationDateFromToken(playgroundVote.getExpiredValidationToken())
        );
    }

    @Transactional
    public void votingPlaygroundVote(final Long voteId, final PlaygroundVoteVotingRequest votingRequest) {
        PlaygroundVote playgroundVote = generatePlaygroundVote(voteId);
        playgroundVote.checkAccessToExpiredVote();

        PlaygroundVoter voter = playgroundVote
                .getVoterByDongAndHo(votingRequest.getDong(), votingRequest.getHo());
        voter.setVoteType(votingRequest.getVoteType());
        playgroundVoterRepository.save(voter);
    }

    private Address generateAddress(final PlaygroundVoteRegisterRequest registerRequest) {
        AddressTag addressTag = addressTagRepository
                .saveAddressTagWithExtraAddress(registerRequest.getExtraAddress());

        Address address = Address.builder()
                .postCode(registerRequest.getPostCode())
                .address(registerRequest.getAddress())
                .detailAddress(registerRequest.getDetailAddress())
                .addressTag(addressTag)
                .build();
        return addressRepository.save(address);
    }

    private Set<UploadFile> generateImages(final List<String> imageDataList) {
        Optional<List<String>> optionalImageDataList = Optional.ofNullable(imageDataList);
        if (optionalImageDataList.isPresent()) {
            Set<UploadFile> images = optionalImageDataList.get().stream()
                    .map(uploadFileUtils::saveFileAndConvertImage)
                    .collect(Collectors.toSet());
            if (!images.isEmpty()) {
                uploadFileRepository.saveAll(images);
                return images;
            }
        }
        return null;
    }

    private PlaygroundVote generatePlaygroundVote(final Long voteId) {
        try {
            PlaygroundVote vote = playgroundVoteRepository.getById(voteId);
            checkVoteExpired(vote);
            vote.checkApprovalStatus();

            return vote;
        } catch (EntityNotFoundException e) {
            throw new NotFoundEntityDataException("vote id: "+ voteId + "에 관한 데이터를 찾지 못했습니다.");
        }
    }

    private void checkVoteExpired(final PlaygroundVote vote) {
        if (vote.getNotVotingDongAndHo().size() == 0) {
            vote.expired();
        } else {
            try {
                Optional
                        .ofNullable(vote.getExpiredValidationToken())
                        .ifPresent(jwtTokenProvider::validateToken);
            } catch (ExpiredJwtException e) {
                vote.expired();
            }

        }
    }

}
