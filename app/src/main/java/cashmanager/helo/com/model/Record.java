package cashmanager.helo.com.model;

import com.orm.SugarRecord;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class Record extends SugarRecord<Record> {

    public String id;

    public String date;

    public String title;

    public String description;

    public Integer cost;

    public Category category;

    public Attachment attachment;

}
