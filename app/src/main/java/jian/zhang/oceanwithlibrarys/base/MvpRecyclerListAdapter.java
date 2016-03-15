package jian.zhang.oceanwithlibrarys.base;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class MvpRecyclerListAdapter<M, P extends BasePresenter, VH extends MvpViewHolder<P>>
        extends MvpRecyclerAdapter<M, P, VH> {

    private final List<M> models;

    public MvpRecyclerListAdapter() {
        models = new ArrayList<>();
    }

    public void clearAndAddAll(Collection<M> data) {
        models.clear();
        mPresenters.clear();

        for(M item: data) {
            addInternal(item);
        }
        notifyDataSetChanged();
    }

    private int getItemPosition(M item) {
        Object modelId = getModelId(item);

        int position = -1;
        for (int i = 0; i < models.size(); i++) {
            M model = models.get(i);
            if (getModelId(model).equals(modelId)) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void addInternal(M item) {
        System.err.println("Adding item " + getModelId(item));
        models.add(item);
        // 因为本class也是abstract, 不用实现createPresenter, 直接用了也可以
        mPresenters.put(getModelId(item), createPresenter(item));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    protected M getItem(int position) {
        return models.get(position);
    }
}
