package io.wisoft.poomi.global.dto.response.child_care.playground.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddressSearchResponse {

    private Results results;

    @Getter
    @Setter
    public static class Results {
        private Common common;
        @JsonProperty("juso")
        private List<Juso> jusos;
    }

    @Getter
    @Setter
    public static class Common {
        private String errorMessage;
        private String errorCode;
        private Integer totalCount;
        private Integer countPerPage;
        private Integer currentPage;
    }

    @Getter
    @Setter
    public static class Juso {
        private String roadAddr;
        private String roadAddrPart1;
        private String roadAddrPart2;
        private String jibunAddr;
        private String engAddr;
        private String zipNo;
        private String admCd;
        private String rnMgtSn;
        private String bdMgtSn;
        private String detBdNmList;
        private String bdNm;
        private Integer bdKdcd;
        private String siNm;
        private String sggNm;
        private String emdNm;
        private String liNm;
        private String rn;
        private Integer udrtYn;
        private Integer buldMnnm;
        private Integer buldSlno;
        private Integer mtYn;
        private Integer lnbrMnnm;
        private Integer lnbrSlno;
        private String emdNo;
        private String hstryYn;
        private String relJibun;
        private String hemdNm;
    }
}
