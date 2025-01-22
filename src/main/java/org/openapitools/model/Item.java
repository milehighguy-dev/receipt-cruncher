package org.openapitools.model;

import java.util.Objects;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.Type;


/**
 * Item
 */

@Entity
@Table(name = "items")
public class Item {

  @JsonIgnore
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  private UUID id;

  @NotNull
  private String shortDescription;

  @NotNull
  private String price;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "receipt_id", nullable = false)
  private Receipt receipt;

  public Item() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Item(String shortDescription, String price) {
    this.shortDescription = shortDescription;
    this.price = price;
  }

  public Item shortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
    return this;
  }


  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Receipt getReceipt() {
    return receipt;
  }

  public void setReceipt(Receipt receipt) {
    this.receipt = receipt;
  }

  /**
   * The Short Product Description for the item.
   * @return shortDescription
   */
  @NotNull @Pattern(regexp = "^[\\w\\s\\-]+$") 
  @Schema(name = "shortDescription", example = "Mountain Dew 12PK", description = "The Short Product Description for the item.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("shortDescription")
  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public Item price(String price) {
    this.price = price;
    return this;
  }

  /**
   * The total price payed for this item.
   * @return price
   */
  @NotNull @Pattern(regexp = "^\\d+\\.\\d{2}$") 
  @Schema(name = "price", example = "6.49", description = "The total price payed for this item.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price")
  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Item item = (Item) o;
    return Objects.equals(this.shortDescription, item.shortDescription) &&
        Objects.equals(this.price, item.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shortDescription, price);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Item {\n");
    sb.append("    shortDescription: ").append(toIndentedString(shortDescription)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

