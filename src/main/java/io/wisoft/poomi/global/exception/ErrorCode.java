package io.wisoft.poomi.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DUPLICATE_MEMBER_INFO(400),
    NO_MATCH_DATA(400),
    ALREADY_EXISTS_GROUP_NAME(400, "이미 존재하는 품앗이반 이름입니다."),
    ALREADY_REQUEST(400, "컨텐츠에 대해 이미 요청한 작업입니다."),
    NOT_FOUND_ENTITY_DATA(404, "입력하신 인증번호는 유효하지 않습니다."),
    BAD_CREDENTIALS(400, "입력한 이메일과 패스워드가 일치하지 않습니다."),
    CERTIFICATION_TOKEN_EXPIRED(400, "인증가능한 시간이 만료되었습니다."),
    NOT_APPROVED_VOTE(400, "아직 승인되지 않은 투표입니다."),
    NO_PERMISSION(403, "컨텐츠에 대한 요청 접근 권한이 없습니다."),

    ILLEGAL_ARGUMENT(400),
    ARGUMENT_NOT_VALID(400),
    NOT_HANDLING_FILE_TYPE(400),
    INVALID_FORMAT(400, "파라미터를 읽어들일 수 없습니다."),
    TYPE_MISMATCH(400, "잘못된 타입으로 요청했습니다."),
    INVALID_JSON_FORMAT(400, "읽어들일 수 없는 데이터입니다."),
    FILE_UPLOAD_FAILED(400, "파일을 첨부해야 합니다."),
    FILE_NOT_READABLE(400, "파일을 읽을 수 없습니다."),
    NOT_SUPPORTED_MEDIA_TYPE(400, "지원하지 않는 Content-type 입니다."),
    HTTP_CLIENT_ERROR(400, "소셜 플랫폼에 요청을 실패하였습니다."),
    UN_AUTHENTICATION(401, "로그인이 필요한 서비스입니다."),
    FORBIDDEN(403, "허가되지 않은 요청입니다."),
    NOT_FOUND(404, "요청하신 페이지를 찾을 수 없습니다."),
    FILE_NOT_FOUND(404, "요청하신 파일을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "지원하지 않는 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "서버측 에러입니다.");

    private final int statusCode;
    private String message;

    private ErrorCode(final int statusCode) {
        this(statusCode, null);
    }

    private ErrorCode(final int statusCode,
                      final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    private void setMessage(final String message) {
        this.message = message;
    }
    public static ErrorCode duplicateMemberInfo(final String message) {
        ErrorCode duplicateMemberInfo = DUPLICATE_MEMBER_INFO;
        duplicateMemberInfo.setMessage(message);

        return duplicateMemberInfo;
    }

    public static ErrorCode notFound(final String message) {
        ErrorCode notFound = NOT_FOUND_ENTITY_DATA;
        notFound.setMessage(message);

        return notFound;
    }

    public static ErrorCode illegalArgument(final String message) {
        ErrorCode illegalArgument = ILLEGAL_ARGUMENT;
        illegalArgument.setMessage(message);

        return illegalArgument;
    }

    public static ErrorCode argumentNotValid(final String message) {
        ErrorCode argumentNotValid = ARGUMENT_NOT_VALID;
        argumentNotValid.setMessage(message);

        return argumentNotValid;
    }

    public static ErrorCode notHandlingFileType(final String message) {
        ErrorCode notHandlingFileType = NOT_HANDLING_FILE_TYPE;
        notHandlingFileType.setMessage(message);

        return notHandlingFileType;
    }

}
