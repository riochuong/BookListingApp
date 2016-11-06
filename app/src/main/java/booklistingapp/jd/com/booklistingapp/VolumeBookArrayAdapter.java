package booklistingapp.jd.com.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by chuondao on 11/6/16.
 */

public class VolumeBookArrayAdapter extends ArrayAdapter {

    public VolumeBookArrayAdapter(Context context, List<VolumeBook> listBook) {
        super(context, 0, listBook);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // check if convert view is reusuable or not
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book_item_layout,null);
        }

        // set the data
        VolumeBook book = (VolumeBook) getItem(position);
        setBookData(convertView,book);
        return convertView;
    }

    /**
     * helper to set all the correct book data
     * @param itemLayout
     */
    private void setBookData (View itemLayout, VolumeBook book){
        TextView titleText = (TextView) itemLayout.findViewById(R.id.title_text);
        TextView authorText = (TextView) itemLayout.findViewById(R.id.authors_text);
        TextView subtitleText = (TextView) itemLayout.findViewById(R.id.subtitle_text);
        TextView publisherText = (TextView) itemLayout.findViewById(R.id.publisher_text);
        TextView publishedDateText = (TextView) itemLayout.findViewById(R.id.published_date_text);
        TextView descriptionText = (TextView) itemLayout.findViewById(R.id.description_text);
        TextView identifiersText = (TextView) itemLayout.findViewById(R.id.identifiers_text);

        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthorsString());
        subtitleText.setText(book.getSubTitle());
        publisherText.setText(book.getPublisher());
        publishedDateText.setText(book.getPublishedDate());
        descriptionText.setText(book.getDescription());
        identifiersText.setText(book.getISBNSString());

    }
}
