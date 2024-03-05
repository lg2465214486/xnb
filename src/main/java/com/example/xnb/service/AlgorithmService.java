package com.example.xnb.service;

import com.example.xnb.entity.Coin;

import java.time.LocalDateTime;
import java.util.List;

public interface AlgorithmService {
    void addCoinGenerateData(Coin param);

    List<LocalDateTime> generateTimeList(LocalDateTime issueTime);
}
