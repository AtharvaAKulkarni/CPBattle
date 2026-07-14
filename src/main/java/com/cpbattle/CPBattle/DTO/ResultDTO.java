package com.cpbattle.CPBattle.DTO;

import com.cpbattle.CPBattle.entity.Question;
import lombok.Data;

import java.util.List;
@Data
public class ResultDTO {
    private List<Question> problems;
}
