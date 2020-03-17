package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.Book;
import play.*;

import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;


import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class BooksController extends Controller {

    public static Map<Integer, Book> books = new HashMap<>();

    public BooksController() {
        Book book1 = new Book(1, "c", "denis c author", 100);
        Book book2 = new Book(2, "c++", "c++ author", 200);
        books.put(1, book1);
        books.put(2, book2);
    }

    public Result allBooks() {
        List<Book> allBooks = new ArrayList<>();
        for (int id : books.keySet()) {
            allBooks.add(books.get(id));
        }
        Map<String, List<Book>> result = new HashMap<>();
        result.put("books", allBooks);
        return ok(Json.toJson(result));
    }

    public Result searchBook(int id) {
        if (books.containsKey(id)) {
            return ok(Json.toJson(books.get(id)));
        }
        return ok("Oops! book with id " + id + " is NOT available");
    }

    public Result addBook(Http.Request request) {
        JsonNode json = request.body().asJson();
        Book book = Json.fromJson(json, Book.class);
        books.put(book.getBookId(), book);
        return ok("book with id  " + book.getBookId() + " added to books");
    }

    public Result updateBook(int id,Http.Request request) {
        JsonNode json = request.body().asJson();
        if (!books.containsKey(id)) {
            return ok("Oops! book with id " + id + " is NOT available in books");
        }
        Book book = Json.fromJson(json, Book.class);
        books.put(book.getBookId(), book);
        return ok("book with id  " + book.getBookId() + " updated in books");
    }

    public Result deleteBook(int id) {
        if (!books.containsKey(id)) {
            return ok("Oops! book with id " + id + " is NOT available in books");
        }
        books.remove(id);
        return ok("book with id  " + id + " deleted in books");
    }

}
