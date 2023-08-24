package com.Board.repository;

import com.Board.Board.AttachFileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

//@DataJpaTest
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(readOnly = true)
class AttachFileRepositoryTest {
    @Autowired
    private AttachFileRepository attachFileRepository;
    @Test
    @DisplayName("첨부파일 삭제 확인")
//    @Transactional
    void delTest(){
        //given
        Long fileId = 1019L;
        //when
        attachFileRepository.deleteById(fileId);
        //then
    }
}