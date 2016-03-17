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

public class OfferItem {

	private Product product;

	private int quantity;

	private Money money;

	private Discount discount;

	public OfferItem(Product product, int quantity) {
		this(product, quantity, null);
	}

	public OfferItem(Product product, int quantity, Discount discount) {
		this.product = product;
		
		this.quantity = quantity;
		
		this.discount = discount;

		this.money = new Money();
		this.money.setTotalCost(calculateTotalCost());
	}

	public BigDecimal getTotalCost() {
		return money.getTotalCost();
	}

	public String getTotalCostCurrency() {
		return money.getCurrency();
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
	
	private BigDecimal calculateTotalCost() {
		BigDecimal discountValue = new BigDecimal(0);
		
		if (discount != null) {
			discountValue = discountValue.subtract(discount.getDiscount());
		}	
		
		return product.getProductPrice().multiply(new BigDecimal(quantity)).subtract(discountValue);
	}
	
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
		if (money.getTotalCost().compareTo(other.money.getTotalCost()) > 0) {
			max = money.getTotalCost();
			min = other.money.getTotalCost();
		} else {
			max = other.money.getTotalCost();
			min = money.getTotalCost();
		}

		BigDecimal difference = max.subtract(min);
		BigDecimal acceptableDelta = max.multiply(new BigDecimal(delta / 100));

		return acceptableDelta.compareTo(difference) > 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((money.getCurrency() == null) ? 0 : money.getCurrency().hashCode());
		result = prime * result + ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((money.getTotalCost() == null) ? 0 : money.getTotalCost().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OfferItem other = (OfferItem) obj;
		if (money.getCurrency() == null) {
			if (other.money.getCurrency() != null)
				return false;
		} else if (!money.getCurrency().equals(other.money.getCurrency()))
			return false;
		if (discount == null) {
			if (other.discount != null)
				return false;
		} else if (!discount.equals(other.discount))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (quantity != other.quantity)
			return false;
		if (money.getTotalCost() == null) {
			if (other.money.getTotalCost() != null)
				return false;
		} else if (!money.getTotalCost().equals(other.money.getTotalCost()))
			return false;
		return true;
	}
}
