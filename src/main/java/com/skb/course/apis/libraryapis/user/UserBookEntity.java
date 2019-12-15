package com.skb.course.apis.libraryapis.user;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "USER_BOOk")
public class UserBookEntity {

    @Column(name = "UserBook_Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userBook_Id_generator")
    @SequenceGenerator(name="userBook_Id_generator", sequenceName = "user_book_sequence", allocationSize=1)
    private int userBook_Id;

    @Column(name = "User_Id")
    private int userId;

    @Column(name = "Book_Id")
    private int bookId;

    @Column(name = "Issued_Date")
    private LocalDate issuedDate;

    @Column(name = "Return_Date")
    private LocalDate returnDate;

    @Column(name = "Number_Of_Times_Issued")
    private int numberOfTimesIssued;

    public UserBookEntity() {
    }

    public UserBookEntity(int userId, int bookId, LocalDate issuedDate, LocalDate returnDate, int numberOfTimesIssued) {
        this.userId = userId;
        this.bookId = bookId;
        this.issuedDate = issuedDate;
        this.returnDate = returnDate;
        this.numberOfTimesIssued = numberOfTimesIssued;
    }

    public int getUserBook_Id() {
        return userBook_Id;
    }

    public void setUserBook_Id(int userBook_Id) {
        this.userBook_Id = userBook_Id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getNumberOfTimesIssued() {
        return numberOfTimesIssued;
    }

    public void setNumberOfTimesIssued(int numberOfTimesIssued) {
        this.numberOfTimesIssued = numberOfTimesIssued;
    }
}
