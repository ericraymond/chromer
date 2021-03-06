package arun.com.chromer.webheads.ui.context;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;

import arun.com.chromer.R;
import arun.com.chromer.webheads.helper.WebSite;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Arun on 05/09/2016.
 */

class WebsiteAdapter extends RecyclerView.Adapter<WebsiteAdapter.WebSiteHolder> {

    private final Context mContext;
    private final List<WebSite> mWebSites = new ArrayList<>();
    private final InteractionListener listener;

    WebsiteAdapter(@NonNull Context context, @NonNull InteractionListener listener) {
        mContext = context.getApplicationContext();
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public WebSiteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WebSiteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.web_head_context_item_template, parent, false));
    }

    @Override
    public void onBindViewHolder(WebSiteHolder holder, int position) {
        final WebSite webSite = mWebSites.get(position);
        holder.url.setText(webSite.url);
        holder.deleteIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_close)
                .color(ContextCompat.getColor(mContext, R.color.accent_icon_nofocus))
                .sizeDp(16));
        holder.shareIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(CommunityMaterial.Icon.cmd_share_variant)
                .color(ContextCompat.getColor(mContext, R.color.accent_icon_nofocus))
                .sizeDp(16));
        if (webSite.title != null && webSite.title.length() > 0) {
            holder.title.setText(webSite.title);
        } else {
            holder.title.setText(webSite.url);
        }

        if (webSite.icon != null) {
            holder.icon.setImageBitmap(webSite.icon);
        } else {
            Glide.with(mContext)
                    .load(webSite.faviconUrl)
                    .crossFade()
                    .into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return mWebSites.size();
    }

    @Override
    public long getItemId(int position) {
        return mWebSites.get(position).hashCode();
    }

    void setWebsites(ArrayList<WebSite> webSites) {
        mWebSites.clear();
        mWebSites.addAll(webSites);
        notifyDataSetChanged();
    }

    List<WebSite> getWebSites() {
        return mWebSites;
    }

    class WebSiteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.web_site_icon)
        ImageView icon;
        @BindView(R.id.web_site_title)
        TextView title;
        @BindView(R.id.web_site_sub_title)
        TextView url;
        @BindView(R.id.delete_icon)
        ImageView deleteIcon;
        @BindView(R.id.share_icon)
        ImageView shareIcon;

        WebSiteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        final WebSite webSite = mWebSites.get(position);
                        if (webSite != null) {
                            listener.onWebSiteItemClicked(webSite);
                        }
                    }
                }
            });

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        final WebSite webSite = mWebSites.get(position);
                        if (webSite != null) {
                            mWebSites.remove(webSite);
                            listener.onWebSiteDelete(webSite);
                            notifyDataSetChanged();
                        }
                    }
                }
            });

            shareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        final WebSite webSite = mWebSites.get(position);
                        if (webSite != null) {
                            listener.onWebSiteShare(webSite);
                        }
                    }
                }
            });
        }
    }

    interface InteractionListener {
        void onWebSiteItemClicked(@NonNull WebSite webSite);

        void onWebSiteDelete(@NonNull WebSite webSite);

        void onWebSiteShare(@NonNull WebSite webSite);
    }
}
