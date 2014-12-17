package cashmanager.helo.com.model;

import java.util.Date;

import cashmanager.helo.com.model.bd.Record;

/**
 * Created by Oleg on 17.12.2014.
 */
public class ReportItem extends Record {

    public int color;

    public ReportItem(Record record) {
        this.id = record.id;
        this.date = record.date;
        this.description = record.description;
        this.cost = record.cost;
    }
}
