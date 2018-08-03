package com.andy.jaa;

import android.graphics.PointF;

/**
 * PointData
 *
 * @author: andyJaa
 */
public final class PointData {
    private PointF pointF;
    private String markTitle;
    private String markContent;

    public PointData(PointF pointF, String markTitle, String markContent) {
        this.pointF = pointF;
        this.markTitle = markTitle;
        this.markContent = markContent;
    }

    public PointF getPointF() {
        return pointF;
    }

    public String getMarkTitle() {
        return markTitle;
    }

    public String getMarkContent() {
        return markContent;
    }

}
