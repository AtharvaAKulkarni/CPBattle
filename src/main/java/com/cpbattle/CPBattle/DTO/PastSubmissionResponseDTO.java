package com.cpbattle.CPBattle.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PastSubmissionResponseDTO {
    private List<ResponseObject> result;
    @Getter
    @Setter
    public static class ResponseObject{
        String verdict;
        Long id;
        Long creationTimeSeconds;
        QuestionIdentifierDTO problem;
    }
}
