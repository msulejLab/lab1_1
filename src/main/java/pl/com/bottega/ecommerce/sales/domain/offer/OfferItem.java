/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.com.bottega.ecommerce.sales.domain.offer;

import java.math.BigDecimal;
import java.util.Date;

public class OfferItem {

	// product
	private Product product;

	private int quantity;

	private BigDecimal totalCost;

	private String currency;

	private Discount discount;

	public OfferItem(Product product, int quantity) {
		this(product, quantity, new Discount());
	}

	public OfferItem(Product product, int quantity, Discount discount) {
		this.product = product;
		
		this.quantity = quantity;
		
		this.discount = discount;

		BigDecimal discountValue = new BigDecimal(0);
		
		if (discount != null)
			discountValue = discountValue.subtract(discount.getDiscount());

		this.totalCost = product.getProductPrice()
				.multiply(new BigDecimal(quantity)).subtract(discountValue);
	}


	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public String getTotalCostCurrency() {
		return currency;
	}

	public BigDecimal getDiscount() {
		return discount.getDiscount();
	}

	public String getDiscountCause() {
		return discount.getDiscountCause();
	}

	public int getQuantity() {
		return quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}	
	
	// TODO equals, hashCode

	/**
	 * 
	 * @param item
	 * @param delta
	 *            acceptable percentage difference
	 * @return
	 */
	public boolean sameAs(OfferItem other, double delta) {
		if (product.getProductName() == null) {
			if (other.getProduct().getProductName() != null)
				return false;
		} else if (!product.getProductName().equals(other.getProduct().getProductName()))
			return false;
		if (product.getProductPrice() == null) {
			if (other.getProduct().getProductPrice() != null)
				return false;
		} else if (!product.getProductPrice().equals(other.getProduct().getProductPrice()))
			return false;
		if (product.getProductId() == null) {
			if (other.getProduct().getProductId() != null)
				return false;
		} else if (!getProduct().getProductId().equals(other.getProduct().getProductId()))
			return false;
		if (product.getProductType() != other.getProduct().getProductType())
			return false;

		if (quantity != other.quantity)
			return false;

		BigDecimal max, min;
		if (totalCost.compareTo(other.totalCost) > 0) {
			max = totalCost;
			min = other.totalCost;
		} else {
			max = other.totalCost;
			min = totalCost;
		}

		BigDecimal difference = max.subtract(min);
		BigDecimal acceptableDelta = max.multiply(new BigDecimal(delta / 100));

		return acceptableDelta.compareTo(difference) > 0;
	}
}
