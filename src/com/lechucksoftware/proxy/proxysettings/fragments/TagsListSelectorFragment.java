package com.lechucksoftware.proxy.proxysettings.fragments;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lechucksoftware.proxy.proxysettings.ApplicationGlobals;
import com.lechucksoftware.proxy.proxysettings.R;
import com.lechucksoftware.proxy.proxysettings.adapters.TagsListAdapter;
import com.lechucksoftware.proxy.proxysettings.components.TagModel;
import com.lechucksoftware.proxy.proxysettings.db.DBProxy;
import com.lechucksoftware.proxy.proxysettings.db.DBTag;
import com.lechucksoftware.proxy.proxysettings.db.DataSource;
import com.lechucksoftware.proxy.proxysettings.db.DatabaseSQLiteOpenHelper;
import com.lechucksoftware.proxy.proxysettings.fragments.base.BaseDialogFragment;
import com.lechucksoftware.proxy.proxysettings.loaders.TagsTaskLoader;
import com.shouldit.proxy.lib.log.LogWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 17/05/13.
 */
public class TagsListSelectorFragment extends BaseDialogFragment implements LoaderManager.LoaderCallbacks<List<TagModel>>
{
    private static final String TAG = TagsListSelectorFragment.class.getSimpleName();
    public static final String DBPROXY_ARG = "DBPROXY_ARG";
    private static TagsListSelectorFragment instance;
    private TextView emptyText;
    private RelativeLayout progress;
    private static final int LOADER_TAGSDB = 1;
    private Loader<List<TagModel>> loader;
    private ListView listView;
    private TagsListAdapter tagsListAdapter;
    private DBProxy proxy;
    private Button okButton;
    private Dialog dialog;

    private TagsListSelectorFragment()
    {}

    public static TagsListSelectorFragment newInstance(DBProxy proxy)
    {
        if (instance == null)
            instance = new TagsListSelectorFragment();

        Bundle args = new Bundle();
        args.putSerializable(DBPROXY_ARG,proxy);
        instance.setArguments(args  );

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        proxy = (DBProxy) getArguments().getSerializable(DBPROXY_ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dialog = getDialog();
        if (dialog != null)
        {
            dialog.setTitle("AAAAAA");
        }

        View v = inflater.inflate(R.layout.tags_dialog_list, container, false);

        progress = (RelativeLayout) v.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        emptyText = (TextView) v.findViewById(android.R.id.empty);
        listView = (ListView) v.findViewById(android.R.id.list);
        okButton = (Button) v.findViewById(R.id.dialog_ok);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for (int i = 0; i<tagsListAdapter.getCount(); i++)
                {
                    TagModel m = tagsListAdapter.getItem(i);
                    if (m.isSelected)
                    {
                        proxy.addTag(m.tag);
                    }
                    else
                    {
                        proxy.removeTag(m.tag);
                    }
                }

                ApplicationGlobals.getDBManager().updateProxy(proxy.getId(),proxy);

                dialog.dismiss();
            }
        });

        if (tagsListAdapter == null)
        {
            tagsListAdapter = new TagsListAdapter(getActivity());
        }

        listView.setAdapter(tagsListAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        loader = getLoaderManager().initLoader(LOADER_TAGSDB, new Bundle(), this);
        loader.forceLoad();

        return v;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(getActivity().getResources().getString(R.string.proxy_configurations));
    }

    /**
     * LoaderManager Interface methods
     */

    @Override
    public Loader<List<TagModel>> onCreateLoader(int i, Bundle bundle)
    {
        TagsTaskLoader tagsDBTaskLoader = new TagsTaskLoader(getActivity(), proxy);
        return tagsDBTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<TagModel>> listLoader, List<TagModel> tagModels)
    {
        if (tagModels != null && tagModels.size() > 0)
        {
            tagsListAdapter.setData(tagModels);
            listView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
        else
        {
            tagsListAdapter.setData(new ArrayList<TagModel>());
            listView.setVisibility(View.GONE);
            emptyText.setText(getResources().getString(R.string.tags_empty_list));
            emptyText.setVisibility(View.VISIBLE);
        }

        progress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<TagModel>> listLoader)
    {

    }

}
