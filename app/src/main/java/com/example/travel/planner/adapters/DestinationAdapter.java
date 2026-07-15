package com.example.travel.planner.adapters;

import android.content.Context;
import android.view.*;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel.planner.R;
import com.example.travel.planner.models.Destination;
import java.util.List;

public class DestinationAdapter extends
        RecyclerView.Adapter<DestinationAdapter.DestViewHolder> {

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    private static final String[][] DESTINATION_IMAGES = {
            {"paris", "france", "eiffel",
                    "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=600&q=80"},
            {"london", "england", "uk", "britain",
                    "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=600&q=80"},
            {"tokyo", "japan", "osaka", "kyoto",
                    "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=600&q=80"},
            {"new york", "manhattan", "nyc", "brooklyn",
                    "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=600&q=80"},
            {"dubai", "uae", "emirates",
                    "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=600&q=80"},
            {"rome", "italy", "milan", "venice", "florence",
                    "https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=600&q=80"},
            {"bali", "indonesia", "java",
                    "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=600&q=80"},
            {"barcelona", "spain", "madrid",
                    "https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=600&q=80"},
            {"singapore",
                    "https://images.unsplash.com/photo-1525625293386-3f8f99389edd?w=600&q=80"},
            {"sydney", "australia", "melbourne",
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=600&q=80"},
            {"new zealand", "auckland", "queenstown",
                    "https://images.unsplash.com/photo-1469521669194-babb45599def?w=600&q=80"},
            {"maldives", "island", "beach", "ocean",
                    "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?w=600&q=80"},
            {"mountain", "alps", "himalaya", "trek",
                    "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=600&q=80"},
            {"delhi", "india", "mumbai", "goa", "bangalore", "chennai",
                    "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=600&q=80"},
            {"bangkok", "thailand", "phuket",
                    "https://images.unsplash.com/photo-1508009603885-50cf7c579365?w=600&q=80"},
            {"amsterdam", "netherlands", "holland",
                    "https://images.unsplash.com/photo-1512470876302-972faa2aa9a4?w=600&q=80"},
            {"cairo", "egypt", "pyramid",
                    "https://images.unsplash.com/photo-1539650116574-8efeb43e2750?w=600&q=80"},
            {"istanbul", "turkey",
                    "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200?w=600&q=80"},
            {"prague", "czech", "budapest",
                    "https://images.unsplash.com/photo-1519677100203-a0e668c92439?w=600&q=80"},
            {"san francisco", "california", "los angeles", "usa", "america",
                    "https://images.unsplash.com/photo-1501594907352-04cda38ebc29?w=600&q=80"},
    };

    private static final String[] FALLBACK_IMAGES = {
            "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=600&q=80",
            "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=600&q=80",
            "https://images.unsplash.com/photo-1488085061387-422e29b40080?w=600&q=80",
            "https://images.unsplash.com/photo-1506197603052-3cc9c3a201bd?w=600&q=80",
            "https://images.unsplash.com/photo-1503220317375-aaad61436b1b?w=600&q=80",
    };

    private final List<Destination> destinations;
    private final Context context;
    private final OnDeleteListener deleteListener;

    public DestinationAdapter(Context ctx,
                              List<Destination> list, OnDeleteListener listener) {
        this.context      = ctx;
        this.destinations = list;
        this.deleteListener = listener;
    }

    @NonNull @Override
    public DestViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_destination, parent, false);
        return new DestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DestViewHolder holder, int pos) {
        Destination dest = destinations.get(pos);

        holder.tvDayLabel.setText(dest.getDayLabel());
        holder.tvName.setText(dest.getName());
        holder.tvAddress.setText(dest.getAddress());
        holder.tvIndex.setText(String.valueOf(pos + 1));
        holder.tvImageStatus.setText("Loading photo...");

        String imageUrl = getImageForDestination(dest.getName(), pos);
        loadImageInWebView(holder.wvDestImage, imageUrl, holder.tvImageStatus);

        if (deleteListener != null) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                // FIX: use getBindingAdapterPosition() instead of deprecated getAdapterPosition()
                int position = holder.getBindingAdapterPosition();
                if (position != RecyclerView.NO_ID) {
                    deleteListener.onDelete(position);
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    private String getImageForDestination(String name, int position) {
        if (name == null || name.isEmpty()) {
            return FALLBACK_IMAGES[position % FALLBACK_IMAGES.length];
        }
        String lower = name.toLowerCase();
        for (String[] entry : DESTINATION_IMAGES) {
            for (int i = 0; i < entry.length - 1; i++) {
                if (lower.contains(entry[i])) {
                    return entry[entry.length - 1];
                }
            }
        }
        return FALLBACK_IMAGES[position % FALLBACK_IMAGES.length];
    }

    private void loadImageInWebView(WebView webView,
                                    String imageUrl, TextView statusText) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // FIX: wrap in parentheses to avoid operator precedence bug
                statusText.setText(
                        imageUrl.contains("unsplash") ? "Photo by Unsplash" : "");
            }

            // FIX: use non-deprecated onReceivedError
            @Override
            public void onReceivedError(WebView view,
                                        WebResourceRequest request, WebResourceError error) {
                statusText.setText("No photo available offline");
            }
        });

        String html = "<!DOCTYPE html><html><head>" +
                "<meta name='viewport' content='width=device-width,initial-scale=1.0'>" +
                "<style>" +
                "* { margin:0; padding:0; box-sizing:border-box; }" +
                "body { width:100vw; height:140px; overflow:hidden; background:#E0E0E0; }" +
                "img { width:100%; height:140px; object-fit:cover;" +
                "      object-position:center; display:block; }" +
                "</style></head><body>" +
                "<img src='" + imageUrl + "' alt='destination'/>" +
                "</body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @Override
    public int getItemCount() { return destinations.size(); }

    public static class DestViewHolder extends RecyclerView.ViewHolder {
        WebView wvDestImage;
        TextView tvDayLabel, tvName, tvAddress, tvIndex, tvImageStatus;
        ImageButton btnDelete;

        public DestViewHolder(View v) {
            super(v);
            wvDestImage   = v.findViewById(R.id.wv_dest_image);
            tvDayLabel    = v.findViewById(R.id.tv_day_label);
            tvName        = v.findViewById(R.id.tv_dest_name);
            tvAddress     = v.findViewById(R.id.tv_dest_address);
            tvIndex       = v.findViewById(R.id.tv_dest_index);
            tvImageStatus = v.findViewById(R.id.tv_image_status);
            btnDelete     = v.findViewById(R.id.btn_delete_dest);
        }
    }
}