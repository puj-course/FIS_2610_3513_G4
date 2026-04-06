package com.ceiba.fashtoll.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "products_search", createIndex = false)
@Setting(settingPath = "/elasticsearch/product-settings.json")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "spanish_analyzer", searchAnalyzer = "spanish_search_analyzer")
    private String name;

    @Field(type = FieldType.Text, analyzer = "spanish_analyzer", searchAnalyzer = "spanish_search_analyzer")
    private String description;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Keyword, index = false)
    private String brandPictureUrl;

    @Field(type = FieldType.Boolean)
    private Boolean brandIsVerified;

    @Field(type = FieldType.Keyword)
    private String productTypeName;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Keyword)
    private String generalFit;

    @Field(type = FieldType.Keyword)
    private String gender;

    @Field(type = FieldType.Keyword)
    private String color;

    @Field(type = FieldType.Boolean)
    private Boolean available;

    @Field(type = FieldType.Double)
    private Double rating;

    @Field(type = FieldType.Text, index = false)
    private String linkProduct;

    @Field(type = FieldType.Keyword, index = false)
    private List<String> imageUrls;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second_millis, DateFormat.date_optional_time, DateFormat.epoch_millis})
    private LocalDateTime createdAt;
}
