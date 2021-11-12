package io.wisoft.poomi.service.child_care.playground;

import io.wisoft.poomi.domain.child_care.playground.ChildCarePlayground;
import io.wisoft.poomi.domain.child_care.playground.ChildCarePlaygroundRepository;
import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.file.UploadFileRepository;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.AddressRepository;
import io.wisoft.poomi.domain.member.address.AddressTagRepository;
import io.wisoft.poomi.global.dto.request.child_care.playground.ChildCarePlaygroundRegisterRequest;
import io.wisoft.poomi.global.dto.response.child_care.playground.vote.ChildCarePlaygroundLookupResponse;
import io.wisoft.poomi.global.utils.UploadFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildCarePlaygroundService {

    private final ChildCarePlaygroundRepository childCarePlaygroundRepository;
    private final UploadFileRepository uploadFileRepository;

    private final PlaygroundVoteService playgroundVoteService;
    private final UploadFileUtils uploadFileUtils;

    @Transactional(readOnly = true)
    public List<ChildCarePlaygroundLookupResponse> lookupPlaygroundList(final Member member) {
        List<ChildCarePlayground> playgroundList = childCarePlaygroundRepository
                .findBySearchTag(member.getAddressTag().getId());

        return playgroundList.stream()
                .map(ChildCarePlaygroundLookupResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void registerPlayground(final ChildCarePlaygroundRegisterRequest registerRequest, final Member member) {
        PlaygroundVote vote = playgroundVoteService.generatePlaygroundVote(registerRequest.getVoteId());
        validateVoteForRegister(vote);

        ChildCarePlayground playground = ChildCarePlayground.of(registerRequest, vote, member);
        childCarePlaygroundRepository.save(playground);

        saveImages(playground, registerRequest.getImageDataList());

    }

    private void saveImages(final ChildCarePlayground playground, final List<String> imageDataList) {
        if (!CollectionUtils.isEmpty(imageDataList)) {
            imageDataList.stream()
                    .map(uploadFileUtils::saveFileAndConvertImage)
                    .forEach(image -> {
                        playground.addImage(uploadFileRepository.save(image));
                    });
        }
    }

    private void validateVoteForRegister(final PlaygroundVote vote) {
        vote.checkApprovalStatus();
        vote.checkAccessToNotExpiredVote();
        vote.checkAgreeRate();
    }

}
