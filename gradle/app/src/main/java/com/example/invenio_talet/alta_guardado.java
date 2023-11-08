package com.example.invenio_talet;

public class alta_guardado {

    private String title, author, date, content;

    public alta_guardado(String title,String author, String date,String content) {
        this.title=title;
        this.author=author;
        this.date=date;
        this.content=content;
    }




    public String getTitle(){return title;}
    public void setTitle(String title) {this.title=title;}

   public String getAuthor(){return author;}
    public void setAuthor(String author){this.author=author;}

    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getContent(){return content;}
    public void setContent(String content){this.content=content;}

}
