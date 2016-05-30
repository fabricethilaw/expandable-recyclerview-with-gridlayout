/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.armani2015.android.common.data;

import com.armani2015.android.utils.CONSTANTS;
import com.armani2015.android.utils.ListChopper;

import java.util.ArrayList;
import java.util.List;

public class ExampleExpandableDataProvider extends AbstractExpandableDataProvider {

    private String mData = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    List<ConcreteGroupData> concreteGroupList;

    /**
     * Constructor
     */
    public ExampleExpandableDataProvider() {
        // extract group items from data
        final String groupItems = mData;
        concreteGroupList = new ArrayList<>();

        for (int i = 0; i < groupItems.length(); i++) {
            //noinspection UnnecessaryLocalVariable
            final long groupId = i;
            final String groupText = Character.toString(groupItems.charAt(i));

            final ConcreteGroupData group = new ConcreteGroupData(groupId, groupText);
            // add group item to groups' list
            concreteGroupList.add(group);
        }

    }

    @Override
    public int getGroupCount() {
        return concreteGroupList.size();
    }

    @Override
    public int getGridCount(int groupPosition) {
        return concreteGroupList.get(groupPosition).getConcreteGridDataList().size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return concreteGroupList.get(groupPosition);
    }

    @Override
    public GridData getGridItem(int groupPosition, int gridPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        if (gridPosition < 0 || gridPosition >= ((ConcreteGroupData) getGroupItem(groupPosition)).getConcreteGridDataList().size()) {
            throw new IndexOutOfBoundsException("gridPosition = " + gridPosition);
        }
        return ((ConcreteGroupData) getGroupItem(groupPosition)).getConcreteGridDataList().get(gridPosition);
    }

    /**
     * A concrete group data. It splits its children data into small arrays.
     */
    public final class ConcreteGroupData extends GroupData {

        private long mId;
        private boolean mPinned;
        private String mText;
        private List<ConcreteGridData> gridChildDataList = new ArrayList<>();

        public ConcreteGroupData(long groupId, String groupText) {
            mText = groupText;
            mId = groupId;
            // each group manages its children data
            String mChildDataSource = "abcdefghijkl";
            List<String> childrenData = new ArrayList<>();
            for (int j = 0; j < mChildDataSource.length(); j++) {
                final String childText = mText + Character.toString(mChildDataSource.charAt(j));
                childrenData.add(childText);
            }
            // split whole children data into small arrays of data, each array must have at most the same size
            List<List<String>> childrenDataArrays = ListChopper.splitListBySize(childrenData, CONSTANTS.MAX_CELLS_PER_GRID_ROW);

            // Then each grid holds an array of children data
            for (int k = 0; k < childrenDataArrays.size(); k++) {
                final ConcreteGridData gridData = new ConcreteGridData(k, childrenDataArrays.get(k));
                gridChildDataList.add(gridData);
            }
        }

        public List<ConcreteGridData> getConcreteGridDataList() {
            return gridChildDataList;
        }

        @Override
        public long getGroupId() {
            return mId;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

    }


    /**
     * An ARRAY of concrete child data
     */
    public static final class ConcreteGridData extends GridData {

        private int mId;

        private boolean mPinned;
        // would lay more than one child data per row
        private List<String> childDataArray;

        ConcreteGridData(int id, List<String> childDataArray) {
            mId = id;
            this.childDataArray = childDataArray;
        }

        @Override
        public long getGridId() {
            return mId;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public void setGridId(int id) {
            this.mId = id;
        }

        public List<String> getGridDataArray() {
            return childDataArray;
        }
    }
}
