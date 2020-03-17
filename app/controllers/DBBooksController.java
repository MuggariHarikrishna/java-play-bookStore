package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import dao.BooksDao;
import model.Book;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBBooksController extends Controller {

    private final BooksDao booksDao;

    @Inject
    public DBBooksController(BooksDao booksDao) {
        this.booksDao = booksDao;
    }

    public Result allBooks() {
        List<Book> allBooks = booksDao.getAllBooks();
        Map<String, List<Book>> result = new HashMap<>();
        result.put("books", allBooks);
        return ok(Json.toJson(result));
    }


    public Result searchBook(int id) {
        Book book = booksDao.searchBook(id);
        if (book!=null) {
            return ok(Json.toJson(book));
        }
        return ok("Oops! book with id " + id + " is NOT available");
    }

    public Result addBook(Request request) {
        JsonNode json = request.body().asJson();
        Book book = Json.fromJson(json, Book.class);
        try{
            booksDao.addBook(book);
            return ok("Book with id "+book.getBookId()+" added");
        }catch (Exception e){
            System.out.println("Exception : "+e);
            return ok("Error while adding Book with id "+book.getBookId()+" ,Try Later");
        }
    }

    public Result updateBook(int id, Request request) {
        JsonNode json = request.body().asJson();
        Book book = Json.fromJson(json, Book.class);
        try{
            int ret=booksDao.updateBook(id,book);
            if(ret==-1){
                return ok("Book with id "+book.getBookId()+" NOT found");
            }
            return ok("Book with id "+book.getBookId()+" updated");
        }catch (Exception e){
            System.out.println("Exception : "+e);
            return ok("Error while updating Book with id "+book.getBookId()+" ,Try Later");
        }
    }

    public Result deleteBook(int id) {
        try{
            int ret=booksDao.deleteBook(id);
            if(ret==-1){
                return ok("Book with id "+id+" NOT found");
            }
            return ok("Book with id "+id+" updated");
        }catch (Exception e){
            System.out.println("Exception : "+e);
            return ok("Error while deleting Book with id "+id+" ,Try Later");
        }
    }

}
