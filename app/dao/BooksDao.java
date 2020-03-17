package dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Book;
import org.bson.Document;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import javax.print.Doc;
import java.util.*;

public class BooksDao {
    MongoCollection<Document> booksCollection;
    MongoClient client;


    public BooksDao() {
//        String host = ConfigFactory.load().getString("mongo.host");
//        int port = Integer.parseInt(ConfigFactory.load().getString("mongo.port"));
        client = new MongoClient("localhost", 27017);
        MongoDatabase database = client.getDatabase("bookStore");
        booksCollection = database.getCollection("books");
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        FindIterable<Document> cursor = booksCollection.find();
        cursor.iterator().forEachRemaining(item -> {
            JsonNode bookJson = Json.toJson(item);
            books.add(Json.fromJson(bookJson, Book.class));
        });
        return books;
    }

    public Book searchBook(int id) {
        Document query = new Document();
        query.put("bookId", id);
        Document bookDoc = booksCollection.find(query).first();
        JsonNode bookJson = Json.toJson(bookDoc);
        Book book = Json.fromJson(bookJson, Book.class);
        return book;
    }

    public void addBook(Book book) {
        JsonNode jsonBook = Json.toJson(book);
        Document docBook = Json.fromJson(jsonBook, Document.class);
        booksCollection.insertOne(docBook);
    }

    public int updateBook(int id, Book book) {
        Document query = new Document();
        query.put("bookId", id);
        Document bookDoc = booksCollection.find(query).first();
        JsonNode bookJson = Json.toJson(bookDoc);
        Book findBook = Json.fromJson(bookJson, Book.class);
        if (findBook == null) {
            return -1;
        }
        Document updateQuery = new Document();
        Document updateDoc = new Document();
        updateDoc.put("bookId", book.getBookId());
        updateDoc.put("bookName", book.getBookName());
        updateDoc.put("author", book.getAuthor());
        updateDoc.put("price", book.getPrice());
        updateQuery.put("$set", updateDoc);
        booksCollection.updateOne(query, updateQuery);
        return 1;
    }

    public int deleteBook(int id) {
        Document query = new Document();
        query.put("bookId", id);
        Document bookDoc = booksCollection.find(query).first();
        JsonNode bookJson = Json.toJson(bookDoc);
        Book findBook = Json.fromJson(bookJson, Book.class);
        if (findBook == null) {
            return -1;
        }
        booksCollection.deleteOne(query);
        return 1;
    }
}
