package com.skb.course.apis.libraryapis.user;

public class IssueBookStatus {

    private Integer bookId;
    private String status;
    private String remarks;

    public IssueBookStatus() {
    }

    public IssueBookStatus(int bookId, String status, String remarks) {
        this.bookId = bookId;
        this.status = status;
        this.remarks = remarks;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
