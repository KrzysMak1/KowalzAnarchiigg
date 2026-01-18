package cc.dreamcode.kowal.libs.cc.dreamcode.notice.minecraft;

import lombok.Generated;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.kowal.libs.cc.dreamcode.notice.Notice;

public class NoticeImpl<R extends Notice<R>> extends Notice<R>
{
    private final NoticeType noticeType;
    private final String noticeText;
    private int titleFadeIn;
    private int titleStay;
    private int titleFadeOut;
    
    public NoticeImpl(final NoticeType noticeType, final String... noticeText) {
        this.titleFadeIn = 10;
        this.titleStay = 20;
        this.titleFadeOut = 10;
        this.noticeType = noticeType;
        if (noticeText.length == 1) {
            this.noticeText = noticeText[0];
            return;
        }
        this.noticeText = StringUtil.join(noticeText, lineSeparator());
    }
    
    @Override
    public String getRaw() {
        return this.noticeText;
    }
    
    @Override
    public Enum<?> getNoticeType() {
        return this.noticeType;
    }
    
    public static String lineSeparator() {
        return "%NEWLINE%";
    }
    
    @Generated
    public void setTitleFadeIn(final int titleFadeIn) {
        this.titleFadeIn = titleFadeIn;
    }
    
    @Generated
    public int getTitleFadeIn() {
        return this.titleFadeIn;
    }
    
    @Generated
    public void setTitleStay(final int titleStay) {
        this.titleStay = titleStay;
    }
    
    @Generated
    public int getTitleStay() {
        return this.titleStay;
    }
    
    @Generated
    public void setTitleFadeOut(final int titleFadeOut) {
        this.titleFadeOut = titleFadeOut;
    }
    
    @Generated
    public int getTitleFadeOut() {
        return this.titleFadeOut;
    }
}
