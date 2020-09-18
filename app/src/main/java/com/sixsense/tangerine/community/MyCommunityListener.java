package com.sixsense.tangerine.community;

import com.sixsense.tangerine.community.item.Member;

public interface MyCommunityListener {
    void showMarketListFragment(); //마켓 갱신
    void showNoLocationFragment();
    void showCookingTipListFragment();

    Member getMember();
    void setMemberLocation(String localString, int localCode);
}