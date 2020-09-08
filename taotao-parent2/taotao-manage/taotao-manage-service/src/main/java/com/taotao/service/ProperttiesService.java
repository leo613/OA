package com.taotao.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProperttiesService {

    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH; //本地路径

    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL; //远程路径
}
