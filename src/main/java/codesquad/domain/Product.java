package codesquad.domain;

import codesquad.support.MoneyFormatter;
import codesquad.support.PriceCalcultor;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.text.NumberFormat;

@Entity
@Slf4j
@NoArgsConstructor @Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 255)
    private String title;

    @Size(min = 1, max = 255)
    private String description;

    @Size(min = 1, max = 255)
    private String imgUrl;

    @DecimalMin(value = "0")
    private Long price;

    //todo categoryId
    @JsonIgnore @ToString.Exclude
    @ManyToOne(optional = false)
    private Category category;

    //todo discountRate -- int? long?
    @Column(nullable = false)
    private Long discountRate = 0L;

    @Column(nullable = false)
    private boolean deliverable = false;

    @Builder
    public Product(long id, @Size(min = 1, max = 255) String title, @Size(min = 1, max = 255) String description, @Size(min = 1, max = 255) String imgUrl, @DecimalMin(value = "0") Long price, Category category, Long discountRate, boolean deliverable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
        this.category = category;
        this.discountRate = discountRate;
        this.deliverable = deliverable;
    }
    public Long calculatePrice(int count) {
        return PriceCalcultor.calculatePrice(price, discountRate, count);
    }
    @JsonGetter("formattedPrice")
    public String getFormattedPrice(){
        log.debug("getFormattedPrice called 2 ");
        return String.valueOf(this.price);
    }
}
