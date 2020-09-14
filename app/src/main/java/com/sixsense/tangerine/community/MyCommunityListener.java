package com.sixsense.tangerine.community;

import com.sixsense.tangerine.community.item.Member;

public interface MyCommunityListener {
    void showMarketListFragment(); //마켓 갱신
    void showBoardListFragment(); //게시판 갱신

    Member getMember();
    void setMemberLocation(String localString, int localCode);
}