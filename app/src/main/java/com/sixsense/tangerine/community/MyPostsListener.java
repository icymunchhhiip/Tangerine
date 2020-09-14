package com.sixsense.tangerine.community;

import com.sixsense.tangerine.community.item.Member;

public interface MyPostsListener {
    void showMyMarketListFragment(); //내가 쓴 글(마켓) 목록갱신
    void showMyBoardListFragment(); //내가 쓴 글(게시판) 목록갱신

    Member getMember();
}
