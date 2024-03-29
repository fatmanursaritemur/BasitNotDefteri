package com.example.myapplication;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    //Rect, bir dikdörtgen için dört tamsayı koordinatını tutar. Dikdörtgen, 4 kenarının koordinatları (sol, üst, sağ alt) ile temsil edilir.
    public void getItemOffsets(Rect oRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            oRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            oRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                oRect.top = spacing;
            }
            oRect.bottom = spacing; // item bottom
        } else {
            oRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            oRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                oRect.top = spacing; // item top
            }
        }
    }
}
