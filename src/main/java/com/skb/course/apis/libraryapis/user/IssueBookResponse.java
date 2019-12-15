package com.skb.course.apis.libraryapis.user;

import java.util.Set;

public class IssueBookResponse {

    private Set<IssueBookStatus> issueBookStatuses;

    public IssueBookResponse() {
    }

    public IssueBookResponse(Set<IssueBookStatus> issueBookStatuses) {
        this.issueBookStatuses = issueBookStatuses;
    }

    public Set<IssueBookStatus> getIssueBookStatuses() {
        return issueBookStatuses;
    }

    public void setIssueBookStatuses(Set<IssueBookStatus> issueBookStatuses) {
        this.issueBookStatuses = issueBookStatuses;
    }
}

