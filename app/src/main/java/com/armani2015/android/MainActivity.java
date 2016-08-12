package com.armani2015.android;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.armani2015.android.adapters.ExpandableGridItemAdapter;
import com.armani2015.android.common.data.AbstractExpandableDataProvider;
import com.armani2015.android.common.data.ExampleExpandableDataProvider;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener, AbstractExpandableItemAdapter.OnCellClickListener {
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private ExampleExpandableDataProvider mDataProvider;

    private int lastExpandedPosition = -10;
    private ExpandableGridItemAdapter myItemAdapter = null;


    /**
     * Callback method to be invoked when a group in this expandable list has been collapsed.
     *
     * @param groupPosition The group position that was collapsed
     * @param fromUser      Whether the collapse request is issued by a user operation
     */
    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {
    }

    /**
     * Callback method to be invoked when a group in this expandable list has been expanded.
     *
     * @param groupPosition The group position that was expanded
     * @param fromUser      Whether the expand request is issued by a user operation
     */
    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        // collapse last expanded group.
        // make sure not to collapse the newly expanded one
        if (lastExpandedPosition != -10 && lastExpandedPosition != groupPosition) {
            mRecyclerViewExpandableItemManager.collapseGroup(lastExpandedPosition);
        }
        lastExpandedPosition = groupPosition;
        myItemAdapter.notifyItemChanged(groupPosition);
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);

        }
    }

    /**
     * Called when a cell has been clicked.
     *
     * @param v    The cell view that was clicked
     * @param data the data object held by this cell
     */
    @Override
    public void onCellClick(View v, Object data) {

        Toast.makeText(this, data + " selected !", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataProvider = new ExampleExpandableDataProvider();

        setupRecyclerView(mDataProvider, savedInstanceState);
        // make the first item expanded by default when activity is first opened
        mRecyclerViewExpandableItemManager.expandGroup(0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPostCreate(Bundle icicle) {
        super.onPostCreate(icicle);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save current state to support screen rotation, etc...
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
    }

    private void setupRecyclerView(ExampleExpandableDataProvider mDataProvider, Bundle savedInstanceState) {
        //noinspection ConstantConditions
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        //adapter
        myItemAdapter = new ExpandableGridItemAdapter(mDataProvider, MainActivity.this);
        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);
        // wrap for expanding
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Need to disable them when using animation indicator.
        animator.setSupportsChangeAnimations(false);

        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(true);
        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z1)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this,
                R.drawable.list_divider_h), true));
        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
    }


    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    private AbstractExpandableDataProvider getDataProvider() {
        return new ExampleExpandableDataProvider();
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = this.getResources().getDimensionPixelSize(R.dimen.list_grid_item_height);
        int topMargin = (int) (this.getResources().getDisplayMetrics().density * 8); // top-spacing: 8dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, topMargin);
    }


}