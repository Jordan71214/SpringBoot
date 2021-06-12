package com.example.demo;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class ObjectMapperTest {
    private ObjectMapper mapper = new ObjectMapper();

//  序列化時, 若欄位符合annotation, 序列化後的JSON則不存在該column
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Book {
        private String id;
        private String name;
        private int price;

        @JsonIgnore
        private String isbn;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdTime;

//      序列化時, 將該類別的屬性展開. 內部類別屬性與外部類別等價
        @JsonUnwrapped
        private Publisher publisher;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Date getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }

        public Publisher getPublisher() {
            return publisher;
        }

        public void setPublisher(Publisher publisher) {
            this.publisher = publisher;
        }
    }

    private static class Publisher {
        private String companyName;
        private String address;


//      序列化時, JSON column 的tel -> telephone
        @JsonProperty("telephone")
        private String tel;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public Publisher(String companyName, String address, String tel) {
            this.companyName = companyName;
            this.address = address;
            this.tel = tel;
        }

        public Publisher(){};
    }


    /*
        序列化 object to JSON
        object -> string -> JSONObject

        反序列化 則反之 JSON to object
        JSONObject -> String -> object

        ---------------------------------

        序列化時, class有設置getter, JSON就會有相對應的 column

        反序列化時, JOSN會根據class的setter, 決定哪些column要寫入object中

     */
    @Test
    public void testSerializeBookToJSON() throws Exception {
        Book book = new Book();
        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());

        String strBookJSON = mapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(strBookJSON);

        Assert.assertEquals(book.getId(), bookJSON.getString("id"));
        Assert.assertEquals(book.getName(), bookJSON.getString("name"));
        Assert.assertEquals(book.getPrice(), bookJSON.getInt("price"));
        Assert.assertEquals(book.getIsbn(), bookJSON.getString("isbn"));
        Assert.assertEquals(book.getCreatedTime().getTime(), bookJSON.getLong("createdTime"));
    }

    @Test
    public void testSerializeJSONToPublisher() throws Exception {
        JSONObject publisherJSON = new JSONObject();
        publisherJSON
                .put("companyName", "Taipei Company")
                .put("address", "Taipei")
                .put("tel", "02-8753-1666");

        String strPublisherJSON = publisherJSON.toString();
        Publisher publisher = mapper.readValue(strPublisherJSON, Publisher.class);

        Assert.assertEquals(publisher.getCompanyName(), publisherJSON.get("companyName"));
        Assert.assertEquals(publisher.getAddress(), publisherJSON.get("address"));
        Assert.assertEquals(publisher.getTel(), publisherJSON.get("tel"));
    }

    @Test
    public void testSerializePublisherToJSONColumnTelToTelephone() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setCompanyName("NiHaoGongSi");
        publisher.setAddress("Taipei");
        publisher.setTel("02-8753-1666");

        String strPublisher = mapper.writeValueAsString(publisher);
        JSONObject publisherJSON = new JSONObject(strPublisher);


        System.out.print(publisherJSON);

    }

    @Test
    public void testSerializeBookToJSONIgnoreIsbnColumn() throws Exception {

        Book book = new Book();
        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());
        book.setPublisher(new Publisher("NiHaoGongSi", "Taipei", "02-8753-1666"));

        String strBookJSON = mapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(strBookJSON);

        System.out.print(bookJSON);
    }

}
