package com.xiaodao.multids.repository.member;

import com.xiaodao.multids.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}