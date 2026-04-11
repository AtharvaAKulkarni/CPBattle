package com.cpbattle.CPBattle.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PastSubmissionResponse {
    private List<ResponseObject> result;
    @Getter
    @Setter
    public static class ResponseObject{
        QuestionIdentifier problem;
    }
}
