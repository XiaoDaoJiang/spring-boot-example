package com.xiaodao.jta.repository.member;

import com.xiaodao.jta.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}