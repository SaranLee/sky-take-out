package com.sky.service.impl;

import com.sky.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CommonServiceImpl implements CommonService {

    @Override
    public String upload(MultipartFile file) {

        return "";
    }
}
