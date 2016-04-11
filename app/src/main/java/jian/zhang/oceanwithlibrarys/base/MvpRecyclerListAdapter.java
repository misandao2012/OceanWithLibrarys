package jian.zhang.oceanwithlibrarys.base;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


// 数据是List的Adapter, 多是完成数据处理的函数
public abstract class MvpRecyclerListAdapter<M, P extends BasePresenter, VH extends MvpViewHolder<P>>
        extends MvpRecyclerAdapter<M, P, VH> {

    private final List<M> mModels;

    public MvpRecyclerListAdapter() {
        mModels = new ArrayList<>();
    }

    public void clearAndAddAll(Collection<M> data) {
        mModels.clear();
        mPresenters.clear();

        for(M item: data) {
            addInternal(item);
        }
        notifyDataSetChanged();
    }

    private int getItemPosition(M item) {
        Object modelId = getModelId(item);

        int position = -1;
        for (int i = 0; i < mModels.size(); i++) {
            M model = mModels.get(i);
            if (getModelId(model).equals(modelId)) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void addInternal(M item) {
        System.err.println("Adding item " + getModelId(item));
        mModels.add(item);
        // 因为本class也是abstract, 不用实现createPresenter, 直接用了也可以
        mPresenters.put(getModelId(item), createPresenter(item));   // 加入相应的presenter, createPresenter在这里调用的
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    @Override
    protected M getItem(int position) {
        return mModels.get(position);
    }
}
