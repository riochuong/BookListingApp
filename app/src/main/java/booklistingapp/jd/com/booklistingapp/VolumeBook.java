package booklistingapp.jd.com.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

/**
 * this class represent a book from googlebooks api query
 */

public class VolumeBook implements Parcelable {
    public static final Creator<VolumeBook> CREATOR = new Creator<VolumeBook>() {
        @Override
        public VolumeBook createFromParcel(Parcel in) {
            return new VolumeBook(in);
        }

        @Override
        public VolumeBook[] newArray(int size) {
            return new VolumeBook[size];
        }
    };
    private String title;
    private String subTitle;
    private List<String> authors; // there might be multiple author
    private String description;
    private String publishedDate;
    private String publisher;
    private HashMap<String, String> industryIdentifiers;

    public VolumeBook(
            String title,
            String subTitle,
            List<String> authors,
            String description,
            String publisher,
            HashMap<String, String> industryIdentifiers,
            String publishedDate
    ) {
        this.authors = authors;
        this.description = description;
        this.industryIdentifiers = industryIdentifiers;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.subTitle = subTitle;
        this.title = title;
    }

    protected VolumeBook(Parcel in) {
        title = in.readString();
        subTitle = in.readString();
        authors = in.createStringArrayList();
        description = in.readString();
        publishedDate = in.readString();
        publisher = in.readString();
    }

    public List<String> getAuthors() {
        return authors;
    }

    /**
     * construct a string of comma-separated author
     *
     * @return
     */
    public String getAuthorsString() {
        StringBuffer sb = new StringBuffer();
        for (String author : getAuthors()) {
            sb.append(author);
            sb.append(",");
        }
        return sb.toString();
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<String, String> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    /**
     * Construct a string of ISBNS for display
     *
     * @return
     */
    public String getISBNSString() {
        StringBuffer sb = new StringBuffer();
        for (String key : industryIdentifiers.keySet()) {
            sb.append(key);
            sb.append("  -  ");
            sb.append(industryIdentifiers.get(key));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getPublisher() {
        return publisher;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(subTitle);
        parcel.writeStringList(authors);
        parcel.writeString(description);
        parcel.writeString(publishedDate);
        parcel.writeString(publisher);
    }

}
