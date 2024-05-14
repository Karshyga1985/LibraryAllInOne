package com.library.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage{

    @FindBy (id = "user_count")
    public WebElement users;

    @FindBy (id = "book_count")
    public WebElement books;

    @FindBy (id = "borrowed_books")
    public WebElement borrowedBooks;
}
