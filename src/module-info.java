/**
 * 
 */
/**
 * 
 */
module FlashCard {
	requires java.sql;

	requires java.desktop;
	requires com.miglayout.swing; // Sử dụng MigLayout
	requires org.jdesktop.animation.timing;
	requires timingframework; // Sử dụng Animator
	requires java.mail;
	requires org.jsoup; // Thêm module Jsoup
	requires org.json;
	requires java.net.http; // Để sử dụng các tính năng HTTP mặc định
	requires org.apache.httpcomponents.client5.httpclient5; // Apache HttpClient
	requires org.apache.httpcomponents.core5.httpcore5; // Apache HttpCore
}