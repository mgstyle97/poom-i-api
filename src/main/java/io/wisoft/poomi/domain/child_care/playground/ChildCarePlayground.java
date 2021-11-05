package io.wisoft.poomi.domain.child_care.playground;

import io.wisoft.poomi.domain.child_care.playground.vote.PlaygroundVote;
import io.wisoft.poomi.domain.file.UploadFile;
import io.wisoft.poomi.domain.member.Member;
import io.wisoft.poomi.domain.member.address.Address;
import io.wisoft.poomi.domain.member.address.AddressTag;
import io.wisoft.poomi.global.dto.request.child_care.playground.ChildCarePlaygroundRegisterRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "playground_sequence_generator",
        sequenceName = "playground_sequence",
        initialValue = 3,
        allocationSize = 1
)
@Table(name = "child_care_playground")
public class ChildCarePlayground {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "playground_sequence_generator"
    )
    @Column(name = "id")
    private Long id;

    private String name;

    @Column(name = "operating_hours")
    private String operatingHours;

    private String holiday;

    @Column(name = "call_number")
    private String callNumber;

    @Column(columnDefinition = "TEXT")
    private String features;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id"
    )
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "registrant_id",
            referencedColumnName = "id"
    )
    private Member registrant;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playground_image",
            joinColumns = {@JoinColumn(name = "playground_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id", referencedColumnName = "id")}
    )
    private Set<UploadFile> images;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playground_search",
            joinColumns = {@JoinColumn(name = "playground_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "address_tag_id", referencedColumnName = "id")}
    )
    private Set<AddressTag> searchTags;

    @Builder
    public ChildCarePlayground(final String name, final String operatingHours,
                               final String holiday, final String callNumber,
                               final String features, final Address address,
                               final Member registrant) {
        this.name = name;
        this.operatingHours = operatingHours;
        this.holiday = holiday;
        this.callNumber = callNumber;
        this.features = features;
        this.address = address;
        this.registrant = registrant;
        this.images = new HashSet<>();
        this.searchTags = new HashSet<>();
    }

    public static ChildCarePlayground of(final ChildCarePlaygroundRegisterRequest registerRequest,
                                         final PlaygroundVote vote,
                                         final Member registrant) {
        ChildCarePlayground playground = ChildCarePlayground.builder()
                .name(registerRequest.getName())
                .operatingHours(registerRequest.getOperatingHours())
                .holiday(registerRequest.getHoliday())
                .callNumber(registerRequest.getCallNumber())
                .features(registerRequest.getFeatures())
                .address(vote.getAddress())
                .registrant(registrant)
                .build();
        playground.addSearchTag(vote.getAddress().getAddressTag());

        return playground;
    }

    public void addImage(final UploadFile image) {
        this.images.add(image);
    }

    private void addSearchTag(final AddressTag searchTag) {
        this.searchTags.add(searchTag);
    }

}
