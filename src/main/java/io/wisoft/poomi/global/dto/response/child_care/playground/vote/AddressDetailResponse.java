package io.wisoft.poomi.global.dto.response.child_care.playground.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddressDetailResponse {

    private Results results;

    @Getter
    @Setter
    public static class Results {
        private Common common;
        @JsonProperty("juso")
        private List<Juso> jusoList;
    }

    @Getter
    @Setter
    public static class Common {
        private Integer totalCount;
        private String errorCode;
        private String errorMessage;
    }

    @Getter
    @Setter
    public static class Juso {
        private String admCd;
        private String rnMgtSn;
        private Integer udrtYn;
        private Integer buldMnnm;
        private Integer buldSlno;
        private String dongNm;
        private String floorNm;
        private String hoNm;
    }

}
