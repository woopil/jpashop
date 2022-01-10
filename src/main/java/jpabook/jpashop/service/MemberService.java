package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 적용된 필드의 생성자 생성
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired - 필드 주입
//    private MemberRepository memberRepository;


//    생성자 주입
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired - 세터 주입
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }



    /*
     * 회원 가입
     * */
    @Transactional  // public M 적용 , 'readOnly = true' 는 읽기 기능만
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    // M-중복 회원 검증 + DB 컬럼에 유니크 제약 조건 추가 => 멀티 쓰레드 대비
    private void validateDuplicateMember(Member member) {
        final List<Member> findMembers = memberRepository.findByName(member.getName());
        // 이룸이 존재하면
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // M-회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // M-회원 아이디 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
