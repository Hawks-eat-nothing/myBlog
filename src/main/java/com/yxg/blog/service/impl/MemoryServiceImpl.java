package com.yxg.blog.service.impl;

import com.yxg.blog.pojo.Memory;
import com.yxg.blog.service.MemoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryServiceImpl implements MemoryService {
    @Override
    public List<Memory> listMemory() {
        return null;
    }

    @Override
    public int saveMemory(Memory memory) {
        return 0;
    }

    @Override
    public Memory getMemory(Long id) {
        return null;
    }

    @Override
    public int updateMemory(Memory memory) {
        return 0;
    }

    @Override
    public void deleteMemory(Long id) {

    }
}
