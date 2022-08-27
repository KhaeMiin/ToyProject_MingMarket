package project.toyproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.toyproject.domain.Address;
import project.toyproject.domain.Member;
import project.toyproject.repository.MemberJpaRepository;
import project.toyproject.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static project.toyproject.dto.MemberDto.*;

@Service //Component > 자동 빈 등록(기본적인 내용이지만 적어둠)
@Transactional(readOnly = true) //스프링이 제공하는 트렌젝션
@RequiredArgsConstructor //final 붙은 필드만 가지고 생성자 만든다.
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final MemberJpaRepository memberJpaRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Member join(CreateMemberForm form) {
        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        Member member = new Member(form.getUserId(), form.getNickname(), form.getPassword(),
                form.getUsername(), form.getHp(), address);
        validateDuplicateMember(member); //중복아이디 체크
        checkPassword(form.getPassword(), form.getUserId()); //비밀번호 영문 숫자 특수문자 조합 체크
        member.hashPassword(passwordEncoder.encode(form.getPassword())); //스프링 시큐리티(암호화)
        member.createDate(LocalDateTime.now());
//        memberRepository.save(member); //순수 JPA
        memberJpaRepository.save(member);
        return member;
    }

    /**
     * 회원 전체 조회
     */
    public List<SelectMemberData> findMembers() {
//        List<Member> members = memberRepository.findAllMembers(); //순수 JPA
        List<Member> members = memberJpaRepository.findAll();
/*        List<SelectMemberData> listMemberData = new ArrayList<>();
        for (Member member : members) {
            SelectMemberData selectMemberData = new SelectMemberData(member.getId(), member.getUserId(),
                    member.getUsername(),member.getNickname(), member.getHp(), member.getAddress());
            listMemberData.add(selectMemberData);
        }*/

        //stream 공부하자
        List<SelectMemberData> listMemberData = members.stream()
                .map(m -> new SelectMemberData(m))
                .collect(Collectors.toList());
        return listMemberData;
    }

    /**
     * 회원 단건 조회
     */
    public SelectMemberData findOneMember(Long memberId) {
//        Member member = memberRepository.findOneMember(memberId); //순수 JPA
        Member member = memberJpaRepository.findById(memberId).orElseThrow(() -> {throw new IllegalStateException("저장된 값이 없습니다.");});
//        Member member = memberJpaRepository.findById(memberId).orElseGet(null); //member에 값이 없을 경우 null
        SelectMemberData memberData = new SelectMemberData(member);
        return memberData;
    }

    /**
     * 회원 정보 수정 (변경 감지 사용)
     */
    @Transactional
    public void editInformation(Long memberId, UpdateMemberForm form) {
//        Member findMember = memberRepository.findOneMember(memberId); //순수 JPA
        Member findMember = memberJpaRepository.findById(memberId).orElseThrow(() -> {throw new IllegalStateException("저장된 값이 없습니다.");});
        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        findMember.change(form.getNickname(), form.getUsername(),form.getHp(), address);

    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public void editPassword(Long memberId, UpdateUserPassForm form) {
//        Member findMember = memberRepository.findOneMember(memberId); //순수 JPA
        Member findMember = memberJpaRepository.findById(memberId).orElseThrow(() -> {throw new IllegalStateException("저장된 값이 없습니다.");});
//        findMember.passwordChange(form.getEditYourPassword());
        findMember.hashPassword(passwordEncoder.encode(form.getEditYourPassword())); //시큐리티 암호화
    }

    /**
     * 중복 아이디 검증 메서드
     */
    private void validateDuplicateMember(Member member) {
//        List<Member> findMembers = memberRepository.findByUserId(member.getUserId()); //순수 JPA
        List<Member> findMembers = memberJpaRepository.findMemberByUserId(member.getUserId());
/*        if (!findMembers.isEmpty()) { //isEmpty(): 문자열 길이가 0일 경우 true 반환, 여기서는 !isEmpty: 값이 있다면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }*/
        if (findMembers.size() > 0) { //이 코드가 더 최적화일 것 같다.
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    /**
     * 비밀번호 영문 숫자 특수문자 조합 체크
     * @param pwd : 비밀번호
     * @param id : 아이디
     * @return
     */
    public void checkPassword(String pwd, String id){

        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{10,20}$");
        Matcher passMatcher1 = passPattern1.matcher(pwd);

        if(!passMatcher1.find()){
            throw new IllegalStateException("비밀번호는 영문과 특수문자 숫자를 포함하며 10자 이상 20자 이하 이어야 합니다.");
        }

        // 반복된 문자 확인
        Pattern passPattern2 = Pattern.compile("(\\w)\\1\\1\\1");
        Matcher passMatcher2 = passPattern2.matcher(pwd);

        if(passMatcher2.find()){
            throw new IllegalStateException("비밀번호에 동일한 문자를 과도하게 연속해서 사용할 수 없습니다.");
        }

        // 아이디 포함 확인
        if(pwd.contains(id)){
            throw new IllegalStateException("비밀번호에 ID를 포함할 수 없습니다.");
        }

        // 특수문자 확인
        Pattern passPattern3 = Pattern.compile("\\W");
        Pattern passPattern4 = Pattern.compile("[!@#$%^*+=-]");

        for(int i = 0; i < pwd.length(); i++){
            String s = String.valueOf(pwd.charAt(i));
            Matcher passMatcher3 = passPattern3.matcher(s);

            if(passMatcher3.find()){
                Matcher passMatcher4 = passPattern4.matcher(s);
                if(!passMatcher4.find()){
                    throw new IllegalStateException("비밀번호에 특수문자는 !@#$^*+=-만 사용 가능합니다.");
                }
            }
        }

        //연속된 문자 확인
        int ascSeqCharCnt = 0; // 오름차순 연속 문자 카운트
        int descSeqCharCnt = 0; // 내림차순 연속 문자 카운트

        char char_0;
        char char_1;
        char char_2;

        int diff_0_1;
        int diff_1_2;

        for(int i = 0; i < pwd.length()-2; i++){
            char_0 = pwd.charAt(i);
            char_1 = pwd.charAt(i+1);
            char_2 = pwd.charAt(i+2);

            diff_0_1 = char_0 - char_1;
            diff_1_2 = char_1 - char_2;

            if(diff_0_1 == 1 && diff_1_2 == 1){
                ascSeqCharCnt += 1;
            }

            if(diff_0_1 == -1 && diff_1_2 == -1){
                descSeqCharCnt -= 1;
            }
        }

        if(ascSeqCharCnt > 1 || descSeqCharCnt > 1){
            throw new IllegalStateException("비밀번호에 연속된 문자열을 사용할 수 없습니다.");
        }

    }

    /**
     * API 회원 정보 수정 메서드
     * @param id : memberId(pk)
     * @param form
     * @return
     */
    @Transactional //수정시 읽기전용이면 스냅샷 저장, 변경감지 수행등을 하지 않는다..
    public SelectMemberData updateMember(Long id, UpdateMemberForm form) {
//        Member member = memberRepository.findOneMember(id);
        Member member = memberJpaRepository.findById(id).orElseThrow(() -> {throw new IllegalStateException("저장된 값이 없습니다.");});

        Address address = new Address(form.getAddress(), form.getDetailedAddress());
        member.change(form.getNickname(), form.getUsername(), form.getHp(), address);
        return new SelectMemberData(member);
    }
}