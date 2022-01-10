package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional // default rollback
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원가입 테스트")
//    @Rollback(value = false) '@Transactional default = rollback'
    public void 회원가입() throws Exception {
        //given -> 주어진 상황
        Member member = new Member();
        member.setName("CWP");

        //when -> 조건
        final Long savedId = memberService.join(member);

        //then -> 검증
        // 파라미터 Member == DB에 저장후 읽어온 Member
        em.flush(); // Insert Query create
        assertEquals(member, memberRepository.findOne(savedId));
    }


    @Test
    @DisplayName("중복 회원 예외 테스트")
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("CWP");

        Member member2 = new Member();
        member2.setName("CWP");

        //when
        memberService.join(member1);
//        memberService.join(member2);

        //then
        final IllegalStateException ex = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.",ex.getMessage());
    }


}