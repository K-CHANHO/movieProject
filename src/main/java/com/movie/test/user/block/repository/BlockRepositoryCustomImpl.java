package com.movie.test.user.block.repository;

import com.movie.test.user.block.entity.QBlockEntity;
import com.movie.test.user.follow.dto.FollowListSearchDTO;
import com.movie.test.user.userinfo.entity.QUserEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class BlockRepositoryCustomImpl implements BlockRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private QBlockEntity block = QBlockEntity.blockEntity;
    private QUserEntity user = QUserEntity.userEntity;

    @Override
    public Slice<Tuple> getBlockList(FollowListSearchDTO searchDTO, Pageable pageable) {

        List<Tuple> fetch = jpaQueryFactory.select(block.blockId, block.targetId, user.nickname)
                .from(block)
                .leftJoin(user).on(block.targetId.eq(user.userId))
                .where(
                        block.userId.eq(searchDTO.getUserId()),
                        block.userId.gt(searchDTO.getLastUserId()),
                        nicknameCheck(searchDTO.getKeyword())
                )
                .limit(pageable.getPageSize() + 1)
                .orderBy(block.createDate.desc())
                .fetch();

        boolean hasNext = false;
        if(fetch.size() > pageable.getPageSize()){
            hasNext = true;
            fetch.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(fetch, pageable, hasNext);
    }

    private BooleanExpression nicknameCheck(String keyword) {
        return keyword != null ? user.nickname.contains(keyword) : null;
    }
}
