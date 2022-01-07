package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("JPA 테스트")
    public void testMember() {
        //given
        Member member = new Member();
        member.setName("CWP2");

        //when
        final Long saveId = memberRepository.save(member);
        final Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember).isEqualTo(member);

    }
}