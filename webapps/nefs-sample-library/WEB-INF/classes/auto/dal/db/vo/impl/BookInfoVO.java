package auto.dal.db.vo.impl;


public class BookInfoVO
implements auto.dal.db.vo.BookInfo
{
    
    public java.lang.String getAuthor()
    {
        return author;
    }
    
    public java.lang.Integer getGenre()
    {
        return genre;
    }
    
    public int getGenreInt()
    {
        return getGenreInt(-1);
    }
    
    public int getGenreInt(int defaultValue)
    {
        return genre != null ? genre.intValue() : defaultValue;
    }
    
    public java.lang.String getId()
    {
        return id;
    }
    
    public java.lang.String getIsbn()
    {
        return isbn;
    }
    
    public java.lang.String getName()
    {
        return name;
    }
    
    public void setAuthor(java.lang.String author)
    {
        this.author = author;
    }
    
    public void setGenre(java.lang.Integer genre)
    {
        this.genre = genre;
    }
    
    public void setGenreInt(int genre)
    {
        this.genre = new java.lang.Integer(genre);
    }
    
    public void setId(java.lang.String id)
    {
        this.id = id;
    }
    
    public void setIsbn(java.lang.String isbn)
    {
        this.isbn = isbn;
    }
    
    public void setName(java.lang.String name)
    {
        this.name = name;
    }
    private java.lang.String author;
    private java.lang.Integer genre;
    private java.lang.String id;
    private java.lang.String isbn;
    private java.lang.String name;
}