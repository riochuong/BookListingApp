package booklistingapp.jd.com.booklistingapp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * this class represent a book from googlebooks api query
 */

public class VolumeBook {
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

    public List<String> getAuthors() {
        return authors;
    }

    /**
     * construct a string of comma-separated author
     * @return
     */
    public String getAuthorsString(){
        StringBuffer sb = new StringBuffer();
        for (String author: getAuthors()){
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
     * @return
     */
    public String getISBNSString(){
        StringBuffer sb = new StringBuffer();
        for (String key: industryIdentifiers.keySet())
        {
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
}
