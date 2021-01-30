package com.e.scrolltabdemo.widget;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weioule
 * on 2021/1/2.
 */
public class OffsetLinearLayoutManager extends StaggeredGridLayoutManager {

    public OffsetLinearLayoutManager() {
        super(2, StaggeredGridLayoutManager.VERTICAL);
    }

    private Map<Integer, Integer> heightMap = new HashMap<>();

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            heightMap.put(i, view.getHeight());
        }
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        try {
            int[] firstVisibleItemPositions = findFirstVisibleItemPositions(new int[2]);
            int firstVisiablePosition = (firstVisibleItemPositions[1] + 1) / 2;
            View firstVisiableView = findViewByPosition(firstVisiablePosition);
            int offsetY = -(int) (firstVisiableView.getY());
            for (int i = 0; i < firstVisiablePosition; i++) {
                offsetY += heightMap.get(i) == null ? 0 : heightMap.get(i);
            }
            return offsetY;
        } catch (Exception e) {
            return 0;
        }
    }
}

